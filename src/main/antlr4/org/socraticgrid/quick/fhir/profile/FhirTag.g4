grammar FhirTag;			// Define a grammar called FhirTag
import CommonLexerRules; // includes all rules from CommonLexerRules.g4

// Parser Rules

handleTagRule
	: handleStructureRule
	| handleElementRule
	;

handleStructureRule
	: structureNameRule
	| structurePublishRule
	| structurePurposeRule
	| structureTypeRule
	;

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
	
structureNameRule
	: profiledStructure DELIM NAME EQUALS IDENTIFIER
	;
	
structurePublishRule
	: profiledStructure DELIM PUBLISH EQUALS BOOLEAN
	;
	
structurePurposeRule
	: profiledStructure DELIM PURPOSE EQUALS IDENTIFIER (IDENTIFIER|DELIM)* //TODO Fix this.
	;
	
structureTypeRule
	: profiledStructure DELIM TYPE EQUALS IDENTIFIER
	;
	
profile
	: PROFILE DELIM MODEL
	;
	
profiledElement
	: profile DELIM ELEMENT
	;
	
profiledStructure
	: profile DELIM STRUCTURE
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
	
STRUCTURE
	: 'structure'
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
	
NAME
	: 'name'
	;
	
PUBLISH
	: 'publish'
	;
	
PURPOSE
	: 'purpose'
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