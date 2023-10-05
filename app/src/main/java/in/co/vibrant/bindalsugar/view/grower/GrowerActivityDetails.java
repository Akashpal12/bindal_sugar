package in.co.vibrant.bindalsugar.view.grower;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.PotatoActivityTrackerAdapter;
import in.co.vibrant.bindalsugar.model.PotatoTodayActivityModal;


public class GrowerActivityDetails extends AppCompatActivity  {

    SQLiteDatabase db;
    Context context;
    RelativeLayout caneLayout,potatoLayout;
    List<PotatoTodayActivityModal> potatoTodayActivityModalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_activities_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= GrowerActivityDetails.this;
        setTitle(getString(R.string.MENU_ACTIVITIES));
        toolbar.setTitle(getString(R.string.MENU_ACTIVITIES));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        caneLayout=findViewById(R.id.line3);
        potatoLayout=findViewById(R.id.line2);
        potatoTodayActivityModalList=new ArrayList<>();
        PotatoTodayActivityModal factoryModel=new PotatoTodayActivityModal();
        for(int i=0;i<15;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            potatoTodayActivityModalList.add(factoryModel);
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        PotatoActivityTrackerAdapter stockSummeryAdapter =new PotatoActivityTrackerAdapter(context,potatoTodayActivityModalList);
        recyclerView.setAdapter(stockSummeryAdapter);
        caneLayout.setVisibility(View.VISIBLE);
        potatoLayout.setVisibility(View.GONE);
    }


    public void openCaneActivities(View v)
    {
        potatoTodayActivityModalList=new ArrayList<>();
        PotatoTodayActivityModal factoryModel=new PotatoTodayActivityModal();
        for(int i=0;i<15;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            potatoTodayActivityModalList.add(factoryModel);
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        PotatoActivityTrackerAdapter stockSummeryAdapter =new PotatoActivityTrackerAdapter(context,potatoTodayActivityModalList);
        recyclerView.setAdapter(stockSummeryAdapter);
        caneLayout.setVisibility(View.VISIBLE);
        potatoLayout.setVisibility(View.GONE);
    }

    public void openPotatoActivities(View v)
    {
        potatoTodayActivityModalList=new ArrayList<>();
        PotatoTodayActivityModal factoryModel=new PotatoTodayActivityModal();
        for(int i=0;i<15;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            potatoTodayActivityModalList.add(factoryModel);
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_list);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        PotatoActivityTrackerAdapter stockSummeryAdapter =new PotatoActivityTrackerAdapter(context,potatoTodayActivityModalList);
        recyclerView.setAdapter(stockSummeryAdapter);
        caneLayout.setVisibility(View.GONE);
        potatoLayout.setVisibility(View.VISIBLE);
    }



}
