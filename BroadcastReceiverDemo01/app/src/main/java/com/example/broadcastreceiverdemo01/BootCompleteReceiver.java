package com.example.broadcastreceiverdemo01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log("BootCompleteReceiver: " + context);

        Toast.makeText(context, "启动广播: context", Toast.LENGTH_LONG).show();
    }
}
