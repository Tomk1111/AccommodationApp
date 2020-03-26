package ie.ul.accommodationapp;


import android.os.Bundle;	import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;	import android.view.LayoutInflater;
import android.view.View;	import android.view.View;
import android.view.ViewGroup;	import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;	import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ie.ul.accommodationapp.Adapters.HomeAdapter;
import io.grpc.InternalDecompressorRegistry;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListedAdsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Listing> mData;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Listings");

    public ListedAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_liked_ads, container, false);
        recyclerView = view.findViewById(R.id.house_listing_recycler_view_1);
        retrieveListings();
        return view;
    }

    public void retrieveListings() {


        mData = new ArrayList<>();
//        mData.add(new Listing(11111, 232, 223, "aaa", 2, 23, "test", new Date(),
//                new Date(),  200, "a"));
        User user=new User();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference users = firebaseFirestore.collection("Users");
        users.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList a = (ArrayList) document.get("listedAds");
                        for ( Object b: a)
                        {
                            HashMap hashMap1=((HashMap)b);
                            Long id= (Long) hashMap1.get("id");
                            Integer id_1=id.intValue();
                            double longitude= (double) hashMap1.get("longitude");
                            double latitude= (double) hashMap1.get("latitude");
                            String address= (String) hashMap1.get("address");
                            Long rooms= (Long) hashMap1.get("rooms");
                            Integer rooms_1=rooms.intValue();
                            Long price= (Long) hashMap1.get("price");
                            Integer price_1=price.intValue();
                            String description= (String) hashMap1.get("descrition");
                            Date startDate= (Date) hashMap1.get("startDate");
                            Date endDate= (Date) hashMap1.get("endDate");
                            Long duration= (Long) hashMap1.get("duration");
                            Integer duration_1=duration.intValue();
                            String uid= (String) hashMap1.get("uid");
                            String userName= (String) hashMap1.get("userName");
                            Listing listing=new Listing(id_1,longitude,latitude,address,rooms_1,price_1,description,startDate,endDate,duration_1,uid,userName);
                            mData.add(listing);
                        }
                        homeAdapter = new HomeAdapter(getContext(), mData);
                        recyclerView.setAdapter(homeAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setStackFromEnd(true);
                        linearLayoutManager.setReverseLayout(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }
        });

    }

}
