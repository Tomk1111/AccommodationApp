package ie.ul.accommodationapp;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
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

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    ArrayList<InputStream> inputStream = new ArrayList<InputStream>();


    private void updateStartDateLabel(EditText startDate, Calendar myCalendar) {
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");

        startDate.setText(formatter1.format(myCalendar.getTime()));
    }

    private void updateEndDateLabel(EditText endDate, Calendar myCalendar) {
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");

        endDate.setText(formatter1.format(myCalendar.getTime()));
    }

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
        final Calendar myCalendar = Calendar.getInstance();
        EditText startDate = getView().findViewById(R.id.startDate);
        EditText endDate = getView().findViewById(R.id.endDate);
        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDateLabel(startDate, myCalendar);
            }
        };
        DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDateLabel(endDate, myCalendar);
            }
        };
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new
                        DatePickerDialog(getContext(), startDateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                // Limiting access to past dates in the step below:
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new
                        DatePickerDialog(getContext(), endDateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                // Limiting access to past dates in the step below:
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                datePickerDialog.show();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final CollectionReference listing1 = db.collection("Listings");
        final CollectionReference listing2 = db.collection("HouseImage");
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
                    if(inputStream != null){
                        int i=1;
                        for (InputStream streamer: inputStream) {
                            listingImagesRef = storageRef.child("House" + id + "image" + i + ".jpg");
                            UploadTask uploadTask = listingImagesRef.putStream(streamer);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    inputStream = null;
                                    LinearLayout linLayout = (LinearLayout) getActivity().findViewById(R.id.linearImageLayout);
                                    linLayout.setVisibility(View.INVISIBLE);
                                    TextView textBanner = (TextView) getActivity().findViewById(R.id.bannerText);
                                    textBanner.setVisibility(View.INVISIBLE);
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getActivity(), "Successfully Uploaded Image.", Toast.LENGTH_SHORT).show();
                                    inputStream = null;
                                    LinearLayout linLayout = (LinearLayout) getActivity().findViewById(R.id.linearImageLayout);
                                    linLayout.setVisibility(View.INVISIBLE);
                                    TextView textBanner = (TextView) getActivity().findViewById(R.id.bannerText);
                                    textBanner.setVisibility(View.INVISIBLE);
                                }
                            });
                            Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(
                                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if(!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            return listingImagesRef.getDownloadUrl();
                                        }
                                    }
                            );

                            getDownloadUriTask.addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()) {
                                        Map<String, Object> houseImage= new HashMap<>();
                                        houseImage.put("id",id);
                                        houseImage.put("URL",task.getResult().toString());
                                        listing2.document("House"+id).set(houseImage);
                                    }
                                }
                            });
                            i++;
                        }
                    }
                    Listing newHouse = new Listing(id,coords.lng,coords.lat,
                            addressText,roomInt,priceInt,
                            description.getText().toString(),sDate,eDate, difInt, FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    try {
                        User currentUser = new User();
                    } catch (Exception e) {}
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final CollectionReference listedAds = db.collection("ListedAds/" + userId + "/userListed");
                    listedAds.document("House"+id).set(newHouse);
                    listing1.document("House"+id).set(newHouse);
                    address.setText("");
                    rooms.setText("");
                    price.setText("");
                    description.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    Toast.makeText(getActivity(), "Successfully Added Listing.", Toast.LENGTH_SHORT).show();
                    ((BottomNavigationActivity) getActivity()).addListing(newHouse);
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
                inputStream.add(getActivity().getContentResolver().openInputStream(data.getData()));
                LinearLayout linLayout = (LinearLayout) getActivity().findViewById(R.id.linearImageLayout);
                try{
                    Bitmap myBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                    ImageView myImage = getActivity().findViewById(R.id.imageView);
                    //ImageView myImage = new ImageView(getActivity());
                    //myImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
                    //myImage.setMaxHeight(200);
                    //myImage.setMaxWidth(200);
                    myImage.setImageBitmap(myBitmap);
                    linLayout.addView(myImage);
                } catch (Exception e) {}
                Toast.makeText(getActivity(), "Successfully Attached Image.", Toast.LENGTH_SHORT).show();
                linLayout.setVisibility(View.VISIBLE);
                TextView banner = (TextView) getActivity().findViewById(R.id.bannerText);
                banner.setVisibility(View.VISIBLE);
            } catch (Exception e) {}
        }
    }

}
