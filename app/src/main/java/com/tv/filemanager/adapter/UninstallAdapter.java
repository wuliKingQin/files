package com.tv.filemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.InstalledApp;
import com.tv.framework.adapter.AbsBaseAdapter;

/**
 * 功能描述：卸载适配器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/23- 18:23
 */

public class UninstallAdapter extends AbsBaseAdapter<InstalledApp,UninstallAdapter.UninstallViewHolder> {


    public UninstallAdapter(RecyclerView context) {
        super(context, R.layout.listview_uninstall_item);
    }

    @Override
    protected UninstallViewHolder onCreateViewHolder(View view, int viewType) {
        return new UninstallViewHolder(view);
    }

    @Override
    protected void onBindViewData(UninstallViewHolder holder, InstalledApp javaBean, int position) {
        holder.name.setText(javaBean.getName());
        holder.icon.setBackground(javaBean.getIcon());
    }

    /**
     * 卸载视图支持器
     */
    public class UninstallViewHolder extends RecyclerView.ViewHolder {

        //应用图标
        ImageView icon;
        //应用名字
        TextView name;

        public UninstallViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            name = (TextView) itemView.findViewById(R.id.tv_app_name);
        }
    }
}
