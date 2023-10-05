package in.co.vibrant.bindalsugar.view.supervisor;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Timer;
import java.util.TimerTask;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ServerPendingShareListAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;


public class StaffCaneReportServerDataListActivity extends AppCompatActivity {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    //List<FactoryModel> factoryModelList;
    List<ControlSurveyModel> controlModelList;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    List<FarmerShareModel> farmerShareModelList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_share_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_CANE_DATA_LIST));
        toolbar.setTitle(getString(R.string.MENU_CANE_DATA_LIST));
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
        //factoryModelList=new ArrayList<>();
        //factoryModelList=dbh.getFactoryModel("");
        controlModelList=dbh.getControlSurveyModel("");
        farmerShareModelList1=new ArrayList<>();
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
        if(farmerShareModelList1.size()>0)
            farmerShareModelList1.clear();
        List<FarmerShareModel> farmerShareModelList=dbh.getFarmerSharePendingFailedData();
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        FarmerShareModel header = new FarmerShareModel();
        header.setVillageCode("Village");
        header.setGrowerCode("Grower");
        header.setCurDate("Date");
        header.setSurveyId("Plot Sr No.");
        header.setShare("Share %");
        header.setServerStatus("Server");
        header.setServerStatusRemark("Remark");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        farmerShareModelList1.add(header);
        double total=0,paid=0;
        for(int i=0;i<farmerShareModelList.size();i++)
        {
            FarmerShareModel saleModel=new FarmerShareModel();
            saleModel.setColId(farmerShareModelList.get(i).getColId());
            saleModel.setVillageCode(farmerShareModelList.get(i).getVillageCode());
            saleModel.setGrowerCode(farmerShareModelList.get(i).getGrowerCode());
            saleModel.setSurveyId(farmerShareModelList.get(i).getSurveyId());
            saleModel.setSrNo(farmerShareModelList.get(i).getSrNo());
            saleModel.setCurDate(farmerShareModelList.get(i).getCurDate());
            saleModel.setShare(farmerShareModelList.get(i).getShare());
            saleModel.setServerStatus(farmerShareModelList.get(i).getServerStatus());
            saleModel.setServerStatusRemark(farmerShareModelList.get(i).getServerStatusRemark());
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
            farmerShareModelList1.add(saleModel);
        }
        /*SaleModel footer = new SaleModel();
        footer.setId("Total");
        footer.setName("");
        footer.setTotalAmount(""+decimalFormat.format(total));
        footer.setPayAmount(""+decimalFormat.format(paid));
        footer.setColor("#000000");
        footer.setTextColor("#FFFFFF");
        saleModelList.add(footer);*/
        recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffCaneReportServerDataListActivity.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        ServerPendingShareListAdapter stockSummeryAdapter =new ServerPendingShareListAdapter(StaffCaneReportServerDataListActivity.this,farmerShareModelList1);
        recyclerView.setAdapter(stockSummeryAdapter);
    }

    public void resendData(View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffCaneReportServerDataListActivity.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to resend all data on server");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dbh.updateAllServerFarmerShareModel("No","Retry to send all data on server");
                        AlertPopUp("Data is successfully queued to send on server.");
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

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffCaneReportServerDataListActivity.this);
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
                StaffCaneReportServerDataListActivity.this);
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
