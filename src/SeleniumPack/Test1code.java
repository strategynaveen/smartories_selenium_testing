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

@Listeners(Test1code.class)
public class Test1code implements ITestListener {
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

// ********************** Title Name Login Page visibility Test ********************** //
	
	@Test(priority = 1)
	public void WebsiteTitleTesting() {
		test = extent.createTest("verifyTitle");
		
		//Title Checking Test
		String actualTitle = driver.getTitle();
		String expectedTitle = "OEE Monitoring!";
		if (actualTitle.equals(expectedTitle)) {
			test.log(Status.PASS, "Title matched successfully");
		} else {
			test.log(Status.FAIL, "Title does not match. Expected: " + expectedTitle + ", Actual: " + actualTitle);
		}
		
		//Login Button CChecking Test
		WebElement loginButton = driver.findElement(By.id("login-mach"));
		if (loginButton.isDisplayed()) {
			test.log(Status.PASS, "Login button is displayed");
		} else {
			test.log(Status.FAIL, "Login button is not displayed");
		}
	 
	}
	
	
	
	
// ********************** Username Password Validation Test ********************** //
	//Starts Each Time
	@BeforeMethod
    public void navigateToLoginPage() {
//        driver.get("https://smartmach-demo.smartories.com/Login");
		driver.get("http://localhost/Selenium_Smart/");
    }

    // Test Case with Data Provider
    @Test( priority = 2 ,dataProvider = "usernamesAndPasswords")
    public void UsernamePasswordTesting(String username, String password) {
    	
    	//Get the Input Ids (User Name and Password)
    	test = extent.createTest("inputValidate");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("userpassword"));
        
        //Send the User name and passwerd into Input Fields
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        //Check the error messge is Showed Or Not
        boolean isGmailValid = driver.findElement(By.id("user_mail_err")).isDisplayed();

        //Validate the Webiste Email password validation with Actuval Email Validation
        if ((isValidEmail(username) == false) && (isGmailValid == true)) {
            test.log(Status.PASS, "Email is Wrong, Error message is Showing, So TestCase Passed");
        } else if ((isValidEmail(username) == false) && (isGmailValid == false)) {
            test.log(Status.FAIL, "Email is Wrong, But Error message is Not showing TestCase Failed, Bug Found");
        } else if ((isValidEmail(username) == true) && (isGmailValid == false)) {
            test.log(Status.PASS, "Email is Correct, Error message is Not Showing, So TestCase Passed");
        }
    }
	
	//Provide the username and password possibilities
	@DataProvider(name = "usernamesAndPasswords")
	public Object[][] userData() {
		return new Object[][] { 
		{ "user_123@example.com", "password1" },        
		{ "john-doe@example.com", "password2" },        							
        { "user.email@example.com", "wonderland" },     							
        { "jane.doe@example.com", "admin123" },         							
        { "invalid_user@example.com", "invalid_password" },    					
    	{ "user_123@example.com", "wrong_password" },         					
    	{ "jane.doe@example.com", "incorrect_pass" },         					
    	{ "user@company.com", "test123" },                    					
    	{ "user_456", "letmein" },                             					
    	{ "bobJones", "securepass" },                          					
    	{ "user123", "myp@ss" },                                					
    	{ "user-789", "changeme" },                             					
    	{ "user_abc", "password123" },                          					
    	{ "user_789", "welcome" },                              				
    	{ "pass_word", "pass@word" },                           					
    	{ "user_10", "123456" }};
	} 

	//Actual Validation
	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	
	
	
