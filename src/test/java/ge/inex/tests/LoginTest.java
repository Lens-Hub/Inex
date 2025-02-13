package ge.inex.tests;

import ge.inex.pages.BasePage;
import ge.inex.pages.LoginPage;
import ge.inex.utils.ExtentManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.List;

public class LoginTest extends ge.inex.runner.TestRunner {  // მემკვიდრეობით იღებს TestRunner-ს

    private LoginPage loginPage;

    @Test
    public void testInvalidLogin() {
        ExtentTest test = ExtentManager.createTest("Invalid Login Test");

        driver.get("https://www.inexi.ge/en/login");

        LoginPage loginPage = new LoginPage(driver);

        // ხელახლა გამოვიძახოთ პოპაპის გათიშვა, რადგან შესაძლოა ისევ გამოჩნდეს ლოგინ გვერდზე გადასვლისას
        loginPage.closePopupIfExists();

        // ვშლით და ვწერთ არასწორ იმეილსა და პაროლს
        loginPage.enterEmail("danelia.elena@yahoo.com");
        loginPage.enterPassword("123456");
        loginPage.clickLoginButton();
        test.info("Entered invalid credentials and clicked login.");

        try {
            // ვიპოვოთ შეცდომის ტექსტის ელემენტი და შევამოწმოთ ტექსტი
            WebElement errorLabel = driver.findElement(By.xpath("//label[@class='error' and @for='password']"));
            String actualErrorMessage = errorLabel.getText();
            String expectedErrorMessage = "Invalid password";

            Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message does not match!");
            test.pass("Error message validated successfully.");
        } catch (Exception e) {
            test.fail("Error message validation failed: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    @Test
    public void testForgotPasswordColor() {
        ExtentTest test = ExtentManager.createTest("Forgot Password Color Test");

        driver.get("https://www.inexi.ge/en/login"); // ლოგინ გვერდზე გადასვლა
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // დაველოდოთ, სანამ "Forgot password?" ბმული გამოჩნდება
            WebElement forgotPasswordLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[@class='psw']/a")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", forgotPasswordLink);

            // ვამოწმებთ, ფერი სწორია თუ არა
            String actualColor = forgotPasswordLink.getCssValue("color");
            String expectedColor = "rgba(255, 63, 63, 1)"; // #FF3F3F

            Assert.assertEquals(actualColor, expectedColor, "Forgot password link color is incorrect!");
            test.pass("Forgot password link color is correct.");
        } catch (Exception e) {
            test.fail("Failed to verify forgot password link color: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    @Test
    public void testFlightsPageNavigation() {
        ExtentTest test = ExtentManager.createTest("Flights Page Navigation Test");

        try {
            // "Flights" ღილაკზე დაჭერის სიმულაცია
            WebElement flightsLink = driver.findElement(By.linkText("Flights"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", flightsLink);

            // ვამოწმებთ, სწორ გვერდზე გადავიდა თუ არა
            String actualUrl = driver.getCurrentUrl();
            Assert.assertEquals(actualUrl, "https://www.inexi.ge/en/flights");
            test.pass("Successfully navigated to Flights page.");
        } catch (Exception e) {
            test.fail("Navigation to Flights page failed: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    @Test
    public void testContactInfo() {
        ExtentTest test = ExtentManager.createTest("Contact Info Test");

        try {
            // ვამოწმებთ, ნამდვილად ეს ინფორმაცია წერია თუ არა გვერდზე
            String phoneNumber = driver.findElement(By.className("phoneNumber")).getText();
            String email = driver.findElement(By.className("email")).getText();

            Assert.assertEquals(phoneNumber, "CALL NOW: (+995 32) 249 26 26");
            Assert.assertEquals(email.toLowerCase(), "email: info@inex.ge");
            test.pass("Contact information validated successfully.");
        } catch (Exception e) {
            test.fail("Contact information validation failed: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    @Test
    public void testLanguageChangeToGeorgian() {
        ExtentTest test = ExtentManager.createTest("Language Change to Georgian Test");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // "ქართული" ენის გადამრთველი ღილაკის მოძებნა და დაჭერა
            WebElement georgianLanguageButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'languageKa')]"))
            );
            georgianLanguageButton.click();

            // ვამოწმებთ, ნამდვილად გადართულია თუ არა ქართული ენა
            String expectedUrl = "https://www.inexi.ge/ka";
            wait.until(ExpectedConditions.urlToBe(expectedUrl));

            String actualUrl = driver.getCurrentUrl();
            Assert.assertEquals(actualUrl, expectedUrl, "The language change to Georgian did not succeed!");
            test.pass("Language successfully changed to Georgian.");
        } catch (Exception e) {
            test.fail("Language change failed: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    @Test
    public void testSellAllOfficesFilter() {
        ExtentTest test = ExtentManager.createTest("Sell ALL Offices Filter Test");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        BasePage basePage = new BasePage(driver);
        basePage.closePopupIfExists();

        try {
            // "Offices" ღილაკზე დაჭერა
            WebElement officesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='navBar ' and contains(@href, '/en/offices')]")
            ));
            officesButton.click();
            test.info("'Offices' button clicked.");

            wait.until(ExpectedConditions.urlToBe("https://www.inexi.ge/en/offices"));
            test.pass("Navigated to Offices page.");

            basePage.closePopupIfExists();
            test.info("Checked and closed popup if existed.");

            // "Tbilisi" შეყვანა
            WebElement cityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
            cityInput.sendKeys("Tbilisi");

            // ვპოულობთ ძებნის ღილაკს
            WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='loopIcon']")));

            // JavaScript-ის გამოყენება ღილაკზე დაჭერისთვის
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", searchButton);
            Thread.sleep(500); // პატარა პაუზა (საჭიროა ზოგიერთ UI ელემენტისთვის)
            js.executeScript("arguments[0].click();", searchButton);

            test.info("Entered 'Tbilisi' and clicked search button.");

            // ვამოწმებთ, რომ მხოლოდ "Tbilisi"-ს მისამართები ჩანს
            wait.until(ExpectedConditions.urlContains("city=Tbilisi"));
            List<WebElement> cityLabels = driver.findElements(By.xpath("//div[contains(@class, 'addressInfo')]/div[@class='address']/span"));

            for (WebElement label : cityLabels) {
                Assert.assertEquals(label.getText().trim(), "TBILISI", "Unexpected city found!");
            }

            test.pass("All displayed offices belong to 'Tbilisi'. Test successful.");
        } catch (Exception e) {
            test.fail("Test failed due to: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }




    @AfterMethod
    public void tearDownExtentReports() {
        ExtentManager.flushReports();
    }
}
