package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class ProfileConfigurationLoaderTest {
	
	private ProfileConfigurationLoader loader;

	@Before
	public void setUp() throws Exception {
		loader = new ProfileConfigurationLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadProfileConfigurations() {
		ProfileMetadata configuration = loader.loadProfileConfigurations();
		println configuration
	}

}
