package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S525140 on 10/19/2016.
 * This class shows the push notification info.It shows the alert type,location,username and fraternity
 */
public class AlertLogFromPush {

    private String alertTypeFromPush;
    private String locationFromPush;
    private String userNameFromPush;
    private String fraternityFromPush;

    public String getFraternityFromPush() {
        return fraternityFromPush;
    }

    public void setFraternityFromPush(String fraternityFromPush) {
        this.fraternityFromPush = fraternityFromPush;
    }

    public String getAlertTypeFromPush() {
        return alertTypeFromPush;
    }

    public void setAlertTypeFromPush(String alertTypeFromPush) {
        this.alertTypeFromPush = alertTypeFromPush;
    }

    public String getLocationFromPush() {
        return locationFromPush;
    }

    public void setLocationFromPush(String locationFromPush) {
        this.locationFromPush = locationFromPush;
    }

    public String getUserNameFromPush() {
        return userNameFromPush;
    }

    public void setUserNameFromPush(String userNameFromPush) {
        this.userNameFromPush = userNameFromPush;
    }

    public AlertLogFromPush(String alertTypeFromPush, String locationFromPush, String userNameFromPush, String fraternityFromPush) {
        this.alertTypeFromPush = alertTypeFromPush;
        this.locationFromPush = locationFromPush;
        this.userNameFromPush = userNameFromPush;
        this.fraternityFromPush = fraternityFromPush;
    }
}
