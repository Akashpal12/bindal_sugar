package in.co.vibrant.bindalsugar.view.supervisor.survey;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

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
import in.co.vibrant.bindalsugar.adapter.StaffSurveyReportPlotGrowerSummeryListAdapter;
import in.co.vibrant.bindalsugar.model.ReportGrowerPlotSummeryModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class StaffSurveyReportFarmerPlotSummery extends AppCompatActivity {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    String villageCode,tvGrower;
    List<ReportGrowerPlotSummeryModel> reportGrowerPlotSummeryModels;
    List<UserDetailsModel> userDetailsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_cane_type_summery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_GROWER_WISE_REPORT));
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
        userDetailsModels=dbh.getUserDetailsModel();
        villageCode=getIntent().getExtras().getString("village_code");
        tvGrower=getIntent().getExtras().getString("tv_grower");
        reportGrowerPlotSummeryModels=new ArrayList<>();
        List<ReportGrowerPlotSummeryModel> reportCaneTypeSummeryModelList1=new ArrayList<>();
        reportGrowerPlotSummeryModels=dbh.getReportGrowerPlotSummeryModel(villageCode,tvGrower);
        ReportGrowerPlotSummeryModel header = new ReportGrowerPlotSummeryModel();
        header.setGrowerCode("Farmer");
        header.setGrowerName("");
        header.setGrowerFather("");
        header.setVillageCode("");
        header.setVillageName("");
        header.setPlotSrNo("P.Sr.");
        header.setArea("G.AREA");
        header.setVarietyCode("VARIETY");
        header.setVarietyName("VARIETY");
        header.setCaneTypeCode("CANE");
        header.setCaneTypeName("CANE");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        reportCaneTypeSummeryModelList1.add(header);
        double total=0,paid=0;
        for(int i=0;i<reportGrowerPlotSummeryModels.size();i++)
        {
            ReportGrowerPlotSummeryModel saleModel=new ReportGrowerPlotSummeryModel();
            saleModel.setVarietyName(reportGrowerPlotSummeryModels.get(i).getVarietyName());
            saleModel.setGrowerCode(reportGrowerPlotSummeryModels.get(i).getGrowerCode());
            saleModel.setGrowerName(reportGrowerPlotSummeryModels.get(i).getGrowerName());
            saleModel.setGrowerFather(reportGrowerPlotSummeryModels.get(i).getGrowerFather());
            saleModel.setVillageCode(reportGrowerPlotSummeryModels.get(i).getVillageCode());
            saleModel.setVillageName(reportGrowerPlotSummeryModels.get(i).getVillageName());
            saleModel.setPlotSrNo(reportGrowerPlotSummeryModels.get(i).getPlotSrNo());
            saleModel.setArea(decimalFormat.format(Double.parseDouble(reportGrowerPlotSummeryModels.get(i).getArea())));
            saleModel.setVarietyCode(reportGrowerPlotSummeryModels.get(i).getVarietyCode());
            saleModel.setVarietyName(reportGrowerPlotSummeryModels.get(i).getVarietyName());
            saleModel.setCaneTypeCode(reportGrowerPlotSummeryModels.get(i).getCaneTypeCode());
            saleModel.setCaneTypeName(reportGrowerPlotSummeryModels.get(i).getCaneTypeName());
            total += Double.parseDouble(reportGrowerPlotSummeryModels.get(i).getArea());
            if(i%2==0)
            {
                saleModel.setColor("#DFDFDF");
                saleModel.setTextColor("#000000");
            }
            else
            {
                saleModel.setColor("#FFFFFF");
                saleModel.setTextColor("#000000");
            }
            reportCaneTypeSummeryModelList1.add(saleModel);
        }
        ReportGrowerPlotSummeryModel footer = new ReportGrowerPlotSummeryModel();
        footer.setGrowerCode("Farmer");
        footer.setGrowerName("");
        footer.setGrowerFather("");
        footer.setVillageCode("");
        footer.setVillageName("");
        footer.setPlotSrNo("TOTAL");
        footer.setArea(""+decimalFormat.format(total));
        footer.setVarietyCode("");
        footer.setVarietyName("");
        footer.setCaneTypeCode("");
        footer.setCaneTypeName("");
        footer.setColor("#000000");
        footer.setTextColor("#FFFFFF");
        reportCaneTypeSummeryModelList1.add(footer);
        recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffSurveyReportFarmerPlotSummery.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        StaffSurveyReportPlotGrowerSummeryListAdapter stockSummeryAdapter =new StaffSurveyReportPlotGrowerSummeryListAdapter(StaffSurveyReportFarmerPlotSummery.this,reportCaneTypeSummeryModelList1);
        recyclerView.setAdapter(stockSummeryAdapter);
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportFarmerPlotSummery.this);
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
                StaffSurveyReportFarmerPlotSummery.this);
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


    public void printData(View v)
    {
        try {
            reportGrowerPlotSummeryModels=dbh.getReportGrowerPlotSummeryModel(villageCode,tvGrower);
            if(reportGrowerPlotSummeryModels.size()>0)
            {
                try {
                DecimalFormat decimalFormat = new DecimalFormat("##.000");
                //PrinterApi printerap = new PrinterApi();
                String printData = "  SEASON: "+getString(R.string.season)+"  \n";
                printData += "  Grower WISE REPORT  \n";
                printData += "======================\n";
                printData += "FARMER: " + reportGrowerPlotSummeryModels.get(0).getGrowerCode() + "\n";
                printData += "NAME  : " + reportGrowerPlotSummeryModels.get(0).getGrowerName() + "\n";
                printData += "FATHER: " + reportGrowerPlotSummeryModels.get(0).getGrowerFather() + "\n";
                printData += "ViCode: " + reportGrowerPlotSummeryModels.get(0).getVillageCode() + "\n";
                printData += "ViName: " + reportGrowerPlotSummeryModels.get(0).getVillageName() + "\n";
                printData += "======================\n";
                printData += "PSR|G.AREA|VARIETY|CANE\n";
                printData += "======================\n";
                double area=0;
                for(int i=0;i<reportGrowerPlotSummeryModels.size();i++)
                {
                    area += Double.parseDouble(reportGrowerPlotSummeryModels.get(i).getArea());
                    printData += reportGrowerPlotSummeryModels.get(i).getPlotSrNo()+"|" + decimalFormat.format(Double.parseDouble(reportGrowerPlotSummeryModels.get(i).getArea())) + "|"+reportGrowerPlotSummeryModels.get(i).getVarietyName()+"|"+reportGrowerPlotSummeryModels.get(i).getCaneTypeName()+"\n";
                }
                printData += "======================\n";
                printData += "Total Area  :     " + decimalFormat.format(area) + "\n";
                printData += "======================\n";
                printData += "\n";
                printData += "\n";
                //Bluetooth_Printer_2inch_Impact printapi = new Bluetooth_Printer_2inch_Impact();
                //	printerap.setPrintCommand(printapi.font_Double_Height_Width_Off());
                int i = 1;
                //while (true) {
                //Thread.sleep(1000);
                //printerap.printData(printData);
                //Thread.sleep(15000);
                    Intent intent = new Intent(StaffSurveyReportFarmerPlotSummery.this, BluetoothChat.class);
                    intent.putExtra(Variables.PrintString, ESC.RESETPRINTER+ESC.NEWLINE+printData);
                    intent.putExtra("printData", printData);
                    startActivity(intent);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }

}
