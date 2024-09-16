import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MattermostTests {
    WebDriver driver;
    @BeforeEach
    public void setup() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void choosing_no_account_should_show_no_access() {
        driver.get("http://localhost:8065/login/");
        driver.findElement(By.className("btn-tertiary")).click();
        driver.findElement(By.className("alternate-link__link")).click();
        Assertions.assertEquals("http://localhost:8065/access_problem",
                driver.getCurrentUrl(), "User wasn't redirected to Access Problem page.");
    }
    @Test
    public void successful_login_should_show_town_square_page() {
        driver.get("http://localhost:8065/login/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("btn-tertiary"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input_loginId"))).sendKeys("polpo");
        driver.findElement(By.id("input_password-input")).sendKeys("polpopolpo");
        driver.findElement(By.id("saveSetting")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.id("loadingSpinner"), 0));
        Assertions.assertEquals("http://localhost:8065/team/channels/town-square",
                driver.getCurrentUrl(), "User is not on Town Square page.");
    }
    @Test
    public void no_changes_made_should_save_button_disabled() {
        driver.get("http://localhost:8065/login/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("btn-tertiary"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input_loginId"))).sendKeys("polpo");
        driver.findElement(By.id("input_password-input")).sendKeys("polpopolpo");
        driver.findElement(By.id("saveSetting")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.id("loadingSpinner"), 0));
        driver.get("http://localhost:8065/admin_console/user_management/permissions/system_scheme");
        WebElement addTeamMembersCheckbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("saveSetting")));
        Assertions.assertFalse(addTeamMembersCheckbox.isEnabled(),
                "\"Save\" button is enabled even though no changes were made");
    }

}