package eason.linyuzai.download.entity;

import java.io.Serializable;
import java.util.Map;

public class DefaultDownloadTaskEntity implements DownloadTaskEntity {

    private String taskId;//任务id
    private long createTime;
    private int state;//任务执行状态
    private Map<String, String> headers;//http(s) headers
    private String url;//url
    private String urlDecoder;
    private long totalBytes;//文件总大小
    private long downloadBytes;//已下载大小
    private String filePath;//文件路径
    private String fileName;//文件名称
    private Serializable extra;//额外数据

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrlDecoder() {
        return urlDecoder;
    }

    @Override
    public void setUrlDecoder(String urlDecoder) {
        this.urlDecoder = urlDecoder;
    }

    @Override
    public long getTotalBytes() {
        return totalBytes;
    }

    @Override
    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    @Override
    public long getDownloadBytes() {
        return downloadBytes;
    }

    @Override
    public void setDownloadBytes(long downloadBytes) {
        this.downloadBytes = downloadBytes;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Serializable getExtra() {
        return extra;
    }

    @Override
    public void setExtra(Serializable extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "DefaultDownloadTaskEntity{" +
                "taskId='" + taskId + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                ", headers=" + headers +
                ", url='" + url + '\'' +
                ", urlDecoder='" + urlDecoder + '\'' +
                ", totalBytes=" + totalBytes +
                ", downloadBytes=" + downloadBytes +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extra=" + extra +
                '}';
    }
}
