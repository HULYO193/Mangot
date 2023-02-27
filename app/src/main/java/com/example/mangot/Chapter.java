package com.example.mangot;

public class Chapter {
    int chapternum;

    public Chapter(int chapternum) {
        this.chapternum = chapternum;
    }
    public String getChapternum() {
        String chapstring = "" + chapternum;
        return chapstring;
    }

    public void setChapternum(int chapternum) {
        this.chapternum = chapternum;
    }


    /* Array of the pictures coming from FireBase */
}
