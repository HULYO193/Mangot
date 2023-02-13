package com.example.mangot;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class MangaStatus {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser fbUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String useremail;
    String mangaName;
    int currChapter;
    int maxChapters;
    String status;// reading/ plan to read/ dropped / completed.

    public MangaStatus(String mangaName, int currChapter, String status) {
        this.useremail = fbUser.getEmail() ;
        this.mangaName = mangaName;
        this.currChapter = currChapter;
        this.status = status;
        this.maxChapters = 100;
    }
    public int getMaxChapters() {
        return maxChapters;
    }

    public void setMaxChapters(int maxChapters) {
        this.maxChapters = maxChapters;
    }

    public MangaStatus() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrChapter() {
        return currChapter;
    }

    public void setCurrChapter(int currChapter) {
        this.currChapter = currChapter;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }


}
