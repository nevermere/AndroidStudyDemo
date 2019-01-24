package com.linyang.study.primary.animation;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.linyang.study.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * Created by fzJiang on 2018/12/20 11:27 星期四
 */
public class AdvancedAnimationActivity extends AppCompatActivity {

    @BindView(R.id.tab_menu)
    TabLayout tabMenu;
    @BindView(R.id.vp_view)
    ViewPager vpView;

    private List<PageModel> mPageModelList = new ArrayList<>();

    {
        mPageModelList.add(new PageModel(R.layout.fragment_argb_evaluator, R.string.title_argb_evaluator));

        mPageModelList.add(new PageModel(R.layout.fragment_rotation, R.string.title_hsv_evaluator));
        mPageModelList.add(new PageModel(R.layout.fragment_scale, R.string.title_of_object));
        mPageModelList.add(new PageModel(R.layout.fragment_alpha, R.string.title_property_values_holder));
        mPageModelList.add(new PageModel(R.layout.fragment_multi_properties, R.string.title_animator_set));
        mPageModelList.add(new PageModel(R.layout.fragment_duration, R.string.title_keyframe));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_animation);
        ButterKnife.bind(this);

        vpView.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = mPageModelList.get(position);
                return PageFragment.newInstance(pageModel.layoutRes);
            }

            @Override
            public int getCount() {
                return mPageModelList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(mPageModelList.get(position).titleRes);
            }
        });
        tabMenu.setupWithViewPager(vpView);
    }

    private class PageModel {

        @LayoutRes
        int layoutRes;
        @StringRes
        int titleRes;

        PageModel(@LayoutRes int layoutRes, @StringRes int titleRes) {
            this.layoutRes = layoutRes;
            this.titleRes = titleRes;
        }
    }
}
