package in.co.vibrant.bindalsugar.view.supervisor;

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
import in.co.vibrant.bindalsugar.view.reportingauthority.RaVarietyWiseAreaReport;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportOndateTodateTypeOfPlanting;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportTypeOfPlanting;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportVarietyWiseOndateTodatePlanting;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportVarietyWisePlanting;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportVillageWithPlantingTypeReport;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.ReportVillageWithVarietyReport;
import in.co.vibrant.bindalsugar.view.supervisor.plantingreport.TodayActivityReportVillageWise;
import in.co.vibrant.bindalsugar.view.supervisor.survey.StaffSurveyReport;


public class StaffReportDashboard extends AppCompatActivity {

    SQLiteDatabase db;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest request;
    private LocationCallback locationCallback;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_cane_report_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = StaffReportDashboard.this;
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

    public void openStaffTargetReport(View v) {
        Intent intent = new Intent(context, StaffTargetReport.class);
        startActivity(intent);
    }

    public void openVarietyWiseReport(View v) {
        Intent intent = new Intent(context, RaVarietyWiseAreaReport.class);
        startActivity(intent);
    }

    public void openStaffSupervisorVarietyDateWiseAreaReport(View v) {
        Intent intent = new Intent(context, StaffSupervisorVarietyDateWiseAreaReport.class);
        startActivity(intent);
    }

    public void openSurveyReport(View v) {
        Intent intent = new Intent(context, StaffSurveyReport.class);
        startActivity(intent);
    }

    public void openSeedReservationDetailsReport(View v) {
        Intent intent = new Intent(context, ReportSupervisorSeedReservationDetails.class);
        startActivity(intent);
    }

    public void openSeedMapping(View v) {
        Intent intent = new Intent(context, ReportSupervisorSeedMappingDetails.class);
        startActivity(intent);
    }

    public void openSeedIndentReport(View v) {
        Intent intent = new Intent(context, ReportSupervisorIndentDetails.class);
        startActivity(intent);
    }


    public void openReportVarietyWiseOndateTodatePlanting(View v) {
        Intent intent = new Intent(context, ReportVarietyWiseOndateTodatePlanting.class);
        startActivity(intent);
    }

    public void openReportVarietyWise(View v) {
        Intent intent = new Intent(context, ReportVarietyWisePlanting.class);
        startActivity(intent);
    }

    public void openReportOndateTodateTypeOfPlanting(View v) {
        Intent intent = new Intent(context, ReportOndateTodateTypeOfPlanting.class);
        startActivity(intent);
    }

    public void openReportTypeOfPlanting(View v) {
        Intent intent = new Intent(context, ReportTypeOfPlanting.class);
        startActivity(intent);
    }

    public void openReportVillageWithVarietyReport(View v) {
        Intent intent = new Intent(context, ReportVillageWithVarietyReport.class);
        startActivity(intent);
    }

    public void openReportVillageWithPlantingTypeReport(View v) {
        Intent intent = new Intent(context, ReportVillageWithPlantingTypeReport.class);
        startActivity(intent);
    }

    public void openTodayActivityReportVillageWise(View v) {
        Intent intent = new Intent(context, TodayActivityReportVillageWise.class);
        startActivity(intent);
    }

    public void openagriReport(View view) {

        Intent intent = new Intent(context, AgreeInputReport.class);
        startActivity(intent);
    }
}
