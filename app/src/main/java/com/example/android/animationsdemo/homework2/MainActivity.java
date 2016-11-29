package com.example.android.animationsdemo.homework2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView iv;
    private TextView tvMessage;
    private RelativeLayout rootLayout;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.imageView);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        rootLayout = (RelativeLayout) findViewById(R.id.activity_main);

        ViewTreeObserver vto = rootLayout.getViewTreeObserver();
        //TODO: how to do it better, without bicycle?
        //Отображаем картинку после того, как будут подсчитаны реальные размеры нашего View
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setImage();
            }
        });
    }

    private void setImage() {
        int width  = rootLayout.getMeasuredWidth();
        int height = rootLayout.getMeasuredHeight();

        File file = null;
        if (DownloadImageUtil.isDowloaded()) {
            file = DownloadImageUtil.getImageFile();

            Log.d(TAG, file.getAbsolutePath());

            tvMessage.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);

            Log.d(TAG, "image decoded Width " + width + " Height " + height);

            iv.setImageBitmap(decodeBitmapFromFile(file, width, height));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (receiver == null) {
            receiver = new DoneLoadingReceiver();
            IntentFilter filter = new IntentFilter(getString(R.string.ACTION_DONE_LOADING_IMAGE));
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromFile(File file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.d(TAG, "inSamleSize = " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private class DoneLoadingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            setImage();
        }
    }
}
