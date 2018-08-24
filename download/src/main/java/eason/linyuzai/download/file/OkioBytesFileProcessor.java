package eason.linyuzai.download.file;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class OkioBytesFileProcessor extends BufferBytesFileProcessor {

    public OkioBytesFileProcessor(int bufferBytesLength) {
        super(bufferBytesLength);
    }

    public OkioBytesFileProcessor() {
        super();
    }

    @Override
    public void writeFile(ResponseBody responseBody, long start, String filePath, String fileName) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(Okio.appendingSink(getOrCreateFile(filePath, fileName)));
        BufferedSource bufferedSource = Okio.buffer(Okio.source(responseBody.byteStream()));
        byte[] buffer = new byte[getBufferBytesLength()];
        int len;
        while ((len = bufferedSource.read(buffer)) != -1) {
            bufferedSink.write(buffer, 0, len);
        }
        bufferedSink.flush();
        bufferedSink.close();
        bufferedSource.close();
    }
}
