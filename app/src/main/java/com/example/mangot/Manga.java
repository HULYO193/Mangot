package com.example.mangot;

public class Manga {
    /* can also be Manhua and Manhwa */
    String name;
    int chapters;
    boolean hasMangaFront;

    public Manga() {
    }

    public Manga(String name) {
        this.name = name;
        chapters = 0;
        this.hasMangaFront = false;
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

    public boolean isHasMangaFront() {
        return hasMangaFront;
    }

    public void setHasMangaFront(boolean hasMangaFront) {
        this.hasMangaFront = hasMangaFront;
    }

}