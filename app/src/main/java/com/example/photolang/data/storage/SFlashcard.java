package com.example.photolang.data.storage;


import java.util.Date;

public class SFlashcard{

    public static final String TABLE_NAME = "flashcards";
    public static final String COLUMN_NAME_CLASS = "class";
    public static final String COLUMN_NAME_LANG = "lang";
    public static final String COLUMN_NAME_SCORE = "score";
    public static final String COLUMN_NAME_IMAGENAME = "image_name";

    public int id;
    public String words;
    public float score;
    public String imageName;

    public SFlashcard(String words, float score){
        this.words = words;
        this.score = score;
        this.imageName = new Date(System.currentTimeMillis()).toString();
    }
}