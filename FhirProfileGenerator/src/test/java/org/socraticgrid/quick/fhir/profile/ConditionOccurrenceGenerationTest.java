package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Profile.ConstraintComponent;
import org.hl7.fhir.instance.model.Profile.ElementComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionMappingComponent;
import org.hl7.fhir.instance.model.Profile.ProfileExtensionDefnComponent;
import org.hl7.fhir.instance.model.Profile.ProfileStructureComponent;
import org.hl7.fhir.instance.model.Profile.TypeRefComponent;
import org.junit.Test;
import org.socraticgrid.uml.UmlClass;

/**
 * Test for the generation of the ConditionOccurrence FHIR profile.
 * Please keep underlying QUICK.xmi file  and test current to reflect model changes.
 * 
 * @author cnanjo
 *
 */
public class ConditionOccurrenceGenerationTest extends TestBase {
	
	@Test
	public void testGenerateConditionOccurrenceProfile() {
		UmlClass conditionOccurrence = (UmlClass)model.getObjectByName("ConditionOccurrence");
		Profile profile = profileGenerator.generateSimpleProfile(conditionOccurrence, new UmlClass("Condition"));
		System.out.println(FhirProfileGenerator.generateProfileAsXml(profile));//Helpful for debugging failing tests
		testExtensions(profile);
		testStructure(profile);
	}
	
	public void testExtensions(Profile profile) {
		List<ProfileExtensionDefnComponent> extensions = profile.getExtensionDefn();
		assertEquals(8, extensions.size());
		for(ProfileExtensionDefnComponent extension : extensions) {
			assertExtensionDefinition(extension);
		}
	}
	
	//TODO Refine at a later date. For now, only assert presence.
	public void assertExtensionDefinition(ProfileExtensionDefnComponent extension) {
		ElementComponent element = extension.getElement().get(0);
		ElementDefinitionComponent definition = element.getDefinition();
		assertNotNull(extension.getName());
		assertNotNull(extension.getDisplay());
		assertNotNull(extension.getContextType());
		assertNotNull(extension.getContext());
		assertNotNull(element.getPath());
		assertNotNull(element.getName());
		assertTrue(definition.getType().size() > 0);
		assertNotNull(definition.getMin());
		assertNotNull(definition.getMax());
		assertNotNull(definition.getFormal());
	}
	
	public void testStructure(Profile profile) {
		List<ProfileStructureComponent> structures = profile.getStructure();
		assertEquals(1, structures.size());
		ProfileStructureComponent structure = structures.get(0);
		testElements(structure);
	}
	
	/**
	 * 24 attributes + 1 root element + modifier extension constraint
	 * TODO add for dependent structures as well.
	 * @param structure
	 */
	public void testElements(ProfileStructureComponent structure) {
		ConstraintComponent differential = structure.getDifferential();
		List<ElementComponent> elements = differential.getElement();
		assertEquals(30, elements.size());
		Map<String,ElementComponent> elementMap = new HashMap<String,ElementComponent>();
		for(ElementComponent item : elements) {
			ElementComponent exists = elementMap.get(item.getName());
			if(exists != null) {
				elementMap.put(item.getName()+".2", item);//TODO Fix
			} else {
				elementMap.put(item.getName(), item);
			}
		}
		testRootElement(elementMap);
		testModifierExtensionConstraint(elementMap);
		testNonExtendedAttributes(elementMap);
		testTransformationForEquivalentFields(elementMap);
		testTransformationForInheritedButEquivalentFields(elementMap);
		testTransformationForFhirExtensions(elementMap);
		testTransformationForEffectiveDate(elementMap);
		testTransformationForLocation(elementMap);
	}
	
	public void testRootElement(Map<String, ElementComponent> elementMap) {
		ElementComponent root = elementMap.get("ConditionOccurrence");
		assertNotNull(root);
		ElementDefinitionComponent definition = root.getDefinition();
		List<TypeRefComponent> types = definition.getType();
		assertEquals(1, types.size());
		assertEquals("Resource", types.get(0).getCode());
		List<ElementDefinitionMappingComponent> mappings = definition.getMapping();
		assertEquals(1, mappings.size());
		ElementDefinitionMappingComponent mapping = mappings.get(0);
		assertEquals("ConditionOccurrence", mapping.getMap());
		assertEquals(1, definition.getMin());
		assertEquals("1", definition.getMax());
	}
	
