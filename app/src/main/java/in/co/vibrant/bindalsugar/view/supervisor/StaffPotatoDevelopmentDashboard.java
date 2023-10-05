package in.co.vibrant.bindalsugar.view.supervisor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.view.grower.GrowerCaneDetailsDashboard;
import in.co.vibrant.bindalsugar.view.grower.GrowerPotatoDetailsDashboard;


public class StaffPotatoDevelopmentDashboard extends AppCompatActivity  {

    DBHelper dbh;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_potato_development_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffPotatoDevelopmentDashboard.this;
        setTitle(getString(R.string.MENU_POTATO_DEVELOPMENT));
        toolbar.setTitle(getString(R.string.MENU_POTATO_DEVELOPMENT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openCaneDetails(View v)
    {
        Intent intent=new Intent(context,GrowerCaneDetailsDashboard.class);
        startActivity(intent);
    }

    public void openPotatoDetails(View v)
    {
        Intent intent=new Intent(context,GrowerPotatoDetailsDashboard.class);
        startActivity(intent);
    }

    public void openSurvey(View v)
    {
        Intent intent=new Intent(context,StaffNewPlotEntry.class);
        startActivity(intent);
    }

    public void downloadMasterData(View v)
    {
        Intent intent=new Intent(context,StaffDownloadMasterData.class);
        startActivity(intent);
    }





}
