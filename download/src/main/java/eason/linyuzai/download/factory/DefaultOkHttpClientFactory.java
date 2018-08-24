package eason.linyuzai.download.factory;

import okhttp3.OkHttpClient;

public class DefaultOkHttpClientFactory implements OkHttpClientFactory {

    private static DefaultOkHttpClientFactory factory = new DefaultOkHttpClientFactory();

    private DefaultOkHttpClientFactory() {
    }

    public static DefaultOkHttpClientFactory get() {
        return factory;
    }

    @Override
    public OkHttpClient.Builder newBuilder() {
        return new OkHttpClient.Builder().retryOnConnectionFailure(true);
    }
}
