package org.meteordev.pts;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.meteordev.pts.psi.*;

public class PtsParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(PtsLanguage.INSTANCE);

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(PtsLanguage.INSTANCE, PtsParser.tokenNames, PtsParser.ruleNames);
    }

    public static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(PtsLanguage.INSTANCE, PtsLexer.COMMENT, PtsLexer.LINE_COMMENT);
    public static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(PtsLanguage.INSTANCE, PtsLexer.WS);
    public static final TokenSet STRING = PSIElementTypeFactory.createTokenSet(PtsLanguage.INSTANCE, PtsLexer.STRING);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        PtsLexer lexer = new PtsLexer(null);
        return new ANTLRLexerAdaptor(PtsLanguage.INSTANCE, lexer);
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        PtsParser parser = new PtsParser(null);
        return new ANTLRParserAdaptor(PtsLanguage.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                return ((PtsParser) parser).pts();
            }
        };
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return STRING;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();

        if (type instanceof TokenIElementType) return new ANTLRPsiNode(node);
        if (!(type instanceof RuleIElementType rule)) return new ANTLRPsiNode(node);

        return switch (rule.getRuleIndex()) {
            case PtsParser.RULE_atStatement -> new PtsAtStatement(node, false);
            case PtsParser.RULE_atVar       -> new PtsAtVar(node);
            case PtsParser.RULE_style       -> new PtsStyle(node);
            case PtsParser.RULE_apply       -> new PtsAtStatement(node, true);
            case PtsParser.RULE_property    -> new PtsProperty(node);
            case PtsParser.RULE_function    -> new PtsFunction(node);
            default                         -> new PtsPsiNode(node);
        };
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new PtsPSIFileRoot(viewProvider);
    }
}
