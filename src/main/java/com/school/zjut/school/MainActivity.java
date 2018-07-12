package com.school.zjut.school;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.school.zjut.school.ui.NewsActivity;
import com.school.zjut.school.ui.StudentActivity;
import com.school.zjut.school.ui.TeacherActivity;
import com.school.zjut.school.utils.ShareUtils;
import com.school.zjut.school.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_news;
    private Spinner spinner;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    //用来记录spinner选中值
    private String item;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String type = ShareUtils.getString(this, "type", "");
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("学生")) {
                startActivity(new Intent(this, StudentActivity.class));
                finish();
            } else if (type.equals("教师")) {
                startActivity(new Intent(this, TeacherActivity.class));
                finish();
            }
        }

        initView();
    }

    //初始化View
    private void initView() {
        item = getResources().getStringArray(R.array.items)[0];
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_news.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = getResources().getStringArray(R.array.items)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_news:
                startActivity(new Intent(this, NewsActivity.class));
                break;
            case R.id.btn_login:
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    HttpParams params = new HttpParams();
                    params.put("username", username);
                    params.put("password", password);
                    if (item.equals("教师")) {
                        url = StaticClass.teacher_login;
                    } else if (item.equals("学生")) {
                        url = StaticClass.student_login;
                    }
                    RxVolley.post(url, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.i(StaticClass.TAG, t);
                            parsingJson(t);
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            Log.e(StaticClass.TAG, strMsg);
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject data = new JSONObject(t);
            int code = data.getInt("code");
            String msg = data.getString("msg");
            if (code == 0) {
                JSONObject json = data.getJSONObject("data");
                ShareUtils.putString(this, "id", json.getString("id"));
                ShareUtils.putString(this, "type", item);
                if (item.equals("教师")) {
                    startActivity(new Intent(this, TeacherActivity.class));
                    finish();
                } else if (item.equals("学生")){
                    startActivity(new Intent(this, StudentActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
