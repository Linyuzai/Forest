package eason.linyuzai.download.file;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public class OkioSourceFileProcessor extends FileProcessor {

    @Override
    public void writeFile(ResponseBody responseBody, long start, String filePath, String fileName) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(Okio.appendingSink(getOrCreateFile(filePath, fileName)));
        Source source = Okio.source(responseBody.byteStream());
        bufferedSink.writeAll(source);
        bufferedSink.flush();
        bufferedSink.close();
        source.close();
    }
}
