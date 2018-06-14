/* Copyright 2018 Resource Innovations Inc
    Developed by: Samson Ugwuodo
//MainActivity source code//
Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
 */

package com.example.samson.snowmobilingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    MapView myMapView;
    LocationDisplay LD;
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