	public void testModifierExtensionConstraint(Map<String, ElementComponent> elementMap) {
		ElementComponent modifierExtension = elementMap.get("modifierExtension");
		assertNotNull(modifierExtension);
		ElementDefinitionComponent definition = modifierExtension.getDefinition();
		assertEquals(0, definition.getMin());
		assertEquals("0", definition.getMax());
	}
	
	public void testNonExtendedAttributes(Map<String, ElementComponent> elementMap) {
		assertFullDefinition(elementMap.get("asserter"), "asserter", "Condition.asserter", "ClinicalStatement.statementAuthor",0,"1");
		HashMap<String,String> asserterMap = new HashMap<>();
		asserterMap.put("Patient", "http://hl7.org/fhir/Profile/Patient-quick-Patient");
		asserterMap.put("Practitioner", "http://hl7.org/fhir/Profile/Practitioner-quick-Practitioner");
		assertMultipleProfiledType(asserterMap, elementMap.get("asserter"));
		assertFullDefinition(elementMap.get("dateAsserted"), "dateAsserted", "Condition.dateAsserted", "Observation.observedAtTime",0,"1");
		testSimpleType(elementMap.get("dateAsserted"), "date");
		assertFullDefinition(elementMap.get("location"), "location", "Condition.location", "Condition.location",0,"*");
		//testTypeProfile(elementMap.get("location"), "http://hl7.org/fhir/Profile/BodySite");
	}
	
	public void testTransformationForEquivalentFields(Map<String, ElementComponent> elementMap) {
		assertFullDefinition(elementMap.get("code"), "code", "Condition.code", "Condition.code", 1,"1");
		assertFullDefinition(elementMap.get("category"), "category", "Condition.category", "Condition.category", 0, "1");
		assertFullDefinition(elementMap.get("status"), "status", "Condition.status", "Condition.status", 1, "1");
		assertFullDefinition(elementMap.get("certainty"), "certainty", "Condition.certainty", "Condition.certainty", 0, "1");
		assertFullDefinition(elementMap.get("severity"), "severity", "Condition.severity", "Condition.severity", 0, "1");
	}
	
	public void testTransformationForInheritedButEquivalentFields(Map<String, ElementComponent> elementMap) {
		assertFullDefinition(elementMap.get("identifier"), "identifier", "Condition.identifier", "ClinicalStatement.identifier",0, "*");
		assertFullDefinition(elementMap.get("subject"), "subject", "Condition.subject", "ClinicalStatement.subject", 1, "1");
		assertSingleProfiledType(elementMap, "subject", "http://hl7.org/fhir/Profile/Patient-quick-Patient", "Patient");
		assertFullDefinition(elementMap.get("encounter"), "encounter", "Condition.encounter", "ClinicalStatement.encounter",0,"1");
		assertSingleProfiledType(elementMap, "encounter", "http://hl7.org/fhir/Profile/Encounter-quick-Encounter", "Encounter");
	}

	public void testSimpleType(ElementComponent element, String code) {
		TypeRefComponent type = element.getDefinition().getType().get(0);
		assertEquals(code, type.getCode());
	}
	
	public void testTypeProfile(ElementComponent element, String profile) {
		TypeRefComponent type = element.getDefinition().getType().get(0);
		assertEquals(profile, type.getProfile());
	}
	
	public void assertSingleProfiledType(Map<String, ElementComponent> elementMap, String name, String profile, String code) {
		ElementComponent element = elementMap.get(name);
		List<TypeRefComponent> types = element.getDefinition().getType();
		assertEquals(profile, types.get(0).getProfile());
		assertEquals(code, types.get(0).getCode());
	}
	
	public void assertMultipleProfiledType(Map<String,String> typeMap, ElementComponent element) {
		List<TypeRefComponent> types = element.getDefinition().getType();
		assertEquals(types.size(), typeMap.size());
		for(TypeRefComponent type : types) {
			String code = type.getCode();
			String profile = type.getProfile();
			assertNotNull(typeMap.get(code));
			if(typeMap.containsKey(code)) {
				assertEquals(profile, typeMap.get(code));
			}
		}
	}
	
	public void assertFullDefinition(ElementComponent element, String name, String path, String mapping, int low, String high) {
		assertNotNull(element);
		assertEquals(name, element.getName());
		assertEquals(path, element.getPath());
		assertEquals(low, element.getDefinition().getMin());
		assertEquals(high, element.getDefinition().getMax());
		assertNotNull(element.getDefinition().getFormal());
		assertTrue(element.getDefinition().getMapping().size() == 1);
		assertMapping(element, mapping);
	}
	
