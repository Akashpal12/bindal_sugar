package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class FieldStaffIndentingCaneArea extends AppCompatActivity {

    String CoordinateArea="0";
    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> loginUserDetailsList;
    List<MasterDropDown> seedSourceList,seedTypeList,irrigationList,supplyModeList,harvestingList,equipmentList,landTypeList,sprayTypeList,
    ploughingTypeList,varietyList,typeOfPlantingList,methodList,cropList,plantationList;

    EditText villageCode,villageName,growerCode,growerName,growerFatherName,plotVillageCode,plotVillageName,mobileNumber,date,
            input_plot_sr_no,manualArea,ind_date;
    Spinner input_seed_source,irrigation,supply,harvesting,equipment,loadType,seedType,sprayType,ploughingType,variety,typeOfPlanting,
    method,crop,plantation;
    TextInputLayout layoutvillageCode,layoutvillageName,layoutgrowerCode,layoutgrowerName,layoutgrowerFatherName,
            layoutplotVillageCode,layoutplotVillageName,layoutAreaMeter,layoutAreaHec,layoutMobileNumber,layoutdate,
    layoutirrigation,layoutsupply,layoutharvesting,layoutequipment,layoutloadType,layoutseedType,layoutsprayType,
    layoutploughingType,layoutvariety,layouttypeOfPlanting,layoutmethod,layoutcrop,layoutplantation,layoutSeedBagQty;

    TextView Corner1,Lat1,Lng1,Distance1,Accuracy1,Corner2,Lat2,Lng2,Distance2,Accuracy2,Corner3,Lat3,Lng3,
            Distance3,Accuracy3,Corner4,Lat4,Lng4,Distance4,Accuracy4;
    int currentDistance=1;
    double currentAccuracy=200000;
    LinearLayout btnLayout;
    RelativeLayout msgLayout;

    AlertDialog dialogPopup;
    EditText AreaMeter,AreaHec,SeedBagQty;
    RelativeLayout rl_master_coordinate;
    TextView t_master_latlng,t_master_acc;
    TextView location_lat,location_lng,location_accuracy;
    double lat,lng;

    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;

    String StrAreaMeter="0.0",StrAreaHec="0.0",StrEastNorthLat="0.0",StrEastNorthLng="0.0",StrEastNorthDistance="0.0",
            StrEastNorthAccuracy="0.0",StrWestNorthLat="0.0",StrWestNorthLng="0.0",StrWestNorthDistance="0.0",StrWestNorthAccuracy="0.0",
            StrEastSouthLat="0.0",StrEastSouthLng="0.0",StrEastSouthDistance="0.0",StrEastSouthAccuracy="",StrWestSouthLat="0.0",
            StrWestSouthLng="0.0",StrWestSouthDistance="0.0",StrWestSouthAccuracy="0.0";

    double Lat,Lng,Accuracy,LastLat=0,LastLng=0,dist=0;

    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;
    boolean checkPlot=false;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    AlertDialog Alertdialog;

    Context context;

    InputFilter filter = new InputFilter() {
        final int maxDigitsBeforeDecimalPoint=1;
        final int maxDigitsAfterDecimalPoint=3;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source
                    .subSequence(start, end).toString());
            if (!builder.toString().matches(
                    "(([0-4]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

            )) {
                if(source.length()==0)
                    return dest.subSequence(dstart, dend);
                return "";
            }

            return null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indenting_cane_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=FieldStaffIndentingCaneArea.this;
        setTitle(getString(R.string.MENU_INDENTING_CANE_AREA));
        toolbar.setTitle(getString(R.string.MENU_INDENTING_CANE_AREA));
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

        try {
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();

            loginUserDetailsList = new ArrayList<>();
            loginUserDetailsList = dbh.getUserDetailsModel();
            seedSourceList = new ArrayList<>();
            irrigationList = new ArrayList<>();
            supplyModeList = new ArrayList<>();
            harvestingList = new ArrayList<>();
            equipmentList = new ArrayList<>();
            landTypeList = new ArrayList<>();
            seedTypeList = new ArrayList<>();
            sprayTypeList = new ArrayList<>();
            ploughingTypeList = new ArrayList<>();

            layoutvillageCode = findViewById(R.id.input_layout_village_code);
            layoutvillageName = findViewById(R.id.input_layout_village_name);
            layoutgrowerCode = findViewById(R.id.input_layout_grower_code);
            layoutgrowerName = findViewById(R.id.input_layout_grower_name);
            layoutgrowerFatherName = findViewById(R.id.input_layout_grower_father);
            layoutplotVillageCode = findViewById(R.id.input_layout_plot_village_code);
            layoutplotVillageName = findViewById(R.id.input_layout_plot_village_name);
            layoutAreaMeter = findViewById(R.id.input_layout_area_meter);
            layoutAreaHec = findViewById(R.id.input_layout_area_hec);
            layoutSeedBagQty = findViewById(R.id.input_layout_seed_bag_qty);
            layoutMobileNumber = findViewById(R.id.input_layout_mobile_number);
            layoutdate = findViewById(R.id.input_layout_date);
            manualArea = findViewById(R.id.manual_area);
            manualArea.setFilters(new InputFilter[] { filter });


            input_seed_source = findViewById(R.id.input_seed_source);
            layoutirrigation = findViewById(R.id.input_layout_irrigation_model);
            layoutsupply = findViewById(R.id.input_layout_supply);
            layoutharvesting = findViewById(R.id.input_layout_harvesting);
            layoutequipment = findViewById(R.id.input_layout_equipment);
            layoutloadType = findViewById(R.id.input_layout_load_type);
            layoutseedType = findViewById(R.id.input_layout_seed_type);
            layoutsprayType = findViewById(R.id.input_layout_spray_type);
            layoutploughingType = findViewById(R.id.input_layout_ploughing_type);
            layoutvariety = findViewById(R.id.input_layout_variety);
            layouttypeOfPlanting = findViewById(R.id.input_layout_type_of_planting);
            layoutmethod = findViewById(R.id.input_layout_method);
            layoutcrop = findViewById(R.id.input_layout_crop);
            layoutplantation = findViewById(R.id.input_layout_plantation);

            villageCode = findViewById(R.id.input_village_code);
            villageName = findViewById(R.id.input_village_name);
            growerCode = findViewById(R.id.input_grower_code);
            growerName = findViewById(R.id.input_grower_name);
            growerFatherName = findViewById(R.id.input_grower_father);
            plotVillageCode = findViewById(R.id.input_plot_village_code);
            plotVillageName = findViewById(R.id.input_plot_village_name);
            AreaMeter = findViewById(R.id.area_meter);
            AreaHec = findViewById(R.id.area_hec);
            SeedBagQty = findViewById(R.id.seed_bag_qty);
            mobileNumber = findViewById(R.id.mobile_number);
            date = findViewById(R.id.date);


            irrigation = findViewById(R.id.input_irrigation_model);
            supply = findViewById(R.id.input_supply);
            harvesting = findViewById(R.id.input_harvesting);
            equipment = findViewById(R.id.input_equipment);
            loadType = findViewById(R.id.input_load_type);
            seedType = findViewById(R.id.input_seed_type);
            sprayType = findViewById(R.id.input_spray_type);
            ploughingType = findViewById(R.id.input_ploughing_type);
            variety = findViewById(R.id.input_variety);
            typeOfPlanting = findViewById(R.id.input_type_of_planting);
            method = findViewById(R.id.input_method);
            crop = findViewById(R.id.input_crop);
            plantation = findViewById(R.id.input_plantation);
            input_plot_sr_no = findViewById(R.id.input_plot_sr_no);


            Corner1 = findViewById(R.id.t_corner_1_name);
            Lat1 = findViewById(R.id.t_corner_1_lat);
            Lng1 = findViewById(R.id.t_corner_1_lng);
            Distance1 = findViewById(R.id.t_corner_1_distance);
            Accuracy1 = findViewById(R.id.t_corner_1_accuracy);
            Corner2 = findViewById(R.id.t_corner_2_name);
            Lat2 = findViewById(R.id.t_corner_2_lat);
            Lng2 = findViewById(R.id.t_corner_2_lng);
            Distance2 = findViewById(R.id.t_corner_2_distance);
            Accuracy2 = findViewById(R.id.t_corner_2_accuracy);
            Corner3 = findViewById(R.id.t_corner_3_name);
            Lat3 = findViewById(R.id.t_corner_3_lat);
            Lng3 = findViewById(R.id.t_corner_3_lng);
            Distance3 = findViewById(R.id.t_corner_3_distance);
            Accuracy3 = findViewById(R.id.t_corner_3_accuracy);
            Corner4 = findViewById(R.id.t_corner_4_name);
            Lat4 = findViewById(R.id.t_corner_4_lat);
            Lng4 = findViewById(R.id.t_corner_4_lng);
            Distance4 = findViewById(R.id.t_corner_4_distance);
            Accuracy4 = findViewById(R.id.t_corner_4_accuracy);
            image = findViewById(R.id.image);
            Corner1.setText("");
            Corner2.setText("");
            Corner3.setText("");
            Corner4.setText("");
            //new getWarehouseList().execute();
            ind_date=findViewById(R.id.ind_date);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            init();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            ind_date.setText(currentDt);
            ind_date.setInputType(InputType.TYPE_NULL);
            ind_date.setTextIsSelectable(true);
            ind_date.setFocusable(false);
            ind_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(FieldStaffIndentingCaneArea.this, new DatePickerDialog.OnDateSetListener() {
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
                            ind_date.setText(year + "-" + temmonth + "-" + temDate);

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            date.setInputType(InputType.TYPE_NULL);
            date.setTextIsSelectable(true);
            date.setFocusable(false);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(FieldStaffIndentingCaneArea.this, new DatePickerDialog.OnDateSetListener() {
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
                            date.setText(year + "-" + temmonth + "-" + temDate);

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            setPermission();
        }
        catch(Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }




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

            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            Location location = locationResult.getLastLocation();
                            //t_master_latlng.setText(""+locationResult.getLastLocation().getAccuracy());
                            /*DecimalFormat decimalFormat = new DecimalFormat("##.00");
                            Lat = location.getLatitude();
                            Lng = location.getLongitude();
                            Location currentLocation = new Location("");
                            currentLocation.setLatitude(Lat);
                            currentLocation.setLongitude(Lng);
                            Accuracy = location.getAccuracy();*/
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            Lat = location.getLatitude();
                            Lng = location.getLongitude();
                            Location currentLocation = new Location("");
                            currentLocation.setLatitude(Lat);
                            currentLocation.setLongitude(Lng);
                            Accuracy = location.getAccuracy();
                            setLatDialogue("" + Lat, "" + Lng, "" + Accuracy);
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

    public void exit(View v)
    {
        finish();
    }

    private void init()
    {
        try {
            villageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (villageCode.getText().toString().length() > 0) {
                        List<VillageModal> villageModalList = dbh.getVillageModal(villageCode.getText().toString());
                        if (villageModalList.size() > 0) {
                            villageCode.setText(villageModalList.get(0).getCode());
                            villageName.setText(villageModalList.get(0).getName());
                        } else {
                            villageCode.setText("");
                            villageName.setText("");
                            AlertPopUp("Please enter valid village code");
                        }

                    }
                }
            });
            growerCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (villageCode.getText().toString().length() > 0) {
                        if (growerCode.getText().toString().length() > 0) {
                            checkPlot = false;
                            if(growerCode.getText().toString().equals("0"))
                            {
                                growerName.setFocusable(true);
                                growerName.setTextIsSelectable(true);
                                growerName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                growerFatherName.setFocusable(true);
                                growerFatherName.setTextIsSelectable(true);
                                growerFatherName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            }
                            else
                            {
                                growerName.setFocusable(false);
                                growerName.setTextIsSelectable(false);
                                growerName.setInputType(InputType.TYPE_NULL);
                                growerFatherName.setFocusable(false);
                                growerFatherName.setTextIsSelectable(false);
                                growerFatherName.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(villageCode.getText().toString(), growerCode.getText().toString());
                                if (growerModelList.size() > 0) {
                                    growerCode.setText(growerModelList.get(0).getGrowerCode());
                                    growerName.setText(growerModelList.get(0).getGrowerName());
                                    growerFatherName.setText(growerModelList.get(0).getGrowerFather());
                                } else {
                                    AlertPopUp("Please enter valid grower code");
                                    growerCode.setText("");
                                    growerName.setText("");
                                    growerFatherName.setText("");
                                }

                            }

                        }
                    } else {
                        AlertPopUp("Please enter village code");
                        villageCode.requestFocus();
                    }
                }
            });
            plotVillageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (plotVillageCode.getText().toString().length() > 0) {
                        checkPlot = false;
                        List<VillageModal> villageModalList = dbh.getVillageModal(villageCode.getText().toString());
                        if (villageModalList.size() > 0) {
                            plotVillageCode.setText(villageModalList.get(0).getCode());
                            plotVillageName.setText(villageModalList.get(0).getName());
                            int plotSrNo=Integer.parseInt(villageModalList.get(0).getMaxIndent());
                            plotSrNo++;
                            input_plot_sr_no.setText(""+plotSrNo);
                        } else {
                            plotVillageCode.setText("");
                            plotVillageName.setText("");
                            input_plot_sr_no.setText("");
                            AlertPopUp("Please enter valid plot village code");
                        }
                    }
                }
            });

        /*
        1-IRRIGATIONLIST
        2-SUPPLYMODELIST
        3-HARVESTINGLIST
        4-EQUIMENTLIST
        5-LANDTYPELIST
        6-SEEDTYPELIST
        7-BASALDOSELIST
        16-SEEDTREATMENTLIST
        8-METHODLIST
        9-SPRAYITEMLIST
        10-SPRAYTYPELIST
        11-PLOUGHINGTYPELIST
        12-VARIETYLIST
        13-PLANTINGTYPELIST
        14-PLANTATIONLIST
        15-CROPLIST
        */
            seedSourceList=dbh.getMasterDropDown("6");
            irrigationList = dbh.getMasterDropDown("1");
            supplyModeList = dbh.getMasterDropDown("2");
            harvestingList = dbh.getMasterDropDown("3");
            equipmentList = dbh.getMasterDropDown("4");
            landTypeList = dbh.getMasterDropDown("5");
            seedTypeList = dbh.getMasterDropDown("17");
            sprayTypeList = dbh.getMasterDropDown("10");
            ploughingTypeList = dbh.getMasterDropDown("19");
            varietyList = dbh.getMasterDropDown("12");
            typeOfPlantingList = dbh.getMasterDropDown("13");
            methodList = dbh.getMasterDropDown("8");
            cropList = dbh.getMasterDropDown("15");
            plantationList = dbh.getMasterDropDown("14");


            ArrayList<String> seedSource = new ArrayList<String>();
            seedSource.add("Seed Source");
            for (int i = 0; i < seedSourceList.size(); i++) {
                seedSource.add(seedSourceList.get(i).getName());
            }
            ArrayAdapter<String> adapterseedSource = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, seedSource);
            input_seed_source.setAdapter(adapterseedSource);

            ArrayList<String> data = new ArrayList<String>();
            data.add("Irrigation");
            for (int i = 0; i < irrigationList.size(); i++) {
                data.add(irrigationList.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, data);
            irrigation.setAdapter(adapter);

            ArrayList<String> datasupply = new ArrayList<String>();
            datasupply.add("Supply");
            for (int i = 0; i < supplyModeList.size(); i++) {
                datasupply.add(supplyModeList.get(i).getName());
            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datasupply);
            supply.setAdapter(adaptersupply);

            ArrayList<String> dataharvesting = new ArrayList<String>();
            dataharvesting.add("Harvesting");
            for (int i = 0; i < harvestingList.size(); i++) {
                dataharvesting.add(harvestingList.get(i).getName());
            }
            ArrayAdapter<String> adapterharvesting = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataharvesting);
            harvesting.setAdapter(adapterharvesting);

            ArrayList<String> dataequipment = new ArrayList<String>();
            dataequipment.add("Equipment");
            for (int i = 0; i < equipmentList.size(); i++) {
                dataequipment.add(equipmentList.get(i).getName());
            }
            ArrayAdapter<String> adapterequipment = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataequipment);
            equipment.setAdapter(adapterequipment);

            ArrayList<String> dataloadType = new ArrayList<String>();
            dataloadType.add("Land Type");
            for (int i = 0; i < landTypeList.size(); i++) {
                dataloadType.add(landTypeList.get(i).getName());
            }
            ArrayAdapter<String> adapterloadType = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataloadType);
            loadType.setAdapter(adapterloadType);

            ArrayList<String> dataseedType = new ArrayList<String>();
            dataseedType.add("Seed Type");
            for (int i = 0; i < seedTypeList.size(); i++) {
                dataseedType.add(seedTypeList.get(i).getName());
            }
            ArrayAdapter<String> adapterseedType = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataseedType);
            seedType.setAdapter(adapterseedType);


            ArrayList<String> datasprayType = new ArrayList<String>();
            datasprayType.add("Spray Type"); //Spray Type
            for (int i = 0; i < sprayTypeList.size(); i++) {
                datasprayType.add(sprayTypeList.get(i).getName());
            }
            ArrayAdapter<String> adaptersprayType = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datasprayType);
            sprayType.setAdapter(adaptersprayType);

            ArrayList<String> dataploughingType = new ArrayList<String>();
            dataploughingType.add("Select");
            for (int i = 0; i < ploughingTypeList.size(); i++) {
                dataploughingType.add(ploughingTypeList.get(i).getName());
            }
            ArrayAdapter<String> adapterloughingType = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataploughingType);
            ploughingType.setAdapter(adapterloughingType);

            ArrayList<String> datavariety = new ArrayList<String>();
            datavariety.add("Variety");
            for (int i = 0; i < varietyList.size(); i++) {
                datavariety.add(varietyList.get(i).getName());
            }
            ArrayAdapter<String> adaptervariety = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datavariety);
            variety.setAdapter(adaptervariety);

            ArrayList<String> datatypeOfPlanting = new ArrayList<String>();
            datatypeOfPlanting.add("Type of Planting");
            for (int i = 0; i < typeOfPlantingList.size(); i++) {
                datatypeOfPlanting.add(typeOfPlantingList.get(i).getName());
            }
            ArrayAdapter<String> adaptertypeOfPlanting = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datatypeOfPlanting);
            typeOfPlanting.setAdapter(adaptertypeOfPlanting);

            ArrayList<String> datamethod = new ArrayList<String>();
            datamethod.add("Method");
            for (int i = 0; i < methodList.size(); i++) {
                datamethod.add(methodList.get(i).getName());
            }
            ArrayAdapter<String> adaptermethod = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datamethod);
            method.setAdapter(adaptermethod);

            ArrayList<String> datacrop = new ArrayList<String>();
            datacrop.add("Crop");
            for (int i = 0; i < cropList.size(); i++) {
                datacrop.add(cropList.get(i).getName());
            }
            ArrayAdapter<String> adaptercrop = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, datacrop);
            crop.setAdapter(adaptercrop);

            ArrayList<String> dataplantation = new ArrayList<String>();
            dataplantation.add("Plantation");  //Spray Type
            for (int i = 0; i < plantationList.size(); i++) {
                dataplantation.add(plantationList.get(i).getName());
            }
            ArrayAdapter<String> adapterplantation = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, dataplantation);
            plantation.setAdapter(adapterplantation);

        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }

    }

    private void setPermission()
    {
        try {
            if (dbh.getControlStatus("IRRIGATION", "INDENTING").equalsIgnoreCase("0")) {
                layoutirrigation.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SUPPLY MODE", "INDENTING").equalsIgnoreCase("0")) {
                layoutsupply.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("HARVESTING", "INDENTING").equalsIgnoreCase("0")) {
                layoutharvesting.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("EQUIPMENT", "INDENTING").equalsIgnoreCase("0")) {
                layoutequipment.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("LAND TYPE", "INDENTING").equalsIgnoreCase("0")) {
                layoutloadType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SEED TYPE", "INDENTING").equalsIgnoreCase("0")) {
                layoutseedType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SPRAY TYPE", "INDENTING").equalsIgnoreCase("0")) {
                layoutsprayType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SOIL", "INDENTING").equalsIgnoreCase("0")) {
                layoutploughingType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("MAREA", "INDENTING").equalsIgnoreCase("0")) {
                layoutAreaMeter.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SEED BAG QTY", "INDENTING").equalsIgnoreCase("0")) {
                layoutSeedBagQty.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SPRAY TYPE", "INDENTING").equalsIgnoreCase("0")) {
                layoutsprayType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("VARIETY", "INDENTING").equalsIgnoreCase("0")) {
                layoutvariety.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("TYPE OF PLANTING", "INDENTING").equalsIgnoreCase("0")) {
                layouttypeOfPlanting.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("METHOD", "INDENTING").equalsIgnoreCase("0")) {
                layoutmethod.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("CROP", "INDENTING").equalsIgnoreCase("0")) {
                layoutcrop.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("PLANTATION", "INDENTING").equalsIgnoreCase("0")) {
                layoutplantation.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    public void saveData(View v)
    {
        String strIrrigation="0",strsupply="0",strharvesting="0",strequipment="0",strloadType="0",strseedType="0",
                strploughingType="0",strsprayType="0",strvariety="0",strtypeOfPlanting="0",strmethod="0",strcrop="0",strplantation="0";
        try {
            labelsavedata :
            {
                if(villageCode.getText().toString().length()==0)
                {
                    AlertPopUp("Please enter valid village code");
                    break labelsavedata;
                }
                if(growerCode.getText().toString().length()==0)
                {
                    AlertPopUp("Please enter valid grower code");
                    break labelsavedata;
                }
                if(plotVillageCode.getText().toString().length()==0)
                {
                    AlertPopUp("Please enter valid plot village code");
                    break labelsavedata;
                }
                if(input_plot_sr_no.getText().toString().length()==0 && input_plot_sr_no.getText().toString().length()>5)
                {
                    AlertPopUp("Please enter valid plot serial number");
                    break labelsavedata;
                }
                if(input_seed_source.getSelectedItemPosition()==0)//seedSourceList
                {
                    AlertPopUp("Please select seed source");
                    break labelsavedata;
                }
                if(typeOfPlanting.getSelectedItemPosition()==0)
                {
                    AlertPopUp("Please select Planting type");
                    break labelsavedata;
                }


                if (dbh.getControlStatus("IRRIGATION", "INDENTING").equalsIgnoreCase("1")) {
                    if(irrigation.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select irrigation ");
                        break labelsavedata;
                    }
                    else
                    {
                        strIrrigation=irrigationList.get(irrigation.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("SUPPLY MODE", "INDENTING").equalsIgnoreCase("1")) {
                    if(supply.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select supply mode");
                        break labelsavedata;
                    }
                    else
                    {
                        strsupply=supplyModeList.get(supply.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("HARVESTING", "INDENTING").equalsIgnoreCase("1")) {
                    if(harvesting.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select harvesting");
                        break labelsavedata;
                    }
                    else
                    {
                        strharvesting=harvestingList.get(harvesting.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("EQUIPMENT", "INDENTING").equalsIgnoreCase("1")) {
                    if(equipment.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select equipment");
                        layoutequipment.setErrorEnabled(true);
                        layoutharvesting.setErrorEnabled(false);
                        break labelsavedata;
                    }
                    else{
                        strequipment=equipmentList.get(equipment.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("LAND TYPE", "INDENTING").equalsIgnoreCase("1")) {
                    if(loadType.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select land type");
                        break labelsavedata;
                    }
                    else{
                        strloadType=landTypeList.get(loadType.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("SEED TYPE", "INDENTING").equalsIgnoreCase("1")) {
                    if(seedType.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select seed type");
                        break labelsavedata;
                    }
                    else{
                        strseedType=seedTypeList.get(seedType.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("SPRAY TYPE", "INDENTING").equalsIgnoreCase("1")) {
                    if(sprayType.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select spray type");
                        break labelsavedata;
                    }
                    else{
                        strsprayType=sprayTypeList.get(sprayType.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("SOIL", "INDENTING").equalsIgnoreCase("1")) {
                    if(ploughingType.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select ploughing type");
                        break labelsavedata;
                    }
                    else
                    {
                        strploughingType=ploughingTypeList.get(ploughingType.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if(Double.parseDouble(CoordinateArea)<=0 && manualArea.getText().toString().length()==0)
                {
                    AlertPopUp("Please capture area");
                    break labelsavedata;
                }
                /*if(Double.parseDouble(AreaHec.getText().toString())<=0 && Double.parseDouble(manualArea.getText().toString())<=0)
                {
                    AlertPopUp("Area should be greater than 0 ");
                    break labelsavedata;
                }*/
                if (dbh.getControlStatus("SEED BAG QTY", "INDENTING").equalsIgnoreCase("1")) {
                    if(SeedBagQty.getText().toString().length()==0)
                    {
                        AlertPopUp("Please enter seed bag qty");
                        break labelsavedata;
                    }

                }
                if (dbh.getControlStatus("VARIETY", "INDENTING").equalsIgnoreCase("1")) {
                    if(variety.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select variety");
                        break labelsavedata;
                    }
                    else{
                        strvariety=varietyList.get(variety.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("TYPE OF PLANTING", "INDENTING").equalsIgnoreCase("1")) {
                    if(typeOfPlanting.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select type of planting");
                        break labelsavedata;
                    }
                    else{
                        strtypeOfPlanting=typeOfPlantingList.get(typeOfPlanting.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("METHOD", "INDENTING").equalsIgnoreCase("1")) {
                    if(method.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select method");
                        break labelsavedata;
                    }
                    else{
                        strmethod=methodList.get(method.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("CROP", "INDENTING").equalsIgnoreCase("1")) {
                    if(crop.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select seed type");
                        break labelsavedata;
                    }
                    else{
                        strcrop=cropList.get(crop.getSelectedItemPosition() - 1).getCode();
                    }

                }
                if (dbh.getControlStatus("PLANTATION", "INDENTING").equalsIgnoreCase("1")) {
                    if(plantation.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select plantation");
                        break labelsavedata;
                    }
                    else{
                        strplantation=plantationList.get(plantation.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if(Double.parseDouble(CoordinateArea)<=0)
                {
                    if(manualArea.getText().toString().length()==0)
                    {
                        AlertPopUp("Please enter manual area");
                        break labelsavedata;
                    }
                }
                if(mobileNumber.getText().toString().length()!=10)
                {
                    AlertPopUp("Please enter mobile number");
                    break labelsavedata;
                }
                if(date.getText().toString().length()==0)
                {
                    AlertPopUp("Please select date");
                    break labelsavedata;
                }
                /*if(pictureImagePath.length()<10)
                {
                    AlertPopUp("Please capture farmer image");
                    layoutAreaMeter.setErrorEnabled(false);
                    break labelsavedata;
                }*/
                else
                {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    IndentModel indentModel=new IndentModel();
                    indentModel.setVillage(villageCode.getText().toString());
                    indentModel.setGrower(growerCode.getText().toString());
                    indentModel.setGrowerName(growerName.getText().toString());
                    indentModel.setGrowerFather(growerFatherName.getText().toString());
                    indentModel.setPLOTVillage(plotVillageCode.getText().toString());
                    indentModel.setSeedSource(seedSourceList.get(input_seed_source.getSelectedItemPosition()-1).getCode());
                    indentModel.setIrrigationmode(strIrrigation);
                    indentModel.setSupplyMode(strsupply);
                    indentModel.setHarvesting(strharvesting);
                    indentModel.setEquipment(strequipment);
                    indentModel.setLandType(strloadType);
                    indentModel.setSeedType(strseedType);
                    indentModel.setDim4(StrWestNorthDistance);
                    indentModel.setArea(AreaHec.getText().toString());
                    indentModel.setVARIETY(strvariety);
                    indentModel.setSprayType(strsprayType);
                    indentModel.setPloughingType(strploughingType);
                    indentModel.setNOFPLOTS(SeedBagQty.getText().toString());
                    indentModel.setINDAREA(manualArea.getText().toString());
                    if(location==null)
                    {
                        indentModel.setInsertLAT("0.0");
                        indentModel.setInsertLON("0.0");
                    }
                    else
                    {
                        indentModel.setInsertLAT(""+location.getLatitude());
                        indentModel.setInsertLON(""+location.getLongitude());
                    }
                    //indentModel.setImage(filename);
                    indentModel.setImage("");
                    indentModel.setSuperviserCode(loginUserDetailsList.get(0).getCode());
                    indentModel.setDim1(StrEastNorthDistance);
                    indentModel.setDim2(StrEastSouthDistance);
                    indentModel.setDim3(StrWestSouthDistance);
                    indentModel.setDim4(StrWestNorthDistance);
                    indentModel.setArea(AreaHec.getText().toString());
                    indentModel.setLAT1(StrEastNorthLat);
                    indentModel.setLON1(StrEastNorthLng);
                    indentModel.setLAT2(StrEastSouthLat);
                    indentModel.setLON2(StrEastSouthLng);
                    indentModel.setLAT3(StrWestSouthLat);
                    indentModel.setLON3(StrWestSouthLng);
                    indentModel.setLAT4(StrWestNorthLat);
                    indentModel.setLON4(StrWestNorthLng);
                    indentModel.setSprayType(strsprayType);
                    indentModel.setMobilNO(mobileNumber.getText().toString());
                    indentModel.setMDATE(date.getText().toString());
                    indentModel.setCrop(strcrop);
                    indentModel.setPLANTINGTYPE(strtypeOfPlanting);
                    indentModel.setPLANTATION(strplantation);
                    indentModel.setMethod(strmethod);
                   // indentModel.setVARIETYNAME(strvariety_name);
                    indentModel.setVARIETY(strvariety);
                    indentModel.setPlotSerialNumber(input_plot_sr_no.getText().toString());
                    indentModel.setServerStatus("No");
                    CheckBox isideal=findViewById(R.id.isideal);
                    if(isideal.isChecked())
                    {
                        indentModel.setIsIdeal("1");
                    }
                    else
                    {
                        indentModel.setIsIdeal("0");
                    }
                    CheckBox isnursery=findViewById(R.id.isnursery);
                    if(isnursery.isChecked())
                    {
                        indentModel.setIsNursery("1");
                    }
                    else
                    {
                        indentModel.setIsNursery("0");
                    }
                    indentModel.setIndDate(ind_date.getText().toString());
                    dbh.insertIndentModel(indentModel);
                    AlertPopUpFinish("Indenting done");
                    if(new InternetCheck(FieldStaffIndentingCaneArea.this).isOnline())
                    {
                        List<IndentModel> indentModelList=dbh.getIndentModel("No","","","","");
                        if(indentModelList.size()>0)
                        {
                            //new ImageUploadTask().execute();
                        }
                    }
                }
            }
        }
        catch (SecurityException e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    public void openCam(View v)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt=dateFormat.format(today);
            filename = "image"+currentDt+".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/indent");
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
            startActivityForResult(intent,RC_CAMERA_REQUEST);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(FieldStaffIndentingCaneArea.this,"Error:"+e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if(file.exists())
                {
                    /*DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat dateFormatter2 = new SimpleDateFormat("HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    dateFormatter2.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = dateFormatter2.format(today);
                    Bitmap bmp = drawTextToBitmap(FieldStaffIndentingCaneArea.this, d, t);
                    FileOutputStream out = new FileOutputStream(pictureImagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();*/
                    //Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
                    //image.setImageBitmap(bitmap);
                    //File imgFile = new File(UriStr);
                    if(file.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        image.setImageBitmap(myBitmap);
                    };
                }
                else
                {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(FieldStaffIndentingCaneArea.this,"Error:"+e.toString());
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
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
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
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        return null;
    }

    private void getCurrentLocation()
    {
        try {
            final DecimalFormat decimalFormat = new DecimalFormat("##");
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(FieldStaffIndentingCaneArea.this);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_get_latlng, null);
            dialogbilder.setView(mView);
            final Spinner location_direction=mView.findViewById(R.id.location_direction);
            btnLayout=mView.findViewById(R.id.btnLayout);
            msgLayout=mView.findViewById(R.id.rl_msg);
            location_lat=mView.findViewById(R.id.location_lat);
            location_lng=mView.findViewById(R.id.location_lng);
            location_accuracy=mView.findViewById(R.id.location_accuracy);
            ArrayList<String> divData=getDirDrop();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FieldStaffIndentingCaneArea.this,
                    R.layout.list_item, divData);
            location_direction.setAdapter(adapter);
            /*Location curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (curLocation != null) {
                location_lat.setText("" + curLocation.getLatitude());
                location_lng.setText("" + curLocation.getLongitude());
                location_accuracy.setText("" + curLocation.getAccuracy());
            }*/
            location_lat.setText("-----");
            location_lng.setText("-----");
            location_accuracy.setText("-----");
            dialogPopup = dialogbilder.create();
            dialogPopup.show();
            dialogPopup.setCancelable(true);
            dialogPopup.setCanceledOnTouchOutside(true);
            Button btn_ok=mView.findViewById(R.id.btn_ok);
            Button skip_cancel=mView.findViewById(R.id.skip_cancel);
            if(currentDistance==4)
            {
                skip_cancel.setVisibility(View.VISIBLE);
            }
            skip_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Corner4.setText(location_direction.getSelectedItem().toString());
                    Lat4.setText("0.0");
                    Lng4.setText("0.0");
                    Accuracy4.setText("0.0");
                    currentDistance++;
                    calAreaTriangle();
                    dialogPopup.dismiss();
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(currentDistance==1)
                        {
                            Corner1.setText(location_direction.getSelectedItem().toString());
                            Lat1.setText(""+location_lat.getText().toString());
                            Lng1.setText(""+location_lng.getText().toString());
                            Accuracy1.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==2)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            Distance1.setText(""+decimalFormat.format(dist));
                            Corner2.setText(location_direction.getSelectedItem().toString());
                            Lat2.setText(""+location_lat.getText().toString());
                            Lng2.setText(""+location_lng.getText().toString());
                            Accuracy2.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==3)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat2.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng2.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            Distance2.setText(""+decimalFormat.format(dist));
                            Corner3.setText(location_direction.getSelectedItem().toString());
                            Lat3.setText(""+location_lat.getText().toString());
                            Lng3.setText(""+location_lng.getText().toString());
                            Accuracy3.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==4)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat3.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng3.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            Location startL=new Location("");
                            startL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            startL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            double dist1=destinationL.distanceTo(startL);
                            Distance3.setText(""+decimalFormat.format(dist));
                            Distance4.setText(""+decimalFormat.format(dist1));
                            Corner4.setText(location_direction.getSelectedItem().toString());
                            Lat4.setText(""+location_lat.getText().toString());
                            Lng4.setText(""+location_lng.getText().toString());
                            Accuracy4.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                            calArea();
                        }
                    }
                    catch(Exception e)
                    {

                    }
                    dialogPopup.dismiss();
                }
            });
        }
        catch(SecurityException e)
        {

        }

    }

    private ArrayList<String> getDirDrop()
    {

        ArrayList<String> divData=new ArrayList<String>();
        try {
            if (currentDistance == 1) {
                divData.add("East");
                divData.add("West");
                divData.add("North");
                divData.add("South");
            } else if (currentDistance == 2) {
                if (Corner1.getText().toString().equalsIgnoreCase("East")) {
                    divData.add("North");
                    divData.add("South");
                } else if (Corner1.getText().toString().equalsIgnoreCase("West")) {
                    divData.add("North");
                    divData.add("South");
                } else if (Corner1.getText().toString().equalsIgnoreCase("North")) {
                    divData.add("East");
                    divData.add("West");
                } else if (Corner1.getText().toString().equalsIgnoreCase("South")) {
                    divData.add("East");
                    divData.add("West");
                }
            } else if (currentDistance == 3) {
                if (Corner1.getText().toString().equalsIgnoreCase("East")) {
                    divData.add("West");
                } else if (Corner1.getText().toString().equalsIgnoreCase("West")) {
                    divData.add("East");
                } else if (Corner1.getText().toString().equalsIgnoreCase("North")) {
                    divData.add("South");
                } else if (Corner1.getText().toString().equalsIgnoreCase("South")) {
                    divData.add("North");
                }
            } else if (currentDistance == 4) {
                if (Corner2.getText().toString().equalsIgnoreCase("East")) {
                    divData.add("West");
                } else if (Corner2.getText().toString().equalsIgnoreCase("West")) {
                    divData.add("East");
                } else if (Corner2.getText().toString().equalsIgnoreCase("North")) {
                    divData.add("South");
                } else if (Corner2.getText().toString().equalsIgnoreCase("South")) {
                    divData.add("North");
                }
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        return divData;
    }

    public void captureCoordinate(View v)
    {
        findLocation();
        getCurrentLocation();
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("##.00");
                Lat = location.getLatitude();
                Lng = location.getLongitude();
                Location currentLocation = new Location("");
                currentLocation.setLatitude(Lat);
                currentLocation.setLongitude(Lng);
                Accuracy = location.getAccuracy();
                setLatDialogue("" + Lat, "" + Lng, "" + Accuracy);
                /*final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                    }
                }, 5000);*/
            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }

        }

        public void onStatusChanged(String s, int i, Bundle b) {
            //t_master_latlng.setText("Provider status changed");
        }

        public void onProviderDisabled(String s) {
            //t_master_latlng.setText("Provider disabled by the user. GPS turned off");
        }

        public void onProviderEnabled(String s) {
            /*Toast.makeText(FieldStaffIndentingCaneArea.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();*/
        }

    }

    private void findLocation()
    {
        //startLocationUpdates();
        try {
            //startLocationUpdates();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());
        }
        catch (SecurityException e)
        {

        }
        catch (Exception e)
        {

        }
    }


    private void calArea()
    {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("##");
            Location l1 = new Location("");
            l1.setLatitude(Double.parseDouble(Lat1.getText().toString()));
            l1.setLongitude(Double.parseDouble(Lng1.getText().toString()));
            Location l2 = new Location("");
            l2.setLatitude(Double.parseDouble(Lat2.getText().toString()));
            l2.setLongitude(Double.parseDouble(Lng2.getText().toString()));
            Location l3 = new Location("");
            l3.setLatitude(Double.parseDouble(Lat3.getText().toString()));
            l3.setLongitude(Double.parseDouble(Lng3.getText().toString()));
            Location l4 = new Location("");
            l4.setLatitude(Double.parseDouble(Lat4.getText().toString()));
            l4.setLongitude(Double.parseDouble(Lng4.getText().toString()));
            Distance1.setText(decimalFormat.format(l1.distanceTo(l2)));
            Distance2.setText(decimalFormat.format(l2.distanceTo(l3)));
            Distance3.setText(decimalFormat.format(l3.distanceTo(l4)));
            Distance4.setText(decimalFormat.format(l4.distanceTo(l1)));
            if (Corner1.getText().toString().equalsIgnoreCase("East")) {
                if (Corner2.getText().toString().equalsIgnoreCase("North")) {
                    StrEastNorthLat = Lat1.getText().toString();
                    StrEastNorthLng = Lng1.getText().toString();
                    StrWestNorthLat = Lat2.getText().toString();
                    StrWestNorthLng = Lng2.getText().toString();
                    StrWestSouthLat = Lat3.getText().toString();
                    StrWestSouthLng = Lng3.getText().toString();
                    StrEastSouthLat = Lat4.getText().toString();
                    StrEastSouthLng = Lng4.getText().toString();
                    StrEastNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestSouthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrEastSouthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrEastNorthAccuracy = Accuracy1.getText().toString();
                    StrWestNorthAccuracy = Accuracy2.getText().toString();
                    StrWestSouthAccuracy = Accuracy3.getText().toString();
                    StrEastSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrEastSouthLat = Lat1.getText().toString();
                    StrEastSouthLng = Lng1.getText().toString();
                    StrWestSouthLat = Lat2.getText().toString();
                    StrWestSouthLng = Lng2.getText().toString();
                    StrWestNorthLat = Lat3.getText().toString();
                    StrWestNorthLng = Lng3.getText().toString();
                    StrEastNorthLat = Lat4.getText().toString();
                    StrEastNorthLng = Lng4.getText().toString();
                    StrEastSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestNorthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrEastNorthDistance = decimalFormat.format(l4.distanceTo(l1));

                    StrEastSouthAccuracy = Accuracy1.getText().toString();
                    StrWestSouthAccuracy = Accuracy2.getText().toString();
                    StrWestNorthAccuracy = Accuracy3.getText().toString();
                    StrEastNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("West")) {
                if (Corner2.getText().toString().equalsIgnoreCase("North")) {
                    StrWestNorthLat = Lat1.getText().toString();
                    StrWestNorthLng = Lng1.getText().toString();
                    StrEastNorthLat = Lat2.getText().toString();
                    StrEastNorthLng = Lng2.getText().toString();
                    StrEastSouthLat = Lat3.getText().toString();
                    StrEastSouthLng = Lng3.getText().toString();
                    StrWestSouthLat = Lat4.getText().toString();
                    StrWestSouthLng = Lng4.getText().toString();
                    StrWestNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastSouthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrWestSouthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrWestNorthAccuracy = Accuracy1.getText().toString();
                    StrEastNorthAccuracy = Accuracy2.getText().toString();
                    StrEastSouthAccuracy = Accuracy3.getText().toString();
                    StrWestSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrWestSouthLat = Lat1.getText().toString();
                    StrWestSouthLng = Lng1.getText().toString();
                    StrEastSouthLat = Lat2.getText().toString();
                    StrEastSouthLng = Lng2.getText().toString();
                    StrEastNorthLat = Lat3.getText().toString();
                    StrEastNorthLng = Lng3.getText().toString();
                    StrWestNorthLat = Lat4.getText().toString();
                    StrWestNorthLng = Lng4.getText().toString();
                    StrWestSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastNorthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrWestNorthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrWestSouthAccuracy = Accuracy1.getText().toString();
                    StrEastSouthAccuracy = Accuracy2.getText().toString();
                    StrEastNorthAccuracy = Accuracy3.getText().toString();
                    StrWestNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("North")) {
                if (Corner2.getText().toString().equalsIgnoreCase("East")) {
                    StrEastNorthLat = Lat1.getText().toString();
                    StrEastNorthLng = Lng1.getText().toString();
                    StrEastSouthLat = Lat2.getText().toString();
                    StrEastSouthLng = Lng2.getText().toString();
                    StrWestSouthLat = Lat3.getText().toString();
                    StrWestSouthLng = Lng3.getText().toString();
                    StrWestNorthLat = Lat4.getText().toString();
                    StrWestNorthLng = Lng4.getText().toString();
                    StrEastNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestSouthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrWestNorthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrEastNorthAccuracy = Accuracy1.getText().toString();
                    StrEastSouthAccuracy = Accuracy2.getText().toString();
                    StrWestSouthAccuracy = Accuracy3.getText().toString();
                    StrWestNorthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrWestNorthLat = Lat1.getText().toString();
                    StrWestNorthLng = Lng1.getText().toString();
                    StrWestSouthLat = Lat2.getText().toString();
                    StrWestSouthLng = Lng2.getText().toString();
                    StrEastSouthLat = Lat3.getText().toString();
                    StrEastSouthLng = Lng3.getText().toString();
                    StrEastNorthLat = Lat4.getText().toString();
                    StrEastNorthLng = Lng4.getText().toString();
                    StrWestNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastSouthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrEastNorthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrWestNorthAccuracy = Accuracy1.getText().toString();
                    StrWestSouthAccuracy = Accuracy2.getText().toString();
                    StrEastSouthAccuracy = Accuracy3.getText().toString();
                    StrEastNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("South")) {
                if (Corner2.getText().toString().equalsIgnoreCase("West")) {
                    StrWestSouthLat = Lat1.getText().toString();
                    StrWestSouthLng = Lng1.getText().toString();
                    StrWestNorthLat = Lat2.getText().toString();
                    StrWestNorthLng = Lng2.getText().toString();
                    StrEastNorthLat = Lat3.getText().toString();
                    StrEastNorthLng = Lng3.getText().toString();
                    StrEastSouthLat = Lat4.getText().toString();
                    StrEastSouthLng = Lng4.getText().toString();
                    StrWestSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastNorthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrEastSouthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrWestSouthAccuracy = Accuracy1.getText().toString();
                    StrWestNorthAccuracy = Accuracy2.getText().toString();
                    StrEastNorthAccuracy = Accuracy3.getText().toString();
                    StrEastSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrEastSouthLat = Lat1.getText().toString();
                    StrEastSouthLng = Lng1.getText().toString();
                    StrEastNorthLat = Lat2.getText().toString();
                    StrEastNorthLng = Lng2.getText().toString();
                    StrWestNorthLat = Lat3.getText().toString();
                    StrWestNorthLng = Lng3.getText().toString();
                    StrWestSouthLat = Lat4.getText().toString();
                    StrWestSouthLng = Lng4.getText().toString();
                    StrEastSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestNorthDistance = decimalFormat.format(l3.distanceTo(l4));
                    StrWestSouthDistance = decimalFormat.format(l4.distanceTo(l1));
                    StrEastSouthAccuracy = Accuracy1.getText().toString();
                    StrEastNorthAccuracy = Accuracy2.getText().toString();
                    StrWestNorthAccuracy = Accuracy3.getText().toString();
                    StrWestSouthAccuracy = Accuracy4.getText().toString();
                }
            }
            double eastWestLength = (Double.parseDouble(StrEastNorthDistance) + Double.parseDouble(StrWestSouthDistance)) / 2;
            double northSouthLength = (Double.parseDouble(StrWestNorthDistance) + Double.parseDouble(StrEastSouthDistance)) / 2;

            DecimalFormat decimalHecFormat = new DecimalFormat("##.000");
            double areaM = eastWestLength * northSouthLength;

            AreaMeter.setText(decimalFormat.format(areaM));
            double ah = areaM / 10000;
            AreaHec.setText("" + decimalHecFormat.format(ah));
            StrAreaMeter = decimalFormat.format(areaM);
            //StrAreaHec=String.format("%f", ah);
            StrAreaHec = decimalHecFormat.format(ah);
            currentDistance++;
            currentAccuracy = 20000;
            CoordinateArea = decimalFormat.format(areaM);
            //AreaMeter.setText("0");
        }
        catch(Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }


    private void calAreaTriangle()
    {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("##");
            Location l1 = new Location("");
            l1.setLatitude(Double.parseDouble(Lat1.getText().toString()));
            l1.setLongitude(Double.parseDouble(Lng1.getText().toString()));
            Location l2 = new Location("");
            l2.setLatitude(Double.parseDouble(Lat2.getText().toString()));
            l2.setLongitude(Double.parseDouble(Lng2.getText().toString()));
            Location l3 = new Location("");
            l3.setLatitude(Double.parseDouble(Lat3.getText().toString()));
            l3.setLongitude(Double.parseDouble(Lng3.getText().toString()));
            Distance1.setText(decimalFormat.format(l1.distanceTo(l2)));
            Distance2.setText(decimalFormat.format(l2.distanceTo(l3)));
            Distance3.setText(decimalFormat.format(l3.distanceTo(l1)));
            if (Corner1.getText().toString().equalsIgnoreCase("East")) {
                if (Corner2.getText().toString().equalsIgnoreCase("North")) {
                    StrEastNorthLat = Lat1.getText().toString();
                    StrEastNorthLng = Lng1.getText().toString();
                    StrWestNorthLat = Lat2.getText().toString();
                    StrWestNorthLng = Lng2.getText().toString();
                    StrWestSouthLat = Lat3.getText().toString();
                    StrWestSouthLng = Lng3.getText().toString();
                    StrEastSouthLat = Lat4.getText().toString();
                    StrEastSouthLng = Lng4.getText().toString();
                    StrEastNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestSouthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrEastSouthDistance = "0";
                    StrEastNorthAccuracy = Accuracy1.getText().toString();
                    StrWestNorthAccuracy = Accuracy2.getText().toString();
                    StrWestSouthAccuracy = Accuracy3.getText().toString();
                    StrEastSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrEastSouthLat = Lat1.getText().toString();
                    StrEastSouthLng = Lng1.getText().toString();
                    StrWestSouthLat = Lat2.getText().toString();
                    StrWestSouthLng = Lng2.getText().toString();
                    StrWestNorthLat = Lat3.getText().toString();
                    StrWestNorthLng = Lng3.getText().toString();
                    StrEastNorthLat = Lat4.getText().toString();
                    StrEastNorthLng = Lng4.getText().toString();
                    StrEastSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestNorthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrEastNorthDistance = "0";
                    StrEastSouthAccuracy = Accuracy1.getText().toString();
                    StrWestSouthAccuracy = Accuracy2.getText().toString();
                    StrWestNorthAccuracy = Accuracy3.getText().toString();
                    StrEastNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("West")) {
                if (Corner2.getText().toString().equalsIgnoreCase("North")) {
                    StrWestNorthLat = Lat1.getText().toString();
                    StrWestNorthLng = Lng1.getText().toString();
                    StrEastNorthLat = Lat2.getText().toString();
                    StrEastNorthLng = Lng2.getText().toString();
                    StrEastSouthLat = Lat3.getText().toString();
                    StrEastSouthLng = Lng3.getText().toString();
                    StrWestSouthLat = Lat4.getText().toString();
                    StrWestSouthLng = Lng4.getText().toString();
                    StrWestNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastSouthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrWestSouthDistance = "0";
                    StrWestNorthAccuracy = Accuracy1.getText().toString();
                    StrEastNorthAccuracy = Accuracy2.getText().toString();
                    StrEastSouthAccuracy = Accuracy3.getText().toString();
                    StrWestSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrWestSouthLat = Lat1.getText().toString();
                    StrWestSouthLng = Lng1.getText().toString();
                    StrEastSouthLat = Lat2.getText().toString();
                    StrEastSouthLng = Lng2.getText().toString();
                    StrEastNorthLat = Lat3.getText().toString();
                    StrEastNorthLng = Lng3.getText().toString();
                    StrWestNorthLat = Lat4.getText().toString();
                    StrWestNorthLng = Lng4.getText().toString();
                    StrWestSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastNorthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrWestNorthDistance = "0";
                    StrWestSouthAccuracy = Accuracy1.getText().toString();
                    StrEastSouthAccuracy = Accuracy2.getText().toString();
                    StrEastNorthAccuracy = Accuracy3.getText().toString();
                    StrWestNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("North")) {
                if (Corner2.getText().toString().equalsIgnoreCase("East")) {
                    StrEastNorthLat = Lat1.getText().toString();
                    StrEastNorthLng = Lng1.getText().toString();
                    StrEastSouthLat = Lat2.getText().toString();
                    StrEastSouthLng = Lng2.getText().toString();
                    StrWestSouthLat = Lat3.getText().toString();
                    StrWestSouthLng = Lng3.getText().toString();
                    StrWestNorthLat = Lat4.getText().toString();
                    StrWestNorthLng = Lng4.getText().toString();
                    StrEastNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestSouthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrWestNorthDistance = "0";
                    StrEastNorthAccuracy = Accuracy1.getText().toString();
                    StrEastSouthAccuracy = Accuracy2.getText().toString();
                    StrWestSouthAccuracy = Accuracy3.getText().toString();
                    StrWestNorthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrWestNorthLat = Lat1.getText().toString();
                    StrWestNorthLng = Lng1.getText().toString();
                    StrWestSouthLat = Lat2.getText().toString();
                    StrWestSouthLng = Lng2.getText().toString();
                    StrEastSouthLat = Lat3.getText().toString();
                    StrEastSouthLng = Lng3.getText().toString();
                    StrEastNorthLat = Lat4.getText().toString();
                    StrEastNorthLng = Lng4.getText().toString();
                    StrWestNorthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestSouthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastSouthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrEastNorthDistance = "0";
                    StrWestNorthAccuracy = Accuracy1.getText().toString();
                    StrWestSouthAccuracy = Accuracy2.getText().toString();
                    StrEastSouthAccuracy = Accuracy3.getText().toString();
                    StrEastNorthAccuracy = Accuracy4.getText().toString();
                }
            }
            if (Corner1.getText().toString().equalsIgnoreCase("South")) {
                if (Corner2.getText().toString().equalsIgnoreCase("West")) {
                    StrWestSouthLat = Lat1.getText().toString();
                    StrWestSouthLng = Lng1.getText().toString();
                    StrWestNorthLat = Lat2.getText().toString();
                    StrWestNorthLng = Lng2.getText().toString();
                    StrEastNorthLat = Lat3.getText().toString();
                    StrEastNorthLng = Lng3.getText().toString();
                    StrEastSouthLat = Lat4.getText().toString();
                    StrEastSouthLng = Lng4.getText().toString();
                    StrWestSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrWestNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrEastNorthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrEastSouthDistance = "0";
                    StrWestSouthAccuracy = Accuracy1.getText().toString();
                    StrWestNorthAccuracy = Accuracy2.getText().toString();
                    StrEastNorthAccuracy = Accuracy3.getText().toString();
                    StrEastSouthAccuracy = Accuracy4.getText().toString();
                } else {
                    StrEastSouthLat = Lat1.getText().toString();
                    StrEastSouthLng = Lng1.getText().toString();
                    StrEastNorthLat = Lat2.getText().toString();
                    StrEastNorthLng = Lng2.getText().toString();
                    StrWestNorthLat = Lat3.getText().toString();
                    StrWestNorthLng = Lng3.getText().toString();
                    StrWestSouthLat = Lat4.getText().toString();
                    StrWestSouthLng = Lng4.getText().toString();
                    StrEastSouthDistance = decimalFormat.format(l1.distanceTo(l2));
                    StrEastNorthDistance = decimalFormat.format(l2.distanceTo(l3));
                    StrWestNorthDistance = decimalFormat.format(l3.distanceTo(l1));
                    StrWestSouthDistance = "0";
                    StrEastSouthAccuracy = Accuracy1.getText().toString();
                    StrEastNorthAccuracy = Accuracy2.getText().toString();
                    StrWestNorthAccuracy = Accuracy3.getText().toString();
                    StrWestSouthAccuracy = Accuracy4.getText().toString();
                }
            }
            double eastWestLength = (Double.parseDouble(StrEastNorthDistance) + Double.parseDouble(StrWestSouthDistance)) / 2;
            double northSouthLength = (Double.parseDouble(StrWestNorthDistance) + Double.parseDouble(StrEastSouthDistance)) / 2;
            /*Location l1=new Location("");
            l1.setLatitude(Double.parseDouble("26.89729784"));
            l1.setLongitude(Double.parseDouble("80.99182373"));
            locationList.add(l1);
            Location l2=new Location("");
            l2.setLatitude(Double.parseDouble("26.89729434"));
            l2.setLongitude(Double.parseDouble("80.99170835"));
            locationList.add(l2);
            Location l3=new Location("");
            l3.setLatitude(Double.parseDouble("26.89740148"));
            l3.setLongitude(Double.parseDouble("80.99174044"));
            locationList.add(l3);
            Location l4=new Location("");
            l4.setLatitude(Double.parseDouble("26.8973886"));
            l4.setLongitude(Double.parseDouble("80.99176205"));
            locationList.add(l4);*/
            double areaM = eastWestLength * northSouthLength;
            /*Double areaM=Double.parseDouble(Distance1.getText().toString())*Double.parseDouble(Distance2.getText().toString())*
                Double.parseDouble(Distance3.getText().toString())*Double.parseDouble(Distance4.getText().toString());*/
            AreaMeter.setText(decimalFormat.format(areaM));
            double ah = Double.parseDouble(decimalFormat.format(areaM)) / 10000;
            AreaHec.setText("" + ah);
            StrAreaMeter = decimalFormat.format(areaM);
            StrAreaHec = "" + ah;
            currentDistance++;
            currentAccuracy = 20000;
            CoordinateArea = decimalFormat.format(areaM);

        }
        catch(Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    public void mapViewPlot(View v)
    {
        if(Double.parseDouble(StrEastNorthLat)>0)
        {
            if(Double.parseDouble(StrEastSouthLat)>0)
            {
                if(Double.parseDouble(StrWestSouthLat)>0)
                {
                    if(Double.parseDouble(StrWestNorthLat)>0)
                    {
                        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_plot_draw_on_map, null);
                        dialogbilder.setView(mView);
                        Alertdialog = dialogbilder.create();
                        GoogleMap googleMap;
                        //holder.ll.removeAllViewsInLayout();
                        MapView mMapView = (MapView) mView.findViewById(R.id.mapView);
                        MapsInitializer.initialize(context);

                        mMapView = (MapView) mView.findViewById(R.id.mapView);
                        mMapView.onCreate(Alertdialog.onSaveInstanceState());
                        mMapView.onResume();// needed to get the map to display immediately
                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap googleMap) {
                                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                                ArrayList<LatLng> latLngs=new ArrayList<>();
                                latLngs.add(new LatLng(Double.parseDouble(StrEastNorthLat), Double.parseDouble(StrEastNorthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrEastSouthLat), Double.parseDouble(StrEastSouthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrWestSouthLat), Double.parseDouble(StrWestSouthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrWestNorthLat), Double.parseDouble(StrWestNorthLng)));
                                LatLng midPoint=new LatLngUtil().getPolygonCenterPoint(latLngs);
                                Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                                        .add(
                                                new LatLng(Double.parseDouble(StrEastNorthLat), Double.parseDouble(StrEastNorthLng)),
                                                new LatLng(Double.parseDouble(StrEastSouthLat), Double.parseDouble(StrEastSouthLng)),
                                                new LatLng(Double.parseDouble(StrWestSouthLat), Double.parseDouble(StrWestSouthLng)),
                                                new LatLng(Double.parseDouble(StrWestNorthLat), Double.parseDouble(StrWestNorthLng))
                                        )
                                        .strokeColor(Color.parseColor("#000000"))
                                        .strokeWidth(1)
                                        .fillColor(Color.parseColor("#8E4CAF50")));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midPoint, 16.0f));
                            }
                        });

                        Button closeMap=mView.findViewById(R.id.closeMap);
                        closeMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alertdialog.dismiss();
                            }
                        });

                        Alertdialog.show();
                        Alertdialog.setCancelable(false);
                        Alertdialog.setCanceledOnTouchOutside(true);
                        LatLng latLng=new LatLng(lat,lng);

                    }
                    else {
                        new AlertDialogManager().RedDialog(context,"1th lat lng not find");
                    }
                }
                else {
                    new AlertDialogManager().RedDialog(context,"2rd lat lng not find");
                }
            }
            else {
                new AlertDialogManager().RedDialog(context,"3nd lat lng not find");
            }
        }
        else {
            new AlertDialogManager().RedDialog(context,"4st lat lng not find");
        }

    }

    public void clearCoordinate(View v)
    {
        //findLocation();
        LastLat=0;
        LastLng=0;
        currentDistance=1;
        currentAccuracy=20000;
        Corner1.setText("");
        Corner2.setText("");
        Corner3.setText("");
        Corner4.setText("");
        Lat1.setText("");
        Lat2.setText("");
        Lat3.setText("");
        Lat4.setText("");
        Lng1.setText("");
        Lng2.setText("");
        Lng3.setText("");
        Lng4.setText("");
        Distance1.setText("");
        Distance2.setText("");
        Distance3.setText("");
        Distance4.setText("");
        Accuracy1.setText("");
        Accuracy2.setText("");
        Accuracy3.setText("");
        Accuracy4.setText("");
        AreaMeter.setText("");
        AreaHec.setText("");
        CoordinateArea="0";
        AreaMeter.setText("0");
        AreaHec.setText("");
    }


    private void setLatDialogue(String lt, String ln, String acr)
    {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            Lat = Double.parseDouble(lt);
            Lng = Double.parseDouble(ln);
            Location currentLocation = new Location("");
            currentLocation.setLatitude(Lat);
            currentLocation.setLongitude(Lng);
            Accuracy = Double.parseDouble(acr);
            dist = 0;
            if (LastLat > 0 && LastLng > 0) {
                Location l1 = new Location("");
                l1.setLatitude(LastLat);
                l1.setLongitude(LastLng);
                dist = l1.distanceTo(currentLocation);
                //dist=getDistance(LastLat,LastLng,Lat,Lng);
            }
            if (location_lat != null) {
                location_lat.setText("" + Lat);
                location_lng.setText("" + Lng);
                location_accuracy.setText(decimalFormat.format(Accuracy) + " M");
            } else {
                location_lat.setText("---");
                location_lng.setText("---");
                location_accuracy.setText("---");
            }
            msgLayout.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    class ImageUploadTask extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(FieldStaffIndentingCaneArea.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(FieldStaffIndentingCaneArea.this, "Uploading",
                    "Please wait while we transfer your file to server", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<IndentModel> indentModelList=dbh.getIndentModel("No","","","","");
                IndentModel indentModel=indentModelList.get(0);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/indent");
                String imgPath=dir.getAbsolutePath()+"/"+indentModel.getImage();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SAVEINDENTINGOFFLINE";
                //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                //int i = Integer.parseInt(params[0]);
                Bitmap bitmap = ShrinkBitmap(imgPath, 1500, 1200);//decodeFile(params[0]);
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currentDt=dateFormat.format(today);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt= Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                entity.add(new BasicNameValuePair("Village",indentModel.getVillage()));
                entity.add(new BasicNameValuePair("Grower",indentModel.getGrower()));
                entity.add(new BasicNameValuePair("PLOTVillage",indentModel.getPLOTVillage()));
                entity.add(new BasicNameValuePair("Irrigationmode",indentModel.getIrrigationmode()));
                entity.add(new BasicNameValuePair("SupplyMode",indentModel.getSupplyMode()));
                entity.add(new BasicNameValuePair("Harvesting",indentModel.getHarvesting()));
                entity.add(new BasicNameValuePair("Equipment",indentModel.getEquipment()));
                entity.add(new BasicNameValuePair("LandType",indentModel.getLandType()));
                entity.add(new BasicNameValuePair("SeedType",indentModel.getSeedType()));
                entity.add(new BasicNameValuePair("SprayType",indentModel.getSprayType()));
                entity.add(new BasicNameValuePair("PloughingType",indentModel.getPloughingType()));
                entity.add(new BasicNameValuePair("NOFPLOTS",indentModel.getNOFPLOTS()));
                entity.add(new BasicNameValuePair("INDAREA",indentModel.getINDAREA()));
                entity.add(new BasicNameValuePair("InsertLAT",indentModel.getInsertLAT()));
                entity.add(new BasicNameValuePair("InsertLON",indentModel.getInsertLON()));
                entity.add(new BasicNameValuePair("MobilNO",indentModel.getMobilNO()));
                entity.add(new BasicNameValuePair("MDATE",indentModel.getMDATE()));
                entity.add(new BasicNameValuePair("VARIETY",indentModel.getVARIETY()));
                entity.add(new BasicNameValuePair("PLANTINGTYPE",indentModel.getPLANTINGTYPE()));
                entity.add(new BasicNameValuePair("SprayType",indentModel.getSprayType()));
                entity.add(new BasicNameValuePair("Crop",indentModel.getCrop()));
                entity.add(new BasicNameValuePair("PLANTATION",indentModel.getPLANTATION()));
                entity.add(new BasicNameValuePair("Dim1",indentModel.getDim1()));
                entity.add(new BasicNameValuePair("Dim2",indentModel.getDim2()));
                entity.add(new BasicNameValuePair("Dim3",indentModel.getDim3()));
                entity.add(new BasicNameValuePair("Dim4",indentModel.getDim4()));
                entity.add(new BasicNameValuePair("Area",indentModel.getArea()));
                entity.add(new BasicNameValuePair("LAT1",indentModel.getLAT1()));
                entity.add(new BasicNameValuePair("LON1",indentModel.getLON1()));
                entity.add(new BasicNameValuePair("LAT2",indentModel.getLAT2()));
                entity.add(new BasicNameValuePair("LON2",indentModel.getLON2()));
                entity.add(new BasicNameValuePair("LAT3",indentModel.getLAT3()));
                entity.add(new BasicNameValuePair("LON3",indentModel.getLON3()));
                entity.add(new BasicNameValuePair("LAT4",indentModel.getLAT4()));
                entity.add(new BasicNameValuePair("LON4",indentModel.getLON4()));
                entity.add(new BasicNameValuePair("SuperviserCode",indentModel.getSuperviserCode()));
                entity.add(new BasicNameValuePair("Image",imgFrmt));
                entity.add(new BasicNameValuePair("OTP",""));
                entity.add(new BasicNameValuePair("ACKID",indentModel.getColId()));
                entity.add(new BasicNameValuePair("SMathod",indentModel.getMethod()));
                entity.add(new BasicNameValuePair("GName",indentModel.getGrowerName()));
                entity.add(new BasicNameValuePair("GFName",indentModel.getGrowerFather()));

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + Content);

                //Content=response.getEntity().toString();
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                AlertPopUp("Error: "+e.toString());
                //Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                {
                    dbh.updateServerStatusIndent(jsonObject.getString("EXCEPTIONMSG"),jsonObject.getString("ACKID"),"Failed");
                    AlertPopUpFinish(jsonObject.getString("EXCEPTIONMSG"));
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                FieldStaffIndentingCaneArea.this);
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

    private void AlertPopUpFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                FieldStaffIndentingCaneArea.this);
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
