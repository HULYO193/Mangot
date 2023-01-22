package com.example.mangot;

public class Manga {
    /* can also be Manhua and Manhwa */
    String name;
    String group; /* translation group */
    int chapters;

    int rating; /* ---> 1-5 */

    public Manga() {

    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}