// ********************** Username Password And Login Test ********************** //
	@Test(priority = 3, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting"})

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

	@Test(priority = 4, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting"})
//@Test
	public void MachineModuleTesting() throws InterruptedException {
		
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
//      buttonToClick.click();
		actions_T.moveToElement(buttonToClick_T).perform();
		buttonToClick_T.click();
		String AfterUrlClick_T = driver.getCurrentUrl();
		if(!beforeUrlClick_T.equals(AfterUrlClick_T) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		
//		test = extent.createTest("Insert Invalid Data in Machines");
//		
//		WebElement AddMachine_T = driver.findElement(By.id("add_machine_button"));
//		AddMachine_T.click();
//		WebElement inputMachinename_T = driver.findElement(By.id("inputMachineName"));
//		WebElement inputMachineRateHour_T = driver.findElement(By.id("inputMachineRateHour"));
//		WebElement inputMachineOffRateHour_T = driver.findElement(By.id("inputMachineOffRateHour"));
//		WebElement inputTonnage_T = driver.findElement(By.id("inputTonnage"));
//		WebElement inputMachineBrand_T = driver.findElement(By.id("inputMachineBrand"));
//		WebElement inputMachineSerialId_T = driver.findElement(By.id("inputMachineSerialId"));
//		inputMachinename_T.sendKeys("Test_machine");
//		inputMachineRateHour_T.sendKeys("123");
//		inputMachineOffRateHour_T.sendKeys("123");
//		inputTonnage_T.sendKeys("12");
//		inputMachineBrand_T.sendKeys("Test");
//		inputMachineSerialId_T.sendKeys("Test");
		
		
		
		test = extent.createTest("MachineModuleTesting");

//		WebElement usernameInput1 = driver.findElement(By.id("username"));
//		WebElement passwordInput1 = driver.findElement(By.id("userpassword"));
//		WebElement loginButton = driver.findElement(By.id("login_submit"));
//
//		usernameInput1.sendKeys("adminadmin@gmail.com");
//		passwordInput1.sendKeys("Admin@123");
//		loginButton.click();
//		Thread.sleep(2000);
		
		
		WebElement SelectSite = driver.findElement(By.id("site_id"));
		SelectSite.click();
//		WebElement SelectSiteoption = driver.findElement(By.xpath("//*[@id=\"site_id\"]/option[2]"));
//		SelectSiteoption.click();
		Thread.sleep(3000);
		String beforeUrlClick = driver.getCurrentUrl();
		WebElement hoverElement = driver.findElement(By.className("fa-gear"));
		Actions actions = new Actions(driver);
		actions.moveToElement(hoverElement).perform();
		Thread.sleep(1000);
		WebElement buttonToClick = driver.findElement(By.xpath("/html/body/div/div[1]/ul/li[4]/ul/li[1]/div[2]/a"));
//      buttonToClick.click();
		actions.moveToElement(buttonToClick).perform();
		buttonToClick.click();
		String AfterUrlClick = driver.getCurrentUrl();
		if(!beforeUrlClick.equals(AfterUrlClick) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		
		test = extent.createTest("Insert Proper Data in Machines");
		
		WebElement AddMachine = driver.findElement(By.id("add_machine_button"));
		AddMachine.click();
		WebElement inputMachinename = driver.findElement(By.id("inputMachineName"));
		WebElement inputMachineRateHour = driver.findElement(By.id("inputMachineRateHour"));
		WebElement inputMachineOffRateHour = driver.findElement(By.id("inputMachineOffRateHour"));
		WebElement inputTonnage = driver.findElement(By.id("inputTonnage"));
		WebElement inputMachineBrand = driver.findElement(By.id("inputMachineBrand"));
		WebElement inputMachineSerialId = driver.findElement(By.id("inputMachineSerialId"));
		inputMachinename.sendKeys("Test_machine");
		inputMachineRateHour.sendKeys("123");
		inputMachineOffRateHour.sendKeys("123");
		inputTonnage.sendKeys("12");
		inputMachineBrand.sendKeys("Test");
		inputMachineSerialId.sendKeys("Test");
		
		WebElement container_click = driver.findElement(By.id("inputMachineSerialIdCunt"));
		container_click.click();
		boolean ErrorField = driver.findElement(By.id("inputMachineSerialId_err")).isDisplayed();
		WebElement Search_body = driver.findElement(By.tagName("body"));
		System.out.print("Body Content "+ Search_body.getText());
		Thread.sleep(3000);
		if (ErrorField == true) {
			WebElement CancelButton = driver.findElement(By.className("btn_cancel"));
			CancelButton.click();
			String search_string = "Test_machine";
			if (Search_body.getText().contains(search_string)) {
				test.log(Status.PASS, "Data is already added, Added Data In Machine page: Test Case Pass");
			} else {
				test.log(Status.FAIL, "Data is already added, Added Data NotIn the Machine page : Test Case Fail");
			}

		}

		else {
			WebElement SaveButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div/div/div[3]/input"));
			SaveButton.click();
			driver.navigate().refresh();
			WebElement Search_body1 = driver.findElement(By.tagName("body"));
			String search_string = "Test_machine";
			if (Search_body1.getText().contains(search_string)) {
				test.log(Status.PASS, "Data added Successfully, Added Data In Machine page: Test Case Pass");
			} else {
				test.log(Status.FAIL, "Added Data NotIn the Machine page : Test Case Fail");
			}
		} 

//		Thread.sleep(1000);
		
		
		test = extent.createTest("ActiveInactiveCheck");

		WebElement option_Button = driver.findElement(By.className("edit-menu"));
		option_Button.click();

		WebElement bodyElement = driver.findElement(By.tagName("body"));
		String bodyText = bodyElement.getText();

		String expectedCheck = "DEACTIVATE";
		String actualCheck = "ACTIVATE";

		String activeCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[1]/span";
		String inactiveCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[2]/span";

		String activeCount = driver.findElement(By.xpath(activeCountXpath)).getText();
		String inactiveCount = driver.findElement(By.xpath(inactiveCountXpath)).getText();

		if (bodyText.contains(expectedCheck)) {
		    WebElement deactivateButton = driver.findElement(By.className("deactivate-machine"));
		    deactivateButton.click();
		    Thread.sleep(1000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-deactive"));
		    deactivateSaveButton.click();

		    String inactiveCountAfter = driver.findElement(By.xpath(inactiveCountXpath)).getText();

		    if (!inactiveCount.equals(inactiveCountAfter)) {
		        test.log(Status.PASS, "Inactive Count has changed, TestCase Passed ");
		    } else {
		        test.log(Status.FAIL, "Inactive Count has not changed, TestCase Failed ");
		    }
		    test.log(Status.PASS, "Deactivated, TestCase Passed ");
		} else if (bodyText.contains(actualCheck)) {
		    WebElement activateButton = driver.findElement(By.className("activate-machine"));
		    activateButton.click();
		    Thread.sleep(1000);
		    WebElement activateSaveButton = driver.findElement(By.className("Status-active"));
		    activateSaveButton.click();

		    String activeCountAfter = driver.findElement(By.xpath(activeCountXpath)).getText();

		    if (!activeCount.equals(activeCountAfter)) {
		        test.log(Status.PASS, "Active Count has changed, TestCase Passed ");
		    } else {
		        test.log(Status.FAIL, "Active Count has not changed, TestCase Failed ");
		    }
		    test.log(Status.PASS, "Activated, TestCase Passed ");
		} else {
		    test.log(Status.FAIL, "Activation or Deactivation, TestCase Failed ");
		}

		if (bodyText.contains("DEACTIVATE")) {
		    WebElement optionButton1 = driver.findElement(By.className("edit-menu"));
		    optionButton1.click();
		    WebElement deactivateButton = driver.findElement(By.className("activate-machine"));
		    deactivateButton.click();
		    Thread.sleep(1000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-active"));
		    deactivateSaveButton.click();
//		    optionButton1.click();
		    System.out.println("Edit Function IF After Button");
		} else {
		    System.out.println("Edit Function Else ");
		}
//		WebElement editButton = driver.findElement(By.className("edit-machine"));
//		editButton.click();

		
//		Thread.sleep(1000);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		test = extent.createTest("EditOptioninmachin");
		WebElement option_Button2 = driver.findElement(By.className("edit-menu"));
		option_Button2.click();
		
		WebElement Edit_Machine = driver.findElement(By.className("edit-menu"));
		Edit_Machine.click();
		WebElement Edit_option=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_option.click();
		WebElement Edit_option_MC_Name=driver.findElement(By.id("editMachineName"));
		Edit_option_MC_Name.clear();
		Edit_option_MC_Name.sendKeys("Test_machine1");
		WebElement Edit_option_MC_Name_Save = driver.findElement(By.id("edit_machine_data"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Save.click();
		String EneteredData = "Test_machine1";
		WebElement bodyElement1 = driver.findElement(By.tagName("body"));
		String bodyText1 = bodyElement1.getText();
		WebElement Close_element=driver.findElement(By.id("EditMachineModal"));
		Close_element.click();
		
	
		if(bodyText1.contains(EneteredData)) {
			test.log(Status.PASS, "First Edited Text Showed In Home screen  , TestCase Passed ");
		} else {
			test.log(Status.FAIL, "First Edited Text Not Showed In Home screen  , TestCase Failed ");
		}
		
		
		
		WebElement Edit_Machine1 = driver.findElement(By.className("edit-menu"));
		Edit_Machine1.click();
		WebElement Edit_option1=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_option1.click();
		WebElement Edit_option_MC_Name1=driver.findElement(By.id("editMachineName"));
		Edit_option_MC_Name1.clear();
		Edit_option_MC_Name1.sendKeys("Test_machine");
		WebElement Edit_option_MC_Name_Save1 = driver.findElement(By.id("edit_machine_data"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Save1.click();
		String EneteredData1 = "Test_machine";
		WebElement bodyElement2 = driver.findElement(By.tagName("body"));
		String bodyText2 = bodyElement2.getText();
		if(bodyText2.contains(EneteredData1)) {
			test.log(Status.PASS, "Second Edited Text Showed In Home screen  , TestCase Passed ");
		} else {
			test.log(Status.FAIL, "Second Edited Text Not Showed In Home screen  , TestCase Failed ");
		}
	}


	@Test(priority = 5, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting","MachineModuleTesting"})
	public void PartModuleTesting() throws InterruptedException {
		test = extent.createTest("PartModuleTesting");

//		WebElement usernameInput1 = driver.findElement(By.id("username"));
//		WebElement passwordInput1 = driver.findElement(By.id("userpassword"));
//		WebElement loginButton = driver.findElement(By.id("login_submit"));
//
//		usernameInput1.sendKeys("adminadmin@gmail.com");
//		passwordInput1.sendKeys("Admin@123");
//		loginButton.click();
//		Thread.sleep(1000); 
		String beforeUrlClick = driver.getCurrentUrl();
//		WebElement SelectSite = driver.findElement(By.id("site_id"));
//		SelectSite.click();
//		WebElement SelectSiteoption = driver.findElement(By.xpath("//*[@id=\"site_id\"]/option[2]"));
//		SelectSiteoption.click();
//		Thread.sleep(2000);

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
		
		test = extent.createTest("AddPartTest");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
		Thread.sleep(1000);
		WebElement AddPart = driver.findElement(By.id("add_part_modal"));
		AddPart.click();
		WebElement PartName = driver.findElement(By.id("inputPartName"));
		PartName.sendKeys("Test_Part");
		Thread.sleep(100);
		WebElement NICT = driver.findElement(By.id("inputNICT"));
		NICT.sendKeys("50");
		Thread.sleep(100);
		WebElement PartPrice = driver.findElement(By.id("inputPartPrice"));
		PartPrice.sendKeys("50");
		Thread.sleep(100);
		WebElement NoOfPart = driver.findElement(By.id("inputNoOfPartsPerCycle"));
		NoOfPart.sendKeys("50");
		Thread.sleep(100);
		WebElement PartWeight = driver.findElement(By.id("inputPartWeight"));
		PartWeight.sendKeys("50");
		Thread.sleep(100);
		WebElement MaterialPrice = driver.findElement(By.id("inputMaterialPrice"));
		MaterialPrice.sendKeys("50");
		Thread.sleep(100);
		WebElement MaterialName = driver.findElement(By.id("inputMaterialName"));
		MaterialName.sendKeys("50");
		Thread.sleep(100);
		WebElement SelectTool = driver.findElement(By.id("inputToolName"));
		SelectTool.click();
		WebElement Selectoption = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[1]/div/div/div[2]/div[1]/div[2]/div/select/option[2]"));
		Selectoption.click();
		WebElement NewTool = driver.findElement(By.id("inputNewToolName"));
		NewTool.sendKeys("Test_Tool");
//		WebElement container_click = driver.findElement(By.className("saveBtnStyle"));
//		container_click.click();
		boolean errorFieldDisplayed = driver.findElement(By.id("inputPartNameErr")).isDisplayed();
//		Thread.sleep(3000);	
		if (errorFieldDisplayed == true) {
		    WebElement cancelButton = driver.findElement(By.className("cancelBtnStyle"));
		    cancelButton.click();
		    test.log(Status.PASS, "Error message is not showing: Test Case Pass");
		} else {
		    WebElement saveButton = driver.findElement(By.className("saveBtnStyle"));
		    Thread.sleep(1000); // Consider using WebDriverWait instead of Thread.sleep for better synchronization
		    saveButton.click();
		    test.log(Status.PASS, "Error message is not showing: New Data added: Test Case Pass");
		}

		WebElement Search_body1 = driver.findElement(By.tagName("body"));
		String search_string = "Test_Part";
		if (Search_body1.getText().contains(search_string)) {
			test.log(Status.PASS, "Data added Successfully, Added Data In Part page: Test Case Pass");
		} else {
			test.log(Status.FAIL, "Added Data NotIn the Part page : Test Case Fail");
		}
		
//		Thread.sleep(1000);
		
		//Active Inactive
		
		test = extent.createTest("ActiveInactiveParts");
		
		WebElement option_Button = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/a"));
		option_Button.click();

		WebElement bodyElement = driver.findElement(By.tagName("body"));
		String bodyText = bodyElement.getText();

		String expectedCheck = "DEACTIVATE";
		String actualCheck = "ACTIVATE";

		String activeCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[1]/span";
		String inactiveCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[2]/span";

		String activeCount = driver.findElement(By.xpath(activeCountXpath)).getText();
		String inactiveCount = driver.findElement(By.xpath(inactiveCountXpath)).getText();

		if (bodyText.contains(expectedCheck)) {
		    WebElement deactivateButton = driver.findElement(By.className("deactivate-tool"));
		    deactivateButton.click();
		    Thread.sleep(1000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-deactivate"));
		    deactivateSaveButton.click();

		    String inactiveCountAfter = driver.findElement(By.xpath(inactiveCountXpath)).getText();

		    if (!inactiveCount.equals(inactiveCountAfter)) {
		        test.log(Status.PASS, "Part Inactive Count has changed, TestCase Passed ");
		    } else {
		        test.log(Status.FAIL, "Part Inactive Count has not changed, TestCase Failed ");
		    }
		    test.log(Status.PASS, "Deactivated, TestCase Passed ");
		} else if (bodyText.contains(actualCheck)) {
		    WebElement activateButton = driver.findElement(By.className("activate-tool"));
		    activateButton.click();
		    Thread.sleep(1000);
		    WebElement activateSaveButton = driver.findElement(By.className("Status-activate"));
		    activateSaveButton.click();

		    String activeCountAfter = driver.findElement(By.xpath(activeCountXpath)).getText();

		    if (!activeCount.equals(activeCountAfter)) {
		        test.log(Status.PASS, "Part Active Count has changed, TestCase Passed ");
		    } else {
		        test.log(Status.FAIL, "Part Active Count has not changed, TestCase Failed ");
		    }
		    test.log(Status.PASS, "Part Activated, TestCase Passed ");
		} else {
		    test.log(Status.FAIL, "Part Activation or Deactivation, TestCase Failed ");
		}

		if (bodyText.contains("DEACTIVATE")) {
		    WebElement optionButton1 = driver.findElement(By.className("edit-menu"));
		    optionButton1.click();
		    WebElement deactivateButton = driver.findElement(By.className("activate-tool"));
		    deactivateButton.click();
		    Thread.sleep(1000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-activate"));
		    deactivateSaveButton.click();
//		    optionButton1.click();
		    System.out.println("Part Edit Function IF After Button");
		} else {
		    System.out.println("Part Edit Function Else ");
		}
		
		
		
		test = extent.createTest("EditOptioninPart");
		WebElement option_Button2 = driver.findElement(By.className("edit-menu"));
		option_Button2.click();
		
		WebElement Edit_Machine = driver.findElement(By.className("edit-menu"));
		Edit_Machine.click();
		WebElement Edit_option=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_option.click();
		WebElement ToolPartName = driver.findElement(By.id("EditPartName"));
		ToolPartName.clear();
		ToolPartName.sendKeys("Test_Part_1");
		
		WebElement Edit_option_MC_Name_Save = driver.findElement(By.className("EditTool"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Save.click();
		String EneteredData = "Test_Part_1";
		WebElement bodyElement1 = driver.findElement(By.tagName("body"));
		String bodyText1 = bodyElement1.getText();
//		WebElement Close_element=driver.findElement(By.id("EditMachineModal"));
//		Close_element.click();
		
	
		if(bodyText1.contains(EneteredData)) {
			test.log(Status.PASS, "First Edited Text Showed In Home screen  , TestCase Passed ");
		} else {
			test.log(Status.FAIL, "First Edited Text Not Showed In Home screen  , TestCase Failed ");
		}
		
		
		
		WebElement Edit_Machine1 = driver.findElement(By.className("edit-menu"));
		Edit_Machine1.click();
		WebElement Edit_option1=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li/ul/li[2]/a"));
		Edit_option1.click();
		WebElement Edit_option_MC_Name1=driver.findElement(By.id("EditPartName"));
		Edit_option_MC_Name1.clear();
		Edit_option_MC_Name1.sendKeys("Test_Part");
		WebElement Edit_option_MC_Name_Save1 = driver.findElement(By.className("EditTool"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Save1.click();
		String EneteredData1 = "Test_Part";
		WebElement bodyElement2 = driver.findElement(By.tagName("body"));
		String bodyText2 = bodyElement2.getText();
		if(bodyText2.contains(EneteredData1)) {
			test.log(Status.PASS, "Second Edited Text Showed In Home screen  , TestCase Passed ");
		} else {
			test.log(Status.FAIL, "Second Edited Text Not Showed In Home screen  , TestCase Failed ");
		}
	}
	
	
	// General Settings 
	
	@Test(priority = 6, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting","MachineModuleTesting"})
	public void GeneralModuleTesting() throws InterruptedException {
		test = extent.createTest("GeneralModuleTesting");

//		WebElement usernameInput1 = driver.findElement(By.id("username"));
//		WebElement passwordInput1 = driver.findElement(By.id("userpassword"));
//		WebElement loginButton = driver.findElement(By.id("login_submit"));
//
//		usernameInput1.sendKeys("adminadmin@gmail.com");
//		passwordInput1.sendKeys("Admin@123");
//		loginButton.click();
//		Thread.sleep(1000);
		String beforeUrlClick = driver.getCurrentUrl();
//		WebElement SelectSite = driver.findElement(By.id("site_id"));
//		SelectSite.click();
//		WebElement SelectSiteoption = driver.findElement(By.xpath("//*[@id=\"site_id\"]/option[2]"));
//		SelectSiteoption.click();
//		Thread.sleep(2000);

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
		
		
		
		WebElement EOTEEP = driver.findElement(By.id("EOTEEP"));
		WebElement EOOOE = driver.findElement(By.id("EOOOE"));
		WebElement EOOEE = driver.findElement(By.id("EOOEE"));
		WebElement EAvailability = driver.findElement(By.id("EAvailability"));
		WebElement EPerformance = driver.findElement(By.id("EPerformance"));
		WebElement EQuality = driver.findElement(By.id("EQuality"));
		EOTEEP.clear();
		EOOOE.clear();
		EOOEE.clear();
		EAvailability.clear();
		EPerformance.clear();
		EQuality.clear();
		Thread.sleep(100);
		EOOEE.sendKeys("96");
		Thread.sleep(100);
		EOOOE.sendKeys("95.0");
		Thread.sleep(100);
		EOTEEP.sendKeys("70");
		Thread.sleep(100);
		EAvailability.sendKeys("89");
		Thread.sleep(100);
		EPerformance.sendKeys("89");
		Thread.sleep(100);
		EQuality.sendKeys("89.0");
		Thread.sleep(100);
		
	

				boolean[] errorFields = {
					    driver.findElement(By.id("EOTEEPErr")).isDisplayed(),
					    driver.findElement(By.id("EOOOEErr")).isDisplayed(),
					    driver.findElement(By.id("EOOEEErr")).isDisplayed(),
					    driver.findElement(By.id("EAvailabilityErr")).isDisplayed(),
					    driver.findElement(By.id("EPerformanceErr")).isDisplayed(),
//					    driver.findElement(By.id("EQualityErr")).isDisplayed(),
					};
					Thread.sleep(1000);  
					boolean anyErrorDisplayed = false; 
					for (boolean field : errorFields) {
					    if (field) {
					        anyErrorDisplayed = true;
					        break;
					    }
					}
					Thread.sleep(1000);
					if (!anyErrorDisplayed) {
					    WebElement saveButton1 = driver.findElement(By.className("Update_GFM"));
					    Thread.sleep(1000); 
					    saveButton1.click();   
					    test.log(Status.PASS, "No error message displayed:  Data Edited Successfully : Test Case Pass");
					} else {
					    WebElement cancelButton = driver.findElement(By.className("cancelBtnStyle"));
					    cancelButton.click();
					    test.log(Status.FAIL, "Error message displayed: Test Case Failed");
					}
					
					
					Thread.sleep(1000);
					
					
					// < - - - - Edit General Goal Normal test - - - ->
					
					test = extent.createTest("EditGoalsNormal");
					WebElement GoalsEdit1 = driver.findElement(By.className("img_font_wh"));
					GoalsEdit1.click();
					Thread.sleep(1000);	
					WebElement EOTEEP1 = driver.findElement(By.id("EOTEEP"));
					WebElement EOOOE1 = driver.findElement(By.id("EOOOE"));
					WebElement EOOEE1 = driver.findElement(By.id("EOOEE"));
					WebElement EAvailability1 = driver.findElement(By.id("EAvailability"));
					WebElement EPerformance1 = driver.findElement(By.id("EPerformance"));
					WebElement EQuality1 = driver.findElement(By.id("EQuality"));
					EOTEEP1.clear();
					EOOOE1.clear();
					EOOEE1.clear();
					EAvailability1.clear();
					EPerformance1.clear();
					EQuality1.clear();
					Thread.sleep(100);
					EOOEE1.sendKeys("86");
					Thread.sleep(100);
					EOOOE1.sendKeys("85.0");
					Thread.sleep(100);
					EOTEEP1.sendKeys("60");
					Thread.sleep(100);
					EAvailability1.sendKeys("79");
					Thread.sleep(100);
					EPerformance1.sendKeys("79");
					Thread.sleep(100);
					EQuality1.sendKeys("79.0");
					Thread.sleep(100);

							boolean[] errorFields1 = {
								    driver.findElement(By.id("EOTEEPErr")).isDisplayed(),
								    driver.findElement(By.id("EOOOEErr")).isDisplayed(),
								    driver.findElement(By.id("EOOEEErr")).isDisplayed(),
								    driver.findElement(By.id("EAvailabilityErr")).isDisplayed(),
								    driver.findElement(By.id("EPerformanceErr")).isDisplayed(),
//								    driver.findElement(By.id("EQualityErr")).isDisplayed(),
								};
								Thread.sleep(1000);  
								boolean anyErrorDisplayed1 = false; 
								for (boolean field : errorFields1) {
								    if (field) {
								        anyErrorDisplayed1 = true;
								        break;
								    }
								}
								Thread.sleep(1000);
								if (!anyErrorDisplayed1) {
								    WebElement saveButton1 = driver.findElement(By.className("Update_GFM"));
								    saveButton1.click();
								    Thread.sleep(1000);    
								    test.log(Status.PASS, "No error message displayed:  Data Re-Edited Successfully : Test Case Pass");
								} else {
								    WebElement cancelButton1 = driver.findElement(By.className("cancelBtnStyle"));
								    cancelButton1.click();
								    test.log(Status.FAIL, "Error message displayed: Test Case Failed");
								}
		
								Thread.sleep(1000); 
								//ThreshHold Data Edit and reEdit
								test = extent.createTest("OPERATOR_USER_INTERFACE");
								WebElement oui = driver.findElement(By.id("click_thresh_hold"));
								oui.click();
								WebElement Thresh_hold = driver.findElement(By.id("Update_DThreshold"));
								Thresh_hold.clear();
								Thresh_hold.sendKeys("79"); 
								
								boolean[] errorFields2 = {
									    driver.findElement(By.id("Update_DThresholdErr")).isDisplayed(),
									    
									};
									Thread.sleep(1000);  
									boolean anyErrorDisplayed2 = false; 
									for (boolean field : errorFields2) {
									    if (field) {
									        anyErrorDisplayed2 = true;
									        break;
									    }
									}
									Thread.sleep(1000);
									if (!anyErrorDisplayed1) {
									    WebElement saveButton1 = driver.findElement(By.className("Update_DT"));
									    saveButton1.click();
									    Thread.sleep(1000);    
									    test.log(Status.PASS, "No error message displayed: OUI Data Edited Successfully : Test Case Pass");
									} else {
									    WebElement cancelButton1 = driver.findElement(By.className("cancelBtnStyle"));
									    cancelButton1.click();
									    test.log(Status.PASS, "Error message displayed: OUI Test Case Pass");
									}
									
									Thread.sleep(1000);
									WebElement oui1 = driver.findElement(By.id("click_thresh_hold"));
									oui1.click();
									WebElement Thresh_hold1 = driver.findElement(By.id("Update_DThreshold"));
									Thresh_hold1.clear();
									Thresh_hold1.sendKeys("50"); 

									Thread.sleep(1000);  

									boolean anyErrorDisplayed3 = false;
									try {
									    WebElement errorField3 = driver.findElement(By.id("Update_DThresholdErr"));
									    if (errorField3.isDisplayed()) {
									        anyErrorDisplayed3 = true;
									    }
									} catch (NoSuchElementException | TimeoutException e) {
									    // Ignore if the error field is not found or not displayed
									}

									if (!anyErrorDisplayed3) {
									    WebElement saveButton2 = driver.findElement(By.className("Update_DT"));
									    Thread.sleep(1000);  
									    saveButton2.click();
									    test.log(Status.PASS, "No error message displayed: OUI Data RE_Edited Successfully : Test Case Pass");
									} else {
									    WebElement cancelButton1 = driver.findElement(By.className("cancelBtnStyle"));
									    Thread.sleep(1000);  
									    cancelButton1.click();
									    test.log(Status.FAIL, "Error message displayed: OUI Test Case Fail");
									}
									
									// < - - - - Edit General Goal Normal test - - - ->
									
									test = extent.createTest("AddDownTime");
									WebElement ClickAdd = driver.findElement(By.id("add_downtime_reason"));
									ClickAdd.click();   
									 
									
									WebElement Userid = driver.findElement(By.id("DTName"));
									Userid.sendKeys("Test Reason"); 
									Thread.sleep(100);
									WebElement DropOpt = driver.findElement(By.id("DTRCategory"));
									DropOpt.click();
									Thread.sleep(100);
									WebElement DropOpt_plan = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[6]/div/div/form/div[1]/div/div/div[2]/div/select/option[2]"));
									DropOpt_plan.click();
									
									Thread.sleep(1000);  

									boolean anyErrorDisplayed4 = false;
									try {
									    WebElement errorField4 = driver.findElement(By.id("DTNameErr"));
									    if (errorField4.isDisplayed()) {
									        anyErrorDisplayed4 = true;
									    }
									} catch (NoSuchElementException | TimeoutException e) {
									    // Ignore if the error field is not found or not displayed
									}

									if (!anyErrorDisplayed4) {
									    WebElement saveButton2 = driver.findElement(By.id("submit_downtime_reason"));
									    Thread.sleep(100);  
									    saveButton2.click();
									    test.log(Status.PASS, "No error message displayed: DownTime Added Successfully : Test Case Pass");
									} else {
									    WebElement cancelButtonDown = driver.findElement(By.className("cancelBtnStyle"));
									    Thread.sleep(100);  
									    cancelButtonDown.click();
									    test.log(Status.FAIL, "Error message displayed: DownTime Not Added :  Test Case Fail");
									}
									
									// < - - - -  Quality Reason Normal test - - - ->
									
									Thread.sleep(1000);
									test = extent.createTest("AddQualityReason");
//									WebElement element = driver.findElement(By.id("add_quality_reasons"));
//									scrollToElement(driver, element);
//									Thread.sleep(1000);
//									WebElement clickAddicon1 = driver.findElement(By.id("add_quality_reasons"));
//									Thread.sleep(1000);
									WebElement clickAddicon = driver.findElement(By.id("add_quality_reasons"));
									((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", clickAddicon);
									Thread.sleep(1000); 
									clickAddicon.click();   
									 
									
									WebElement QReasonName = driver.findElement(By.id("QReasonName"));
									QReasonName.sendKeys("Test Reason Quality Rejection"); 
									Thread.sleep(100);
									
									boolean anyErrorDisplayed5 = false;
									try {
									    WebElement errorField5 = driver.findElement(By.id("QReasonNameErr"));
									    if (errorField5.isDisplayed()) {
									        anyErrorDisplayed5 = true;
									    }
									} catch (NoSuchElementException | TimeoutException e) {
									    // Ignore if the error field is not found or not displayed
									}

									if (!anyErrorDisplayed5) {
									    WebElement saveButton2 = driver.findElement(By.id("submit_quality_reasons"));
									    Thread.sleep(100);  
									    saveButton2.click();
									    test.log(Status.PASS, "No error message displayed: Quality Reason Added Successfully : Test Case Pass");
									} else {
									    WebElement cancelButtonDown = driver.findElement(By.className("cancelBtnStyle"));
									    Thread.sleep(100);  
									    cancelButtonDown.click();
									    test.log(Status.FAIL, "Error message displayed: Quality Reason Not Added :  Test Case Fail");
									}
									
									
									
									
									// < - - - -  Check the Content Shpwn in the Main page (Qu/Down) test - - - ->
									
									test = extent.createTest("DataCheck_QA_Downtime");
									Thread.sleep(1000);
									WebElement bodyElement = driver.findElement(By.tagName("body"));
									String bodyText = bodyElement.getText();
									
									String Quality = "Test Reason Quality Rejection";
									String Downtime = "Test Reason";
									if (bodyText.contains(Quality) && bodyText.contains(Downtime)) {
										
										test.log(Status.PASS, "Downtime Data & Quality Reason Data Added Successfully, TestCase Passed ");   
									}
									else {
									    test.log(Status.FAIL, "TestCase Failed ");
									}
									
									// < - - - - Edit General Goal Normal test - - - ->
									
									test = extent.createTest("AddButtonConfig");
									WebElement ButtonConfig = driver.findElement(By.id("add_btn_interface"));
									ButtonConfig.click();   
									 
									
									WebElement btn_num = driver.findElement(By.id("btn_num"));
									btn_num.sendKeys("11"); 
									Thread.sleep(100);
									WebElement btn_ui_check_dq = driver.findElement(By.id("btn_ui_check_dq"));
									btn_ui_check_dq.click();
									Thread.sleep(100);
									WebElement DropOpt_plan1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div/select/option[3]"));
									DropOpt_plan1.click();
									
									Thread.sleep(1000);  
									WebElement btn_ui_aquality_rdrp = driver.findElement(By.id("btn_ui_aquality_rdrp"));
									btn_ui_aquality_rdrp.click();
									Thread.sleep(100);
									WebElement DropOpt_plan2 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div/div/div[2]/div[2]/div[3]/div[2]/div/div/select/option[2]"));
									DropOpt_plan2.click();

									boolean anyErrorDisplayed6 = false;
									try {
									    WebElement errorField6 = driver.findElement(By.className("add_btn_ui_err"));
									    if (errorField6.isDisplayed()) {
									        anyErrorDisplayed6 = true;
									    }
									} catch (NoSuchElementException | TimeoutException e) {
									    // Ignore if the error field is not found or not displayed
									}

									if (!anyErrorDisplayed6) {
									    WebElement saveButton2 = driver.findElement(By.className("add_btn_ui_submission"));
									    Thread.sleep(100);  
									    saveButton2.click();
									    test.log(Status.PASS, "No error message displayed: Button Added Successfully : Test Case Pass");
									} else {
									    WebElement cancelButtonDown = driver.findElement(By.className("cancelBtnStyle"));
									    Thread.sleep(100);  
									    cancelButtonDown.click();
									    test.log(Status.FAIL, "Error message displayed:  Button Added unSuccessfull :  Test Case Fail");
									}
									
									
									//Test
									test = extent.createTest("DataCheck_QA_Downtime");
									Thread.sleep(1000);
									WebElement bodyElement1 = driver.findElement(By.tagName("body"));
									String bodyText1 = bodyElement.getText();
									
									String Buttonin = "11";
									
									if (bodyText1.contains(Buttonin)) {
										
										test.log(Status.PASS, "Downtime Data & Quality Reason Data Added Successfully, TestCase Passed ");   
									}
									else {
									    test.log(Status.FAIL, "TestCase Failed ");
									}
		
	}
	
	
	// UserModuleTesting
	
	@Test(priority = 7, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting","MachineModuleTesting", "GeneralModuleTesting"})
	public void UserModuleTesting() throws InterruptedException {
		test = extent.createTest("UserModuleTesting");
//		WebElement usernameInput1 = driver.findElement(By.id("username"));
//		WebElement passwordInput1 = driver.findElement(By.id("userpassword"));
//		WebElement loginButton = driver.findElement(By.id("login_submit"));
//
//		usernameInput1.sendKeys("adminadmin@gmail.com");
//		passwordInput1.sendKeys("Admin@123");
//		loginButton.click();
//		Thread.sleep(1000);
		String beforeUrlClick = driver.getCurrentUrl();
//		WebElement SelectSite = driver.findElement(By.id("site_id"));
//		SelectSite.click();
//		WebElement SelectSiteoption = driver.findElement(By.xpath("//*[@id=\"site_id\"]/option[2]"));
//		SelectSiteoption.click();
//		Thread.sleep(2000);

		WebElement hoverElement = driver.findElement(By.className("fa-gear"));
		Actions actions = new Actions(driver);
		actions.moveToElement(hoverElement).perform();
		Thread.sleep(1000);
		WebElement buttonToClick = driver.findElement(By.xpath("/html/body/div[2]/div[1]/ul/li[4]/ul/li[4]/div[2]/a"));
		actions.moveToElement(buttonToClick).perform();
		buttonToClick.click();
		
		String AfterUrlClick = driver.getCurrentUrl();
		if(!beforeUrlClick.equals(AfterUrlClick) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		
		Thread.sleep(1000);
		
		
		// Add User Part
		
		test = extent.createTest("AddUserTest");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
		Thread.sleep(1000);
		
		WebElement AddUser = driver.findElement(By.id("add_user_model"));
		AddUser.click();
		WebElement Role = driver.findElement(By.id("inputRoleAdd"));
		Role.click();
		WebElement Selectoption = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div/div/div[2]/div[1]/div[1]/div/select/option[2]"));
		Selectoption.click();
		Thread.sleep(100);
		WebElement Userid = driver.findElement(By.id("inputUserEMail"));
		Userid.sendKeys("Tester@gmail.com");
		Thread.sleep(100);
		WebElement First_Name = driver.findElement(By.id("inputUserFirstName"));
		First_Name.sendKeys("Tester");
		Thread.sleep(100);
		WebElement Last_Name = driver.findElement(By.id("inputUserLastName"));
		Last_Name.sendKeys("One");
		Thread.sleep(100);
		WebElement Phone_No = driver.findElement(By.id("inputUserPhone"));
		Phone_No.sendKeys("9234567890");
		Thread.sleep(100);
		WebElement Designation = driver.findElement(By.id("inputUserDesignation"));
		Designation.sendKeys("Testing");
		Thread.sleep(100);

		boolean[] errorFields = {
			    driver.findElement(By.id("inputUserEMailErr")).isDisplayed(),
			    driver.findElement(By.id("inputUserPhoneErr")).isDisplayed(),
			    driver.findElement(By.id("inputUserDesignationErr")).isDisplayed(),
			    driver.findElement(By.id("inputUserFirstNameErr")).isDisplayed(),
			    driver.findElement(By.id("inputUserLastNameErr")).isDisplayed(),
			    driver.findElement(By.id("inputUserEMailErr")).isDisplayed(),
			    driver.findElement(By.id("input_dept_err")).isDisplayed(),
			    driver.findElement(By.id("validate_role")).isDisplayed()
			};
			Thread.sleep(3000);  
			boolean anyErrorDisplayed = false;
			for (boolean field : errorFields) {
			    if (field) {
			        anyErrorDisplayed = true;
			        break;
			    }
			}
			Thread.sleep(1000);
			if (!anyErrorDisplayed) {
			    WebElement saveButton = driver.findElement(By.className("saveBtnStyle"));
			    saveButton.click();
			    Thread.sleep(8000);    
			    test.log(Status.PASS, "No error message displayed: New Data added: Test Case Pass");
			} else {
			    WebElement cancelButton = driver.findElement(By.className("cancelBtnStyle"));
			    cancelButton.click();
			    test.log(Status.PASS, "Error message displayed: Test Case Pass");
			}



		
		
		
		WebElement Search_body1 = driver.findElement(By.tagName("body"));
		String search_string = "Tester@gmail.com";
		if (Search_body1.getText().contains(search_string)) {
			test.log(Status.PASS, "Data added Successfully, Added Data In Part page: Test Case Pass");
		} else {
			test.log(Status.FAIL, "Added Data NotIn the Part page : Test Case Fail");
		}
			

		
		
		Thread.sleep(1000);
		test = extent.createTest("ActiveInactiveUsers");
		
		WebElement option_Button3 = driver.findElement(By.className("edit-menu"));
		option_Button3.click();

		WebElement bodyElement = driver.findElement(By.tagName("body"));
		String bodyText = bodyElement.getText();

//		System.out.println("heloooooooooooooooooooooooooooo-- "+ bodyText);

		String expectedCheck = "DEACTIVATE";
		String actualCheck = "ACTIVATE";

		String activeCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[1]/span";
		String inactiveCountXpath = "/html/body/div/div[2]/div[1]/nav[1]/div/div/p[2]/span";

		String activeCount = driver.findElement(By.xpath(activeCountXpath)).getText();
		String inactiveCount = driver.findElement(By.xpath(inactiveCountXpath)).getText();

		if (bodyText.contains(expectedCheck)) {
		    WebElement deactivateButton = driver.findElement(By.className("deactivate-user"));
		    deactivateButton.click();
		    Thread.sleep(2000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-deactivate"));
		    deactivateSaveButton.click();
		    String inactiveCountAfter = driver.findElement(By.xpath(inactiveCountXpath)).getText();
		    
		    if (!inactiveCount.equals(inactiveCountAfter)) {
		        test.log(Status.PASS, "User Inactive Count has changed, TestCase Passed ");
		    } else {
		    	test.log(Status.PASS, "Deactivated, TestCase Passed ");
		    }
		    test.log(Status.PASS, "Deactivated, TestCase Passed ");
		} else if (bodyText.contains(actualCheck)) {
		    WebElement activateButton = driver.findElement(By.className("activate-user"));
		    activateButton.click();
		    Thread.sleep(1000);
		    WebElement activateSaveButton = driver.findElement(By.className("Status-activate"));
		    activateSaveButton.click();

		    String activeCountAfter = driver.findElement(By.xpath(activeCountXpath)).getText();

		    if (!activeCount.equals(activeCountAfter)) {
		        test.log(Status.PASS, "User Active Count has changed, TestCase Passed ");
		    } else {
		        test.log(Status.FAIL, "User Active Count has not changed, TestCase Failed ");
		    }
		    test.log(Status.PASS, "User Activated, TestCase Passed ");
		} else {
		    test.log(Status.FAIL, "User Activation or Deactivation, TestCase Failed ");
		}
		
		Thread.sleep(1000);
		if (bodyText.contains("DEACTIVATE")) {
			WebElement Edit_option = driver.findElement(By.className("edit-menu"));
			Edit_option.click();
			Thread.sleep(1000);
		    WebElement deactivateButton = driver.findElement(By.className("activate-user"));
		    Thread.sleep(1000);
		    deactivateButton.click();
		    Thread.sleep(1000);
		    WebElement deactivateSaveButton = driver.findElement(By.className("Status-activate"));
		    Thread.sleep(1000);
		    deactivateSaveButton.click();
		    test.log(Status.PASS, "User Activated, TestCase Passed");
		} else {
			test.log(Status.FAIL, "User Activation or Deactivation, TestCase Failed");
		}
		
		Thread.sleep(2000);
		// Edit user 
		
		
		test = extent.createTest("EditOptioninUser");
		WebElement option_Button2 = driver.findElement(By.className("edit-menu"));
		option_Button2.click();
		
		WebElement Edit_User = driver.findElement(By.className("edit-menu"));
		Edit_User.click();
		WebElement Edit_option=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/div[7]/ul/li/ul/li[1]/a"));
		Edit_option.click();
		WebElement ToolPartName = driver.findElement(By.id("EditUserDesignation"));
		ToolPartName.clear();
		ToolPartName.sendKeys("Tester Edit Check");
		
		WebElement Edit_option_MC_Name_Save = driver.findElement(By.className("EditUserData"));
		Thread.sleep(1000);
		Edit_option_MC_Name_Save.click();
		String EneteredData = "Tester Edit Check";
		WebElement bodyElement1 = driver.findElement(By.tagName("body"));
		String bodyText2 = bodyElement1.getText();
//		WebElement Close_element=driver.findElement(By.id("EditMachineModal"));
//		Close_element.click();
		Thread.sleep(1000);
		
	
		if(bodyText2.contains(EneteredData)) {
			test.log(Status.PASS, "First Edited Text Showed In Home screen  , TestCase Passed ");
		} else {
			test.log(Status.FAIL, "First Edited Text Not Showed In Home screen  , TestCase Failed ");
		}
		
	}
	
	
	@Test(priority = 7, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting","MachineModuleTesting", "GeneralModuleTesting", "UserModuleTesting"})
	public void DailyProductionStatus() throws InterruptedException {
		Thread.sleep(1000);
		test = extent.createTest("DailyProductionStatus_Test");
		String beforeUrlClick = driver.getCurrentUrl();
		WebElement hoverElement = driver.findElement(By.className("fa-calendar-day"));
		Actions actions = new Actions(driver);
		actions.moveToElement(hoverElement).perform();
		Thread.sleep(1000);
		WebElement hoverElement1 = driver.findElement(By.xpath("/html/body/div[2]/div[1]/ul/li[5]/a"));
		hoverElement1.click();
		String AfterUrlClick = driver.getCurrentUrl();
		if(!beforeUrlClick.equals(AfterUrlClick) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		 
		Thread.sleep(1000);
		
		test = extent.createTest("Input_Test");
		WebElement Date_input = driver.findElement(By.id("changed_date"));
		Date_input.click();
		Date_input.clear();
		Date_input.sendKeys("2024-01-02");
		Thread.sleep(2000);
		WebElement bodyElement = driver.findElement(By.tagName("body"));
		String bodyText = bodyElement.getText();
		WebElement Date_input1 = driver.findElement(By.id("logo"));
		Date_input1.click();
		Thread.sleep(1000);
		WebElement bodyElement1 = driver.findElement(By.tagName("body"));
		String bodyText1 = bodyElement1.getText();
		if(!bodyText.equals(bodyText1) ) {
			test.log(Status.PASS, "Data Loaded Successfully : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Data not Loaded : Test Case Fail");
		}
	}
	
	

//	 //Production Downtime
	@Test(priority = 7, dependsOnMethods = {"WebsiteTitleTesting", "UsernamePasswordTesting", "CorrectUserPassTesting","MachineModuleTesting", "GeneralModuleTesting", "UserModuleTesting"})
	    public void ProductionDowntime() throws InterruptedException {
		Thread.sleep(1000);
		test = extent.createTest("ProductionDowntime_Test");
		String beforeUrlClick = driver.getCurrentUrl();
		WebElement hoverElement = driver.findElement(By.className("fa-clock"));
		Actions actions = new Actions(driver);
		actions.moveToElement(hoverElement).perform();
		Thread.sleep(1000);
		WebElement hoverElement1 = driver.findElement(By.xpath("/html/body/div[2]/div[1]/ul/li[6]/a/img"));
		hoverElement1.click();
		String AfterUrlClick = driver.getCurrentUrl();
		if(!beforeUrlClick.equals(AfterUrlClick) ) {
			test.log(Status.PASS, "Page Redirected Successfully so : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Page Not Redirected So : Test Case Fail");
		}
		
		Thread.sleep(1000);
		
		test = extent.createTest("Input_Test");
		WebElement Date_input = driver.findElement(By.className("fromDate"));
		Date_input.click();
		Date_input.clear();
		Date_input.sendKeys("2024-02-25 14:00");
		WebElement L_Date_input = driver.findElement(By.className("toDate"));
		L_Date_input.click();
		L_Date_input.clear();
		L_Date_input.sendKeys("2024-03-01 13:00");
		Thread.sleep(1000);  
		WebElement Apply_input = driver.findElement(By.className("overall_filter_header_css"));
		Apply_input.click();
		
		WebElement bodyElement = driver.findElement(By.tagName("body"));
		String bodyText = bodyElement.getText();
		WebElement Date_input1 = driver.findElement(By.id("logo"));
		Date_input1.click();
		Thread.sleep(1000);
		WebElement bodyElement1 = driver.findElement(By.tagName("body"));
		String bodyText1 = bodyElement1.getText();
		if(!bodyText.equals(bodyText1) ) {
			test.log(Status.PASS, "Data Loaded Successfully : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Data not Loaded : Test Case Fail");
		}
		
		
		Thread.sleep(1000);
		
		test = extent.createTest("TableView_Test");
		
		WebElement bodyElement2 = driver.findElement(By.className("fa-calculator"));
		bodyElement2.click();
		WebElement bodyElement3 = driver.findElement(By.id("machine_text_down"));
		bodyElement3.click();
		WebElement drop_opt_1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[6]/div/div/div[2]/div[1]"));
		drop_opt_1.click();
		Thread.sleep(1000);
		WebElement bodyElement4 = driver.findElement(By.id("part_text_down"));
		bodyElement4.click();
		Thread.sleep(1000);
		WebElement drop_opt_2 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[5]/div/div/div[2]/div[1]"));
		drop_opt_2.click();
		Thread.sleep(1000);
		WebElement bodyElement5 = driver.findElement(By.id("reason_text_down"));
		bodyElement5.click();
		WebElement drop_opt_3 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[4]/div/div/div[2]/div[1]"));
		drop_opt_3.click();
		Thread.sleep(1000);
		WebElement bodyElement6 = driver.findElement(By.id("category_text_down"));
		bodyElement6.click();
		WebElement drop_opt_4 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[3]/div/div/div[2]/div[1]"));
		drop_opt_4.click();
		Thread.sleep(1000);
		WebElement bodyElement7 = driver.findElement(By.id("user_text_down"));
		bodyElement7.click();
		WebElement drop_opt_5 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]"));
		drop_opt_5.click();
		Thread.sleep(1000);
		WebElement applybtn = driver.findElement(By.id("apply_filter_btn"));
		applybtn.click();
		
		
		test = extent.createTest("TableView_Date_Test");
		test = extent.createTest("Input_Test");
		WebElement F_Date_input = driver.findElement(By.className("fromDate"));
		F_Date_input.click();
		F_Date_input.clear();
		F_Date_input.sendKeys("2024-02-25 14:00");
		WebElement La_Date_input = driver.findElement(By.className("toDate"));
		La_Date_input.click();
		La_Date_input.clear();
		La_Date_input.sendKeys("2024-03-01 13:00");
		Thread.sleep(1000);  
		WebElement Apply_input_btn = driver.findElement(By.className("overall_filter_header_css"));
		Apply_input_btn.click();
		
		WebElement b_Element = driver.findElement(By.tagName("body"));
		String b_Element_T = b_Element.getText();
		WebElement Date_input_1 = driver.findElement(By.id("logo"));
		Date_input_1.click();
		Thread.sleep(1000);
		WebElement b_Element_1 = driver.findElement(By.tagName("body"));
		String b_Element_T_1 = b_Element_1.getText();
		if(!b_Element_T.equals(b_Element_T_1) ) {
			test.log(Status.PASS, "Date Fuction Used : Test Case Pass");
		} else {
			test.log(Status.FAIL, "Date Fuction Not Used : Test Case Fail");
		}
	}
	
	

	// ********************** After All Test ********************** //

	// Close the Chrome Window
	@AfterClass
	public void tearDown() {
		if (driver != null) {
            driver.quit(); 
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
