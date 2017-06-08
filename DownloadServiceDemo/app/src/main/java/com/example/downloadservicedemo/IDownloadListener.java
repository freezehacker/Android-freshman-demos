package com.example.downloadservicedemo;

/**
 * Created by sjk on 17-6-8.
 */

public interface IDownloadListener {

    void onProgress(int progress);

    void onOK();

    void onFailed();

    void onPaused();

    void onCancelled();
}
