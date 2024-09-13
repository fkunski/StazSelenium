import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class bookStoreSiteTests {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        options.addArguments("--disable-search-engine-choice-screen");
    }
    @Test
    public  void addToCartTest(){
        String url = "http://localhost:8080/product/a-popular-history-of-astronomy-during-the-nineteenth-century-by-agnes-m-clerke/";
        driver.get(url);
        WebElement addToCartButton = driver.findElement(By.name("add-to-cart"));
        addToCartButton.click();
        WebElement miniCartAmount = driver.findElement(By.className("wc-block-mini-cart__amount"));
        Assertions.assertEquals("12,00 €",miniCartAmount.getText(),"The price displayed in minicart is not correct");
    }
    @Test
    public void loginTest(){
        String url = "http://localhost:8080/my-account";
        driver.get(url);
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("admin");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("admin");
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();
        Assertions.assertDoesNotThrow(()->driver.findElement(By.className("woocommerce-MyAccount-content")),"The my account content is missing. User probably is not logged in.");


    }
    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
