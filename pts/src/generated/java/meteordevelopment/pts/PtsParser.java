// Generated from java-escape by ANTLR 4.11.1
package meteordevelopment.pts;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PtsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, NUMBER=11, STRING=12, HEX_COLOR=13, RGB_COLOR=14, PX=15, IDENTIFIER=16, 
		OPENING_BRACE=17, CLOSING_BRACE=18, OPENING_BRACKET=19, CLOSING_BRACKET=20, 
		COMMA=21, SEMICOLON=22, BANG=23, COMMENT=24, LINE_COMMENT=25, WS=26, UNKNOWN=27;
	public static final int
		RULE_pts = 0, RULE_statement = 1, RULE_atStatement = 2, RULE_atTitle = 3, 
		RULE_atAuthors = 4, RULE_atFont = 5, RULE_atInclude = 6, RULE_atVar = 7, 
		RULE_atMixin = 8, RULE_style = 9, RULE_declaration = 10, RULE_apply = 11, 
		RULE_property = 12, RULE_expression = 13, RULE_unit = 14, RULE_vec2 = 15, 
		RULE_vec4 = 16, RULE_color = 17, RULE_color4 = 18, RULE_string = 19, RULE_variable = 20, 
		RULE_identifier = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"pts", "statement", "atStatement", "atTitle", "atAuthors", "atFont", 
			"atInclude", "atVar", "atMixin", "style", "declaration", "apply", "property", 
			"expression", "unit", "vec2", "vec4", "color", "color4", "string", "variable", 
			"identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'@title'", "'@authors'", "'@font'", "'@include'", "'@var'", "':'", 
			"'='", "'@mixin'", "'.'", "'@apply'", null, null, null, null, "'px'", 
			null, "'{'", "'}'", "'['", "']'", "','", "';'", "'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "NUMBER", 
			"STRING", "HEX_COLOR", "RGB_COLOR", "PX", "IDENTIFIER", "OPENING_BRACE", 
			"CLOSING_BRACE", "OPENING_BRACKET", "CLOSING_BRACKET", "COMMA", "SEMICOLON", 
			"BANG", "COMMENT", "LINE_COMMENT", "WS", "UNKNOWN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PtsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PtsContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PtsParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public PtsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterPts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitPts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitPts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PtsContext pts() throws RecognitionException {
		PtsContext _localctx = new PtsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pts);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 66430L) != 0) {
				{
				{
				setState(44);
				statement();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public AtStatementContext atStatement() {
			return getRuleContext(AtStatementContext.class,0);
		}
		public StyleContext style() {
			return getRuleContext(StyleContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(54);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
			case T__2:
			case T__3:
			case T__4:
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				atStatement();
				}
				break;
			case T__5:
			case T__8:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				style();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtStatementContext extends ParserRuleContext {
		public AtTitleContext atTitle() {
			return getRuleContext(AtTitleContext.class,0);
		}
		public AtAuthorsContext atAuthors() {
			return getRuleContext(AtAuthorsContext.class,0);
		}
		public AtFontContext atFont() {
			return getRuleContext(AtFontContext.class,0);
		}
		public AtIncludeContext atInclude() {
			return getRuleContext(AtIncludeContext.class,0);
		}
		public AtVarContext atVar() {
			return getRuleContext(AtVarContext.class,0);
		}
		public AtMixinContext atMixin() {
			return getRuleContext(AtMixinContext.class,0);
		}
		public AtStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtStatementContext atStatement() throws RecognitionException {
		AtStatementContext _localctx = new AtStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_atStatement);
		try {
			setState(62);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				atTitle();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				atAuthors();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(58);
				atFont();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(59);
				atInclude();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(60);
				atVar();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 6);
				{
				setState(61);
				atMixin();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtTitleContext extends ParserRuleContext {
		public Token title;
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public TerminalNode STRING() { return getToken(PtsParser.STRING, 0); }
		public AtTitleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atTitle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtTitle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtTitle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtTitle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtTitleContext atTitle() throws RecognitionException {
		AtTitleContext _localctx = new AtTitleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atTitle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(T__0);
			setState(65);
			((AtTitleContext)_localctx).title = match(STRING);
			setState(66);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtAuthorsContext extends ParserRuleContext {
		public Token STRING;
		public List<Token> authors = new ArrayList<Token>();
		public TerminalNode OPENING_BRACKET() { return getToken(PtsParser.OPENING_BRACKET, 0); }
		public TerminalNode CLOSING_BRACKET() { return getToken(PtsParser.CLOSING_BRACKET, 0); }
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public List<TerminalNode> STRING() { return getTokens(PtsParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(PtsParser.STRING, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PtsParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PtsParser.COMMA, i);
		}
		public AtAuthorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atAuthors; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtAuthors(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtAuthors(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtAuthors(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtAuthorsContext atAuthors() throws RecognitionException {
		AtAuthorsContext _localctx = new AtAuthorsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_atAuthors);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(T__1);
			setState(69);
			match(OPENING_BRACKET);
			setState(70);
			((AtAuthorsContext)_localctx).STRING = match(STRING);
			((AtAuthorsContext)_localctx).authors.add(((AtAuthorsContext)_localctx).STRING);
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(71);
				match(COMMA);
				setState(72);
				((AtAuthorsContext)_localctx).STRING = match(STRING);
				((AtAuthorsContext)_localctx).authors.add(((AtAuthorsContext)_localctx).STRING);
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(78);
			match(CLOSING_BRACKET);
			setState(79);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtFontContext extends ParserRuleContext {
		public Token font;
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public TerminalNode STRING() { return getToken(PtsParser.STRING, 0); }
		public AtFontContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atFont; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtFont(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtFont(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtFont(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtFontContext atFont() throws RecognitionException {
		AtFontContext _localctx = new AtFontContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atFont);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__2);
			setState(82);
			((AtFontContext)_localctx).font = match(STRING);
			setState(83);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtIncludeContext extends ParserRuleContext {
		public Token include;
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public TerminalNode STRING() { return getToken(PtsParser.STRING, 0); }
		public AtIncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atInclude; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtInclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtInclude(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtIncludeContext atInclude() throws RecognitionException {
		AtIncludeContext _localctx = new AtIncludeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_atInclude);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__3);
			setState(86);
			((AtIncludeContext)_localctx).include = match(STRING);
			setState(87);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtVarContext extends ParserRuleContext {
		public Token name;
		public Token type;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(PtsParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PtsParser.IDENTIFIER, i);
		}
		public AtVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtVarContext atVar() throws RecognitionException {
		AtVarContext _localctx = new AtVarContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_atVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(T__4);
			setState(90);
			((AtVarContext)_localctx).name = match(IDENTIFIER);
			setState(91);
			match(T__5);
			setState(92);
			((AtVarContext)_localctx).type = match(IDENTIFIER);
			setState(93);
			match(T__6);
			setState(94);
			expression();
			setState(95);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtMixinContext extends ParserRuleContext {
		public Token name;
		public DeclarationContext declaration;
		public List<DeclarationContext> properties = new ArrayList<DeclarationContext>();
		public TerminalNode OPENING_BRACE() { return getToken(PtsParser.OPENING_BRACE, 0); }
		public TerminalNode CLOSING_BRACE() { return getToken(PtsParser.CLOSING_BRACE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PtsParser.IDENTIFIER, 0); }
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public AtMixinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atMixin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterAtMixin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitAtMixin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitAtMixin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtMixinContext atMixin() throws RecognitionException {
		AtMixinContext _localctx = new AtMixinContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_atMixin);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(T__7);
			setState(98);
			((AtMixinContext)_localctx).name = match(IDENTIFIER);
			setState(99);
			match(OPENING_BRACE);
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9 || _la==IDENTIFIER) {
				{
				{
				setState(100);
				((AtMixinContext)_localctx).declaration = declaration();
				((AtMixinContext)_localctx).properties.add(((AtMixinContext)_localctx).declaration);
				}
				}
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(106);
			match(CLOSING_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StyleContext extends ParserRuleContext {
		public Token name;
		public Token tag;
		public Token state;
		public DeclarationContext declaration;
		public List<DeclarationContext> declarations = new ArrayList<DeclarationContext>();
		public TerminalNode OPENING_BRACE() { return getToken(PtsParser.OPENING_BRACE, 0); }
		public TerminalNode CLOSING_BRACE() { return getToken(PtsParser.CLOSING_BRACE, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(PtsParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PtsParser.IDENTIFIER, i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public StyleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_style; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterStyle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitStyle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitStyle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StyleContext style() throws RecognitionException {
		StyleContext _localctx = new StyleContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_style);
		int _la;
		try {
			setState(142);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				((StyleContext)_localctx).name = match(IDENTIFIER);
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5 || _la==T__8) {
					{
					setState(113);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__8:
						{
						{
						setState(109);
						match(T__8);
						setState(110);
						((StyleContext)_localctx).tag = match(IDENTIFIER);
						}
						}
						break;
					case T__5:
						{
						{
						setState(111);
						match(T__5);
						setState(112);
						((StyleContext)_localctx).state = match(IDENTIFIER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(117);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(118);
				match(OPENING_BRACE);
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9 || _la==IDENTIFIER) {
					{
					{
					setState(119);
					((StyleContext)_localctx).declaration = declaration();
					((StyleContext)_localctx).declarations.add(((StyleContext)_localctx).declaration);
					}
					}
					setState(124);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(125);
				match(CLOSING_BRACE);
				}
				break;
			case T__5:
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(130); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					setState(130);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__8:
						{
						{
						setState(126);
						match(T__8);
						setState(127);
						((StyleContext)_localctx).tag = match(IDENTIFIER);
						}
						}
						break;
					case T__5:
						{
						{
						setState(128);
						match(T__5);
						setState(129);
						((StyleContext)_localctx).state = match(IDENTIFIER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(132); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__5 || _la==T__8 );
				setState(134);
				match(OPENING_BRACE);
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9 || _la==IDENTIFIER) {
					{
					{
					setState(135);
					((StyleContext)_localctx).declaration = declaration();
					((StyleContext)_localctx).declarations.add(((StyleContext)_localctx).declaration);
					}
					}
					setState(140);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(141);
				match(CLOSING_BRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationContext extends ParserRuleContext {
		public ApplyContext apply() {
			return getRuleContext(ApplyContext.class,0);
		}
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_declaration);
		try {
			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(144);
				apply();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				property();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ApplyContext extends ParserRuleContext {
		public Token name;
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PtsParser.IDENTIFIER, 0); }
		public ApplyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_apply; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterApply(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitApply(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitApply(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyContext apply() throws RecognitionException {
		ApplyContext _localctx = new ApplyContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_apply);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(T__9);
			setState(149);
			((ApplyContext)_localctx).name = match(IDENTIFIER);
			setState(150);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyContext extends ParserRuleContext {
		public Token name;
		public ExpressionContext expr;
		public Token field;
		public TerminalNode SEMICOLON() { return getToken(PtsParser.SEMICOLON, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(PtsParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PtsParser.IDENTIFIER, i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_property);
		try {
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				((PropertyContext)_localctx).name = match(IDENTIFIER);
				setState(153);
				match(T__5);
				setState(154);
				((PropertyContext)_localctx).expr = expression();
				setState(155);
				match(SEMICOLON);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				((PropertyContext)_localctx).name = match(IDENTIFIER);
				setState(158);
				match(T__8);
				setState(159);
				((PropertyContext)_localctx).field = match(IDENTIFIER);
				setState(160);
				match(T__5);
				setState(161);
				((PropertyContext)_localctx).expr = expression();
				setState(162);
				match(SEMICOLON);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
		public Vec2Context vec2() {
			return getRuleContext(Vec2Context.class,0);
		}
		public Vec4Context vec4() {
			return getRuleContext(Vec4Context.class,0);
		}
		public ColorContext color() {
			return getRuleContext(ColorContext.class,0);
		}
		public Color4Context color4() {
			return getRuleContext(Color4Context.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expression);
		try {
			setState(174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				unit();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				vec2();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(168);
				vec4();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(169);
				color();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(170);
				color4();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(171);
				string();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(172);
				variable();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(173);
				identifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(PtsParser.NUMBER, 0); }
		public TerminalNode PX() { return getToken(PtsParser.PX, 0); }
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_unit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(NUMBER);
			setState(177);
			match(PX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Vec2Context extends ParserRuleContext {
		public UnitContext x;
		public UnitContext y;
		public List<UnitContext> unit() {
			return getRuleContexts(UnitContext.class);
		}
		public UnitContext unit(int i) {
			return getRuleContext(UnitContext.class,i);
		}
		public Vec2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vec2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterVec2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitVec2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitVec2(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Vec2Context vec2() throws RecognitionException {
		Vec2Context _localctx = new Vec2Context(_ctx, getState());
		enterRule(_localctx, 30, RULE_vec2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			((Vec2Context)_localctx).x = unit();
			setState(180);
			((Vec2Context)_localctx).y = unit();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Vec4Context extends ParserRuleContext {
		public UnitContext x;
		public UnitContext y;
		public UnitContext z;
		public UnitContext w;
		public List<UnitContext> unit() {
			return getRuleContexts(UnitContext.class);
		}
		public UnitContext unit(int i) {
			return getRuleContext(UnitContext.class,i);
		}
		public Vec4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vec4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterVec4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitVec4(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitVec4(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Vec4Context vec4() throws RecognitionException {
		Vec4Context _localctx = new Vec4Context(_ctx, getState());
		enterRule(_localctx, 32, RULE_vec4);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			((Vec4Context)_localctx).x = unit();
			setState(183);
			((Vec4Context)_localctx).y = unit();
			setState(184);
			((Vec4Context)_localctx).z = unit();
			setState(185);
			((Vec4Context)_localctx).w = unit();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColorContext extends ParserRuleContext {
		public TerminalNode HEX_COLOR() { return getToken(PtsParser.HEX_COLOR, 0); }
		public TerminalNode RGB_COLOR() { return getToken(PtsParser.RGB_COLOR, 0); }
		public ColorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_color; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterColor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitColor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitColor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColorContext color() throws RecognitionException {
		ColorContext _localctx = new ColorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_color);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			_la = _input.LA(1);
			if ( !(_la==HEX_COLOR || _la==RGB_COLOR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Color4Context extends ParserRuleContext {
		public List<ColorContext> color() {
			return getRuleContexts(ColorContext.class);
		}
		public ColorContext color(int i) {
			return getRuleContext(ColorContext.class,i);
		}
		public Color4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_color4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterColor4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitColor4(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitColor4(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Color4Context color4() throws RecognitionException {
		Color4Context _localctx = new Color4Context(_ctx, getState());
		enterRule(_localctx, 36, RULE_color4);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			color();
			setState(190);
			color();
			setState(191);
			color();
			setState(192);
			color();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(PtsParser.STRING, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public Token name;
		public TerminalNode BANG() { return getToken(PtsParser.BANG, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PtsParser.IDENTIFIER, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(BANG);
			setState(197);
			((VariableContext)_localctx).name = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PtsParser.IDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PtsListener ) ((PtsListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PtsVisitor ) return ((PtsVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001b\u00ca\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0001\u0000\u0005\u0000.\b\u0000\n\u0000\f\u00001\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u00017\b\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002?\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004J\b"+
		"\u0004\n\u0004\f\u0004M\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0005\bf\b\b\n\b\f\bi\t\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0005\tr\b\t\n\t\f\tu\t\t\u0001\t\u0001\t\u0005\ty\b\t\n\t"+
		"\f\t|\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0004\t\u0083\b\t\u000b"+
		"\t\f\t\u0084\u0001\t\u0001\t\u0005\t\u0089\b\t\n\t\f\t\u008c\t\t\u0001"+
		"\t\u0003\t\u008f\b\t\u0001\n\u0001\n\u0003\n\u0093\b\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00a5"+
		"\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003"+
		"\r\u00af\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0000\u0000\u0016\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*\u0000\u0001\u0001\u0000\r\u000e\u00cc\u0000/\u0001\u0000\u0000\u0000"+
		"\u00026\u0001\u0000\u0000\u0000\u0004>\u0001\u0000\u0000\u0000\u0006@"+
		"\u0001\u0000\u0000\u0000\bD\u0001\u0000\u0000\u0000\nQ\u0001\u0000\u0000"+
		"\u0000\fU\u0001\u0000\u0000\u0000\u000eY\u0001\u0000\u0000\u0000\u0010"+
		"a\u0001\u0000\u0000\u0000\u0012\u008e\u0001\u0000\u0000\u0000\u0014\u0092"+
		"\u0001\u0000\u0000\u0000\u0016\u0094\u0001\u0000\u0000\u0000\u0018\u00a4"+
		"\u0001\u0000\u0000\u0000\u001a\u00ae\u0001\u0000\u0000\u0000\u001c\u00b0"+
		"\u0001\u0000\u0000\u0000\u001e\u00b3\u0001\u0000\u0000\u0000 \u00b6\u0001"+
		"\u0000\u0000\u0000\"\u00bb\u0001\u0000\u0000\u0000$\u00bd\u0001\u0000"+
		"\u0000\u0000&\u00c2\u0001\u0000\u0000\u0000(\u00c4\u0001\u0000\u0000\u0000"+
		"*\u00c7\u0001\u0000\u0000\u0000,.\u0003\u0002\u0001\u0000-,\u0001\u0000"+
		"\u0000\u0000.1\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u0000/0\u0001"+
		"\u0000\u0000\u000002\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u0000"+
		"23\u0005\u0000\u0000\u00013\u0001\u0001\u0000\u0000\u000047\u0003\u0004"+
		"\u0002\u000057\u0003\u0012\t\u000064\u0001\u0000\u0000\u000065\u0001\u0000"+
		"\u0000\u00007\u0003\u0001\u0000\u0000\u00008?\u0003\u0006\u0003\u0000"+
		"9?\u0003\b\u0004\u0000:?\u0003\n\u0005\u0000;?\u0003\f\u0006\u0000<?\u0003"+
		"\u000e\u0007\u0000=?\u0003\u0010\b\u0000>8\u0001\u0000\u0000\u0000>9\u0001"+
		"\u0000\u0000\u0000>:\u0001\u0000\u0000\u0000>;\u0001\u0000\u0000\u0000"+
		"><\u0001\u0000\u0000\u0000>=\u0001\u0000\u0000\u0000?\u0005\u0001\u0000"+
		"\u0000\u0000@A\u0005\u0001\u0000\u0000AB\u0005\f\u0000\u0000BC\u0005\u0016"+
		"\u0000\u0000C\u0007\u0001\u0000\u0000\u0000DE\u0005\u0002\u0000\u0000"+
		"EF\u0005\u0013\u0000\u0000FK\u0005\f\u0000\u0000GH\u0005\u0015\u0000\u0000"+
		"HJ\u0005\f\u0000\u0000IG\u0001\u0000\u0000\u0000JM\u0001\u0000\u0000\u0000"+
		"KI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LN\u0001\u0000\u0000"+
		"\u0000MK\u0001\u0000\u0000\u0000NO\u0005\u0014\u0000\u0000OP\u0005\u0016"+
		"\u0000\u0000P\t\u0001\u0000\u0000\u0000QR\u0005\u0003\u0000\u0000RS\u0005"+
		"\f\u0000\u0000ST\u0005\u0016\u0000\u0000T\u000b\u0001\u0000\u0000\u0000"+
		"UV\u0005\u0004\u0000\u0000VW\u0005\f\u0000\u0000WX\u0005\u0016\u0000\u0000"+
		"X\r\u0001\u0000\u0000\u0000YZ\u0005\u0005\u0000\u0000Z[\u0005\u0010\u0000"+
		"\u0000[\\\u0005\u0006\u0000\u0000\\]\u0005\u0010\u0000\u0000]^\u0005\u0007"+
		"\u0000\u0000^_\u0003\u001a\r\u0000_`\u0005\u0016\u0000\u0000`\u000f\u0001"+
		"\u0000\u0000\u0000ab\u0005\b\u0000\u0000bc\u0005\u0010\u0000\u0000cg\u0005"+
		"\u0011\u0000\u0000df\u0003\u0014\n\u0000ed\u0001\u0000\u0000\u0000fi\u0001"+
		"\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000"+
		"hj\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000jk\u0005\u0012\u0000"+
		"\u0000k\u0011\u0001\u0000\u0000\u0000ls\u0005\u0010\u0000\u0000mn\u0005"+
		"\t\u0000\u0000nr\u0005\u0010\u0000\u0000op\u0005\u0006\u0000\u0000pr\u0005"+
		"\u0010\u0000\u0000qm\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"ru\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000st\u0001\u0000\u0000"+
		"\u0000tv\u0001\u0000\u0000\u0000us\u0001\u0000\u0000\u0000vz\u0005\u0011"+
		"\u0000\u0000wy\u0003\u0014\n\u0000xw\u0001\u0000\u0000\u0000y|\u0001\u0000"+
		"\u0000\u0000zx\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{}\u0001"+
		"\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000}\u008f\u0005\u0012\u0000"+
		"\u0000~\u007f\u0005\t\u0000\u0000\u007f\u0083\u0005\u0010\u0000\u0000"+
		"\u0080\u0081\u0005\u0006\u0000\u0000\u0081\u0083\u0005\u0010\u0000\u0000"+
		"\u0082~\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0084"+
		"\u0085\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086"+
		"\u008a\u0005\u0011\u0000\u0000\u0087\u0089\u0003\u0014\n\u0000\u0088\u0087"+
		"\u0001\u0000\u0000\u0000\u0089\u008c\u0001\u0000\u0000\u0000\u008a\u0088"+
		"\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008d"+
		"\u0001\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008d\u008f"+
		"\u0005\u0012\u0000\u0000\u008el\u0001\u0000\u0000\u0000\u008e\u0082\u0001"+
		"\u0000\u0000\u0000\u008f\u0013\u0001\u0000\u0000\u0000\u0090\u0093\u0003"+
		"\u0016\u000b\u0000\u0091\u0093\u0003\u0018\f\u0000\u0092\u0090\u0001\u0000"+
		"\u0000\u0000\u0092\u0091\u0001\u0000\u0000\u0000\u0093\u0015\u0001\u0000"+
		"\u0000\u0000\u0094\u0095\u0005\n\u0000\u0000\u0095\u0096\u0005\u0010\u0000"+
		"\u0000\u0096\u0097\u0005\u0016\u0000\u0000\u0097\u0017\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0005\u0010\u0000\u0000\u0099\u009a\u0005\u0006\u0000"+
		"\u0000\u009a\u009b\u0003\u001a\r\u0000\u009b\u009c\u0005\u0016\u0000\u0000"+
		"\u009c\u00a5\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u0010\u0000\u0000"+
		"\u009e\u009f\u0005\t\u0000\u0000\u009f\u00a0\u0005\u0010\u0000\u0000\u00a0"+
		"\u00a1\u0005\u0006\u0000\u0000\u00a1\u00a2\u0003\u001a\r\u0000\u00a2\u00a3"+
		"\u0005\u0016\u0000\u0000\u00a3\u00a5\u0001\u0000\u0000\u0000\u00a4\u0098"+
		"\u0001\u0000\u0000\u0000\u00a4\u009d\u0001\u0000\u0000\u0000\u00a5\u0019"+
		"\u0001\u0000\u0000\u0000\u00a6\u00af\u0003\u001c\u000e\u0000\u00a7\u00af"+
		"\u0003\u001e\u000f\u0000\u00a8\u00af\u0003 \u0010\u0000\u00a9\u00af\u0003"+
		"\"\u0011\u0000\u00aa\u00af\u0003$\u0012\u0000\u00ab\u00af\u0003&\u0013"+
		"\u0000\u00ac\u00af\u0003(\u0014\u0000\u00ad\u00af\u0003*\u0015\u0000\u00ae"+
		"\u00a6\u0001\u0000\u0000\u0000\u00ae\u00a7\u0001\u0000\u0000\u0000\u00ae"+
		"\u00a8\u0001\u0000\u0000\u0000\u00ae\u00a9\u0001\u0000\u0000\u0000\u00ae"+
		"\u00aa\u0001\u0000\u0000\u0000\u00ae\u00ab\u0001\u0000\u0000\u0000\u00ae"+
		"\u00ac\u0001\u0000\u0000\u0000\u00ae\u00ad\u0001\u0000\u0000\u0000\u00af"+
		"\u001b\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005\u000b\u0000\u0000\u00b1"+
		"\u00b2\u0005\u000f\u0000\u0000\u00b2\u001d\u0001\u0000\u0000\u0000\u00b3"+
		"\u00b4\u0003\u001c\u000e\u0000\u00b4\u00b5\u0003\u001c\u000e\u0000\u00b5"+
		"\u001f\u0001\u0000\u0000\u0000\u00b6\u00b7\u0003\u001c\u000e\u0000\u00b7"+
		"\u00b8\u0003\u001c\u000e\u0000\u00b8\u00b9\u0003\u001c\u000e\u0000\u00b9"+
		"\u00ba\u0003\u001c\u000e\u0000\u00ba!\u0001\u0000\u0000\u0000\u00bb\u00bc"+
		"\u0007\u0000\u0000\u0000\u00bc#\u0001\u0000\u0000\u0000\u00bd\u00be\u0003"+
		"\"\u0011\u0000\u00be\u00bf\u0003\"\u0011\u0000\u00bf\u00c0\u0003\"\u0011"+
		"\u0000\u00c0\u00c1\u0003\"\u0011\u0000\u00c1%\u0001\u0000\u0000\u0000"+
		"\u00c2\u00c3\u0005\f\u0000\u0000\u00c3\'\u0001\u0000\u0000\u0000\u00c4"+
		"\u00c5\u0005\u0017\u0000\u0000\u00c5\u00c6\u0005\u0010\u0000\u0000\u00c6"+
		")\u0001\u0000\u0000\u0000\u00c7\u00c8\u0005\u0010\u0000\u0000\u00c8+\u0001"+
		"\u0000\u0000\u0000\u000f/6>Kgqsz\u0082\u0084\u008a\u008e\u0092\u00a4\u00ae";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}