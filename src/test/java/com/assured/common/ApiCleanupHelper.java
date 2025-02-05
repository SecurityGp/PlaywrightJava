package com.assured.common;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

public class ApiCleanupHelper {

    public static void cleanupTestData(String baseUrl, String resourcePath) {
        try (Playwright playwright = Playwright.create()) {
            // Create an APIRequestContext without options.
            APIRequestContext request = playwright.request().newContext();

            // (Optional) If your endpoint needs the base URL included in every request,
            // you can prepend it to resourcePath or configure your API calls accordingly.
            APIResponse response = request.delete(baseUrl + resourcePath);
            int status = response.status();

            if (status == 200) {
                System.out.println("Test data cleaned up successfully.");
            } else {
                System.out.println("Failed to delete test data. Status: " + status + ", Body: " + response.text());
            }

            request.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
