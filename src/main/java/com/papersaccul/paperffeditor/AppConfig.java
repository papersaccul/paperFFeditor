package com.papersaccul.paperffeditor;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * AppConfig class is responsible for managing application-wide configurations.
 */
public class AppConfig {
    // Default locale for the application
    private static Locale locale = Locale.getDefault();
    // ResourceBundle for internationalization
    private static final String MessagesBundleName = "com.papersaccul.paperffeditor.MessagesBundle";
    private static ResourceBundle resourceBundle = loadResourceBundle();

    /**
     * Loads the ResourceBundle with a fallback to English if the default is not found.
     * 
     * @return The loaded ResourceBundle.
     */
    private static ResourceBundle loadResourceBundle() {
        try {
            return ResourceBundle.getBundle(MessagesBundleName, locale);
        } catch (Exception e) {
            // Fallback to English if the default locale's bundle is not found
            return ResourceBundle.getBundle(MessagesBundleName, new Locale("en", "GB"));
        }
    }

    /**
     * Gets the current application locale.
     * 
     * @return The current Locale.
     */
    public static Locale getLocale() {
        return locale;
    }

    /**
     * Sets the application locale and updates the ResourceBundle accordingly.
     * 
     * @param newLocale The new Locale to set.
     */
    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        resourceBundle = loadResourceBundle();
    }

    /**
     * Gets the ResourceBundle for the current locale. This is used for internationalization.
     * 
     * @return The ResourceBundle for the current locale.
     */
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
