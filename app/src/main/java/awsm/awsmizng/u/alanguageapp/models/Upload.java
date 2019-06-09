package awsm.awsmizng.u.alanguageapp.models;

public class Upload {
    public String name;
    public String url;
    public String uploaderName;
    public String uploaderID;
    public String uploadTime;

    public Upload(){}

    public Upload(String name, String url, String uploaderName, String uploaderID, String uploadTime) {
        this.name = name;
        this.url = url;
        this.uploaderName = uploaderName;
        this.uploaderID = uploaderID;
        this.uploadTime = uploadTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
