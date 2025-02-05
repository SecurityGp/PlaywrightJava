package com.assured.model;

import lombok.Data;
import lombok.Getter;

@Data
public class SignInModel {

    public static int row;

    @Getter
    public static String testCaseName = "TESTCASENAME";

    @Getter
    public static String email = "EMAIL";

    @Getter
    public static String password = "PASSWORD";

    @Getter
    public static String expectedTitle = "EXPECTED_TITLE";

    @Getter
    public static String expectedError = "EXPECTED_ERROR";

    public static String expectedUrl = "EXPECTED_URL";

    public static int getRow() {
        return row;
    }

    public static String getExpectedUrl() {
        return expectedUrl;
    }
}
