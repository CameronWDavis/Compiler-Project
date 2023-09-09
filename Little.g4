grammar Little;

KEYWORD : 'PROGRAM'|'BEGIN'|'STRING'|'FUNCTION'|'INT'|'IF'|'RETURN'|'ELSE'|'RETURN'|'ENDIF'|'END'|'VOID'|'READ'|'WRITE'|'ENDWHILE' ;
OPERATOR : '='|';'|'+'|')'|'('|','|'<='|'<'|'-'|'!=';
INTLITERAL : [0-9]+ ;
WS : [ \t]+ -> skip;
