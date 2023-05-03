package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Discovery extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Manga> discoveryManga = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);


        db.collection("mangot").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    discoveryManga = (ArrayList<Manga>) task.getResult().toObjects(Manga.class);
                    RecyclerView recyclerdiscovery = findViewById(R.id.discoveryrecycler);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Discovery.this, 2, RecyclerView.VERTICAL, false);
                    recyclerdiscovery.setLayoutManager(layoutManager);

                    DiscoveryAdapter discoveryadapter = new DiscoveryAdapter(discoveryManga,Discovery.this);
                    recyclerdiscovery.setAdapter(discoveryadapter);
                }
                Toast.makeText(Discovery.this, "hello", Toast.LENGTH_SHORT).show();
            }
        });


    }
}