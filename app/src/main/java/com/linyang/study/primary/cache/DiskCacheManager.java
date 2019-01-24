package com.linyang.study.primary.cache;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;
import com.linyang.study.BuildConfig;

import java.io.File;
import java.io.IOException;


/**
 * 描述:磁盘缓存工具类
 * Created by fzJiang on 2018/12/28 13:42 星期五
 */
public class DiskCacheManager {

    private static final int CACHE_MAXSIZE = 1024 * 1024 * 10;

    private DiskLruCache mDiskLruCache;
    private DiskLruCache.Editor mEditor;
    private DiskLruCache.Snapshot mSnapshot;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DiskCacheManager(Context context, String uniqueName) {
        try {
            // 先关闭已有的缓存
            if (mDiskLruCache != null) {
                mDiskLruCache.close();
                mDiskLruCache = null;
            }
            File cacheFile = getCacheFile(context, uniqueName);
            if (!cacheFile.exists()) {
                cacheFile.mkdir();
            }
            mDiskLruCache = DiskLruCache.open(cacheFile, BuildConfig.VERSION_CODE, 1, CACHE_MAXSIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存的路径 两个路径在卸载程序时都会删除，因此不会在卸载后还保留乱七八糟的缓存
     * 有SD卡时获取  /sdcard/Android/data/<application package>/cache
     * 无SD卡时获取  /data/data/<application package>/cache
     *
     * @param context    上下文
     * @param uniqueName 缓存目录下的细分目录，用于存放不同类型的缓存
     * @return 缓存目录 File
     */
    private File getCacheFile(Context context, String uniqueName) {
        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable())
                && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取缓存
     *
     * @return
     */
    public DiskLruCache getDiskLruCache() {
        return mDiskLruCache;
    }

    /**
     * 获取缓存 editor
     *
     * @param key 缓存的key
     * @return editor
     */
    public DiskLruCache.Editor getEditor(String key) {
        try {
            if (mDiskLruCache != null) {
                mEditor = mDiskLruCache.edit(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mEditor;
    }

    /**
     * 根据 key 获取缓存缩略
     *
     * @param key 缓存的key
     * @return Snapshot
     */
    public DiskLruCache.Snapshot getSnapshot(String key) {
        try {
            if (mDiskLruCache != null) {
                mSnapshot = mDiskLruCache.get(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mSnapshot;
    }

    public void flushCache() {
        try {
            if (mDiskLruCache != null) {
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
