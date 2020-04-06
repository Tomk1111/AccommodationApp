package ie.ul.accommodationapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikedAdsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference notebookRef = db.collection("LikedAds/" + uid + "/userLikes");

    private ListAdapter listAdapter;
    public LikedAdsFragment() {
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

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_listed_ads);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Listing listing = documentSnapshot.toObject(Listing.class);
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


