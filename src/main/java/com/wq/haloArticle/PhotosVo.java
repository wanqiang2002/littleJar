package com.wq.haloArticle;

import java.io.Serializable;

public class PhotosVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String thumbnail;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
