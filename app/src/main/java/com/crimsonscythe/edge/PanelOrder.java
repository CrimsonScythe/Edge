package com.crimsonscythe.edge;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.crimsonscythe.edge.helper.OnStartDragListener;
import com.crimsonscythe.edge.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class PanelOrder extends AppCompatActivity implements OnStartDragListener{

    private ImageView reorderAppsIcon;
    private CardView appsCard, contactsCard, weatherCard, calcCard;

    private static final String TAG_APPS_ICON = "icon apps";
    private static final String TAG_CONTACTS_ICON = "icon contacts";
    private static final String TAG_WEATHER_ICON = "icon weather";
    private static final String TAG_CALC_ICON = "icon calc";

    private float dy;

    private GridLayout gridLayout;
    private ScrollView scrollView;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ItemTouchHelper itemTouchHelper;
    private final List<CardView> dataset = new ArrayList<>(4);

    CardView[] cardViews;
    String[] tags;
    int indexApps=0;
    int indexContacts=1;
    int indexWeather=2;
    int indexCalc=3;

    String KEY_APPS = "KEY_APPS";
    String KEY_CONTACTS = "KEY_CONTACTS";
    String KEY_WEATHER = "KEY_WEATHER";
    String KEY_CALC = "KEY_CALC";

    String KEY_ZERO="zero";
    String KEY_ONE="one";
    String KEY_TWO="two";
    String KEY_THREE="three";

    String[] index = new String[4];
//
//    String index0;
//    String index1;
//    String index2;
//    String index3;

//    String indexZero;
//    String indexOne;
//    String indexTwo;
//    String indexThree;


    SharedPreferences sharedPreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_order2);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        indexApps = sharedPreferences.getInt(KEY_APPS, 0);
        indexContacts = sharedPreferences.getInt(KEY_CONTACTS, 1);
        indexWeather = sharedPreferences.getInt(KEY_WEATHER, 2);
        indexCalc = sharedPreferences.getInt(KEY_CALC, 3);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager = manager;

        recyclerView.setLayoutManager(layoutManager);

        cardViews = new CardView[4];

        String[] strings = new String[] {"Apps Edge", "Contacts Edge", "Weather Edge", "Calculator Edge"};

        tags = new String[] {
                sharedPreferences.getString(KEY_ZERO,"apps"),
                sharedPreferences.getString(KEY_ONE,"contacts"),
                sharedPreferences.getString(KEY_TWO,"weather"),
                sharedPreferences.getString(KEY_THREE,"calculator")};

        Drawable[] imgs = new Drawable[] {
                ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null)};


        switch (sharedPreferences.getString(KEY_ZERO, "apps")){
            case "apps":cardViews[0] = findViewById(R.id.apps_card);strings[0]="Apps Edge";imgs[0]=ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null);break;
            case "contacts":cardViews[0] = findViewById(R.id.contacts_card);strings[0]="Contacts Edge";imgs[0]=ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null);break;
            case "weather":cardViews[0] = findViewById(R.id.weather_card);strings[0]="Weather Edge";imgs[0]=ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null);break;
            case "calculator":cardViews[0] = findViewById(R.id.calc_card);strings[0]="Calculator Edge";imgs[0]=ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null);break;
        }
        switch (sharedPreferences.getString(KEY_ONE, "contacts")){
            case "apps":cardViews[1] = findViewById(R.id.apps_card);strings[1]="Apps Edge";imgs[1]=ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null);break;
            case "contacts":cardViews[1] = findViewById(R.id.contacts_card);strings[1]="Contacts Edge";imgs[1]=ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null);break;
            case "weather":cardViews[1] = findViewById(R.id.weather_card);strings[1]="Weather Edge";imgs[1]=ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null);break;
            case "calculator":cardViews[1] = findViewById(R.id.calc_card);strings[1]="Calculator Edge";imgs[1]=ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null);break;
        }
        switch (sharedPreferences.getString(KEY_TWO, "weather")){
            case "apps":cardViews[2] = findViewById(R.id.apps_card);strings[2]="Apps Edge";imgs[2]=ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null);break;
            case "contacts":cardViews[2] = findViewById(R.id.contacts_card);strings[2]="Contacts Edge";imgs[2]=ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null);break;
            case "weather":cardViews[2] = findViewById(R.id.weather_card);strings[2]="Weather Edge";imgs[2]=ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null);break;
            case "calculator":cardViews[2] = findViewById(R.id.calc_card);strings[2]="Calculator Edge";imgs[2]=ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null);break;
        }
        switch (sharedPreferences.getString(KEY_THREE, "calculator")){
            case "apps":cardViews[3] = findViewById(R.id.apps_card);strings[3]="Apps Edge";imgs[3]=ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null);break;
            case "contacts":cardViews[3] = findViewById(R.id.contacts_card);strings[3]="Contacts Edge";imgs[3]=ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null);break;
            case "weather":cardViews[3] = findViewById(R.id.weather_card);strings[3]="Weather Edge";imgs[3]=ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null);break;
            case "calculator":cardViews[3] = findViewById(R.id.calc_card);strings[3]="Calculator Edge";imgs[3]=ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null);break;
        }


