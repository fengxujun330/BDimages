package com.xj.images.presenter;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xj.images.ImageApp;
import com.xj.images.beans.Image;
import com.xj.images.data.DataSource;
import com.xj.images.data.SoDataSource;
import com.xj.images.view.ViewInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by ajun on 2017/1/21.
 */

public class DefaultPresenterImpl implements Presenter {

    private ViewInterface mViewInterface;
    private DataSource mDataSource;
    private int mCurrentPage = 1;
    private String mCurrentKeyWord;

    private volatile boolean mLoading = false;
    public DefaultPresenterImpl(@NonNull ViewInterface viewInterface){
        this.mViewInterface = viewInterface;
//        this.mDataSource = new BaiduDataSourceImpl();
        this.mDataSource = new SoDataSource();
    }
    @Override
    public void loadIamges() {
        if(mLoading){
            Log.i("alanMms", "loading:" + mCurrentPage+" return");
            Toast.makeText(ImageApp.getInstance(),"正在加载" + mCurrentPage + "页数据，请稍后", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("alanMms", "will load:" + mCurrentPage);
        mLoading = true;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String images = mDataSource.getData(mCurrentKeyWord, mCurrentPage);
                if(TextUtils.isEmpty(images)){
                    mLoading = false;
                    return;
                }
                subscriber.onNext(images);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if(!mLoading){
                                return;
                            }
                            JSONObject root = new JSONObject(s);
                            JSONArray datas = root.getJSONArray("list");
                            int lenght = datas.length();
                            if(lenght > 1){
                                ArrayList<Image> images = new ArrayList<Image>();
                                for(int i = 0; i < lenght; i++){
                                    JSONObject imageJO = datas.optJSONObject(i);
                                    String imageURL = imageJO.optString("img");
                                    String imageThumbURL = imageJO.optString("thumb");
                                    String imageThumbBakURL = imageJO.optString("thumb_bak");
                                    Image image = new Image();
                                    image.setImageURL(imageURL);
                                    image.setImageThumbURL(imageThumbURL);
                                    image.setImageThumbBakURL(imageThumbBakURL);
                                    images.add(image);
                                }
                                mCurrentPage++;
                                mViewInterface.addImages(images);
                                mViewInterface.notifyDataChanged();
                            }else {
                                Toast.makeText(ImageApp.getInstance(),"没有加载出第"+ mCurrentPage+"页数据",Toast.LENGTH_SHORT).show();
                            }
                            mLoading = false;
                        }catch (Exception exception){
                            mLoading = false;
                            Toast.makeText(ImageApp.getInstance(),"加载第"+ mCurrentPage+"页数据失败",Toast.LENGTH_SHORT).show();
                            Log.i("alanMms", "", exception);
                        }
                    }
                });
        /*Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String images = mDataSource.getData(mCurrentKeyWord, mCurrentPage);
                if(TextUtils.isEmpty(images)){
                    return;
                }
                subscriber.onNext(images);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject root = new JSONObject(s);
                            JSONArray datas = root.getJSONArray("data");
                            int lenght = datas.length();
                            if(lenght > 1){
                                ArrayList<Image> images = new ArrayList<Image>();
                                for(int i = 0; i < lenght - 1; i++){
                                    JSONObject imageJO = datas.optJSONObject(i);
                                    String imageURL = imageJO.optString("thumbURL");
                                    String imageThumbURL = imageJO.optString("hoverURL");
                                    String imageThumbBakURL = imageJO.optString("middleURL");
                                    Image image = new Image();
                                    image.setImageURL(imageURL);
                                    image.setImageThumbURL(imageThumbURL);
                                    image.setImageThumbBakURL(imageThumbBakURL);
                                    images.add(image);
                                }
                                mCurrentPage++;
                                mViewInterface.addImages(images);
                                mViewInterface.notifyDataChanged();
                            }
                        }catch (Exception exception){
                            Log.i("alanMms", "", exception);
                        }
                    }
                });*/

    }

    @Override
    public void setQueryKeyWord(String keyWord) {
        if(!TextUtils.equals(keyWord, mCurrentKeyWord)){
            mCurrentKeyWord = keyWord;
            mLoading = false;
            mCurrentPage = 1;
            mViewInterface.clearAllDatas();
            mViewInterface.notifyDataChanged();
        }
    }
}
