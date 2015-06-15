package com.daren.base.base_adapter;

/**
 * Description:
 * Created by wangjianlei on 15/6/8.
 */
public interface MultiItemTypeSupport<T> {
    int getLayoutId(int position , T t);

    int getViewTypeCount();

    int getItemViewType(int postion,T t );
}
