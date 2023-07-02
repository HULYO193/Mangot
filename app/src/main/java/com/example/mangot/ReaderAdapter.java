package com.example.mangot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ReaderViewHolder> {
    private ArrayList<StorageReference> readerchapter;
    private Context c;
    public ReaderAdapter(ArrayList<StorageReference> readerchapter ,Context c){
        this.readerchapter = readerchapter;
        this.c = c;
    }
    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chapimageview = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_info_reader,parent,false);
        return new ReaderViewHolder(chapimageview);
    }
    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, int position) { downloadImageFromStorage(holder.chapterIV,this.readerchapter.get(position));}
    @Override
    public int getItemCount() {
        return readerchapter.size();
    }

    public static class ReaderViewHolder extends RecyclerView.ViewHolder{
            public ImageView chapterIV;
        public ReaderViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterIV = itemView.findViewById(R.id.chapterImage);
        }
    }
    private void downloadImageFromStorage(ImageView imgv,StorageReference ref) {

     //   StorageReference imageRef = storageRef.child(""+MangaPathName+"/"+cnum+"/file"+ (position) +"");
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgv.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d(TAG, "onFailure: problem");
            }
        });
    }
}
