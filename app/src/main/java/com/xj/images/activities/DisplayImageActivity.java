package com.xj.images.activities;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.xj.images.R;
import com.xj.images.beans.Image;
import com.xj.images.glide.ImageGlideModule;
import com.xj.images.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by ajun on 2017/1/22.
 */

public class DisplayImageActivity extends Activity {

    @BindView(R.id.image_from) TextView mFrom;
    @BindView(R.id.menu_bar) View mMenus;
    @BindView(R.id.save_image) Button mSaveImage;
    @BindView(R.id.set_wallpaper) Button mSetWallpaper;
    @BindView(R.id.image) ImageView imageView;
    @OnLongClick(R.id.image) boolean onImageLongClick(View view){
        DiskCache diskCache = ImageGlideModule.getDiskCache(this);
        File file = diskCache.get(new StringSignature(mShowingImageUrl));
        if(null != file){
            File newImageFile = new File(FileUtils.getExternalDir(),mShowingImageUrl.hashCode() + ".jpg");
            try {
                if(!newImageFile.exists()) {
                    Log.i("alanMms", "saved:" + mShowingImageUrl.hashCode());
                    FileUtils.copyFile(file, newImageFile);
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"已经保存过了，无需保存",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            /*Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getTitle()));*/
        }
        return  false;
    }

    private boolean mMenusShowing = false;
    private WallpaperManager mWallpaperManager;

    @OnClick({R.id.save_image,R.id.share_image,R.id.image,R.id.set_wallpaper})
     public void doClick(View view){
        if(R.id.save_image == view.getId()) {
            DiskCache diskCache = ImageGlideModule.getDiskCache(this);
            File file = diskCache.get(new StringSignature(mShowingImageUrl));
            if (null != file) {
                File newImageFile = new File(FileUtils.getExternalDir(), mShowingImageUrl.hashCode() + ".jpg");
                try {
                    if (!newImageFile.exists()) {
                        Log.i("alanMms", "saved:" + mShowingImageUrl.hashCode());
                        FileUtils.copyFile(file, newImageFile);
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "已经保存过了，无需保存", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(R.id.share_image == view.getId()){
            DiskCache diskCache = ImageGlideModule.getDiskCache(this);
            File file = diskCache.get(new StringSignature(mShowingImageUrl));
            if(null != file){
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getTitle()));
            }else{
                Log.d("alanF", "showImage:" + mShowingImageUrl);
                Toast.makeText(this, "不好意思，亲，出现问题了。。。", Toast.LENGTH_SHORT).show();
            }
        }else if(R.id.image == view.getId()){
            mMenusShowing = !mMenusShowing;
            if(mMenusShowing){
                mMenus.setVisibility(View.VISIBLE);
                mFrom.setVisibility(View.VISIBLE);
            }else{
                mMenus.setVisibility(View.INVISIBLE);
                mFrom.setVisibility(View.INVISIBLE);
            }
        }else if (R.id.set_wallpaper == view.getId()){
            DiskCache diskCache = ImageGlideModule.getDiskCache(this);
            File file = diskCache.get(new StringSignature(mShowingImageUrl));
            try {
                mWallpaperManager.setStream(new FileInputStream(file));
                Toast.makeText(this, "可以欣赏了！！！", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("alanF","",e);
                Toast.makeText(this, "设置失败了！！！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Image mImage;
    private String mShowingImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_activity);
        ButterKnife.bind(this);
        mWallpaperManager = WallpaperManager.getInstance(this);
        Intent intent = getIntent();
        mImage = intent.getParcelableExtra("image");
        mShowingImageUrl = mImage.getImageURL();
        Log.d("alanF", "from:"+ mImage.getFrom());
        mFrom.setText(mImage.getFrom());
        Glide.with(this)
                .load(mImage.getImageURL())
                .crossFade()
                .error(R.drawable.failed)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mShowingImageUrl = mImage.getImageThumbURL();
                        Log.d("alanF", "loadImage:" + mShowingImageUrl);
                        Glide.with(DisplayImageActivity.this).load(mShowingImageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                mShowingImageUrl = mImage.getImageThumbBakURL();
                                Log.d("alanF", "loadBakImage:" + mShowingImageUrl);
                                Glide.with(DisplayImageActivity.this).load(mShowingImageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(target);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.animator.default_anim_out);
    }
}
