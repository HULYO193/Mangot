package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.sql.Array;
import java.util.ArrayList;

public class Reader extends AppCompatActivity {
    private ArrayList<StorageReference> Readerchapter=new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        String nameofmanga =  getIntent().getStringExtra("mangaPathname");
        String chapterNum = getIntent().getStringExtra("chapterNum");

            //storageRef.getFile("")
        // get from chapter num the number of images...
        
        getChapterImagesFromFirebase(nameofmanga,chapterNum);

      




    }

    private void getChapterImagesFromFirebase(String nameofmanga, String chapterNum) {
        String refString = nameofmanga + "/Chapter " +chapterNum + "/";
        StorageReference listRef = storage.getReference().child(refString);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
 /*                       for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }



                        for (int i = 0; i <listResult.getItems().size() ; i++) {
                            StorageReference item = listResult.getItems().get(i);




                        }

 */
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Readerchapter.add(item);


                        }
                        RecyclerView recyclerreader = findViewById(R.id.readerRecycler);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Reader.this);
                        recyclerreader.setLayoutManager(layoutManager);

                        ReaderAdapter adapter = new ReaderAdapter(Readerchapter,Reader.this);

                        recyclerreader.setAdapter(adapter);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }
}