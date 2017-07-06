package com.xj.images.data;

import android.util.Log;

import com.xj.images.beans.Image;
import com.xj.images.utils.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajun on 2017/1/21.
 */

public class BaiduDataSourceImpl implements DataSource {
    private static final String URL_PRE = "http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=0&ic=0&s=&se=&tab=&face=0&istype=2&qc=&nc=1&fr=&gsm=5a&1483760694304=";

    private final static int PUR_PAGE_NUMBER = 30;
    @Override
    public String getData(String keyWord, int pageNumber) {
        return OkHttpUtil.useGetMethodGetData(getURL(keyWord,pageNumber));
    }

    @Override
    public List<Image> getImages(String imagesJson) {
        ArrayList<Image> images = new ArrayList<Image>();
        try {
            JSONObject root = new JSONObject(imagesJson);
            JSONArray datas = root.getJSONArray("data");
            int lenght = datas.length();
            if(lenght > 1){
                for(int i = 0; i < lenght - 1; i++){
                    JSONObject imageJO = datas.optJSONObject(i);
                    String imageURL = imageJO.optString("thumbURL");
                    String imageThumbURL = imageJO.optString("hoverURL");
                    String imageThumbBakURL = imageJO.optString("middleURL");
                    Image image = new Image();
                    image.setImageURL(imageURL);
                    image.setImageThumbURL(imageThumbURL);
                    image.setImageThumbBakURL(imageThumbBakURL);
                    image.setFrom("来自百度搜索");
                    images.add(image);
                }
            }
        }catch (Exception exception){
            Log.e("alanMms", "", exception);
        }
        return images;
    }

    @Override
    public int getPurPageNumber() {
        return PUR_PAGE_NUMBER;
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
            sb.append("&width=").append(DEFAULT_WIDTH);
            sb.append("&height=").append(DEFAULT_HEIGHT);
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
