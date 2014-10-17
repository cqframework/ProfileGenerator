package org.socraticgrid.quick.fhir.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.socraticgrid.uml.TaggedValue;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlModel;
import org.socraticgrid.uml.UmlProperty;

/**
 * Static utility class that handles specifically FHIR annotations on UML properties.
 * 
 * FHIR attribute annotations, at this time, are dot '.' delimited composite strings
 * such as 'profile.fhir.element.type'.
 * 
 * @author cnanjo
 *
 */
public class FhirAttributeAnnotationHandler {
	
	/**
	 * Method returns true if the attribute should not appear in the FHIR profile.
	 * 
	 * @param attribute
	 * @return
	 */
	public static boolean skip(UmlProperty attribute) {
		return attribute.getTaggedValueAsBoolean("profile.fhir.element.skip");
	}
	
	/**
	 * Method returns true if the attribute maps exactly to its equivalent in FHIR.
	 * 
	 * @param attribute
	 * @return
	 */
	public static boolean hasExactFhirEquivalent(UmlProperty attribute) {
		return attribute.getTaggedValueAsBoolean("profile.fhir.element.sameas");
	}
	
	/**
	 * Method returns true if a tagged value exists with key: 
	 * profile.fhir.element.extension_base
	 * 
	 * @param attribute
	 * @return
	 */
	public static boolean hasTargetContext(UmlProperty attribute) {
		return attribute.getTaggedValueByKey("profile.fhir.element.target_context") != null;
	}
	
	/**
	 * Method returns the base extension for this attribute or null if none exists.
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getTargetContext(UmlProperty attribute) {
		return attribute.getTaggedValueAsString("profile.fhir.element.target_context");
	}
	
	/**
	 * Method returns the base extension for this attribute or null if none exists.
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getSourceContext(UmlProperty attribute) {
		return attribute.getTaggedValueAsString("profile.fhir.element.source_context");
	}
	
	/**
	 * Returns true if attribute has no FHIR equivalent and requires a FHIR extension
	 * 
	 * @return
	 */
	public static boolean requiresFhirExtension(UmlProperty attribute) {
		return attribute.getTaggedValueAsBoolean("profile.fhir.element.extension");
	}
	
	public static String getElementSource(UmlProperty attribute) {//TODO Rename to path or sourcePath?
		return attribute.getTaggedValueAsString("profile.fhir.element.source");
	}
	
	public static boolean requiresSplitting(UmlProperty attribute) {
		boolean found = false;
		for(TaggedValue tag : attribute.getTags()) {
			FhirAnnotation annotation = new FhirAnnotation(tag.getKey());
			if(annotation.isPrefix("profile.fhir.element.split")) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public static Collection<UmlProperty> split(UmlProperty attribute, UmlModel model) {
		List<TaggedValue> properties = new ArrayList<TaggedValue>();
		properties.addAll(attribute.getTags());
		Map<String, UmlProperty> splitGroup = new HashMap<String, UmlProperty>();
		for(TaggedValue tag : properties) {
			FhirAnnotation annotation = new FhirAnnotation(tag.getKey());
			if(annotation.isPrefix("profile.fhir.element.split")) {
				String key = annotation.getPrefixAsString(5);
				String tail = annotation.getSuffix(1).toString();
				UmlProperty property = splitGroup.get(key);
				if(property == null) {
					property = attribute.clone();
					property.getTags().clear();
					splitGroup.put(key, property);
				}
				if(tail.equalsIgnoreCase("target")) {
					property.setName(tag.getValue());
				}
				else if(tail.equalsIgnoreCase("target_context")) {
					property.addTag(new TaggedValue("profile.fhir.element.target_context", tag.getValue()));
				}
				else if(tail.equalsIgnoreCase("type")) { //TODO Be consistent as to whether case is relevant or not
					property.addTag(new TaggedValue("profile.fhir.element.type", tag.getValue()));
					property.addType((UmlClass)model.getObjectByName(tag.getValue()));
				}
				else if(tail.equalsIgnoreCase("source")) {
					property.addTag(new TaggedValue("profile.fhir.element.source", tag.getValue()));
				}
				else if(tail.equalsIgnoreCase("source_context")) {
					property.addTag(new TaggedValue("profile.fhir.element.source_context", tag.getValue()));
				}
				else if(tail.equalsIgnoreCase("source_type")) {
					//TODO Handle
				}
				else if(tail.equalsIgnoreCase("extension")) {
					property.addTag(new TaggedValue("profile.fhir.element.extension", "true"));
				}
				else if(tail.equalsIgnoreCase("cardinality_low")) {
					property.addTag(new TaggedValue("profile.fhir.element.cardinality.low", tag.getValue()));
					property.setLow(Integer.parseInt(tag.getValue()));
				}
				else if(tail.equalsIgnoreCase("cardinality_high")) {
					property.addTag(new TaggedValue("profile.fhir.element.cardinality.high", tag.getValue()));
					if(tag.getValue().equals("*")) {
						property.setHigh(-1);
					} else {
						property.setHigh(Integer.parseInt(tag.getValue()));
					}
				}
			}
		}
		return splitGroup.values();
	}
}
