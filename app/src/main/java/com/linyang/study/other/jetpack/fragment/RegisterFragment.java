package com.linyang.study.other.jetpack.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linyang.study.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 描述:
 * Created by fzJiang on 2018/11/28 下午 1:52 星期三
 */
public class RegisterFragment extends Fragment {

    @BindView(R.id.bt_register)
    AppCompatButton btRegister;
    private View mView;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.bt_register)
    public void onViewClicked() {
        // 返回上一页
        Navigation.findNavController(mView).navigateUp();
    }
}
