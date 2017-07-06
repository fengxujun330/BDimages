package com.xj.images.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xj.images.R;
import com.xj.images.activities.DisplayImageActivity;
import com.xj.images.beans.Image;
import com.xj.images.utils.Display;

import java.util.List;

/**
 * Created by ajun on 2017/1/8.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MyRecyclerViewHolder> {

    private Context mContext;
    private List<Image> mImages;

    public ImageRecyclerAdapter(Context context, List<Image> images){
        this.mContext = context;
        this.mImages = images;
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyRecyclerViewHolder viewHolder = new MyRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_recycler_view_adapter_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {
        final Image image = mImages.get(position);
        ImageView imageView = holder.image1;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if(0 < image.getWidth() && 0 < image.getHeight()) {
            float rotate = image.getHeight()/(image.getWidth() * 1.0f);
            int heigt = (int) (rotate * Display.getDisplayWidth(mContext)/2);
            params.height = heigt;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setLayoutParams(params);
        }else{
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        Glide.with(mContext)
                .load(image.getImageURL())
                .crossFade()
                .placeholder(R.drawable.so)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Glide.with(mContext).load(image.getImageThumbURL()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(R.drawable.failed)
                .into(holder.image1);
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DisplayImageActivity.class);
                intent.putExtra("image", image);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.animator.default_anim_in, R.animator.default_anim_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mImages?0:mImages.size();
    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView image1;
        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.recycler_view_adapter_item);

        }
    }
}
