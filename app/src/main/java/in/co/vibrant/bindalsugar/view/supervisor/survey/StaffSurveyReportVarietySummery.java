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
import in.co.vibrant.bindalsugar.adapter.ReportCaneTypeSummeryListAdapter;
import in.co.vibrant.bindalsugar.model.ReportCaneTypeSummeryModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class StaffSurveyReportVarietySummery extends AppCompatActivity {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    String villageCode,fromDate,toDate;
    List<ReportCaneTypeSummeryModel> reportCaneTypeSummeryModelList;
    List<UserDetailsModel> userDetailsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_cane_type_summery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_VARIETY_SUMMARY_REPORT));
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
        fromDate=getIntent().getExtras().getString("from_date");
        toDate=getIntent().getExtras().getString("to_date");
        reportCaneTypeSummeryModelList=new ArrayList<>();
        List<ReportCaneTypeSummeryModel> reportCaneTypeSummeryModelList1=new ArrayList<>();
        reportCaneTypeSummeryModelList=dbh.getReportVarietySummery(villageCode,fromDate,toDate);
        ReportCaneTypeSummeryModel header = new ReportCaneTypeSummeryModel();
        header.setTotalPlot("Plot");
        header.setVarietyName("Cane Type");
        header.setArea("Area");
        header.setFromDate("");
        header.setToDate("");
        header.setVillageName("");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        DecimalFormat intFormat = new DecimalFormat("0");
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        reportCaneTypeSummeryModelList1.add(header);
        double total=0,plot=0;
        for(int i=0;i<reportCaneTypeSummeryModelList.size();i++)
        {
            ReportCaneTypeSummeryModel saleModel=new ReportCaneTypeSummeryModel();
            saleModel.setTotalPlot(reportCaneTypeSummeryModelList.get(i).getTotalPlot());
            saleModel.setVarietyName(reportCaneTypeSummeryModelList.get(i).getVarietyName());
            saleModel.setArea(decimalFormat.format(Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getArea())));
            saleModel.setFromDate(reportCaneTypeSummeryModelList.get(i).getFromDate());
            saleModel.setToDate(reportCaneTypeSummeryModelList.get(i).getToDate());
            saleModel.setVillageName(reportCaneTypeSummeryModelList.get(i).getVillageName());
            saleModel.setVarietyName(reportCaneTypeSummeryModelList.get(i).getVarietyName());
            total += Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getArea());
            plot += Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getTotalPlot());
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
        ReportCaneTypeSummeryModel footer = new ReportCaneTypeSummeryModel();
        footer.setVarietyName("Total");
        footer.setTotalPlot(""+intFormat.format(plot));
        footer.setArea(""+decimalFormat.format(total));
        footer.setColor("#000000");
        footer.setTextColor("#FFFFFF");
        reportCaneTypeSummeryModelList1.add(footer);
        recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffSurveyReportVarietySummery.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        ReportCaneTypeSummeryListAdapter stockSummeryAdapter =new ReportCaneTypeSummeryListAdapter(StaffSurveyReportVarietySummery.this,reportCaneTypeSummeryModelList1);
        recyclerView.setAdapter(stockSummeryAdapter);
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffSurveyReportVarietySummery.this);
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
                StaffSurveyReportVarietySummery.this);
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
            reportCaneTypeSummeryModelList=dbh.getReportVarietySummery(villageCode,fromDate,toDate);
            DecimalFormat intFormat = new DecimalFormat("##");
            DecimalFormat decimalFormat = new DecimalFormat("##.0000");
            //PrinterApi printerap = new PrinterApi();
            String printData = "  SEASON: "+getString(R.string.season)+"  \n";
            printData += "  VARIETY SUMMERY  \n";
            printData += "======================\n";
            printData += "From Date: " + reportCaneTypeSummeryModelList.get(0).getFromDate() + "\n";
            printData += "To Date  : " + reportCaneTypeSummeryModelList.get(0).getToDate() + "\n";
            printData += "Village  : " + reportCaneTypeSummeryModelList.get(0).getVillageName() + "\n";
            printData += "======================\n";
            printData += "Variety  |  Plot | Area\n";
            printData += "======================\n";
            double area=0,plot=0;
            for(int i=0;i<reportCaneTypeSummeryModelList.size();i++)
            {
                area += Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getArea());
                plot += Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getTotalPlot());
                printData += reportCaneTypeSummeryModelList.get(i).getVarietyName()+  "  | " + intFormat.format(Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getTotalPlot())) +"    | " + decimalFormat.format(Double.parseDouble(reportCaneTypeSummeryModelList.get(i).getArea())) + "\n";
            }
            printData += "======================\n";
            printData += "Total |  "+ intFormat.format(plot) +"   |  " + decimalFormat.format(area) + "\n";
            printData += "======================\n";
            printData += "\n";
            printData += "\n";
            //Bluetooth_Printer_2inch_Impact printapi = new Bluetooth_Printer_2inch_Impact();
            //	printerap.setPrintCommand(printapi.font_Double_Height_Width_Off());
            int i = 1;
            //while (true) {\
            Intent intent = new Intent(StaffSurveyReportVarietySummery.this, BluetoothChat.class);
            intent.putExtra(Variables.PrintString, ESC.RESETPRINTER+ESC.NEWLINE+printData);
            intent.putExtra("printData", printData);
            startActivity(intent);

        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }

}
