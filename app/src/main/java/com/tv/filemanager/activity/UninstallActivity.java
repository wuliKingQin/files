package com.tv.filemanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.tv.filemanager.R;
import com.tv.filemanager.adapter.UninstallAdapter;
import com.tv.filemanager.bean.InstalledApp;
import com.tv.framework.activity.AbsBaseActivity;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.adapter.DividerGridItemDecoration;

import java.util.List;

/**
 * 功能描述：卸载界面
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:40
 */

public class UninstallActivity extends AbsBaseActivity {

    //卸载适配器
    private UninstallAdapter mAdapter;
    //卸载列表视图
    private RecyclerView mUninstallView;

    @Override
    public void onCreateView() {
        setContentView(R.layout.activity_uninstall);
        mUninstallView = (RecyclerView) findViewById(R.id.rv_uninstall);
    }

    @Override
    public void onInitObjects() {
        mAdapter = new UninstallAdapter(mUninstallView);
    }

    @Override
    public void onSetListeners() {
        mAdapter.setOnItemClickListener(new AbsBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AbsBaseAdapter adapter, int position) {
                final InstalledApp app = mAdapter.getItem(position);
                uninstallApk(app.getPackageName());
            }
        });
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        mUninstallView.setLayoutManager(new GridLayoutManager(this,5));
        mUninstallView.addItemDecoration(new DividerGridItemDecoration(this));
        mUninstallView.setAdapter(mAdapter);

        //注册安装和卸载广播监听器
        registerReceiver(mUninstallAppReceiver,makeIntentFilter());

        //初始化已经安装的app
        initInstalledApp();
    }

    /**
     * 初始化已经安装的app
     */
    private void initInstalledApp() {
        InstalledApp app;
        final List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            //过滤掉系统app
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }
            if (packageInfo.applicationInfo.loadIcon(getPackageManager()) == null) {
                continue;
            }
            if(TextUtils.equals(packageInfo.packageName,getPackageName())) {
                continue;
            }
            app = new InstalledApp();
            app.setName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            app.setPackageName(packageInfo.packageName);
            app.setIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
            mAdapter.putData(app);
        }
    }

    /**
     * 卸载apk
     * @param packageName 包名
     */
    private void uninstallApk(String packageName) {
        final Uri uri = Uri.parse("package:" + packageName);
        final Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);
    }

    /**
     * 监听安装和卸载app
     */
    public BroadcastReceiver mUninstallAppReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_PACKAGE_ADDED.equals(action) ||
                    Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                mAdapter.clear();
                initInstalledApp();
            }
        }
    };

    /**
     * 广播过滤器
     * @return IntentFilter 实例
     */
    private IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUninstallAppReceiver);
    }
}
