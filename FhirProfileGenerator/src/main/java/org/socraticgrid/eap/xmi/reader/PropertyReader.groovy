package org.socraticgrid.eap.xmi.reader

import org.socraticgrid.uml.UmlClass
import org.socraticgrid.uml.UmlModel
import org.socraticgrid.uml.UmlProperty


/**
 * Reader used to unmarshal an XMI 2.1 UML class property.
 * 
 * @author cnanjo
 *
 */
class PropertyReader {
	
	def uml = new groovy.xml.Namespace("http://schema.omg.org/spec/UML/2.1", 'uml')
	def xmi = new groovy.xml.Namespace("http://schema.omg.org/spec/XMI/2.1", 'xmi')

	public PropertyReader() {}

	public UmlProperty readProperty(Node propertyNode, UmlModel model) {
		def upperValue = propertyNode.upperValue.'@value'[0]
		if(upperValue == "*" || upperValue == "-1") {
			upperValue = -1
		} else {
			upperValue = upperValue.toInteger()
		}
		UmlProperty property = new UmlProperty(propertyNode.'@name', 
			propertyNode.lowerValue.'@value'[0].toInteger(),
			upperValue)
		property.setId(propertyNode.attribute(xmi.id))
		property.setTypeId(propertyNode.type[0].attribute(xmi.idref))
		model.putObject(property.getId(), property)
		return property
	}
	
	def processOwnedAttribute(Node ownedAttribute, UmlClass parent, UmlModel model) {
		UmlProperty parsedItem = null
		if(ownedAttribute.attribute(xmi.type) == 'uml:Property') {
			parsedItem = readProperty(ownedAttribute, model)
			parent.addProperty(parsedItem);
			parsedItem.setSource(parent);
		} else {
			throw new RuntimeException("Unknown attribute " + ownedAttribute.attribute(xmi.type))
		}
		return parsedItem
	}
}
