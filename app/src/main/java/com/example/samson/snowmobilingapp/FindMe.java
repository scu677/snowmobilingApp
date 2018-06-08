package com.example.samson.snowmobilingapp;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

public class FindMe extends AppCompatActivity {

    MapView myMapView;
    LocationDisplay LD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_me);
        myMapView = findViewById(R.id.mapView1);

        ArcGISTiledLayer tl = new ArcGISTiledLayer(getResources().getString(R.string.topographic));
        Basemap bm = new Basemap(tl);
        ArcGISMap map = new ArcGISMap(bm);
        //create initial viewpoint and extent
        Point leftPoint = new Point(-6641791.193399999, 5853988.327799998, SpatialReference.create(3857));
        Point rightPoint = new Point(-5808352.0265, 6772993.915799998, SpatialReference.create(3857));
        Envelope initialExtent = new Envelope(leftPoint, rightPoint);
        Viewpoint vp = new Viewpoint(initialExtent);
        map.setInitialViewpoint(vp);

        // create feature layer from ArcGIS featured service
        FeatureLayer featureLayers0 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_shelter)));
        FeatureLayer featureLayers2 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_trails)));
        FeatureLayer featureLayers1 = new FeatureLayer(new ServiceFeatureTable(getResources().getString(R.string.featured_roads)));

        map.getOperationalLayers().add(featureLayers2);
        map.getOperationalLayers().add(featureLayers1);
        map.getOperationalLayers().add(featureLayers0);

        myMapView.setMap(map);
        LD = myMapView.getLocationDisplay();
        LD.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        LD.startAsync();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
