package com.linyang.study.primary.custom_view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.linyang.study.R;
import com.linyang.study.primary.custom_view.widget.MyNavView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:实现字母导航栏
 * Created by fzJiang on 2018-08-22
 */
public class NavViewActivity extends AppCompatActivity {

    @BindView(R.id.search_view)
    EditText mSearchView;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.tint_view)
    TextView mTintView;
    @BindView(R.id.nav_view)
    MyNavView mNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_view);
        ButterKnife.bind(this);

        mNavView.setTextView(mTintView);
    }
}
