package com.app.wx.donation_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class MyAchievementActivity extends AppCompatActivity {

    private TextView tvMyAchievement;
    private static final String TAG = "我的荣誉页面";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_achievement);
        findAchievement();
        initView();
    }

    private void initView() {
        tvMyAchievement = findViewById(R.id.tv_my_achievement);
    }

    private void findAchievement() {
        int userId = SPUtil.getUser(MyAchievementActivity.this).getUserId();
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("userId",userId);

        httpClient.post(Constant.URL_USER_ACHIEVEMENT, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG,jsonString);

                    BasePojo<String> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<String>>(){}.getType());
                    if(basePojo.isSuccess()){
                        String achievement = basePojo.getData().get(0);
                        Log.i(TAG, "onSuccess:Achievement"+achievement);
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                        tvMyAchievement.setText("您已获得"+achievement);
                    }else{
                        Log.i(TAG, "获取失败: "+basePojo.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: statusCode："+statusCode);
                Log.i(TAG, "onFailure: responseBody："+responseBody);
                Log.i(TAG, "onFailure: error："+error);
                Log.i(TAG, "onFailure: 连接服务器失败！");
            }
        });
    }
}
