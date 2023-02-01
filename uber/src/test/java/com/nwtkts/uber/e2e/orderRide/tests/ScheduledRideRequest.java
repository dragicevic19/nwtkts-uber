package com.nwtkts.uber.e2e.orderRide.tests;

import com.nwtkts.uber.e2e.orderRide.pages.*;
import com.nwtkts.uber.model.RideStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScheduledRideRequest {

    public static WebDriver webDriver;

    @BeforeAll
    public static void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
    }

    @AfterAll
    public static void quitDriver() {
        webDriver.quit();
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
    public void requestRideWhenClientDoesNotHaveEnoughTokens() {
        login(webDriver, "user@gmail.com", "123");

        RideRequestPage rideRequestPage = new RideRequestPage(webDriver);
        rideRequestPage.enterPickupLocation("nis novi sad");
        rideRequestPage.enterDestinationLocation("promenada ");
        rideRequestPage.selectSedanAsVehicleType();
        rideRequestPage.enterAdditionalLocation("futoska 2 novi sad");
        rideRequestPage.clickOnRouteSelection();
        rideRequestPage.clickOnScheduledBtn();

        ScheduleRidePage scheduleRidePage = new ScheduleRidePage(webDriver);
        scheduleRidePage.selectTimeForScheduledRide();

        rideRequestPage.submitForm();

        rideRequestPage.goToActiveRides();
        ActiveRidesPage clientsActiveRides = new ActiveRidesPage(webDriver);
        Assertions.assertTrue(clientsActiveRides.checkIfRideIsAddedToActive(RideStatus.SCHEDULED.name()));
        Assertions.assertTrue(clientsActiveRides.checkIfNumberOfRidesIsMatching(1));
    }
}
