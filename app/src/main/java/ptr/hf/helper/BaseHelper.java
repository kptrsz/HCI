package ptr.hf.helper;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Provides a base helper class for the most common help functionality
 */
public class BaseHelper {

    private static Context context;

    /**
     * Initialize helper context
     *
     * @param context Application context
     */
    public static void initialize(Context context) {
        BaseHelper.context = context;
    }

    /**
     * @return A common context
     */
    public static Context getContext() {
        if (context == null) {
            throw new ContextNotInitializedException();
        }
        return context;
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Custom exception for indicating that context was not initialized before use
     */
    private static class ContextNotInitializedException extends RuntimeException {
        private ContextNotInitializedException() {
            super("Context is not initialized! In order to use helpers, initialize BaseHelper");
        }
    }
}