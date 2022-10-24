grammar Pts;

pts : statement* EOF ;

// Statements
statement : atStatement
          | style
          ;

// At Statements
atStatement : atTitle
            | atAuthors
            | atFont
            | atInclude
            | atVar
            | atMixin
            ;

atTitle : '@title' title=STRING SEMICOLON ;
atAuthors : '@authors' OPENING_BRACKET authors+=STRING (COMMA authors+=STRING)* CLOSING_BRACKET SEMICOLON ;
atFont : '@font' font=STRING SEMICOLON ;
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
           ;

unit : NUMBER PX ;

color : HEX_COLOR
      | RGB_COLOR
      ;

string : STRING ;
variable : BANG name=IDENTIFIER ;
identifier : IDENTIFIER ;

// Lexer
NUMBER : '-'? INT ('.' [0-9]+)? ;
STRING : '"' ~[\\"]+ '"' ;

HEX_COLOR : '#' HEX HEX HEX HEX HEX HEX
          | '#' HEX HEX HEX HEX HEX HEX HEX HEX
          ;

RGB_COLOR : 'rgb(' WS* INT WS* COMMA WS* INT WS* COMMA WS* INT WS* ')'
          | 'rgba(' WS* INT WS* COMMA WS* INT WS* COMMA WS* INT WS* COMMA WS* INT WS* ')'
          ;

PX : 'px' ;

IDENTIFIER : [a-zA-Z_\-][a-zA-Z_\-0-9]* ;

OPENING_BRACE : '{' ;
CLOSING_BRACE : '}' ;

OPENING_BRACKET : '[' ;
CLOSING_BRACKET : ']' ;

COMMA : ',' ;
SEMICOLON : ';' ;
BANG : '!' ;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

WS : [ \n\t\r]+ -> channel(HIDDEN);
UNKNOWN : . ;

fragment INT : '0' | [1-9][0-9]* ;
fragment HEX : [0-9a-fA-F] ;