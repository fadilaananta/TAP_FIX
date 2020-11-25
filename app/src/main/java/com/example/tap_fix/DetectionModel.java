package com.example.tap_fix;

public class DetectionModel {
    public String getWebUri() {
        return webUri;
    }

    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }

    private String webUri;

    public DetectionModel(String webUri) {
        this.webUri = webUri;
    }
}
