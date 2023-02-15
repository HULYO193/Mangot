package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MangaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managa);
    }

    public void addChapters(View view) {
        TextView mangaName = findViewById(R.id.mangaName);

        String manga_name = mangaName.getText().toString();

        Intent mangaintent = new Intent(this, AddChapterActivity.class);
        mangaintent.putExtra("MangaName",manga_name);
        startActivity(mangaintent);


    }

    public void addToDashboard(View view)
    {
        //Intent to go to the dashboardActivity and put the Manga's data as an Intent.putExtra
        //when we are in the DashboardActivity we will add the data as to the usersmangas(Arraylist<MangaStatus>)
    }
}