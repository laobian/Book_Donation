package com.app.wx.donation_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.dto.DonationDTO;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.fragment.HomeFragment;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LoveValueActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvLoveValue, tvToDonate, tvGetDonateNumber;
    private final String TAG = "我的爱心值";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_value);
        initView();
        load();
    }

    private void load() {
        final User user = SPUtil.getUser(this);
        int userId = user.getUserId();
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params = new RequestParams();     //封装参数
        params.put("userId", userId);

        httpClient.post(Constant.URL_USER_TOTAL_DONATE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if (statusCode == 200) {        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG, jsonString);

                    BasePojo<Integer> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<Integer>>() {
                            }.getType());
                    if (basePojo.isSuccess()) {
                        List<Integer> integers = basePojo.getData();
                        tvGetDonateNumber.setText("共捐赠" + integers.get(0) + "本书获得" + user.getLoveValue() + "积分");
                        Log.d(TAG, "获取成功：\n" + basePojo.toString());
                    } else {
                        Log.i(TAG, "获取失败: " + basePojo.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: statusCode：" + statusCode);
                Log.i(TAG, "onFailure: responseBody：" + responseBody);
                Log.i(TAG, "onFailure: error：" + error);
                Log.i(TAG, "onFailure: 连接服务器失败！");
            }
        });


        AsyncHttpClient httpClientGetUser = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams userParams = new RequestParams();     //封装参数
        userParams.put("userId",userId);
        /*获取更新后的爱心值*/
        httpClientGetUser.post(Constant.URL_Get, userParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if (statusCode == 200) {        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG, jsonString);

                    BasePojo<User> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<User>>() {
                            }.getType());
                    if (basePojo.isSuccess()) {
                        List<User> users = basePojo.getData();
                        /*更新本地用户信息*/
                        tvLoveValue.setText(users.get(0).getLoveValue() + "点");
                        SPUtil.saveUser(LoveValueActivity.this,users.get(0));
                        Log.d(TAG, "获取成功：\n" + basePojo.toString());
                    } else {
                        Log.i(TAG, "获取失败: " + basePojo.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: statusCode：" + statusCode);
                Log.i(TAG, "onFailure: responseBody：" + responseBody);
                Log.i(TAG, "onFailure: error：" + error);
                Log.i(TAG, "onFailure: 连接服务器失败！");
            }
        });
    }

    private void initView() {
        ivBack = findViewById(R.id.ib_love_value_back);
        tvLoveValue = findViewById(R.id.tv_love_value);
        tvToDonate = findViewById(R.id.tv_to_donate);
        tvGetDonateNumber = findViewById(R.id.tv_get_donate_number);

        tvToDonate.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_love_value_back:
                finish();
                break;
            case R.id.tv_to_donate:
                Intent intent = new Intent(LoveValueActivity.this, MainActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                finish();
                break;
        }

    }
}
