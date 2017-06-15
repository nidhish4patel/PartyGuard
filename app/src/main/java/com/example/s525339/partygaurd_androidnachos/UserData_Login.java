package com.example.s525339.partygaurd_androidnachos;

/**
 * Created by S524977 on 10/8/2016.
 * This class holds the token information.For each log-in entry a new token would be generated.
 * After specific time the token expires.
 */
public class UserData_Login {
    String accessToken;
    String tokenType;
    String expireIn;
    String username;
    String issued;
    String expires;

    public UserData_Login(String expireIn, String accessToken, String expires, String issued, String tokenType, String username) {
        this.expireIn = expireIn;
        this.accessToken = accessToken;
        this.expires = expires;
        this.issued = issued;
        this.tokenType = tokenType;
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    //    "access_token": "I0E9QlwZwmuLX2WRCUjKGUzk1Mjm6-BPX7Dh43LAT00B_Kw3zmyA8PX3-PlspDc4F_00wmQ90AP-JoB0XcGcLR1qx5s6y_8OV2tzTNC4dXF_A3Rn5etRWnD8Ub4ZXeDD67VlnLNZE0ugHfut1rTD_HtxZte-L9CBZpHoNfo41ZJVu8MzNr4X8ziYPf0fd-HF7T-ScOy3JjMnIzALXDU3vLJEQaGTtxAGffnYOE2JexN4ZxSM42jgelZOiXqtJMu3Jt7CZUv9qJch6YbPFetDssZXOJRhtdcoCLChbWonhuNLGn_FmP1650hfDCXUKXc5Vw0WXwhzS1FCVaYw5aSE42Gi6FCG0pWK-FHJ2KOG6DXr2lSAmoulBJf9pq9uNJLpX-mWDMDISC8NwS4fMy_Qo26yMBMuVnWNDM-qP9fgZ-v09vD8LKsUfrXWPAX6y50jw567YqiZE-EXMEfGiw5nwb5xrbqSAw9AKKwsKcx6GfZZLvaLdm24z4jhMHdOh7No",
//            "token_type": "bearer",
//            "expires_in": 1209599,
//            "userName": "ashish@gmail.com",
//            ".issued": "Thu, 29 Sep 2016 01:42:46 GMT",
//            ".expires": "Thu, 13 Oct 2016 01:42:46 GMT"
}
