package org.socraticgrid.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MavenUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetResourceAsUrl() {
		try {
			URL url = MavenUtils.getResourceAsUrl("/xsd/fhir-single.xsd");
			assertTrue(true);
			assertNotNull(url);
			assertEquals("file", url.getProtocol());
			File file = new File(url.getPath());
			assertTrue(file.exists() && file.isFile());
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
