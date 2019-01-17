package com.crimsonscythe.edge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.service.autofill.SaveRequest;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class edge_activity extends Activity{

    private static final int NUMBER_FRAGMENTS = 4;
    private int width;

    private List<ImageView> dots;

    GestureDetector gestureDetector2;

    String FRAG_0 = "FRAG0";
    String FRAG_1 = "FRAG1";
    String FRAG_2 = "FRAG2";
    String FRAG_3 = "FRAG3";

    String KEY_ZERO="zero";
    String KEY_ONE="one";
    String KEY_TWO="two";
    String KEY_THREE="three";

    int indexApps=0;
    int indexContacts=1;
    int indexWeather=2;
    int indexCalc=3;

    String KEY_APPS = "KEY_APPS";
    String KEY_CONTACTS = "KEY_CONTACTS";
    String KEY_WEATHER = "KEY_WEATHER";
    String KEY_CALC = "KEY_CALC";

    public boolean flag=false;

    private static boolean AppsEdge;
    private static boolean ContactsEdge;
    private static boolean WeatherEdge;
    private static boolean CalculatorEdge;


    public List<Integer> list;

    public int counter=0;
    private int selectedDot=0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        AppsEdge = sharedPreferences.getBoolean("AppsEdge", true);
        ContactsEdge = sharedPreferences.getBoolean("ContactsEdge", true);
        WeatherEdge = sharedPreferences.getBoolean("WeatherEdge", true);
        CalculatorEdge = sharedPreferences.getBoolean("CalculatorEdge", true);

        switch (sharedPreferences.getString(KEY_ZERO, "apps")){
            case "apps": indexApps=0;break;
            case "contacts":indexContacts=0;break;
            case "weather": indexWeather=0;break;
            case "calculator":indexCalc=0;break;
        }
        switch (sharedPreferences.getString(KEY_ONE, "contacts")){
            case "apps": indexApps=1;break;
            case "contacts":indexContacts=1;break;
            case "weather": indexWeather=1;break;
            case "calculator":indexCalc=1;break;
        }
        switch (sharedPreferences.getString(KEY_TWO, "weather")){
            case "apps": indexApps=2;break;
            case "contacts":indexContacts=2;break;
            case "weather": indexWeather=2;break;
            case "calculator":indexCalc=2;break;
        }
        switch (sharedPreferences.getString(KEY_THREE, "calculator")){
            case "apps": indexApps=3;break;
            case "contacts":indexContacts=3;break;
            case "weather": indexWeather=3;break;
            case "calculator":indexCalc=3;break;
        }

        setContentView(R.layout.blurvview);

        addDots();

        RelativeLayout layout = findViewById(R.id.blurlayoutid);


        gestureDetector2 = new GestureDetector(this, new GestureDetectorsecond());

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector2.onTouchEvent(event);
            }
        });


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        String currentedge = sharedPreferences.getString("currentEdge", "");


        if (currentedge.equals("")) {

            for (int i=0; i<4; i++){
                if (find("FRAG"+i)!=null) {
                    fragmentTransaction.add(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);
                    break;
                }
            }


//            fragmentTransaction.add(R.id.blurlayoutid, find(FRAG_0), FRAG_0);




            selectDot(0);
        }
        else {
//            Toast.makeText(this, "you are wrong", Toast.LENGTH_SHORT).show();

//            Log.i("helloworld", currentedge);

            switch (currentedge){
                case "FRAG0": fragmentTransaction.add(R.id.blurlayoutid, find(FRAG_0), FRAG_0);selectDot(0);break;
                case "FRAG1": fragmentTransaction.add(R.id.blurlayoutid, find(FRAG_1), FRAG_1);selectDot(3);break;
                case "FRAG2": fragmentTransaction.add(R.id.blurlayoutid, find(FRAG_2), FRAG_2);selectDot(2);break;
                case "FRAG3": fragmentTransaction.add(R.id.blurlayoutid, find(FRAG_3), FRAG_3);selectDot(1);break;
            }
        }

        fragmentTransaction.commit();


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    public void setUp (){

    }


    public void addDots (){
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.dots);


        for (int j =0; j<NUMBER_FRAGMENTS; j++){
            if (find("FRAG"+j)!=null)
                counter=counter+1;
        }

        for (int i = 0; i < counter; i++){
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            dotsLayout.addView(dot, params);
            dot.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
            dot.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);
            dots.add(dot);
        }
    }

    public void selectDot (int index){
        Resources res = getResources();
         for (int i = 0; i < counter; i++){
             int drawableId = (i==index)?(R.drawable.ic_dot_selected):(R.drawable.ic_dot_unselected);
             Drawable drawable = res.getDrawable(drawableId);
             dots.get(i).setImageDrawable(drawable);
         }
    }

    public void takeout(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        FragmentManager fragmentManager = getFragmentManager();
        String FRAG="";
        if (fragmentManager.findFragmentByTag(FRAG_0) != null)
            FRAG=FRAG_0;
        if (fragmentManager.findFragmentByTag(FRAG_1) != null)
            FRAG=FRAG_1;
        if (fragmentManager.findFragmentByTag(FRAG_2) != null)
            FRAG=FRAG_2;
        if (fragmentManager.findFragmentByTag(FRAG_3) != null)
            FRAG=FRAG_3;

        editor.putString("currentEdge", FRAG);
        editor.apply();

        /**
         * Why is the code below being called?
         */

        Log.i("takeout", "called");

        Intent intent = new Intent(edge_activity.this, edge_service.class);
        startService(intent);
        finish();
    }


    @Override
    protected void onDestroy() {

        /**
         * This resets the edges so that the current edge is not remembered when the activity is killed.
         * Solves an error; tradeoff is less feaatures -> decide>
         */

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentEdge", "");
        editor.commit();
        super.onDestroy();
    }



    public static class AppViewFragment extends android.app.Fragment{

        public ImageView[] appicons = new ImageView[10];
        public ImageView[] removeappicons = new ImageView[10];
        PackageManager packageManager;
        public Button editapps;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.appview, container, false);

            registerViewsapps(view);

