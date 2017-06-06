package com.tv.filemanager.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.CacheItem;
import com.tv.framework.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：清理适配器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/4/11- 17:07
 */

public class CleanAdapter extends BaseExpandableListAdapter {

    //用于获取子项布局实例
    private LayoutInflater mInflater = null;
    //缓存清理数据
    private List<CacheItem> cacheItems = new ArrayList<>();

    public CleanAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return cacheItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final List<CFile> files = cacheItems.get(groupPosition).getFiles();
        if(files != null) {
            return files.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cacheItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final List<CFile> files = cacheItems.get(groupPosition).getFiles();
        if(files != null) {
            return files.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_clean_item,null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.bindGroupData(cacheItems.get(groupPosition), groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_clean_child_item,null);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.bindChildData((CFile) getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 添加数据
     * @param cacheItems 数据
     */
    public void putData(List<CacheItem> cacheItems) {
        cacheItems.clear();
        cacheItems.addAll(cacheItems);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param cacheItem 缓存项
     */
    public void putData(CacheItem cacheItem) {
        cacheItems.add(cacheItem);
        notifyDataSetChanged();
    }

    /**
     * 获取字符资源
     * @param resId 资源Id
     * @return 资源字符串
     */
    private String getString(int resId) {
        return mInflater.getContext().getString(resId);
    }

    /**
     * 清除数据
     */
    public void clear() {
        cacheItems.clear();
    }

    /**
     * 组合视图支持器
     */
    public class GroupHolder {
        //箭头
        ImageView arrow;
        //清理名
        TextView cleanName;
        //清理大小
        TextView cleanSize;
        //清理单位
        TextView cleanUnit;
        //item布局
        View itemView;

        public GroupHolder(View itemView) {
            this.itemView = itemView;
            arrow = (ImageView) itemView.findViewById(R.id.cb_select);
            cleanName = (TextView) itemView.findViewById(R.id.tv_scan_name);
            cleanSize = (TextView) itemView.findViewById(R.id.tv_clean_content);
            cleanUnit = (TextView) itemView.findViewById(R.id.tv_unit_mb);
        }

        /**
         * 绑定群组数据
         * @param cacheItem 缓存数据
         */
        public void bindGroupData(CacheItem cacheItem,int groupPosition, boolean isExpanded) {
            cleanName.setText(cacheItem.getName());
            if(getChildrenCount(groupPosition) > 0){
                arrow.setVisibility(View.VISIBLE);
                int count = 0;
                for(CFile file:cacheItem.getFiles()) {
                    if(!file.isSelected()) {
                        count++;
                    }
                }
                cleanSize.setText(count + "");
                cleanUnit.setText(getString(R.string.one_item));
                if(isExpanded) {
                    arrow.setImageResource(R.mipmap.ic_arrow_top);
                } else {
                    arrow.setImageResource(R.mipmap.ic_arrow_bottom);
                }
            } else {
                arrow.setVisibility(View.GONE);
                cleanSize.setText(cacheItem.getContent());
                cleanUnit.setText(cacheItem.getUnit());
            }
        }
    }

    /**
     * 孩子视图支持器
     */
    public class ChildHolder {
        //文件名
        TextView fileName;
        //文件大小
        TextView fileSize;
        //选择
        CheckBox selected;
        //item
        View itemView;

        public ChildHolder(View itemView) {
            this.itemView = itemView;
            fileName = (TextView) itemView.findViewById(R.id.tv_child_name);
            fileSize = (TextView) itemView.findViewById(R.id.tv_child_size);
            selected = (CheckBox) itemView.findViewById(R.id.cb_child_select);
        }

        /**
         * 绑定群组数据
         * @param file 文件数据
         */
        public void bindChildData(CFile file) {
            fileName.setText(file.getName());
            fileSize.setText(Formatter.formatFileSize(mInflater.getContext(), file.getLength()));
            selected.setChecked(!file.isSelected());
        }
    }
}
