package ie.ul.accommodationapp;


import android.os.Bundle;	import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;	import android.view.LayoutInflater;
import android.view.View;	import android.view.View;
import android.view.ViewGroup;	import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;	import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //    private CollectionReference notebookRef = db.collection("Listings");
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference notebookRef = db.collection("ListedAds/" + uid + "/userListed");

    private ListAdapter listAdapter;
    public ListedAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_listed_ads, container, false);
        Query query = notebookRef.orderBy("price", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        listAdapter = new ListAdapter(options);
        System.out.println(options.toString());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_listed_ads);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("listingModel", listing);
                Navigation.findNavController(view).navigate(R.id.action_listedAdsFragment_to_houseDetailsFragment4, bundle);
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        listAdapter.stopListening();
    }
}
