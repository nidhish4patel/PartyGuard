package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S525339 on 9/7/2016.
 * This class holds the info. for each fraternity i.e the description and whether the paid for the app or not.
 */
public class Fraternities {
    String fraternityName;
    String fraternityDescription;
    String imageId;
    String paymentStatus;

    public Fraternities(String fraternityName, String fraternityDescription, String imageId,String paymentStatus) {
        this.fraternityName = fraternityName;
        this.fraternityDescription = fraternityDescription;
        this.imageId = imageId;
        this.paymentStatus=paymentStatus;
    }

    public String getFraternityName() {
        return fraternityName;
    }

    public void setFraternityName(String fraternityName) {
        this.fraternityName = fraternityName;
    }

    public String getFraternityDescription() {
        return fraternityDescription;
    }

    public void setFraternityDescription(String fraternityDescription) {
        this.fraternityDescription = fraternityDescription;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
