package com.school.zjut.school.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.school.zjut.school.R;
import com.school.zjut.school.adapter.SpinnerAdapter;
import com.school.zjut.school.entity.ItemData;
import com.school.zjut.school.utils.ShareUtils;
import com.school.zjut.school.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.ui
 * 文件名:   OnlineSignActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 17:48
 * 描述:    在线签到
 */

public class OnlineSignActivity extends BaseActivity implements View.OnClickListener {

    private List<ItemData> mList = new ArrayList<>();
    private List<String> mValueList = new ArrayList<>();
    private Spinner spinner;
    private String id;
    private SpinnerAdapter adapter;
    private String value;
    private Button btn_start;
    private EditText et_distance;
    private TextView tv_num;
    private Button btn_fresh;
    private Button btn_end;
    private LinearLayout ll_fresh;

    private MapView bmapView;
    private BaiduMap mBaiduMap;
    //定位
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private double lat, lon;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_sign);

        initView();
        initMap();
    }

    private void initMap() {
        bmapView  = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        initLocation();

        //开启定位
        mLocationClient.start();
        Log.i(StaticClass.TAG, "开启定位");
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    private void initView() {

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        et_distance = (EditText) findViewById(R.id.et_distance);
        spinner = (Spinner) findViewById(R.id.mSpinner);
        id = ShareUtils.getString(this, "id", "");
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_end.setOnClickListener(this);
        btn_fresh = (Button) findViewById(R.id.btn_fresh);
        btn_fresh.setOnClickListener(this);
        tv_num = (TextView) findViewById(R.id.tv_num);
        ll_fresh = (LinearLayout) findViewById(R.id.ll_fresh);

        isShowView(ShareUtils.getBoolean(this, "isShow", false));

        RxVolley.get(StaticClass.teacher_lesson_list + id, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(StaticClass.TAG, t);
                parsingJson(t);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                value = mValueList.get(i);
                Log.i(StaticClass.TAG, value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void isShowView(boolean isShow) {
        if (isShow) {
            btn_end.setVisibility(View.VISIBLE);
            ll_fresh.setVisibility(View.VISIBLE);
        } else {
            btn_end.setVisibility(View.GONE);
            ll_fresh.setVisibility(View.GONE);
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONArray data = result.getJSONArray("data");
                for (int i=0; i<data.length(); i++) {
                    JSONObject json = (JSONObject) data.get(i);
                    ItemData item = new ItemData();
                    item.setLabel(json.getString("label"));
                    item.setValue(json.getString("value"));
                    mValueList.add(json.getString("value"));
                    mList.add(item);
                }
                adapter = new SpinnerAdapter(this, mList);
                spinner.setAdapter(adapter);
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
            case R.id.btn_start:
                String distance = et_distance.getText().toString().trim();
                if (TextUtils.isEmpty(ShareUtils.getString(this, "lessonId", ""))) {
                    if (!TextUtils.isEmpty(distance) && !TextUtils.isEmpty(value)) {
                        HttpParams params = new HttpParams();
                        params.put("lessonId", value);
                        params.put("teaLon", String.valueOf(lon));
                        params.put("teaLat", String.valueOf(lat));
                        params.put("distance", Integer.parseInt(distance));
                        RxVolley.post(StaticClass.online_sign_start, params, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingStart(t);
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg) {
                                Log.e(StaticClass.TAG, errorNo + " " + strMsg);
                            }
                        });
                    } else {
                        Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先取消当前在线签到", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_end:
                String lessonId = ShareUtils.getString(this, "lessonId", "");
                if (!TextUtils.isEmpty(lessonId)) {
                    String signDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    HttpParams params = new HttpParams();
                    params.put("lessonId", lessonId);
                    params.put("signDate", signDate);
                    Log.i(lessonId, signDate);
                    RxVolley.post(StaticClass.online_sign_end, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.i(StaticClass.TAG, t);
                            parsingEnd(t);
                        }
                    });
                } else {
                    Toast.makeText(this, "在线签到为空", Toast.LENGTH_SHORT).show();
                    isShowView(false);
                }
                break;
            case R.id.btn_fresh:
                String lesson = ShareUtils.getString(this, "lessonId", "");
                if (!TextUtils.isEmpty(lesson)) {
                    String signDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    HttpParams params = new HttpParams();
                    params.put("lessonId", lesson);
                    params.put("signDate", signDate);
                    RxVolley.post(StaticClass.online_now_amount, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            parsingNowAmount(t);
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            Log.e(StaticClass.TAG, errorNo + " " + strMsg);
                        }
                    });
                } else {
                    Toast.makeText(this, "在线签到为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parsingNowAmount(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONObject data = result.getJSONObject("data");
                int nowAmount = data.getInt("nowAmount");
                int amount = ShareUtils.getInt(this,"amount", 0);
                tv_num.setText("应到 " + nowAmount + "/"+ amount +" 实到");
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingEnd(String t) {
        try {
            JSONObject json = new JSONObject(t);
            if (json.getInt("code") == 0) {
                String lessonId = ShareUtils.getString(this, "lessonId", "");
                new RxVolley.Builder().url(StaticClass.online_sign_delete + lessonId)
                        .httpMethod(RxVolley.Method.DELETE)
                        .callback(new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingDelete(t);
                            }
                        }).doTask();
            } else {
                Toast.makeText(OnlineSignActivity.this,
                        json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingDelete(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                Toast.makeText(this, "在线签到已关闭", Toast.LENGTH_SHORT).show();
                ShareUtils.deleShare(this, "amount");
                ShareUtils.deleShare(this, "lessonId");
                ShareUtils.deleShare(this, "isShow");
                isShowView(false);
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parsingStart(String t) {
        try {
            JSONObject json = new JSONObject(t);
            if (json.getInt("code") == 0) {
                Toast.makeText(OnlineSignActivity.this,
                        "签到成功开启", Toast.LENGTH_SHORT).show();
                RxVolley.get(StaticClass.online_amount + value, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.i(StaticClass.TAG, t);
                        parsingAmount(t);
                    }
                });
                ShareUtils.putString(this, "lessonId", value);
                ShareUtils.putBoolean(this, "isShow", true);
                isShowView(true);
            } else {
                Toast.makeText(OnlineSignActivity.this,
                        json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingAmount(String t) {
        try {
            JSONObject json = new JSONObject(t);
            if (json.getInt("code") == 0) {
                JSONObject data = json.getJSONObject("data");
                ShareUtils.putInt(this, "amount", data.getInt("amount"));
                tv_num.setText("应到 0/"+ data.getInt("amount") +" 实到");
            } else {
                Toast.makeText(OnlineSignActivity.this,
                        json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            //移动到我的位置
            //设置缩放，确保屏幕内有我
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
            mBaiduMap.setMapStatus(mapStatusUpdate);

            //开始移动
            MapStatusUpdate mapLatLng = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            mBaiduMap.setMapStatus(mapLatLng);

            //绘制图层
            //定义Marker坐标点
            LatLng point = new LatLng(latitude, longitude);
            lat = latitude;
            lon = longitude;
            //Log.i(StaticClass.TAG, lat + " " + lon);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ip);
            mBaiduMap.clear();
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            //在地图上添加Marker,并展示
            mBaiduMap.addOverlay(option);
        }
    }

}
