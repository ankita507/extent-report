import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
//import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportGetCode {
		
	    ExtentHtmlReporter reporter;
		ExtentReports reports;
		ExtentTest logger;
		WebDriver driver;
		
		
		@BeforeClass
		public void startTest()
		{
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-ms");
			String path=System.getProperty("user.dir")+"/"+sd.format(new Date())+".html";
			reporter=new ExtentHtmlReporter(path);
			reports=new ExtentReports();
			reports.attachReporter(reporter);
			reports.setSystemInfo("Hostname", "Localhost");
			reports.setSystemInfo("Environment", "Testing Environment");
			reports.setSystemInfo("Username", "Remi");
			
			reporter.config().setDocumentTitle("Remi's Reports");
			//reporter.config().setReportName"Next Gen Testing Reports");
			reporter.config().setDocumentTitle("Next Gen Testing Report");
			reporter.config().setTheme(Theme.DARK);
		}
		
		
		@AfterClass
		public void endTest() {
			reports.flush();
		}
		
		@AfterMethod
		public void captureStatus(ITestResult result)
		{
			if (result.getStatus()==ITestResult.SUCCESS) {
				logger.log(Status.PASS, result.getMethod().getMethodName()+" test is passed");
				
			}
			else if (result.getStatus()==ITestResult.FAILURE)
			{
				String imagepath=System.getProperty("User.dir")+"/extent-reports/capture/"+result.getMethod().getMethodName()+".png";
				logger.log(Status.FAIL, result.getMethod().getMethodName()+" test is failed");
				TakesScreenshot capture=(TakesScreenshot) driver;
				File src=capture.getScreenshotAs(OutputType.FILE);
			
				try {
					
					FileUtils.copyFile(src, new File(imagepath));
					logger.addScreenCaptureFromPath(imagepath);
					
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
			}
			else if (result.getStatus()==ITestResult.SKIP)
			{
				logger.log(Status.SKIP, result.getMethod().getMethodName()+ " test is skipped");
				
			}
		}
		@Test
		public void passTest()
		{
			logger=reports.createTest("Pass Test");
			Assert.assertTrue(true);
		}
		
		@Test
		public void demowebshoploginTest()
		{
			logger=reports.createTest("FAIL TEST");
			System.setProperty("webdriver.chrome.driver", "C:\\selenium_drivers\\chromedriver\\chromedriver.exe");
            driver=new ChromeDriver();  
			driver.get("http://demowebshop.tricentis.com/login");
			driver.manage().window().maximize();
			driver.findElement(By.id("Email")).sendKeys("remi.jullian@accenture.com");
			driver.findElement(By.id("Pass")).sendKeys("test@1234");
			
		}
		
		
		@Test
		public void skipTest() {
			logger=reports.createTest("SKIP TEST");
			throw new SkipException("Some reason");
		}
		
		
		

	

}
