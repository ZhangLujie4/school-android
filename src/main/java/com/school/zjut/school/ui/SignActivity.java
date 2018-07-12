package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.school.zjut.school.R;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   SignActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 17:11
 * 描述:    签到
 */

public class SignActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_call_sign;
    private TextView tv_online_sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
    }

    private void initView() {
        tv_call_sign = (TextView) findViewById(R.id.tv_call_sign);
        tv_online_sign = (TextView) findViewById(R.id.tv_online_sign);
        tv_call_sign.setOnClickListener(this);
        tv_online_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_call_sign:

                break;
            case R.id.tv_online_sign:
                startActivity(new Intent(this, OnlineSignActivity.class));
                break;
        }
    }
}
