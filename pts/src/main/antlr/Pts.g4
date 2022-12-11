grammar Pts;

pts : statement* EOF ;

// Statements
statement : atStatement
          | style
          ;

// At Statements
atStatement : atTitle
            | atAuthors
            | atInclude
            | atVar
            | atMixin
            ;

atTitle : '@title' title=STRING SEMICOLON ;
atAuthors : '@authors' OPENING_BRACKET authors+=STRING (COMMA authors+=STRING)* CLOSING_BRACKET SEMICOLON ;
atInclude : '@include' include=STRING SEMICOLON ;
atVar : '@var' name=IDENTIFIER ':' type=IDENTIFIER '=' expression+ SEMICOLON ;
atMixin : '@mixin' name=IDENTIFIER OPENING_BRACE properties+=declaration* CLOSING_BRACE ;

// Style
style : name=IDENTIFIER (('.' tag=IDENTIFIER) | (':' state=IDENTIFIER))* OPENING_BRACE declarations+=declaration* CLOSING_BRACE
      | (('.' tag=IDENTIFIER) | (':' state=IDENTIFIER))+ OPENING_BRACE declarations+=declaration* CLOSING_BRACE
      ;

// Declarations
declaration : apply
            | property
            ;

apply : '@apply' name=IDENTIFIER SEMICOLON ;

property : name=IDENTIFIER ':' expression+ SEMICOLON
         | name=IDENTIFIER '.' accessor=IDENTIFIER ':' expression+ SEMICOLON
         ;

// Expressions
expression : unit
           | color
           | identifier
           | string
           | variable
           | function
           ;

unit : NUMBER PX ;
color : HEX_COLOR ;
identifier : IDENTIFIER ;
string : STRING ;
variable : BANG name=IDENTIFIER ;
function : name=IDENTIFIER OPENING_PAREN args+=NUMBER? (COMMA args+=NUMBER*)* CLOSING_PAREN ;


// Lexer
NUMBER : '-'? INT ('.' INT)? ;
STRING : QUOTE ~[\\"]* QUOTE ;

HEX_COLOR : '#' HEX HEX HEX HEX HEX HEX
          | '#' HEX HEX HEX HEX HEX HEX HEX HEX
          ;

PX : 'px' ;

IDENTIFIER : [a-zA-Z_\-][a-zA-Z_\-0-9]* ;

OPENING_PAREN : '(' ;
CLOSING_PAREN : ')' ;

OPENING_BRACE : '{' ;
CLOSING_BRACE : '}' ;

OPENING_BRACKET : '[' ;
CLOSING_BRACKET : ']' ;

COMMA : ',' ;
SEMICOLON : ';' ;
BANG : '!' ;
QUOTE : '"' ;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

WS : [ \n\t\r]+ -> channel(HIDDEN);
UNKNOWN : . ;

fragment INT : [0-9]+ ;
fragment HEX : [0-9a-fA-F] ;