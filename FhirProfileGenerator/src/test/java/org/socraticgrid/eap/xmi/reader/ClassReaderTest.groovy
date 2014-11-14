package org.socraticgrid.eap.xmi.reader;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.socraticgrid.uml.TaggedValue
import org.socraticgrid.uml.UmlClass
import org.socraticgrid.uml.UmlModel

class ClassReaderTest {
	
	private UmlModel model
	private UmlModelLoader loader
	private List<UmlClass> classes1
	private List<UmlClass> classes2
	
	@Before
	public void setup() {
		loader = new UmlModelLoader()
		model = loader.loadModel(loader.loadFromStream,"/xmi/UnitTestEapModel.xml")
		model.buildIndex()
		classes1 = model.getPackages()[0].getPackages()[0].getPackages()[0].getClasses()
		classes2 = model.getPackages()[0].getPackages()[0].getPackages()[1].getClasses()
	}

	@Test
	public void testClassUnmarshalling() {
		assertEquals(2, classes1.size())
		assertEquals(3, classes2.size())
	}
	
	@Test
	public void testClassId() {
		assertNotNull(model.getObjectByName("Parent1").getId())
		assertNotNull(model.getObjectByName("Parent2").getId())
		assertNotNull(model.getObjectByName("Child").getId())
		assertNotNull(model.getObjectByName("DataType1").getId())
		assertNotNull(model.getObjectByName("DataType2").getId())
	}

	@Test
	public void testClassTaggedValues() {
		UmlClass child = model.getObjectByName("Child")
		assertNotNull(child)
		assertEquals(1, child.getTags().size())
		assertEquals("map.fhir.class", child.tags[0].key)
		assertEquals("FhirClassX", child.tags[0].value)
	}
	
	@Test
	public void testGetTaggedValueByKey() {
		UmlClass child = model.getObjectByName("Child")
		assertNotNull(child)
		child.buildTaggedValueIndex()
		TaggedValue tag = child.getTaggedValueByKey("map.fhir.class");
		assertNotNull(tag)
	}
	
	@Test
	public void testGeneralizationLoading() {
		UmlClass child = model.getObjectByName("Child")
		assertNotNull(child)
		assertEquals(2, child.getGeneralizations().size())
		assertEquals("Parent1", child.getGeneralizations()[0].getName())
		assertEquals("Parent2", child.getGeneralizations()[1].getName())
	}
}
