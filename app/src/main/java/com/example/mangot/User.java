package com.example.mangot;

public class User {
    String name;
    String email;
    String password;
    Dashboard dashboard;


    public User(String name, String email, String password, Dashboard dashboard) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dashboard = dashboard;
    }
}
