package org.socraticgrid.quick.fhir.profile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;

import org.hl7.fhir.instance.formats.XmlComposer;
import org.hl7.fhir.instance.model.Contact;
import org.hl7.fhir.instance.model.DateAndTime;
import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Profile.ConstraintComponent;
import org.hl7.fhir.instance.model.Profile.ElementComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionMappingComponent;
import org.hl7.fhir.instance.model.Profile.ExtensionContext;
import org.hl7.fhir.instance.model.Profile.ProfileExtensionDefnComponent;
import org.hl7.fhir.instance.model.Profile.ProfileStructureComponent;
import org.hl7.fhir.instance.model.Profile.ResourceProfileStatus;
import org.hl7.fhir.instance.model.Profile.TypeRefComponent;
import org.socraticgrid.uml.OneToOnePropertyMapping;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlProperty;

/**
 * 
 * Class responsible for the generation of FHIR profiles from an annotated QUICK model.
 * 
 * Label: none
 * Processing: Throw exception.
 * 
 * Label: profile.fhir.element.sameas
 * Processing: 
 * If type is simple FHIR data type, populate element with mustSupport = true and with mapping to QUICK specified.
 * If type is a FHIR resource, populate element with mustSupport = true and with a profiled type (both code and profile specified for type)
 * 
 * @author cnanjo
 *
 */
public class FhirProfileGenerator {
	
	//TODO Add Profile.text
	//TODO Add Profile.ivariant = not(descendant::f:modifierExtension)
	//TODO Put all attribute extension definitions in a separate profile and reference the profile in the element definition
	//TODO Add tag for extension display name (e.g., 'Contribution to death')
	//TODO Add tests for extension definitions
	//TODO Refactor and generalize ConditionOccurrenceGenerationTest. Build test scaffold.
	//TODO Index by not just names since many things are called code
	//TODO Handle terminology bindings
	//TODO Constrain out status = 'Refuted'
	//TODO Handle BodySite type
	//TODO Map effectiveTime to ObservedAtTime. Make statementDateTime an FHIR extension.
	
	//QUESTION: Is modifyingExtension constraint valid? Do I need to do this for every attribute as well? How about their extensions? Do we just make part of spec to reject? What if attribute is required?
	//QUESTION: Do we wish to use QUICK formal documentation on classes and attributes or leave the FHIR defaults?
	
	private static final Logger LOGGER = Logger.getLogger(FhirProfileGenerator.class.getName()); 
	private static final String QUICK_CORE_PROFILE_PATH = "http://hl7.org/quick/profile/";//TODO Fix based on Lloyd's direction
	private static final String MAP_IDENTITY = "quick";
	
	public FhirProfileGenerator() {}

	/**
	 * Method generates a FHIR profile for the given QUICK UML leaf-level class. 
	 * The class will be mapped to a FHIR Profile Structure object 
	 * represented by the target. Elements will be added to the 
	 * differential for the target structure according to the 
	 * mappings specified as TaggedValue annotations in the QUICK model.
	 * 
	 * @param source
	 * @param target
	 */
	public Profile generateSimpleProfile(UmlClass source, UmlClass target) {//TODO Build target from structure tag information
		Profile profile = new Profile();
		FhirClassAnnotationHandler fhirClass = new FhirClassAnnotationHandler(source, target);
		List<OneToOnePropertyMapping> mappings = fhirClass.initializeMappings();
		populateProfileHeaderInformation(profile, fhirClass);
		ProfileStructureComponent structure = addStructureWithDifferential(profile, fhirClass);
		ConstraintComponent constraint = structure.getDifferential();
		buildRootElement(fhirClass, constraint);
		constrainModifierExtension(fhirClass, constraint);
		processMappings(profile, mappings, constraint);
		return profile;
	}
	
