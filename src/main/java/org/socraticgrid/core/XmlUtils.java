package org.socraticgrid.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlUtils {
	
	public static Document getDocument(String xmlDocument) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(xmlDocument.getBytes("UTF-8")));
		} catch(Exception e) {
			throw new RuntimeException("Error parsing XML document", e);
		}
	}

	public static boolean validate(URL schemaFile, String xmlString) {
		boolean success = false;
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			Source xmlSource = null;
			try {
				xmlSource = new StreamSource(new java.io.StringReader(xmlString));
				validator.validate(xmlSource);
				System.out.println("Congratulations, the document is valid");
				success = true;
			} catch (SAXException e) {
				e.printStackTrace();
			  System.out.println(xmlSource.getSystemId() + " is NOT valid");
			  System.out.println("Reason: " + e.getLocalizedMessage());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}

}
