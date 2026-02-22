package pl.wampirok.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    private final String BASE_URL = "https://demowebshop.tricentis.com";

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.setAcceptInsecureCerts(true);


        driver = new ChromeDriver(options);
        driver.manage().window().maximize();



    }

    @Test
    public void registerAndLoginTest() {

        driver.get(BASE_URL+"/register");

        String email = "test" + System.currentTimeMillis() + "@gmail.com";

        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Test");
        driver.findElement(By.id("LastName")).sendKeys("Testing");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys("Test123!");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("Test123!");
        driver.findElement(By.id("register-button")).click();

        driver.findElement(By.className("ico-logout")).click();

        driver.get(BASE_URL+"/login");

        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys("Test123!");
        driver.findElement(By.cssSelector("input.login-button")).click();

        Assert.assertTrue(driver.findElement(By.className("ico-logout")).isDisplayed());
    }

    @Test
    public void loginWithInvalidCredentials() {

        driver.get(BASE_URL+"/login");

        driver.findElement(By.id("Email")).sendKeys("wrongemail@test.com");

        driver.findElement(By.id("Password")).sendKeys("WrongPassword123");

        driver.findElement(By.cssSelector("input.login-button")).click();

        String errorMessage = driver.findElement(By.className("validation-summary-errors")).getText();

        Assert.assertTrue(errorMessage.contains("Login was unsuccessful"),
                "Error message was not displayed for invalid login");
    }

    @Test
    public void addProductToCart() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


        driver.get(BASE_URL + "/register");

        String email = "test" + System.currentTimeMillis() + "@gmail.com";
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Test");
        driver.findElement(By.id("LastName")).sendKeys("Testing");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys("Test123!");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("Test123!");
        driver.findElement(By.id("register-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ico-logout")));

        driver.findElement(By.linkText("Books")).click();
        String productName = driver.findElement(By.cssSelector(".product-item .product-title a")).getText();
        driver.findElement(By.cssSelector(".product-item .product-title a")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Add to cart']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.success")));
        driver.findElement(By.className("ico-cart")).click();

        String productInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("td.product a"))).getText();

        Assert.assertEquals(productInCart, productName, "Product in cart does not match selected product");
    }



    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}
