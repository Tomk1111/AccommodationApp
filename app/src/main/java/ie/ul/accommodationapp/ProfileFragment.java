package ie.ul.accommodationapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Toolbar mToolbar;
    private TextView profileEditText,inboxText, likedAdsText, listedAdsText, devInfoText;
    private Button logoutButton;
    private SearchView searchView;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(null);

        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        TextView personalInfo = view.findViewById(R.id.personal_information_header_tag);
        profileEditText = view.findViewById(R.id.profile_edit_tag);
        inboxText = view.findViewById(R.id.inbox_item);
        likedAdsText = view.findViewById(R.id.liked_ads_header);
        listedAdsText = view.findViewById(R.id.listed_ads_header);
        devInfoText = view.findViewById(R.id.developer_information_header);
        logoutButton = view.findViewById(R.id.logout_button);
        inboxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_inboxFragment);
            }
        });
        devInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_aboutFragment);
            }
        });
        profileEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_personalInformationFragment);
            }
        });
        likedAdsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_likedAdsFragment2);
            }
        });
        listedAdsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_listedAdsFragment2);
            }
        });





        logoutButton.setOnClickListener(new View.OnClickListener() {
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
