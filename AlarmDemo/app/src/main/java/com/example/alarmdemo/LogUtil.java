package com.example.alarmdemo;

import android.util.Log;

/**
 * Created by sjk on 17-6-9.
 */

public class LogUtil {

    public static final String TAG = "AlarmDemoTag";

    public static void log(String s) {
        Log.i(TAG, s);
    }
}
