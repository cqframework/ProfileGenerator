package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.AliasRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.CardinalityContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.ElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.EquivalentElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.ExtensionContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.HandleElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.HandleTagRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.SkipElementRuleContext;
import org.socraticgrid.uml.OneToOnePropertyMapping;
import org.socraticgrid.uml.TaggedValue;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlProperty;

public class FhirTagParserTest {//TODO Add negative cases
	
	private final String core = "profile.fhir.element.Condition.location.anatomicalLocation=Condition.location.code";
	private final String coreWithParameters = "profile.fhir.element.Condition.location.anatomicalLocation=Condition.location.code(cardinality=1..2, alias=anatLoc, type=CodeableConcept, extension=false)";
	private final String extensionOnly = "profile.fhir.element.Condition.location.laterality=Condition.location.laterality(extension=true)";
	private final String cardinalityUnlimited = "cardinality = 0..*";
	private final String cardinalityOptional = "cardinality = 0..1";
	private final String alias = "alias=anatLoc";
	private final String extension = "extension=false";
	private final String skipElement = "profile.fhir.element.skip";
	private final String skipElementWithBoolean = "profile.fhir.element.skip=true";
	private final String equivalentElement = "profile.fhir.element.ClinicalStatement.identifier.equivalent";
	private final String typeAnnotation = "profile.fhir.element.Condition.location[BodySite].laterality=Condition.location.laterality(cardinality=0..1,type=CodeableConcept,extension=true)";
	private final String badTag1 = "profile.fhir.ClinicalStatement.statementAuthor=Other.author(extension=false)";
	private final String structureName = "profile.fhir.structure.name=CommunicationProposalOccurrence";
	private final String structurePurpose = "profile.fhir.structure.purpose=Represents a clinical statement stating that a communication proposal has been made.";
	private final String bindingExample = "profile.fhir.element.Condition.code=Condition.code(cardinality=1..2, conformance=required, reference=\"http://www.hla.org/aaa/bindings/schemabindings.xml\", extension=false)";
	private final String conformanceRuleExample = "conformance=required";
	private final String referenceRuleExample = "reference=\"http://www.hla.org/aaa/bindings/schemabindings.xml\"";
	private final String bindingRuleExample = "conformance=required,  reference=\"http://www.hla.org/aaa/bindings/schemabindings.xml\"";
	private final String parameterExample = "(cardinality=1..2, conformance=required, reference=\"http://www.hla.org/aaa/bindings/schemabindings.xml\", extension=false)";
	private final String debug1 = "profile.fhir.element.Procedure.procedureCode=DestinationConcept.typo(type=CodeableConcept,extension=true)";
	private final String pathExpression1 = "profile.fhir.element.Procedure.procedureCode=DestinationConcept.procedureCode(type=CodeableConcept,extension=true)";
	
	private final UmlClass targetResource = new UmlClass("Condition");
	private FhirTagParseErrorListener errorListener;

