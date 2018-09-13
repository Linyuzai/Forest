package eason.linyuzai.download.file;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class OkioSourceFileProcessor extends FileProcessor {

    @Override
    public void writeFile(ResponseBody responseBody, long start, String filePath, String fileName) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(Okio.appendingSink(getOrCreateFile(filePath, fileName)));
        BufferedSource bufferedSource = responseBody.source();
        bufferedSink.writeAll(bufferedSource);
        bufferedSink.flush();
        bufferedSource.close();
        bufferedSink.close();
    }
}
