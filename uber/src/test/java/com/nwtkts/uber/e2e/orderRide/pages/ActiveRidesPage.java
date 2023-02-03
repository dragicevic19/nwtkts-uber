package com.nwtkts.uber.e2e.orderRide.pages;

import com.nwtkts.uber.model.RideStatus;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ActiveRidesPage {

    private WebDriver webDriver;

    @FindBy(xpath = "//*[contains(@class, 'active-ride-row')]")
    List<WebElement> rowsOfActiveRides;

    @FindBy(xpath = "//*[contains(@class, 'active-ride-row')][1]//span[contains(@class, 'rideStatus')]")
    WebElement firstRowOfActiveRidesRideStatus;

    @FindBy(xpath = "//*[contains(@class, 'active-ride-row')][2]//span[contains(@class, 'rideStatus')]")
    WebElement secondRowOfActiveRidesRideStatus;

    @FindBy(id = "start-ride-btn")
    WebElement startRideBtn;

    @FindBy(id = "finish-ride-btn")
    WebElement finishRideBtn;

    @FindBy(id = "home-nav-item")
    WebElement homeNavItem;


    public ActiveRidesPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);

    }

    public boolean checkIfRideIsAddedToActive(String rideStatus) {
        return (new WebDriverWait(webDriver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.textToBePresentInElement(firstRowOfActiveRidesRideStatus, rideStatus));
    }

    public boolean checkIfSecondRideIsAddedToActive(String rideStatus) {
        return (new WebDriverWait(webDriver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(secondRowOfActiveRidesRideStatus, rideStatus));
    }

    public boolean checkIfNumberOfRidesIsMatching(int expectedSize) {
        return rowsOfActiveRides.size() == expectedSize;
    }

    public void clickStartRide() {
        (new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .until(ExpectedConditions.elementToBeClickable(startRideBtn)).click();
    }

    public void clickFinishRide() {
        (new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .until(ExpectedConditions.elementToBeClickable(finishRideBtn)).click();
    }

    public void clickHome() {
        (new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .until(ExpectedConditions.elementToBeClickable(homeNavItem)).click();
    }
}