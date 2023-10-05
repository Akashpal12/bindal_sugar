package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import in.co.vibrant.bindalsugar.R;


public class RaReportNonTargetWiseDashboard extends AppCompatActivity  {

    SQLiteDatabase db;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest request;
    private LocationCallback locationCallback;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_target_wise_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaReportNonTargetWiseDashboard.this;
        setTitle(getString(R.string.MENU_REPORT_NON_TARGET));
        toolbar.setTitle(getString(R.string.MENU_REPORT_NON_TARGET));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest();
        request.setInterval(10 * 1000);
        request.setFastestInterval(5 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };
    }

    public void openZoneWiseArea(View v)
    {
        Intent intent=new Intent(context, RaNonZoneWiseAreaReport.class);
        startActivity(intent);
    }

    public void openVarietyWiseArea(View v)
    {
        Intent intent=new Intent(context, RaNonVarietyWiseAreaReport.class);
        startActivity(intent);
    }

    public void openZoneWithVarietyReport(View v)
    {
        Intent intent=new Intent(context, RaNonZoneWithVarietyReport.class);
        startActivity(intent);
    }

    public void openVarietyWithZone(View v)
    {
        Intent intent=new Intent(context, RaNonVarietyWithZoneReport.class);
        startActivity(intent);
    }

    public void openSupervisorWithVarietyReport(View v)
    {
        Intent intent=new Intent(context, RaNonSupervisorWithVarietyReport.class);
        startActivity(intent);
    }

    public void openRaCircleWithZoneReport(View v)
    {
        Intent intent=new Intent(context, RaNonCircleWithZoneReport.class);
        startActivity(intent);
    }

    public void openSupervisorReport(View v)
    {
        Intent intent=new Intent(context, RaNonSupervisorWiseAreaReport.class);
        startActivity(intent);
    }

    public void openRaCircleReport(View v)
    {
        Intent intent=new Intent(context, RaCircleWithZoneReport.class);
        startActivity(intent);
    }

}
