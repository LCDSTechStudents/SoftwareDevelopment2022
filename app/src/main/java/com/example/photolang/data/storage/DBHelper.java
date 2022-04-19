package com.example.photolang.data.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "photolang.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context,@Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists flashcards(id INT primary key autoincrement, word TEXT, score TEXT, image_name TEXT) " ;
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists flashcards" ;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
