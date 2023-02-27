package com.example.mangot;

import androidx.recyclerview.widget.RecyclerView;
import  androidx.annotation.NonNull;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChaptersViewHolder> {
    ArrayList<Chapter> chapters;

    public ChapterAdapter(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_manga, parent, false);
        return new ChaptersViewHolder(chapterView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersViewHolder holder, int position) {
        Chapter currentChapter = chapters.get(position);
        holder.chapterTextView.setText("chapter " + currentChapter.getChapternum());

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public static class ChaptersViewHolder extends RecyclerView.ViewHolder {

        public TextView chapterTextView;

        public ChaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTextView = itemView.findViewById(R.id.textView_chapter);

        }

    }


}

