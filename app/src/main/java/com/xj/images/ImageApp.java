package com.xj.images;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by ajun on 2017/1/7.
 */

public class ImageApp extends Application {

    @Override
    public void onCreate() {
        Fresco.initialize(this);
    }
}
