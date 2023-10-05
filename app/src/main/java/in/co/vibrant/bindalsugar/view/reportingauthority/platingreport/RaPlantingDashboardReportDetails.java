package in.co.vibrant.bindalsugar.view.reportingauthority.platingreport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class RaPlantingDashboardReportDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<UserDetailsModel> userDetailsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_planting_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaPlantingDashboardReportDetails.this;
        setTitle("Planting Report");
        toolbar.setTitle("Planting Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);

    }

    public void openRaVarietyWiseOndateTodatePlanting(View v)
    {
        Intent intent=new Intent(context, RaVarietyWiseOndateTodatePlanting.class);
        startActivity(intent);
    }

    public void openRaOndateTodateTypeOfPlanting(View v)
    {
        Intent intent=new Intent(context, RaOndateTodateTypeOfPlanting.class);
        startActivity(intent);
    }

    public void openRaTypeOfPlanting(View v)
    {
        Intent intent=new Intent(context, RaTypeOfPlanting.class);
        startActivity(intent);
    }

    public void openRaVarietyWisePlanting(View v)
    {
        Intent intent=new Intent(context, RaVarietyWisePlanting.class);
        startActivity(intent);
    }

    public void openRaSupervisorWithVarietyReport(View v)
    {
        Intent intent=new Intent(context, RaSupervisorWithVarietyReport.class);
        startActivity(intent);
    }

    public void openRaSupervisorWithPlantingTypeReport(View v)
    {
        Intent intent=new Intent(context, RaSupervisorWithPlantingTypeReport.class);
        startActivity(intent);
    }



}
