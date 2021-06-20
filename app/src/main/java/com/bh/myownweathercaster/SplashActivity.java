package com.bh.myownweathercaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast toast_GPS_setting = Toast.makeText(this, "GPS 설정이 꺼져있습니다\n설정을 통해 GPS를 켠 후 앱을 재실행해주세요\n잠시 후 설정으로 이동합니다", Toast.LENGTH_LONG);
            toast_GPS_setting.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                    finish();
                }
            }, 3000);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkInternetState();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                permissionCloseNow();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkInternetState();
            }
        }
    }

    private void permissionCloseNow() {
        Toast toast_permission = Toast.makeText(this, "권한이 허가되지 않았습니다\n재실행 혹은 설정을 통해 권한을 허가해주세요", Toast.LENGTH_LONG);
        toast_permission.show();
        Intent intent = new Intent(this, SplashErrorActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void checkInternetState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())) {
            networkCloseNow();
        } else {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    private void networkCloseNow() {
        Toast toast_permission = Toast.makeText(this, "네트워크가 연결되어 있지 않거나 문제가 있습니다\n잠시 후에 다시 시도해주세요", Toast.LENGTH_LONG);
        toast_permission.show();
        Intent intent = new Intent(this, SplashErrorActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}