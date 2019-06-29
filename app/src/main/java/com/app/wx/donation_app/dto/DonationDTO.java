package com.app.wx.donation_app.dto;

import com.app.wx.donation_app.entry.ProjectDetail;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:wx
 * Date:2019/3/4 20:20
 */
@Data
public class DonationDTO implements Serializable {

    private String donationId;

    private int donationNum;

    private Date createTime;

    private ProjectDetail projectDetail;

    public DonationDTO() {
    }

    public DonationDTO(int donationNum, ProjectDetail projectDetail) {
        this.donationNum = donationNum;
        this.projectDetail = projectDetail;
    }
}
