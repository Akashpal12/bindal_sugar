package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class AddPlotEntryLog extends AppCompatActivity {
    String AREA;
    String GSRNO;
    String G_CODE;
    String G_MOBILE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String SHARE_AREA;
    String StrCaneType = "";
    String StrVarietyCode = "", VARIETY;
    String TAG = "AddGrowerShare";
    String V_CODE;
    Spinner category, input_meeting_with;
    Context context;
    List<MasterDropDown> meetingWith,varietyList,catogoryList;

    /*TextInputLayout input_layout_spray_item;
    EditText activityDate,activityNumber,description,input_activity_area;
    Spinner input_item,plot_type,activityCode,activityMethod;
    List<MasterSubDropDown> activityCodeList,activityMethodList;
    List<MasterDropDown> itemMasterList;
    List<PlotActivityModel> plotActivityModelList;*/

    /* renamed from: db */
    SQLiteDatabase db;
    DBHelper dbh;
    EditText grower_name;
    List<MasterCaneSurveyModel> masterCaneTypeList;
    List<MasterCaneSurveyModel> masterVarietyModelList;
    EditText plot_sr_no;
    EditText remark;
    EditText standing_cane, input_meeting_name, input_meeting_number;
    List<UserDetailsModel> userDetailsModels;
    Spinner variety;
    EditText village_code;
    double lat = 0.0, lng = 0.0;
    String address;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plot_entry_log);
        try {
            context = this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU_VARITAL_CHECK));
            toolbar.setTitle(getString(R.string.MENU_VARITAL_CHECK));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            userDetailsModels = dbh.getUserDetailsModel();
            V_CODE = getIntent().getExtras().getString("V_CODE");
            G_CODE = getIntent().getExtras().getString("G_CODE");
            PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
            GSRNO = getIntent().getExtras().getString("GSRNO");
            PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
            AREA = getIntent().getExtras().getString("AREA");
            SHARE_AREA = getIntent().getExtras().getString("SHARE_AREA");
            G_NAME = getIntent().getExtras().getString("G_NAME");
            VARIETY = getIntent().getExtras().getString("VARIETY");
            G_MOBILE = getIntent().getExtras().getString("MOBILE");
            village_code = (EditText) findViewById(R.id.village_code);
            grower_name = (EditText) findViewById(R.id.grower_name);
            plot_sr_no = (EditText) findViewById(R.id.plot_sr_no);
            category = (Spinner) findViewById(R.id.category);
            variety = (Spinner) findViewById(R.id.variety);
            standing_cane = (EditText) findViewById(R.id.standing_cane);
            remark = (EditText) findViewById(R.id.remark);
            village_code.setText(V_CODE);
            grower_name.setText(G_NAME);

            plot_sr_no.setText(PLOT_SR_NO);
            input_meeting_with = findViewById(R.id.input_meeting_with);
            input_meeting_name = findViewById(R.id.input_meeting_name);
            input_meeting_number = findViewById(R.id.input_meeting_number);
            input_meeting_number.setText(G_MOBILE);

            /*plotActivityModelList=new ArrayList<>();
            input_layout_spray_item = findViewById(R.id.input_layout_spray_item);
            input_layout_spray_item.setVisibility(View.GONE);
            input_item=findViewById(R.id.input_item);
            activityDate = findViewById(R.id.input_date);
            plot_type = findViewById(R.id.input_plot_type);
            activityCode = findViewById(R.id.input_activity);
            activityMethod = findViewById(R.id.input_activity_method);
            activityNumber = findViewById(R.id.input_activity_no);
            description = findViewById(R.id.input_description);
            input_activity_area = findViewById(R.id.input_activity_area);*/

           // masterVarietyModelList = dbh.getMasterModel("1");
            varietyList=dbh.getMasterDropDown("12");
            ArrayList<String> dataVarietyModelList = new ArrayList<>();
            dataVarietyModelList.add("Select");
            for (int i = 0; i < varietyList.size(); i++) {
                dataVarietyModelList.add(varietyList.get(i).getName());
            }
            ArrayAdapter<String> adapterSprayType = new ArrayAdapter<>(context, R.layout.list_item, dataVarietyModelList);
            variety.setAdapter(adapterSprayType);
            variety.setSelection(-1);

            // variety.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataVarietyModelList));

           //int preselectedPosition = -1;

           // Find the position based on the matching value
           /* for (int i = 0; i < dataVarietyModelList.size(); i++) {
                if (dataVarietyModelList.get(i).equals(VARIETY)) {
                    preselectedPosition = i;
                    break;
                }
            }*/
             // Set the preselected position if found



            variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        AddPlotEntryLog addPlotEntryLog = AddPlotEntryLog.this;
                        addPlotEntryLog.StrVarietyCode = addPlotEntryLog.varietyList.get(variety.getSelectedItemPosition() - 1).getCode();
                        return;
                    }
                    StrVarietyCode = "";
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
           // masterCaneTypeList = dbh.getMasterModel("2");
          catogoryList = dbh.getMasterDropDown("26");
            ArrayList<String> dataCaneTypeList = new ArrayList<>();
            dataCaneTypeList.add(getString(R.string.LBL_SELECT));

            for (int i2 = 0; i2 < catogoryList.size(); i2++) {
                dataCaneTypeList.add(catogoryList.get(i2).getName());
            }
            category.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneTypeList));
            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        AddPlotEntryLog addPlotEntryLog = AddPlotEntryLog.this;
                        addPlotEntryLog.StrCaneType = addPlotEntryLog.catogoryList.get(category.getSelectedItemPosition() - 1).getCode();
                        return;
                    }
                    StrCaneType = "";
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            meetingWith = dbh.getMasterDropDown("27");
            ArrayList arrayListMeeting = new ArrayList();
            arrayListMeeting.add(" - Select -");
            for (int i = 0; i < meetingWith.size(); i++) {
                arrayListMeeting.add(meetingWith.get(i).getCode() + " - " + meetingWith.get(i).getName());
            }
            ArrayAdapter<String> adapterMeeting = new ArrayAdapter<String>(context, R.layout.list_item, arrayListMeeting);
            input_meeting_with.setAdapter(adapterMeeting);
            input_meeting_with.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String[] meetingArr = input_meeting_with.getSelectedItem().toString().split(" - ");
                    TextInputLayout input_layout_meeting_number = findViewById(R.id.input_layout_meeting_number);
                    TextInputLayout input_layout_meeting_name = findViewById(R.id.input_layout_meeting_name);
                    input_layout_meeting_name.setVisibility(View.VISIBLE);
                    input_layout_meeting_number.setVisibility(View.VISIBLE);
                    if (meetingArr[0].equalsIgnoreCase("1")) {
                        input_meeting_name.setText(grower_name.getText().toString());
                        input_meeting_number.setText(G_MOBILE);
                        //input_meeting_number.setText(growerName.getText().toString());
                    } else if (meetingArr[0].equalsIgnoreCase("2")) {
                        input_layout_meeting_name.setVisibility(View.GONE);
                        input_layout_meeting_number.setVisibility(View.GONE);
                    } else {
                        input_meeting_name.setText("");
                        input_meeting_number.setText("");

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            startLocationUpdates();
            //initActivity();
        } catch (Exception e) {
            AlertDialogManager alertDialogManager = new AlertDialogManager();
            Context context2 = context;
            alertDialogManager.RedDialog(context2, "Error:" + e.toString());
        }
    }

    protected void startLocationUpdates() {

        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            LocationRequest locationRequest = new LocationRequest();
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
                                //lat=28.917547499999998;
                                //lng=78.3702595;
                                //lat=28.9604204;
                                //lng=78.2882313;
                                if (APIUrl.isDebug) {
                                    lat = APIUrl.lat;
                                    lng = APIUrl.lng;
                                }
                                final List<Address> addressesList;
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                addressesList = geocoder.getFromLocation(lat, lng, 1);
                                address = addressesList.get(0).getAddressLine(0);
                            } catch (Exception e) {

                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException se) {
            new AlertDialogManager().RedDialog(this, "Security Error 3:" + se.getMessage());
        } catch (Exception se) {
            new AlertDialogManager().RedDialog(this, "Error 4:" + se.getMessage());
        }

    }

    public void exit(View v) {
        finish();
    }

    /*public void initActivity()
    {
        itemMasterList=dbh.getMasterDropDown("9");
        ArrayList arrayListItem = new ArrayList();
        arrayListItem.add(" - Select -");
        for (int i = 0; i < itemMasterList.size(); i++) {
            arrayListItem.add(itemMasterList.get(i).getCode()+" - "+itemMasterList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, arrayListItem);
        input_item.setAdapter(adapter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        //paymentDate.setText(currentDt);
        activityDate.setInputType(InputType.TYPE_NULL);
        activityDate.setTextIsSelectable(true);
        activityDate.setFocusable(false);
        activityDate.setOnClickListener(new View.OnClickListener() {
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
                        activityDate.setText(year + "-" + temmonth + "-" + temDate);

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        activityCodeList = new ArrayList<>();
        //new getMasterData().execute();

        //new getWarehouseList().execute();
        activityMethodList = new ArrayList();
        ArrayList arrayListSprayType = new ArrayList();
        arrayListSprayType.add("Select Plant Type");
        arrayListSprayType.add("Plant");
        arrayListSprayType.add("Ratoon");
        ArrayAdapter<String> adapterSprayType = new ArrayAdapter<String>(context,
                R.layout.list_item, arrayListSprayType);
        plot_type.setAdapter(adapterSprayType);
        plot_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                try{
                    ArrayList arrayList = new ArrayList();
                    arrayList.add("Select Activity");
                    if(position==0)
                    {

                    }
                    else{
                        activityCodeList = dbh.getMasterSubDropDownActivity("25",(plot_type.getSelectedItemPosition()-1)+"");
                        for (int i = 0; i < activityCodeList.size(); i++) {
                            arrayList.add(activityCodeList.get(i).getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            R.layout.list_item, arrayList);
                    activityCode.setAdapter(adapter);
                    activityCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try{
                                ArrayList arrayList = new ArrayList();
                                arrayList.add("Select Activity");
                                if(i>0)
                                {
                                    activityMethodList = dbh.getMasterSubDropDown("22",activityCodeList.get(activityCode.getSelectedItemPosition()-1).getCode());
                                    for (int j = 0; j < activityMethodList.size(); j++) {
                                        arrayList.add(activityMethodList.get(j).getName());
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                        R.layout.list_item, arrayList);
                                activityMethod.setAdapter(adapter);
                                activityMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        try{
                                            if(position==0)
                                            {
                                                input_layout_spray_item.setVisibility(View.GONE);
                                                input_item.setSelection(0);
                                            }
                                            else{
                                                String t=activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag();
                                                if(t.equalsIgnoreCase("1"))
                                                {
                                                    input_layout_spray_item.setVisibility(View.VISIBLE);
                                                    //input_item.setSelection(0);
                                                }
                                                else{
                                                    input_layout_spray_item.setVisibility(View.GONE);
                                                    input_item.setSelection(0);
                                                }
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            catch (Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void addActivity(View v)
    {
        CheckValidation :{
            try {
                if (activityCode.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select activity");
                    break CheckValidation;
                }
                if (activityNumber.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please enter activity numer");
                    break CheckValidation;
                }
                if (activityDate.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select date");
                    break CheckValidation;
                }
                if (activityMethod.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select activity method");
                    break CheckValidation;
                }
                if(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag().equalsIgnoreCase("1"))
                {
                    if(input_item.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().showToast((Activity) context, "Please select item");
                        break CheckValidation;
                    }
                }
                String[] item=input_item.getSelectedItem().toString().split(" - ");
                PlotActivityModel plotActivityModel = new PlotActivityModel();
                plotActivityModel.setActivity(Integer.parseInt(activityCodeList.get(activityCode.getSelectedItemPosition() - 1).getCode()));
                plotActivityModel.setActivityName(activityCodeList.get(activityCode.getSelectedItemPosition() - 1).getName());
                plotActivityModel.setActivityNumber(Integer.parseInt(activityNumber.getText().toString()));
                plotActivityModel.setDate(activityDate.getText().toString());
                plotActivityModel.setDescription(description.getText().toString());
                plotActivityModel.setArea(input_activity_area.getText().toString());
                plotActivityModel.setActivityMethod(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getCode());
                plotActivityModel.setActivityMethodName(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getName());
                if(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag().equalsIgnoreCase("1")){
                    plotActivityModel.setItemList(item[0]);
                }
                else{
                    plotActivityModel.setItemList(null);
                }
                if (plotActivityModelList.contains(plotActivityModel)) {
                    new AlertDialogManager().showToast((Activity) context, "This item already added");
                    activityCode.setSelection(0);
                    input_item.setSelection(0);
                    activityNumber.setText("");
                    activityDate.setText("");
                    description.setText("");
                    input_activity_area.setText("");
                    //activityMethod.setText("");
                } else {
                    plotActivityModelList.add(plotActivityModel);
                    activityCode.setSelection(0);
                    input_item.setSelection(0);
                    activityNumber.setText("");
                    activityDate.setText("");
                    description.setText("");
                    input_activity_area.setText("");
                    setData();
                }
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
            }
        }
    }

    private void setData()
    {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListPlotActivityAdapter listPloughingAdapter = new ListPlotActivityAdapter(context, plotActivityModelList);
            recyclerView.setAdapter(listPloughingAdapter);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
    }*/

    public void saveData(View v) {
        CheckValidate:
        {
            if (V_CODE.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter village code");
                break CheckValidate;
            }
            if (G_CODE.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter grower code");
                break CheckValidate;
            }
            if (PLOT_SR_NO.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter plot serial number");
                break CheckValidate;
            }
            if (PLOT_VILL.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter plot village code");
                break CheckValidate;
            }
            if (StrCaneType.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select cane type");
                break CheckValidate;
            }
            if (StrVarietyCode.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select variety");
                break CheckValidate;
            }
            if (AREA.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter area");
                break CheckValidate;
            }
            if (SHARE_AREA.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter share area");
                break CheckValidate;
            }
            if (standing_cane.getText().toString().length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter standing cane");
                break CheckValidate;
            }
            if (input_meeting_with.getSelectedItemPosition() == 0) {
                new AlertDialogManager().showToast(AddPlotEntryLog.this, "Please select meeting type");
                break CheckValidate;
            }
            String[] meetingArr = input_meeting_with.getSelectedItem().toString().split(" - ");
            if (!meetingArr[0].equalsIgnoreCase("2")) {
                if (input_meeting_name.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(AddPlotEntryLog.this, "Please enter meeting name");
                    break CheckValidate;
                }
                if (input_meeting_number.getText().toString().length() != 10) {
                    new AlertDialogManager().showToast(AddPlotEntryLog.this, "Please enter 10 digit valid mobile number of meeting person name");
                    break CheckValidate;
                }
            }
            /*if(plotActivityModelList.size()==0)
            {
                new AlertDialogManager().showToast(AddPlotEntryLog.this,"Please add activity");
                break CheckValidate;
            }*/
            new SaveLog().execute(new String[]{standing_cane.getText().toString(), remark.getText().toString()});
        }

    }

    /* renamed from: in.co.vibrant.canepotatodevelopment.view.fieldstaff.AddPlotEntryLog$SaveLog */
    private class SaveLog extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        String message;

        private SaveLog() {
            dialog = new ProgressDialog(context);
        }

        /* access modifiers changed from: protected */
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog show = ProgressDialog.show(context, getString(R.string.app_name), "Please wait", true);
            dialog = show;
            show.show();
        }

        protected Void doInBackground(String... params) {
            try {
                Gson gson = new Gson();
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetGrowerFinderDataSave);
                request1.addProperty(UserDetailsModel.U_CODE, userDetailsModels.get(0).getCode());
                request1.addProperty(UserDetailsModel.DIVN, userDetailsModels.get(0).getDivision());

                request1.addProperty("V_CODE", ""+V_CODE);
                request1.addProperty("G_CODE", ""+G_CODE);
                request1.addProperty("PLOT_SR_NO", ""+PLOT_SR_NO);
                request1.addProperty("PLOT_VILL", ""+PLOT_VILL);
                request1.addProperty("CANETYPE", ""+StrCaneType);
                request1.addProperty("VARIETY", ""+StrVarietyCode);
                request1.addProperty("AREA", ""+AREA);
                request1.addProperty("SHARE_AREA", ""+AREA);
                request1.addProperty("STANDING_CANE", ""+params[0]);
                request1.addProperty("REMARK", ""+params[1]);
                request1.addProperty("IMEINO", imei);
                //request1.addProperty("Activity", gson.toJson(plotActivityModelList));
                request1.addProperty("MEETINGTYPE", ""+input_meeting_with.getSelectedItem().toString().split(" - ")[0]);
                request1.addProperty("MEETINGNAME", ""+input_meeting_name.getText().toString());
                request1.addProperty("MEETINGNUMBER", ""+input_meeting_number.getText().toString());
                request1.addProperty("LAT", "" + lat);
                request1.addProperty("LNG", "" + lng);
                request1.addProperty("ADDRESS", ""+address);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetGrowerFinderDataSave, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("GetGrowerFinderDataSaveResult").toString();
                return null;
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            } catch (Exception e2) {
                Log.e("Exception", e2.getMessage());
                message = e2.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            String str = TAG;
            Log.d(str, "onPostExecute: " + result);
            if (message != null) {
                try {
                    Log.d(TAG, message);
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                        new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                    } else {
                        new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                    }

                } catch (Exception e)
                {

                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } else if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


}
