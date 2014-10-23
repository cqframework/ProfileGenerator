package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.socraticgrid.uml.CardinalityRange;
import org.socraticgrid.uml.OneToOnePropertyMapping;
import org.socraticgrid.uml.TaggedValue;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlProperty;

/**
 * The FHIR Class Annotation Handler handles all FHIR UML class-level annotations.
 * 
 * Note that, in order to not alter the underlying UML model, the FhirClassAnnotationHandler
 * clones the UML class' properties so that they may be annotated as necessary for the generation
 * of a particular profile.
 * 
 * Unlike the FhirAttributeAnnotationHandler, this class maintains state and is therefore not a
 * static utility class.
 * 
 * @author Claude Nanjo
 *
 */
public class FhirClassAnnotationHandler {
	
	/**
	 * The UML class we are processing.
	 */
	private final UmlClass umlClass;
	private UmlClass targetResource;
	
	/**
	 * A namespace qualified property index. The index key consists of:
	 * owningClass.attributeName. The value of the index is the attribute itself.
	 * Note that a namespace qualified key is required to prevent attribute name
	 * collisions (e.g., two superclasses have a 'code' attribute).
	 * 
	 */
	private Map<String, UmlProperty> indexedProperties = new HashMap<String, UmlProperty>();
	
	/**
	 * The set of properties for this class to exclude from consideration during processing.
	 */
	private List<UmlProperty> subtractedProperties = new ArrayList<UmlProperty>();
	
	/**
	 * Holds all attributes for the given class including any attributes originating from
	 * zero or more superclasses.
	 * 
	 */
	private List<UmlProperty> collectedProperties = new ArrayList<UmlProperty>();
	
	/**
	 * Constructors a class annotator for the class passed in as an argument.
	 * 
	 * @param umlClass
	 */
	public FhirClassAnnotationHandler(UmlClass umlClass) {
		this.umlClass = umlClass;
	}
	
	/**
	 * Constructors a class annotator for the class passed in as an argument and the
	 * target class specified.
	 * 
	 * @param umlClass
	 */
	public FhirClassAnnotationHandler(UmlClass umlClass, UmlClass target) {
		this(umlClass);
		this.targetResource = target;
	}
	
	/**
	 * Initialization method that:
	 * 1. build indexes
	 * 2. preprocesses attributes
	 * 
	 * This method must be called before the FhirClassAnnotationHandler can be used. This logic
	 * was not placed in the constructor as any of these steps may trigger an exception.
	 */
	public void initialize() {
		collectAndIndexProperties();
		handleAttributeSplitting();
		processClassScopedAttribute();
		processClassLevelElementTags();
	}
	
	/**
	 * Processes each attribute for the given class and defines the mapping(s) to FHIR
	 * based on the annotation associated with this property.
	 * 
	 * @return
	 */
	public List<OneToOnePropertyMapping> initializeMappings() {
		collectAndIndexProperties();
		processClassScopedAttributeNew();
		return generateMappingsForClassProperties();
	}
	
	/**
	 * Generates and collects the FHIR mappings for the relevant UML properties
	 * for the class.
	 */
	public List<OneToOnePropertyMapping> generateMappingsForClassProperties() {
		List<OneToOnePropertyMapping> mappings = new ArrayList<>();
		FhirTagParseErrorListener errorListener = new FhirTagParseErrorListener();//TODO For now, use a single error listener for all mappings.
		for(UmlProperty property : getCollectedProperties()) {
			mappings.addAll(MappingAnnotationListener.getPropertyMappings(property,targetResource, errorListener));
		}
		if(errorListener.hasErrors()) {
			throw new RuntimeException("Error processing mapping tags. " + errorListener.getParseErrorCount() + " error(s) found. See logs for details.");
		}
		return mappings;
	}
	
	/**
	 * Convenience method that collects all properties from this 
	 * class and any superclass and indexes these properties.
	 */
	public void collectAndIndexProperties() {
		umlClass.buildTaggedValueIndex();
		indexPropertiesByQualifiedName(true);
	}
	
	public void indexPropertiesByQualifiedName(boolean subtactRemovedProperties) {
		indexedProperties.clear();
		collectProperties(subtactRemovedProperties);
		for(UmlProperty property: getCollectedProperties()) {
			String context = getAttributeSourceContext(property);
			if(context == null) {
				context = property.getSource().getName();
			}
			indexedProperties.put(context + "." + property.getName(), property);
		}
	}
	
