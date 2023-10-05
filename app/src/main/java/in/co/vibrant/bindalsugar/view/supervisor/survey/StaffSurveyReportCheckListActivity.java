package in.co.vibrant.bindalsugar.view.supervisor.survey;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.BluetoothPrint.BluetoothChat;
import in.co.vibrant.bindalsugar.BluetoothPrint.ESC;
import in.co.vibrant.bindalsugar.BluetoothPrint.Variables;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ServerCheckListAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.PrintPurcheyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;


public class StaffSurveyReportCheckListActivity extends AppCompatActivity {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    List<ControlSurveyModel> controlModelList;
    List<PrintPurcheyModel> printPurcheyModelList;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    List<FarmerShareModel> farmerShareModelList1;
    String villageCode,fromDate,toDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_report_check_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_CHECK_LIST_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button resendData=findViewById(R.id.resendData);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        villageCode=getIntent().getExtras().getString("village_code");
        fromDate=getIntent().getExtras().getString("from_date");
        toDate=getIntent().getExtras().getString("to_date");
        userDetailsModels=new ArrayList<>();
        printPurcheyModelList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        controlModelList=dbh.getControlSurveyModel("");
        farmerShareModelList1=new ArrayList<>();
        List<VillageSurveyModel> villageModelListprint = dbh.getVillageModel(villageCode);
        List<PlotSurveyModel> uploadPlotSurveyModelList=new ArrayList<>();
        uploadPlotSurveyModelList=dbh.getPlotSurveyModelAllVillageCheckList(villageCode,fromDate,toDate);
        if(uploadPlotSurveyModelList.size()>0) {
            for (int i = 0; i < uploadPlotSurveyModelList.size(); i++) {
                List<MasterCaneSurveyModel> masterVarietyModelPrint = dbh.getMasterModelById("1", uploadPlotSurveyModelList.get(i).getVarietyCode());
                List<MasterCaneSurveyModel> masterCaneTypeModelPrint = dbh.getMasterModelById("2", uploadPlotSurveyModelList.get(i).getCaneType());
                List<FarmerShareModel> farmerDetails = dbh.getFarmerShareModel(uploadPlotSurveyModelList.get(i).getColId());
                for (int k = 0; k < farmerDetails.size(); k++) {
                    List<VillageSurveyModel> villageFarmerModelListprint = dbh.getVillageModel(farmerDetails.get(k).getVillageCode());
                    PrintPurcheyModel printPurcheyModel=new PrintPurcheyModel();
                    printPurcheyModel.setPlotSrNo(uploadPlotSurveyModelList.get(i).getPlotSrNo());
                    printPurcheyModel.setVillageCode(villageFarmerModelListprint.get(0).getVillageCode());
                    printPurcheyModel.setVillageName(villageFarmerModelListprint.get(0).getVillageName());
                    printPurcheyModel.setPlotVillageCode(villageModelListprint.get(0).getVillageCode());
                    printPurcheyModel.setPlotVillageName(villageModelListprint.get(0).getVillageName());
                    //printPurcheyModel.setGrowerUniqueCode(farmerDetails.get(0).getGrowerCode());
                    printPurcheyModel.setGrowerCode(farmerDetails.get(k).getGrowerCode());
                    printPurcheyModel.setGrowerName(farmerDetails.get(k).getGrowerName());
                    printPurcheyModel.setGrowerFather(farmerDetails.get(k).getGrowerFatherName());
                    printPurcheyModel.setEastDistacne(uploadPlotSurveyModelList.get(i).getEastNorthDistance());
                    printPurcheyModel.setWestDistance(uploadPlotSurveyModelList.get(i).getWestSouthDistance());
                    printPurcheyModel.setNorthDistance(uploadPlotSurveyModelList.get(i).getWestNorthDistance());
                    printPurcheyModel.setSouthDistance(uploadPlotSurveyModelList.get(i).getEastSouthDistance());
                    printPurcheyModel.setPlotSharePer(farmerDetails.get(k).getShare());
                    printPurcheyModel.setVarietyName(masterVarietyModelPrint.get(0).getName());
                    printPurcheyModel.setCaneTypeName(masterCaneTypeModelPrint.get(0).getName());
                    printPurcheyModel.setCaneArea(uploadPlotSurveyModelList.get(i).getAreaHectare());
                    printPurcheyModelList.add(printPurcheyModel);
                }
            }
            resendData.setText("Print Receipt (Total "+printPurcheyModelList.size()+")");
            recyclerView =findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(StaffSurveyReportCheckListActivity.this, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ServerCheckListAdapter stockSummeryAdapter =new ServerCheckListAdapter(StaffSurveyReportCheckListActivity.this,printPurcheyModelList);
            recyclerView.setAdapter(stockSummeryAdapter);
        }
    }


