package com.example.mangot;

public class Post {
    private String userName;
    private String subject;
    private String content;

    public Post(String userName, String subject, String content) {
        this.userName = userName;
        this.subject = subject;
        this.content = content;
    }

    public Post() {
    }

    public String getUserName() {
        return userName;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
