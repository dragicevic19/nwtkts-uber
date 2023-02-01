package com.nwtkts.uber.e2e.orderRide.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SplitFareRequestsPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[contains(@class, 'split-fare-ride-row')]")
    List<WebElement> rowsOfSplitFareRequests;

    @FindBy(xpath = "//*[contains(@class, 'split-fare-ride-row')][1]//td[contains(@class, 'split-fare-requested-by')]")
    WebElement firstRowOfSplitFareRequestsReqBy;


    public SplitFareRequestsPage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkIfNumberOfRidesIsMatching(int expectedSize) {
        return rowsOfSplitFareRequests.size() == expectedSize;
    }

    public boolean checkIfRequestedByIsCorrect(String requestedBy) {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(firstRowOfSplitFareRequestsReqBy, requestedBy));
    }


}
