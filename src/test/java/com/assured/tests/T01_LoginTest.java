package com.assured.tests;

import com.assured.annotations.FrameworkAnnotation;
import com.assured.common.BaseTest;
import com.assured.dataprovider.DataProviderManager;
import com.assured.enums.AuthorType;
import com.assured.enums.CategoryType;
import com.assured.pages.P01_LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.Hashtable;

/**
 * T01_LoginTest contains the login test methods.
 */
@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class T01_LoginTest extends BaseTest {

    private P01_LoginPage loginPage;

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC01_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithValidCredentials(Hashtable<String, String> data) {
        // Instantiate the login page.
        loginPage = new P01_LoginPage();
        // Perform login with valid credentials.
        loginPage.loginWithValidCredentials(data);
        // Add assertions to verify successful login.
    }

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 2, description = "TC02_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithInValidCredentials(Hashtable<String, String> data) {
        // Instantiate the login page.
        loginPage = new P01_LoginPage();
        // Perform login with the supplied (invalid) credentials.
        loginPage.loginWithValidCredentials(data);
        // Add assertions to verify behavior for invalid credentials.
    }
}
