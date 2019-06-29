package com.app.wx.donation_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.OrderDetail;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.app.wx.donation_app.widget.CustomDialog;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ProjectDetail projectDetail;
    private final String TAG = "项目详情";
    private TextView btnToHome, tvProjectName,tvProjectDescription,tvNeedNumber,tvReceiveNumber,tvProjectStatue
            ,tvDonateNumber1,tvDonateNumber2,tvDonateNumber3,tvDefaultNumber,tvDonateConfirmProjectName,tvDetailBookName;
    private Button btnSingleDonate,continueDonate,cancelDonate,btnDonate;
    private CustomDialog dialogConfirm,dialogDonate;
    private ImageView ivBack,ivProject;
    private int donateNumber;
    private RelativeLayout rlStatue;
    private EditText etDonateNumber;
    private List<OrderDetail> orderDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        initView();
    }

    private void initView() {
        tvProjectName = findViewById(R.id.tv_detail_project_name);
        tvNeedNumber = findViewById(R.id.tv_detail_need_number);
        tvReceiveNumber = findViewById(R.id.tv_detail_received_number);
        tvProjectDescription = findViewById(R.id.tv_detail_project_description);
        tvProjectStatue = findViewById(R.id.tv_detail_project_statue);
        tvDetailBookName = findViewById(R.id.tv_detail_book_name);
        btnSingleDonate = findViewById(R.id.btn_detail_to_single_donate);
        btnToHome = findViewById(R.id.btn_detail_to_home);
        rlStatue = findViewById(R.id.rl_statue);
        ivBack = findViewById(R.id.ib_project_detail_back);

        /*获得是否继续捐赠对话框*/
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_donate_confirm, null);
        dialogConfirm = new CustomDialog(this, 0.83, 0.22, view, R.style.customDialogStyle);
        // 点击对话框以外的地方，对话框不消失。按返回键对话框会消失。
        dialogConfirm.setCanceledOnTouchOutside(false);
        // 点击对话框以外的地方，对话框不消失。按返回键对话框也不消失。
        dialogConfirm.setCancelable(false);

        continueDonate = view.findViewById(R.id.btn_continue_donate);
        cancelDonate = view.findViewById(R.id.btn_cancel_donate);
        TextView tvDonateProjectName = view.findViewById(R.id.tv_donate_project_name);

        /*捐赠对话框*/
        View viewDonate = LayoutInflater.from(this).inflate(R.layout.dialog_donate, null);
        dialogDonate = new CustomDialog(this, 0.85, 0.22,
                viewDonate, R.style.customDialogStyle);
        dialogDonate.setCanceledOnTouchOutside(true);
        dialogDonate.setCancelable(true);
        /*初始化控件*/
        LinearLayout llRandom = viewDonate.findViewById(R.id.ll_random_number);
        btnDonate = viewDonate.findViewById(R.id.btn_donate);
        tvDonateConfirmProjectName = viewDonate.findViewById(R.id.tv_donate_confirm_project_name);
        tvDefaultNumber = viewDonate.findViewById(R.id.tv_confirm_donate_number);
        tvDonateNumber1 = viewDonate.findViewById(R.id.tv_donate_number1);
        tvDonateNumber2 = viewDonate.findViewById(R.id.tv_donate_number2);
        tvDonateNumber3 = viewDonate.findViewById(R.id.tv_donate_number3);

        etDonateNumber = viewDonate.findViewById(R.id.et_donate_number);
        ivProject = viewDonate.findViewById(R.id.iv_confirm_project);
        projectDetail = (ProjectDetail) getIntent().getSerializableExtra("project_detail");
        tvDonateProjectName.setText(projectDetail.getProjectName()+"】");
        tvDonateProjectName.setText(projectDetail.getProjectName());
        if (projectDetail.getProjectStatus() == 0){
            tvProjectStatue.setText("筹备中");
        }else{
            tvProjectStatue.setText("已完成");
        }
        tvNeedNumber.setText(projectDetail.getNeedNumber()+"本");
        tvReceiveNumber.setText(projectDetail.getReceivedNumber()+"本");
        tvProjectName.setText(projectDetail.getProjectName());
        tvDetailBookName.setText("所需书籍:"+projectDetail.getBookName());
        tvProjectDescription.setText(projectDetail.getProjectDescription());
        tvDonateConfirmProjectName.setText(projectDetail.getProjectName());
        Glide.with(this).load(projectDetail.getImageUrl()).into(ivProject);
        btnSingleDonate.setOnClickListener(this);
        btnToHome.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        continueDonate.setOnClickListener(this);
        cancelDonate.setOnClickListener(this);
        btnDonate.setOnClickListener(this);
        tvDonateNumber1.setOnClickListener(this);
        tvDonateNumber2.setOnClickListener(this);
        tvDonateNumber3.setOnClickListener(this);
        etDonateNumber.setOnClickListener(this);
        llRandom.setOnClickListener(this);
        etDonateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String numString = etDonateNumber.getText().toString();
                if (numString.equals("")||numString == null){
                    tvDefaultNumber.setText(1 + "本");
                }else {
                    int number = Integer.parseInt(numString);
                    tvDefaultNumber.setText(number + "本");
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_detail_to_single_donate:
                if (!SPUtil.isLogin(ProjectDetailActivity.this)){
                    Toast.makeText(this,"请先登录",Toast.LENGTH_LONG).show();
                }else {
                    dialogConfirm.show();
                }
                break;
            case R.id.btn_continue_donate:
                dialogConfirm.dismiss();    /*确认继续捐赠*/
                dialogDonate.show();        /*捐赠框出现*/
                break;

            case R.id.btn_cancel_donate:    /*取消捐赠*/
                dialogConfirm.dismiss();    /*消失*/
                break;


            case R.id.btn_detail_to_home:
                Intent intentHome = new Intent(ProjectDetailActivity.this,MainActivity.class);
                intentHome.putExtra("id",0);
                startActivity(intentHome);
                finish();
                break;
            case R.id.ib_project_detail_back:
                finish();
                break;

            case R.id.btn_donate:   //执行捐赠业务
                dialogDonate.dismiss();
                donate();
                break;
                /*点击的第一个默认框*/
            case R.id.tv_donate_number1:
                donateNumber = Integer.parseInt(tvDonateNumber1.getText().toString());
                tvDefaultNumber.setText(donateNumber+"本");
                break;
                /*点击的第二个默认框*/
            case R.id.tv_donate_number2:
                donateNumber = Integer.parseInt(tvDonateNumber2.getText().toString());
                tvDefaultNumber.setText(donateNumber + "本");
                break;
                /*点击的第三个默认捐赠框*/
            case R.id.tv_donate_number3:
                donateNumber = Integer.parseInt(tvDonateNumber3.getText().toString());
                tvDefaultNumber.setText(donateNumber + "本");
                break;
            case R.id.ll_random_number:
                int random = getRandom();
                etDonateNumber.setText(random+"");
                donateNumber = Integer.parseInt(etDonateNumber.getText().toString());
                tvDefaultNumber.setText(donateNumber + "本");
                break;
        }
    }


    private int getRandom() {
        Random random = new Random();
        Integer number = random.nextInt(6)+1;
        return number;
    }

    private void donate() {
        int userId = SPUtil.getUser(this).getUserId();
        int projectId = projectDetail.getProjectId();
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("projectId",projectId);
        params.put("userId",userId);
        params.put("donationNum",donateNumber);

        httpClient.post(Constant.ORDER_CREATE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG,jsonString);

                    BasePojo<OrderDetail> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<OrderDetail>>(){}.getType());
                    if(basePojo.isSuccess()){
                        orderDetails = basePojo.getData();
                        Intent intent = new Intent(ProjectDetailActivity.this,MainActivity.class);
                        intent.putExtra("i",3);
                        startActivity(intent);
                        /*跳转到捐赠订单页面*/
                        finish();
                        Log.i(TAG, "onSuccess: orderDetails"+ orderDetails);
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                        Toast.makeText(ProjectDetailActivity.this,basePojo.getMsg(),Toast.LENGTH_SHORT).show();
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