	/**
	 * Method populates the profile-level metadata based on information contained in a metadata
	 * file.
	 * 
	 * @param profile
	 * @param fhirClass
	 */
	public void populateProfileHeaderInformation(Profile profile, FhirClassAnnotationHandler fhirClass) {
		ProfileConfigurationLoader loader = new ProfileConfigurationLoader();
		ProfileMetadata metadata = loader.loadProfileConfigurations();
		profile.setUrlSimple(metadata.getSharedProperty("profileRootPath") + fhirClass.getStructureName());
		profile.setVersionSimple(metadata.getSharedProperty("profileBaseVersion"));
		profile.setNameSimple(fhirClass.getStructureName());
		profile.setPublisherSimple(metadata.getSharedProperty("profilePublisher"));
		Contact contact = profile.addTelecom();
		contact.setValueSimple(metadata.getSharedProperty("profileEmailContact"));
		profile.setDescriptionSimple(metadata.getPropertyForProfile(fhirClass.getStructureName(), "profileDescription"));
		profile.setStatusSimple(ResourceProfileStatus.draft);//TODO read from config
		profile.setRequirementsSimple(metadata.getPropertyForProfile(fhirClass.getStructureName(), "profileRequirements"));
		profile.setDateSimple(DateAndTime.now());//TODO Fix as needed
		profile.setFhirVersionSimple(metadata.getSharedProperty("profileFhirVersion"));
	}
	
	/**
	 * Adds a FHIR profile structure that corresponds to the target UML class.
	 * 
	 * Precondition: UML class is tagged with the following tagged values:
	 * 1. profile.fhir.structure.type - Maps to Structure.type
	 * 2. profile.fhir.structure.name - Maps to Structure.name
	 * 3. profile.fhir.structure.publish - Maps to Structure.publish
	 * 4. profile.fhir.structure.purpose - Maps to Structure.purpose
	 * 
	 * @param fhirClass
	 * @return
	 */
	public ProfileStructureComponent addStructureWithDifferential(Profile profile, FhirClassAnnotationHandler fhirClass) {
		ProfileStructureComponent structure = profile.addStructure();
		structure.setTypeSimple(fhirClass.getStructureType());
		structure.setNameSimple(fhirClass.getStructureName());
		structure.setPublishSimple(fhirClass.getStructurePublishFlag());
		structure.setPurposeSimple(fhirClass.getStructurePurpose());
		ConstraintComponent differential = new ConstraintComponent();
		structure.setDifferential(differential);
		return structure;
	}
	
	/**
	 * Processes all mappings defined between QUICK and FHIR for profile generation.
	 * 
	 * @param profile
	 * @param mappings
	 * @param constraint
	 */
	public void processMappings(Profile profile, List<OneToOnePropertyMapping> mappings, ConstraintComponent constraint) {
		for(OneToOnePropertyMapping mapping : mappings) {
			if(mapping.isMapped()) {
				processMapping(profile, mapping, constraint);
			}
		}
	}
	
	/*******************************************
	 * Profile Generation Methods
	 *******************************************/
	
	/**
	 * Build the root element for this structure. All subsequent element entries
	 * are relative to this root element. The root element is generally the target
	 * FHIR class itself.
	 * 
	 * @param fhirClass
	 * @param constraint
	 */
	public void buildRootElement(FhirClassAnnotationHandler fhirClass, ConstraintComponent constraint) {
		ElementComponent element = constraint.addElement();
		element.setPathSimple(fhirClass.getStructureType());
		element.setNameSimple(fhirClass.getStructureName());
		ElementDefinitionComponent definition = new ElementDefinitionComponent();
		element.setDefinition(definition);
		definition.setFormalSimple(fhirClass.getUmlClass().getDescription());
		definition.setMinSimple(1);
		definition.setMaxSimple("1");
		TypeRefComponent type = definition.addType();
		type.setCodeSimple("Resource");//TODO Get from FHIR class
		setQuickMapping(fhirClass.getStructureName(), definition);
	}
	
