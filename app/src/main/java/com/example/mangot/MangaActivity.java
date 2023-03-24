package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MangaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managa);

        ImageView imgv = findViewById(R.id.imageView2);

        ArrayList<Chapter> chapters = new ArrayList<>();
//        int chapter = getIntent().getIntExtra("chapter",chapters.size() + 1);
//        Chapter chap = new Chapter(chapter);
//        chapters.add(chap);
        chapters.add(new Chapter(1));
        chapters.add(new Chapter(2));
        chapters.add(new Chapter(3));
        chapters.add(new Chapter(4));
        chapters.add(new Chapter(5));

        RecyclerView recyclerChapter = findViewById(R.id.recyclerchapters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerChapter.setLayoutManager(layoutManager);


        ChapterAdapter chapterAdapter = new ChapterAdapter(chapters);
        recyclerChapter.setAdapter(chapterAdapter);



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

        TextView mangaName = findViewById(R.id.mangaName);
        RecyclerView recyclerChapter = findViewById(R.id.recyclerchapters);
        String manga_name = "what you doing";//mangaName.getText().toString();
        //MangaStatus ms = new MangaStatus(manga_name,);


       DocumentReference dr = db.collection("mangot").document(""+manga_name);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                Manga c = ds.toObject(Manga.class);
                int chaptersnum = c.getChapters();
                MangaStatus ms = new MangaStatus(manga_name,chaptersnum);
                db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail()).collection("userMangas")
                        .document(""+manga_name).set(ms).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Intent todashboard = new Intent(MangaActivity.this,DashboardActivity.class);
                                    startActivity(todashboard);
                                }
                            }
                        });



            }
        });


    }
}