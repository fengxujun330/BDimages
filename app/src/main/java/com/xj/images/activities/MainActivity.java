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
import okhttp3.OkHttpClient;

import static com.facebook.common.internal.Preconditions.checkNotNull;

public class MainActivity extends Activity implements ViewInterface{

    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.id_search_edittext) EditText mSearchEditText;
//    private static final String BAIDU_SEARCH_IMAGES_URL = "http://image.baidu.com/search/acjson";
//    private static final String URL_PRE = "http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=0&ic=0&s=&se=&tab=&face=0&istype=2&qc=&nc=1&fr=&gsm=5a&1483760694304=";
    private OkHttpClient mHttpClient;

//    private static final int default_width = 1920;
//    private static final int default_height = 1080;
    private ArrayList<Image> mImages = new ArrayList<Image>();

    private ImageRecyclerAdapter mAdapter;
//    private int mCurrentPage = 1;
//    private static final int per_page_count = 30;
//    private String mCurrentQueryWords;

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
                        if(mImages.size() - 1 == pos[pos.length -1]) {
//                            loadImages(mCurrentQueryWords);
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

    /*private String getUrl(String queryWord, int pageNumber,int width, int height){
        StringBuilder sb = new StringBuilder(URL_PRE);
        sb.append("&queryWord=").append(URLEncoder.encode(queryWord))
                .append("&word=").append(URLEncoder.encode(queryWord))
                .append("&width=").append("")
                .append("&height=").append("")
                .append("&pn=").append(pageNumber)
                .append("&rn=").append(30);

        return sb.toString();
    }
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.id_search_btn:
//                search();
                String queryWord = mSearchEditText.getText().toString();
                mCurrentQueryWords = queryWord;
                mPresenter.loadIamges(queryWord);
                break;
        }
    }*/

    /*private void search() {

        String queryWord = mSearchEditText.getText().toString();
        if(isValidQueryWords(queryWord)){
            mImages.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(),0);
                }
            });
        }else {
//            Toast.makeText(this, R.string.no_query_words, Toast.LENGTH_SHORT).show();
            Toast.makeText(this,R.string.do_nothing , Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.loadIamges(queryWord);
        mCurrentQueryWords = queryWord;
        loadImages(mCurrentQueryWords);
    }

    private boolean isValidQueryWords(String words){
        boolean result = true;
        if(TextUtils.isEmpty(words) || TextUtils.equals(words,mCurrentQueryWords)){
            result = false;
        }
        return result;
    }
    private volatile boolean isLoading = false;
    private void loadImages(String queryWord) {
        if(isLoading){
            return;
        }

        String url = getUrl(queryWord,per_page_count * mCurrentPage,default_width,default_height);
        if (null == mHttpClient){
            mHttpClient = new OkHttpClient();
        }
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isLoading = false;
                    Response response = mHttpClient.newCall(request).execute();
                    if (response.isSuccessful()){
                        JSONObject root = new JSONObject(response.body().string());
                        JSONArray datas = root.getJSONArray("data");
                        int lenght = datas.length();
                        if(lenght > 1){
                            for(int i = 0; i < lenght - 1; i++){
                                JSONObject imageJO = datas.optJSONObject(i);
                                String thumbURL = imageJO.optString("thumbURL");
                                String middleURL = imageJO.optString("middleURL");
                                String hoverURL = imageJO.optString("hoverURL");
                                Image image = new Image();
                                image.setThumbURL(thumbURL);
                                image.setHoverURL(hoverURL);
                                image.setMiddleURL(middleURL);
                                mImages.add(image);
                            }
                            mCurrentPage++;
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    isLoading = false;
                                }
                            });
                        }
                    }else {
                        Log.i("alanMms", "failed");
                    }
                } catch (Exception e) {
                    Log.i("alanMms", "error");
                }finally {
                    isLoading = false;
                }
            }
        }).start();
    }*/

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
        checkNotNull(image);
        mImages.add(image);
    }

    @Override
    public void addImages(@NonNull List<Image> images) {
        checkNotNull(images);
        mImages.addAll(images);
    }

    @Override
    public void clearAllDatas() {
        mImages.clear();
    }
}
