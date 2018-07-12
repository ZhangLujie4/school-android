package com.school.zjut.school.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.school.zjut.school.R;
import com.school.zjut.school.entity.ItemData;

import java.util.List;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.adapter
 * 文件名:   SpinnerAdapter
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 18:04
 * 描述:    spinnerAdapter
 */

public class SpinnerAdapter extends BaseAdapter {

    private List<ItemData> mList;
    private Context mContext;
    private ItemData data;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context mContext, List<ItemData> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.spinner_item, null);
            viewHolder.tv_label = (TextView) view.findViewById(R.id.tv_label);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        data = mList.get(i);
        viewHolder.tv_label.setText(data.getLabel());
        return view;
    }

    class ViewHolder {
        private TextView tv_label;
    }
}
