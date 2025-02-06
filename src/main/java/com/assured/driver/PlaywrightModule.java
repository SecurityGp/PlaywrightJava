package com.assured.driver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.microsoft.playwright.*;
import com.microsoft.playwright.Browser.NewContextOptions;

public class PlaywrightModule extends AbstractModule {
    @Override
    protected void configure() {
        // No explicit bindings required if using @Provides methods.
    }

    @Provides
    public Playwright providePlaywright() {
        return Playwright.create();
    }

    @Provides
    public Browser provideBrowser(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
        // Using Chromium; adjust if needed for Firefox or WebKit.
        return playwright.chromium().launch(launchOptions);
    }

    @Provides
    public BrowserContext provideBrowserContext(Browser browser) {
        NewContextOptions contextOptions = new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)
                .setViewportSize(1880, 1000);
        return browser.newContext(contextOptions);
    }

    @Provides
    public Page providePage(BrowserContext context) {
        return context.newPage();
    }
}
