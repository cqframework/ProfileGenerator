grammar FhirTag;			// Define a grammar called FhirTag
import CommonLexerRules; // includes all rules from CommonLexerRules.g4

// Parser Rules

handleElementRule
	: skipElementRule
	| equivalentElementRule
	| elementRule
	;

skipElementRule
	: profiledElement DELIM SKIP (EQUALS BOOLEAN)?
	;
	
equivalentElementRule
	: profiledElement DELIM pathExpression DELIM SAMEAS (EQUALS BOOLEAN)?
	;

elementRule
	: profiledElement DELIM pathExpression EQUALS pathExpression parameters? 
	;
	
profile
	: PROFILE DELIM MODEL
	;
	
profiledElement
	: profile DELIM ELEMENT
	;
	
pathExpression
	: path (DELIM path)*
	;
	  
path
	: IDENTIFIER + (baseTypeAnnotation)?
	;
	
baseTypeAnnotation
	: LBRACKET + IDENTIFIER + RBRACKET
	;
	
name
	: IDENTIFIER
	;
	
parameters
	: LPAREN (cardinality COMMA)? (aliasRule COMMA)? (complexTypeRule COMMA)? extension RPAREN
	;
	
cardinality
	: CARDINALITY EQUALS INTEGER RANGE_DELIM high
	;
	
aliasRule
	: ALIAS EQUALS name
	;
	
extension
	: EXTENSION EQUALS BOOLEAN
	;
	
complexTypeRule
	: simpleTypeRule (COMMA simpleTypeRule)*
	;
	
simpleTypeRule
	: TYPE EQUALS name
	;
		
high
	: UNLIMITED
	| INTEGER
	;

// Lexer Rules

PROFILE
	: 'profile'
	;

DELIM
	: '.' 
	;

MODEL
	: 'fhir' | 'quick'
	;
	
ELEMENT
	: 'element'
	;
	
ALIAS
	: 'alias'
	;
	
CARDINALITY
	: 'cardinality'
	;
	
EXTENSION
	: 'extension'
	;
	
TYPE
	: 'type'
	;
	
BOOLEAN
	: 'true' | 'false'
	;
	
EQUALS
	: '='
	;
	
COMMA
	: ',' 
	;
	
LPAREN
	: '('
	;
	
RPAREN
	: ')'
	;
	
LBRACKET
	: '['
	;
	
RBRACKET
	: ']'
	;
	
UNLIMITED
	: '*'
	;
	
SKIP
	: 'skip'
	;
	
SAMEAS
	: 'equivalent'
	;
	
RANGE_DELIM
	: '..'
	;
	
WS
	: ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip
	;