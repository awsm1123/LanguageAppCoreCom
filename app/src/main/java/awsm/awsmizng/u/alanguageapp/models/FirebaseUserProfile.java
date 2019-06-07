package awsm.awsmizng.u.alanguageapp.models;

public class FirebaseUserProfile {

    public String userName;
    public String language;
    public String userID;

    public FirebaseUserProfile(){}

    public FirebaseUserProfile(String userName, String language, String userID) {
        this.userName = userName;
        this.language = language;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
