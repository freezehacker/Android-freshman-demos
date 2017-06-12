package com.example.databindingdemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sjk on 17-6-12.
 *
 * 这是在DataBinding中可以重用的adapter类
 * （不与任何activity或xml有耦合）
 */

public class UniversalAdapter<T> extends ArrayAdapter<T> {

    private Context mContext;
    private int mItemLayoutId;
    private List<T> mList;
    private int mVariableId;    // 在item的XML中、绑定的变量的id
    private LayoutInflater mLayoutInflater;

    public UniversalAdapter(Context context, int itemLayoutId, List<T> list, int variableId) {
        super(context, itemLayoutId, list);
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mList = list;
        this.mVariableId = variableId;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*为了该Adapter类的重用性，这里声明的是ViewDataBinding类，而非特定的binding类*/
        ViewDataBinding binding;

        /*
        * 获取缓存item
        * 这里不需要自定义ViewHolder，因为binding就相当与ViewHolder了
        * */
        if (convertView == null) {
            binding = DataBindingUtil.inflate(mLayoutInflater, mItemLayoutId, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        /*数据绑定*/
        binding.setVariable(mVariableId, mList.get(position));


        return binding.getRoot();
    }


}
