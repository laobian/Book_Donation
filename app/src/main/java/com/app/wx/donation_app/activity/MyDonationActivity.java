package com.app.wx.donation_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.adapter.ItemMyDonationAdapter;
import com.app.wx.donation_app.adapter.ItemProjectAdapter;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.dto.DonationDTO;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.app.wx.donation_app.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MyDonationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivback;
    private RecyclerView rvMyDonation;
    private ItemMyDonationAdapter donationAdapter;
    private List<DonationDTO> donationDTOList;
    private static final String TAG = "我捐赠的页面";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation);
        intiView();
        loadData();
    }

    private void intiView() {
        rvMyDonation = findViewById(R.id.rc_my_donation);
        ivback = findViewById(R.id.ib_my_donation_back);
        ivback.setOnClickListener(this);
    }

    private void loadData() {
        User user = SPUtil.getUser(MyDonationActivity.this);
        int userId = user.getUserId();
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("userId",userId);

        httpClient.post(Constant.URL_USER_MY_DONATION, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG,jsonString);

                    BasePojo<DonationDTO> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<DonationDTO>>(){}.getType());
                    if(basePojo.isSuccess()){
                        donationDTOList = basePojo.getData();
                        Log.i(TAG, "onSuccess: donationDTOList"+donationDTOList);
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                        Toast.makeText(MyDonationActivity.this,basePojo.getMsg(),Toast.LENGTH_SHORT).show();
                        initRvDonation();
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

    private void initRvDonation() {
        Log.i(TAG,"初始化Rv");
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyDonationActivity.this);
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 配置LinearLayoutManager
        rvMyDonation.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvMyDonation.addItemDecoration(new RecycleViewDivider(MyDonationActivity.this,
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.background)));
        donationAdapter = new ItemMyDonationAdapter(MyDonationActivity.this,donationDTOList);
        rvMyDonation.setAdapter(donationAdapter);
        donationAdapter.setmOnItemClickListener(new ItemMyDonationAdapter.OnItemClickListener() {
            @Override
            public void myClick(int position) {
                /*点击显示项目详情*/
                Log.i("点击了","list"+position);
                Intent intent = new Intent(MyDonationActivity.this,ProjectDetailActivity.class);
                intent.putExtra("project_detail",donationDTOList.get(position).getProjectDetail());
                startActivity(intent);
            }
            @Override
            public void myOnLongClick(int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();                   //结束当前页面
    }
}