//        cardViews[0] = findViewById(R.id.apps_card);
//        cardViews[1] = findViewById(R.id.contacts_card);
//        cardViews[2] = findViewById(R.id.weather_card);
//        cardViews[3] = findViewById(R.id.calc_card);



//        String[] strings = new String[] {"Apps Edge", "Contacts Edge", "Weather Edge", "Calculator Edge"};
//        Drawable[] imgs = new Drawable[] {
//                ResourcesCompat.getDrawable(getResources(), R.drawable.appsedge, null),
//                ResourcesCompat.getDrawable(getResources(), R.drawable.contactsedge, null),
//                ResourcesCompat.getDrawable(getResources(), R.drawable.weatheredge, null),
//                ResourcesCompat.getDrawable(getResources(), R.drawable.calcedge, null)};

//        tags = new String[] {"apps", "contacts", "weather", "calculator"};

        adapter = new MyAdapter(cardViews, strings, imgs, this, tags, layoutManager);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
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
    protected void onPause() {
        super.onPause();

        savePrefs();
    }

    public void savePrefs (){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        index[0] = sharedPreferences.getString(KEY_ZERO, "apps");
        index[1] = sharedPreferences.getString(KEY_ONE, "contacts");
        index[2] = sharedPreferences.getString(KEY_TWO, "weather");
        index[3] = sharedPreferences.getString(KEY_THREE, "calculator");


        try {
            index[0]= (String) layoutManager.findViewByPosition(0).getTag();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            index[1]= (String) layoutManager.findViewByPosition(1).getTag();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            index[2]= (String) layoutManager.findViewByPosition(2).getTag();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            index[3]= (String) layoutManager.findViewByPosition(3).getTag();
        } catch (NullPointerException e){
            e.printStackTrace();
        }


        editor.putString(KEY_ZERO, index[0]);
        editor.putString(KEY_ONE, index[1]);
        editor.putString(KEY_TWO, index[2]);
        editor.putString(KEY_THREE, index[3]);

        Log.i("TAGGG", index[0]);
        Log.i("TAGG", index[1]);
        Log.i("TAGGG", index[2]);
        Log.i("TAGGG", index[3]);

        editor.apply();

    }

    //    protected class myDragEventListener implements View.OnDragListener {
//
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//
//            final int action = event.getAction();
//
//            switch (action){
//                case DragEvent.ACTION_DRAG_STARTED:
//                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
//                        //v.setBackgroundColor(Color.BLUE);
//                        //v.invalidate();
//                        return true;
//                    }
//                    return false;
//
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    //v.setBackgroundColor(Color.GREEN);
//                    //v.invalidate();
//                    return true;
//
//                case DragEvent.ACTION_DRAG_EXITED:
//                    //v.setBackgroundColor(Color.BLUE);
//                    //v.invalidate();
//                    return true;
//
//                case DragEvent.ACTION_DROP:
//                    // Gets the item containing the dragged data
//                    ClipData.Item item = event.getClipData().getItemAt(0);
//
//                    CharSequence dragData = item.getText();
//
//
//
////                    layout.addView
//
//                    // Displays a message containing the dragged data.
//                    Toast.makeText(PanelOrder.this, "Dragged data is " + dragData.toString(), Toast.LENGTH_LONG).show();
//
//                    // Invalidates the view to force a redraw
//                    v.invalidate();
//
//                    return true;
//
//                case DragEvent.ACTION_DRAG_ENDED:
//
////                    gridLayout.removeView(v);
////                    gridLayout.addView(v, 3);
//
//
//                    for (int i = 0; i < 4; i++){
//                        if (event.getX() > gridLayout.getChildAt(i).getX()){
//                            gridLayout.removeView(v);
//                            gridLayout.addView(v, i);
//                        }
//                    }
//
//                    for (int i = 3; i >= 0; i--){
//                        if (event.getX() < gridLayout.getChildAt(i).getX()){
//                            gridLayout.removeView(v);
//                            gridLayout.addView(v, i);
//                        }
//                    }
//
//                    // Turns off any color tinting
//                    Toast.makeText(PanelOrder.this, "HELLLL", Toast.LENGTH_SHORT).show();
//
//
//
//                    Log.e("phoosna", String.valueOf(event.getX()));
//                    Log.e("phoosna", String.valueOf(event.getY()));
//
//                    // Invalidates the view to force a redraw
//                    v.invalidate();
//
//                    // Does a getResult(), and displays what happened.
//                    if (event.getResult()) {
//                        Toast.makeText(PanelOrder.this, "The drop was handled.", Toast.LENGTH_LONG).show();
//
//                    } else {
//                        Toast.makeText(PanelOrder.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
//
//                    }
//
//
//
//                    // returns true; the value is ignored.
//                    return true;
//
//                default:
//                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
//                    break;
//            }
//
//            return false;
//        }
//
//
//    }

}
