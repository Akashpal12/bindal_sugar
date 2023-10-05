package in.co.vibrant.bindalsugar.view.supervisor.survey;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.supervisor.PlotFinderMapViewMaster;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCaneDeleteShareListActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCanePendingShareListActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerDetails;


public class StaffSurveyDashboard extends AppCompatActivity  {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;

    AlertDialog alertDialog;
    double lat,lng,accuracy;
    Geocoder geocoder;
    TextView latLngAddress;
    List<ControlSurveyModel> controlSurveyModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_cane_marketing_dashboard);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffSurveyDashboard.this;
        setTitle(getString(R.string.MENU_SURVEY));
        toolbar.setTitle(getString(R.string.MENU_SURVEY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        controlSurveyModelList =dbh.getControlSurveyModel("");
        startLocationUpdates();
    }

    public void changeSurveyVillage(View v)
    {
        changeSurveyVillage();
    }

    private void changeSurveyVillage()
    {
        if(new InternetCheck(context).isOnline())
        {
            try{
                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffSurveyDashboard.this);
                View mView = getLayoutInflater().inflate(R.layout.dialogue_change_village, null);
                dialogbilder.setView(mView);
                final Spinner village=mView.findViewById(R.id.village);
                //warehouseModalList;

                List<VillageSurveyModel> defaultVillageModelList=new ArrayList<>();
                List<VillageSurveyModel> villageModelList=new ArrayList<>();
                villageModelList=dbh.getVillageModelFiltered("","0");
                defaultVillageModelList=dbh.getDefaultSurveyVillage();
                ArrayList<String> data=new ArrayList<String>();
                data.add("Select Village");
                int setSelection=0;
                for(int i=0;i<villageModelList.size();i++)
                {
                    if(defaultVillageModelList.size()>0)
                    {
                        if(defaultVillageModelList.get(0).getVillageCode().equalsIgnoreCase(villageModelList.get(i).getVillageCode()))
                        {
                            setSelection=i+1;
                        }
                    }
                    data.add(villageModelList.get(i).getVillageCode()+" / "+villageModelList.get(i).getVillageName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaffSurveyDashboard.this,
                        R.layout.list_item, data);
                village.setAdapter(adapter);
                village.setSelection(setSelection);
                alertDialog = dialogbilder.create();
                alertDialog.show();
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(true);
                Button btn_ok=mView.findViewById(R.id.btn_ok);
                final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
                final List<VillageSurveyModel> finalDefaultVillageModelList = defaultVillageModelList;
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(village.getSelectedItemPosition()==0)
                            {
                                new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Please select village");
                            }
                            else
                            {
                                alertDialog.dismiss();
                                if(finalDefaultVillageModelList.size()>0)
                                {
                                    List<FarmerShareModel> pendingShare=dbh.getPendingFarmerShareModel(finalDefaultVillageModelList.get(0).getVillageCode());
                                    if(pendingShare.size()==0)
                                    {
                                        dbh.setDefaultVillage(finalVillageModelList1.get(village.getSelectedItemPosition()-1).getVillageCode());
                                        new DownloadMasterData().GetDownloadFarmerPlantingSurvey(context,finalVillageModelList1.get(village.getSelectedItemPosition()-1).getVillageCode());
                                        List<VillageSurveyModel> defaultVillageModelLists=dbh.getDefaultSurveyVillage();
                                        if(defaultVillageModelLists.size()>0)
                                        {
                                            new AlertDialogManager().GreenDialog(StaffSurveyDashboard.this,"Default village successfully updated \nYour new village is "+defaultVillageModelLists.get(0).getVillageName());
                                        }
                                        else
                                        {
                                            new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Sorry default village can not be updated ");
                                        }
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Sorry you cant change village due to pending share in farmer ");
                                    }
                                }
                                else
                                {
                                    dbh.setDefaultVillage(finalVillageModelList1.get(village.getSelectedItemPosition()-1).getVillageCode());
                                    List<VillageSurveyModel> defaultVillageModelLists=dbh.getDefaultSurveyVillage();
                                    if(defaultVillageModelLists.size()>0)
                                    {
                                        new AlertDialogManager().GreenDialog(StaffSurveyDashboard.this,"Default village successfully updated \nYour new village is "+defaultVillageModelLists.get(0).getVillageName());
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Sorry default village can not be updated ");
                                    }
                                }

                            }

                        }
                        catch(Exception e)
                        {
                            new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Error : "+e.toString());
                        }
                    }
                });
            }
            catch (Exception e)
            {

            }
        }
        else{
            new AlertDialogManager().showToast((Activity) context,getString(R.string.oops_connect_your_internet));
            return;
        }
    }

    public void openSurveyReport(View v)
    {
        Intent intent=new Intent(context, StaffSurveyReport.class);
        startActivity(intent);
    }

    protected void startLocationUpdates() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //checkUpdate();
                }
            });
            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity) context,
                                    1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            try {
                                Location location = locationResult.getLastLocation();
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                accuracy = location.getAccuracy();

                                final List<Address> addresses;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                String address = "";
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latLngAddress.setText("\nLattitude : "+lat+"\nLongitude : "+lng+"\nAccuracy : "+new DecimalFormat("0.00").format(accuracy) +"\n\nAddress : "+ addresses.get(0).getAddressLine(0));
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().RedDialog(this,"Security Error:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().RedDialog(this,"Error:"+se.toString());
        }

    }



    public void openNewPlotSurvey(final View v)
    {
        List<VillageSurveyModel> villageSurveyModelList=dbh.getSurveyVillageModel("");
        List<MasterCaneSurveyModel> masterCaneSurveyModels=dbh.getMasterModel("");
        List<ControlSurveyModel> masterControlSurvey=dbh.getControlSurveyModel("");
        List<VillageSurveyModel> defaultVillageModelLists=dbh.getDefaultSurveyVillage();
        if(villageSurveyModelList.size()==0)
        {
            try {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                        context, R.style.AlertDialogGreen);
                //alertDialog.setTitle(context.getString(R.string.app_name));
                alertDialog.setMessage("Village data not found\n\nDo you want to download village data?");
                alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                try {
                                    if(new InternetCheck(context).isOnline())
                                    {
                                        new DownloadMasterData().GetVillageData(context);
                                    }
                                    else{
                                        new AlertDialogManager().showToast((Activity) context,getString(R.string.oops_connect_your_internet));
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if(masterCaneSurveyModels.size()==0)
        {
            try {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                        context, R.style.AlertDialogGreen);
                //alertDialog.setTitle(context.getString(R.string.app_name));
                alertDialog.setMessage("Master data not found\n\nDo you want to download Master data?");
                alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                try {
                                    if(new InternetCheck(context).isOnline())
                                    {
                                        new DownloadMasterData().GetDownloadMaster(context);
                                    }
                                    else{
                                        new AlertDialogManager().showToast((Activity) context,getString(R.string.oops_connect_your_internet));
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if(masterControlSurvey.size()==0)
        {
            try {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                        context, R.style.AlertDialogGreen);
                //alertDialog.setTitle(context.getString(R.string.app_name));
                alertDialog.setMessage("Control data not found\n\nDo you want to download Control data?");
                alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                try {
                                    if(new InternetCheck(context).isOnline())
                                    {
                                        new DownloadMasterData().GetControlData(context);
                                    }
                                    else{
                                        new AlertDialogManager().showToast((Activity) context,getString(R.string.oops_connect_your_internet));
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
            return;
        }

        if(defaultVillageModelLists.size()==0)
        {
            try {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                        context, R.style.AlertDialogGreen);
                //alertDialog.setTitle(context.getString(R.string.app_name));
                alertDialog.setMessage("Default village not set\n\nDo you want to set default village for survey?");
                alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                try {
                                    changeSurveyVillage();
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
            return;
        }

        final List<FarmerShareModel> farmerShareModelList=dbh.getPendingFarmerShareZeroModel("");
        if(farmerShareModelList.size()>0)
        {
            final List<PlotSurveyModel> plotSurveyModelList=dbh.getPlotSurveyModelById(farmerShareModelList.get(0).getSurveyId());
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                    StaffSurveyDashboard.this);
            alertDialog.setTitle("Error : ");
            alertDialog.setMessage("You have incomplete survey data if you want to remove then press delete button otherwise complete this data" +
                    "\n\nPlot Village : "+plotSurveyModelList.get(0).getVillageCode()+
                    "  Plot Serial : "+plotSurveyModelList.get(0).getPlotSrNo()+
                    "\nVariety : "+plotSurveyModelList.get(0).getVarietyCode()+"/"+dbh.getMasterModelById("1",plotSurveyModelList.get(0).getVarietyCode()).get(0).getName()+
                    " Cane Type : "+plotSurveyModelList.get(0).getCaneType()+"/"+dbh.getMasterModelById("2",plotSurveyModelList.get(0).getCaneType()).get(0).getName()+
                    "\nSurvey Date :"+plotSurveyModelList.get(0).getInsertDate()
            );
            alertDialog.setPositiveButton("Complete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent=new Intent(StaffSurveyDashboard.this, AddGrowerShareCane.class);
                            intent.putExtra("survey_id",farmerShareModelList.get(0).getSurveyId());
                            startActivity(intent);
                        }
                    });
            alertDialog.setNegativeButton("Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dbh.deletePlotSurveyModelById(plotSurveyModelList.get(0).getColId());
                            Intent intent=new Intent(StaffSurveyDashboard.this, NewPlotEntryCane.class);
                            //startActivity(intent);
                            openNewPlotSurvey(v);
                        }
                    });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        else
        {
            if(Integer.parseInt(controlSurveyModelList.get(0).getIshunPerShare())==1)
            {
                final List<FarmerShareModel> farmerShareModelListNonZero=dbh.getPendingFarmerShareModel("");
                if(farmerShareModelListNonZero.size()>0)
                {
                    final List<PlotSurveyModel> plotSurveyModelList=dbh.getPlotSurveyModelById(farmerShareModelListNonZero.get(0).getSurveyId());
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                            StaffSurveyDashboard.this);
                    alertDialog.setTitle("Error : ");
                    alertDialog.setMessage("You have incomplete survey data please complete plot survey data 100%" +
                            "\n\nPlot Village : "+plotSurveyModelList.get(0).getVillageCode()+
                            " Plot Serial : "+plotSurveyModelList.get(0).getPlotSrNo()+
                            "\nVariety : "+plotSurveyModelList.get(0).getVarietyCode()+"/"+dbh.getMasterModelById("1",plotSurveyModelList.get(0).getVarietyCode()).get(0).getName()+
                            "\nCane Type : "+plotSurveyModelList.get(0).getCaneType()+"/"+dbh.getMasterModelById("2",plotSurveyModelList.get(0).getCaneType()).get(0).getName()+
                            "\nPending Share : "+(100-Integer.parseInt(farmerShareModelListNonZero.get(0).getShare()))+" %"+
                            "\nSurvey Date :"+plotSurveyModelList.get(0).getInsertDate()
                    );
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent=new Intent(StaffSurveyDashboard.this, AddGrowerShareCane.class);
                                    intent.putExtra("survey_id",plotSurveyModelList.get(0).getColId());
                                    startActivity(intent);
                                }
                            });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                else
                {
                    //Intent intent=new Intent(StaffSurveyDashboard.this, NewPlotEntryCane.class);
                    Intent intent=new Intent(StaffSurveyDashboard.this, StaffSurveyNearByPlotList.class);
                    startActivity(intent);
                }
            }else
            {
                //Intent intent=new Intent(StaffSurveyDashboard.this, NewPlotEntryCane.class);
                Intent intent=new Intent(StaffSurveyDashboard.this, StaffSurveyNearByPlotList.class);
                startActivity(intent);
            }
        }
        //Intent intent=new Intent(context,NewPlotEntryCane.class);
        //startActivity(intent);
    }

    public void openPlotFinderPathFinder(View v)
    {
        Intent intent=new Intent(context, StaffGrowerDetails.class);
        startActivity(intent);
    }

    public void openStaffCanePendingShareListActivity(View v)
    {
        Intent intent=new Intent(context, StaffCanePendingShareListActivity.class);
        startActivity(intent);
    }

    public void openDeleteSharePlot(View v)
    {
        android.app.AlertDialog.Builder dialogbilder = new android.app.AlertDialog.Builder(StaffSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
        dialogbilder.setView(mView);
        final EditText password=mView.findViewById(R.id.password);

        final android.app.AlertDialog dialogPopup = dialogbilder.create();
        dialogPopup.show();
        dialogPopup.setCancelable(true);
        dialogPopup.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(password.getText().toString().length()==0)
                    {
                        new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Please enter password");
                    }
                    else
                    {
                        if(password.getText().toString().equalsIgnoreCase("dplot"))
                        {
                            Intent intent=new Intent(context, StaffCaneDeleteShareListActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                        else
                        {
                            new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Please enter valid password");
                        }

                    }
                }
                catch(Exception e)
                {
                    new AlertDialogManager().RedDialog(StaffSurveyDashboard.this,"Error:"+e.toString());
                }
                dialogPopup.dismiss();
            }
        });
        //StaffCaneDeleteShareListActivity
    }

    public void openGrowerEnquiry(View v)
    {
        try
        {
            /*AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
            dialogbilder.setView(mView);
            alertDialog = dialogbilder.create();
            ArrayList<String> divData=new ArrayList<String>();
            latLngAddress=mView.findViewById(R.id.err_msg);
            latLngAddress.setText("\nLattitude : --\nLongitude : --\nAccuracy : --\n\nAddress : --");
            Button b1 = (Button) mView.findViewById(R.id.btn_ok);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, StaffGrowerFinder.class);
                            //intent.putExtra("lat", "" + lat);
                            intent.putExtra("lat", "" + 28.86489);
                            intent.putExtra("lng", "" + 78.19481);
                           // intent.putExtra("lng", "" + lng);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"ErrorL:"+e.toString());
                    }
                }
            });*/
            /*Button recheck = (Button) mView.findViewById(R.id.recheck);
            recheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"ErrorL:"+e.toString());
                    }
                }
            });
            alertDialog.show();*/
            Intent intent = new Intent(context, PlotFinderMapViewGrowerFinder.class);
            startActivity(intent);

        }

        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void openVeritalCheck(View v)
    {
        try
        {
            Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
            //intent.putExtra("lat", "" + lat);
            //intent.putExtra("lng", "" + lng);
            startActivity(intent);
        }
        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


}
