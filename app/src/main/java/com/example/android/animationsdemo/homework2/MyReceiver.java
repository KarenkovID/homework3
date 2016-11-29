package com.example.android.animationsdemo.homework2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "onReceive");
        context.startService(new Intent(context, DownloadImageService.class));
    }
}
