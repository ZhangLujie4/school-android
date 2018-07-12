package com.school.zjut.school.adapter;

import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.school.zjut.school.R;
import com.school.zjut.school.entity.NewsData;

import java.util.List;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.adapter
 * 文件名:   ListViewAdapter
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 1:28
 * 描述:    listViewAdapter
 */

public class ListViewAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private List<NewsData> mList;
    private NewsData data;

    public ListViewAdapter(Context mContext, List<NewsData> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
            view = inflater.inflate(R.layout.news_item, null);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        data = mList.get(i);
        viewHolder.tv_date.setText(data.getCreateTime());
        viewHolder.tv_title.setText(data.getNewsTitle());
        return view;
    }

    class ViewHolder {
        private TextView tv_title;
        private TextView tv_date;
    }
}
