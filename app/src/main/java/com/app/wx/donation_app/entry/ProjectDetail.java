package com.app.wx.donation_app.entry;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:wx
 * @Date:2019/3/3 8:58
 */
@Data
public class ProjectDetail implements Serializable{

    public ProjectDetail() {

    }

    private int projectId;

    /* 项目名*/
    private String projectName;

    /* 所需书籍名*/
    private String bookName;

    /* 所需书籍数量*/
    private int needNumber;

    /* 已接受捐赠数量*/
    private int receivedNumber;

    /* 捐赠用户id*/
    private int startUserId;

    /* 项目图片路径*/
    private String imageUrl;

    /* 开始时间*/
    private Date startTime;

    /* 项目状态 0正在进行中 1已完成*/
    private int projectStatus;

    /* 项目描述*/
    private String projectDescription;

    /* 项目类型*/
    private int typeId;

    public ProjectDetail(String projectName, String bookName, int needNumber, String imageUrl, String projectDescription) {
        this.projectName = projectName;
        this.bookName = bookName;
        this.needNumber = needNumber;
        this.receivedNumber = 0;
        /*0代表未完成*/
        this.projectStatus = 0;
        this.typeId = 1;
        this.startTime = new Date();
        this.imageUrl = imageUrl;
        this.projectDescription = projectDescription;
    }

    public ProjectDetail(String projectName, String bookName, int needNumber, int receivedNumber,
                         int userId, String imageUrl, int projectStatus,
                         String projectDescription, int typeId) {
        this.projectName = projectName;
        this.bookName = bookName;
        this.needNumber = needNumber;
        this.receivedNumber = receivedNumber;
        this.startUserId = userId;
        this.imageUrl = imageUrl;
        this.projectStatus = projectStatus;
        this.projectDescription = projectDescription;
        this.typeId = typeId;
    }

    public int getProjectId() {
        return projectId;
    }

}