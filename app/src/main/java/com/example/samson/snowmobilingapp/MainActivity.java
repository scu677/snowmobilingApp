package com.example.samson.snowmobilingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.LocatorAttribute;
import com.esri.arcgisruntime.tasks.geocode.LocatorInfo;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.util.ListChangedEvent;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    MapView myMapView;
     LocationDisplay LD;


    String runtimeLicenseKey = "runtimelite,1000,rudxxxxxxxxx,28-feb-2018,xxxxxxxxxxxxxxxxxxxx";
    String smpNorthAmerica = "runtimesmpna,1000,rudxxxxxxxxx,13-mar-2018,xxxxxxxxxxxxxxxxxxxx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make activity full screen;
        //getSupportActionBar().hide();
            setContentView(R.layout.activity_main);
            myMapView = findViewById(R.id.mapView);
            //Create a Basemap
            ArcGISTiledLayer tl = new ArcGISTiledLayer(getResources().getString(R.string.world_street_map));
            Basemap bm = new Basemap(tl);
            ArcGISMap map = new ArcGISMap(bm);
            //create initial viewpoint and extent
            Point leftPoint = new Point(-6641791.193399999, 5853988.327799998, SpatialReference.create(3857));
            Point rightPoint = new Point(-5808352.0265, 6772993.915799998, SpatialReference.create(3857));
            Envelope initialExtent = new Envelope(leftPoint, rightPoint);
            Viewpoint vp = new Viewpoint(initialExtent);
            map.setInitialViewpoint(vp);
            map.setMaxScale(20000);
            map.setMinScale(5800000);

            // create feature layer from ArcGIS featured service
            FeatureLayer featureLayers0 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_shelter)));
            FeatureLayer featureLayers2 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_trails)));
            //FeatureLayer featureLayers1 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_roads)));

            map.getOperationalLayers().add(featureLayers2);
            //map.getOperationalLayers().add(featureLayers1);
            map.getOperationalLayers().add(featureLayers0);
            myMapView.setMap(map);

            List<String> extensions = new ArrayList<>();
            extensions.add(smpNorthAmerica);
             // Set the license for ArcGIS Runtime and the three extensions (areas)
            ArcGISRuntimeEnvironment.setLicense(runtimeLicenseKey, extensions);
            // Initialize the ArcGIS Runtime before any components are created
            ArcGISRuntimeEnvironment.initialize();


        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                LD = myMapView.getLocationDisplay();
                LD.setShowPingAnimation(true);
                LD.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                LD.startAsync();
            }
        });



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
                Toast.makeText(getBaseContext(), "Download offline map", Toast.LENGTH_SHORT).show();
                return true;

                default:return super.onOptionsItemSelected(item);

        }

    }


}
