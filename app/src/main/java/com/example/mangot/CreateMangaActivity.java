package com.example.mangot;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateMangaActivity extends BaseActivity {
    private static final int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT =1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // general reference to firebase storage
    StorageReference storageRef = storage.getReference();

    private ArrayList<Uri> uriArr = new ArrayList<>();

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            // Checking whether data is null or not
            if (result != null) {
                  //  Log.d("fileUri: ", String.valueOf(uri));
                    uriArr.add(result);
                }else{
                    Toast.makeText(CreateMangaActivity.this, "Failed, please select a single file", Toast.LENGTH_SHORT).show();
                }
                Button b = findViewById(R.id.createManga);
                b.setClickable(true);
            }
        }

    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manga);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.createNewManga);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.discovery:
                        startActivity(new Intent(getApplicationContext(), Discovery.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.createNewManga:
                        return true;
                }
                return false;

            }

            //Creates the new Manga and add it to FireBase
            public void CreateManga(View view) {
                EditText manga_name = findViewById(R.id.etMangaName);
                String mangas_name = manga_name.getText().toString();

                // check if such a manga exists

                db.collection("mangot").whereEqualTo("name", mangas_name).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // check if we have data
                            if (task.getResult().size() == 1) {
                                // problem...
                                Toast.makeText(CreateMangaActivity.this, "EXISTS ", Toast.LENGTH_LONG).show();

                            } else // no such exists
                            {
                                boolean isPic = false;

                                if (uriArr.size() > 0) // if a file is chosen
                                {
                                    Uri u = uriArr.get(0);
                                    isPic = true;

                                    try {
                                        // read the uri into image
                                        // comrpess the image and store as jpeg
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateMangaActivity.this.getContentResolver(), u);
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


/*
                            String filename = "MangaFront";
                            String title = "Front";
                            String pathName = mangas_name + "/" + title + "/" + filename;
                            // upload to storage
                            StorageReference frontReference = storageRef.child(pathName);
                            frontReference.putFile(u);

 */
                                }


                                Manga manga = new Manga(mangas_name);
                                manga.setHasMangaFront(isPic);


                                db.collection("mangot").document("" + mangas_name).set(manga).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // if success -> go to Dashboard page
                                        // else -> problem ->
                                        if (task.isSuccessful()) {
                                            Intent gotodashboard = new Intent(CreateMangaActivity.this, DashboardActivity.class);
                                            startActivity(gotodashboard);
                                        } else {
                                            Toast.makeText(CreateMangaActivity.this, "failed " + task.getException(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });


                            }
                        }
                    }
                });
            }

            public void uploadMangaFront(View view) {

                mGetContent.launch("image/*");

            }

        });
    }
}