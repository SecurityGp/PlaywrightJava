package com.assured.tests;

import com.assured.annotations.FrameworkAnnotation;
import com.assured.common.BaseTest;
import com.assured.dataprovider.DataProviderManager;
import com.assured.enums.AuthorType;
import com.assured.enums.CategoryType;
import com.assured.pages.P01_LoginPage;
import com.assured.pages.P02_InviteProvider;
import org.testng.annotations.Test;

import java.util.Hashtable;

public class T02_InviteProvider extends BaseTest {
    private P02_InviteProvider InviteProvider;

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC01_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithValidCredentials(Hashtable<String, String> data) {
        // Create an instance of PageActionsService using the Page created in BaseTest.

        // Instantiate the login page with the actions service.
        InviteProvider = new P02_InviteProvider();
        // Execute the login method.
        InviteProvider.loginWithValidCredentials(data);
        // Optionally, add assertions to verify successful login.
    }

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC02_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithInValidCredentials(Hashtable<String, String> data) {
        // Create an instance of PageActionsService using the Page created in BaseTest.
        // Instantiate the login page with the actions service.
        InviteProvider = new P02_InviteProvider();
        // Execute the login method.
        InviteProvider.loginWithValidCredentials(data);
        // Optionally, add assertions to verify behavior for invalid credentials.
    }
}
