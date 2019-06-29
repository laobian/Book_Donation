package com.app.wx.donation_app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.OrderDetail;
import com.app.wx.donation_app.entry.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:wx
 * @Date:2019/3/5
 */
public class SPUtil {

    private static final String SP_NAME = "SP_USER";
    private static final String SP_ORDER = "SP_ORDER";

    /*保存用户登录状态及个人信息*/
    public static void saveUser(Context context, boolean isLogin, User user){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.putString("user", gson.toJson(user));
        editor.commit();
    }

    /*保存用户*/
    public static void saveUser(Context context, User user){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",gson.toJson(user));
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    /*获取用户登录状态*/
    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        Log.i("登录状态", "是否登录"+isLogin);
        return isLogin;
    }

    /*获取用户个人信息*/
    public static User getUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String userJson = sp.getString("user", "");
        try {
            if(userJson != null){
                User user = JsonUtil.getObjectFromJson(userJson, User.class);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*退出登录*/
    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("user", null);
        //editor.putBoolean("rememberpassword", false);
        editor.commit();
    }

    /*保存商品*/
    public static void saveOrder(Context context, OrderDetail orderDetail){
        List<OrderDetail> orderExist = getOrderList(context);
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (orderExist!=null){
            orderDetails.addAll(orderExist);
        }
        orderDetails.add(orderDetail);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        SharedPreferences sp = context.getSharedPreferences(SP_ORDER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("orderList",gson.toJson(orderDetails));
        editor.commit();
    }

    /*查找订单*/
    public static List<OrderDetail> getOrderList(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_ORDER, Context.MODE_PRIVATE);
        String orderList = sp.getString("orderList", null);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            if(orderList != null){
                 orderDetailList = JsonUtil.getListFromJson(orderList,new TypeToken<List<OrderDetail>>(){}.getType());
                return orderDetailList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
