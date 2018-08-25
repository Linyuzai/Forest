package eason.linyuzai.download.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import eason.linyuzai.download.entity.DownloadTaskEntity;

public class SQLiteManager extends DatabaseManager {

    private SQLiteOpenHelper helper;

    private AtomicInteger openCounter = new AtomicInteger();
    private SQLiteDatabase database;

    public SQLiteManager(Context context) {
        helper = new Helper(context);
    }

    @Override
    public boolean createDownloadTask(DownloadTaskEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(getColumnTaskId(), entity.getTaskId());
        contentValues.put(getColumnCreateTime(), entity.getCreateTime());
        contentValues.put(getColumnState(), entity.getState());
        contentValues.put(getColumnHeaders(), fromObject(entity.getHeaders()));
        contentValues.put(getColumnUrl(), entity.getUrl());
        contentValues.put(getColumnUrlDecoder(), entity.getUrlDecoder());
        contentValues.put(getColumnTotalBytes(), entity.getTotalBytes());
        contentValues.put(getColumnDownloadBytes(), entity.getDownloadBytes());
        contentValues.put(getColumnFilePath(), entity.getFilePath());
        contentValues.put(getColumnFileName(), entity.getFileName());
        contentValues.put(getColumnExtra(), fromObject(entity.getExtra()));

        long row = getDatabase().insert(getTableName(), null, contentValues);
        close();
        return row != -1;
    }

    @Override
    public boolean updateDownloadTask(DownloadTaskEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(getColumnTotalBytes(), entity.getTotalBytes());
        contentValues.put(getColumnDownloadBytes(), entity.getDownloadBytes());
        contentValues.put(getColumnState(), entity.getState());
        if (entity.getFileName() != null)
            contentValues.put(getColumnFileName(), entity.getFileName());
        int count = getDatabase().update(getTableName(), contentValues,
                getColumnTaskId() + "=?", new String[]{entity.getTaskId()});
        close();
        return count == 1;
    }

    @Override
    public boolean deleteDownloadTask(DownloadTaskEntity entity) {
        int count = getDatabase().delete(getTableName(), getColumnTaskId() + "=?", new String[]{entity.getTaskId()});
        close();
        return count == 1;
    }

    @Override
    public int count(DownloadTaskEntity entity) {
        Cursor cursor = getDatabase().query(getTableName(), null,
                getColumnTaskId() + "=?", new String[]{entity.getTaskId()}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        return count;
    }

    @Override
    public List<? extends DownloadTaskEntity> selectAll(DownloadTaskEntity.Creator creator) {
        List<DownloadTaskEntity> entities = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery("select * from " + getTableName(), null);
        while (cursor.moveToNext()) {
            String taskId = cursor.getString(cursor.getColumnIndex(getColumnTaskId()));
            long createTime = cursor.getLong(cursor.getColumnIndex(getColumnCreateTime()));
            int state = cursor.getInt(cursor.getColumnIndex(getColumnState()));
            byte[] headers = cursor.getBlob(cursor.getColumnIndex(getColumnHeaders()));
            String url = cursor.getString(cursor.getColumnIndex(getColumnUrl()));
            String urlDecoder = cursor.getString(cursor.getColumnIndex(getColumnUrlDecoder()));
            long totalBytes = cursor.getLong(cursor.getColumnIndex(getColumnTotalBytes()));
            long downloadBytes = cursor.getLong(cursor.getColumnIndex(getColumnDownloadBytes()));
            String filepath = cursor.getString(cursor.getColumnIndex(getColumnFilePath()));
            String filename = cursor.getString(cursor.getColumnIndex(getColumnFileName()));
            byte[] extra = cursor.getBlob(cursor.getColumnIndex(getColumnExtra()));

            DownloadTaskEntity entity = creator.create();
            entity.setTaskId(taskId);
            entity.setCreateTime(createTime);
            entity.setState(state);
            entity.setHeaders(toObject(headers));
            entity.setUrl(url);
            entity.setUrlDecoder(urlDecoder);
            entity.setTotalBytes(totalBytes);
            entity.setDownloadBytes(downloadBytes);
            entity.setFilePath(filepath);
            entity.setFileName(filename);
            entity.setExtra(toObject(extra));

            entities.add(entity);
        }
        cursor.close();
        close();
        return entities;
    }

    @Override
    public void deleteAll() {
        getDatabase().delete(getTableName(), null, null);
        close();
    }

    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return helper;
    }

    public void setSQLiteOpenHelper(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public synchronized SQLiteDatabase getDatabase() {
        if (openCounter.incrementAndGet() == 1) {
            // Opening new database
            database = helper.getWritableDatabase();
        }
        return database;
    }

    public synchronized void close() {
        if (database != null && openCounter.decrementAndGet() == 0) {
            // Closing database
            database.close();
        }
    }

    public class Helper extends SQLiteOpenHelper {

        Helper(Context context) {
            super(context, DatabaseManager.getDatabaseName(), null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_TABLE = "create table if not exists " + getTableName() + " ("
                    //+ "id integer primary key autoincrement,"
                    + getColumnTaskId() + " varchar(16) primary key,"
                    + getColumnCreateTime() + " integer not null,"
                    + getColumnState() + " integer not null,"
                    + getColumnHeaders() + " blob,"
                    + getColumnUrl() + " varchar(255) not null,"
                    + getColumnUrlDecoder() + " varchar(16),"
                    + getColumnTotalBytes() + " integer not null,"
                    + getColumnDownloadBytes() + " integer not null,"
                    + getColumnFilePath() + " varchar(32) not null,"
                    + getColumnFileName() + " varchar(16),"
                    + getColumnExtra() + " blob"
                    + ");";
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
