package com.example.s525339.partygaurd_androidnachos;

/**
 * This class is used to maintain the singleton pattern.The entire issue life-cycle information is stored here.
 *
 */
public class ComplaintSingleton {
    private String univerSityName="";
    private String fraternityName = "";
    private String access_token="";

    private String email_UserInfo;
    private String firstName_UserInfo;
    private String lastName_UserInfo;
    private String university_UserInfo;
    private String phoneNumber_UserInfo;
    private String userType_UserInfo;
    private String status_UserInfo;
    private String age_UserInfo;
    private String image_UserInfo="";
    private String fraternityName_UserInfo;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getFraternityDesc() {
        return fraternityDesc;
    }

    public void setFraternityDesc(String fraternityDesc) {
        this.fraternityDesc = fraternityDesc;
    }

    private String fraternityDesc="";
    private String fraternityImageURL="";
    private String issueType = "";
    private String issueLocation = "";
    private String issueComments = "";

    public String getIssueTypeDialog() {
        return issueTypeDialog;
    }

    public void setIssueTypeDialog(String issueTypeDialog) {
        this.issueTypeDialog = issueTypeDialog;
    }

    public String getFraternityImageURL() {
        return fraternityImageURL;
    }

    public void setFraternityImageURL(String fraternityImageURL) {
        this.fraternityImageURL = fraternityImageURL;
    }

    private String issueTypeDialog="";
    private static ComplaintSingleton ourInstance = new ComplaintSingleton();

    public static ComplaintSingleton getInstance() {
        return ourInstance;
    }

    private ComplaintSingleton() {
    }

    public String getFraternityName() {
        return fraternityName;
    }

    public void setFraternityName(String fraternityName) {
        this.fraternityName = fraternityName;
    }

    public String getIssueComments() {
        return issueComments;
    }

    public void setIssueComments(String issueComments) {
        this.issueComments = issueComments;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public static ComplaintSingleton getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(ComplaintSingleton ourInstance) {
        ComplaintSingleton.ourInstance = ourInstance;
    }

    public String getUniverSityName() {
        return univerSityName;
    }

    public void setUniverSityName(String univerSityName) {
        this.univerSityName = univerSityName;
    }


    public String getImage_UserInfo() {
        return image_UserInfo;
    }

    public void setImage_UserInfo(String image_UserInfo) {
        this.image_UserInfo = image_UserInfo;
    }

    public String getAge_UserInfo() {
        return age_UserInfo;
    }

    public void setAge_UserInfo(String age_UserInfo) {
        this.age_UserInfo = age_UserInfo;
    }

    public String getStatus_UserInfo() {
        return status_UserInfo;
    }

    public void setStatus_UserInfo(String status_UserInfo) {
        this.status_UserInfo = status_UserInfo;
    }

    public String getUserType_UserInfo() {
        return userType_UserInfo;
    }

    public void setUserType_UserInfo(String userType_UserInfo) {
        this.userType_UserInfo = userType_UserInfo;
    }

    public String getPhoneNumber_UserInfo() {
        return phoneNumber_UserInfo;
    }

    public void setPhoneNumber_UserInfo(String phoneNumber_UserInfo) {
        this.phoneNumber_UserInfo = phoneNumber_UserInfo;
    }

    public String getUniversity_UserInfo() {
        return university_UserInfo;
    }

    public void setUniversity_UserInfo(String university_UserInfo) {
        this.university_UserInfo = university_UserInfo;
    }

    public String getLastName_UserInfo() {
        return lastName_UserInfo;
    }

    public void setLastName_UserInfo(String lastName_UserInfo) {
        this.lastName_UserInfo = lastName_UserInfo;
    }

    public String getFirstName_UserInfo() {
        return firstName_UserInfo;
    }

    public void setFirstName_UserInfo(String firstName_UserInfo) {
        this.firstName_UserInfo = firstName_UserInfo;
    }

    public String getEmail_UserInfo() {
        return email_UserInfo;
    }

    public void setEmail_UserInfo(String email_UserInfo) {
        this.email_UserInfo = email_UserInfo;
    }

    public String getFraternityName_UserInfo() {
        return fraternityName_UserInfo;
    }

    public void setFraternityName_UserInfo(String fraternityName_UserInfo) {
        this.fraternityName_UserInfo = fraternityName_UserInfo;
    }
}
