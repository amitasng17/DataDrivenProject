package rediff.testcases;

import java.text.ParseException;

import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import TestBase.basetest;
import util.ExcelDataProvider;

public class StockManagement extends basetest{

@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void addStock(ITestContext context,String PortfolioName,String stockName,String purchaseDate,
		int Quantity, int Price) throws InterruptedException, ParseException {
	  app.log("Adding stockQuantity "+ Quantity+"of "+stockName);
	  int quantityBeforeModification=app.findCurrentStockQuantity(stockName);
	  context.setAttribute("quantityBeforeModification", quantityBeforeModification);
	  app.click("addStockBtn_css");
	  app.selectCompany(stockName,"stockName_xpath");
	  app.click("dateSelection_xpath");
	  app.wait(5);
	  app.selectDateFromCalendar(purchaseDate);
	  app.type("stockQty_xpath", Integer.toString(Quantity));
	  app.type("stockPrice_xpath",Integer.toString(Price));
	  app.click("addStockBtn_xpath");
	  app.WaitForPagetoLoad();
}


@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void modifyStock(ITestContext context,String PortfolioName,String stockName,String purchaseDate,
		int Quantity, int Price) throws InterruptedException, ParseException {
	
	String action=(String)context.getAttribute("action");	
	  app.log("Adding stockQuantity "+ Quantity+"of "+stockName);
	  int quantityBeforeModification=app.findCurrentStockQuantity(stockName);
	  context.setAttribute("quantityBeforeModification", quantityBeforeModification);
	  app.goToBuySell(stockName);
	  if(action.equals("sellstock"))
	      app.selectByVisibleText("equityaction_id", "Sell");
	  else
	      app.selectByVisibleText("equityaction_id", "Buy");
	      app.click("buySellCalendar_id");
	     app.log("Selecting Date "+ purchaseDate);
	     app.selectDateFromCalendar(purchaseDate);
	     app.type("buysellqty_id", Integer.toString(Quantity));
	     app.type("buysellprice_id", Integer.toString(Price));
	     app.click("buySellStockButton_id");
	     app.WaitForPagetoLoad();
	     app.log("Stock Sold ");	
}

@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void VerifyStockPresent(String PortfolioName,String companyName,String purchaseDate,
		int Quantity, int Price) {
 int rowNum=app.getRowWithCellData("stockTable_xpath",companyName);
 if(rowNum==-1)
   app.reportFailure("Stock not present" + companyName,true);
 app.log("Stock present in the table "+ companyName);
}


@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void VerifyStockQuantity( ITestContext context,String PortfolioName,String stockName,String purchaseDate,
		int Quantity, int Price) {
	
  String action=(String)context.getAttribute("action");	
  int quantityAfterModification=app.findCurrentStockQuantity(stockName);
  int quantityBeforeModification=(Integer)context.getAttribute("quantityBeforeModification");
  int modifiedQuantity=Quantity;
  int expectedModifiedQuantity=0;
  if(action.equals("sellstock"))
	  expectedModifiedQuantity=quantityBeforeModification-quantityAfterModification;
     
  else 
	  expectedModifiedQuantity=quantityAfterModification-quantityBeforeModification;
  if (expectedModifiedQuantity!=modifiedQuantity)
     app.reportFailure("Quantity did not match", true);
  app.log("Stock quantity changes as expected" + modifiedQuantity);
}


@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void verifyTransactionHistory(ITestContext context,String PortfolioName,String stockName,String purchaseDate,
		int Quantity, int Price){
	
   String action=(String)context.getAttribute("action");	
  app.log("Verifying transaction history for "+stockName +"for "+action);
  app.goToTransactionHistory(stockName);
  String noOfShares=app.gettext("noofshares_xpath");
  String qty=Integer.toString(Quantity);
  if(action.equals("sellstock"))
	  qty="-"+qty;
  if(!noOfShares.equals(qty))
     app.reportFailure("The quantity in the transaction history is not correct", true);
  app.log("Transaction History shows correct value");
}


}
