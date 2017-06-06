package com.tv.filemanager.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tv.filemanager.R;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.fragment.AbsCommonFragment;
import com.tv.filemanager.utils.SortUtil;
import com.tv.framework.utils.PreferencesUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 功能描述：排序选中弹出窗体
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 9:10
 */

public class SortWindowOp implements View.OnClickListener {

    //升序
    private View mStlOrder;
    //降序
    private View mLtsOrder;
    //文件名
    private View mFileNameSort;
    //文件大小
    private View mFileSizeSort;
    //修改时间
    private View mModifyTimeSort;
    //文件名被选中
    private ImageView mFileNameIv;
    //文件大小被选中
    private ImageView mFileSizeIv;
    //选择升序
    private ImageView mOrderStlIv;
    //选择降序
    private ImageView mOrderLtsIv;
    //文件修改时间被选中
    private ImageView mModifyTimeIv;
    //保存排序实例
    private Sort mSort;
    //选项视图
    private View mOptionView;
    //用于弹出选择窗体
    private PopupWindow mWindow;
    //保存回调接口实例
    private Map<AbsCommonFragment,ISelectCallback> mCallback;
    //保存实例
    private static SortWindowOp mInstance;

    /**
     * 获取实例，在调用该方法前必须要保证你已经实例化了
     * @return 实例
     */
    public static SortWindowOp instance() {
        return mInstance;
    }

    /**
     * 带参数的构造函数
     * @param context 运行环境
     */
    public SortWindowOp(Context context) {
        mInstance = this;
        mCallback = new HashMap<>();
        mOptionView = LayoutInflater.from(context).inflate(R.layout.window_file_sort,null);
        mModifyTimeSort = mOptionView.findViewById(R.id.rl_sort_modify_time);
        mFileSizeSort = mOptionView.findViewById(R.id.rl_sort_file_size);
        mLtsOrder = mOptionView.findViewById(R.id.rl_sort_order_lts);
        mStlOrder = mOptionView.findViewById(R.id.rl_sort_oder_stl);
        mFileNameSort = mOptionView.findViewById(R.id.rl_sort_file_name);

        mFileNameIv = (ImageView) mOptionView.findViewById(R.id.iv_sort_file_name);
        mFileSizeIv = (ImageView) mOptionView.findViewById(R.id.iv_sort_file_size);
        mOrderStlIv = (ImageView) mOptionView.findViewById(R.id.iv_order_stl);
        mOrderLtsIv = (ImageView) mOptionView.findViewById(R.id.iv_order_lts);
        mModifyTimeIv = (ImageView) mOptionView.findViewById(R.id.iv_modify_time);

        mModifyTimeSort.setOnClickListener(this);
        mFileSizeSort.setOnClickListener(this);
        mLtsOrder.setOnClickListener(this);
        mStlOrder.setOnClickListener(this);
        mFileNameSort.setOnClickListener(this);

        initOldSelected();

        mWindow = new PopupWindow(mOptionView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
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

    /**
     * 初始化之前的选择
     */
    private void initOldSelected() {
        mSort = SortUtil.getSetSort();
        setSortTypeSelected(mSort.getType());
        setOrderSelected(mSort.getOrder());
    }

    /**
     * 发送改变监听
     */
    private void sendChangeNotify() {
        AbsCommonFragment fragment;
        final Iterator<AbsCommonFragment> iterator = mCallback.keySet().iterator();
        while (iterator.hasNext()) {
            fragment = iterator.next();
            if(fragment.isActivate()) {
                mCallback.get(fragment).onSelected(mSort);
                break;
            }
        }
    }

    /**
     * 弹出选择窗体
     * @param supportView 支持视图
     */
    public void windowOption(View supportView) {
        if(mWindow != null)  {
            initOldSelected();
            mWindow.showAsDropDown(supportView);
        }
    }

    /**
     * 取消弹出框
     */
    public void dismiss() {
        if(mWindow != null) {
            mWindow.dismiss();
        }
    }

    /**
     * 设置选择回调接口
     * @param callback 回调接口实例
     */
    public void addSelectCallback(AbsCommonFragment fragment,ISelectCallback callback) {
        if(!mCallback.containsKey(fragment))  {
            mCallback.put(fragment,callback);
        }
    }

    /**
     * 保存排序
     * @param sort 排序
     */
    private void setSort(Sort sort) {
        PreferencesUtil.putInt("order",sort.getOrder());
        PreferencesUtil.putInt("typeValue",getTypeValue(sort.getType()));
    }

    /**
     * 获取类型值
     * @param type 排序类型值
     * @return
     */
    private int getTypeValue(Sort.Type type) {
        int typeValue = 1;
        switch (type) {
            case MODIFY_TIME:
                typeValue = 1;
                break;
            case FILE_NAME:
                typeValue = 2;
                break;
            case FILE_SIZE:
                typeValue = 3;
                break;
            default:
                break;
        }
        return typeValue;
    }

    /**
     * 根据类型值获取资源Id
     * @param typeValue 类型值
     * @return 对应的资源ID
     */
    private void setSortTypeSelected(Sort.Type typeValue) {
        switch (typeValue) {
            case MODIFY_TIME:
                mModifyTimeIv.setVisibility(View.VISIBLE);
                mFileNameIv.setVisibility(View.INVISIBLE);
                mFileSizeIv.setVisibility(View.INVISIBLE);
                break;
            case FILE_NAME:
                mModifyTimeIv.setVisibility(View.INVISIBLE);
                mFileNameIv.setVisibility(View.VISIBLE);
                mFileSizeIv.setVisibility(View.INVISIBLE);
                break;
            default:
                mModifyTimeIv.setVisibility(View.INVISIBLE);
                mFileNameIv.setVisibility(View.INVISIBLE);
                mFileSizeIv.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 根据排序值获取对应的资源Id
     * @param order 排序值
     * @return 对应的资源Id
     */
    public void setOrderSelected(int order) {
        if(order == -1) {
            mOrderLtsIv.setVisibility(View.VISIBLE);
            mOrderStlIv.setVisibility(View.INVISIBLE);
        } else {
            mOrderStlIv.setVisibility(View.VISIBLE);
            mOrderLtsIv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sort_file_name:
                mSort.setType(Sort.Type.FILE_NAME);
                break;
            case R.id.rl_sort_modify_time:
                mSort.setType(Sort.Type.MODIFY_TIME);
                break;
            case R.id.rl_sort_file_size:
                mSort.setType(Sort.Type.FILE_SIZE);
                break;
            case R.id.rl_sort_order_lts:
                mSort.setOrder(-1);
                break;
            case R.id.rl_sort_oder_stl:
                mSort.setOrder(1);
                break;
            default:
                break;
        }
        setSort(mSort);
        sendChangeNotify();
        mWindow.dismiss();
    }

    /**
     * 选择回调接口
     */
    public interface ISelectCallback {
        /**
         * 选择排序结果
         * @param sort 排序实例
         */
        void onSelected(Sort sort);
    }

}
