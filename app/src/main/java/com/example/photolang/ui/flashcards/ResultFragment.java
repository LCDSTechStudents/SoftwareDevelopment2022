package com.example.photolang.ui.flashcards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photolang.R;
import com.example.photolang.data.storage.DBHelper;
import com.example.photolang.data.tf.ModelResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String DB = "param1";


    // TODO: Rename and change types of parameters
    private ModelResult modelResult;
    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mr The model result..
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
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

            modelResult.getImage();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            new Thread(save).start();
        }
    };

    Runnable save = new Runnable() {
        @Override
        public void run() {
            DBHelper dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert("flashcards", null, modelResult.getContentValues());
            Cursor cursor = db.query("flashcards", null, null, null, null, null, null);
            cursor.moveToFirst()
            dbHelper.close();
            db.close();
        }
    };

}
