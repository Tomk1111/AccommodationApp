package ie.ul.accommodationapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
public class ListedAdsFragment extends Fragment {

    private Toolbar mToolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference notebookRef = db.collection("ListedAds/" + uid + "/userListed");
    private SearchView searchView;
    private SharedPreferences sharedPreferences;
    private ListAdapter listAdapter;

    public ListedAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_listed_ads, container, false);
        updateUI();
        Query query = notebookRef.orderBy("price", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        listAdapter = new ListAdapter(options);
        System.out.println(options.toString());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_listed_ads);
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

    public void updateUI() {
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isNightMode = sharedPreferences.getBoolean("nightModeEnabled", false);
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setTitle("Listed Ads");
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        if (isNightMode) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        } else {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}