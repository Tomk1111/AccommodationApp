package ie.ul.accommodationapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class User implements Parcelable {
    private String uid;
    private String userName;
    private List<Listing> likedAds= new ArrayList<>();
    private List<Listing> listedAds=new ArrayList<>();
    List<Listing> tempLikedAds=new ArrayList<>();
    List<Listing> tempListedAds=new ArrayList<>();


    //this populates a user object with the current users info from the users collection
    //including their listed and liked adds
    public User() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.uid=uid;
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        this.userName=userName;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference users = firebaseFirestore.collection("Users");
        users.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tempLikedAds=(List<Listing>)document.get("likedAds");
                        tempListedAds=(List<Listing>)document.get("listedAds");
                        updateArrayLists(tempLikedAds,tempListedAds);
                        updateUserInDB();
                    }
                }
            }
        });
    }

    public User(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
        this.likedAds = new ArrayList<>();
        this.listedAds = new ArrayList<>();
    }

    public User(String uid, String userName, List<Listing> likedAds, List<Listing> listedAds)
    {
        this.uid = uid;
        this.userName = userName;
        this.likedAds = likedAds;
        this.listedAds = listedAds;
    }

    private void updateArrayLists(List<Listing> likedAds, List<Listing> listedAds) {
        for(int i=0; i<likedAds.size();i++)
            this.likedAds.add(likedAds.get(i));
        this.listedAds=listedAds;
        for(int j=0; j<listedAds.size();j++)
            this.listedAds.add(listedAds.get(j));
    }

    //removes old user info and adds the new info
    //use: if a new liked add is added to the arraylist, remove old user info add new.
    private void updateUserInDB() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference users = firebaseFirestore.collection("Users");
        users.document(this.uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    }
                }
            }
        });
        users.document(this.uid).set(new User(this.uid,this.userName,this.likedAds,this.listedAds));
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

    public void addToLikedAdds(Listing e) {
        if (this.likedAds==null){
            ArrayList<Listing> temp=new ArrayList<Listing>();
            this.likedAds=temp;
        }
        else{
            this.likedAds.add(e);
        }
    }

    public void addToListedAdds(Listing e) {
        this.listedAds.add(e);
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
