package org.meteordev.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.meteordev.pts.PtsLexer;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class PtsFunction extends PtsPsiNode {
    public PtsFunction(@NotNull ASTNode node) {
        super(node);
    }

    public PsiElement getNameElement() {
        return getChildFromStart(0);
    }

    public Iterable<PsiElement> argumentIterator() {
        return () -> new ArgumentIterator(childIterator().iterator());
    }

    private static class ArgumentIterator implements Iterator<PsiElement> {
        private final Iterator<PsiElement> childIt;
        private PsiElement next;

        public ArgumentIterator(Iterator<PsiElement> childIt) {
            this.childIt = childIt;
            getNext();
        }

        private void getNext() {
            while (true) {
                if (!childIt.hasNext()) {
                    next = null;
                    break;
                }

                next = childIt.next();

                if (next.getNode().getElementType() instanceof TokenIElementType type) {
                    int token = type.getANTLRTokenType();
                    if (token != PtsLexer.IDENTIFIER && token != PtsLexer.OPENING_PAREN && token != PtsLexer.COMMA && token != PtsLexer.CLOSING_PAREN) break;
                }
                else break;
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public PsiElement next() {
            PsiElement arg = next;
            getNext();
            return arg;
        }
    }
}
