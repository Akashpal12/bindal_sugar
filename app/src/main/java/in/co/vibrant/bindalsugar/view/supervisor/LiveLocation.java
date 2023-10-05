package in.co.vibrant.bindalsugar.view.supervisor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.model.LastGpsDataModel;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.GpsCheck;

public class LiveLocation extends AppCompatActivity
        implements OnMapReadyCallback {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    AlertDialog AlertdialogBox;
    //List<LoginUserDetails> loginUserDetailsList;
    LinearLayout map_layout;
    Context context;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    TextView app_version, last_refresh_time, device_id, full_name, designation, last_lat, last_lng, last_speed, last_direction, last_accuracy, gps_provider,
            last_time, last_address, last_server, pending_record, error, leave, end_time, time_from, last_distance;
    Switch overtime_mode;

    FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    LocationRequest locationRequest;
    GoogleMap mMap;
    List<UserDetailsModel> userDetailsModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.live_location);
            context = this;


            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            //loginUserDetailsList =dbh.getLoginUserAllData();

            //--------------BACK BUTTON----------------------------------
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.MENU_LIVE_LOCATION));
            boolean IsRatingDialogue = true;
            Boolean doubleBackToExitPressedOnce = true;
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            map_layout = findViewById(R.id.map_layout);
            app_version = findViewById(R.id.app_version);
            last_refresh_time = findViewById(R.id.last_refresh_time);
            device_id = findViewById(R.id.device_id);
            full_name = findViewById(R.id.full_name);
            designation = findViewById(R.id.designation);
            last_lat = findViewById(R.id.last_lat);
            last_lng = findViewById(R.id.last_lng);
            last_speed = findViewById(R.id.last_speed);
            last_direction = findViewById(R.id.last_direction);
            last_accuracy = findViewById(R.id.last_accuracy);
            gps_provider = findViewById(R.id.gps_provider);
            last_time = findViewById(R.id.last_time);
            last_address = findViewById(R.id.last_address);
            last_server = findViewById(R.id.last_server);
            pending_record = findViewById(R.id.pending_record);
            error = findViewById(R.id.error);
            leave = findViewById(R.id.leave);
            end_time = findViewById(R.id.end_time);
            time_from = findViewById(R.id.time_from);
            overtime_mode = findViewById(R.id.overtime_mode);
            last_distance = findViewById(R.id.last_distance);
            userDetailsModels = dbh.getUserDetailsModel();
            if (userDetailsModels.get(0).getOvertimeStatus() == 0) {
                overtime_mode.setChecked(false);
            } else {
                overtime_mode.setChecked(true);
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            /*Intent intent1 = new Intent(Dashboard.this, MyLocationService.class);
            intent1.setAction(MyLocationService.ACTION_START_FOREGROUND_SERVICE);
            *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder ( ServiceWorker.class ).addTag ( "Send_Location_To_Server" ).build ();
                WorkManager.getInstance ( context ).enqueue ( request );
            } else*//*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent1);
            }
            else{
                startService(intent1);
            }*/

            showData();
            findLocation();
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!new GpsCheck(LiveLocation.this).isGpsEnable()) {

        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    showData();
                } catch (Exception e) {
                    //new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                }
            }
        }, 0, 10000);//put here time 1000 milliseconds=1 second
    }



    private void showData() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);

            List<LastGpsDataModel> gpsRecord = dbh.gpsLastRecord();
            String imei = new GetDeviceImei(LiveLocation.this).GetDeviceImeiNumber();
            last_refresh_time.setText(currentDt);
            device_id.setText(imei);
            app_version.setText(BuildConfig.VERSION_NAME);
            device_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            if (userDetailsModels.size() > 0) {
                full_name.setText(userDetailsModels.get(0).getName());
                designation.setText(userDetailsModels.get(0).getUserTypeName());
                time_from.setText("" + userDetailsModels.get(0).getTimeFrom());
                end_time.setText("" + userDetailsModels.get(0).getTimeTo());
                leave.setText("" + userDetailsModels.get(0).getLeaveFlag());

                overtime_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        try {
                            if (b) {
                                dbh.updateOvertime(1);
                                //overtime_mode.setChecked(true);
                            } else {
                                dbh.updateOvertime(0);
                                //overtime_mode.setChecked(false);
                            }
                        } catch (Exception e) {
                            //new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                    }
                });
            }
            if (gpsRecord.size() > 0) {
                last_lat.setText(gpsRecord.get(0).getLat());
                last_lng.setText(gpsRecord.get(0).getLng());
                last_speed.setText(gpsRecord.get(0).getSpeed());
                last_direction.setText(gpsRecord.get(0).getBearing());
                last_accuracy.setText(gpsRecord.get(0).getAccuracy());
                gps_provider.setText(gpsRecord.get(0).getProvider());
                last_time.setText(gpsRecord.get(0).getCreatedAt());
                last_address.setText(gpsRecord.get(0).getAddress());
                last_server.setText(gpsRecord.get(0).getServerSent());
                pending_record.setText("" + dbh.getTotalRecord() + " Records");
                //error.setText(gpsRecord.get(0).getError());
                error.setText("No Error");
                last_distance.setText(gpsRecord.get(0).getDistance() + " M");
                if (Double.parseDouble(gpsRecord.get(0).getAccuracy()) > 100) {
                    map_layout.setVisibility(View.GONE);
                } else {
                    map_layout.setVisibility(View.GONE);
                }

            }
        } catch (Exception ex) {
            //new AlertDialogManager().RedDialog(context,"Error : "+ex.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }


    protected void findLocation() throws Exception, SecurityException {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            //locationRequest.setInterval(1000);
            //locationRequest.setFastestInterval(500);

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
                            new AlertDialogManager().AlertPopUpFinish(context, "Security Error: please enable gps service");
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
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                updateMap(location);
                            }
                        }
                    });
        } catch (SecurityException se) {
            //new AlertDialogManager().RedDialog(context,"Error : "+se.getMessage());
        } catch (Exception se) {
            //new AlertDialogManager().RedDialog(context,"Error : "+se.getMessage());
        }
    }

    private void updateMap(Location locations) {
        try {
            LatLng latLng = new LatLng(locations.getLatitude(), locations.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.setMyLocationEnabled(true);
            GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_map));
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setAllGesturesEnabled(true);

            mMap.addMarker(new MarkerOptions().position(latLng).title("")
                    // below line is use to add custom marker on our map.
                    .icon(BitmapFromVector(LiveLocation.this, R.drawable.mapicon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            CameraPosition myPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(18).bearing(90).tilt(30).build();
            mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(myPosition));
        } catch (SecurityException ex) {
            //new AlertDialogManager().RedDialog(context,"Error : "+ex.getMessage());
        }
    }


}
