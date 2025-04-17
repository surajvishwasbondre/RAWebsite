package com.tests;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pages.LandingPagesAffiliateMarketingPage;
import com.pages.PopupFormPage;

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

public class LandingPagesAffiliateMarketingTest extends TestBase{

	LandingPagesAffiliateMarketingPage LpAffiliateMarketingpg;


	public LandingPagesAffiliateMarketingTest(){
		super();
	}
	
	@BeforeMethod
	public void Setup()
	{
		//initDriver();
		LpAffiliateMarketingpg=new LandingPagesAffiliateMarketingPage(driver);
		//driver.get("https://realtyassistant.in/properties"); 
	}
     
	@Test(priority=1)
	public void VerifyAffiliateMarketingLandingPages() throws InterruptedException, IOException {
        
//    	System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//        driver.manage().window().maximize();
        
        // Path to Excel file
        String excelFilePath = System.getProperty("user.dir") + "/TestData/LandingPageAutomation-Affiliate Marketing.xlsx";


         FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(file); 

            // Assuming the links are in the first sheet and the links are in the first column (index 0)
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows
            for (Row row : sheet) {
            	 if (row.getRowNum() == 0) continue;
                 
            	   String link = row.getCell(0).getStringCellValue();
              
     			// Navigate to initial URL
            	  driver.get(link);
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
                            
                            TakesScreenshot ts = (TakesScreenshot)driver;
                            File scrFile= ts.getScreenshotAs(OutputType.FILE);
                            String filePath = System.getProperty("user.dir") + "/LandingPagesScreenshot-AffiliateMarketing/" + pageTitle + System.currentTimeMillis() + ".png";

                            File destFile=new File(filePath);
                            FileUtils.copyFile(scrFile, destFile);
                           
                        } else {
                            System.out.println("URL is not working. Response Code: " + responseCode);
                            resultCell.setCellValue("Failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    if (driver.getTitle() != "404 Not Found") {
//                    	
//                    	String pageTitle = driver.getTitle();
//                        resultCell.setCellValue("Pass");
////                        
////                        TakesScreenshot ts = (TakesScreenshot)driver;
////                        File scrFile= ts.getScreenshotAs(OutputType.FILE);
////                        String filePath = System.getProperty("user.dir")+"/LandingPagesScreenshot-AffiliateMarketing/"+pageTitle+System.currentTimeMillis()+"png";
////                        File destFile=new File(filePath);
////                        FileUtils.copyFile(scrFile, destFile);
//                    } else {
//                        resultCell.setCellValue("Failed");
//                    }
                }
         // Save the updated Excel file
            FileOutputStream out = new FileOutputStream("TestData/LandingPageAutomation-Affiliate Marketing.xlsx");
            workbook.write(out);
            out.close();
     
            // Close WebDriver
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
