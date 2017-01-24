package com.xj.images.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

/**
 * Created by ajun on 2017/1/22.
 */

public class DisplayImageActivity extends Activity {

    @BindView(R.id.image) ImageView imageView;
    @OnLongClick(R.id.image) boolean onImageLongClick(View view){
        DiskCache diskCache = ImageGlideModule.getDiskCache(this);
        File file = diskCache.get(new StringSignature(mImage.getImageURL()));
        if(null != file){
            File newImageFile = new File(FileUtils.getExternalDir(),mImage.getImageURL().hashCode() + ".jpg");
            try {
                if(!newImageFile.exists()) {
                    Log.i("alanMms", "saved:" + mImage.getImageURL().hashCode());
                    FileUtils.copyFile(file, newImageFile);
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"已经保存过了，无需保存",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            /*Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setDataAndType(Uri.parse("file://" + newImageFile.getAbsolutePath()),"image*//*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(intent);*/
        }
        return  false;
    }
    private Image mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mImage = intent.getParcelableExtra("image");
        Glide.with(this)
                .load(mImage.getImageURL())
                .crossFade()
                .error(R.drawable.failed)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Glide.with(DisplayImageActivity.this).load(mImage.getImageThumbURL()).into(target);
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
