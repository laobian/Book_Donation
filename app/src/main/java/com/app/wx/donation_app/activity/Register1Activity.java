package com.app.wx.donation_app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.widget.LoginErrorDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getSupportedCountries;

public class Register1Activity extends FullTranStatActivity implements View.OnClickListener {

    private static final String TAG = "注册页面";
    private ImageButton ibBack;     //返回键
    private EditText etPhone;       //手机号
    private EditText etVeriCode;    //验证码
    private Button btnRegist;       //注册
    private Button btnGetVeriCode;  //获取验证码
    private TextView tvToLogin;     //跳转到登录
    private LoginErrorDialog errorDialog;  //错误提示框

    private String phone;           //手机号
    private String vreiCode;        //验证码
    private String country = "86";       //国家代码

    private EventHandler eh;

    private static final int VERICODE_COUNTDOWM = 60;       //获取验证码倒计时

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVreiCode(String vreiCode) {
        this.vreiCode = vreiCode;
    }

    private  Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //客户端验证成功，进行跳转
                    Log.i("00000000000000000000", "handleMessage: "+msg.obj.toString());
                    Log.i("RegistActivity", "验证成功：phone="+phone);
                    Intent intent = new Intent(Register1Activity.this,RegisterActivity.class);
                    intent.putExtra("cust_account",phone);
                    startActivity(intent);
                    break;
                case 1:
                    //获取验证码成功

                    Log.i("11111111111111111111", "handleMessage: "+msg.obj.toString());
                    break;
                case 2:
                    //返回支持发送验证码的国家列表
                    Log.i("22222222222222222222", "handleMessage: "+msg.obj.toString());
                    break;
                case 3:
                    //返回错误信息
                    Log.i("33333333333333333333", "handleMessage: "+msg.obj.toString());
                    //errorDialog.setMsg("验证码输入错误！");
                    String jsonString = new Gson().toJson(msg.obj);
                    JsonObject jsonObject = new Gson().fromJson(jsonString,JsonObject.class);
                    String messageString = jsonObject.get("detailMessage").getAsString();
                    JsonObject messgaeObject = new Gson().fromJson(messageString,JsonObject.class);


                    errorDialog.setMsg(messgaeObject.get("detailMessage").getAsString());
                    errorDialog.show();
                    break;
            }
            return false;
        }
    });


    //分割EditText输入的手机号码
    public boolean isBankCard=true;
    private String addString=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        setStatusBarFullTransparent(true);      //状态栏透明
        setFitSystemWindow(true);               //添加状态栏高度

        init();


        MobSDK.init(this);             // 启动短信验证sdk
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Message msg = new Message();
                        msg.arg1 = 0;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Message msg = new Message();
                        //获取验证码成功
                        msg.arg1 = 1;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        Message msg = new Message();
                        //返回支持发送验证码的国家列表
                        msg.arg1 = 2;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.arg1 = 3;
                    msg.obj = data;
                    handler.sendMessage(msg);
                    ((Throwable) data).printStackTrace();
                }
            }
        };

        SMSSDK.registerEventHandler(eh); //注册短信回调
        getSupportedCountries();         //获取支持的国家列表

    }

    private void init() {
        ibBack = findViewById(R.id.ib_regist_back);
        etPhone = findViewById(R.id.et_regist_account);
        etVeriCode = findViewById(R.id.et_regist_verification_code);
        btnGetVeriCode = findViewById(R.id.btn_regist_get_verification_code);
        btnRegist = findViewById(R.id.btn_regist_regist);
        tvToLogin = findViewById(R.id.tv_regist_login);
        errorDialog = new LoginErrorDialog(this,R.style.LoginErrorDialog);

        btnGetVeriCode.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        tvToLogin.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        etListener();

    }


    /**
     * 监听EditView的内容变化
     */
    private void etListener() {

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setBtnRegist();
                setBtnVeriCode();

                if (isBankCard) {
                    StringBuilder finalString= new StringBuilder();
                    int index=0;
                    String telString=s.toString().replace(" ", "");
                    if ((index+3)<telString.length()) {
                        finalString.append(telString.substring(index, index + 3)).append(addString);
                        index+=3;
                    }
                    while ((index+4)<telString.length()) {
                        finalString.append(telString.substring(index, index + 4)).append(addString);
                        index+=4;
                    }
                    finalString.append(telString.substring(index, telString.length()));

                    etPhone.removeTextChangedListener(this);  //先移除监听，不然会导致堆栈溢出
                    etPhone.setText(finalString.toString());
                    //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                    etPhone.setSelection(finalString.length());
                    etPhone.addTextChangedListener(this);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        etVeriCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnRegist();



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置注册按钮按钮是否禁用
     */
    private void setBtnRegist() {
        if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etVeriCode.getText()) && !btnRegist.isEnabled()) {     //账号与密码同时不为空激活按钮
            btnRegist.setEnabled(true);

        }else if(TextUtils.isEmpty(etPhone.getText()) || TextUtils.isEmpty(etVeriCode.getText())){
            btnRegist.setEnabled(false);
        }
    }

    /**
     * 设置验证码按钮按钮是否禁用
     */
    private void setBtnVeriCode() {
        if (!TextUtils.isEmpty(etPhone.getText()) && !btnGetVeriCode.isEnabled()) {
            btnGetVeriCode.setEnabled(true);

        }else if(TextUtils.isEmpty(etPhone.getText()) ){
            btnGetVeriCode.setEnabled(false);
        }
    }

    /**
     * 点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist_regist:      //按下注册按钮
                regist();
                break;
            case R.id.btn_regist_get_verification_code:         //点击获取验证码
                changeBtnGetCode();
                setPhone(etPhone.getText().toString().replace(" ", ""));
                SMSSDK.getVerificationCode("86", phone);
                break;
            case R.id.tv_regist_login:
                toLogin();
                break;
            case R.id.ib_regist_back:
                finish();                   //结束当前activity
                break;
        }
    }

    /**
     * 跳转到登录页面
     */
    private void toLogin() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 注册
     */
    private void regist() {
        setVreiCode(etVeriCode.getText().toString());
        // 提交验证码，其中的code表示验证码，如“1357”
        SMSSDK.submitVerificationCode(country, phone, vreiCode);

    }


    //验证手机码格式
    private boolean isMobilePhone(String phone) {
         /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(phone) && phone.matches(telRegex);
    }

    //改变按钮样式
    private void changeBtnGetCode() {
        Thread thread=new Thread(){
            public void run(){
                int veriCodeCountdown = VERICODE_COUNTDOWM;
                while(veriCodeCountdown>0) {
                    veriCodeCountdown--;

                    //如果活动为空
                    final int finalVeriCodeCountdown = veriCodeCountdown;
                    Register1Activity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            btnGetVeriCode.setText(finalVeriCodeCountdown +"s后可重发");
                            btnGetVeriCode.setEnabled(false);       //禁用验证码按钮
                            etPhone.setEnabled(false);              //禁用EditText输入手机号
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Register1Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnGetVeriCode.setText("获取验证码");
                        btnGetVeriCode.setEnabled(true);
                        etPhone.setEnabled(true);              //解除禁用EditText输入手机号
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);     // 使用完EventHandler需注销，否则可能出现内存泄漏
    }
}
