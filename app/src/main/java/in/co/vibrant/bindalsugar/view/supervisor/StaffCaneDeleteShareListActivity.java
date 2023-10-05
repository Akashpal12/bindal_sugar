package in.co.vibrant.bindalsugar.view.supervisor;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.DeleteShareListAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class StaffCaneDeleteShareListActivity extends AppCompatActivity {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModelList;
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
        setTitle(getString(R.string.MENU_DELETE_SHARE_LIST));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button resendData = findViewById(R.id.resendData);
        resendData.setVisibility(View.GONE);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModelList = new ArrayList<>();
        userDetailsModelList = dbh.getUserDetailsModel();
        controlModelList = dbh.getControlSurveyModel("");
        farmerShareModelList1 = new ArrayList<>();
        List<FarmerShareModel> farmerShareModelList = dbh.getDeleteFarmerShareModel();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        FarmerShareModel header = new FarmerShareModel();
        header.setVillageCode("Village");
        header.setGrowerCode("Grower");
        header.setSurveyId("Ghashti");
        header.setGrowerName("Grower Name");
        header.setShare("Share %");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        farmerShareModelList1.add(header);
        double total = 0, paid = 0;
        for (int i = 0; i < farmerShareModelList.size(); i++) {
            FarmerShareModel saleModel = new FarmerShareModel();
            saleModel.setColId(farmerShareModelList.get(i).getColId());
            saleModel.setSurveyId(farmerShareModelList.get(i).getSurveyId());
            saleModel.setVillageCode(farmerShareModelList.get(i).getVillageCode());
            saleModel.setGrowerCode(farmerShareModelList.get(i).getGrowerCode());
            saleModel.setSurveyId(farmerShareModelList.get(i).getSurveyId());
            saleModel.setGrowerName(farmerShareModelList.get(i).getGrowerName());
            saleModel.setShare(farmerShareModelList.get(i).getShare());
            if (i % 2 == 0) {
                saleModel.setColor("#DFDFDF");
                saleModel.setTextColor("#000000");
            } else {
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
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffCaneDeleteShareListActivity.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        DeleteShareListAdapter stockSummeryAdapter = new DeleteShareListAdapter(StaffCaneDeleteShareListActivity.this, farmerShareModelList1);
        recyclerView.setAdapter(stockSummeryAdapter);
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffCaneDeleteShareListActivity.this);
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
                StaffCaneDeleteShareListActivity.this);
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
