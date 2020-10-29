package com.ebanking.testcases;

import java.io.IOException;

import org.apache.logging.log4j.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ebanking.pageObjects.LoginPage;
import com.ebanking.utilities.ExcelUtils;
import com.ebanking.utilities.StoringCookie;

import resources.base;




public class TC_loginTest_001 extends base {

	public WebDriver driver;
	public static Logger log = LogManager.getLogger(base.class.getName());
	//how to send different browser from testng xml
	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browserParam) throws IOException {
		log.info("Driver need to initialize");
		driver = initializeDriver(browserParam);		
		log.info("Driver initialized");
		//xdriver.manage().window().maximize();
		//xdriver.manage().deleteAllCookies();
		
	}
	
	
	
	
	@Test
	public void loginTest() {
		
		//xdriver.get(url);
		log.info("LoginPage instance is created");
		LoginPage lp = new LoginPage(driver);
		
		lp.setUsername(userName);
		lp.setPassword(password);
		lp.getLogonButton();
		log.info("Button clicked");
		
		StoringCookie.getCookie(driver);
		//Assert.assertTrue(true);
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Guru99 Bank']")));
		
				
		if(driver.getTitle().contains("Bank Manager")) {
			
			Assert.assertTrue(true);
			log.info("Successfully validated test cases");
		}else {
			Assert.assertTrue(false);
		}
	}
	
	
	@Test(dataProvider ="LoginData")
	public void loginDDT(String user,String pwd) {
		
		LoginPage lx = new LoginPage(driver);
		
		lx.setUsername(user);
		log.info("Username provided: "+user);
		
		lx.setPassword(pwd);	
		log.info("Password provided: "+pwd);
		
		lx.getLogonButton();
		log.info("Button Clicked");
		
		if(isAlertPresent()) {
			 driver.switchTo().alert().accept();
			 driver.switchTo().defaultContent();
			Assert.assertTrue(false, "Login Failed");
			log.warn("Logout unsuccessful at"+user+" and "+pwd);
		}
		else {
			Assert.assertTrue(true, "Logged in Successfully");
			log.info("Successfully logged in at "+user+" and "+pwd);
		}
		
	}
	
	public boolean isAlertPresent() {
	try {
    driver.switchTo().alert();
    return true;
    
    }catch (NoAlertPresentException ex) {
		return false;
	}
	
    }   
	
	

	@DataProvider(name="LoginData")
	public String[][] getData() throws Exception{
		
	//Locate the excel file path
	String path = System.getProperty("user.dir")+"//src//test//java//com//ebanking//testData//LoginData.xlsx"	;
	
	
	
	int rownum = ExcelUtils.getRowCount(path, "Sheet1");
	int cellCount = ExcelUtils.getCellCount(path, "Sheet1", rownum);
	
	//Then create 2 dimensional array and the size depends uponrows and column of the excel sheet
	String loginData[][] = new String[rownum][cellCount];
	
	//then iterate through each and ecery row to get the cell data
	for (int i = 1; i <=rownum; i++) {
		
		for (int j = 0; j <cellCount;j++) {
			
			loginData[i-1][j] = ExcelUtils.getCellData(path, "Sheet1",i,j);			
			
			
			
		}
		
	}
	
	return loginData;
		
	}
	

	
	
	
	
	
	
	
	@AfterMethod
	public void tearDown() {
		
		driver.close();
		log.info("Driver is closed");
	}
	
	
	
}
