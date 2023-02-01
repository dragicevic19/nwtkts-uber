package com.nwtkts.uber.e2e.orderRide.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private static String CLIENT_URL = "http://localhost:4200/";

    private WebDriver driver;

    @FindBy(id = "to-login-btn")
    WebElement toLoginButton;

    public HomePage(WebDriver webDriver) {
        this.driver = webDriver;

        this.driver.get(CLIENT_URL);
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(toLoginButton, "Login"));
    }

    public void clickLoginBtn() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(toLoginButton)).click();
    }
}
