package com.example.mangot;

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

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDataStorage extends AppCompatActivity {



    // 1. pass the manga name via intent
    // 2 diplay mange name in text view
    // 3. after files where chosen
    //      a. upload the images for this chapter for the firebase storage
    //      b. update number of chapters in the firebase database document-  for this mange -chapter counter
    private static final int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT =1 ;
    FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    EditText editTextTitle;
    EditText editTextDescription;
    private String mangaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasedatastorage);

        mangaName = getIntent().getStringExtra("MangaName");
        TextView mName = findViewById(R.id.tvMangaName);
        mName.setText(mangaName);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
    }

    public void SaveNote(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();


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
                            }
                        }else{

                            // Getting the URI of the selected file and logging into logcat at debug level
                            Uri uri = data.getData();
                            Log.d("fileUri: ", String.valueOf(uri));
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