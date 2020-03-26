package ie.ul.accommodationapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseDetailsFragment extends Fragment {

    private Listing listingModel;
    private TextView addressTextView, pricePerWeekView, descriptionView, roomsView,
            moveInDateView, moveOutViewDate;
    private Button button;

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            User currentUser=new User();
            currentUser.addToLikedAdds(listingModel);
            }
        });

        return view;
    }
}
