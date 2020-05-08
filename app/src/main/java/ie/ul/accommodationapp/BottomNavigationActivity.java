package ie.ul.accommodationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private NetworkChangeReceiver receiver;
    //private ConnectivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        //manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mToolbar = findViewById(R.id.main_toolbar);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean nightModeKey = sharedPreferences.getBoolean("nightModeEnabled", false);
        toggleNightMode(nightModeKey);
        setSupportActionBar(mToolbar);
        navController = Navigation.findNavController(this, R.id.bottom_navigation_fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void toggleNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

   @Override
    protected void onStart() {
        super.onStart();
        receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                if (isOnline(context)) {
                    System.out.println("wifi on");
                } else {
                    Intent i = new Intent(getApplicationContext(),NoWifiActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
