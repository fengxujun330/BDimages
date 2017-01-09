package com.xj.images.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.images.R;
import com.xj.images.adapters.ImageRecyclerAdapter;
import com.xj.images.beans.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mSearchEditText;
    private static final String BAIDU_SEARCH_IMAGES_URL = "http://image.baidu.com/search/acjson";
    private static final String URL_PRE = "http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=0&ic=0&s=&se=&tab=&face=0&istype=2&qc=&nc=1&fr=&gsm=5a&1483760694304=";
    private OkHttpClient mHttpClient;

    private static final int default_width = 1920;
    private static final int default_height = 1080;
    private ArrayList<Image> mImages = new ArrayList<Image>();

//    private ListView mListView;
//    private ImageAdapter mAdapter;
    private ImageRecyclerAdapter mAdapter;
    private int mCurrentPage = 1;
    private static final int per_page_count = 30;
    private String mCurrentQueryWords;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchEditText = (TextView) findViewById(R.id.id_search_edittext);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageRecyclerAdapter(this,mImages);
        mRecyclerView.setAdapter(mAdapter);
        /*mListView = (ListView) findViewById(R.id.my_list);
        mAdapter = new ImageAdapter(this, mImages);
        mListView.setAdapter(mAdapter);
        Button footer = (Button) LayoutInflater.from(this).inflate(R.layout.list_view_footer,null, false);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImages(mCurrentQueryWords);
            }
        });
        mListView.addFooterView(footer);*/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING){
                    int pos[] = mLayoutManager.findLastVisibleItemPositions(null);
                    if (null != pos && 0 < pos.length){
                        if(mImages.size() - 1 == pos[pos.length -1]) {
                            loadImages(mCurrentQueryWords);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private String getUrl(String queryWord, int pageNumber,int width, int height){
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
                search();
                break;
        }
    }

    private void search() {
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
                /*.addHeader("ipn","rj")
                .addHeader("ct","201326592")
                .addHeader("is", "")
                .addHeader("fp","result")
                .addHeader("queryWord",queryWord)
                .addHeader("cl","2")
                .addHeader("lm","-1")
                .addHeader("ie","utf-8")
                .addHeader("oe","utf-8")
                .addHeader("adpicid","")
                .addHeader("st","-1")
                .addHeader("z","")
                .addHeader("ic","0")
                .addHeader("word",queryWord)
                .addHeader("s","")
                .addHeader("se","")
                .addHeader("tab","")
                .addHeader("width","1080")
                .addHeader("height","1920")
                .addHeader("face","0")
                .addHeader("istype","2")
                .addHeader("qc","")
                .addHeader("nc","1")
                .addHeader("fr","")
                .addHeader("cg","girl")
                .addHeader("pn","30") //page number
                .addHeader("rn","30") //r number
                .addHeader("gsm","1e00000000001e")*/
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
                        Log.i("alanMms", "lenght:" + lenght);
                        if(lenght > 1){
//                            mImages.clear();
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
                                /*Log.i("alanMms", "thumbURL:" + thumbURL);
                                Log.i("alanMms", "middleURL:" + middleURL);
                                Log.i("alanMms", "hoverURL:" + hoverURL);
                                Log.i("alanMms", "---------------------------------------------" + i);*/
                            }
                            mCurrentPage++;
                            /*mListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("alanMms", "datas:" + mImages.size());
                                    mAdapter.notifyDataSetChanged();
                                }
                            });*/

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
    }
}
