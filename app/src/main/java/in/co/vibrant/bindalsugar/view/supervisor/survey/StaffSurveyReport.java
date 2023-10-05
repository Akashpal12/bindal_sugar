package in.co.vibrant.bindalsugar.view.supervisor.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.FarmerAutoSearchAdapter;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GenerateLogFile;
import in.co.vibrant.bindalsugar.view.supervisor.PlotFinderMapViewMaster;
import in.co.vibrant.bindalsugar.view.supervisor.StaffAllSurveyDataListActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCaneReportServerDataListActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCropObservationReport;


public class StaffSurveyReport extends AppCompatActivity {

    String TAG="MasterData";

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    List<FarmerModel> farmerModelList;
    FarmerAutoSearchAdapter farmerAutoSearchAdapter;

    androidx.appcompat.app.AlertDialog dialog;
    TextView download_control_data,download_village_data,download_master_data,download_farmer_data;
    Double Lat,Lng;
    List<Address> addressList;
    AlertDialog alertDialog;
    Context context;

    private int mYear, mMonth, mDay, mHour, mMinute;

    double lat,lng,accuracy;
    Geocoder geocoder;
    TextView latLngAddress;

    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_report_dashboard);
        context=this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_SURVEY_REPORT));
        //toolbar.setTitle("Import Master Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startLocationUpdates();
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

    public void openServerDataStatus(View v)
    {
        Intent intent=new Intent(context, StaffCaneReportServerDataListActivity.class);
        startActivity(intent);
    }

    public void openAllDataStatus(View v)
    {
        Intent intent=new Intent(context, StaffAllSurveyDataListActivity.class);
        startActivity(intent);
    }

    public void openExportSurveyData(View v)
    {
        new GenerateLogFile(context).generateSurveyData();
    }

    public void openSurveyImportData(View v)
    {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
            dialogbilder.setView(mView);
            final EditText password=mView.findViewById(R.id.password);
            alertDialog = dialogbilder.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            Button btn_ok=mView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(password.getText().toString().length()==0)
                        {
                            new AlertDialogManager().RedDialog(context,"Please enter password");
                        }
                        else
                        {
                            if(password.getText().toString().equalsIgnoreCase("vispl"))
                            {
                                new GenerateLogFile(context).importSurveyData();
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(context,"Please enter valid password");
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                    }
                    alertDialog.dismiss();
                }
            });
        }
        catch(SecurityException e)
        {

        }
    }

    public void openStaffSurveyReportMap(View v)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffSurveyReport.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_report_plot_wise_grower, null);
        dialogbilder.setView(mView);
        final Spinner village=mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        final AutoCompleteTextView tv_grower=mView.findViewById(R.id.tv_grower);

        List<VillageSurveyModel> villageModelList=new ArrayList<>();
        villageModelList=dbh.getVillageModel("");
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<villageModelList.size();i++)
        {
            data.add(villageModelList.get(i).getVillageCode()+"/"+villageModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaffSurveyReport.this,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        farmerModelList=new ArrayList<>();
        final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                farmerModelList=dbh.getFarmerWithVillageModel(""+finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode(),"");
                farmerAutoSearchAdapter = new FarmerAutoSearchAdapter(StaffSurveyReport.this, R.layout.all_list_row_item_search, farmerModelList);
                tv_grower.setThreshold(1);
                tv_grower.setAdapter(farmerAutoSearchAdapter);
                tv_grower.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        farmerAutoSearchAdapter.getFilter().filter(s);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_grower.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FarmerModel purcheyDataModel = (FarmerModel) parent.getItemAtPosition(position);
                        tv_grower.setText(purcheyDataModel.getFarmerCode());
                    }
                });
        alertDialog = dialogbilder.create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                try {
                    Intent intent=new Intent(StaffSurveyReport.this, SuperViserMapViewReport.class);
                    intent.putExtra("village_code", finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode());
                    intent.putExtra("tv_grower",tv_grower.getText().toString());
                    startActivity(intent);
                }
                catch(Exception e)
                {

                }
            }
        });
    }


    public void caneTypeSummery(View v)
    {
        try {
            androidx.appcompat.app.AlertDialog.Builder dialogbilder = new androidx.appcompat.app.AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_report_cane_type_summery, null);
            dialogbilder.setView(mView);
            final Spinner village = mView.findViewById(R.id.tv_village);
            //warehouseModalList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String currDate = dateFormat.format(today);
            final EditText tv_from_date = mView.findViewById(R.id.tv_from_date);
            tv_from_date.setText(currDate);
            tv_from_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            String temDate = "" + dayOfMonth;
                            if (temDate.length() == 1) {
                                temDate = "0" + temDate;
                            }
                            String temmonth = "" + (monthOfYear + 1);
                            if (temmonth.length() == 1) {
                                temmonth = "0" + temmonth;
                            }
                            tv_from_date.setText(year + "-" + temmonth + "-"
                                    + temDate);

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            final EditText tv_to_date = mView.findViewById(R.id.tv_to_date);
            tv_to_date.setText(currDate);
            tv_to_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            String temDate = "" + dayOfMonth;
                            if (temDate.length() == 1) {
                                temDate = "0" + temDate;
                            }
                            String temmonth = "" + (monthOfYear + 1);
                            if (temmonth.length() == 1) {
                                temmonth = "0" + temmonth;
                            }
                            tv_to_date.setText(year + "-" + temmonth + "-"
                                    + temDate);

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            List<VillageSurveyModel> villageModelList = new ArrayList<>();
            villageModelList = dbh.getVillageModelFiltered("", "0");
            ArrayList<String> data = new ArrayList<String>();
            for (int i = 0; i < villageModelList.size(); i++) {
                data.add(villageModelList.get(i).getVillageCode() + " / " + villageModelList.get(i).getVillageName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            village.setAdapter(adapter);
            dialog = dialogbilder.create();
            dialog.show();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            final List<VillageSurveyModel> finalVillageModelList = villageModelList;
            final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (tv_from_date.getText().toString().length() == 0) {
                            AlertPopUp("Please enter from date");
                        } else if (tv_to_date.getText().toString().length() == 0) {
                            AlertPopUp("Please enter to date");
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(context, StaffSurveyReportCaneTypeSummery.class);
                            intent.putExtra("village_code", finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode());
                            intent.putExtra("from_date", tv_from_date.getText().toString());
                            intent.putExtra("to_date", tv_to_date.getText().toString());
                            startActivity(intent);
                        }

                    } catch (Exception e) {

                    }
                }
            });
        }
        catch(Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void openVarietyWiseSummary(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder dialogbilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_report_cane_type_summery, null);
        dialogbilder.setView(mView);
        final Spinner village=mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);
        final EditText tv_from_date=mView.findViewById(R.id.tv_from_date);
        tv_from_date.setText(currDate);
        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        tv_from_date.setText(year+"-"+temmonth + "-"
                                +temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        final EditText tv_to_date=mView.findViewById(R.id.tv_to_date);
        tv_to_date.setText(currDate);
        tv_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        tv_to_date.setText(year+"-"+temmonth + "-"
                                +temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        List<VillageSurveyModel> villageModelList=new ArrayList<>();
        villageModelList=dbh.getVillageModelFiltered("","0");
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<villageModelList.size();i++)
        {
            data.add(villageModelList.get(i).getVillageCode()+" / "+villageModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageModelList = villageModelList;
        final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(tv_from_date.getText().toString().length()==0)
                    {
                        AlertPopUp("Please enter from date");
                    }
                    else if(tv_to_date.getText().toString().length()==0)
                    {
                        AlertPopUp("Please enter to date");
                    }
                    else
                    {
                        dialog.dismiss();
                        Intent intent=new Intent(context, StaffSurveyReportVarietySummery.class);
                        intent.putExtra("village_code", finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode());
                        intent.putExtra("from_date",tv_from_date.getText().toString());
                        intent.putExtra("to_date",tv_to_date.getText().toString());
                        startActivity(intent);
                    }

                }
                catch(Exception e)
                {

                }
            }
        });
    }

    public void plotWiseReport(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder dialogbilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_report_plot_wise_grower, null);
        dialogbilder.setView(mView);
        final Spinner village=mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        final AutoCompleteTextView tv_grower=mView.findViewById(R.id.tv_grower);

        List<VillageSurveyModel> villageModelList=new ArrayList<>();
        villageModelList=dbh.getVillageModel("");
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<villageModelList.size();i++)
        {
            data.add(villageModelList.get(i).getVillageCode()+" / "+villageModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        farmerModelList=new ArrayList<>();
        final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                farmerModelList=dbh.getFarmerWithVillageModel(""+finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode(),"");
                farmerAutoSearchAdapter = new FarmerAutoSearchAdapter(context, R.layout.all_list_row_item_search, farmerModelList);
                tv_grower.setThreshold(1);
                tv_grower.setAdapter(farmerAutoSearchAdapter);
                tv_grower.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        farmerAutoSearchAdapter.getFilter().filter(s);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_grower.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FarmerModel purcheyDataModel = (FarmerModel) parent.getItemAtPosition(position);
                        tv_grower.setText(purcheyDataModel.getFarmerCode());
                    }
                });
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                try {
                    Intent intent=new Intent(context, StaffSurveyReportFarmerPlotSummery.class);
                    intent.putExtra("village_code", finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode());
                    intent.putExtra("tv_grower",tv_grower.getText().toString());
                    startActivity(intent);
                }
                catch(Exception e)
                {

                }
            }
        });
    }


    public void checkListReport(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder dialogbilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_report_cane_type_summery, null);
        dialogbilder.setView(mView);
        final Spinner village=mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        final EditText tv_from_date=mView.findViewById(R.id.tv_from_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);
        tv_from_date.setText(currDate);
        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        tv_from_date.setText(year+"-"+temmonth + "-"
                                +temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        final EditText tv_to_date=mView.findViewById(R.id.tv_to_date);
        tv_to_date.setText(currDate);
        tv_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        tv_to_date.setText(year+"-"+temmonth + "-"
                                +temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        List<VillageSurveyModel> villageModelList=new ArrayList<>();
        villageModelList=dbh.getVillageModelFiltered("","0");
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<villageModelList.size();i++)
        {
            data.add(villageModelList.get(i).getVillageCode()+" / "+villageModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageModelList = villageModelList;
        final List<VillageSurveyModel> finalVillageModelList1 = villageModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                if(tv_from_date.getText().toString().length()==0)
                {
                    AlertPopUp("Please enter from date");
                }
                else if(tv_to_date.getText().toString().length()==0)
                {
                    AlertPopUp("Please enter to date");
                }
                else
                {
                    dialog.dismiss();
                    Intent intent=new Intent(context, StaffSurveyReportCheckListActivity.class);
                    intent.putExtra("village_code", finalVillageModelList1.get(village.getSelectedItemPosition()).getVillageCode());
                    intent.putExtra("from_date",tv_from_date.getText().toString());
                    intent.putExtra("to_date",tv_to_date.getText().toString());
                    startActivity(intent);
                }
            }
            catch(Exception e)
            {

            }
            }
        });
    }

    public void openResendAllDataToServer(View v)
    {
        try
        {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffSurveyReport.this);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
            dialogbilder.setView(mView);
            final EditText password=mView.findViewById(R.id.password);

            final AlertDialog dialogPopup = dialogbilder.create();
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
                            new AlertDialogManager().RedDialog(StaffSurveyReport.this,"Please enter password");
                        }
                        else
                        {
                            if(password.getText().toString().equalsIgnoreCase("dplot"))
                            {
                                dbh.updateAllServerFarmerShareModel();
                                dialogPopup.dismiss();
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(StaffSurveyReport.this,"Please enter valid password");
                            }

                        }
                    }
                    catch(Exception e)
                    {
                        new AlertDialogManager().RedDialog(StaffSurveyReport.this,"Error:"+e.toString());
                    }
                    dialogPopup.dismiss();
                }
            });
        }
        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    /*public void openGrowerEnquiry(View v)
    {
        try
        {
            addressList=new ArrayList<>();
            Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (l != null) {
                // Logic to handle location object
                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String address = "";
                addressList = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                opendialogue(l.getLatitude(), l.getLongitude());
            } else {
                AlertPopUp("Sorry location not found please try again");
            }
        }
        catch (SecurityException e) {
            AlertPopUp("Error:"+e.toString());
        }
        catch (Exception e) {
            AlertPopUp("Error:"+e.toString());
        }
    }

    private void opendialogue(final double Lat1, final double Lng1)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        ArrayList<String> divData=new ArrayList<String>();
        final TextView err_msg=mView.findViewById(R.id.err_msg);
        if(addressList.size()>0)
        {
            err_msg.setText("\nLattitude : "+Lat1+"\nLongitude : "+Lng1+"\nAddress : "+ addressList.get(0).getAddressLine(0));
        }
        else
        {
            err_msg.setText("\nLattitude : "+Lat1+"\nLongitude : "+Lng1);
        }
        Button b1 = (Button) mView.findViewById(R.id.btn_ok);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    Intent intent=new Intent(context, GrowerFinder.class);
                    intent.putExtra("lat",""+Lat1);
                    intent.putExtra("lng",""+Lng1);
                    startActivity(intent);
                }
            }
        });
        alertDialog.show();
    }*/



    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void AlertPopUpWithFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void openCropObservationReport(View v)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffSurveyReport.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_observation, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView=mView.findViewById(R.id.textView);
        textView.setText("Search Cane Observation");

        /*final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_variety_from = mView.findViewById(R.id.rl_report_variety_from);
        final TextInputLayout rl_report_variety_to = mView.findViewById(R.id.rl_report_variety_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);*/

        final TextView err_msg = mView.findViewById(R.id.err_msg);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        final EditText report_vill_code = mView.findViewById(R.id.report_vill_code);
        final EditText report_grower_code = mView.findViewById(R.id.report_grower_code);

        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(StaffSurveyReport.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        report_from_date.setText(temDate+"-"+temmonth+"-"+year );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(StaffSurveyReport.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        report_to_date.setText(temDate+"-"+temmonth+"-"+year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(report_to_date.getText().toString().length()==0 && report_from_date.getText().toString().length()>0)
                {
                    err_msg.setText("Please enter to date");
                }
                else
                {
                    Intent intent = new Intent(StaffSurveyReport.this, StaffCropObservationReport.class);
                    intent.putExtra("factory", userDetailsModels.get(0).getDivision());
                    intent.putExtra("Village", report_vill_code.getText().toString());
                    intent.putExtra("Grower", report_grower_code.getText().toString());
                    intent.putExtra("DateFrom", report_from_date.getText().toString());
                    intent.putExtra("DateTo", report_to_date.getText().toString());
                    intent.putExtra("Supervisor", ""+userDetailsModels.get(0).getCode());
                    intent.putExtra("season", "");
                    startActivity(intent);
                    //finish();
                    while (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openCropObservation(View v)
    {
        Intent intent=new Intent(context, PlotFinderMapViewMaster.class);
        startActivity(intent);
    }

}
