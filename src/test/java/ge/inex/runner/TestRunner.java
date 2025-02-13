package ge.inex.runner;

import ge.inex.pages.BasePage;
import ge.inex.utils.ConfigReader;
import ge.inex.utils.ExtentManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class TestRunner {
    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();

        //  ელოდება
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.getProperty("WAIT_TIME"))
        ));
        driver.manage().window().maximize();

        //  URL-ის წამოღება config-დან
        String baseUrl = ConfigReader.getProperty("BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RuntimeException("Base URL is missing in config.properties");
        }
        driver.get(baseUrl);

        //  ყველა ტესტამდე გათიშოს პოპაპი
        BasePage basePage = new BasePage(driver);
        basePage.closePopupIfExists();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //  ყველა ტესტის დასრულების შემდეგ ExtentReports-ის flush()
    @AfterSuite
    public void tearDownExtentReports() {
        ExtentManager.flushReports();
    }

}
