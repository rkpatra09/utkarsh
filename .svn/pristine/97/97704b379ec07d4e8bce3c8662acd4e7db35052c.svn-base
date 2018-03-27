package com.beroe.utility;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelHandler {
	public static String[][] excelRead(String fileName, String sheet,String sh) {
		String[][] data = null;
		//List<String[][]> dataL = new ArrayList<String[][]>();
		int i = 0, j = 0;
		/*try {
			File excel = new File(fileName);
			FileInputStream fis = new FileInputStream(excel);
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet ws = wb.getSheet("Input");
			int rowNum = ws.getLastRowNum() + 1;
			int colNum = ws.getRow(0).getLastCellNum();
			data = new String[rowNum][colNum];
			for (i = 0; i < rowNum; i++) {
				HSSFRow row = ws.getRow(i);
				for (j = 0; j < colNum; j++) {
					HSSFCell cell = row.getCell(j);
					String value = cellToString(cell);
					if (value == null)
						throw new Exception();
					data[i][j] = value;
				}
			}
		} catch (Exception e) {
			// Utils.debug("Data = " +data.toString());
			Utils.debug("Invalid Values at " + i + " " + j);
		}*/
		try {
			
			File excel =null;
			FileInputStream fis = null;
			HSSFWorkbook wb = null;
			HSSFSheet ws;
		
//			List<String> arr = Arrays.asList(sheet.split(",")); //Splitting the Shet names by comma 
		
			//for(String sh : arr) {
					excel = new File(fileName);
					fis = new FileInputStream(excel);
					wb = new HSSFWorkbook(fis);
			
				//	Utils.debug("??? " +sh);
				
					ws = wb.getSheet(sh);
				
					int rowNum = ws.getLastRowNum() + 1;
					int colNum = ws.getRow(0).getLastCellNum();
					data = new String[rowNum][colNum];
				
					i=0; j=0;
						for (i = 0; i < rowNum; i++) {
							HSSFRow row = ws.getRow(i);
							
							if(null == row) //If Null rows after the endtc, it continues
								continue;
							
								for (j = 0; j < colNum; j++) {
						//		HSSFCell cell = row.getCell(j);
								HSSFCell cell = row.getCell(j, org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
								String value = cellToString(cell);
								
								if (value == null)
									throw new Exception();
								data[i][j] = value;
								
							}
					    } 
				
			//	dataL.add(data);
		//	}
			
		} catch (Exception e) {
			// Utils.debug("Data = " +data.toString());
			e.printStackTrace();
			//Utils.debug("TESTING");
			Utils.debug("Invalid Values at " + i + " " + j);
		}
		return data;
	}
	public static List<String[][]> excelRead(String fileName, String sheet) {
		String[][] data = null;
		List<String[][]> dataL = new ArrayList<String[][]>();
		int i = 0, j = 0;
		/*try {
			File excel = new File(fileName);
			FileInputStream fis = new FileInputStream(excel);
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet ws = wb.getSheet("Input");
			int rowNum = ws.getLastRowNum() + 1;
			int colNum = ws.getRow(0).getLastCellNum();
			data = new String[rowNum][colNum];
			for (i = 0; i < rowNum; i++) {
				HSSFRow row = ws.getRow(i);
				for (j = 0; j < colNum; j++) {
					HSSFCell cell = row.getCell(j);
					String value = cellToString(cell);
					if (value == null)
						throw new Exception();
					data[i][j] = value;
				}
			}
		} catch (Exception e) {
			// Utils.debug("Data = " +data.toString());
			Utils.debug("Invalid Values at " + i + " " + j);
		}*/
		try {
			
			File excel =null;
			FileInputStream fis = null;
			HSSFWorkbook wb = null;
			HSSFSheet ws;
		
			List<String> arr = Arrays.asList(sheet.split(",")); //Splitting the Shet names by comma 
		
			for(String sh : arr) {
					excel = new File(fileName);
					fis = new FileInputStream(excel);
					wb = new HSSFWorkbook(fis);
			
				//	Utils.debug("??? " +sh);
				
					ws = wb.getSheet(sh);
				
					int rowNum = ws.getLastRowNum() + 1;
					int colNum = ws.getRow(0).getLastCellNum();
					data = new String[rowNum][colNum];
				
					i=0; j=0;
						for (i = 0; i < rowNum; i++) {
							HSSFRow row = ws.getRow(i);
							
							if(null == row) //If Null rows after the endtc, it continues
								continue;
							
								for (j = 0; j < colNum; j++) {
								HSSFCell cell = row.getCell(j);
								String value = cellToString(cell);
								
								if (value == null)
									throw new Exception();
								data[i][j] = value;
								
							}
					    } 
				
				dataL.add(data);
			}
			
		} catch (Exception e) {
			// Utils.debug("Data = " +data.toString());
			e.printStackTrace();
			//Utils.debug("TESTING");
			Utils.debug("Invalid Values at " + i + " " + j);
		}
		return dataL;
	}

	public static String cellToString(HSSFCell cell) {
		int type;
		
		try{
			
			if(cell != null)
			{
				Object result=null;
			
				type = cell.getCellType();
				//	Utils.debug("Type:"+type);
					switch (type) {

					case 0: // numeric value in Excel
						if (DateUtil.isCellDateFormatted(cell)) {
				//			Utils.debug("TIme:"+cell.getDateCellValue());
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
							Date date=null;
							try {
								date = (Date)formatter.parse(cell.getDateCellValue().toString());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							SimpleDateFormat sd = null;
							Utils.debug("===="+date.getYear());
							if(date.getYear() == -1 ){
								sd = new SimpleDateFormat("HH:mm:ss");
							}else{
								sd = new SimpleDateFormat("yyyy-MM-dd");
							}
							
							result = sd.format(date);
			            } else {
			                Utils.debug(String.valueOf(cell.getNumericCellValue()));
			                result = cell.getNumericCellValue();
			            }
						
						// Utils.debug("Result case 0: " +result);
						break;
					case 1: // String Value in Excel
						result = cell.getStringCellValue();
						// Utils.debug("Result case 1: " +result);
						break;
					case 3: // Blank Cell
						result = "-";
						// Utils.debug("Result case 2: " +result);
						break;

					default:
						throw new RuntimeException(
								"There are no support for this type of cell");
					}
					return result.toString();
			}			
		}catch(Exception e)
		{
			Utils.debug("Execption in fetching the value of cell :"+e.getMessage());
		}
		return null;
				
	}
	
	public static void main(String ar[])
	{
		ReadConfiguration.readValues();
		final int KEYWORD_CELL = 4;
		final int PARAMS_CELL = 7;
		final int LOCATE_TYPE = 5;
		final int LOCATE_STRING = 6;
		final int TC_NAME = 1;
		
		String stepFile = ReadConfiguration.SetupTestStepFile();
		Utils.debug("Step File----> : " + stepFile);
		List<String> fileList = Arrays.asList(stepFile.split(","));
		List<String[][]> dataL = new ArrayList<String[][]>();
		for (String filearray : fileList) {
			Utils.debug("File Name: " + filearray);
			// Code changes for removing hard coding of sheet name starts here
			String sheetConf = ReadConfiguration.SHEET_CONFIG;
			List<String> arr = Arrays.asList(sheetConf.split(","));
			for (String sh : arr) {
				Utils.debug("Sheet Name : " + sh);
				String[][] data = ExcelHandler.excelRead(filearray, sheetConf, sh);
				dataL.add(data);
				for (String[][] steps : dataL) {
					for (int i = 1; i < steps.length; i++) {
						String keyword = steps[i][KEYWORD_CELL];
						String paramCell = steps[i][PARAMS_CELL];
						String locateType = steps[i][LOCATE_TYPE];
						String locateString = steps[i][LOCATE_STRING];
						String testCaseName = steps[i][TC_NAME];
						
						Utils.debug("keyword: " + keyword);
						Utils.debug("paramCell: " + paramCell);
						Utils.debug("locateType: " + locateType);
						Utils.debug("locateString: " + locateString);
						Utils.debug("testCaseName: " + testCaseName);
						
						if(keyword==null && paramCell == null && locateType == null && locateString == null && testCaseName == null)
						{
							Utils.debug("Suneel : Format Need for Sheet Name : " + sh);
							
						}
					}
				}				
			}			
		}
	}
}
