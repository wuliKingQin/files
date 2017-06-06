package com.tv.filemanager.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.utils.SettingUtil;
import com.tv.framework.activity.AbsBaseActivity;
import com.tv.framework.utils.PreferencesUtil;
import com.tv.framework.utils.ToastUtil;

import static com.tv.filemanager.utils.SettingUtil.getUsbSetting;

/**
 * 功能描述：设置界面
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:41
 */

public class SettingActivity extends AbsBaseActivity
        implements View.OnClickListener{

    //统计ok键
    private int mOkCount = 0;
    //用于拦截ok键
    private View mEnterView;
    //提示视图
    private TextView mHintTv;
    //选中图片设置
    private CheckBox mPicChecked;
    //选中usb设置
    private CheckBox mUsbChecked;
    //图片设置视图
    private View mPicSettingView;
    //usb设置视图
    private View mUsbSettingView;

    @Override
    public void onCreateView() {
        setContentView(R.layout.activity_setting);
        mPicSettingView = findViewById(R.id.rl_pic);
        mUsbSettingView = findViewById(R.id.rl_usb);
        mEnterView = findViewById(R.id.v_enter);
        mHintTv = (TextView) findViewById(R.id.tv_hint);
        mPicChecked = (CheckBox) findViewById(R.id.ch_pic_setting);
        mUsbChecked = (CheckBox) findViewById(R.id.ch_usb_setting);
    }

    @Override
    public void onInitObjects() {

    }

    @Override
    public void onSetListeners() {
        mPicSettingView.setOnClickListener(this);
        mUsbSettingView.setOnClickListener(this);
        mEnterView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mHintTv.setVisibility(View.VISIBLE);
                } else {
                    mHintTv.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        mPicChecked.setChecked(SettingUtil.getPicSetting());
        mUsbChecked.setChecked(SettingUtil.getUsbSetting());
    }

    @Override
    public void onClick(View v) {
        boolean isChecked;
        if(v.getId() == R.id.rl_pic) {
            isChecked = !mPicChecked.isChecked();
            PreferencesUtil.putBoolean("settingPic",isChecked);
            mPicChecked.setChecked(isChecked);
        } else {
            isChecked = !mUsbChecked.isChecked();
            PreferencesUtil.putBoolean("settingUsb",isChecked);
            mUsbChecked.setChecked(isChecked);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getRepeatCount() == 0 )
                || (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0)) {
            mOkCount ++;
            if(mOkCount >= 6) {
                SettingUtil.setInstallAkp(true);
                ToastUtil.toast(this,R.string.install_out_app_hint);
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
