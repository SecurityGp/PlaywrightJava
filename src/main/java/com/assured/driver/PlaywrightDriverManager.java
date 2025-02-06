package com.assured.driver;

import com.microsoft.playwright.Page;

/**
 * PlaywrightDriverManager manages the Playwright Page instance in a thread-safe manner.
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
     * Closes the BrowserContext (which in turn closes the Page) and clears the ThreadLocal.
     */
    public static void quit() {
        Page page = getPage();
        if (page != null) {
            page.context().close();
            removePage();
        }
    }
}
