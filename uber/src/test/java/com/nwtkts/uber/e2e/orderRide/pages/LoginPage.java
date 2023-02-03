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

    @FindBy(id = "email-is-required")
    WebElement emailIsRequiredElement;

    @FindBy(id = "invalid-email")
    WebElement invalidEmailElement;

    @FindBy(id = "password-is-required")
    WebElement passwordIsRequiredElement;

    @FindBy(xpath = "//div[@aria-label='Login Error']")
    WebElement toastrElement;

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

    public void clickOnEmailField() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(emailTextField)).click();
    }

    public void clickOnPasswordField() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(passwordTextField)).click();
    }

    public boolean isEmailIsRequiredElementVisible() {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(emailIsRequiredElement)).isDisplayed();
    }

    public boolean isPasswordRequiredElementVisible() {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(passwordIsRequiredElement)).isDisplayed();
    }

    public boolean isLogInBtnClickable() {
        return loginBtn.isEnabled();
    }

    public boolean isToastrElementVisible() {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(toastrElement)).isDisplayed();
//        return toastrElement.isDisplayed();
    }


}
