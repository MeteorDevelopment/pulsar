package org.meteordev.pts;

import com.intellij.lang.Language;

public class PtsLanguage extends Language {
    public static final PtsLanguage INSTANCE = new PtsLanguage();

    public PtsLanguage() {
        super("PTS");
    }
}
