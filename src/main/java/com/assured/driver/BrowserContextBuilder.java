package com.assured.driver;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;

import java.nio.file.Paths;

public class BrowserContextBuilder {
    private final NewContextOptions options = new NewContextOptions();

    public BrowserContextBuilder withViewportSize(int width, int height) {
        options.setViewportSize(width, height);
        return this;
    }

    public BrowserContextBuilder ignoreHttpsErrors(boolean ignore) {
        options.setIgnoreHTTPSErrors(ignore);
        return this;
    }

    public BrowserContextBuilder recordVideo(String videoDir) {
        options.setRecordVideoDir(Paths.get(videoDir));
        return this;
    }

    public NewContextOptions build() {
        return options;
    }
}
