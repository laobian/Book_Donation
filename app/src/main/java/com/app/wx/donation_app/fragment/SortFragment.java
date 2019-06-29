package com.app.wx.donation_app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.fragment.sortFragment.TabFragment1;
import com.app.wx.donation_app.fragment.sortFragment.TabFragment2;
import com.app.wx.donation_app.fragment.sortFragment.TabFragment3;
import com.app.wx.donation_app.fragment.sortFragment.TabFragment4;
import com.app.wx.donation_app.fragment.sortFragment.TabFragment5;

/**
 * Author:wx
 * Date:2019/3/8
 */
public class SortFragment extends BaseFragment {

    private TabLayout tabLayout = null;

    private ViewPager viewPager;


    private Fragment[] mFragmentArrays = new Fragment[5];

    private String[] mTabTitles = new String[5];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mTabTitles[0] = "童话";
        mTabTitles[1] = "小说";
        mTabTitles[2] = "漫画";
        mTabTitles[3] = "名著";
        mTabTitles[4] = "科普";
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        mFragmentArrays[0] = new TabFragment1();
        mFragmentArrays[1] = new TabFragment2();
        mFragmentArrays[2] = new TabFragment3();
        mFragmentArrays[3] = new TabFragment4();
        mFragmentArrays[4] = new TabFragment5();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(this.getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);//设置ViewPage缓存界面数
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }


        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }
}
