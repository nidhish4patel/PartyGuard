package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S525339 on 9/7/2016.
 * This is HostAlert class which holds each alert info.Host user can see and allocate the issue to the gusrd user
 */
public class HostAlert {
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
    String basicFullName;
    String issueID;

    public HostAlert(String incidence, String date, String name, String fraternity, String location, String comments, String status, String time, String imageURL,String age,String basicFullName,String issueID) {
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
        this.basicFullName=basicFullName;
        this.issueID=issueID;
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

    public String getBasicFullName() {
        return basicFullName;
    }

    public void setBasicFullName(String basicFullName) {
        this.basicFullName = basicFullName;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }
}
