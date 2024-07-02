package keywords;

import org.openqa.selenium.support.ui.Select;

//import org.openqa.selenium.support.ui.Select;

public class ValidationKeywords extends GenericKeywords{

	
public void ValidateTitle() {
	
}

public void ValidateText() {
	
}

public void ValidateElementPresent(String locator) {
	boolean result=isElementPresent(locator);
	if(!result)
	reportFailure("Element not found"+locator,true);
	
}
	
public void validateSelectedValueInDropDown(String locatorKey, String option) {
	Select s = new Select(GetElement(locatorKey));
	String text = s.getFirstSelectedOption().getText();
	if(!text.equals(option)){
		reportFailure("Option"+option+" not present in Drop Down "+locatorKey,true);
	}
	
}

public void validateSelectedValueNotInDropDown(String locatorKey, String option) {
	Select s = new Select(GetElement(locatorKey));
	String text = s.getFirstSelectedOption().getText();
	if(text.equals(option)){
		reportFailure("Option"+option+" present in Drop Down "+locatorKey,true);
	}
	
}
}