//            Log.i("EE", "22");

            return view;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (requestCode==525 && resultCode==RESULT_OK) {

                handleapp(data);

                editor.putBoolean("flag2", false);

            } else if (requestCode==525 && resultCode==RESULT_CANCELED){
                editor.putBoolean("flag2", false);
            }

            editor.commit();
        }

        public void registerViewsapps (View view) {
//
//            for (int i = 0; i < contacticon.length; i++){
//                String ID = "contacticon"+i;
//                int resID = getResources().getIdentifier(ID, "id", getActivity().getPackageName());
//                contacticon[i] = view.findViewById(resID);
//            }

        for (int i = 0; i < appicons.length; i++){
            String ID = "appicon"+i;
            int resID = getResources().getIdentifier(ID, "id", getActivity().getPackageName());
            appicons[i] = view.findViewById(resID);

            String ID2 = "appremoveicon"+i;
            int resID2 = getResources().getIdentifier(ID2, "id", getActivity().getPackageName());
            removeappicons[i] = view.findViewById(resID2);
        }

//        for (int i = 0; i < contacticon.length; i++){
//            contacticon[i].setOnClickListener(clicklistener2);
//        }
//
        for (int i=0; i < appicons.length; i++){
            appicons[i].setOnClickListener(clicklistener);
            removeappicons[i].setOnClickListener(clicklistenerremove);
        }

        editapps = (Button) view.findViewById(R.id.editapps);
        editapps.setOnClickListener(editappsclick);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPreferences.getBoolean("done", false)){

        } else {

            for (int i =0; i < appicons.length; i++){

//                String packagename = sharedPreferences.getString("appicon"+i, "none");
                String packagename = sharedPreferences.getString(String.valueOf(appicons[i].getId()), null);
                try {
                    appicons[i].setImageDrawable(getActivity().getPackageManager().getApplicationIcon(packagename));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }

        }

        View.OnClickListener clicklistenerremove = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (v.getId()){
                    case R.id.appremoveicon0:{
                        v.setVisibility(View.GONE);
                        appicons[0].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[0].getId()), null);
//                        editor.putString(String.valueOf(appicons[9].getId()), info.packageName);
                        break;
                    }
                    case R.id.appremoveicon1:{
                        v.setVisibility(View.GONE);
                        appicons[1].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[1].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon2:{
                        v.setVisibility(View.GONE);
                        appicons[2].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[2].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon3:{
                        v.setVisibility(View.GONE);
                        appicons[3].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[3].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon4:{
                        v.setVisibility(View.GONE);
                        appicons[4].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[4].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon5:{
                        v.setVisibility(View.GONE);
                        appicons[5].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[5].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon6:{
                        v.setVisibility(View.GONE);
                        appicons[6].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[6].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon7:{
                        v.setVisibility(View.GONE);
                        appicons[7].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[7].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon8:{
                        v.setVisibility(View.GONE);
                        appicons[8].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[8].getId()), null);
                        break;
                    }
                    case R.id.appremoveicon9:{
                        v.setVisibility(View.GONE);
                        appicons[9].setImageResource(R.drawable.ic_circle_outline);
                        editor.putString(String.valueOf(appicons[9].getId()), null);
                        break;
                    }
                }
                editor.apply();

            }
        };

        View.OnClickListener editappsclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String.valueOf(editapps.getText()).equals("edit")
                if (v.getTag().toString().equals("edit")) {
                    v.setTag("done");
                    editapps.setText("DONE");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    for (int i = 0; i < appicons.length; i++) {

                        if (sharedPreferences.getString(String.valueOf(appicons[i].getId()), null) != null) {
                            removeappicons[i].setVisibility(View.VISIBLE);
                            appicons[i].setOnClickListener(null);

                        }
                    }
                } else if (v.getTag().toString().equals("done")){
                    v.setTag("edit");
                    editapps.setText("EDIT");

                    for (int i = 0; i < appicons.length; i++){
                        removeappicons[i].setVisibility(View.GONE);
                        appicons[i].setOnClickListener(clicklistener);
                    }
                }
            }
        };


        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.getString(String.valueOf(view.getId()), null);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (sharedPreferences.getString(String.valueOf(view.getId()), null)==null){

                    editor.putBoolean("flag2", true);


                    Intent intent = new Intent(getActivity(), applist.class);
                    intent.putExtra("appview", view.getId());
                    startActivityForResult(intent, 525);

                } else {

                    editor.putBoolean("flag2", false);

                    PackageManager packageManager = getActivity().getPackageManager();
                    startActivity(packageManager.getLaunchIntentForPackage(sharedPreferences.getString(String.valueOf(view.getId()), null)));
                }

                editor.commit();


            }
        };



        public void handleapp (Intent intent) {

            ApplicationInfo info = intent.getParcelableExtra("applicationinfo");
            packageManager = getActivity().getPackageManager();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();



            switch (intent.getIntExtra("appviewid", 0)){
                case R.id.appicon0: {
                    appicons[0].setImageDrawable(info.loadIcon(packageManager));
                    appicons[0].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[0].getId()), info.packageName);

                    break;
                }
                case R.id.appicon1: {
                    appicons[1].setImageDrawable(info.loadIcon(packageManager));
                    appicons[1].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[1].getId()), info.packageName);

                    break;
                }
                case R.id.appicon2: {
                    appicons[2].setImageDrawable(info.loadIcon(packageManager));
                    appicons[2].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[2].getId()), info.packageName);

                    break;
                }
                case R.id.appicon3: {
                    appicons[3].setImageDrawable(info.loadIcon(packageManager));
                    appicons[3].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[3].getId()), info.packageName);


                    break;
                }
                case R.id.appicon4: {
                    appicons[4].setImageDrawable(info.loadIcon(packageManager));
                    appicons[4].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[4].getId()), info.packageName);


                    break;
                }
                case R.id.appicon5: {
                    appicons[5].setImageDrawable(info.loadIcon(packageManager));
                    appicons[5].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[5].getId()), info.packageName);


                    break;
                }
                case R.id.appicon6: {
                    appicons[6].setImageDrawable(info.loadIcon(packageManager));
                    appicons[6].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[6].getId()), info.packageName);


                    break;
                }
                case R.id.appicon7: {
                    appicons[7].setImageDrawable(info.loadIcon(packageManager));
                    appicons[7].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[7].getId()), info.packageName);


                    break;
                }
                case R.id.appicon8: {
                    appicons[8].setImageDrawable(info.loadIcon(packageManager));
                    appicons[8].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[8].getId()), info.packageName);

                    break;
                }
                case R.id.appicon9: {
                    appicons[9].setImageDrawable(info.loadIcon(packageManager));
                    appicons[9].setTag(info.packageName);
                    editor.putString(String.valueOf(appicons[9].getId()), info.packageName);

                    break;
                }
            }



            editor.putBoolean("done", true);
            editor.apply();

        }
    }


    public static class ContactViewFragment extends  android.app.Fragment{

        public int contactviewid;
        public ImageView[] contacticon = new ImageView[10];
        public TextView[] contacttext = new TextView[10];
        public ImageView[] removecontacticons = new ImageView[10];
        public String contactview="";
        public Button editcontacts;
        public boolean CONTACTS_VISIBILITY = false;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.contactview, container, false);

            registerViewscontacts(view);

            return view;
        }

        View.OnClickListener clicklistener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                if (sharedPreferences.getString(String.valueOf(v.getId()), null)==null) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("flag2", true);
                    editor.commit();

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    contactviewid = v.getId();
                    startActivityForResult(intent, 565);

                } else {
                    //TODO: add stuff
                    Log.d("no", sharedPreferences.getString(String.valueOf(v.getId()), null));
                    final String no=sharedPreferences.getString(String.valueOf(v.getId()), null);
int root = 0;
                    switch (v.getId()){
                        case R.id.contacticon0 :{
                            root = R.id.rl0;
                            break;
                        }
                        case R.id.contacticon1 :{
                            root = R.id.rl1;
                            break;
                        }
                        case R.id.contacticon2 :{
                            root = R.id.rl2;
                            break;
                        }
                        case R.id.contacticon3 :{
                            root = R.id.rl3;
                            break;
                        }
                        case R.id.contacticon4 :{
                            root = R.id.rl4;
                            break;
                        }
                        case R.id.contacticon5 :{
                            root = R.id.rl5;
                            break;
                        }case R.id.contacticon6 :{
                            root = R.id.rl6;
                            break;
                        }case R.id.contacticon7 :{
                            root = R.id.rl7;
                            break;
                        }
                        case R.id.contacticon8 :{
                            root = R.id.rl8;
                            break;
                        }
                        case R.id.contacticon9 :{
                            root = R.id.rl9;
                            break;
                        }
                    }

                    final ViewGroup viewGroup = getActivity().findViewById(root);

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(R.layout.bubble, viewGroup,false);


                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, v.getId());
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    view.setLayoutParams(params);

                    viewGroup.addView(view);

                    ImageView call = view.findViewById(R.id.call);
                    ImageView txt = view.findViewById(R.id.txt);

