package ptr.hf;

import android.app.Application;

import com.facebook.stetho.Stetho;

//import net.danlew.android.joda.JodaTimeAndroid;

//import net.danlew.android.joda.JodaTimeAndroid;

import ptr.hf.helper.BaseHelper;

public class ChargerApplication extends Application{
    private static boolean activityVisible = false;


    @Override
    public void onCreate() {
        super.onCreate();

        BaseHelper.initialize(this);
        Stetho.initializeWithDefaults(this);
//        JodaTimeAndroid.init(this);
    }
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

}
