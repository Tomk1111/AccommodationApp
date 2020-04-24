package ie.ul.accommodationapp;

import android.content.Intent;
import android.graphics.PorterDuff;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button loginButton;
    private ProgressBar progressBar;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        loginButton = view.findViewById(R.id.login_button);
        emailField = view.findViewById(R.id.email_input_field);
        passwordField = view.findViewById(R.id.password_input_field);
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                firebaseLogin();
            }
        });
        return view;
    }

    public void firebaseLogin() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (validateFields(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
                                progressBar.setVisibility(View.GONE);
                                loginButton.setVisibility(View.VISIBLE);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                loginButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    public boolean validateFields(String email, String password) {
        if (email.isEmpty() || email == null) {
            Toast.makeText(getContext(), "Please enter email.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty() || password == null) {
            Toast.makeText(getContext(), "Please enter password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
