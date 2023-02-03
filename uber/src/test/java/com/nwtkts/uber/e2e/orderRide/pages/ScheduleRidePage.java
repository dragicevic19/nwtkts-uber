package com.nwtkts.uber.e2e.orderRide.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ScheduleRidePage {
    private WebDriver driver;

    @FindBy(id = "choose-time-dropdown-btn")
    WebElement chooseTimeDropdownBtn;

    @FindBy(xpath = "//*[contains(@class, 'schedule-time-item')][10]")
    WebElement timeInDropdown;

    @FindBy(id = "schedule-time-submit")
    WebElement submitBtn;


    public ScheduleRidePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    public void selectTimeForScheduledRide() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(chooseTimeDropdownBtn)).click();
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(timeInDropdown)).click();
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
    }
}
