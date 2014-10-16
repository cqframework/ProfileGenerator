package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FhirAnnotationTest {
	
	private String tag1 = "profile.fhir.element.split.1.type";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPrefixAsString() {
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		String prefix = annotation.getPrefixAsString(5);
		assertEquals("profile.fhir.element.split.1", prefix);
		assertNotEquals("profile.fhir.element.extension", prefix);
	}
	
	@Test
	public void testRemoveTokens() {
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertEquals("profile.element.type", annotation.remove(1,3,4).toString());
	}
	
	@Test
	public void testToString() {
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertEquals("profile.fhir.element.split.1.type", annotation.toString());
	}
	
	@Test
	public void testIsPrefix() {
		
		//Exact match is valid prefix
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertTrue(annotation.isPrefix(new FhirAnnotation(tag1)));
		
		//Formal prefix is valid prefix
		assertTrue(annotation.isPrefix(new FhirAnnotation("profile.fhir.element")));
		
		//Invalid prefix should fail
		assertFalse(annotation.isPrefix(new FhirAnnotation("profile.quick.element")));
		
	}
	
	@Test
	public void testIsStringPrefix() {
		
		//Exact match is valid prefix
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertTrue(annotation.isPrefix(tag1));
		
		//Formal prefix is valid prefix
		assertTrue(annotation.isPrefix("profile.fhir.element"));
		
		//Invalid prefix should fail
		assertFalse(annotation.isPrefix("profile.quick.element"));
		
	}
	
	@Test
	public void testGetSuffix() {
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertEquals(new FhirAnnotation("1.type"), annotation.getSuffix(2));
	}
	
	@Test
	public void testEquality() {
		FhirAnnotation annotation = new FhirAnnotation(tag1);
		assertEquals(annotation, new FhirAnnotation(tag1));
		assertTrue(annotation.equals(new FhirAnnotation(tag1)));
		assertNotEquals(new FhirAnnotation(tag1), new FhirAnnotation("some.thing.else"));
	}

}
