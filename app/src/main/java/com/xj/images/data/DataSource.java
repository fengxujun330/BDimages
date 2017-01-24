package com.xj.images.data;

/**
 * Created by ajun on 2017/1/21.
 */

public interface DataSource {
    public static final int DEFAULT_HEIGHT = 1920;
    public static final int DEFAULT_WIDTH = 1080;
    public String getData(String keyWord, int pageNumber);
}
