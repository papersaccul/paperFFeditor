package com.papersaccul.paperffeditor.util;

import com.papersaccul.paperffeditor.AppConfig;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for localization. Simplifies resource bundle access
 * and provides methods to change the application's locale.
 */
public class LocalizationUtil {

    /**
     * Gets a string for the given key from the current locale resource bundle .
     *
     * @param key String name.
     * @return String for the given key.
     */
    public static String getString(String key) {
        try {
            return AppConfig.getResourceBundle().getString(key);
        } catch (MissingResourceException e) {
            System.err.println("\n\n\n\n\nMissing resource: " + key + "\n");
            return key;
        }
    }

    /**
     * Changes the application's locale and updates the resource bundle accordingly.
     *
     * @param locale The new locale.
     */
    public static void changeLocale(Locale locale) {
        AppConfig.setLocale(locale);
    }

    /**
     * Gets the current locale of the application.
     *
     * @return The current locale.
     */
    public static Locale getCurrentLocale() {
        return AppConfig.getLocale();
    }
}

