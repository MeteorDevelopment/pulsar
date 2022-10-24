// Generated from java-escape by ANTLR 4.11.1
package meteordevelopment.pts;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PtsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PtsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PtsParser#pts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPts(PtsParser.PtsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PtsParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtStatement(PtsParser.AtStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atTitle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtTitle(PtsParser.AtTitleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atAuthors}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtAuthors(PtsParser.AtAuthorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atFont}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtFont(PtsParser.AtFontContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atInclude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtInclude(PtsParser.AtIncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtVar(PtsParser.AtVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#atMixin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtMixin(PtsParser.AtMixinContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#style}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyle(PtsParser.StyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(PtsParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#apply}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApply(PtsParser.ApplyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(PtsParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PtsParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(PtsParser.UnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#vec2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVec2(PtsParser.Vec2Context ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#vec4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVec4(PtsParser.Vec4Context ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#color}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColor(PtsParser.ColorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#color4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColor4(PtsParser.Color4Context ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(PtsParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(PtsParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PtsParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(PtsParser.IdentifierContext ctx);
}