package TestBase;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.opentelemetry.context.Context;
import keywords.ApplicationKeywords;
import reports.ExtentManager;

public class basetest {
	
public ApplicationKeywords app;
public ExtentReports rep;
public ExtentTest test;

@BeforeTest(alwaysRun=true)
public void beforetest(ITestContext con) throws NumberFormatException, FileNotFoundException, IOException, ParseException {
	System.out.println("-------beforetest----");
		//init the app object and share it with all the tests
	try {
		app=new ApplicationKeywords();
		rep=ExtentManager.getReports();
		test=rep.createTest(con.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Starting Test"+con.getCurrentXmlTest().getName());
	} catch (IOException e) {
		e.printStackTrace();
	}
	con.setAttribute("app", app);
	con.setAttribute("reports", rep);
	con.setAttribute("test", test);
	app.setReport(test);
	}

@Parameters({"action"})
@BeforeMethod(alwaysRun=true)
public void beforeMethod(ITestContext con, String action) {
	System.out.println("******beforeMethod****");
	 
	 test=(ExtentTest)con.getAttribute("test");
	 app=(ApplicationKeywords)con.getAttribute("app");
	 rep=(ExtentReports)con.getAttribute("reports");
	 String criticalFailure=(String)con.getAttribute("criticalFailure");
	 if(criticalFailure != null && criticalFailure.equals("Y"))
	 {
		 test.log(Status.SKIP,"Critical Failure in previous method");
		 throw new SkipException("Critical Failure in previous method");
	 }
	 con.setAttribute("action", action);

		
}


@AfterTest
public void quit(ITestContext con) {
	app = (ApplicationKeywords)con.getAttribute("app");
	if(app!=null)
		app.quit();
	if(rep!=null)
	    rep.flush();
}


}
