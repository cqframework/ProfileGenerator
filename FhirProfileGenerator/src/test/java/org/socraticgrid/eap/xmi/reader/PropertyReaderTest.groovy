package org.socraticgrid.eap.xmi.reader;

import static org.junit.Assert.*

import java.util.List;

import org.junit.Before;
import org.junit.Test
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlModel
import org.socraticgrid.uml.UmlProperty

class PropertyReaderTest {

	private UmlModel model
	private UmlModelLoader loader
	private UmlClass parent1
	
	@Before
	public void setup() {
		loader = new UmlModelLoader()
		model = loader.loadModel(loader.loadFromStream,"/xmi/UnitTestEapModel.xml")
		model.buildIndex()
		parent1 = model.getObjectByName("Parent1")
	}
	
	@Test
	public void testParent1AttributeLoading() {
		List<UmlProperty> properties = parent1.getProperties()
		UmlProperty attribute1 = properties[0]
		UmlProperty attribute2 = properties[1]
		
		assertEquals(2, properties.size())
		
		assertNotNull(attribute1)
		assertNotNull(attribute2)
		
		assertEquals("attribute1", attribute1.name)
		assertEquals(0, attribute1.low)
		assertEquals(1, attribute1.high)
		
		assertEquals("attribute2", attribute2.name)
		assertEquals(3, attribute2.low)
		assertEquals(-1, attribute2.high)
	}
	
	@Test
	public void testAtributeTaggedValues() {
		assertEquals(2, parent1.properties.size)
		assertEquals(0, parent1.properties[0].tags.size)
		assertEquals("map.fhir.attribute.cardinality.low", parent1.properties[1].tags[0].key)
		assertEquals("0", parent1.properties[1].tags[0].value)
		assertEquals("map.fhir.attribute.cardinality.high", parent1.properties[1].tags[1].key)
		assertEquals("1", parent1.properties[1].tags[1].value)
	}

}
