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
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS flashcards");
        String sql = "create table if not exists flashcards(id INTEGER primary key autoincrement, class TEXT, lang TEXT, score TEXT, image_name TEXT) " ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists flashcards" ;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
