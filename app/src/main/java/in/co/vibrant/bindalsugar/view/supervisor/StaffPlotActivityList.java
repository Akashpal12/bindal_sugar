package in.co.vibrant.bindalsugar.view.supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListFindPlotActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class StaffPlotActivityList extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    List <GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    String TAG="";
    Toolbar toolbar;
    String Lat,Lng;
    Context context;
    List<PlotFarmerShareModel> plotFarmerShareModelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verital_check_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffPlotActivityList.this;
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        //Lat="28.896658";
        //Lng="78.3980435";
        Lat=getIntent().getExtras().getString("lat");
        Lng=getIntent().getExtras().getString("lng");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);

        TextView latitudeTxt=findViewById(R.id.latitude);
        latitudeTxt.setText("Lat:"+Lat);
        TextView longitudeTxt=findViewById(R.id.longitude);
        longitudeTxt.setText("Lng:"+Lng);
        TextView date=findViewById(R.id.date);
        date.setText("Date:"+dateFormat.format(today));

        TextView android_version=findViewById(R.id.android_version);
        android_version.setText("Android:"+ Build.VERSION.RELEASE);
        TextView app_version=findViewById(R.id.app_version);
        app_version.setText("App Version:"+ BuildConfig.VERSION_NAME);
        TextView model=findViewById(R.id.model);
        model.setText("Model:"+ Build.MODEL);

        LinearLayout latlng_layout=findViewById(R.id.latlng_layout);
        latlng_layout.setVisibility(View.VISIBLE);
        userDetailsModels=dbh.getUserDetailsModel();
        setTitle("Plot Activity List");
        toolbar.setTitle("Plot Activity List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }


    private void init()
    {
        try{
            plotFarmerShareModelsList=dbh.getPlotActivityModelByLatLngExats(Double.parseDouble(Lat),Double.parseDouble(Lng),"");
            recyclerView =findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListFindPlotActivityAdapter listGrowerFinderAdapter =new ListFindPlotActivityAdapter(context,plotFarmerShareModelsList);
            recyclerView.setAdapter(listGrowerFinderAdapter);
        }
        catch (Exception e)
        {

        }


    }


}