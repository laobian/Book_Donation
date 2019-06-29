package com.app.wx.donation_app.entry;

import lombok.Data;
import java.util.*;


/**
 * Author:wx
 * Date:2019/3/4 19:03
 */
@Data
public class Donation {

    private String donationId;

    /*对应的项目id*/
    private int projectId;

    /*对应的用户*/
    private int userId;

    /*捐赠数量*/
    private int donationNum;

    /*创建时间*/
    private Date createTime;

    public Donation() {
    }

    public Donation(int projectId, int userId, int donationNum) {
        this.projectId = projectId;
        this.userId = userId;
        this.donationNum = donationNum;
    }

    public Donation(int projectId, int userId) {
        this.projectId = projectId;
        this.userId = userId;
    }
}
