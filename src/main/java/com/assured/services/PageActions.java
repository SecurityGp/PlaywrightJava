package com.assured.services;

import com.assured.driver.PlaywrightFactory;
import com.assured.enums.FailureHandling;
import com.assured.report.AllureManager;
import com.assured.report.ExtentReportManager;
import com.assured.driver.PlaywrightDriverManager;
import com.assured.utils.DateUtils;
import com.assured.utils.LogUtils;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.*;
import com.manybrain.mailinator.client.message.GetInboxRequest;
import com.manybrain.mailinator.client.message.GetLinksRequest;
import com.manybrain.mailinator.client.message.Links;
import com.manybrain.mailinator.client.message.Message;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.assured.report.ExtentReportManager.initReports;

/**
 * PageActions provides static methods for common Playwright interactions.
 * It retrieves the current Page instance from PlaywrightDriverManager.
 */
public class PageActions {

    /**
     * Retrieves the current Playwright Page from the driver manager.
     * If the Page is not initialized, throws an IllegalStateException.
     *
     * @return the active Playwright Page.
     */
    private static Page getPage() {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            throw new IllegalStateException("Playwright page is not initialized. Please ensure BaseTest creates a Page instance.");
        }
        return page;
    }

    // Assuming you have a soft assertion mechanism available.
    private static SoftAssert softAssert = new SoftAssert();

    /**
     * Navigates the current Page to the given URL using a 60-second timeout.
     *
     * @param url the URL to navigate to.
     */
    @Step("Navigate to URL: {0}")
    public static void navigate(String url) {
        Page page = PlaywrightDriverManager.getPage();
        if (page == null) {
            throw new IllegalStateException("Page instance is null. Ensure the driver is initialized.");
        }
        page.setDefaultNavigationTimeout(80000);
        LogUtils.info("Navigating to URL: " + url);
        try {
            Page.NavigateOptions options = new Page.NavigateOptions();
            options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
            options.setWaitUntil(WaitUntilState.LOAD);
            options.setWaitUntil(WaitUntilState.NETWORKIDLE);
            page.navigate(url, options);
            AllureManager.saveTextLog("Navigated to URL: " + url);
            addScreenshotToReport("navigate_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Navigation failed with error: " + e.getMessage());

            AllureManager.saveTextLog("Navigation failed with error: " + e.getMessage());
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
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.fill(text);
            LogUtils.info("Filled element with selector: " + selector + " with text: " + text);

            AllureManager.saveTextLog("Filled element with selector: " + selector + " with text: " + text);
            addScreenshotToReport("setText_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to fill element with selector: " + selector + " with error: " + e.getMessage(), e);

            AllureManager.saveTextLog("Failed to fill element with selector: " + selector + " with error: " + e.getMessage());
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
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.click();
            LogUtils.info("Clicked element with selector: " + selector);

            AllureManager.saveTextLog("Clicked element with selector: " + selector);
            addScreenshotToReport("clickElement_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to click element with selector: " + selector + " with error: " + e.getMessage(), e);

            AllureManager.saveTextLog("Failed to click element with selector: " + selector + " with error: " + e.getMessage());
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
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            // Wait until the element is visible
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
            // Poll until enabled (Playwright does not have a direct "clickable" state)
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that the element’s text exactly equals the expected text.
     *
     * @param selector     the selector of the element.
     * @param expectedText the expected text.
     * @param flowControl  failure handling schema.
     * @return true if equals; otherwise, false.
     */
    @Step("Verify text of element {0} equals: {1}")
    public static boolean verifyElementTextEquals(String selector, String expectedText, FailureHandling flowControl) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = actualText.equals(expectedText.trim());
            if (result) {
                LogUtils.info("Verify text equals: " + result);

            } else {
                LogUtils.error("❌ Verify text equals: " + result);

            }
            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertEquals(actualText, expectedText.trim(),
                        "❌ The actual text is '" + actualText + "' not equals '" + expectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertEquals(actualText, expectedText.trim(),
                        "❌ The actual text is '" + actualText + "' not equals '" + expectedText.trim() + "'");
                if (!result) {

                }
            } else if (flowControl.equals(FailureHandling.OPTIONAL)) {

                AllureManager.saveTextLog("Verify text equals - " + result + ". The actual text is '" + actualText + "' not equals '" + expectedText.trim() + "'");
            }
            addScreenshotToReport("verifyElementTextEquals_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            LogUtils.error("Exception in verifyElementTextEquals: " + e.getMessage());

            AllureManager.saveTextLog("Exception in verifyElementTextEquals: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element’s text contains the expected text.
     *
     * @param selector     the selector of the element.
     * @param expectedText the text that should be contained.
     * @param flowControl  failure handling schema.
     * @return true if contains; otherwise, false.
     */
    @Step("Verify text of element {0} contains: {1}")
    public static boolean verifyElementTextContains(String selector, String expectedText, FailureHandling flowControl) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = actualText.contains(expectedText.trim());
            if (result) {
                LogUtils.info("Verify text contains: " + result);

            } else {
                LogUtils.error("❌ Verify text contains: " + result);

            }
            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertTrue(result,
                        "❌ The actual text is '" + actualText + "' does not contain '" + expectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertTrue(result,
                        "❌ The actual text is '" + actualText + "' does not contain '" + expectedText.trim() + "'");

            }
                AllureManager.saveTextLog("Verify text contains - " + result + ". The actual text is '" + actualText + "' does not contain '" + expectedText.trim() + "'");

            addScreenshotToReport("verifyElementTextContains_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            LogUtils.error("Exception in verifyElementTextContains: " + e.getMessage());

            AllureManager.saveTextLog("Exception in verifyElementTextContains: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Uploads a file to the element (e.g., an input[type=file]) identified by the selector.
     *
     * @param selector the selector of the file upload element.
     * @param filePath the path of the file to upload.
     */
    @Step("Upload file using selector: {0} with file: {1}")
    public static void uploadFile(String selector, String filePath) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.setInputFiles(Paths.get(filePath));
            LogUtils.info("Uploaded file using selector: " + selector + " with file: " + filePath);

            AllureManager.saveTextLog("Uploaded file using selector: " + selector + " with file: " + filePath);
            addScreenshotToReport("uploadFile_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Failed to upload file using selector: " + selector + " with error: " + e.getMessage());

            AllureManager.saveTextLog("Failed to upload file using selector: " + selector + " with error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element’s text does NOT contain the specified text.
     *
     * @param selector        the selector of the element.
     * @param notExpectedText the text that should not be present.
     * @param flowControl     failure handling schema.
     * @return true if the text is not contained; otherwise, false.
     */
    @Step("Verify text of element {0} does not contain: {1}")
    public static boolean verifyElementTextNotContains(String selector, String notExpectedText, FailureHandling flowControl) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String actualText = locator.textContent().trim();
            boolean result = !actualText.contains(notExpectedText.trim());
            if (result) {
                LogUtils.info("Verify text not contains: " + result);

            } else {
                LogUtils.error("❌ Verify text not contains: " + result);

            }
            if (flowControl.equals(FailureHandling.STOP_ON_FAILURE)) {
                Assert.assertFalse(actualText.contains(notExpectedText.trim()),
                        "❌ The actual text is '" + actualText + "' contains '" + notExpectedText.trim() + "'");
            } else if (flowControl.equals(FailureHandling.CONTINUE_ON_FAILURE)) {
                softAssert.assertFalse(actualText.contains(notExpectedText.trim()),
                        "❌ The actual text is '" + actualText + "' contains '" + notExpectedText.trim() + "'");
                if (!result) {

                }
            } else if (flowControl.equals(FailureHandling.OPTIONAL)) {

                AllureManager.saveTextLog("Verify text not contains - " + result + ". The actual text is '" + actualText + "' contains '" + notExpectedText.trim() + "'");
            }
            addScreenshotToReport("verifyElementTextNotContains_" + DateUtils.getCurrentDateTime());
            return result;
        } catch (Exception e) {
            LogUtils.error("Exception in verifyElementTextNotContains: " + e.getMessage());

            AllureManager.saveTextLog("Exception in verifyElementTextNotContains: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element is enabled.
     *
     * @param selector    the selector of the element.
     * @param flowControl failure handling schema.
     * @return true if enabled; otherwise, false.
     */
    @Step("Verify element is enabled: {0}")
    public static boolean verifyElementIsEnabled(String selector, FailureHandling flowControl) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            boolean isEnabled = locator.isEnabled();
            if (isEnabled) {
                LogUtils.info("Element is enabled: " + selector);

            } else {
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
            LogUtils.error("Exception in verifyElementIsEnabled: " + e.getMessage());

            AllureManager.saveTextLog("Exception in verifyElementIsEnabled: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifies that the element is disabled.
     *
     * @param selector    the selector of the element.
     * @param flowControl failure handling schema.
     * @return true if disabled; otherwise, false.
     */
    @Step("Verify element is disabled: {0}")
    public static boolean verifyElementIsDisabled(String selector, FailureHandling flowControl) {
        Page page = PlaywrightDriverManager.getPage();
        try {
            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            boolean isDisabled = !locator.isEnabled();
            if (isDisabled) {
                LogUtils.info("Element is disabled: " + selector);

            } else {
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
            Page page = PlaywrightDriverManager.getPage();
            BrowserContext context = page.context();
            LogUtils.info("Closing the browser");
            context.close();

            AllureManager.saveTextLog("Browser closed successfully.");
            // No screenshot as the browser is closing.
        } catch (Exception e) {
            LogUtils.error("Failed to close browser with error: " + e.getMessage());

            AllureManager.saveTextLog("Failed to close browser with error: " + e.getMessage());
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
        // Save the original (current) Page instance so we can return to it later.
        Page originalPage = PlaywrightDriverManager.getPage();

        try {
            // Create a new browser instance (non-headless mode in this example) and obtain a new Page.
            Page newPage = PlaywrightFactory.createPage(false);

            // Set the newly created page as the current active page in the driver manager.
            PlaywrightDriverManager.setPage(newPage);

            // Log that a new browser instance has been opened.
            LogUtils.info("Opened new browser instance for additional actions.");
            AllureManager.saveTextLog("Opened new browser instance for additional actions.");

            // Capture and attach a screenshot indicating that the new browser has been opened.
            addScreenshotToReport("newBrowserOpened_" + DateUtils.getCurrentDateTime());

            // Execute the provided actions (passed as a lambda) in the context of the new browser.
            action.run();

            // Capture another screenshot after the actions have been executed.
            addScreenshotToReport("actionsPerformedInNewBrowser_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            // Log any errors that occur during the actions in the new browser.
            LogUtils.error("Error during actions in new browser: " + e.getMessage(), e);
            AllureManager.saveTextLog("Error during actions in new browser: " + e.getMessage());
            // Wrap and rethrow the exception to signal failure.
            throw new RuntimeException(e);
        } finally {
            // Close the new browser instance and clean up its resources.
            PlaywrightFactory.quit();

            // Restore the original browser instance in the driver manager.
            PlaywrightDriverManager.setPage(originalPage);

            // Log that control has returned to the original browser session.
            LogUtils.info("Returned to original browser instance.");
            AllureManager.saveTextLog("Returned to original browser instance.");

            // Capture a final screenshot confirming that we've returned to the original browser.
            addScreenshotToReport("returnedToOriginalBrowser_" + DateUtils.getCurrentDateTime());
        }
    }


    /**
     * Switches to the newly opened tab, performs the provided action, and returns to the original tab.
     *
     * @param action The action to perform on the new tab.
     */
    @Step("Switch to newly opened tab, perform action, and return to original tab")
    public static void switchToNewTabAndPerformAction(Runnable action) {
        try {
            Page originalPage = PlaywrightDriverManager.getPage();
            BrowserContext context = originalPage.context();
            // Wait briefly for the new page to open (customize as needed)
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
            // Switch to new page
            PlaywrightDriverManager.setPage(newPage); // Assumes you have a method to update the current Page
            LogUtils.info("Switched to new tab");

            AllureManager.saveTextLog("Switched to new tab.");
            addScreenshotToReport("switchToNewTab_" + DateUtils.getCurrentDateTime());
            // Perform action on the new tab
            action.run();
            // Optionally capture a screenshot after action
            addScreenshotToReport("actionOnNewTab_" + DateUtils.getCurrentDateTime());
            // Return to original page
            PlaywrightDriverManager.setPage(originalPage);
            LogUtils.info("Returned to original tab");

            AllureManager.saveTextLog("Returned to original tab.");
            addScreenshotToReport("returnToOriginalTab_" + DateUtils.getCurrentDateTime());
        } catch (Exception e) {
            LogUtils.error("Error in switching tabs: " + e.getMessage());

            AllureManager.saveTextLog("Error in switching tabs: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the URL associated with a specific email message from Mailinator.
     * It first attempts to extract the URL from the message subject; if not found,
     * it retrieves it using the links API.
     *
     * @param domain    the Mailinator domain (e.g., "private").
     * @param mailbox   the mailbox (e.g., "abc").

     * @return the extracted URL, or null if not found.
     */
    @Step("Retrieve mail URL for domain: {0}, mailbox: {1}")
    public static String getMailUrl(String domain, String mailbox) {
        String url = null;
        // Update or externalize your API key as needed
        String apiKey = "947fc29e9d3b4c4b80be0e65f27fd8db";
        try {
            LogUtils.info("Requesting inbox for domain: " + domain);
            AllureManager.saveTextLog("Requesting inbox for domain: " + domain);

            MailinatorClient mailinatorClient = new MailinatorClient(apiKey);
            Inbox inbox = mailinatorClient.request(new GetInboxRequest(domain));
            List<Message> messages = inbox.getMsgs();
            LogUtils.info("Inbox received with " + messages.size() + " messages");
            AllureManager.saveTextLog("Inbox received with " + messages.size() + " messages");

            // Check if there is at least one message
            if (messages.isEmpty()) {
                LogUtils.warn("No messages found in inbox for domain: " + domain);
                AllureManager.saveTextLog("No messages found in inbox for domain: " + domain);
                return null;
            }

            // Assume the first message is the latest one
            Message latestMessage = messages.get(0);
            String messageId = latestMessage.getId();
            String subject = latestMessage.getSubject();
            LogUtils.info("Using latest message with id: " + messageId + " and subject: " + subject);
            AllureManager.saveTextLog("Using latest message with id: " + messageId + " and subject: " + subject);

            // First, try to extract the URL from the subject
            url = extractUrlFromSubject(subject);
            if (url != null) {
                LogUtils.info("Extracted URL from subject: " + url);
                AllureManager.saveTextLog("Extracted URL from subject: " + url);
                return url;
            } else {
                LogUtils.warn("No URL found in subject. Trying links API for message id: " + messageId);
                AllureManager.saveTextLog("No URL found in subject. Trying links API for message id: " + messageId);

                // Retrieve URL using the links API
                Links linksResponse = mailinatorClient.request(new GetLinksRequest(domain, mailbox, messageId));
                List<String> links = linksResponse.getLinks();
                if (links != null && !links.isEmpty()) {
                    url = links.get(0);
                    LogUtils.info("Retrieved URL from links API: " + url);
                    AllureManager.saveTextLog("Retrieved URL from links API: " + url);
                } else {
                    LogUtils.error("No URL found from links API for message id: " + messageId);
                    AllureManager.saveTextLog("No URL found from links API for message id: " + messageId);
                }
            }
        } catch (Exception e) {
            LogUtils.error("Exception occurred while retrieving mail URL: " + e.getMessage(), e);
            AllureManager.saveTextLog("Exception occurred while retrieving mail URL: " + e.getMessage());
            throw e;
        }
        return url;
    }

    /**
     * Helper method to extract the first URL found in the subject string.
     *
     * @param subject the subject text to search for a URL.
     * @return the URL if found, or null otherwise.
     */
    private static String extractUrlFromSubject(String subject) {
        String urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+(/\\S*)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(subject);
        if (matcher.find()) {
            return matcher.group(0); // Return the first matched URL.
        }
        return null;
    }

    /**
     * Utility method to capture a screenshot and attach it to the report.
     *
     * @param screenshotName the name for the screenshot.
     */
    private static void addScreenshotToReport(String screenshotName) {
        try {
            Page page = PlaywrightDriverManager.getPage();
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots", screenshotName + ".png")));
            AllureManager.takeScreenshotToAttachOnAllureReport();
            // You can also attach the screenshot to Extent Report if your manager supports it.
        } catch (Exception e) {
            LogUtils.error("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
