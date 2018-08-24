package eason.linyuzai.download.recycle;

import eason.linyuzai.download.task.DownloadTask;

public interface TaskRecycler {
    void recycle(DownloadTask task);

    DownloadTask getTask();
}
