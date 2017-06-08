package com.example.downloadservicedemo;

import android.net.Network;
import android.os.AsyncTask;
import android.webkit.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by sjk on 17-6-8.
 */

public class DownloadTask extends AsyncTask<String, Integer, DownloadTask.State> {

    public enum State {
        FAILED,
        OK,
        CANCELLED,
        PAUSED
    }

    private IDownloadListener listener;

    private boolean isPaused;
    private boolean isCancelled;

    public DownloadTask(IDownloadListener listener) {
        this.listener = listener;
        isPaused = isCancelled = false;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    protected State doInBackground(String... params) {
        String url = params[0];
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            String filename = FileUtils.extractFilename(url);
            file = FileUtils.getFile(filename);
            long fileLength = file.exists() ? file.length() : 0;
            long contentLength = NetworkUtils.getContentLength(url);

            if (contentLength == 0) {   // 资源出错。下载失败。
                return State.FAILED;
            } else if (contentLength == fileLength) {   // 资源与文件大小相等，即文件已下载：下载成功
                return State.OK;
            }

            // 进行下载、或者续断点
            is = NetworkUtils.getStream(url, fileLength);
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(fileLength); // 跳过已经下载好的字节
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                if (isCancelled) {
                    return State.CANCELLED;
                } else if (isPaused) {
                    return State.PAUSED;
                } else {
                    savedFile.write(buffer, 0, length);
                    fileLength += length;
                    int percent = (int) (fileLength * 100 / contentLength);
                    publishProgress(percent);
                }
            }
            return State.OK;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return State.FAILED;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (file != null && isCancelled) {  // 如果是关闭状态，那就在这一步删除已经下载的部分文件
                    file.delete();
                }
            } catch (Exception e) { // 统一处理关闭资源时出现的异常
                e.printStackTrace();
            }
        }
    }

    private int lastProgress = 0;

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (lastProgress != progress) { // 优化1：只有进度不一样时，才通知。可以减少回调频率。
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(State state) {
        if (state == State.OK) {
            listener.onOK();
        } else if (state == State.CANCELLED) {
            listener.onCancelled();
        } else if (state == State.FAILED) {
            listener.onFailed();
        } else if (state == State.PAUSED) {
            listener.onPaused();
        }
    }
}
