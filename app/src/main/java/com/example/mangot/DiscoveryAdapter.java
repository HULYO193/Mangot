package com.example.mangot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.DiscoveryViewHolder> {
    private ArrayList<Manga> discoverymanga;
    private Context c;


    public DiscoveryAdapter(ArrayList<Manga> discoverymanga,Context c) {
        this.discoverymanga = discoverymanga;
        this.c = c;
    }

    @NonNull
    @Override
    public DiscoveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View discoveryview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discovery_grid, parent, false);
        return new DiscoveryViewHolder(discoveryview);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoveryViewHolder holder, int position) {
        Manga currManga = discoverymanga.get(position);
        holder.nameTextView.setText(currManga.getName());
        if(currManga.hasMangaFront) {
            downloadImageFromStorage(holder.imgv, currManga);
        }
        else
        {
            Bitmap b = BitmapFactory.decodeResource(c.getResources(),R.drawable.loadingdungeonodyssey);
            holder.imgv.setImageBitmap(b);
        }
        holder.imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Discovery)c,MangaActivity.class);
                intent.putExtra("mangaName",currManga.getName());
                ((Discovery)c).startActivity(intent);
            }
        });



    }

    private void downloadImageFromStorage(ImageView imgv, Manga currManga) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference();
            // at the moment add random name
            StorageReference imageRef = storageRef.child(currManga.getName()+"/Front/MangaFront");
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

    @Override
    public int getItemCount() {
        return discoverymanga.size();
    }

    public static class DiscoveryViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgv;
        public TextView nameTextView;
        public DiscoveryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv = itemView.findViewById(R.id.frontmanga);
            nameTextView = itemView.findViewById(R.id.textViewmanga);
        }
    }
}
