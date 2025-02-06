package com.assured.pages;

import com.assured.constants.FrameworkConstants;
import com.assured.helpers.ExcelHelpers;
import com.assured.model.SignInModel;
import com.assured.services.PageActions;
import com.assured.utils.LogUtils;

import java.util.Hashtable;

public class P02_InviteProvider {
    private String pageUrl = "sign-in";
    private String pageTitle = "Assured";

    // Locators defined as string selectors (using XPath).
    public static final String inputEmailSelector = "//*[@id='normal_login_email']";
    public static final String inputPasswordSelector = "//*[@id='normal_login_password']";
    public static final String buttonSignInSelector = "//button[@type=\"submit\"]/span";
    public static final String alertErrorMessageSelector = "//div[@role='alert']";
    public static final String linkForgotPasswordSelector = "//a[normalize-space()='Forgot password?']";
    public static final String linkSignUpSelector = "//a[normalize-space()='Sign up']";
    public static final String labelEmailErrorSelector = "//span[@id='email-error']";
    public static final String labelPasswordErrorSelector = "//span[@id='password-error']";

    public P02_InviteProvider() {
        super();
        // Default constructor; you can add additional initialization if needed.
    }

    /**
     * Logs in using valid credentials from the Excel file.
     *
     * @param data a Hashtable containing test data.
     * @return a new instance of P01_LoginPage (or the next page in the flow).
     */
    public P02_InviteProvider loginWithValidCredentials(Hashtable<String, String> data) {
        // Create an instance of ExcelHelpers and set the Excel file and sheet.
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");

        // Navigate to the staging URL.
        PageActions.navigate(FrameworkConstants.URL_STAGING);

        // Fill in the email and password using data from the Excel file.
        String email = excelHelpers.getCellData(1, SignInModel.getEmail());
        String password = excelHelpers.getCellData(1, SignInModel.getPassword());

        LogUtils.info("Filling in email: " + email);
        PageActions.setText(inputEmailSelector, email);

        LogUtils.info("Filling in password.");
        PageActions.setText(inputPasswordSelector, password);

        // Click the sign-in button.
        PageActions.clickElement(buttonSignInSelector);

        // Optionally, return a new page object representing the next page in your flow.
        // Here, we simply return a new instance of P01_LoginPage for demonstration.
        return new P02_InviteProvider();
    }
}
