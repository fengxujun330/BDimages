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
 * Created by alanfeng on 2017/7/5 0005.
 */

public class SogouDataSource implements DataSource {
    private static final String URL_PRE = "http://pic.sogou.com/pics?";
    private final static int PUR_PAGE_NUMBER = 48;
    @Override
    public String getData(String keyWord, int pageNumber) {
        return OkHttpUtil.useGetMethodGetData(getURL(keyWord,pageNumber));
    }

    private static String getURL(String queryWord, int pageNumber){
        return getURL(queryWord,pageNumber,-1);
    }
    private static String getURL(String queryWord, int pageNumber,int zoom){

        return getURL(queryWord, pageNumber, zoom, -1, -1);
    }

    private static String getURL(String queryWord, int pageNumber,int zoom, int height, int width){
        StringBuilder sb = new StringBuilder(URL_PRE);
        sb.append("query=").append(URLEncoder.encode(queryWord))
            .append("&reqType=").append("ajax")
            .append("&start=").append((pageNumber -1)* PUR_PAGE_NUMBER);

        /*if(0 >= height || 0 >= width){
            height = DEFAULT_HEIGHT;
            width = DEFAULT_WIDTH;
        }*/
        if(height > 0 && width > 0) {
            sb.append("&cheight=").append(height)
                    .append("&cwidth=").append(width);
        }
        Log.i("alanMms", sb.toString());
        return sb.toString();
    }

    @Override
    public List<Image> getImages(String imagesJson) {
        ArrayList<Image> images = new ArrayList<Image>();
        try {
            JSONObject root = new JSONObject(imagesJson);
            JSONArray datas = root.getJSONArray("items");
            int lenght = datas.length();
            if(lenght > 1){
                for(int i = 0; i < lenght; i++){
                    JSONObject imageJO = datas.optJSONObject(i);
                    String imageURL = imageJO.optString("pic_url");
                    String imageThumbURL = imageJO.optString("pic_url_noredirect");
                    String imageThumbBakURL = imageJO.optString("thumbUrl");
                    int height = imageJO.optInt("height");
                    int width = imageJO.optInt("width");
                    Image image = new Image();
                    image.setImageURL(imageURL);
                    image.setImageThumbURL(imageThumbURL);
                    image.setImageThumbBakURL(imageThumbBakURL);
                    image.setFrom("来自搜狗搜索");
                    image.setWidth(width);
                    image.setHeight(height);
                    images.add(image);
                }
            }
        }catch (Exception exception){
            Log.i("alanMms", "", exception);
        }
        return images;
    }

    @Override
    public int getPurPageNumber() {
        return PUR_PAGE_NUMBER;
    }

    public static void main(String[] args) {
        DataSource dataSource = new SogouDataSource();
        System.out.println(getURL("美女",1));
        System.out.print(dataSource.getData("美女",1));
    }
}
