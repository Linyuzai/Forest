package eason.linyuzai.download.entity;

import java.io.Serializable;
import java.util.Map;

public interface DownloadTaskEntity {

    int STATE_IDLE = 0;//空闲
    int STATE_ATTACH = 1;//准备
    int STATE_RUNNING = 2;//下载
    int STATE_PAUSE = 3;//暂停
    int STATE_FINISH = 4;//完成
    int STATE_ERROR = 5;//错误
    int STATE_CANCEL = 6;//取消

    /**
     * 获得TaskId
     *
     * @return TaskId
     */
    String getTaskId();

    void setTaskId(String taskId);

    long getCreateTime();

    void setCreateTime(long createTime);

    /**
     * 获得任务执行状态
     *
     * @return 执行状态
     */
    int getState();

    void setState(int state);

    /**
     * 获得Http(s) Headers
     *
     * @return http(s) Headers
     */
    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> headers);

    /**
     * 获得Url
     *
     * @return Url
     */
    String getUrl();

    void setUrl(String url);

    String getUrlDecoder();

    void setUrlDecoder(String urlDecoder);

    /**
     * 获得文件总大小
     *
     * @return 文件总大小
     */
    long getTotalBytes();

    void setTotalBytes(long totalBytes);

    /**
     * 获得已下载大小
     *
     * @return 已下载大小
     */
    long getDownloadBytes();

    void setDownloadBytes(long downloadBytes);

    /**
     * 获得文件路径
     *
     * @return 文件路径
     */
    String getFilePath();

    void setFilePath(String filePath);

    /**
     * 获得文件名字
     *
     * @return 文件名字
     */
    String getFileName();

    void setFileName(String fileName);

    /**
     * 获得额外数据
     *
     * @return 额外数据
     */
    Serializable getExtra();

    void setExtra(Serializable extra);

    interface Creator {

        DownloadTaskEntity create();
    }
}
