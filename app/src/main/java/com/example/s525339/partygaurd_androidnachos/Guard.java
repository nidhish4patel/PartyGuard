package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S525142 on 10/8/2016.
 * This class holds the info. for each guard user.
 */
public class Guard {

    private String guardName;
    private boolean isAvailable;
    private String imageURL;
    private String mobile;
    private String email;
    private int guardID;


    public Guard(String guardName, boolean isAvailable, String imageURL, String mobile,String email,int guardID) {
        this.mobile=mobile;
        this.email=email;
        this.guardName = guardName;
        this.isAvailable = isAvailable;
        this.imageURL = imageURL;
        this.guardID=guardID;
    }

    public Guard() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGuardID() {
        return guardID;
    }

    public void setGuardID(int guardID) {
        this.guardID = guardID;
    }
}
