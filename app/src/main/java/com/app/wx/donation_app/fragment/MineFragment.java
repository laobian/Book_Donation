package com.app.wx.donation_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.LoginActivity;
import com.app.wx.donation_app.activity.LoveValueActivity;
import com.app.wx.donation_app.activity.MyAchievementActivity;
import com.app.wx.donation_app.activity.MyDonationActivity;
import com.app.wx.donation_app.activity.PersonInfoActivity;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.util.MessageEvent;
import com.app.wx.donation_app.util.SPUtil;
import com.sunfusheng.glideimageview.GlideImageView;

/**
 * @Author:wx
 * @Date:2019/3/8
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUSET_LOGIN = 1;           // 跳到登录页面请求码
    private static final int REQUSET_PERSON_INFO = 2;     // 个人信息请求码
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final String TAG = "我的页面";

    private GlideImageView givHead;         //头像
    private TextView tvToLogin;             //立即登录
    private TextView tvUserName;            //用户名
    private LinearLayout llPersonInfo;      //个人信息
    /*我捐赠的*/
    private LinearLayout llMyDonation;
    /*我的爱心值*/
    private LinearLayout llLoveValue;

    /*我的荣誉称号*/
    private LinearLayout llAchievement;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        // 获取新建视图View中布局文件的空间
        initView(view);
        return view;
    }

    private void initView(View view) {

        givHead = view.findViewById(R.id.giv_mine_head);        //头像
        givHead.setOnClickListener(this);                       //头像点击监听

        tvUserName = view.findViewById(R.id.tv_mine_name);    //用户名
        tvToLogin = view.findViewById(R.id.mine_tv_to_login);      //立即登录
        llPersonInfo = view.findViewById(R.id.mine_ll_person_info); //个人信息
        llMyDonation = view.findViewById(R.id.ll_my_donation);      //我的捐赠
        llLoveValue = view.findViewById(R.id.ll_love_value);
        llAchievement = view.findViewById(R.id.ll_my_achievement);

        tvToLogin.setOnClickListener(this);                        //点击立即登录
        llPersonInfo.setOnClickListener(this);         //点击个人信息
        llMyDonation.setOnClickListener(this);
        llLoveValue.setOnClickListener(this);
        llAchievement.setOnClickListener(this);
        showHead();                                             //显示头像
    }

    /*显示顶部信息*/
    private void showHead() {

        if (!SPUtil.isLogin(getContext())) {      //判断是否登录，未登录则不做操作
            //本地头像
            givHead.loadLocalCircleImage(R.drawable.ic_mine_head, R.drawable.ic_mine_head);
            tvUserName.setText("未登录");
            tvToLogin.setVisibility(View.VISIBLE);  //显示立即登录
            llPersonInfo.setVisibility(View.GONE);  //隐藏个人信息跳转
            return;
        }
        User user = SPUtil.getUser(getContext());
        String headImgUrl = user.getHeadImg();    //获取头像地址
        tvUserName.setText(user.getUserName());
        tvToLogin.setVisibility(View.GONE);             //隐藏立即登录
        llPersonInfo.setVisibility(View.VISIBLE);       //显示个人信息跳转
        Log.i("头像地址",headImgUrl);
        givHead.loadCircleImage(headImgUrl, R.drawable.ic_mine_head);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mine_ll_person_info: //点击个人信息跳转
            case R.id.mine_tv_to_login:    //点击登录跳转
            case R.id.giv_mine_head:       //点击头像
                Log.i("MineFragment", "onClick: 点击头像" + SPUtil.isLogin(getContext()));

                if (!SPUtil.isLogin(getContext())) {          //若未登录则跳转到登录页面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("isShow", true);
                    startActivityForResult(intent, REQUSET_LOGIN);
                } else {
                    Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                    startActivityForResult(intent, REQUSET_PERSON_INFO);
                }
                break;
            case R.id.ll_my_donation:
                if (!SPUtil.isLogin(getContext())) {          //若未登录则跳转到登录页面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("isShow", true);
                    startActivityForResult(intent, REQUSET_LOGIN);
                }else{
                    Intent intent = new Intent(getContext(), MyDonationActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_love_value:
                if (!SPUtil.isLogin(getContext())) {          //若未登录则跳转到登录页面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("isShow", true);
                    startActivityForResult(intent, REQUSET_LOGIN);
                }else {
                    Intent intent = new Intent (getContext(), LoveValueActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_my_achievement:
                if (!SPUtil.isLogin(getContext())) {          //若未登录则跳转到登录页面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("isShow", true);
                    startActivityForResult(intent, REQUSET_LOGIN);
                }else {
                    Intent intent = new Intent(getContext(), MyAchievementActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 用于处理上一个页面的返回结果
     * @param requestCode 请求码
     * @param resultCode  返回结果码
     * @param data         上一个页面返回的回数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("requestCode",requestCode+"");
        Log.i("resultCode",resultCode+"");
        Log.i("Intent",data+"");

        if (resultCode == RESULT_SUCCESS) {      //返回成功结果码

            switch (requestCode) {
                case REQUSET_LOGIN:
                case REQUSET_PERSON_INFO:
                    showHead();         //更新头像及其他个人信息
                    break;
            }
        }

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getMsg()) {
            case "MineFragment":
                Log.i(TAG, "onMessageEvent: 更新我的页面");
                showHead();         //更新头像及其他个人信息
                break;
        }
    }
}
