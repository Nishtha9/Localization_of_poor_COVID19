package com.example.covid_19helplineforpoor;

public class Details {
    private  String name;
    private  Integer num;
    private  String address;
    private  String area;
    private Integer pin;
    private  String district;
    private  String state;
    private Double latitude;
    private Double longitude;
    //image and gps location to be added

     public Details()
     {

     }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLatitude(){
         return latitude;
    }

    public void setLatitude(Double lat) {
        this.latitude = lat;
    }

    public Double getLongitude(){
         return longitude;
    }

    public void setLongitude(Double lng) {
        this.longitude = lng;
    }
}
