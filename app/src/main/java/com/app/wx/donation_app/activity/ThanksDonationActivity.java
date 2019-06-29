package com.app.wx.donation_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.dto.DonationDTO;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.util.SPUtil;
import com.sunfusheng.glideimageview.GlideImageView;

public class ThanksDonationActivity extends AppCompatActivity {

    private GlideImageView givThanksHead; //头像
    private TextView tvThanksUserName,tvThanksProjectName,tvThanksDonateNumber,tvGetLoveValue;
    private DonationDTO donationDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_donation);
        initView();
    }

    private void initView() {
        givThanksHead = findViewById(R.id.giv_thanks_user_head);
        tvThanksUserName = findViewById(R.id.tv_thanks_userName);
        tvThanksProjectName = findViewById(R.id.tv_thanks_project_name);
        tvThanksDonateNumber = findViewById(R.id.tv_thanks_donate_number);
        tvGetLoveValue = findViewById(R.id.tv_get_love_value);

        User user = SPUtil.getUser(this);
        donationDTO =(DonationDTO) getIntent().getSerializableExtra("donationDto");
        String head = user.getHeadImg();
        givThanksHead.loadCircleImage(head,R.drawable.ic_mine_head);
        tvThanksUserName.setText(user.getUserName());
        tvThanksProjectName.setText(donationDTO.getProjectDetail().getProjectName());
        tvThanksDonateNumber.setText(donationDTO.getDonationNum()+"");
        tvGetLoveValue.setText(donationDTO.getDonationNum()*2+"");
    }
}
