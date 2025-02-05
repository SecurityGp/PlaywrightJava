package com.assured.listeners;

import com.assured.driver.PlaywrightDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import com.microsoft.playwright.Page;
import java.io.ByteArrayInputStream;

import static com.assured.constants.FrameworkConstants.*;

public class AllureListener implements TestLifecycleListener {

    // Remove empty overrides if not needed:
    // beforeTestSchedule, afterTestSchedule, beforeTestUpdate, afterTestUpdate,
    // beforeTestStart, afterTestStart, afterTestStop can be removed if not used.

    @Override
    public void beforeTestSchedule(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void afterTestSchedule(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void beforeTestUpdate(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void afterTestUpdate(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void beforeTestStart(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void afterTestStart(TestResult result) {
        // Removed since no additional behavior is needed.
    }

    @Override
    public void beforeTestStop(TestResult result) {
        Page page = PlaywrightDriverManager.getPage();
        if (page != null) {
            try {
                byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());
                if (SCREENSHOT_PASSED_TCS.equalsIgnoreCase(YES) && result.getStatus().equals(Status.PASSED)) {
                    Allure.addAttachment(result.getName() + "_Passed_Screenshot",
                            new ByteArrayInputStream(screenshotBytes));
                }
                if (SCREENSHOT_FAILED_TCS.equalsIgnoreCase(YES) && result.getStatus().equals(Status.FAILED)) {
                    Allure.addAttachment(result.getName() + "_Failed_Screenshot",
                            new ByteArrayInputStream(screenshotBytes));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Optionally, attach video if available:
        // AllureManager.addAttachmentVideoMP4();
    }

    // You can keep afterTestStop if needed (or remove if empty).
    @Override
    public void afterTestStop(TestResult result) {
        // Removed if not adding additional behavior.
    }
}
