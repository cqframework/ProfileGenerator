package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FhirAnnotationParserTest {
	
	private FhirAnnotationParser parser;
	private String tag1 = "profile.fhir.element.type";

	@Before
	public void setUp() throws Exception {
		parser = new FhirAnnotationParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() {
		FhirAnnotation annotation = parser.parse(tag1);
		assertNotNull(annotation);
		assertEquals(4, annotation.size());
		assertEquals("profile", annotation.get(0));
		assertEquals("fhir", annotation.get(1));
		assertEquals("element", annotation.get(2));
		assertEquals("type", annotation.get(3));
		assertTrue(annotation.contains("element"));
		assertFalse(annotation.contains("elements"));
	}

}