    public void printAllData(View v)
    {
        int a=printPurcheyModelList.size()*8;
        int b=a/60;
        String time="";
        if(b==0)
        {
            time=a+" Sec.";
        }
        else
        {

            time=b+" Minutes "+(a%60)+" Seconds.";
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportCheckListActivity.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to print receipt it will take approx "+time);
        alertDialog.setPositiveButton("Print",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        printData();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }

//
//
    private void printData()
    {
        try {
            String printData="\n";
            //PrinterApi printerap = new PrinterApi();
            if(printPurcheyModelList.size()>0)
            {
                for(int i=0;i<printPurcheyModelList.size();i++)
                {
                    double shareArea=(Double.parseDouble(printPurcheyModelList.get(i).getCaneArea())* Double.parseDouble(printPurcheyModelList.get(i).getPlotSharePer()))/100;
                    printData += "  SEASON: "+getString(R.string.season)+"  \n";
                    printData += "S.No. :" + (i+1) + "\n";
                    printData += "Survey Vill CD : " + printPurcheyModelList.get(i).getPlotVillageCode() + "\n";
                    printData += "Survey Vill NM : " + printPurcheyModelList.get(i).getPlotVillageName() + "\n";
                    printData += "========================\n";
                    printData += "Plot Sr No.:  " + printPurcheyModelList.get(i).getPlotSrNo() + "\n";
                    printData += "GrowVillCD:" + printPurcheyModelList.get(i).getVillageCode() + "\n";
                    printData += "GrowVillNM:" + printPurcheyModelList.get(i).getVillageName() + "\n";
                    printData += "GrowerCode: "+ printPurcheyModelList.get(i).getGrowerCode() +"\n";
                    printData += "Name : " + printPurcheyModelList.get(i).getGrowerName() + "\n";
                    printData += "Fath : " + printPurcheyModelList.get(i).getGrowerFather() + "\n";
                    printData += "East : " + printPurcheyModelList.get(i).getEastDistacne() + "   West : " + printPurcheyModelList.get(i).getWestDistance() + "\n";
                    printData += "North: " + printPurcheyModelList.get(i).getNorthDistance() + "   South: " + printPurcheyModelList.get(i).getSouthDistance() + "\n";
                    printData += "Plot Share : " + printPurcheyModelList.get(i).getPlotSharePer() + "% : 100%\n";
                    printData += "Variety: " + printPurcheyModelList.get(i).getVarietyName() + "\n";
                    printData += "Cane Type : " + printPurcheyModelList.get(i).getCaneTypeName() + "\n";
                    printData += "Plot Area : "+printPurcheyModelList.get(i).getCaneArea()+"\n";
                    printData += "ShareArea : "+new DecimalFormat("0.000").format(shareArea)+"\n";
                    printData += "========================\n";
                    printData += "\n";
                    printData += "\n";

                }

                Intent intent = new Intent(StaffSurveyReportCheckListActivity.this, BluetoothChat.class);
                intent.putExtra(Variables.PrintString, ESC.RESETPRINTER+ESC.NEWLINE+printData);
                intent.putExtra("printData", printData);
                startActivity(intent);

            }
            else
            {
                AlertPopUp("No plot found on given date");
            }

            //	printerap.setPrintCommand(printapi.font_Double_Height_Width_Off());
        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportCheckListActivity.this);
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

    private void AlertPopUpWithBack(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportCheckListActivity.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

}
