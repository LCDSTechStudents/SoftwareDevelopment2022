package com.example.photolang.data.tf;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.photolang.data.storage.SFlashcard;

public class ModelResult implements Parcelable {
    private String cls;
    private float score;
    private Bitmap image;

    public ModelResult(String cls, float score, Bitmap image) {
        this.cls = cls;
        this.score = score;
        this.image = image;

    }

    protected ModelResult(Parcel in) {
        cls = in.readString();
        score = in.readFloat();
    }

    public static final Creator<ModelResult> CREATOR = new Creator<ModelResult>() {
        @Override
        public ModelResult createFromParcel(Parcel in) {
            return new ModelResult(in);
        }

        @Override
        public ModelResult[] newArray(int size) {
            return new ModelResult[size];
        }
    };

    //getter and setter
    public String getCls() {
        return cls;
    }
    public float getScore() {
        return score;
    }
    public Bitmap getImage() {
        return image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cls);
        parcel.writeFloat(score);
    }
}
