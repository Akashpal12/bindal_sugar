package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListPlotActivityAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.PlotActivityModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

//,
public class AddCropObservation extends AppCompatActivity {
    String G_CODE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String MOBILE;
    String GSRNO;
    String AREA;
    String G_MOBILE;
    String SHARE_AREA, selectedAffectedCode;
    String StrCaneType = "";
    String StrVarietyCode = "";
    String TAG = "AddGrowerShare", GroupArea;
    String V_CODE;
    Context context;

    double lat = 0.00, lng = 0.00;

    /* renamed from: db */
    SQLiteDatabase db;
    DBHelper dbh;
    EditText grower_name, input_meeting_name, input_meeting_number;
    List<MasterDropDown> masterCaneTypeList, masterDisease, masterCropCondition, masterIrrigation, masterCaneEarthing, masterCaneProping, diseffectedtype;
    EditText plot_sr_no;
    List<UserDetailsModel> userDetailsModels;
    Spinner cane_type, disease, crop_condition, irrigation, cane_earthing, cane_proping, input_meeting_with;
    EditText village_code, remark;

    TextInputLayout input_layout_spray_item, input_layout_affected_condition;

    EditText activityDate, description, input_activity_area;
    Spinner input_item, plot_type, activityCode, activityMethod, effect_condition;
    List<MasterSubDropDown> activityCodeList, activityMethodList;
    List<MasterDropDown> itemMasterList;
    List<PlotActivitySaveModel> plotActivitySaveModelList;
    List<PlotActivityModel> plotActivityModelList;

    List<MasterDropDown> meetingWith;

