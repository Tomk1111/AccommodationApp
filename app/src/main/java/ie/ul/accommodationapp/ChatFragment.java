package ie.ul.accommodationapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * */
public class ChatFragment extends Fragment {

    private Toolbar mToolbar;
    private View MessageView;
    private FirebaseAuth mAuth;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MessageView = inflater.inflate(R.layout.fragment_chat, container, false);


        //needed for the toolbar
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return MessageView;
    }
}
