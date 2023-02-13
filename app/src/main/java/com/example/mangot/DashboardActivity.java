package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ArrayList<MangaStatus> usersmangas = new ArrayList<MangaStatus>();

        usersmangas.add(new MangaStatus("hello world",1,"reading"));
        usersmangas.add(new MangaStatus("helloing",5,"reading"));
        usersmangas.add(new MangaStatus("idk",19,"reading"));
        usersmangas.add(new MangaStatus("dave",31,"reading"));
        usersmangas.add(new MangaStatus("solo leveling",11,"reading"));

        RecyclerView recyclerView =findViewById(R.id.Dashboard);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        MangaAdapter mangaAdapter = new MangaAdapter(usersmangas);
        recyclerView.setAdapter(mangaAdapter);

    }



    public void gotomangapage(View view)
    {
        Intent npage = new Intent(this,MangaActivity.class);
        startActivity(npage);
    }

}