package com.example.photolang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.photolang.data.storage.DBHelper;
import com.example.photolang.data.storage.SFlashcard;
import com.example.photolang.ui.flashcards.FlashcardListAdapter;

import java.util.concurrent.locks.ReentrantLock;

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
    private static final int MSG_DELAY_LOADING = 5;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final ReentrantLock lock = new ReentrantLock();

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
                    if (flashcards == null) {
                        throw new RuntimeException("flashcards is null");
                    }
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
        this.view = inflater.inflate(R.layout.fragment_flashcards, container, false);
        MainHandler.sendMessage(MainHandler.obtainMessage(MSG_SHOW_LOADING));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() { subHandler.sendMessage(Message.obtain(subHandler,MSG_GET_ALL_FLASHCARDS));
            }
        }, 2000);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //waiting for subhandler to be initialized
    }

    public void renderCards(View v, SFlashcard[] flashcards) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        RecyclerView list = v.findViewById(R.id.flashcards_list_recycler);
        FlashcardListAdapter adapter = new FlashcardListAdapter(flashcards);
        list.setLayoutManager(layoutManager);
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
            msg.what = MSG_GET_SUB_HANDLER;
            msg.obj = childHandler;
            UIHandler.sendMessage(msg);
            Looper.loop();
            super.run();
        }

        private final Handler childHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_GET_ALL_FLASHCARDS:
                        UIHandler.sendMessage(obtainMessage(MSG_SHOW_LOADING));
                        SFlashcard[] cards = getAllFlashcards();
                        UIHandler.sendMessage(obtainMessage(MSG_LOAD_FLASHCARDS, cards));
                        UIHandler.sendMessage(obtainMessage(MSG_HIDE_LOADING));
                        break;

                }
            }
        };

        private SFlashcard[] getAllFlashcards(){
            DBHelper dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM flashcards", null);
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
            cursor.close();
            db.close();
            return flashcards;
        }
    }

}