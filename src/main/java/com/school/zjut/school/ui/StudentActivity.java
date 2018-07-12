package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.school.zjut.school.MainActivity;
import com.school.zjut.school.R;
import com.school.zjut.school.application.BaseApplication;
import com.school.zjut.school.utils.ShareUtils;
import com.school.zjut.school.utils.StaticClass;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   StudentActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/17 23:38
 * 描述:    学生
 */

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_basic;
    private LinearLayout ll_schedule;
    private LinearLayout ll_chat;
    private LinearLayout ll_sign;
    private LinearLayout ll_sign_all;
    private LinearLayout ll_change_pass;
    private LinearLayout ll_logout;
    private TextView tv_news;

    private WebSocketClient client;
    private static final String wsUrl = "ws://47.95.215.194/socket/zjut/webSocket/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

//        String id = ShareUtils.getString(this, "id", "");
//        init(id);

        initView();
    }

    private void initView() {
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
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_news.setOnClickListener(this);
        getDataOne();
    }

    private void getDataOne() {

        String id = ShareUtils.getString(this, "id", "");
        RxVolley.get(StaticClass.student_detail + id, new HttpCallback() {
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
                JSONObject data = result.getJSONObject("data");
                ShareUtils.putString(this, "name", data.getString("name"));
                ShareUtils.putString(this, "classId", data.getString("classId"));
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_basic:
                startActivity(new Intent(this, BasicStuActivity.class));
                break;
//            case R.id.ll_schedule:
//                break;
//            case R.id.ll_chat:
//                break;
            case R.id.ll_sign:
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setClass(this, StuSignActivity.class);
                startActivity(new Intent(this, StuSignActivity.class));
                break;
//            case R.id.ll_sign_all:
//                break;
            case R.id.ll_change_pass:
                startActivity(new Intent(this, ChangePassActivity.class));
                break;
            case R.id.ll_logout:
                ShareUtils.deleAll(this);
                startActivity(new Intent(this, MainActivity.class));
//                close();
                finish();
                break;
            case R.id.tv_news:
                startActivity(new Intent(this, NewsActivity.class));
                break;
        }
    }

//    private void init(String id) {
//        try {
//            client = new WebSocketClient(new URI(wsUrl+id), new Draft_17()) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    Log.i(StaticClass.TAG, "建立连接");
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    Log.i(StaticClass.TAG, message);
//                    getMessage(message);
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    Log.i(StaticClass.TAG, code + " /" + reason + " /" + remote);
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    Log.i(StaticClass.TAG, ex.getMessage());
//                }
//            };
//            client.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getMessage(String message) {
//        Looper.prepare();
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        Looper.loop();
//        Looper.getMainLooper().quit();
//    }
//
//
//    private void close() {
//        if (client != null) {
//            client.close();
//        }
//    }

}
