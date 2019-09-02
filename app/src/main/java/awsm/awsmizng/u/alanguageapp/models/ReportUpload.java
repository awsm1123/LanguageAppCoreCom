package awsm.awsmizng.u.alanguageapp.models;

public class ReportUpload {

    public String rName;
    public String rUploaderID;
    public String rtimeStamp;
    public String rText;

    public ReportUpload() {
    }

    public ReportUpload(String rName, String rUploaderID, String rtimeStamp, String rText) {
        this.rName = rName;
        this.rUploaderID = rUploaderID;
        this.rtimeStamp = rtimeStamp;
        this.rText = rText;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrUploaderID() {
        return rUploaderID;
    }

    public void setrUploaderID(String rUploaderID) {
        this.rUploaderID = rUploaderID;
    }

    public String getRtimeStamp() {
        return rtimeStamp;
    }

    public void setRtimeStamp(String rtimeStamp) {
        this.rtimeStamp = rtimeStamp;
    }

    public String getrText() {
        return rText;
    }

    public void setrText(String rText) {
        this.rText = rText;
    }
}
