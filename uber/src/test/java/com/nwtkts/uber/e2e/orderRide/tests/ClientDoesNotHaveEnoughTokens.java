package com.nwtkts.uber.e2e.orderRide.tests;

import com.nwtkts.uber.e2e.orderRide.pages.ActiveRidesPage;
import com.nwtkts.uber.e2e.orderRide.pages.HomePage;
import com.nwtkts.uber.e2e.orderRide.pages.LoginPage;
import com.nwtkts.uber.e2e.orderRide.pages.RideRequestPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ClientDoesNotHaveEnoughTokens {
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
        login(webDriver, "seconduser@gmail.com", "123");

        RideRequestPage rideRequestPage = new RideRequestPage(webDriver);
        rideRequestPage.enterPickupLocation("nis novi sad");
        rideRequestPage.enterDestinationLocation("promenada ");
        rideRequestPage.selectSedanAsVehicleType();
        rideRequestPage.enterAdditionalLocation("futoska 2 novi sad");
        rideRequestPage.clickOnRouteSelection();
        rideRequestPage.submitForm();

        rideRequestPage.goToActiveRides();
        ActiveRidesPage clientsActiveRides = new ActiveRidesPage(webDriver);
        Assertions.assertTrue(clientsActiveRides.checkIfNumberOfRidesIsMatching(0));
    }
}
