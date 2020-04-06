package ie.ul.accommodationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        navController = Navigation.findNavController(this, R.id.bottom_navigation_fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
