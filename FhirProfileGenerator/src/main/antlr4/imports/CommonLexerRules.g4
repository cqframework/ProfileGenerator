lexer grammar CommonLexerRules; // note "lexer grammar"
 	
 	
IDENTIFIER
	:   [a-zA-Z]+
	;
 	
INTEGER 
	:  [0-9]+
	;
 	
NEWLINE
	:'\r'? '\n' 
	;
 	
//WS  :   [ \t]+ -> skip ;