
package br.com.recomusic.persistencia.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BundleUtils {

    private static ResourceBundle bundleMSG;

    private static BundleUtils instance = null;

    private BundleUtils() {

    }

    public static BundleUtils getInstance() {
        if (instance == null) {
            instance = new BundleUtils();
        }
        return instance;
    }

    /**
    */
    public static ResourceBundle getBundleMessage() {

        if (bundleMSG == null) {
            try {

                bundleMSG = ResourceBundle.getBundle("messages", Locale.getDefault(), Thread.currentThread()
                                .getContextClassLoader());

            } catch (MissingResourceException e) {

                throw new RuntimeException("The 'messages.properties' file was " + "not found in classpath.");

            }

        }

        return bundleMSG;

    }

    /**
     * @return String
     */
    public static String getMessage(String key) {
        return "C://Users//Guilherme//FT";
    }

}
