package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.socraticgrid.uml.TaggedValue;
import org.socraticgrid.uml.UmlProperty;

public class FhirAttributeTest {
	
	private UmlProperty attribute;

	@Before
	public void setUp() throws Exception {
		attribute = new UmlProperty("Test");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRequiresSplittingTrue() {
		attribute.addTag(new TaggedValue("profile.fhir.element.split.1", "true"));
		assertTrue(FhirAttributeAnnotationHandler.requiresSplitting(attribute));
	}
	
	@Test
	public void testRequiresSplittingFalse() {
		attribute.addTag(new TaggedValue("profile.fhir.element.hello.1", "true"));
		assertFalse(FhirAttributeAnnotationHandler.requiresSplitting(attribute));
	}

}
