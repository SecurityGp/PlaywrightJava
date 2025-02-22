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

import static java.lang.Thread.sleep;


@Epic("Regression Test CRM")
@Feature("Sign In Test")
public class T01_LoginTest extends BaseTest {

    private P01_LoginPage loginPage;

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 1, description = "TC01_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithValidCredentials(Hashtable<String, String> data) {
       loginPage = new P01_LoginPage();

        loginPage.loginWithValidCredentials(data);

    }

    @FrameworkAnnotation(author = {AuthorType.Gnanapandithan}, category = {CategoryType.REGRESSION})
    @Test(priority = 2, description = "TC02_signInWithDataProvider",
            dataProvider = "getSignInDataHashTable", dataProviderClass = DataProviderManager.class)
    public void loginWithInValidCredentials(Hashtable<String, String> data) {
        // Instantiate the login page.
        loginPage = new P01_LoginPage();
        // Perform login with the supplied (invalid) credentials.
        loginPage.loginWithValidCredentials(data);

    }
    @Test(priority = 3, description = "TC03_urlTest")
    public void urlTest() throws InterruptedException {
        loginPage = new P01_LoginPage();
        loginPage.login();


    }
}
