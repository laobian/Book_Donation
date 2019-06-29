package com.app.wx.donation_app.fragment.sortFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.ProjectDetailActivity;
import com.app.wx.donation_app.adapter.ProductListGridAdapter;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.view.RecycleViewDivider;
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
public class TabFragment5 extends Fragment {

    private final String TAG = "科普";
    private List<ProjectDetail> projectDetailList = new ArrayList<>();
    RecyclerView rvProject;
    private ProductListGridAdapter projectAdapter;    // 爱心项目列表适配器
    private View view;
    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    public static Fragment newInstance() {
        TabFragment5 fragment = new TabFragment5();
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

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            isLoadOver = true;
            setDates();
        }
    }

    private void setDates() {

        loadDate();
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
        rvProject = (RecyclerView) view.findViewById(R.id.recycler);
    }


    private void initRecyclerView() {
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 配置LinearLayoutManager
        rvProject.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProject.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.background)));
        projectAdapter = new ProductListGridAdapter(getActivity(), projectDetailList);
        rvProject.setAdapter(projectAdapter);
        projectAdapter.setmOnItemClickListener(new ProductListGridAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                /*点击显示项目详情*/
                Log.i("点击了","list"+position);
                Intent intent = new Intent(getContext(), ProjectDetailActivity.class);
                intent.putExtra("project_detail",projectDetailList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }
    private void loadDate() {
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        int typeId = 5;
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("typeId",typeId);
        httpClient.post(Constant.PROJECT_TYPE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    Log.i(TAG,jsonString);

                    BasePojo<ProjectDetail> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString,
                            new TypeToken<BasePojo<ProjectDetail>>(){}.getType());
                    if(basePojo.isSuccess()){
                        pgDialog.dismiss();
                        projectDetailList = basePojo.getData();
                        initRecyclerView();
                        Log.i(TAG, "onSuccess: projectDetailList"+ projectDetailList);
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
}
