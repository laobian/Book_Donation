package com.app.wx.donation_app.entry;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BasePojo<T> {

    private boolean success;
    private String msg;
//    private int page; 	// 页码
//    private int total; // 总页数
    private List<T> data = new ArrayList<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BasePojo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
