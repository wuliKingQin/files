package com.tv.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tv.filemanager.R;
import com.tv.filemanager.activity.MainActivity;
import com.tv.filemanager.adapter.CommonAdapter;
import com.tv.filemanager.bean.CFile;
import com.tv.filemanager.bean.ClickPath;
import com.tv.filemanager.bean.Sort;
import com.tv.filemanager.other.AbsAsyncTask;
import com.tv.filemanager.other.SortTask;
import com.tv.filemanager.utils.FileUtil;
import com.tv.filemanager.utils.SortUtil;
import com.tv.filemanager.widget.SortWindowOp;
import com.tv.framework.adapter.AbsBaseAdapter;
import com.tv.framework.adapter.DividerGridItemDecoration;
import com.tv.framework.fragment.AbsBaseFragment;
import com.tv.framework.widget.anim.DefaultItemAnimator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 功能描述：公共的碎片，用在加载文件
 * 开发状况：正在开发中
 * 开发作者：黎丝军
 * 开发时间：2017/3/17- 16:20
 */

public abstract class AbsCommonFragment extends AbsBaseFragment
        implements AbsBaseAdapter.OnItemClickListener,SortWindowOp.ISelectCallback{

    //保存当前路径
    protected String mCurrentPath;
    //是否点击进入下级目录
    protected boolean isNextDir = false;
    //保存文件是否被打开
    protected boolean isFileOpen = false;
    //空的内容显示
    private TextView mEmptyFolderView;
    //是否被激活
    private boolean isActivate = false;
    //保存主界面实例
    protected MainActivity mActivity;
    //用于承载数据
    private RecyclerView mCommonRv;
    //文件列表适配器
    private CommonAdapter mCommonAdapter;
    //保存点击目录进入路径
    private List<ClickPath> mPaths = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreateView() {
        setContentView(R.layout.fragment_common);
        mCommonRv = findViewById(R.id.rv_common);
        mEmptyFolderView = findViewById(R.id.tv_empty_folder);
    }

    @Override
    public void onInitObjects() {
        mCommonAdapter = new CommonAdapter(mCommonRv);
    }

    @Override
    public void onSetListeners() {
        getAdapter().setOnItemClickListener(this);
        getAdapter().registerAdapterDataObserver(mDataObserver);
        SortWindowOp.instance().addSelectCallback(this,this);
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        mCommonRv.setLayoutManager(getLayoutManager());
        mCommonRv.setAdapter(getAdapter());
        mCommonRv.addItemDecoration(new DividerGridItemDecoration(getContext()));
        mCommonRv.setItemAnimator(new DefaultItemAnimator());

        setFocusItemPosition(0);
        //加载数据
        updateFile();
    }

    /**
     * 获取布局管理器
     * @return 布局管理器实例
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(),5);
    }

    /**
     * 获取适配器
     * @return 适配器实例
     */
    public AbsBaseAdapter getAdapter() {
        return mCommonAdapter;
    }

    /**
     * 浏览文件任务
     * @param path 路径
     * @param comparator 比较器
     * @return 任务实例
     */
    protected abstract AbsAsyncTask getScanFileTask(String path, Comparator comparator);

    /**
     * 设置菜单视图Id
     * @param menuViewId 菜单资源Id
     */
    protected void setMenuViewId(int menuViewId) {
        mCommonAdapter.setMenuViewId(menuViewId);
    }

    @Override
    public void onItemClick(AbsBaseAdapter adapter, int position) {
        final CFile file = mCommonAdapter.getItem(position);
        if(file.isFile()) {
            isFileOpen = true;
        }
        mCurrentPath = file.getPath();
        onItemClick(file,position);
    }

    @Override
    public void onSelected(Sort sort) {
        final SortTask sortTask = new SortTask(getAdapter(),sort);
        sortTask.startTask();
    }

    /**
     * 点击item时执行该方法
     * @param file 对应item的实例
     * @param position 位置
     */
    protected abstract void onItemClick(CFile file,int position);

    /**
     * 设置路径
     * @param path 路径显示
     */
    protected void setPath(String path) {
        if(mActivity != null) {
            mActivity.getPathTv().setText(path);
        }
    }

    /**
     * 设置路径横条是否可见
     * @param visibility 是否可见
     */
    protected void setPathBarVisibility(int visibility) {
        if(mActivity != null) {
            if(visibility == View.VISIBLE) {
                mActivity.getSortBtn().setFocusable(true);
            } else {
                mActivity.getSortBtn().setFocusable(false);
            }
            mActivity.getPathBarRl().setVisibility(visibility);
        }
    }

    /**
     * 按键监听
     * @return
     */
    public boolean onKeyDown() {
        return false;
    }

    /**
     * 更新数据
     */
    private void updateData(Sort sort) {
        setPathBarVisibility(View.VISIBLE);
        final AbsAsyncTask task = getScanTask(FileUtil.getSortType(sort));
        if(task != null) {
            task.startTask();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isFileOpen) {
            updateFile();
        } else {
            isFileOpen = false;
            mCommonAdapter.refreshView();
        }
        isActivate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivate = false;
    }

    /**
     * 设置适配器为多选模式
     * @param isMuSelectedModel true表示为多选模式，否则相反
     */
    public void setAdapterMuselectModel(boolean isMuSelectedModel) {
        mCommonAdapter.setMuselectModel(isMuSelectedModel);
    }

    /**
     * 获取是否是多选模式
     * @return true表示多选模式，否则表示不是
     */
    public boolean isMuselectModel() {
        if(mCommonAdapter != null) {
            return mCommonAdapter.isMuselectModel();
        }
        return  false;
    }

    /**
     * 获取多选数据
     * @return 多选数据
     */
    public List<CFile> getMuselectedData() {
        return mCommonAdapter.getSelectedData();
    }

    /**
     * 获取到默认的排序
     * @return 排序实例
     */
    protected Sort getDefaultSort() {
        return SortUtil.getSetSort();
    }

    /**
     * 获取扫描任务器
     * @return 实例
     */
    protected abstract AbsAsyncTask getScanTask(Comparator comparator);

    public boolean isActivate() {
        return isActivate;
    }

    /**
     * 设置该页面是否被激活
     * @param isActivate 是否被激活
     */
    public void setActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }

    //数据观察
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if(getAdapter().getItemCount() <= 0) {
                mActivity.getSortBtn().setFocusable(false);
                mActivity.getSortBtn().setVisibility(View.INVISIBLE);
                mEmptyFolderView.setVisibility(View.VISIBLE);
                mCommonRv.setVisibility(View.GONE);
            } else {
                mEmptyFolderView.setVisibility(View.GONE);
                mCommonRv.setVisibility(View.VISIBLE);
                mActivity.getSortBtn().setFocusable(true);
                mActivity.getSortBtn().setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 添加点击路径
     * @param path 路径
     * @param position 位置
     */
    protected void addClickPath(String path,int position) {
        mCurrentPath = path;
        mPaths.add(new ClickPath(path,position));
    }

    /**
     * 移除点击路径
     * @param position 位置
     */
    protected void removeClickPath(int position) {
        mPaths.remove(position);
    }

    /**
     * 获取点击路径
     * @param position 位置
     * @return 路径实例
     */
    protected ClickPath getClickPath(int position) {
        if(!mPaths.isEmpty()) {
            return mPaths.get(position);
        }
        return null;
    }

    /**
     * 清除点击路径
     */
    protected void clearClickPath() {
        mCurrentPath = null;
        mPaths.clear();
    }

    /**
     * 获取点击路径大小
     * @return 个数
     */
    protected int clickPathSize() {
        return mPaths.size();
    }

    /**
     * 设置选中的位置
     * @param position 位置
     */
    protected void setFocusItemPosition(int position) {
        mCommonAdapter.setSelectedPosition(position);
    }

    /**
     * 用于更新文件
     */
    public void updateFile() {
        updateData(getDefaultSort());
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        if(mCurrentPath != null) {
            getScanFileTask(mCurrentPath, FileUtil.getSortType(getDefaultSort())).startTask();
        } else {
        }
    }
}
