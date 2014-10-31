package org.socraticgrid.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	public static void buildRowFromListOfArrays(Sheet sheet,
			Map<String, Object[]> data) {
		//Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
	}

	public static void persistWorksheet(String resourceName, Workbook workbook) {
		try
        {
            //Write the workbook in file system
        	File outputFile = new File(resourceName + ".xlsx");
            FileOutputStream out = new FileOutputStream(outputFile);
            System.out.println(outputFile.getCanonicalPath());
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
	public static Workbook createBlankWorkbook() {
		return new XSSFWorkbook();
	}
	
	public static Sheet createSheet(Workbook workbook, String sheetName) {
		return workbook.createSheet(sheetName);
	}

}