//                    final SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+no));
                            startActivity(intent);
                            viewGroup.removeView(view);
                        }
                    });

                    txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("sms:"+no));
                            startActivity(intent);
                            viewGroup.removeView(view);
                        }
                    });

//                    view.setLayoutParams(params);
//                    viewGroup.addView(inflater.inflate(R.layout.bubble,viewGroup));


//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:"+sharedPreferences.getString(String.valueOf(v.getId()),null)));
//                    startActivity(intent);
                    // call/sms the person

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("flag2", false);
                    editor.commit();

                }

//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("flag2", true);
//                editor.commit();
            }
        };

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Bitmap photo = null;
            String contactid="";

            if (requestCode==565 && resultCode==RESULT_OK) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String name="";
                String no = "";

                Uri uri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                Cursor cursorid = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.Contacts._ID},null,null,null);
                if (cursorid.moveToFirst()){
                    contactid = cursorid.getString(cursorid.getColumnIndex(ContactsContract.Contacts._ID));
                }

                cursorid.close();

                Cursor cursor1 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                        new String[]{contactid},
                        null);

                if (cursor1.moveToFirst()){
                    no = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

                cursor1.close();


//                try {
//                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
//                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactid)));
//                    if (inputStream != null){
//                        photo = BitmapFactory.decodeStream(inputStream);
//                    }
//
//                    assert inputStream != null;
//                    inputStream.close();
//
//                } catch (IOException e){
//                    e.printStackTrace();
//                }

                Log.i("NAME", name);
                Log.i("NO", no);


                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

                switch (contactviewid){
                    case R.id.contacticon0 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[0].setImageDrawable(drawable);
                        contacttext[0].setText(name);
                        contactview = "contacticon0";
                        editor.putString(String.valueOf(contacticon[0].getId()), no);
                        break;
                    }
                    case R.id.contacticon1 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[1].setImageDrawable(drawable);
                        contacttext[1].setText(name);
                        contactview = "contacticon1";
                        editor.putString(String.valueOf(contacticon[1].getId()), no);

                        break;
                    }
                    case R.id.contacticon2 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[2].setImageDrawable(drawable);
                        contacttext[2].setText(name);
                        contactview = "contacticon2";
                        editor.putString(String.valueOf(contacticon[2].getId()), no);

                        break;
                    }
                    case R.id.contacticon3 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[3].setImageDrawable(drawable);
                        contacttext[3].setText(name);
                        contactview = "contacticon3";
                        editor.putString(String.valueOf(contacticon[3].getId()), no);

                        break;
                    }
                    case R.id.contacticon4 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[4].setImageDrawable(drawable);
                        contacttext[4].setText(name);
                        contactview = "contacticon4";
                        editor.putString(String.valueOf(contacticon[4].getId()), no);

                        break;
                    }
                    case R.id.contacticon5 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[5].setImageDrawable(drawable);
                        contacttext[5].setText(name);
                        contactview = "contacticon5";
                        editor.putString(String.valueOf(contacticon[5].getId()), no);

                        break;
                    }
                    case R.id.contacticon6 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[6].setImageDrawable(drawable);
                        contacttext[6].setText(name);
                        contactview = "contacticon6";
                        editor.putString(String.valueOf(contacticon[6].getId()), no);

                        break;
                    }
                    case R.id.contacticon7 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[7].setImageDrawable(drawable);
                        contacttext[7].setText(name);
                        contactview = "contacticon7";
                        editor.putString(String.valueOf(contacticon[7].getId()), no);

                        break;
                    }
                    case R.id.contacticon8 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[8].setImageDrawable(drawable);
                        contacttext[8].setText(name);
                        contactview = "contacticon8";
                        editor.putString(String.valueOf(contacticon[8].getId()), no);

                        break;
                    }
                    case R.id.contacticon9 : {
                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[9].setImageDrawable(drawable);
                        contacttext[9].setText(name);
                        contactview = "contacticon9";
                        editor.putString(String.valueOf(contacticon[9].getId()), no);

                        break;
                    }

                }

                editor.putString(contactview, name);
                editor.putBoolean("donecontacts", true);
                editor.apply();

            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flag2", false);
            editor.commit();
        }

        View.OnClickListener removecontactsclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (v.getId()){
                    case R.id.contactremoveicon0:{
                        v.setVisibility(View.GONE);
                        contacticon[0].setImageResource(R.drawable.ic_box_outline);
                        contacttext[0].setText("");
                        editor.putString(String.valueOf(contacticon[0].getId()), null);
                        editor.putString("contacticon0", "");
//                        editor.putString(String.valueOf(appicons[9].getId()), info.packageName);
                        break;
                    }
                    case R.id.contactremoveicon1:{
                        v.setVisibility(View.GONE);
                        contacticon[1].setImageResource(R.drawable.ic_box_outline);
                        contacttext[1].setText("");
                        editor.putString(String.valueOf(contacticon[1].getId()), null);
                        editor.putString("contacticon1", "");
                        break;
                    }
                    case R.id.contactremoveicon2:{
                        v.setVisibility(View.GONE);
                        contacticon[2].setImageResource(R.drawable.ic_box_outline);
                        contacttext[2].setText("");
                        editor.putString(String.valueOf(contacticon[2].getId()), null);
                        editor.putString("contacticon2", "");
                        break;
                    }
                    case R.id.contactremoveicon3:{
                        v.setVisibility(View.GONE);
                        contacticon[3].setImageResource(R.drawable.ic_box_outline);
                        contacttext[3].setText("");
                        editor.putString(String.valueOf(contacticon[3].getId()), null);
                        editor.putString("contacticon3", "");
                        break;
                    }
                    case R.id.contactremoveicon4:{
                        v.setVisibility(View.GONE);
                        contacticon[4].setImageResource(R.drawable.ic_box_outline);
                        contacttext[4].setText("");
                        editor.putString(String.valueOf(contacticon[4].getId()), null);
                        editor.putString("contacticon4", "");
                        break;
                    }
                    case R.id.contactremoveicon5:{
                        v.setVisibility(View.GONE);
                        contacticon[5].setImageResource(R.drawable.ic_box_outline);
                        contacttext[5].setText("");
                        editor.putString(String.valueOf(contacticon[5].getId()), null);
                        editor.putString("contacticon5", "");
                        break;
                    }
                    case R.id.contactremoveicon6:{
                        v.setVisibility(View.GONE);
                        contacticon[6].setImageResource(R.drawable.ic_box_outline);
                        contacttext[6].setText("");
                        editor.putString(String.valueOf(contacticon[6].getId()), null);
                        editor.putString("contacticon6", "");
                        break;
                    }
                    case R.id.contactremoveicon7:{
                        v.setVisibility(View.GONE);
                        contacticon[7].setImageResource(R.drawable.ic_box_outline);
                        contacttext[7].setText("");
                        editor.putString(String.valueOf(contacticon[7].getId()), null);
                        editor.putString("contacticon7", "");
                        break;
                    }
                    case R.id.contactremoveicon8:{
                        v.setVisibility(View.GONE);
                        contacticon[8].setImageResource(R.drawable.ic_box_outline);
                        contacttext[8].setText("");
                        editor.putString(String.valueOf(contacticon[8].getId()), null);
                        editor.putString("contacticon8", "");
                        break;
                    }
                    case R.id.contactremoveicon9:{
                        v.setVisibility(View.GONE);
                        contacticon[9].setImageResource(R.drawable.ic_box_outline);
                        contacttext[9].setText("");
                        editor.putString(String.valueOf(contacticon[9].getId()), null);
                        editor.putString("contacticon9", "");
                        break;
                    }
                }
                editor.apply();
            }
        };

        View.OnClickListener editcontactsclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("edit")) {
                    v.setTag("done");
                    editcontacts.setText("DONE");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    for (int i = 0; i < contacticon.length; i++) {

                        if (sharedPreferences.getString(String.valueOf(contacticon[i].getId()), null) != null) {
                            removecontacticons[i].setVisibility(View.VISIBLE);
                            contacticon[i].setOnClickListener(null);

                        }
                    }
                } else if (v.getTag().toString().equals("done")){
                    v.setTag("edit");
                    editcontacts.setText("EDIT");

                    for (int i = 0; i < contacticon.length; i++){
                        removecontacticons[i].setVisibility(View.GONE);
                        contacticon[i].setOnClickListener(clicklistener2);
                    }
                }
            }
        };

        public void registerViewscontacts (View view){

            for (int i = 0; i < contacticon.length; i++){
                String ID = "contacttext"+i;
                int resID = getResources().getIdentifier(ID, "id", getActivity().getPackageName());
                contacttext[i] = view.findViewById(resID);
            }

            for (int i = 0; i < contacticon.length; i++){
                String ID = "contacticon"+i;
                int resID = getResources().getIdentifier(ID, "id", getActivity().getPackageName());
                contacticon[i] = view.findViewById(resID);

                String ID2 = "contactremoveicon"+i;
                int resID2 = getResources().getIdentifier(ID2, "id", getActivity().getPackageName());
                removecontacticons[i] = view.findViewById(resID2);
                }

            for (int i = 0; i < contacticon.length; i++){
            contacticon[i].setOnClickListener(clicklistener2);
            removecontacticons[i].setOnClickListener(removecontactsclick);
            }

            editcontacts = view.findViewById(R.id.editcontacts);
            editcontacts.setOnClickListener(editcontactsclick);


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (!sharedPreferences.getBoolean("donecontacts", false)){

            } else {
//                ColorGenerator colorGenerator = new ColorGenerator().MATERIAL;
                for (int i =0; i < contacticon.length; i++){


                    String name = sharedPreferences.getString("contacticon"+i, "");
                    if (name!=""){

                        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

                        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(name.charAt(0)), colorGenerator.getRandomColor());
                        contacticon[i].setImageDrawable(drawable);
                        contacttext[i].setText(name);
                    }

                }


            }
        }

    }