	/**
	 * Collect all properties for this class whether defined in the class itself or
	 * whether they are defined in one of its superclasses.
	 * 
	 * Note that this method supports multi-inheritance
	 * 
	 * @param subtactRemovedProperties
	 */
	public void collectProperties(boolean subtactRemovedProperties) {
		List<UmlProperty> union = new ArrayList<UmlProperty>();
		collectProperties(union, umlClass);
		if(subtactRemovedProperties) {
			union.removeAll(subtractedProperties);
		}
		collectedProperties.clear();
		collectedProperties.addAll(union);
	}
	
	private void collectProperties(List<UmlProperty> union, UmlClass target) {
		for(UmlProperty attribute : target.getProperties()) {
			UmlProperty clone = attribute.clone();
			clone.buildTaggedValueIndex();
			union.add(clone);
		}
		for(UmlClass parent : target.getGeneralizations()) {
			collectProperties(union, parent);
		}
	}
	
	public void handleAttributeSplitting() {
		List<UmlProperty> union = new ArrayList<UmlProperty>();
		List<UmlProperty> properties = getCollectedProperties();
		Iterator<UmlProperty> propertyIterator = properties.iterator();
		while(propertyIterator.hasNext()) {
			UmlProperty property = propertyIterator.next();
			if(FhirAttributeAnnotationHandler.requiresSplitting(property)) {
				Collection<UmlProperty> splitProperties = FhirAttributeAnnotationHandler.split(property, umlClass.getModel());
				union.addAll(splitProperties);
				if(FhirAttributeAnnotationHandler.skip(property)) {
					propertyIterator.remove();
				}
			}
		}
		for(UmlProperty attribute : union) {
			attribute.buildTaggedValueIndex();
		}
		System.out.println(union);
		getCollectedProperties().addAll(union);
	}
	
	/**
	 * Overrides cloned property-level tags with class-level tags.
	 * Note: Original property in UML model is not affected.
	 * <p>
	 * Precondition: Tag format is profile.fhir.element.attribute_name...
	 * where attribute_name corresponds to the name of an existing attribute
	 * for this class.
	 * 
	 * Postcondition: Class attribute with name attribute_name will:
	 * <ol>
	 * <li> have a new tag added without the attribute_name part if 
	 * none exist for that key.</li>
	 * <li> have the new tag without the attribute_name part override 
	 * the existing tag if such a tag exists.</li>
	 * </ol>
	 * 
	 */
	public void processClassScopedAttribute() {
		List<TaggedValue> tags = umlClass.getTags();
		for(TaggedValue tag: tags) {
			if(tag.getKey().startsWith("profile.fhir.element")) {
				FhirAnnotation annotation = new FhirAnnotation(tag.getKey());
				String context = annotation.get(3);
				String attributeName = annotation.get(4);
				UmlProperty attribute = getPropertyWithName(context + "." + attributeName);
				if(attribute == null) {
					throw new RuntimeException("Error! Property " + context + "." + attributeName + " does not exist in class " + umlClass.getName());
				}
				attribute.replaceTag(new TaggedValue(annotation.remove(3,4).toString(), tag.getValue()));
			}
		}
	}
	
	/**
	 * Note changes:
	 * 2. clear tags
	 * 3. Readd tag without truncating it.
	 */
	public void processClassScopedAttributeNew() {//TODO Replace others with this one
		List<TaggedValue> tags = umlClass.getTags();
		for(TaggedValue tag: tags) {
			if(!validateTag(tag)) {
				throw new RuntimeException(tag + " has an invalid syntax!");
			}
			if(tag.getKey().startsWith("profile.fhir.element")) {
				FhirAnnotation annotation = new FhirAnnotation(tag.getKey());
				String context = annotation.get(3);
				String attributeName = annotation.get(4);
				UmlProperty attribute = getPropertyWithName(context + "." + attributeName);
				if(attribute == null) {
					throw new RuntimeException("Error! Property " + context + "." + attributeName + " does not exist in class " + umlClass.getName());
				}
				attribute.getTags().clear();//override original tags - need to fix to only remove specific tags
				attribute.addTag(tag);//adding class-level tag to property
			}
		}
	}
	
