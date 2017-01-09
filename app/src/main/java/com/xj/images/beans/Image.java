package com.xj.images.beans;

/**
 * Created by ajun on 2017/1/7.
 */

public class Image {
    private String thumbURL;
    private String middleURL;
    private String hoverURL;

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public void setMiddleURL(String middleURL) {
        this.middleURL = middleURL;
    }

    public void setHoverURL(String hoverURL) {
        this.hoverURL = hoverURL;
    }

    public String getMiddleURL() {

        return middleURL;
    }

    @Override
    public String toString() {
        return "Image{" +
                "thumbURL='" + thumbURL + '\'' +
                ", middleURL='" + middleURL + '\'' +
                ", hoverURL='" + hoverURL + '\'' +
                '}';
    }

    public String getHoverURL() {
        return hoverURL;
    }

    public String getThumbURL() {

        return thumbURL;
    }
}
