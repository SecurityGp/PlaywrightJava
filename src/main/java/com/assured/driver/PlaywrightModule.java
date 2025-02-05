package com.assured.driver;
import com.assured.constants.FrameworkConstants;
import com.assured.enums.Target;
import com.assured.exceptions.TargetNotValidException;
import com.assured.driver.BrowserContextBuilder;
import com.microsoft.playwright.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class PlaywrightModule extends AbstractModule {


    @Provides
    @Singleton
    public Playwright providePlaywright() {
        return Playwright.create();
    }

    @Provides
    @Singleton
    public Browser provideBrowser(Playwright playwright) {
        // Use FrameworkConstants and your enum to decide on local vs remote execution.
        Target target = Target.valueOf(FrameworkConstants.TARGET.toUpperCase());
        switch (target) {
            case LOCAL:
                return BrowserFactory.valueOf(FrameworkConstants.BROWSER.toUpperCase())
                        .launchBrowser(playwright);
            case REMOTE:
                String remoteUrl = String.format("ws://%s:%s", FrameworkConstants.REMOTE_URL, FrameworkConstants.REMOTE_PORT);
                return BrowserFactory.valueOf(FrameworkConstants.BROWSER.toUpperCase())
                        .connectRemote(playwright, remoteUrl);
            default:
                throw new TargetNotValidException(target.toString());
        }
    }

    @Provides
    @Singleton
    public BrowserContext provideBrowserContext(Browser browser) {
        // Use the builder to configure the BrowserContext
        BrowserContextBuilder builder = new BrowserContextBuilder()
                .withViewportSize(1880, 1000)
                .ignoreHttpsErrors(true);
        // Optionally add: builder.recordVideo("videos/");
        return browser.newContext(builder.build());
    }

    @Provides
    @Singleton
    public Page providePage(BrowserContext context) {
        return context.newPage();
    }
    @Override
    protected void configure() {
        // Additional explicit bindings can be added here if needed.
    }
}
