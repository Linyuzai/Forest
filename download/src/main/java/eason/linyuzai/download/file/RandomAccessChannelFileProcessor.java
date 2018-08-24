package eason.linyuzai.download.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

public class RandomAccessChannelFileProcessor extends BufferBytesFileProcessor {

    public RandomAccessChannelFileProcessor() {
        super();
    }

    public RandomAccessChannelFileProcessor(int bufferBytesLength) {
        super(bufferBytesLength);
    }

    @Override
    public void writeFile(ResponseBody body, long start, String filePath, String fileName) throws IOException {
        InputStream in = body.byteStream();
        // 随机访问文件，可以指定断点续传的起始位置
        File file = getOrCreateFile(filePath, fileName);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
        FileChannel channelOut = randomAccessFile.getChannel();
        // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, start, body.contentLength());
        byte[] buffer = new byte[getBufferBytesLength()];
        int len;
        while ((len = in.read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
        }
        mappedBuffer.force();
        in.close();
        channelOut.close();
        randomAccessFile.close();
        in.close();
        channelOut.close();
        randomAccessFile.close();
    }
}
