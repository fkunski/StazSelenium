import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class bookStoreSiteTests {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        options.addArguments("--disable-search-engine-choice-screen");
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    @Test
    public void cart_not_changed_should_update_button_disabled(){
        driver.get("http://localhost:8080/product/the-elements-of-qualitative-chemical-analysis-vol-1-parts-1-and-2-by-stieglitz/");
        driver.findElement(By.name("add-to-cart")).click();
        driver.get("http://localhost:8080/cart");
        WebElement quantityField = driver.findElement(By.className("qty"));

        WebElement updateButton = driver.findElement(By.name("update_cart"));
        Assertions.assertFalse(updateButton.isEnabled(),"Update button is enabled while it shouldn't. There were no changes in cart.");
    }
    @Test
    public void update_quantity_in_cart_should_update_total_price(){
        String url = "http://localhost:8080/product/the-elements-of-qualitative-chemical-analysis-vol-1-parts-1-and-2-by-stieglitz/";
        driver.get(url);
        driver.findElement(By.name("add-to-cart")).click();
        driver.get("http://localhost:8080/cart");
        WebElement quantityField = driver.findElement(By.className("qty"));
        quantityField.clear();
        quantityField.sendKeys("2");
        driver.findElement(By.name("update_cart")).click();
        //wait.until(ExpectedConditions.numberOfElementsToBe(By.className("blockUI"),0));
        wait.until(driver -> driver.findElements(By.className("blockUI")).size()==0);

        WebElement total = driver.findElement(By.className("order-total"));
        Assertions.assertEquals("Total 28,00 €",total.getText(),"Total price is not correct");
    }
    @Test
    public  void add_product_to_cart_should_show_header_price(){
        String url = "http://localhost:8080/product/a-popular-history-of-astronomy-during-the-nineteenth-century-by-agnes-m-clerke/";
        driver.get(url);
        WebElement addToCartButton = driver.findElement(By.name("add-to-cart"));
        addToCartButton.click();
        WebElement miniCartAmount = driver.findElement(By.className("wc-block-mini-cart__amount"));
        Assertions.assertEquals("12,00 €",miniCartAmount.getText(),
                "The price displayed in header is not correct");

    }
    @Test
    public  void add_product_to_cart_should_right_panel_with_price(){
        String url = "http://localhost:8080/product/a-popular-history-of-astronomy-during-the-nineteenth-century-by-agnes-m-clerke/";
        driver.get(url);
        WebElement addToCartButton = driver.findElement(By.name("add-to-cart"));
        addToCartButton.click();
        WebElement minicartButton = driver.findElement(By.className("wc-block-mini-cart__button"));
        minicartButton.click();
        //WebElement totalPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("wc-block-components-totals-item__value")));
        WebElement totalPrice = wait.until(driver -> driver.findElement(By.className("wc-block-components-totals-item__value")));
        Assertions.assertEquals("12,00 €",totalPrice.getText(),
                "The price displayed in minicart is not correct");
    }
    @Test
    public void admin_successful_login_should_display_my_account_content(){
        String url = "http://localhost:8080/my-account";
        driver.get(url);
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys("admin");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("admin");
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();
        Assertions.assertDoesNotThrow(()->driver.findElement(By.className("woocommerce-MyAccount-content")),"The my account content is missing. User probably is not logged in.");
    }
    @Test
    public void product_virtual_should_not_show_shipping(){
        driver.get("http://localhost:8080/my-account");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.get("http://localhost:8080/wp-admin/post-new.php?post_type=product");
        driver.findElement(By.id("_virtual")).click();
        WebElement shippingOptions = driver.findElement(By.className("shipping_options"));
        Assertions.assertFalse(shippingOptions.isDisplayed(),"Shipping tab is still visible.");

    }
    @Test
    public void select_all_posts_should_select_each_of_them(){
        driver.get("http://localhost:8080/my-account");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.get("http://localhost:8080/wp-admin/edit.php?post_type=product");
        driver.findElement(By.id("cb-select-all-1")).click();
        List<WebElement> productCheckboxes = driver.findElements(By.name("post[]"));
        long numberOfSelectedCheckboxes = productCheckboxes.stream().filter(checkbox -> checkbox.isSelected()).count();
        Assertions.assertEquals(7,numberOfSelectedCheckboxes,"Not all product checkboxes are selected.");
    }
    @Test
    public void search_field_should_have_placeholder(){
        driver.get("http://localhost:8080/");
        WebElement searchField = driver.findElement(By.id("wc-block-search__input-1"));
        Assertions.assertEquals("Search products…", searchField.getDomAttribute("placeholder"),"Placeholder for search field is not correct.");
    }
    @Test
    public void new_product_quantity_typed_in_should_changed_product_quantity(){
        driver.get("http://localhost:8080/product/the-elements-of-qualitative-chemical-analysis-vol-1-parts-1-and-2-by-stieglitz/");
        WebElement productQuantity = driver.findElement(By.className("qty"));
        productQuantity.clear();
        productQuantity.sendKeys("3");
        Assertions.assertEquals("3", productQuantity.getDomProperty("value"),"Product quantity not changed.");

    }
    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
