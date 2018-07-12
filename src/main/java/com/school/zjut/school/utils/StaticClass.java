package com.school.zjut.school.utils;

/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.utils
 * 文件名:   StaticClass
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/17 21:30
 * 描述:    TODO
 */

public interface StaticClass {

    String TAG = "ZLJLog";

    String BUGLY_APP_ID = "2c8c1f6f27";

    String url = "http://47.95.215.194";

    String teacher_login = "http://47.95.215.194/api/zjut/teacher/login";

    String student_login = "http://47.95.215.194/api/zjut/student/login";

    String all_news = "http://47.95.215.194/api/zjut/news/all";

    String news_detail = "http://47.95.215.194/api/zjut/news/detail/";

    String teacher_change_pass = "http://47.95.215.194/api/zjut/teacher/password";

    String student_change_pass = "http://47.95.215.194/api/zjut/student/password";

    String student_detail = "http://47.95.215.194/api/zjut/student/detail/";

    String student_class_teacher = "http://47.95.215.194/api/zjut/teacher/information/";

    String student_institute = "http://47.95.215.194/api/zjut/institute/detail/";

    String teacher_detail = "http://47.95.215.194/api/zjut/teacher/detail/";

    String teacher_institute = "http://47.95.215.194/api/zjut/institute/instruction/";

    String teacher_lesson_list = "http://47.95.215.194/api/zjut/lesson/teaList/";

    String online_sign_start = "http://47.95.215.194/api/zjut/signIn/online";

    String online_sign_end = "http://47.95.215.194/api/zjut/signIn/outline";

    String online_amount = "http://47.95.215.194/api/zjut/lesson/amount/";

    String online_now_amount = "http://47.95.215.194/api/zjut/signIn/nowAmount";

    String online_sign_delete = "http://47.95.215.194/api/zjut/signIn/delete/";

    String student_online_sign = "http://47.95.215.194/api/zjut/signIn/available/";

    String student_sign_create = "http://47.95.215.194/api/zjut/signIn/stuCreate";
}
