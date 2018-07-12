package com.school.zjut.school.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.utils
 * 文件名:   UtilTools
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 0:14
 * 描述:    常用方法
 */

public class UtilTools {

    public static String formatDate(long createTime) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(createTime));
    }
}
