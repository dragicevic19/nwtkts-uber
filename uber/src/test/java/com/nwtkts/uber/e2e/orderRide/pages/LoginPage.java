package com.nwtkts.uber.e2e.orderRide.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    @FindBy(id = "emailLogin")
    WebElement emailTextField;

    @FindBy(id = "passLogin")
    WebElement passwordTextField;

    @FindBy(id = "loginBtn")
    WebElement loginBtn;

    public LoginPage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    public void typeEmail(String email) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(emailTextField)).click();

        emailTextField.sendKeys(email);
    }

    public void typePassword(String password) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(passwordTextField)).click();

        passwordTextField.sendKeys(password);
    }

    public void submitForm() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }


}
