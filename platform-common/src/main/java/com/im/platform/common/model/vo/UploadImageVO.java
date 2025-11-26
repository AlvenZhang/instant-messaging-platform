package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 图片上传VO
 */
public class UploadImageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原图
     */
    private String originUrl;

    /**
     * 缩略图
     */
    private String thumbUrl;

    public UploadImageVO() {
    }

    public UploadImageVO(String originUrl, String thumbUrl) {
        this.originUrl = originUrl;
        this.thumbUrl = thumbUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return "UploadImageVO{" +
                "originUrl='" + originUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }
}

