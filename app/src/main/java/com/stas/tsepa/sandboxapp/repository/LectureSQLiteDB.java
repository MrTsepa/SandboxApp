package com.stas.tsepa.sandboxapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stas.tsepa.sandboxapp.LectureItem;

import java.util.ArrayList;
import java.util.List;

public class LectureSQLiteDB implements Repository<LectureItem> {

    private static final String DB_NAME = "lectoryi";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "lecture";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DURATION = "duration";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TITLE + " text, " +
                        COLUMN_DURATION + " integer" +
                    ");";

    private final Context mContext;
    private final Callback<LectureItem> mCallback;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public LectureSQLiteDB(Context mContext, Callback<LectureItem> mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        open();
    }

    private void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }

    private void add(LectureItem item) {
        mDB.insert(DB_TABLE, null, getContentValues(item));
    }

    @Override
    public void addAll(List<LectureItem> items) {
        for (LectureItem item : items) {
            add(item);
        }
        mCallback.onDataAppended(items);
    }

    @Override
    public List<LectureItem> getAll() {
        Cursor cursor = mDB.query(DB_TABLE, null, null, null, null, null, null);
        if (cursor == null)
            return null;
        List<LectureItem> lectureItems = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LectureItem item = getLectureItem(cursor);
            if (item != null) {
                lectureItems.add(item);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return lectureItems;
    }

    @Override
    public int getCount() {
        Cursor cursor = mDB.query(DB_TABLE, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void clear() {
        mDB.delete(DB_TABLE, null, null);
        mCallback.onDataDeleted();
    }

    private LectureItem getLectureItem(Cursor cursor) {
        if (cursor == null)
            return null;
        if (cursor.isBeforeFirst() || cursor.isAfterLast())
            return null;
        LectureItem item = new LectureItem();
        item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        item.setDuration(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION)));
        return item;
    }

    private ContentValues getContentValues(LectureItem item) {
        if (item == null)
            return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, item.getTitle());
        contentValues.put(COLUMN_DURATION, item.getDuration());
        return contentValues;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
