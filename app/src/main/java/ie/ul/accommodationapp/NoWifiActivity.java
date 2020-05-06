package ie.ul.accommodationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class NoWifiActivity extends AppCompatActivity {

    private WifiManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_wifi);
        manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiState){
                case WifiManager.WIFI_STATE_ENABLED:
                    System.out.println("wifi on");
                    Intent i = new Intent(getApplicationContext(),BottomNavigationActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    System.out.println("wifi off");
                    break;
            }
        }
    };
}
