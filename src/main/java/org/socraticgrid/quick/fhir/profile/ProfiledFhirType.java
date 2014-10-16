package org.socraticgrid.quick.fhir.profile;

/**
 * 
 * Enumeration introduced for FHIR types that are typically
 * profiled. This is to reduce the need for annotations in
 * the model. This is equivalent to tagging an attribute as:
 * 
 * profile.fhir.element.type.profile=profilename
 * 
 * @author cnanjo
 *
 */
public enum ProfiledFhirType {
	
	PATIENT("Patient"),
	PRACTITIONER("Practitioner"),
	DEVICE("Device"),
	ORGANIZATION("Organization"),
	RELATED_PERSON("RelatedPerson"),
	ENCOUNTER("Encounter");
	
	private String name; 
	
	private ProfiledFhirType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static boolean contains(String name) {
		boolean found = false;
		for(ProfiledFhirType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * Maps QUICK leaf-level class names to FHIR resource names
	 * 
	 * @param quickClass
	 * @return
	 */
	public static ProfiledFhirType getFhirResourceForQuickClass(String quickClass) {
		if(quickClass.equals("EncounterPerformanceOccurrence")) {
			return ProfiledFhirType.ENCOUNTER;
		} else {
			return null;
		}
	}
	
	/**
	 * Method returns the FHIR name for the given QUICK class.
	 * 
	 * @param quickClass
	 * @return
	 */
	public static String getFhirTypeName(String quickClass) {
		String fhirType = null;
		if(contains(quickClass)) {
			fhirType =  quickClass;
		} else {
			ProfiledFhirType fhirResource = ProfiledFhirType.getFhirResourceForQuickClass(quickClass);
			if(fhirResource != null) {
				fhirType = fhirResource.getName();
			}
		}
		return fhirType;
	}
}
