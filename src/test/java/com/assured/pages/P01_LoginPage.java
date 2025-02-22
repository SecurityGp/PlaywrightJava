package com.assured.pages;

import com.assured.constants.FrameworkConstants;
import com.assured.helpers.ExcelHelpers;
import com.assured.services.PageActions;
import com.assured.model.SignInModel;
import com.assured.utils.LogUtils;
import com.assured.pages.test;

import java.util.Hashtable;

import static com.assured.services.PageActions.*;
import static java.lang.Thread.sleep;

/**
 * P01_LoginPage represents the login page of your application.
 * It uses PageActions (which in turn should use the Page instance from PlaywrightDriverManager)
 * to perform UI interactions.
 */
public class P01_LoginPage extends CommonPageCRM {

    private String pageUrl = "sign-in";
    private String pageTitle = "Assured";

    // Locators defined as string selectors (using XPath).
    public static final String inputEmailSelector = "//*[@id='normal_login_email']";
    public static final String inputPasswordSelector = "//*[@id='normal_login_password']";
    public static final String buttonSignInSelector = "//button[@type='submit']/span";
    public static final String alertErrorMessageSelector = "//div[@role='alert']";
    public static final String linkForgotPasswordSelector = "//a[normalize-space()='Forgot password?']";
    public static final String linkSignUpSelector = "//a[normalize-space()='Sign up']";
    public static final String labelEmailErrorSelector = "//span[@id='email-error']";
    public static final String labelPasswordErrorSelector = "//span[@id='password-error']";
    public static final String inputNewPassword = "input[placeholder=\"New Password\"]";
    public static final String inputConfirmPassword = "input[placeholder=\"Confirm Password\"]";
    public static final String buttonCreateAccountSelector = "#createAdminAccountForm > button > span";


    public P01_LoginPage() {
        super();
        // Additional initialization if required.
    }

    /**
     * Logs in using credentials provided via the Excel file.
     *
     * @param data a Hashtable containing test data.
     * @return a new instance of P01_LoginPage (or the next page in the flow).
     */
    public P01_LoginPage loginWithValidCredentials(Hashtable<String, String> data) {
        // Prepare Excel helpers to read the test data.
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");

        // Navigate to the staging URL.
        navigate(FrameworkConstants.URL_STAGING);
        String email = data.get(SignInModel.getEmail());
        String password = data.get(SignInModel.getPassword());

        // Read email and password from the Excel file.
//       String email = excelHelpers.getCellData(1, SignInModel.getEmail());
//        String password = excelHelpers.getCellData(1, SignInModel.getPassword());

        LogUtils.info("Filling in email: " + email);
        PageActions.setText(inputEmailSelector, email);

        LogUtils.info("Filling in password.");
        PageActions.setText(inputPasswordSelector, password);

        // Click the sign-in button.
        PageActions.clickElement(buttonSignInSelector);

        // Optionally, you may return a different page object if the login was successful.
        return new P01_LoginPage();
    }


    public P01_LoginPage login() throws InterruptedException {

        navigate(FrameworkConstants.URL_STAGING);
        LogUtils.info("Filling in email: ");
        PageActions.setText(inputEmailSelector, "abc@gmail.com");

        LogUtils.info("Filling in password.");
        PageActions.setText(inputPasswordSelector, "abc");
        sleep(8000);

        String domain = "private";
        String mailbox = "abc1";
        String expectedFrom = "no-reply@withassured.com";
        String expectedSubject = "Onboarding Invite";

        // Retrieve URL from email
        String mailUrl = PageActions.getMailUrl(domain, mailbox, expectedFrom, expectedSubject);

        PageActions.openNewBrowserAndPerformAction(() -> {
            navigate(mailUrl);
            setText(inputNewPassword, "123456");
            setText(inputConfirmPassword, "123456");
            clickElement(buttonCreateAccountSelector);
            try {
                sleep(8000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Navigate to the staging URL.


        try {
            sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Optionally, you may return a different page object if the login was successful.
        return new P01_LoginPage();
    }
}
