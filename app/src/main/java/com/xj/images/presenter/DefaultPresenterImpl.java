package com.xj.images.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.xj.images.beans.Image;
import com.xj.images.data.BaiduDataSourceImpl;
import com.xj.images.data.DataSource;
import com.xj.images.view.ViewInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.facebook.common.internal.Preconditions.checkNotNull;

/**
 * Created by ajun on 2017/1/21.
 */

public class DefaultPresenterImpl implements Presenter {

    private ViewInterface mViewInterface;
    private DataSource mDataSource;
    private int mCurrentPage = 1;
    private String mCurrentKeyWord;

    public DefaultPresenterImpl(ViewInterface viewInterface){
        this.mViewInterface = checkNotNull(viewInterface);
        this.mDataSource = new BaiduDataSourceImpl();
    }
    @Override
    public void loadIamges() {
        mCurrentKeyWord = checkNotNull(mCurrentKeyWord);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i("alanMms", Thread.currentThread().getName());
                String images = mDataSource.getData(mCurrentKeyWord, mCurrentPage);
                if(TextUtils.isEmpty(images)){
                    return;
                }
//                Log.i("alanMms", "iamges:" + images);
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
                                    String thumbURL = imageJO.optString("thumbURL");
                                    String middleURL = imageJO.optString("middleURL");
                                    String hoverURL = imageJO.optString("hoverURL");
                                    Image image = new Image();
                                    image.setThumbURL(thumbURL);
                                    image.setHoverURL(hoverURL);
                                    image.setMiddleURL(middleURL);
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
                });

    }

    @Override
    public void setQueryKeyWord(String keyWord) {
        checkNotNull(keyWord);
        if(!TextUtils.equals(keyWord, mCurrentKeyWord)){
            mCurrentKeyWord = keyWord;
            mViewInterface.clearAllDatas();
            mViewInterface.notifyDataChanged();
        }
    }
}
