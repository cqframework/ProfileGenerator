package org.socraticgrid.eap.xmi.reader;

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.socraticgrid.uml.UmlModel
import org.socraticgrid.uml.UmlPackage

class UmlModelLoaderTest {
	
	def loader

	@Before
	public void setUp() throws Exception {
		loader = new UmlModelLoader()
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadFromFilePath() {
		loader.loadFromStream("/xmi/QUICK.xmi")
	}
	
	@Test
	public void testLoadFromClassPath() {
		assertNotNull(loader.loadModelFromClassPath("/xmi/QUICK.xmi"))
	}

	@Test
	public void testLoadModel() {
		UmlModel model = loader.loadModel(loader.loadFromStream,"/xmi/QUICK.xmi")
		model.buildIndex();
		UmlPackage topLevelPackage = model.getPackages().get(0);
		UmlPackage secondLevelPackage = topLevelPackage.getPackages().get(0);
		UmlPackage thirdLevelPackage = null;
		for(UmlPackage umlPackage: secondLevelPackage.getPackages()) {
			if(umlPackage.getName() == "action") {
				thirdLevelPackage = umlPackage;
			}
		}
		assertEquals("EA_Model", model.getName())
		assertEquals(1, model.getPackages().size())
		assertEquals("Model",topLevelPackage.getName())
		assertEquals(1, topLevelPackage.getPackages().size())
		assertEquals("QUICK Class Model", secondLevelPackage.getName())
		assertEquals(6, secondLevelPackage.getPackages().size())
		assertTrue(thirdLevelPackage.getPackages().size() == 3)
		assertEquals("action", thirdLevelPackage.getName())
		assertNotNull(model.getObjectByName("ConditionOccurrence"))
		println model.getObjectByName("ConditionOccurrence")
	}

}
