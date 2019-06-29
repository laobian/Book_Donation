package com.app.wx.donation_app.fragment;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.ProjectDetailActivity;
import com.app.wx.donation_app.activity.SearchActivity;
import com.app.wx.donation_app.adapter.ItemProjectAdapter;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.util.GlideImageLoader;
import com.app.wx.donation_app.util.JsonUtil;
import com.app.wx.donation_app.view.RecycleViewDivider;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @Author:wx
 * @Date:2019/3/8
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private Banner banner;  // 广告栏控件
    private GridView gv;    // 分类导航控件
    private RecyclerView rvProject;    // 资讯列表控件
    //    private ClassifyAdapter classifyAdapter;    // 分类导航列表适配器
    private ItemProjectAdapter projectAdapter;    // 爱心项目列表适配器
    private List<ProjectDetail> projectDetailList = new ArrayList<>();

    private List<Integer> bannerImgList = new ArrayList<Integer>();
    private List<String> bannerTitleList = new ArrayList<>();
    private LinearLayout llSearchContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(); // 加载服务端数据
    }



    // fragment中布局以及控件的获取都写在此方法中
    // onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // 获取新建视图View中布局文件的空间
        initViews(view);
        initRefreshLayout();
        initBanner();
        initRecyclerView();
        return view;
    }

    private void loadData() {
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("加载中，请稍后...");
        pgDialog.show();
//        // 加载广告数据
//        new AsyncHttpClient().get(Constant.URL_BANNER_GET, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                    if (json != null) {
//                        BasePojo<List<Banners>> basePojo = JsonUtil.getBaseFromJson(getActivity(),
//                                json, new TypeToken<BasePojo<List<Banners>>>(){}.getType());
//                        if(basePojo != null){
//                            pgDialog.dismiss();
//                            bannerImgList.clear();
//                            bannerTitleList.clear();
//                            // 依次取出广告图片地址以及广告标题，存入集合中
//                            for (int i=0; i<basePojo.getData().size(); i++){
//                                bannerImgList.add(basePojo.getData().get(i).getUrl());
//                                bannerTitleList.add(basePojo.getData().get(i).getTitle());
//                            }
//                            initBanner();
//                            refreshLayout.finishRefresh();  //停止刷新
//                        }
//                    }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });

        /*加载贫困项目*/
        new AsyncHttpClient().get(Constant.PROJECT_LIST_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                try {
                    /*获取返回json字符串并转换成basePojo对象*/
                    BasePojo<ProjectDetail> basePojo = JsonUtil.getBaseFromJson(getActivity(),
                            json, new TypeToken<BasePojo<ProjectDetail>>() {
                            }.getType());
                    if (basePojo != null) {
                        pgDialog.dismiss();
                        /*获取data数据，为List类型*/
                        List<ProjectDetail> list = basePojo.getData();
                        projectDetailList.clear();
                        projectDetailList.addAll(list);
                        projectAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                        refreshLayout.finishRefresh();  //停止刷新
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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
        projectAdapter = new ItemProjectAdapter(getActivity(), projectDetailList);
        rvProject.setAdapter(projectAdapter);
        projectAdapter.setmOnItemClickListener(new ItemProjectAdapter.OnItemClickListener() {
            @Override
            public void myClick(int position) {
                /*点击显示项目详情*/
                Log.i("点击了","list"+position);
                Intent intent = new Intent(getContext(), ProjectDetailActivity.class);
                intent.putExtra("project_detail",projectDetailList.get(position));
                startActivity(intent);
            }

            @Override
            public void myOnLongClick(int position) {

            }
        });
    }

    private void initBanner() {
        bannerImgList.add(R.mipmap.banner_img1);
        bannerImgList.add(R.mipmap.banner_img2);
        bannerImgList.add(R.mipmap.banner_img4);
        bannerTitleList.add("你眼中的垃圾，别人严重的珍宝。");
        bannerTitleList.add("捐出一本书，献出一份爱");
        bannerTitleList.add("One's people's trash,another one's treasure");
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(bannerImgList);
        banner.setBannerTitles(bannerTitleList);
        banner.start();
    }

    private void initRefreshLayout() {
        refreshLayout.setIsOverLay(false);  // 是否是侵入式下拉刷新
        refreshLayout.setWaveShow(true);    // 是否显示波浪纹
        refreshLayout.setLoadMore(false);   // 是否需要上拉加载
        refreshLayout.setWaveColor(getResources().getColor(R.color.iconAfter));   // 设置波浪纹颜色
        // 注册下拉刷新监听器
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            // 下拉刷新执行的方法
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadData(); // 重新加载数据
            }
        });
    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refresh_home);
        banner = view.findViewById(R.id.banner_home);
        gv = view.findViewById(R.id.gv_home);
        rvProject = view.findViewById(R.id.rc_project);
        llSearchContainer = view.findViewById(R.id.home_search_textView);
        llSearchContainer.setOnClickListener(this);
//        classifyAdapter = new ClassifyAdapter();
//        gv.setAdapter(classifyAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_search_textView:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
        }
    }
}
