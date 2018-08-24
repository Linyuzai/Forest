package eason.linyuzai.download.listener;

import eason.linyuzai.download.task.DownloadTask;

public abstract class AbsDownloadListener implements DownloadTask.DownloadListener {
    @Override
    public void onDownloadContentLengthRead(DownloadTask task, long contentLength) {

    }

    @Override
    public void onDownloadBytesRead(DownloadTask task, long bytesLength) {

    }

    @Override
    public void onDownloadComplete(DownloadTask task) {

    }
}