	/**
	 * The name of class attributes that are inherited by a  QUICK subclass
	 * may correspond to FHIR attributes whose names vary based on the FHIR
	 * resource context. In such cases, these attributes specify their target 
	 * not at the attribute level, but rather at the class level.
	 * 
	 * Method replaces superclass attribute with a clone of the attribute specified by the
	 * class-level tags (in addition to the attribute level tags). Class level
	 * tags with the same key as attribute-level tags, trump.
	 * 
	 * @param fhirClass
	 */
	public void processClassLevelElementTags() {//TODO merge with processClassScopedAttribute and then remove
		for(TaggedValue tag: umlClass.getTags()) {
			if(tag.getKey().contains("profile.fhir.element")) {
				String[] components = tag.getKey().split("\\.");
				UmlProperty property = getPropertyWithName(components[3]+ "." + components[4]);
				if(property == null) {
					throw new RuntimeException("Error! Property " + components[3]+ "." + components[4] + " does not exist in class " + umlClass.getName());
				}
				//FhirAttribute clonedProperty = property.clone();
				//clonedProperty.addMapping(property);
				//fhirClass.subtractProperty(property);
				//fhirClass.getProperties().add(clonedProperty);
				if(components[5].equals("target")) {
					//clonedProperty.setName(tag.getValue());
					property.addAliasForScope(umlClass.getName(), tag.getValue());
				}
				if(components[5].equals("cardinality")) {
					CardinalityRange cardinalityConstraint = new CardinalityRange();
					if(components[5].equals("low")) {
						cardinalityConstraint.setLow(Integer.valueOf(tag.getValue()));
					}
					if(components[6].equals("high")) {
						cardinalityConstraint.setHigh(Integer.valueOf(tag.getValue()));
					}
					property.addCardinalityConstraint(umlClass.getName(), cardinalityConstraint);
				}
			}
		}
	}
	
	public String getAttributeSourceContext(UmlProperty property) {
		String sourceContext = FhirAttributeAnnotationHandler.getSourceContext(property);
		if(sourceContext == null) {
			sourceContext = property.getSource().getName();
		}
		return sourceContext;
	}
	
	public String getAttributeTargetContext(UmlProperty property) {
		String targetContext = FhirAttributeAnnotationHandler.getTargetContext(property);
		if(targetContext == null) {
			targetContext = getStructureType();
		}
		return targetContext;
	}
	
	public boolean validateTag(TaggedValue tag) {
		boolean success = true;
		FhirTagParseErrorListener errorListener = new FhirTagParseErrorListener();
		FhirTagParser parser = MappingAnnotationListener.setUpParser(tag.toString(), errorListener);
		parser.handleTagRule();
		if(errorListener.hasErrors()) {
			success = false;
		}
		return success;
	}
	
	public String getUmlClassName() {
		return umlClass.getName();
	}
	
	public Map<String, UmlProperty> getIndexedProperties() {
		return indexedProperties;
	}

	public List<UmlProperty> getSubtractedProperties() {
		return subtractedProperties;
	}
	
	public List<UmlProperty> getCollectedProperties() {
		return collectedProperties;
	}

	public void setCollectedProperties(List<UmlProperty> collectedProperties) {
		this.collectedProperties = collectedProperties;
	}
	
	public String getStructureType() {
		return umlClass.getTaggedValueAsString("profile.fhir.structure.type");
	}
	
	public String getStructureName() {
		return umlClass.getTaggedValueAsString("profile.fhir.structure.name");
	}
	
	public boolean getStructurePublishFlag() {
		return umlClass.getTaggedValueAsBoolean("profile.fhir.structure.publish");
	}
	
	public String getStructurePurpose() {
		return umlClass.getTaggedValueAsString("profile.fhir.structure.purpose");
	}
	
	public UmlClass getUmlClass() {
		return umlClass;
	}
	
	public UmlClass getTargetResource() {
		return targetResource;
	}
	
	public void setTargetResource(UmlClass targetResource) {
		this.targetResource = targetResource;
	}
	
	/*******************************************
	 * CLASS PREPROCESSING RULES
	 *******************************************/
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UmlProperty getPropertyWithName(String name) {
		return indexedProperties.get(name);
	}
	
	/**
	 * Flags a property for removal when collecting properties from the class.
	 * 
	 * @param attribute
	 */
	public void subtractProperty(UmlProperty attribute) {
		subtractedProperties.add(attribute);
	}
}
