package com.xj.images.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xj.images.R;
import com.xj.images.beans.Image;

import java.util.List;

/**
 * Created by ajun on 2017/1/7.
 */

public class ImageAdapter extends BaseAdapter {

    private List<Image> mImages;
    private Context mContext;
    public ImageAdapter(Context context, List<Image> images){
        this.mContext = context;
        this.mImages = images;
    }
    @Override
    public int getCount() {
        return null != mImages ? mImages.size() / 2 : 0;
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.base_adapter_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.image1);
            viewHolder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mImages.get(position * 2).getImageURL()).into(viewHolder.image1);
        Glide.with(mContext).load(mImages.get(position * 2 + 1).getImageURL()).into(viewHolder.image2);
        return convertView;
    }

    private static class ViewHolder{
        ImageView image1;
        ImageView image2;
    }
}
