package com.example.samson.snowmobilingapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;


public class MainActivity extends AppCompatActivity {
    private MapView myMapView;
    boolean connected = false;
    //Viewpoint mViewPoint;
     LocationDisplay LD;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make activity full screen;
        //getSupportActionBar().hide();
            setContentView(R.layout.activity_main);
            myMapView = findViewById(R.id.mapView);
            //Create a Basemap
            ArcGISTiledLayer tl = new ArcGISTiledLayer(getResources().getString(R.string.topographic));
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
            FeatureLayer featureLayers1 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_roads)));

            map.getOperationalLayers().add(featureLayers2);
            map.getOperationalLayers().add(featureLayers1);
            map.getOperationalLayers().add(featureLayers0);

            myMapView.setMap(map);

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
            case R.id.findlocation:
                Intent intent1 = new Intent(this, FindLocation.class);
                this.startActivity(intent1);
                Toast.makeText(getBaseContext(), "Address", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.findme:
                Intent intent2 = new Intent(MainActivity.this, FindMe.class);
                this.startActivity(intent2);
                //startActivityForResult(intent2,0);
                Toast.makeText(getBaseContext(), "Current Location", Toast.LENGTH_SHORT).show();
                return true;
                default:return true;

        }

    }
}
