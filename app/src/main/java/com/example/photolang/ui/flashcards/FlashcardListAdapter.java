package com.example.photolang.ui.flashcards;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photolang.R;
import com.example.photolang.data.storage.ImageSaver;
import com.example.photolang.data.storage.SFlashcard;

import java.util.Arrays;
import java.util.List;

public class FlashcardListAdapter extends RecyclerView.Adapter<FlashcardListAdapter.ViewHolder> {
    private List<SFlashcard> flashcards;
    public FlashcardListAdapter(@NonNull SFlashcard[] flashcards){
        this.flashcards = Arrays.asList(flashcards);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SFlashcard flashcard = flashcards.get(position);
        try {

            holder.cImageView.setImageBitmap(ImageSaver.readFlashcard(holder.cImageView.getContext(), flashcard.imageName));
        } catch (Exception e) {
            Toast.makeText(holder.cImageView.getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        holder.cTextClass.setText(flashcard.getWords());
        holder.cTextTranslate.setText(flashcard.getWords());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View cView;
        ImageView cImageView;
        TextView cTextClass;
        TextView cTextTranslate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cView = itemView;
            cImageView = cView.findViewById(R.id.flashcard_preview_image);
            cTextClass = cView.findViewById(R.id.flashcard_class);
            cTextTranslate = cView.findViewById(R.id.flashcard_translate);
        }
    }
}
