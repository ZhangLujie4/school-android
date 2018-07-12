package com.school.zjut.school.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.school.zjut.school.R;
import com.school.zjut.school.utils.ShareUtils;
import com.school.zjut.school.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   BasicTeaActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 13:11
 * 描述:    教师个人资料
 */

public class BasicTeaActivity extends BaseActivity {

    private TextView tv_id;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_year;
    private TextView tv_institute_name;
    private TextView tv_major_name;
    private TextView tv_class;
    private TextView tv_institute;
    private TextView tv_institute_desc;
    private String id;
    private String instituteId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_teacher);

        initView();
    }

    private void initView() {

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birth = (TextView) findViewById(R.id.tv_birth);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_institute_name = (TextView) findViewById(R.id.tv_institute_name);
        tv_major_name = (TextView) findViewById(R.id.tv_major_name);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_institute = (TextView) findViewById(R.id.tv_institute);
        tv_institute_desc = (TextView) findViewById(R.id.tv_institute_desc);
        id = ShareUtils.getString(this, "id", "");
        getDataOne();
    }

    private void getDataOne() {

        RxVolley.get(StaticClass.teacher_detail + id, new HttpCallback() {
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
                tv_id.setText("工号：" + data.getString("id"));
                tv_name.setText("姓名：" + data.getString("name"));
                tv_sex.setText("性别：" + data.getString("sex"));
                tv_birth.setText("生日：" + data.getString("birth"));
                tv_year.setText("入职年份：" + data.getInt("year"));
                instituteId = data.getString("institute");
                tv_institute_name.setText("学院名称：" + data.getString("instituteName"));
                tv_major_name.setText("专业班级：" + data.getString("majorName")
                        + " " + data.getString("classNum") + "班");
                tv_class.setText("班级编号：" + data.getString("classId"));
                getDataTwo();
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataTwo() {

        RxVolley.get(StaticClass.teacher_institute + instituteId, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(StaticClass.TAG, t);
                parsingInstitute(t);
            }
        });
    }

    private void parsingInstitute(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONObject data = result.getJSONObject("data");
                tv_institute.setText("学院：" + data.getString("instituteName"));
                tv_institute_desc.setText("简介：" + data.getString("description"));
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
