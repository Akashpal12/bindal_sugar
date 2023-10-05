package in.co.vibrant.bindalsugar.view.supervisor;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListPendingPlantingAdapter;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class StaffServerPendingPlantingReport extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    AlertDialog dialog;
    List<PlantingModel> plantingModelList;
    RelativeLayout RecLayout;
    //FloatingActionButton fab;
    Button btn;
    List<UserDetailsModel> loginUserDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_pending_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Server Pending Planting");
        toolbar.setTitle("Server Pending Planting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        loginUserDetailsList=dbh.getUserDetailsModel();
        //LogoLayout=findViewById(R.id.home_layout);
        RecLayout=findViewById(R.id.rec_layout);
        plantingModelList=new ArrayList<>();


        //fab = findViewById(R.id.fab);
        btn = findViewById(R.id.btn_ok);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowMaterialList();
            }
        });*/
        setData();
        Button resendData=findViewById(R.id.resendData);
        resendData.setVisibility(View.VISIBLE);
        Timer bookingStatusTimer = new Timer();
        bookingStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });
            }
        }, 0, 20000);
    }

    private void setData()
    {
        plantingModelList=dbh.getPlantingModel("No","","","","");
        RecyclerView recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffServerPendingPlantingReport.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        ListPendingPlantingAdapter listPloughingAdapter =new ListPendingPlantingAdapter(StaffServerPendingPlantingReport.this,plantingModelList);
        recyclerView.setAdapter(listPloughingAdapter);
    }


    public void resendAllDataToServer(View v)
    {
        dbh.resetServerStatusPlanting();
        setData();
        AlertPopUp("Data successfully process on server");
    }


    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                StaffServerPendingPlantingReport.this);
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
}
