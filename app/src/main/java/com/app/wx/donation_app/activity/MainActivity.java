package com.app.wx.donation_app.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.fragment.DonateFragment;
import com.app.wx.donation_app.fragment.HomeFragment;
import com.app.wx.donation_app.fragment.MineFragment;
import com.app.wx.donation_app.fragment.OrderFragment;
import com.app.wx.donation_app.fragment.SortFragment;
import com.app.wx.donation_app.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbHome, rbSort, rbDonate ,rbOrder,rbMine;  // 底部导航栏按钮

    //Fragment Object
    private List<Fragment> fgList;  // 装载Fragment的集合
    private int currPosition = 0;
    private FragmentManager fm;
    private String[] tags = new String[]{"HomeFragment",  "SortFragment","DonateFragment", "OrderFragment","MineFragment"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initFragment();
    }

    private void initFragment() {
        fgList = new ArrayList<>();
        fgList.add(new HomeFragment());
        fgList.add(new SortFragment());
        fgList.add(new DonateFragment());
        fgList.add(new OrderFragment());
        fgList.add(new MineFragment());

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction(); // 实例化Fragment事物管理器

        for (int i = 0; i < fgList.size(); i++) {     // 将3个Fragment装在容器中，交给ft管理
            ft.add(R.id.fl_container, fgList.get(i), tags[i]);
        }
        ft.commit();    // 提交保存
        int i = getIntent().getIntExtra("i",0);
        showFragment(i);    // 默认显示首页Fragment
    }

    private void init() {
        rbHome = findViewById(R.id.rb_home);
        rbSort = findViewById(R.id.rb_sort);
        rbDonate = findViewById(R.id.rb_donate);
        rbOrder = findViewById(R.id.rb_order);
        rbMine = findViewById(R.id.rb_mine);
        rbHome.setOnClickListener(this);
        rbSort.setOnClickListener(this);
        rbDonate.setOnClickListener(this);
        rbOrder.setOnClickListener(this);
        rbMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:    // 选择首页
                showFragment(0);
                clickAgain(0);
                break;
            case R.id.rb_sort:    // 选择分类
                showFragment(1);
                clickAgain(1);
                break;
            case R.id.rb_donate:    // 选择求书
                showFragment(2);
                clickAgain(2);
                break;
            case R.id.rb_order:     // 选择捐书订单
                showFragment(3);
                clickAgain(3);
                break;
            case R.id.rb_mine:     // 选择我的
                showFragment(4);
                clickAgain(4);
                break;
        }
    }

    private void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fg : fgList) {
            ft.hide(fg);        // 隐藏所有Fragment
        }
        ft.commit();
    }

    private void showFragment(int i) {
        hideFragments();    // 先隐藏所有Fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fgList.get(i));     // 显示Fragment
        ft.commit();
    }

    private void clickAgain(int i) {
        if(currPosition == i){      // 当前显示为某页面，并再次点击了此页面的导航按钮
            EventBus.getDefault().post(new MessageEvent(tags[i]));  // 通知Fragment刷新
        }
        currPosition = i;
    }

}
