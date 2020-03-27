package ie.ul.accommodationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

public class PersonalInformationFragment extends Fragment {
    private TextView nameText, emailText, dateJoinedText,lastLoginText;
    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        nameText = view.findViewById(R.id.name_item);
        emailText = view.findViewById(R.id.email_item);
        dateJoinedText = view.findViewById(R.id.date_joined_item);
        lastLoginText = view.findViewById(R.id.date_lastLogin);
        mAuth = FirebaseAuth.getInstance();
        nameText.setText(profileViewModel.getNameText());
        emailText.setText(profileViewModel.getEmailText());
        dateJoinedText.setText(profileViewModel.getJoined());
        lastLoginText.setText(profileViewModel.getLastLogin());
        return view;
    }
}
