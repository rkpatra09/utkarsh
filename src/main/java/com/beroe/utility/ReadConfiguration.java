package com.beroe.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.sshtools.j2ssh.util.Base64.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class ReadConfiguration {
	
	public static String BROWSER_TYPE=null;
	public static String BROWSER_VERSION=null;
	public static String IS_PROXY_ENABLED="false";
	public static String PROXY=null;
	public static String DRIVER_PATH=null;
	public static String IMPLICIT_WAIT=null;
	
	public static String ASSET_FLOW_IP=null;
	public static String ASSET_FLOW_PORT=null;
	public static String ASSET_FLOW_USER=null;
	public static String ASSET_FLOW_PASSWORD=null;
	public static String APP_PATH=null;
	
	public static String DB_SERVER_IP=null;
	public static String DB_SERVER_PORT=null;
	public static String DB_SERVER_USER=null;
	public static String DB_SERVER_PASSWORD=null;
	public static String DB_NAME=null;
	public static String DRIVER_CLASS=null;
	public static String MAX_DB_SEARCH_WAIT=null;
	public static String SHEET_CONFIG=null;

	
	public static String REPORTS_DRIVE=null;
	public static String STEP_FILE_DRIVE=null;
	public static Properties properties=null;
	
//Adding New config variables
	public static String ASSET_FLOW_GUI_USERNAME=null;
	public static String ASSET_FLOW_GUI_PASSWORD=null;
	public static String ASSET_FLOW_CONNECTION_PORT=null;
	public static String STORAGE_IP=null;
	public static String STORAGE_USERNAME=null;
	public static String STORAGE_PASSWORD=null;
	
	
	public static void readValues() {
		
		
		//Browser Configuration Settings
		BROWSER_TYPE=readExcelData("Configuration","Browser", 1);
		BROWSER_VERSION=readExcelData("Configuration","Version", 1);
		IS_PROXY_ENABLED=readExcelData("Configuration","Set_Proxy", 1);
		PROXY=readExcelData("Configuration","Proxy", 1);
		DRIVER_PATH=readExcelData("Configuration","Driver_Path", 1);
		IMPLICIT_WAIT=readExcelData("Configuration","Implicit_Wait", 1);
		

		// Application Configuration Settings 
		ASSET_FLOW_IP = readExcelData("Configuration","AssetFlow_serverIP", 1);
		ASSET_FLOW_PORT = readExcelData("Configuration","AssetFlow_port", 1);
		ASSET_FLOW_USER = readExcelData("Configuration","AssetFlow_Username",1);
		ASSET_FLOW_PASSWORD = readExcelData("Configuration","AssetFlow_Password",1);
		
		//ADDING NEW CONGIGURATION SETTINGS
		ASSET_FLOW_GUI_USERNAME= readExcelData("Configuration","AssetFlow_Gui_username",1);
		ASSET_FLOW_GUI_PASSWORD = readExcelData("Configuration","AssetFlow_Gui_password",1);
		ASSET_FLOW_CONNECTION_PORT = readExcelData("Configuration","AssetFlow_connetion_port",1);
		STORAGE_IP = readExcelData("Configuration","AssetFlow_storage_IP",1);
		STORAGE_USERNAME = readExcelData("Configuration","AssetFlow_storage_UserName",1);
		STORAGE_PASSWORD = readExcelData("Configuration","AssetFlow_storage_Password",1);
		
		// Application DB Configuration Settings
		DB_SERVER_IP = readExcelData("Configuration","AFM_DB_serverIP",1);
		DB_SERVER_PORT = readExcelData("Configuration","AFM_DB_portNumber", 1);
		DB_SERVER_USER = readExcelData("Configuration","AFM_DB_username",1);
		DB_SERVER_PASSWORD = readExcelData("Configuration","AFM_DB_password",1);
		DB_NAME = readExcelData("Configuration","AFM_DB_sid", 1);
		DRIVER_CLASS = readExcelData("Configuration","DB_Driver_class",1);
		MAX_DB_SEARCH_WAIT=readExcelData("Configuration","Max_DB_Search_Wait", 1);
		
		// Test Reports and Logs Path
		REPORTS_DRIVE=readExcelData("Configuration","Reports_Drive",1);
		
		//Input Data Directory
		STEP_FILE_DRIVE=readExcelData("Configuration","Step_File_Drive",1);
		SHEET_CONFIG=readExcelData("Configuration","Sheet_Name",1);
	}

	public static String readExcelData(String sheetName, String key, int reqCol) {
		String sFile = SetupTestStepFile();
		String value = null;
		try {
			Workbook wb = Workbook.getWorkbook(new File(sFile));//"C:\\Automation\\Data.xls"));
			Sheet sheet = wb.getSheet(sheetName);
			int rows = sheet.getRows();
			for (int row = 1; row < rows; row++)
				if (sheet.getCell(0, row).getContents().equalsIgnoreCase(key))
					value = sheet.getCell(reqCol, row).getContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String SetupTestStepFile(){
		String stepFile = null;
		try {
			java.io.InputStream inputStream = ReadConfiguration.class.getClassLoader().getResourceAsStream("build.properties");
			properties = new Properties();
			
			//properties = new Properties();
	//		FileInputStream is = new FileInputStream(new File("build.properties"));
			
			//FileInputStream is = new FileInputStream(new File("D:\\Automation\\Beroe\\Build_prop\\build.properties"));
		
			properties.load(inputStream);
			stepFile = properties.getProperty("STEP_FILE_PATH");
			//System.out.println("STEP_FILE_PATH: "+stepFile);
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("File Not Found");
		}
		return stepFile;
	}
}
