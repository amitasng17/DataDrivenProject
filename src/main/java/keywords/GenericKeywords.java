package keywords;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reports.ExtentManager;

public class GenericKeywords {
	public WebDriver driver = null;
	Properties prop = null;
	Properties envprop = null;
	public ExtentTest test;
	SoftAssert softAssert;

	public void LaunchBrowser(String BrowserName) {
		System.out.println("Opening browser------" + BrowserName);
		log("Opening Browser");
		if (BrowserName.equals("Chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\D Drive\\Test Automation\\Drivers\\AllDrivers\\chromedriver.exe");
			ChromeOptions ops = new ChromeOptions();
			ops.setPageLoadStrategy(PageLoadStrategy.EAGER);
			ops.addArguments("--disable-notifications");
			ops.addArguments("--start-maximized");
			ops.addArguments("ignore-certificate-errors");
			driver = new ChromeDriver(ops);

		} else if (BrowserName.equals("Mozilla")) {
			System.setProperty("webdriver.firefox.driver",
					"C:\\D Drive\\Test Automation\\Drivers\\geckodriver-v0.34.0-win-aarch64\\geckodriver.exe");
			FirefoxOptions op = new FirefoxOptions();
			op.setPageLoadStrategy(PageLoadStrategy.EAGER);
			FirefoxProfile prof = new FirefoxProfile();

			// for turning off notifications
			prof.setPreference("dom.webnotifications.enabled", false);

			// for SSL Certificate errors
			prof.setAcceptUntrustedCertificates(true);
			prof.setAssumeUntrustedCertificateIssuer(false);
			op.setProfile(prof);

			driver = new FirefoxDriver(op);
		} else if (BrowserName.equals("Edge")) {
			System.setProperty("webdriver.edge.drive",
					"C:\\D Drive\\Test Automation\\Drivers\\AllDrivers\\msedgedriver.exe");
			System.setProperty(EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY, "logs\\edge.log");
			EdgeOptions ops = new EdgeOptions();
			ops.addArguments("--disable-notifications");
			ops.addArguments("--start-maximized");
			ops.addArguments("ignore-certificate-errors");
			driver = new EdgeDriver(ops);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	}

	public void navigateurl(String urlkey) {

		log("Navigating to url-----" + urlkey);
		driver.get(envprop.getProperty(urlkey));
	}

	public void click(String locatorkey) {

		log("Clicking on ---" + locatorkey);
		GetElement(locatorkey).click();
		
	}

	public void type(String locatorkey, String Data) {

		log("Entering text in ---" + locatorkey);
		GetElement(locatorkey).sendKeys(Data);
	}

	public void clear(String locatorkey) {

		log("clearing text in ---" + locatorkey);
		GetElement(locatorkey).clear();
	}

	public void selectByVisibleText(String locatorKey, String data) {
		Select s = new Select(GetElement(locatorKey));
		s.selectByVisibleText(data);
	}

	public void acceptAlert() {
		log("Switching to alert");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.alertIsPresent());
		try {
			Thread.sleep(3000);
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			log("Alert accepted successfully");
		} catch (Exception e) {
			 reportFailure("Alert not found when mandatory",true);
		}

	}

//central function to extract elements
	public WebElement GetElement(String locatorkey) {
		// check the prsenece of element
		if (!isElementPresent(locatorkey)) {

			reportFailure("Element not found---" + locatorkey, true);
		}
		// check the visibility of element
		if (!isElementVisible(locatorkey)) {
			reportFailure("Element not visible---" + locatorkey, true);
		}
		WebElement e = driver.findElement(getlocator(locatorkey));
		;
		return e;
	}
	
	
	
