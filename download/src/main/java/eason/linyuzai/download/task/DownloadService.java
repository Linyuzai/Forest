package eason.linyuzai.download.task;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

interface DownloadService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@HeaderMap Map<String, String> headers, @Url String url);
}
