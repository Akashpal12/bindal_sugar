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
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

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
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.PlotActivityModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;


public class PlotActivityForm extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    String AREA;
    String GSRNO;
    String G_CODE;
    String G_NAME;
    String G_FATHER;
    String PLOT_SR_NO,imgFrmt="N/A";
    String G_MOBILE;
    String PLOT_VILL;
    String SHARE_AREA;
    String OLDSEAS;
    String OLDGHID;
    String PLOT_VILL_NAME;
    String MOBILE, selectedAffectedCode;
    List<UserDetailsModel> loginUserDetailsList;
    private int RC_CAMERA_REQUEST = 1001;
    String ActivityselectedOption;
    LinearLayout rl_image;
    String filename = "", pictureImagePath = "", CANETYPE;
    List<MasterDropDown> itemMasterList, meetingWith, diseffectedtype;
    List<MasterCaneSurveyModel> masterVarietyModelList;
    List<MasterSubDropDown> activityCodeList, activityMethodList;
    EditText villageCode, villageName, growerCode, growerName, growerFatherName, plotVillageCode, plotVillageName, plotVillageNumber,
            area, description, mobileNumber, activityDate, input_activity_no, input_meeting_name, input_meeting_number,
            input_activity_area;
    Spinner plot_type, activityCode, activityMethod, input_planting_number, input_item, input_meeting_with, effect_condition;
    LocationManager locationManager;
    String ID = "";
    Context context;
    TextInputLayout input_layout_spray_item, input_layout_affected_condition;
    //List<PlotFarmerShareOldModel> plotFarmerShareModelsList;
    //List<PlotFarmerShareModel> plotFarmerShareModelsList;
    List<PlotActivityModel> plotActivityModelList;
    List<PlotActivitySaveModel> plotActivitysaveModelList;
    //double lat=0.0,lng=0.0;
    String V_CODE, V_NAME, PLOT_FROM, CANE_TYPE;
    double lat = 0.00, lng = 0.00;
    String address;
    ImageView image;
    PlotActivitySaveModel plotActivitySaveModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot_activity_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Plot Activity");
        toolbar.setTitle("Plot Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = PlotActivityForm.this;
        plotActivityModelList = new ArrayList<>();

        plotActivitySaveModel = new PlotActivitySaveModel();
        plotActivitysaveModelList = new ArrayList<>();
        try {
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            //dbh.deleteSprayItem("");
            loginUserDetailsList = new ArrayList<>();
            //plotFarmerShareModelsList = new ArrayList<>();
            loginUserDetailsList = dbh.getUserDetailsModel();
            image = findViewById(R.id.image);
            rl_image = findViewById(R.id.rl_image);
            try {
                V_CODE = getIntent().getExtras().getString("V_CODE");
                V_NAME = getIntent().getExtras().getString("V_NAME");
                PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
                PLOT_FROM = getIntent().getExtras().getString("SURTYPE");

                G_CODE = getIntent().getExtras().getString("G_CODE");
                GSRNO = getIntent().getExtras().getString("GSRNO");
                PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
                PLOT_VILL_NAME = getIntent().getExtras().getString("PLOT_VILL_NAME");
                AREA = getIntent().getExtras().getString("AREA");
                SHARE_AREA = getIntent().getExtras().getString("SHARE_AREA");
                G_NAME = getIntent().getExtras().getString("G_NAME");
                G_FATHER = getIntent().getExtras().getString("G_FATHER");
                OLDSEAS = getIntent().getExtras().getString("OLDSEAS");
                OLDGHID = getIntent().getExtras().getString("OLDGHID");
                MOBILE = getIntent().getExtras().getString("MOBILE");
                CANE_TYPE = getIntent().getExtras().getString("CANE_TYPE");
                G_MOBILE = getIntent().getExtras().getString("MOBILE");
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error : " + e.getMessage());
            }
            //lat=28.894429;
            //lng=78.233282;
            TextView txt_lat = findViewById(R.id.txt_lat);
            TextView txt_lng = findViewById(R.id.txt_lng);
            txt_lat.setText("Plor Sr No : " + PLOT_SR_NO);
            txt_lng.setText("Plot From : " + PLOT_FROM);
            input_layout_spray_item = findViewById(R.id.input_layout_spray_item);
            input_layout_affected_condition = findViewById(R.id.input_layout_affected_condition);
            input_layout_spray_item.setVisibility(View.GONE);
            input_layout_affected_condition.setVisibility(View.GONE);
            villageCode = findViewById(R.id.input_village_code);
            villageName = findViewById(R.id.input_village_name);
            growerCode = findViewById(R.id.input_grower_code);
            growerName = findViewById(R.id.input_grower_name);
            growerFatherName = findViewById(R.id.input_grower_father);
            plotVillageCode = findViewById(R.id.input_plot_village_code);
            plotVillageName = findViewById(R.id.input_plot_village_name);
            plotVillageNumber = findViewById(R.id.input_plot_village_number);
            area = findViewById(R.id.input_area);
            description = findViewById(R.id.input_description);
            mobileNumber = findViewById(R.id.input_mobile_number);
            effect_condition = findViewById(R.id.effect_condition);
            mobileNumber.setText(MOBILE);


            //activityNumber = findViewById(R.id.input_activity_no);
            activityDate = findViewById(R.id.input_date);
            input_activity_no = findViewById(R.id.input_activity_no);
            input_meeting_with = findViewById(R.id.input_meeting_with);
            input_meeting_name = findViewById(R.id.input_meeting_name);
            input_meeting_number = findViewById(R.id.input_meeting_number);
            input_activity_area = findViewById(R.id.input_activity_area);

            plot_type = findViewById(R.id.input_plot_type);
            input_planting_number = findViewById(R.id.input_planting_number);
            activityCode = findViewById(R.id.input_activity);
            activityMethod = findViewById(R.id.input_activity_method);
            input_item = findViewById(R.id.input_item);

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
                        input_meeting_name.setText(growerName.getText().toString());
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
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            init();
            startLocationUpdates();

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
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
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

    private void init() throws Exception {
        //List<VillageModal> villageModalList = dbh.getVillageModal(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotVillageCode());
        plotVillageCode.setText(PLOT_VILL);
        plotVillageName.setText(PLOT_VILL_NAME);
        //int plotSrNo = Integer.parseInt(villageModalList.get(0).getMaxPlant());

        //List<VillageModal> villageModalList1 = dbh.getVillageModal(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getVillageCode());
        villageCode.setText(V_CODE);
        //if(villageModalList1.size()>0)
        villageName.setText(V_NAME);

        growerCode.setText(G_CODE);
        growerName.setText(G_NAME);
        growerFatherName.setText(G_FATHER);

        //plotSrNo++;
        plotVillageNumber.setText(PLOT_SR_NO);
        area.setText(AREA);
        //input_plot_sr_no.setText(""+plotSrNo);
        /*plotFarmerShareModelsList=dbh.getPlotDetailsByTypeSrno(PLOT_SR_NO,PLOT_FROM,V_CODE);
        if(plotFarmerShareModelsList.size()==0)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Sorry this plot not registered in our system");
        }
        else{
            ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
            indentPlotSrNoArrayList.add("Select");
            for (int i = 0; i < plotFarmerShareModelsList.size(); i++) {
                indentPlotSrNoArrayList.add("Village : " + plotFarmerShareModelsList.get(i).getPlotVillageCode() + "\nGrower : " + plotFarmerShareModelsList.get(i).getGrowerCode() + "\nPlot Serial : " + plotFarmerShareModelsList.get(i).getPlotSrNo());
            }

            ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                    R.layout.list_item, indentPlotSrNoArrayList);
            input_planting_number.setAdapter(adapterIndentSrNo);
            input_planting_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        //input_actual_area.setSelection(0);
                        plotVillageCode.setText("");
                        plotVillageName.setText("");
                        plotVillageNumber.setText("");

                        villageCode.setText("");
                        villageName.setText("");
                        growerCode.setText("");
                        growerName.setText("");
                        growerFatherName.setText("");

                        area.setText("");
                    } else {
                        //input_actual_area.setSelection(0);
                        List<VillageModal> villageModalList = dbh.getVillageModal(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotVillageCode());
                        if (villageModalList.size() > 0) {
                            plotVillageCode.setText(villageModalList.get(0).getCode());
                            plotVillageName.setText(villageModalList.get(0).getName());
                            int plotSrNo = Integer.parseInt(villageModalList.get(0).getMaxPlant());

                            List<VillageModal> villageModalList1 = dbh.getVillageModal(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getVillageCode());
                            villageCode.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getVillageCode());
                            if(villageModalList1.size()>0)
                            villageName.setText(villageModalList1.get(0).getName());

                            growerCode.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getGrowerCode());
                            growerName.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getGrowerName());
                            growerFatherName.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getGrowerFatherName());

                            plotSrNo++;
                            plotVillageNumber.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotSrNo());
                            area.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getAreaHectare());
                            //input_plot_sr_no.setText(""+plotSrNo);
                        } else {
                            plotVillageCode.setText("");
                            plotVillageName.setText("");
                            area.setText("");
                            //input_plot_sr_no.setText("");
                            new AlertDialogManager().RedDialog(context, "Plot village code not found in our record please contact admin");
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (indentPlotSrNoArrayList.size() > 1) {
                input_planting_number.setSelection(1);
            }
        }*/
        villageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (villageCode.getText().toString().length() > 0) {

                    //new getVillageData().execute(villageCode.getText().toString());
                    List<VillageModal> plotVillageModelList = dbh.getVillageModal(villageCode.getText().toString());
                    if (plotVillageModelList.size() > 0) {
                        villageCode.setText(plotVillageModelList.get(0).getCode());
                        villageName.setText(plotVillageModelList.get(0).getName());
                    } else {
                        resetPlotSerial();
                        new AlertDialogManager().RedDialog(context, "Please enter correct village code");
                    }
                }
            }
        });
        growerCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (villageCode.getText().toString().length() > 0) {
                        if (growerCode.getText().toString().length() > 0) {
                            //new getGrowerData().execute(villageCode.getText().toString(),growerCode.getText().toString());
                            List<GrowerModel> growerModelList = dbh.getGrowerModel(villageCode.getText().toString(), growerCode.getText().toString());
                            if (growerModelList.size() > 0) {
                                growerCode.setText(growerModelList.get(0).getGrowerCode());
                                growerName.setText(growerModelList.get(0).getGrowerName());
                                growerFatherName.setText(growerModelList.get(0).getGrowerFather());

                                /*if(plot_type.getSelectedItemPosition()==1)
                                {
                                    verifyFromGasti();
                                }
                                else
                                {
                                    verifyFromGasti();
                                    *//*growerCode.setText("");
                                    growerName.setText("");
                                    growerFatherName.setText("");
                                    villageCode.setText("");
                                    villageName.setText("");*//*
                                }*/

                            } else {
                                growerCode.setText("");
                                growerName.setText("");
                                growerFatherName.setText("");
                                new AlertDialogManager().RedDialog(context, "Invalid grower code!!!!");
                            }

                        }
                    } else {
                        new AlertDialogManager().RedDialog(context, "Please enter village code");
                        villageCode.requestFocus();
                    }
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                }
            }
        });
        plotVillageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (plotVillageCode.getText().toString().length() > 0) {
                    //new getPlotVillage().execute(plotVillageCode.getText().toString());
                    List<VillageModal> plotVillageModelList = dbh.getVillageModal(plotVillageCode.getText().toString());
                    if (plotVillageModelList.size() > 0) {
                        plotVillageCode.setText(plotVillageModelList.get(0).getCode());
                        plotVillageName.setText(plotVillageModelList.get(0).getName());
                        /*plotVillageNumber.setText(jsonObject.getString("VillPLNO"));
                        growerPlotNumber.setText(jsonObject.getString("GPLNO"));
                        area.setText(jsonObject.getString("Area"));
                        ID=jsonObject.getString("ID");*/
                    } else {
                        plotVillageCode.setText("");
                        plotVillageName.setText("");
                        plotVillageNumber.setText("");
                        area.setText("");
                        ID = "";
                        new AlertDialogManager().RedDialog(context, "Please enter correct plot village code");
                    }

                }
            }
        });
    }


    private void resetPlotSerial() {
        villageCode.setText("");
        villageName.setText("");
        growerCode.setText("");
        growerName.setText("");
        growerFatherName.setText("");
        plotVillageCode.setText("");
        plotVillageName.setText("");
        plotVillageNumber.setText("");
        area.setText("");
        ID = "";
        ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
        indentPlotSrNoArrayList.add("Select");
        ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                R.layout.list_item, indentPlotSrNoArrayList);
        input_planting_number.setAdapter(adapterIndentSrNo);
    }

    public void searchData(View v) {
        try {
            if (villageCode.getText().toString().length() == 0) {

            } else if (growerCode.getText().toString().length() == 0) {

            } else if (plotVillageCode.getText().toString().length() == 0) {

            } else {
                /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat=location.getLatitude();
                lng=location.getLongitude();
                new verifyData().execute(villageCode.getText().toString(), growerCode.getText().toString(),
                        plotVillageCode.getText().toString(),
                        "" +lat , "" +lng );*/
            }
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

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



    /*    masterVarietyModelList = dbh.getMasterModel("1");
        ArrayList<String> dataVarietyModelList = new ArrayList<>();
        dataVarietyModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterVarietyModelList.size(); i++) {
            dataVarietyModelList.add(masterVarietyModelList.get(i).getName());}

        plot_type.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataVarietyModelList));*/

        activityMethodList = new ArrayList();
        ArrayList arrayListSprayType = new ArrayList();
        arrayListSprayType.add("Select Plant Type");
        arrayListSprayType.add("PLANT");
        arrayListSprayType.add("RATOON");
        ArrayAdapter<String> adapterSprayType = new ArrayAdapter<String>(context, R.layout.list_item, arrayListSprayType);
        plot_type.setAdapter(adapterSprayType);


      /*  if (CANE_TYPE.equals("PLANT")){
            plot_type.setSelection(1);
        } else {
           plot_type.setSelection(2);
        }*/
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
                                if ("Diseases".equalsIgnoreCase(ActivityselectedOption)) {
                                    input_layout_affected_condition.setVisibility(View.VISIBLE);
                                    rl_image.setVisibility(View.VISIBLE);
                                } else {
                                    rl_image.setVisibility(View.GONE);
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
                if (mobileNumber.getText().toString().length() != 10) {
                    new AlertDialogManager().showToast((Activity) context, "Please enter 10 digit mobile number");
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
                if (ActivityselectedOption.equalsIgnoreCase("Diseases")) {
                    if (effect_condition.getSelectedItemPosition() == 0) {
                        new AlertDialogManager().showToast((Activity) context, "Please select affected condition");
                        break CheckValidation;
                    }

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

                //plotActivityModel.setMoileNumber(mobileNumber.getText().toString());
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
                saveMyData();

               /* if(activityMethodList.get(activityMethod.getSelectedItemPosition() - 1).getItemFlag().equalsIgnoreCase("1"))

                {

                    plotActivityModel.setItemList(item[0]);
                }
                else{
                    plotActivityModel.setItemList(null);
                }*/

                if (plotActivityModelList.contains(plotActivityModel)) {
                    new AlertDialogManager().showToast((Activity) context, "This item already added");
                    if (plotActivitysaveModelList.get(0).getActivityMethod().equalsIgnoreCase("Diseases")) {
                        input_layout_affected_condition.setVisibility(View.VISIBLE);
                        rl_image.setVisibility(View.VISIBLE);
                    } /*else {
                        activityCode.setSelection(0);
                    }*/
                    effect_condition.setSelection(0);
                    input_item.setSelection(0);
                    activityDate.setText("");
                    description.setText("");
                   activityMethod.setSelection(0);
                    input_activity_area.setText("");

                    //activityMethod.setText("");
                } else {
                    plotActivityModelList.add(plotActivityModel);
                    if (plotActivitysaveModelList.get(0).getActivityMethod().equalsIgnoreCase("Diseases")) {
                        input_layout_affected_condition.setVisibility(View.VISIBLE);
                        rl_image.setVisibility(View.VISIBLE);
                    } /*else {
                        activityCode.setSelection(0);
                    }*/
                    input_item.setSelection(0);
                    activityMethod.setSelection(0);
                    effect_condition.setSelection(0);
                    activityDate.setText("");
                    description.setText("");
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


    public void saveData(View v) {
        try {
            CheckValidation:
            {
                if (plot_type.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please select plot type");
                    break CheckValidation;
                }
                if (villageCode.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter village code");
                    break CheckValidation;
                }
                if (villageName.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter village code");
                    break CheckValidation;
                }
                if (growerCode.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter grower code");
                    break CheckValidation;
                }
                if (growerName.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter grower code");
                    break CheckValidation;
                }
                if (plotVillageNumber.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter plot village number");
                    break CheckValidation;
                }
                if (plotVillageCode.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter plot village code");
                    break CheckValidation;
                }
                if (plotVillageName.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter plot village code");
                    break CheckValidation;
                }
                if (area.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter area");
                    break CheckValidation;
                }
                if (mobileNumber.getText().toString().length() != 10) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter mobile number");
                    break CheckValidation;
                }
                if (input_meeting_with.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please select meeting type");
                    break CheckValidation;
                }
                if (ActivityselectedOption.equalsIgnoreCase("Diseases")) {
                    if (pictureImagePath.isEmpty()) {
                        new AlertDialogManager().showToast((Activity) context, "Please capture image");
                        break CheckValidation;
                    }
                }
                String[] meetingArr = input_meeting_with.getSelectedItem().toString().split(" - ");
                if (!meetingArr[0].equalsIgnoreCase("2")) {
                    if (input_meeting_name.getText().toString().length() == 0) {
                        new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter meeting name");
                        break CheckValidation;
                    }
                    if (input_meeting_number.getText().toString().length() != 10) {
                        new AlertDialogManager().showToast(PlotActivityForm.this, "Please enter 10 digit valid mobile number of meeting person name");
                        break CheckValidation;
                    }
                }

                if (plotActivityModelList.size() == 0) {
                    new AlertDialogManager().showToast(PlotActivityForm.this, "Please add activity");
                    break CheckValidation;

                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currDate = dateFormat.format(today);
                String json = new Gson().toJson(plotActivityModelList);
                Log.d("Json", json);

                plotActivitySaveModel.setPlotType(plot_type.getSelectedItemPosition() + "");
                plotActivitySaveModel.setVillageCode(villageCode.getText().toString());
                plotActivitySaveModel.setGrwerCode(growerCode.getText().toString());
                plotActivitySaveModel.setPlotSerialNumber(plotVillageNumber.getText().toString());
                plotActivitySaveModel.setPlotVillage(plotVillageCode.getText().toString());
                plotActivitySaveModel.setArea(area.getText().toString());
                plotActivitySaveModel.setSurveyType(PLOT_FROM);
                plotActivitySaveModel.setMobileNumber(mobileNumber.getText().toString());
                plotActivitySaveModel.setOldSeason("");
                plotActivitySaveModel.setOldGhid(PLOT_SR_NO);
                plotActivitySaveModel.setJsonArrayData(json);
                plotActivitySaveModel.setServerStatus("Pending");
                plotActivitySaveModel.setRemark("");
                plotActivitySaveModel.setCurrentDate(currDate);
                plotActivitySaveModel.setMeetingType(input_meeting_with.getSelectedItem().toString().split(" - ")[0]);
                plotActivitySaveModel.setMeetingName(input_meeting_name.getText().toString());
                plotActivitySaveModel.setMeetingNumber(input_meeting_number.getText().toString());
                //long insId=dbh.insertPlotActivitySaveModel(plotActivitySaveModel);
                //if(insId>0)
                //{
                //    new AlertDialogManager().AlertPopUpFinish(context,"Activity successfully saved");
                //}
                new SaveData().execute();
               // new AlertDialogManager().GreenDialog(context, "Data Save Succesfully");
            }

        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    private class SaveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {



                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEPLOTACTIVITY1";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                //entity.add(new BasicNameValuePair("Divn",userDetailsModels.get(0).getDivision()));
                //entity.add(new BasicNameValuePair("Seas",getString(R.string.season)));

                if (ActivityselectedOption.equalsIgnoreCase("Diseases")){
                    Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    byte[] byteFormat = bao.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    System.out.println(imgFrmt);
                    Log.d("Image : ", imgFrmt);
                    entity.add(new BasicNameValuePair("Image", ""+imgFrmt));
                }else {
                    Log.d("Image : ", imgFrmt);
                    entity.add(new BasicNameValuePair("Image", ""));
                }

                entity.add(new BasicNameValuePair("FactId", loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("PlotType", plotActivitySaveModel.getPlotType()));
                entity.add(new BasicNameValuePair("Village", plotActivitySaveModel.getVillageCode()));
                entity.add(new BasicNameValuePair("Grower", plotActivitySaveModel.getGrwerCode()));
                entity.add(new BasicNameValuePair("PLOTVillage", plotActivitySaveModel.getPlotVillage()));
                entity.add(new BasicNameValuePair("PlotSerialNumber", plotActivitySaveModel.getPlotSerialNumber()));
                entity.add(new BasicNameValuePair("Area", plotActivitySaveModel.getArea()));
                entity.add(new BasicNameValuePair("data", plotActivitySaveModel.getJsonArrayData()));
                entity.add(new BasicNameValuePair("SupCode", loginUserDetailsList.get(0).getCode()));
                entity.add(new BasicNameValuePair("mobileNumber", plotActivitySaveModel.getMobileNumber()));
                entity.add(new BasicNameValuePair("Season", getString(R.string.season)));
                entity.add(new BasicNameValuePair("AckId", "0"));
                entity.add(new BasicNameValuePair("CurrentDate", plotActivitySaveModel.getCurrentDate()));
                entity.add(new BasicNameValuePair("IMINO", new GetDeviceImei(getApplicationContext()).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("SURTYPE", plotActivitySaveModel.getSurveyType()));
                entity.add(new BasicNameValuePair("OLDSEAS", plotActivitySaveModel.getOldSeason()));
                entity.add(new BasicNameValuePair("OLDGHID", plotActivitySaveModel.getOldGhid()));
                entity.add(new BasicNameValuePair("MEETINGTYPE", plotActivitySaveModel.getMeetingType()));
                entity.add(new BasicNameValuePair("MEETINGNAME", plotActivitySaveModel.getMeetingName()));
                entity.add(new BasicNameValuePair("MEETINGNUMBER", plotActivitySaveModel.getMeetingNumber()));
                entity.add(new BasicNameValuePair("LAT", "" + lat));
                entity.add(new BasicNameValuePair("LNG", "" + lng));
                entity.add(new BasicNameValuePair("ADDRESS", address));

                Log.d("AllData",entity.toString());



                String debugData = new MiscleniousUtil().ListNameValueToString("SAVEPLOTACTIVITY1", entity);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                //{"EXCEPTIONMSG":"","SAVEMSG":"Data Save","ACKID":"0"}
                JSONObject obj = new JSONObject(message);
                if (obj.getString("SAVEMSG").equalsIgnoreCase("Data Save")) {
                    new AlertDialogManager().AlertPopUpFinish(context, "Plot activity saved successfully");
                } else {
                    new AlertDialogManager().showToast((Activity) context, obj.getString("EXCEPTIONMSG"));
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                if (dialog.isShowing())
                    dialog.dismiss();
               // new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }

    void saveMyData() {
        PlotActivitySaveModel model = new PlotActivitySaveModel();
        model.setAffectedCondition("" + selectedAffectedCode);
        model.setImageFormat("" + pictureImagePath);
        model.setActivityMethod("" + activityCode.getSelectedItem().toString());
        plotActivitysaveModelList.add(model);
        Log.d("My Data", "" + plotActivitysaveModelList.get(0).getAffectedCondition());
        System.out.println(plotActivitySaveModel.getActivityMethod());
    }

}
