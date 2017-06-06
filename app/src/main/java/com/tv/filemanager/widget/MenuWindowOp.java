package com.tv.filemanager.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.CFile;

/**
 * 功能描述：菜单选中弹出框选中
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/24- 9:15
 */

public class MenuWindowOp implements View.OnClickListener{

    //被选中的视图
    private View mSelectedView;
    //保存当前选中的文件路径
    private CFile mSelectedFilePath;
    //保存需要隐藏的资源Id
    private int mHideResId = -1;
    //新建文件夹
    private TextView mNewFolderBtn;
    //删除
    private TextView mDeleteFileBtn;
    //复制
    private TextView mCopyFileBtn;
    //多选
    private TextView mSelectModeBtn;
    //重命名
    private TextView mRenameFileBtn;
    //文件属性
    private TextView mFileInfoBnt;
    //选项视图
    private View mOptionView;
    //用于弹出选择窗体
    private PopupWindow mWindow;
    //运行环境
    private Context mContext;
    //选中接口回调
    private IMenuSelectCallback mCallback;

    public MenuWindowOp(Context context) {
        mContext = context;
        mOptionView = LayoutInflater.from(context).inflate(R.layout.window_menu,null);
        mWindow = new PopupWindow(mOptionView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mNewFolderBtn = (TextView) mOptionView.findViewById(R.id.tv_menu_create);
        mDeleteFileBtn = (TextView) mOptionView.findViewById(R.id.tv_menu_delete);
        mCopyFileBtn = (TextView) mOptionView.findViewById(R.id.tv_menu_copy);
        mSelectModeBtn = (TextView) mOptionView.findViewById(R.id.tv_menu_select_mode);
        mRenameFileBtn = (TextView) mOptionView.findViewById(R.id.tv_menu_rename);
        mFileInfoBnt = (TextView) mOptionView.findViewById(R.id.tv_menu_file_info);

        mNewFolderBtn.setOnClickListener(this);
        mDeleteFileBtn.setOnClickListener(this);
        mCopyFileBtn.setOnClickListener(this);
        mSelectModeBtn.setOnClickListener(this);
        mRenameFileBtn.setOnClickListener(this);
        mFileInfoBnt.setOnClickListener(this);

        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mOptionView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    //重置多选按钮文本
    public void resetMuSelectBtnText() {
        mSelectModeBtn.setText(mContext.getString(R.string.multiple_choice));
        mRenameFileBtn.setVisibility(View.VISIBLE);
        mFileInfoBnt.setVisibility(View.VISIBLE);
    }

    //处于多选模式下
    public void muselectModel() {
        mSelectModeBtn.setText(mContext.getString(R.string.cancel_choice));
        mNewFolderBtn.setVisibility(View.GONE);
        mRenameFileBtn.setVisibility(View.GONE);
        mFileInfoBnt.setVisibility(View.GONE);
    }

    /**
     * 弹出选择窗体
     */
    public void windowOption() {
        windowOption(null, -1);
    }

    /**
     * 弹出选择窗体
     * @param hideResId 需要隐藏的视图资源Id
     */
    public void windowOption(int hideResId) {
        windowOption(null, hideResId);
    }

    /**
     * 弹出选择窗体
     * @param supportView 支持视图
     */
    public void windowOption(View supportView,int hideResId) {
        if(mWindow != null)  {
            if(hideResId != -1) {
                mHideResId = hideResId;
                hideMenuViewById(hideResId,View.GONE);
            }
            if(supportView == null && mSelectedView != null) {
                supportView = mSelectedView;
            }
            mWindow.showAsDropDown(supportView);
        }
    }

    /**
     * 取消弹出框
     */
    public void dismiss() {
        if(mWindow != null) {
            if(mHideResId != -1) {
                hideMenuViewById(mHideResId,View.VISIBLE);
            }
            mWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if(mCallback != null) {
            mCallback.onSelected(v,mSelectedFilePath);
        }
        dismiss();
    }

    /**
     * 根据资源Id来隐藏不需要菜单
     * @param resId 资源Id
     */
    public void hideMenuViewById(int resId,int visible) {
        (mOptionView.findViewById(resId)).setVisibility(visible);
    }

    /**
     * 设置当前选中的文件
     * @param selectedFile 选中的文件
     */
    public void setSelectedFile(View view,CFile selectedFile) {
        mSelectedView = view;
        if(selectedFile != null) {
            mSelectedFilePath = selectedFile;
        }
    }

    /**
     * 获取被选中的路径
     * @return 路径
     */
    public CFile getSelectedFilePath() {
        return mSelectedFilePath;
    }

    /**
     * 设置菜单选择监听器
     * @param callback 监听回调
     */
    public void setMenuSelectedCallback(IMenuSelectCallback callback) {
        mCallback = callback;
    }

    /**
     * 选中菜单回调接口
     */
    public interface IMenuSelectCallback {
        /**
         * 选中处理方法
         * @param view 被选中的视图
         * @param selectedFilePath 路径
         */
        void onSelected(View view,CFile selectedFilePath);
    }
}
