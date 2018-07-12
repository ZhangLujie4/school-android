package com.school.zjut.school.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.school.zjut.school.R;
import com.school.zjut.school.utils.ShareUtils;
import com.school.zjut.school.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   ChangePassActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 11:32
 * 描述:    修改密码
 */

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_old_pass;
    private EditText et_new_pass;
    private EditText et_re_pass;
    private Button btn_change_pass;
    private String url;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        initView();
    }

    private void initView() {
        et_old_pass = (EditText) findViewById(R.id.et_old_pass);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_re_pass = (EditText) findViewById(R.id.et_re_pass);
        btn_change_pass = (Button) findViewById(R.id.btn_change_pass);
        btn_change_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pass:
                String password = et_old_pass.getText().toString().trim();
                String newPassword = et_new_pass.getText().toString().trim();
                String rePassword = et_re_pass.getText().toString().trim();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(newPassword)
                        && !TextUtils.isEmpty(rePassword)) {
                    if (newPassword.equals(rePassword)) {
                        String username = ShareUtils.getString(this, "id", "");
                        type = ShareUtils.getString(this, "type", "");
                        HttpParams params = new HttpParams();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("newPassword", newPassword);
                        Log.i(StaticClass.TAG, username + " " + password + " " + newPassword);
                        if (type.equals("学生")) {
                            url = StaticClass.student_change_pass;
                        } else if (type.equals("教师")) {
                            url = StaticClass.teacher_change_pass;
                        }
                        RxVolley.post(url, params, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                Log.i(StaticClass.TAG, t);
                                parsingJson(t);
                            }
                        });
                    } else {
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
                if (type.equals("教师")) {
                    startActivity(new Intent(this, TeacherActivity.class));
                } else if (type.equals("学生")) {
                    startActivity(new Intent(this, StudentActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
