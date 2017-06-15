package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S525039 on 10/10/2016.
 * This shows  alerts information
 */
public class AlertLog {

    String alerts;
    String date;
    String name;
    String fraternity;
    String location;
    String comments;
    String status;
    String time;

    public AlertLog(String alerts, String date, String time) {
        this.alerts = alerts;
        this.date = date;
        this.time = time;
    }

    public AlertLog(String alerts, String date){
        this.alerts = alerts;
        this.date = date;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
