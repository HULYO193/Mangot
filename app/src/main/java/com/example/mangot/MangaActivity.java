package com.example.mangot;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MangaActivity extends BaseActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    private ArrayList<Uri> uriArr = new ArrayList<>();
    ArrayList<Chapter> chapters = new ArrayList<>();
    private String mangas_name;
    boolean isPic;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            // Checking whether data is null or not
            if (result != null) {
                //  Log.d("fileUri: ", String.valueOf(uri));
                uriArr.add(result);
                db.collection("mangot").whereEqualTo("name", mangas_name).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        isPic = false;


                        if (uriArr.size() > 0) // if a file is chosen
                        {
                            Uri u = uriArr.get(0);
                            isPic = true;

                            try {
                                // read the uri into image
                                // comrpess the image and store as jpeg
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(MangaActivity.this.getContentResolver(), u);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                byte[] byteArray = stream.toByteArray();
                                String filename = "MangaFront";
                                String title = "Front";
                                String pathName = mangas_name + "/" + title + "/" + filename;
                                // upload to storage
                                StorageReference frontReference = storageRef.child(pathName);
                                frontReference.putBytes(byteArray);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        db.collection("mangot").document(mangas_name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    Manga currmanga = task.getResult().toObject(Manga.class);
                                        currmanga.setHasMangaFront(isPic);
                                    db.collection("mangot").document("" + mangas_name).set(currmanga).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // if success -> go to Dashboard page
                                            // else -> problem ->
                                            if (task.isSuccessful()) {
                                                Intent gotodashboard = new Intent(MangaActivity.this, DashboardActivity.class);
                                                startActivity(gotodashboard);
                                            } else {
                                                Toast.makeText(MangaActivity.this, "failed " + task.getException(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });


                    }
                });
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        ImageView imgv = findViewById(R.id.imageView2);
        String dbname = getIntent().getStringExtra("mangaName");
        mangas_name = dbname;




        db.collection("mangot").document(dbname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Manga m = task.getResult().toObject(Manga.class);
                    TextView mangaName = findViewById(R.id.mangaName);
                    String manga_name = m.getName();
                    mangaName.setText(manga_name);
                    if(m.hasMangaFront)
                    {
                        StorageReference pathReference= storage.getReference().child(""+manga_name + "/Front/MangaFront");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Data for "images/island.jpg" is returns, use this as needed
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgv.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Log.d("manga upload", "onFailure: " + exception.getMessage());
                            }
                        });
                    }
                    else
                    {
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.loadingdungeonodyssey);
                        imgv.setImageBitmap(b);

                    }
                    //ArrayList<Chapter> chapters = new ArrayList<>();
                    for (int i = 1; i <= m.getChapters(); i++) {
                        Chapter c = new Chapter(i);
                        chapters.add(c);

                    }
                    RecyclerView recyclerChapter = findViewById(R.id.recyclerchapters);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MangaActivity.this);
                    recyclerChapter.setLayoutManager(layoutManager);


                    ChapterAdapter chapterAdapter = new ChapterAdapter(chapters,MangaActivity.this,m.getName());
                    recyclerChapter.setAdapter(chapterAdapter);




                }
                else
                {
                    Toast.makeText(MangaActivity.this,"failed " + task.getException(),Toast.LENGTH_LONG).show();
                }

            }
        });

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });


    }


    public void uploadMangaFront(View view) {



    }
    public void ToReader(String mangas_name,Chapter currentChapter){
        Intent toReader = new Intent(this,Reader.class);
        toReader.putExtra("mangaPathname",mangas_name);
        toReader.putExtra("chapterNum",currentChapter.getChapternum());
        startActivity(toReader);
    }

    public void getName(Chapter chapter)
    {

    }
    public void addChapters(View view) {
        TextView mangaName = findViewById(R.id.mangaName);

        String manga_name = mangaName.getText().toString();

        Intent mangaintent = new Intent(this, AddChapterActivity.class);
        mangaintent.putExtra("MangaName",manga_name);
        mangaintent.putExtra("maxchaps",chapters.size());
        startActivity(mangaintent);
    }

    public void addToDashboard(View view)
    {
        //Intent to go to the dashboardActivity and put the Manga's data as an Intent.putExtra
        //when we are in the DashboardActivity we will add the data as to the usersmangas(Arraylist<MangaStatus>)

        TextView mangaName = findViewById(R.id.mangaName);
        //  RecyclerView recyclerChapter = findViewById(R.id.recyclerchapters);
        String manga_name =mangaName.getText().toString();
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