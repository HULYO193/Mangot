package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardDialog.DashboardDialogListener {
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        int maxChapters = getIntent().getIntExtra("max_chapters",0);
        String manganame = getIntent().getStringExtra("manga_name");
        ArrayList<MangaStatus> usersmangas = new ArrayList<MangaStatus>();


        usersmangas.add(new MangaStatus(manganame,maxChapters));

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

    public void CreateManga(View view) {
        Intent createManga = new Intent(this,CreateMangaActivity.class);
        startActivity(createManga);
    }
    //public void UpdateMangaStatus
}
