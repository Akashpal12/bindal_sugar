package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;


public class TotalTripActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce=false;
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    /*List<TruckDetails> truckDetailsList;
    List<FactoryModel> factoryModelList;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context= TotalTripActivity.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        setTitle(getString(R.string.MENU_TRUCK_TRIP));
        toolbar.setTitle(getString(R.string.MENU_TRUCK_TRIP));
        /*factoryModelList=new ArrayList<>();
        FactoryModel factoryModel=new FactoryModel();
        for(int i=0;i<10;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            factoryModelList.add(factoryModel);
        }*/
      /*  RecyclerView recyclerView =findViewById(R.id.recycler_list);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        TotalTripListAdapter stockSummeryAdapter =new TotalTripListAdapter(context,factoryModelList);
        recyclerView.setAdapter(stockSummeryAdapter);*/
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
