package com.crimsonscythe.edge;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kwabenaberko.openweathermaplib.Lang;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.io.IOException;
import java.security.Provider;
import java.util.List;
import java.util.Locale;

public class WeatherViewFragment extends android.app.Fragment {

    String API_KEY = "bdba85493d24d10eb9f42f9a25d4ca6f";
    TextView currentTemp, currentLocation, lowview, highview;
    private FusedLocationProviderClient fusedLocationProviderClient;
    ImageView weathericon;
    SharedPreferences sharedPreferences;
    private LruCache lruCache;
    private String icon;
    private long TIME_THRESHHOLD = (60*60000);
//    private long TIME_THRESHHOLD = (0);

    private long elapsedTime;
    private double latitude;
    private double longitude;




    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weatherview, container, false);

        //permissions();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        currentTemp = view.findViewById(R.id.currenttemp);
        currentLocation = view.findViewById(R.id.currentlocation);
        lowview = view.findViewById(R.id.templow);
        highview = view.findViewById(R.id.temphigh);
        weathericon = view.findViewById(R.id.weathericon);




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (fusedLocationProviderClient.getLastLocation()==null){
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading", "Getting your location..",
                    true, true);

            // get location
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = String.valueOf(locationManager.getBestProvider(criteria, true));
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    progressDialog.dismiss();
                    Log.i("Lat", String.valueOf(location.getLatitude()));
                    Log.i("Long", String.valueOf(location.getLongitude()));

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currentLocation.setText(list.get(0).getLocality());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            }, null);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
//                    try {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.i("ohmy", "address is:" + list.get(0));
                        currentLocation.setText(list.get(0).getLocality());
//                    } catch (NullPointerException e){Log.i("null pointer", "null");e.printStackTrace();}
                    } catch (IOException e) {
                        Log.i("stupid", "stupid");
                        e.printStackTrace();
                    }
                }
            });
        }

        Log.i("xxxx", String.valueOf(System.currentTimeMillis()));
        Log.i("xxxxx", String.valueOf(TIME_THRESHHOLD));
        Log.i("xxxxxx" , (String.valueOf(System.currentTimeMillis() - sharedPreferences.getLong("starttime" , System.currentTimeMillis()+2*TIME_THRESHHOLD))));

        if ((System.currentTimeMillis() - sharedPreferences.getLong("starttime" , 0))>=TIME_THRESHHOLD){

            saveData(true);
            Initialize();

        } else {

            currentLocation.setText(sharedPreferences.getString("location", ""));
            currentTemp.setText(sharedPreferences.getString("temp", ""));
            lowview.setText(sharedPreferences.getString("tempmin", ""));
            highview.setText(sharedPreferences.getString("tempmax", ""));
            setIcon(sharedPreferences.getString("icon", ""));
        }
        return view;
    }



    public void Initialize(){
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper();
        helper.setApiKey(API_KEY);
        helper.setUnits(Units.METRIC);
        helper.setLang(Lang.ENGLISH);



        helper.getCurrentWeatherByGeoCoordinates(latitude, longitude, new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {

                currentTemp.setText(String.valueOf((int) currentWeather.getMain().getTemp())+"°C\n"+currentWeather.getWeatherArray().get(0).getDescription());
                lowview.setText("low\n"+String.valueOf((int) Math.floor(currentWeather.getMain().getTempMin())));
                highview.setText("high\n"+String.valueOf((int) Math.ceil(currentWeather.getMain().getTempMax())));

//                weathericon.setImageIcon(currentWeather.getWeatherArray().get(0).getIcon());

                icon = currentWeather.getWeatherArray().get(0).getIcon();

                saveData(false);

                setIcon(icon);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getActivity(), "Error retrieving weather data :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void permissions(){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},666);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},777);
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void saveData (boolean time){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!time) {

            editor.putString("temp", currentTemp.getText().toString());
            editor.putString("tempmax", lowview.getText().toString());
            editor.putString("tempmin", highview.getText().toString());
            editor.putString("icon", icon);
            editor.putString("location", currentLocation.getText().toString());


        } else {
            elapsedTime = System.currentTimeMillis();
            editor.putLong("starttime", elapsedTime);
        }

        editor.commit();
    }


    public void setIcon (String icon){

        switch (icon){
            case "01d":weathericon.setImageResource(R.drawable.ic_weather_sunny);break;
            case "02d":weathericon.setImageResource(R.drawable.ic_weather_partlycloudy);break;
            case "03d":weathericon.setImageResource(R.drawable.ic_weather_cloudy);break;
            case "04d":weathericon.setImageResource(R.drawable.ic_weather_cloudy);break;
            case "09d":weathericon.setImageResource(R.drawable.ic_weather_pouring);break;
            case "10d":weathericon.setImageResource(R.drawable.ic_weather_rainy);break;
            case "11d":weathericon.setImageResource(R.drawable.ic_weather_lightning);break;
            case "13d":weathericon.setImageResource(R.drawable.ic_weather_snowy);break;
            case "50d":weathericon.setImageResource(R.drawable.ic_weather_fog);break;
        }
        if (icon.contains("n"))
            weathericon.setImageResource(R.drawable.ic_weather_night);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("temp", currentTemp.getText().toString());
        outState.putString("tempmax" , lowview.getText().toString());
        outState.putString("tempmin", highview.getText().toString());
        outState.putString("icon" , icon);
        outState.putString("location" , currentLocation.getText().toString());

        super.onSaveInstanceState(outState);
    }
}
