package ie.ul.accommodationapp;

import java.util.Date;
import java.util.UUID;

public class Listing {
    private int id;
    private double longitude;
    private double latitude;
    private String address;
    private int rooms;
    private int price;
    private String description;
    private Date startDate;
    private Date endDate;
    private int duration;

    public Listing(){

    }

    public Listing(int id, double longitude,
               double latitude, String address, int rooms,int price,String description,
                   Date startDate,Date endDate,int duration)
    {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.rooms = rooms;
        this.price = price;
        this.description = description;
        this.startDate =startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}

