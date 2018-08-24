package eason.linyuzai.download.file;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;

public abstract class FileProcessor {

    public File getOrCreateFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //Log.d("ChannelFileProcessor", file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public abstract void writeFile(ResponseBody responseBody, long start, String filePath, String fileName) throws IOException;
}
