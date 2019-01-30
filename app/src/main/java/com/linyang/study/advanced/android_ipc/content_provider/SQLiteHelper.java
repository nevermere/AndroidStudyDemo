package com.linyang.study.advanced.android_ipc.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述:
 * Created by fzJiang on 2018-11-08
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "content_provider_test.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_USER_NAME = "user";
    static final String TABLE_BOOK_NAME = "book";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT," + "sex INT)";
    private static final String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOK_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT)";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
