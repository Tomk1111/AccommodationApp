package ie.ul.accommodationapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseDetailsFragment extends Fragment {

    private Toolbar mToolbar;
    private Listing listingModel;
    private TextView addressTextView, pricePerWeekView, descriptionView, roomsView,
            moveInDateView, moveOutViewDate;
    private ImageView listingImage, likeButtonImage;
    private CardView likeButton;
    private Button contactSellerBtn;
    private FirebaseFirestore db;
    protected String imageURL = "";
    protected String listingUserID; // accessed in anonymous class
    protected String listingUserName; // accessed in anonymous class

    private View view;
    private SearchView searchView;
    // Firebase RTDB additions
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private DatabaseReference contactRef;
    private DatabaseReference usersRef;
    private String currentUserId;


    public HouseDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_details, container, false);
        Activity activity = getActivity();
        if (activity != null) {
            updateUI();
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
            likeButton = view.findViewById(R.id.button_like);
            likeButtonImage = view.findViewById(R.id.like_button_image);
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

            //Firebase RTDB setup
            mAuth = FirebaseAuth.getInstance();
            currentUserId = mAuth.getCurrentUser().getUid();

            contactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
            //if newUserID doesnt exist in users, they cannot have any conversations -
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

            if (currentUserId.equals(listingModel.getUid())) {
                //User should not be able to press 'contact seller' or 'like' on a house they list.
                contactSellerBtn.setVisibility(View.GONE);
                likeButton.setVisibility(View.GONE);
                likeButtonImage.setVisibility(View.GONE);
            }

            contactSellerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String uid = listingModel.getUid();
                    String username = listingModel.getUserName();
                    String url = imageURL;

                    String uid1 = currentUserId;
                    String username1 = mAuth.getCurrentUser().getDisplayName();
                    String url1 = ""; //cant get any data for the logged in user here


                    System.out.println("Contact seller button pressed");
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putString("userName", username);
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot documentSnapshot = task.getResult();
//                                String name = documentSnapshot.getString("userName");
//                                bundle.putString("userName", name);
//                                Navigation.findNavController(v).navigate(R.id.action_global_chatFragment, bundle);
//                            } else {
//                                Toast.makeText(getActivity(), "Error communicating with Server", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });



                    // does user with listing have a user account ?
                    // does logged in user have a user account ?
                    // create a conversation

                    //these check if a user exists and creates a user if it doesnt
                    userCheck(uid, username, url);
                    userCheck(uid1, username1, "");
                    createConnection(uid);
                    //change this to the specific message conversation of the two users - change where the global action directs to
                    Navigation.findNavController(v).navigate(R.id.action_global_chatFragment, bundle);
                }
            });
        }
        return view;
    }

    private void userCheck(String uid, String username, String imageURL) {
        Activity activity = getActivity();
        if (activity != null) {

            //check does a user with a listing have a profile in Users and if not create one
            //check does the current user logged in have a profile in users in the RTDB
            usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        System.out.println(dataSnapshot.getValue().toString());
                        //check do they have a profile image - key, if not try and get it from this listing and add it
                        if (dataSnapshot.child("image").getValue().toString().equals("")) {
                            //this user profile has no image
                            if (imageURL.equals("")) {
                                //there is no new house image to use to add to the user profile
                                System.out.println("there is no new house image to use to add to the user profile");
                            } else {
                                //a house image exists, update the user account with this image.
                                HouseDetailsFragment.this.createUser(uid, username, imageURL);
                                System.out.println("add image to profile");
                            }
                        }
                    } else {
                        System.out.println("Creating user profile on RTDB");
                        HouseDetailsFragment.this.createUser(uid, username, imageURL);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void createUser(String uid, String name, String image) {
        // you need the uid, need username, - no image == "", boolean toggle to add a image if available? future feature

        Activity activity = getActivity();
        if (activity != null) {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("image", image);
            profileMap.put("name", name);
            profileMap.put("uid", uid);

            usersRef.child(uid).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "User Created in RealTimeDB", Toast.LENGTH_SHORT).show();

                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(getActivity(), "Error creating user: " + error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    public void createConnection(String uid) {
        //check is there a connection already made
        //Make a connection between two users here
        Activity activity = getActivity();
        if (activity != null) {
            contactRef.child(uid).child(currentUserId).child("Chat").setValue("Yes")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                contactRef.child(currentUserId).child(uid).child("Chat").setValue("Yes")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(getActivity(), "Your new connection is in INBOX", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    public void updateLikeStatus(boolean buttonPressed) {
        Activity activity = getActivity();
        if (activity != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("LikedAds/" + uid + "/userLikes").document(listingModel.getId() + "");
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
                                            likeButton.setCardBackgroundColor(getResources().getColor(R.color.personalInfoCards));
                                            likeButtonImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                                        }
                                    }
                                });
                            } else {
                                likeButton.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                likeButtonImage.setImageResource(R.drawable.ic_favorite_white_24dp);
                            }
                        } else {
                            if (buttonPressed) {
                                documentReference.set(listingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), listingModel.getAddress()
                                                    + " added to likes.", Toast.LENGTH_SHORT).show();
                                            likeButton.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                            likeButtonImage.setImageResource(R.drawable.ic_favorite_white_24dp);
                                        }
                                    }
                                });
                            }
                            likeButton.setCardBackgroundColor(getResources().getColor(R.color.personalInfoCards));
                            likeButtonImage.setImageResource(R.drawable.ic_favorite_black_24dp);
//                        likeButton.setBackgroundColor(getResources().getColor(R.color.lightText2));
                        }
                    }
                }
            });
        }
    }

    public void updateUI() {
        Activity activity = getActivity();
        if (activity != null) {
            sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            boolean isNightMode = sharedPreferences.getBoolean("nightModeEnabled", false);
            mToolbar = getActivity().findViewById(R.id.main_toolbar);
            mToolbar.setTitle("Listing");
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


    public void getHouseImageURL() {
        Activity activity = getActivity();
        if (activity != null) {
            DocumentReference documentReference = db.collection("HouseImage").document("House" + listingModel.getId() + "");
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
}
