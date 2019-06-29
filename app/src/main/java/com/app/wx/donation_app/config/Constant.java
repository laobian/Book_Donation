package com.app.wx.donation_app.config;

import java.net.URL;

/**
 * @Author:wx
 * @Date:2019/3/5
 */
public class Constant {
/*192.168.43.190*/
    public static final String URL_BASE = "http://192.168.43.190:8080/donation";

    public static final String QINIU_BASE_URL = "http://pqtosfjyo.bkt.clouddn.com/";

    /*七牛云获取uptoken*/
    public static final String QINIU_UPTOKEN = URL_BASE + "/UploadAction/getToken";


    public static final String USER_URL = URL_BASE+"/user";
    public static final String PROJECT_URL = URL_BASE+"/project";
    public static final String ORDER_URL = URL_BASE+"/order";
    /*获取广告信息的接口*/
    public static final String URL_BANNER_GET = URL_BASE + "/BannerGet";
    public static final String URL_Register = USER_URL + "/add";    //用户注册
    public static final String URL_Login = USER_URL + "/login";     //用户登录
    public static final String URL_Get = USER_URL + "/getUser";     //查询用户个人信息
    /*更新用户个人信息*/
    public static final String URL_USER_UPDATE = USER_URL + "/info_update";

    /*查找贫困项目*/
    public static final String URL_USER_SEARCH = USER_URL + "/search";

    /*我捐过的贫困项目*/
    public static final String URL_USER_MY_DONATION = USER_URL + "/donation";

    /*发起一起捐*/
    public static final String URL_USER_START_PROJECT = USER_URL+"/startDonation";

    /*查找我的荣誉*/
    public static final String URL_USER_ACHIEVEMENT = USER_URL + "/myAchievement";

    /*捐赠*/
    public static final String URL_USER_DONATE = USER_URL+"/donate";

    /*查看捐赠的数的总数量*/
    public static final String URL_USER_TOTAL_DONATE = USER_URL+"/total_number";

    /*projectController*/
    /*查找所有项目*/
    public static final String PROJECT_LIST_URL = PROJECT_URL+"/list";

    /*按照类别查找*/
    public static final String PROJECT_TYPE_URL = PROJECT_URL+"/sort";

    /*订单创建*/
    public static final String ORDER_CREATE = ORDER_URL+"/create";

    /*查找我的订单*/
    public static final String ORDER_MINE = ORDER_URL+"/myOrder";

    /*更新订单状态*/
    public static final String ORDER_UPDATE = ORDER_URL+"/update";

    /*根据订单状态查找订单*/
    public static final String ORDER_FIND_BY_STATUE = ORDER_URL+"/findByOrderStatue";
}

