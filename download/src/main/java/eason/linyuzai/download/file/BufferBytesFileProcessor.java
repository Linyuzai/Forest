package eason.linyuzai.download.file;

public abstract class BufferBytesFileProcessor extends FileProcessor {

    private int bufferBytesLength;

    public BufferBytesFileProcessor(int bufferBytesLength) {
        this.bufferBytesLength = bufferBytesLength;
    }

    public BufferBytesFileProcessor() {
        this(1024);
    }

    public int getBufferBytesLength() {
        return bufferBytesLength;
    }

    public void setBufferBytesLength(int bufferBytesLength) {
        this.bufferBytesLength = bufferBytesLength;
    }
}
