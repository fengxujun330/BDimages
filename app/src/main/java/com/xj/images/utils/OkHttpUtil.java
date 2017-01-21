package com.xj.images.utils;

import okhttp3.OkHttpClient;

/**
 * Created by ajun on 2017/1/21.
 */

public class OkHttpUtil {
    private static OkHttpUtil INSTANCE;

    private OkHttpClient mOkHttpClient;
    private OkHttpUtil(){
        mOkHttpClient = new OkHttpClient();
    }

    public synchronized static OkHttpUtil getInstance(){
        if(null == INSTANCE){
            synchronized (OkHttpUtil.class){
                if(null == INSTANCE){
                    INSTANCE = new OkHttpUtil();
                }
            }
        }
        return INSTANCE;
    }

    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }
}
