package com.crimsonscythe.edge;

import android.Manifest;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * LATEST DEVELOPMENT VERSION
 * VERSION CURRENTLY IN BETA TESTING
 */

/**
 * I love my code
 */

public class MainActivity extends AppCompatActivity {
private Switch serviceswitch;
private Intent serviceintent;

private ImageView imgView;

    ApplicationInfo info;
    PackageManager packageManager;
private boolean done=false;
float dx, dy;

private CheckBox appscheckBox, contactscheckBox, weathercheckBox, calccheckBox;

private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        overlaypermission();

        appscheckBox = findViewById(R.id.apps_panel);
        contactscheckBox = findViewById(R.id.contacts_panel);
        weathercheckBox = findViewById(R.id.weather_panel);
        calccheckBox = findViewById(R.id.calc_panel);

        appscheckBox.setChecked(sharedPreferences.getBoolean("appscheck", true));
        contactscheckBox.setChecked(sharedPreferences.getBoolean("contactscheck", true));
        weathercheckBox.setChecked(sharedPreferences.getBoolean("weathercheck", true));
        calccheckBox.setChecked(sharedPreferences.getBoolean("calccheck", true));

//        Log.i("strange", String.valueOf(sharedPreferences.getBoolean("switch", false)));
        serviceswitch = findViewById(R.id.serviceswitch);
        serviceswitch.setChecked(sharedPreferences.getBoolean("switchState", false));

        final SharedPreferences.Editor editor = sharedPreferences.edit();

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//
//        editor.putInt("width", (int)(0.05*displayMetrics.widthPixels));
//        editor.putInt("height", (int)(0.25*displayMetrics.heightPixels));


        serviceswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    serviceintent = new Intent(MainActivity.this, edge_service.class);

                     DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    serviceintent.putExtra("sWidth", (int)(displayMetrics.widthPixels*0.05));
                    serviceintent.putExtra("sHeight", (int)(displayMetrics.heightPixels*0.20));

                    editor.putBoolean("AppsEdge", appscheckBox.isChecked());
                    editor.putBoolean("ContactsEdge", contactscheckBox.isChecked());
                    editor.putBoolean("WeatherEdge", weathercheckBox.isChecked());
                    editor.putBoolean("CalculatorEdge", calccheckBox.isChecked());

//                    serviceintent.putExtra("AppsEdge", appscheckBox.isChecked());
                    Log.i("Testing", String.valueOf(appscheckBox.isChecked()));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceintent);
                    }else
                    startService(serviceintent);

                }
                else {
                    editor.putBoolean("edgeadded", false);
                    stopService(new Intent(MainActivity.this, edge_service.class).putExtra("stopService", true));
                }


                editor.putBoolean("switchState", serviceswitch.isChecked());
                editor.commit();
            }

        });
//
//        View frag = (View) findViewById(R.id.frag);
//
//        load1 load2 = new load1(this, frag);
//        load2.execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},555);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    public void button (View view){
        Intent intent =  new Intent(this, applist.class);
        startActivityForResult(intent, 8967);
    }

    private void overlaypermission (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.edge_settings:
                Intent intent = new Intent(this, EdgeSettings.class);
                startActivity(intent);
                break;
            case R.id.reorder:
                Intent intent1 = new Intent(this, PanelOrder.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void clicked (View v){
        serviceswitch.setChecked(false);
//        Toast.makeText(this, "Please restart service", Toast.LENGTH_LONG).show();
        switch (v.getId()){
            case R.id.apps_linear_layout:
                if (appscheckBox.isChecked()) {
                    appscheckBox.setChecked(false);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        startForegroundService(new Intent(MainActivity.this, edge_service.class).putExtra("hideApps", true));
//                    }else
//                        startService(new Intent(MainActivity.this, edge_service.class).putExtra("hideApps", true));

                    break;
                }
                else {

//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        startForegroundService(new Intent(MainActivity.this, edge_service.class).putExtra("hideApps", false));
//                    }else
//                        startService(new Intent(MainActivity.this, edge_service.class).putExtra("hideApps", false));


                    appscheckBox.setChecked(true);
                    break;
                }
            case R.id.contacts_linear_layout:
                if (contactscheckBox.isChecked()) {
                    contactscheckBox.setChecked(false);
                    break;
                }
                else {
                    contactscheckBox.setChecked(true);
                    break;
                }
            case R.id.weather_linear_layout:
                if (weathercheckBox.isChecked()) {
                    weathercheckBox.setChecked(false);
                    break;
                }
                else {
                    weathercheckBox.setChecked(true);
                    break;
                }
            case R.id.calculator_linear_layout:
                if (calccheckBox.isChecked()) {
                    calccheckBox.setChecked(false);
                    break;
                }
                else {
                    calccheckBox.setChecked(true);
                    break;
                }

        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("appscheck", appscheckBox.isChecked());
        editor.putBoolean("contactscheck", contactscheckBox.isChecked());
        editor.putBoolean("weathercheck", weathercheckBox.isChecked());
        editor.putBoolean("calccheck", calccheckBox.isChecked());
        editor.commit();
    }

}