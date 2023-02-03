package com.nwtkts.uber.e2e.login.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminHomePage {

    private WebDriver driver;

    @FindBy(id = "admin-navbar")
    WebElement navbar;

    @FindBy(id = "admin-navbar-dropdown")
    WebElement navbarDropDown;

    public AdminHomePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(String userMail) {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(navbarDropDown, userMail));
    }

}
