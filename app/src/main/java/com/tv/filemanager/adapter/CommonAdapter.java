package com.tv.filemanager.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.activity.MainActivity;
import com.tv.filemanager.bean.CFile;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.widget.view.MarqueTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：公共的文件列表适配器
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 16:39
 */

public class CommonAdapter extends AbsBaseAdapter<CFile,CommonAdapter.FileViewHolder> {

    //保存菜单资源ID
    private int mMenuViewId;
    //是否进入多选模式
    private boolean isMuselectModel = false;
    //申明多选数据
    private List<CFile> mMuSelectData = new ArrayList<>();

    public CommonAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.listview_file_item);
    }

    @Override
    protected FileViewHolder onCreateViewHolder(View view, int viewType) {
        return new FileViewHolder(view);
    }

    @Override
    protected void onBindViewData(FileViewHolder holder, CFile javaBean, int position) {
        holder.fileName.setText(javaBean.getName());
        holder.setNameLeftIcon(javaBean.getNameLeftIcon());
        if(isMuselectModel) {
            holder.muSelectModel.setVisibility(View.VISIBLE);
            holder.muSelectModel.setChecked(javaBean.isSelected());
        } else {
            holder.muSelectModel.setVisibility(View.GONE);
        }
        if(!javaBean.isFile()) {
            holder.fileCount.setText(javaBean.getChildFileCount() + "");
            holder.fileCount.setVisibility(View.VISIBLE);
            holder.fileIcon.setBackgroundResource(javaBean.getIcon());
        } else {
            holder.fileCount.setVisibility(View.INVISIBLE);
            if(javaBean.getThumbnail() != null) {
                holder.fileIcon.setBackground(new BitmapDrawable(javaBean.getThumbnail()));
            } else {
                holder.fileIcon.setBackgroundResource(javaBean.getIcon());
            }
        }

        if(position == getSelectedPosition()) {
            holder.itemView.requestFocus();
            holder.itemView.requestFocusFromTouch();
        }

        if(position >= 0 && position <= 3 && mMenuViewId > 0) {
            holder.itemView.setNextFocusUpId(mMenuViewId);
        }

        if(getItemCount() < 4) {
            holder.itemView.setNextFocusRightId(R.id.btn_sort);
        }

        if(position == 4) {
            holder.itemView.setNextFocusUpId(R.id.btn_sort);
        }
    }

    @Override
    protected void setOnItemClickListener(final FileViewHolder holder, final int position) {
        if(!isMuselectModel) {
            super.setOnItemClickListener(holder, position);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CFile cFile = getItem(position);
                    cFile.setSelected(!cFile.isSelected());
                    if(cFile.isSelected()) {
                        putSelectedData(cFile);
                    } else {
                        removeSelectData(cFile);
                    }
                    refreshView(new IRefreshCallback() {
                        @Override
                        public void onRefresh() {
                            notifyItemChanged(position);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void focusStatusHandle(FileViewHolder holder, int position) {
//        super.focusStatusHandle(holder, position);
        ((MainActivity) getContext()).setCurrSelectedFilePath(holder.itemView,getItem(position));
    }

    @Override
    protected void loseFocusAnimation(RecyclerView.ViewHolder holder, float scale) {
//        super.loseFocusAnimation(holder, scale);
        ((MainActivity) getContext()).setCurrSelectedFilePath(holder.itemView,null);
    }

    /**
     * 设置多选模式
     * @param muselectModel true表示多选，false表示不需要
     */
    public void setMuselectModel(boolean muselectModel) {
        clearSelectedData();
        isMuselectModel = muselectModel;
        for (CFile file:getData()) {
            file.setSelected(false);
        }
        refreshView();
    }

    /**
     * 获取是否是多选模式
     * @return true表示多选模式，否则表示不是
     */
    public boolean isMuselectModel() {
        return isMuselectModel;
    }

    /**
     * 添加多选的数据
     * @param cFile 实例
     */
    public void putSelectedData(CFile cFile) {
        if(!mMuSelectData.contains(cFile)) {
            mMuSelectData.add(cFile);
        }
    }

    /**
     * 移除选中的数据
     * @param cFile 数据
     */
    public void removeSelectData(CFile cFile) {
        if(mMuSelectData.contains(cFile)) {
            mMuSelectData.remove(cFile);
        }
    }

    /**
     * 清除已经选中的数据
     */
    public void clearSelectedData() {
        mMuSelectData.clear();
    }

    /**
     * 得到已经选中的数据
     * @return 选中的数据
     */
    public List<CFile> getSelectedData() {
        return mMuSelectData;
    }

    /**
     * 文件item视图支持器
     */
    public static class FileViewHolder extends RecyclerView.ViewHolder {

        //文件类型图标
        ImageView fileIcon;
        //文件名
        MarqueTextView fileName;
        //子文件个数
        TextView fileCount;
        //进入多选模式
        CheckBox muSelectModel;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileIcon = (ImageView) itemView.findViewById(R.id.iv_file_type_icon);
            fileName = (MarqueTextView) itemView.findViewById(R.id.tv_file_name);
            fileCount = (TextView) itemView.findViewById(R.id.tv_file_count);
            muSelectModel = (CheckBox) itemView.findViewById(R.id.cb_muselected);
        }

        /**
         * 设置文件名左边的图标
         * @param resId 资源Id
         */
        public void setNameLeftIcon(int resId) {
            fileName.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
        }
    }

    /**
     * 设置菜单资源Id
     * @param menuViewId 资源ID
     */
    public void setMenuViewId(int menuViewId) {
        mMenuViewId = menuViewId;
    }
}
