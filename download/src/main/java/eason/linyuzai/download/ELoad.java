package eason.linyuzai.download;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import eason.linyuzai.download.database.DatabaseManager;
import eason.linyuzai.download.entity.DefaultDownloadTaskEntityCreator;
import eason.linyuzai.download.factory.OkHttpClientFactory;
import eason.linyuzai.download.factory.RetrofitFactory;
import eason.linyuzai.download.file.FileProcessor;
import eason.linyuzai.download.file.OkioSourceFileProcessor;
import eason.linyuzai.download.recycle.TaskQueueRecycler;
import eason.linyuzai.download.recycle.TaskRecycler;
import eason.linyuzai.download.task.DownloadTask;
import eason.linyuzai.download.entity.DownloadTaskEntity;
import eason.linyuzai.download.task.RetrofitDownloadTask;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ELoad {

    public static final String TAG = "E-LOAD";

    private OkHttpClientFactory okHttpClientFactory;
    private RetrofitFactory retrofitFactory;
    private TaskRecycler taskRecycler;
    private FileProcessor fileProcessor;
    private String downloadPath;
    private DatabaseManager databaseManager;
    private DownloadTaskEntity.Creator entityCreator;
    private List<DownloadTask.DownloadListener> downloadListeners;
    private List<DownloadTask.DownloadTaskListener> downloadTaskListeners;
    private Set<DownloadTask> downloadTaskSet = new ConcurrentSkipListSet<>();

    public static class Builder {
        private Context context;
        private TaskRecycler taskRecycler;
        private FileProcessor fileProcessor;
        private String downloadPath;
        private OkHttpClientFactory okHttpClientFactory;
        private RetrofitFactory retrofitFactory;
        private DatabaseManager databaseManager;
        private DownloadTaskEntity.Creator entityCreator;
        private List<DownloadTask.DownloadListener> downloadListeners = new ArrayList<>();
        private List<DownloadTask.DownloadTaskListener> downloadTaskListeners = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTaskRecycler(TaskRecycler taskRecycler) {
            this.taskRecycler = taskRecycler;
            return this;
        }

        public Builder setFileProcessor(FileProcessor fileProcessor) {
            this.fileProcessor = fileProcessor;
            return this;
        }

        public Builder setDownloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        public Builder setOkHttpClientFactory(OkHttpClientFactory okHttpClientFactory) {
            this.okHttpClientFactory = okHttpClientFactory;
            return this;
        }

        public Builder setRetrofitFactory(RetrofitFactory retrofitFactory) {
            this.retrofitFactory = retrofitFactory;
            return this;
        }

        public Builder setDatabaseManager(DatabaseManager databaseManager) {
            this.databaseManager = databaseManager;
            return this;
        }

        public Builder setEntityCreator(DownloadTaskEntity.Creator entityCreator) {
            this.entityCreator = entityCreator;
            return this;
        }

        public Builder addDownloadListeners(DownloadTask.DownloadListener downloadListener) {
            if (downloadListener != null)
                this.downloadListeners.add(downloadListener);
            return this;
        }

        public Builder addDownloadTaskListener(RetrofitDownloadTask.DownloadTaskListener downloadTaskListener) {
            if (downloadTaskListener != null)
                this.downloadTaskListeners.add(downloadTaskListener);
            return this;
        }

        public List<DownloadTask.DownloadListener> getDownloadListeners() {
            return downloadListeners;
        }

        public List<DownloadTask.DownloadTaskListener> getDownloadTaskListeners() {
            return downloadTaskListeners;
        }

        public ELoad build() {
            if (downloadPath == null) {
                File cache = Objects.requireNonNull(context.getExternalCacheDir());
                downloadPath = new File(cache, "ELoad").getAbsolutePath();
            }
            if (taskRecycler == null)
                taskRecycler = new TaskQueueRecycler();
            if (fileProcessor == null)
                fileProcessor = new OkioSourceFileProcessor();
            if (entityCreator == null)
                entityCreator = DefaultDownloadTaskEntityCreator.get();
            return new ELoad(this);
        }
    }

    ELoad(Builder builder) {
        this.okHttpClientFactory = builder.okHttpClientFactory;
        this.retrofitFactory = builder.retrofitFactory;
        this.taskRecycler = builder.taskRecycler;
        this.fileProcessor = builder.fileProcessor;
        this.databaseManager = builder.databaseManager;
        this.downloadListeners = builder.downloadListeners;
        this.downloadTaskListeners = builder.downloadTaskListeners;
        this.downloadPath = builder.downloadPath;
        this.entityCreator = builder.entityCreator;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public FileProcessor getFileProcessor() {
        return fileProcessor;
    }

    public TaskRecycler getTaskRecycler() {
        return taskRecycler;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public List<DownloadTask.DownloadListener> getDownloadListeners() {
        return downloadListeners;
    }

    public List<DownloadTask.DownloadTaskListener> getDownloadTaskListeners() {
        return downloadTaskListeners;
    }

    public List<DownloadTask> getRunningTasks() {
        List<DownloadTask> runningTasks = new ArrayList<>();
        for (DownloadTask task : downloadTaskSet) {
            if (task.isRunning()) {
                runningTasks.add(task);
            }
        }
        return Collections.unmodifiableList(runningTasks);
    }

    public List<DownloadTaskEntity> loadTaskEntitiesFromDatabase() {
        if (databaseManager == null) {
            Log.w(TAG, "Database Manager is Null");
            return new ArrayList<>();
        }
        return databaseManager.selectAll(entityCreator);
    }

    private DownloadTask.Builder wrapper(DownloadTask.Builder builder) {
        builder.getDownloadListeners().addAll(downloadListeners);
        builder.getDownloadTaskListeners().addAll(downloadTaskListeners);
        return builder.setOkHttpClientFactory(okHttpClientFactory)
                .setRetrofitFactory(retrofitFactory)
                .setTaskRecycler(taskRecycler)
                .setFileProcessor(fileProcessor)
                .setDatabaseManager(databaseManager);
    }

    public DownloadRequest convert(DownloadTaskEntity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cant be null");
        if (entity.getFilePath() == null)
            entity.setFilePath(downloadPath);
        return new DownloadRequest(wrapper(new DownloadTask.Builder().setEntity(entity)), taskRecycler);
    }

    public DownloadRequest url(String url) {
        DownloadTaskEntity entity = entityCreator.create();
        entity.setUrl(url);
        return convert(entity);
    }

    public static class DownloadRequest {

        private DownloadTask.Builder builder;
        private TaskRecycler taskRecycler;

        DownloadRequest(DownloadTask.Builder builder, TaskRecycler taskRecycler) {
            this.builder = builder;
            this.taskRecycler = taskRecycler;
        }

        public DownloadRequest header(String headerName, String headerValue) {
            if (builder.getEntity().getHeaders() == null)
                builder.getEntity().setHeaders(new HashMap<>());
            builder.getEntity().getHeaders().put(headerName, headerValue);
            return this;
        }

        public DownloadRequest headers(Map<String, String> headers) {
            builder.getEntity().setHeaders(headers);
            return this;
        }

        public DownloadRequest filepath(String filepath) {
            builder.getEntity().setFilePath(filepath);
            return this;
        }

        public DownloadRequest filename(String filename) {
            builder.getEntity().setFileName(filename);
            return this;
        }

        public DownloadRequest urlDecoder(String urlDecoder) {
            builder.getEntity().setUrlDecoder(urlDecoder);
            return this;
        }

        public DownloadRequest extra(Serializable extra) {
            builder.getEntity().setExtra(extra);
            return this;
        }

        public DownloadRequest downloadListener(DownloadTask.DownloadListener downloadListener) {
            builder.addDownloadListener(downloadListener);
            return this;
        }

        public DownloadRequest downloadTaskListener(DownloadTask.DownloadTaskListener downloadTaskListener) {
            builder.addDownloadTaskListener(downloadTaskListener);
            return this;
        }

        public DownloadTaskEntity entity() {
            return builder.getEntity();
        }

        public DownloadTask create() {
            DownloadTask task = taskRecycler.getTask();
            if (task == null) {
                task = builder.build();
            } else {
                ((RetrofitDownloadTask) task).setEntity(builder.getEntity());
                task.attach(builder);
            }
            return task;
        }
    }
}
