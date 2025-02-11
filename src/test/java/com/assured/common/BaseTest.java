package com.assured.common;

import com.assured.driver.PlaywrightDriverManager;
import com.assured.driver.PlaywrightFactory;
import com.microsoft.playwright.Page;
import com.assured.utils.LogUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest now uses the PlaywrightFactory to create and manage the Page instance.
 */
public class BaseTest {

    protected Page page;

    @Parameters("BROWSER")
    @BeforeMethod
    public void createDriver(@Optional("chromium") String browserName) {
        LogUtils.info("Creating Playwright instance using factory method...");
        // Create a Page instance using our factory.
        // You can pass true for headless mode if needed (here we use false).
        page = PlaywrightFactory.createPage(false);
        LogUtils.info("Page instance created: " + page);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        LogUtils.info("Closing browser and cleaning up...");
        PlaywrightFactory.quit();
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
