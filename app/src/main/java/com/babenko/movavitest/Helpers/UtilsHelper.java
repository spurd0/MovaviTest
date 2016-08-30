package com.babenko.movavitest.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Roman Babenko (roman.babenko@sibers.com) on 8/30/2016.
 */
public class UtilsHelper {
    static String TAG = "UtilsHelper";

    public static boolean checkPermission(Activity activity, String permission)
    {
        int res = activity.checkCallingOrSelfPermission(permission);
        Log.d(TAG + " checkPermission ", permission + " " + (res == PackageManager.PERMISSION_GRANTED));
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(Activity activity, String permission, int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{
            permission},
        code);
        }
    }

    public static void makeToast (final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            }
        });
    }

}
