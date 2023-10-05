package in.co.vibrant.bindalsugar.view.supervisor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.admin.LiveTrackingLise;


public class LiveLocationMap extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {




    View view;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Context context;
    DBHelper dbh;
    SQLiteDatabase db;
    String lat;
    String lag;
    String Name;
    String Date;
    String Address;
    String Speed;
    String Accuracy;
    String Appversion;
    int Battery;
    String Bearing;
    String Charging;
    String Creatdate;
    String GpsStatus;
    String InternetStatus;
    String Provider;
    ImageView battery_icon,charger,internat;
    SupportMapFragment mapFragment;
    TextView name,date,address,speed,buttery,CHARGING,CREATEDAT,GPSSTATUS;
    double sett_longitude2,sett_latitude2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.livelocationmap);
            context=this;
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            setTitle("Live Location");

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(LiveLocationMap.this);
            //--------------BACK BUTTON----------------------------------
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LiveTrackingLise.class);
                    startActivity(intent);
                    finish();
                    onBackPressed();

                }
            });
            //-----------------------------------------------------------
            name=findViewById(R.id.name);
            date=findViewById(R.id.date);
            address=findViewById(R.id.address);
            speed=findViewById(R.id.speed);
            buttery=findViewById(R.id.buttery);
            CHARGING=findViewById(R.id.CHARGING);
            CREATEDAT=findViewById(R.id.CREATEDAT);
            GPSSTATUS=findViewById(R.id.GPSSTATUS);
            battery_icon=findViewById(R.id.battery_icon);
            charger=findViewById(R.id.charger);
            internat=findViewById(R.id.internat);


        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
        }






        String sett_longitude = getIntent().getStringExtra("lag");
        sett_longitude2 = Double.parseDouble(sett_longitude);

        String sett_latitude = getIntent().getStringExtra("lat");
        sett_latitude2 = Double.parseDouble(sett_latitude);




        Name=getIntent().getExtras().getString("Name");
        name.setText(Name);

        Address=getIntent().getExtras().getString("Address");
        address.setText(Address);

        Date=getIntent().getExtras().getString("Dates");
        date.setText(Date);

        Speed=getIntent().getExtras().getString("Speed");
        Accuracy=getIntent().getExtras().getString("Accuracy");
        Appversion=getIntent().getExtras().getString("Appversion");
        speed.setText("Speed : " +""+Speed+" "+"  Accuracy : "+Accuracy);

        Battery=getIntent().getExtras().getInt("Buttery");

        Bearing=getIntent().getExtras().getString("Bearing");



        Charging =getIntent().getExtras().getString("charger");
        /*Creatdate =getIntent().getExtras().getString("Dates");
        CREATEDAT.setText(Creatdate);
*/
        GpsStatus =getIntent().getExtras().getString("GPS");
        InternetStatus =getIntent().getExtras().getString("InternetStatus");
        Provider =getIntent().getExtras().getString("Provider");
        GPSSTATUS.setText("GPSStatus : "+""+GpsStatus+ "   InternetStatus : "+InternetStatus+"   Provider : "+Provider);



        //---------------battery logic------------------------------------------------
        if(Battery<25 ){
            buttery.setText("Buttery : "+Battery);
            battery_icon.setImageResource(R.drawable.ic_baseline_battery_0_bar_24);

        }else if( Battery<50) {
            buttery.setText("Buttery : "+Battery);
            battery_icon.setImageResource(R.drawable.ic_baseline_battery_4_bar_24);
        }
        else if(Battery >50) {
            buttery.setText("Buttery : "+Battery);
            battery_icon.setImageResource(R.drawable.ic_baseline_battery_5_bar_24);
        }
        else if(Battery<75) {
            buttery.setText("Buttery : "+Battery);
            battery_icon.setImageResource(R.drawable.ic_baseline_battery_6_bar_24);
        }
        else if(Battery>100) {
            buttery.setText("Buttery : "+Battery);
            battery_icon.setImageResource(R.drawable.ic_baseline_battery_std_24);
        }
        //-----------------------------------------------------------------------------------
        if(Charging.equalsIgnoreCase("Charger Not Connected")) {
            charger.setImageResource(R.drawable.ic_baseline_battery_charging_full_24);

        }else{
            charger.setImageResource(R.drawable.ic_baseline_battery_std_24);
        }

        if(InternetStatus.equalsIgnoreCase("true")) {

            internat.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_24);

        }else{
            internat.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_2_bar_24);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplication(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplication())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.getUiSettings().setZoomControlsEnabled(true);
*/


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (location != null) {
            //sync map
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                    mMap = googleMap;
                    //Initialize lat lng
                    LatLng latLng = new LatLng(sett_latitude2, sett_longitude2);
                    // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                    //Create marker options
                            /*MarkerOptions options=new MarkerOptions().position(latLng).title("");
                            googleMap.addMarker(options);*/

                    //Zoom map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                    //set the min/max google_map
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    //Add Marker on map
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("")
                            // below line is use to add custom marker on our map.
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.mapicon)));

                }
            });

        }



        //-------------------set the current Address------------------------------------
        try {
            Geocoder geo = new Geocoder(getApplication().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.isEmpty()) {
                //activity.getSupportActionBar().setSubtitle("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {

                    // activity.getSupportActionBar().setSubtitle(addresses.get(0).getFeatureName()+"," + addresses.get(0).getLocality()+", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        //-----------------------------------------------------------------------------

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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}








