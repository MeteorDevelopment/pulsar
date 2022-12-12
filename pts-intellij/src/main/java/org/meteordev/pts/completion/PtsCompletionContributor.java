package org.meteordev.pts.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.meteordev.pts.psi.PtsProperty;
import org.meteordev.pts.psi.PtsStyle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PtsCompletionContributor extends CompletionContributor {
    public PtsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withParent(PtsStyle.class),
                new MyCompletionProvider<>(Completions.AT_STATEMENTS)
        );

        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(PsiErrorElement.class).withParent(PtsProperty.class)),
                new MyCompletionProvider<>(Completions.PROPERTIES)
        );
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        PsiFile file = context.getFile();
        PsiElement element = file.findElementAt(context.getStartOffset());
        //System.out.println(element);
    }

    private static class MyCompletionProvider<T extends CompletionParameters> extends CompletionProvider<T> {
        private final List<LookupElement> elements;

        public MyCompletionProvider(List<LookupElement> elements) {
            this.elements = elements;
        }

        @Override
        protected void addCompletions(@NotNull T parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(elements);
        }
    }
}
