package TestScript;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.LogStatus;

import CommonFunctions.CommonFunction;

public class KeywordDriver {
	
	public void executeKeywords(String action,String target, String val, Xls_Reader xrtd, int k) throws Throwable
	{
		String value = ReplaceData(val,xrtd,k);
		switch(action)
		{
		case "open":
			open(target);
			break;
		case "verifyTitle":
			verifyTitle(target);
			break;
		case "verifyElementPresent":
			verifyElementPresent(findlocator(target));
			break;
		case "verifyText":
			verifyText(findlocator(target),value);
			break;
		case "type":
			type(findlocator(target),value);
			break;
		case "verifyValue":
			verifyValue(findlocator(target),value);
			break;
		case "clickAndWait":
			click(findlocator(target));
			break;
		case "click":
			click(findlocator(target));
			break;
		}
	}

	static String getAlphaNumericString() 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(10); 
  
        for (int i = 0; i < 10; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
	public String ReplaceData(String value,Xls_Reader xrtd, int k)
	{
		String data = "";
		if(value.startsWith("TestData"))
		{
			data = ReadTestData(value,xrtd,k);
		}
		if(value.equals("RandomData"))
		{
			data = getAlphaNumericString();
		}
		return data;
	}
	
	public String ReadTestData(String value,Xls_Reader xrtd, int k)
	{
		return xrtd.getCellData(DriverScript.Modules, value, k).trim();
	}
	public void click(WebElement elm) throws Throwable {
		boolean val = false;
		try
		{
		val = elm.isDisplayed();	
		elm.click();
		DriverScript.logger.log(LogStatus.PASS, "Element Clicked successfuly");
		}
		catch(Throwable t)
		{	
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, t.getMessage()+"<a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
		
	}

	public void verifyValue(WebElement elm, String value) throws Throwable {
		String val = elm.getAttribute("value");
		if(value.equals(val))
		{
			System.out.println("PASSED");
			DriverScript.logger.log(LogStatus.PASS, "Expected value verified successfuly");
		}
		else
		{
			System.out.println("FAILED");
			//DriverScript.logger.log(LogStatus.FAIL, "Expected value did not match");
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, "Expected value did not match  <a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
	}

	public void type(WebElement elm, String value) throws Throwable {
		boolean val = false;
		try
		{
		val = elm.isDisplayed();			
		}
		catch(Throwable t)
		{			
			//DriverScript.logger.log(LogStatus.FAIL, t.getMessage());
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, t.getMessage()+"<a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
		if(val)
		{
			elm.clear();
			elm.sendKeys(value);
			DriverScript.logger.log(LogStatus.PASS, "text entered successfuly");
		}
		
				
	}

	public void verifyText(WebElement elm, String value) throws Throwable {
		String txt = "";
		try
		{
		txt = elm.getText().trim();
		}
		catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
		if(txt.equals(value))
		{
			System.out.println("PASSED");
			DriverScript.logger.log(LogStatus.PASS, "text verified successfuly");
		}
		else
		{
			System.out.println("FAILED");
			//DriverScript.logger.log(LogStatus.FAIL, "text did not match");
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, "text did not match <a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
	}

	public void verifyElementPresent(WebElement elm) throws Throwable {
		boolean val = false;
		try
		{
		 val = elm.isDisplayed();
		}
		catch(Throwable t)
		{
			t.getMessage();			
		}
		
		if(val)
		{
			System.out.println("Passed");
			DriverScript.logger.log(LogStatus.PASS, "Element present on page");
		}
		else
		{
			System.out.println("FAILED");
			//DriverScript.logger.log(LogStatus.FAIL, "Element not found");
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, "Element not found <a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
	}

	public WebElement findlocator(String target)	
	{
		WebElement elm = null;
	try
	{
		if(target.startsWith("//"))
		{
			elm = DriverScript.driver.findElement(By.xpath(target));
		}
		else if(target.startsWith("name="))
		{
			elm = DriverScript.driver.findElement(By.name(target.replace("name=", "")));
		}
		else if(target.startsWith("css="))
		{
			elm = DriverScript.driver.findElement(By.cssSelector(target.replace("css=", "")));
		}
		else if(target.startsWith("link="))
		{
			elm = DriverScript.driver.findElement(By.linkText(target.replace("link=", "")));
		}
		else if(target.startsWith("id="))
		{
			elm = DriverScript.driver.findElement(By.id(target.replace("id=", "")));
		}
	}
	catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
		return elm;
	}
	
	public void verifyTitle(String target) throws Throwable {
		if(DriverScript.driver.getTitle().trim().equals(target))
		{
			System.out.println("Passed");
			DriverScript.logger.log(LogStatus.PASS, "Title matched");
		}
		else
		{
			System.out.println("FAiled");
			//DriverScript.logger.log(LogStatus.FAIL, "Title did not match");
			String file = CommonFunction.getscreenshot(DriverScript.TestCases, DriverScript.driver);
			DriverScript.logger.log(LogStatus.FAIL, "Title did not match <a href="+file+"><span class='test-status label pass'>Screenshot</span></a>");
		}
		
	}

	public void open(String target) {
		DriverScript.driver.navigate().to(DriverScript.AppUrl+target);		
	}

}
