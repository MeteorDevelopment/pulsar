// Generated from java-escape by ANTLR 4.11.1
package meteordevelopment.pts;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PtsParser}.
 */
public interface PtsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PtsParser#pts}.
	 * @param ctx the parse tree
	 */
	void enterPts(PtsParser.PtsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#pts}.
	 * @param ctx the parse tree
	 */
	void exitPts(PtsParser.PtsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PtsParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PtsParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atStatement}.
	 * @param ctx the parse tree
	 */
	void enterAtStatement(PtsParser.AtStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atStatement}.
	 * @param ctx the parse tree
	 */
	void exitAtStatement(PtsParser.AtStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atTitle}.
	 * @param ctx the parse tree
	 */
	void enterAtTitle(PtsParser.AtTitleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atTitle}.
	 * @param ctx the parse tree
	 */
	void exitAtTitle(PtsParser.AtTitleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atAuthors}.
	 * @param ctx the parse tree
	 */
	void enterAtAuthors(PtsParser.AtAuthorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atAuthors}.
	 * @param ctx the parse tree
	 */
	void exitAtAuthors(PtsParser.AtAuthorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atFont}.
	 * @param ctx the parse tree
	 */
	void enterAtFont(PtsParser.AtFontContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atFont}.
	 * @param ctx the parse tree
	 */
	void exitAtFont(PtsParser.AtFontContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atInclude}.
	 * @param ctx the parse tree
	 */
	void enterAtInclude(PtsParser.AtIncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atInclude}.
	 * @param ctx the parse tree
	 */
	void exitAtInclude(PtsParser.AtIncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atVar}.
	 * @param ctx the parse tree
	 */
	void enterAtVar(PtsParser.AtVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atVar}.
	 * @param ctx the parse tree
	 */
	void exitAtVar(PtsParser.AtVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#atMixin}.
	 * @param ctx the parse tree
	 */
	void enterAtMixin(PtsParser.AtMixinContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#atMixin}.
	 * @param ctx the parse tree
	 */
	void exitAtMixin(PtsParser.AtMixinContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#style}.
	 * @param ctx the parse tree
	 */
	void enterStyle(PtsParser.StyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#style}.
	 * @param ctx the parse tree
	 */
	void exitStyle(PtsParser.StyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(PtsParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(PtsParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#apply}.
	 * @param ctx the parse tree
	 */
	void enterApply(PtsParser.ApplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#apply}.
	 * @param ctx the parse tree
	 */
	void exitApply(PtsParser.ApplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(PtsParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(PtsParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PtsParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PtsParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(PtsParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(PtsParser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#vec2}.
	 * @param ctx the parse tree
	 */
	void enterVec2(PtsParser.Vec2Context ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#vec2}.
	 * @param ctx the parse tree
	 */
	void exitVec2(PtsParser.Vec2Context ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#vec4}.
	 * @param ctx the parse tree
	 */
	void enterVec4(PtsParser.Vec4Context ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#vec4}.
	 * @param ctx the parse tree
	 */
	void exitVec4(PtsParser.Vec4Context ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#color}.
	 * @param ctx the parse tree
	 */
	void enterColor(PtsParser.ColorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#color}.
	 * @param ctx the parse tree
	 */
	void exitColor(PtsParser.ColorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#color4}.
	 * @param ctx the parse tree
	 */
	void enterColor4(PtsParser.Color4Context ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#color4}.
	 * @param ctx the parse tree
	 */
	void exitColor4(PtsParser.Color4Context ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(PtsParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(PtsParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(PtsParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(PtsParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PtsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(PtsParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PtsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(PtsParser.IdentifierContext ctx);
}