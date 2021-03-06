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
	: PROFILE DELIM (IDENTIFIER DELIM)? MODEL
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
	: LPAREN (cardinality COMMA)? (aliasRule COMMA)? (complexTypeRule COMMA)? (bindingRule COMMA)? extension RPAREN
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
	
bindingRule
	: bindingConformanceRule COMMA bindingReferenceRule
	;
	
bindingConformanceRule
	: CONFORMANCE EQUALS conformanceValue
	;
	
bindingReferenceRule
	: REFERENCE EQUALS url
	;

conformanceValue
	: 'required'
	| 'preferred'
	| 'example'
	;	
	
high
	: UNLIMITED
	| INTEGER
	;
	
url
	: QUOTED_STRING //SCHEME + COLON + PATH_SEPARATOR + PATH_SEPARATOR IDENTIFIER (DELIM IDENTIFIER)* (PATH_SEPARATOR IDENTIFIER)* (DELIM IDENTIFIER)? //TODO address more generally later
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
	
SCHEME
	: 'http'
	| 'https'
	;
	
PATH_SEPARATOR
	: '/'
	;
	
EQUALS
	: '='
	;
	
COMMA
	: ',' 
	;
	
COLON
	: ':'
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
	
CONFORMANCE
	: 'conformance'
	;
	
REFERENCE
	: 'reference'
	;
	
QUOTED_STRING
	: '"' [a-zA-Z0-9:/.-_&;]+ '"'
	;

WS
	: ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip
	;