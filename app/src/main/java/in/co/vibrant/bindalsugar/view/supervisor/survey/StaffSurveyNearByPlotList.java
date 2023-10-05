package in.co.vibrant.bindalsugar.view.supervisor.survey;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.LocationDataModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;


public class StaffSurveyNearByPlotList extends AppCompatActivity implements OnMapReadyCallback {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;
    double lat=0,lng=0,accuracy;
    Geocoder geocoder;
    TextView txtLat,txtLng,txtAccuracy,txtAddress;
    GoogleMap mMap;
    Context context;
    List<PlotFarmerShareModel> plotFarmerShareModelList;
    List<UserDetailsModel> userDetailsModels;
    String message;
    List<PlotFarmerShareModel> plotFarmerShareArray;
    List<VillageSurveyModel> defaultVillageModelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_plot_map_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context= StaffSurveyNearByPlotList.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbh = new DBHelper(this);
        RelativeLayout rl_notification=findViewById(R.id.rl_notification);
        rl_notification.setVisibility(View.VISIBLE);
        db = new DBHelper(this).getWritableDatabase();
        defaultVillageModelLists=dbh.getDefaultSurveyVillage();
        setTitle("Find plot in "+defaultVillageModelLists.get(0).getVillageName());
        toolbar.setTitle("Find plot in "+defaultVillageModelLists.get(0).getVillageName());
        userDetailsModels=dbh.getUserDetailsModel();
        plotFarmerShareModelList=new ArrayList<>();
        plotFarmerShareArray=new ArrayList<>();
        try {
            //lat=29.3934217;
            //lng=78.16364;
            txtLat=findViewById(R.id.txt_lat);
            txtLng=findViewById(R.id.txt_lng);
            txtAccuracy=findViewById(R.id.txt_accuracy);
            txtAddress=findViewById(R.id.txt_address);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(StaffSurveyNearByPlotList.this);
            //fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
            startLocationUpdates();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error 2:"+e.toString());
        }

    }

    protected void startLocationUpdates() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //checkUpdate();
                }
            });
            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity) context,
                                    1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            try {
                                Location location = locationResult.getLastLocation();
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                accuracy = location.getAccuracy();
                                try{
                                    final List<Address> addresses;
                                    geocoder = new Geocoder(context, Locale.getDefault());
                                    String address = "";
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    txtAddress.setText(""+addresses.get(0).getAddressLine(0));
                                }
                                catch (Exception e)
                                {
                                    txtAddress.setText("Error : "+e.getMessage());
                                }

                                txtLat.setText(""+lat);
                                txtLng.setText(""+lng);
                                txtAccuracy.setText(""+new DecimalFormat("0.00").format(accuracy) +"  ("+plotFarmerShareArray.size()+" Plot Find)");

                                LatLng latLng=new LatLng(lat,lng);
                                MarkerOptions markerOpt = new MarkerOptions();
                                markerOpt.title(userDetailsModels.get(0).getName());
                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_man);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                markerOpt.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                //mMap.animateCamera();
                                mMap.clear();
                                if(plotFarmerShareArray.size()>0)
                                    drawPlot();
                                mMap.addMarker(markerOpt);//.showInfoWindow();

                            }
                            catch(SecurityException se)
                            {
                                //new AlertDialogManager().RedDialog(this,"Security Error 3:"+se.toString());
                            }
                            catch (Exception e)
                            {
                                Log.d("Error",e.getMessage());
                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().RedDialog(this,"Security Error 3:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().RedDialog(this,"Error 4:"+se.toString());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            try{
                                if (location != null) {
                                    mMap.setBuildingsEnabled(true);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                    mMap.setMyLocationEnabled(true);
                                    GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
                                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                    //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ReportMap.this, R.raw.style_night_map));
                                    UiSettings uiSettings = mMap.getUiSettings();
                                    uiSettings.setMyLocationButtonEnabled(true);
                                    uiSettings.setAllGesturesEnabled(true);
                                    //new GetMapData().execute();
                                    plotFarmerShareArray=dbh.getIdentifyPlotSurveyPlantingModelByLatLngNearBy(lat,lng,defaultVillageModelLists.get(0).getVillageCode());
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 18.0f));
                                }
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    public void openRefreshPlot(View v)
    {
        try{
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 18.0f));
            plotFarmerShareArray=dbh.getIdentifyPlotSurveyPlantingModelByLatLngNearBy(lat,lng,defaultVillageModelLists.get(0).getVillageCode());
            //drawPlot();
        }
        catch (Exception e)
        {

        }
    }

    public void openPlotSurvey(View v)
    {
        Intent intent = new Intent(context, NewPlotEntryCane.class);
        intent.putExtra("plotFrom", "None");
        intent.putExtra("plotSerialNumber", "0");
        finish();
        startActivity(intent);
    }

    public void openPlotActivity(View v)
    {
        try{
            List<PlotFarmerShareModel> plotFarmerShareArray1=dbh.getIdentifyPlotSurveyPlantingModelByLatLngExats(lat,lng,defaultVillageModelLists.get(0).getVillageCode());
            if(plotFarmerShareArray1.size()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Your are not inside plot please move in plot then click on proceed");
                return;
            }
            if(plotFarmerShareArray1.get(0).getPlotFrom().equalsIgnoreCase("Current Year Survey"))
            {
                new AlertDialogManager().showToast((Activity) context,"Survey already completed for this plot");
                return;
            }
            dbh.truncateLocationDataModel();
            LocationDataModel locationDataModel1=new LocationDataModel();
            locationDataModel1.setLat(Double.parseDouble(plotFarmerShareArray1.get(0).getEastNorthLat()));
            locationDataModel1.setLng(Double.parseDouble(plotFarmerShareArray1.get(0).getEastNorthLng()));
            locationDataModel1.setAccuracy(Double.parseDouble(plotFarmerShareArray1.get(0).getEastNorthAccuracy()));
            locationDataModel1.setDistance(Double.parseDouble(plotFarmerShareArray1.get(0).getEastNorthDistance()));
            locationDataModel1.setSerialNumber(dbh.getLocationDataModel("").size()+1);
            dbh.insertLocationDataModel(locationDataModel1);

            LocationDataModel locationDataModel2=new LocationDataModel();
            locationDataModel2.setLat(Double.parseDouble(plotFarmerShareArray1.get(0).getEastSouthLat()));
            locationDataModel2.setLng(Double.parseDouble(plotFarmerShareArray1.get(0).getEastSouthLng()));
            locationDataModel2.setAccuracy(Double.parseDouble(plotFarmerShareArray1.get(0).getEastSouthAccuracy()));
            locationDataModel2.setDistance(Double.parseDouble(plotFarmerShareArray1.get(0).getEastSouthDistance()));
            locationDataModel2.setSerialNumber(dbh.getLocationDataModel("").size()+1);
            dbh.insertLocationDataModel(locationDataModel2);

            LocationDataModel locationDataModel3=new LocationDataModel();
            locationDataModel3.setLat(Double.parseDouble(plotFarmerShareArray1.get(0).getWestSouthLat()));
            locationDataModel3.setLng(Double.parseDouble(plotFarmerShareArray1.get(0).getWestSouthLng()));
            locationDataModel3.setAccuracy(Double.parseDouble(plotFarmerShareArray1.get(0).getWestSouthAccuracy()));
            locationDataModel3.setDistance(Double.parseDouble(plotFarmerShareArray1.get(0).getWestSouthDistance()));
            locationDataModel3.setSerialNumber(dbh.getLocationDataModel("").size()+1);
            dbh.insertLocationDataModel(locationDataModel3);

            LocationDataModel locationDataModel4=new LocationDataModel();
            locationDataModel4.setLat(Double.parseDouble(plotFarmerShareArray1.get(0).getWestNorthLat()));
            locationDataModel4.setLng(Double.parseDouble(plotFarmerShareArray1.get(0).getWestNorthLng()));
            locationDataModel4.setAccuracy(Double.parseDouble(plotFarmerShareArray1.get(0).getWestNorthAccuracy()));
            locationDataModel4.setDistance(Double.parseDouble(plotFarmerShareArray1.get(0).getWestNorthDistance()));
            locationDataModel4.setSerialNumber(dbh.getLocationDataModel("").size()+1);
            dbh.insertLocationDataModel(locationDataModel4);

            Intent intent = new Intent(context, NewPlotEntryCane.class);
            intent.putExtra("plotFrom", plotFarmerShareArray1.get(0).getPlotFrom());
            intent.putExtra("plotSerialNumber", plotFarmerShareArray1.get(0).getPlotSrNo());
            finish();
            startActivity(intent);
        }
        catch (Exception e)
        {

        }
    }


    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }

    public void drawPlot() throws JSONException {
        if(plotFarmerShareArray.size()>0)
        {
            for (int i = 0; i < plotFarmerShareArray.size(); i++) {
                PlotFarmerShareModel plotFarmerShareModel=new PlotFarmerShareModel();
                plotFarmerShareModel=plotFarmerShareArray.get(i);
                ArrayList<LatLng> latLngs=new ArrayList<>();

                String color="#6AFBED6A";//Yellow'
                if(plotFarmerShareArray.get(i).getPlotFrom().equalsIgnoreCase("Current Year Survey"))
                {
                    color="#6AFBED6A";
                }
                else if(plotFarmerShareArray.get(i).getPlotFrom().equalsIgnoreCase("Last Year Survey"))
                {
                    color="#593F51B5";
                }
                else
                {
                    color="#5CE91E63";
                }
                /*if(jsonObject.getString("COLORCODE").equalsIgnoreCase("0"))
                {
                    color="#6AFBED6A";//Yellow
                }
                else
                {
                    color=jsonObject.getString("COLORCODE").toUpperCase();
                }*/
                PolygonOptions polygonOptions=new PolygonOptions();
                if(Double.parseDouble(plotFarmerShareModel.getEastNorthLat())>0)
                {
                    polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getEastNorthLat()), Double.parseDouble(plotFarmerShareModel.getEastNorthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getEastNorthLat()), Double.parseDouble(plotFarmerShareModel.getEastNorthLng())));
                }
                if(Double.parseDouble(plotFarmerShareModel.getEastSouthLat())>0){
                    polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getEastSouthLat()), Double.parseDouble(plotFarmerShareModel.getEastSouthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getEastSouthLat()), Double.parseDouble(plotFarmerShareModel.getEastSouthLng())));
                }
                if(Double.parseDouble(plotFarmerShareModel.getWestSouthLat())>0){
                    polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getWestSouthLat()), Double.parseDouble(plotFarmerShareModel.getWestSouthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getWestSouthLat()), Double.parseDouble(plotFarmerShareModel.getWestSouthLng())));
                }
                if(Double.parseDouble(plotFarmerShareModel.getWestNorthLat())>0)
                {
                    polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getWestNorthLat()), Double.parseDouble(plotFarmerShareModel.getWestNorthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModel.getWestNorthLat()), Double.parseDouble(plotFarmerShareModel.getWestNorthLng())));
                }


                polygonOptions.strokeColor(Color.parseColor("#000000"));
                polygonOptions.strokeWidth(2);
                polygonOptions.fillColor(Color.parseColor(color));

                mMap.addPolygon(polygonOptions);

                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(StaffSurveyNearByPlotList.this);
                mMap.setInfoWindowAdapter(adapter);
                MarkerOptions markerOpt = new MarkerOptions();
                LatLng midPoint=getPolygonCenterPoint(latLngs);
                markerOpt.position(midPoint)
                        .title(plotFarmerShareModel.getGrowerCode()+" - "+plotFarmerShareModel.getGrowerName())
                        .snippet("Father  :"+plotFarmerShareModel.getGrowerFatherName()+"\n"+
                                "Area     :"+plotFarmerShareModel.getAreaHectare()+"\n"+
                                "Area %   :"+plotFarmerShareModel.getShare()+"\n"+
                                "E: "+plotFarmerShareModel.getEastNorthDistance()+"   S: "+plotFarmerShareModel.getEastSouthDistance()+
                                "   W: "+plotFarmerShareModel.getWestSouthDistance()+"   N: "+plotFarmerShareModel.getWestNorthDistance()
                        )
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green));
                mMap.addMarker(markerOpt);

            }

        }
    }

}
