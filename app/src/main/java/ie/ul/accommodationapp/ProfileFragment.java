package ie.ul.accommodationapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Toolbar mToolbar;
    private TextView profileEditText,inboxText, likedAdsText, listedAdsText, devInfoText;
    private Button logoutButton;
    private Switch nightModeSwitch;
    private SearchView searchView;
    private SharedPreferences sharedPreferences;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        updateUI();
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nightModeSwitch = view.findViewById(R.id.night_mode_switch);
        boolean key = sharedPreferences.getBoolean("nightModeEnabled", false);
        nightModeSwitch.setChecked(key);
        profileEditText = view.findViewById(R.id.profile_edit_tag);
        inboxText = view.findViewById(R.id.inbox_item);
        likedAdsText = view.findViewById(R.id.liked_ads_header);
        listedAdsText = view.findViewById(R.id.listed_ads_header);
        devInfoText = view.findViewById(R.id.developer_information_header);
        logoutButton = view.findViewById(R.id.logout_button);

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putBoolean("nightModeEnabled", true).apply();
                    ((BottomNavigationActivity) getActivity()).toggleNightMode(true);
                    getActivity().recreate();
                } else {
                    editor.putBoolean("nightModeEnabled", false).apply();
                    ((BottomNavigationActivity) getActivity()).toggleNightMode(false);
                    getActivity().recreate();
                }
            }
        });

        inboxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_inboxFragment);
                Navigation.findNavController(v).navigate(R.id.action_global_inboxFragment3);
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

    public void updateUI() {
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(null);
        mToolbar.setTitle("Profile");
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
    }
}
