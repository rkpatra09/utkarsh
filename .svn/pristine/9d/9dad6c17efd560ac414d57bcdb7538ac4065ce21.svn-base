package com.beroe.connectivity;

import java.io.File;
import java.util.Arrays;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.beroe.utility.ReadConfiguration;


public class DriverConnectionFactory {

	ProfilesIni profile ;
	public RemoteWebDriver CreateNewDriver(String Browser) throws Exception {
		RemoteWebDriver driver = null;
		File driverPath = null;
		String PROXY = ReadConfiguration.readExcelData("Configuration",
				"Proxy_IP", 1);
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		DesiredCapabilities cap = null;
		int bID = 0;
		if (Browser.equals("firefox"))
			bID = 1;
		else if (Browser.equals("chrome"))
			bID = 2;
		else if (Browser.equalsIgnoreCase("ie"))
			bID = 3;

		try {
			switch (bID) {
			case 1:
				if (ReadConfiguration.readExcelData("Configuration",
						"Set_Proxy", 1).equalsIgnoreCase("on")) {
					proxy.setHttpProxy(PROXY).setFtpProxy(PROXY)
							.setSslProxy(PROXY);
					cap = new DesiredCapabilities();
					cap.setCapability(CapabilityType.PROXY, proxy);
					driver = new FirefoxDriver(cap);
				} else
					profile = new ProfilesIni();
		        FirefoxProfile ffprofile = profile.getProfile("default");
					driver = new FirefoxDriver(ffprofile);
				break;
			case 2:
				cap = DesiredCapabilities.chrome();
				cap.setCapability("chrome.switches",
						Arrays.asList("--start-maximized"));
				driverPath = new File(ReadConfiguration.readExcelData(
						"Configuration", "Driver_Path", 1)
						+ "/chromedriver.exe");
				System.setProperty(
						ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,
						driverPath.toString());
				// System.setProperty("webdriver.chrome.driver",
				// driverPath.toString());
				driver = new ChromeDriver(cap);
				break;
			case 3:
				cap = DesiredCapabilities.internetExplorer();
				cap.setBrowserName("iexplorer");
				cap.setVersion("8");
				driverPath = new File(ReadConfiguration.readExcelData(
						"Configuration", "Driver_Path", 1)
						+ "/IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", driverPath.toString());
				driver = new InternetExplorerDriver(cap);
				break;
			default:
				proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
				cap = new DesiredCapabilities();
				cap.setCapability(CapabilityType.PROXY, proxy);
				driver = new FirefoxDriver(cap);
				break;
			}
			driver.manage().window().maximize();
			return driver;
		} catch (Exception e) {
			System.out
					.println("Exception occured while opening the browser, try again");
			e.printStackTrace();
			System.out
					.println("\n$$$$~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~$$$$");
			System.out
					.println("$$$$Driver can't load selected browser type, terminating execution$$$$");
			System.out
					.println("$$$$~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~$$$$");
			System.out
					.print("This window will remain open for another 10 seconds");
			Thread.sleep(10000L);
			System.exit(0);
		}
		return null;
	}
}
