package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by s525140 on 10/23/2016.
 * Host can see all alerts here.The location where the issue happnend along with comments can be seen here.
 */
public class HistoryAlerts {

    String incidence;
    String date;
    String name;
    String fraternity;
    String location;
    String comments;
    String status;
    String time;
    String age;
    String imageURL;

    public HistoryAlerts(String incidence, String date, String name, String fraternity, String location, String comments, String status, String time, String imageURL,String age) {
        this.incidence = incidence;
        this.date = date;
        this.name = name;
        this.fraternity = fraternity;
        this.location = location;
        this.comments = comments;
        this.status = status;
        this.time = time;
        this.imageURL = imageURL;
        this.age=age;
    }

    public String getIncidence() {
        return incidence;
    }

    public void setIncidence(String alerts) {
        this.incidence = alerts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFraternity() {
        return fraternity;
    }

    public void setFraternity(String fraternity) {
        this.fraternity = fraternity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
