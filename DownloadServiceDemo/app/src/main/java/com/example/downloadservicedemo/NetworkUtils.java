package com.example.downloadservicedemo;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sjk on 17-6-8.
 */

public class NetworkUtils {

    public static InputStream getStream(final String url, final long fromByte) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("RANGE", String.format(Locale.getDefault(), "bytes=%d-", fromByte))  // %d包含long
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response == null || response.body() == null) {
            return null;
        } else {
            return response.body().byteStream();
        }
    }

    public static long getContentLength(final String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            return response.body().contentLength();
        }
        return 0;
    }
}
