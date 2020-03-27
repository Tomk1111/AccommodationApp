package ie.ul.accommodationapp;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class ProfileViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private String nameText;
    private String emailText;
    private Date joined;
    private Date lastLogin;


    public ProfileViewModel() {
        mAuth = FirebaseAuth.getInstance();
        nameText = mAuth.getCurrentUser().getDisplayName();
        emailText = mAuth.getCurrentUser().getEmail();
        long b=mAuth.getCurrentUser().getMetadata().getCreationTimestamp();
        joined = new Date(b);
        long a=mAuth.getCurrentUser().getMetadata().getLastSignInTimestamp();
        lastLogin = new Date(a);

    }

    public String getNameText() {
        return nameText;
    }

    public String getEmailText() {
        return emailText;
    }

    public String getJoined(){
        return String.valueOf(joined);
    }

    public String getLastLogin(){
        return String.valueOf(lastLogin);
    }
}
