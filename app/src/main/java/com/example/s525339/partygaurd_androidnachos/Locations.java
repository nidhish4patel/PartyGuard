package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by s525140 on 9/8/2016.
 * This is the Locations class where user can select the location
 */
public class Locations {
    String locationName;
    public Locations(String locationName){
        this.locationName=locationName;
    }

    public String getLocationName(){
        return locationName;
    }
    public void setLocationName(String locationName){
        this.locationName=locationName;
    }
}
