package com.assured.services;

import com.assured.driver.PlaywrightDriverManager;
import com.assured.utils.LogUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;

/**
 * WebUI provides static methods for common Playwright interactions.
 * It retrieves the current Page instance from PlaywrightDriverManager.
 */
public class PageActions{

    /**
     * Retrieves the current Playwright Page from the driver manager.
     * If the Page is not initialized, throws an IllegalStateException.
     *
     * @return the active Playwright Page.
     */
    private static Page getPage() {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            throw new IllegalStateException("Playwright page is not initialized. Please ensure BaseTest creates a Page instance.");
        }
        return page;
    }

    /**
     * Navigates the current Page to the given URL using a 60-second timeout.
     *
     * @param url the URL to navigate to.
     */
    @Step("Navigate to URL: {0}")
    public static void navigate(String url) {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            throw new IllegalStateException("Page instance is null. Ensure the driver is initialized.");
        }
        page.setDefaultNavigationTimeout(60000);
        LogUtils.info("Navigating to URL: " + url);
        try {
            NavigateOptions options = new NavigateOptions();
            options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
            options.setWaitUntil(WaitUntilState.LOAD);
            options.setWaitUntil(WaitUntilState.NETWORKIDLE);
            page.navigate(url,options);
        } catch (Exception e) {
            LogUtils.error("Navigation failed with error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fills the element identified by the given selector with the provided text.
     *
     * @param selector the selector (CSS, XPath, etc.) of the element.
     * @param text     the text to fill.
     */
    @Step("Fill element using selector: {0} with text: {1}")
    public static void setText(String selector, String text) {
        Page page = getPage();
        try {
            Locator locator = page.locator(selector);
            locator.fill(text);
            LogUtils.info("Filled element with selector: " + selector + " with text: " + text);
        } catch (Exception e) {
            LogUtils.error("Failed to fill element with selector: " + selector + " with error: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Clicks the element identified by the given selector.
     *
     * @param selector the selector (CSS, XPath, etc.) of the element.
     */
    @Step("Click element using selector: {0}")
    public static void clickElement(String selector) {
        Page page = getPage();
        try {
            Locator locator = page.locator(selector);
            locator.click();
            LogUtils.info("Clicked element with selector: " + selector);
        } catch (Exception e) {
            LogUtils.error("Failed to click element with selector: " + selector + " with error: " + e.getMessage(), e);
            throw e;
        }
    }
}
