package com.linyang.study.primary.custom_view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.linyang.study.R;
import com.linyang.study.primary.custom_view.widget.HorizontalScrollViewEx;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * Created by fzJiang on 2018/11/16 下午 4:17  星期五
 */
public class HorizontalScrollViewExActivity extends AppCompatActivity {

    @BindView(R.id.horizontal_scroll_view)
    HorizontalScrollViewEx horizontalScrollView;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.list_view_1)
    ListView listView1;
    @BindView(R.id.tv_title_2)
    TextView tvTitle2;
    @BindView(R.id.list_view_2)
    ListView listView2;
    @BindView(R.id.tv_title_3)
    TextView tvTitle3;
    @BindView(R.id.list_view_3)
    ListView listView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_scroll_view);
        ButterKnife.bind(this);

        initListView();
    }

    private void initListView() {
        List<String> list = new ArrayList<>();
        for (int j = 0; j < 20; j++) {
            list.add("---数据---:" + j);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.layout_list_item, R.id.tv_name, list);
        listView1.setAdapter(adapter1);
        tvTitle.setText("这是第 1 页");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.layout_list_item, R.id.tv_name, list);
        listView2.setAdapter(adapter2);
        tvTitle2.setText("这是第 2 页");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.layout_list_item, R.id.tv_name, list);
        listView3.setAdapter(adapter3);
        tvTitle3.setText("这是第 3 页");


//        LayoutInflater inflater = getLayoutInflater();
//
//        int width = ScreenUtils.getScreenWidth(this);
//
//
//        List<String> list = new ArrayList<>();
//        for (int j = 0; j < 20; j++) {
//            list.add("---数据---:" + j);
//        }
//        for (int i = 0; i < 3; i++) {
//            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.layout_content, horizontalScrollView, false);
//            viewGroup.getLayoutParams().width = width;
//            TextView title = viewGroup.findViewById(R.id.tv_title);
//            title.setText(MessageFormat.format("这是第 {0} 页", i + 1));
//
//            ListView listView = viewGroup.findViewById(R.id.list_view);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_list_item, R.id.tv_name, list);
//            listView.setAdapter(adapter);
//            listView.setBackgroundColor((Color.rgb(255 / (i + 1), 255 / (i + 1), 0)));
//
//            horizontalScrollView.addView(viewGroup);
//        }
    }
}
