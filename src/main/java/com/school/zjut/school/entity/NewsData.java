package com.school.zjut.school.entity;


/**
 * 项目名:   School
 * 包名:     com.school.zjut.school.entity
 * 文件名:   NewsData
 * 创建者:   zhanglujie
 * 创建时间: 2018/6/18 1:38
 * 描述:    新闻类
 */

public class NewsData {
    private int newsId;

    private String newsTitle;

    private String createTime;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
