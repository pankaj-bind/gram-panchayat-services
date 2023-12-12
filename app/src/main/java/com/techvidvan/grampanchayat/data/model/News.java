package com.techvidvan.grampanchayat.data.model;

public class News {

    private String newsId;
    private String title;
    private String description;
    private String urlToImage;
    private String date;

    public News() {
    }

    public News(String newsId, String title, String description, String urlToImage, String date) {
        this.newsId = newsId;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.date = date;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

