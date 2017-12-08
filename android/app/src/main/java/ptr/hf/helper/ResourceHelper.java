package ptr.hf.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import java.text.Normalizer;


/**
 * Helper for quick access on resources
 */
public class ResourceHelper {
    /**
     * Get a sharedPreference with the given name in private mode
     *
     * @param preferencesName The name of the preference
     * @return The shared preference
     */
    public static SharedPreferences getSharedPreferences(String preferencesName) {
        return getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    /**
     * Get the sharedPreferences with the given name and opens in with the given mode
     *
     * @param preferencesName The name of the preference
     * @param mode            The opening mode
     * @return The sharedPreference
     */
    public static SharedPreferences getSharedPreferences(String preferencesName, int mode) {
        return BaseHelper.getContext().getSharedPreferences(preferencesName, mode);
    }

    /**
     * Convert pixels to dp according to the system display metrics
     *
     * @param px
     * @return
     */
    public static float convertPixelToDp(final float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * Convert dp to pixels according to the system display metrics
     *
     * @param dp
     * @return
     */
    public static float convertDpToPixel(final float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * Provide the app accessible resources
     *
     * @return
     */
    public static Resources getResources() {
        return BaseHelper.getContext().getResources();
    }

    /**
     * Return the string associated with the given resource id
     *
     * @param resourceId
     * @return
     */
    public static String getStringResources(int resourceId) {
        return getResources().getString(resourceId);
    }

    /**
     * Return the string associated with the given resource id
     *
     * @param resourceId
     * @return
     */
    public static String getStringResources(int resourceId, Object... formatArgs) {
        return getResources().getString(resourceId, formatArgs);
    }

    /**
     * Return a string array with the data of the given array id
     *
     * @param arrayResourceId
     * @return
     */
    public static String[] getStringArray(int arrayResourceId) {
        return getResources().getStringArray(arrayResourceId);
    }

    /**
     * Return a raw typed array for the given resource
     *
     * @param arrayResourceId
     * @return
     */
    public static TypedArray getTypedArray(int arrayResourceId) {
        return getResources().obtainTypedArray(arrayResourceId);
    }

    /**
     * Return a drawable associated with the given resource id
     *
     * @param resourceId
     * @return
     */
    public static Drawable getDrawableResource(int resourceId) {
        return ResourcesCompat.getDrawable(getResources(), resourceId, null);
    }

    /**
     * Return a color associated with the given resource id
     *
     * @param resourceId
     * @return
     */
    public static int getColorResource(int resourceId) {
        return getResources().getColor(resourceId);
    }

    /**
     * Return a dimension associated with the given resource id
     *
     * @param resourceId
     * @return
     */
    public static float getDimension(int resourceId) {
        return getResources().getDimension(resourceId);
    }

    /**
     * Sets the specified background Drawable to a View
     *
     * @param view
     * @param resourceId
     */
    public static void setBackgroundDrawable(View view, int resourceId) {
        final int bottom = view.getPaddingBottom();
        final int top = view.getPaddingTop();
        final int right = view.getPaddingRight();
        final int left = view.getPaddingLeft();
        view.setBackgroundResource(resourceId);
        view.setPadding(left, top, right, bottom);
    }

    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean hasFlash() {
        return BaseHelper.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static boolean hasCamera(boolean isFrontAllowed) {
        PackageManager pm = BaseHelper.getContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                (isFrontAllowed && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT));
    }

    public static String removeAccents(String source) {
        String normalized = Normalizer.normalize(source, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
}
