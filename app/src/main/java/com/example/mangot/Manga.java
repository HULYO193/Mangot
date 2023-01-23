package com.example.mangot;

public class Manga {
    /* can also be Manhua and Manhwa */
    String name;
    String group; /* translation group */
    String description; // add description of the Manga
    int chapters;

    int rating; /* ---> 1-5 */

    public Manga(String name,String description) {
        this.name = name;
        this.group = "";
        this.description = description;
        chapters = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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