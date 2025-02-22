package com.assured.driver;

import com.assured.constants.FrameworkConstants;
import com.microsoft.playwright.*;

public final class PlaywrightFactory {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    private PlaywrightFactory() { }

    public static Page createPage(String headless) {
        Playwright playwright = null;
        Browser browser = null;
        try {
            // Create Playwright instance and store it in the thread-local
            playwright = Playwright.create();
            playwrightThreadLocal.set(playwright);

            // Configure browser launch options
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(Boolean.parseBoolean(headless));

            // Choose the browser type based on FrameworkConstants
            String browserType = FrameworkConstants.BROWSER.toLowerCase();
            switch (browserType) {
                case "chromium":
                    browser = playwright.chromium().launch(launchOptions);
                    break;
                case "firefox":
                    browser = playwright.firefox().launch(launchOptions);
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(launchOptions);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser type: " + browserType);
            }
            browserThreadLocal.set(browser);

            // Set up context options and create a new page
            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setViewportSize(1880, 1000);
            BrowserContext context = browser.newContext(contextOptions);
            Page page = context.newPage();

            // Store the Page instance
            PlaywrightDriverManager.setPage(page);

            return page;
        } catch (Exception e) {
            // Ensure cleanup if something goes wrong during creation
            quit();
            throw e;
        }
    }

    public static Page createPage() {
        return createPage(FrameworkConstants.HEADLESS);
    }

    public static void quit() {
        // Close the page context and remove the page
        Page page = PlaywrightDriverManager.getPage();
        if (page != null) {
            try {
                page.context().close();
            } catch (Exception e) {
                // Optionally log the exception
            } finally {
                PlaywrightDriverManager.removePage();
            }
        }
        // Close the browser
        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            try {
                browser.close();
            } catch (Exception e) {
                // Optionally log the exception
            } finally {
                browserThreadLocal.remove();
            }
        }
        // Close the Playwright instance
        Playwright playwright = playwrightThreadLocal.get();
        if (playwright != null) {
            try {
                playwright.close();
            } catch (Exception e) {
                // Optionally log the exception
            } finally {
                playwrightThreadLocal.remove();
            }
        }
    }
}
