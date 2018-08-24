package eason.linyuzai.download.task;

import android.text.TextUtils;
import android.util.Log;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import eason.linyuzai.download.ELoad;
import eason.linyuzai.download.header.RangeHeader;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitDownloadTask extends AbsDownloadTask {

    private Map<String, String> headersWrapper = new HashMap<>();

    private DownloadService downloadService;
    private ProgressListener progressListener;
    private Disposable disposable;

    private Emitter<DownloadTaskWrapper> downloadEmitter;
    private Emitter<DownloadTaskWrapper> taskEmitter;

    private String contentDisposition;

    class ResponseHeadersListenerImpl implements ResponseHeadersListener {

        @Override
        public void headers(Headers headers) {
            //Log.d("headers", "headers");
            synchronized (RetrofitDownloadTask.this) {
                for (String name : headers.names()) {
                    if (name.equalsIgnoreCase("Content-Disposition")) {
                        contentDisposition = headers.get(name);
                        break;
                    }
                }
            }
        }
    }

    class ProgressListenerImpl implements ProgressListener {

        boolean firstUpdate = true;

        @Override
        public void reset() {
            if (isIdle()) {
                firstUpdate = true;
                getEntity().setTotalBytes(-1L);
                getEntity().setDownloadBytes(0L);
            }
        }

        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            //Log.d(ELoad.TAG, "bytesRead=" + bytesRead + ",contentLength=" + contentLength + ",done=" + done);
            if (done) {
                //System.out.println("completed");
                for (DownloadListener downloadListener : getDownloadListeners())
                    downloadListener.onDownloadComplete(RetrofitDownloadTask.this);
                if (downloadEmitter != null)
                    downloadEmitter.onNext(DownloadTaskWrapper.downloadComplete(RetrofitDownloadTask.this));
            } else {
                if (firstUpdate) {
                    firstUpdate = false;
                    if (getEntity().getTotalBytes() == -1L) {
                        getEntity().setTotalBytes(contentLength);
                    }
                    for (DownloadListener downloadListener : getDownloadListeners())
                        downloadListener.onDownloadContentLengthRead(RetrofitDownloadTask.this, contentLength);
                    //System.out.format("content-length: %d\n", contentLength);
                    if (downloadEmitter != null)
                        downloadEmitter.onNext(DownloadTaskWrapper.downloadContentLength(
                                RetrofitDownloadTask.this, contentLength));
                }

                //System.out.println(bytesRead);
                /*if (contentLength != -1) {
                    System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
                }*/
                setDownloadBytesTemp(getEntity().getDownloadBytes() + bytesRead);
                for (DownloadListener downloadListener : getDownloadListeners())
                    downloadListener.onDownloadBytesRead(RetrofitDownloadTask.this, bytesRead);
                if (downloadEmitter != null)
                    downloadEmitter.onNext(DownloadTaskWrapper.downloadBytesRead(
                            RetrofitDownloadTask.this, bytesRead));
            }
        }
    }

    RetrofitDownloadTask(Builder builder) {
        progressListener = new ProgressListenerImpl();
        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(new ResponseHeadersListenerImpl(), progressListener);
        OkHttpClient client = builder.okHttpClientFactory.newBuilder().addNetworkInterceptor(downloadInterceptor).build();
        Retrofit retrofit = builder.retrofitFactory.newBuilder().client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        setEntity(builder.entity);
        setTaskRecycler(builder.taskRecycler);
        setFileProcessor(builder.fileProcessor);
        setDatabaseManager(builder.databaseManager);
        this.downloadService = retrofit.create(DownloadService.class);
        attach(builder);
    }

    /**
     * 下载任务准备。当且仅当当前状态为空闲状态---idle
     *
     * @param builder 设置下载url,headers,文件路径,文件名称等
     * @return 是否成功。如已经是准备状态将会返回false，并不会触发监听
     */
    @Override
    public boolean attach(Builder builder) {
        if (isRunning()) {
            Log.e(ELoad.TAG, "cant attach builder when running");
            return false;
        }
        fromBuilder(builder);
        resetHeadersAndProgressListener();
        if (isIdle()) {
            toAttach();
        }
        return true;
    }

    private void resetHeadersAndProgressListener() {
        RangeHeader.copyRangeHeader(getEntity().getHeaders(), headersWrapper);
        progressListener.reset();
    }

    /**
     * 开始下载。非强制重新下载时，当且仅当当前状态为准备状态---prepare
     * 可在准备---prepare，暂停---pause，异常---error时强制重新下载
     *
     * @param force 是否强制重新下载
     * @return 是否成功。如已是下载状态将会返回false，并不会触发监听，
     */
    @Override
    public boolean start(boolean force) {
        if (isRunning()) {
            infoState("running");
            return false;
        }
        if (!force && isAttach() || force && (isPause() || isCancel() || isError())) {
            if (force) {
                resetHeadersAndProgressListener();
            }
            Observable<ResponseBody> observable = downloadService.download(headersWrapper, getEntity().getUrl())
                    //.doOnSubscribe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());
            //.observeOn(AndroidSchedulers.mainThread());
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskPrepare(this, observable);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskPrepare(this, observable);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskPrepare(this, observable));
            disposable = observable.subscribe(this::downloadSuccess, this::downloadError, this::downloadComplete);
            toRunning();
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskStart(this);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskStart(this);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskStart(this));
            return true;
        } else {
            if (force)
                errorState("pause or cancel or error", "start(true)");
            else
                errorState("prepare", "start(false)");
            return false;
        }
    }

    private void downloadSuccess(ResponseBody responseBody) {
        synchronized (RetrofitDownloadTask.this) {
            if (contentDisposition != null && TextUtils.isEmpty(getEntity().getFileName())) {
                String rfn = null;
                String fn = null;
                if (contentDisposition.contains("filename=")) {
                    fn = contentDisposition.split("filename=")[1].split(";")[0];
                } else if (contentDisposition.contains("filename*=")) {
                    fn = contentDisposition.split("filename\\*=")[1].split(";")[0].split("''")[1];
                }
                if (getEntity().getUrlDecoder() != null) {
                    try {
                        rfn = URLDecoder.decode(fn, getEntity().getUrlDecoder());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    rfn = fn;
                }
                getEntity().setFileName(rfn);
            }
            if (TextUtils.isEmpty(getEntity().getFileName())) {
                MediaType mediaType = responseBody.contentType();
                if (mediaType != null) {
                    String type = mediaType.toString();
                    MimeTypes types = MimeTypes.getDefaultMimeTypes();
                    try {
                        String ext = types.forName(type).getExtension();
                        getEntity().setFileName(getEntity().getCreateTime() + ext);
                    } catch (MimeTypeException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (TextUtils.isEmpty(getEntity().getFileName())) {
                getEntity().setFileName(getEntity().getCreateTime() + ".eload");
            }
        }
        try {
            getFileProcessor().writeFile(responseBody, getEntity().getDownloadBytes(), getEntity().getFilePath(),
                    getEntity().getFileName());
        } catch (Exception e) {
            //e.printStackTrace();
            if (isRunning())
                downloadError(e);
        }
        /*String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc|html|htm|ico|java|jsp|";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");//正则判断
        Matcher mc = pat.matcher(getEntity().getUrl());//条件匹配
        while (mc.find()) {
            String fn = mc.group();//截取文件名后缀名
            //Log.e("fn", fn);
            getEntity().setFileName(fn);
            break;
        }*/
    }

    private void downloadComplete() {
        if (isPause() || isCancel() || isError())
            return;
        getEntity().setDownloadBytes(getDownloadBytesTemp());
        toFinish();
        if (getDatabaseManager() != null)
            getDatabaseManager().onDownloadTaskComplete(this);
        for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
            downloadTaskListener.onDownloadTaskComplete(this);
        if (taskEmitter != null)
            taskEmitter.onNext(DownloadTaskWrapper.taskComplete(this));
        //recycle();
    }

    private void downloadError(Throwable e) {
        //Log.d(ELoad.TAG, "Error");
        //e.printStackTrace();
        toError();
        getEntity().setDownloadBytes(getDownloadBytesTemp());
        if (getDatabaseManager() != null)
            getDatabaseManager().onDownloadTaskError(this, e);
        for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
            downloadTaskListener.onDownloadTaskError(this, e);
        if (taskEmitter != null)
            taskEmitter.onNext(DownloadTaskWrapper.taskError(this, e));
    }

    @Override
    public boolean pause() {
        if (isPause()) {
            infoState("pause");
            return false;
        }
        if (isRunning()) {
            if (disposable.isDisposed())
                return true;
            disposable.dispose();
            getEntity().setDownloadBytes(getDownloadBytesTemp());
            toPause();
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskPause(this);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskPause(this);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskPause(this));
            return true;
        } else {
            errorState("running", "pause");
            return false;
        }
    }

    @Override
    public boolean resume() {
        if (isRunning()) {
            infoState("running");
            return false;
        }
        if (isPause() || isError()) {
            //long start = RangeHeader.getStartRange(getHeaders());
            RangeHeader.updateRangeHeader(headersWrapper, getEntity().getDownloadBytes());
            toAttach();
            start();
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskResume(this);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskResume(this);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskResume(this));
            return true;
        } else {
            errorState("pause or error", "resume");
            return false;
        }
    }

    @Override
    public boolean cancel(boolean delFile) {
        if (isCancel()) {
            infoState("cancel");
            return false;
        }
        if (!isPause())
            pause();
        if (isPause()) {
            if (delFile && !TextUtils.isEmpty(getEntity().getFileName())) {
                new File(getEntity().getFilePath(), getEntity().getFileName()).delete();
            }
            toCancel();
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskCancel(this);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskCancel(this);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskCancel(this));
            return true;
        } else {
            errorState("running or pause", "cancel");
            return false;
        }
    }

    @Override
    public boolean reset() {
        if (isIdle()) {
            infoState("idle");
            return false;
        }
        if (isFinish() || isError() || isCancel()) {
            toIdle();
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskReset(this);
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskReset(this);
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskReset(this));
            return true;
        } else {
            errorState("finish or error or cancel", "reset");
            return false;
        }
    }

    @Override
    public boolean recycle() {
        if (!isIdle()) {
            if (!reset()) {
                return false;
            }
        }
        if (getTaskRecycler() != null) {
            getTaskRecycler().recycle(this);
            if (getDatabaseManager() != null)
                getDatabaseManager().onDownloadTaskRecycle(this, getTaskRecycler());
            for (DownloadTaskListener downloadTaskListener : getDownloadTaskListeners())
                downloadTaskListener.onDownloadTaskRecycle(this, getTaskRecycler());
            if (taskEmitter != null)
                taskEmitter.onNext(DownloadTaskWrapper.taskRecycle(this, getTaskRecycler()));
            rxComplete();
            return true;
        } else {
            Log.w(ELoad.TAG, "Cant Recycle!task recycler is null");
            rxComplete();
            return false;
        }
    }

    private void rxComplete() {
        if (downloadEmitter != null)
            downloadEmitter.onComplete();
        if (taskEmitter != null)
            taskEmitter.onComplete();
    }

    @Override
    public Observable<DownloadTaskWrapper> toObservable() {
        Observable<DownloadTaskWrapper> observable = Observable.create(emitter -> {
            downloadEmitter = emitter;
            taskEmitter = emitter;
        });
        return observable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<DownloadTaskWrapper> toDownloadObservable() {
        Observable<DownloadTaskWrapper> observable = Observable.create(emitter -> downloadEmitter = emitter);
        return observable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<DownloadTaskWrapper> toTaskObservable() {
        Observable<DownloadTaskWrapper> observable = Observable.create(emitter -> taskEmitter = emitter);
        return observable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<DownloadTaskWrapper> toFlowable(BackpressureStrategy strategy) {
        Flowable<DownloadTaskWrapper> flowable = Flowable.create(emitter -> {
            downloadEmitter = emitter;
            taskEmitter = emitter;
        }, strategy);
        return flowable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<DownloadTaskWrapper> toDownloadFlowable(BackpressureStrategy strategy) {
        Flowable<DownloadTaskWrapper> flowable = Flowable.create(emitter -> downloadEmitter = emitter, strategy);
        return flowable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<DownloadTaskWrapper> toTaskFlowable(BackpressureStrategy strategy) {
        Flowable<DownloadTaskWrapper> flowable = Flowable.create(emitter -> taskEmitter = emitter, strategy);
        return flowable
                //.doOnSubscribe(it -> start())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void errorState(String states, String method) {
        Log.e(ELoad.TAG, "Error State(" + getEntity().getState() + ")!The state must be " + states + " when " + method);
    }

    private void infoState(String state) {
        Log.i(ELoad.TAG, "This is the current state: " + state);
    }

    @Override
    public int hashCode() {
        return downloadService.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RetrofitDownloadTask) {
            return ((RetrofitDownloadTask) obj).downloadService.equals(downloadService);
        }
        return super.equals(obj);
    }
}
