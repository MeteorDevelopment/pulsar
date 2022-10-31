package meteordevelopment.pts.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.util.PlatformIcons;
import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pts.properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Completions {
    public static final List<LookupElement> AT_STATEMENTS = Arrays.asList(
            createAt("title", AtAfter.String),
            createAt("authors", AtAfter.StringArray),
            createAt("include", AtAfter.String),
            createAt("var", AtAfter.Identifier),
            createAt("mixin", AtAfter.Identifier)
    );

    public static final List<LookupElement> PROPERTIES = new ArrayList<>();

    static {
        for (Property<?> property : Properties.getAll()) PROPERTIES.add(createProperty(property));
    }

    private static LookupElement createAt(String name, AtAfter after) {
        return LookupElementBuilder
                .create("@" + name)
                .withIcon(PlatformIcons.FIELD_ICON)
                .withTypeText(after.toString())
                .withInsertHandler((context, item) -> {
                    switch (after) {
                        case String -> insert(context, " \"\";", 2);
                        case StringArray -> insert(context, " [ \"\" ];", 4);
                        case Identifier -> insert(context, " ", 1);
                    }
                });
    }

    private static LookupElement createProperty(Property<?> property) {
        return LookupElementBuilder
                .create(property.name())
                .withIcon(PlatformIcons.PROPERTY_ICON)
                .withTypeText(space(property.type().name))
                .withInsertHandler((context, item) -> insert(context, ": ;", 2));
    }

    private static String space(String string) {
        return string.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
    }

    private static void insert(InsertionContext context, String string, int caretOffset) {
        CaretModel caret = context.getEditor().getCaretModel();

        context.getDocument().insertString(caret.getOffset(), string);
        caret.moveToOffset(caret.getOffset() + caretOffset);
    }

    private enum AtAfter {
        String,
        StringArray,
        Identifier;

        @Override
        public java.lang.String toString() {
            return this == StringArray ? "String[]" : super.toString();
        }
    }
}
