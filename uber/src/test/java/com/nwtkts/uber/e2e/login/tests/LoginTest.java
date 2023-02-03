package com.nwtkts.uber.e2e.login.tests;

import com.nwtkts.uber.e2e.login.pages.AdminHomePage;
import com.nwtkts.uber.e2e.login.pages.ClientHomePage;
import com.nwtkts.uber.e2e.login.pages.DriverHomePage;
import com.nwtkts.uber.e2e.orderRide.pages.ActiveRidesPage;
import com.nwtkts.uber.e2e.orderRide.pages.HomePage;
import com.nwtkts.uber.e2e.orderRide.pages.LoginPage;
import com.nwtkts.uber.e2e.orderRide.pages.RideRequestPage;
import com.nwtkts.uber.model.RideStatus;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {

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

    @Test
    @DisplayName("Should login as client.")
    public void loginAsClient() {
        String email = "user@gmail.com";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);
        loginPage.submitForm();

        ClientHomePage clientHomePage = new ClientHomePage(webDriver);
        Assertions.assertTrue(clientHomePage.isPageOpened(email));

    }

    @Test
    @DisplayName("Should login as driver.")
    public void loginAsDriver() {
        String email = "driver@gmail.com";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);
        loginPage.submitForm();

        DriverHomePage clientHomePage = new DriverHomePage(webDriver);
        Assertions.assertTrue(clientHomePage.isPageOpened(email));

    }

    @Test
    @DisplayName("Should login as admin.")
    public void loginAsAdmin() {
        String email = "test@gmail.com";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);
        loginPage.submitForm();

        AdminHomePage clientHomePage = new AdminHomePage(webDriver);
        Assertions.assertTrue(clientHomePage.isPageOpened(email));

    }

    @Test
    @DisplayName("Should show email is required and password is required.")
    public void shouldShowEmailAndPasswordIsRequired() {
        String email = "client@gmail.com";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.clickOnEmailField();
        loginPage.clickOnPasswordField();

        Assertions.assertTrue(loginPage.isEmailIsRequiredElementVisible());
        Assertions.assertTrue(loginPage.isPasswordRequiredElementVisible());

    }

    @Test
    @DisplayName("Should not be able to login when email format is not valid.")
    public void shouldNotAllowToLogInWhenEmailFormatIsWrong() {
        String email = "client";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);

        Assertions.assertFalse(loginPage.isLogInBtnClickable());

    }


    @Test
    @DisplayName("Should not be able to login when credentials are not valid.")
    public void shouldNotBeAbleToLogInWithFalseCredentials() {
        String email = "client@garopoih";
        String password = "123";

        HomePage homePage = new HomePage(webDriver);
        Assertions.assertTrue(homePage.isPageOpened());
        homePage.clickLoginBtn();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeEmail(email);
        loginPage.typePassword(password);
        loginPage.submitForm();

        Assertions.assertTrue(loginPage.isToastrElementVisible());

    }


}
