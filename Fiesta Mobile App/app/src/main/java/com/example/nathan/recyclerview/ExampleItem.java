package com.example.nathan.recyclerview;

public class ExampleItem {

    private String test_name;
    private String province;
    private String image;
    private String municipal;
    private String history;
    private String latitude;
    private String longitude;
    private String fid;



    public ExampleItem(String fid, String test_name, String province, String image, String municipal, String history, String latitude, String longitude) {
        this.test_name = test_name;
        this.province = province;
        this.image = image;
        this.municipal= municipal;
        this.history =history;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fid = fid;

    }

    public String getFid() {
        return fid;
    }
    public String getTest_name() {
        return test_name;
    }
    public String getProvince() {
        return province;
    }

    public String getImage() {
        return image;
    }

    public String getMunicipal() {
        return municipal;
    }
    public String getHistory() {
        return history;
    }
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
