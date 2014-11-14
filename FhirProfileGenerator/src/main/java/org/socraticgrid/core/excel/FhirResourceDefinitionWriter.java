package org.socraticgrid.core.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Profile.ConstraintComponent;
import org.hl7.fhir.instance.model.Profile.ElementComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionMappingComponent;
import org.hl7.fhir.instance.model.Profile.ProfileExtensionDefnComponent;
import org.hl7.fhir.instance.model.Profile.ProfileStructureComponent;
import org.hl7.fhir.instance.model.Profile.TypeRefComponent;
import org.socraticgrid.common.excel.ExcelWriter;

public class FhirResourceDefinitionWriter {
	
	private Map<String,ProfileExtensionDefnComponent> extensionIndex = new HashMap<String, ProfileExtensionDefnComponent>();
	private String outputFolderPath;
	private String resourceName;
	
	public FhirResourceDefinitionWriter(String resourceName, Profile profile, String outputPath) {
		this.outputFolderPath = outputPath;
		this.resourceName = resourceName;
		indexProfile(profile);
	}

	public void writeProfileAsResourceDefinition(String resourceName, Profile profile) {
		System.out.println("I am writing a resource definition");
		
        Workbook workbook = ExcelWriter.createBlankWorkbook();
        
        createInstructionSheet(workbook);
        Sheet sheet = ExcelWriter.createSheet(workbook, resourceName);
          
        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        ProfileStructureComponent structure = profile.getStructure().get(0);
        ConstraintComponent differential = structure.getDifferential();
        List<ElementComponent> elements = differential.getElement();
        data.put("1", new Object[] {"Element", "Card.", "Type", "Definition", "QUICK Mapping"});
        data.put("2", new Object[] {resourceName, "", "Resource", "TBD", ""});
        for(int index = 0; index < elements.size(); index++) {
        	ElementComponent element = elements.get(index);
        	if(element.getName().equalsIgnoreCase("modifierExtension")) {
        		continue;
        	}
        	data.put("" + (index + 3), new Object[] {resourceName + "." + element.getName(), getCardinality(element), getElementType(element), element.getDefinition().getFormal(), getMapping(element)});
        }
          
        ExcelWriter.buildRowFromListOfArrays(sheet, data);
        ExcelWriter.persistWorksheet(outputFolderPath + File.separator + resourceName, workbook);
    }

	private String getMapping(ElementComponent element) {
		List<ElementDefinitionMappingComponent> mappings = element.getDefinition().getMapping();
		if(mappings.size() > 0) {
			ElementDefinitionMappingComponent mapping = mappings.get(0);
			return mapping.getMap();
		} else {
			return null;
		}
	}
	
	public void createInstructionSheet(Workbook workbook) {
		Sheet sheet = ExcelWriter.createSheet(workbook, "Instructions");
        
        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"FHIR Resource-authoring Spreadsheet"});
        data.put("2", new Object[] {"This spreadsheet is used to support the definition of resources or data types (structures).  A complete set of instructions on the various tabs, columns and rules associated with populating this spreadsheet can be found here:"});
        data.put("3", new Object[] {"http://wiki.hl7.org?title=FHIR_Spreadsheet_Authoring"});
        
        ExcelWriter.buildRowFromListOfArrays(sheet, data);
	}
	
	public String getElementType(ElementComponent element) {
		StringBuilder builder = new StringBuilder();
		int size = 0;
		List<TypeRefComponent> types = null;
		ProfileExtensionDefnComponent extensionDef = extensionIndex.get(element.getName());
		if(extensionDef != null) {
			types = extensionDef.getElement().get(0).getDefinition().getType();
			size = types.size();
		} else {
			types = element.getDefinition().getType();
			size = types.size();
		}
		
		for(int index = 0; index < size; index++) {
			if(types.get(index).getCode() != null) {
				builder.append(types.get(index).getCode());
				if(index < size - 1) {
					builder.append("|");
				}
			}
		}
		
		if(size == 0) {
			builder.append(element.getName());
		}
		return builder.toString();
	}
	
	public String getCardinality(ElementComponent element) {
		int low = element.getDefinition().getMin();
		String max = element.getDefinition().getMax();
		return low + ".." + max;
	}
	
	public void indexProfile(Profile profile) {
		List<ProfileExtensionDefnComponent> extensions = profile.getExtensionDefn();
		for(ProfileExtensionDefnComponent extension:extensions) {
			extensionIndex.put(extension.getName(), extension);
		}
	}

}
