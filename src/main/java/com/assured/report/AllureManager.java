package com.assured.report;

import com.assured.constants.FrameworkConstants;
import com.assured.driver.PlaywrightDriverManager;
import com.assured.enums.Browser;
import com.assured.helpers.FileHelpers;
import com.assured.utils.BrowserInfoUtils;
import com.assured.utils.LogUtils;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import com.microsoft.playwright.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.assured.constants.FrameworkConstants.EXPORT_VIDEO_PATH;

public class AllureManager {

    private AllureManager() {
    }

    /**
     * Writes environment information to the Allure report.
     */
    public static void setAllureEnvironmentInformation() {
        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Test URL", FrameworkConstants.URL_STAGING)
                        .put("Target Execution", FrameworkConstants.TARGET)
                        .put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT))
                        .put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED))
                        .put("Headless Mode", FrameworkConstants.HEADLESS)
                        // You may wish to update the browser information based on your Playwright configuration.
                        .put("Local Browser", String.valueOf(Browser.CHROME))
                        .put("Remote URL", FrameworkConstants.REMOTE_URL)
                        .put("Remote Port", FrameworkConstants.REMOTE_PORT)
                        .build());

        System.out.println("Allure Reports is installed.");
    }

    /**
     * Attaches a screenshot for a failed test to the Allure report.
     *
     * @return A byte array representing the screenshot.
     */
    @Attachment(value = "Failed test Screenshot", type = "image/png")
    public static byte[] takeScreenshotToAttachOnAllureReport() {
        try {
            // Obtain the current Playwright Page.
            Page page = PlaywrightDriverManager.getPage();
            // Using Playwright's screenshot API which returns a byte array.
            return page.screenshot(new Page.ScreenshotOptions());
        } catch (Exception ex) {
            LogUtils.error("Error capturing screenshot for failed test: " + ex.getMessage(), ex);
        }
        return new byte[0];
    }

    /**
     * Attaches a screenshot taken at a specific test step to the Allure report.
     *
     * @return A byte array representing the screenshot.
     */
    @Attachment(value = "Step Screenshot", type = "image/png")
    public static byte[] takeScreenshotStep() {
        try {
            Page page = PlaywrightDriverManager.getPage();
            return page.screenshot(new Page.ScreenshotOptions());
        } catch (Exception ex) {
            LogUtils.error("Error capturing step screenshot: " + ex.getMessage(), ex);
        }
        return new byte[0];
    }

    /**
     * Attaches browser information to the Allure report.
     *
     * @return A text string containing OS and browser information.
     */
    @Attachment(value = "Browser Information", type = "text/plain")
    public static String addBrowserInformationOnAllureReport() {
        return BrowserInfoUtils.getOSInfo();
    }

    /**
     * Attaches a text log to the Allure report.
     *
     * @param message The log message.
     * @return The same log message.
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    /**
     * Attaches HTML content to the Allure report.
     *
     * @param html The HTML content.
     * @return The same HTML content.
     */
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    /**
     * Attaches an AVI video record to the Allure report if available.
     */
    public static void addAttachmentVideoAVI() {
        if (FrameworkConstants.VIDEO_RECORD.toLowerCase().trim().equals(FrameworkConstants.YES)) {
            try {
                // Get the most recently modified video file from the export path.
                File video = FileHelpers.getFileLastModified(EXPORT_VIDEO_PATH);
                if (video != null) {
                    Allure.addAttachment("Video record AVI", "video/avi", new FileInputStream(video), "avi");
                } else {
                    LogUtils.warn("Video record not found. Cannot attach video in Allure report.");
                }
            } catch (IOException e) {
                LogUtils.error("Cannot attach AVI video in Allure report", e);
            }
        }
    }

    /**
     * Attaches an MP4 video record to the Allure report if available.
     */
    public static void addAttachmentVideoMP4() {
        try {
            File video = FileHelpers.getFileLastModified(EXPORT_VIDEO_PATH);
            if (video != null) {
                Allure.addAttachment("Video record MP4", "video/mp4", new FileInputStream(video), "mp4");
            } else {
                LogUtils.warn("Video record not found. Cannot attach video in Allure report.");
            }
        } catch (IOException e) {
            LogUtils.error("Cannot attach MP4 video in Allure report", e);
        }
    }
}
