package com.crimsonscythe.edge;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crimsonscythe.edge.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hasee on 09/02/2016.
 */
public class customlistadapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Integer> imgid;

    public customlistadapter(Activity context , ArrayList<String> itemname , ArrayList<Integer> imgid){
        super(context , R.layout.mylist , itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;

    }

    public View getView (int position , View view , ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView textView = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(itemname.get(position));
        imageView.setImageResource(imgid.get(position));
//        imageView.setImageResource(imgid[position]);

        return rowView;

    }

}
