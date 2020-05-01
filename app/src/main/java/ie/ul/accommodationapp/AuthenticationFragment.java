package ie.ul.accommodationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AuthenticationFragment extends Fragment {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;
    private CardView authButton;
    private TextView titleView;
    private ImageView icon;
    Animation fromBottom;
    Animation fromTop;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        container.removeAllViews();
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
        titleView = view.findViewById(R.id.title_view);
        authButton = view.findViewById(R.id.authButton2);
        icon = view.findViewById(R.id.icon_home);
        fromBottom = AnimationUtils.loadAnimation(getContext(),R.anim.frombottom);
        fromTop = AnimationUtils.loadAnimation(getContext(),R.anim.fromtop);
        authButton.setAnimation(fromBottom);
        titleView.setAnimation(fromTop);
        icon.setAnimation(fromTop);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_authenticationFragment_to_authenticationChoiceFragment);
            }
        });
        return view;
    }
}