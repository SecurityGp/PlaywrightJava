package com.assured.tests;

import com.assured.annotations.FrameworkAnnotation;
import com.assured.common.BaseTest;
import com.assured.dataprovider.DataProviderManager;
import com.assured.enums.AuthorType;
import com.assured.enums.CategoryType;
import com.assured.pages.P01_LoginPage;
import com.assured.services.PageActionsService;
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
        PageActionsService actionsService = new PageActionsService(page);

        // Instantiate the login page with the actions service.
        loginPage = new P01_LoginPage(actionsService);

        // Execute the login method (which uses the service to interact with the page).
        loginPage.loginWithValidCredentials(data);

        // Optionally, add assertions here to verify successful login.
    }
}
