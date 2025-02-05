package com.assured.services;

import com.assured.utils.LogUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

/**
 * PageActionsService encapsulates common page interaction methods using Playwright.
 * It includes flexible locator methods as well as common actions such as fill and click.
 */
@Getter
public class PageActionsService {

    /**
     * -- GETTER --
     *  Returns the underlying Playwright Page instance.
     *
     * @return the Page instance.
     */
    private final Page page;

    /**
     * Constructs a PageActionsService instance with the provided Playwright Page.
     *
     * @param page the Playwright Page instance to interact with.
     */
    public PageActionsService(Page page) {
        if (page == null) {
            throw new IllegalArgumentException("Page instance cannot be null");
        }
        this.page = page;
    }

    /**
     * Fills the element identified by the given selector with the specified text.
     *
     * @param selector The Playwright selector (CSS, XPath, etc.) for the element.
     * @param text     The text to fill into the element.
     */
    @Step("Fill element using selector: {0} with text: {1}")
    public void fill(String selector, String text) {
        Locator locator = page.locator(selector);
        locator.fill(text);
        LogUtils.info("Filled element with selector: " + selector + " with text: " + text);
    }

    /**
     * Clicks the element identified by the given selector.
     *
     * @param selector The Playwright selector (CSS, XPath, etc.) for the element.
     */
    @Step("Click element using selector: {0}")
    public void click(String selector) {
        Locator locator = page.locator(selector);
        locator.click();
        LogUtils.info("Clicked element with selector: " + selector);
    }

    // Additional flexible locator methods (e.g., getLocatorByStrategy) can be added here.
}
