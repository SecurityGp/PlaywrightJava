package com.assured.utils;

import com.assured.constants.FrameworkConstants;
import net.datafaker.Faker;

import java.util.Locale;

public class DataFakerUtils {
    // Java Locale List: https://www.viralpatel.net/java-locale-list-tutorial/
    private static Faker faker = null; // Default: English US

    /**
     * Returns a singleton instance of Faker initialized with the locale
     * defined in FrameworkConstants.
     *
     * @return Faker instance
     */
    public static Faker getFaker() {
        if (faker == null) {
            faker = new Faker(new Locale(FrameworkConstants.LOCATE));
        }
        return faker;
    }

    /**
     * Sets a custom Faker instance.
     *
     * @param faker Custom Faker instance
     */
    public static void setFaker(Faker faker) {
        DataFakerUtils.faker = faker;
    }

    /**
     * Updates the Faker instance with a new locale.
     *
     * @param localeName Name of the locale (e.g., "en_US", "vi")
     */
    public static void setLocale(String localeName) {
        faker = new Faker(new Locale(localeName));
    }
}
