/***********************************************************************************
 //Copyright (c) 2018, Resource Innovations Inc.
// Developed by: Samson Ugwuodo
// MainActivity source code
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
 //limitations under the License.
 **********************************************************************************/
package com.example.samson.snowmobilingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;


public class MainActivity extends AppCompatActivity {

    private MapView myMapView;
    private LocationDisplay LD;
    private static ArcGISMap map;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMapView = findViewById(R.id.mapView);
        myMapView.getCallout();


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            Portal portal = new Portal("https://gis.resourceinnovations.ca/gis");
            PortalItem portalItem = new PortalItem(portal, "b53f696fea544843b1c139a3c1b252ef");
            map = new ArcGISMap(portalItem);
            myMapView.setMap(map);
            InitialExtend();

            //download offline map//
            FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FindLocation();
                    Toast.makeText(getBaseContext(), "Display location", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //Toast.makeText(getBaseContext(), "No Internet Connection !", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Map fail to load because internet is disconnected. Close and connect your device to internet or use offline.");
            alertDialog.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    onResume();
                }
            });
            alertDialog.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    finish();
                }
            }); alertDialog.show();alertDialog.create();
        }
    }


    public static void InitialExtend(){
        Point leftPoint = new Point(-6641791.193399999, 5853988.327799998, SpatialReference.create(3857));
        Point rightPoint = new Point(-5808352.0265, 6772993.915799998, SpatialReference.create(3857));

        Envelope initialExtent = new Envelope(leftPoint, rightPoint);
        Viewpoint vp = new Viewpoint(initialExtent);
        //vp.getTargetGeometry();
        map.setInitialViewpoint(vp);
        map.setMaxScale(20000);
        map.setMinScale(5800000);

    }

    //Find device location method
    private void FindLocation(){
        LD = myMapView.getLocationDisplay();
        LD.setShowPingAnimation(true);
        LD.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        LD.startAsync();

    }

    @Override
    protected void onPause(){
        myMapView.pause();
        super.onPause();

    }

    @Override
    protected void onResume(){
        super.onResume();
        myMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMapView.dispose();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.offline:
                Intent intent1 = new Intent(MainActivity.this, OfflineMaps.class);
                this.startActivity(intent1);
                //Toast.makeText(getBaseContext(), "Download offline map", Toast.LENGTH_SHORT).show();
                return true;
                default:return super.onOptionsItemSelected(item);

        }

    }

}
