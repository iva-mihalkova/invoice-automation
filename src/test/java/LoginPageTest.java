import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class LoginPageTest {
    private WebDriver chrome;

    @BeforeAll
    static void beforeAll() {
        //This is method will be executed before all tests in the class
        WebDriverManager.chromedriver().setup(); //Download chrome driver and configure it
    }

    @BeforeEach
    void beforeEach() {
        //This method will be executed before each test in the class
        chrome = new ChromeDriver(); //Create instance of chrome browser
        //chrome.manage().window().maximize(); //maximizes the browser window
        chrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); //wait for elements to appear
        chrome.navigate().to("https://iva-test.inv.bg"); //Navigate to login page
    }

    @AfterEach
    void afterEach() {
        //This method will be executed after each test in the class
        chrome.quit();
    }


    @Test
    @Tag("ui")
    @DisplayName("Can login with valid credentials")
    void canLoginWithValidCredentials() {
        //Navigate to Login page
        WebElement heading1 = chrome.findElement(By.xpath("//h1"));
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
        //Enter email
        WebElement emailField = chrome.findElement(By.id("loginusername")); //locate element in the dom
        emailField.clear(); //Clear the text in the field
        emailField.sendKeys("iva_angelowa@abv.bg"); //type text in the field
        //Enter password
        WebElement passwordField = chrome.findElement(By.name("password"));
        passwordField.sendKeys("123456"); //type text in the field
        //Click Login button
        WebElement loginButton = chrome.findElement(By.cssSelector("input.selenium-submit-button"));
        loginButton.click(); //Clicks Login button
        //Check the homepage is loaded
        WebElement homePageHeadline = chrome.findElement(By.xpath("//div[@id='headline']//h2"));
        Assertions.assertEquals("Система за фактуриране", homePageHeadline.getText());
        //Check that user logged in (email is displayed at the top right)
        WebElement userPanel = chrome.findElement(By.cssSelector("div.userpanel-header"));
        Assertions.assertEquals("iva_angelowa@abv.bg", userPanel.getText());
    }


    @Test
    @Tag("ui")
    @DisplayName("Cant login with invalid credentials")
    void cantLoginWithInvalidCredentials() {
        //Navigate to Login page
        WebElement heading1 = chrome.findElement(By.xpath("//h1"));
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
        //Enter email
        WebElement emailField = chrome.findElement(By.id("loginusername")); //locate element in the dom
        emailField.clear(); //Clear the text in the field
        emailField.sendKeys("iva_angelowa@abv.bg"); //type text in the field
        //Enter password
        WebElement passwordField = chrome.findElement(By.name("password"));
        passwordField.sendKeys("12345678"); //type text in the field
        //Click Login button
        WebElement loginButton = chrome.findElement(By.cssSelector("input.selenium-submit-button"));
        loginButton.click(); //Clicks Login button
        //Check error message
        WebElement loginError = chrome.findElement(By.cssSelector("div.selenium-error-block"));
        Assertions.assertEquals("Грешно потребителско име или парола. Моля, опитайте отново.  ", loginError.getText());
    }

    @Test
    @Tag("ui")
    @DisplayName("Cant login with blank credentials")
    void cantLoginWithBlankCredentials() {
        //Navigate to Login page
        WebElement heading1 = chrome.findElement(By.xpath("//h1"));
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
        //Click Login button
        WebElement loginButton = chrome.findElement(By.cssSelector("input.selenium-submit-button"));
        loginButton.click(); //Clicks Login button
        //Check error message
        WebElement loginError = chrome.findElement(By.cssSelector("div.selenium-error-block"));
        Assertions.assertEquals("Моля, попълнете вашия email  ", loginError.getText());
    }

    @Test
    @Tag("ui")
    @DisplayName("Can login with valid credentials and logout")
    void canLoginWithValidCredentialsAndLogout() {
        //Navigate to Login page
        WebElement heading1 = chrome.findElement(By.xpath("//h1"));
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
        //Enter email
        WebElement emailField = chrome.findElement(By.id("loginusername")); //locate element in the dom
        emailField.clear(); //Clear the text in the field
        emailField.sendKeys("iva.angelowa@abv.bg"); //type text in the field
        //Enter password
        WebElement passwordField = chrome.findElement(By.name("password"));
        passwordField.sendKeys("123456"); //type text in the field
        //Click Login button
        WebElement loginButton = chrome.findElement(By.cssSelector("input.selenium-submit-button"));
        loginButton.click(); //Clicks Login button
        //Check the homepage is loaded
        WebElement homePageHeadline = chrome.findElement(By.xpath("//div[@id='headline']//h2"));
        Assertions.assertEquals("Система за фактуриране", homePageHeadline.getText());
        //Check that user logged in (email is displayed at the top right)
        WebElement userPanel = chrome.findElement(By.cssSelector("div.userpanel-header"));
        Assertions.assertEquals("iva_angelowa@abv.bg", userPanel.getText());
        // Logout
        userPanel.click();
        WebElement logoutLink = chrome.findElement(By.cssSelector("a.selenium-button-logout"));
        logoutLink.click();
        //Check success message
        WebElement successMessage = chrome.findElement(By.cssSelector("#okmsg"));
        Assertions.assertEquals("Вие излязохте от акаунта си. ", successMessage.getText());
        //Check user is at the Login page (redirect)
        heading1 = chrome.findElement(By.xpath("//h1")); //Rediscover of the element in the dom
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
    }
}
