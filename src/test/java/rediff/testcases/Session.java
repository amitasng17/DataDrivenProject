package rediff.testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import TestBase.basetest;
import keywords.ApplicationKeywords;
import util.ExcelDataProvider;

public class Session extends basetest{
	
@Test
public void doLogin(ITestContext con) throws InterruptedException {
System.out.println("Logging in");
app.log("Logging in");
app.log("Logging in");
app.LaunchBrowser("Chrome");
app.navigateurl("url");
app.Login();

}

@Test
public void doLogOut() {
System.out.println("Logging out");	
app.log("Logging out");
}

}
