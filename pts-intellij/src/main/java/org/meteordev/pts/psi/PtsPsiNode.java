package org.meteordev.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class PtsPsiNode extends ANTLRPsiNode {
    public PtsPsiNode(@NotNull ASTNode node) {
        super(node);
    }

    public PsiElement getChildFromStart(int i) {
        PsiElement child = getFirstValid(getFirstChild(), true);
        if (child == null) return null;

        for (int j = 0; j < i; j++) {
            child = getFirstValid(child.getNextSibling(), true);
            if (child == null) return null;
        }

        return child;
    }

    public PsiElement getChildFromEnd(int i) {
        PsiElement child = getFirstValid(getLastChild(), false);
        if (child == null) return null;

        for (int j = 0; j < i; j++) {
            child = getFirstValid(child.getPrevSibling(), false);
            if (child == null) return null;
        }

        return child;
    }

    public Iterable<PsiElement> childIterator() {
        return () -> new ChildIterator(getFirstChild());
    }

    private static PsiElement getFirstValid(PsiElement element, boolean next) {
        if (element == null) return null;

        while (element instanceof PsiWhiteSpace || element instanceof PsiComment) {
            if (next) element = element.getNextSibling();
            else element = element.getPrevSibling();

            if (element == null) return null;
        }

        return element;
    }

    private static class ChildIterator implements Iterator<PsiElement> {
        private PsiElement next;

        private ChildIterator(PsiElement first) {
            this.next = getFirstValid(first, true);
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public PsiElement next() {
            PsiElement child = next;
            next = getFirstValid(next.getNextSibling(), true);
            return child;
        }
    }
}
