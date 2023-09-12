grammar Little;

KEYWORD : 'PROGRAM'|'BEGIN'|'STRING'|'FUNCTION'|'INT'|'IF'|'RETURN'|'ELSE'|'RETURN'|'ENDIF'|'END'|'VOID'|'READ'|'WRITE'|'ENDWHILE'|'ENDIF'|'WHILE'|'CONTINUE'|'BREAK'|'INT'|'FLOAT' ;
OPERATOR : '='|'-'|'+'|'*'|'/'|'!='|'<'|'>'|'('|')'|';'|','|'<='|'>=' ;
FLOATLITERAL: [0-9] '.' [0-9]+;
COMMENT : '--' ~( '\r' | '\n' )* -> skip ;
INTLITERAL : [0-9]+ ;
IDENTIFIER: [a-zA-Z]+ [a-zA-Z0-9]* ;
STRINGLITERAL: '"'.*?'"'; 