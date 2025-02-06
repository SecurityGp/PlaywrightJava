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
import com.microsoft.playwright.Page;
import java.util.Base64;

import java.io.File;
import java.util.Objects;

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static String link = "";

    public static void initReports() {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();

            if (FrameworkConstants.OVERRIDE_REPORTS.trim().equalsIgnoreCase(FrameworkConstants.NO)) {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FOLDER_PATH + File.separator +
                        DateUtils.getCurrentDateTimeCustom("_") + "_" + FrameworkConstants.EXTENT_REPORT_FILE_NAME;
                LogUtils.info("Link Extent Report: " + link);
            } else {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FILE_PATH;
                LogUtils.info("Link Extent Report: " + link);
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(link);
            extentReports.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
            spark.config().setReportName(FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Framework Name", FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Author", FrameworkConstants.AUTHOR);

            LogUtils.info("Extent Reports is installed.");
        }
    }

    public static void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentTestManager.unload();
        ReportUtils.openReports(link);
    }

    public static void createTest(String testCaseName) {
        ExtentTestManager.setExtentTest(
                extentReports.createTest(IconUtils.getBrowserIcon() + " " + testCaseName)
        );
        LogUtils.info("Created test: " + testCaseName);
    }

    public static void createTest(String testCaseName, String description) {
        ExtentTestManager.setExtentTest(
                extentReports.createTest(testCaseName, description)
        );
        LogUtils.info("Created test: " + testCaseName + " with description: " + description);
    }

    public static void removeTest(String testCaseName) {
        extentReports.removeTest(testCaseName);
        LogUtils.info("Removed test: " + testCaseName);
    }

    /**
     * Adds a screenshot to the report.
     * The provided 'message' parameter is used as the friendly name for the locator/step.
     */
    public static void addScreenShot(String friendlyLocatorName) {
        try {
            Page page = PlaywrightDriverManager.getPage();
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());
            // Convert the byte array to Base64 string
            String base64Image = "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(screenshotBytes);
            // Log the screenshot with the provided friendly name
            ExtentTestManager.getExtentTest().log(Status.INFO, friendlyLocatorName,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
            LogUtils.info("Screenshot added with friendly locator name: " + friendlyLocatorName);
        } catch (Exception e) {
            LogUtils.error("Error capturing screenshot: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a screenshot with a specified status to the report.
     * The 'message' parameter is used as the friendly name for the locator/step.
     */
    public static void addScreenShot(Status status, String screenshotName) {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            LogUtils.error("Cannot capture screenshot because the Page is not initialized.");
            return;
        }

        try {
            // Optionally, create a more unique screenshot name (e.g., appending a timestamp)
            // or iteration detail to differentiate multiple screenshots in the same test.
            String uniqueScreenshotName = screenshotName + " - " + System.currentTimeMillis();

            // Capture the screenshot
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions()
                    // Uncomment if you want the full page or specify other screenshot settings
                    // .setFullPage(true)
            );

            // Convert the byte array to a Base64 string
            String base64Image = "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(screenshotBytes);

            // Log the screenshot in Extent with a Base64 string
            ExtentTestManager.getExtentTest()
                    .log(status, uniqueScreenshotName,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image)
                                    .build());

            LogUtils.info("Screenshot added with status: " + status
                    + " | Name: " + uniqueScreenshotName);
        } catch (Exception e) {
            LogUtils.error("Error capturing screenshot: " + e.getMessage(), e);
        }
    }


    public static synchronized void addAuthors(AuthorType[] authors) {
        if (authors == null) {
            ExtentTestManager.getExtentTest().assignAuthor("Gp");
            LogUtils.info("Assigned default author: Gp");
        } else {
            for (AuthorType author : authors) {
                ExtentTestManager.getExtentTest().assignAuthor(author.toString());
                LogUtils.info("Assigned author: " + author);
            }
        }
    }

    public static synchronized void addCategories(CategoryType[] categories) {
        if (categories == null) {
            ExtentTestManager.getExtentTest().assignCategory("REGRESSION");
            LogUtils.info("Assigned default category: REGRESSION");
        } else {
            for (CategoryType category : categories) {
                ExtentTestManager.getExtentTest().assignCategory(category.toString());
                LogUtils.info("Assigned category: " + category);
            }
        }
    }

    public static synchronized void addDevices() {
        String deviceInfo = BrowserInfoUtils.getBrowserInfo();
        ExtentTestManager.getExtentTest().assignDevice(deviceInfo);
        LogUtils.info("Assigned device info: " + deviceInfo);
    }

    public static void logMessage(String message) {
        ExtentTestManager.getExtentTest().log(Status.INFO, message);
        LogUtils.info("Log message: " + message);
    }

    public static void logMessage(Status status, String message) {
        ExtentTestManager.getExtentTest().log(status, message);
        LogUtils.info("Log message with status " + status + ": " + message);
    }

    public static void logMessage(Status status, Object message) {
        ExtentTestManager.getExtentTest().log(status, (Throwable) message);
        LogUtils.error("Log exception with status " + status + ": " + message);
    }

    public static void pass(String message) {
        ExtentTestManager.getExtentTest().pass(message);
        LogUtils.info("Test passed: " + message);
    }

    public static void pass(Markup message) {
        ExtentTestManager.getExtentTest().pass(message);
        LogUtils.info("Test passed: " + message.getMarkup());
    }

    public static void fail(String message) {
        ExtentTestManager.getExtentTest().fail(message);
        LogUtils.error("Test failed: " + message);
    }

    public static void fail(Object message) {
        ExtentTestManager.getExtentTest().fail((String) message);
        LogUtils.error("Test failed: " + message);
    }

    public static void fail(Markup message) {
        ExtentTestManager.getExtentTest().fail(message);
        LogUtils.error("Test failed: " + message.getMarkup());
    }

    public static void skip(String message) {
        ExtentTestManager.getExtentTest().skip(message);
        LogUtils.info("Test skipped: " + message);
    }

    public static void skip(Markup message) {
        ExtentTestManager.getExtentTest().skip(message);
        LogUtils.info("Test skipped: " + message.getMarkup());
    }

    public static void info(Markup message) {
        ExtentTestManager.getExtentTest().info(message);
        LogUtils.info("Test info: " + message.getMarkup());
    }

    public static void info(String message) {
        ExtentTestManager.getExtentTest().info(message);
        LogUtils.info("Test info: " + message);
    }

    public static void warning(String message) {
        ExtentTestManager.getExtentTest().log(Status.WARNING, message);
        LogUtils.warn("Test warning: " + message);
    }
}
