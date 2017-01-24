package com.xj.images.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ajun on 2017/1/7.
 */

public class Image implements Parcelable{
    private String imageURL;
    private String imageThumbURL;
    private String imageThumbBakURL;

    public Image(){

    }
    protected Image(Parcel in) {
        imageURL = in.readString();
        imageThumbURL = in.readString();
        imageThumbBakURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeString(imageThumbURL);
        dest.writeString(imageThumbBakURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public String toString() {
        return "Image{" +
                "imageURL='" + imageURL + '\'' +
                ", imageThumbURL='" + imageThumbURL + '\'' +
                ", imageThumbBakURL='" + imageThumbBakURL + '\'' +
                '}';
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageThumbURL() {
        return imageThumbURL;
    }

    public void setImageThumbURL(String imageThumbURL) {
        this.imageThumbURL = imageThumbURL;
    }

    public String getImageThumbBakURL() {
        return imageThumbBakURL;
    }

    public void setImageThumbBakURL(String imageThumbBakURL) {
        this.imageThumbBakURL = imageThumbBakURL;
    }
}
