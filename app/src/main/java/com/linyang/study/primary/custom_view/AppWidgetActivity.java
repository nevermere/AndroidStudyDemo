package com.linyang.study.primary.custom_view;

import android.os.Bundle;

import com.linyang.study.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 描述:
 * Created by fzJiang on 2018/11/21 下午 3:38  星期三
 */
public class AppWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget);
    }
}
