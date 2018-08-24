package eason.linyuzai.download.task;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

class DownloadInterceptor implements Interceptor {

    private DownloadTask.ResponseHeadersListener headersListener;
    private DownloadTask.ProgressListener progressListener;

    DownloadInterceptor(DownloadTask.ResponseHeadersListener headersListener, DownloadTask.ProgressListener progressListener) {
        this.headersListener = headersListener;
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        headersListener.headers(originalResponse.headers());
        TaskResponseBody responseBody = new TaskResponseBody(originalResponse.body(), progressListener);
        return originalResponse.newBuilder()
                //.headers(originalResponse.headers())
                .body(responseBody)
                .build();
    }
}
