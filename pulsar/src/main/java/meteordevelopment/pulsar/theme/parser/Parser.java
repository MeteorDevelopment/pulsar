package meteordevelopment.pulsar.theme.parser;

import meteordevelopment.pts.PtsBaseListener;
import meteordevelopment.pts.PtsLexer;
import meteordevelopment.pts.PtsParser;
import meteordevelopment.pulsar.rendering.FontInfo;
import meteordevelopment.pulsar.theme.Style;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.fileresolvers.IFileResolver;
import meteordevelopment.pulsar.theme.properties.*;
import meteordevelopment.pulsar.utils.ColorFactory;
import meteordevelopment.pulsar.utils.Utils;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private record Variable(PropertyType<?> type, Object value) {}

    public static Theme parse(IFileResolver fileResolver, String path) {
        ParserRuleContext ast = parseFile(fileResolver, path);
        processIncludes(fileResolver, path, ast);

        Theme theme = new Theme();
        theme.setFileResolver(fileResolver);

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

        private PropertyType<?> variableType;
        private String variableName;

        private Style style;
        private Property<Object> property;
        private String accessorName;

        private final List<ValueType> valueTypes = new ArrayList<>();
        private final List<Object> values = new ArrayList<>();

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
            PropertyType<?> type = PropertyTypes.get(string(ctx.type));
            if (type == null) throw new ParseException("%s [%d:%d] Unknown property type with name '%s'.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine(), string(ctx.type));

            variableType = type;
            variableName = string(ctx.name);
        }

        @Override
        public void exitAtVar(PtsParser.AtVarContext ctx) {
            PropertyConstructor<?> constructor = variableType.getConstructor(valueTypes);
            if (constructor == null) throw new ParseException("%s [%d:%d] Invalid values.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine());

            Object value = constructor.create(values);
            if (value == null) throw new ParseException("%s [%d:%d] Invalid value.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine());

            variables.put(variableName, new Variable(variableType, value));

            variableType = null;
            variableName = null;
            valueTypes.clear();
            values.clear();
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
            if (ctx.accessor != null) this.accessorName = string(ctx.accessor);
        }

        @Override
        public void exitProperty(PtsParser.PropertyContext ctx) {
            if (accessorName == null) {
                PropertyConstructor<?> constructor = property.type().getConstructor(valueTypes);
                if (constructor == null) throw new ParseException("%s [%d:%d] Invalid values.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine());

                Object value = constructor.create(values);
                if (value == null) throw new ParseException("%s [%d:%d] Invalid value.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine());

                style.set(property, value);
            }
            else {
                PropertyAccessor<Object> accessor = property.type().getAccessor(accessorName, valueTypes);
                if (accessor == null) throw new ParseException("%s [%d:%d] Invalid values.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine());

                Object target = style.getRaw(property);
                if (target == null) {
                    target = property.type().accessorDefaultValue.get();
                    style.set(property, target);
                }

                accessor.set(target, values);
            }

            property = null;
            accessorName = null;
            valueTypes.clear();
            values.clear();
        }

        @Override
        public void enterUnit(PtsParser.UnitContext ctx) {
            String text = string(ctx);
            double number = Double.parseDouble(text.substring(0, text.length() - 2));

            valueTypes.add(ValueType.Unit);
            values.add(number);
        }

        @Override
        public void enterColor(PtsParser.ColorContext ctx) {
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

            valueTypes.add(ValueType.Color);
            values.add(ColorFactory.create(r, g, b, a));
        }

        @Override
        public void enterIdentifier(PtsParser.IdentifierContext ctx) {
            valueTypes.add(ValueType.Identifier);
            values.add(string(ctx));
        }

        @Override
        public void enterString(PtsParser.StringContext ctx) {
            valueTypes.add(ValueType.String);
            values.add(string(ctx));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void enterVariable(PtsParser.VariableContext ctx) {
            Variable variable = variables.get(string(ctx.name));
            if (variable == null) throw new ParseException("%s [%d:%d] Unknown variable '%s'.", fileResolver.resolvePath(currentPath), ctx.name.getLine(), ctx.name.getCharPositionInLine(), string(ctx.name));

            ((PropertyType<Object>) variable.type).decompose(variable.value, valueTypes, values);
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
