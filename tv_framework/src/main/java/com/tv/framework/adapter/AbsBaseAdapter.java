package com.tv.framework.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.tv.framework.anim.ScaleAnimEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：基础列表数据适配器，合适android tv开发，暴露出按键焦点处理视图的方法
 *           1.暴露出putData()、removeData()、putLastData()、removeLastData()等添加和移除的数据的方法
 *           2.抽象出了onCreateViewHolder()、onBindViewData()、focusStatusHandle()、
 *             loseFocusStatusHandle()等方法
 * 开发状况：开发完毕
 * 开发作者：黎丝军
 * 开发时间：2017/3/6- 14:06
 * @param <JB> 泛型，代表JavaBean数据类型
 * @param <VH> 泛型，代表ViewHolder的子类
 */
public abstract class AbsBaseAdapter<JB,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    //保存运行环境
    private Context mContext;
    //保存Item视图
    private int mItemLayoutId;
    //用于保存上次选中的位置，返回时给焦点
    private int mSelectedPosition = -1;
    //保存视图
    private RecyclerView mRecyclerView;
    //动画效果
    private ScaleAnimEffect mAnimEffect;
    //用于在视图计算完毕是刷新列表
    private Handler mHandler = new Handler();
    //用于保存数据
    private List<JB> mData = new ArrayList<>();
    //保存item点击监听器
    private OnItemClickListener mItemClickListener;
    //保存item长按监听器
    private OnItemLongClickListener mItemLongClickListener;

    public AbsBaseAdapter(RecyclerView recyclerView){
        this(recyclerView,0,null);
    }

    public AbsBaseAdapter(RecyclerView recyclerView, int itemLayoutId) {
        this(recyclerView,itemLayoutId,null);
    }

    public AbsBaseAdapter(RecyclerView recyclerView, int itemLayoutId, List<JB> data) {
        mRecyclerView = recyclerView;
        mContext = recyclerView.getContext();
        mItemLayoutId = itemLayoutId;
        if(data != null && !data.isEmpty()) {
            mData.addAll(data);
        }
        mAnimEffect = new ScaleAnimEffect();
    }

    /**
     * 通过Id获取对应的布局文件
     * @param parentView 父容器
     * @param layoutId 布局Id
     * @return View实例
     */
    protected View findItemViewById(ViewGroup parentView, int layoutId) {
        return LayoutInflater.from(getContext()).inflate(layoutId,parentView,false);
    }

    /**
     * 创建ViewHolder实例的方法,方法传回Item视图实例
     * @param view 视图
     * @param viewType 视图类型
     * @return 返回实例化的ViewHolder
     */
    protected abstract VH onCreateViewHolder(View view, int viewType);

    /**
     * 为item绑定数据的方法
     * @param holder ViewHolder的子类实例
     * @param javaBean 数据实例
     * @param position item位置
     */
    protected abstract void onBindViewData(VH holder, JB javaBean,int position);

    /**
     * 处理获取焦点的处理方法
     */
    protected void focusStatusHandle(VH holder, int position) {
        focusAnimation(holder,1.20f);
    }

    /**
     * 处理失去焦点状态的处理方法
     */
    protected void loseFocusStatusHandle(VH holder, int position) {
        loseFocusAnimation(holder,1.20f);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        try {
            itemView = findItemViewById(parent,mItemLayoutId);
        } catch (Exception e) {
            itemView = parent;
        }
        return onCreateViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        setOnHoverListener(holder,position);
        setOnFocusChangeListener(holder,position);
        setOnItemClickListener(holder,position);
        setOnItemLongClickListener(holder,position);
        onBindViewData(holder,getItem(position),position);
    }

    /**
     * 设置item的长按点击监听
     * @param holder 视图
     * @param position item位置
     */
    private void setOnItemLongClickListener(VH holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mItemLongClickListener != null) {
                    return mItemLongClickListener.onItemLongClick(AbsBaseAdapter.this,position);
                }
                return true;
            }
        });
    }

    /**
     * 设置item点击监听
     * @param holder 视图
     * @param position item位置
     */
    protected void setOnItemClickListener(VH holder, final int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null) {
                    mItemClickListener.onItemClick(AbsBaseAdapter.this,position);
                }
            }
        });
    }

    /**
     * 设置选中的值的位置
     * @param selectedPosition 位置
     */
    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    /**
     * 获取选中值的位置
     * @return 位置
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 设置焦点改变监听
     * @param holder 视图
     * @param position item位置
     */
    private void setOnFocusChangeListener(final VH holder, final int position) {
        holder.itemView.setFocusable(isFocusable());
        holder.itemView.setFocusableInTouchMode(isFocusable());
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    focusStatusHandle(holder,position);
                } else {
                    loseFocusStatusHandle(holder,position);
                }
            }
        });
    }

    /**
     * 是否获得焦点，该方法用于设置item是否需要获取焦点
     * 默认情况下是true，如何不需要重写返回false就可以了
     * @return true表示可以，false表示不需要
     */
    protected boolean isFocusable() {
        return true;
    }

    /**
     * 设置item的Hover监听
     * @param holder 视图
     * @param position item位置
     */
    private void setOnHoverListener(final VH holder, final int position) {
        holder.itemView.setHovered(true);
        holder.itemView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if(v == null) return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        final int[] locations = new int[2];
                        final RecyclerView rv = (RecyclerView) holder.itemView.getParent();
                        rv.getLocationOnScreen(locations);
                        final int x = locations[0];
                        //防止滚动冲突
                        if(rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            //当超出RecyclerView的边缘时不去响应滚动
                            if(event.getRawX() > rv.getWidth() + x || event.getRawX() < x) {
                                return true;
                            }
                            v.requestFocusFromTouch();
                            v.requestFocus();
                            focusStatusHandle(holder,position);
                        }
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        loseFocusStatusHandle(holder,position);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 当获取焦点时，默认item动画
     * @param holder 视图保持器
     * @param scale 一般传值0或者1
     */
    protected void focusAnimation(RecyclerView.ViewHolder holder, float scale) {
//        holder.itemView.startAnimation(createAnimation(1.0f, scale, 1.0f, scale, 100));
//        holder.itemView.bringToFront();
    }

    /**
     * 当失去焦点时，默认item动画
     * @param holder 视图保持器
     * @param scale 一般传值0或者1
     */
    protected void loseFocusAnimation(RecyclerView.ViewHolder holder, float scale) {
//        holder.itemView.startAnimation(createAnimation(scale, 1.0f, scale, 1.0f, 100));
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    /**
     * 创建动画
     * @return 返回动画实例
     */
    private Animation createAnimation(float fromXScale, float toXScale, float fromYScale, float toYScale, long duration) {
        ScaleAnimation anim = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(duration);
        return anim;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加数据到列表适配器里
     * @param javaBean 你自己的数据实例
     */
    public void putData(JB javaBean) {
        mData.add(javaBean);
        refreshView(new IRefreshCallback() {
            @Override
            public void onRefresh() {
                notifyItemInserted(getItemCount());
            }
        });
    }

    /**
     * 添加数据到列表适配器里
     * @param position 位置
     * @param javaBean 你自己的数据实例
     */
    public void putData(final int position, JB javaBean) {
        mData.add(position,javaBean);
        refreshView(new IRefreshCallback() {
            @Override
            public void onRefresh() {
                notifyItemInserted(position);
            }
        });
    }

    /**
     * 添加数据列表
     * @param jbList
     */
    public void putData(List<JB> jbList) {
        mData.clear();
        mData.addAll(jbList);
        refreshView();
    }

    /**
     * 添加数据添加到列表最后
     * @param jbList 数据列表
     */
    public void putLastData(List<JB> jbList) {
        putData(getItemCount(), jbList);
    }

    /**
     * 根据位置插入数据集到列表
     * @param position 位置
     * @param jbList 数据集
     */
    public void putData(final int position,final List<JB> jbList) {
        mData.addAll(position,jbList);
        refreshView(new IRefreshCallback() {
            @Override
            public void onRefresh() {
                notifyItemRangeInserted(position, jbList.size());
            }
        });
    }

    /**
     * 按位置从列表中移除数据
     * @param position 位置
     */
    public void removeData(final int position) {
        final int itemCount = getItemCount();
        if(position < itemCount && position >= 0) {
            mData.remove(position);
            refreshView(new IRefreshCallback() {
                @Override
                public void onRefresh() {
                    notifyItemChanged(position);
                }
            });
        } else {
            throw new IndexOutOfBoundsException("indexOutOfBounds adapter data");
        }
    }

    /**
     * 移除最后一个数据
     */
    public void removeLastData() {
        final int lastPosition = getItemCount() - 1;
        if(lastPosition >= 0) {
            removeData(lastPosition);
        }
    }

    /**
     * 按照数据实例移除列表中的数据
     * @param javaBean 需要移除的数据实例
     */
    public void removeData(JB javaBean) {
        if(mData.contains(javaBean)) {
            int position = mData.indexOf(javaBean);
            removeData(position);
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        mData.clear();
        refreshView();
    }

    /**
     * 用于刷新视图
     */
    public void refreshView() {
        refreshView(new IRefreshCallback() {
            @Override
            public void onRefresh() {
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 判断是否计算完毕
     * @return 计算完毕才刷新
     */
    protected void refreshView(final IRefreshCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mRecyclerView != null && !mRecyclerView.isComputingLayout()
                        && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    callback.onRefresh();
                } else {
                    refreshView(callback);
                }
            }
        });
    }

    /**
     * 获取Item的数据实例
     * @param position item位置
     * @return 用户自定义的数据实例
     */
    public JB getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取列表数据集
     * @return 列表
     */
    public List<JB> getData() {
        return mData;
    }

    /**
     * 获取运行环境
     * @return Context实例
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 设置点击item监听器
     * @param itemClickListener 点击监听实例
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * 设置长按点击监听器
     * @param itemLongClickListener 长按监听实例
     */
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    /**
     * 设置视图
     * @param recyclerView 视图
     */
    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    /**
     * 处理点击事件监听器
     */
    public interface OnItemClickListener {
        /**
         * 处理点击事件的方法
         * @param adapter 适配器
         * @param position 点击位置
         */
        void onItemClick(AbsBaseAdapter adapter, int position);
    }

    /**
     * item长按点击事件监听器
     */
    public interface OnItemLongClickListener {
        /**
         * 处理长按事件的方法
         * @param adapter 适配器
         * @param position 点击位置
         * @return true表示只发生长按事件，否则点击长按都会会执行
         */
        boolean onItemLongClick(AbsBaseAdapter adapter, int position);
    }

    /**
     * 内部刷新数据
     */
    protected interface IRefreshCallback {
        /**
         * 刷新数据
         */
        void onRefresh();
    }
}
