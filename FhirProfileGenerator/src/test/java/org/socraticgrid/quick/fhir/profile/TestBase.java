package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.socraticgrid.eap.xmi.reader.UmlModelLoader;
import org.socraticgrid.uml.UmlModel;

public abstract class TestBase {
	
	protected FhirProfileGenerator profileGenerator;
	protected UmlModel model;

	@Before
	public void setup() {
		try {
			UmlModelLoader loader = new UmlModelLoader();
			model = loader.loadModelFromClassPath("/xmi/QUICK.xmi");
			profileGenerator = new FhirProfileGenerator();
		} catch(Exception e) {
			fail();
		}
	}

}
