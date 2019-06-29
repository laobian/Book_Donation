package com.app.wx.donation_app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wx.donation_app.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 112 on 2018/10/11.
 */

public class LoginErrorDialog extends Dialog {

    private ImageView ivMsg;
    private TextView tvMsg;
    private String msg;

    public LoginErrorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_error_dialog);
        setCanceledOnTouchOutside(false);
        initDialog();
        initData();
        autoDismiss();
    }

    private void initData() {
        if(msg != null){
            tvMsg.setText(msg);
        }
    }

    private void initDialog() {
        ivMsg = findViewById(R.id.iv_login_error_img);
        tvMsg = findViewById(R.id.tv_login_error_msg);


    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    private void autoDismiss() {

        Timer nTimer = new Timer();
        nTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismiss();
            }
        },2000);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
