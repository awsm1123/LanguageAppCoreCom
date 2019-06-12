package awsm.awsmizng.u.alanguageapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "userDetails")
public class FirebaseUserProfile {

    @PrimaryKey
    @NonNull
    public String userID;
    public String userName;
    public String language;
    @Ignore
    public String points;
    @Ignore
    public String lastActive;


    public FirebaseUserProfile(String userID, String userName, String language) {
        this.userName = userName;
        this.language = language;
        this.userID = userID;
    }

    @Ignore
    public FirebaseUserProfile(){}

    @Ignore
    public FirebaseUserProfile(String userName, String language, String userID, String points, String lastActive) {
        this.userName = userName;
        this.language = language;
        this.userID = userID;
        this.points = points;
        this.lastActive = lastActive;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }
}
