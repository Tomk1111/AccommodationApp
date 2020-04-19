package ie.ul.accommodationapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * */
public class ChatFragment extends Fragment {

    private Toolbar mToolbar;
    private View MessageView;
    private FirebaseAuth mAuth;
    private String messageReceiverID;
    private String messageReceiverName;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values in onCreate not onCreateView
        Bundle chatBundle = this.getArguments();
        //Data validation on the bundle to check whether data was passed or not.
        if (chatBundle != null) {
            messageReceiverID = chatBundle.getString("uid");
            messageReceiverName = chatBundle.getString("userName");
            //need to get one of the users house images too if one exists
            // need to find out if an account has already been created for this user - if from inbox they definitely have
        }
        System.out.println("userid " + messageReceiverID);
        System.out.println("username " + messageReceiverName);
        Toast.makeText(getActivity(), messageReceiverID, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "name: " + messageReceiverName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MessageView = inflater.inflate(R.layout.fragment_chat, container, false);


        /*//needed for the toolbar
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });*/

        return MessageView;
    }
}
