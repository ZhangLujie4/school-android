package com.school.zjut.school.application;

import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.school.zjut.school.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 项目名:   ZLJapp
 * 包名:     com.zlj.zljapp.application
 * 文件名:   BaseApplication
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/9 1:54
 * 描述:    Application
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, false);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }
}
