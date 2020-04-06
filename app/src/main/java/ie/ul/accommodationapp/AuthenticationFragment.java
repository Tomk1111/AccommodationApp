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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private Button authButton;
    private ImageView icon;
    Animation fromBottom;
    Animation fromTop;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        container.removeAllViews();

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
        authButton = view.findViewById(R.id.authButton);
        icon = view.findViewById(R.id.icon_home);
        fromBottom = AnimationUtils.loadAnimation(getContext(),R.anim.frombottom);
        fromTop = AnimationUtils.loadAnimation(getContext(),R.anim.fromtop);
        authButton.setAnimation(fromBottom);
        icon.setAnimation(fromTop);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                );

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(), RC_SIGN_IN);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUserDatabase(user);
                Context context = getActivity();
                CharSequence text = "Sign in Successful. Welcome " + user.getDisplayName()+ "!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().finish();
                startActivity(intent);
            } else {
                if(response == null){
                    return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Context context = getActivity();
                    CharSequence text = "No Internet Connection.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
            }
        }
    }


    //TEMP METHOD UNTIL WE REDO THE LOGIN/REGISTRATION SCREENS
    // CHECKS USER DB IF USER ALREADY REGISTERED, IF NOT IT ADDS THEM
    public void updateUserDatabase(FirebaseUser user) {
        String uid = user.getUid();
        String userName = user.getDisplayName();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference users = firebaseFirestore.collection("Users");
        users.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    } else {
                        User user = new User(uid, userName);
                        users.document(uid).set(user);
                        //Toast.makeText(getContext(), " Added " + userName, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}