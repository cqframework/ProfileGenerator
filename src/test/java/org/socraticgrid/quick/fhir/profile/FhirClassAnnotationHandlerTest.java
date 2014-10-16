package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.socraticgrid.eap.xmi.reader.UmlModelLoader;
import org.socraticgrid.uml.OneToOnePropertyMapping;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlModel;
import org.socraticgrid.uml.UmlProperty;

public class FhirClassAnnotationHandlerTest {

	private UmlModel model;
	
	@Before
	public void setup() {
		try {
			UmlModelLoader loader = new UmlModelLoader();
			model = loader.loadModelFromClassPath("/xmi/QUICK.xmi");
		} catch(Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInitializeMappings() {
		UmlClass umlClass = (UmlClass)model.getObjectByName("ConditionOccurrence");
		assertNotNull(umlClass);
		FhirClassAnnotationHandler classHandler = new FhirClassAnnotationHandler(umlClass);
		classHandler.setTargetResource(new UmlClass("Condition"));
		List<OneToOnePropertyMapping> classMappings = classHandler.initializeMappings();
		for(OneToOnePropertyMapping mapping : classMappings) {
			System.out.println(mapping.toString());
		}
		assertEquals(26, classMappings.size());
		int count = 0;
		for(OneToOnePropertyMapping mapping : classMappings) {
			if(mapping.isMapped()) {
				count++;
			}
		}
		assertEquals(24, count);
	}

	@After
	public void tearDown() throws Exception {
	}

}
