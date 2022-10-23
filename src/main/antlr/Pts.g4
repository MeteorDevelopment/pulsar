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

atTitle : '@title' title=STRING ';' ;
atAuthors : '@authors' '[' authors+=STRING (',' authors+=STRING)* ']' ';' ;
atFont : '@font' font=STRING ';' ;
atInclude : '@include' include=STRING ';' ;
atVar : '@var' name=IDENTIFIER ':' type=IDENTIFIER '=' expression ';' ;
atMixin : '@mixin' name=IDENTIFIER '{' properties+=declaration* '}' ;

// Style
style : name=IDENTIFIER (('.' tag=IDENTIFIER) | (':' state=IDENTIFIER))* '{' declarations+=declaration* '}'
      | (('.' tag=IDENTIFIER) | (':' state=IDENTIFIER))+ '{' declarations+=declaration* '}'
      ;

// Declarations
declaration : apply
            | property
            ;

apply : '@apply' name=IDENTIFIER ';' ;

property : name=IDENTIFIER ':' expr=expression ';'
         | name=IDENTIFIER '.' field=IDENTIFIER ':' expr=expression ';'
         ;

// Expressions
expression : unit
           | vec2
           | vec4
           | color
           | color4
           | string
           | variable
           | identifier
           ;

unit : NUMBER 'px' ;
vec2 : x=unit y=unit ;
vec4 : x=unit y=unit z=unit w=unit ;

color : HEX_COLOR
      | RGB_COLOR
      ;

color4 : color color color color ;

string : STRING ;
variable : '!' name=IDENTIFIER ;
identifier : IDENTIFIER ;

// Lexer
NUMBER : '-'? INT ('.' [0-9]+)? ;
STRING : '"' ~[\\"]+ '"' ;

HEX_COLOR : '#' HEX HEX HEX HEX HEX HEX
          | '#' HEX HEX HEX HEX HEX HEX HEX HEX
          ;

RGB_COLOR : 'rgb(' WS* INT WS* ',' WS* INT WS* ',' WS* INT WS* ')'
          | 'rgba(' WS* INT WS* ',' WS* INT WS* ',' WS* INT WS* ',' WS* INT WS* ')'
          ;

IDENTIFIER : [a-zA-Z_\-][a-zA-Z_\-0-9]* ;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

WS : [ \n\t\r]+ -> channel(HIDDEN);

fragment INT : '0' | [1-9][0-9]* ;
fragment HEX : [0-9a-fA-F] ;