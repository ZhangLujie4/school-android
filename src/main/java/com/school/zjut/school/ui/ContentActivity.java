package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.school.zjut.school.R;
import com.school.zjut.school.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   ContentActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 2:47
 * 描述:    新闻详情
 */

public class ContentActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_content;
    private int newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        initView();
    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);

        newsId = getIntent().getIntExtra("newsId", 0);

        RxVolley.get(StaticClass.news_detail + newsId, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(StaticClass.TAG, t);
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONObject json = result.getJSONObject("data");
                tv_title.setText(json.getString("newsTitle"));
                getSupportActionBar().setTitle(json.getString("newsTitle"));
                tv_content.setText(json.getString("newsContent"));
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
