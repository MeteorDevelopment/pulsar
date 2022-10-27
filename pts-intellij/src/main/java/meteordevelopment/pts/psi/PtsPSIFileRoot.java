package meteordevelopment.pts.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import meteordevelopment.pts.PtsFileType;
import meteordevelopment.pts.PtsLanguage;
import org.jetbrains.annotations.NotNull;

public class PtsPSIFileRoot extends PsiFileBase {
    public PtsPSIFileRoot(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, PtsLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return PtsFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "PTS File";
    }
}
