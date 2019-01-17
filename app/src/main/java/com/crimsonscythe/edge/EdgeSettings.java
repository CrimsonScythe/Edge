package com.crimsonscythe.edge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class EdgeSettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private AppCompatSeekBar seekPos, seekSize, seekOpacity;
    private Intent intent;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private ImageView imageView;
    private float dy;
    int Layoutparam;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int deviceWidth;
    int deviceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_settings);

        seekPos = findViewById(R.id.seekEdgePos);
        seekSize = findViewById(R.id.seekEdgeSize);
        seekOpacity = findViewById(R.id.seekEdgeOpc);
        intent = new Intent(this, edge_service.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        seekPos.setOnSeekBarChangeListener(this);
        seekSize.setOnSeekBarChangeListener(this);
        seekOpacity.setOnSeekBarChangeListener(this);

        seekPos.setProgress(sharedPreferences.getInt("posSwitch", 3));
        seekSize.setProgress(sharedPreferences.getInt("sizeSwitch", 1));
        seekOpacity.setProgress(sharedPreferences.getInt("opcSwitch",3));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case android.R.id.home:
//                savePrefs();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getTag().equals("seekPos")){
            int temp=0;
            switch (progress){
                case 3: temp=0;break;
                case 2: temp=-200;break;
                case 1: temp=-400;break;
                case 0: temp=-600;break;
                case 4: temp=200;break;
                case 5: temp=400;break;
                case 6: temp=600;break;
            }

            editor.putInt("position", temp);
            editor.putInt("posSwitch",progress);
        } else if (seekBar.getTag().equals("seekSize")){
            if (progress==0) {
                //25, 225
                editor.putInt("width", (int)(0.025*deviceWidth));
                editor.putInt("height", (int)(0.125*deviceHeight));
            }
            else if (progress==1) {
                //50, 450
                editor.putInt("width", (int)(0.05*deviceWidth));
                editor.putInt("height", (int)(0.20*deviceHeight));
            }
            else {
                //75, 675
                editor.putInt("width", (int)(0.1*deviceWidth));
                editor.putInt("height", (int)(0.5*deviceHeight));
            }
            editor.putInt("sizeSwitch",progress);
        }
        else{
            float temp = 0.6f;
            switch (progress){
                case 0: temp=0.0f;break;
                case 1: temp=0.2f;break;
                case 2: temp=0.4f;break;
                case 3: temp=0.6f;break;
                case 4: temp=0.8f;break;
                case 5: temp=1.0f;break;
            }
            editor.putFloat("alpha", temp);
            editor.putInt("opcSwitch", progress);
        }
//        Toast.makeText(this, "moved", Toast.LENGTH_SHORT).show();

        editor.commit();


        stopService(new Intent(this, edge_service.class).putExtra("stopService", true));
        startService(intent);



    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getTag().equals("seekPos")){

        } else {

        }
    }
}
