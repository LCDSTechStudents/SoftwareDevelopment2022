package com.example.photolang.data.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.photolang.data.tf.ModelResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ImageSaver {
    public static final String IMAGE_PATH = "/store/images/";

    /*
    @param modelResult - flashcard model result to save
    @return - return file name of saved image
     */
    public static String saveFlashCard(SFlashcard flashcard) throws Exception {
        File appDir = new File(IMAGE_PATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = flashcard.getImageName();
        File file = new File(appDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        flashcard.getImage().compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return fileName;
    }
    public static Bitmap readFlashcard(String fileName) throws Exception {
        File file = new File(IMAGE_PATH, fileName);
        return BitmapFactory.decodeFile(file.getPath());
    }
}
