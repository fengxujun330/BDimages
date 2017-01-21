package com.xj.images.view;

import com.xj.images.beans.Image;

import java.util.List;

/**
 * Created by ajun on 2017/1/21.
 */

public interface ViewInterface {
    public void notifyDataChanged();
    public void addImage(Image image);
    public void addImages(List<Image> images);
    public void clearAllDatas();
}
