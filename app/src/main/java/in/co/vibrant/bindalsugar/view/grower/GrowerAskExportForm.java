package in.co.vibrant.bindalsugar.view.grower;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ExpertAnserAdapter;
import in.co.vibrant.bindalsugar.model.ExpertHistoryModal;


public class GrowerAskExportForm extends AppCompatActivity  {

    Context context;
    Spinner issue_type;
    List<ExpertHistoryModal> expertHistoryModalList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_ask_an_export);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= GrowerAskExportForm.this;
        setTitle(getString(R.string.MENU_ASK_AN_EXPORT));
        toolbar.setTitle(getString(R.string.MENU_ASK_AN_EXPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        issue_type=findViewById(R.id.issue_type);
        ArrayList<String> data=new ArrayList<String>();
        data.add(getString(R.string.LBL_ISSUE_TYPE));
        data.add(getString(R.string.LBL_CANE_RELATED));
        data.add(getString(R.string.LBL_POTATO_RELATED));
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        issue_type.setAdapter(adaptersupply);


        expertHistoryModalList=new ArrayList<>();
        ExpertHistoryModal expertHistoryModal=new ExpertHistoryModal();
        expertHistoryModal.setDescription("Precaution of redrot diseases ?");
        expertHistoryModal.setIssueType("Symptoms of redrod. Hot water bath 50 c for 2 hours. Can be used to kill the pathogen on seeds and control the redrod.\n\nAlwayes contidor and intergated approch with preventive measure  togethor with biological method.");
        expertHistoryModalList.add(expertHistoryModal);
        RecyclerView recyclerView =findViewById(R.id.recycler_list_history);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        ExpertAnserAdapter stockSummeryAdapter =new ExpertAnserAdapter(context,expertHistoryModalList);
        recyclerView.setAdapter(stockSummeryAdapter);

    }



}
