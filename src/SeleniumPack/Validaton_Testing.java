package SeleniumPack;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.openqa.selenium.support.ui.WebDriverWait;

@Listeners(Validaton_Testing.class)
public class Validaton_Testing implements ITestListener{

	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;

	@BeforeClass
	public void setUp() {
		
		
// ********************** Html Report File With Date and Time  And Setup Chrome Driver********************** //

// Html Report File Grenerate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(formatter);
		String defaultPath = System.getProperty("user.dir");
		String reportPath = defaultPath + "\\extent-report_" + timestamp + ".html";
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// SetUp Chrome driver Path & Get Chrome Driver
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
//		driver.get("https://smartmach-demo.smartories.com/Login");
		driver.get("http://localhost/Selenium_Smart/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
	}

// ********************** Username Password And Login Test ********************** //
	@Test(priority = 1)
	public void CorrectUserPassTesting() throws InterruptedException {
		    test = extent.createTest("CorrectUserPassTesting");

		    // Get the URL before login
		    String beforeUrl = driver.getCurrentUrl();

		    // Login Process
		    WebElement usernameInput1 = driver.findElement(By.id("username"));
		    WebElement passwordInput1 = driver.findElement(By.id("userpassword"));
		    WebElement loginButton = driver.findElement(By.id("login_submit"));

		    usernameInput1.sendKeys("adminadmin@gmail.com");
		    passwordInput1.sendKeys("Admin@123");
		    loginButton.click();

// Perform actions after login 
		    
		    try {
		        // Other actions after successful login
		        String redirectedUrl = driver.getCurrentUrl();
		        if (!beforeUrl.equals(redirectedUrl)) {
		            test.log(Status.PASS, "Login successful");
		        } else {
		            test.log(Status.FAIL, "Login failed");
		        }
		    } catch (StaleElementReferenceException e) {
		        // Handle stale element reference exception gracefully
		        test.log(Status.FAIL, "StaleElementReferenceException: " + e.getMessage());      
		    }
		    
		} 
 	
// ********************** Machine Module Add Machine Input Validation ********************** //
	@Test(priority = 2, dependsOnMethods = {"CorrectUserPassTesting"})
	public void MachineModuleTesting_Validation() throws InterruptedException {
			
			test = extent.createTest("MachineModuleTesting_Validation");
			WebElement SelectSite_T = driver.findElement(By.id("site_id"));
			SelectSite_T.click();
			Thread.sleep(3000);
			String beforeUrlClick_T = driver.getCurrentUrl();
			WebElement hoverElement_T = driver.findElement(By.className("fa-gear"));
			Actions actions_T = new Actions(driver);
			actions_T.moveToElement(hoverElement_T).perform();
			Thread.sleep(1000);
			WebElement buttonToClick_T = driver.findElement(By.xpath("/html/body/div/div[1]/ul/li[4]/ul/li[1]/div[2]/a"));
			actions_T.moveToElement(buttonToClick_T).perform();
			buttonToClick_T.click();
			String AfterUrlClick_T = driver.getCurrentUrl();
			if(!beforeUrlClick_T.equals(AfterUrlClick_T) ) {
				test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
			} else {
				test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
			}
			

		
			
			
			test = extent.createTest("Insert Proper Data in Machines Machine Rate Hour");
			WebElement AddMachine = driver.findElement(By.id("add_machine_button"));
			AddMachine.click();
			
			WebElement inputMachineName = driver.findElement(By.id("inputMachineName"));
			String[] inputMachineNamevalue = {""}; 
			for (String value : inputMachineNamevalue) {
				inputMachineName.clear();
				inputMachineName.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean nameErrorField_4 = driver.findElement(By.id("inputMachineNameErr")).isDisplayed();
			    if (nameErrorField_4 == true) {
			        test.log(Status.PASS, "Empty value validation for inputMachineName input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for inputMachineName input with value: " + value + " failed");
			    }
			} 
			Thread.sleep(100);
			String[] inputMachineNamesql = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
			for (String value : inputMachineNamesql) {
				inputMachineName.clear(); 
				inputMachineName.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean sqlErrorField_4 = driver.findElement(By.id("inputMachineNameErr")).isDisplayed();
			    if (sqlErrorField_4 == true) {
			        test.log(Status.PASS, "SQL Injection validation for inputMachineName input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for inputMachineName input with value: " + value + " failed");
			    }
			}
		
			
			WebElement inputMachineRateHour = driver.findElement(By.id("inputMachineRateHour"));
			// Test with negative values
			String[] negativeValues = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
			for (String value : negativeValues) {
			    inputMachineRateHour.clear(); // Clear the input field
			    inputMachineRateHour.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField == false) {
			        test.log(Status.PASS, "Negative value validation for Machine Rate Hour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Negative value validation for Machine Rate Hour input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			// Test with float values
			String[] floatValues = {"12.5", "0.01", "3.14"}; // Add more float values as needed
			for (String value : floatValues) {
			    inputMachineRateHour.clear(); // Clear the input field
			    inputMachineRateHour.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField == false) {
			        test.log(Status.PASS, "Float value validation for Machine Rate Hour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Float value validation for Machine Rate Hour input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			String[] MixedValue = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
			for (String value : MixedValue) {
			    inputMachineRateHour.clear(); // Clear the input field
			    inputMachineRateHour.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField == false) {
			        test.log(Status.PASS, "MixedValue validation for Machine Rate Hour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "MixedValue validation for Machine Rate Hour input with value: " + value + " failed");
			    }
			}
			
			Thread.sleep(100);
			// Machine Input - Machine OFF Rate
			
			test = extent.createTest("Insert Proper Data in Machines Machine OFF Rate Hour");
			WebElement nputMachineOffRateHour = driver.findElement(By.id("inputMachineOffRateHour"));

			// Test with negative values
			String[] negativeValues_1 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
			for (String value : negativeValues_1) {
				nputMachineOffRateHour.clear(); // Clear the input field
				nputMachineOffRateHour.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_1 = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField_1 == false) {
			        test.log(Status.PASS, "Negative value validation for inputMachineRateHour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Negative value validation for inputMachineRateHour input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			// Test with float values
			String[] floatValues_1 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
			for (String value : floatValues_1) {
				nputMachineOffRateHour.clear(); // Clear the input field
				nputMachineOffRateHour.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField_2 = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField_2 == false) {
			        test.log(Status.PASS, "Float value validation for inputMachineRateHour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Float value validation for inputMachineRateHour input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			String[] MixedValue_1 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
			for (String value : MixedValue_1) {
				nputMachineOffRateHour.clear(); // Clear the input field
				nputMachineOffRateHour.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField_3 = driver.findElement(By.id("inputMachineRateHourErr")).isDisplayed();
			    if (ErrorField_3 == false) {
			        test.log(Status.PASS, "MixedValue validation for inputMachineRateHour input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "MixedValue validation for inputMachineRateHour input with value: " + value + " failed");
			    }
			}
			
			Thread.sleep(100);
			// Machine Input - Tonnage
			
			test = extent.createTest("Insert Proper Data in Machines Machine inputTonnage");
			WebElement inputTonnage = driver.findElement(By.id("inputTonnage"));
			//Test with Empty Value
			String[] EmptyValue_3 = {""};
			for (String value : EmptyValue_3) {
				inputTonnage.clear(); // Clear the input field
				inputTonnage.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_4 = driver.findElement(By.id("inputTonnageErr")).isDisplayed();
			    if (ErrorField_4 == true) {
			        test.log(Status.PASS, "Empty value validation for inputTonnage input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for inputTonnage input with value: " + value + " failed");
			    }
			}
			
			Thread.sleep(100);

			// Test with negative values
			String[] negativeValues_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
			for (String value : negativeValues_3) {
				inputTonnage.clear(); // Clear the input field
				inputTonnage.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_1 = driver.findElement(By.id("inputTonnageErr")).isDisplayed();
			    if (ErrorField_1 == false) {
			        test.log(Status.PASS, "Negative value validation for inputTonnage input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Negative value validation for inputTonnage input with value: " + value + " failed");
			    }
			}

			// Test with float values
			String[] floatValues_3 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
			for (String value : floatValues_3) {
				inputTonnage.clear(); // Clear the input field
				inputTonnage.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField_2 = driver.findElement(By.id("inputTonnageErr")).isDisplayed();
			    if (ErrorField_2 == false) {
			        test.log(Status.PASS, "Float value validation for inputTonnage input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Float value validation for inputTonnage input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			
			//Test With Mixed Value
			String[] MixedValue_3 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
			for (String value : MixedValue_3) {
				inputTonnage.clear(); // Clear the input field
				inputTonnage.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("AddMachineModal1"));
			    container_click.click();
			    boolean ErrorField_3 = driver.findElement(By.id("inputTonnageErr")).isDisplayed();
			    if (ErrorField_3 == false) {
			        test.log(Status.PASS, "MixedValue validation for inputTonnage input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "MixedValue validation for inputTonnage input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			
			//Test With SQL Query
			String[] SqlValues_3 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"};
			for (String value : SqlValues_3) {
				inputTonnage.clear(); // Clear the input field
				inputTonnage.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_4 = driver.findElement(By.id("inputTonnageErr")).isDisplayed();
			    if (ErrorField_4 == true) {
			        test.log(Status.PASS, "SQL Injection validation for inputTonnage input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for inputTonnage input with value: " + value + " failed");
			    }
			}

			
			
			Thread.sleep(100);
			
			
			
			
			// Machine Input - Brand
			test = extent.createTest("Insert Data in Machines Brand");
			WebElement inputMachineBrand = driver.findElement(By.id("inputMachineBrand"));

			// Test with negative values
			String[] negativeValues_4 = {""}; // Add more negative values as needed
			for (String value : negativeValues_4) {
				inputMachineBrand.clear(); // Clear the input field
				inputMachineBrand.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_4 = driver.findElement(By.id("inputMachineBrandErr")).isDisplayed();
			    if (ErrorField_4 == true) {
			        test.log(Status.PASS, "Empty value validation for MachineBrand input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for MachineBrand input with value: " + value + " failed");
			    }
			} 
			
			Thread.sleep(100);
			String[] negativeValues_5 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; // Add more negative values as needed
			for (String value : negativeValues_5) {
				inputMachineBrand.clear(); // Clear the input field
				inputMachineBrand.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean ErrorField_4 = driver.findElement(By.id("inputMachineBrandErr")).isDisplayed();
			    if (ErrorField_4 == true) {
			        test.log(Status.PASS, "SQL Injection validation for MachineBrand input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for MachineBrand input with value: " + value + " failed");
			    }
			}
			
			
			Thread.sleep(100);
			// Machine Input - SerialID
			test = extent.createTest("Insert Data in Machines SerialID");
			WebElement inputMachineSerialId = driver.findElement(By.id("inputMachineSerialId"));

			// Test with Empty values
			String[] inputMachineSerialIdValues_4 = {""}; // Add more negative values as needed
			for (String value : inputMachineSerialIdValues_4) {
				inputMachineSerialId.clear(); // Clear the input field
				inputMachineSerialId.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean inputMachineSerialIdErrorField_4 = driver.findElement(By.id("inputMachineSerialId_err")).isDisplayed();
			    if (inputMachineSerialIdErrorField_4 == true) {
			        test.log(Status.PASS, "Empty value validation for MachineSerialId input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for MachineSerialId input with value: " + value + " failed");
			    }
			} 
			Thread.sleep(100);
			String[] inputMachineSerialIdValues_5 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; // Add more negative values as needed
			for (String value : inputMachineSerialIdValues_5) {
				inputMachineSerialId.clear(); // Clear the input field
				inputMachineSerialId.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("AddMachineModal1"));
			    container_click_1.click();
			    boolean inputMachineSerialIdValuesErrorField_4 = driver.findElement(By.id("inputMachineSerialId_err")).isDisplayed();
			    if (inputMachineSerialIdValuesErrorField_4 == true) {
			        test.log(Status.PASS, "SQL Injection validation for MachineSerialId input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for MachineSerialId input with value: " + value + " failed");
			    }
			}
			
			Thread.sleep(500);
			WebElement CancelButton = driver.findElement(By.className("btn_cancel"));
			CancelButton.click();
	}

	
// ********************** Machine Module Edit Input Validation ********************** //
	@Test(priority = 2, dependsOnMethods = {"CorrectUserPassTesting","MachineModuleTesting_Validation"})
	public void MachineModuleTesting_Edit_Validation() throws InterruptedException {
		test = extent.createTest("EditOptioninmachin_Validation");
		WebElement option_Button2 = driver.findElement(By.className("edit-menu"));
		option_Button2.click();
		
		WebElement Edit_Machine = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_Machine.click();
		
		WebElement editMachineName = driver.findElement(By.id("editMachineName"));
		WebElement editMachineRateHour = driver.findElement(By.id("editMachineRateHour"));
		WebElement editMachineOffRateHour = driver.findElement(By.id("editMachineOffRateHour"));
		WebElement editTonnage = driver.findElement(By.id("editTonnage"));
		WebElement editMachineBrand = driver.findElement(By.id("editMachineBrand"));
		WebElement editMachineSerialNumber = driver.findElement(By.id("editMachineSerialNumber"));
		editMachineName.clear();
		editMachineRateHour.clear();
		editMachineOffRateHour.clear();
		editTonnage.clear();
		editMachineBrand.clear();
		editMachineSerialNumber.clear();
		
		//Edit Input Validation 
		WebElement inputMachineName = driver.findElement(By.id("editMachineName"));
		String[] inputMachineNamevalue = {""}; // Add more negative values as needed
		for (String value : inputMachineNamevalue) {
			inputMachineName.clear(); // Clear the input field
			inputMachineName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean nameErrorField_4 = driver.findElement(By.id("editMachineNameErr")).isDisplayed();
		    if (nameErrorField_4 == true) {
		        test.log(Status.PASS, "Empty value validation for EditMachineName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for EditMachineName input with value: " + value + " failed");
		    }
		} 
		Thread.sleep(100);
		String[] inputMachineNamesql = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; // Add more negative values as needed
		for (String value : inputMachineNamesql) {
			inputMachineName.clear(); // Clear the input field
			inputMachineName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean sqlErrorField_4 = driver.findElement(By.id("editMachineNameErr")).isDisplayed();
		    if (sqlErrorField_4 == true) {
		        test.log(Status.PASS, "SQL Injection validation for EditMachineName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for EditMachineName input with value: " + value + " failed");
		    }
		}
	
		
		WebElement inputMachineRateHour = driver.findElement(By.id("editMachineRateHour"));
		// Test with negative values
		String[] negativeValues = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : negativeValues) {
		    inputMachineRateHour.clear(); // Clear the input field
		    inputMachineRateHour.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField = driver.findElement(By.id("editMachineRateHourErr")).isDisplayed();
		    if (ErrorField == false) {
		        test.log(Status.PASS, "Negative value validation for Machine Rate Hour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for Machine Rate Hour input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		// Test with float values
		String[] floatValues = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : floatValues) {
		    inputMachineRateHour.clear(); // Clear the input field
		    inputMachineRateHour.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField = driver.findElement(By.id("editMachineRateHourErr")).isDisplayed();
		    if (ErrorField == false) {
		        test.log(Status.PASS, "Float value validation for Machine Rate Hour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for Machine Rate Hour input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		String[] MixedValue = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : MixedValue) {
		    inputMachineRateHour.clear(); // Clear the input field
		    inputMachineRateHour.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField = driver.findElement(By.id("editMachineRateHourErr")).isDisplayed();
		    if (ErrorField == false) {
		        test.log(Status.PASS, "MixedValue validation for Machine Rate Hour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for Machine Rate Hour input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100);
		// Machine Input - Machine OFF Rate
		
		test = extent.createTest("Insert Proper Data in Machines Machine OFF Rate Hour");
		WebElement nputMachineOffRateHour = driver.findElement(By.id("editMachineOffRateHour"));

		// Test with negative values
		String[] negativeValues_1 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : negativeValues_1) {
			nputMachineOffRateHour.clear(); // Clear the input field
			nputMachineOffRateHour.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_1 = driver.findElement(By.id("editMachineOffRateHourErr")).isDisplayed();
		    if (ErrorField_1 == false) {
		        test.log(Status.PASS, "Negative value validation for inputMachineRateHour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for inputMachineRateHour input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		// Test with float values
		String[] floatValues_1 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : floatValues_1) {
			nputMachineOffRateHour.clear(); // Clear the input field
			nputMachineOffRateHour.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField_2 = driver.findElement(By.id("editMachineOffRateHourErr")).isDisplayed();
		    if (ErrorField_2 == false) {
		        test.log(Status.PASS, "Float value validation for inputMachineRateHour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for inputMachineRateHour input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		String[] MixedValue_1 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : MixedValue_1) {
			nputMachineOffRateHour.clear(); // Clear the input field
			nputMachineOffRateHour.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField_3 = driver.findElement(By.id("editMachineOffRateHourErr")).isDisplayed();
		    if (ErrorField_3 == false) {
		        test.log(Status.PASS, "MixedValue validation for inputMachineRateHour input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for inputMachineRateHour input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100);
		// Machine Input - Tonnage
		
		test = extent.createTest("Insert Proper Data in Machines Machine inputTonnage");
		WebElement inputTonnage = driver.findElement(By.id("editTonnage"));
		//Test with Empty Value
		String[] EmptyValue_3 = {""};
		for (String value : EmptyValue_3) {
			inputTonnage.clear(); // Clear the input field
			inputTonnage.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_4 = driver.findElement(By.id("editTonnageErr")).isDisplayed();
		    if (ErrorField_4 == true) {
		        test.log(Status.PASS, "Empty value validation for inputTonnage input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for inputTonnage input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100);

		// Test with negative values
		String[] negativeValues_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : negativeValues_3) {
			inputTonnage.clear(); // Clear the input field
			inputTonnage.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_1 = driver.findElement(By.id("editTonnageErr")).isDisplayed();
		    if (ErrorField_1 == false) {
		        test.log(Status.PASS, "Negative value validation for inputTonnage input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for inputTonnage input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] floatValues_3 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : floatValues_3) {
			inputTonnage.clear(); // Clear the input field
			inputTonnage.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField_2 = driver.findElement(By.id("editTonnageErr")).isDisplayed();
		    if (ErrorField_2 == false) {
		        test.log(Status.PASS, "Float value validation for inputTonnage input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for inputTonnage input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] MixedValue_3 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : MixedValue_3) {
			inputTonnage.clear(); // Clear the input field
			inputTonnage.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditMachineModal1"));
		    container_click.click();
		    boolean ErrorField_3 = driver.findElement(By.id("editTonnageErr")).isDisplayed();
		    if (ErrorField_3 == false) {
		        test.log(Status.PASS, "MixedValue validation for inputTonnage input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for inputTonnage input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With SQL Query
		String[] SqlValues_3 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"};
		for (String value : SqlValues_3) {
			inputTonnage.clear(); // Clear the input field
			inputTonnage.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_4 = driver.findElement(By.id("editTonnageErr")).isDisplayed();
		    if (ErrorField_4 == true) {
		        test.log(Status.PASS, "SQL Injection validation for inputTonnage input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for inputTonnage input with value: " + value + " failed");
		    }
		}
 
		
		
		Thread.sleep(100);
		
		
		
		
		// Machine Input - Brand
		test = extent.createTest("Insert Data in Machines Brand");
		WebElement inputMachineBrand = driver.findElement(By.id("editMachineBrand"));

		// Test with negative values
		String[] negativeValues_4 = {""}; // Add more negative values as needed
		for (String value : negativeValues_4) {
			inputMachineBrand.clear(); // Clear the input field
			inputMachineBrand.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_4 = driver.findElement(By.id("editMachineBrandErr")).isDisplayed();
		    if (ErrorField_4 == true) {
		        test.log(Status.PASS, "Empty value validation for MachineBrand input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MachineBrand input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] negativeValues_5 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; // Add more negative values as needed
		for (String value : negativeValues_5) {
			inputMachineBrand.clear(); // Clear the input field
			inputMachineBrand.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean ErrorField_4 = driver.findElement(By.id("editMachineBrandErr")).isDisplayed();
		    if (ErrorField_4 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MachineBrand input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MachineBrand input with value: " + value + " failed");
		    }
		}
		
		
		Thread.sleep(100);
		// Machine Input - SerialID
		test = extent.createTest("Insert Data in Machines SerialID");
		WebElement inputMachineSerialId = driver.findElement(By.id("editMachineSerialNumber"));

		// Test with Empty values
		String[] inputMachineSerialIdValues_4 = {""}; // Add more negative values as needed
		for (String value : inputMachineSerialIdValues_4) {
			inputMachineSerialId.clear(); // Clear the input field
			inputMachineSerialId.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean inputMachineSerialIdErrorField_4 = driver.findElement(By.id("editMachineSerialNumber_err")).isDisplayed();
		    if (inputMachineSerialIdErrorField_4 == true) {
		        test.log(Status.PASS, "Empty value validation for MachineSerialId input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MachineSerialId input with value: " + value + " failed");
		    }
		} 
		Thread.sleep(100);
		String[] inputMachineSerialIdValues_5 = {"\" or \"\"=\"", "105; DROP TABLE","' OR '5'='5'"}; // Add more negative values as needed
		for (String value : inputMachineSerialIdValues_5) {
			inputMachineSerialId.clear(); // Clear the input field
			inputMachineSerialId.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditMachineModal1"));
		    container_click_1.click();
		    boolean inputMachineSerialIdValuesErrorField_4 = driver.findElement(By.id("editMachineSerialNumber_err")).isDisplayed();
		    if (inputMachineSerialIdValuesErrorField_4 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MachineSerialId input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MachineSerialId input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(1000);
		WebElement CancelButton1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[5]/div/div/div[3]/a[2]"));
		CancelButton1.click();
	} 
	
// ********************** Part Module Add Part Input Validation ********************** //
	@Test(priority = 3, dependsOnMethods = {"CorrectUserPassTesting","MachineModuleTesting_Validation","MachineModuleTesting_Edit_Validation"})
	public void PartModuleTesting_Validation() throws InterruptedException {
		
		test = extent.createTest("PartModuleTesting_Validation");
		String beforeUrlClick = driver.getCurrentUrl();
		WebElement hoverElement = driver.findElement(By.className("fa-gear"));
		Actions actions = new Actions(driver);
		actions.moveToElement(hoverElement).perform();
		Thread.sleep(1000);
		WebElement buttonToClick = driver.findElement(By.xpath("/html/body/div[2]/div[1]/ul/li[4]/ul/li[2]/div[2]/a"));
		actions.moveToElement(buttonToClick).perform();
		buttonToClick.click();
		
		String AfterUrlClick = driver.getCurrentUrl();
		if(!beforeUrlClick.equals(AfterUrlClick) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		
		Thread.sleep(1000);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
		Thread.sleep(1000);
		WebElement AddPart = driver.findElement(By.id("add_part_modal"));
		AddPart.click();
		
		

		//ADD-Part-Input-Validation:
		test = extent.createTest("Add Part Input Validate");
		WebElement inputPartName = driver.findElement(By.id("inputPartName"));
		String[] inputPartNamevalue = {""}; 
		for (String value : inputPartNamevalue) {
			inputPartName.clear();
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean errorModal1_1 = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
		    if (errorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for PartName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartName_1 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartName_1) {
			inputPartName.clear();
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean errorModal1_2 = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
		    if (errorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for PartName input with value: " + value + " failed");
		    }
		}String[] inputPartName_2 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputPartName_2) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean errorModal1_2 = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
		    if (errorModal1_2 == false) {
		        test.log(Status.PASS, "Negative value validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for PartName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartName_3 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputPartName_3) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean errorModal1_2 = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
		    if (errorModal1_2 == false) {
		        test.log(Status.PASS, "Float value validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for PartName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartName_4 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputPartName_4) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean errorModal1_2 = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
		    if (errorModal1_2 == false) {
		        test.log(Status.PASS, "MixedValue validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for PartName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);   
		
		
		// Add PArt Edit NICT Input Validate
		test = extent.createTest("Add Part NICT-Input Validate");
		
		WebElement inputNICT = driver.findElement(By.id("inputNICT"));
		String[] inputNICT_1 = {""}; 
		for (String value : inputNICT_1) {
			inputNICT.clear();
			inputNICT.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_1 = driver.findElement(By.id("inputNICTErr")).isDisplayed();
		    if (NICTerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for NICT input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputNICT_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNICT_2) {
			inputNICT.clear();
			inputNICT.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_2 = driver.findElement(By.id("inputNICTErr")).isDisplayed();
		    if (NICTerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for NICT input with value: " + value + " failed");
		    }
		}String[] inputNICT_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputNICT_3) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_3 = driver.findElement(By.id("inputNICTErr")).isDisplayed();
		    if (NICTerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for NICT input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNICT_4 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputNICT_4) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean NICTerrorModal1_4 = driver.findElement(By.id("inputNICTErr")).isDisplayed();
		    if (NICTerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for NICT input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNICT_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputNICT_5) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean NICTerrorModal1_5 = driver.findElement(By.id("inputNICTErr")).isDisplayed();
		    if (NICTerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for NICT input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		
		
		
		// Add PArt Edit NICT Input Validate
		test = extent.createTest("Add Part inputPartPrice Validate");
		
		WebElement inputPartPrice = driver.findElement(By.id("inputPartPrice"));
		String[] inputPartPrice_1 = {""}; 
		for (String value : inputPartPrice_1) {
			inputPartPrice.clear();
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_1 = driver.findElement(By.id("inputPartPriceErr")).isDisplayed();
		    if (NICTerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for PartPrice input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartPrice_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartPrice_2) {
			inputPartPrice.clear();
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean PriceerrorModal1_2 = driver.findElement(By.id("inputPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for PartPrice input with value: " + value + " failed");
		    }
		}String[] inputPartPrice_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputPartPrice_3) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean PriceerrorModal1_3 = driver.findElement(By.id("inputPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for PartPrice input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartPrice_4 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputPartPrice_4) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean PriceerrorModal1_4 = driver.findElement(By.id("inputPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for PartPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartPrice_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputPartPrice_5) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean PriceerrorModal1_5 = driver.findElement(By.id("inputPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for PartPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		// Add PArt Edit inputNoOfPartsPerCycle Validate
		test = extent.createTest("Add Part inputNoOfPartsPerCycle Validate");
		
		WebElement inputNoOfPartsPerCycle = driver.findElement(By.id("inputNoOfPartsPerCycle"));
		String[] inputNoOfPartsPerCycle_1 = {""}; 
		for (String value : inputNoOfPartsPerCycle_1) {
			inputNoOfPartsPerCycle.clear();
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_1 = driver.findElement(By.id("inputNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputNoOfPartsPerCycle_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNoOfPartsPerCycle_2) {
			inputNoOfPartsPerCycle.clear();
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_2 = driver.findElement(By.id("inputNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}String[] inputNoOfPartsPerCycle_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputNoOfPartsPerCycle_3) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_3 = driver.findElement(By.id("inputNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNoOfPartsPerCycle_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputNoOfPartsPerCycle_4) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_4 = driver.findElement(By.id("inputNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNoOfPartsPerCycle_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputNoOfPartsPerCycle_5) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_5 = driver.findElement(By.id("inputNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100); 
		
		
		// Add PArt Edit inputNoOfPartsPerCycle Validate
		test = extent.createTest("Add Part inputPartWeight Validate");
		
		WebElement inputPartWeight = driver.findElement(By.id("inputPartWeight"));
		String[] inputPartWeight_1 = {""}; 
		for (String value : inputPartWeight_1) {
			inputPartWeight.clear();
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_1 = driver.findElement(By.id("inputPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for PartWeight input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartWeight_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartWeight_2) {
			inputPartWeight.clear();
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_2 = driver.findElement(By.id("inputPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for PartWeight input with value: " + value + " failed");
		    }
		}String[] inputPartWeight_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputPartWeight_3) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_3 = driver.findElement(By.id("inputPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for PartWeight input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartWeight_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputPartWeight_4) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputPartWeighterrorModal1_4 = driver.findElement(By.id("inputPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for PartWeight input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartWeight_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputPartWeight_5) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputPartWeighterrorModal1_5 = driver.findElement(By.id("inputPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for PartWeight input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		
		// Add PArt Edit MaterialPrice Validate
		test = extent.createTest("Add Part MaterialPrice Validate");
		
		WebElement inputMaterialPrice = driver.findElement(By.id("inputMaterialPrice"));
		String[] inputMaterialPrice_1 = {""}; 
		for (String value : inputMaterialPrice_1) {
			inputMaterialPrice.clear();
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_1 = driver.findElement(By.id("inputMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MaterialPrice input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputMaterialPrice_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputMaterialPrice_2) {
			inputMaterialPrice.clear();
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_2 = driver.findElement(By.id("inputMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MaterialPrice input with value: " + value + " failed");
		    }
		}String[] inputMaterialPrice_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputMaterialPrice_3) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_3 = driver.findElement(By.id("inputMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for MaterialPrice input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputMaterialPrice_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputMaterialPrice_4) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputMaterialPriceerrorModal1_4 = driver.findElement(By.id("inputMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for MaterialPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputMaterialPrice_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputMaterialPrice_5) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputMaterialPriceerrorModal1_5 = driver.findElement(By.id("inputMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for MaterialPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);

		
		// Add PArt Edit MaterialName Validate
		test = extent.createTest("Add Part MaterialName Validate");
		
		WebElement inputMaterialName = driver.findElement(By.id("inputMaterialName"));
		String[] inputMaterialName_1 = {""}; 
		for (String value : inputMaterialName_1) {
			inputMaterialName.clear();
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_1 = driver.findElement(By.id("inputMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MaterialName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputMaterialName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputMaterialName_2) {
			inputMaterialName.clear();
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_2 = driver.findElement(By.id("inputMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MaterialName input with value: " + value + " failed");
		    }
		}String[] inputMaterialName_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputMaterialName_3) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_3 = driver.findElement(By.id("inputMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for MaterialName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputMaterialName_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputMaterialName_4) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputMaterialNameerrorModal1_4 = driver.findElement(By.id("inputMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputMaterialName_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputMaterialName_5) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputMaterialNameerrorModal1_5 = driver.findElement(By.id("inputMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for MaterialName input with value: " + value + " failed");
		    }
		}
		
		WebElement SelectTool = driver.findElement(By.id("inputToolName"));
		SelectTool.click();
		WebElement Selectoption = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[1]/div/div/div[2]/div[1]/div[2]/div/select/option[2]"));
		Selectoption.click();
		
		
		// Add PArt NewToolName Validate
		test = extent.createTest("Add Part MaterialName Validate");
		
		WebElement inputNewToolName = driver.findElement(By.id("inputNewToolName"));
		String[] inputNewToolName_1 = {""}; 
		for (String value : inputNewToolName_1) {
			inputNewToolName.clear();
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputNewToolNameerrorModal1_1 = driver.findElement(By.id("inputNewToolNameErr")).isDisplayed();
		    if (inputNewToolNameerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MaterialName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputNewToolName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNewToolName_2) {
			inputNewToolName.clear();
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputNewToolNameerrorModal1_2 = driver.findElement(By.id("inputNewToolNameErr")).isDisplayed();
		    if (inputNewToolNameerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MaterialName input with value: " + value + " failed");
		    }
		}String[] inputNewToolName_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputNewToolName_3) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("AddPartModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_3 = driver.findElement(By.id("inputNewToolNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for MaterialName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNewToolName_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputNewToolName_4) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputNewToolNameerrorModal1_4 = driver.findElement(By.id("inputNewToolNameErr")).isDisplayed();
		    if (inputNewToolNameerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNewToolName_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputNewToolName_5) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("AddPartModal1"));
		    container_click.click();
		    boolean inputNewToolNameerrorModal1_5 = driver.findElement(By.id("inputNewToolNameErr")).isDisplayed();
		    if (inputNewToolNameerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(500);
		WebElement container_click = driver.findElement(By.className("cancelBtnStyle"));
		container_click.click();
		
	}
	
	
// ********************** Part Module Edit Part Input Validation ********************** //
		@Test(priority = 4, dependsOnMethods = {"CorrectUserPassTesting","MachineModuleTesting_Validation","MachineModuleTesting_Edit_Validation","PartModuleTesting_Validation"})
	public void Edit_Part_Input_Validation1() throws InterruptedException {
		// Edit Part Input Validation
		
		test = extent.createTest("Edit_Part_Input_Validation");
		WebElement option_Button2 = driver.findElement(By.className("edit-menu"));
		option_Button2.click();
		
		WebElement Edit_Machine = driver.findElement(By.className("edit-menu"));
		Edit_Machine.click();
		WebElement Edit_option=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_option.click();
		
		
		
		
		
//		//ADD-Part-Input-Validation:
		test = extent.createTest("Edit Add Part Input Validate");
		WebElement inputPartName = driver.findElement(By.id("EditPartName"));
		String[] inputPartNamevalue = {""}; 
		for (String value : inputPartNamevalue) {
			inputPartName.clear();
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean errorModal1_1 = driver.findElement(By.className("EditPartNameErr")).isDisplayed();
		    if (errorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for Edit PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for Edit PartName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartName_1 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartName_1) {
			inputPartName.clear();
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean errorModal1_2 = driver.findElement(By.className("EditPartNameErr")).isDisplayed();
		    if (errorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for Edit PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for  EditPartName input with value: " + value + " failed");
		    }
		}
		
		String[] inputPartName_2 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputPartName_2) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean errorModal1_3 = driver.findElement(By.className("EditPartNameErr")).isDisplayed();
		    if (errorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for PartName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartName_3 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputPartName_3) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean errorModal1_4 = driver.findElement(By.className("EditPartNameErr")).isDisplayed();
		    if (errorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for PartName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartName_4 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputPartName_4) {
			inputPartName.clear(); // Clear the input field
			inputPartName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean errorModal1_5 = driver.findElement(By.className("EditPartNameErr")).isDisplayed();
		    if (errorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for PartName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for PartName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);   
		
		
		// Add PArt Edit NICT Input Validate
		test = extent.createTest("Edit Add Part NICT-Input Validate");
		
		WebElement inputNICT = driver.findElement(By.id("EditNICT"));
		String[] inputNICT_1 = {""}; 
		for (String value : inputNICT_1) {
			inputNICT.clear();
			inputNICT.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean NICTerrorModal1_1 = driver.findElement(By.className("EditNICTErr")).isDisplayed();
		    if (NICTerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for NICT input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100);
		
		String[] inputNICT_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNICT_2) {
			inputNICT.clear();
			inputNICT.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean EditNICTerrorModal1_2 = driver.findElement(By.className("EditNICTErr")).isDisplayed();
		    if (EditNICTerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for NICT input with value: " + value + " failed");
		    }
		}
		String[] inputNICT_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputNICT_3) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
			WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_3 = driver.findElement(By.className("EditNICTErr")).isDisplayed();
		    if (NICTerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for Edit NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for Edit NICT input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNICT_4 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputNICT_4) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
			WebElement container_click_3 = driver.findElement(By.id("EditToolModal1"));
		    container_click_3.click();
		    boolean NICTerrorModal1_4 = driver.findElement(By.className("EditNICTErr")).isDisplayed();
		    if (NICTerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for NICT input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNICT_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputNICT_5) {
			inputNICT.clear(); // Clear the input field
			inputNICT.sendKeys(value);
			WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean NICTerrorModal1_5 = driver.findElement(By.className("EditNICTErr")).isDisplayed();
		    if (NICTerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for Edit NICT input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for Edit NICT input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		
		
		
		// Add PArt Edit NICT Input Validate
		test = extent.createTest("Edit Part inputPartPrice Validate");
		
		WebElement inputPartPrice = driver.findElement(By.id("EditPartPrice"));
		String[] inputPartPrice_1 = {""}; 
		for (String value : inputPartPrice_1) {
			inputPartPrice.clear();
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean NICTerrorModal1_1 = driver.findElement(By.className("EditPartPriceErr")).isDisplayed();
		    if (NICTerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for Edit PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for Edit PartPrice input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartPrice_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartPrice_2) {
			inputPartPrice.clear();
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean PriceerrorModal1_2 = driver.findElement(By.className("EditPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for Edit PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for Edit PartPrice input with value: " + value + " failed");
		    }
		}String[] inputPartPrice_3 = {"-23", "-0.5", "-1000"}; // Add more negative values as needed
		for (String value : inputPartPrice_3) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean PriceerrorModal1_3 = driver.findElement(By.className("EditPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for Edit PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for Edit PartPrice input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartPrice_4 = {"12.5", "0.01", "3.14"}; // Add more float values as needed
		for (String value : inputPartPrice_4) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean PriceerrorModal1_4 = driver.findElement(By.className("EditPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for Edit PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for Edit PartPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartPrice_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; // Add more float values as needed
		for (String value : inputPartPrice_5) {
			inputPartPrice.clear(); // Clear the input field
			inputPartPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean PriceerrorModal1_5 = driver.findElement(By.className("EditPartPriceErr")).isDisplayed();
		    if (PriceerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for Edit PartPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for Edit PartPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		// Add PArt Edit inputNoOfPartsPerCycle Validate
		test = extent.createTest("Add Part inputNoOfPartsPerCycle Validate");
		
		WebElement inputNoOfPartsPerCycle = driver.findElement(By.id("EditNoOfPartsPerCycle"));
		String[] inputNoOfPartsPerCycle_1 = {""}; 
		for (String value : inputNoOfPartsPerCycle_1) {
			inputNoOfPartsPerCycle.clear();
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_1 = driver.findElement(By.className("EditNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for Edit NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for Edit NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputNoOfPartsPerCycle_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNoOfPartsPerCycle_2) {
			inputNoOfPartsPerCycle.clear();
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_2 = driver.findElement(By.className("EditNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for Edit NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for Edit NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}String[] inputNoOfPartsPerCycle_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputNoOfPartsPerCycle_3) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_3 = driver.findElement(By.className("EditNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for Edit NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for Edit NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNoOfPartsPerCycle_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputNoOfPartsPerCycle_4) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_4 = driver.findElement(By.className("EditNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for Edit NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for Edit NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNoOfPartsPerCycle_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputNoOfPartsPerCycle_5) {
			inputNoOfPartsPerCycle.clear(); 
			inputNoOfPartsPerCycle.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputNoOfPartsPerCycleerrorModal1_5 = driver.findElement(By.className("EditNoOfPartsPerCycleErr")).isDisplayed();
		    if (inputNoOfPartsPerCycleerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for Edit NoOfPartsPerCycle input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for Edit NoOfPartsPerCycle input with value: " + value + " failed");
		    }
		}
		
		Thread.sleep(100); 
		
		
		// Add PArt Edit inputNoOfPartsPerCycle Validate
		test = extent.createTest("Add Part inputPartWeight Validate");
		
		WebElement inputPartWeight = driver.findElement(By.id("EditPartWeight"));
		String[] inputPartWeight_1 = {""}; 
		for (String value : inputPartWeight_1) {
			inputPartWeight.clear();
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_1 = driver.findElement(By.className("EditPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for PartWeight input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputPartWeight_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputPartWeight_2) {
			inputPartWeight.clear();
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_2 = driver.findElement(By.className("EditPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for PartWeight input with value: " + value + " failed");
		    }
		}String[] inputPartWeight_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputPartWeight_3) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputPartWeighterrorModal1_3 = driver.findElement(By.className("EditPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for PartWeight input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputPartWeight_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputPartWeight_4) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputPartWeighterrorModal1_4 = driver.findElement(By.className("EditPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for PartWeight input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputPartWeight_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputPartWeight_5) {
			inputPartWeight.clear(); 
			inputPartWeight.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputPartWeighterrorModal1_5 = driver.findElement(By.className("EditPartWeightErr")).isDisplayed();
		    if (inputPartWeighterrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for PartWeight input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for PartWeight input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		
		// Add PArt Edit MaterialPrice Validate
		test = extent.createTest("Edit Part MaterialPrice Validate");
		
		WebElement inputMaterialPrice = driver.findElement(By.id("EditMaterialPrice"));
		String[] inputMaterialPrice_1 = {""}; 
		for (String value : inputMaterialPrice_1) {
			inputMaterialPrice.clear();
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_1 = driver.findElement(By.className("EditMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MaterialPrice input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputMaterialPrice_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputMaterialPrice_2) {
			inputMaterialPrice.clear();
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_2 = driver.findElement(By.className("EditMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MaterialPrice input with value: " + value + " failed");
		    }
		}String[] inputMaterialPrice_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputMaterialPrice_3) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialPriceerrorModal1_3 = driver.findElement(By.className("EditMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for MaterialPrice input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputMaterialPrice_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputMaterialPrice_4) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputMaterialPriceerrorModal1_4 = driver.findElement(By.className("EditMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for MaterialPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputMaterialPrice_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputMaterialPrice_5) {
			inputMaterialPrice.clear(); 
			inputMaterialPrice.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputMaterialPriceerrorModal1_5 = driver.findElement(By.className("EditMaterialPriceErr")).isDisplayed();
		    if (inputMaterialPriceerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for MaterialPrice input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for MaterialPrice input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);

		
		// Add PArt Edit MaterialName Validate
		test = extent.createTest("Edit Part MaterialName Validate");
		
		WebElement inputMaterialName = driver.findElement(By.id("EditMaterialName"));
		String[] inputMaterialName_1 = {""}; 
		for (String value : inputMaterialName_1) {
			inputMaterialName.clear();
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_1 = driver.findElement(By.className("EditMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for MaterialName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputMaterialName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputMaterialName_2) {
			inputMaterialName.clear();
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_2 = driver.findElement(By.className("EditMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for MaterialName input with value: " + value + " failed");
		    }
		}String[] inputMaterialName_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputMaterialName_3) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_3 = driver.findElement(By.className("EditMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for MaterialName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputMaterialName_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputMaterialName_4) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputMaterialNameerrorModal1_4 = driver.findElement(By.className("EditMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputMaterialName_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputMaterialName_5) {
			inputMaterialName.clear(); 
			inputMaterialName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputMaterialNameerrorModal1_5 = driver.findElement(By.className("EditMaterialNameErr")).isDisplayed();
		    if (inputMaterialNameerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for MaterialName input with value: " + value + " failed");
		    }
		}

		// Add PArt NewToolName Validate
		test = extent.createTest("Edit Part MaterialName Validate");
		
		WebElement inputNewToolName = driver.findElement(By.id("inputEditToolName"));
		String[] inputNewToolName_1 = {""}; 
		for (String value : inputNewToolName_1) {
			inputNewToolName.clear();
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputNewToolNameerrorModal1_1 = driver.findElement(By.className("edit_tool_name_err")).isDisplayed();
		    if (inputNewToolNameerrorModal1_1 == true) {
		        test.log(Status.PASS, "Empty value validation for Edit MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Empty value validation for Edit MaterialName input with value: " + value + " failed");
		    }
		} 
		
		Thread.sleep(100);
		String[] inputNewToolName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
		for (String value : inputNewToolName_2) {
			inputNewToolName.clear();
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputNewToolNameerrorModal1_2 = driver.findElement(By.className("edit_tool_name_err")).isDisplayed();
		    if (inputNewToolNameerrorModal1_2 == true) {
		        test.log(Status.PASS, "SQL Injection validation for Edit MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "SQL Injection validation for Edit MaterialName input with value: " + value + " failed");
		    }
		}String[] inputNewToolName_3 = {"-23", "-0.5", "-1000"}; 
		for (String value : inputNewToolName_3) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click_1 = driver.findElement(By.id("EditToolModal1"));
		    container_click_1.click();
		    boolean inputMaterialNameerrorModal1_3 = driver.findElement(By.className("edit_tool_name_err")).isDisplayed();
		    if (inputMaterialNameerrorModal1_3 == false) {
		        test.log(Status.PASS, "Negative value validation for Edit MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Negative value validation for Edit MaterialName input with value: " + value + " failed");
		    }
		}

		// Test with float values
		String[] inputNewToolName_4 = {"12.5", "0.01", "3.14"}; 
		for (String value : inputNewToolName_4) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputNewToolNameerrorModal1_4 = driver.findElement(By.className("edit_tool_name_err")).isDisplayed();
		    if (inputNewToolNameerrorModal1_4 == false) {
		        test.log(Status.PASS, "Float value validation for Edit MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "Float value validation for Edit MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(100);
		
		//Test With Mixed Value
		String[] inputNewToolName_5 = {"12.5ABQQ", "-12.5ABQQ", "ANMA23"}; 
		for (String value : inputNewToolName_5) {
			inputNewToolName.clear(); 
			inputNewToolName.sendKeys(value);
		    WebElement container_click = driver.findElement(By.id("EditToolModal1"));
		    container_click.click();
		    boolean inputNewToolNameerrorModal1_5 = driver.findElement(By.className("edit_tool_name_err")).isDisplayed();
		    if (inputNewToolNameerrorModal1_5 == false) {
		        test.log(Status.PASS, "MixedValue validation for Edit MaterialName input with value: " + value + " is successful");
		    } else {
		        test.log(Status.FAIL, "MixedValue validation for Edit MaterialName input with value: " + value + " failed");
		    }
		}
		Thread.sleep(500);
		
		
		
		WebElement Edit_option_MC_Name_Cancel = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[4]/div/div/div[3]/a[2]"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Cancel.click();
		
		}
		
	
// ********************** General Module Input Validation ********************** //
	@Test(priority = 4, dependsOnMethods = {"CorrectUserPassTesting","MachineModuleTesting_Validation","MachineModuleTesting_Edit_Validation","PartModuleTesting_Validation","Edit_Part_Input_Validation1"})
	public void GeneralInputValidate() throws InterruptedException {
			String beforeUrlClick = driver.getCurrentUrl();
			WebElement hoverElement = driver.findElement(By.className("fa-gear"));
			Actions actions = new Actions(driver);
			actions.moveToElement(hoverElement).perform();
			Thread.sleep(1000);
			WebElement buttonToClick = driver.findElement(By.xpath("/html/body/div[2]/div[1]/ul/li[4]/ul/li[3]/div[2]/a"));
			actions.moveToElement(buttonToClick).perform();
			buttonToClick.click();
			String AfterUrlClick = driver.getCurrentUrl();
			if(!beforeUrlClick.equals(AfterUrlClick) ) {
				test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
			} else {
				test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
			}
			
			Thread.sleep(1000);
			
			test = extent.createTest("EditGoals");
			Thread.sleep(1000);	
			WebElement GoalsEdit = driver.findElement(By.className("img_font_wh"));
			GoalsEdit.click();
			Thread.sleep(100);
			
			test = extent.createTest("General - EOTEEP Input Validate");
			
			WebElement EOTEEP = driver.findElement(By.id("EOTEEP"));
			String[] EOTEEP_1 = {""}; 
			for (String value : EOTEEP_1) {
				EOTEEP.clear();
				EOTEEP.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOTEEPerrorModal1_1 = driver.findElement(By.id("EOTEEPErr")).isDisplayed();
			    if (EOTEEPerrorModal1_1 == true) {
			        test.log(Status.PASS, "Empty value validation for General -EOTEEP input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for General -EOTEEP input with value: " + value + " failed");
			    }
			} 
			
			Thread.sleep(100);
			String[] EOTEEP_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
			for (String value : EOTEEP_2) {
				EOTEEP.clear();
				EOTEEP.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOTEEPerrorModal1_2 = driver.findElement(By.id("EOTEEPErr")).isDisplayed();
			    if (EOTEEPerrorModal1_2 == true) {
			        test.log(Status.PASS, "SQL Injection validation for General -EOTEEP input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for General -EOTEEP input with value: " + value + " failed");
			    }
			}String[] EOTEEP_3 = {"-23", "-0.5", "-1000"}; 
			for (String value : EOTEEP_3) {
				EOTEEP.clear(); 
				EOTEEP.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOTEEPerrorModal1_3 = driver.findElement(By.id("EOTEEPErr")).isDisplayed();
			    if (EOTEEPerrorModal1_3 == false) {
			        test.log(Status.PASS, "Negative value validation for General -EOTEEP input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Negative value validation for General -EOTEEP input with value: " + value + " failed");
			    }
			}

			// Test with float values
			String[] EOTEEP_4 = {"1.5", "0.01", "3.14"}; 
			for (String value : EOTEEP_4) {
				EOTEEP.clear(); 
				EOTEEP.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
			    container_click.click();
			    boolean EOTEEPerrorModal1_4 = driver.findElement(By.id("EOTEEPErr")).isDisplayed();
			    if (EOTEEPerrorModal1_4 == false) {
			        test.log(Status.PASS, "Float value validation for General -EOTEEP input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Float value validation for General -EOTEEP input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			
			//Test With Mixed Value
			String[] EOTEEP_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA12"}; 
			for (String value : EOTEEP_5) {
				EOTEEP.clear(); 
				EOTEEP.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
			    container_click.click();
			    boolean EOTEEPErrerrorModal1_5 = driver.findElement(By.id("EOTEEPErr")).isDisplayed();
			    if (EOTEEPErrerrorModal1_5 == false) {
			        test.log(Status.PASS, "MixedValue validation for General -EOTEEP input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "MixedValue validation for General -EOTEEP input with value: " + value + " failed");
			    }
			}
			Thread.sleep(500);
			
			
			
            test = extent.createTest("General - EOOOE Input Validate");
			
			WebElement EOOOE = driver.findElement(By.id("EOOOE"));
			String[] EOOOE_1 = {""}; 
			for (String value : EOOOE_1) {
				EOOOE.clear();
				EOOOE.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOOOEerrorModal1_1 = driver.findElement(By.id("EOOOEErr")).isDisplayed();
			    if (EOOOEerrorModal1_1 == true) {
			        test.log(Status.PASS, "Empty value validation for General -EOOOE input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Empty value validation for General -EOOOE input with value: " + value + " failed");
			    }
			} 
			
			Thread.sleep(100);
			String[] EOOOE_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
			for (String value : EOOOE_2) {
				EOOOE.clear();
				EOOOE.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOOOEerrorModal1_2 = driver.findElement(By.id("EOOOEErr")).isDisplayed();
			    if (EOOOEerrorModal1_2 == true) {
			        test.log(Status.PASS, "SQL Injection validation for General -EOOOE input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "SQL Injection validation for General -EOOOE input with value: " + value + " failed");
			    }
			}String[] EOOOE_3 = {"-23", "-0.5", "-1000"}; 
			for (String value : EOOOE_3) {
				EOOOE.clear(); 
				EOOOE.sendKeys(value);
			    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
			    container_click_1.click();
			    boolean EOOOEerrorModal1_3 = driver.findElement(By.id("EOOOEErr")).isDisplayed();
			    if (EOOOEerrorModal1_3 == false) {
			        test.log(Status.PASS, "Negative value validation for General -EOOOE input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Negative value validation for General -EOOOE input with value: " + value + " failed");
			    }
			}

			// Test with float values
			String[] EOOOE_4 = {"1.5", "0.01", "3.14"}; 
			for (String value : EOOOE_4) {
				EOOOE.clear(); 
				EOOOE.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
			    container_click.click();
			    boolean EOOOEerrorModal1_4 = driver.findElement(By.id("EOOOEErr")).isDisplayed();
			    if (EOOOEerrorModal1_4 == false) {
			        test.log(Status.PASS, "Float value validation for General -EOOOE input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "Float value validation for General -EOOOE input with value: " + value + " failed");
			    }
			}
			Thread.sleep(100);
			
			//Test With Mixed Value
			String[] EOOOE_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
			for (String value : EOOOE_5) {
				EOOOE.clear(); 
				EOOOE.sendKeys(value);
			    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
			    container_click.click();
			    boolean EOOOEErrerrorModal1_5 = driver.findElement(By.id("EOOOEErr")).isDisplayed();
			    if (EOOOEErrerrorModal1_5 == false) {
			        test.log(Status.PASS, "MixedValue validation for General -EOOOE input with value: " + value + " is successful");
			    } else {
			        test.log(Status.FAIL, "MixedValue validation for General -EOOOE input with value: " + value + " failed");
			    }
			}
			Thread.sleep(500);
			
			
			
			
			 test = extent.createTest("General - EOOEE Input Validate");
				
				WebElement EOOEE = driver.findElement(By.id("EOOEE"));
				String[] EOOEE_1 = {""}; 
				for (String value : EOOEE_1) {
					EOOEE.clear();
					EOOEE.sendKeys(value);
				    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
				    container_click_1.click();
				    boolean EOOEEerrorModal1_1 = driver.findElement(By.id("EOOEEErr")).isDisplayed();
				    if (EOOEEerrorModal1_1 == true) {
				        test.log(Status.PASS, "Empty value validation for General -EOOEE input with value: " + value + " is successful");
				    } else {
				        test.log(Status.FAIL, "Empty value validation for General -EOOEE input with value: " + value + " failed");
				    }
				} 
				
				Thread.sleep(100);
				String[] EOOEE_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
				for (String value : EOOEE_2) {
					EOOEE.clear();
					EOOEE.sendKeys(value);
				    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
				    container_click_1.click();
				    boolean EOOEEerrorModal1_2 = driver.findElement(By.id("EOOEEErr")).isDisplayed();
				    if (EOOEEerrorModal1_2 == true) {
				        test.log(Status.PASS, "SQL Injection validation for General -EOOEE input with value: " + value + " is successful");
				    } else {
				        test.log(Status.FAIL, "SQL Injection validation for General -EOOEE input with value: " + value + " failed");
				    }
				}String[] EOOEE_3 = {"-23", "-0.5", "-1000"}; 
				for (String value : EOOEE_3) {
					EOOEE.clear(); 
					EOOEE.sendKeys(value);
				    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
				    container_click_1.click();
				    boolean EOOEEerrorModal1_3 = driver.findElement(By.id("EOOEEErr")).isDisplayed();
				    if (EOOEEerrorModal1_3 == false) {
				        test.log(Status.PASS, "Negative value validation for General -EOOEE input with value: " + value + " is successful");
				    } else {
				        test.log(Status.FAIL, "Negative value validation for General -EOOEE input with value: " + value + " failed");
				    }
				}

				// Test with float values
				String[] EOOEE_4 = {"1.5", "0.01", "3.14"}; 
				for (String value : EOOEE_4) {
					EOOEE.clear(); 
					EOOEE.sendKeys(value);
				    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
				    container_click.click();
				    boolean EOOEEerrorModal1_4 = driver.findElement(By.id("EOOEEErr")).isDisplayed();
				    if (EOOEEerrorModal1_4 == false) {
				        test.log(Status.PASS, "Float value validation for General -EOOEE input with value: " + value + " is successful");
				    } else {
				        test.log(Status.FAIL, "Float value validation for General -EOOEE input with value: " + value + " failed");
				    }
				}
				Thread.sleep(100);
				
				//Test With Mixed Value
				String[] EOOEE_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA89"}; 
				for (String value : EOOEE_5) {
					EOOEE.clear(); 
					EOOEE.sendKeys(value);
				    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
				    container_click.click();
				    boolean EOOEEErrerrorModal1_5 = driver.findElement(By.id("EOOEEErr")).isDisplayed();
				    if (EOOEEErrerrorModal1_5 == false) {
				        test.log(Status.PASS, "MixedValue validation for General -EOOEE input with value: " + value + " is successful");
				    } else {
				        test.log(Status.FAIL, "MixedValue validation for General -EOOEE input with value: " + value + " failed");
				    }
				}
				Thread.sleep(500);
			
				
				
				 test = extent.createTest("General - EAvailability Input Validate");
					
					WebElement EAvailability = driver.findElement(By.id("EAvailability"));
					String[] EAvailability_1 = {""}; 
					for (String value : EAvailability_1) {
						EAvailability.clear();
						EAvailability.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EAvailabilityerrorModal1_1 = driver.findElement(By.id("EAvailabilityErr")).isDisplayed();
					    if (EAvailabilityerrorModal1_1 == true) {
					        test.log(Status.PASS, "Empty value validation for General -EAvailability input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Empty value validation for General -EAvailability input with value: " + value + " failed");
					    }
					} 
					
					Thread.sleep(100);
					String[] EAvailability_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
					for (String value : EAvailability_2) {
						EAvailability.clear();
						EAvailability.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EAvailabilityerrorModal1_2 = driver.findElement(By.id("EAvailabilityErr")).isDisplayed();
					    if (EAvailabilityerrorModal1_2 == true) {
					        test.log(Status.PASS, "SQL Injection validation for General -EAvailability input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "SQL Injection validation for General -EAvailability input with value: " + value + " failed");
					    }
					}String[] EAvailability_3 = {"-23", "-0.5", "-1000"}; 
					for (String value : EAvailability_3) {
						EAvailability.clear(); 
						EAvailability.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EAvailabilityerrorModal1_3 = driver.findElement(By.id("EAvailabilityErr")).isDisplayed();
					    if (EAvailabilityerrorModal1_3 == false) {
					        test.log(Status.PASS, "Negative value validation for General -EAvailability input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Negative value validation for General -EAvailability input with value: " + value + " failed");
					    }
					}

					// Test with float values
					String[] EAvailability_4 = {"1.5", "0.01", "3.14"}; 
					for (String value : EAvailability_4) {
						EAvailability.clear(); 
						EAvailability.sendKeys(value);
					    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
					    container_click.click();
					    boolean EAvailabilityerrorModal1_4 = driver.findElement(By.id("EAvailabilityErr")).isDisplayed();
					    if (EAvailabilityerrorModal1_4 == false) {
					        test.log(Status.PASS, "Float value validation for General -EAvailability input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Float value validation for General -EAvailability input with value: " + value + " failed");
					    }
					}
					Thread.sleep(100);
					
					//Test With Mixed Value
					String[] EAvailability_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
					for (String value : EAvailability_5) {
						EAvailability.clear(); 
						EAvailability.sendKeys(value);
					    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
					    container_click.click();
					    boolean EAvailabilityErrerrorModal1_5 = driver.findElement(By.id("EAvailabilityErr")).isDisplayed();
					    if (EAvailabilityErrerrorModal1_5 == false) {
					        test.log(Status.PASS, "MixedValue validation for General -EAvailability input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "MixedValue validation for General -EAvailability input with value: " + value + " failed");
					    }
					}
					Thread.sleep(500);
					
					test = extent.createTest("General - EPerformance Input Validate");
					
					WebElement EPerformance = driver.findElement(By.id("EPerformance"));
					String[] EPerformance_1 = {""}; 
					for (String value : EPerformance_1) {
						EPerformance.clear();
						EPerformance.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EPerformanceerrorModal1_1 = driver.findElement(By.id("EPerformanceErr")).isDisplayed();
					    if (EPerformanceerrorModal1_1 == true) {
					        test.log(Status.PASS, "Empty value validation for General -EPerformance input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Empty value validation for General -EPerformance input with value: " + value + " failed");
					    }
					} 
					
					Thread.sleep(100);
					String[] EPerformance_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
					for (String value : EPerformance_2) {
						EPerformance.clear();
						EPerformance.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EPerformanceerrorModal1_2 = driver.findElement(By.id("EPerformanceErr")).isDisplayed();
					    if (EPerformanceerrorModal1_2 == true) {
					        test.log(Status.PASS, "SQL Injection validation for General -EPerformance input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "SQL Injection validation for General -EPerformance input with value: " + value + " failed");
					    }
					}String[] EPerformance_3 = {"-23", "-0.5", "-1000"}; 
					for (String value : EPerformance_3) {
						EPerformance.clear(); 
						EPerformance.sendKeys(value);
					    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
					    container_click_1.click();
					    boolean EPerformanceerrorModal1_3 = driver.findElement(By.id("EPerformanceErr")).isDisplayed();
					    if (EPerformanceerrorModal1_3 == false) {
					        test.log(Status.PASS, "Negative value validation for General -EPerformance input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Negative value validation for General -EPerformance input with value: " + value + " failed");
					    }
					}

					// Test with float values
					String[] EPerformance_4 = {"1.5", "0.01", "3.14"}; 
					for (String value : EPerformance_4) {
						EPerformance.clear(); 
						EPerformance.sendKeys(value);
					    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
					    container_click.click();
					    boolean EPerformanceerrorModal1_4 = driver.findElement(By.id("EPerformanceErr")).isDisplayed();
					    if (EPerformanceerrorModal1_4 == false) {
					        test.log(Status.PASS, "Float value validation for General -EPerformance input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "Float value validation for General -EPerformance input with value: " + value + " failed");
					    }
					}
					Thread.sleep(100);
					
					//Test With Mixed Value
					String[] EPerformance_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
					for (String value : EPerformance_5) {
						EPerformance.clear(); 
						EPerformance.sendKeys(value);
					    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
					    container_click.click();
					    boolean EPerformanceErrerrorModal1_5 = driver.findElement(By.id("EPerformanceErr")).isDisplayed();
					    if (EPerformanceErrerrorModal1_5 == false) {
					        test.log(Status.PASS, "MixedValue validation for General -EPerformance input with value: " + value + " is successful");
					    } else {
					        test.log(Status.FAIL, "MixedValue validation for General -EPerformance input with value: " + value + " failed");
					    }
					}
					
					
					
					 test = extent.createTest("General - EQuality Input Validate");
						
						WebElement EQuality = driver.findElement(By.id("EQuality"));
						String[] EQuality_1 = {""}; 
						for (String value : EQuality_1) {
							EQuality.clear();
							EQuality.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
						    container_click_1.click();
						    boolean EQualityerrorModal1_1 = driver.findElement(By.id("EQualityErr")).isDisplayed();
						    if (EQualityerrorModal1_1 == true) {
						        test.log(Status.PASS, "Empty value validation for General -EQuality input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Empty value validation for General -EQuality input with value: " + value + " failed");
						    }
						} 
						
						Thread.sleep(100);
						String[] EQuality_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
						for (String value : EQuality_2) {
							EQuality.clear();
							EQuality.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
						    container_click_1.click();
						    boolean EQualityerrorModal1_2 = driver.findElement(By.id("EQualityErr")).isDisplayed();
						    if (EQualityerrorModal1_2 == true) {
						        test.log(Status.PASS, "SQL Injection validation for General -EQuality input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "SQL Injection validation for General -EQuality input with value: " + value + " failed");
						    }
						}String[] EQuality_3 = {"-23", "-0.5", "-1000"}; 
						for (String value : EQuality_3) {
							EQuality.clear(); 
							EQuality.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditFMModal1"));
						    container_click_1.click();
						    boolean EQualityerrorModal1_3 = driver.findElement(By.id("EQualityErr")).isDisplayed();
						    if (EQualityerrorModal1_3 == false) {
						        test.log(Status.PASS, "Negative value validation for General -EQuality input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Negative value validation for General -EQuality input with value: " + value + " failed");
						    }
						}

						// Test with float values
						String[] EQuality_4 = {"1.5", "0.01", "3.14"}; 
						for (String value : EQuality_4) {
							EQuality.clear(); 
							EQuality.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
						    container_click.click();
						    boolean EQualityerrorModal1_4 = driver.findElement(By.id("EQualityErr")).isDisplayed();
						    if (EQualityerrorModal1_4 == false) {
						        test.log(Status.PASS, "Float value validation for General -EQuality input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Float value validation for General -EQuality input with value: " + value + " failed");
						    }
						}
						Thread.sleep(100);
						
						//Test With Mixed Value
						String[] EQuality_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
						for (String value : EQuality_5) {
							EQuality.clear(); 
							EQuality.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditFMModal1"));
						    container_click.click();
						    boolean EQualityErrerrorModal1_5 = driver.findElement(By.id("EQualityErr")).isDisplayed();
						    if (EQualityErrerrorModal1_5 == false) {
						        test.log(Status.PASS, "MixedValue validation for General -EQuality input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "MixedValue validation for General -EQuality input with value: " + value + " failed");
						    } 
						}
						Thread.sleep(500);
						WebElement cancelButton1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[4]/div/div/div[3]/a"));
					    cancelButton1.click();
					    
					    
					    //DownTime Threshold
					    
					    test = extent.createTest("General - Input - Threshold - Validation");
						WebElement oui = driver.findElement(By.id("click_thresh_hold"));
						oui.click();
						
						WebElement Update_DThreshold = driver.findElement(By.id("Update_DThreshold"));
						String[] Update_DThreshold_1 = {""}; 
						for (String value : Update_DThreshold_1) {
							Update_DThreshold.clear();
							Update_DThreshold.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDTModal1"));
						    container_click_1.click();
						    boolean Update_DThresholderrorModal1_1 = driver.findElement(By.id("Update_DThresholdErr")).isDisplayed();
						    if (Update_DThresholderrorModal1_1 == true) {
						        test.log(Status.PASS, "Empty value validation for General -Update_DThreshold input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Empty value validation for General -Update_DThreshold input with value: " + value + " failed");
						    }
						} 
						
						Thread.sleep(100);
						String[] Update_DThreshold_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
						for (String value : Update_DThreshold_2) {
							Update_DThreshold.clear();
							Update_DThreshold.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDTModal1"));
						    container_click_1.click();
						    boolean Update_DThresholderrorModal1_2 = driver.findElement(By.id("Update_DThresholdErr")).isDisplayed();
						    if (Update_DThresholderrorModal1_2 == true) {
						        test.log(Status.PASS, "SQL Injection validation for General -Update_DThreshold input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "SQL Injection validation for General -Update_DThreshold input with value: " + value + " failed");
						    }
						}String[] Update_DThreshold_3 = {"-23", "-0.5", "-1000"}; 
						for (String value : Update_DThreshold_3) {
							Update_DThreshold.clear(); 
							Update_DThreshold.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDTModal1"));
						    container_click_1.click();
						    boolean Update_DThresholderrorModal1_3 = driver.findElement(By.id("Update_DThresholdErr")).isDisplayed();
						    if (Update_DThresholderrorModal1_3 == false) {
						        test.log(Status.PASS, "Negative value validation for General -Update_DThreshold input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Negative value validation for General -Update_DThreshold input with value: " + value + " failed");
						    }
						}

						// Test with float values
						String[] Update_DThreshold_4 = {"1.5", "0.01", "3.14"}; 
						for (String value : Update_DThreshold_4) {
							Update_DThreshold.clear(); 
							Update_DThreshold.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditDTModal1"));
						    container_click.click();
						    boolean Update_DThresholderrorModal1_4 = driver.findElement(By.id("Update_DThresholdErr")).isDisplayed();
						    if (Update_DThresholderrorModal1_4 == false) {
						        test.log(Status.PASS, "Float value validation for General -Update_DThreshold input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Float value validation for General -Update_DThreshold input with value: " + value + " failed");
						    }
						}
						Thread.sleep(100);
						
						//Test With Mixed Value
						String[] Update_DThreshold_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
						for (String value : Update_DThreshold_5) {
							Update_DThreshold.clear(); 
							Update_DThreshold.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditDTModal1"));
						    container_click.click();
						    boolean Update_DThresholdErrerrorModal1_5 = driver.findElement(By.id("Update_DThresholdErr")).isDisplayed();
						    if (Update_DThresholdErrerrorModal1_5 == false) {
						        test.log(Status.PASS, "MixedValue validation for General -Update_DThreshold input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "MixedValue validation for General -Update_DThreshold input with value: " + value + " failed");
						    }
						}
						
						WebElement cancelButton_edit = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[5]/div/div/div[3]/a"));
						cancelButton_edit.click();
						
						
						
//DownTime Reasons
					    
					    test = extent.createTest("General - Input- Downtime Reasons - Validation");
						WebElement ouit = driver.findElement(By.id("add_downtime_reason"));
						ouit.click();
						
						
						WebElement DTName = driver.findElement(By.id("DTName"));
						String[] DTName_1 = {""}; 
						for (String value : DTName_1) {
							DTName.clear();
							DTName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDRModal1"));
						    container_click_1.click();
						    boolean DTNameerrorModal1_1 = driver.findElement(By.id("DTNameErr")).isDisplayed();
						    if (DTNameerrorModal1_1 == true) {
						        test.log(Status.PASS, "Empty value validation for General -DTName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Empty value validation for General -DTName input with value: " + value + " failed");
						    }
						} 
						
						Thread.sleep(100);
						String[] DTName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
						for (String value : DTName_2) {
							DTName.clear();
							DTName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDRModal1"));
						    container_click_1.click();
						    boolean DTNameerrorModal1_2 = driver.findElement(By.id("DTNameErr")).isDisplayed();
						    if (DTNameerrorModal1_2 == true) {
						        test.log(Status.PASS, "SQL Injection validation for General -DTName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "SQL Injection validation for General -DTName input with value: " + value + " failed");
						    }
						}String[] DTName_3 = {"-23", "-0.5", "-1000"}; 
						for (String value : DTName_3) {
							DTName.clear(); 
							DTName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditDRModal1"));
						    container_click_1.click();
						    boolean DTNameerrorModal1_3 = driver.findElement(By.id("DTNameErr")).isDisplayed();
						    if (DTNameerrorModal1_3 == false) {
						        test.log(Status.PASS, "Negative value validation for General -DTName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Negative value validation for General -DTName input with value: " + value + " failed");
						    }
						}

						// Test with float values
						String[] DTName_4 = {"1.5", "0.01", "3.14"}; 
						for (String value : DTName_4) {
							DTName.clear(); 
							DTName.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditDRModal1"));
						    container_click.click();
						    boolean DTNameerrorModal1_4 = driver.findElement(By.id("DTNameErr")).isDisplayed();
						    if (DTNameerrorModal1_4 == false) {
						        test.log(Status.PASS, "Float value validation for General -DTName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Float value validation for General -DTName input with value: " + value + " failed");
						    }
						}
						Thread.sleep(100);
						
						//Test With Mixed Value
						String[] DTName_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
						for (String value : DTName_5) {
							DTName.clear(); 
							DTName.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditDRModal1"));
						    container_click.click();
						    boolean DTNameErrerrorModal1_5 = driver.findElement(By.id("DTNameErr")).isDisplayed();
						    if (DTNameErrerrorModal1_5 == false) {
						        test.log(Status.PASS, "MixedValue validation for General -DTName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "MixedValue validation for General -DTName input with value: " + value + " failed");
						    }
						}
						
						WebElement cancelButton_edit_1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[6]/div/div/form/div[2]/a"));
						cancelButton_edit_1.click(); //add_downtime_reason
						
						
//Quality  Reasons
					    
					    test = extent.createTest("General - Input- Quality Reasons - Validation");
						WebElement ouity = driver.findElement(By.id("add_quality_reasons"));
						ouity.click();
						
						
						WebElement QReasonName = driver.findElement(By.id("QReasonName"));
						String[] QReasonName_1 = {""}; 
						for (String value : QReasonName_1) {
							QReasonName.clear();
							QReasonName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditQRModal1"));
						    container_click_1.click();
						    boolean QReasonNameerrorModal1_1 = driver.findElement(By.id("QReasonNameErr")).isDisplayed();
						    if (QReasonNameerrorModal1_1 == true) {
						        test.log(Status.PASS, "Empty value validation for General -QReasonName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Empty value validation for General -QReasonName input with value: " + value + " failed");
						    }
						} 
						
						Thread.sleep(100);
						String[] QReasonName_2 = {"\" or \"\"=\"", "; DROP TABLE","' OR ''=''"}; 
						for (String value : QReasonName_2) {
							QReasonName.clear();
							QReasonName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditQRModal1"));
						    container_click_1.click();
						    boolean QReasonNameerrorModal1_2 = driver.findElement(By.id("QReasonNameErr")).isDisplayed();
						    if (QReasonNameerrorModal1_2 == true) {
						        test.log(Status.PASS, "SQL Injection validation for General -QReasonName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "SQL Injection validation for General -QReasonName input with value: " + value + " failed");
						    }
						}String[] QReasonName_3 = {"-23", "-0.5", "-1000"}; 
						for (String value : QReasonName_3) {
							QReasonName.clear(); 
							QReasonName.sendKeys(value);
						    WebElement container_click_1 = driver.findElement(By.id("EditQRModal1"));
						    container_click_1.click();
						    boolean QReasonNameerrorModal1_3 = driver.findElement(By.id("QReasonNameErr")).isDisplayed();
						    if (QReasonNameerrorModal1_3 == false) {
						        test.log(Status.PASS, "Negative value validation for General -QReasonName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Negative value validation for General -QReasonName input with value: " + value + " failed");
						    }
						}

						// Test with float values
						String[] QReasonName_4 = {"1.5", "0.01", "3.14"}; 
						for (String value : QReasonName_4) {
							QReasonName.clear(); 
							QReasonName.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditQRModal1"));
						    container_click.click();
						    boolean QReasonNameerrorModal1_4 = driver.findElement(By.id("QReasonNameErr")).isDisplayed();
						    if (QReasonNameerrorModal1_4 == false) {
						        test.log(Status.PASS, "Float value validation for General -QReasonName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "Float value validation for General -QReasonName input with value: " + value + " failed");
						    }
						}
						Thread.sleep(100);
						
						//Test With Mixed Value
						String[] QReasonName_5 = {"1.5ABQQ", "-2.5ABQQ", "ANMA22"}; 
						for (String value : QReasonName_5) {
							QReasonName.clear(); 
							QReasonName.sendKeys(value);
						    WebElement container_click = driver.findElement(By.id("EditQRModal1"));
						    container_click.click();
						    boolean QReasonNameErrerrorModal1_5 = driver.findElement(By.id("QReasonNameErr")).isDisplayed();
						    if (QReasonNameErrerrorModal1_5 == false) {
						        test.log(Status.PASS, "MixedValue validation for General -QReasonName input with value: " + value + " is successful");
						    } else {
						        test.log(Status.FAIL, "MixedValue validation for General -QReasonName input with value: " + value + " failed");
						    }
						}
						
						WebElement cancelButton_edit_general = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[8]/div/div/form/div[2]/a"));
						cancelButton_edit_general.click();
			 
						
						
		}
		

	
// Close the Chrome Window
		@AfterClass
		public void tearDown() {
			if (driver != null) {
//	            driver.quit();
				extent.flush();
			}

		} 

// ********************** Test Result Report ********************** //

// If the Test case is Passed
		@Override
		public void onTestSuccess(ITestResult result) {
			System.out.println("Test '" + result.getName() + "' is PASSED");
		}

// If the Test case is Failed
		@Override
		public void onTestFailure(ITestResult result) {
			System.out.println("Test '" + result.getName() + "' is FAILED");
		}

// If the Test case is Skipped
		@Override
		public void onTestSkipped(ITestResult result) {
			System.out.println("Test '" + result.getName() + "' is SKIPPED");
		}
	}
