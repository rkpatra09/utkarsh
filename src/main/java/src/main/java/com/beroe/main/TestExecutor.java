package src.main.java.com.beroe.main;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JComboBox;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;




import com.beroe.utility.ExcelHandler;
//import com.beroe.utility.ExcelHandler;
//import com.beroe.utility.ExcelHandler;
import com.beroe.utility.ReadConfiguration;
import com.beroe.utility.Utils;
import com.beroe.keywords.KeywordsImpl;
import com.beroe.reporting.CaptureScreenShot;
import com.beroe.reporting.WriteHTMLReport;
//import com.beroe.reporting.WriteHTMLReport;
import com.beroe.reporting.WriteXlReports;

import org.apache.log4j.Logger;

import java.io.*;


public class TestExecutor {
//	private static Logger log = Logger.getLogger(TestExecutor.class.getClass());
	public static Logger log = Logger.getLogger("devpinoyLogger");
	private static final int TC_NAME = 1;
	private static final int STEP_DESC = 3;
	private static final int KEYWORD_CELL = 4;
	private static final int LOCATE_TYPE = 5;
	private static final int LOCATE_STRING = 6;
	private static final int PARAMS_CELL = 7;
	private static ArrayList<String> stepresult = new ArrayList<String>();
	private static ArrayList<String> loopVars = new ArrayList<String>();
	private static String strResultPath = null;
	private static String ReportsPath = null;
	private static boolean executionFlag = false;

	private static String[] splitvalues = null;
	private static String table = null;
	private static String tableRow = null;
	private static String expectedOutput = null;
	private static String curr_tc_name = null;
	private static String tc_result = null;
	private static String tc_start_time = null;
	private static String strtmp1 = null, strtmp2 = null, strtmp3 = null,
			strtmp4 = null, strtmp5 = null;

	private static int curr_step = 0;
	public static int sl_no = 0;
	public static int loopstart = 0;
	public static int loopcount = 0;
	public static String loop_cnt1;
	public static int cnt = 0;
	public static WebDriver wd = null;
	public static boolean isLoopingEnabled = false;

	private static Map configurationMap = null;
	//// Logger log1 = Logger.getLogger("devpinoyLogger");

	/*
	 * public static void launch_browser() throws InterruptedException {
	 * FirefoxProfile profile = allProfiles.getProfile("SARIT");
	 * profile.setPreference("browser.cache.disk.enable", false);
	 * profile.setPreference("browser.cache.memory.enable", false);
	 * profile.setPreference("browser.cache.offline.enable", false);
	 * profile.setPreference("network.http.use-cache", false); WebDriver driver
	 * = new FirefoxDriver(profile); driver.manage().deleteAllCookies(); }
	 */

	private static void setup() {
		log.info("*****Loading Configurations for Test Execution*****");
		ReadConfiguration.readValues();
		configurationMap = new Properties();
		configurationMap.put("AssetFlow_serverIP",
				ReadConfiguration.ASSET_FLOW_IP);
		configurationMap.put("AssetFlow_Username",
				ReadConfiguration.ASSET_FLOW_USER);
		configurationMap.put("AssetFlow_Password",
				ReadConfiguration.ASSET_FLOW_PASSWORD);
		configurationMap.put("AssetFlow_port",
				ReadConfiguration.ASSET_FLOW_PORT);

		// adding new values
		configurationMap.put("AssetFlow_Gui_username",
				ReadConfiguration.ASSET_FLOW_GUI_USERNAME);
		configurationMap.put("AssetFlow_Gui_password",
				ReadConfiguration.ASSET_FLOW_GUI_PASSWORD);
		configurationMap.put("AssetFlow_connetion_port",
				ReadConfiguration.ASSET_FLOW_CONNECTION_PORT);
		configurationMap.put("AssetFlow_storage_IP",
				ReadConfiguration.STORAGE_IP);
		configurationMap.put("AssetFlow_storage_UserName",
				ReadConfiguration.STORAGE_USERNAME);
		configurationMap.put("AssetFlow_storage_Password",
				ReadConfiguration.STORAGE_PASSWORD);

		log.info("OS Version: " + System.getProperty("os.name"));
		log.info("Browser:" + ReadConfiguration.BROWSER_TYPE);
		wd = KeywordsImpl.open_browser(wd, ReadConfiguration.BROWSER_TYPE,
				false);
		// Utils.deleteCookies(wd);

		log.info("Launching " + ReadConfiguration.BROWSER_TYPE + " Browser");
	}

