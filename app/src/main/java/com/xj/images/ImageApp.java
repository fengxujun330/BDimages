package com.xj.images;

import android.app.Application;
import android.content.Context;

/**
 * Created by ajun on 2017/1/7.
 */

public class ImageApp extends Application {

    static ImageApp app;
    @Override
    public void onCreate() {
        app = this;
    }

    public static Context getInstance(){
        return app;
    }
}
