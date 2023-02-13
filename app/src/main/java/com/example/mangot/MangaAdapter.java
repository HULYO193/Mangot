package com.example.mangot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private ArrayList<MangaStatus> usersmangas;

    public MangaAdapter(ArrayList<MangaStatus> usersmangas) {
        this.usersmangas = usersmangas;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mangaview = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_manga,parent,false);
        return new MangaViewHolder(mangaview);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {

        MangaStatus currMangaStatus = usersmangas.get(position);
        holder.mangaTextView.setText(currMangaStatus.getMangaName());
        holder.statusTextView.setText(currMangaStatus.getStatus());
        holder.currChapterTextView.setText(""+currMangaStatus.getCurrChapter());
        holder.maxChaptersTextView.setText(""+currMangaStatus.getMaxChapters());


    }

    @Override
    public int getItemCount() {
        return usersmangas.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {

        public TextView mangaTextView;
        public TextView statusTextView;
        public TextView currChapterTextView;
        public TextView maxChaptersTextView;
        public Button optionsButton;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            mangaTextView = itemView.findViewById(R.id.name_manga);
            statusTextView = itemView.findViewById(R.id.status);
            currChapterTextView = itemView.findViewById(R.id.currchapter);
            maxChaptersTextView = itemView.findViewById(R.id.maxchapters);

        }
    }
}
