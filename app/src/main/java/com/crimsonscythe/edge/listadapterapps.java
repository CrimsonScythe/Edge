package com.crimsonscythe.edge;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class listadapterapps extends ArrayAdapter<ApplicationInfo> {
    private final Context context2;
    private List<ApplicationInfo> appslist = null;
    private PackageManager packageManager;

    public listadapterapps(Context context2 , int textViewResourceId , List<ApplicationInfo> appslist ) {
        super(context2 , textViewResourceId , appslist);

        this.context2 = context2;
        this.appslist = appslist;
        packageManager = context2.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appslist) ? appslist.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appslist) ? appslist.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position , View view , ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.applist2, null);

        ApplicationInfo info = appslist.get(position);



        if (null != info) {
            ImageView imageView = (ImageView) root.findViewById(R.id.icon2);
            TextView textView = (TextView) root.findViewById(R.id.item2);


            textView.setText(info.loadLabel(packageManager));
            imageView.setImageDrawable(info.loadIcon(packageManager));

        }
        return root;
    }
}