	/**
	 * Method constrains the root of the profile to not support modifying extensions.
	 * 
	 * @param fhirClass
	 * @param constraint
	 */
	public void constrainModifierExtension(FhirClassAnnotationHandler fhirClass, ConstraintComponent constraint) {
		ElementComponent element = constraint.addElement();
		element.setPathSimple(fhirClass.getStructureType() + "." + "modifierExtension");
		element.setNameSimple("modifierExtension");
		ElementDefinitionComponent definition = new ElementDefinitionComponent();
		element.setDefinition(definition);
		definition.setMinSimple(0);
		definition.setMaxSimple("0");
	}
	
	/**
	 * Workhorse of the FhirProfileGenerator. Method processes each mapping and
	 * generates the equivalent attribute element entry in the differential of the
	 * structure.
	 * 
	 * @param profile
	 * @param mapping
	 * @param constraint
	 */
	public void processMapping(Profile profile, OneToOnePropertyMapping mapping, ConstraintComponent constraint) {
		ElementComponent element = constraint.addElement();
		element.setNameSimple(mapping.getDestination().getName());
		setElementPath(mapping, element);
		ElementDefinitionComponent definition = new ElementDefinitionComponent();
		element.setDefinition(definition);
		definition.setMinSimple(mapping.getDestination().getLow());
		definition.setMaxSimple((mapping.getDestination().getHigh() == -1)?"*":mapping.getDestination().getHigh().toString());//TODO move to the right place
		definition.setFormalSimple(mapping.getDestination().getDocumentation());
		List<UmlClass> types = mapping.getDestination().getTypes();
		for(UmlClass type : types) {
			if(mapping.isExtension()) {
				processExtendedTypes(mapping.getDestination(), definition);
			} else {
				processNonExtendedType(type, definition);
			}
		}
		setQuickMapping(mapping.getSourcePath(), definition);
		definition.setMustSupportSimple(true);
		if(mapping.isExtension()) {
			addClassAttributeExtension(profile, mapping);
		}
	}
	
	public void setElementPath(OneToOnePropertyMapping mapping, ElementComponent element) {
		if(mapping.isExtension()) {
			element.setPathSimple(mapping.getDestinationPathPrefix() + ".extension");
		} else {
			element.setPathSimple(mapping.getDestinationPath());
		}
	}
	
	public void addClassAttributeExtension(Profile profile, OneToOnePropertyMapping mapping) {
		ProfileExtensionDefnComponent extension = profile.addExtensionDefn();
		extension.setCodeSimple(mapping.getDestination().getName());
		extension.setDisplaySimple(mapping.getDestination().getName());
		extension.setContextTypeSimple(ExtensionContext.resource);
		extension.addContextSimple("Any");
		
		ElementComponent extensionDefinition = extension.addElement();
		extensionDefinition.setPathSimple(mapping.getDestination().getName());
		extensionDefinition.setNameSimple(mapping.getDestination().getName());
		
		ElementDefinitionComponent definition = new ElementDefinitionComponent();
		extensionDefinition.setDefinition(definition);
		defineElement(mapping, definition, false, false);
	}
	
	public void defineElement(OneToOnePropertyMapping mapping, ElementDefinitionComponent definition, boolean isExtension, boolean includeMapping) {
		processNonExtendedType(mapping.getDestination().getFirstType(), definition);
		if(includeMapping) {
			setQuickMapping(mapping.getDestination(), definition);
		}
		if(!isExtension) {
			definition.setFormalSimple(mapping.getDestination().getDocumentation());
			definition.setMinSimple(mapping.getDestination().getLow());
			definition.setMaxSimple((mapping.getDestination().getHigh() == -1)?"*":mapping.getDestination().getHigh().toString());//TODO move somewhere else?
		}
	}
	
	/**
	 * Assigns to profile element definition the corresponding mapping to the QUICK model.
	 * 
	 * @param attribute
	 * @param definition
	 */
	public void setQuickMapping(UmlProperty attribute, ElementDefinitionComponent definition) {
		String source = FhirAttributeAnnotationHandler.getElementSource(attribute);
		String map = null;
		if(source == null) {
			map = attribute.getSource().getName() + "." + attribute.getName();
		} else {
			map = attribute.getSource().getName() + "." + source;
		}
		setQuickMapping(map, definition);
	}
	
