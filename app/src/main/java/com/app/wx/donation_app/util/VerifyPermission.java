package com.app.wx.donation_app.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * 申请敏感性的权限，比如拨号、短信、读写SD卡、摄像头等
 */
public class VerifyPermission {

    private static final int STORAGE_REQUSET_CODE = 1;
    private static final int CAMERA_REQUSET_CODE = 2;

    // 校验并申请SD卡读取权限
    public static void verifyStoragePermission(Activity activity) {
        int readPermission = ActivityCompat.checkSelfPermission(activity,
                "android.permission.READ_EXTERNAL_STORAGE");
        int writePermission = ActivityCompat.checkSelfPermission(activity,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        // 未获得权限，则重新申请
        if (readPermission != PackageManager.PERMISSION_GRANTED ||
                writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                            "android.permission.WRITE_EXTERNAL_STORAGE"}, STORAGE_REQUSET_CODE);
        }
    }

    // 校验并申请摄像头权限
    public static void verifyCameraPermission(Activity activity){
        int cameraPermission = ActivityCompat.checkSelfPermission(activity,
                "android.permission.CAMERA");
        if(cameraPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{"android.permission.CAMERA"}, CAMERA_REQUSET_CODE);
        }
    }



}
