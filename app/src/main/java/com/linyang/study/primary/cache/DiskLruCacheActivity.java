package com.linyang.study.primary.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.jakewharton.disklrucache.DiskLruCache;
import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描述:
 * Created by fzJiang on 2018/12/28 11:56 星期五
 */
public class DiskLruCacheActivity extends BaseActivity {

    private static final String IMAGE_URL = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";

    @BindView(R.id.bt_download_cache)
    AppCompatButton btDownloadCache;
    @BindView(R.id.bt_read_cache)
    AppCompatButton btReadCache;
    @BindView(R.id.bt_clear_cache)
    AppCompatButton btClearCache;
    @BindView(R.id.iv_image_cache)
    AppCompatImageView ivImageCache;
    @BindView(R.id.tv_cache_size)
    AppCompatTextView tvCacheSize;

    private DiskCacheManager mDiskCacheManager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_disklruchche;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mDiskCacheManager = new DiskCacheManager(getApplicationContext(), "fuck_ss");
    }

    @OnClick({R.id.bt_download_cache, R.id.bt_read_cache, R.id.bt_clear_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_download_cache:
                downLoadImage(IMAGE_URL);
                break;

            case R.id.bt_read_cache:
                readCache(IMAGE_URL);
                break;

            case R.id.bt_clear_cache:
                clearCache(IMAGE_URL);
                break;
        }
    }

    /**
     * 下载并写入缓存
     *
     * @param url
     */
    private void downLoadImage(String url) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            // 以MD5加密，作为存储key值
            String md5Key = MD5Util.getMD5String(url);
            L.i("md5Key:" + md5Key);

            DiskLruCache.Editor editor = mDiskCacheManager.getEditor(md5Key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (outputStream != null) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();

                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        outputStream.write(responseBody.bytes());
                        editor.commit();
                        outputStream.close();
                        emitter.onNext(true);
                    } else {
                        editor.abort();
                        emitter.onError(new Throwable("no Response"));
                    }
                } else {
                    editor.abort();
                    emitter.onError(new Throwable("no OutputStream"));
                }
            } else {
                emitter.onError(new Throwable("no DiskLruCache Editor"));
            }
            mDiskCacheManager.flushCache();
            emitter.onComplete();

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        L.i("-----下载并写入缓存成功--------" + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("-----下载并写入缓存失败--------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 读取缓存
     *
     * @param key
     */
    private void readCache(String key) {
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            // 以MD5加密，作为存储key值
            String md5Key = MD5Util.getMD5String(key);
//            L.i("md5Key:" + md5Key);

            DiskLruCache.Snapshot snapshot = mDiskCacheManager.getSnapshot(md5Key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    emitter.onNext(BitmapFactory.decodeStream(inputStream));
                    inputStream.close();
                } else {
                    emitter.onError(new Throwable("no InputStream"));
                }
            } else {
                emitter.onError(new Throwable("no Snapshot"));
            }
            emitter.onComplete();

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        L.i("-----读取缓存--------" + (bitmap == null ? "失败" : "成功"));
                        if (bitmap != null) {
                            ivImageCache.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("-----读取缓存失败--------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 清除缓存
     *
     * @param key
     */
    private void clearCache(String key) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            DiskLruCache lruCache = mDiskCacheManager.getDiskLruCache();
            if (lruCache != null) {
                // 以MD5加密，作为存储key值
                String md5Key = MD5Util.getMD5String(key);
//                L.i("md5Key:" + md5Key);
                emitter.onNext(lruCache.remove(md5Key));
            } else {
                emitter.onError(new Throwable("no DiskLruCache"));
            }
            emitter.onComplete();

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        L.i("-----清除缓存--------" + (aBoolean ? "失败" : "成功"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("-----清除缓存失败--------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
