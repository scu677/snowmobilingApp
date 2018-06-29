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


import android.app.AlertDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.service.autofill.SaveInfo;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.internal.c.n;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
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
import com.esri.arcgisruntime.tasks.offlinemap.PreplannedMapArea;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;


public class OfflineMaps extends AppCompatActivity {
   // private Geometry areaOfInterest;
    private MapView myMapView;
    private ArcGISMap map;
    private OfflineMapTask offlineMapTask;
    private Portal myPortal;
    private PortalItem myPortalItem;
    private Button downloadButton;
    private OfflineMapItemInfo ItemInfo;
    private LocationDisplay LD;
    private GenerateOfflineMapJob offlineMapJob;
    private GenerateOfflineMapParameters mapParameters;
    private ListenableFuture<Bitmap> exportImage;
    private ListenableFuture<GenerateOfflineMapParameters> offlineMapParameters;
    private SaveInfo saveInfo;
    private ProgressBar ProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_maps);
        Portal myPortal = new Portal("https://gis.resourceinnovations.ca/gis");
        PortalItem myPortalItem = new PortalItem(myPortal, "b53f696fea544843b1c139a3c1b252ef");
        map = new ArcGISMap(myPortalItem);
        myMapView = findViewById(R.id.mapOffline);
        myMapView.setMap(map);
        Point leftPoint = new Point(-6641791.193399999, 5853988.327799998, SpatialReference.create(3857));
        Point rightPoint = new Point(-5808352.0265, 6772993.915799998, SpatialReference.create(3857));
        Envelope initialExtent = new Envelope(leftPoint, rightPoint);
        Viewpoint vp = new Viewpoint(initialExtent);
        map.setInitialViewpoint(vp);
        map.setMaxScale(20000);
        map.setMinScale(5800000);
        ProgressBar =  findViewById(R.id.progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        if (offlineMapTask.getLoadStatus() != null) {
            GenerateParameters();

        } else {
            showMessage("offlineTask failed");

        }
            OfflineButton();
            DownloadButton();

    }
        //Generate map parameters method
         private void GenerateParameters() {
        OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        Geometry areaOfInterest = new Geometry() {
            @Override
            public Envelope getExtent() {

                return super.getExtent();
            }
        };

        final ListenableFuture<GenerateOfflineMapParameters> ParameterFuture = offlineMapTask.createDefaultGenerateOfflineMapParametersAsync(areaOfInterest);

        ParameterFuture.addDoneListener(new Runnable(){
            @Override
            public void run(){
                try {
                    final GenerateOfflineMapParameters mapParameters = ParameterFuture.get();
                    mapParameters.setMaxScale(5000);
                    mapParameters.setIncludeBasemap(true);
                    mapParameters.setAttachmentSyncDirection(GenerateGeodatabaseParameters.AttachmentSyncDirection.NONE);
                    mapParameters.setReturnLayerAttachmentOption(GenerateOfflineMapParameters.ReturnLayerAttachmentOption.READ_ONLY_LAYERS);
                    mapParameters.setReturnSchemaOnlyForEditableLayers(true);
                    mapParameters.getItemInfo().setTitle(mapParameters.getItemInfo().getTitle() + "(Central)");
                    showMessage("GenerateOfflineMapParameters has been set");

                } catch (Exception e) {
                    e.getMessage();
                }
            }

        }); showMessage("Generating Parameters");
    }
    //protected void onDraw(Canvas canvas) {
        //drawable.draw(canvas);

   // }

    //Start Bitmap action method
    public void BitmapView(){
        ProgressBar.setVisibility(View.VISIBLE);

                    final OfflineMapItemInfo itemInfo = new OfflineMapItemInfo();

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

                                //Bitmap capturing and scaling
                                Bitmap thumbnailImage = Bitmap.createScaledBitmap(mapImage, 400, 233, false);
                                Drawable drawable = new BitmapDrawable(getResources(), thumbnailImage);
                                //imageView.setVisibility(View.VISIBLE);
                                //Canvas canvas = null;
                                //drawable.draw(canvas);
                                imageView.setImageDrawable(drawable);
                                  //imageView.setImageBitmap(thumbnailImage);
                                  imageView.getBackground();

                                  //imageView.setImageDrawable(drawable);
                                //if (mapImage != thumbnailImage) {
                                  //  mapImage.recycle();
                                //}
                                // Convert to byte[]
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                thumbnailImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                byte[] thumbnailBytes = stream.toByteArray();
                                stream.flush();
                                stream.close();
                                ProgressBar.setVisibility(View.INVISIBLE);
                                itemInfo.setTitle("snowmobiling (Central)");
                                itemInfo.setSnippet(myPortalItem.getSnippet()); // Copy from the source map
                                itemInfo.setDescription(myPortalItem.getDescription()); // Copy from the source map
                                itemInfo.setAccessInformation(myPortalItem.getAccessInformation()); // Copy from the source map
                                itemInfo.getTags().add("NLSF Trails");
                                itemInfo.getTags().add("Shelter");

                                // Set values to the itemInfo
                                itemInfo.setThumbnailData(thumbnailBytes);

                                // Set metadata to parameters
                                mapParameters.setItemInfo(itemInfo);
                                showMessage("ItemInfo has been set");
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    });
    }


    //Define Error message method
    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    //Check Offline capabilities method
    private void CheckCapability(){


        offlineMapTask = new OfflineMapTask(map);
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

    //Start offline DownloadJob
    private void StartDownload() {

        String mExportPath = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + File.separator + "New";
        showMessage(mExportPath);
        showMessage("Generating Parameters");
        //downloadButton.setVisibility(View.VISIBLE);

        // Create and start a job to generate the offline map
        OfflineMapTask offlineMapTask = new OfflineMapTask(map);
        final GenerateOfflineMapJob generateOfflineJob = offlineMapTask.generateOfflineMap(mapParameters, mExportPath);
        // Show that job started
       //final ProgressBar progressBarOffline = (ProgressBar) findViewById(R.id.progressBar);
       // progressBarOffline.setVisibility(View.VISIBLE);

        generateOfflineJob.start();
        generateOfflineJob.addJobDoneListener(new Runnable() {
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

    private void OfflineButton() {
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
    private void DownloadButton() {
        //setup export tiles button
        downloadButton = (Button) findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                StartDownload();
                Toast.makeText(getBaseContext(), "Start Download", Toast.LENGTH_SHORT).show();
            }
        });
    }


  /*@Override
  protected void onPause(){
      myMapView.pause();
      super.onPause();

  }

    @Override
    protected void onResume(){
        super.onResume();
        myMapView.resume();
    }*/



}
