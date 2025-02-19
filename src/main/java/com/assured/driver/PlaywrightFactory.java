package com.assured.driver;

import com.assured.constants.FrameworkConstants;
import com.microsoft.playwright.*;

/**
 * PlaywrightFactory creates and manages Playwright objects using the browser type
 * defined in FrameworkConstants. It uses a switch-case to select the browser.
 */
public final class PlaywrightFactory {

    // ThreadLocal storage for Playwright and Browser instances to manage per–thread resources.
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    // Private constructor to prevent instantiation.
    private PlaywrightFactory() { }

    /**
     * Creates a new Playwright Page instance with the given headless option.
     *
     * @param headless whether to run the browser in headless mode.
     * @return a new Page instance.
     */
    public static Page createPage(boolean headless) {
        // Create the Playwright instance.
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        // Configure launch options.
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);

        // Select the browser based on FrameworkConstants.
        String browserType = FrameworkConstants.BROWSER; // Expected values: "chromium", "firefox", "webkit"
        Browser browser = switch (browserType.toLowerCase()) {
            case "chromium" -> playwright.chromium().launch(launchOptions);
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit" -> playwright.webkit().launch(launchOptions);
            default -> throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        };
        browserThreadLocal.set(browser);

        // Set up context options (for example, ignoring HTTPS errors and setting viewport size).
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true)
                .setViewportSize(1880, 1000);

        // Create a new browser context and then a new page.
        BrowserContext context = browser.newContext(contextOptions);
        Page page = context.newPage();

        // Store the Page instance in your driver manager.
        PlaywrightDriverManager.setPage(page);

        return page;
    }

    /**
     * Convenience method that defaults to non-headless mode.
     *
     * @return a new Page instance.
     */
    public static Page createPage() {
        return createPage(false);
    }

    /**
     * Closes the Playwright-related resources for the current thread.
     */
    public static void quit() {
        // Close the Page's context.
        Page page = PlaywrightDriverManager.getPage();
        if (page != null) {
            page.context().close();
            PlaywrightDriverManager.removePage();
        }

        // Close the Browser.
        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            browser.close();
            browserThreadLocal.remove();
        }

        // Close the Playwright instance.
        Playwright playwright = playwrightThreadLocal.get();
        if (playwright != null) {
            playwright.close();
            playwrightThreadLocal.remove();
        }
    }
}
