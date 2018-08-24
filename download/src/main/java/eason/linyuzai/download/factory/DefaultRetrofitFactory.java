package eason.linyuzai.download.factory;

import retrofit2.Retrofit;

public class DefaultRetrofitFactory implements RetrofitFactory {

    private static DefaultRetrofitFactory factory = new DefaultRetrofitFactory();

    private DefaultRetrofitFactory() {
    }

    public static DefaultRetrofitFactory get() {
        return factory;
    }

    @Override
    public Retrofit.Builder newBuilder() {
        return new Retrofit.Builder().baseUrl("http://eload");
    }
}
