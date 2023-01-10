package com.example.fyp.ui.UserProfile;

public class User {

    String usernname;
    String email;
    String password;
    String pImage;

    public User(String usernname, String email, String password, String pImage) {
        this.usernname = usernname;
        this.email = email;
        this.password = password;
        this.pImage = pImage;
    }

    public User() {
    }

    public String getUsernname() {
        return usernname;
    }

    public void setUsernname(String usernname) {
        this.usernname = usernname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}