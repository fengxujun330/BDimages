package com.xj.images.presenter;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xj.images.ImageApp;
import com.xj.images.beans.Image;
import com.xj.images.data.BaiduDataSourceImpl;
import com.xj.images.data.DataSource;
import com.xj.images.data.SoDataSource;
import com.xj.images.data.SogouDataSource;
import com.xj.images.view.ViewInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
    private volatile boolean mEnd;

    private volatile boolean mLoading = false;
    public DefaultPresenterImpl(@NonNull ViewInterface viewInterface){
        this.mViewInterface = viewInterface;
//        this.mDataSource = new BaiduDataSourceImpl();
        this.mDataSource = new SoDataSource();
        mBaidu = new SogouDataSource();
        mSo = new SoDataSource();
    }

    private ExecutorService mPool = Executors.newSingleThreadExecutor();
    private DataSource mBaidu;
    private SoDataSource mSo;
    /*@Override
    public void loadIamges() {
        if(mEnd){
            Toast.makeText(ImageApp.getInstance(),"只有这些图片了，亲，试试其他关键字？？？", Toast.LENGTH_LONG).show();
            return;
        }
        if(mLoading){
            Log.i("alanF", "loading:" + mCurrentPage+" return");
            Toast.makeText(ImageApp.getInstance(),"正在加载" + mCurrentPage + "页数据，请稍后", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("alanF", "will load:" + mCurrentPage);
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
                            List<Image> images = mDataSource.getImages(s);
                            if(null != images && 0 < images.size()){
                                if(mDataSource.getPurPageNumber() > images.size()){
                                    mEnd = true;
                                }else {
                                    mCurrentPage++;
                                }
                                mViewInterface.addImages(images);
                                mViewInterface.notifyDataChanged();
                            }else {
                                Toast.makeText(ImageApp.getInstance(),"没有加载出第"+ mCurrentPage+"页数据",Toast.LENGTH_SHORT).show();
                            }
                            mLoading = false;
                        }catch (Exception exception){
                            mLoading = false;
                            Toast.makeText(ImageApp.getInstance(),"加载第"+ mCurrentPage+"页数据失败",Toast.LENGTH_SHORT).show();
                            Log.i("alanF", "", exception);
                        }
                    }
                });
    }*/

    private Object mLock = new Object();
    private boolean mBaiduEnd = false;
    private boolean mSoEnd = false;
    @Override
    public void loadIamges() {
        if(TextUtils.isEmpty(mCurrentKeyWord)){
            return;
        }
        if(mBaiduEnd && mSoEnd){
            Toast.makeText(ImageApp.getInstance(),"只有这些图片了，亲，试试其他关键字？？？", Toast.LENGTH_LONG).show();
            return;
        }
        if(mLoading){
            Log.i("alanF", "loading:" + mCurrentPage+" return");
            Toast.makeText(ImageApp.getInstance(),"正在加载" + mCurrentPage + "页数据，请稍后", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("alanF", "will load:" + mCurrentPage);
        mLoading = true;

        mPool.submit(new Runnable() {
            @Override
            public void run() {
                String aJson = mBaidu.getData(mCurrentKeyWord, mCurrentPage);
                String bJson = mSo.getData(mCurrentKeyWord, mCurrentPage);
                List<Image> allImages = new ArrayList<Image>();
                if(!mBaiduEnd && !TextUtils.isEmpty(aJson)){
                    List<Image> aImages = mBaidu.getImages(aJson);
                    if(aImages.size() < mBaidu.getPurPageNumber()){
                        mBaiduEnd = true;
                    }
                    allImages.addAll(aImages);
                }
                if(!mSoEnd && !TextUtils.isEmpty(bJson)){
                    List<Image> aImages = mSo.getImages(bJson);
                    if(aImages.size() < mSo.getPurPageNumber()){
                        mSoEnd = true;
                    }
                    allImages.addAll(aImages);
                }
                if(!mBaiduEnd || !mSoEnd){
                    mCurrentPage++;
                }
                mLoading = false;
                mViewInterface.addImages(allImages);
                mViewInterface.notifyDataChanged();
            }
        });
    }

    @Override
    public void setQueryKeyWord(String keyWord) {
        if(!TextUtils.equals(keyWord, mCurrentKeyWord)){
            mCurrentKeyWord = keyWord;
            mLoading = false;
            mCurrentPage = 1;
            mViewInterface.clearAllDatas();
            mViewInterface.notifyDataChanged();
            mEnd = false;
            mBaiduEnd = false;
            mSoEnd = false;
        }
    }
}
