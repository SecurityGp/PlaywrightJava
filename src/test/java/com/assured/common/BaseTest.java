package com.assured.common;

import com.assured.driver.PlaywrightDriverManager;
import com.assured.driver.PlaywrightModule;
import com.microsoft.playwright.Page;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    // Injector for our Guice-based dependency injection
    protected Injector injector;
    // The Playwright Page (analogous to Selenium's WebDriver)
    protected Page page;

    /**
     * Before each test method, initialize the Playwright Page.
     * The browser type can be passed as a parameter (e.g. "chrome", "edge", etc.).
     * In this design, the actual browser selection is driven by FrameworkConstants
     * and the PlaywrightModule.
     *
     * @param browser the browser name (optional, default "chrome")
     */
    @Parameters("BROWSER")
    @BeforeMethod
    public void createDriver(@Optional("chrome") String browser) {
        // Create the Guice injector using our PlaywrightModule.
        // The module uses FrameworkConstants for configuration.
        injector = Guice.createInjector(new PlaywrightModule());

        // Retrieve the Page instance from the injector.
        // This Page was created using the configured Browser and BrowserContext.
        page = injector.getInstance(Page.class);

        // Store the Page in our thread-local driver manager.
        PlaywrightDriverManager.setPage(page);

        // In Playwright the viewport and other settings are typically set during context creation.
        // Therefore, you do not need to call maximize() as in Selenium.
    }

    /**
     * After each test method, clean up by closing the BrowserContext,
     * which in turn closes the Page.
     */
    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        PlaywrightDriverManager.quit();
    }

    /**
     * Creates a Playwright Page (browser) and stores it in the driver manager.
     * This method can be used if you need to create a browser on demand.
     *
     * @param browser the browser name (optional, default "chrome")
     * @return the Playwright Page
     */
    public Page createBrowser(@Optional("chrome") String browser) {
        if (PlaywrightDriverManager.getPage() == null) {
            createDriver(browser);
        }
        return PlaywrightDriverManager.getPage();
    }
}
