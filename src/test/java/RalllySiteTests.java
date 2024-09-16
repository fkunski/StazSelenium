import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class RalllySiteTests {
    WebDriver driver;
    @BeforeEach
    public void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        driver = new ChromeDriver(options);
    }
    @Test
    public void newMeetingForm(){
        String url = "http://localhost:3000/new";
        driver.get(url);
        Assertions.assertEquals(url, driver.getCurrentUrl(),"You're not on the correct site" );

        WebElement title = driver.findElement(By.id("title"));
        title.sendKeys("Comiesięczne spotkanie");
        WebElement location = driver.findElement(By.id("location"));
        location.sendKeys("Comiesięczne spotkanie");
        WebElement description = driver.findElement(By.id("description"));
        description.sendKeys("Comiesięczne spotkanie");
        WebElement button = driver.findElement(By.className("btn-primary"));
        button.click();
        String expectedEtap = "Etap 2 z 3";
        String actualEtap = driver.findElement(By.className("tracking-tight")).getText();
        Assertions.assertEquals(expectedEtap, actualEtap,"That's not what I expected it");
    }
    @Test
    public void placeholderTest(){
        driver.get("http://localhost:3000/new");
        WebElement titleField = driver.findElement(By.id("title"));
        Assertions.assertEquals("Miesięczne spotkanie", titleField.getDomAttribute("placeholder"),"Placeholder has different value than expected.");
        WebElement locationField = driver.findElement(By.id("location"));
        Assertions.assertEquals("Sklep z kawą Joe", locationField.getDomAttribute("placeholder"),"Placeholder has different value than expected.");
        WebElement descriptionField = driver.findElement(By.id("description"));
        Assertions.assertEquals("Cześć wszystkim, wybierzcie terminy, które Wam pasują!", descriptionField.getDomAttribute("placeholder"),"Placeholder has different value than expected.");

    }

    @AfterEach
    public void driverQuit(){
    driver.quit();
    }

}
//(xpath = "//input[@id='username']")