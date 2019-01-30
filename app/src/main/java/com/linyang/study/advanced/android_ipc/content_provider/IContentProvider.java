package com.linyang.study.advanced.android_ipc.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.linyang.study.app.util.LogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 描述:
 * Created by fzJiang on 2018-11-08
 */
public class IContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.linyang.study.server.provider";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");

    public static final int USER_URI_CODE = 0;
    public static final int BOOK_URI_CODE = 1;

    private static final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
        mMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        // 初始化
        mContext = getContext();
        mDatabase = new SQLiteHelper(mContext).getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogUtil.i("-----query-----");

        String tableName = getTableName(uri);
        if (!TextUtils.isEmpty(tableName)) {
            return mDatabase.query(tableName, projection, selection, selectionArgs, null, sortOrder, null);
        }
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        LogUtil.i("-----getType-----");
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (!TextUtils.isEmpty(tableName)) {
            long count = mDatabase.insert(tableName, null, values);
            LogUtil.i("-----insert-----" + count);

            mContext.getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (!TextUtils.isEmpty(tableName)) {
            int count = mDatabase.delete(tableName, selection, selectionArgs);
            LogUtil.i("-----delete-----" + count);

            if (count > 0) {
                mContext.getContentResolver().notifyChange(uri, null);
            }
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (!TextUtils.isEmpty(tableName)) {
            int count = mDatabase.update(tableName, values, selection, selectionArgs);
            LogUtil.i("-----update-----" + count);

            if (count > 0) {
                mContext.getContentResolver().notifyChange(uri, null);
            }
        }
        return 0;
    }

    private String getTableName(Uri uri) {
        String tableName = "";
        switch (mMatcher.match(uri)) {
            case USER_URI_CODE:
                tableName = SQLiteHelper.TABLE_USER_NAME;
                break;
            case BOOK_URI_CODE:
                tableName = SQLiteHelper.TABLE_BOOK_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
