package com.example.mangot;

public class Manga {
    /* can also be Manhua and Manhwa */
    String name;
    int chapters;
    boolean hasMangaFront;

    public Manga(String name) {
        this.name = name;
        chapters = 0;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}