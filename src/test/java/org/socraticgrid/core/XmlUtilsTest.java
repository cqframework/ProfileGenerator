package org.socraticgrid.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class XmlUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDocument() {
		Document document = XmlUtils.getDocument("<root><comment>hello world</comment></root>");
		assertNotNull(document);
		
		try {
			document = XmlUtils.getDocument("<root><comment>hello world</comment></error>");
			fail();
		} catch(Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testValidate() {
		URL url = MavenUtils.getResourceAsUrl("/xsd/fhir-single.xsd");
		assertTrue(XmlUtils.validate(url, "<Alert xmlns='http://hl7.org/fhir'><status value='active'/><subject><reference value='Patient/example'/><display value='Peter Patient'/></subject><note value='Some notes'/></Alert>"));
	}

}
