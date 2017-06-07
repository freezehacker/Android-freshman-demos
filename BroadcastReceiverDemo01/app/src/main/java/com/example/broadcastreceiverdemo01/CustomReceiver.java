package com.example.broadcastreceiverdemo01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sjk on 17-6-7.
 */

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");

        Toast.makeText(context, data, Toast.LENGTH_LONG).show();
        Logger.log("customReceiver: 收到有序广播");
        /*abortBroadcast();
        clearAbortBroadcast();*/
    }
}
