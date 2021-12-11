package meteordevelopment.pulsar.utils;

public class CharFilters {
    public static final ICharFilter ALL = (text, c, cursor) -> true;

    public static final ICharFilter INTEGER = (text, c, cursor) -> {
        if (c == '-') return text.isEmpty() || (cursor == 0 && !text.contains("-"));
        return c >= '0' && c <= '9';
    };

    public static final ICharFilter DOUBLE = (text, c, cursor) -> {
        if (c == '-') return text.isEmpty() || (cursor == 0 && !text.contains("-"));
        if (c == '.') return !text.contains(".") && (!text.contains("-") || cursor > 0);
        return c >= '0' && c <= '9';
    };
}
