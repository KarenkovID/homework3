package com.example.android.animationsdemo.homework2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;

public class DownloadImageService extends IntentService {

    public DownloadImageService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            DownloadImageUtil.downloadImage(this);
            Intent doneLoadingIntent = new Intent(getString(R.string.ACTION_DONE_LOADING_IMAGE));
            LocalBroadcastManager.getInstance(this).sendBroadcast(doneLoadingIntent);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(DownloadImageUtil.class.getSimpleName(), e.toString());
        }
    }

}
