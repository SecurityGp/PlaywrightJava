package com.assured.report;

import com.aventstack.extentreports.ExtentTest;

/**
 * ExtentTestManager manages the ExtentTest instance for each test thread.
 * It ensures that in parallel test execution each thread holds its own ExtentTest object.
 */
public final class ExtentTestManager {

    // ThreadLocal storage for ExtentTest instances.
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Private constructor to prevent instantiation.
    private ExtentTestManager() {}

    /**
     * Returns the current thread's ExtentTest instance.
     *
     * @return The ExtentTest for the current thread.
     */
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    /**
     * Assigns an ExtentTest instance to the current thread.
     *
     * @param test The ExtentTest instance to be stored.
     */
    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    /**
     * Clears the ExtentTest instance from the current thread.
     * Should be called after test completion.
     */
    public static void unload() {
        extentTest.remove();
    }
}
