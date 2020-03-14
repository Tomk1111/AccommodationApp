package ie.ul.accommodationapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private LinearLayout personalInformationOption, likedAdsOption, listedAdsOption,
            inboxOption, aboutOption, signOutOption;
    private TextView displayName;
    private ProfileViewModel profileViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        displayName = view.findViewById(R.id.profile_display_name);
        displayName.setText(profileViewModel.getNameText());
        personalInformationOption = view.findViewById(R.id.personal_details_item);
        personalInformationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_personalInformationFragment);
            }
        });
        likedAdsOption = view.findViewById(R.id.liked_ads_item);
        likedAdsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_likedAdsFragment);
            }
        });
        listedAdsOption = view.findViewById(R.id.listed_ads_item);
        listedAdsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_listedAdsFragment);
            }
        });
        inboxOption = view.findViewById(R.id.inbox_item);
        inboxOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_inboxFragment);
            }
        });
        aboutOption = view.findViewById(R.id.about_item);
        aboutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_aboutFragment);
            }
        });
        signOutOption = view.findViewById(R.id.logout_item);
        signOutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        return view;
    }
}
