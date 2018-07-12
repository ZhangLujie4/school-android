package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.school.zjut.school.MainActivity;
import com.school.zjut.school.R;
import com.school.zjut.school.utils.ShareUtils;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   TeacherActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/17 23:38
 * 描述:    教师
 */

public class TeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_news;
    private LinearLayout ll_basic;
    private LinearLayout ll_schedule;
    private LinearLayout ll_chat;
    private LinearLayout ll_sign;
    private LinearLayout ll_sign_all;
    private LinearLayout ll_change_pass;
    private LinearLayout ll_logout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        initView();
    }

    private void initView() {
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_news.setOnClickListener(this);
        ll_basic = (LinearLayout) findViewById(R.id.ll_basic);
        ll_basic.setOnClickListener(this);
//        ll_schedule = (LinearLayout) findViewById(R.id.ll_schedule);
//        ll_schedule.setOnClickListener(this);
//        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
//        ll_chat.setOnClickListener(this);
        ll_sign = (LinearLayout) findViewById(R.id.ll_sign);
        ll_sign.setOnClickListener(this);
//        ll_sign_all = (LinearLayout) findViewById(R.id.ll_sign_all);
//        ll_sign_all.setOnClickListener(this);
        ll_change_pass = (LinearLayout) findViewById(R.id.ll_change_pass);
        ll_change_pass.setOnClickListener(this);
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_news:
                startActivity(new Intent(this, NewsActivity.class));
                break;
            case R.id.ll_basic:
                startActivity(new Intent(this, BasicTeaActivity.class));
                break;
//            case R.id.ll_schedule:
//                break;
//            case R.id.ll_chat:
//                break;
            case R.id.ll_sign:
                startActivity(new Intent(this, SignActivity.class));
                break;
//            case R.id.ll_sign_all:
//                break;
            case R.id.ll_change_pass:
                startActivity(new Intent(this, ChangePassActivity.class));
                break;
            case R.id.ll_logout:
                ShareUtils.deleAll(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
