package com.beroe.reporting;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverException;

import com.beroe.utility.Utils;
import com.beroe.keywords.KeywordsImpl;
import org.openqa.selenium.TakesScreenshot;

public class CaptureScreenShot {
public static String date = KeywordsImpl.get_timeStamp();
public static void captureScreenShot(WebDriver wd,String tc_name, String strResultPath) throws IOException {
		//String date = KeywordsImpl.get_timeStamp();
		File screenshot = ((TakesScreenshot)wd).getScreenshotAs(OutputType.FILE);
		System.out.println(screenshot.getAbsolutePath());
		String filename = strResultPath+"ScreenShot"+"_"+tc_name +"_" +date+ ".png";
		FileUtils.copyFile(screenshot, new File(filename));
	}

public static void captureErrorScreenShot(WebDriver wd,String tc_name, String strResultPath) {
	//String date = KeywordsImpl.get_timeStamp();
	try {
		File screenshot = ((TakesScreenshot)wd).getScreenshotAs(OutputType.FILE);
		System.out.println(screenshot.getAbsolutePath());
		String filename = strResultPath+"Error_ScreenShot"+"_"+tc_name +"_" +date+ ".png";
	//	Thread.sleep(20*1000);
		FileUtils.copyFile(screenshot, new File(filename));
		
	} catch (Exception e) {
		
		System.err.println("Unable to Capture the Screen Shot for the testcase :"+
		  tc_name+" . The Reason :"+e.getMessage() );
	}
}

/*
public static void captureErrorScreenShot(WebDriver wd,String tc_name, String strResultPath) throws IOException, InterruptedException {
	//String date = KeywordsImpl.get_timeStamp();
	
		File screenshot = ((TakesScreenshot)wd).getScreenshotAs(OutputType.FILE);
		System.out.println(screenshot.getAbsolutePath());
		String filename = strResultPath+"Error_ScreenShot"+"_"+tc_name +"_" +date+ ".png";
		Thread.sleep(120*1000);
		System.out.println("Started Copying file .............");
		FileUtils.copyFile(screenshot, new File(filename));
	*/
}

//}