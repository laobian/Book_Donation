package com.app.wx.donation_app.fragment.orderSubFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.ThanksDonationActivity;
import com.app.wx.donation_app.adapter.ItemOrderAdapter;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.dto.DonationDTO;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.OrderDetail;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.app.wx.donation_app.view.RecycleViewDivider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by zq on 2017/1/12.
 */
public class OrderWaitFragment extends Fragment {

    private final String TAG = "待寄出";
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private RecyclerView rvOrder;
    private ItemOrderAdapter itemOrderAdapter;
    private View view;
    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    private ItemOrderAdapter.IUpdateOrder iUpdateOrder = new ItemOrderAdapter.IUpdateOrder() {
        @Override
        public void update(final int position, final int orderStatue) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
            RequestParams params=new RequestParams();     //封装参数
            OrderDetail orderDetailUpdate = orderDetails.get(position);
            orderDetailUpdate.setOrderStatue(orderStatue);
            String orderJson = gson.toJson(orderDetailUpdate);
            params.put("orderDetailJson",orderJson);


            httpClient.post(Constant.ORDER_UPDATE, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("订单更新", "onSuccess: 连接服务器成功！");
                    if(statusCode==200){        //响应成功
                        String jsonString = new String(responseBody);
                        Log.i("订单更新",jsonString);
                        BasePojo<OrderDetail> basePojo = null; //把JSON字符串转为对象
                        basePojo = JsonUtil.getBaseFromJson(jsonString,
                                new TypeToken<BasePojo<OrderDetail>>(){}.getType());
                        if(basePojo.isSuccess()){
                            List<OrderDetail> orderUpdate = basePojo.getData();
                            Log.i("订单更新", "onSuccess: orderUpdate"+ orderUpdate);
                            Log.d("订单更新", "获取成功：\n"+basePojo.toString());
                            Toast.makeText(getContext(),basePojo.getMsg(),Toast.LENGTH_SHORT).show();
                            if (orderStatue == 1){
                                donate(position);
                            }
                            loadDate();
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
    };

    private void donate(int position) {
        int projectId = orderDetails.get(position).getProjectId();
        int userId = orderDetails.get(position).getUserId();
        int donateNumber = orderDetails.get(position).getDonationNum();
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("projectId",projectId);
        params.put("userId",userId);
        params.put("donateNumber",donateNumber);

        httpClient.post(Constant.URL_USER_DONATE, params, new AsyncHttpResponseHandler() {
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
                        DonationDTO donationDTO = basePojo.getData().get(0);
                        Log.i("线程名",Thread.currentThread().getName());
                        Intent intent = new Intent(getContext(), ThanksDonationActivity.class);
                        intent.putExtra("donationDto",donationDTO);
                        startActivity(intent);
                        /*跳转到捐赠订单页面*/
                        Log.i(TAG, "onSuccess: donationDto"+ donationDTO);
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
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

    public static Fragment newInstance() {
        OrderWaitFragment fragment = new OrderWaitFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        setParam();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            setDates();
            isLoadOver = true;
        }
        isLoadOver = false;
    }

    private void setDates() {
        if (SPUtil.isLogin(getContext())){
            loadDate();
        }else {
            Toast.makeText(getContext(),"尚未登录",Toast.LENGTH_SHORT).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tab, container, false);
            init(view);
            isInit = true;
            setParam();
        }
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(new ProductListGridAdapter());
        return view;
    }

    private void init(View view) {
        rvOrder = (RecyclerView) view.findViewById(R.id.recycler);
    }


    private void initRecyclerView() {
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 配置LinearLayoutManager
        rvOrder.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvOrder.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.background)));
        itemOrderAdapter = new ItemOrderAdapter(getActivity(), orderDetails,iUpdateOrder);
        rvOrder.setAdapter(itemOrderAdapter);
        itemOrderAdapter.setmOnItemClickListener(new ItemOrderAdapter.OnItemClickListener() {
            @Override
            public void myClick(int position) {
                /*点击进入订单详情*/
                //TODO
                Log.i("点击了", "list" + position);
            }

            @Override
            public void myOnLongClick(int position) {

            }
        });
    }

    /*查找待寄出订单*/
    private void loadDate() {
        int userId = SPUtil.getUser(getContext()).getUserId();
        int orderStatue = 0;
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("userId",userId);
        params.put("orderStatue",orderStatue);
        httpClient.post(Constant.ORDER_FIND_BY_STATUE, params, new AsyncHttpResponseHandler() {
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
                        initRecyclerView();
                        Log.i(TAG, "onSuccess: orderDetail"+ orderDetails);
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                    }else{
                        if (orderDetails.size()>0){
                            orderDetails.clear();
                            itemOrderAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getContext(),"暂无该类型订单",Toast.LENGTH_LONG).show();
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
