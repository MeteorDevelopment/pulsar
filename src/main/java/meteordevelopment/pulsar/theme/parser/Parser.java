package meteordevelopment.pulsar.theme.parser;

import meteordevelopment.pulsar.rendering.FontInfo;
import meteordevelopment.pulsar.theme.*;
import meteordevelopment.pulsar.theme.fileresolvers.IFileResolver;
import meteordevelopment.pulsar.utils.*;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

public class Parser {
    private final IFileResolver fileResolver;
    private final Scanner scanner;
    private final Theme theme;

    private Token next, current, previous;

    private Parser(IFileResolver fileResolver, String path) {
        InputStream in = fileResolver.get(path);
        if (in == null) throw error(null, "Failed to read file '" + fileResolver.resolvePath(path) + "'.");

        this.fileResolver = fileResolver;
        this.scanner = new Scanner(new InputStreamReader(in));
        this.theme = new Theme("test", List.of("hi"));

        advance();
        advance();
    }

    public static Theme parse(IFileResolver fileResolver, String path) {
        Parser parser = new Parser(fileResolver, path);

        while (!parser.isAtEnd()) {
            parser.declaration();
        }

        if (parser.theme.getFontInfo() == null) throw parser.error(null, "No font specified.");

        return parser.theme;
    }

    // Declarations

    private void declaration() {
        // At declarations
        if (match(TokenType.At)) {
            Token token = consume(TokenType.Identifier, "Expected at declaration name.");

            switch (token.lexeme()) {
                case "font" -> font();
                default -> throw error(token, "Unknown at declaration.");
            }
        }

        // Style
        Style style = new Style();

        //     Name
        if (match(TokenType.Identifier)) {
            style.name = previous().lexeme();

            // Tag
            if (match(TokenType.Dot)) {
                Token tag = consume(TokenType.Identifier, "Expected tag.");
                style.tags = List.of(tag.lexeme());

                // State
                if (match(TokenType.Colon)) {
                    Token state = consume(TokenType.Identifier, "Expected state");
                    style.state = Style.State.of(state.lexeme());
                }
            }
            // State
            else if (match(TokenType.Colon)) {
                Token state = consume(TokenType.Identifier, "Expected state");
                style.state = Style.State.of(state.lexeme());
            }
        }
        //     Tag
        else if (match(TokenType.Dot)) {
            Token tag = consume(TokenType.Identifier, "Expected tag.");
            style.tags = List.of(tag.lexeme());

            // State
            if (match(TokenType.Colon)) {
                Token state = consume(TokenType.Identifier, "Expected state");
                style.state = Style.State.of(state.lexeme());
            }
        }

        if (style.name == null && style.tags == null && style.state == null) throw error(peek(), "Expected style selector.");

        style(style);
        theme.addStyle(style);
    }

    private void font() {
        Token font = consume(TokenType.String, "Expected font path.");
        String path = font.lexeme().substring(1, font.lexeme().length() - 1);

        if (!path.endsWith(".ttf")) throw error(font, "Fonts must be in ttf file format.");

        InputStream in = fileResolver.get(path);
        if (in == null) throw error(font, "Failed to read file '" + fileResolver.resolvePath(path) + "'.");

        theme.setFontInfo(new FontInfo(in));
    }

    // Styles

    private void style(Style style) {
        consume(TokenType.LeftBrace, "Expected '{' before style body.");

        while (!check(TokenType.RightBrace)) property(style);

        consume(TokenType.RightBrace, "Expected '' after style body.");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void property(Style style) {
        Token name = consume(TokenType.Identifier, "Expected property name.");

        Property property = Properties.get(name.lexeme());
        if (property == null) throw error(name, "Unknown property with name '" + name.lexeme() + "'.");

        consume(TokenType.Colon, "Expected ':' before property value.");

        switch (property.type()) {
            case Number -> style.set(property, number());
            case Vec2 -> style.set(property, vec2());
            case Vec4 -> style.set(property, vec4());
            case Identifier -> style.set(property, identifier());
            case File -> style.set(property, file());
            case Color -> style.set(property, color());
            case Color4 -> style.set(property, color4());
            case Enum -> style.set(property, enum_(property.defaultValue().getClass()));
        }
    }

    private double number() {
        return Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private Vec2 vec2() {
        double x = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());

        if (match(TokenType.Number)) {
            double y = Double.parseDouble(previous().lexeme());
            return new Vec2(x, y);
        }

        return new Vec2(x, x);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private Vec4 vec4() {
        double x = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());

        if (match(TokenType.Number)) {
            double y = Double.parseDouble(previous().lexeme());
            double z = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());
            double w = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());

            return new Vec4(x, y, z, w);
        }

        return new Vec4(x, x, x, x);
    }

    private String identifier() {
        return consume(TokenType.Identifier, "Expected identifier.").lexeme();
    }

    private String file() {
        Token string = consume(TokenType.String, "Expected string.");
        String path = string.lexeme().substring(1, string.lexeme().length() - 1);

        InputStream in = fileResolver.get(path);
        if (in == null) throw error(string, "Failed to read file '" + fileResolver.resolvePath(path) + "'.");

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

        return path;
    }

    private IColor color() {
        // Hex
        if (match(TokenType.Hex)) {
            // TODO: This can probably be made better
            String str = previous().lexeme().substring(1);
            if (str.length() != 6 && str.length() != 8) throw error(previous(), "Invalid hex color.");

            int r = Integer.parseInt(str.substring(0, 2), 16);
            int g = Integer.parseInt(str.substring(2, 4), 16);
            int b = Integer.parseInt(str.substring(4, 6), 16);

            int a = 255;
            if (str.length() == 8) a = Integer.parseInt(str.substring(6, 8), 16);

            return ColorFactory.create(r, g, b, a);
        }

        // RGBA
        double r = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());
        double g = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());
        double b = Double.parseDouble(consume(TokenType.Number, "Expected number.").lexeme());

        double a = 255;
        if (match(TokenType.Number)) a = Double.parseDouble(previous().lexeme());

        return ColorFactory.create((int) Math.floor(r), (int) Math.floor(g), (int) Math.floor(b), (int) Math.floor(a));
    }

    private Color4 color4() {
        IColor c1 = color();

        if (match(TokenType.Comma)) {
            IColor c2 = color();
            consume(TokenType.Comma, "Expected ',' after second color value.");

            IColor c3 = color();
            consume(TokenType.Comma, "Expected ',' after third color value.");

            IColor c4 = color();

            return new Color4(c1, c2, c3, c4);
        }

        return new Color4(c1);
    }

    private Object enum_(Class<?> klass) {
        try {
            Token name = consume(TokenType.Identifier, "Expected enum name.");

            Method valuesMethod = klass.getDeclaredMethod("values");
            Enum<?>[] values = (Enum<?>[]) valuesMethod.invoke(null);

            for (Enum<?> value : values) {
                if (value.name().equalsIgnoreCase(name.lexeme())) return value;
            }

            throw error(name, "Unknown value.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utils

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            previous = current;
            current = next;
            next = scanner.next();
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek() != null && peek().type() == TokenType.Eof;
    }

    private Token peek() {
        return current;
    }

    private Token peekNext() {
        return next;
    }

    private Token previous() {
        return previous;
    }

    private ParseException error(Token token, String message) {
        return new ParseException(token, message);
    }
}
