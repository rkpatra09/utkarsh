package com.beroe.keywords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
//import com.beroe.connectivity.DBUtil;

//import com.beroe.connectivity.SSHChannelImpl;




import com.beroe.connectivity.DBUtil;
import com.beroe.connectivity.SSHChannelImpl;
import com.beroe.utility.ReadConfiguration;
import com.google.common.base.Function;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.SftpException;
//import com.sun.istack.internal.NotNull;







//import com.sun.org.apache.bcel.internal.generic.Select;
import org.openqa.selenium.support.ui.Select;
public class KeywordsImpl {

	private static Date cur_dt = null;
	private static final String splitchar = ":";
	public static DesiredCapabilities cap = null;
	
	private static final long IMPLICIT_WAIT=30;

	public static void tableContent_Display(WebDriver wd, WebElement we) {
		WebElement table_element = findElement((SearchContext)wd,wd,By
				.id("drowBoxListGridTable_mainDataGrid_Content_TR"));
		List<WebElement> tr_collection = findElements(table_element,By.xpath("id('drowBoxListGridTable_mainDataGrid_Content_0_Table')/tbody/tr"),wd);

		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ tr_collection.size());
		int row_num, col_num;
		row_num = 1;
		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				System.out.println("row # " + row_num + ", col # " + col_num
						+ "text=" + tdElement.getText());
				col_num++;
			}
			row_num++;
		}
	}

	public static void find_link_text(WebDriver wd, String text) {
		try {
			findElement((SearchContext)wd,wd,By.linkText(text));
		} catch (NoSuchElementException missing) {
			System.out.println(missing.toString());
		}
	}

	public static boolean execution_pause(String steps) {
		boolean result = false;
		System.out.println("Inside execution pause for " + steps + " mins");
		int count = Integer.parseInt(steps);
		for (int i = 1; i <= count; i++) {
			try {
				// thread to sleep for 1000 milliseconds
				Thread.sleep(10 * 1000);
				System.out.println(i + " mins waited till now");
				if (i == count) {
					System.out.println("Reached time out...");
					return true;
				}
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted!!!");
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void execution_pause_notrequired() throws InterruptedException {
		
				// thread to sleep for 1000 milliseconds
				Thread.sleep(5000);
				
	}

	public static Boolean check_database_end2end(String query,
			String expetedOutput, String column, String wait) {
		ResultSet rs = null;
		Boolean result = false;
		int max_wait_period = Integer.parseInt(wait);
		for (int i = 1; i <= max_wait_period; i++) {
			try {
				System.out.println("Inside check_database_end2end loop "
						+ query + ":" + expetedOutput + ":" + column);
				rs = DBUtil.executeQuery(query);
				try {
					System.out.println("rs lenght = " + rs.getRow());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					while (rs.next()) {
						String status = rs.getString(column.trim());
						System.out.println("status = " + status.toString());
						if (status.equals(expetedOutput)) {
							System.out
									.println("Expected result matched with DB");
							return true;
						}
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
				System.out.println("Result = " + result);

				// thread to sleep for 1000 milliseconds
				Thread.sleep(60 * 1000);
				System.out.println(i + " mins waited till now");
				if (i == max_wait_period) {
					System.out.println("Reached Time out...");
					break;
				}
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted!!!");
				e.printStackTrace();
			}
		}
		return result;

	}

	public static boolean execute_command(String IP, String user, String pwd,
			String command) {
		boolean result = false;
		SSHChannelImpl instance = new SSHChannelImpl(user, pwd, IP, "");
		String errorMessage = instance.connect();
		if (errorMessage != null) {
			System.out.println(errorMessage);
		}
		try {
			try {
				result = instance.sendCommand(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Result from sendcommand");
			System.out.println("Result = " + result);
		} catch (SftpException e) {
			e.printStackTrace();
		} 
		instance.close();
		return result;
	}
	
	public static String execute_command_results(String IP, String user, String pwd,
			String command, String output) {
		String result = null;
		//Object commandOutput = null;
		SSHChannelImpl instance = new SSHChannelImpl(user, pwd, IP, "");
		String errorMessage = instance.connect();
		if (errorMessage != null) {
			System.out.println(errorMessage);
		}
		try {
			result = instance.sendCommandresults(command, output);
			System.out.println("Result from sendcommand");
			System.out.println("Result = " + result);
			System.out.println("output = " + output);
		} catch (SftpException | IOException e) {
			e.printStackTrace();
		} 
		instance.close();
		System.out.println("Result = " + result);
		return result;
	}
	
	public static String construct_query(String result){
		String query = null;
		query = "SELECT * FROM `eam_asset` WHERE STATUS =4 AND provider_asset_id LIKE '%"+result+"%';";
		return result;
	}
	public static boolean click_table_checkbox(WebDriver wd, String table,
			String tableRow, String siteToDistribute) {
		boolean result = false;
		System.out.println("inside select_checkbox  = " + siteToDistribute);
//		SearchContext context = (SearchContext)wd;
//		waitElementIsVisible(context, By.id(table),wd);
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table));
		
//		waitElementIsVisible(table_element, By
//				.xpath(tableRow),wd);
		List<WebElement> tr_collection = findElements(table_element,By
				.xpath(tableRow),wd);

		int row_num, col_num;
		row_num = 1;
		WebElement checkbox = null;
		for (WebElement trElement : tr_collection) {
			
//			waitElementIsVisible(trElement, By
//					.xpath("td"),wd);
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);
			checkbox = findElement(trElement,wd,By.xpath(".//input"));
			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				if (tdElement.getText().equalsIgnoreCase(siteToDistribute)) {
					System.out.println("checkbox = " + checkbox.getTagName());
					System.out.println("row # " + row_num + ", col # "
							+ col_num + "text=" + tdElement.getText());
					System.out.println("checkbox = " + checkbox.getTagName());
					checkbox.click();
					System.out.println("Site exists and selecting it");
					return true;
				}
				col_num++;
			}
			row_num++;
		}
 	return result;
	}
	
	/*public static boolean click_table_checkbox(WebDriver wd, String table,
			String tableRow, String siteToDistribute,String image) {
		
		boolean result = false;
		System.out.println("inside select_checkbox  = " + siteToDistribute);
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table));
		List<WebElement> tr_collection = table_element.findElements(By
				.xpath(tableRow));

		int row_num, col_num;
		row_num = 1;
		WebElement checkbox = null;
		
		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = trElement.findElements(By
					.xpath("td"));
			checkbox = trElement.findElement(By.xpath(".//input"));
			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
			if (tdElement.getText().equalsIgnoreCase(siteToDistribute)) {
					if((image != null) && (!("".equals(image.trim()))))
					{
						System.out.println("image --->"+image.trim());
						String locateString = "//img[contains(@src,'" + image.trim()
								+ "')]";
						
						WebElement status = trElement.findElement(By.xpath(locateString));
						System.out.println("status tag name = "
								+ status.getTagName());
					}
					System.out.println("checkbox = " + checkbox.getTagName());
					System.out.println("row # " + row_num + ", col # "
							+ col_num + "text=" + tdElement.getText());
					System.out.println("checkbox = " + checkbox.getTagName());
					checkbox.click();
					System.out.println("Site exists and selecting it");
					return true;
				}
				col_num++;
			}
			row_num++;
	}
	return result;
	}

*/
	public static boolean click_table(WebDriver wd, String table,
			String tableRow, String siteToDistribute, String locateString) {
		boolean result = false;

		System.out.println("Inside select_checkbox " + table + " " + tableRow
				+ " " + siteToDistribute + " " + locateString);
		System.out.println("inside edit");
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table));
		List<WebElement> tr_collection = findElements(table_element,By
				.xpath(tableRow),wd);

		int row_num, col_num;
		row_num = 1;
		WebElement checkbox = null;
		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);
			System.out
					.println("td_collection.size() = " + td_collection.size());
			checkbox = findElement(trElement,wd,By.linkText(locateString));
			System.out.println("checkbox = " + checkbox.getTagName());
			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				System.out.println("row # " + row_num + ", col # " + col_num
						+ "text=" + tdElement.getText());
				if (tdElement.getText().equals(siteToDistribute)) {
					checkbox.click();
					System.out.println("Site exists and selecting it");
					return true;
				}
				col_num++;
			}
			row_num++;
		}
		return result;
	}
	/*
	 * Below method to give all available links in a page and click the one as per the user input
	 */
	public static boolean click_link(WebDriver wd, String locateString ) throws Exception
	{
		boolean result = false;
		
		List<WebElement> link=findElements((SearchContext)wd,By.tagName("a"),wd);
		for(int i=0;i<link.size();i++){
		System.out.println(link.get(i).getText());
		}
		for(int i=0;i<link.size();i++){

		    
	    if (link.get(i).getText().contains(locateString)){

		        link.get(i).click();
	      result = true;
	      Thread.sleep(20000L);
		        break;

		    }

		}
		
		return result;
		
	}
	public static boolean click_table_link(WebDriver wd, String table,
			String tableRow, String siteToDistribute, String locateString) {
		boolean result = false;
		System.out.println("inside select_checkbox  = " + siteToDistribute);
		System.out.println("locateString = " + locateString);
		
		SearchContext context = (SearchContext)wd;
		waitElementIsVisible(context, By.id(table),wd);
		
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table));
		
		waitElementIsVisible(table_element, By
				.xpath(tableRow),wd);
		
		List<WebElement> tr_collection = findElements(table_element,By
				.xpath(tableRow),wd);

		int row_num, col_num;
		row_num = 1;
		WebElement checkbox = null;
		for (WebElement trElement : tr_collection) {
			
			waitElementIsVisible(trElement, By
					.xpath("td"),wd);
			
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);
			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				if (tdElement.getText().equalsIgnoreCase(siteToDistribute)) {
					waitElementIsVisible(trElement, By.linkText(locateString),wd);
					checkbox = findElement(trElement,wd,By.linkText(locateString));
					System.out.println("checkbox = " + checkbox.getTagName());
					System.out.println("row # " + row_num + ", col # "
							+ col_num + "text=" + tdElement.getText());
					checkbox.click();
					System.out.println("Site exists and selecting it");
					return true;
				}
				col_num++;
			}
			row_num++;
		}
		return result;
	}

	public static boolean validate_GUI_table(WebDriver wd, String table,
			String tableRow, String expectedCell) {
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table));
		System.out.println("TableROW == " + tableRow);
		List<WebElement> tr_collection = findElements(table_element,By
				.xpath(tableRow),wd);
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ tr_collection.size());
		int row_num, col_num;
		row_num = 1;

		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);

			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				System.out.println("row # " + row_num + ", col # " + col_num
						+ "text=" + tdElement.getText());
				if (tdElement.getText().equals(expectedCell)) {
					System.out.println("Dropbox exists");
					return true;
				}
				col_num++;
			}
			row_num++;
		}
		return false;
	}

	public static void execute_javascript(WebDriver wd, String jscript) {
		try{
		wd.switchTo().frame("mainFrame");
		}catch(NoSuchElementException | StaleElementReferenceException | NoSuchFrameException e){
			
			System.out.println("Error in switching to mainframe....");
		}
		
		
		JavascriptExecutor js = (JavascriptExecutor) wd;
		
		System.out.println("jscript = " + jscript);
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		ExpectedCondition<Boolean> pageLoadCondition = new
		        ExpectedCondition<Boolean>() {
		            public Boolean apply(WebDriver driver) {
		                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
		            }
		        };
		    WebDriverWait wait = new WebDriverWait(wd,IMPLICIT_WAIT );
		    wait.until(pageLoadCondition);
		    
		js.executeScript(jscript);
		System.out.println("Executed javascript");
	}

	public static String[] splitParams(String params) {
		String[] splitValues = params.split(splitchar);
		return splitValues;
	}

	public static Boolean check_database(String query, String expetedOutput,
			String column) {
		ResultSet rs = null;
		Boolean result = false;
		System.out.println("Inside check_database " + query + ":"
				+ expetedOutput + ":" + column);
		
		System.out.println("check_database:query = " + query );
		System.out.println("check_database:expetedOutput = " + expetedOutput );
		System.out.println("check_database:column = " + column );
				
		
		if(query!= null)
		{
			
			rs = DBUtil.executeQuery(query.trim());
//			try {
//				System.out.println("rs lenght = " + rs.getRow());
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
			try {
				if (rs.next()) {
					String status = rs.getString(column.trim());
					System.out.println("status = " + status.toString());
					if (status.equals(expetedOutput)) {
						System.out.println("FOUND IN DB");
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Result = " + result);
		return result;

	}

	@SuppressWarnings("deprecation")
	public static WebDriver open_browser(WebDriver wd, String browserType,boolean clearCache) {

		if (browserType.equalsIgnoreCase("FireFox")) {
			if (Boolean.parseBoolean(ReadConfiguration.IS_PROXY_ENABLED)) {
				setProxy();

				wd = new FirefoxDriver(cap);
				//require 'selenium-webdriver'
/*
				profile = Selenium::WebDriver::Firefox::Profile.new
				profile['network.proxy.type'] = 2
				profile['network.proxy.autoconfig_url'] = "http://abc.xyz.com/tester/proxy"

				driver = Selenium::WebDriver.for :firefox, :profile => profile*/
				
				wd.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			} else {
				wd = new FirefoxDriver();
				wd.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			}
		} else if (browserType.equalsIgnoreCase("IE")) {
			if (Boolean.parseBoolean(ReadConfiguration.IS_PROXY_ENABLED)) {
				setProxy();
				setIEBrowserConfig();
				wd = new InternetExplorerDriver(cap);
			} else {
				setIEBrowserConfig();
				wd = new InternetExplorerDriver(cap);
				wd.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
				System.out.println(cap.getBrowserName());
				System.out.println(cap.getVersion());
			}
		} else if (browserType.equalsIgnoreCase("chrome")) {

		//DesiredCapabilities capability = DesiredCapabilities.chrome();
		//wd = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);



			//setProxy();
			System.setProperty("webdriver.chrome.driver", 
		               "D:\\Automation\\Automation_InSync2.0\\chrome\\chromedriver.exe");
			Log.info(System.getProperty("webdriver.chrome.driver"));
			wd = new ChromeDriver();
			wd.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			
			//wd.close();
            wd.quit();
		}
		wd.manage()
				.timeouts()
				.implicitlyWait(
						Integer.parseInt(ReadConfiguration.IMPLICIT_WAIT),
						TimeUnit.SECONDS);
		wd.manage().window().maximize();
		wd.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
		if(clearCache)
		{
			wd.manage().deleteAllCookies();
		}
		return wd;
	}

	public static void setIEBrowserConfig() {
		cap = DesiredCapabilities.internetExplorer();
		cap.setBrowserName("iexplorer");
		cap.setVersion(ReadConfiguration.BROWSER_VERSION);
		cap.setCapability(
				InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				true);
		File driverPath = new File(ReadConfiguration.DRIVER_PATH
				+ "\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", driverPath.getAbsolutePath());
	}
	
	/*public void clearBrowserCache() {
		webDriver.manage().deleteAllCookies();
		selenium.refresh();
		webDriver.navigate().to("file:///c:/tmp/ClearCacheFirefox.html");
		}*/

	public static void setProxy() {
		System.out.println("Proxy Enabled:"
				+ ReadConfiguration.IS_PROXY_ENABLED + "Poxy IP:"
				+ ReadConfiguration.PROXY);
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(ReadConfiguration.PROXY)
				.setFtpProxy(ReadConfiguration.PROXY)
				.setSslProxy(ReadConfiguration.PROXY);
		cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.PROXY, proxy);
	}

	public static void navigate_to(WebDriver wd, String url) {
		System.out.println("nav url = " + url);
		if (wd == null) {
			System.out.println("wd is null");
		}
		System.out.println("wd = " + wd.getCurrentUrl());
		wd.get(url);
	}

	public static void send_keys(WebDriver wd, String locator,
			String locString, String data) {
	//	wd.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		switch (locator) {
		case "xpath":
			WebElement we = findElement((SearchContext)wd,wd,By.xpath(locString));
			we.clear();
			we.sendKeys(data);
			break;

		case "name":
			findElement((SearchContext)wd,wd,By.name(locString)).clear();
			findElement((SearchContext)wd,wd,By.name(locString)).sendKeys(data);
			break;

		case "id":
			
			SearchContext context = (SearchContext)wd;
			waitElementIsVisible(context, By.id(locString),wd);
			List<WebElement> table_delete = findElements(context,By.id(locString),wd);
			// System.out.println("table_delete.size() = " +
			// table_delete.size());
			if (table_delete.isEmpty() || table_delete == null) {
				System.out.println("No element found");
			}
			we = table_delete.get(table_delete.size() - 1);
			// System.out.println("we = " + we.getSize());
			we.clear();			
			we.sendKeys(data);
			break;
		}
	}
	
	/**
	 * 
	 * @param wd
	 * @param by
	 * @return
	 */
	private static WebElement findElement(SearchContext context,WebDriver driver,By by)
	{
		// wait till elements is visible before
		
		waitElementIsVisible(context,by,driver);
	//	List<WebElement> table_delete = findElements(By.id(locString));
		System.out.println("context = "+context);
		WebElement webElement = context.findElement(by);
		
		return webElement;
		
	}
	
	/**
	 * 
	 * @param context
	 * @param by
	 * @param driver
	 * @return
	 */
	private static List<WebElement> findElements(SearchContext context, By by,
			WebDriver driver)
	{
		// wait till elements is visible before
		
	 boolean result = waitElementIsVisible(context,by,driver);
		
		List<WebElement> webElements = context.findElements(by);
	//	WebElement webElement = driver.findElement(by);
		
		return webElements;
		
	}
	
	
	/**
	 * Waits until WebElement becomes visible not longer than timeOutSeconds.
	 * Sample:
	 * 
	 * <code><pre>
	 * goToPagePartWithButton.click();
	 * boolean visible = waitElementIsVisible(driver, By.className(&quot;button&quot;), 20);
	 * if (visible) {
	 *     System.out.println(&quot;We see the button, so we are on the correct page&quot;);
	 * } else {
	 *     System.out.println(&quot;Something is rotten in the state of Denmark.&quot;);
	 * }
     * </code></pre>
	 * 
	 * @param context
	 *            search context (can be {@link WebDriver}, some
	 *            {@link WebElement})
	 * @param by
	 *            The locating mechanism
	 * @param timeoutSeconds
	 *            time in seconds, must be >=0
	 * @return true if WebElement is visible or becomes visible during
	 *         timeoutSeconds; false if the element has not become visible
	 *         during given time period or cannot be found
	 */
	private static boolean waitElementIsVisible(SearchContext context, By by,
			WebDriver driver) {
		boolean visible = false;
	//	setTimeout(driver,0);
		FluentWait<SearchContext> wait = new FluentWait<SearchContext>(context)
				.withTimeout(IMPLICIT_WAIT, TimeUnit.SECONDS).ignoring(
						NotFoundException.class);
		long time = System.currentTimeMillis();
		try {
			visible = wait.until(elementVisible(by));
		} catch (TimeoutException e) {
			visible = false;
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
		long executionTime = System.currentTimeMillis()-time;
		System.out.println("Element Visibility = "+visible + "after executionTime" + executionTime +"Seconds");
		return visible;
		
	}
	
	
	

	/**
	 * Set implicitly wait timeout for the {@link WebDriver}
	 * 
	 * @param seconds
	 *            timeout measured in seconds
	 */
	public void setTimeout(WebDriver driver,long seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	private static Function<SearchContext, Boolean> elementVisible(final By by) {
		return new Function<SearchContext, Boolean>() {
			public Boolean apply(@NotNull SearchContext context) {
				return context != null ? context.findElement(by).isDisplayed()
						: false;
			}
		};
	}

	
	public static void send_keys_variable(WebDriver wd, String locator,
			String locString, String data) {
		switch (locator) {
		case "xpath":
			WebElement we = findElement((SearchContext)wd,wd,By.xpath(locString));
			we.clear();
			we.sendKeys(data);
			break;

		case "name":
			findElement((SearchContext)wd,wd,By.name(locString)).clear();
			findElement((SearchContext)wd,wd,By.name(locString)).sendKeys(data);
			break;

		case "id":
			List<WebElement> table_delete = findElements((SearchContext)wd,By.id(locString),wd);
			// System.out.println("table_delete.size() = " +
			// table_delete.size());
			if (table_delete.isEmpty() || table_delete == null) {
				System.out.println("No element found");
			}
			we = table_delete.get(table_delete.size() - 1);
			// System.out.println("we = " + we.getSize());
			we.clear();
			we.sendKeys(data);
			break;
		}
	}

	public static void press_enter(WebDriver wd, String locator,
			String locString) {
		WebElement we;

		switch (locator) {
		case "id":
			we = findElement((SearchContext)wd,wd,By.id(locString));
			System.out.println(we.getText());
			we.sendKeys(Keys.RETURN);
			break;

		case "xpath":
			we = findElement((SearchContext)wd,wd,By.xpath(locString));
			System.out.println(we.getText());
			we.sendKeys(Keys.RETURN);
			break;
		}
	}

	public static Boolean KillAllIe() {
		try {
			System.out.println("Kill All IE and IEdrivers");
			Runtime rt = Runtime.getRuntime();
			rt.exec("TASKKILL /F /IM \"iexplore.exe\"");
			rt.exec("TASKKILL /F /IM \"IEDriverServer.exe\"");
			Thread.sleep(2000);
			return true;
		} catch (Exception exp) {
			System.out.println("Exception when kill all Ie");
			return false;
		}
	}

	public static void scrollVertical(WebDriver wd,String locator,
			String locString) {
		try {
			findElement((SearchContext)wd,wd,(By.xpath(locString))).sendKeys(Keys.DOWN);
			
		} catch (Exception e) {
			System.out.println("not able to scroll vertical");
			e.printStackTrace();

		}
	}
	public static void scrollVertical(WebDriver wd,String end)
	{
		
		try {
			if(end.equalsIgnoreCase("end"))
			{
			Actions actions = new Actions(wd);
			actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
			System.out.println("scroll done");	
			}
			
		} catch (Exception e) {
			System.out.println("not able to scroll vertical");
			e.printStackTrace();

		}
	}
	public static void scrollVertical(WebDriver wd) {
		try {
		
			((JavascriptExecutor) wd).executeScript("scroll(0,300);");
		} catch (Exception e) {
			System.out.println("not able to scroll vertical");
			e.printStackTrace();

		}
	}

	public static void scrollhorizontal(WebDriver wd,String locator,
			String locString) {
		try {
			findElement((SearchContext)wd,wd,(By.xpath(locString))).sendKeys(Keys.ARROW_RIGHT);
			//((JavascriptExecutor) wd).executeScript("scroll(300,0);");
		} catch (Exception e) {
			System.out.println("not able to scroll horizontal");
			e.printStackTrace();

		}
	}
	public static void scrollhorizontal(WebDriver wd) {
		try {
			((JavascriptExecutor) wd).executeScript("scroll(300,0);");
		} catch (Exception e) {
			System.out.println("not able to scroll horizontal");
			e.printStackTrace();

		}
	}

	public static void click_element(WebDriver wd, String locator,
			String locString) {
		System.out.println("KeyWordIMPL.driver: "+wd);
		System.out.println("Locator: "+locator);
		System.out.println("LocStrnig: "+locString);
		switch (locator) {
		case "xpath":
			try {
				WebElement element = findElement((SearchContext)wd,wd,By.xpath(locString));
				System.out.println("element = "+element);
				SearchContext context = (SearchContext)wd;
				waitElementIsVisible(context, By.xpath(locString),wd);
				element.click();
			} catch (NoSuchElementException nsee) {
				System.out.println(nsee.toString());
			}

			break;
		case "name":
			findElement((SearchContext)wd,wd,By.name(locString)).click();
			break;
		case "id":
			try {
				findElement((SearchContext)wd,wd,By.id(locString)).click();
			} catch (Exception e) {

				JavascriptExecutor js = (JavascriptExecutor) wd;
				js.executeScript("arguments[0].click();",
						findElement((SearchContext)wd,wd,By.id(locString)));
				System.out.println("Clicking Element Using JS");
			}

			break;
			
			
		case "class":
			findElement((SearchContext)wd,wd,By.className(locString)).click();

		}

	}
	
	/*public static void click_element_dropdown(WebDriver wd, String locator,
			String locString)
	{
		Select droplist = new Select(webDriver.findElement(By.id(fieldNameArr[1])));

		List<WebElement> dd = droplist.getOptions();

		    for (WebElement option : dd) {

		         if (option.getAttribute("value").equalsIgnoreCase(Administrator)) {

		               String listValue = option.getAttribute("value");

		               option.click();
		               droplist.selectByVisibleText(listValue);

		                                                        }

		                                                }
		    }*/

	public static boolean wait_explicit(WebDriver wd, String locator,
            String locString, String paramString) {
     boolean result = false;
     WebDriverWait wait =null;
     switch (locator) {

     case "id":
            wait = new WebDriverWait(wd,
                         Integer.parseInt(paramString));
            wait.until(ExpectedConditions.presenceOfElementLocated(By
                         .id(locString)));
            break;
     case "name":
            wait = new WebDriverWait(wd,
                         Integer.parseInt(paramString));
            wait.until(ExpectedConditions.presenceOfElementLocated(By
                         .name(locString)));
            break;
     case "xpath":
            wait = new WebDriverWait(wd,
                         Integer.parseInt(paramString));
            wait.until(ExpectedConditions.presenceOfElementLocated(By
                         .xpath(locString)));
            break;

     case "default":
            System.out.println("Not a predefined locator");
            break;
     }

     return result;
}


	public static void verify_element(WebDriver wd, String locator,
			String locString) {
		WebDriverWait wait = null;
		switch (locator) {
		case "xpath":
			wait = new WebDriverWait(wd, 180);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(locString)));
			break;
		case "name":
			wait = new WebDriverWait(wd, 180);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.name(locString)));
			break;
		case "id":
			wait = new WebDriverWait(wd, 180);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.id(locString)));
			break;
		}
	}

	public static void store_text(WebDriver wd, String locator,
			String locString, String steps) {
		String v_txt = null;
		switch (locator) {
		case "xpath":
			v_txt = (findElement((SearchContext)wd,wd,By.xpath(locString)).getText());
			System.out.println(v_txt);
			break;
		case "name":
			v_txt = (findElement((SearchContext)wd,wd,By.name(locString)).getText());
			System.out.println(v_txt);
			break;
		case "id":
			v_txt = (findElement((SearchContext)wd,wd,By.id(locString)).getText());
			System.out.println(v_txt);
			break;
		}
		// return v_txt;
	}

	public static void close_browser(WebDriver wd) {
		if (wd instanceof FirefoxDriver) {
			try {
				wd.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (wd instanceof InternetExplorerDriver) {
			try {
				KeywordsImpl.KillAllIe();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String get_timeStamp() {
		cur_dt = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String strTimeStamp = dateFormat.format(cur_dt);
		return strTimeStamp;
	}

	public static String getCurTime() {
		Date cur_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String strTimeStamp = dateFormat.format(cur_time);
		return strTimeStamp;
	}
	
	public static void Logout(WebDriver wd, String jscript)
	{
		try{
		wd.switchTo().frame("mainFrame");
		}
		catch(NoSuchElementException | StaleElementReferenceException | NoSuchFrameException e){}
		JavascriptExecutor js = (JavascriptExecutor) wd;
		System.out.println("jscript = " + jscript);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		js.executeScript(jscript);
		System.out.println("Executed javascript");
	}
		
	

	public static boolean click_table_image(WebDriver wd, String table,
			String tableRow, String siteToDistribute, String image)
			throws InterruptedException {
		boolean result = false;
		System.out.println("inside click_table_image  = " + siteToDistribute);
		System.out.println("locateString = " + image);
		WebElement table_element = findElement((SearchContext)wd,wd,By.id(table.trim()));
		System.out.println("table_element = " + table_element.getText());
		List<WebElement> tr_collection = findElements(table_element,By
				.xpath(tableRow),wd);

		int row_num, col_num;
		row_num = 1;
		WebElement checkbox = null;
		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = findElements(trElement,By
					.xpath("td"),wd);
			System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;

			for (WebElement tdElement : td_collection) {
				if (tdElement.getText().equalsIgnoreCase(siteToDistribute.trim())) {
					String locateString = "//img[contains(@src,'" + image.trim()
							+ "')]";

					checkbox = findElement(trElement,wd,By.xpath(locateString));
					System.out.println("checkbox tag name = "
							+ checkbox.getTagName());
					/*
					 * System.out.println("checkbox.gettext = " +
					 * checkbox.getText());
					 */

					System.out.println("checkbox.isSelected = "
							+ checkbox.isSelected());
					System.out.println("checkbox.isEnabled = "
							+ checkbox.isEnabled());

					System.out.println("row # " + row_num + ", col # "
							+ col_num + "text=" + tdElement.getText());

					checkbox.click();
					System.out.println("Site exists and selecting it");
					return true;
				}
				col_num++;
			}
			row_num++;
		}
		return result;
	}

	public static boolean save_table(WebDriver wd, String text) {
		boolean result = false;

		if (text.contains("Save")) {
			System.out.println("Inside save_table new impl");
			List<WebElement> table_save = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Save Changes')]"),wd);
			if (table_save.isEmpty() || table_save == null) {
				System.out.println("No element found");
			}
			WebElement we = table_save.get(table_save.size() - 1);
			we.click();
			result = true;
		}else if (text.contains("Search")) {
			System.out.println("Inside Table Search");

			List<WebElement> table_cancel = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Search')]"),wd);
			if (table_cancel.isEmpty() || table_cancel == null) {
				System.out.println("No element found");
			}
			
			System.out.println("table_cancel.size = "+table_cancel.size());

			for (WebElement we : table_cancel) {
				System.out.println("we.getText = "+we.getText());
				if (we.getText().equals("Search")) {
					we.click();
					result = true;
				}
			}
		}
		
		else if (text.contains("Delete")) {
			List<WebElement> table_delete = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Delete')]"),wd);
			if (table_delete.isEmpty() || table_delete == null) {
				System.out.println("No element found");
			}
			WebElement we = table_delete.get(table_delete.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Cancel")) {

			List<WebElement> table_cancel = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Cancel')]"),wd);
			if (table_cancel.isEmpty() || table_cancel == null) {
				System.out.println("No element found");
			}
			WebElement we = table_cancel.get(table_cancel.size() - 1);
			we.click();
			result = true;
		}
//		if (text.contains("Search")) {
//			System.out.println("Inside Table Search");
//
//			List<WebElement> table_cancel = findElements((SearchContext)wd,By
//					.xpath("//*[contains(text(), 'Search')]"),wd);
//			if (table_cancel.isEmpty() || table_cancel == null) {
//				System.out.println("No element found");
//			}
//
//			for (WebElement we : table_cancel) {
//				if (we.getText().equals("Search")) {
//					we.click();
//					result = true;
//				}
//			}
//		}
		else if (text.contains("Assign Workflow")) {
			System.out.println("Assign Workflow");

			List<WebElement> table_cancel = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Assign Workflow')]"),wd);
			if (table_cancel.isEmpty() || table_cancel == null) {
				System.out.println("No element found");
			}
			WebElement we = table_cancel.get(table_cancel.size() - 1);
			we.click();
			result = true;

		}
		else if (text.contains("OK")) {
			System.out.println("OK");

			List<WebElement> table_cancel = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'OK')]"),wd);
			if (table_cancel.isEmpty() || table_cancel == null) {
				System.out.println("No element found");
			}
			System.out.println("table_cancel size = " + table_cancel.size());
			System.out
					.println("table_cancel size = " + table_cancel.indexOf(0));
			WebElement we = table_cancel.get(table_cancel.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Confirm")) {
			System.out.println("Inside Table Search, looking for Confirm");

			List<WebElement> confirm = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Confirm')]"),wd);
			if (confirm.isEmpty() || confirm == null) {
				System.out.println("Element not found");
			}
			System.out.println("confirm size = " + confirm.size());
			System.out.println("confirm size = " + confirm.indexOf(0));
			WebElement we = confirm.get(confirm.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Assign to Workflow")) {
			System.out
					.println("Inside Table Search, looking for Assign to Workflow");

			List<WebElement> assign_workflow = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Assign to Workflow')]"),wd);
			if (assign_workflow.isEmpty() || assign_workflow == null) {
				System.out.println("Element not found");
			}
			WebElement we = assign_workflow.get(assign_workflow.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Cancel and Delete Assets")) {
			System.out
					.println("Inside Table Search, looking for Cancel and Delete Assets");

			List<WebElement> cancel_delete_assets = findElements((SearchContext)wd,By
							.xpath("//*[contains(text(), 'Cancel and Delete Assets')]"),wd);
			if (cancel_delete_assets.isEmpty() || cancel_delete_assets == null) {
				System.out.println("Element not found");
			}
			WebElement we = cancel_delete_assets.get(cancel_delete_assets
					.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Apply")) {
			System.out.println("Inside Table Search, looking for Apply");

			List<WebElement> apply = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Apply')]"),wd);
			if (apply.isEmpty() || apply == null) {
				System.out.println("Element not found");
			}
			WebElement we = apply.get(apply.size() - 1);
			we.click();
			result = true;
		}
		else if (text.contains("Disable")) {
			System.out.println("Inside Table Search, looking for Disable");

			List<WebElement> Disable = findElements((SearchContext)wd,By
					.xpath("//*[contains(text(), 'Disable')]"),wd);
			if (Disable.isEmpty() || Disable == null) {
				System.out.println("Element not found");
			}
			WebElement we = Disable.get(Disable.size() - 1);
			we.click();
			result = true;
		}

		return result;
	}
	/*
	 Inserting below method to run ssh commands in server
	 */
	public static boolean runSshCommand  (WebDriver wd, String user,
			String password, String command, String hostname) throws IOException{
		// TODO Auto-generated method stub
		boolean result=false;
		try
		{
			JSch js = new JSch();
	    com.jcraft.jsch.Session s = js.getSession(user, hostname, 22);
	    s.setPassword(password);
	    Properties config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    s.setConfig(config);
	    s.connect();

	    Channel c = s.openChannel("exec");
	    ChannelExec ce = (ChannelExec) c;

	    //ce.setCommand("cd /seachange;cat ADI.xml | grep title | grep Asset_ID | cut -d \"=\" -f10 | cut -d \" \" -f1");
	    ce.setCommand(command);
	    ce.setErrStream(System.err);
	    ce.setOutputStream(System.out);

	    ce.connect();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
	    String line;
	   
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	    
	    }

	    ce.disconnect();
	    s.disconnect();

	    System.out.println("Exit code: " + ce.getExitStatus());
	    if ((ce.getExitStatus()==0) ||  (ce.getExitStatus()==-1))
	    {
	    	result = true;
	    }
}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	/*
	 * Below method validates if the read data from excel can be converted to integer or not
	 */
	public static boolean result(String s) 
	{
		boolean result;
		try {
			
			int i1= Integer.parseInt(s);
			System.out.println("successfully converted"+i1);
			result=true;
			
		} 
		
		catch (NumberFormatException e) {
			result=false;
			System.out.println("unable to convert");
					
		}
		
			return result;
		
	}
	
	
	/*
	 * Adding below three methods "readAndCopyXml" , "switchToFrame" , "switchToMain"
	 * for Frame Handling
	 */
	public static void readAndCopyXml(WebDriver wd , String filePath , String locType, String locString) throws IOException, InterruptedException
	{
		System.out.println(filePath);
		FileInputStream fis = new FileInputStream(filePath);
		String xmlContent = IOUtils.toString(fis);
		System.out.println(xmlContent);
		Thread.sleep(10000);
		switch (locType)
		{
		case "xpath":
			findElement((SearchContext)wd,wd,By.xpath(locString)).click();
			findElement((SearchContext)wd,wd,By.xpath(locString)).sendKeys(Keys.CONTROL + "a");
			findElement((SearchContext)wd,wd,By.xpath(locString)).sendKeys(Keys.DELETE);  
			findElement((SearchContext)wd,wd,By.xpath(locString)).sendKeys(xmlContent);
		break;
		case "id":
			findElement((SearchContext)wd,wd,By.id(locString)).sendKeys(xmlContent);
			break;
		case "name":
			findElement((SearchContext)wd,wd,By.name(locString)).sendKeys(xmlContent);
			break;
		
		}
		
		}
	public static void click_element_dropdown(WebDriver wd, String locator,
            String locString,String value)
{
     System.out.println("KeyWordIMPL.driver: "+wd);
     System.out.println("Locator: "+locator);
     System.out.println("LocStrnig: "+locString);
     switch (locator) {
     case "xpath":
            try {
                  Select select = new Select(findElement((SearchContext)wd,wd,By.xpath(locString)));
                  select.selectByValue(value);
            } catch (NoSuchElementException nsee) {
                  System.out.println(nsee.toString());
            }

            break;
     case "name":
            Select select = new Select(findElement((SearchContext)wd,wd,By.name(locString)));
            select.selectByValue(value);
            break;
     case "id":
            try {
                  Select select1 = new Select(findElement((SearchContext)wd,wd,By.id(locString)));
                  select1.selectByValue(value);
            } catch (Exception e) {

                  JavascriptExecutor js = (JavascriptExecutor) wd;
                  js.executeScript("arguments[0].click();",
                                findElement((SearchContext)wd,wd,By.id(locString)));
                  System.out.println("Clicking Element Using JS");
            }

            break;
            
            
     case "class":
            Select select2 = new Select(findElement((SearchContext)wd,wd,By.className(locString)));
            select2.selectByValue(value);

     }
}

		
	public static WebDriver switchToFrame(WebDriver wd , String frameName)
	{
		WebDriver frame = wd.switchTo().frame(frameName);
		return frame;
	}
	

	public static WebDriver switchToMain (WebDriver wd)
	{
		WebDriver frame = wd.switchTo().defaultContent();
		return frame;
	}
	public static void select_from_dropdown_menu(WebDriver wd, String locType, String option ){
        Select select = null;
        switch(locType){
               case "xpath":
                     try{
                            select = new Select(findElement((SearchContext)wd,wd,By.xpath(locType)));
                            select.selectByValue(option);
                     }catch(Exception e){
                            e.printStackTrace();
                     }
                     
               case "id":
                     try{
                            select = new Select(findElement((SearchContext)wd,wd,By.id(locType)));
                            select.selectByValue(option);
                     }catch(Exception e){
                            e.printStackTrace();
                     }
                     
               case "name":
                     try{
                            select = new Select(findElement((SearchContext)wd,wd,By.name(locType)));
                            select.selectByValue(option);
                     }catch(Exception e){
                            e.printStackTrace();
                     }
        }
	}
//Implementation of find_and_click_element
	
	public static void find_and_click_element(WebDriver wd, String Loctype,
			String LocString, String paramsCell) {
		int i=0;
		List<WebElement> ele = findElements((SearchContext)wd,By.xpath(LocString),wd);
				System.out.println(ele.size());
				Iterator<WebElement> itr=ele.iterator();
				while(itr.hasNext())
				{
				 if(ele.get(i).getText() .contains(paramsCell))
				 { ele.get(i).click();
					break;
				 }
					else
					{
						i++;
					}
				}
				
				
		
		
	}
	//Implementation of Scroll_end
	public static void scroll_end(WebDriver wd) {
		System.out.println("scroll done");
		Actions actions = new Actions(wd);
		actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		System.out.println("scroll done");	
	}

}

