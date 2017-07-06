package com.xj.images.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by alanfeng on 2017/7/6 0006.
 */

public class Display {
    public static int getDisplayWidth(Context context){
        DisplayMetrics displayMetrics =  context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
