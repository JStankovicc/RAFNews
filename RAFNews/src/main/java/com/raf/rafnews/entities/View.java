package com.raf.rafnews.entities;

import java.time.LocalDate;

public class View {
    private int id;
    private int newsId;
    private String viewerIp;
    private String createdAt;

    public View() {}

    public View(int newsId, String viewerIp) {
        this.newsId = newsId;
        this.viewerIp = viewerIp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getViewerIp() {
        return viewerIp;
    }

    public void setViewerIp(String viewerIp) {
        this.viewerIp = viewerIp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
