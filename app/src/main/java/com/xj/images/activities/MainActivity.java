package com.xj.images.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;

import com.xj.images.R;
import com.xj.images.adapters.ImageRecyclerAdapter;
import com.xj.images.beans.Image;
import com.xj.images.presenter.DefaultPresenterImpl;
import com.xj.images.presenter.Presenter;
import com.xj.images.view.ViewInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity implements ViewInterface{

    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.id_search_edittext) EditText mSearchEditText;

    private ArrayList<Image> mImages = new ArrayList<Image>();

    private ImageRecyclerAdapter mAdapter;

    private StaggeredGridLayoutManager mLayoutManager;
    private Presenter mPresenter;

    @OnClick(R.id.id_search_btn) void search(){
        String keyWord = mSearchEditText.getText().toString();
        if(TextUtils.isEmpty(keyWord)){
            return;
        }
        mPresenter.setQueryKeyWord(keyWord);
        mPresenter.loadIamges();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageRecyclerAdapter(this,mImages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING){
                    int pos[] = mLayoutManager.findLastVisibleItemPositions(null);
                    if (null != pos && 0 < pos.length){
                        /*Log.i("alanMms", "images:" + mImages.size());
                        Log.i("alanMms", (mImages.size() - 1) + "====" + pos[pos.length -1]);*/
                        if((mImages.size() - 1) - pos[pos.length -1] < 2) {
                            mPresenter.loadIamges();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        if(null == mPresenter){
            mPresenter = new DefaultPresenterImpl(this);
        }

    }

    @Override
    public void notifyDataChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void addImage(Image image) {
        mImages.add(image);
    }

    @Override
    public void addImages(@NonNull List<Image> images) {
        mImages.addAll(images);
    }

    @Override
    public void clearAllDatas() {
        mImages.clear();
    }
}
