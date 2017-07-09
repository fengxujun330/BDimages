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
 * Created by ajun on 2017/1/22.
 */

public class SoDataSource implements DataSource {
    private static final String URL_PRE = "http://image.so.com/j?";

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
            JSONArray datas = root.getJSONArray("list");
            int lenght = datas.length();
            if(lenght > 1){
                for(int i = 0; i < lenght; i++){
                    JSONObject imageJO = datas.optJSONObject(i);
                    String imageURL = imageJO.optString("img");
                    String imageThumbURL = imageJO.optString("thumb");
                    String imageThumbBakURL = imageJO.optString("thumb_bak");
                    int height = imageJO.optInt("height");
                    int width = imageJO.optInt("width");
                    Image image = new Image();
                    image.setImageURL(imageURL);
                    image.setImageThumbURL(imageThumbURL);
                    image.setImageThumbBakURL(imageThumbBakURL);
                    image.setFrom("来自360搜索");
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

    private static String getURL(String queryWord, int pageNumber){
        return getURL(queryWord,pageNumber,-1);
    }

    //从0-4分别代表：全尺寸、大尺寸、中尺寸、小尺寸、壁纸尺寸
    private static String getURL(String queryWord, int pageNumber,int zoom){

        return getURL(queryWord, pageNumber, zoom, -1, -1);
    }

    private static String getURL(String queryWord, int pageNumber,int zoom, int height, int width){
        StringBuilder sb = new StringBuilder(URL_PRE);
        sb//.append("correct=").append(URLEncoder.encode(queryWord))
                .append("q=").append(URLEncoder.encode(queryWord))
                .append("&pn=").append(PUR_PAGE_NUMBER)
                .append("&sn=").append(pageNumber * PUR_PAGE_NUMBER)
                .append("&src=").append("tab_www");

        //从0-4分别代表：全尺寸、大尺寸、中尺寸、小尺寸、壁纸尺寸
        if(zoom == 0 || zoom == 1 || zoom == 2 || zoom == 3 || zoom == 4){
            sb.append("&zoom=").append(zoom);
        }

        /*if(0 >= height || 0 >= width){
            height = DEFAULT_HEIGHT;
            width = DEFAULT_WIDTH;
        }*/
        if(height > 0 && width > 0){
            sb.append("&height=").append(height)
                    .append("&width=").append(width);
        }
        Log.i("alanMms", sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        DataSource dataSource = new SoDataSource();
        System.out.println(getURL("girl",2));
        System.out.print(dataSource.getData("girl",2));
    }
}
