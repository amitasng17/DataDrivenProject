package TestCases;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import keywords.ApplicationKeywords;



public class CreatePortfolioTest {
	
@Test
public void createPortfolioTest(ITestContext con) throws InterruptedException, IOException {
ApplicationKeywords app=new ApplicationKeywords();
 app.LaunchBrowser("Chrome");
 app.navigateurl("url");
 app.type("username_id","er_amita");	
 app.type("password_xpath","Pass@12345");
 Thread.sleep(10000);
 app.click("login_button_id");
 JSONObject data=(JSONObject)con.getAttribute("data");
 String portfolioName=(String)data.get("portfolioname");
 
 

}

}
