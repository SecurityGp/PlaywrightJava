package com.assured.driver;

import com.microsoft.playwright.Page;

/**
 * PlaywrightDriverManager manages the Playwright Page instance in a thread-safe way.
 * <p>
 * It is similar in purpose to the Selenium DriverManager; however, instead of managing a WebDriver,
 * it manages a Playwright Page (which you typically get from a BrowserContext).
 * </p>
 */
public final class PlaywrightDriverManager {

    // ThreadLocal storage for a Playwright Page instance.
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    // Private constructor to prevent instantiation.
    private PlaywrightDriverManager() { }

    /**
     * Retrieves the current thread's Playwright Page.
     *
     * @return the Page associated with the current thread, or null if not set.
     */
    public static Page getPage() {
        return pageThreadLocal.get();
    }

    /**
     * Sets the Playwright Page for the current thread.
     *
     * @param page the Playwright Page instance to store.
     */
    public static void setPage(Page page) {
        pageThreadLocal.set(page);
    }

    /**
     * Removes the Page from the current thread's storage.
     */
    public static void removePage() {
        pageThreadLocal.remove();
    }

    /**
     * Quits the browser session for the current thread.
     * <p>
     * This method closes the BrowserContext (which, in turn, closes the Page) and then clears the ThreadLocal.
     * Adjust this logic if your design requires closing the entire Browser.
     * </p>
     */
    public static void quit() {
        Page page = getPage();
        if (page != null) {
            // Close the BrowserContext which will close the associated Page.
            page.context().close();
            removePage();
        }
    }
}
