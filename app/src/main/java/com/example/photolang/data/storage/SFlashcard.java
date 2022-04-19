package com.example.photolang.data.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class SFlashcard {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "class")
    public String word;

    @ColumnInfo(name ="score")
    public float score;

    @ColumnInfo(name = "image_name")
    public String imageName;

    public SFlashcard(String word, float score) {
        this.word = word;
        this.score = score;
        this.imageName =  new Date(System.currentTimeMillis()).toString();
    }
}
