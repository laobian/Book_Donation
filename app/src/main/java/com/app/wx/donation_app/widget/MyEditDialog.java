package com.app.wx.donation_app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.wx.donation_app.R;


/**
 * Created by 112 on 2018/10/31.
 */

/**
 * 自定义编辑对话框
 */
public class MyEditDialog extends Dialog {

    public TextView tvTitle;           //标题框
    private String title;               //标题
    private EditText editContent;       //输入内容
    private Button btnSure;             //确定按钮
    private Button btnCancel;           //取消按钮
    private onMedClickListener mOnMedClickListener;

    public MyEditDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_edit_dialog);

        init();
    }

    private void init() {

        tvTitle = findViewById(R.id.med_tv_title);
        editContent = findViewById(R.id.med_et_content);
        btnSure = findViewById(R.id.med_btn_sure);
        btnCancel = findViewById(R.id.med_btn_cancel);

        tvTitle.setText(title);
        
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();

                mOnMedClickListener.onBtnSureClick(content);
                MyEditDialog.this.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMedClickListener.onBtnCancelClick();
                MyEditDialog.this.dismiss();
            }
        });

    }
    public void setTitle(String title){         //设置标题
        this.title=title;
    }

    /*设置监听*/
    public interface onMedClickListener{
        void onBtnSureClick(String content);
        void onBtnCancelClick();
    }

    public void setmOnMedClickListener(onMedClickListener mOnMedClickListener) {
        this.mOnMedClickListener = mOnMedClickListener;
    }
}
