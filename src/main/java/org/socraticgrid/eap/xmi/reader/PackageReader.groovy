package org.socraticgrid.eap.xmi.reader

import org.socraticgrid.uml.UmlModel
import org.socraticgrid.uml.UmlPackage


/**
 * Reader used to unmarshal an XMI 2.1 UML package.
 * 
 * @author cnanjo
 *
 */
class PackageReader {
	
	def uml = new groovy.xml.Namespace("http://schema.omg.org/spec/UML/2.1", 'uml')
	def xmi = new groovy.xml.Namespace("http://schema.omg.org/spec/XMI/2.1", 'xmi')
	def classReader = new ClassReader()

	public PackageReader() {}
	
	public UmlPackage readPackage(Node packageNode, UmlModel model) {
		UmlPackage umlPackage = new UmlPackage(packageNode.'@name')
		umlPackage.setId(packageNode.attribute(xmi.id))
		model.putObject(umlPackage.getId(), umlPackage)
		return umlPackage
	}

	def processPackagedElements(NodeList packagedElements, def parent, UmlModel model) {
		packagedElements.each{it -> processPackagedElement(it, parent, model)}
	}
	
	def processPackagedElement(Node packagedElement, def parent, UmlModel model) {
		def parsedItem = null
		if(packagedElement instanceof Node && ReaderUtils.getLocalName(packagedElement.name()) == "packagedElement") {
			if(packagedElement.attribute(xmi.type) == 'uml:Package') {
				parsedItem = readPackage(packagedElement, model)
				parent.addPackage(parsedItem)
				processPackagedElements(packagedElement.children(), parsedItem, model)
			} else if(packagedElement.attribute(xmi.type) == 'uml:Class') {
				classReader.processUmlClass(packagedElement, parent, model)
			} else {
				//throw new RuntimeException("Unknown packagedElement child " + packagedElement.attribute(xmi.type)) 
			}
		}
		return parsedItem
	}
	
}
