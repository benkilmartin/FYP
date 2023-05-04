package com.example.fyp.UI.UserProfile;

public class User {

    String id;
    String usernname;
    String email;
    String password;
    String pImage;
    boolean bParent;

    public User(String id, String usernname, String email, String password, String pImage, boolean bParent) {
        this.id = id;
        this.usernname = usernname;
        this.email = email;
        this.password = password;
        this.pImage = pImage;
        this.bParent = bParent;
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

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public boolean isParent() {
        return bParent;
    }

    public void setParent(boolean bParent) {
        this.bParent = bParent;
    }
}