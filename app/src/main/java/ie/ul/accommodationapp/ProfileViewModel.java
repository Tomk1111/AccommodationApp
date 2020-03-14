package ie.ul.accommodationapp;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private String nameText;
    private String emailText;


    public ProfileViewModel() {
        mAuth = FirebaseAuth.getInstance();
        nameText = mAuth.getCurrentUser().getDisplayName();
        emailText = mAuth.getCurrentUser().getEmail();
    }

    public String getNameText() {
        return nameText;
    }

    public String getEmailText() {
        return emailText;
    }
}
