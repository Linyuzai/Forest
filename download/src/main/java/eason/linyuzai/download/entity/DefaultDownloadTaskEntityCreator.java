package eason.linyuzai.download.entity;

public class DefaultDownloadTaskEntityCreator implements DownloadTaskEntity.Creator {

    private static DefaultDownloadTaskEntityCreator creator = new DefaultDownloadTaskEntityCreator();

    public static DefaultDownloadTaskEntityCreator get() {
        return creator;
    }

    private DefaultDownloadTaskEntityCreator() {
    }

    @Override
    public DownloadTaskEntity create() {
        return new DefaultDownloadTaskEntity();
    }
}
