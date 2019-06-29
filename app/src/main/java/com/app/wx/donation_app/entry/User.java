package com.app.wx.donation_app.entry;

import com.app.wx.donation_app.util.GetUserNameUtil;

import java.io.Serializable;
import java.util.*;

import lombok.Data;

@Data
public class User implements Serializable {
    private int userId;

    //用户名
    private String userName;

    /*手机号*/
    private String phone;

    //密码
    private String password;

//    //捐赠记录id
//    private int donationId;
//
//    //发起的项目id
//    private int projectId;

    /*性别*/
    private int sex;

    /*生日*/
    private Date birth;

    //爱心值
    private int loveValue;

    /*用户头像*/
    private String headImg;

    /*个性签名*/
    private String description;

    public User() {
    }


    public User(String phone, String password) {
        this.userName = GetUserNameUtil.getStringRandom();
        this.phone = phone;
        this.password = password;
        this.birth = new Date(96,8,15);
        this.description = "这个人很懒，什么都没留下";
        this.headImg = "http://pic.51yuansu.com/pic3/cover/01/69/80/595f67c3a6eb1_610.jpg";
    }

    public User(String userName, String phone, String password, int sex, Date birth,
                int loveValue, String headImg, String description) {
        this.userName = userName;
        this.phone = phone;
        this.password = password;
        this.sex = sex;
        this.birth = birth;
        this.loveValue = loveValue;
        this.headImg = headImg;
        this.description = description;
    }
}