	public void assertMapping(ElementComponent element, String path) {
		assertEquals(path, element.getDefinition().getMapping().get(0).getMap());
		assertEquals("quick", element.getDefinition().getMapping().get(0).getIdentity());
	}
	
	public void testTransformationForFhirExtensions(Map<String, ElementComponent> elementMap) {
		assertExtensionElementDefinition(elementMap.get("recordedOn"), "recordedOn", "ClinicalStatement.statementDateTime");
		assertExtensionElementDefinition(elementMap.get("criticality"), "criticality", "Condition.criticality");
		assertExtensionElementDefinition(elementMap.get("contributionToDeath"), "contributionToDeath", "Condition.contributionToDeath");
		assertExtensionElementDefinition(elementMap.get("profileId"), "profileId", "ClinicalStatement.profileId");
		assertExtensionElementDefinition(elementMap.get("statementSource"), "statementSource", "ClinicalStatement.statementSource");
	}
	
	public void assertExtensionElementDefinition(ElementComponent element, String name, String quickPath) {
		assertEquals(name, element.getName());
		assertEquals("Condition.extension", element.getPath());
		assertEquals("Extension", element.getDefinition().getType().get(0).getCode());
		assertEquals("#" + name, element.getDefinition().getType().get(0).getProfile());
		assertMapping(element, quickPath);
	}
	
	public void testTransformationForEffectiveDate(Map<String, ElementComponent> elementMap) {
		assertNotNull(elementMap.get("onsetDate"));
		assertNotNull(elementMap.get("abatementDate"));
		assertNull(elementMap.get("effectiveDate"));
	}
	
	public void testTransformationForLocation(Map<String, ElementComponent> elementMap) {
		ElementComponent locationLaterality = elementMap.get("location.laterality");
		ElementComponent locationDirectionality = elementMap.get("location.directionality");
		
		assertNotNull(locationLaterality);
		assertNotNull(locationDirectionality);
		
		testLocationCodeGeneration(elementMap);
		testLocationLateralityGeneration(elementMap);
		testLocationDirectionalityGeneration(elementMap);
	}
	
	public void testLocationCodeGeneration(Map<String, ElementComponent> elementMap) {
		ElementComponent locationCode = elementMap.get("location.code");
		assertNotNull(locationCode);
		ElementDefinitionComponent definition = locationCode.getDefinition();
		List<TypeRefComponent> types = definition.getType();
		assertEquals(1, types.size());
		assertEquals("CodeableConcept", types.get(0).getCode());
		List<ElementDefinitionMappingComponent> mappings = definition.getMapping();
		assertEquals(1, mappings.size());
		ElementDefinitionMappingComponent mapping = mappings.get(0);
		assertEquals("Condition.location.anatomicalLocation", mapping.getMap());
	}
	
	public void testLocationLateralityGeneration(Map<String, ElementComponent> elementMap) {
		ElementComponent locationCode = elementMap.get("location.laterality");
		assertNotNull(locationCode);
		assertEquals("Condition.location.extension", locationCode.getPath());
		assertEquals("location.laterality", locationCode.getName());
		ElementDefinitionComponent definition = locationCode.getDefinition();
		List<TypeRefComponent> types = definition.getType();
		assertEquals(1, types.size());
		assertEquals("Extension", types.get(0).getCode());
		assertEquals("#location.laterality", types.get(0).getProfile());
		List<ElementDefinitionMappingComponent> mappings = definition.getMapping();
		assertEquals(1, mappings.size());
		ElementDefinitionMappingComponent mapping = mappings.get(0);
		assertEquals("Condition.location.laterality", mapping.getMap());
	}
	
	public void testLocationDirectionalityGeneration(Map<String, ElementComponent> elementMap) {
		ElementComponent locationCode = elementMap.get("location.directionality");
		assertNotNull(locationCode);
		assertEquals("Condition.location.extension", locationCode.getPath());
		assertEquals("location.directionality", locationCode.getName());
		ElementDefinitionComponent definition = locationCode.getDefinition();
		List<TypeRefComponent> types = definition.getType();
		assertEquals(1, types.size());
		assertEquals("Extension", types.get(0).getCode());
		assertEquals("#location.directionality", types.get(0).getProfile());
		List<ElementDefinitionMappingComponent> mappings = definition.getMapping();
		assertEquals(1, mappings.size());
		ElementDefinitionMappingComponent mapping = mappings.get(0);
		assertEquals("Condition.location.directionality", mapping.getMap());
	}
	
}
