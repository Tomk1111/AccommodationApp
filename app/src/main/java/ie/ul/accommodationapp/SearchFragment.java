package ie.ul.accommodationapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ie.ul.accommodationapp.Adapters.HomeAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchView searchView;
    private Toolbar mToolbar;
    private EditText searchText;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Listing> mData;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Listings");


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        updateUI();
        recyclerView = view.findViewById(R.id.house_listing_recycler_view);
        retrieveListings();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                homeAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return view;
    }

    public void updateUI() {
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isNightMode = sharedPreferences.getBoolean("nightModeEnabled", false);
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(null);
        mToolbar.setTitle("Search");
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
    }



    public void retrieveListings() {

        mData = ((BottomNavigationActivity) getActivity()).getListings();
        homeAdapter = new HomeAdapter(getContext(), mData);
        recyclerView.setAdapter(homeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


}