//:TODO check this
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
//            onBackPressed();
//            return true;
//        } else
        return gestureDetector2.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (isApplicationSentToBackground(this)) //IF HOME PRESSED only want flag to be set when activity is exited but not in the case of contact picker activity.
//            flag=true;
//
//        if (flag) {
//            Log.e("BMW","BMW");
//            takeout();
//            flag=false;
//        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("flag2", false)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flag2", true);
            editor.commit();
        } else {
            takeout();// display edge
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag=true;
//        onPause();
//        takeout();
    }

    public Fragment find (String TAG){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (TAG) {

            case "FRAG0":
            switch (sharedPreferences.getString(KEY_ZERO, "apps")) {
                case "apps":
                    if (AppsEdge)
                    return new AppViewFragment();
                    else
                    return null;
                case "contacts":
                    if (ContactsEdge)
                    return new ContactViewFragment();
                    else
                    return null;
                case "weather":
                    if (WeatherEdge)
                    return new WeatherViewFragment();
                    else
                    return null;
                case "calculator":
                    if (CalculatorEdge)
                    return new CalculatorFragment();
                    else
                    return null;
            }
            break;
            case "FRAG1":
            switch (sharedPreferences.getString(KEY_ONE, "contacts")) {
/*testing code in apps*/
                case "apps":
                    if (AppsEdge)
                    return new AppViewFragment();
                    else
                    return null;
                case "contacts":
                    if (ContactsEdge)
                    return new ContactViewFragment();
                    else
                    return null;

                case "weather":
                    if (WeatherEdge)
                        return new WeatherViewFragment();
                    else
                        return null;

                case "calculator":
                    if (CalculatorEdge)
                        return new CalculatorFragment();
                    else
                        return null;

            }
            break;
            case "FRAG2":
            switch (sharedPreferences.getString(KEY_TWO, "weather")) {
                case "apps":
                    if (AppsEdge)
                    return new AppViewFragment();
                    else
                    return null;
                case "contacts":
                    if (ContactsEdge)
                    return new ContactViewFragment();
                    else
                    return null;
                case "weather":
                    if (WeatherEdge)
                        return new WeatherViewFragment();
                    else
                        return null;

                case "calculator":
                    if (CalculatorEdge)
                        return new CalculatorFragment();
                    else
                        return null;
            }
            break;
            case "FRAG3":
            switch (sharedPreferences.getString(KEY_THREE, "calculator")) {
                case "apps":
                    if (AppsEdge)
                    return new AppViewFragment();
                    else
                    return null;
                case "contacts":
                    if (ContactsEdge)
                    return new ContactViewFragment();
                    else
                    return null;
                case "weather":
                    if (WeatherEdge)
                        return new WeatherViewFragment();
                    else
                        return null;

                case "calculator":
                    if (CalculatorEdge)
                        return new CalculatorFragment();
                    else
                        return null;

            }
            break;

        }

        return new AppViewFragment();
    }

    public void SwitchPanels (String direction) {



        Fragment fragment0;
        Fragment fragment1;
        Fragment fragment2;
        Fragment fragment3;
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.findFragmentByTag(FRAG_0) != null) {
            fragment0 = fragmentManager.findFragmentByTag(FRAG_0);
        } else
            fragment0 = null;

        if (fragmentManager.findFragmentByTag(FRAG_1) != null) {
            fragment1 = fragmentManager.findFragmentByTag(FRAG_1);
        } else
            fragment1 = null;

        if (fragmentManager.findFragmentByTag(FRAG_2) != null) {
            fragment2 = fragmentManager.findFragmentByTag(FRAG_2);
        } else
            fragment2 = null;

        if (fragmentManager.findFragmentByTag(FRAG_3) != null) {
            fragment3 = fragmentManager.findFragmentByTag(FRAG_3);
        } else
            fragment3 = null;

