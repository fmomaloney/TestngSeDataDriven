package com.hashmap.java;
// utilities
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
// log and json
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
// selenium
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
// testng
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

public class TestNgHashmapTest {
	// maps are global to the class so I can share between methods
	private Map<String, Map<String, String>> jsonData;
	private Map<String, String> innerMap;
	private WebDriver driver;
	// Initialize the logger
    private static final Logger logger = LogManager.getLogger(TestNgHashmapTest.class);
	
  @Test
  public void testWebshopRegistration() {
	  
	  logger.info("OOOOO  starting @test methods  OOOOO"); 
	  /* registerTestManual();
	     registerJsonData("Joey","Burton","yahoo.com","123456","123456"); */
	  
	  // Iterate over the mapped jsonData to run tests 
	  for (Map.Entry<String, Map<String, String>> entry : jsonData.entrySet()) { 
		  String testCase = entry.getKey(); 
		  Map<String, String> innerMap = entry.getValue(); 
		  logger.info("Running test case: " + testCase); 
		  String regEmail = innerMap.get("RegEmail");
		  String regPwd = innerMap.get("RegPwd");
		  String regConfirm = innerMap.get("PwdConfirm");
		  String firstName = innerMap.get("FirstName");
		  String lastName = innerMap.get("LastName");
		  logger.info("grabbing values from map: {},{},{},{},{}.",firstName,lastName,regEmail,regPwd,regConfirm);
		  // call register method with variables
		  registerJsonData(firstName,lastName,regEmail,regPwd,regConfirm);
		  // now call a validate method
		  List<String> failures = assertRegisterSuccess(testCase);
		  if (!failures.isEmpty()) { 
			  logger.warn("Failures: " + String.join(": ", failures)); 
			  } 
		  // reopen to registration page after testCase
		  driver.get("https://demowebshop.tricentis.com/Register/");

		/* Show the inner map data
	  	for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) { 
	  		String field = innerEntry.getKey(); 
	  		String value = innerEntry.getValue(); 
	  		logger.info("field = " + field + " value = " + value); 
	  	 } */
	  }
  }
  
  @BeforeTest
  public void beforeMyTest() {
	  logger.info("##### before tests, fill data map #####");
	  // first fill the map with JSON data
	  try {
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader("MyData\\NestedTestData.json");
			Object jsonObj = parser.parse(reader);
			JSONObject myJson = (JSONObject) jsonObj;
			
			// Add data to an appropriate map
			jsonData = new HashMap<>();
			// Iterate through outer objects (test cases)
	         for (Object key : myJson.keySet()) {
	             String testCase = (String) key;
	             JSONObject properties = (JSONObject) myJson.get(testCase);
	             logger.info("populating data for test " + testCase + "!" );

	             // Convert test string-string properties to an inner map
	             innerMap = new HashMap<>();
	             for (Object innerKey : properties.keySet()) {
	                 String innerKeyStr = (String) innerKey;
	                 String value = (String) properties.get(innerKey);
	                 innerMap.put(innerKeyStr, value);
	                 logger.info("key" + innerKeyStr +  " = " + value +"!");
	             }
	             // Add the inner map to the test case map
	             jsonData.put(testCase, innerMap);
	         }
	             
	         } catch (ParseException e) {
	             e.printStackTrace();
	               logger.error("parsing error occurred " + e);
	    	 } catch (Exception e ) {
	               logger.error("unexpected error occurred " + e);
	    	 }
	         logger.info("done with the map");
	         //logger.info(jsonData);
	         
	         // set up the webdriver initially
	         logger.info("set up the webdriver now");
	         driver = new ChromeDriver();      
  }

  @BeforeMethod
  public void beforeMethod() {
  
	  logger.info("----- before a method? ------");
	  // go to the register page before each test. might need to just call this from @test in loop.
	  driver.get("https://demowebshop.tricentis.com/Register/");
	  //driver.findElement(By.linkText("Register")).click();
	  //driver.manage().window().minimize();
  }
  
  @AfterTest
  public void afterMyTest() {
	  
	  logger.info("starting @after methods");
	  //driver.close(); 
  }

    // these are class methods called from @test, no annotations here 
  
  	  /*
      private void registerTestManual() {
    	// first do a totally manual test
    	// create a random number for the email
        Random randomNum = new Random(); 
    	int randomSuffix = 1000 + randomNum.nextInt(9000);
    	// click radio button <input id="gender-male" name="Gender" type="radio" value="M">
	    WebElement myRadio = driver.findElement(By.id("gender-male"));
	    myRadio.click();
	    // enter first and last names
	    driver.findElement(By.name("FirstName")).sendKeys("Frank");
	    driver.findElement(By.id("LastName")).sendKeys("Maloney");
	    // enter a random email so we can repeat tests
	    String myManualEmail = "fmaloney" + String.valueOf(randomSuffix) + "@berfo.com";
	    driver.findElement(By.id("Email")).sendKeys(myManualEmail);
	    // enter pwd twice, 6 chars
	    WebElement pwd = driver.findElement(By.id("Password"));
		pwd.sendKeys("123412");
	    WebElement confirm = driver.findElement(By.id("ConfirmPassword"));
	    //WebElement confirm = driver.findElement(By.cssSelector("div > p"));
		confirm.sendKeys("123412");
		// click the submit button <input type="submit" id="register-button" value="Register" name="register-button">
		WebElement login = driver.findElement(By.id("register-button"));
		login.click();
	    logger.info("registered a user " + myManualEmail + " manually");    
      } */
  
      private void registerJsonData(String myFirst, String myLast, String myEmail, String myPwd, String myConfirm) {
    	// this method populates the registration form with JSON data
    	// create a random number to dedupe the email address so I can rerun tests
    	Random randomNum = new Random(); 
        int randomSuffix = 1000 + randomNum.nextInt(9000);
	    String regEmail = myFirst.toLowerCase() + myLast.toLowerCase() + String.valueOf(randomSuffix) + "@" + myEmail;
        // fill in the registration form
        driver.findElement(By.id("gender-male")).click();
	    driver.findElement(By.name("FirstName")).sendKeys(myFirst);
	    driver.findElement(By.id("LastName")).sendKeys(myLast);
	    driver.findElement(By.id("Email")).sendKeys(regEmail);
	    logger.info("register user {} {} at {}.",myFirst,myLast,regEmail);
	    driver.findElement(By.id("Password")).sendKeys(myPwd); 
	    driver.findElement(By.id("ConfirmPassword")).sendKeys(myConfirm);
	    driver.findElement(By.id("register-button")).click();
      }
      
      private List<String> assertRegisterSuccess(String testCase) {
    	  // not sure I really need a list the way I am calling this...
    	  List<String> failures = new ArrayList<>();
    	  // success message seen at https://demowebshop.tricentis.com/registerresult/1
    	  Boolean success = driver.getPageSource().contains("Your registration completed");
    	  String success1 = String.valueOf(success);
    	  logger.info("test result for {} is {}!",testCase,success1);
    	  try {
    		Assert.assertTrue(success.booleanValue());
    		  
    	  } catch (AssertionError e){
    		  failures.add(e.getMessage());
    	  }
    	  return failures;
      }
}
