package com.assured.services;

import com.assured.driver.PlaywrightDriverManager;
import com.assured.driver.PlaywrightFactory;
import com.assured.enums.FailureHandling;
import com.assured.report.AllureManager;
import com.assured.report.ExtentReportManager;
import com.assured.utils.DateUtils;
import com.assured.utils.LogUtils;
import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.GetInboxRequest;
import com.manybrain.mailinator.client.message.GetLinksRequest;
import com.manybrain.mailinator.client.message.Inbox;
import com.manybrain.mailinator.client.message.Links;
import com.manybrain.mailinator.client.message.Message;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PageActions provides static methods for common Playwright interactions.
 * It retrieves the current Page instance from PlaywrightDriverManager.
 */
public class PageActions {

    // Soft assertions (optional usage)
    private static SoftAssert softAssert = new SoftAssert();

    /**
     * Retrieves the current Playwright Page from the driver manager.
     * Throws an exception if not found.
     */
    private static Page getPage() {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            throw new IllegalStateException("Playwright page is not initialized. "
                    + "Please ensure BaseTest creates a Page instance.");
        }
        return page;
    }

    /**
     * Navigates the current Page to the given URL using a ~60-second timeout.
     *
     * @param url the URL to navigate to.
     */
    @Step("Navigate to URL: {0}")
    public static void navigate(String url) {
        Page page = getPage();
        page.setDefaultNavigationTimeout(80000);

        LogUtils.info("Navigating to URL: " + url);
        ExtentReportManager.info("Navigating to URL: " + url); // <-- Extent log

        try {
            Page.NavigateOptions options = new Page.NavigateOptions()
                    .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                    .setWaitUntil(WaitUntilState.LOAD)
                    .setWaitUntil(WaitUntilState.NETWORKIDLE);
            page.navigate(url, options);

            AllureManager.saveTextLog("Navigated to URL: " + url);
            addScreenshotToReport("navigate_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Navigation failed with error: " + e.getMessage());
            AllureManager.saveTextLog("Navigation failed with error: " + e.getMessage());
            ExtentReportManager.fail("Navigation failed with error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fills the element identified by the given selector with the provided text.
     *
     * @param selector the selector of the element.
     * @param text     the text to fill.
     */
    @Step("Fill element using selector: {0} with text: {1}")
    public static void setText(String selector, String text) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Filling text into element with selector: " + selector
                    + " | Value: " + text);

            Locator locator = page.locator(selector);
            locator.fill(text);

            LogUtils.info("Filled element with selector: " + selector + " with text: " + text);
            AllureManager.saveTextLog("Filled element with selector: " + selector + " with text: " + text);

            addScreenshotToReport("setText_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to fill element with selector: " + selector
                    + " with error: " + e.getMessage(), e);
            AllureManager.saveTextLog("Failed to fill element with selector: " + selector
                    + " with error: " + e.getMessage());
            ExtentReportManager.fail("Failed to fill text in element (selector: " + selector
                    + ") | Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clicks the element identified by the given selector.
     *
     * @param selector the selector of the element.
     */
    @Step("Click element using selector: {0}")
    public static void clickElement(String selector) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Clicking element with selector: " + selector);

            Locator locator = page.locator(selector);
            locator.click();

            LogUtils.info("Clicked element with selector: " + selector);
            AllureManager.saveTextLog("Clicked element with selector: " + selector);

            addScreenshotToReport("clickElement_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to click element with selector: " + selector
                    + " with error: " + e.getMessage(), e);
            AllureManager.saveTextLog("Failed to click element with selector: " + selector
                    + " with error: " + e.getMessage());
            ExtentReportManager.fail("Failed to click element (selector: " + selector
                    + ") | Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Waits for the element identified by the selector to be clickable (visible and enabled).
     *
     * @param selector the selector of the element.
     */
    @Step("Wait for element clickable: {0}")
    public static void waitForElementClickable(String selector) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Waiting for element to be clickable: " + selector);

            Locator locator = page.locator(selector);
            // Wait until the element is visible
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));

            // Poll until enabled
            int retries = 0;
            while (!locator.isEnabled() && retries < 10) {
                Thread.sleep(500);
                retries++;
            }
            LogUtils.info("Element is clickable: " + selector);
            AllureManager.saveTextLog("Element is clickable: " + selector);

            addScreenshotToReport("waitForElementClickable_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Element not clickable: " + selector + " with error: " + e.getMessage());
            AllureManager.saveTextLog("Element not clickable: " + selector + " with error: " + e.getMessage());
            ExtentReportManager.fail("Element not clickable (selector: " + selector
                    + ") | Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that the element is present (i.e., visible) in the DOM.
     */
    @Step("Verify element is present (visible): {0}")
    public static boolean verifyElementIsPresent(String selector, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying element is present (visible): " + selector);

            Locator locator = page.locator(selector);
            // Wait briefly for it to become visible
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

            boolean isVisible = locator.isVisible();

            if (isVisible) {
                ExtentReportManager.pass("Element is present (selector: " + selector + ")");
                LogUtils.info("Element is present: " + selector);
            } else {
                ExtentReportManager.fail("Element is NOT present (selector: " + selector + ")");
                LogUtils.error("❌ Element is not present: " + selector);
            }

            // Handle flow control
            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(isVisible, "❌ Element with selector '" + selector + "' is not present.");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(isVisible, "❌ Element with selector '" + selector + "' is not present.");
            }
            addScreenshotToReport("verifyElementIsPresent_" + DateUtils.getCurrentDateTime());
            return isVisible;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementIsPresent: " + e.getMessage());
            LogUtils.error("Exception in verifyElementIsPresent: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementIsPresent: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element is NOT present (i.e., not visible).
     */
    @Step("Verify element is not present (not visible): {0}")
    public static boolean verifyElementIsNotPresent(String selector, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying element is NOT present (not visible): " + selector);

            Locator locator = page.locator(selector);
            // If the element might appear briefly, we can do a short wait for it to be hidden
            boolean notVisible;
            try {
                locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(5000));
                notVisible = true; // If it successfully waited for hidden, it's not visible
            } catch (Exception ex) {
                // If the waitFor hidden times out or fails, check isVisible
                notVisible = !locator.isVisible();
            }

            if (notVisible) {
                ExtentReportManager.pass("Element is not present (selector: " + selector + ")");
                LogUtils.info("Element is not present: " + selector);
            } else {
                ExtentReportManager.fail("Element IS present (selector: " + selector + ")");
                LogUtils.error("❌ Element is present (should not be): " + selector);
            }

            // Handle flow control
            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(notVisible, "❌ Element with selector '" + selector + "' is present, but shouldn't be.");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(notVisible, "❌ Element with selector '" + selector + "' is present, but shouldn't be.");
            }
            addScreenshotToReport("verifyElementIsNotPresent_" + DateUtils.getCurrentDateTime());
            return notVisible;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementIsNotPresent: " + e.getMessage());
            LogUtils.error("Exception in verifyElementIsNotPresent: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementIsNotPresent: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element’s text exactly equals the expected text.
     */
    @Step("Verify text of element {0} equals: {1}")
    public static boolean verifyElementTextEquals(String selector, String expectedText, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying text of element " + selector
                    + " equals: " + expectedText);

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = actualText.equals(expectedText.trim());

            if (result) {
                ExtentReportManager.pass("Text equals success. Actual: " + actualText
                        + " | Expected: " + expectedText);
                LogUtils.info("Verify text equals: " + result);
            } else {
                ExtentReportManager.fail("Text equals check failed. Actual: " + actualText
                        + " | Expected: " + expectedText);
                LogUtils.error("❌ Verify text equals: " + result);
            }

            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertEquals(actualText, expectedText.trim(),
                        "❌ The actual text is '" + actualText
                                + "' not equals '" + expectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertEquals(actualText, expectedText.trim(),
                        "❌ The actual text is '" + actualText
                                + "' not equals '" + expectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.OPTIONAL)) {
                AllureManager.saveTextLog("Verify text equals - " + result
                        + ". The actual text is '" + actualText
                        + "' not equals '" + expectedText.trim() + "'");
            }

            addScreenshotToReport("verifyElementTextEquals_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementTextEquals: " + e.getMessage());
            LogUtils.error("Exception in verifyElementTextEquals: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementTextEquals: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element’s text contains the expected text.
     */
    @Step("Verify text of element {0} contains: {1}")
    public static boolean verifyElementTextContains(String selector, String expectedText, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying text of element " + selector
                    + " contains: " + expectedText);

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = actualText.contains(expectedText.trim());

            if (result) {
                ExtentReportManager.pass("Text contains success. Actual: " + actualText
                        + " | Expected substring: " + expectedText);
                LogUtils.info("Verify text contains: " + result);
            } else {
                ExtentReportManager.fail("Text contains check failed. Actual: " + actualText
                        + " | Expected substring: " + expectedText);
                LogUtils.error("❌ Verify text contains: " + result);
            }

            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(result,
                        "❌ The actual text is '" + actualText
                                + "' does not contain '" + expectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(result,
                        "❌ The actual text is '" + actualText
                                + "' does not contain '" + expectedText.trim() + "'");
            }

            AllureManager.saveTextLog("Verify text contains - " + result
                    + ". The actual text is '" + actualText
                    + "' does not contain '" + expectedText.trim() + "'");
            addScreenshotToReport("verifyElementTextContains_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementTextContains: " + e.getMessage());
            LogUtils.error("Exception in verifyElementTextContains: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementTextContains: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Uploads a file to the element (e.g., an input[type=file]) identified by the selector.
     */
    @Step("Upload file using selector: {0} with file: {1}")
    public static void uploadFile(String selector, String filePath) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Uploading file to selector: " + selector
                    + " | filePath: " + filePath);

            Locator locator = page.locator(selector);
            locator.setInputFiles(Paths.get(filePath));
            LogUtils.info("Uploaded file using selector: " + selector + " with file: " + filePath);

            AllureManager.saveTextLog("Uploaded file using selector: " + selector + " with file: " + filePath);
            addScreenshotToReport("uploadFile_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to upload file using selector: " + selector
                    + " with error: " + e.getMessage());
            AllureManager.saveTextLog("Failed to upload file using selector: " + selector
                    + " with error: " + e.getMessage());
            ExtentReportManager.fail("Failed to upload file (selector: " + selector
                    + ") | Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element’s text does NOT contain the specified text.
     */
    @Step("Verify text of element {0} does not contain: {1}")
    public static boolean verifyElementTextNotContains(String selector, String notExpectedText, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying text of element " + selector
                    + " does NOT contain: " + notExpectedText);

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = !actualText.contains(notExpectedText.trim());

            if (result) {
                ExtentReportManager.pass("Text not-contains check success. Actual text: " + actualText
                        + " | Not-Expected: " + notExpectedText);
                LogUtils.info("Verify text not contains: " + result);
            } else {
                ExtentReportManager.fail("Text not-contains check failed. Actual: " + actualText
                        + " | Disallowed substring: " + notExpectedText);
                LogUtils.error("❌ Verify text not contains: " + result);
            }

            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertFalse(actualText.contains(notExpectedText.trim()),
                        "❌ The actual text is '" + actualText
                                + "' contains '" + notExpectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertFalse(actualText.contains(notExpectedText.trim()),
                        "❌ The actual text is '" + actualText
                                + "' contains '" + notExpectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.OPTIONAL)) {
                AllureManager.saveTextLog("Verify text not contains - " + result
                        + ". The actual text is '" + actualText
                        + "' contains '" + notExpectedText.trim() + "'");
            }
            addScreenshotToReport("verifyElementTextNotContains_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementTextNotContains: " + e.getMessage());
            LogUtils.error("Exception in verifyElementTextNotContains: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementTextNotContains: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element is enabled.
     */
    @Step("Verify element is enabled: {0}")
    public static boolean verifyElementIsEnabled(String selector, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying element is enabled: " + selector);

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            boolean isEnabled = locator.isEnabled();

            if (isEnabled) {
                ExtentReportManager.pass("Element is enabled (selector: " + selector + ")");
                LogUtils.info("Element is enabled: " + selector);
            } else {
                ExtentReportManager.fail("Element is NOT enabled (selector: " + selector + ")");
                LogUtils.error("❌ Element is not enabled: " + selector);
            }

            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(isEnabled, "❌ Element with selector '" + selector + "' is not enabled.");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(isEnabled, "❌ Element with selector '" + selector + "' is not enabled.");
                AllureManager.saveTextLog("Verify element is enabled - " + isEnabled);
            }
            addScreenshotToReport("verifyElementIsEnabled_" + DateUtils.getCurrentDateTime());
            return isEnabled;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementIsEnabled: " + e.getMessage());
            LogUtils.error("Exception in verifyElementIsEnabled: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementIsEnabled: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element is disabled.
     */
    @Step("Verify element is disabled: {0}")
    public static boolean verifyElementIsDisabled(String selector, FailureHandling flowControl) {
        Page page = getPage();
        try {
            ExtentReportManager.info("Verifying element is disabled: " + selector);

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            boolean isDisabled = !locator.isEnabled();

            if (isDisabled) {
                ExtentReportManager.pass("Element is disabled (selector: " + selector + ")");
                LogUtils.info("Element is disabled: " + selector);
            } else {
                ExtentReportManager.fail("Element is NOT disabled (selector: " + selector + ")");
                LogUtils.error("❌ Element is not disabled: " + selector);
            }

            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(isDisabled, "❌ Element with selector '" + selector + "' is not disabled.");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(isDisabled, "❌ Element with selector '" + selector + "' is not disabled.");
                AllureManager.saveTextLog("Verify element is disabled - " + isDisabled);
            }
            addScreenshotToReport("verifyElementIsDisabled_" + DateUtils.getCurrentDateTime());
            return isDisabled;
        } catch (Exception e) {
            ExtentReportManager.fail("Exception in verifyElementIsDisabled: " + e.getMessage());
            LogUtils.error("Exception in verifyElementIsDisabled: " + e.getMessage());
            AllureManager.saveTextLog("Exception in verifyElementIsDisabled: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Closes the current browser context.
     */
    @Step("Close the browser")
    public static void closeBrowser() {
        try {
            Page page = getPage();
            BrowserContext context = page.context();

            ExtentReportManager.info("Closing the browser for page: " + page);
            LogUtils.info("Closing the browser");
            context.close();

            AllureManager.saveTextLog("Browser closed successfully.");

        } catch (Exception e) {
            LogUtils.error("Failed to close browser with error: " + e.getMessage());
            AllureManager.saveTextLog("Failed to close browser with error: " + e.getMessage());
            ExtentReportManager.fail("Failed to close browser | Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opens a new browser instance, performs the provided actions, closes that browser,
     * and then restores the original browser session.
     *
     * @param action A Runnable containing the actions to perform on the new browser.
     */
    @Step("Open a new browser instance, perform actions, then return to original browser")
    public static void openNewBrowserAndPerformAction(Runnable action) {

        Page originalPage = PlaywrightDriverManager.getPage();

        try {
            ExtentReportManager.info("Opening a new browser instance to perform additional actions");

            Page newPage = PlaywrightFactory.createPage(false);
            PlaywrightDriverManager.setPage(newPage);

            LogUtils.info("Opened new browser instance for additional actions.");
            AllureManager.saveTextLog("Opened new browser instance for additional actions.");
            addScreenshotToReport("newBrowserOpened_" + DateUtils.getCurrentDateTime());

            action.run();

            addScreenshotToReport("actionsPerformedInNewBrowser_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Error during actions in new browser: " + e.getMessage(), e);
            AllureManager.saveTextLog("Error during actions in new browser: " + e.getMessage());
            ExtentReportManager.fail("Error during actions in new browser | " + e.getMessage());
            throw new RuntimeException(e);
        } finally {

            PlaywrightFactory.quit();


            PlaywrightDriverManager.setPage(originalPage);

            LogUtils.info("Returned to original browser instance.");
            AllureManager.saveTextLog("Returned to original browser instance.");
            ExtentReportManager.info("Returned to original browser instance.");
            addScreenshotToReport("returnedToOriginalBrowser_" + DateUtils.getCurrentDateTime());
        }
    }

    /**
     * Switches to the newly opened tab, performs the provided action, and returns to the original tab.
     */
    @Step("Switch to newly opened tab, perform action, and return to original tab")
    public static void switchToNewTabAndPerformAction(Runnable action) {
        try {
            ExtentReportManager.info("Switching to newly opened tab to perform action");

            Page originalPage = PlaywrightDriverManager.getPage();
            BrowserContext context = originalPage.context();


            Thread.sleep(1000);
            List<Page> pages = context.pages();
            Page newPage = null;
            for (Page p : pages) {
                if (!p.equals(originalPage)) {
                    newPage = p;
                    break;
                }
            }
            if (newPage == null) {
                throw new RuntimeException("No new tab found");
            }

            PlaywrightDriverManager.setPage(newPage);
            LogUtils.info("Switched to new tab");
            AllureManager.saveTextLog("Switched to new tab");
            addScreenshotToReport("switchToNewTab_" + DateUtils.getCurrentDateTime());


            action.run();
            addScreenshotToReport("actionOnNewTab_" + DateUtils.getCurrentDateTime());


            PlaywrightDriverManager.setPage(originalPage);
            LogUtils.info("Returned to original tab");
            AllureManager.saveTextLog("Returned to original tab");
            ExtentReportManager.info("Returned to original tab");
            addScreenshotToReport("returnToOriginalTab_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Error in switching tabs: " + e.getMessage());
            AllureManager.saveTextLog("Error in switching tabs: " + e.getMessage());
            ExtentReportManager.fail("Error in switching tabs | " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the URL from a matching message in the specified Mailinator inbox.
     * It looks for a message whose "From" address and "Subject" match the expected values.
     * If found, it attempts to extract the invite URL from the subject or the links API.
     *
     * @param domain          The Mailinator domain (e.g., "private").
     * @param mailbox         The mailbox (e.g., "abc").
     * @param expectedFrom    The expected sender email address (e.g., "no-reply@withassured.com").
     * @param expectedSubject The expected subject or subject substring (e.g., "Onboarding Invite").
     * @return The extracted URL, or null if not found.
     */
    @Step("Retrieve mail URL for domain: {0}, mailbox: {1}, expectedFrom: {2}, expectedSubject: {3}")
    public static String getMailUrl(String domain, String mailbox, String expectedFrom, String expectedSubject) {
        String url = null;
        String apiKey = "947fc29e9d3b4c4b80be0e65f27fd8db"; // Example API key
        try {
            ExtentReportManager.info("Retrieving mail URL for domain: " + domain
                    + ", mailbox: " + mailbox
                    + ", expectedFrom: " + expectedFrom
                    + ", expectedSubject: " + expectedSubject);

            LogUtils.info("Requesting inbox for domain: " + domain);
            AllureManager.saveTextLog("Requesting inbox for domain: " + domain);

            MailinatorClient mailinatorClient = new MailinatorClient(apiKey);
            Inbox inbox = mailinatorClient.request(new GetInboxRequest(domain));
            List<Message> messages = inbox.getMsgs();

            LogUtils.info("Inbox received with " + messages.size() + " messages");
            AllureManager.saveTextLog("Inbox received with " + messages.size() + " messages");

            if (messages.isEmpty()) {
                LogUtils.warn("No messages found in inbox for domain: " + domain);
                AllureManager.saveTextLog("No messages found in inbox for domain: " + domain);
                ExtentReportManager.info("No messages found in mailbox: " + mailbox);
                return null;
            }


            Message matchingMessage = null;
            for (Message msg : messages) {
                String from = msg.getFrom();
                String subject = msg.getSubject();
                // If "from" and "subject" both match your expected criteria
                if (from != null
                        && from.equalsIgnoreCase(expectedFrom)
                        && subject != null
                        && subject.contains(expectedSubject)) {

                    matchingMessage = msg;
                    break;
                }
            }


            if (matchingMessage == null) {
                String warnMsg = String.format(
                        "No matching email found. Expected from='%s', subject containing='%s'",
                        expectedFrom, expectedSubject
                );
                LogUtils.warn(warnMsg);
                AllureManager.saveTextLog(warnMsg);
                ExtentReportManager.fail(warnMsg);
                return null;
            }


            String messageId = matchingMessage.getId();
            String subject = matchingMessage.getSubject();
            String from = matchingMessage.getFrom();

            LogUtils.info("Matched message -> ID: " + messageId
                    + ", From: " + from
                    + ", Subject: " + subject);
            AllureManager.saveTextLog("Matched message -> ID: " + messageId
                    + ", From: " + from
                    + ", Subject: " + subject);
            ExtentReportManager.info("Matched message details -> ID: " + messageId
                    + ", From: " + from
                    + ", Subject: " + subject);


            url = extractUrlFromSubject(subject);
            if (url != null) {
                LogUtils.info("Extracted URL from subject: " + url);
                AllureManager.saveTextLog("Extracted URL from subject: " + url);
                ExtentReportManager.info("Mail invite URL extracted from subject: " + url);
                return url;
            } else {

                LogUtils.warn("No URL found in subject. Trying links API for message id: " + messageId);
                AllureManager.saveTextLog("No URL found in subject. Trying links API for message id: " + messageId);
                ExtentReportManager.info("No URL in subject, checking links API...");

                Links linksResponse = mailinatorClient.request(new GetLinksRequest(domain, mailbox, messageId));
                List<String> links = linksResponse.getLinks();
                if (links != null && !links.isEmpty()) {
                    url = links.get(0);
                    LogUtils.info("Retrieved URL from links API: " + url);
                    AllureManager.saveTextLog("Retrieved URL from links API: " + url);
                    ExtentReportManager.info("Mail invite URL retrieved from links API: " + url);
                } else {
                    String noUrlErr = "No URL found from links API for message id: " + messageId;
                    LogUtils.error(noUrlErr);
                    AllureManager.saveTextLog(noUrlErr);
                    ExtentReportManager.fail(noUrlErr);
                }
            }
        } catch (Exception e) {
            LogUtils.error("Exception occurred while retrieving mail URL: " + e.getMessage(), e);
            AllureManager.saveTextLog("Exception occurred while retrieving mail URL: " + e.getMessage());
            ExtentReportManager.fail("Exception retrieving mail URL: " + e.getMessage());
            throw e;
        }
        return url;
    }

    /**
     * Helper method to extract the first URL found in the subject string.
     */
    private static String extractUrlFromSubject(String subject) {
        String urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+(/\\S*)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(subject);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }


    /**
     * Utility method to capture a screenshot and attach it to both Allure and Extent.
     *
     * @param screenshotName the name for the screenshot file.
     */
    private static void addScreenshotToReport(String screenshotName) {
        try {
            Page page = getPage();
            // Save screenshot to disk
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots", screenshotName + ".png")));

            // Attach to Allure
            AllureManager.takeScreenshotToAttachOnAllureReport();

            // Attach to Extent
            ExtentReportManager.addScreenShot(screenshotName);
        } catch (Exception e) {
            LogUtils.error("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
