package com.xj.images.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static String useGetMethodGetData(String url){
        OkHttpClient client = OkHttpUtil.getInstance().getOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
        return null;
    }
}
