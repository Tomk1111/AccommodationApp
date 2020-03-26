package ie.ul.accommodationapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Listing implements Parcelable {
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
    private String uid;
    private String userName;


    public Listing(){

    }

    public Listing(int id, double lng, double lat, String addressText, int roomInt, int priceInt, String toString, Date sDate, Date eDate, int difInt, String uid, String userName) {
        this.id = id;
        this.longitude = lng;
        this.latitude = lat;
        this.address = addressText;
        this.rooms = roomInt;
        this.price = priceInt;
        this.description = toString;
        this.startDate =sDate;
        this.endDate = eDate;
        this.duration = difInt;
        this.uid = uid;
        this.userName = userName;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.address);
        dest.writeInt(this.rooms);
        dest.writeInt(this.price);
        dest.writeString(this.description);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeInt(this.duration);
    }

    protected Listing(Parcel in) {
        this.id = in.readInt();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.address = in.readString();
        this.rooms = in.readInt();
        this.price = in.readInt();
        this.description = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.duration = in.readInt();
    }

    public static final Parcelable.Creator<Listing> CREATOR = new Parcelable.Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel source) {
            return new Listing(source);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };
}