//        fragment3 = fragmentManager.findFragmentByTag(FRAG_3);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (direction.equals("right")) {

            if (selectedDot==0)
                selectedDot=counter;

            fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);


            if (fragment0 != null && fragment0.isVisible()) {

                /**
                 * run from 1,2,3
                 */

                int i = 1;
                while (find("FRAG" + i) == null) {
                    i = i + 1;
                    if (i == 4) {
                        i = 0;
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);
//                selectDot(3);
//                selectDot(counter-1);
            }


            if (fragment1 != null && fragment1.isVisible()) {

                /**
                 * run from 2,3,0
                 */

                int i = 2;
                while (find("FRAG" + i) == null) {
                    i = i + 1;
                    if (i == 4) {
                        i = 0;
                    }
                    if (i == 1) {
                        break;
                    }
                }
                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);
//                selectDot(2);
//                selectDot(counter-2);
            }

            if (fragment2 != null && fragment2.isVisible()) {

                /**
                 * run from 3,0,1
                 */

                int i = 3;
                while (find("FRAG" + i) == null) {
                    i = i + 1;
                    if (i == 4) {
                        i = 0;
                    }
                    if (i == 2) {
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);
//                selectDot(1);
//                selectDot(counter-3);
            }
            if (fragment3 != null && fragment3.isVisible()) {

                /**
                 * run
                 */
                int i = 0;
                while (find("FRAG" + i) == null) {
                    i = i + 1;
                    if (i == 3) {
                        i = 3;
                        break;
                    }
                }
                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);
//                selectDot(0);
//                selectDot(counter-4);
            }

            selectedDot=selectedDot-1;
            selectDot(selectedDot);
            fragmentTransaction.commit();

        } else {

            if (selectedDot==counter-1)
                selectedDot=-1;

            fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);


            if (fragment0 != null && fragment0.isVisible()) {

                /**
                 * run from 3,2,1
                 */

                int i = 3;
                while (find("FRAG" + i) == null) {
                    i = i - 1;
                    if (i == 0) {
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);

            }


//            selectDot(1);

            if (fragment3 != null && fragment3.isVisible()) {


                /**
                 * run from 2,1,0
                 */

                int i = 2;
                while (find("FRAG" + i) == null) {
                    i = i - 1;
                    if (i == -1) {
                        i = 3;
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);

//                selectDot(2);
            }
            if (fragment2 != null && fragment2.isVisible()) {

                /**
                 * run from 1,0,3
                 */

                int i = 1;
                while (find("FRAG" + i) == null) {
                    i = i - 1;
                    if (i == -1) {
                        i = 3;
                    }
                    if (i == 2) {
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);

//                selectDot(3);
            }
            if (fragment1 != null && fragment1.isVisible()) {

                /**
                 * run from 0,3,2
                 */

                int i = 0;
                while (find("FRAG" + i) == null) {
                    i = i - 1;
                    if (i == -1) {
                        i = 3;
                    }
                    if (i == 1) {
                        break;
                    }
                }

                fragmentTransaction.replace(R.id.blurlayoutid, find("FRAG" + i), "FRAG" + i);

//                selectDot(0);
            }
            selectedDot=selectedDot+1;
            selectDot(selectedDot);
            fragmentTransaction.commit();
        }

    }



    private class GestureDetectorsecond extends android.view.GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try{
                if (e1.getX() - e2.getX() > width && Math.abs(velocityX) > 1){
                    SwitchPanels("left");
                    //onBackPressed();

                }
                if (e2.getX() - e1.getX() > width && Math.abs(velocityX) > 1){
                    SwitchPanels("right");
                }
            }
            catch (Exception e){

            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

    }


}