package com.assured.helpers;

import com.assured.utils.LogUtils;
import com.microsoft.playwright.Page;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * CaptureHelpers provides methods to capture screenshots using Playwright.
 */
public class CaptureHelpers {

    /**
     * Captures a screenshot using the given Playwright Page.
     * If the page or its context is closed, a warning is logged and the method returns.
     *
     * @param page       the Playwright Page instance.
     * @param screenName the name to use for the screenshot file and logs.
     */
    public static void captureScreenshot(Page page, String screenName) {
        try {
            if (page == null || page.context() == null) {
                LogUtils.warn("Page or its context is null; cannot capture screenshot for: " + screenName);
                return;
            }
            // Capture screenshot as a byte array.
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());

            // Define the output file path (adjust the path as needed).
            String filePath = System.getProperty("user.dir") + "/screenshots/" + screenName + ".png";
            File outputFile = new File(filePath);
            // Create the parent directories if they don't exist.
            outputFile.getParentFile().mkdirs();
            // Write the screenshot bytes to the file.
            FileUtils.writeByteArrayToFile(outputFile, screenshotBytes);
            LogUtils.info("Screenshot captured and saved for: " + screenName);

            // Optional: Attach the screenshot to Allure
            // Allure.addAttachment(screenName, new ByteArrayInputStream(screenshotBytes));

        } catch (IOException e) {
            LogUtils.error("IOException while saving screenshot for " + screenName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            // Check if the exception message indicates that the page or target is closed.
            if (msg.contains("Target closed") || msg.contains("page has been closed")) {
                LogUtils.warn("Page is already closed. Skipping screenshot for: " + screenName);
            } else {
                LogUtils.error("Exception while taking screenshot for " + screenName + ": " + msg, e);
            }
        }
    }
}
