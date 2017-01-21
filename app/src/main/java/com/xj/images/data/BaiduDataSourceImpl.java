package com.xj.images.data;

import com.xj.images.utils.OkHttpUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ajun on 2017/1/21.
 */

public class BaiduDataSourceImpl implements DataSource {
    private static final String URL_PRE = "http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=0&ic=0&s=&se=&tab=&face=0&istype=2&qc=&nc=1&fr=&gsm=5a&1483760694304=";

    private final static int PUR_PAGE_NUMBER = 30;
    @Override
    public String getData(String keyWord, int pageNumber) {
        OkHttpClient client = OkHttpUtil.getInstance().getOkHttpClient();
        Request request = new Request.Builder()
                .url(getURL(keyWord, pageNumber))
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

    private static String getURL(String queryWord, int pageNumber){
        return getURL(queryWord,pageNumber,-1,-1);
    }
    private static String getURL(String queryWord, int pageNumber,int width, int height){
        StringBuilder sb = new StringBuilder(URL_PRE);
        sb.append("&queryWord=").append(URLEncoder.encode(queryWord))
                .append("&word=").append(URLEncoder.encode(queryWord))
                .append("&pn=").append(pageNumber * PUR_PAGE_NUMBER)
                .append("&rn=").append(PUR_PAGE_NUMBER);

        if(0 >= width || 0 >= height){
            sb.append("&width=").append("1920");
            sb.append("&height=").append("1080");
        }else{
            sb.append("&width=").append(width);
            sb.append("&height=").append(height);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        DataSource dataSource = new BaiduDataSourceImpl();
        String data = dataSource.getData("美女",3);
        System.out.print(data);
    }
}
