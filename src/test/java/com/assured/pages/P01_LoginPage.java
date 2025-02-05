package com.assured.pages;

import com.assured.constants.FrameworkConstants;
import com.assured.helpers.ExcelHelpers;
import com.assured.model.SignInModel;
import com.assured.services.PageActionsService;
import java.util.Hashtable;

public class P01_LoginPage extends CommonPageCRM {

    private String pageUrl = "sign-in";
    private String pageTitle = "Assured";

    // Define locators as string selectors (using XPath for example).
    public String inputEmailSelector = "//input[@id='normal_login_email']";
    public String inputPasswordSelector = "//input[@id='normal_login_password']";
    public String buttonSignInSelector = "//span[normalize-space()='Log in']";
    public String alertErrorMessageSelector = "//div[@role='alert']";
    public String linkForgotPasswordSelector = "//a[normalize-space()='Forgot password?']";
    public String linkSignUpSelector = "//a[normalize-space()='Sign up']";
    public String labelEmailErrorSelector = "//span[@id='email-error']";
    public String labelPasswordErrorSelector = "//span[@id='password-error']";

    /**
     * Constructor that accepts a PageActionsService instance.
     * This is required by the new CommonPageCRM constructor.
     *
     * @param pageActionsService the shared PageActionsService instance.
     */
    public P01_LoginPage(PageActionsService pageActionsService) {
        super(pageActionsService); // Pass the dependency to the superclass constructor.
    }

    /**
     * Logs in using valid credentials loaded from an Excel file.
     *
     * @param data A Hashtable containing test data.
     * @return a new instance of P01_LoginPage (or the next page object in the flow).
     */
    public P01_LoginPage loginWithValidCredentials(Hashtable<String, String> data) {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(FrameworkConstants.EXCEL_DATA_FILE_PATH, "SignIn");

        // Open website, fill email, fill password, and click the Sign In button.
        // (Assuming your PageActionsService instance is used within methods like 'openWebsite', 'setText', 'clickElement'
        //  that you should update to work with Playwright if necessary.)
        openWebsite(FrameworkConstants.URL_STAGING);
        setText(inputEmailSelector, excelHelpers.getCellData(1, SignInModel.getEmail()));
        setText(inputPasswordSelector, excelHelpers.getCellData(1, SignInModel.getPassword()));
        clickElement(buttonSignInSelector);

        return new P01_LoginPage(super.pageActionsService); // Create and return a new page object if needed.
    }

    // You should update openWebsite, setText, and clickElement methods to use your PageActionsService.
    // For example:
    public void openWebsite(String url) {
        super.pageActionsService.getPage().navigate(url);
    }

    public void setText(String selector, String text) {
        super.pageActionsService.fill(selector, text);
    }

    public void clickElement(String selector) {
        super.pageActionsService.click(selector);
    }
}
