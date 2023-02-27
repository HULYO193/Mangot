package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardDialog.DashboardDialogListener {

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

        RecyclerView recyclerView = findViewById(R.id.Dashboard);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        MangaAdapter mangaAdapter = new MangaAdapter(usersmangas,this);
        recyclerView.setAdapter(mangaAdapter);

    }



    public void gotomangapage(View view)
    {
        Intent nextpage = new Intent(this,MangaActivity.class);
        startActivity(nextpage);
    }

    @Override
    public void applyText(String username, String password) {
        Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show();
    }
    //public void UpdateMangaStatus
}
//kjhgguyg