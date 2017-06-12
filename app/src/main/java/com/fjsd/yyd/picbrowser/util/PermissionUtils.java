package com.fjsd.yyd.picbrowser.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/6/10 0010.
 * 权限工具类
 */

public class PermissionUtils {
    public static boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }
    //检查是否有某个权限
    private static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    //检查是否有外部存储卡的读取权限
    public static boolean isDeviceInfoGranted(Context context) {
        return checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    //请求某个权限
    public static void requestPermissions(Object o, int permissionId, String... permissions) {
        if (o instanceof Activity) {
            ActivityCompat.requestPermissions((AppCompatActivity) o, permissions, permissionId);
        }
    }
}
