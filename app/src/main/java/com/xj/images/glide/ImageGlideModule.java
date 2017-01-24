package com.xj.images.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by ajun on 2017/1/23.
 */

public class ImageGlideModule implements GlideModule {
    public static final String DEFAULT_DISK_CACHE_DIR = "A-Images";
    public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 1024;//1G
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return  getDiskCache(context);
            }
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
    private static DiskCache diskCache;
    public static DiskCache getDiskCache(Context context){
        if(null == diskCache){
            synchronized (ImageGlideModule.class){
                if(null == diskCache) {
                    diskCache = new ExternalCacheDiskCacheFactory(context,DEFAULT_DISK_CACHE_DIR, DEFAULT_DISK_CACHE_SIZE).build();
                }
            }
        }
        return diskCache;
    }
}
