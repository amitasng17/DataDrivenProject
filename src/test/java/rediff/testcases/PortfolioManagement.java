package rediff.testcases;

//import org.openqa.selenium.By;
//import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.Test;

//import com.aventstack.extentreports.Status;

//import com.aventstack.extentreports.Status;

import TestBase.basetest;
import util.ExcelDataProvider;

public class PortfolioManagement extends basetest {

	
@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void createPortfolio(ITestContext con, String PortfolioName) {
	app.log("Creating portfolio");
	System.out.println("Creating portfolio");
	//app.reportFailure("Some non critical error",false);
	app.click("createportfolio_xpath");
	app.clear("createtextbox_xpath");
    app.type("createtextbox_xpath", PortfolioName);
    app.click("createPortfoliobutton_xpath");
	try {
		app.WaitForPagetoLoad();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	app.validateSelectedValueInDropDown("portfolioselect_id",PortfolioName);
	
}

@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void deletePortfolio(String PortfolioName) throws InterruptedException {
	//String PortfolioName="Amita";
	app.log("Deleting portfolio");
	System.out.println("Deleting portfolio");
	app.selectByVisibleText("portfolioselect_id", PortfolioName);
	app.click("deleteportfoliobutton_id");
	//app.WaitForPagetoLoad();
	Thread.sleep(10000);
	app.acceptAlert();
	app.WaitForPagetoLoad();
	app.validateSelectedValueNotInDropDown("portfolioselect_id", PortfolioName);
}


@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void selectPortFolio(ITestContext context,String portfolioName,String stockName,String purchaseDate,
		int Quantity, int Price) throws InterruptedException {
	
	System.out.println(portfolioName+"  "+stockName+" "+Quantity+" "+" "+Price);
	//String portfolioName="test1";		
	app.log("Selecting Profolio");
	app.selectByVisibleText("portfolioselect_id", portfolioName);
    app.WaitForPagetoLoad();
}
}
