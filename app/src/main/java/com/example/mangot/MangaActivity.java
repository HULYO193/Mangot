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

        String manga_name = mangaName.toString();

        Intent mangaintent = new Intent(this,FirebaseDataStorage.class);
        mangaintent.putExtra("MangaName",manga_name);


    }
}