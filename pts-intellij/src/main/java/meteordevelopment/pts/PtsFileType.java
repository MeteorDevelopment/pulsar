package meteordevelopment.pts;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PtsFileType extends LanguageFileType {
    public static final PtsFileType INSTANCE = new PtsFileType();

    public PtsFileType() {
        super(PtsLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "PTS File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Pulsar theme styles file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "pts";
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