	public void clickEnterButton(String locatorKey) {
		log("Clinking enter button");
		GetElement(locatorKey).sendKeys(Keys.ENTER);
	}
	
	
	public void wait(int time) {
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isElementPresent(String locatorkey) {
		log("checking presence of -----" + locatorkey);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getlocator(locatorkey)));
		} catch (Exception e) {
			return false;
		}
		System.out.println("Element found" + locatorkey);
		return true;
	}

	public boolean isElementVisible(String locatorkey) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getlocator(locatorkey)));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	public By getlocator(String locatorkey) {
		By by = null;
		if (locatorkey.endsWith("_xpath"))
			by = By.xpath(prop.getProperty(locatorkey));
		if (locatorkey.endsWith("_css"))
			by = By.cssSelector(prop.getProperty(locatorkey));
		if (locatorkey.endsWith("_id"))
			by = By.id(prop.getProperty(locatorkey));
		if (locatorkey.endsWith("_name"))
			by = By.name(prop.getProperty(locatorkey));
		return by;

	}

	public void WaitForPagetoLoad() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i = 0;
		while (i != 10) {
			String state = (String) js.executeScript("return document.readyState");
			System.out.println(state);
			if (state.equals("Complete"))
				break;
			else
				Thread.sleep(2000);
			i++;

		}
		i = 0;
		while (i != 10) {
			Long d = (Long) js.executeScript("return jQuery.active");
			System.out.println(d);
			if (d.longValue() == 0)
				break;
			else
				Thread.sleep(2000);
			i++;

		}

	}

	public void quit() {
		driver.quit();
	}

	public void log(String msg) {
		System.out.println(msg);
		test.log(Status.INFO, msg);
	}

	public void reportFailure(String msg, boolean stopOnFailure) {
		System.out.println(msg);
		test.log(Status.FAIL, msg);
		takeScreenShot();
		softAssert.fail();
		if (stopOnFailure)
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
		AssertAll();

	}

	public void AssertAll() {
		softAssert.assertAll();
	}
	
	

	public void Login() {
		LaunchBrowser("Chrome");
	}

	public void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
			// put screenshot file in reports
			test.log(Status.INFO, "Screenshot-> "
					+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int getRowWithCellData(String tableLocator,String companyName) {
        WebElement table=GetElement(tableLocator);
        List<WebElement> rows=table.findElements(By.xpath("//table[@class='dataTable sortable']/tbody/tr"));
        for(int rowNum=0;rowNum<rows.size(); rowNum++){
             WebElement row= rows.get(rowNum);
             List<WebElement> cells=row.findElements(By.tagName("td"));
             for (int col=0; col<cells.size();col++){
                  String celltext=cells.get(col).getText();
                  System.out.println(celltext);
                  if(!celltext.trim().equals(""))
                  if(companyName.startsWith(celltext))
                        return (rowNum+1);
               }
        }
        return -1;
     }

     public void selectDateFromCalendar(String DateToBeSelected) throws ParseException{
         Date currentDate=new Date();
    	 SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        Date dateToSel=df.parse(DateToBeSelected);
        df=new SimpleDateFormat("d");
        String day=df.format(dateToSel);
        df=new SimpleDateFormat("MMMM");
        String month=df.format(dateToSel);
         df=new SimpleDateFormat("YYYY");
        String year=df.format(dateToSel);
        String monthYearDisplayed=GetElement("monthYear_xpath").getText();
        String monthYearToSelect=month+" "+year;
        while(!monthYearDisplayed.equalsIgnoreCase(monthYearToSelect)){
            if(currentDate.compareTo(dateToSel)>0) {
            	click("backBtnCalendar_xpath");
            }else if (currentDate.compareTo(dateToSel)<0)
            	click("fwdBtnCalendar_xpath");
            monthYearDisplayed=GetElement("monthYear_xpath").getText();
         }
        driver.findElement(By.xpath("//td[text()='"+day+"']")).click();

     }
       
     
     public String gettext(String locatorkey) {

 		log("Getting text from ---" + locatorkey);
 		return GetElement(locatorkey).getText();
 	}
}
