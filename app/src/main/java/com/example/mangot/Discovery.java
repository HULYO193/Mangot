package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Discovery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        ArrayList<MangaStatus> discoveryManga = new ArrayList<MangaStatus>();

        RecyclerView recyclerdiscovery = findViewById(R.id.discoveryrecycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2 ,RecyclerView.VERTICAL,true);
        recyclerdiscovery.setLayoutManager(layoutManager);
    }
}