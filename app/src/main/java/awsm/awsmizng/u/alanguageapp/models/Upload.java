package awsm.awsmizng.u.alanguageapp.models;

public class Upload {
    public String name;
    public String url;
    public String theme;

    public Upload(){}

    public Upload(String name, String url, String theme) {
        this.name = name;
        this.url = url;
        this.theme = theme;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