    String filename = "", pictureImagePath = "", CANE_TYPE, ActivityselectedOption;
    ImageView image;
    String address;
    private int RC_CAMERA_REQUEST = 1001;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_crop_observation_log);
        try {
            context = this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU_UPDATE_CROP_OBSERVATION));
            toolbar.setTitle(getString(R.string.MENU_UPDATE_CROP_OBSERVATION));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            image = findViewById(R.id.image);
            activityCodeList = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();
            V_CODE = getIntent().getExtras().getString("V_CODE");
            G_CODE = getIntent().getExtras().getString("G_CODE");
            PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
            GSRNO = getIntent().getExtras().getString("GSRNO");
            AREA = getIntent().getExtras().getString("AREA");
            SHARE_AREA = getIntent().getExtras().getString("SHARE_AREA");
            PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
            G_NAME = getIntent().getExtras().getString("G_NAME");
            MOBILE = getIntent().getExtras().getString("MOBILE");
            CANE_TYPE = getIntent().getExtras().getString("CANE_TYPE");
            G_MOBILE = getIntent().getExtras().getString("MOBILE");
            GroupArea = getIntent().getExtras().getString("GroupArea");
            village_code = (EditText) findViewById(R.id.village_code);
            grower_name = (EditText) findViewById(R.id.grower_name);
            plot_sr_no = (EditText) findViewById(R.id.plot_sr_no);
            cane_type = (Spinner) findViewById(R.id.cane_type);
            disease = (Spinner) findViewById(R.id.disease);
            crop_condition = findViewById(R.id.crop_condition);
            irrigation = findViewById(R.id.irrigation);
            cane_earthing = findViewById(R.id.cane_earthing);
            cane_proping = findViewById(R.id.cane_proping);
            remark = findViewById(R.id.remark);
            input_meeting_with = findViewById(R.id.input_meeting_with);
            input_meeting_name = findViewById(R.id.input_meeting_name);
            input_meeting_number = findViewById(R.id.input_meeting_number);
            village_code.setText(V_CODE);
            grower_name.setText(G_CODE + " / " + G_NAME + " / Area : " + GroupArea);
            plot_sr_no.setText(PLOT_SR_NO);

            plotActivityModelList = new ArrayList<>();
            input_layout_spray_item = findViewById(R.id.input_layout_spray_item);
            input_layout_affected_condition = findViewById(R.id.input_layout_affected_condition);
            input_layout_spray_item.setVisibility(View.GONE);
            input_item = findViewById(R.id.input_item);
            activityDate = findViewById(R.id.input_date);
            plot_type = findViewById(R.id.input_plot_type);
            activityCode = findViewById(R.id.input_activity);
            activityMethod = findViewById(R.id.input_activity_method);
            //activityNumber = findViewById(R.id.input_activity_no);
            description = findViewById(R.id.input_description);
            input_activity_area = findViewById(R.id.input_activity_area);
            effect_condition = findViewById(R.id.effect_condition);
            plotActivitySaveModelList = new ArrayList<>();


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
                        input_meeting_name.setText(G_NAME);
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

            initActivity();
            startLocationUpdates();
            new GetMaster().execute();


            diseffectedtype = dbh.getMasterDropDown("28");
            ArrayList diseffectedList = new ArrayList();
            diseffectedList.add(" Select Condition ");
            for (int i = 0; i < diseffectedtype.size(); i++) {
                diseffectedList.add(diseffectedtype.get(i).getCode() + " - " + diseffectedtype.get(i).getName());
            }
            ArrayAdapter<String> diseffectedTypeList = new ArrayAdapter<String>(context, R.layout.list_item, diseffectedList);
            effect_condition.setAdapter(diseffectedTypeList);
            effect_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {

                    }
                    if (position == 1) {
                        selectedAffectedCode = "1";
                    }
                    if (position == 2) {
                        selectedAffectedCode = "2";
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


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
            new AlertDialogManager().RedDialog(this, "Security Error 3:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().RedDialog(this, "Error 4:" + se.toString());
        }

    }

    public void saveData(View v) {
        CheckValidation:
        {
            if (V_CODE.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter village code");
                break CheckValidation;
            }
            if (G_CODE.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter grower code");
                break CheckValidation;
            }
            if (PLOT_SR_NO.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter plot serial number");
                break CheckValidation;
            }
            if (PLOT_VILL.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter plot village code");
                break CheckValidation;
            }
            if (cane_type.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select cane type");
                break CheckValidation;
            }
            if (disease.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select disease");
                break CheckValidation;
            }
            if (crop_condition.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select crop condition");
                break CheckValidation;
            }
            if (irrigation.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select irrigation");
                break CheckValidation;
            }
            if (cane_earthing.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select cane earthing");
                break CheckValidation;
            }
            if (cane_proping.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select cane proping");
                break CheckValidation;
            }
            if (pictureImagePath.length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please capture image");
                break CheckValidation;
            }

            if (input_meeting_with.getSelectedItemPosition() == 0) {
                new AlertDialogManager().showToast(AddCropObservation.this, "Please select meeting type");
                break CheckValidation;
            }
            String[] meetingArr = input_meeting_with.getSelectedItem().toString().split(" - ");
            if (!meetingArr[0].equalsIgnoreCase("2")) {
                if (input_meeting_name.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(AddCropObservation.this, "Please enter meeting name");
                    break CheckValidation;
                }
                if (input_meeting_number.getText().toString().length() != 10) {
                    new AlertDialogManager().showToast(AddCropObservation.this, "Please enter 10 digit valid mobile number of meeting person name");
                    break CheckValidation;
                }
            }
            if (plotActivityModelList.size() == 0) {
                new AlertDialogManager().showToast(AddCropObservation.this, "Please add activity");
                break CheckValidation;
            }
            new SaveLog().execute();
        }
    }

    public void ExitBtn(View v) {
        finish();
    }

    /* renamed from: in.co.vibrant.canepotatodevelopment.view.fieldstaff.AddPlotEntryLog$SaveLog */

    public void initActivity() {
        itemMasterList = dbh.getMasterDropDown("9");
        ArrayList arrayListItem = new ArrayList();
        arrayListItem.add(" - Select -");
        for (int i = 0; i < itemMasterList.size(); i++) {
            arrayListItem.add(itemMasterList.get(i).getCode() + " - " + itemMasterList.get(i).getName());
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
        arrayListSprayType.add("PLANT");
        arrayListSprayType.add("RATOON");
        ArrayAdapter<String> adapterSprayType = new ArrayAdapter<String>(context,
                R.layout.list_item, arrayListSprayType);
        plot_type.setAdapter(adapterSprayType);

//       if (CANE_TYPE.equals("PLANT")) {
//            plot_type.setSelection(1);
//        } else {
//            plot_type.setSelection(2);
//        }
        plot_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add("Select Activity");
                    if (position == 0) {

                    } else {
                        activityCodeList = dbh.getMasterSubDropDownActivity("25", (plot_type.getSelectedItemPosition() - 1) + "");
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
                            try {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add("Select Activity");
                                if (i > 0) {
                                    activityMethodList = dbh.getMasterSubDropDown("22", activityCodeList.get(activityCode.getSelectedItemPosition() - 1).getCode());
                                    for (int j = 0; j < activityMethodList.size(); j++) {
                                        arrayList.add(activityMethodList.get(j).getName());
                                    }
                                }
                                ActivityselectedOption = activityCode.getSelectedItem().toString();
                                if (ActivityselectedOption.equalsIgnoreCase("Diseases")) {
                                    input_layout_affected_condition.setVisibility(View.VISIBLE);
                                } else {
                                    input_layout_affected_condition.setVisibility(View.GONE);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                        R.layout.list_item, arrayList);
                                activityMethod.setAdapter(adapter);
                                activityMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        try {
                                            if (position == 0) {
                                                input_layout_spray_item.setVisibility(View.GONE);
                                                input_item.setSelection(0);
                                            } else {
                                                String t = activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag();
                                                if (t == null) {
                                                    input_layout_spray_item.setVisibility(View.GONE);
                                                    input_item.setSelection(0);

                                                } else {
                                                    if (t.equalsIgnoreCase("1")) {
                                                        input_layout_spray_item.setVisibility(View.VISIBLE);
                                                        //input_item.setSelection(0);
                                                    } else {
                                                        input_layout_spray_item.setVisibility(View.GONE);
                                                        input_item.setSelection(0);
                                                    }
                                                }

                                            }
                                        } catch (Exception e) {
                                            new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } catch (Exception e) {
                                new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void addActivity(View v) {
        CheckValidation:
        {
            try {
                if (activityCode.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select activity");
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


                String t = activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag();
                if (t != null) {
                    if (t.equalsIgnoreCase("1")) {
                        if (input_item.getSelectedItemPosition() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select item");
                            break CheckValidation;
                        }
                    }
                }
                String[] item = input_item.getSelectedItem().toString().split(" - ");
                PlotActivityModel plotActivityModel = new PlotActivityModel();
                plotActivityModel.setActivity(Integer.parseInt(activityCodeList.get(activityCode.getSelectedItemPosition() - 1).getCode()));
                plotActivityModel.setActivityName(activityCodeList.get(activityCode.getSelectedItemPosition() - 1).getName());
                plotActivityModel.setActivityNumber(0);
                plotActivityModel.setDiseasefact(selectedAffectedCode);
                plotActivityModel.setDate(activityDate.getText().toString());
                plotActivityModel.setDescription(description.getText().toString());
                plotActivityModel.setArea(input_activity_area.getText().toString());
                plotActivityModel.setActivityMethod(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getCode());
                plotActivityModel.setActivityMethodName(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getName());
                if (t != null) {
                    if (t.equalsIgnoreCase("1")) {

                        plotActivityModel.setItemList(item[0]);
                    } else {
                        plotActivityModel.setItemList(null);
                    }

                }
                PlotActivitySaveModel model = new PlotActivitySaveModel();
                model.setAffectedCondition(selectedAffectedCode);
                plotActivitySaveModelList.add(model);
                if (plotActivityModelList.contains(plotActivityModel)) {
                    new AlertDialogManager().showToast((Activity) context, "This item already added");
                    // activityCode.setSelection(0);
                    input_item.setSelection(0);
                    //activityNumber.setText("");
                    activityDate.setText("");
                    description.setText("");
                    input_activity_area.setText("");
                    //activityMethod.setText("");
                } else {
                    plotActivityModelList.add(plotActivityModel);
                    // activityCode.setSelection(0);
                    input_item.setSelection(0);
                    //activityNumber.setText("");
                    activityDate.setText("");
                    description.setText("");
                    effect_condition.setSelection(0);
                    input_activity_area.setText("");
                    setData();
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
            }
        }
    }

    private void setData() {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListPlotActivityAdapter listPloughingAdapter = new ListPlotActivityAdapter(context, plotActivityModelList);
            recyclerView.setAdapter(listPloughingAdapter);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }
    }

    public void openCam(View v) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            filename = "image" + currentDt + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/planting");
            dir.mkdirs();
            pictureImagePath = dir.getAbsolutePath() + "/" + filename;
            File file = new File(pictureImagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if (file.exists()) {
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat dateFormatter2 = new SimpleDateFormat("HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    dateFormatter2.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = dateFormatter2.format(today);
                    Bitmap bmp = drawTextToBitmap(context, d, t);
                    FileOutputStream out = new FileOutputStream(pictureImagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
                    image.setImageBitmap(bitmap);
                } else {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1) {
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);

            Bitmap.Config bitmapConfig =
                    bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(120);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(gText, 0, gText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) - 250;
            int y = (bitmap.getHeight() + bounds.height()) - 250;

            canvas.drawText(gText, x, y, paint);
            paint.getTextBounds(gText1, 0, gText1.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) - 250;
            int y1 = (bitmap.getHeight() + bounds.height()) - 100;
            canvas.drawText(gText1, x1, y1, paint);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {
        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

            if (heightRatio > 1 || widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }

    private class GetMaster extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/CropObservation";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                //entity.add(new BasicNameValuePair("appid","1211973affa328939680b7a52bd0cbfe"));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                //,,,,masterCaneProping;
                //,,,,,;
                masterDisease = new ArrayList<>();
                masterCropCondition = new ArrayList<>();
                masterIrrigation = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(Content);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                JSONArray masterCaneTypeArray = jsonObject.getJSONArray("CANETYPE");
                ArrayList<String> dataCaneTypeList = new ArrayList<>();
                dataCaneTypeList.add("Select CANE TYPE");
                for (int i = 0; i < masterCaneTypeArray.length(); i++) {
                    dataCaneTypeList.add((String) masterCaneTypeArray.get(i));
                }
                cane_type.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneTypeList));

                JSONArray masterCaneDiseaseArray = jsonObject.getJSONArray("Disease");
                ArrayList<String> dataCaneDisease = new ArrayList<>();
                dataCaneDisease.add("Select Disease");
                for (int i = 0; i < masterCaneDiseaseArray.length(); i++) {
                    dataCaneDisease.add(masterCaneDiseaseArray.getJSONObject(i).getString("Name"));
                    MasterDropDown masterDropDown = new MasterDropDown();
                    masterDropDown.setCode(masterCaneDiseaseArray.getJSONObject(i).getString("Code"));
                    masterDropDown.setName(masterCaneDiseaseArray.getJSONObject(i).getString("Name"));
                    masterDisease.add(masterDropDown);
                }
                disease.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneDisease));

                JSONArray masterCropConditionArray = jsonObject.getJSONArray("CropCondition");
                ArrayList<String> dataCropCondition = new ArrayList<>();
                dataCropCondition.add("Select Crop Condition");
                for (int i = 0; i < masterCropConditionArray.length(); i++) {
                    dataCropCondition.add(masterCropConditionArray.getJSONObject(i).getString("Name"));
                    MasterDropDown masterDropDown = new MasterDropDown();
                    masterDropDown.setCode(masterCropConditionArray.getJSONObject(i).getString("Code"));
                    masterDropDown.setName(masterCropConditionArray.getJSONObject(i).getString("Name"));
                    masterCropCondition.add(masterDropDown);
                }
                crop_condition.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCropCondition));

                JSONArray masterIrrigationArray = jsonObject.getJSONArray("Irrigation");
                ArrayList<String> dataIrrigation = new ArrayList<>();
                dataIrrigation.add("select Irrigation");
                for (int i = 0; i < masterIrrigationArray.length(); i++) {
                    dataIrrigation.add(masterIrrigationArray.getJSONObject(i).getString("Name"));
                    MasterDropDown masterDropDown = new MasterDropDown();
                    masterDropDown.setCode(masterIrrigationArray.getJSONObject(i).getString("Code"));
                    masterDropDown.setName(masterIrrigationArray.getJSONObject(i).getString("Name"));
                    masterIrrigation.add(masterDropDown);
                }
                irrigation.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataIrrigation));

                JSONArray masterCaneEarthingArray = jsonObject.getJSONArray("Caneearthing");
                ArrayList<String> dataCaneEarthingist = new ArrayList<>();
                dataCaneEarthingist.add("select Cane earthing");
                for (int i = 0; i < masterCaneEarthingArray.length(); i++) {
                    dataCaneEarthingist.add((String) masterCaneEarthingArray.get(i));
                }
                cane_earthing.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneEarthingist));

                JSONArray masterCanePropingArray = jsonObject.getJSONArray("Canepropping");
                ArrayList<String> dataCanePropingist = new ArrayList<>();
                dataCanePropingist.add("select Cane propping");
                for (int i = 0; i < masterCanePropingArray.length(); i++) {
                    dataCanePropingist.add((String) masterCanePropingArray.get(i));
                }
                cane_proping.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCanePropingist));
                //
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, e.getMessage());
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    private class SaveLog extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog;

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
                Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                Gson gson = new Gson();
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVECROPOBSERVATION1);
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("U_CODE", userDetailsModels.get(0).getCode());
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("PLOTVILL", PLOT_VILL);
                request1.addProperty("VillCode", V_CODE);
                request1.addProperty("Grower", G_CODE);
                request1.addProperty("PLOTNO", PLOT_SR_NO);
                request1.addProperty("Variety", "0");
                request1.addProperty("CaneType", cane_type.getSelectedItem().toString());
                request1.addProperty("Disease", masterDisease.get(disease.getSelectedItemPosition() - 1).getCode());
                request1.addProperty("CropCondition", masterCropCondition.get(crop_condition.getSelectedItemPosition() - 1).getCode());
                request1.addProperty("Irrigation", masterIrrigation.get(irrigation.getSelectedItemPosition() - 1).getCode());
                request1.addProperty("Caneearthing", cane_earthing.getSelectedItem().toString());
                request1.addProperty("Canepropping", cane_proping.getSelectedItem().toString());
                request1.addProperty("Remark", remark.getText().toString());
                request1.addProperty("Activity", gson.toJson(plotActivityModelList));
                request1.addProperty("Image", imgFrmt);
                request1.addProperty("Lat", "" + lat);
                request1.addProperty("Lng", "" + lng);
                request1.addProperty("ADDRESS", "" + address);
                request1.addProperty("MEETINGTYPE", input_meeting_with.getSelectedItem().toString().split(" - ")[0]);
                request1.addProperty("MEETINGNAME", G_NAME);
                request1.addProperty("MEETINGNUMBER", input_meeting_number.getText().toString());
                CheckBox send_sms = findViewById(R.id.send_sms);
                if (send_sms.isChecked()) {
                    request1.addProperty("SendSms", 1);
                } else {
                    request1.addProperty("SendSms", 0);
                }
                Log.d("", "doInBackground: " + gson.toJson(plotActivityModelList));
                Log.d("", "AllData" +request1 );
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVECROPOBSERVATION1, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVECROPOBSERVATION1Result").toString();
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
                        /*Intent intent = new Intent(AddCropObservation.this, StaffAddCropGrowthObservation.class);
                        intent.putExtra("V_CODE", V_CODE);
                        intent.putExtra("G_CODE", G_CODE);
                        intent.putExtra("PLOT_SR_NO", PLOT_SR_NO);
                        intent.putExtra("GSRNO", GSRNO);
                        intent.putExtra("PLOT_VILL", PLOT_VILL);
                        intent.putExtra("AREA", AREA);
                        intent.putExtra("SHARE_AREA", SHARE_AREA);
                        intent.putExtra("G_NAME", G_NAME);*/
                        //finish();
                        new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                    } else {
                        new AlertDialogManager().RedDialog(context, message);
                    }

                } catch (Exception e) {
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
