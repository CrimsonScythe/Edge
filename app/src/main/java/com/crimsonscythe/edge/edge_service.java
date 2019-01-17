package com.crimsonscythe.edge;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.hardware.display.DisplayManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class edge_service extends Service{

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private ImageView imageView;
    private boolean longclicked = false;
    private int width;

    private float dy;
    int Layoutparam;
    String CHANNEL_ID = "esque";

    private SharedPreferences sharedPreferences;


    private boolean AppsEdge;

    private int moveEdge;

    private int pos;

    int defaultWidth=50;
    int defaultHeight=450;

    public edge_service() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AppsEdge=intent.getBooleanExtra("AppsEdge",true);
        Log.i("in_service", String.valueOf(AppsEdge));

        if (intent.getBooleanExtra("stopService", false)){
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.removeView(this.imageView);
            stopSelf();
        }

        defaultWidth = intent.getIntExtra("sWidth", 50);
        defaultHeight = intent.getIntExtra("sHeight", 450);

        Log.i("Width is: ", String.valueOf(defaultHeight));
        Log.i("Height is: ", String.valueOf(defaultWidth));

        if (intent.getBooleanExtra("changed", false)){
//            this.layoutParams.y = intent.getIntExtra("update", 0);
//            this.windowManager.updateViewLayout(this.imageView, this.layoutParams);
//            this.layoutParams.y = this.sharedPreferences.getInt("Sppos", 0);
//            this.windowManager.updateViewLayout(this.imageView, this.layoutParams);
        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(edge_service.this);



        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "s", NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("ddd");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(edge_service.this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("service is running")
                .setContentText("tap to stop")
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(5555, builder.build());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Layoutparam = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            Layoutparam = WindowManager.LayoutParams.TYPE_PHONE;
        }

        SetParams();

        imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.BLUE);

        imageView.setBackgroundResource(R.drawable.edge);

        /**
         * setting alpha below? new?
         */

        imageView.setAlpha(sharedPreferences.getFloat("alpha", 0.5f));



        final GestureDetector gestureDetector = new android.view.GestureDetector(this, new MyGestureDetector());

        /**
         * if statement below gives trouble, fix before production
         */
        //TODO:FIX BELOW
        if (!sharedPreferences.getBoolean("edgeadded", false)){
            windowManager.addView(imageView, layoutParams);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("edgeadded", true);
            editor.apply();
        }

        int SpWidth;
        int SpHeight;

        /**
         * original default values:
         * width: 50
         * height: 450
         */


        SpWidth = sharedPreferences.getInt("width", defaultWidth);
        SpHeight = sharedPreferences.getInt("height", defaultHeight);


        //default value for wifth is 20
        imageView.getLayoutParams().width = SpWidth;
        width=imageView.getLayoutParams().width;
        imageView.getLayoutParams().height = SpHeight;

        layoutParams.y = sharedPreferences.getInt("position", 0);

        windowManager.updateViewLayout(imageView, layoutParams);


        final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dy = event.getRawY() - layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (longclicked) {
                            layoutParams.y = Math.round(event.getRawY() - dy);
                            windowManager.updateViewLayout(imageView, layoutParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP: {
                        longclicked = false;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("pos", layoutParams.y);
                        editor.putBoolean("posis", true);
                        editor.putInt("position", layoutParams.y);
                        editor.commit();
                    }

                }
                return gestureDetector.onTouchEvent(event);
            }
        };

        imageView.setOnTouchListener(onTouchListener);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longclicked=true;
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator.vibrate(VibrationEffect.createOneShot(50, 255));
                else
                    vibrator.vibrate(50);
                return false;
            }
        });


        /**
         * Previously return START_STICKY
         */
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onLowMemory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("edgeadded", false);
        editor.commit();

        super.onLowMemory();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("edgeadded", false);
        editor.commit();

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {

        windowManager.removeView(imageView);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("edgeadded", false);
        editor.commit();

        super.onDestroy();

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    private void SetParams (){

        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Layoutparam,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        layoutParams.gravity = Gravity.START;

        if (sharedPreferences.getBoolean("posis", false))
            layoutParams.y = sharedPreferences.getInt("pos", 0);

        /**
         * new below
         */
//        layoutParams.y = sharedPreferences.getInt("Sppos", 0);

//        layoutParams.y = 50;
        layoutParams.x = 0;

    }


private class MyGestureDetector extends android.view.GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            try{
                if (e2.getX() - e1.getX() > width && Math.abs(velocityX) > 1){

                    /**
                     * Hide handle
                     */

//                    windowManager.removeView(imageView);

                    Intent intent = new Intent(edge_service.this, edge_activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    /**
                     * add and remove functionality below
                     */

//                    Toast.makeText(edge_service.this, ""+hideApps, Toast.LENGTH_SHORT).show();
                    intent.putExtra("AppsEdge", AppsEdge);

                    PendingIntent intent1 = PendingIntent.getActivity(edge_service.this, 0,intent,0);
                    try {
                        intent1.send();
                    }catch (PendingIntent.CanceledException e){
                        e.printStackTrace();
                    }

                    windowManager.removeView(imageView);
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(edge_service.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("edgeadded",false);
                    editor.apply();

//                    stopSelf();

                }
            }catch (Exception e){

            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }



}
