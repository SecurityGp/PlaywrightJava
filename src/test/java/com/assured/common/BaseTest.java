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
import com.assured.utils.LogUtils;

public class BaseTest {

    protected Injector injector;
    protected Page page;

    @Parameters("BROWSER")
    @BeforeMethod
    public void createDriver(@Optional("chromium") String browserName) {
        LogUtils.info("Creating Guice injector and Playwright instance...");
        injector = Guice.createInjector(new PlaywrightModule());
        // Guice will provide a new Page instance per injection.
        page = injector.getInstance(Page.class);
        LogUtils.info("Page instance created: " + page);
        // Store the Page in the ThreadLocal driver manager.
        PlaywrightDriverManager.setPage(page);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        LogUtils.info("Closing browser and cleaning up...");
        PlaywrightDriverManager.quit();
    }

    /**
     * Creates a browser on demand if needed.
     *
     * @param browser the browser name (default "chromium")
     * @return the Playwright Page instance.
     */
    public Page createBrowser(@Optional("chromium") String browser) {
        if (PlaywrightDriverManager.getPage() == null) {
            createDriver(browser);
        }
        return PlaywrightDriverManager.getPage();
    }
}
