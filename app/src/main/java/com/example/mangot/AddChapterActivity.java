package com.example.mangot;

import androidx.activity.result.ActivityResultLauncher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddChapterActivity extends AppCompatActivity {
//FirebaseDataStorage changed into AddChapterActivity(as well the xml to addchapter)
private FirebaseAuth mAuth =  FirebaseAuth.getInstance();


    // 1. pass the manga name via intent - check
    // 2 diplay mange name in text view - check
    // 3. after files where chosen
    //      a. upload the images for this chapter for the firebase storage
    //      b. update number of chapters in the firebase database document-  for this mange -chapter counter
    private static final int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT =1 ;
    FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private ArrayList<Uri> uriArr = new ArrayList<>();
    EditText editTextTitle;
    EditText editTextDescription;
    private String mangaName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchapter);

        mangaName = getIntent().getStringExtra("MangaName");
        TextView mName = findViewById(R.id.tvMangaName);
        mName.setText(mangaName);

        Button b = findViewById(R.id.button);

        b.setClickable(false);

        editTextTitle = findViewById(R.id.edit_text_title);

    }

    //uplaoding the pictures of the chapter(any number>0) to storage and going back to MangaActivity
    public void SaveNote(View view) {
        String title = editTextTitle.getText().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // genberal reference to firebase storage
        StorageReference storageRef = storage.getReference();

        // loop over the array
        // for each element -> get the Uri
        // upload the uri to firebase storage

        // for testing only!!!
        //
        //the loop is the main factor of the activity-> uploading the pictures of the chapter
        for (int i=0;i<uriArr.size();i++) {
            Uri u = uriArr.get(i);
            if(u!=null)
            {

                String filename = "file"+i;
                String pathName = mangaName + "/" + title +"/" + filename;
                // upload to storage
                StorageReference chapterReference = storageRef.child(pathName);
                if(i==uriArr.size()-1)
                chapterReference.putFile(u).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        db.collection("MangaStatus").document(mAuth.getCurrentUser().getEmail()).collection("userMangas").document(mangaName).update("maxChapters", FieldValue.increment(1));
                        db.collection("mangot").document(mangaName).update("chapters", FieldValue.increment(1));
                        //after the loop is done we can go back to the last page
                        Intent backtomanga = new Intent(AddChapterActivity.this,MangaActivity.class);
                        backtomanga.putExtra("chapter",title);
                        startActivity(backtomanga);


                    }
                });
                else
                    chapterReference.putFile(u);



                // storage/manganame/title
            }

        }

    }




    public void UploadChapters(View view) {
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
                        if (data.getClipData() != null){

                            // Getting the length of data and logging up the logs using index
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {

                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                Uri uri = data.getClipData().getItemAt(index).getUri();
                                Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );
                                uriArr.add(uri);
                            }
                        }else{

                            // Getting the URI of the selected file and logging into logcat at debug level
                            Uri uri = data.getData();
                            Log.d("fileUri: ", String.valueOf(uri));
                            uriArr.add(uri);

                        }
                        Button b = findViewById(R.id.button);
                        b.setClickable(true);
                    }
                    break;
            }
        }
        else
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }
}