package com.example.photolang.ui.flashcards;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photolang.R;
import com.example.photolang.data.storage.DBHelper;
import com.example.photolang.data.storage.ImageSaver;
import com.example.photolang.data.storage.SFlashcard;
import com.example.photolang.data.tf.ModelResult;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    private Handler childHandler;

    private ModelResult modelResult;
    private SFlashcard card;

    public ResultFragment() {}

    public static ResultFragment newInstance(ModelResult mr) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();

        args.putParcelable("modelResult", mr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modelResult = getArguments().getParcelable("modelResult");
            card = new SFlashcard(modelResult);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ImageView imageView = view.findViewById(R.id.imageView2);
        imageView.setImageBitmap(modelResult.getImage());

        TextView trust = view.findViewById(R.id.result_trust);
        trust.setText(String.format("%.2f", modelResult.getScore()));
        TextView object = view.findViewById(R.id.result_object);
        object.setText(modelResult.getCls());

        Button save = view.findViewById(R.id.btn_save);
        save.setOnClickListener(saveListener);
        Button retake = view.findViewById(R.id.btn_retake);
        retake.setOnClickListener(retakeListener);

        HandleThread ht = new HandleThread(UIHandler);
        ht.start();

        return view;
    }

    private final View.OnClickListener retakeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    private final View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            msg.what = 1;
            childHandler.sendMessage(msg);
        }
    };

    private Handler UIHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    //accept handler
                    childHandler =(Handler) msg.obj;
                    break;
                case 1:
                    //error handler
                    Toast.makeText(getContext(), String.format("error happen") , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    showAlert();
                    break;
                case 3:
                    //success
                    Toast.makeText(getContext(), "save success", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void showAlert(){
        AlertDialog ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("This object is already saved. Do you want to add another one?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        //true save
                        msg.what = 2;
                        childHandler.sendMessage(msg);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed();
                    }
                })
                .create();
        ad.show();
    }


    public class HandleThread extends Thread{
        Handler uiHandler;
        //handler thread constructor
        HandleThread(Handler handler){
            uiHandler = handler;
        }
        Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        //error
                    case 1:
                        //save image
                        saveImage(card);
                        break;
                    case 2:
                        try {
                            trueSave(card);
                        }catch (Exception e){
                            e.printStackTrace();
                            UIHandler.sendMessage(Message.obtain(UIHandler, 1, e));
                        }

                        break;
                }
            }
        };
        @Override
        public void run() {
            super.run();
            Looper.prepare();

            Message msg = new Message();
            msg.what = 0;
            msg.obj = handler;
            uiHandler.sendMessage(msg);
        }

        private void saveImage(SFlashcard card){
            String[] columns = new String[]{
                    SFlashcard.COLUMN_NAME_CLASS,
                    //more if needed
            };
            String selection = SFlashcard.COLUMN_NAME_CLASS + " = ?";
            String[] selectionArgs = new String[]{
                    modelResult.getCls(),
            };
            DBHelper dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(SFlashcard.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if(cursor.getCount() != 0){
                Message msg = new Message();
                msg.what = 2;
                uiHandler.sendMessage(msg);
                cursor.close();
                dbHelper.close();
                db.close();
                return;
            }
            try {
                String name = ImageSaver.saveFlashCard(getContext(),card);
                if(ImageSaver.hasFlashcard(getContext(),name)){
                    db.insert(SFlashcard.TABLE_NAME, null, card.getContentValues());
                    uiHandler.obtainMessage(3).sendToTarget();
                    cursor.close();
                    dbHelper.close();
                    db.close();
                    return;
                }else{
                    uiHandler.obtainMessage(1).sendToTarget();
                    cursor.close();
                    dbHelper.close();
                    db.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = e;
                uiHandler.sendMessage(msg);
                cursor.close();
                dbHelper.close();
                db.close();
            }
            cursor.close();
            dbHelper.close();
            db.close();
        }

        private void trueSave(SFlashcard card) throws Exception {

            DBHelper dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(SFlashcard.TABLE_NAME, null, card.getContentValues());
            String name = ImageSaver.saveFlashCard(getContext(),card);
            Message msg = new Message();
            msg.what = 3;
            uiHandler.sendMessage(msg);
            dbHelper.close();
            db.close();

        }
    }
}
