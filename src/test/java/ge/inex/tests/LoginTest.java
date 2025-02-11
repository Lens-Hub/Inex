package ge.inex.tests;

import ge.inex.pages.BasePage;
import ge.inex.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import ge.inex.utils.ExtentManager;
import com.aventstack.extentreports.ExtentTest;




public class LoginTest extends ge.inex.runner.TestRunner {  // მემკვიდრეობით იღებს TestRunner-ს

    private LoginPage loginPage;

    @Test
    public void testInvalidLogin() {
        driver.get("https://www.inexi.ge/en/login");

        LoginPage loginPage = new LoginPage(driver);

        // ხელახლა გამოვიძახოთ პოპაპის გათიშვა, რადგან შესაძლოა ისევ გამოჩნდეს ლოგინ გვერდზე გადასვლისას
        loginPage.closePopupIfExists();

        loginPage.enterEmail("danelia.elena@yahoo.com");
        loginPage.enterPassword("123456");
        loginPage.clickLoginButton();

        // ვიპოვოთ შეცდომის ტექსტის ელემენტი და შეავამოწმოთ ტექსტი
        WebElement errorLabel = driver.findElement(By.xpath("//label[@class='error' and @for='password']"));

        String actualErrorMessage = errorLabel.getText();
        String expectedErrorMessage = "Invalid password";

        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message does not match!");
    }


    @Test
    public void testForgotPasswordColor() {
        driver.get("https://www.inexi.ge/en/login"); // ლოგინ გვერდზე გადასვლა

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // დაველოდოთ, სანამ "Forgot password?" ბმული გამოჩნდება
        WebElement forgotPasswordLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='psw']/a")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", forgotPasswordLink);

        // ფერის წამოღება და შემწმება
        String actualColor = forgotPasswordLink.getCssValue("color");
        String expectedColor = "rgba(255, 63, 63, 1)"; // #FF3F3F

        Assert.assertEquals(actualColor, expectedColor, "Forgot password link color is incorrect!");
    }


    @Test
    public void testFlightsPageNavigation() {
        WebElement flightsLink = driver.findElement(By.linkText("Flights"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", flightsLink);

        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, "https://www.inexi.ge/en/flights");
    }


    @Test
    public void testContactInfo() {
        String phoneNumber = driver.findElement(By.className("phoneNumber")).getText();
        String email = driver.findElement(By.className("email")).getText();
        // ნამდვილად ეს ინფორმაცია წერია თუ არა გვერდზე
        Assert.assertEquals(phoneNumber, "CALL NOW: (+995 32) 249 26 26");
        Assert.assertEquals(email.toLowerCase(), "email: info@inex.ge");
    }

    @Test
    public void testLanguageChangeToGeorgian() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // ლოფინის 5 წამის
        WebElement georgianLanguageButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'languageKa')]"))
        );
        // ტარგმნა და გვერდის შემოწმება ნამდვილად ქართილზე გადაითარგმნა თუ არა
        georgianLanguageButton.click();

        String expectedUrl = "https://www.inexi.ge/ka";
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "The language change to Georgian did not succeed!");
    }


    @Test
    public void testSellAllOfficesFilter() {
        ExtentTest test = ExtentManager.createTest("Sell ALL Offices Filter Test");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        BasePage basePage = new BasePage(driver);
        basePage.closePopupIfExists();


        try {
            //  "Offices" ღილაკზე დაჭერა
            WebElement officesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='navBar ' and contains(@href, '/en/offices')]")

            ));
            officesButton.click();
            test.info("'Offices' button clicked.");

            wait.until(ExpectedConditions.urlToBe("https://www.inexi.ge/en/offices"));
            test.pass("Navigated to Offices page.");

            //  პოპაპის გათიშვა
            basePage.closePopupIfExists();
            test.info("Checked and closed popup if existed.");

            //  "Tbilisi" შეყვანა
            WebElement cityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
            cityInput.sendKeys("Tbilisi");
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@id='city']/following-sibling::*[name()='svg']")

            ));
            searchButton.click();
            test.info("Entered 'Tbilisi' and clicked search button.");

            // ვამოწმებთ, რომ მხოლოდ "Tbilisi"-ს მისამართები ჩანს
            wait.until(ExpectedConditions.urlContains("city=Tbilisi"));
            List<WebElement> cityLabels = driver.findElements(By.xpath("//div[contains(@class, 'addressInfo')]"));

            for (WebElement label : cityLabels) {
                Assert.assertEquals(label.getText().trim(), "TBILISI", "Unexpected city found!");
            }

            test.pass("All displayed offices belong to 'Tbilisi'. Test successful.");
        } catch (Exception e) {
            test.fail("Test failed due to: " + e.getMessage());
        } finally {
            ExtentManager.flushReports();
        }
    }



}