	@Before
	public void setUp() throws Exception {
		errorListener = new FhirTagParseErrorListener();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Sets up property with the following metadata:
	 * 
	 * X.attributeName(0..*)
	 * @return
	 */
	public UmlProperty setUpProperty() {
		UmlProperty property = new UmlProperty("attributeName");
		property.setLow(0);
		property.setHigh(-1);
		property.setSource(new UmlClass("SourceClass"));
		return property;
	}

	@Test
	public void testCore() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(core, errorListener);
		    OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, setUpProperty(), targetResource);
		    assertEquals(0, errorListener.getParseErrorCount());
		    assertEquals("Condition.location.anatomicalLocation", mapping.getSourcePath());
			assertEquals("Condition.location.code", mapping.getDestinationPath());
			assertFalse(mapping.isExtension());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testCoreWithParameters() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(coreWithParameters, errorListener);
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertEquals("anatLoc", mapping.getDestination().getName());
			assertEquals(new Integer(1), mapping.getDestination().getLow());
			assertEquals(new Integer(2), mapping.getDestination().getHigh());
			assertFalse(mapping.isExtension());
			assertEquals("Condition.location.anatomicalLocation", mapping.getSourcePath());
			assertEquals("Condition.location.code", mapping.getDestinationPath());
			assertEquals("CodeableConcept", mapping.getDestination().getFirstType().getName());//TODO support multiple types in UmlProperty
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testTypeAnnotation() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(typeAnnotation, errorListener);
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertEquals(new Integer(0), mapping.getDestination().getLow());
			assertEquals(new Integer(1), mapping.getDestination().getHigh());
			assertEquals("Condition.location[BodySite].laterality", mapping.getSourcePath());
			assertEquals("Condition.location.laterality", mapping.getDestinationPath());
			assertEquals("CodeableConcept", mapping.getDestination().getFirstType().getName());//TODO support multiple types in UmlProperty
			assertTrue(mapping.isExtension());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testExtensionOnly() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(extensionOnly, errorListener);
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertTrue(mapping.isExtension());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testCardinalityUnlimited() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(cardinalityUnlimited, errorListener);
			CardinalityContext context = parser.cardinality();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testCardinalityOptional() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(cardinalityOptional, errorListener);
			CardinalityContext context = parser.cardinality();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testAlias() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(alias, errorListener);
			AliasRuleContext context = parser.aliasRule();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testExtension() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(extension, errorListener);
			ExtensionContext context = parser.extension();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testParameterRule() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(parameterExample, errorListener);
			parser.parameters();
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testBindingConformanceRule() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(conformanceRuleExample, errorListener);
			parser.bindingConformanceRule();
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testBindingReferenceRule() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(referenceRuleExample, errorListener);
			parser.bindingReferenceRule();
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testBindingRule() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(bindingRuleExample, errorListener);
			parser.bindingRule();
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testBindingExample() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(bindingExample, errorListener);
			HandleTagRuleContext context = parser.handleTagRule();
			System.out.println(context.toStringTree());
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertEquals("required", mapping.getBinding().getConformance());
			assertEquals("http://www.hla.org/aaa/bindings/schemabindings.xml", mapping.getBinding().getValueSetUri());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testBadTag1() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(badTag1, errorListener);
			parser.elementRule();
			if(!errorListener.hasErrors()) {
				fail();
			}
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testStructureName() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(structureName, errorListener);
			parser.handleStructureRule();
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testDebug1() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(debug1, errorListener);
			HandleElementRuleContext context = parser.handleElementRule();
			assertFalse(errorListener.hasErrors());
			System.out.println(context.toStringTree(parser));
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testStructurePurpose() { //TODO Revisit why this test works
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(structurePurpose, errorListener);
			System.out.println(parser.structurePurposeRule().toStringTree());
			assertFalse(errorListener.hasErrors());
		} catch(Exception re) {
			fail();
		}
	}
	
	@Test
	public void testSkipElement() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(skipElement, errorListener);
			SkipElementRuleContext context = parser.skipElementRule();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertNull(mapping.getDestination());
			assertTrue(mapping.isUnmapped());
			assertEquals("profile.fhir.element.skip", mapping.getRationale());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testSkipElementWithBoolean() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(skipElementWithBoolean, errorListener);
			SkipElementRuleContext context = parser.skipElementRule();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testEquivalentElement() {
		try {
			FhirTagParser parser = MappingAnnotationListener.setUpParser(equivalentElement, errorListener);
			EquivalentElementRuleContext context = parser.equivalentElementRule();
			OneToOnePropertyMapping mapping = MappingAnnotationListener.retrieveMapping(parser, context, setUpProperty(), targetResource);
			assertEquals(0, errorListener.getParseErrorCount());
			assertNotNull(mapping.getDestination());
			assertEquals("profile.fhir.element.ClinicalStatement.identifier.equivalent", mapping.getRationale());
			assertEquals("attributeName", mapping.getDestination().getName());
			assertEquals(new Integer(0), mapping.getDestination().getLow());
			assertEquals(new Integer(-1), mapping.getDestination().getHigh());
		} catch(RecognitionException re) {
			fail();
		}
	}
	
	@Test
	public void testGetPropertyMappings() {
		UmlProperty property = setUpProperty();
		property.addTag(new TaggedValue("profile.fhir.element.Condition.location.anatomicalLocation","Condition.location.code(cardinality=1..2, alias=anatLoc, type=CodeableConcept, extension=true)"));
		List<OneToOnePropertyMapping> mappings = MappingAnnotationListener.getPropertyMappings(property, targetResource, errorListener);
		assertEquals(0, errorListener.getParseErrorCount());
		assertEquals(1, mappings.size());
		assertEquals("Condition.location.anatomicalLocation", mappings.get(0).getSourcePath());
	}
	
	@Test
	public void testRelativePath() {
		String canonicalPath = "Condition.location.code";
		String relativePath = MappingAnnotationListener.getAttributeRelativePath(canonicalPath);
		assertEquals(0, errorListener.getParseErrorCount());
		assertEquals("location.code", relativePath);
	}
	
	@Test
	public void testGetContainingResource() {
		String canonicalPath = "Condition.location.code";
		String resource = MappingAnnotationListener.getContainingResource(canonicalPath);
		assertEquals(0, errorListener.getParseErrorCount());
		assertEquals("Condition", resource);
	}
	
	@Test
	public void testGetPathExpression() {
		assertEquals("Procedure.procedureCode.value", MappingAnnotationListener.getLHSPathExpression(new TaggedValue("profile.fhir.element.Procedure.procedureCode.value","DestinationConcept.procedureCode(type=CodeableConcept,extension=true)")));
	}
}	
