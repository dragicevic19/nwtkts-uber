package com.nwtkts.uber.e2e.orderRide.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RideRequestPage {

    private WebDriver driver;

    @FindBy(css = "#location0 input")
    WebElement pickupInput;
    @FindBy(css = "#location1 input")
    WebElement destinationInput;
    @FindBy(css = "#location2 input")
    WebElement additionalStopInput;

    @FindBy(xpath = "//*[@class='geoapify-autocomplete-items']/div[1]")
    WebElement firstAutocompleteRes;

    @FindBy(id = "addMoreStopsBtn")
    WebElement addMoreStopsBtn;

    @FindBy(id = "selectRouteBtn")
    WebElement selectRouteBtn;
    @FindBy(id = "selectVehicleBtn")
    WebElement selectVehicleBtn;
    @FindBy(id = "vehicleTypeValue1")
    WebElement vehicleTypeHatchBack;
    @FindBy(id = "vehicleTypeValue2")
    WebElement vehicleTypeSedan;
    @FindBy(id = "btnForAddFriendsModal")
    WebElement btnForAddFriendsModal;
    @FindBy(id = "requestRideBtn")
    WebElement requestRideBtn;

    @FindBy(id = "client-active-rides-nav-item")
    WebElement activeRidesNavItem;

    @FindBy(id = "split-fare-friend-input")
    WebElement addFriendTextField;
    @FindBy(id = "split-fare-add-friend-btn")
    WebElement addFriendBtn;
    @FindBy(id = "close-modal-x")
    WebElement closeModalBtn;

    @FindBy(id = "client-navbar-dropdown")
    WebElement navbarDropdown;
    @FindBy(id = "split-fare-nav-item")
    WebElement splitFareNavItem;

    @FindBy(id = "scheduled-ride-btn")
    WebElement scheduledRideBtn;


    public RideRequestPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterPickupLocation(String pickup) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(pickupInput)).click();

        pickupInput.sendKeys(pickup);

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(firstAutocompleteRes)).click();
    }

    public void enterDestinationLocation(String destination) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(destinationInput)).click();

        destinationInput.sendKeys(destination);

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(firstAutocompleteRes)).click();

    }

    public void enterAdditionalLocation(String additionalLocation) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(addMoreStopsBtn)).click();

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(additionalStopInput)).click();

        additionalStopInput.sendKeys(additionalLocation);

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(firstAutocompleteRes)).click();
    }

    public void selectSedanAsVehicleType() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(selectVehicleBtn)).click();

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(vehicleTypeSedan)).click();
    }

    public void submitForm() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(requestRideBtn)).click();
    }

    public void goToActiveRides() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(activeRidesNavItem)).click();
    }

    public void clickOnRouteSelection() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(selectRouteBtn)).click();
    }

    public void clickOnAddFriendsToRide() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(btnForAddFriendsModal)).click();
    }

    public void addFriendToRide(String email) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(addFriendTextField)).click();

        addFriendTextField.sendKeys(email);

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(addFriendBtn)).click();

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(closeModalBtn)).click();
    }


    public void goToSplitFareReqs() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(navbarDropdown)).click();
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(splitFareNavItem)).click();
    }

    public void clickOnScheduledBtn() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(scheduledRideBtn)).click();
    }
}
