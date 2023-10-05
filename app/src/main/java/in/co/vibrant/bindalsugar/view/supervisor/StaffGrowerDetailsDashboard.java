package in.co.vibrant.bindalsugar.view.supervisor;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class StaffGrowerDetailsDashboard extends AppCompatActivity  {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    Spinner village,grower;
    List<VillageModal> villageModelList;
    List<GrowerModel> growerModelList;
    List<GrowerModel> growerSurveyModelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_details_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffGrowerDetailsDashboard.this;
        setTitle(getString(R.string.MENU_GROWER_DETAILS));
        toolbar.setTitle(getString(R.string.MENU_GROWER_DETAILS));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        villageModelList=new ArrayList<>();
        growerModelList=new ArrayList<>();
        growerSurveyModelsList=new ArrayList<>();
        village=findViewById(R.id.village);
        grower=findViewById(R.id.grower);
        dbh=new DBHelper(context);


        /*ArrayList<String> data=new ArrayList<String>();
        data.add("Select Village");
        data.add("Dhanoura");
        data.add("Chinoura");
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setSelection(1);

        ArrayList<String> growerdata=new ArrayList<String>();
        growerdata.add("All Grower");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);*/
        getVillage();

        ArrayList<String> growerdata=new ArrayList<String>();
        growerdata.add("Select Grower");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);
        growerModelList=new ArrayList<>();
    }

    public void getVillage()
    {
        villageModelList=dbh.getVillageModal("");
        ArrayList<String> data=new ArrayList<String>();
        data.add("Select Village");
        for(int i=0;i<villageModelList.size();i++)
        {
            data.add(villageModelList.get(i).getCode()+" - "+villageModelList.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    //getGrower("");
                }
                else
                {
                    getGrower(villageModelList.get(village.getSelectedItemPosition()-1).getCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getGrower(String villCode)
    {
        growerModelList=dbh.getGrowerModel(villCode,"");
        if(growerSurveyModelsList.size()>0)
            growerSurveyModelsList.clear();
        GrowerModel growerSurveyModel1 =new GrowerModel();
        growerSurveyModel1.setVillageCode("");
        growerSurveyModel1.setGrowerCode("");
        growerSurveyModel1.setGrowerName("All Grower");
        //growerSurveyModelsList.add(growerSurveyModel1);
        ArrayList<String> data=new ArrayList<String>();
        data.add("Select Grower");
        for(int i=0;i<growerModelList.size();i++)
        {
            GrowerModel growerSurveyModel =new GrowerModel();
            growerSurveyModel.setVillageCode(growerModelList.get(i).getVillageCode());
            growerSurveyModel.setGrowerCode(growerModelList.get(i).getGrowerCode());
            growerSurveyModel.setGrowerName(growerModelList.get(i).getGrowerName());
            data.add(growerModelList.get(i).getGrowerCode()+" - "+growerModelList.get(i).getGrowerName());
            growerSurveyModelsList.add(growerSurveyModel);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        grower.setAdapter(adaptersupply);

    }

    public void openGrowerDetails(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffGrowerEnquiry.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }

    public void openAccountDetails(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffAccountDetails.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }

    public void openLoanDetail(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffGrowerLoanActivity.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }

    public void openPurcheyDetail(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffGrowerPurchiActivity.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }

    public void openPaymentDetail(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffGrowerPaymentActivity.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }

    public void openWeightmentDetail(View v)
    {
        if(village.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select village");
        }
        else if(grower.getSelectedItemPosition()==0)
        {
            new AlertDialogManager().RedDialog(context,"Please select grower");
        }
        else
        {
            Intent intent=new Intent(context,StaffGrowerWeightmentActivity.class);
            intent.putExtra("village",villageModelList.get(village.getSelectedItemPosition()-1).getCode());
            intent.putExtra("grower",growerSurveyModelsList.get(grower.getSelectedItemPosition()-1).getGrowerCode());
            startActivity(intent);
        }

    }






}
