package com.tests;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pages.LandingPageAffiliateMarketingPage2;
import com.pages.LandingPagesAffiliateMarketingPage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LandingPagesAffiliateMarketing2 extends TestBase{

	LandingPageAffiliateMarketingPage2 LpAffiliateMarketingpg2;


	public LandingPagesAffiliateMarketing2(){
		super();
	}
	
	@BeforeMethod
	public void Setup()
	{
		//initDriver();
		LpAffiliateMarketingpg2=new LandingPageAffiliateMarketingPage2(driver);
		//driver.get("https://realtyassistant.in/properties"); 
	}
     
	@Test(priority=1)
	public void VerifyAffiliateMarketingLandingPages() throws InterruptedException, IOException {
        
        // Path to Excel file
        String excelFilePath ="TestData\\LP Affiliate Marketing SampleData.xlsx";

         FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(file); 

         
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
            	 if (row.getRowNum() == 0) continue;
                 
            	   String link = row.getCell(0).getStringCellValue();
              
     			// Navigate to initial URL
            	  driver.get(link);
            	  
            	  WebDriverWait wait = new WebDriverWait(driver,50);
            	  WebElement popupEle = driver.findElement(By.partialLinkText("Register here and Avail the"));
            	  //   //button[@name="submit_form"]//following::button[15]
            	  wait.until(ExpectedConditions.visibilityOf(popupEle));
      			driver.switchTo().activeElement();
                  
                 //frameToBeAvailableAndSwitchToIt(popupEle)
                 
      			WebElement nameField = driver.findElement(By.name("name"));
      			nameField.sendKeys("Test Lead");
      			
      			WebElement mobileField = driver.findElement(By.name("phone"));
      			mobileField.sendKeys("9999999999");
      			
      			WebElement emailField = driver.findElement(By.name("email"));
      			emailField.sendKeys("test@gmail.com");
      			
      			WebElement submitButton = driver.findElement(By.name("submit_form"));
      			submitButton.click();
      			
      			
      			Cell formresultCell = row.createCell(3);
      			 if (driver.getCurrentUrl().contains("thank-you.php")) {
      	            System.out.println("Thank You page is displayed. Form submitted successfully!");
      	          formresultCell.setCellValue("Pass");
      	        } else {
      	            System.out.println("Form submission failed or Thank You page not displayed!");
      	          formresultCell.setCellValue("Failed");
      	        }
      			WebElement BackToHome = driver.findElement(By.className("fa fa-undo"));
      			BackToHome.click();
      			 //driver.switchTo().defaultContent();      
            	// Get the current time and date
                  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
                  Date date = new Date();
                  String currentDate = dateFormat.format(date);

                  // Update the Excel file with the current time and date
                  Cell dateCell = row.createCell(2);
                  dateCell.setCellValue(currentDate);
            	  String urlToCheck = driver.getCurrentUrl();
            	  String pageTitle = driver.getTitle();
                  System.out.println(link);
                    
                    // Pause for a moment (you might want to replace this with a more sophisticated wait mechanism)
                   // Thread.sleep(2000);
                    
                    Cell resultCell = row.createCell(1);
                    
                    try {
                        int responseCode = getResponseCode(urlToCheck);
                        
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            System.out.println("URL is working fine. Response Code: " + responseCode);
                            resultCell.setCellValue("Pass");
                           // driver.getCurrentUrl();
                            
//                            TakesScreenshot ts = (TakesScreenshot)driver;
//                            File scrFile= ts.getScreenshotAs(OutputType.FILE);
//                            String filePath = System.getProperty("user.dir")+"/LandingPagesScreenshot-AffiliateMarketing/"+pageTitle+System.currentTimeMillis()+"png";
//                            File destFile=new File(filePath);
//                            FileUtils.copyFile(scrFile, destFile);
                           
                        } else {
                            System.out.println("URL is not working. Response Code: " + responseCode);
                            resultCell.setCellValue("Failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        
            FileOutputStream out = new FileOutputStream("TestData\\LP Affiliate Marketing SampleData.xlsx");
            workbook.write(out);
            out.close();
     
       
            driver.quit();
            }
    public static int getResponseCode(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode;
    }

       
    }
