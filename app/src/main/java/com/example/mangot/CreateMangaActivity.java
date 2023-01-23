package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateMangaActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manga);


    }
    //Creates the new Manga and add it to FireBase
    public void CreateManga(View view)
    {


        EditText manga_name = findViewById(R.id.etMangaName);
        EditText mangadescrition = findViewById(R.id.etDescription);

        String mangas_name = manga_name.getText().toString();
        String manga_description = mangadescrition.getText().toString();



        // check if such a manga exists

        db.collection("mangot").whereEqualTo("name",mangas_name).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    // check if we have data
                    if(task.getResult().size()==1)
                    {
                        // probelm...
                        Toast.makeText(CreateMangaActivity.this,"EXISTS " ,Toast.LENGTH_LONG).show();

                    }
                    else // no such exists
                    {
                        Manga manga = new Manga(mangas_name,manga_description);

                        db.collection("mangot").add(manga).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                // if success -> go to Dashboard page
                                // else -> problem ->
                                if(task.isSuccessful()){
                                    Intent gotodashboard = new Intent(CreateMangaActivity.this,MangaActivity.class);
                                    startActivity(gotodashboard);
                                }
                                else{
                                    Toast.makeText(CreateMangaActivity.this,"failed " + task.getException(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                }
            }
        });





    }
}