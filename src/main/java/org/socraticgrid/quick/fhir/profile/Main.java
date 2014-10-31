package org.socraticgrid.quick.fhir.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hl7.fhir.instance.model.Profile;
import org.socraticgrid.core.excel.FhirResourceDefinitionWriter;
import org.socraticgrid.eap.xmi.reader.UmlModelLoader;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	
	/**
	 * Method generates profiles for the specified QUICK classes.
	 * 
	 * Required argument:
	 *  - Path to profile generation configuration file specifying profiles to generate
	 *  - Path to XMI model file
	 *  - Path to output folder
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		File configurationFile = null;
		File outputFolder = null;
		File modelFile = null;
		if(args.length != 3) {
			System.out.println("Invalid arguments: Main path-to-configuration-file path-to-model-file path-to-output-folder");
		} else {
			configurationFile = new File(args[0]);
			modelFile = new File(args[1]);
			outputFolder = new File(args[2]);
			if(hasValidArguments(args, configurationFile, modelFile, outputFolder)) {
				buildProfiles(configurationFile, modelFile, outputFolder);
			}
		}
	}
	
	/**
	 * Returns true if arguments are valid file and folder paths. Returns false otherwise.
	 * 
	 * @param args
	 * @param configurationFile
	 * @param modelFile
	 * @param outputFolder
	 * @return
	 */
	private static boolean hasValidArguments(String[] args, File configurationFile, File modelFile, File outputFolder) {
		boolean isValid = true;
		if(!configurationFile.exists() || !configurationFile.isFile()) {
			System.out.println("The following path is not a valid configuration file path " + args[0]);
			isValid = false;
		}
		if(!modelFile.exists() || !modelFile.isFile()) {
			System.out.println("The following path is not a valid model file path " + args[0]);
			isValid = false;
		}
		if(!outputFolder.exists() || !outputFolder.isDirectory()) {
			System.out.println("The following path is not a valid output folder path " + args[1]);
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Workhorse method that iterates through the profile generation items in the configuration file
	 * and generates the profiles.
	 * 
	 * @param configurationFile - File listing profiles to generate.
	 * @param modelFile - The UML containing the profile generation metadata.
	 * @param outputFolder - Folder where profiles are to be output.
	 */
	private static void buildProfiles(File configurationFile, File modelFile, File outputFolder) {
		List<ProfileGenerationMetadata> profiles = getProfiles(configurationFile);
		for(ProfileGenerationMetadata profileMeta:profiles) {
			System.out.println(profileMeta.toString());
			try {
				File profileFile = new File(outputFolder.getCanonicalPath() + File.separator + profileMeta.getName() + ".xml");
				UmlModel model = loadModel(modelFile);
				FhirProfileGenerator generator = new FhirProfileGenerator();
				UmlClass conditionOccurrence = (UmlClass)model.getObjectByName(profileMeta.getSource());
				Profile profile = generator.generateSimpleProfile(conditionOccurrence, new UmlClass(profileMeta.getTarget()));
				FhirResourceDefinitionWriter resourceDefWriter = new FhirResourceDefinitionWriter(profileMeta.getResourceName(),profile, outputFolder.getCanonicalPath());
				FileWriter writer = new FileWriter(profileFile);
				writer.write(FhirProfileGenerator.generateProfileAsXml(profile));
				if(profileMeta.isGenerateResourceDefinition()) {
					resourceDefWriter.writeProfileAsResourceDefinition(profileMeta.getResourceName(), profile);
				}
				writer.close();
			} catch(Exception e) {
				throw new RuntimeException("Error generating profile.", e);
			}
		}
	}
	
	/**
	 * Parsing of configuration file.
	 * 
	 * @param configurationFile
	 * @return
	 */
	private static List<ProfileGenerationMetadata> getProfiles(File configurationFile) {
		List<ProfileGenerationMetadata> profiles = new ArrayList<ProfileGenerationMetadata>();
		try {
			InputStream is = new FileInputStream(configurationFile);
			Document document = loadDocumentFromStream(is);
			NodeList profileMetadataList = document.getElementsByTagName("profileMetadata");
			for(int index=0;index<profileMetadataList.getLength(); index++) {
				Node profileMetadata = profileMetadataList.item(index);
				processProfileMetadataItem(profiles, profileMetadata);
			}
			is.close();
		} catch(Exception e) {
			throw new RuntimeException("Error loading configuration file", e);
		}
		return profiles;
	}
	
	/**
	 * Unmarshalling of configuration line items - profile generation instructions.
	 * 
	 * @param profiles
	 * @param profileMetadata
	 */
	private static void processProfileMetadataItem(List<ProfileGenerationMetadata> profiles, Node profileMetadata) {
		NodeList children = profileMetadata.getChildNodes();
		ProfileGenerationMetadata profileMetadataItem = new ProfileGenerationMetadata();
		for(int j = 0; j < children.getLength(); j++) {
			Node node = children.item(j);
			processProfileMetadataNode(profileMetadataItem, node);
		}
		profiles.add(profileMetadataItem);
	}

	/**
	 * Parsing of profile generation instructions.
	 * 
	 * @param profileMetadataItem
	 * @param node
	 */
	private static void processProfileMetadataNode(ProfileGenerationMetadata profileMetadataItem, Node node) {
		if(node.getNodeType() == Node.ELEMENT_NODE) {
			if(node.getNodeName().equals("name")) {
				 profileMetadataItem.setName(node.getTextContent());
			} else if(node.getNodeName().equals("source")) {
				profileMetadataItem.setSource(node.getTextContent());
			} else if(node.getNodeName().equals("target")) {
				profileMetadataItem.setTarget(node.getTextContent());
			} else if(node.getNodeName().equals("generateResourceDefinition")) {
				profileMetadataItem.setGenerateResourceDefinition(Boolean.parseBoolean(node.getTextContent()));
			} else if(node.getNodeName().equals("proposedResourceName")) {
				profileMetadataItem.setResourceName(node.getTextContent());
			}
		}
	}
	
	/**
	 * Method loads UML model from an XMI file.
	 * 
	 * @param modelFile
	 * @return
	 */
	public static UmlModel loadModel(File modelFile) {
		UmlModelLoader loader = new UmlModelLoader();
		try {
			return loader.loadModelFromFilePath(modelFile.getCanonicalPath());
		} catch(Exception e) {
			throw new RuntimeException("Error loading model", e);
		}
	}
	
	/**
	 * Helper method for reading an XML file as a DOM.
	 * 
	 * @param is
	 * @return
	 */
	public static Document loadDocumentFromStream(InputStream is) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        return docBuilder.parse (is);
		} catch(Exception e) {
			throw new RuntimeException("Error loading XML document", e);
		}
	}
}
