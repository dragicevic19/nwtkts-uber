package com.nwtkts.uber.e2e.orderRide.tests;

import com.nwtkts.uber.e2e.orderRide.pages.ActiveRidesPage;
import com.nwtkts.uber.e2e.orderRide.pages.HomePage;
import com.nwtkts.uber.e2e.orderRide.pages.LoginPage;
import com.nwtkts.uber.e2e.orderRide.pages.RideRequestPage;
import com.nwtkts.uber.model.RideStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OrderRideHappyFlowTest {

    public static WebDriver webDriver;
    public static WebDriver driverWebDriver;

    @BeforeAll
    public static void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driverWebDriver = new ChromeDriver();
        driverWebDriver.manage().window().maximize();
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
    }

    @AfterAll
    public static void quitDriver() {
        webDriver.quit();
        driverWebDriver.quit();
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
    public void requestRideHappyFlow() {
        login(driverWebDriver, "driver@gmail.com", "123");
        ActiveRidesPage driverHomePage = new ActiveRidesPage(driverWebDriver);

        login(webDriver, "user@gmail.com", "123");

        RideRequestPage rideRequestPage = new RideRequestPage(webDriver);
        rideRequestPage.enterPickupLocation("nis novi sad");
        rideRequestPage.enterDestinationLocation("promenada ");
        rideRequestPage.selectSedanAsVehicleType();
        rideRequestPage.enterAdditionalLocation("futoska 2 novi sad");
        rideRequestPage.clickOnRouteSelection();
        rideRequestPage.submitForm();

        Assertions.assertTrue(driverHomePage.checkIfRideIsAddedToActive(RideStatus.TO_PICKUP.name()));
        rideRequestPage.submitForm();   // kad ponovo posaljem zahtev trebalo bi da bude waiting_for_driver..
        Assertions.assertTrue(driverHomePage.checkIfSecondRideIsAddedToActive(RideStatus.WAITING_FOR_DRIVER_TO_FINISH.name()));
        rideRequestPage.submitForm();   // kad treci put posaljem ne bi trebalo da doda novu voznju
        Assertions.assertTrue(driverHomePage.checkIfNumberOfRidesIsMatching(2));

        rideRequestPage.goToActiveRides();
        ActiveRidesPage clientsActiveRides = new ActiveRidesPage(webDriver);
        Assertions.assertTrue(clientsActiveRides.checkIfRideIsAddedToActive(RideStatus.TO_PICKUP.name()));
        Assertions.assertTrue(clientsActiveRides.checkIfSecondRideIsAddedToActive(RideStatus.WAITING_FOR_DRIVER_TO_FINISH.name()));
        Assertions.assertTrue(clientsActiveRides.checkIfNumberOfRidesIsMatching(2));

    }

}
