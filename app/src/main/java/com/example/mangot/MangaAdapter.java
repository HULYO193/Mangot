package com.example.mangot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dalvik.system.InMemoryDexClassLoader;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> implements DashboardDialog.DashboardDialogListener {
    private ArrayList<MangaStatus> usersmangas;
    private Context c;

    public MangaAdapter(ArrayList<MangaStatus> usersmangas,Context c) {
        this.usersmangas = usersmangas;
        this.c = c;
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
        holder.currChapterTextView.setText("chapter "+currMangaStatus.getCurrChapter());
        holder.maxChaptersTextView.setText("chapter "+currMangaStatus.getMaxChapters());
        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1 - delete- longholdonclick
                // 2 - change status
                // 3 - current chapter

                // show dialog:
                openDialog(currMangaStatus.getMangaName(),currMangaStatus.getStatus(),currMangaStatus.getCurrChapter());

            }
        });

        holder.mangaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clicked manga name
                //  pass:
                //  manga name
                // move to manga activity
                Intent i = new Intent((DashboardActivity)c,MangaActivity.class);
                i.putExtra("mangaName",currMangaStatus.getMangaName());
                ((DashboardActivity)c).startActivity(i);
            }
        });


    }
    public void openDialog(String mName,String mStatus,int mChapters) {
        DashboardDialog dashboardDialog = new DashboardDialog(mName,mStatus,mChapters);
        dashboardDialog.show(((DashboardActivity)this.c).getSupportFragmentManager(),"dialog of dashboard");
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
            optionsButton = itemView.findViewById(R.id.optionsButton);

        }
    }

    @Override
    public void applyText(String username, String password) {
        //will show on the dialog the context
    }




}
