package meteordevelopment.pulsar.theme.parser;

import meteordevelopment.pts.PtsBaseListener;
import meteordevelopment.pts.PtsLexer;
import meteordevelopment.pts.PtsParser;
import meteordevelopment.pulsar.rendering.FontInfo;
import meteordevelopment.pulsar.theme.*;
import meteordevelopment.pulsar.theme.fileresolvers.IFileResolver;
import meteordevelopment.pulsar.utils.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private record Variable(PropertyType type, Object value) {}

    public static Theme parse(IFileResolver fileResolver, String path) {
        ParserRuleContext ast = parseFile(fileResolver, path);
        processIncludes(fileResolver, path, ast);

        Theme theme = new Theme();
        processAst(fileResolver, path, ast, theme);

        return theme;
    }

    private static void processIncludes(IFileResolver fileResolver, String currentPath, ParserRuleContext ast) {
        ParseTreeWalker.DEFAULT.walk(new PtsBaseListener() {
            @Override
            public void enterAtInclude(PtsParser.AtIncludeContext ctx) {
                String path = string(ctx.include);
                if (!path.endsWith(".pts")) throw new ParseException("%s [%d:%d] File must end with 'pts' extension.", fileResolver.resolvePath(currentPath), ctx.include.getLine(), ctx.include.getCharPositionInLine());

                ParserRuleContext tree = parseFile(fileResolver, path);

                for (ParseTree child : tree.children) {
                    child.setParent(ast);
                    ast.addAnyChild(child);
                }
            }
        }, ast);
    }

    private static void processAst(IFileResolver fileResolver, String currentPath, ParserRuleContext ast, Theme theme) {
        Processor processor = new Processor(fileResolver, currentPath, theme);

        // Process variables and mixins
        ParseTreeWalker.DEFAULT.walk(new PtsBaseListener() {
            private boolean inVariableOrMixin = false;

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                if (ctx instanceof PtsParser.AtVarContext || ctx instanceof PtsParser.AtMixinContext) inVariableOrMixin = true;

                if (inVariableOrMixin) ctx.enterRule(processor);
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                if (inVariableOrMixin) ctx.exitRule(processor);

                if (ctx instanceof PtsParser.AtVarContext || ctx instanceof PtsParser.AtMixinContext) inVariableOrMixin = false;
            }
        }, ast);

        // Process rest
        ParseTreeWalker.DEFAULT.walk(new PtsBaseListener() {
            private boolean inVariableOrMixin = false;

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                if (ctx instanceof PtsParser.AtVarContext || ctx instanceof PtsParser.AtMixinContext) inVariableOrMixin = true;

                if (!inVariableOrMixin) ctx.enterRule(processor);
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                if (!inVariableOrMixin) ctx.exitRule(processor);

                if (ctx instanceof PtsParser.AtVarContext || ctx instanceof PtsParser.AtMixinContext) inVariableOrMixin = false;
            }
        }, ast);
    }

    private static class Processor extends PtsBaseListener {
        private final IFileResolver fileResolver;
        private final String currentPath;
        private final Theme theme;

        private final Map<String, Variable> variables = new HashMap<>();
        private final Map<String, Style> mixins = new HashMap<>();

        private String variableName;
        private PropertyType variableType;

        private Style style;
        private Property<Object> property;

        public Processor(IFileResolver fileResolver, String currentPath, Theme theme) {
            this.fileResolver = fileResolver;
            this.currentPath = currentPath;
            this.theme = theme;
        }

        @Override
        public void enterAtTitle(PtsParser.AtTitleContext ctx) {
            theme.title = string(ctx.title);
        }

        @Override
        public void enterAtAuthors(PtsParser.AtAuthorsContext ctx) {
            theme.authors = new ArrayList<>(ctx.authors.size());

            for (Token author : ctx.authors) {
                theme.authors.add(string(author));
            }
        }

        @Override
        public void enterAtFont(PtsParser.AtFontContext ctx) {
            String path = string(ctx.font);
            if (!path.endsWith(".ttf")) throw new ParseException("%s [%d:%d] File must end with 'pts' extension.", fileResolver.resolvePath(currentPath), ctx.font.getLine(), ctx.font.getCharPositionInLine());

            InputStream in = fileResolver.get(path);
            if (in == null) throw new ParseException("Failed to read file '%s'.", fileResolver.resolvePath(path));

            theme.setFontInfo(new FontInfo(in));
        }

        @Override
        public void enterAtVar(PtsParser.AtVarContext ctx) {
            PropertyType type;
            try {
                type = PropertyType.valueOf(string(ctx.type));
            }
            catch (IllegalArgumentException ignored) {
                throw new ParseException("%s [%d:%d] Unknown variable type '%s'.", fileResolver.resolvePath(currentPath), ctx.type.getLine(), ctx.type.getCharPositionInLine(), string(ctx.type));
            }

            variableName = string(ctx.name);
            variableType = type;
        }

        @Override
        public void exitAtVar(PtsParser.AtVarContext ctx) {
            variableName = null;
            variableType = null;
        }

        @Override
        public void enterAtMixin(PtsParser.AtMixinContext ctx) {
            style = new Style();
            style.name = string(ctx.name);
        }

        @Override
        public void exitAtMixin(PtsParser.AtMixinContext ctx) {
            mixins.put(style.name, style);
            style = null;
        }

        @Override
        public void enterStyle(PtsParser.StyleContext ctx) {
            style = new Style();

            if (ctx.name != null) style.name = string(ctx.name);
            if (ctx.tag != null) style.tags = List.of(string(ctx.tag));
            if (ctx.state != null) style.state = Style.State.of(string(ctx.state));
        }

        @Override
        public void exitStyle(PtsParser.StyleContext ctx) {
            theme.addStyle(style);
            style = null;
        }

        @Override
        public void enterApply(PtsParser.ApplyContext ctx) {
            String name = string(ctx.name);

            Style mixin = mixins.get(name);
            if (mixin == null) throw new ParseException("%s [%d:%d] Unknown mixin with name '%s'.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine(), name);

            style.merge(mixin);
        }

        @Override
        public void enterProperty(PtsParser.PropertyContext ctx) {
            Property<?> property = Properties.get(string(ctx.name));
            if (property == null) throw new ParseException("%s [%d:%d] Unknown property with name '%s'.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine(), string(ctx.name));

            //noinspection unchecked
            this.property = (Property<Object>) property;
        }

        @Override
        public void exitProperty(PtsParser.PropertyContext ctx) {
            property = null;
        }

        @Override
        public void enterUnit(PtsParser.UnitContext ctx) {
            // TODO: Handle this better
            if (ctx.parent instanceof PtsParser.Vec2Context || ctx.parent instanceof PtsParser.Vec4Context) return;

            String text = string(ctx);
            double number = Double.parseDouble(text.substring(0, text.length() - 2));

            if (variableName != null) {
                switch (variableType) {
                    case Number -> variables.put(variableName, new Variable(variableType, number));
                    case Vec2 ->   variables.put(variableName, new Variable(variableType, new Vec2(number)));
                    case Vec4 ->   variables.put(variableName, new Variable(variableType, new Vec4(number)));
                    default ->     throw new ParseException("%s [%d:%d] Cannot assign Unit to a %s variable.", fileResolver.resolvePath(currentPath), ctx.start.getLine(), ctx.start.getCharPositionInLine(), variableType);
                }
            }
            else {
                switch (property.type()) {
                    case Number -> style.set(property, number);
                    case Vec2 ->   style.set(property, new Vec2(number));
                    case Vec4 ->   style.set(property, new Vec4(number));
                    default ->     throw new ParseException("%s [%d:%d] Cannot assign Unit to a %s property.", fileResolver.resolvePath(currentPath), ctx.start.getLine(), ctx.start.getCharPositionInLine(), property.type());
                }
            }
        }

        @Override
        public void enterVec2(PtsParser.Vec2Context ctx) {
            if (type() != PropertyType.Vec2) typeError("Vec2", ctx.start);

            String textX = string(ctx.x);
            double x = Double.parseDouble(textX.substring(0, textX.length() - 2));

            String textY = string(ctx.y);
            double y = Double.parseDouble(textY.substring(0, textY.length() - 2));

            if (variableName != null) variables.put(variableName, new Variable(variableType, new Vec2(x, y)));
            else style.set(property, new Vec2(x, y));
        }

        @Override
        public void enterVec4(PtsParser.Vec4Context ctx) {
            if (type() != PropertyType.Vec4) typeError("Vec4", ctx.start);

            String textX = string(ctx.x);
            double x = Double.parseDouble(textX.substring(0, textX.length() - 2));

            String textY = string(ctx.y);
            double y = Double.parseDouble(textY.substring(0, textY.length() - 2));

            String textZ = string(ctx.z);
            double z = Double.parseDouble(textZ.substring(0, textZ.length() - 2));

            String textW = string(ctx.w);
            double w = Double.parseDouble(textW.substring(0, textW.length() - 2));

            if (variableName != null) variables.put(variableName, new Variable(variableType, new Vec4(x, y, z, w)));
            else style.set(property, new Vec4(x, y, z, w));
        }

        @Override
        public void enterColor(PtsParser.ColorContext ctx) {
            // TODO: Handle this better
            if (ctx.parent instanceof PtsParser.Color4Context) return;

            if (type() != PropertyType.Color && type() != PropertyType.Color4) typeError("Color", ctx.start);

            Object value = parseColor(ctx);
            if (type() == PropertyType.Color4) value = new Color4((IColor) value);

            if (variableName != null) variables.put(variableName, new Variable(variableType, value));
            else style.set(property, value);
        }

        @Override
        public void enterColor4(PtsParser.Color4Context ctx) {
            if (type() != PropertyType.Color4) typeError("Color4", ctx.start);

            Color4 value = new Color4(
                    parseColor(ctx.color(0)),
                    parseColor(ctx.color(1)),
                    parseColor(ctx.color(2)),
                    parseColor(ctx.color(3))
            );

            if (variableName != null) variables.put(variableName, new Variable(variableType, value));
            else style.set(property, value);
        }

        private IColor parseColor(PtsParser.ColorContext ctx) {
            int r;
            int g;
            int b;
            int a = 255;

            if (ctx.HEX_COLOR() != null) {
                String text = string(ctx.HEX_COLOR()).substring(1);
                if (text.length() != 6 && text.length() != 8) throw new ParseException("%s [%d:%d] Invalid hex color.", fileResolver.resolvePath(currentPath), ctx.start.getLine(), ctx.start.getCharPositionInLine());

                r = Integer.parseInt(text.substring(0, 2), 16);
                g = Integer.parseInt(text.substring(2, 4), 16);
                b = Integer.parseInt(text.substring(4, 6), 16);

                if (text.length() == 8) a = Integer.parseInt(text.substring(6, 8), 16);
            }
            else {
                String text = string(ctx.RGB_COLOR());

                text = text.substring(text.indexOf('(') + 1);
                text = text.substring(0, text.indexOf(')'));

                String[] values = text.split(",");

                r = Integer.parseInt(values[0].trim());
                g = Integer.parseInt(values[1].trim());
                b = Integer.parseInt(values[2].trim());

                if (values.length > 3) a = Integer.parseInt(values[3].trim());
            }

            r = Utils.clamp(r, 0, 255);
            g = Utils.clamp(g, 0, 255);
            b = Utils.clamp(b, 0, 255);
            a = Utils.clamp(a, 0, 255);

            return ColorFactory.create(r, g, b, a);
        }

        @Override
        public void enterString(PtsParser.StringContext ctx) {
            if (type() != PropertyType.File) typeError("String", ctx.start);

            String path = string(ctx);
            InputStream in = fileResolver.get(path);
            if (in == null) throw new ParseException("%s [%d:%d] Failed to read file '%s'.", fileResolver.resolvePath(currentPath), ctx.start.getLine(), ctx.start.getCharPositionInLine(), path);

            if (theme.getBuffer(path) == null) {
                byte[] bytes = Utils.read(in);
                ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);

                theme.putBuffer(path, buffer);
            }
            else {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (variableName != null) variables.put(variableName, new Variable(variableType, path));
            else style.set(property, path);
        }

        @Override
        public void enterVariable(PtsParser.VariableContext ctx) {
            Variable variable = variables.get(string(ctx.name));
            if (variable == null) throw new ParseException("%s [%d:%d] Unknown variable '%s'.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine(), string(ctx.name));

            Object value = variable.value;
            if (type() == PropertyType.Color4 && variable.type == PropertyType.Color) value = new Color4((IColor) value);
            else if (type() != variable.type()) typeError(variable.type().toString(), ctx.name);

            if (variableName != null) variables.put(variableName, new Variable(variableType, value));
            else style.set(property, value);
        }

        @Override
        public void enterIdentifier(PtsParser.IdentifierContext ctx) {
            if (type() != PropertyType.Identifier && type() != PropertyType.Enum) typeError("Identifier", ctx.start);

            String text = string(ctx);
            Object value = null;

            if (type() == PropertyType.Identifier) value = text;
            else {
                Class<?> klass = property.defaultValue().getClass();

                try {
                    Method valuesMethod = klass.getDeclaredMethod("values");
                    Enum<?>[] values = (Enum<?>[]) valuesMethod.invoke(null);

                    for (Enum<?> value2 : values) {
                        if (value2.name().equalsIgnoreCase(text)) {
                            value = value2;
                            break;
                        }
                    }

                    if (value == null) throw new ParseException("%s [%d:%d] Unknown value.", fileResolver.resolvePath(currentPath), ctx.start.getLine(), ctx.start.getCharPositionInLine());
                }
                catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            if (variableName != null) variables.put(variableName, new Variable(variableType, value));
            else style.set(property, value);
        }

        private PropertyType type() {
            return variableName != null ? variableType : property.type();
        }

        private void typeError(String current, Token token) {
            throw new ParseException("%s [%d:%d] Cannot assign %s to a %s %s.", fileResolver.resolvePath(currentPath), token.getLine(), token.getCharPositionInLine(), current, type(), variableName != null ? "variable" : "property");
        }
    }

    private static String string(Token token) {
        String text = token.getText();
        return text.startsWith("\"") ? text.substring(1, text.length() - 1) : text;
    }

    private static String string(ParseTree node) {
        String text = node.getText();
        return text.startsWith("\"") ? text.substring(1, text.length() - 1) : text;
    }

    private static ParserRuleContext parseFile(IFileResolver fileResolver, String path) {
        InputStream in = fileResolver.get(path);
        if (in == null) throw new ParseException("Failed to read file '%s'.", fileResolver.resolvePath(path));

        PtsLexer lexer;

        try {
            lexer = new PtsLexer(CharStreams.fromStream(in));
        }
        catch (IOException e) {
            throw new ParseException("Failed to create lexer.");
        }

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PtsParser parser = new PtsParser(tokens);
        parser.getSourceName();
        parser.setErrorHandler(new BailErrorStrategy());

        try {
            return parser.pts();
        }
        catch (ParseCancellationException ex) {
            if (ex.getCause() instanceof InputMismatchException e) {
                Token token = e.getOffendingToken();
                throw new ParseException("%s [%d:%d] Mismatched input %s expecting %s.", fileResolver.resolvePath(path), token.getLine(), token.getCharPositionInLine(), getTokenErrorDisplay(token), e.getExpectedTokens().toString(parser.getVocabulary()));
            }
            else if (ex.getCause() instanceof NoViableAltException e) {
                Token token = e.getOffendingToken();
                String input;

                if (e.getStartToken().getType() == Token.EOF) input = "<EOF>";
                else input = tokens.getText(e.getStartToken(), token);

                throw new ParseException("%s [%d:%d] No viable alternative at input %s.", fileResolver.resolvePath(path), token.getLine(), token.getCharPositionInLine(), escapeWSAndQuote(input));
            }
            else {
                throw new ParseException("Failed to parse file.");
            }
        }
    }

    private static String getTokenErrorDisplay(Token t) {
        if (t == null) return "<no token>";
        String s = t.getText();

        if (s == null) {
            if (t.getType() == Token.EOF) s = "<EOF>";
            else s = "<" + t.getType() + ">";
        }

        return escapeWSAndQuote(s);
    }

    private static String escapeWSAndQuote(String s) {
//		if ( s==null ) return s;

        s = s.replace("\n","\\n");
        s = s.replace("\r","\\r");
        s = s.replace("\t","\\t");

        return "'" + s + "'";
    }
}
