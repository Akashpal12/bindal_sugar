package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.supervisor.survey.StaffSurveyDashboard;

public class StaffDevelopmentDashboard extends AppCompatActivity {
    SQLiteDatabase db;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModelList;
    Context context;

    private void init() {

        try {

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU));
            toolbar.setTitle(getString(R.string.MENU));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = StaffDevelopmentDashboard.this;
            dbh = new DBHelper(context);
            userDetailsModelList = dbh.getUserDetailsModel();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StaffMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.development_dashboard);

        init();

    }

    public void openLiveLocationData(View v) {
        Intent intent=new Intent(context, LiveLocation.class);
        startActivity(intent);
    }

    public void openCaneDevelopment(View v) {
        userDetailsModelList = dbh.getUserDetailsModel();
        Log.d("MyData",""+userDetailsModelList.get(0).getDivision()+" / "+userDetailsModelList.get(0).getCode());
        if (userDetailsModelList.get(0).getIsUpdateMaster() == 1) {

            Intent intent = new Intent(context, StaffCaneDevelopmentDashboard.class);
            startActivity(intent);
        } else {

            try {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context, R.style.AlertDialogGreen);
                //alertDialog.setTitle(context.getString(R.string.app_name));
                alertDialog.setMessage("Master data not found\n\nDo you want to download Master data?");
                alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                try {
                                    if (new InternetCheck(context).isOnline()) {
                                        new DownloadMasterData().DownloadMaster(context);
                                    } else {
                                        new AlertDialogManager().showToast((Activity) context, getString(R.string.oops_connect_your_internet));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.setNegativeButton(context.getString(R.string.BTN_LATER),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void openPotatoDevelopment(View v) {
        if (userDetailsModelList.get(0).getIsUpdateMaster() == 1) {
            Intent intent = new Intent(context, StaffPotatoDevelopmentDashboard.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, StaffDownloadMasterData.class);
            new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
        }
    }

    public void downloadActivitySchedule(View v) {
        if (userDetailsModelList.get(0).getIsUpdateMaster() == 1) {
            Intent intent = new Intent(context, StaffActivityDetails.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, StaffDownloadMasterData.class);
            new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
        }
    }

    public void downloadMasterData(View v) {
        Intent intent = new Intent(context, StaffDownloadMasterData.class);
        startActivity(intent);
    }

    public void openaddmem(View v) {
        Intent intent = new Intent(context, StaffAddFamilyMember.class);
        startActivity(intent);
    }

    public void opengrowerDocument(View v) {
        Intent intent = new Intent(context, StaffAddGrowerFormActivity.class);
        startActivity(intent);
    }

    public void downloadStaffCaneMarketingDashboard(View v) {
        Intent intent = new Intent(context, StaffSurveyDashboard.class);
        startActivity(intent);
    }

    public void openReport(View v) {
        Intent intent = new Intent(context, StaffReportDashboard.class);
        startActivity(intent);
    }

    public void openSupervisorAttendance(View v) {
        Intent intent = new Intent(context, SupervisorAttendance.class);
        startActivity(intent);
    }

    public void openvehiclecollection(View v) {
        Intent intent = new Intent(context, StaffVehicleCollection.class);
        startActivity(intent);
    }

    public void SupervisorPerReport(View v) {
        Intent intent = new Intent(context, SuperVisorPerformanceReport.class);
        startActivity(intent);
    }

    public void PlotActivitySummaryreport(View v) {
        Intent intent = new Intent(context, PlotActivitySummaryReport.class);
        startActivity(intent);
    }

    public void SuperviserAttendence(View v) {
        Intent intent = new Intent(context, SuperviserAttancanceActivity.class);
        startActivity(intent);
    }

    public void PocPlotReport(View v) {
        Intent intent = new Intent(context, POCPlotsActivity.class);
        startActivity(intent);
    }
   public void GisLeadsReport(View v) {
        Intent intent = new Intent(context, GetGisLeadsSummaryReport.class);
        startActivity(intent);
    }

    public void CrmSummaryReport(View v) {
        Intent intent = new Intent(context, CRMSummaryReport.class);
        startActivity(intent);
    }

}