package pl.wampirok.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest {

    WebDriver driver;
    private final String BASE_URL = "https://demowebshop.tricentis.com";

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
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

        Assert.assertTrue(
                driver.findElement(By.className("ico-logout")).isDisplayed()
        );
    }

    @Test
    public void loginWithInvalidCredentials() {

        driver.get(BASE_URL+"/login");

        driver.findElement(By.id("Email"))
                .sendKeys("wrongemail@test.com");

        driver.findElement(By.id("Password"))
                .sendKeys("WrongPassword123");

        driver.findElement(By.cssSelector("input.login-button"))
                .click();

        String errorMessage = driver.findElement(By.className("validation-summary-errors"))
                .getText();

        Assert.assertTrue(
                errorMessage.contains("Login was unsuccessful"),
                "Error message was not displayed for invalid login"
        );
    }


    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}
