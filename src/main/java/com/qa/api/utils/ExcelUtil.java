package com.qa.api.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
	
	private static String TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/APITestData.xlsx";
	
	public static Object[][] readData(String sheetName) {
		
		Object[][] data = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(TEST_DATA_SHEET_PATH);
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			
			//workbook --> sheet --> rows --> cells
			//get information from the desired sheet
			Sheet sheet = workbook.getSheet(sheetName);
			int totalRows = sheet.getLastRowNum();
			int totalColumns = sheet.getRow(0).getLastCellNum();
			
			data = new Object[totalRows][totalColumns];
			
			//read excel data
			for (int row = 0; row < totalRows; row++) {
				for(int column = 0; column < totalColumns; column++) {
					//avoid header row
					data[row][column] = sheet.getRow(row+1).getCell(column).toString();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
}
