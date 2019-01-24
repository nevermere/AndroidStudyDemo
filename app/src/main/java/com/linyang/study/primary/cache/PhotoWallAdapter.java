package com.linyang.study.primary.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.linyang.study.R;
import com.linyang.study.app.util.LogUtil;

import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描述:
 * Created by fzJiang on 2018/12/29 9:31 星期六
 */
public class PhotoWallAdapter extends ArrayAdapter<String> {

    private CompositeDisposable composite;// 记录所有正在下载或等待下载的任务
    private LruCache<String, Bitmap> mMemoryCache;// 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉
    private DiskCacheManager mDiskCacheManager;// 磁盘缓存工具类
    private GridView mGridView;
    private int mItemHeight = 0;// 记录每个子项的高度

    PhotoWallAdapter(Context context, int textViewResourceId, String[] objects, GridView gridView) {
        super(context, textViewResourceId, objects);
        this.mGridView = gridView;

        // 获取应用程序最大可用内存/8
        int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
        this.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        this.mDiskCacheManager = new DiskCacheManager(context.getApplicationContext(), "thumb");
        this.composite = new CompositeDisposable();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_photo_wall_item, parent, false);
        } else {
            view = convertView;
        }

        // 设置行高
        final ImageView imageView = view.findViewById(R.id.iv_photo);
//        if (imageView.getLayoutParams().height != mItemHeight) {
//            imageView.getLayoutParams().height = mItemHeight;
//        }

        String url = getItem(position);
        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        imageView.setTag(url);
        loadBitmap(url, imageView);

        return view;
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     */
    private void loadBitmap(String imageUrl, ImageView imageView) {
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            // 首先尝试获取缓存中的图片
            Bitmap cacheBitmap = getBitmapFromMemoryCache(imageUrl);
            // 无缓存，则从网络获取后存入缓存
            if (cacheBitmap == null) {
                // 以MD5加密，作为存储key值
                String md5Key = MD5Util.getMD5String(imageUrl);
//                LogUtil.i("md5Key:" + md5Key);

                // 执行下载操作并存入缓存
                DiskLruCache.Editor editor = mDiskCacheManager.getEditor(md5Key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (outputStream != null) {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(imageUrl).build();
                        Response response = okHttpClient.newCall(request).execute();
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            byte[] imageBytes = responseBody.bytes();
                            outputStream.write(imageBytes);
                            editor.commit();
                            outputStream.close();
                            Bitmap loadBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            if (loadBitmap != null) {
                                // 存入缓存
                                addBitmapToMemoryCache(md5Key, loadBitmap);
                                emitter.onNext(loadBitmap);
                            } else {
                                emitter.onError(new Throwable("no bitmap"));
                            }
                        } else {
                            editor.abort();
                            emitter.onError(new Throwable("no ResponseBody"));
                        }
                    } else {
                        editor.abort();
                        emitter.onError(new Throwable("no OutputStream"));
                    }
                } else {
                    emitter.onError(new Throwable("no DiskLruCache Editor"));
                }
            } else {
                emitter.onNext(cacheBitmap);
            }
            emitter.onComplete();

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addTask(d);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        LogUtil.e("加载成功:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        imageView.setImageResource(R.drawable.empty_photo);
                        LogUtil.e("加载失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache != null && getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache == null ? null : mMemoryCache.get(key);
    }

    /**
     * 添加请求句柄
     *
     * @param disposable 请求
     */
    private void addTask(Disposable disposable) {
        if (this.composite == null) {
            this.composite = new CompositeDisposable();
        }
        this.composite.add(disposable);
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (this.composite != null) {
            this.composite.clear();
        }
        this.composite = null;
    }

    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    public void flushCache() {
        if (mDiskCacheManager != null) {
            mDiskCacheManager.flushCache();
        }
    }
}
