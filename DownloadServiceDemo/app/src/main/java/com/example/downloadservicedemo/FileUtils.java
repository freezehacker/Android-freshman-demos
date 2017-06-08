package com.example.downloadservicedemo;

import android.os.Environment;

import java.io.File;

/**
 * Created by sjk on 17-6-8.
 */

public class FileUtils {

    public static String getExtDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    }

    public static File getFile(String uri) {
        return new File(getExtDir() + File.separator + uri);
    }

    public static void clearFilesInExtDir() {
        File dir = new File(getExtDir());
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }

    public static String extractFilename(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
