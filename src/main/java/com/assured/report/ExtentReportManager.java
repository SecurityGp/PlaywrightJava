package com.assured.report;

import com.assured.constants.FrameworkConstants;
import com.assured.driver.PlaywrightDriverManager; // Updated to use Playwright's manager
import com.assured.enums.AuthorType;
import com.assured.enums.CategoryType;
import com.assured.utils.BrowserInfoUtils;
import com.assured.utils.DateUtils;
import com.assured.utils.IconUtils;
import com.assured.utils.LogUtils;
import com.assured.utils.ReportUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import java.io.File;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static String link = "";
    private static final String SCREENSHOT_ALL_STEPS = FrameworkConstants.SCREENSHOT_ALL_STEPS;

    public static void addScreenShot(String friendlyLocatorName) {
        if (!"yes".equalsIgnoreCase(SCREENSHOT_ALL_STEPS)) {
            return;
        }
        try {
            Page page = PlaywrightDriverManager.getPage();
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());
            String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(screenshotBytes);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentTestManager.getExtentTest().log(Status.INFO, friendlyLocatorName,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
            } else {
                LogUtils.warn("ExtentTest instance is null. Unable to add screenshot: " + friendlyLocatorName);
            }
            LogUtils.info("Screenshot added with friendly locator name: " + friendlyLocatorName);
        } catch (Exception e) {
            LogUtils.error("Error capturing screenshot: " + e.getMessage(), e);
        }
    }

    private static void addScreenshotToReport(String screenshotName) {
        if (!"yes".equalsIgnoreCase(SCREENSHOT_ALL_STEPS)) {
            return;
        }
        try {
            Page page = PlaywrightDriverManager.getPage();
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots", screenshotName + ".png")));
            AllureManager.takeScreenshotToAttachOnAllureReport();
            ExtentReportManager.addScreenShot(screenshotName);
        } catch (Exception e) {
            LogUtils.error("Failed to capture screenshot: " + e.getMessage());
        }
    }
    @Step("Double Click element using selector: {0}")
    public static void doubleClickElement(String selector) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            ExtentReportManager.info("Double clicking element with selector: " + selector);
            Locator locator = page.locator(selector);
            locator.dblclick();
            LogUtils.info("Clicked element with selector: " + selector);
            AllureManager.saveTextLog("Double clicked element with selector: " + selector);
            addScreenshotToReport("doubleClickElement_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to click element with selector: " + selector + " with error: " + e.getMessage(), e);
            AllureManager.saveTextLog("Failed to double click element with selector: " + selector + " with error: " + e.getMessage());
            ExtentReportManager.fail("Failed to double click element (selector: " + selector + ") | Error: " + e.getMessage());
            throw e;
        }
    }
}

