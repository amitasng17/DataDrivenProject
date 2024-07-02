package keywords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

public class ApplicationKeywords extends ValidationKeywords {

	public ApplicationKeywords() throws IOException {
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\env.properties";
		System.out.println(path);
		prop = new Properties();
		envprop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
			String env = prop.getProperty("env") + ".properties";
			path = System.getProperty("user.dir") + "\\src\\test\\resources\\" + env;
			fs = new FileInputStream(path);
			envprop.load(fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		softAssert = new SoftAssert();

	}

	public void Login() {
		type("username_id",envprop.getProperty("username"));	
		type("password_xpath",envprop.getProperty("password"));
		try {
			WaitForPagetoLoad();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ValidateElementPresent("login_button_id");
		click("login_button_id");
		wait(5);
	}

	public void SelectDate() {

	}

	public void VerfiyStockAdded() {

	}

	public void setReport(ExtentTest test) {
		this.test = test;
	}

	public int findCurrentStockQuantity(String companyName) {
		log("Finding current Stock Quantity for company" + companyName);
		int rowNum = getRowWithCellData("stockTable_xpath", companyName);
		if (rowNum == -1) {
			log("Current Stock Quantity is 0 as the Stock is not present in the list");
			return 0;
		}
		String Quantity = driver.findElement(By.xpath("//table[@id='stock']/tbody/tr[" + rowNum + "]/td[4]")).getText();
		log("Current Stock Quantity is " + Quantity);
		return (Integer.parseInt(Quantity));
	}

	public void selectCompany(String companyName, String locatorKey) {

		String Stock = companyName.substring(0, companyName.length() - 1);
		GetElement(locatorKey).sendKeys(Stock);
		driver.findElement(By.xpath("//div[@id='ajax_listOfOptions']/div[text()='" + companyName + "']")).click();
	}
	
	public void goToTransactionHistory(String companyName){
		 int rowNum=getRowWithCellData("stockTable_xpath",companyName) ;
		 if (rowNum==-1) 
		   {log("Stock is not present in the list");  }
		 driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rowNum+"]//input[@type='radio']")).click();
		 driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rowNum+"]//input[@name='Transaction History']")).click();
		}

		public void goToBuySell(String companyName){
		 int rowNum=getRowWithCellData("stockTable_xpath",companyName) ;
		 if (rowNum==-1) 
		   {log("Stock is not present in the list");  }
		 driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rowNum+"]//input[@type='radio']")).click();
		 driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rowNum+"]//input[@name='Buy / Sell']")).click();
		}

}
