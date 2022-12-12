package org.meteordev.pts.completion;

import com.intellij.codeInsight.lookup.CharFilter;
import com.intellij.codeInsight.lookup.Lookup;
import org.meteordev.pts.PtsLanguage;
import org.jetbrains.annotations.Nullable;

public class PtsCharFilter extends CharFilter {
    @Override
    public @Nullable Result acceptChar(char c, int prefixLength, Lookup lookup) {
        if (lookup.getPsiFile().getLanguage() != PtsLanguage.INSTANCE) return null;

        if (
                Character.isLetter(c)
                || Character.isDigit(c)
                || c == '_'
                || c == '-'
                || c == '@'
        ) return Result.ADD_TO_PREFIX;

        if (c == '.' || c == ':') return Result.SELECT_ITEM_AND_FINISH_LOOKUP;

        return Result.HIDE_LOOKUP;
    }
}
