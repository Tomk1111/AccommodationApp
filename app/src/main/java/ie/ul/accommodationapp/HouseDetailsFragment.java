package ie.ul.accommodationapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseDetailsFragment extends Fragment {

    private Toolbar mToolbar;
    private Listing listingModel;
    private TextView addressTextView, pricePerWeekView, descriptionView, roomsView,
            moveInDateView, moveOutViewDate;
    private ImageView listingImage;
    private Button likeButton;
    private Button contactSellerBtn;
    private FirebaseFirestore db;
    private CollectionReference userLikes;
    private String imageURL;
    private View view;
    public HouseDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_details, container, false);
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
        likeButton=view.findViewById(R.id.button_like);
        contactSellerBtn = view.findViewById(R.id.contact_seller);
        listingImage = view.findViewById(R.id.listing_image);
        updateLikeStatus(false);
        getHouseImageURL();
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLikeStatus(true);
            }
        });
        contactSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = listingModel.getUid();
                String username = listingModel.getUserName();
                String url = imageURL;
                System.out.println("Contact seller button pressed");
                Bundle bundle = new Bundle();
                bundle.putString("uid",uid); //string key - pair
                bundle.putString("userName",username); //string key - pair#

                Toast.makeText(getActivity(),"Navigation.findNavController will send this to a new chat",Toast.LENGTH_SHORT).show();
                //Navigation.findNavController(v).navigate(R.id.action_houseDetailsFragment4_to_inboxFragment);
                // cant use navigation to get to either the chat page or the inbox page
                //  java.lang.IllegalArgumentException: navigation destination ie.ul.accommodationapp:id/action_houseDetailsFragment4_to_chatFragment2 is unknown to this NavController

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
                                        likeButton.setBackgroundColor(getResources().getColor(R.color.lightText2));
                                    }
                                }
                            });
                        } else {
                            likeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                    } else {
                        if (buttonPressed) {
                            documentReference.set(listingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), listingModel.getAddress()
                                                + " added to likes.", Toast.LENGTH_SHORT).show();
                                        likeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    }
                                }
                            });
                        }
                        likeButton.setBackgroundColor(getResources().getColor(R.color.lightText2));
                    }
                }
            }
        });
    }

    public void getHouseImageURL() {
        DocumentReference documentReference = db.collection("HouseImage").document("House"+ listingModel.getId()+"");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        imageURL = documentSnapshot.getString("URL");
                        Picasso.get().load(imageURL).fit().into(listingImage);
                    }
                }
                }
        });
    }
}
