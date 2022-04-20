package com.example.photolang.ui.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photolang.R;
import com.example.photolang.data.storage.SFlashcard;

import java.util.List;

public class FlashcardListAdapter extends ArrayAdapter<SFlashcard> {

    private int resourceId;

    public FlashcardListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<SFlashcard> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SFlashcard flashcard = getItem(position);
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new RecyclerView.ViewHolder(view);
        }
    }
}
