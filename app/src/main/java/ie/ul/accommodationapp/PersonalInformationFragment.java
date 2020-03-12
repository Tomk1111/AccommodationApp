package ie.ul.accommodationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class PersonalInformationFragment extends Fragment {
    private TextView nameText, emailText, dateJoinedText;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        nameText = view.findViewById(R.id.name_item);
        emailText = view.findViewById(R.id.email_item);
        dateJoinedText = view.findViewById(R.id.date_joined_item);
        mAuth = FirebaseAuth.getInstance();
        nameText.setText(mAuth.getCurrentUser().getDisplayName());
        emailText.setText(mAuth.getCurrentUser().getEmail());
        return view;
    }
}
