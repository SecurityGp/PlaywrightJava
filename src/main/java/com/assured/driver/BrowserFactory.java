package com.assured.driver;

import com.assured.constants.FrameworkConstants;
import com.assured.exceptions.HeadlessNotSupportedException;
import com.microsoft.playwright.*;
import java.util.Arrays;


public enum BrowserFactory {

    CHROME {
        @Override
        public Browser launchBrowser(Playwright playwright) {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(Boolean.parseBoolean(FrameworkConstants.HEADLESS));
            // Basic arguments for Chrome
            options.setArgs(Arrays.asList(
                    "--disable-extensions",
                    "--disable-infobars",
                    "--disable-notifications",
                    "--remote-allow-origins=*"
            ));
            // Additional headless-related arguments if running headless
            if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
                options.setArgs(Arrays.asList(
                        "--headless=new",
                        "--disable-gpu",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--window-size=1880,1000"
                ));
            }
            return playwright.chromium().launch(options);
        }
    },

    EDGE {
        @Override
        public Browser launchBrowser(Playwright playwright) {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(Boolean.parseBoolean(FrameworkConstants.HEADLESS));
            // Use the msedge channel to launch Edge
            options.setChannel("msedge");
            options.setArgs(Arrays.asList(
                    "--disable-extensions",
                    "--disable-infobars",
                    "--disable-notifications",
                    "--remote-allow-origins=*"
            ));
            if (Boolean.parseBoolean(FrameworkConstants.HEADLESS)) {
                options.setArgs(Arrays.asList(
                        "--headless=new",
                        "--disable-gpu",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--window-size=1880,1000"
                ));
            }
            return playwright.chromium().launch(options);
        }
    },

    FIREFOX {
        @Override
        public Browser launchBrowser(Playwright playwright) {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(Boolean.parseBoolean(FrameworkConstants.HEADLESS));
            // Note: For Firefox, viewport dimensions are better set at the context level.
            return playwright.firefox().launch(options);
        }
    },

    SAFARI {
        @Override
        public Browser launchBrowser(Playwright playwright) {
            // In this example, we do not support headless mode for Safari/WebKit.
            if (Boolean.parseBoolean(FrameworkConstants.HEADLESS))
                throw new HeadlessNotSupportedException("Safari (WebKit)");
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(false);
            return playwright.webkit().launch(options);
        }
    };


    public abstract Browser launchBrowser(Playwright playwright);


    public BrowserContext createBrowserContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        contextOptions.setIgnoreHTTPSErrors(true);
        contextOptions.setViewportSize(1880, 1000);
        return browser.newContext(contextOptions);
    }


    public Page createPage(Browser browser) {
        return createBrowserContext(browser).newPage();
    }
    // In BrowserFactory enum
    public Browser connectRemote(Playwright playwright, String remoteUrl) {
        return switch (this) {
            case CHROME, EDGE -> playwright.chromium().connect(remoteUrl);
            case FIREFOX -> playwright.firefox().connect(remoteUrl);
            case SAFARI -> playwright.webkit().connect(remoteUrl);
            default -> throw new UnsupportedOperationException("Remote connection not supported for: " + this);
        };
    }



}
