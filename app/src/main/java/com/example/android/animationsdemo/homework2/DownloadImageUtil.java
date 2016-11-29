package com.example.android.animationsdemo.homework2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Игорь on 28.11.2016.
 */

public final class DownloadImageUtil {
    public static final String TAG = DownloadImageUtil.class.getSimpleName();
    public static final String IMAGE_URI = "https://prisemarteau.files.wordpress.com/2013/06/escalade.jpg";

    public static final String fileName = "image.jpg";

    public static HttpURLConnection getConnection() throws IOException {
        return (HttpURLConnection) new URL(IMAGE_URI).openConnection();
    }

    public static boolean isConnectionAvailable(@NonNull Context context, boolean defaultValue) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return defaultValue;
        }
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static void downloadImage(Context context) throws IOException {
        if (isConnectionAvailable(context, false)) {
            Log.d(TAG, "Connection is alive");

            HttpURLConnection connection = getConnection();

            connection.connect();
            InputStream in = null;
            try {
                in = connection.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    if (!file.delete()) {
                        throw new IOException("can not delete old file");
                    }
                }

                if (!file.createNewFile()) {
                    throw new IOException("Can not create file");
                }
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    byte buffer[] = new byte[1024 * 8];
                    int bytesReaded;
                    Log.d(TAG, "writing to file");
                    while ((bytesReaded = in.read(buffer)) >= 0) {
                        Log.d(TAG, "writing...");
                        out.write(buffer, 0, bytesReaded);
                    }
                    out.flush();
                } catch (IOException e) {
                    throw new IOException("Error while writing file");
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } else {
            Log.d(TAG, "Connection is not alive");
        }
    }

    @Nullable
    public static File getImageFile() {
        return new File(Environment.getExternalStorageDirectory(), fileName);
    }

    public static boolean isDowloaded() {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        return file.exists();
    }
}
