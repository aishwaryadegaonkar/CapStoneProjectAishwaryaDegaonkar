package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmazonLoginSteps {

    private WebDriver driver;
    private String username;
    private String password;

    @Before
    public void setup() {
        // Any setup steps can go here
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I launch the {string} browser")
    public void i_launch_the_browser(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":   
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
    }

    @Given("I navigate to the Amazon login page")
    public void i_navigate_to_the_amazon_login_page() {
        driver.get("https://www.amazon.in/");
        WebElement signInButton = driver.findElement(By.id("nav-link-accountList"));
        signInButton.click();
    }

    @When("I enter the username from the Excel file")
    public void i_enter_the_username_from_the_excel_file() {
        readCredentialsFromExcel();
        WebElement emailField = driver.findElement(By.id("ap_email"));
        emailField.sendKeys(username);
    }

    @When("I enter the password from the Excel file")
    public void i_enter_the_password_from_the_excel_file() {
        WebElement passwordField = driver.findElement(By.id("ap_password"));
        passwordField.sendKeys(password);
    }

    @When("I click the Sign-In button")
    public void i_click_the_sign_in_button() {
        WebElement submitButton = driver.findElement(By.id("signInSubmit"));
        submitButton.click();
    }

    @Then("I should see the user account name on the homepage")
    public void i_should_see_the_user_account_name_on_the_homepage() {
        WebElement accountName = driver.findElement(By.id("nav-link-accountList-nav-line-1"));
        assertTrue(accountName.getText().contains("Hello,"));
    }

    // Utility method to read credentials from an Excel file
    private void readCredentialsFromExcel() {
        try (FileInputStream fis = new FileInputStream(new File("src/test/resources/data/amazon_credentials.xlsx"));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(1);  // Assuming first row contains the headers
            this.username = row.getCell(0).getStringCellValue();
            this.password = row.getCell(1).getStringCellValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
