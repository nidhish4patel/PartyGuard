package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by s525140 on 10/23/2016.
 * This class shows the alerts for the history tab under guard user.
 */
public class AlertsForHistoryTab_GuardUser {

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
    String basicUserFullName;
    String issueLocation;
    String basicUserImage;

    public AlertsForHistoryTab_GuardUser(String basicUserFullName,String issueLocation,String basicUserImage) {
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
        this.basicUserFullName=basicUserFullName;
        this.issueLocation=issueLocation;
        this.basicUserImage=basicUserImage;
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

    public String getBasicUserFullName() {
        return basicUserFullName;
    }

    public void setBasicUserFullName(String basicUserFullName) {
        this.basicUserFullName = basicUserFullName;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public String getBasicUserImage() {
        return basicUserImage;
    }

    public void setBasicUserImage(String basicUserImage) {
        this.basicUserImage = basicUserImage;
    }
}
