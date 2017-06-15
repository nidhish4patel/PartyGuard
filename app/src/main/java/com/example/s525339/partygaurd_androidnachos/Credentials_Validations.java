package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by s525140 on 10/22/2016.
 * This class validates the user credentials
 */
public class Credentials_Validations {
    String email;
    String password;
    String accesstoken;

    public Credentials_Validations(String email, String password, String accesstoken) {
        this.email = email;
        this.password = password;
        this.accesstoken = accesstoken;
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

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}
