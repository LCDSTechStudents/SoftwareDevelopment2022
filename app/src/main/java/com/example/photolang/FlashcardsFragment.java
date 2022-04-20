package com.example.photolang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.photolang.data.storage.DBHelper;
import com.example.photolang.data.storage.SFlashcard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardsFragment extends Fragment {

    private Handler subHandler;
    private SFlashcard[] flashcards;
    private View view;

    private static final int MSG_GET_SUB_HANDLER = 0;
    private static final int MSG_GET_ALL_FLASHCARDS = 1;
    private static final int MSG_LOAD_FLASHCARDS = 2;
    private static final int MSG_SHOW_LOADING = 3;
    private static final int MSG_HIDE_LOADING = 4;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Handler MainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_SUB_HANDLER:
                    //receive handler from child thread
                    subHandler = (Handler) msg.obj;
                    break;
                case MSG_LOAD_FLASHCARDS:
                    //receive all flashcards from child thread
                    flashcards = (SFlashcard[]) msg.obj;
                    renderCards(view, flashcards);
                    break;
                case MSG_SHOW_LOADING:
                    //show loading
                    break;
                case MSG_HIDE_LOADING:
                    //close loading
                    break;
            }
        }
    };

    public FlashcardsFragment() {}

    public static FlashcardsFragment newInstance() {
        FlashcardsFragment fragment = new FlashcardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //set parameters if needed
        }
        HandleThread handleThread = new HandleThread(MainHandler);
        handleThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flashcards, container, false);
        subHandler.sendMessage(subHandler.obtainMessage(MSG_GET_ALL_FLASHCARDS, v));
        this.view = v;
        return v;
    }

    public void renderCards(View v, SFlashcard[] flashcards) {
        ArrayAdapter<SFlashcard> adapter = new ArrayAdapter<SFlashcard>(getContext(), android.R.layout.simple_list_item_1, flashcards);
        ListView list = v.findViewById(R.id.flashcards_list);
        list.setAdapter(adapter);
    }

    public class HandleThread extends Thread{
        private Handler UIHandler;

        public HandleThread (Handler handler){
            this.UIHandler = handler;
        }

        @Override
        public void run() {
            Looper.prepare();
            Message msg = new Message();
            msg.what = 0;
            msg.obj = childHandler;
            super.run();
        }

        private final Handler childHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_GET_ALL_FLASHCARDS:
                        sendMessage(obtainMessage(MSG_SHOW_LOADING));
                        SFlashcard[] cards = getAllFlashcards();
                        sendMessage(obtainMessage(MSG_LOAD_FLASHCARDS, cards));
                        sendMessage(obtainMessage(MSG_HIDE_LOADING, cards));
                        break;

                }
            }
        };

        private SFlashcard[] getAllFlashcards(){
            DBHelper dbHelper = new DBHelper(getContext(), "flashcards", null, DBHelper.DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("flashcards", null, null, null, null, null, null);
            SFlashcard[] flashcards = new SFlashcard[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()){
                flashcards[i] = new SFlashcard(cursor.getInt(SFlashcard.COLUMN_INDEX_ID),
                        cursor.getString(SFlashcard.COLUMN_INDEX_CLASS),
                        cursor.getString(SFlashcard.COLUMN_INDEX_LANG),
                        cursor.getFloat(SFlashcard.COLUMN_INDEX_SCORE),
                        cursor.getString(SFlashcard.COLUMN_INDEX_IMAGENAME));
                i++;
            }
            return flashcards;
        }
    }

}