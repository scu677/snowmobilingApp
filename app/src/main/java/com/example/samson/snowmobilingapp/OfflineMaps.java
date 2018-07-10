/***********************************************************************************
 //Copyright (c) 2018, Resource Innovations Inc.
 // Developed by: Samson Ugwuodo
 // Offline source code
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

import java.io.File;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Environment;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import com.esri.arcgisruntime.tasks.geodatabase.GenerateGeodatabaseParameters;
import com.esri.arcgisruntime.tasks.offlinemap.GenerateOfflineMapJob;
import com.esri.arcgisruntime.tasks.offlinemap.GenerateOfflineMapParameters;
import com.esri.arcgisruntime.tasks.offlinemap.GenerateOfflineMapResult;
import com.esri.arcgisruntime.tasks.offlinemap.OfflineCapability;
import com.esri.arcgisruntime.tasks.offlinemap.OfflineMapCapabilities;
import com.esri.arcgisruntime.tasks.offlinemap.OfflineMapItemInfo;
import com.esri.arcgisruntime.tasks.offlinemap.OfflineMapTask;
import com.esri.arcgisruntime.geometry.*;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import static com.example.samson.snowmobilingapp.MainActivity.InitialExtend;


public class OfflineMaps extends AppCompatActivity {
    // private Geometry areaOfInterest;
    private MapView myMapView;
    private Button downloadButton;
   // private OfflineMapItemInfo ItemInfo;
    private Portal myPortal = new Portal("https://gis.resourceinnovations.ca/gis");
    private PortalItem myPortalItem = new PortalItem(myPortal, "b53f696fea544843b1c139a3c1b252ef");
    private ArcGISMap map = new ArcGISMap(myPortalItem);
    OfflineMapTask offlineMapTask = new OfflineMapTask(map);



    //Define Error message method
    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public Geometry areaOfInterest = new Geometry() {
        //@Override
        public Envelope getExtent(){
            return getExtent();

       }
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.offline_maps);
            // Portal myPortal = new Portal("https://gis.resourceinnovations.ca/gis");
            // PortalItem myPortalItem = new PortalItem(myPortal, "b53f696fea544843b1c139a3c1b252ef");
            // ArcGISMap map = new ArcGISMap(myPortalItem);
            myMapView = findViewById(R.id.mapOffline);
            myMapView.setMap(map);
            InitialExtend();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //GenerateOfflineMapParameters mapParameters = null;

            offlineMapTask.getLoadStatus();
            //OfflineMaps offlineMaps = new OfflineMaps();
            if (offlineMapTask.getLoadStatus() == LoadStatus.FAILED_TO_LOAD) {

                showMessage("offlineTask failed");
                offlineMapTask.getLoadError();}

            // } else {
            //GenerateParameters();
            final OfflineMapTask offlineMapTask = new OfflineMapTask(map);
            final ListenableFuture<GenerateOfflineMapParameters> ParameterFuture = offlineMapTask.createDefaultGenerateOfflineMapParametersAsync(areaOfInterest);


            try {
                final GenerateOfflineMapParameters mapParameters = ParameterFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //CheckCapability();
            // }
            OfflineButton();
            DownloadButton();
        }

        private GenerateOfflineMapParameters mapParameters;


    public void GenerateParameters() throws ExecutionException, InterruptedException {
        final OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        final ListenableFuture<GenerateOfflineMapParameters> ParameterFuture = offlineMapTask.createDefaultGenerateOfflineMapParametersAsync(areaOfInterest);
        showMessage("Generating done!");
        GenerateOfflineMapParameters mapParameters = ParameterFuture.get();
        /*ParameterFuture.addDoneListener(new Runnable(){

           /* @Override
            public void run(){

                try {
                    Thread.sleep(5000);
                    mapParameters = ParameterFuture.get();

                    mapParameters.setMaxScale(5000);
                    mapParameters.setMinScale(0);
                    mapParameters.setIncludeBasemap(true);
                    mapParameters.setAttachmentSyncDirection(GenerateGeodatabaseParameters.AttachmentSyncDirection.UPLOAD);
                    mapParameters.setReturnLayerAttachmentOption(GenerateOfflineMapParameters.ReturnLayerAttachmentOption.EDITABLE_LAYERS);
                    mapParameters.setReturnSchemaOnlyForEditableLayers(true);
                    mapParameters.getItemInfo().setTitle(mapParameters.getItemInfo().getTitle() + "(Central)");

                    showMessage("GenerateOfflineMapParameters has been set");
                    //mapParameters1 = mapParameters;

                } catch (Exception e) {
                    e.getMessage();
                }*/
        //}});
    }


    public void BitmapView(){

        final OfflineMapItemInfo itemInfo = new OfflineMapItemInfo();

        itemInfo.setTitle("snowmobiling (Central)");
        itemInfo.setSnippet(myPortalItem.getSnippet()); // Copy from the source map
        itemInfo.setDescription(myPortalItem.getDescription()); // Copy from the source map
        itemInfo.setAccessInformation(myPortalItem.getAccessInformation()); // Copy from the source map
        itemInfo.getTags().add("NLSF Trails");
        itemInfo.getTags().add("Shelter");

        final ListenableFuture<Bitmap> exportImage = myMapView.exportImageAsync();
        exportImage.addDoneListener(new Runnable(){

            @Override
            public void run() {

                try {

                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap mapImage = exportImage.get();
                    MediaActionSound sound = new MediaActionSound();
                    sound.play(MediaActionSound.SHUTTER_CLICK);
                    ImageView imageView = findViewById(R.id.imageView);
                    Bitmap thumbnailImage = Bitmap.createScaledBitmap(mapImage, 200, 200, false);
                    //imageView.getDrawable();
                    imageView.setImageBitmap(thumbnailImage);
                    //BitmapDrawable drawable = new BitmapDrawable(getResources(),thumbnailImage);
                    // imageView.setImageDrawable(drawable);

                    // Convert to byte[]
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnailImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] thumbnailBytes = stream.toByteArray();
                    stream.close();
                    // Set values to the itemInfo
                    itemInfo.setThumbnailData(thumbnailBytes);
                    showMessage("Generating done!");
                    // Set metadata to parameters
                    mapParameters.setItemInfo(itemInfo);
                    showMessage("ItemInfo has been set");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
    }



    //Start offline DownloadJob

    public void StartDownload(){
        final OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        String mExportPath = String.valueOf(Environment.getExternalStorageDirectory()) + File.separator + "New";
        showMessage(mExportPath);

        //downloadButton.setVisibility(View.VISIBLE);
        // Create and start a job to generate the offline map

        final GenerateOfflineMapJob generateOfflineJob = offlineMapTask.generateOfflineMap(mapParameters, mExportPath);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Show that job started
        //final ProgressBar progressBarOffline = (ProgressBar) findViewById(R.id.progressBar);
        //progressBarOffline.setVisibility(View.VISIBLE);
        generateOfflineJob.start();
        generateOfflineJob.addJobDoneListener(new Runnable(){
            @Override
            public void run() {

                // Generate the offline map and download it
                GenerateOfflineMapResult result = generateOfflineJob.getResult();

                if (!result.hasErrors()) {

                    showMessage("no error");
                    MobileMapPackage mobileMapPackage = result.getMobileMapPackage();
                    // Job is finished and all content was generated
                    showMessage("Map " + mobileMapPackage.getItem().getTitle() + " saved to " + mobileMapPackage.getPath());

                    // Show offline map in a MapView
                    myMapView.setMap(null);

                    myMapView.setMap(result.getOfflineMap());

                    // Show that job completed
                    // progressBarOffline.setVisibility(View.INVISIBLE);
                } else {

                    showMessage("error");
                    // Job is finished but some of the layers/tables had errors
                    if (result.getLayerErrors().size() > 0) {
                        for (java.util.Map.Entry<Layer, ArcGISRuntimeException> layerError : result.getLayerErrors().entrySet()) {
                            showMessage("Error occurred when taking " + layerError.getKey().getName() + " offline.");
                            showMessage("Error : " + layerError.getValue().getMessage());
                        }
                    }
                    if (result.getTableErrors().size() > 0) {
                        for (java.util.Map.Entry<FeatureTable, ArcGISRuntimeException> tableError : result.getTableErrors().entrySet()) {
                            showMessage("Error occurred when taking " + tableError.getKey().getTableName() + " offline.");
                            showMessage("Error : " + tableError.getValue().getMessage());
                        }
                    }
                    // Show that job completed
                    // progressBarOffline.setVisibility(View.INVISIBLE);
                }
            }

        });

    }


    //Check Offline capabilities method
    public void CheckCapability() {
        final OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        final ListenableFuture<OfflineMapCapabilities> offlineMapCapabilitiesFuture = offlineMapTask.getOfflineMapCapabilitiesAsync(mapParameters);
        offlineMapCapabilitiesFuture.addDoneListener(new Runnable() {
            @Override public void run() {
                try {
                    OfflineMapCapabilities offlineMapCapabilities = offlineMapCapabilitiesFuture.get();
                    if (offlineMapCapabilities.hasErrors()) {
                        // Handle possible errors with layers
                        for (java.util.Map.Entry<Layer, OfflineCapability> layerCapability :
                                offlineMapCapabilities.getLayerCapabilities().entrySet()) {
                            if (!layerCapability.getValue().isSupportsOffline()) {
                                showMessage(layerCapability.getKey().getName() + " cannot be taken offline\n" + "Error : " + layerCapability.getValue().getError().getMessage());
                            }
                        }

                        // Handle possible errors with tables
                        for (java.util.Map.Entry<FeatureTable, OfflineCapability> tableCapability :
                                offlineMapCapabilities.getTableCapabilities().entrySet()) {
                            if (!tableCapability.getValue().isSupportsOffline()) {
                                showMessage(tableCapability.getKey().getTableName() + " cannot be taken offline\n" + "Error : " + tableCapability.getValue().getError().getMessage());
                            }
                        }
                    } else {
                        // All layers and tables can be taken offline!
                        showMessage("All layers are good to go!");
                        // downloadButton.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void showMessage(String s) {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void OfflineButton() {
        //setup export tiles button
        downloadButton = (Button) findViewById(R.id.offline_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                BitmapView();

                Toast.makeText(getBaseContext(), "Select Map Area", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void DownloadButton() {
        //setup export tiles button
        downloadButton = (Button) findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                StartDownload();
                Toast.makeText(getBaseContext(), "Start Download", Toast.LENGTH_SHORT).show();
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

}
