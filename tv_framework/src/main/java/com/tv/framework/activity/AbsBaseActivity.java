package com.tv.framework.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tv.framework.AppHelper;
import com.tv.framework.widget.handler.FragmentHandler;
import com.tv.framework.widget.handler.IUIHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：抽象基础界面activity
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/8- 9:11
 */

public abstract class AbsBaseActivity extends FragmentActivity
        implements IUIHandler {

    //保存碎片处理器，用于加载fragment碎片
    private FragmentHandler mFragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onCreateView();
        onInitObjects();
        onSetListeners();
        onInitData(savedInstanceState);
    }

    /**
     * 初始化必要的内容
     */
    private void initView() {
        AppHelper.instance().putActivity(this);
        mFragmentHandler = new FragmentHandler(getSupportFragmentManager());
    }

    @Override
    public FragmentHandler getFragmentHandler() {
        return mFragmentHandler;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppHelper.instance().removeActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    protected static final int REQUEST_CODE = 111;

    protected String[] getPermissions() {
        final String[] permissions = new String[20];
        permissions[0] = Manifest.permission.INTERNET;
        permissions[1] = Manifest.permission.CHANGE_NETWORK_STATE;
        permissions[2] = Manifest.permission.CHANGE_WIFI_STATE;
        permissions[3] = Manifest.permission.ACCESS_NETWORK_STATE;
        permissions[4] = Manifest.permission.ACCESS_WIFI_STATE;
        permissions[5] = Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;
        permissions[6] = Manifest.permission.WAKE_LOCK;
        permissions[7] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[8] = Manifest.permission.READ_PHONE_STATE;
        permissions[9] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[10] = Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[11] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[12] = Manifest.permission.USE_FINGERPRINT;
        permissions[13] = Manifest.permission.VIBRATE;
        permissions[14] = Manifest.permission.CAMERA;
        permissions[15] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[16] = Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[17] = Manifest.permission.CLEAR_APP_CACHE;
        permissions[18] = Manifest.permission.RESTART_PACKAGES;
        permissions[19] = Manifest.permission.RECEIVE_BOOT_COMPLETED;
        return permissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            PackageManager pm=getPackageManager();
            String[] rights=null;
            final List<String> deniedPermissions=new ArrayList<>();
            try{
                PackageInfo pi=pm.getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
                rights=pi.requestedPermissions;
            }catch (Exception e){
                e.printStackTrace();
            }
            if(rights!=null){
                for(int i=0;i<permissions.length;i++){
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        for(int j=0;j<rights.length;j++){
                            if(rights[j].equals(permissions[i])){
                                String permission=permissions[i];
                                permission=permission.substring(permission.lastIndexOf('.')+1);
                                deniedPermissions.add(permission);
                                break;
                            }
                        }
                    }
                }
            }
            if(deniedPermissions.size()>0){
                StringBuffer temp=new StringBuffer();
                for(String s:deniedPermissions){
                    temp.append(s+"/");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
