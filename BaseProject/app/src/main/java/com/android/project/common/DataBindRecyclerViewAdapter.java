package com.android.project.common;

import android.databinding.ViewDataBinding;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.android.barball_library.BGAAdapte.BGABindingRecyclerViewAdapter;
import com.android.barball_library.BGAAdapte.BGABindingViewHolder;

import java.util.List;

/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/7
 */

public abstract class DataBindRecyclerViewAdapter<M,B extends ViewDataBinding> extends BGABindingRecyclerViewAdapter<M,B> {

    public DataBindRecyclerViewAdapter() {

    }

    public DataBindRecyclerViewAdapter(int defaultItemLayoutId) {
        super(defaultItemLayoutId);
    }

    @Override
    public void onBindViewHolder(BGABindingViewHolder<B> viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        onBindHolder(viewHolder.getBinding(),mData.get(position),position);
    }

    public abstract void onBindHolder(B binding,M itemData,int position);


    /**
     * change data
     */
    public void setData(@IntRange(from = 0) int index, @NonNull M data) {
        mData.set(index, data);
        notifyItemChanged(index + getHeaderAndFooterAdapter().getHeadersCount());
    }

    /**
     * add new data in to certain location
     *
     * @param position
     */
    public void addData(@IntRange(from = 0) int position, @NonNull List<M> data) {
        mData.addAll(position, data);
        notifyItemRangeInserted(position + getHeaderAndFooterAdapter().getHeadersCount(), data.size());
        compatibilityDataSizeChanged(data.size());
    }

    /**
     * additional data;
     *
     * @param newData
     */
    public void addData(@NonNull List<M> newData) {
        this.mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size() + getHeaderAndFooterAdapter().getHeadersCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        int internalPosition = position + getHeaderAndFooterAdapter().getHeadersCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

}
