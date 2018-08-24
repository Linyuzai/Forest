package eason.linyuzai.download.factory;

import okhttp3.OkHttpClient;

public interface OkHttpClientFactory {

    OkHttpClient.Builder newBuilder();
}
