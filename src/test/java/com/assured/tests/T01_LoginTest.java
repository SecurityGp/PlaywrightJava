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

@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class T01_LoginTest extends BaseTest {

    private P01_LoginPage loginPage;

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC01_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithValidCredentials(Hashtable<String, String> data) {
        // Create an instance of PageActionsService using the Page created in BaseTest.

        // Instantiate the login page with the actions service.
        loginPage = new P01_LoginPage();
        // Execute the login method.
        loginPage.loginWithValidCredentials(data);
        // Optionally, add assertions to verify successful login.
    }

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC02_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithInValidCredentials(Hashtable<String, String> data) {
        // Create an instance of PageActionsService using the Page created in BaseTest.
        // Instantiate the login page with the actions service.
        loginPage = new P01_LoginPage();
        // Execute the login method.
        loginPage.loginWithValidCredentials(data);
        // Optionally, add assertions to verify behavior for invalid credentials.
    }
}
