package com.example.photolang.data.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.photolang.data.tf.ModelResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ImageSaver {

    /*
    @param modelResult - flashcard model result to save
    @return - return file name of saved image
     */
    public static String saveFlashCard(Context context, SFlashcard flashcard) throws Exception {
        File appDir = new File(context.getFilesDir(), "images");
        if(!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = flashcard.getImageName();
        File file = new File(appDir, fileName+".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        flashcard.getImage().compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return fileName;
    }
    public static Bitmap readFlashcard(Context context, String fileName) throws Exception {
        File file = new File(context.getFilesDir(), "images/" +fileName + ".jpg");
        return BitmapFactory.decodeFile(file.getPath());
    }
    public static Boolean hasFlashcard(Context context, String fileName) throws Exception {
        File file = new File(context.getFilesDir(), "images/" +fileName + ".jpg");
        return file.exists();
    }

}
