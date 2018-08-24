package eason.linyuzai.download.recycle;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import eason.linyuzai.download.task.DownloadTask;

public class TaskQueueRecycler implements TaskRecycler {

    private Queue<DownloadTask> taskQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void recycle(DownloadTask task) {
        taskQueue.offer(task);
    }

    @Override
    public DownloadTask getTask() {
        return taskQueue.poll();
    }
}
