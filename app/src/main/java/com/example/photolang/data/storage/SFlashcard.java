package com.example.photolang.data.storage;


import android.content.ContentValues;
import android.graphics.Bitmap;

import com.example.photolang.data.Config;
import com.example.photolang.data.tf.ModelResult;

import java.util.Date;

public class SFlashcard{

    public static final String TABLE_NAME = "flashcards";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_CLASS = "class";
    public static final String COLUMN_NAME_LANG = "lang";
    public static final String COLUMN_NAME_SCORE = "score";
    public static final String COLUMN_NAME_IMAGENAME = "image_name";

    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_CLASS = 1;
    public static final int COLUMN_INDEX_LANG = 2;
    public static final int COLUMN_INDEX_SCORE = 3;
    public static final int COLUMN_INDEX_IMAGENAME = 4;

    public int id;
    public String words;
    public String lang;
    public float score;
    public String imageName;
    public ModelResult modelResult;

    public SFlashcard(int id, String words, String lang, float score, String imageName) {
        this.id = id;
        this.words = words;
        this.lang = lang;
        this.score = score;
        this.imageName = imageName;
    }

    public SFlashcard(String words, float score){
        this.words = words;
        this.score = score;
        Date date = new Date(System.currentTimeMillis());
        this.imageName = date.getDay() + "_" + date.getMonth() + "_" + date.getYear() + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds();
    }
    public SFlashcard(String words, float score, String lang){
        this.words = words;
        this.score = score;
        this.lang = Config.DEFAULT_LANGUAGE;
        Date date = new Date(System.currentTimeMillis());
        this.imageName = date.getDay() + "_" + date.getMonth() + "_" + date.getYear() + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds();
    }

    public SFlashcard(ModelResult mr){
        this.words = mr.getCls();
        this.score = mr.getScore();
        this.modelResult = mr;
        this.lang = Config.DEFAULT_LANGUAGE;
        Date date = new Date(System.currentTimeMillis());
        this.imageName = date.getDay() + "_" + date.getMonth() + "_" + date.getYear() + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds();
    }
    public SFlashcard(ModelResult mr, String lang){
        this.words = mr.getCls();
        this.score = mr.getScore();
        this.modelResult = mr;
        this.lang = lang;
        Date date = new Date(System.currentTimeMillis());
        this.imageName = date.getDay() + "_" + date.getMonth() + "_" + date.getYear() + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds();
    }
    //getters
    public int getId(){
        return id;
    }
    public String getWords(){
        return words;
    }
    public float getScore(){
        return score;
    }
    public String getLang(){
        return lang;
    }
    public String getImageName(){
        return imageName;
    }
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SFlashcard.COLUMN_NAME_CLASS, getWords());
        values.put(SFlashcard.COLUMN_NAME_SCORE, getScore());
        values.put(SFlashcard.COLUMN_NAME_IMAGENAME, getImageName());
        return values;
    }
    public Bitmap getImage(){
        if (modelResult != null && modelResult.getImage() != null){
            return modelResult.getImage();
        }
        throw  new NullPointerException("Image is null");
    }
}