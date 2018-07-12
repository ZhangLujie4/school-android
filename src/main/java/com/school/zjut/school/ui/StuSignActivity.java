package com.school.zjut.school.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.baidu.mapapi.utils.DistanceUtil;
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
 * 文件名:   StuSignActivity
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/19 20:16
 * 描述:    学生签到
 */

public class StuSignActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_lesson_detail;
    private Button btn_sign;
    private int distance;
    private String lessonNum;
    private String lessonId;
    private LatLng aim;
    private double nowDistance;
//    private Button btn_fresh;

    //定位
    private MapView bmapView;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_sign);

        initView();
    }

    private void initView() {
        tv_lesson_detail = (TextView) findViewById(R.id.tv_lesson_detail);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(this);
        bmapView  = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
//        btn_fresh = (Button) findViewById(R.id.btn_fresh);
//        btn_fresh.setOnClickListener(this);

        String classId = ShareUtils.getString(this, "classId", "");
        new RxVolley.Builder()
                .url(StaticClass.student_online_sign + classId)
                .httpMethod(RxVolley.Method.GET)
                .shouldCache(false)    //这个非常的关键，默认是缓存的
                .callback(new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.i(StaticClass.TAG, t);
                        parsingLesson(t);
                    }
                }).encoding("UTF-8")
                .doTask();
    }

    private void initMap() {
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

    private void parsingLesson(String t) {
        try {
            JSONObject result = new JSONObject(t);
            if (result.getInt("code") == 0) {
                JSONObject data = result.getJSONObject("data");
                tv_lesson_detail.setText(data.getString("teaId") + " "
                        + data.getString("lessonName") + " 星期"
                        + data.getInt("lessonWeekday")
                        + " " + data.getString("lessonPlace"));
                double teaLon = data.getDouble("teaLon");
                double teaLat = data.getDouble("teaLat");
                lessonId = data.getString("lessonId");
                lessonNum = data.getString("lessonNum");
                distance = data.getInt("distance");
                aim = new LatLng(teaLat, teaLon);
                btn_sign.setVisibility(View.VISIBLE);
                bmapView.setVisibility(View.VISIBLE);
                initMap();
            } else {
                Toast.makeText(this, result.getString("msg"), Toast.LENGTH_SHORT).show();
                tv_lesson_detail.setText("暂无");
                btn_sign.setVisibility(View.GONE);
                bmapView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                final double dis = nowDistance;
                if (dis <= distance) {
                    HttpParams params = new HttpParams();
                    params.put("lessonId", lessonId);
                    params.put("lessonNum", lessonNum);
                    params.put("stuId", ShareUtils.getString(this, "id", ""));
                    params.put("stuName", ShareUtils.getString(this, "name", ""));
                    RxVolley.post(StaticClass.student_sign_create, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.i(StaticClass.TAG, t);
                            parsingJson(t, dis);
                        }
                    });
                } else {
                    Toast.makeText(this, "距离目标点"+nowDistance+"米,大于"+distance+"米",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parsingJson(String t, double dis) {
        try {
            JSONObject json = new JSONObject(t);
            if (json.getInt("code") == 0) {
                Toast.makeText(this, "签到成功,距签到点"+dis+"米", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                btn_sign.setVisibility(View.GONE);
                tv_lesson_detail.setText("暂无");
                bmapView.setVisibility(View.GONE);
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

            Log.i(StaticClass.TAG, latitude + " " + longitude);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ip);
            nowDistance = DistanceUtil.getDistance(point, aim);
            mBaiduMap.clear();
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            OverlayOptions option1 = new MarkerOptions().position(aim).icon(bitmap);
            //在地图上添加Marker,并展示
            mBaiduMap.addOverlay(option);
            mBaiduMap.addOverlay(option1);
        }
    }
}
