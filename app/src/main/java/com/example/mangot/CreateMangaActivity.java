package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CreateMangaActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT =1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // general reference to firebase storage
    StorageReference storageRef = storage.getReference();

    private ArrayList<Uri> uriArr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manga);



    }
    //Creates the new Manga and add it to FireBase
    public void CreateManga(View view)
    {
        EditText manga_name = findViewById(R.id.etMangaName);
        String mangas_name = manga_name.getText().toString();

        // check if such a manga exists

        db.collection("mangot").whereEqualTo("name",mangas_name).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    // check if we have data
                    if(task.getResult().size()==1)
                    {
                        // problem...
                        Toast.makeText(CreateMangaActivity.this,"EXISTS " ,Toast.LENGTH_LONG).show();

                    }
                    else // no such exists
                    {
                        Manga manga = new Manga(mangas_name);

                        db.collection("mangot").add(manga).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                // if success -> go to Dashboard page
                                // else -> problem ->
                                if(task.isSuccessful()){
                                    Intent gotodashboard = new Intent(CreateMangaActivity.this,DashboardActivity.class);
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



        // loop over the array
        // for each element -> get the Uri
        // upload the uri to firebase storage

        // for testing only!!!
        //mangaName = "test_manga";//
        //the loop is the main factor of the activity-> uploading the pictures of the Front
                EditText Name  = findViewById(R.id.etMangaName);
                String mangaName = Name.toString();
                String filename = "MangaFront" ;
                String title = "Front";
                String pathName = mangaName + "/" + title + "/" + filename;
                // upload to storage
                StorageReference chapterReference = storageRef.child(pathName);
                //chapterReference.putFile(u);

                // storage/manganame/title




    }

    public void uploadMangaFront(View view) {
        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("image/*");  //use image/* for photos, etc.
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "files returned",Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FOR_ON_ACTIVITY_RESULT:

                    // Checking whether data is null or not
                    if (data != null) {

                        // Checking for selection multiple files or single.
                        if (data.getClipData() == null){
                            // Getting the URI of the selected file and logging into logcat at debug level
                            Uri uri = data.getData();
                            Log.d("fileUri: ", String.valueOf(uri));
                            uriArr.add(uri);


                        }else{

                            Toast.makeText(this, "Failed, please select a single file", Toast.LENGTH_SHORT).show();

                        }
                        Button b = findViewById(R.id.createManga);
                        b.setClickable(true);
                    }
                    break;
            }
        }
        else
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }
}