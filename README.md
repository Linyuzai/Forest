# Eason-Download
- **断点续传**
- **多任务**
- **数据库存储**
---
![preview.gif](https://upload-images.jianshu.io/upload_images/2113387-ab70c6194e00935a.gif?imageMogr2/auto-orient/strip)
```
//核心库
implementation 'eason.linyuzai:eason-download:1.0.1'
//如有需要可以添加额外的监听器
implementation 'eason.linyuzai:eason-download-listeners:1.0.1'
```
最简单的用法
```
//可以当成Http Client
ELoad eload = ELoad.Builder(Context context).build();
//可以当成一个Request
DownloadTask task = eload.url(String url).create();
task.start();
```
**自定义**配置
**ELoad配置**
```
//ELoad配置，配置对所有该ELoad生成的Task生效
ELoad eload = ELoad.Builder(Context context)
//任务回收器，默认使用TaskQueueRecycler
.setTaskRecycler(TaskRecycler taskRecycler)
//文件写入器，默认使用OkioSourceFileProcessor
.setFileProcessor(FileProcessor fileProcessor)
//下载路径，默认context.getExternalCacheDir()+"/ELoad"
.setDownloadPath(String downloadPath)
//生成OkHttpClient.Builder，默认使用DefaultOkHttpClientFactory
.setOkHttpClientFactory(OkHttpClientFactory okHttpClientFactory)
//生成Retrofit.Builder，默认使用DefaultRetrofitFactory
.setRetrofitFactory(RetrofitFactory retrofitFactory)
//数据库，默认null，不写入数据库
//提供SQLiteManager，也可自己定义，使用其他ORM库
.setDatabaseManager(DatabaseManager databaseManager)
//生成数据库Bean，如有需要，用于扩展注解式ORM框架
.setEntityCreator(DownloadTaskEntity.Creator entityCreator)
//下载进度监听
.addDownloadListeners(DownloadTask.DownloadListener listener)
//下载任务状态监听
.addDownloadTaskListener(DownloadTask.DownloadTaskListener listener)
.build();
```
**DownloadTask配置**
```
//方式1:
DownloadTask task = eload.url(String url)//url配置
//添加header
.header(String headerName, String headerValue)
//设置headers
.headers(Map<String, String> headers)
//下载路径，对单个任务有效
.filepath(String filepath)
//文件名称，不设置会尝试根据Response等信息获取或随机生成
.filename(String filename)
//URLDecoder，配合服务端进行中文的编解码等
.urlDecoder(String urlDecoder)
//自定义数据
.extra(Serializable extra)
//下载进度监听，对单个任务有效
.downloadListener(DownloadTask.DownloadListener listener)
//下载任务状态监听，对单个任务有效
.downloadTaskListener(DownloadTask.DownloadTaskListener listener)
.create();

//方式2:
//通过数据库
List<DownloadTaskEntity> entities = eload.loadTaskEntitiesFromDatabase()
//将保存的任务记录重新生成DownloadTask
DownloadTask task = eload.convert(DownloadTaskEntity entity)
//下载进度监听，对单个任务有效
.downloadListener(DownloadTask.DownloadListener listener)
//下载任务状态监听，对单个任务有效
.downloadTaskListener(DownloadTask.DownloadTaskListener listener)
.create();
```
**操作DownloadTask**
```
//开始
DownloadTask.start();
//强制开始，在暂停，取消，异常的状态时可以重新下载
DownloadTask.start(true);
//暂停
DownloadTask.pause();
//继续，在暂停或异常的状态时可以继续下载
DownloadTask.resume();
//取消
DownloadTask.cancel();
//取消，并删除已下载的部分文件
DownloadTask.cancel(true);
//回收，可以在任务未开始，完成，异常，取消的状态时回收，复用OkHttpClient
DownloadTask.recycle();
```
**DownloadTask属性**
```
//获得DownloadTaskEntity
DownloadTaskEntity getEntity();
//running状态时，下载总进度
long getDownloadBytesTemp();
//是否是空闲状态
boolean isIdle();
//是否是准备状态
boolean isAttach();
//是否是下载状态
boolean isRunning();
//是否是暂停状态
boolean isPause();
//是否完成
boolean isFinish();
//是否异常
boolean isError();
//是否取消
boolean isCancel();
```
**DownloadTaskEntity属性**
```
//TaskId
String getTaskId();
//创建时间
long getCreateTime();
//执行状态
int getState();
//http(s) Headers
Map<String, String> getHeaders();
//Url
String getUrl();
//URLDecoder
String getUrlDecoder();
//文件总大小
long getTotalBytes();
//已下载大小，只有暂停，取消，异常时才会更新
//更新进度请用DownloadTask.getDownloadBytesTemp()
long getDownloadBytes();
//文件路径
String getFilePath();
//文件名字
String getFileName();
//额外数据
Serializable getExtra();
```
使用RxJava2代替DownloadListener和DownloadTaskListener的监听
```
//回调下载进度和任务状态
DownloadTask.toObservable();
//回调下载进度
DownloadTask.toDownloadObservable();
//回调任务状态
DownloadTask.toTaskObservable();

DownloadTask.toObservable().subscribe({
    switch(it.callType) {
    case DownloadTaskWrapper.DOWNLOAD_CONTENT_LENGTH:
        //文件大小
        break;
    case DownloadTaskWrapper.DOWNLOAD_BYTES_READ:
        //字节读入
        break;
    case DownloadTaskWrapper.DOWNLOAD_COMPLETE:
        //下载完成
        break;
    case DownloadTaskWrapper.TASK_PREPARE:
        //任务准备
        break;
    case DownloadTaskWrapper.TASK_START:
        //任务开始
        break;
    case DownloadTaskWrapper.TASK_PAUSE:
        //任务暂停
        break;
    case DownloadTaskWrapper.TASK_RESUME:
        //任务继续
        break;
    case DownloadTaskWrapper.TASK_ERROR:
        //任务异常
        break;
    case DownloadTaskWrapper.TASK_COMPLETE:
        //任务完成
        break;
    case DownloadTaskWrapper.TASK_CANCEL:
        //任务取消
        break;
    case DownloadTaskWrapper.TASK_RESET:
        //任务重置
        break;
    case DownloadTaskWrapper.TASK_RECYCLE:
        //任务回收
        break;
    }
});
DownloadTask.start();
```
也提供Flowable的转换
**额外的监听器**
```
//网速计算
NetPerSecDownloadListener(TextView textView)
//剩余时间
RemainingTimeDownloadListener(TextView textView)
//百分比下载进度
PercentProgressDownloadListener(TextView textView, ProgressBar progressBar, long interval)
//具体数据下载进度
NumberProgressDownloadListener(TextView textView, ProgressBar progressBar, long interval)
//上述监听器都可以设置回调间隔时间
//主线程回调下载进度监听
MainThreadDownloadListener()
//主线程回调下载任务状态监听
MainThreadDownloadTaskListener()
```
