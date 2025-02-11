package ge.inex.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    private WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // ელემენტის ლოდინი 5 წამი
    }

    public void closePopupIfExists() {
        try {
            WebElement popupDismissButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("hide-news-popup")));  // ღილაკის ლოკატორი

            if (popupDismissButton.isDisplayed()) {
                popupDismissButton.click();
                System.out.println("Popup dismissed using 'I read the news' button.");
                wait.until(ExpectedConditions.invisibilityOf(popupDismissButton)); // დაველოდოთ, რომ პოპაპი გაქრეს
            }
        } catch (Exception e) {
            System.out.println("No popup displayed or already closed.");
        }
    }


}
