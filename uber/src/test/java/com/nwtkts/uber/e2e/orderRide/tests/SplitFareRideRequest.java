package com.nwtkts.uber.e2e.orderRide.tests;

import com.nwtkts.uber.e2e.orderRide.pages.*;
import com.nwtkts.uber.model.RideStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SplitFareRideRequest {

    public static WebDriver webDriver;
    public static WebDriver secondWebDriver;


    @BeforeAll
    public static void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        secondWebDriver = new ChromeDriver();
        secondWebDriver.manage().window().maximize();
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();

    }

    @AfterAll
    public static void quitDriver() {
        webDriver.quit();
        secondWebDriver.quit();
    }

    private void login(WebDriver driver, String email, String password) {
        HomePage homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);
        loginPage.submitForm();
    }

    @Test
    public void splitFareRideRequest() {
        login(webDriver, "user@gmail.com", "123");
        login(secondWebDriver, "seconduser@gmail.com", "123");

        RideRequestPage rideRequestPage = new RideRequestPage(webDriver);
        rideRequestPage.enterPickupLocation("nis novi sad");
        rideRequestPage.enterDestinationLocation("promenada ");
        rideRequestPage.selectSedanAsVehicleType();
        rideRequestPage.enterAdditionalLocation("futoska 2 novi sad");
        rideRequestPage.clickOnRouteSelection();
        rideRequestPage.clickOnAddFriendsToRide();
        rideRequestPage.addFriendToRide("seconduser@gmail.com");
        rideRequestPage.submitForm();

        RideRequestPage secondClientRideReqPage = new RideRequestPage(secondWebDriver);
        secondClientRideReqPage.goToSplitFareReqs();
        SplitFareRequestsPage splitFareRequestsPage = new SplitFareRequestsPage(secondWebDriver);

        Assertions.assertTrue(splitFareRequestsPage.checkIfRequestedByIsCorrect("user@gmail.com"));
    }
}
