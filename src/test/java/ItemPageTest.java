import groovyjarjarantlr4.v4.tool.LabelElementPair;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import rest.Item;
import rest.ItemAPI;

import java.time.Duration;

public class ItemPageTest {
    private WebDriver chrome;

    @BeforeAll
    static void beforeAll() {
        //This is method will be executed before all tests in the class
        WebDriverManager.chromedriver().setup(); //Download chrome driver and configure it
    }

    @BeforeEach
    void beforeEach() {
        //This method will be executed before each test in the class
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        chrome = new ChromeDriver(options); //Create instance of chrome browser
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
    @DisplayName("Can navigate to Item page via direct navigation")
    void canNavigateToItemPageViaDirectNavigation() {
        login();
        chrome.navigate().to("https://iva-test.inv.bg/objects/manage"); //Direct navigation
        WebElement itemsHeadline = chrome.findElement(By.xpath("//div[@id='headline']//h2"));
        Assertions.assertEquals("Артикули", itemsHeadline.getText()); //Check headline text
        WebElement itemsTable = chrome.findElement(By.id("fakturi_table"));
        Assertions.assertTrue(itemsTable.isDisplayed(), "Items table is not displayed");
    }

    @Test
    @DisplayName("Can search for not-existing items")
    void canSearchForNotExistingItemsByName() {
        login();
        chrome.navigate().to("https://iva-test.inv.bg/objects/manage");
        //Expand search
        WebElement expandSearchButton = chrome.findElement(By.id("searchbtn"));
        expandSearchButton.click();
        //Populate item name
        WebElement nameField = chrome.findElement(By.name("nm"));
        nameField.clear();
        nameField.sendKeys("Coffee");
        //Trigger search
        WebElement triggerSearchButton = chrome.findElement(By.name("s"));
        System.out.println("The button size is: " + triggerSearchButton.getSize().height);
        Assertions.assertEquals(24, triggerSearchButton.getSize().height);
        triggerSearchButton.click();
        //Check that the no item is found
        WebElement noItemFoundMessage = chrome.findElement(By.id("emptylist"));
        Assertions.assertEquals("Не са намерени артикули, отговарящи на зададените критерии.", noItemFoundMessage.getText());

    }

    @Test
    @DisplayName("Can search for existing item")
    void canSearchForExistingItem(){
        //Clean all items from the system API
        ItemAPI.deleteAllItems();
        //Create item via API
        String itemName = "Coffee";
        Item coffee = new Item(itemName, "kg.", 10.24);
        ItemAPI.createItem(coffee);
        //Create second item
        coffee.name = "Different item";
        ItemAPI.createItem(coffee);
        login();
        chrome.navigate().to("https://iva-test.inv.bg/objects/manage");
        //Expand search
        WebElement expandSearchButton = chrome.findElement(By.id("searchbtn"));
        expandSearchButton.click();
        //Populate item name
        WebElement nameField = chrome.findElement(By.name("nm"));
        nameField.clear();
        nameField.sendKeys(itemName); //Search the item created via API by name
        //Trigger search
        WebElement triggerSearchButton = chrome.findElement(By.name("s"));
        triggerSearchButton.click();
        //Check that the item is found and the item is only one
        WebElement table = chrome.findElement(By.id("fakturi_table"));
        System.out.println(table.getText());
        Assertions.assertTrue(table.getText().contains(itemName), "Could not find the item name in the text");
    }


    @Test
    @DisplayName("")
    @Tag("homework")
    void systemDisplayCorrectMessageWhenNoItemsExist(){
        //Delete all items via API
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
        WebElement homePageHeadline;
        homePageHeadline = chrome.findElement(By.ByXPath("//div[@id='headline']//h2"));
        Assertions.assertEquals("iva_angelowa@abv.bg", userPanel.getText()));
        // Logout
        WebElement logoutLink = chrome.findElement(By.cssSelector("a.selenium-button-logout"));
        logoutLink.click();
        //Check success message
        WebElement successMessage = chrome.findElement(By.cssSelector("#okmsg"));
        Assertions.assertEquals("Вие излязохте от акаунта си. ", successMessage.getText());
        //Check user is at the Login page (redirect)
        heading1 = chrome.findElement(By.xpath("//h1")); //Rediscover of the element in the dom
        Assertions.assertEquals("Вход в inv.bg", heading1.getText(), "Default text is different");
        //Login
        //Navigate to Item page
        WebElement topmenuontabbutton = chrome.findElement(By.cssSelector("item"));
        topmenuontabbutton.click();
        WebElement SelectAllRowscheckbox = chrome.findElement(By.cssSelector("fakturi-table"));
        WebElement itemsTable = chrome.findElement(By.id("fakturi_table"));
        Assertions.assertTrue(itemsTable.isDisplayed(), "Items table is displayed");
        SelectAllRowscheckbox.click();



        //Check correct message is displayed
    }


    @Test
    @DisplayName("Can search items by price (from - to)")
    @Tag("homework")
    void canSearchForItemByPriceFromTo(){
        //Delete all items
        //Create few items with different prices (you can create two items using the same item object)
        //Login
        //Navigate to Items page
        //Expand search form
        //Search for items by price from to
        //WebElement priceFromField = chrome.findElementBy(By.name("pr1"));
        //WebElement priceToField = chrome.findElementBy(By.name("pr2"));
        //Check whether table contains only valid item names

    }






    




    private void login() {
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
}
