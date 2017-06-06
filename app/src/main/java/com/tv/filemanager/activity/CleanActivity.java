package com.tv.filemanager.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.adapter.CleanAdapter;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.CacheItem;
import com.tv.filemanager.other.CleanManager;
import com.tv.filemanager.utils.AnimUtil;
import com.tv.filemanager.widget.ExListView;
import com.tv.framework.activity.AbsBaseActivity;
import com.tv.framework.utils.ToastUtil;

/**
 * 功能描述：清理界面
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/15- 15:41
 */

public class CleanActivity extends AbsBaseActivity {

    //清理多少内容
    private TextView mShowMbTv;
    //显示单位
    private TextView mShowMbUnit;
    //显示扫描状态
    private TextView mScanStatus;
    //扫描需要清理多少项
    private TextView mScanItemTv;
    //一键清理按钮
    private Button mOneKeyCleanBtn;
    //大的进度图
    private ImageView mBigProgressIv;
    //小的进度图
    private ImageView mSmallProgressIv;
    //清理管理器
    private CleanManager mCleanMgr;
    //清理视图列表
    private ExListView mCleanListView;
    //用于装载数据
    private CleanAdapter mCleanAdapter;

    @Override
    public void onCreateView() {
        setContentView(R.layout.activity_clear);
        mShowMbUnit = (TextView) findViewById(R.id.tv_unit_mb);
        mBigProgressIv = (ImageView) findViewById(R.id.iv_big_cycle);
        mSmallProgressIv = (ImageView) findViewById(R.id.iv_small_cycle);
        mShowMbTv = (TextView) findViewById(R.id.tv_clean_content);
        mOneKeyCleanBtn = (Button) findViewById(R.id.btn_one_clean);
        mCleanListView = (ExListView) findViewById(R.id.rv_clean_list);
        mScanItemTv = (TextView) findViewById(R.id.tv_scan_count);
        mScanStatus = (TextView) findViewById(R.id.tv_state);
    }

    @Override
    public void onInitObjects() {
        mCleanMgr = new CleanManager();
        mCleanAdapter = new CleanAdapter(this);
    }

    @Override
    public void onSetListeners() {
        mOneKeyCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCleanCache();
            }
        });

        mCleanListView.setClickOkCallBack(new ExListView.IClickOkCallback() {
            @Override
            public void onClickOk(Object itemBean) {
                if(itemBean instanceof CFile) {
                    final CFile file = (CFile) itemBean;
                    file.setSelected(!file.isSelected());
                    mCleanAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        mCleanListView.setGroupIndicator(null);
        mCleanListView.setAdapter(mCleanAdapter);
        startScanCache();
    }

    /**
     * 开始扫描缓存
     */
    private void startScanCache() {
        AnimUtil.startAnim(mBigProgressIv);
        AnimUtil.startAnim(mSmallProgressIv);
        mScanStatus.setText(R.string.scanning);
        mCleanMgr.clear();
        mCleanAdapter.clear();
        mCleanMgr.startScanCache(this, new CleanManager.IScanCacheCallback() {
            @Override
            public void onScanCache(CacheItem scanItem, int scanCount,String cacheSize, String unit) {
                mShowMbTv.setText(cacheSize);
                mShowMbUnit.setText(unit);
                mScanItemTv.setText(getString(R.string.have_scan) + scanCount + getString(R.string.item));
                mCleanAdapter.putData(scanItem);
            }

            @Override
            public void onScanEnd(int scanCount) {
                AnimUtil.stopAnim(mBigProgressIv);
                AnimUtil.stopAnim(mSmallProgressIv);
                mScanStatus.setText(R.string.scanning_end);
                mScanItemTv.setText(getString(R.string.have_scan) + scanCount + getString(R.string.item));
            }
        });
    }

    /**
     * 开始清理缓存
     */
    private void startCleanCache() {
        AnimUtil.startAnim(mBigProgressIv);
        AnimUtil.startAnim(mSmallProgressIv);
        mScanStatus.setText(R.string.cleaning);
        mCleanMgr.startCleanCache(new CleanManager.ICleanCacheCallback() {
            @Override
            public void onCleanCache(CacheItem cleanItem) {
                if(cleanItem != null) {
                    mCleanAdapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onCleanEnd(String clearSize,String unit) {
                AnimUtil.stopAnim(mBigProgressIv);
                AnimUtil.stopAnim(mSmallProgressIv);
                mScanStatus.setText(R.string.clean_end);
                mScanItemTv.setText(R.string.have_clean);
                mShowMbTv.setText(clearSize);
                mShowMbUnit.setText(unit);
            }
        });
    }
}
