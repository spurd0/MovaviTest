package com.babenko.movavitest.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.babenko.movavitest.data.Codes;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/30/2016.
 */
public class UtilsHelper {
    private static final String TAG = "UtilsHelper";

    public static boolean checkPermission(Activity activity, String permission)
    {
        int res = activity.checkCallingOrSelfPermission(permission);
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
