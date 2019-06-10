package awsm.awsmizng.u.alanguageapp.models;

public class FirebaseProfileRV {
    public String userName;
    public String points;
    public String language;

    public FirebaseProfileRV(){}

    public FirebaseProfileRV(String userName, String points, String language) {
        this.userName = userName;
        this.points = points;
        this.language = language;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
