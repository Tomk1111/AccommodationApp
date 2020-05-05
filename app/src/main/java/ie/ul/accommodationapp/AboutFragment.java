package ie.ul.accommodationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private Toolbar mToolbar;
    private SharedPreferences sharedPreferences;
    private SearchView searchView;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        updateUI();
        return view;
    }

    public void updateUI() {
            sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            boolean isNightMode = sharedPreferences.getBoolean("nightModeEnabled", false);
            mToolbar = getActivity().findViewById(R.id.main_toolbar);
            mToolbar.setTitle("Developer Information");
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
