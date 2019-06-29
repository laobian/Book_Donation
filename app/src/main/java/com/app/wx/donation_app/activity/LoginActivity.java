package com.app.wx.donation_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOGIN = 2;
    private Button btnLogin, setting;
    private EditText etUser,etPassword;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inti();
    }

    //初始化页面布局
    private void inti() {
        btnLogin = findViewById(R.id.btn_login);
        etUser = findViewById(R.id.et_user);
        etPassword = findViewById(R.id.et_password);
        tvRegister = findViewById(R.id.tv_register);
        btnLogin.setOnClickListener(this);

        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                register();

        }
    }

    /*登录*/
    private void login() {
        /*获取输入框的用户名和密码*/
        String userName = etUser.getText().toString();
        String password = etPassword.getText().toString();

        /*当用户名和密码都不为空时*/
        if(userName!=null&&!userName.equals("")&&password!=null&&!password.equals("")){
            //post请求发送的参数：
            RequestParams params = new RequestParams();
            params.put("phone",userName);
            params.put("password",password);

            /*发送post请求*/
            new AsyncHttpClient().post(Constant.URL_Login, params, new AsyncHttpResponseHandler() {
                /*如果请求成功*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String json = new String(responseBody);
                        Log.i("login",json);
                        BasePojo<User> basePojo = JsonUtil.getBaseFromJson(
                                LoginActivity.this, json, new TypeToken<BasePojo<User>>(){}.getType());
                        if(basePojo!=null){
                            if(basePojo.isSuccess()){
//                            UserUtil userUtil = new UserUtil(Login.this);
//                            User user = basePojo.getData().get(0);
//                            userUtil.saveUser(user);
//                            Intent intent = new Intent(Login.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();

                                /*得到用户信息*/
                                User user = basePojo.getData().get(0);
                                System.out.print(user);
                                /*保存*/
                                SPUtil.saveUser(LoginActivity.this,true,user);
                                /*设置结果码*/
                                setResult(RESULT_LOGIN);
                                /*跳转到主页*/
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                /*销毁活动*/
                                finish();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        }
        else{
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    /*注册*/
    private void register(){
        Intent intent = new Intent(LoginActivity.this,Register1Activity.class);
        startActivity(intent);
        finish();

    }
}
