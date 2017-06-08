package com.me.customeventbus.activity;

import android.util.Log;


/**
 * Created by sjk on 17-6-9.
 */

public class Logger {

    private static final String TAG = "CustomEventBusTag";

    public static void log(String s) {
        Log.i(TAG, s);
    }
}
