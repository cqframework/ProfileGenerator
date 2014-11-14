package org.socraticgrid.eap.xmi.reader

import org.socraticgrid.uml.UmlClass
import org.socraticgrid.uml.UmlModel


/**
 * Reader used to unmarshal an XMI 2.1 UML class.
 * 
 * @author cnanjo
 *
 */
class ClassReader {
	
	def uml = new groovy.xml.Namespace("http://schema.omg.org/spec/UML/2.1", 'uml')
	def xmi = new groovy.xml.Namespace("http://schema.omg.org/spec/XMI/2.1", 'xmi')
	def propertyReader = new PropertyReader()

	public ClassReader() {}
	
	public UmlClass readClass(Node classNode, UmlModel model) {
		UmlClass umlClass = new UmlClass(classNode.'@name')
		umlClass.setId(classNode.attribute(xmi.id))
		model.putObject(umlClass.getId(), umlClass)
		umlClass.setModel(model);
		classNode.ownedAttribute.each { it -> propertyReader.processOwnedAttribute(it, umlClass, model) }
		classNode.generalization.each { it -> umlClass.addGeneralizationId(it.'@general') }
		return umlClass
	}
	
	def processUmlClass(Node classNode, def parent, UmlModel model) {
		def umlClass = readClass(classNode, model)
		parent.addClass(umlClass)
	}

}
