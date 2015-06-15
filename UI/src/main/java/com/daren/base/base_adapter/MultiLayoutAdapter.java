package com.daren.base.base_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Description:list view 多个布局文件支持
 * Created by wangjianlei on 15/6/8.
 */
public abstract class MultiLayoutAdapter<T> extends BaseQuickAdapter<T, BaseAdapterHelper> {

    protected MultiItemTypeSupport<T> mMultiItemSupport;

    public MultiLayoutAdapter(Context context, ArrayList<T> data,
                              MultiItemTypeSupport<T> multiItemSupport) {
        super(context, -1, data);
        this.mMultiItemSupport = multiItemSupport;
    }

    @Override
    public int getViewTypeCount() {
        if (this.mMultiItemSupport != null)
            return this.mMultiItemSupport.getViewTypeCount() + 1;
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (this.mMultiItemSupport != null)
            return position >= data.size() ? mMultiItemSupport
                    .getItemViewType(position, data.get(position)) : 0;
        return super.getItemViewType(position);
    }

    @Override
    protected BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        if (mMultiItemSupport == null) {
            throw new IllegalArgumentException("MultiLayoutAdapter doesn't have mMultiItemSupport");
        }
        return BaseAdapterHelper.get(
                context,
                convertView,
                parent,
                mMultiItemSupport.getLayoutId(position, data.get(position)),
                position);
    }
}
