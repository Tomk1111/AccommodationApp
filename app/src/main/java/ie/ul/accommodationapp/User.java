package ie.ul.accommodationapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String uid;
    private String userName;
    private List<Listing> likedAds;
    private List<Listing> listedAds;

    public User() {}
    public User(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
        this.likedAds = new ArrayList<>();
        this.listedAds = new ArrayList<>();
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

    public List<Listing> getLikedAds() {
        return likedAds;
    }

    public void setLikedAds(List<Listing> likedAds) {
        this.likedAds = likedAds;
    }

    public List<Listing> getListedAds() {
        return listedAds;
    }

    public void setListedAds(List<Listing> listedAds) {
        this.listedAds = listedAds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.userName);
        dest.writeTypedList(this.likedAds);
        dest.writeTypedList(this.listedAds);
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.userName = in.readString();
        this.likedAds = in.createTypedArrayList(Listing.CREATOR);
        this.listedAds = in.createTypedArrayList(Listing.CREATOR);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
