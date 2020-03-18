package ie.ul.accommodationapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeListFragment extends Fragment {

    int id;
    private static final int PICK_PHOTO_FOR_LISTING = 0;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference listingImagesRef = null;
    InputStream inputStream;


    public HomeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_list, container, false);
    }

    @Override
    public void onViewCreated(@Nonnull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference listing1 = db.collection("Listings");
        listing1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        id++;
                    }
                    id++;
                }
            }
        });
        final GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCJorsEbO8BWUmHINg18AXww1wJpItBqQg")
                .build();
        Button attachImage = view.findViewById(R.id.attachImage);
        attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FOR_LISTING);
            }
        });
        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText address = getView().findViewById(R.id.address);
                EditText rooms = getView().findViewById(R.id.rooms);
                EditText price = getView().findViewById(R.id.price);
                EditText description = getView().findViewById(R.id.description);
                EditText startDate = getView().findViewById(R.id.startDate);
                EditText endDate = getView().findViewById(R.id.endDate);
                SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    String addressText = address.getText().toString();
                    GeocodingResult[] results =  GeocodingApi.geocode(context,
                            addressText).await();
                    LatLng coords  = (results[0].geometry.location);
                    int priceInt = Integer.parseInt(price.getText().toString());
                    int roomInt = Integer.parseInt(rooms.getText().toString());
                    Date sDate = formatter1.parse(startDate.getText().toString());
                    Date eDate = formatter1.parse(endDate.getText().toString());
                    long diff = eDate.getTime() - sDate.getTime();
                    int difInt = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    Listing newHouse = new Listing(id,coords.lng,coords.lat,
                            addressText,roomInt,priceInt,
                            description.getText().toString(),sDate,eDate, difInt, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
                    StrictMode.setThreadPolicy(policy);
                    listing1.document("House"+id).set(newHouse);
                    address.setText("");
                    rooms.setText("");
                    price.setText("");
                    description.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    Toast.makeText(getActivity(), "Successfully Added Listing.", Toast.LENGTH_SHORT).show();
                    if(inputStream != null){
                        listingImagesRef = storageRef.child("House"+id+".jpg");
                        UploadTask uploadTask = listingImagesRef.putStream(inputStream);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                inputStream = null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getActivity(), "Successfully Uploaded Image.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_LISTING && resultCode == getActivity().RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                Toast.makeText(getActivity(), "Successfully Attached Image.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {}
        }
    }

}
