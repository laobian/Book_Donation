package com.app.wx.donation_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.fragment.orderSubFragment.OrderCancleFragment;
import com.app.wx.donation_app.fragment.orderSubFragment.OrderSendFragment;
import com.app.wx.donation_app.fragment.orderSubFragment.OrderWaitFragment;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Author:wx
 * Date:2019/3/8
 */
public class OrderFragment extends BaseFragment {

    private TabLayout tabLayout = null;

    private ViewPager viewPager;


    private Fragment[] mFragmentArrays = new Fragment[3];

    private String[] mTabTitles = new String[3];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        tabLayout = (TabLayout) view.findViewById(R.id.order_tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.order_view_pager);

        mTabTitles[0] = "待寄出";
        mTabTitles[1] = "已寄出";
        mTabTitles[2] = "取消";
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        mFragmentArrays[0] = new OrderWaitFragment();
        mFragmentArrays[1] = new OrderSendFragment();
        mFragmentArrays[2] = new OrderCancleFragment();
        PagerAdapter pagerAdapter = new OrderFragment.MyViewPagerAdapter(this.getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);//设置ViewPage缓存界面数
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }


        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public long getItemId(int position) {
            int hashCode = mFragmentArrays[position].hashCode();
            return hashCode;
        }
    }
//    private final String TAG = "我的订单";
//    private RecyclerView rvOrder;
//    private ItemOrderAdapter itemOrderAdapter;
//    private TextView tvProjectName, tvOrderId, tvOrderStatue, tvDonationDetail;
//    private Button btnSend, btnCancel;
//    private List<OrderDetail> orderDetails = new ArrayList<>();
//
//    private ItemOrderAdapter.IUpdateOrder iUpdateOrder = new ItemOrderAdapter.IUpdateOrder() {
//        @Override
//        public void update(final int position, final int orderStatue) {
//            Gson gson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                    .create();
//            AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
//            RequestParams params=new RequestParams();     //封装参数
//            OrderDetail orderDetailUpdate = orderDetails.get(position);
//            orderDetailUpdate.setOrderStatue(orderStatue);
//            String orderJson = gson.toJson(orderDetailUpdate);
//            params.put("orderDetailJson",orderJson);
//
//
//            httpClient.post(Constant.ORDER_UPDATE, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Log.i("订单更新", "onSuccess: 连接服务器成功！");
//                    if(statusCode==200){        //响应成功
//                        String jsonString = new String(responseBody);
//                        Log.i("订单更新",jsonString);
//                        BasePojo<OrderDetail> basePojo = null; //把JSON字符串转为对象
//                        basePojo = JsonUtil.getBaseFromJson(jsonString,
//                                new TypeToken<BasePojo<OrderDetail>>(){}.getType());
//                        if(basePojo.isSuccess()){
//                            List<OrderDetail> orderUpdate = basePojo.getData();
//                            Log.i("订单更新", "onSuccess: orderUpdate"+ orderUpdate);
//                            Log.d("订单更新", "获取成功：\n"+basePojo.toString());
//                            Toast.makeText(getContext(),basePojo.getMsg(),Toast.LENGTH_SHORT).show();
//                            searchMyOrder();
//                        }else{
//                            Log.i(TAG, "获取失败: "+basePojo.getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    Log.i(TAG, "onFailure: statusCode："+statusCode);
//                    Log.i(TAG, "onFailure: responseBody："+responseBody);
//                    Log.i(TAG, "onFailure: error："+error);
//                    Log.i(TAG, "onFailure: 连接服务器失败！");
//                }
//            });
//        }
//    };
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        searchMyOrder();
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_order, container, false);
//        initView(view);
//        return view;
//    }
//
//    private void initView(View view) {
//        rvOrder = view.findViewById(R.id.rv_order);
//
//        btnCancel = view.findViewById(R.id.btn_order_cancel);
//        btnSend = view.findViewById(R.id.btn_send);
//    }
//
//
//
//    private void searchMyOrder() {
//        int userId;
//        if (!SPUtil.isLogin(getContext())) {
//            userId = 0;
//        } else {
//            userId = SPUtil.getUser(getContext()).getUserId();
//        }
//        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
//        RequestParams params = new RequestParams();     //封装参数
//        params.put("userId", userId);
//        httpClient.post(Constant.ORDER_MINE, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.i(TAG, "onSuccess: 连接服务器成功！");
//                if (statusCode == 200) {        //响应成功
//                    String jsonString = new String(responseBody);
//                    Log.i(TAG, jsonString);
//
//                    BasePojo<OrderDetail> basePojo = null; //把JSON字符串转为对象
//                    basePojo = JsonUtil.getBaseFromJson(jsonString,
//                            new TypeToken<BasePojo<OrderDetail>>() {
//                            }.getType());
//                    if (basePojo.isSuccess()) {
//                        orderDetails = basePojo.getData();
//                        initRecyclerView();
//                        Log.i(TAG, "onSuccess: orderDetails" + orderDetails);
//                        Log.d(TAG, "获取成功：\n" + basePojo.toString());
//                        Toast.makeText(getContext(), basePojo.getMsg(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Log.i(TAG, "获取失败: " + basePojo.getMsg());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.i(TAG, "onFailure: statusCode：" + statusCode);
//                Log.i(TAG, "onFailure: responseBody：" + responseBody);
//                Log.i(TAG, "onFailure: error：" + error);
//                Log.i(TAG, "onFailure: 连接服务器失败！");
//            }
//        });
////        if (SPUtil.isLogin(getContext())){
////            orderDetails = SPUtil.getOrderList(getContext());
////            Log.i(TAG,"orderDetails"+orderDetails);
////            if (orderDetails!=null && orderDetails.size()>0){
////                initRecyclerView();
////            }
////        }else{
////            rvOrder.removeAllViews();
////            itemOrderAdapter.notifyDataSetChanged();
////        }
//    }
//
//    private void initRecyclerView() {
//        // RecyclerView必须用LinearLayoutManager进行配置
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        // 设置列表的排列方向
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        // 配置LinearLayoutManager
//        rvOrder.setLayoutManager(layoutManager);
//        // 添加分割线（重新绘制分割线）
//        rvOrder.addItemDecoration(new RecycleViewDivider(getActivity(),
//                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.background)));
//        itemOrderAdapter = new ItemOrderAdapter(getActivity(), orderDetails,iUpdateOrder);
//        rvOrder.setAdapter(itemOrderAdapter);
//        itemOrderAdapter.setmOnItemClickListener(new ItemOrderAdapter.OnItemClickListener() {
//            @Override
//            public void myClick(int position) {
//                /*点击进入订单详情*/
//                //TODO
//                Log.i("点击了", "list" + position);
//            }
//
//            @Override
//            public void myOnLongClick(int position) {
//
//            }
//        });
//    }

}
