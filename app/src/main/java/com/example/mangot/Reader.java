package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.media.MediaParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.sql.Array;
import java.util.ArrayList;

public class Reader extends BaseActivity {
    private ArrayList<StorageReference> Readerchapter=new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private String chapterNum;
    private String nameofmanga;
    private Spinner spinnerchapters;
    String baseStatus = "";
    private  static final ArrayList<String> statusChaptersnums = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    int mChapters = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        statusChaptersnums.clear();
        nameofmanga =  getIntent().getStringExtra("mangaPathname");
        chapterNum = getIntent().getStringExtra("chapterNum");

            //storageRef.getFile("")
        // get from chapter num the number of images...
        
        getChapterImagesFromFirebase(nameofmanga,chapterNum);
        StatusChaptersConstractor(statusChaptersnums);

    }

    private void getChapterImagesFromFirebase(String nameofmanga, String chapterNum) {
        Readerchapter.clear();

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

    public void PrevChapter(View view) {
        String value = chapterNum;
        int chap = Integer.valueOf(value);
        chap = chap - 1;
        String prevchapt = ""+chap;
        chapterNum = prevchapt;
        getChapterImagesFromFirebase(nameofmanga,prevchapt);


    }

    public void NextChapter(View view) {
        String value = chapterNum;
        int chap = Integer.valueOf(value);
        chap = chap + 1;
        String nextchapt = ""+chap;
        chapterNum = nextchapt;
        getChapterImagesFromFirebase(nameofmanga,nextchapt);

    }

    private void StatusChaptersConstractor(ArrayList<String> chaptersSize){

        db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail())
                .collection("userMangas")
                .document(""+nameofmanga)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            MangaStatus mangaStatus = task.getResult().toObject(MangaStatus.class);
                            mChapters = mangaStatus.getMaxChapters();
                            spinnerchapters = (Spinner) findViewById(R.id.chaptersReader);
                            chaptersSize.add(baseStatus);
                            for (int i = mChapters; i >=1; i--) {
                                chaptersSize.add("" + i);
                            }
                            ArrayAdapter<String> statusAdapterchapters = new ArrayAdapter<String>(Reader.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,chaptersSize);
                            statusAdapterchapters.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            spinnerchapters.setAdapter(statusAdapterchapters);
                            spinnerchapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String value = chaptersSize.get(i);
                                    if(value.equals(baseStatus))
                                        return;

                                    getChapterImagesFromFirebase(nameofmanga,value);


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        }
                    }
                });


    }
}