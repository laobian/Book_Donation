package com.app.wx.donation_app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.app.wx.donation_app.R;


/**
 * Created by 112 on 2018/10/31.
 */

public class ChoseSexDialog extends Dialog implements View.OnClickListener {

    private LinearLayout llMan;           //男
    private LinearLayout llWoman;         //女
    
    private onCsdClickListener mOnCsdClickListener;
    

    public ChoseSexDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.chose_sex_dialog);

        init();
    }

    private void init() {

        llMan = findViewById(R.id.csd_ll_man);
        llWoman = findViewById(R.id.csd_ll_woman);
        llMan.setOnClickListener(this);
        llWoman.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.csd_ll_man:       //选择为男
                int man = 0;
                mOnCsdClickListener.onManSureClick(man);
                ChoseSexDialog.this.dismiss();
                break;
            case R.id.csd_ll_woman:     //选择为女
                int woman = 1;
                mOnCsdClickListener.onWomanCancelClick(woman);
                ChoseSexDialog.this.dismiss();
                break;
        }
    }

    public interface onCsdClickListener{
        void onManSureClick(int man);
        void onWomanCancelClick(int woman);
    }

    public void setmOnCsdClickListener(onCsdClickListener mOnCsdClickListener) {
        this.mOnCsdClickListener = mOnCsdClickListener;
    }
}
