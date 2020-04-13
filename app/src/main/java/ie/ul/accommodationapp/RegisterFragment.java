package ie.ul.accommodationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private EditText usernameField, emailField, passwordField, passwordField2;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        usernameField = view.findViewById(R.id.username_input_field);
        emailField = view.findViewById(R.id.email_input_field);
        passwordField = view.findViewById(R.id.password_input_field);
        passwordField2 = view.findViewById(R.id.password_input_field2);
        registerButton = view.findViewById(R.id.signup_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.GONE);
                firebaseSignUp();
            }
        });
        return view;
    }
    public void firebaseSignUp() {
        final String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String password2 = passwordField2.getText().toString();
        if (validateFields(username, email, password, password2)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                registerButton.setVisibility(View.VISIBLE);
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                updateUser(username);
                            } else {
                                Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                registerButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            progressBar.setVisibility(View.GONE);
            registerButton.setVisibility(View.VISIBLE);
        }
    }

    public void updateUser(final String username) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        mUser.updateProfile(profileChangeRequest);
        User user = new User(mUser.getUid(), username);
        db.collection("Users").document(mUser.getUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), username + " registered.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {

                        }
                    }
                });
    }


    public boolean validateFields(String username, String email, String password, String password2) {
        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Please enter email.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username == null || username.isEmpty()){
            Toast.makeText(getContext(), "Please enter username.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else  {
            if (password == null || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter password.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (password2 == null || password2.isEmpty()) {
                Toast.makeText(getContext(), "Please confirm password.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!password.equals(password2)){
                Toast.makeText(getContext(), "Passwords not matching.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