	/**
	 * Assigns to profile element definition the corresponding mapping to the QUICK model.
	 * 
	 * @param attribute
	 * @param definition
	 */
	public void setQuickMapping(String mapName, ElementDefinitionComponent definition) {
		if(mapName != null && mapName.trim().length() > 0) {
			ElementDefinitionMappingComponent mapping = definition.addMapping();
			mapping.setIdentitySimple(MAP_IDENTITY);
			mapping.setMapSimple(mapName);
		}
	}

	/**
	 * Elements that reference extended attributes need to indicate so in their type definition. 
	 * The type definition must also reference the extension definition in the corresponding profile.
	 * 
	 * E.g., Type(code: 'Extension', profile: '#criticality')
	 * 
	 * @param attribute
	 * @param definition
	 */
	public void processExtendedTypes(UmlProperty attribute, ElementDefinitionComponent definition) {
		TypeRefComponent type = definition.addType();
		type.setCodeSimple("Extension");
		type.setProfileSimple("#" + attribute.getName());
	}
	
	public void processNonExtendedType(UmlClass typeClass, ElementDefinitionComponent definition) {
		String typeName = typeClass.getName();
		TypeRefComponent type = definition.addType();
		if(hasNativeFhirType(typeName)) {
			type.setCodeSimple(typeName);
		} else if(hasProfiledFhirType(typeName)) { //TODO: Handle the case where the attribute has tag: profile.fhir.element.type.profile
			type.setCodeSimple(ProfiledFhirType.getFhirTypeName(typeName));
			type.setProfileSimple(QUICK_CORE_PROFILE_PATH + ProfiledFhirType.getFhirTypeName(typeName));
		}else  {
			type.setProfileSimple(QUICK_CORE_PROFILE_PATH + typeName);
		}
	}
	
	/*******************************************
	 * Helper Methods
	 *******************************************/
	
	/**
	 * Returns true if the type of the attribute argument is a core FHIR type.
	 * 
	 * @param attribute The attribute whose type we are checking.
	 * @return
	 */
	public boolean hasNativeFhirType(UmlProperty attribute) {
		return hasNativeFhirType(attribute.getFirstType().getName());
	}
	
	/**
	 * Returns true if the type of the attribute argument is a core FHIR type.
	 * 
	 * @param attribute The attribute whose type we are checking.
	 * @return
	 */
	public boolean hasNativeFhirType(String name) {
		return FhirType.contains(name);
	}
	
	/**
	 * Returns true if the type of the attribute argument is a profiled FHIR type.
	 * 
	 * TODO: Handle the case where the attribute has tag: profile.fhir.element.type.profile
	 * 
	 * @param attribute The attribute whose type we are checking.
	 * @return
	 */
	public boolean hasProfiledFhirType(UmlProperty attribute) {
		return hasProfiledFhirType(attribute.getFirstType().getName());
	}
	
	/**
	 * Returns true if the type of the attribute argument is a profiled FHIR type.
	 * 
	 * TODO: Handle the case where the attribute has tag: profile.fhir.element.type.profile
	 * 
	 * @param name The attribute whose type we are checking.
	 * @return
	 */
	public boolean hasProfiledFhirType(String name) {
		return ProfiledFhirType.contains(name) || ProfiledFhirType.getFhirResourceForQuickClass(name) != null;
	}
	
	/**
	 * Method marshals FHIR Profile as an XML string.
	 * 
	 * @return
	 */
	public static String generateProfileAsXml(Profile profile ) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmlComposer composer = new XmlComposer();
		try {
			composer.compose(baos, profile, true);
		} catch(Exception e) {
			throw new RuntimeException("Error marshalling profile to XML", e);
		}
		return new String(baos.toByteArray());
	}
}
