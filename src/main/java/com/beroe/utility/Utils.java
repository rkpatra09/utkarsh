package com.beroe.utility;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.seleniumhq.jetty7.util.log.Log;

public class Utils {
	
	public static Logger log = Logger.getLogger("Test");
	
	public static void deleteCookies(WebDriver driver) {
	//	driver.manage().deleteAllCookies();
		System.out.println("Deleting cookies before launcing browser"+driver.manage().getCookies());
		//driver.get("http://192.168.201.62:8080/eam/");
		//System.out.println("Deleting cookies after launcing browser"+driver.manage().getCookies());
		//driver.manage().deleteAllCookies();
	}
	
	/**
	 * 
	 * 
	 * @param message
	 * @param status
	 */
	
	
	public static void debug(String message,boolean status)
	{
		
		if(status)
		{
			//log.//debug(message);
			log.info(message);
		}
	}
	
	/**
	 * 
	 * 
	 * @param message
	 * @param status
	 */
	public static void debug(String message)
	{
		debug(message,true);
	}


}
