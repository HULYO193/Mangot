package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ArrayList<String>
    }

    public void gotomangapage(View view)
    {
        Intent npage = new Intent(this,MangaActivity.class);
        startActivity(npage);
    }

}