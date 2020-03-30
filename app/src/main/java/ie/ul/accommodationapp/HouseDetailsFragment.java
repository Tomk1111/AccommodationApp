package ie.ul.accommodationapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseDetailsFragment extends Fragment {

    private Listing listingModel;
    private TextView addressTextView, pricePerWeekView, descriptionView, roomsView,
            moveInDateView, moveOutViewDate;
    private Button button;
    private FirebaseFirestore db;
    private CollectionReference userLikes;
    public HouseDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_details, container, false);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        listingModel = getArguments().getParcelable("listingModel");
        db = FirebaseFirestore.getInstance();
        addressTextView = view.findViewById(R.id.address_view);
        pricePerWeekView = view.findViewById(R.id.price_view);
        descriptionView = view.findViewById(R.id.description_view);
        roomsView = view.findViewById(R.id.rooms_view);
        moveInDateView = view.findViewById(R.id.move_in_view);
        moveOutViewDate = view.findViewById(R.id.move_out_view);
        addressTextView.setText(listingModel.getAddress());
        pricePerWeekView.setText("€" + listingModel.getPrice());
        descriptionView.setText(listingModel.getDescription());
        roomsView.setText(listingModel.getRooms() + " rooms");
        moveInDateView.setText(simpleDateFormat.format(listingModel.getStartDate()));
        moveOutViewDate.setText(simpleDateFormat.format(listingModel.getEndDate()));
        button=view.findViewById(R.id.button_like);

        updateLikeStatus(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLikeStatus(true);
            }
        });




        return view;
    }

    public void updateLikeStatus(boolean buttonPressed) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("LikedAds/" + uid + "/userLikes").document(listingModel.getId()+"");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        if (buttonPressed) {
                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), listingModel.getAddress()
                                                + " removed from likes.", Toast.LENGTH_SHORT).show();
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    }
                                }
                            });
                        } else {
                            button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                    } else {
                        if (buttonPressed) {
                            documentReference.set(listingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), listingModel.getAddress()
                                                + " added to likes.", Toast.LENGTH_SHORT).show();
                                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    }
                                }
                            });
                        }
                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
        });
    }
}
