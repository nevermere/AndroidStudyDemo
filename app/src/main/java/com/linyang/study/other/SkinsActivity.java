package com.linyang.study.other;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.linyang.study.R;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feng.skin.manager.base.BaseActivity;
import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.loader.SkinManager;
import cn.feng.skin.manager.util.L;

/**
 * 描述:
 * Created by fzJiang on 2018/11/22 下午 2:29  星期四
 */
public class SkinsActivity extends BaseActivity {

    @BindView(R.id.bt_load_skins)
    AppCompatButton btLoadSkins;
    @BindView(R.id.bt_reset_skins)
    AppCompatButton btResetSkins;

    private static final String SKIN_NAME = "BlackFantacy.skin";
    private static final String SKIN_DIR = Environment.getExternalStorageDirectory() + File.separator + SKIN_NAME;

    private boolean isOfficalSelected = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        ButterKnife.bind(this);

        isOfficalSelected = !SkinManager.getInstance().isExternalSkin();
    }

    @OnClick({R.id.bt_load_skins, R.id.bt_reset_skins})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_load_skins:
                if (!isOfficalSelected) {
                    return;
                }

                File skin = new File(SKIN_DIR);
                if (!skin.exists()) {
                    Toast.makeText(getApplicationContext(), "请检查" + SKIN_DIR + "是否存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                SkinManager.getInstance().load(skin.getAbsolutePath(), new ILoaderListener() {

                    @Override
                    public void onStart() {
                        L.i("---------onStart-------");
                    }

                    @Override
                    public void onSuccess() {
                        L.i("---------onSuccess-------");
                        isOfficalSelected = false;
                    }

                    @Override
                    public void onFailed() {
                        L.e("---------onFailed-------");
                    }
                });
                break;
            case R.id.bt_reset_skins:
                if (!isOfficalSelected) {
                    SkinManager.getInstance().restoreDefaultTheme();
                    isOfficalSelected = true;
                }
                break;
        }
    }
}
