package com.linyang.study.other.moshi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.linyang.study.R;
import com.linyang.study.app.util.GsonUtil;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018/11/27 上午 9:35 星期二
 */
public class MoshiActivity extends AppCompatActivity {

    private static final String TAG = MoshiActivity.class.getSimpleName();

    @BindView(R.id.bt_moshi)
    AppCompatButton btMoshi;
    @BindView(R.id.bt_gson)
    AppCompatButton btGson;
    @BindView(R.id.bt_jackson)
    AppCompatButton btJackson;
    @BindView(R.id.tv_time_1)
    TextView tvTime1;
    @BindView(R.id.tv_time_2)
    TextView tvTime2;
    @BindView(R.id.tv_time_3)
    TextView tvTime3;

    private String mJsonData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moshi);
        ButterKnife.bind(this);
        mJsonData = readAssetsFile(this, "jsondata.txt");
    }

    @OnClick({R.id.bt_moshi, R.id.bt_gson, R.id.bt_jackson})
    public void onViewClicked(View view) {
        MovieBean movieBean = null;
        switch (view.getId()) {
            case R.id.bt_moshi:
                try {
                    long time1 = System.currentTimeMillis();
                    movieBean = new Moshi.Builder().build().adapter(MovieBean.class).fromJson(mJsonData);
                    if (movieBean != null) {
                        time1 = System.currentTimeMillis() - time1;
                        tvTime1.setText(MessageFormat.format("使用Moshi解析时间: {0} ms", time1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bt_gson:
                long time2 = System.currentTimeMillis();
                movieBean = GsonUtil.GsonToBean(mJsonData, MovieBean.class);
                if (movieBean != null) {
                    time2 = System.currentTimeMillis() - time2;
                    tvTime2.setText(MessageFormat.format("使用Gson解析时间: {0} ms", time2));
                }
                break;

            case R.id.bt_jackson:
                break;
        }

        if (movieBean != null) {
            Log.i(TAG, "解析:" + movieBean.getCount());
        }
    }

    /**
     * 读取asset目录下音频文件。
     *
     * @return 二进制文件数据
     */
    private String readAssetsFile(Context context, String filename) {
        InputStream ins = null;
        try {
            ins = context.getAssets().open(filename);
            byte[] data = new byte[ins.available()];
            int read = ins.read(data);
            return new String(data);
        } catch (IOException e) {
            return null;
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
