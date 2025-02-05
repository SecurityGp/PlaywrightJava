package com.assured.listeners;

import com.assured.annotations.FrameworkAnnotation;
import com.assured.constants.FrameworkConstants;
import com.assured.driver.PlaywrightDriverManager;
import com.assured.enums.AuthorType;
import com.assured.enums.Browser;
import com.assured.enums.CategoryType;
import com.assured.helpers.CaptureHelpers;
import com.assured.helpers.FileHelpers;
import com.assured.helpers.PropertiesHelpers;
import com.assured.report.AllureManager;
import com.assured.report.ExtentReportManager;
import com.assured.utils.BrowserInfoUtils;
import com.assured.utils.LogUtils;
import com.assured.utils.ZipUtils;
import com.aventstack.extentreports.Status;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import org.testng.*;
import org.testng.IInvokedMethodListener;
import org.testng.IInvokedMethod;

import java.io.IOException;

import static com.assured.constants.FrameworkConstants.*;

public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

    static int count_totalTCs;
    static int count_passedTCs;
    static int count_skippedTCs;
    static int count_failedTCs;

    // Convert these helper methods to static so they can be used in static context.
    public static String getTestName(ITestResult result) {
        return result.getTestName() != null
                ? result.getTestName()
                : result.getMethod().getConstructorOrMethod().getName();
    }

    public static String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null
                ? result.getMethod().getDescription()
                : getTestName(result);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Optional: Log before each method invocation.
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // Optional: Log after each method invocation.
    }

    @Override
    public void onStart(ISuite suite) {
        LogUtils.info("********** RUN STARTED **********");
        LogUtils.info("========= INSTALLING CONFIGURATION DATA =========");
        PropertiesHelpers.loadAllFiles();
        AllureManager.setAllureEnvironmentInformation();
        ExtentReportManager.initReports();
        LogUtils.info("========= CONFIGURATION DATA INSTALLED =========");
        LogUtils.info("=====> Starting Suite: " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        LogUtils.info("********** RUN FINISHED **********");
        LogUtils.info("=====> Ending Suite: " + suite.getName());
        ExtentReportManager.flushReports();
        ZipUtils.zipReportFolder();

        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Target Execution", FrameworkConstants.TARGET)
                        .put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT))
                        .put("Page Load Timeout", String.valueOf(FrameworkConstants.WAIT_PAGE_LOADED))
                        .put("Headless Mode", FrameworkConstants.HEADLESS)
                        .put("Local Browser", String.valueOf(Browser.CHROME))
                        .put("Remote URL", FrameworkConstants.REMOTE_URL)
                        .put("Remote Port", FrameworkConstants.REMOTE_PORT)
                        .put("TCs Total", String.valueOf(count_totalTCs))
                        .put("TCs Passed", String.valueOf(count_passedTCs))
                        .put("TCs Skipped", String.valueOf(count_skippedTCs))
                        .put("TCs Failed", String.valueOf(count_failedTCs))
                        .build()
        );

        FileHelpers.copyFile("src/test/resources/config/allure/categories.json",
                "target/allure-results/categories.json");
        FileHelpers.copyFile("src/test/resources/config/allure/executor.json",
                "target/allure-results/executor.json");
    }

    public static AuthorType[] getAuthorType(ITestResult result) {
        if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class) == null) {
            return null;
        }
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).author();
    }

    public static CategoryType[] getCategoryType(ITestResult result) {
        if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class) == null) {
            return null;
        }
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).category();
    }

    @Override
    public void onTestStart(ITestResult result) {
        LogUtils.info("Test case: " + getTestName(result) + " is starting...");
        count_totalTCs++;
        ExtentReportManager.createTest(result.getName());
        ExtentReportManager.addAuthors(getAuthorType(result));
        ExtentReportManager.addCategories(getCategoryType(result));
        ExtentReportManager.addDevices();
        ExtentReportManager.info(BrowserInfoUtils.getOSInfo());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("Test case: " + getTestName(result) + " passed.");
        count_passedTCs++;
        if (SCREENSHOT_PASSED_TCS.equals(YES)) {
            CaptureHelpers.captureScreenshot(PlaywrightDriverManager.getPage(), getTestName(result));
            ExtentReportManager.addScreenShot(Status.PASS, getTestName(result));
        }
        ExtentReportManager.logMessage(Status.PASS, "Test case: " + getTestName(result) + " passed.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.error("FAILED: Test case " + getTestName(result) + " failed.");
        LogUtils.error(result.getThrowable());
        count_failedTCs++;
        if (SCREENSHOT_FAILED_TCS.equals(YES)) {
            CaptureHelpers.captureScreenshot(PlaywrightDriverManager.getPage(), getTestName(result));
            ExtentReportManager.addScreenShot(Status.FAIL, getTestName(result));
        }
        ExtentReportManager.logMessage(Status.FAIL, result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.warn("WARNING: Test case: " + getTestName(result) + " is skipped.");
        count_skippedTCs++;
        if (SCREENSHOT_SKIPPED_TCS.equals(YES)) {
            CaptureHelpers.captureScreenshot(PlaywrightDriverManager.getPage(), getTestName(result));
        }
        ExtentReportManager.logMessage(Status.SKIP, "Test case: " + getTestName(result) + " is skipped.");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentReportManager.logMessage("Test failed but within defined success percentage: " + getTestName(result));
    }
}