	public static void main(String[] args) throws Exception {

		setup();
		/*
		 * Shuchita: 1. comments need to be added 2. Parameterization for
		 * looping input criterion 3. Global parameter class need to be created
		 */
		// String[][] steps;
		/*
		 * String stepFile =
		 * ReadConfiguration.STEP_FILE_DRIVE+"/Automation"+"/Data.xls";
		 * log.info("Step File in use: " + stepFile); steps =
		 * ExcelHandler.excelRead(stepFile);
		 */

		// List<String[][]> stepsL = null;
		// String stepFile =
		// ReadConfiguration.STEP_FILE_DRIVE+"/Automation"+"/Data.xls";

		// Code changes for removing hard coding of sheet name starts here
		// String sheetConf = ReadConfiguration.SHEET_CONFIG;
		// if(null != stepFile) {
		// stepsL = ExcelHandler.excelRead(stepFile, sheetConf);
		// log.info("Step File in use: " + stepFile);
		// }
		// Code changes for removing hard coding of sheet name Ends here

		ReportsPath = ReadConfiguration.REPORTS_DRIVE;
		strResultPath = ReportsPath + "/TestReports" + "/";
		log.info("Test Reports (both .xls and .html) will be saved at: "
				+ strResultPath);
		String strScreenShotPath = ReportsPath + "/TestReports" + "/"
				+ "Screenshots" + "/";
		log.info("Error and Expected Screen Shot Files will be saved at: "
				+ strScreenShotPath);
		String testReport = ReportsPath + "TestReports" + "/Test_Results_"
				+ KeywordsImpl.get_timeStamp() + ".html";
		File f = new File(strResultPath);
		f.mkdirs();
		WriteHTMLReport.htmlReportHeaders(testReport);
		File file = new File(testReport.replace(".html", ".xls"));
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("TestReport", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		WriteXlReports.createLabel(excelSheet);
		List<String[][]> dataL = new ArrayList<String[][]>();
		List<String[][]> stepsL = null;
		// String stepFile =
		// ReadConfiguration.STEP_FILE_DRIVE+"/Auto_SAM"+"/Data.xls";
		String stepFile = ReadConfiguration.SetupTestStepFile();
		Utils.debug("----> Step File: " + stepFile);
		
		String[][] data = null;

		List<String> fileList = Arrays.asList(stepFile.split(","));
		int inc = 0;
		for (String filearray : fileList) {
			Utils.debug("File LIst: " + filearray);

			// Code changes for removing hard coding of sheet name starts here
			String sheetConf = ReadConfiguration.SHEET_CONFIG;
			if (null != stepFile) {
				// stepsL = ExcelHandler.excelRead(stepFile, sheetConf);
				// original stepsL = ExcelHandler.excelRead(filearray,
				// sheetConf);

				List<String> arr = Arrays.asList(sheetConf.split(","));
				for (String sh : arr) {
					try {
						System.out.println("SHEEET NAME:"+sh);
						data = null;

						data = ExcelHandler.excelRead(filearray, sheetConf, sh);
						int stepIndex = 0;
						int loopcnt = 1;
						String input_from_prev_step = null;
						dataL.add(data);
						
						System.out.println("Added Data into the list SHEEET NAME:"+dataL.size());

						for (String[][] steps : dataL) {
							for (int i = 1; i < steps.length; i++) {
								// Condition to check loop keyword in Step
								// File(Input.xls) file
								// if
								// (steps[i][KEYWORD_CELL].equalsIgnoreCase("loop"))
								// {
//								if ("loop"
//										.equalsIgnoreCase(steps[i][KEYWORD_CELL])) {
//									System.out
//											.println("Start of Loop Detection block");
//									if (loopcnt == 1) {
//										isLoopingEnabled = true;
//										loop_cnt1 = steps[i][PARAMS_CELL];
//										cnt = Integer.valueOf(loop_cnt1) - 1;
//									}
//									Utils.debug("Current cnt value is: "
//											+ cnt);
//									i++;
//									stepIndex = i;
//									System.out
//											.println("end of Loop Detection block");
//								}
								// Condition to check 'loopend' keyword in Step
								// File(Input.xls) file
								// if
								// (steps[i][KEYWORD_CELL].equalsIgnoreCase("loopend"))
//								if ("loopend"
//										.equalsIgnoreCase(steps[i][KEYWORD_CELL]))
//									if (cnt > 0) {
//										i = stepIndex;
//										cnt--;
//										Utils.debug("Loopend completed");
//										loopcnt++;
//									} else {
//										isLoopingEnabled = false;
//										loopVars.clear();
//									}
								// Condition to check Execution flag in Step
								// File(Input.xls) file
								// if ((steps[i][0].equalsIgnoreCase("Y") &&
								// steps[i][KEYWORD_CELL]
								// .equalsIgnoreCase("start_tc"))) {
								// executionFlag = true;
								// }
								
								String keyword = steps[i][KEYWORD_CELL];
								
								if(keyword != null)
								{
									keyword =keyword.trim();
								}


								if ((("Y".equalsIgnoreCase(steps[i][0]) || "Y"
										.equalsIgnoreCase(steps[i][0])) && "start_tc"
									.equalsIgnoreCase(keyword))) {
									executionFlag = true;
								}
								/*if ((steps[i][0].equalsIgnoreCase("Y") && steps[i][KEYWORD_CELL]
										.equalsIgnoreCase("start_tc"))) {
									executionFlag = true;
								}*/

								if (executionFlag == true) {
									// while(executionFlag) {
									Utils.debug(" i ===" + i
											+ " KEYWORD CELL = " + KEYWORD_CELL
											+ " " + steps[i][KEYWORD_CELL]);
									Utils.debug("Keyword: "
											+ steps[i][KEYWORD_CELL]);
									
									
									System.out.println("Keyword: "+keyword);

									try {
										
										
										
										switch (keyword) {

										case "start_tc":
											System.out
													.println("-----------------inside start_tc---------------------");
											curr_step = 0;
											String start_time = KeywordsImpl
													.get_timeStamp();
											tc_start_time = KeywordsImpl
													.getCurTime();
											curr_tc_name = steps[i][TC_NAME];
											log.info("*********************TEST CASE: "
													+ curr_tc_name
													+ "\t Execution Started*********************");
											System.out
													.println("\n\n************************************************************************");
											Utils.debug(start_time
													+ "  TestCase:\t"
													+ curr_tc_name
													+ "\tExecution Started"); // add
																				// this
																				// to
											Utils.debug("LOOP COUNT:"
													+ cnt);
											// report
											break;

										case "end_tc":
											System.out
													.println("-----------------inside end_tc---------------------");
											String end_time = KeywordsImpl
													.get_timeStamp();
											String tc_end_time = KeywordsImpl
													.getCurTime();
											Utils.debug(end_time
													+ "  TestCase:\t"
													+ curr_tc_name
													+ "\tExecution Completed");
											executionFlag = false;
											// Converting Start time and End
											// Time into Time Format
											DateFormat formatter = new SimpleDateFormat(
													"hh:mm:ss");
											Date start_time1 = (Date) formatter
													.parse(tc_start_time);
											Date end_time1 = (Date) formatter
													.parse(tc_end_time);
											// to Calculate the Time Difference
											long diff = end_time1.getTime()
													- start_time1.getTime();
											long diffS = diff / 1000 % 60;
											long diffM = diff / (60 * 1000)
													% 60;
											long diffH = diff
													/ (60 * 60 * 1000);
											System.out
													.println("The Difference is:"
															+ diffH
															+ ":"
															+ diffM
															+ ":"
															+ diffS);
											String timeDiff = +diffH + ":"
													+ diffM + ":" + diffS;
											System.out
													.println("The Difference is:"
															+ timeDiff);
											sl_no++;
											if (stepresult.contains("fail")) {
												tc_result = "Fail";
												System.out
														.println("FINAL TEST RESULT IS FAIL");
												WriteHTMLReport
														.updateFailureHtmlReport(
																curr_tc_name,
																tc_result,
																tc_start_time,
																tc_end_time,
																timeDiff, sl_no,sh);

												WriteXlReports.createContent(
														excelSheet,
														curr_tc_name,
														tc_result,
														tc_start_time,
														tc_end_time, timeDiff,sh);

												log.info("TEST CASE: "
														+ curr_tc_name
														+ "\tTest Result:"
														+ tc_result
														+ "\tExecution Duration "
														+ timeDiff);
												log.info("TEST CASE: "
														+ curr_tc_name
														+ "\tExecution Completed");
											} else {
												tc_result = "Pass";
												System.out
														.println("FINAL TEST RESULT IS PASS");
												WriteHTMLReport
														.updatePassHtmlReport(
																curr_tc_name,
																tc_result,
																tc_start_time,
																tc_end_time,
																timeDiff, sl_no,sh);
												WriteXlReports.createContent(
														excelSheet,
														curr_tc_name,
														tc_result,
														tc_start_time,
														tc_end_time, timeDiff,sh);
												log.info("*********************TEST CASE: "
														+ curr_tc_name
														+ "\t Execution Completed*********************");
												log.info("TEST CASE: "
														+ curr_tc_name
														+ "\tTest Result:"
														+ tc_result
														+ "\tExecution Duration "
														+ timeDiff);
											}
											System.out
													.println("\n************************************************************************");
											stepresult.clear();
											break;

										case "open_browser":
											System.out
													.println("-----------------inside open_browser---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												wd = KeywordsImpl.open_browser(
														wd,
														steps[i][PARAMS_CELL],
														false);
												System.out
														.println("Browser:"
																+ steps[i][PARAMS_CELL]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Browser: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Browser: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: Failed");
											}
											break;

										case "find_link_text":
											System.out
													.println("-----------------find_link_text---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												KeywordsImpl
														.find_link_text(
																wd,
																steps[i][LOCATE_STRING]);
												stepresult.add("pass");
												System.out
														.println("String found...");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Pass");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Failed");
											}
											break;

										case "execution_pause":
											System.out
													.println("-----------------execution_pause---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												if (KeywordsImpl
														.execution_pause(steps[i][PARAMS_CELL])) {
													stepresult.add("pass");
													System.out
															.println(stepresult
																	.add("pass"));
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][PARAMS_CELL]
															+ " Execution Status: Passed");
												}

												else
													stepresult.add("fail");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
												stepresult.add("fail");
											}
											break;

										case "validate_database_end2end":
											System.out
													.println("-----------------validate_database_end2end---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												String query = null;
												String expedOp = null;
												String column = null;
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													query = splitvalues[0];
													expedOp = splitvalues[1];
													column = splitvalues[2];
													System.out
															.println("splitvalues="
																	+ splitvalues[0]);
													System.out
															.println("splitvalues="
																	+ splitvalues[1]);
													System.out
															.println("splitvalues="
																	+ splitvalues[2]);
													strtmp4 = query;
													strtmp5 = expedOp;
												}

												// --> Starts Here
												if (strtmp4.contains("$")
														&& strtmp5
																.contains("$")
														&& isLoopingEnabled) {
													strtmp4 = strtmp4
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													strtmp5 = strtmp5
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(30000);
												}
												System.out
														.println("After:Expected Query :"
																+ strtmp4
																+ " Expected Output: "
																+ strtmp5);
												// --> Ends Here

												String wait = ReadConfiguration.MAX_DB_SEARCH_WAIT;
												if (KeywordsImpl
														.check_database_end2end(
																strtmp4,
																strtmp5,
																column, wait)) {
													System.out
															.println("Entry found in DB");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " DB Verification"
															+ " Execution Status: Passed");
												} else {
													stepresult.add("fail");
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " DB Verification"
															+ " Execution Status: Failed");
												}
												wd.manage()
														.timeouts()
														.implicitlyWait(
																100,
																TimeUnit.SECONDS);

											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " DB Verification"
														+ " Execution Status: Failed");
											}
											break;

										case "construct_query":
											System.out
													.println("-----------------construct_query---------------------");
											String assetId = null;
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												KeywordsImpl
														.construct_query(assetId);
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " DB Verification"
														+ " Execution Status: Failed");
											}
											break;

										case "vScroll":
											System.out
													.println("-----------------vScroll---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}

											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]
																+ " : "
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("COUNT:"
																+ cnt
																+ ",i value="
																+ i
																+ ",step value:"
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("Before:"
																+ steps[i][PARAMS_CELL]);

												strtmp1 = steps[i][PARAMS_CELL];

												if (((steps[i][LOCATE_TYPE]
														.equalsIgnoreCase("id")) || (steps[i][LOCATE_TYPE]
														.equalsIgnoreCase("xpath")))) {
													KeywordsImpl
															.scrollVertical(
																	wd,
																	steps[i][LOCATE_TYPE],
																	steps[i][LOCATE_STRING]);

												} else if (steps[i][PARAMS_CELL]
														.equalsIgnoreCase("end")) {
													KeywordsImpl
															.scrollVertical(wd,
																	"end");

												} else {

													KeywordsImpl
															.scrollVertical(wd);
												}

												System.out
														.println("After:"
																+ steps[i][PARAMS_CELL]);

												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ strtmp1
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "hScroll":
											System.out
													.println("-----------------hScroll---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]
																+ " : "
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("COUNT:"
																+ cnt
																+ ",i value="
																+ i
																+ ",step value:"
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("Before:"
																+ steps[i][PARAMS_CELL]);

												strtmp1 = steps[i][PARAMS_CELL];

												if (((steps[i][LOCATE_TYPE]
														.equalsIgnoreCase("id")) || (steps[i][LOCATE_TYPE]
														.equalsIgnoreCase("xpath")))) {
													KeywordsImpl
															.scrollhorizontal(
																	wd,
																	steps[i][LOCATE_TYPE],
																	steps[i][LOCATE_STRING]);

												} else {

													KeywordsImpl
															.scrollhorizontal(wd);
												}

												System.out
														.println("After:"
																+ steps[i][PARAMS_CELL]);

												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ strtmp1
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;
										// wd.manage().window().maximize();
										// break;

										case "captureScreenShot":
											System.out
													.println("-----------------capture_screenshot---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												CaptureScreenShot
														.captureScreenShot(wd,
																curr_tc_name,
																strScreenShotPath);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ "  Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ "  Execution Status: Failed");
												//e.printStackTrace();
											}
											break;

										case "navigate_to":
											System.out
													.println("-----------------navigate_to---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												Thread.sleep(1000);
												String value = replaceDynamicString(steps[i][PARAMS_CELL]);
												KeywordsImpl.navigate_to(wd,
														value);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " URL: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " URL: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "click_element":
											System.out
													.println("-----------------click_element---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]);
												wd.manage()
														.timeouts()
														.implicitlyWait(
																10,
																TimeUnit.SECONDS);
												KeywordsImpl
														.click_element(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Failed");
											}
											break;

										// case "click_dropdown":
										// Utils.debug("-----------------click_dropdown---------------------");
										/*
										 * By driver; WebElement select =
										 * driver.findElement(By.id("gender"));
										 * List<WebElement> options =
										 * select.findElements
										 * (By.tagName("option"));
										 * 
										 * for (WebElement option : options) {
										 * 
										 * if("Germany".equals(option.getText().trim
										 * ()))
										 * 
										 * option.click(); }
										 */

										/*
										 * String[] petStrings = { "All", "su" ,
										 * "Administrator" };
										 * 
										 * //Create the combo box, select item
										 * at index 4. //Indices start at 0, so
										 * 4 specifies the pig. JComboBox
										 * petList = new JComboBox(petStrings);
										 * petList.setSelectedIndex(2);
										 * //petList.addActionListener(this);
										 */// break;

										case "send_keys":
											System.out
													.println("-----------------send_keys---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]
																+ " : "
																+ steps[i][PARAMS_CELL]);
												
												String value = replaceDynamicString(steps[i][PARAMS_CELL]);

												System.out
														.println("COUNT:"
																+ cnt
																+ ",i value="
																+ i
																+ ",step value:"
																+ value);

												

												KeywordsImpl
														.send_keys(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																value);

												System.out
														.println("After:"
																+ steps[i][PARAMS_CELL]);

												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ value
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "send_keys_variable":
											System.out
													.println("-----------------send_keys_variable---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]
																+ " : "
																+ input_from_prev_step);

												Utils.debug("COUNT:"
														+ cnt + ",i value=" + i
														+ ",step value:"
														+ input_from_prev_step);

												System.out
														.println("Before:   send_keys_variable"
																+ input_from_prev_step);

												// strtmp1 =
												// steps[i][PARAMS_CELL];

												if (input_from_prev_step
														.endsWith("$")
														&& isLoopingEnabled) {
													input_from_prev_step = input_from_prev_step
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(3000);
												}
												Utils.debug("After:"
														+ input_from_prev_step);

												KeywordsImpl
														.send_keys(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																input_from_prev_step);

												System.out
														.println("After:"
																+ steps[i][PARAMS_CELL]);

												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ input_from_prev_step
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "verify_element":
											System.out
													.println("-----------------verify_element---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												KeywordsImpl
														.verify_element(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Failed");
											}
											break;
										/*
										 * Below case to call runSshCommand
										 * which runs and gives back the
										 * terminal command outputs
										 */
										case "Run_ssh_command":
											System.out
													.println("-----------------Run_ssh_command---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												String user = null;
												String password = null;
												String command = null;
												if (steps[i][LOCATE_STRING]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][LOCATE_STRING]);
													user = splitvalues[0];
													password = splitvalues[1];
													command = splitvalues[2];
													Utils.debug("user="
															+ splitvalues[0]);
													Utils.debug("pwd="
															+ splitvalues[1]);
													System.out
															.println("command="
																	+ splitvalues[2]);
												}
												KeywordsImpl.runSshCommand(wd,
														user, password,
														command,
														steps[i][PARAMS_CELL]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Failed");
											}
											break;
										case "press_enter":
											System.out
													.println("-----------------press_enter---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											KeywordsImpl.press_enter(wd,
													steps[i][LOCATE_TYPE],
													steps[i][LOCATE_STRING]);
											System.out
													.println("Called Pressed Enter");
											break;

										case "store_text":
											System.out
													.println("-----------------store_text---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											break;

										case "close_browser":
											System.out
													.println("-----------------close_browser---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												KeywordsImpl.close_browser(wd);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "validate_database":
											System.out
													.println("-----------------validate_database---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												String query = null;
												String expedOp = null;
												String column = null;
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													query = splitvalues[0];
													expedOp = splitvalues[1];
													column = splitvalues[2];
													System.out
															.println("splitvalues="
																	+ splitvalues[0]);
													System.out
															.println("splitvalues="
																	+ splitvalues[1]);
													System.out
															.println("splitvalues="
																	+ splitvalues[2]);
													strtmp4 = query;
													strtmp5 = expedOp;
												}

												// --> Starts Here
												if (strtmp4.contains("$")
														&& strtmp5
																.contains("$")
														&& isLoopingEnabled) {
													strtmp4 = strtmp4
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													strtmp5 = strtmp5
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(30000);
												}
												System.out
														.println("After:Expected Query :"
																+ strtmp4
																+ " Expected Output: "
																+ strtmp5);
												// --> Ends Here

												if (KeywordsImpl
														.check_database(
																strtmp4,
																strtmp5, column)) {
													System.out
															.println("DATABASE CHECK IS SUCCESSFULL");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " DB Verification"
															+ " Execution Status: Passed");
												} else {
													stepresult.add("fail");
													System.out
															.println("DATABASE CHECK IS UNSUCCESSFULL");
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " DB Verification"
															+ " Execution Status: Failed");
												}
												wd.manage()
														.timeouts()
														.implicitlyWait(
																100,
																TimeUnit.SECONDS);

											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " DB Verification"
														+ " Execution Status: Failed");
											}
											break;

										case "validate_currentURL":
											System.out
													.println("-----------------validate_currentURL---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											break;

										case "validate_value":
											System.out
													.println("-----------------validate_value---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											break;

										case "execute_javascript":
											System.out
													.println("-----------------execute_javaScript---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												KeywordsImpl
														.execute_javascript(wd,
																steps[i][6]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Java Script"
														+ steps[i][6]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Java Script"
														+ steps[i][6]
														+ " Execution Status: failed . The reason is "+e.getMessage());
											}
											break;

										case "select_menu":
											System.out
													.println("-----------------select_menu---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												wd.switchTo()
														.frame("mainFrame");

												List<WebElement> menus = wd
														.findElements(By
																.id("menuLevel1SelectDiv"));
												if (menus.isEmpty()
														|| menus == null) {
													System.out
															.println("No element found");
												}
												Iterator<WebElement> itr = menus
														.iterator();
												while (itr.hasNext()) {
													if (itr.next().equals(
															"Configuration ?"))
														itr.next().click();
													Utils.debug(String.valueOf(itr
															.next()));
												}
												menus.get(3).click();
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Menu Selected Successfully");
											} catch (Exception e) {
												stepresult.add("fail");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Menu Selection Failed");
												//e.printStackTrace();
											}
											break;
										/*
										 * Below case modified in such a way
										 * that it can skip the creation steps
										 * if the entity is already present eg-
										 * if CPS tool is already there then the
										 * CPS creation part can be skipped
										 */
										case "validate_GUI_table":
											System.out
													.println("-----------------validate_GUI_table---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													table = splitvalues[0];
													tableRow = splitvalues[1];
													expectedOutput = splitvalues[2];
												}

												// --->>Looping Logic Starts
												// Here
												strtmp3 = expectedOutput;

												if (strtmp3.endsWith("$")
														&& isLoopingEnabled) {
													strtmp3 = strtmp3
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(30000);
												}
												Utils.debug("After:"
														+ strtmp3);

												if (KeywordsImpl
														.validate_GUI_table(wd,
																table,
																tableRow,
																strtmp3)) {
													/*
													 * Inserting the below logic
													 * to skip the creation part
													 * if the entity is already
													 * available and proceed
													 * with next steps
													 */
													int i1 = 0;
													System.out
															.println(steps[i][LOCATE_STRING]);

													if (KeywordsImpl
															.result(steps[i][LOCATE_STRING])) {

														i1 = Integer
																.parseInt(steps[i][LOCATE_STRING]);
														System.out
																.println("current i value is :"
																		+ i1);
														i = i + i1;
													}
													System.out
															.println("Validation GUI Successful");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Passed");
												} else {
													stepresult.add("fail");
													CaptureScreenShot
															.captureErrorScreenShot(
																	wd,
																	curr_tc_name,
																	strScreenShotPath);
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: failed");
												}

											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ strtmp3
														+ " Execution Status: failed");
											}
											break;

										case "click_table":
											System.out
													.println("-----------------click_table---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}

											try {
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													table = splitvalues[0];
													tableRow = splitvalues[1];
													expectedOutput = splitvalues[2];
												}
												if (KeywordsImpl
														.click_table(
																wd,
																table,
																tableRow,
																expectedOutput,
																steps[i][LOCATE_STRING])) {
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Passed");
													break;
												} else {
													stepresult.add("fail");
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Failed");
												}
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: Failed");
											}
											break;

										/*
										 * case "click_table_checkbox":
										 * Utils.debug(
										 * "-----------------click_table_checkbox---------------------"
										 * ); curr_step = curr_step + 1; if
										 * (stepresult.contains("fail")) {
										 * break; }
										 * 
										 * try { if
										 * (steps[i][PARAMS_CELL].contains(":"))
										 * { splitvalues = KeywordsImpl
										 * .splitParams(steps[i][PARAMS_CELL]);
										 * table = splitvalues[0]; tableRow =
										 * splitvalues[1]; expectedOutput =
										 * splitvalues[2]; } if
										 * (KeywordsImpl.click_table_checkbox
										 * (wd, table, tableRow,
										 * expectedOutput,steps
										 * [i][LOCATE_STRING])) {
										 * Utils.debug("Clicked");
										 * stepresult.add("pass");
										 * log.info("Step: " + curr_step + "." +
										 * steps[i][KEYWORD_CELL] +
										 * " Execution Status: Passed"); //
										 * break; } else {
										 * Utils.debug("Element not found"
										 * ); stepresult.add("fail");
										 * CaptureScreenShot
										 * .captureErrorScreenShot(wd,
										 * curr_tc_name, strScreenShotPath);
										 * log.error("Step: " + curr_step + "."
										 * + steps[i][KEYWORD_CELL] +
										 * " Execution Status: Failed"); //
										 * break; }
										 * 
										 * } catch (Exception e) { // TODO
										 * Auto-generated catch block
										 * //e.printStackTrace();
										 * stepresult.add("fail");
										 * CaptureScreenShot
										 * .captureErrorScreenShot(wd,
										 * curr_tc_name, strScreenShotPath);
										 * log.error("Step: " + curr_step + "."
										 * + steps[i][KEYWORD_CELL] +
										 * " Execution Status: Failed"); }
										 * break;
										 */
										case "click_table_checkbox":
											System.out
													.println("-----------------click_table_checkbox---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}

											try {
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													table = splitvalues[0];
													tableRow = splitvalues[1];
													expectedOutput = splitvalues[2];
												}
												if (KeywordsImpl
														.click_table_checkbox(
																wd, table,
																tableRow,
																expectedOutput)) {
													System.out
															.println("Clicked");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Passed");
													// break;
												} else {
													System.out
															.println("Element not found");
													stepresult.add("fail");
													CaptureScreenShot
															.captureErrorScreenShot(
																	wd,
																	curr_tc_name,
																	strScreenShotPath);
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Failed");
													// break;
												}

											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: Failed");
											}
											break;
										case "click_table_link":
											System.out
													.println("-----------------click_table_link---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}

											try {
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													table = splitvalues[0];
													tableRow = splitvalues[1];
													expectedOutput = splitvalues[2];
												}
												if (KeywordsImpl
														.click_table_link(
																wd,
																table,
																tableRow,
																expectedOutput,
																steps[i][LOCATE_STRING])) {
													System.out
															.println("Clicked");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Passed");
													// break;
												} else {
													System.out
															.println("Element not found");
													System.out
															.println("Not Clicked");
													stepresult.add("fail");
													CaptureScreenShot
															.captureErrorScreenShot(
																	wd,
																	curr_tc_name,
																	strScreenShotPath);
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: failed");
												}

											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: failed");
											}
											break;
										/*
										 * Below case to call click_link method
										 * which can click user requested link
										 * from the current page
										 */
										case "click_link":
											System.out
													.println("-----------------click_link---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												if (KeywordsImpl.click_link(wd,
														steps[i][PARAMS_CELL])) {
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][PARAMS_CELL]
															+ " Execution Status: Passed");
												}

												else
													stepresult.add("fail");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
												stepresult.add("fail");
											}
											break;
										case "save_table":
											System.out
													.println("-----------------save_table---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												if (KeywordsImpl.save_table(wd,
														steps[i][PARAMS_CELL])) {
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][PARAMS_CELL]
															+ " Execution Status: Passed");
												}

												else
													stepresult.add("fail");
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
												stepresult.add("fail");
											}
											break;

										case "wait":
											System.out
													.println("-----------------wait---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
											//	 Thread.sleep(5000);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ "3 Seconds"
														+ " Execution Status: Passed");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												stepresult.add("fail");
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ "3 Seconds"
														+ " Execution Status: failed");
											}
											break;

										case "wait_explicit":
											System.out
													.println("-----------------wait_explicit---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												KeywordsImpl.wait_explicit(wd,
														steps[i][KEYWORD_CELL],
														steps[i][LOCATE_TYPE],
														steps[i][PARAMS_CELL]);
												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {

												//e.printStackTrace();
												stepresult.add("fail");
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "execute_command":
											System.out
													.println("-----------------execute_command---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;

											}
											try {
												strtmp2 = steps[i][PARAMS_CELL];

												if (strtmp2.endsWith("$")
														&& isLoopingEnabled) {
													strtmp2 = strtmp2
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(30000);
												}
												Utils.debug("After:"
														+ strtmp2);

												KeywordsImpl
														.send_keys(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																strtmp2);

												Boolean result = KeywordsImpl
														.execute_command(
																ReadConfiguration.ASSET_FLOW_IP,
																ReadConfiguration.ASSET_FLOW_USER,
																ReadConfiguration.ASSET_FLOW_PASSWORD,
																strtmp2);

												System.out
														.println("Result main = "
																+ result);
												if (result == true) {
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Command: "
															+ strtmp2
															+ " Execution Status: Passed");
												} else {
													stepresult.add("fail");
													log.error("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Command: "
															+ strtmp2
															+ " Execution Status: failed");
												}
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Command: "
														+ strtmp2
														+ " Execution Status: failed");
											}
											break;

										case "execute_command_results":
											System.out
													.println("-----------------execute_command_results---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;

											}
											try {
												strtmp2 = steps[i][PARAMS_CELL];

												if (strtmp2.endsWith("$")
														&& isLoopingEnabled) {
													strtmp2 = strtmp2
															.replaceAll(
																	"\\$",
																	String.valueOf(cnt));
													Thread.sleep(30000);
												}
												Utils.debug("After:"
														+ strtmp2);

												KeywordsImpl
														.send_keys(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																strtmp2);

												input_from_prev_step = KeywordsImpl
														.execute_command_results(
																ReadConfiguration.ASSET_FLOW_IP,
																ReadConfiguration.ASSET_FLOW_USER,
																ReadConfiguration.ASSET_FLOW_PASSWORD,
																strtmp2,
																input_from_prev_step);

												// Utils.debug("Result main = "
												// + result);
												System.out
														.println("input_from_prev_step main = "
																+ input_from_prev_step);
												// if (result == true) {
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Command: "
														+ strtmp2
														+ " Execution Status: Passed");
												/*
												 * } else {
												 * stepresult.add("fail");
												 * log.error("Step: " +
												 * curr_step + "." +
												 * steps[i][KEYWORD_CELL] +
												 * " Command: " + strtmp2 +
												 * " Execution Status: failed");
												 * }
												 */
											} catch (Exception e) {
												stepresult.add("fail");
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Command: "
														+ strtmp2
														+ " Execution Status: failed");
											}
											break;

										case "wait_implicit":
											System.out
													.println("-----------------wait_implicit---------------------");
											curr_step = curr_step + 1;

											if (stepresult.contains("fail")) {
												break;
											}
											try {
												wd.manage()
														.timeouts()
														.implicitlyWait(
																Long.parseLong(steps[i][PARAMS_CELL]),
																TimeUnit.SECONDS);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												stepresult.add("fail");
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: failed");
											}
											break;

										case "refresh_page":
											System.out
													.println("-----------------refresh_page---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												wd.navigate().refresh();
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Page Refresh Status: Passed");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Page Refresh Status: failed");
											}
											break;

										case "accept_alert":
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											System.out
													.println("-----------------alert---------------------");

											break;

										case "switch_alert":
											System.out
													.println("-----------------switch_alert---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												wd.switchTo().activeElement();
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Switch to alert operation Successful");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Switch to alert operation failed");
											}
											break;

										case "click_table_image":
											System.out
													.println("-----------------click_table_image---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}

											try {
												if (steps[i][PARAMS_CELL]
														.contains(":")) {
													splitvalues = KeywordsImpl
															.splitParams(steps[i][PARAMS_CELL]);
													table = splitvalues[0];
													tableRow = splitvalues[1];
													expectedOutput = splitvalues[2];
												}
												if (KeywordsImpl
														.click_table_image(
																wd,
																table,
																tableRow,
																expectedOutput,
																steps[i][LOCATE_STRING])) {
													System.out
															.println("Clicked");
													stepresult.add("pass");
													log.info("Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: Passed");
													// break;
												} else {
													System.out
															.println("Element not found");
													System.out
															.println("Not Clicked");
													stepresult.add("fail");
													CaptureScreenShot
															.captureErrorScreenShot(
																	wd,
																	curr_tc_name,
																	strScreenShotPath);
													log.error("TEST CASE: "
															+ curr_tc_name
															+ "Step: "
															+ curr_step
															+ "."
															+ steps[i][KEYWORD_CELL]
															+ " Execution Status: failed");
												}
												wd.switchTo().frame("mainSet");
											} catch (Exception e) {
												//e.printStackTrace();
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("TEST CASE: "
														+ curr_tc_name
														+ "Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: failed");
											}
											break;
										case "LogOut": 
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												String jscript = "logout();";
												KeywordsImpl.Logout(wd, jscript);
											} catch (Exception e1) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("TEST CASE: "
														+ curr_tc_name
														+ "Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Execution Status: failed");
												
												System.out
														.println("Error in Logout");
											}
											break;

										
										/*
										 * case "Help": { String
										 * jscript="showHelp();";
										 * KeywordsImpl.Logout(wd, jscript);
										 * 
										 * 
										 * }
										 */

										/*
										 * Adding below three keywords
										 * "switchToFrame" , "readAndCopyXml" ,
										 * "switchToMain" to handle frames
										 */
										case "switchToFrame":
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											switch (steps[i][LOCATE_TYPE]) {
											case "string":
												wd = KeywordsImpl
														.switchToFrame(
																wd,
																steps[i][LOCATE_STRING]);
												break;
											case "xpath":
												wd = wd.switchTo()
														.frame(wd.findElement(By
																.xpath(steps[i][LOCATE_STRING])));
												break;
											}
											break;

										case "readAndCopyXml":
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											KeywordsImpl.readAndCopyXml(wd,
													steps[i][PARAMS_CELL],
													steps[i][LOCATE_TYPE],
													steps[i][LOCATE_STRING]);
											break;

										case "switchToMain":
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											wd = KeywordsImpl.switchToMain(wd);
											break;
										case "select_from_dropdown_menu":
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]);
												wd.manage()
														.timeouts()
														.implicitlyWait(
																10,
																TimeUnit.SECONDS);
												KeywordsImpl
														.select_from_dropdown_menu(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Passed");
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												stepresult.add("fail");
												//e.printStackTrace();
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Element: "
														+ steps[i][LOCATE_STRING]
														+ " Execution Status: Failed");
											}
											break;
										case "click_dropdown":
											System.out
													.println("-----------------click_dropdown---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {
												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]);
												wd.manage()
														.timeouts()
														.implicitlyWait(
																10,
																TimeUnit.SECONDS);
												KeywordsImpl
														.click_element_dropdown(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																steps[i][PARAMS_CELL]);
												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: Passed");
											} catch (Exception e) {

												//e.printStackTrace();
												stepresult.add("fail");
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Wait Duration: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;

										// Implementation of find and click
										// element keyword
										case "find_and_click_element":
											System.out
													.println("-----------------send_keys---------------------");
											curr_step = curr_step + 1;
											if (stepresult.contains("fail")) {
												break;
											}
											try {

												System.out
														.println(steps[i][LOCATE_TYPE]
																+ " : "
																+ steps[i][LOCATE_STRING]
																+ " : "
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("COUNT:"
																+ cnt
																+ ",i value="
																+ i
																+ ",step value:"
																+ steps[i][PARAMS_CELL]);

												System.out
														.println("Before:"
																+ steps[i][PARAMS_CELL]);

												strtmp1 = steps[i][PARAMS_CELL];

												KeywordsImpl
														.find_and_click_element(
																wd,
																steps[i][LOCATE_TYPE],
																steps[i][LOCATE_STRING],
																steps[i][PARAMS_CELL]);

												System.out
														.println("After:"
																+ steps[i][PARAMS_CELL]);

												stepresult.add("pass");
												log.info("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ strtmp1
														+ " Execution Status: Passed");
											} catch (Exception e) {
												stepresult.add("fail");
												CaptureScreenShot
														.captureErrorScreenShot(
																wd,
																curr_tc_name,
																strScreenShotPath);
												//e.printStackTrace();
												log.error("Step: "
														+ curr_step
														+ "."
														+ steps[i][KEYWORD_CELL]
														+ " Data: "
														+ steps[i][PARAMS_CELL]
														+ " Execution Status: failed");
											}
											break;
										// Implementation of Scroll_End
										/*
										 * case "Scroll_end":
										 * Utils.debug(
										 * "-----------------send_keys---------------------"
										 * ); curr_step = curr_step + 1; if
										 * (stepresult.contains("fail")) {
										 * break; } try {
										 * 
										 * Utils.debug(steps[i][LOCATE_TYPE
										 * ] + " : " + steps[i][LOCATE_STRING] +
										 * " : " + steps[i][PARAMS_CELL]);
										 * 
										 * Utils.debug("COUNT:" + cnt +
										 * ",i value=" + i + ",step value:" +
										 * steps[i][PARAMS_CELL]);
										 * 
										 * Utils.debug("Before:" +
										 * steps[i][PARAMS_CELL]);
										 * 
										 * 
										 * 
										 * 
										 * KeywordsImpl.scroll_end(wd);
										 * 
										 * stepresult.add("pass");
										 * log.info("Step: " + curr_step + "." +
										 * steps[i][KEYWORD_CELL] + " Data: " +
										 * strtmp1 +
										 * " Execution Status: Passed"); } catch
										 * (Exception e) {
										 * stepresult.add("fail");
										 * CaptureScreenShot
										 * .captureErrorScreenShot(wd,
										 * curr_tc_name, strScreenShotPath);
										 * //e.printStackTrace();
										 * log.error("Step: " + curr_step + "."
										 * + steps[i][KEYWORD_CELL] + " Data: "
										 * + steps[i][PARAMS_CELL] +
										 * " Execution Status: failed"); }
										 * break;
										 */

										}

									} catch (Throwable e) {
										stepresult.add("fail");
										CaptureScreenShot
												.captureErrorScreenShot(wd,
														curr_tc_name,
														strScreenShotPath);
									//	//e.printStackTrace();
										log.error("Step: " + curr_step + "."
												+ steps[i][KEYWORD_CELL]
												+ " Data: "
												+ steps[i][PARAMS_CELL]
												+ " Execution Status: failed");
										
										System.out.println("Inside Key word exception block");

									}

								}

							}

						}
						/*
						 * Utils.debug("End of Step file "+filearray);
						 * Utils.debug("\n\n");
						 */
						
					} catch (Exception e) {
						e.printStackTrace();
						log.error("Sheet : " + sh + " Execution Status: failed");
					}
					finally
					{
						Utils.debug("Before remove data from list sheet :"+dataL.size());
					dataL.remove(data);
					Utils.debug("Removed data from list sheet :"+dataL.size());

						
					}
					System.out.println("End of Data SIZE in the list :"+dataL.size());

				}

				log.info("Step File in use: " + stepFile);
			}

		}

		workbook.write();
		workbook.close();
		WriteHTMLReport.closeReport();
		//KeywordsImpl.close_browser(wd);
	}

	// Code for replacement of strings
	private static String replaceDynamicString(String input) {

		StrSubstitutor sub = new StrSubstitutor(configurationMap);
		return sub.replace(input);
	}
	
	
}

