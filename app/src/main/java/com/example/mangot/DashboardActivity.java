package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends BaseActivity implements DashboardDialog.DashboardDialogListener {
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MangaAdapter mangaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
       // int maxChapters = getIntent().getIntExtra("max_chapters",0);
       // String manganame = getIntent().getStringExtra("manga_name");

        getDataFromFirebase();
    }
        // get all the documents
        // + the newely added one(From addToDashboard) in the Usermangas and put them in the array list for the recyclerview;
    private void getDataFromFirebase() {

        RecyclerView recyclerView = findViewById(R.id.Dashboard);

        ArrayList<MangaStatus> usersmangas = new ArrayList<MangaStatus>();

        db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail()).collection("userMangas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            ArrayList<MangaStatus> lowPriority = new ArrayList<>();
                            for (DocumentSnapshot doc: task.getResult())

                            {
                                MangaStatus ms =doc.toObject(MangaStatus.class);
                                if(ms.getCurrChapter()==ms.getMaxChapters())
                                    lowPriority.add(ms);
                                else
                                    usersmangas.add(ms);
                            }

                            usersmangas.addAll(lowPriority);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this);

                            recyclerView.setLayoutManager(layoutManager);

                             mangaAdapter = new MangaAdapter(usersmangas,DashboardActivity.this);
                            recyclerView.setAdapter(mangaAdapter);

                        }

                    }
                });
/*
                .add(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc: queryDocumentSnapshots)
                {
                    usersmangas.add(doc.toObject(MangaStatus.class));
                }

            }

        }); */

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.dashboard:
                        return true;

                    case R.id.discovery:
                        startActivity(new Intent(getApplicationContext(),Discovery.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.createNewManga:
                        startActivity(new Intent(getApplicationContext(),CreateMangaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
    @Override
    public void applyText(String username, String password) {
        Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show();
    }

    public void refreshAdapter() {

        mangaAdapter.notifyDataSetChanged();
    }

    public void LogOut(View view) {
        mAuth.signOut();
        Intent singout = new Intent(this,Login.class);
        startActivity(singout);
    }
}
