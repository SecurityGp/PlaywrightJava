package com.assured.listeners;

import com.assured.constants.FrameworkConstants;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    private int count = 0;
    private static int maxTry = Integer.parseInt(FrameworkConstants.RETRY_TEST_FAIL);

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {                      // If test did not succeed
            if (count < maxTry) {                            // and max retries not reached
                count++;                                     // increase counter
                iTestResult.setStatus(ITestResult.FAILURE);  // mark test as failed
                return true;                                 // retry the test
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);  // if max reached, leave as failure
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);      // if passed, mark as success
        }
        return false;
    }
}
