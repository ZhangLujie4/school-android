package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.school.zjut.school.R;
import com.school.zjut.school.adapter.ListViewAdapter;
import com.school.zjut.school.entity.NewsData;
import com.school.zjut.school.utils.StaticClass;
import com.school.zjut.school.utils.UtilTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   NewsActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/17 23:13
 * 描述:    学校新闻
 */

public class NewsActivity extends BaseActivity {

    private ListView listView;
    private List<NewsData> mList = new ArrayList<>();
    private LinearLayout ll_news;
    private NewsData data;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initView();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.mListView);
        RxVolley.get(StaticClass.all_news, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(StaticClass.TAG, t);
                parsingJson(t);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data = mList.get(i);
                Intent intent = new Intent(NewsActivity.this, ContentActivity.class);
                intent.putExtra("newsId", data.getNewsId());
                startActivity(intent);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONArray data = result.getJSONArray("data");
                for (int i=0; i<data.length(); i++) {
                    JSONObject news = (JSONObject) data.get(i);
                    NewsData item = new NewsData();
                    item.setNewsId(news.getInt("newsId"));
                    item.setCreateTime(UtilTools.formatDate(news.getLong("createTime")));
                    item.setNewsTitle(news.getString("newsTitle"));
                    mList.add(item);
                }
                adapter = new ListViewAdapter(this, mList);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
