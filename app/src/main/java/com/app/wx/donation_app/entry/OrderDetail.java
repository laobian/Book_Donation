package com.app.wx.donation_app.entry;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WX 2019/3/28 9:06
 */
@Data
public class OrderDetail implements Serializable {

    /*订单号*/
    String orderId ;

    /*项目id*/
    int projectId;

    /*用户id*/
    int userId;

    /*项目名称*/
    String projectName;

    /*书名*/
    String bookName;

    /*捐赠数量*/
    int donationNum;

    /*项目图片描述*/
    String imageUrl;

    /*捐赠订单状态 0.未寄出 1.已寄出 2.取消*/
    int orderStatue = 0;

    /*创建时间*/
    private Date createTime = new Date();

    public OrderDetail(int projectId, int userId, int donationNum) {
        this.projectId = projectId;
        this.userId = userId;
        this.donationNum = donationNum;
    }
    public OrderDetail(int projectId, int userId, String projectName, String bookName, int donationNum, String imageUrl) {
        this.projectId = projectId;
        this.userId = userId;
        this.projectName = projectName;
        this.bookName = bookName;
        this.donationNum = donationNum;
        this.imageUrl = imageUrl;
    }

    public OrderDetail() {
    }
}
