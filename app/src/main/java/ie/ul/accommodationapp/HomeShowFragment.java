package ie.ul.accommodationapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ie.ul.accommodationapp.Adapters.HomeAdapter;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeShowFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Listings");


    private ListAdapter adapter;
    private int previousPosition=-1;
    public HomeShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_show, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        Spinner filterbutton=view.findViewById(R.id.filterbutton);
        //Creating the ArrayAdapter instance having the country list
        String[] filters = { "price (low to high)", "price (high to low)", "rooms (low to high)", "rooms (high to low)",
                "startDate (low to high)", "startDate (high to low)", "endDate (low to high)", "endDate (high to low)"};
        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext(),R.layout.simple_spinner,filters);
        aa.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        filterbutton.setAdapter(aa);
        Query query = notebookRef.orderBy("price", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .build();
        System.out.println((options.getSnapshots().toArray().length));
        adapter = new ListAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("listingModel", listing);
                Navigation.findNavController(view).navigate(R.id.action_homeTabFragment4_to_houseDetailsFragment, bundle);
            }
        });
        filterbutton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
//                if (position!=previousPosition)
//                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                previousPosition=position;
                if (item.contains("(low to high)")) {
                    String tmp = item.replace(" (low to high)", "");
                    notebookRef
                            .orderBy(tmp,Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    System.out.println(queryDocumentSnapshots.toObjects(Listing.class));
                                    List <Listing>ab=new ArrayList<Listing>();
                                    ab=queryDocumentSnapshots.toObjects(Listing.class);
                                    HomeAdapter homeAdapter= new HomeAdapter(getContext(), ab);
                                    recyclerView.setAdapter(homeAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                    linearLayoutManager.setStackFromEnd(true);
                                    linearLayoutManager.setReverseLayout(true);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                }
                            });
                } else {
                    String tmp = item.replace(" (high to low)", "");
                    notebookRef
                            .orderBy(tmp,Query.Direction.ASCENDING)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    System.out.println(queryDocumentSnapshots.toObjects(Listing.class));
                                    List <Listing>ab=new ArrayList<Listing>();
                                    ab=queryDocumentSnapshots.toObjects(Listing.class);
                                    HomeAdapter homeAdapter= new HomeAdapter(getContext(), ab);
                                    recyclerView.setAdapter(homeAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                    linearLayoutManager.setStackFromEnd(true);
                                    linearLayoutManager.setReverseLayout(true);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                }
                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
