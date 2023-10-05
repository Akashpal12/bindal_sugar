package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;
import in.co.vibrant.bindalsugar.view.supervisor.StaffDevelopmentGrowerFinder;

public class RaStaffGrowerFinderMapView extends AppCompatActivity implements OnMapReadyCallback {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ra_staff_crop_observation_map_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Near By Plot View");
        context= RaStaffGrowerFinderMapView.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button resendData=findViewById(R.id.resendData);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=dbh.getUserDetailsModel();
        plotFarmerShareModelList=new ArrayList<>();
        try {
            //lat=29.3934217;
            //lng=78.16364;
            txtLat=findViewById(R.id.txt_lat);
            txtLng=findViewById(R.id.txt_lng);
            txtAccuracy=findViewById(R.id.txt_accuracy);
            txtAddress=findViewById(R.id.txt_address);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(RaStaffGrowerFinderMapView.this);
            //fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
            startLocationUpdates();
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
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
                                if(APIUrl.isDebug)
                                {
                                    lat=APIUrl.lat;
                                    lng=APIUrl.lng;
                                }
                                final List<Address> addresses;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                String address = "";
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                txtLat.setText(""+lat);
                                txtLng.setText(""+lng);
                                txtAccuracy.setText(""+new DecimalFormat("0.00").format(accuracy) +"  ("+plotFarmerShareModelList.size()+" Plot Find)");
                                txtAddress.setText(""+addresses.get(0).getAddressLine(0));
                                LatLng latLng=new LatLng(lat,lng);
                                MarkerOptions markerOpt = new MarkerOptions();
                                markerOpt.title(userDetailsModels.get(0).getName());
                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_man);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                markerOpt.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                mMap.clear();
                                drawPlot();
                                mMap.addMarker(markerOpt);//.showInfoWindow();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().RedDialog(this,"Security Error:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().RedDialog(this,"Error:"+se.toString());
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
                            if (location != null) {
                                mMap.setBuildingsEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.setMyLocationEnabled(true);
                                GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ReportMap.this, R.raw.style_night_map));
                                UiSettings uiSettings = mMap.getUiSettings();
                                uiSettings.setMyLocationButtonEnabled(true);
                                uiSettings.setAllGesturesEnabled(true);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location
                                                .getLongitude()), 20.0f));
                                try {
                                    plotFarmerShareModelList = dbh.getNearByPlotList(location.getLatitude(), location
                                            .getLongitude());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                drawPlot();
                            }
                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    public void openRefreshPlot(View v) throws Exception {
        plotFarmerShareModelList = dbh.getNearByPlotList(lat, lng);
        drawPlot();
    }

    private void drawPlot()
    {
        try {

            //List<PlotFarmerShareModel> plotFarmerShareModelList = dbh.getPlotFarmerShareModelForMap("1014", "");
            if (plotFarmerShareModelList.size() > 0) {
                for (int i = 0; i < plotFarmerShareModelList.size(); i++) {
                    ArrayList<LatLng> latLngs=new ArrayList<>();

                    PolygonOptions polygonOptions=new PolygonOptions();
                    if(Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat())>0)
                    {
                        latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLng())));
                        polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLng())));
                    }
                    if(Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLat())>0)
                    {
                        latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLng())));
                        polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLng())));
                    }
                    if(Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLat())>0)
                    {
                        latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLng())));
                        polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLng())));
                    }
                    if(Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLat())>0)
                    {
                        latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLng())));
                        polygonOptions.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLng())));
                    }
                    polygonOptions.strokeColor(Color.parseColor("#000000"));
                    polygonOptions.strokeWidth(2);
                    polygonOptions.fillColor(Color.parseColor("#6AFBED6A"));
                    mMap.addPolygon(polygonOptions);

                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(RaStaffGrowerFinderMapView.this);
                    mMap.setInfoWindowAdapter(adapter);
                    MarkerOptions markerOpt = new MarkerOptions();
                    LatLng midPoint=getPolygonCenterPoint(latLngs);
                    markerOpt.position(midPoint)
                            .title(plotFarmerShareModelList.get(i).getGrowerCode()+" - "+plotFarmerShareModelList.get(i).getGrowerName())
                            .snippet("Father  :"+plotFarmerShareModelList.get(i).getGrowerFatherName()+"\n"+
                                    "Area     :"+plotFarmerShareModelList.get(i).getAreaHectare()+"\n"+
                                    "Area %   :"+plotFarmerShareModelList.get(i).getShare()+"\n"+
                                    "Variety  :"+plotFarmerShareModelList.get(i).getVarietyCode()+"\n"+
                                    "Cane Type:"+plotFarmerShareModelList.get(i).getCaneType()+"\n"+
                                    "E: "+plotFarmerShareModelList.get(i).getEastNorthDistance()+"   S: "+plotFarmerShareModelList.get(i).getEastSouthDistance()+
                                    "   W: "+plotFarmerShareModelList.get(i).getWestSouthDistance()+"   N: "+plotFarmerShareModelList.get(i).getWestNorthDistance()
                            )
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green));
                    mMap.addMarker(markerOpt);//.showInfoWindow();

                }
                LatLng latLng=new LatLng(lat,lng);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            } else {
                //AlertPopUp("Plot not found");
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    public void openRacropobservation(View v)
    {
        Intent intent = new Intent(context, StaffDevelopmentGrowerFinder.class);
        intent.putExtra("lat", "" + lat);
        intent.putExtra("lng", "" + lng);
        startActivity(intent);
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

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RaStaffGrowerFinderMapView.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void AlertPopUpWithBack(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RaStaffGrowerFinderMapView.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }


}
