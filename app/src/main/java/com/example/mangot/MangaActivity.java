package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MangaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managa);
        ArrayList<Chapter> chapters = new ArrayList<>();
        int chapter = getIntent().getIntExtra("chapter",chapters.size() + 1);
        Chapter chap = new Chapter(chapter);
        chapters.add(chap);

        RecyclerView recyclerChapter = findViewById(R.id.recyclerchapters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerChapter.setLayoutManager(layoutManager);
        //keep working on chapterAdapter and finish it so you can finish working on the "addToDashboard" and the work on DashboardActivity;
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
        TextView mangaName = findViewById(R.id.mangaName);

        String manga_name = mangaName.getText().toString();
        //MangaStatus = new MangaStatus(manga_name,)

        //Intent to go to the dashboardActivity and put the Manga's data as an Intent.putExtra
        //when we are in the DashboardActivity we will add the data as to the usersmangas(Arraylist<MangaStatus>)
        //db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail())
        //        .collection("userMangas")
        //        .document(""+manga_name)


    }
}