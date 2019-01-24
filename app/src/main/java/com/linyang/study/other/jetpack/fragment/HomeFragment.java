package com.linyang.study.other.jetpack.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linyang.study.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;

/**
 * 描述:
 * Created by fzJiang on 2018/11/28 下午 2:00 星期三
 */
public class HomeFragment extends Fragment {

    private View mView;

    ActionBarDrawerToggle mActionBarDrawerToggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        return mView;
    }
}
