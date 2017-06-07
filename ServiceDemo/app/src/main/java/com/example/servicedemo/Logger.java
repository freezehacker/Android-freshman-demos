package com.example.servicedemo;

import android.util.Log;

/**
 * Created by sjk on 17-6-7.
 */

public class Logger {

    private static final String TAG = "ServiceDemoTag";

    public static void log(String s) {
        Log.i(TAG, s);
    }
}
