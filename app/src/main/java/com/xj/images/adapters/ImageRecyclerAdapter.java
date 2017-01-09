package com.xj.images.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.images.R;
import com.xj.images.beans.Image;

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
        Log.i("alanMms", "type:" + viewType);
        MyRecyclerViewHolder viewHolder = new MyRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_recycler_view_adapter_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {
        holder.image1.setImageURI(mImages.get(position).getThumbURL());
    }

    @Override
    public int getItemCount() {
        return null == mImages?0:mImages.size();
    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView image1;
        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            image1 = (SimpleDraweeView) itemView.findViewById(R.id.recycler_view_adapter_item);
        }
    }
}
