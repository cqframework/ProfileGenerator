package org.socraticgrid.core.xmlreader

import org.socraticgrid.uml.UmlModel;

class BaseReader {
	
	def loadFromFilePath = {filePath -> new XmlParser().parse(new File(filePath))}
	def loadFromStream = {filePath -> new XmlParser().parse((InputStream)getClass().getResourceAsStream(filePath))}

	public BaseReader() {}
	
	def Node loadModelFromStream(def filePath) {
		return loadFromStream.call(filePath)
	}

}
