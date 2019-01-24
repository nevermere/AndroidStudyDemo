package com.linyang.study.primary.animation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.linyang.study.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 11:18 星期三
 */
public class PageFragment extends Fragment {

    private static final String LAYOUT_RES = "layout_res";

    @BindView(R.id.view_stub)
    ViewStub viewStub;

    @LayoutRes
    private int mLayoutRes;

    public static PageFragment newInstance(@LayoutRes int layoutRes) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, layoutRes);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mLayoutRes = args.getInt(LAYOUT_RES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);

        viewStub.setLayoutResource(mLayoutRes);
        viewStub.inflate();
        return view;
    }
}
