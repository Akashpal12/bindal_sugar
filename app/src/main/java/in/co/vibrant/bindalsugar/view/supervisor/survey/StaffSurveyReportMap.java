package in.co.vibrant.bindalsugar.view.supervisor.survey;


import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;


public class StaffSurveyReportMap extends AppCompatActivity implements OnMapReadyCallback {
    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    String villageCode,growerCode;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    double lat,lng;
    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_plot);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_MAP_VIEW_REPORT));
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
        try {
            villageCode = getIntent().getExtras().getString("village_code");
            growerCode = getIntent().getExtras().getString("tv_grower");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(StaffSurveyReportMap.this);
            fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location
                                        .getLongitude()), 15.0f));
                    }
                }

                ;
            };
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
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
                                                .getLongitude()), 16.0f));
                                drawPlot();
                            }
                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    private void drawPlot()
    {
        try {
            List<PlotFarmerShareModel> plotFarmerShareModelList = dbh.getPlotFarmerShareModelForMap(villageCode, growerCode);
            if (plotFarmerShareModelList.size() > 0) {
                for (int i = 0; i < plotFarmerShareModelList.size(); i++) {
                    lat = Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat());
                    lng = Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLng());
                    ArrayList<LatLng> latLngs=new ArrayList<>();
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLng())));
                    latLngs.add(new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLng())));


                    LatLng midPoint=getPolygonCenterPoint(latLngs);
                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(
                                    new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastNorthLng())),
                                    new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestNorthLng())),
                                    new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getWestSouthLng())),
                                    new LatLng(Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLat()), Double.parseDouble(plotFarmerShareModelList.get(i).getEastSouthLng()))
                            )

                            .strokeColor(Color.parseColor("#000000"))
                            .strokeWidth(2)
                            .fillColor(Color.parseColor("#6AFBED6A")));
                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(StaffSurveyReportMap.this);
                    mMap.setInfoWindowAdapter(adapter);
                    MarkerOptions markerOpt = new MarkerOptions();
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
                    mMap.addMarker(markerOpt).showInfoWindow();
                }
                LatLng latLng=new LatLng(lat,lng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            } else {
                AlertPopUp("Plot not found");
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
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

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportMap.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                (dialog, which) ->
                        dialog.cancel()
        );
        alertDialog.show();
    }
    private void AlertPopUpWithBack(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StaffSurveyReportMap.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                (dialog, which) -> {
                    dialog.cancel();
                    finish();
                });
        alertDialog.show();
    }


}
