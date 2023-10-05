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
import in.co.vibrant.bindalsugar.view.reportingauthority.platingreport.RaPlantingDashboardReportDetails;
import in.co.vibrant.bindalsugar.view.reportingauthority.platingreport.TodayActivityReportSupervisorWise;


public class RaReportDashboard extends AppCompatActivity  {

    SQLiteDatabase db;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest request;
    private LocationCallback locationCallback;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaReportDashboard.this;
        setTitle(getString(R.string.MENU_REPORT));
        toolbar.setTitle(getString(R.string.MENU_REPORT));
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

    public void openTargetReport(View v)
    {
        Intent intent=new Intent(context, RaReportTargetWiseDashboard.class);
        startActivity(intent);
    }

    public void openNonTargetReport(View v)
    {
        Intent intent=new Intent(context, RaReportNonTargetWiseDashboard.class);
        startActivity(intent);
    }

    public void openIndentReport(View v)
    {
        Intent intent=new Intent(context, RaIndentReportDetails.class);
        startActivity(intent);
    }

    public void openPlantingReport(View v)
    {
        Intent intent=new Intent(context, RaPlantingDashboardReportDetails.class);
        startActivity(intent);
    }

    public void openTodayActivityReportSupervisorWise(View v)
    {
        Intent intent=new Intent(context, TodayActivityReportSupervisorWise.class);
        startActivity(intent);
    }



}
