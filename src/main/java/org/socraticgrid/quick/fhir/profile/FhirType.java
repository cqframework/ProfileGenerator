package org.socraticgrid.quick.fhir.profile;

public enum FhirType {
	
	CODEABLE_CONCEPT("CodeableConcept"),
	CODE("code"),
	PERIOD("Period"),
	IDENTIFIER("Identifier"),
	RANGE("Range"),
	DATETIME("dateTime"),
	STRING("string"),
	DATE("date");
	
	private String name; 
	
	private FhirType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static boolean contains(String name) {
		boolean found = false;
		for(FhirType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) {
				found = true;
				break;
			}
		}
		return found;
	}
}
