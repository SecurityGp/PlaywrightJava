package com.assured.utils;

import org.testng.annotations.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

import static com.assured.utils.DataFakerUtils.getFaker;

public class DataGenerateUtils {

    private DataGenerateUtils() {
        super();
    }

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom random = new SecureRandom();

    // Generates a random alphanumeric string of given length
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(random.nextInt(AB.length())));
        }
        return sb.toString();
    }

    // Generates a random string with hexadecimal encoding (based on byte length)
    public static String randomStringHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); // Hexadecimal encoding
    }

    // Generates a random UUID string
    public static String randomStringUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    // Generates a random integer between given range (from, to)
    public static int randomNumberIntFromTo(int from, int to) {
        int random_int = (int) Math.floor(Math.random() * (to - from + 1) + from);
        return random_int;
    }

    // Generates a random full name using Faker
    public static String randomFullName() {
        return getFaker().name().fullName();
    }

    // Generates a random country using Faker
    public static String randomCountry() {
        return getFaker().address().country();
    }

    // Generates a random zip code using Faker
    public static String randomZipCode() {
        return getFaker().address().zipCode();
    }

    // Generates a random full address using Faker
    public static String randomAddress() {
        return getFaker().address().fullAddress();
    }

    // Generates a random city name using Faker
    public static String randomCity() {
        return getFaker().address().cityName();
    }

    // Generates a random state name using Faker
    public static String randomState() {
        return getFaker().address().state();
    }

    // Generates a random street name using Faker
    public static String randomStreetName() {
        return getFaker().address().streetName();
    }

    // Generates a random 10-digit phone number (numeric)
    public static String randomPhoneNumber() {
        return generateRandomNumericString(10);
    }

    // Generates a random 10-digit Tax ID number (numeric)
    public static String randomTaxId() {
        return generateRandomNumericString(10);
    }

    // Generates a random 10-digit NPI number (numeric)
    public static String randomNpiNumber() {
        return generateRandomNumericString(10);
    }

    // Helper method to generate a random numeric string of given length
    private static String generateRandomNumericString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Adds a random digit (0-9)
        }
        return sb.toString();
    }

    // Test method to check the generation of random data
    @Test
    public void testDataGeneration() {

        System.out.println(randomFullName());
        System.out.println(randomAddress());
        System.out.println(randomCountry());
        System.out.println(randomZipCode());
        System.out.println(randomState());
        System.out.println(randomCity());
        System.out.println(randomStreetName());

        // Test the new methods
        System.out.println(randomPhoneNumber());
        System.out.println(randomTaxId());
        System.out.println(randomNpiNumber());
    }
}
