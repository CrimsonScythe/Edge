package com.crimsonscythe.edge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class applist extends AppCompatActivity {

    int appviewid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2);

        appviewid = getIntent().getIntExtra("appview", appviewid);

        View frag = (View) findViewById(R.id.frag);

        load1 load2 = new load1(this, frag);
        load2.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private class load1 extends AsyncTask<Void, Void, Void> {

        listadapterapps listadapterapps6;
        View root;
        private ProgressDialog progressDialog = null;
        private List<ApplicationInfo> appInfos;

        public load1(Context context, View root) {
            this.root = root;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            final PackageManager packageManager = getPackageManager();
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final List<ResolveInfo> resInfos = packageManager.queryIntentActivities(intent, 0);
            //using hashset so that there will be no duplicate packages,
            //if no duplicate packages then there will be no duplicate apps
            final HashSet<String> packageNames = new HashSet<String>(0);
            //   final List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>(0);
            appInfos = new ArrayList<ApplicationInfo>(0);

            //getting package names and adding them to the hashset
            for(ResolveInfo resolveInfo : resInfos) {
                packageNames.add(resolveInfo.activityInfo.packageName);
            }

            //now we have unique packages in the hashset, so get their application infos
            //and add them to the arraylist
            for(String packageName : packageNames) {
                try {
                    appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
                } catch (PackageManager.NameNotFoundException e) {
                    //Do Nothing
                }
            }

            Collections.sort(appInfos, new ApplicationInfo.DisplayNameComparator(packageManager));


            listadapterapps6 = new listadapterapps(applist.this, R.layout.applist2, appInfos);


            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(applist.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            final ListView list5 = (ListView) root.findViewById(R.id.list5);
            list5.setAdapter(listadapterapps6);
            //  list.setVisibility(View.GONE);
            list5.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            super.onPostExecute(result);
            list5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PackageManager packageManager = getPackageManager();
                    ApplicationInfo info = appInfos.get(position);

//                    Intent intent = new Intent(applist.this, edge_activity.class);

                    Intent intent = new Intent();

                    intent.putExtra("appviewid", appviewid);
                    intent.putExtra("applicationinfo", info);
                    intent.putExtra("returned", true);

//                    startService(intent);
//                    finish();
//                    intent.putExtra("applicationinfo", info);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

}
