package com.tests;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.utils.ScreenShot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestBase {

    public static WebDriver driver;
    public static Properties prop;
    public static ExtentHtmlReporter htmlreporter;
    public static ExtentReports extent;
    public static ExtentTest test;
    public static Logger logger;

    public TestBase() {
        try {
            prop = new Properties();
            InputStream configInput = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (configInput == null) {
                throw new IOException("Property file 'config.properties' not found in the classpath");
            }
            prop.load(configInput);

            // For Log4j
            String log4jPath = System.getProperty("user.dir") + "/src/test/resources/log4j.properties";
            PropertyConfigurator.configure(log4jPath);

            logger = Logger.getLogger(TestBase.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void initDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @BeforeTest
    public void setupExtentEnv() {
        htmlreporter = new ExtentHtmlReporter("extentreport/extent-report.html");
        htmlreporter.config().setDocumentTitle("Automation Report");
        htmlreporter.config().setReportName("Functional Report");
        htmlreporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlreporter);
        extent.setSystemInfo("Host Name", "Localhost");
        extent.setSystemInfo("OS", "Windows 11");
        extent.setSystemInfo("Tester Name", "Suraj");
        extent.setSystemInfo("Browser", "Chrome");

        logger.info("Extent report setup completed");
    }

    @BeforeMethod
    public void register(Method method) {
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "TEST CASE FAILED: " + result.getName());
            test.log(Status.FAIL, result.getThrowable());
            String screenshotPath = ScreenShot.getScreenshot(driver, result.getName());
            test.addScreenCaptureFromPath(screenshotPath);
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "TEST CASE SKIPPED: " + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "TEST CASE PASSED: " + result.getName());
        }
    }

    @AfterTest
    public void cleanup() {
        extent.flush();
    }

    @AfterSuite
    public void browserTeardown() {
        driver.quit();
    }
}
