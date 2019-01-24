package com.linyang.study.other.jetpack.activity;

import android.os.Bundle;

import com.linyang.study.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

/**
 * 描述:
 * Created by fzJiang on 2018/11/28 上午 11:46 星期三
 */
public class JetPackMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jetpack_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}
