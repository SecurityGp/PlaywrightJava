package com.assured.pages;

import com.assured.services.PageActionsService;

public class CommonPageCRM {

    // Shared service instance for page actions
    protected final PageActionsService pageActionsService;

    // Lazy-loaded instance of the Login Page
    private P01_LoginPage loginPage;

    /**
     * Constructs a CommonPageCRM with the given PageActionsService.
     *
     * @param pageActionsService the shared PageActionsService instance (must not be null)
     */
    public CommonPageCRM(PageActionsService pageActionsService) {
        if (pageActionsService == null) {
            throw new IllegalArgumentException("PageActionsService cannot be null");
        }
        this.pageActionsService = pageActionsService;
    }

    /**
     * Returns an instance of the Login Page.
     * If it has not been instantiated yet, a new instance is created using the shared PageActionsService.
     *
     * @return an instance of P01_LoginPage
     */
    public P01_LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new P01_LoginPage(pageActionsService);
        }
        return loginPage;
    }
}
