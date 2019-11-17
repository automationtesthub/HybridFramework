package TestScript;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import CommonFunctions.CommonFunction;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;

public class DriverScript {
	
	public static String AppUrl;
	public static String AppName;
	public static String TestDataFile;
	public static String ScenarioFile;
	public static String Modules;
	public static String Priority;
	public static String TestCases;
	public static String Description;
	public static String browser;
	
	public static WebDriver driver;
	public  static ExtentReports report;
	public static ExtentTest logger; 
	public  static String extentReport;

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		KeywordDriver kd = new KeywordDriver();
		Xls_Reader xr = new Xls_Reader(System.getProperty("user.dir")+"/src/test/java/DriverFiles/AppDriver.xlsx");
		
		int rowcount= xr.getRowCount("App");
		System.out.println(rowcount);
		for(int i=2;i<=rowcount;i++)
		{
			String vRun= xr.getCellData("App", "Run", i).trim();
			if(vRun.equalsIgnoreCase("ON"))
			{
				AppUrl = xr.getCellData("App", "AppUrl", i).trim();
				AppName = xr.getCellData("App", "AppName", i).trim();
				TestDataFile = xr.getCellData("App", "TestDataFile", i).trim();
				ScenarioFile = xr.getCellData("App", "ScenarioFile", i).trim();
				browser = xr.getCellData("App", "Browser", i).trim();
				//############### Report################
				CommonFunction cm= new CommonFunction();
				report=cm.setupResult(AppName);
				//######################################
				
				
				if(browser.equals("Firefox"))
				{
					FirefoxDriverManager.getInstance().setup();
					driver = new FirefoxDriver();
					driver.get(AppUrl);
				}
				else 
				{					
					//ChromeDriverManager.getInstance().setup();
					System.setProperty("webdriver.chrome.driver", "E:/Selenium/Selenium_Software/chromedriver.exe");
					driver = new ChromeDriver();
					driver.get(AppUrl);
				}
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				
				Xls_Reader xrtd = new Xls_Reader(System.getProperty("user.dir")+"/src/test/java/TestData/"+TestDataFile+".xlsx");
				Xls_Reader xrsc = new Xls_Reader(System.getProperty("user.dir")+"/src/test/java/Scenarios/"+ScenarioFile+".xlsx");
				int modcount= xr.getRowCount(AppName);
				for(int j=2;j<=modcount;j++)
				{
					String vModRun= xr.getCellData(AppName, "Run", j).trim();
					if(vModRun.equalsIgnoreCase("ON"))
					{
						Modules = xr.getCellData(AppName, "Modules", j).trim();
						Priority = xr.getCellData(AppName, "Priority", j).trim();
						int tccount= xrtd.getRowCount(Modules);
						for(int k=2;k<=tccount;k++)
						{
							String vTCRun= xrtd.getCellData(Modules, "Run", k).trim();
							if(vTCRun.equalsIgnoreCase("ON"))
							{
								TestCases = xrtd.getCellData(Modules, "TestCases", k).trim();
								Description = xrtd.getCellData(Modules, "Description", k).trim();
								System.out.println(TestCases);
								logger = report.startTest(TestCases);
								int sccount= xrsc.getRowCount(Modules);
								int flag = 0;
								int tcrownum = 0;
								for(int m=2;m<=sccount;m++)
								{
									String Action= xrsc.getCellData(Modules, "Action", m).trim();
									if(Action.replace("TC-", "").equals(TestCases))
									{
										flag = 1;
										tcrownum = m;
									}
									
									if((flag == 1) && (m>tcrownum))
									{
										if(Action.startsWith("TC-"))
										{
											break;
										}
										else
										{
											String keyword = Action;											
											System.out.println(keyword);
											String Target = xrsc.getCellData(Modules, "Target", m).trim();
											String value = xrsc.getCellData(Modules, "Value", m).trim();
											kd.executeKeywords(Action, Target, value,xrtd,k);
										}
									}
									
								}
								
								report.endTest(logger);
								report.flush();
								
							}
						}
						
					}
				}
				
					
				driver.quit();
			}
		}

	}

}
