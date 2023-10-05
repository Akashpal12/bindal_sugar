package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListPlantingTempItemAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.ChcekPlantingPolygonGrowerModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.GpsCheck;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class FieldStaffPlantingCaneArea extends AppCompatActivity {

    String ID="0";
    ArrayList<String> dataploughingType,datasprayType;
    String CoordinateArea="0.00";
    DBHelper dbh;
    SQLiteDatabase db;
    GoogleMap mMap;
    List<UserDetailsModel> loginUserDetailsList;
    String message;
    JSONArray plotFarmerShareArray;
    String listStatus;
    TextView Corner1,Lat1,Lng1,Distance1,Accuracy1,Corner2,Lat2,Lng2,Distance2,Accuracy2,Corner3,Lat3,Lng3,
            Distance3,Accuracy3,Corner4,Lat4,Lng4,Distance4,Accuracy4;
    int currentDistance=1;
    double currentAccuracy=200000;
    LinearLayout btnLayout,rl_capture_coordinate;
    RelativeLayout msgLayout;
    List <ChcekPlantingPolygonGrowerModel> chcekPlantingPolygonGrowerModelList;

    android.app.AlertDialog dialogPopup;
    EditText AreaMeter,AreaHec,NoOfPlot,input_seed_bag_qty;
    //RelativeLayout rl_master_coordinate,rl_seed_source_fac_oth;
    TextView t_master_latlng,t_master_acc;
    TextView location_lat,location_lng,location_accuracy;
    boolean checkPlot=false;
    double lat,lng;
    double manLat=0.00,manLng=0.00;
    MapView mMapView;
    GoogleMap googleMap;

    String StrAreaMeter="0.0",StrAreaHec="0.0",StrEastNorthLat="0.0",StrEastNorthLng="0.0",StrEastNorthDistance="0.0",
            StrEastNorthAccuracy="0.0",StrWestNorthLat="0.0",StrWestNorthLng="0.0",StrWestNorthDistance="0.0",StrWestNorthAccuracy="0.0",
            StrEastSouthLat="0.0",StrEastSouthLng="0.0",StrEastSouthDistance="0.0",StrEastSouthAccuracy="",StrWestSouthLat="0.0",
            StrWestSouthLng="0.0",StrWestSouthDistance="0.0",StrWestSouthAccuracy="0.0";

    String StrSeedSource="0",StrSeedSetType="0",StrSeedType="0",StrSoilTreatment="0",StrSeedTreatment="0",StrTypeOfPlanting="0",
            StrPlantingMethod="0",StrRowToRowDistance="0",StrInterCrop="0",StrIrrigationManner="0",StrVariety="0",strloadType="0";

    EditText input_village_code,input_village_name,input_grower_code,input_grower_name,input_grower_father,input_plot_village_code,
            input_layout_plot_varietys,input_layout_plot_varietys_name,input_plot_village_name,/*input_seed_qty,input_rate,input_distance,input_other_amount,input_pay_amount,input_mill_purchey_number,*/
            input_mobile_number,input_date,basel_item_qty,/*input_village_code_seed_source,input_village_name_seed_source,input_grower_code_seed_source,
            input_grower_name_seed_source,input_grower_father_seed_source,*/input_grower_plot_sr_no,input_plot_sr_no,input_plot_indent_area;


    Spinner input_seed_source,input_seed_type,/*input_actual_area,*/input_variety,/*input_transport,input_pay_mode,*/input_item,input_seed_set_type,
            input_soil_treatment,input_seed_treatment,input_type_of_planting,input_method_of_planting,input_row_to_row_distance,
            input_inter_crop,input_irrigation_manner,input_equipment,input_indent_number,loadType;

    List<MasterDropDown> seedTypeList,seedtTypeList,seedSetTypeList,varietyList,inputItemList,soilTreatmentList,seedTreatmentList,typeOfPlantingList,
            methodOfPlantingList,rowToRowDistanceList,interCropList,irrigationMannerList,equipmentList,landTypeList;

    List<Map<String, String>> objectList,equipmentObjectList;

    double Lat,Lng,Accuracy,LastLat=0,LastLng=0,dist=0;
    LocationManager locationManager;

    Context context;
    List<IndentModel> indentModelList;

    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private AlertDialog Alertdialog;


    TextInputLayout input_layout_seed_source,input_layout_seed_type,input_layout_seed_set_type,input_layout_soil_treatment,
            input_layout_seed_treatment,input_layout_type_of_planting,input_layout_method_of_planting,input_layout_row_to_row_distance,
            input_layout_inter_crop,input_layout_irrigation_manner,input_layout_variety,input_layout_seed_bag_qty,layoutloadType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        new SessionConfig(FieldStaffPlantingCaneArea.this).setListFound("0");
        try {
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU_PLANTING_CANE_AREA));
            toolbar.setTitle(getString(R.string.MENU_PLANTING_CANE_AREA));
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
            context=FieldStaffPlantingCaneArea.this;

            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();

            loginUserDetailsList = new ArrayList<>();
            chcekPlantingPolygonGrowerModelList=new SessionConfig(context).getcheckPPGModelList();



            loginUserDetailsList = dbh.getUserDetailsModel();
            plotFarmerShareArray=new JSONArray();


            //rl_seed_source_fac_oth = findViewById(R.id.rl_seed_source_fac_oth);
            rl_capture_coordinate = findViewById(R.id.rl_capture_coordinate);


            AreaMeter = findViewById(R.id.area_meter);
            AreaHec = findViewById(R.id.area_hec);
            NoOfPlot = findViewById(R.id.no_of_plot);




            input_layout_seed_bag_qty = findViewById(R.id.input_layout_seed_bag_qty);
            input_layout_seed_source = findViewById(R.id.input_layout_seed_source);
            input_layout_seed_type = findViewById(R.id.input_layout_seed_type);
            input_layout_seed_set_type = findViewById(R.id.input_layout_seed_set_type);
            input_layout_soil_treatment = findViewById(R.id.input_layout_soil_treatment);
            input_layout_seed_treatment = findViewById(R.id.input_layout_seed_treatment);
            input_layout_type_of_planting = findViewById(R.id.input_layout_type_of_planting);
            input_layout_method_of_planting = findViewById(R.id.input_layout_method_of_planting);
            input_layout_row_to_row_distance = findViewById(R.id.input_layout_row_to_row_distance);
            input_layout_inter_crop = findViewById(R.id.input_layout_inter_crop);
            input_layout_irrigation_manner = findViewById(R.id.input_layout_irrigation_manner);
            input_layout_variety = findViewById(R.id.input_layout_variety);
            input_layout_plot_varietys=findViewById(R.id.input_layout_plot_varietys);
            input_layout_plot_varietys_name=findViewById(R.id.input_layout_plot_varietys_name);
            loadType=findViewById(R.id.input_load_type);
            layoutloadType = findViewById(R.id.input_layout_load_type);


            input_grower_plot_sr_no = findViewById(R.id.input_grower_plot_sr_no);

            input_village_code = findViewById(R.id.input_village_code);
            // Set the Value in this
            input_village_name = findViewById(R.id.input_village_name);
            input_grower_code = findViewById(R.id.input_grower_code);
            // Set The Value
            input_grower_name = findViewById(R.id.input_grower_name);
            input_grower_father = findViewById(R.id.input_grower_father);
            input_plot_village_code = findViewById(R.id.input_plot_village_code);
            // Set the Value
            input_plot_village_name = findViewById(R.id.input_plot_village_name);
            /*input_village_code_seed_source = findViewById(R.id.input_village_code_seed_source);
            input_village_name_seed_source = findViewById(R.id.input_village_name_seed_source);
            input_grower_code_seed_source = findViewById(R.id.input_grower_code_seed_source);
            input_grower_name_seed_source = findViewById(R.id.input_grower_name_seed_source);
            input_grower_father_seed_source = findViewById(R.id.input_grower_father_seed_source);
            input_seed_qty = findViewById(R.id.input_seed_qty);
            input_rate = findViewById(R.id.input_rate);
            input_distance = findViewById(R.id.input_distance);
            input_other_amount = findViewById(R.id.input_other_amount);
            input_pay_amount = findViewById(R.id.input_pay_amount);
            input_mill_purchey_number = findViewById(R.id.input_mill_purchey_number);*/
            input_mobile_number = findViewById(R.id.input_mobile_number);
            input_date = findViewById(R.id.input_date);
            basel_item_qty = findViewById(R.id.basel_item_qty);
            input_plot_sr_no = findViewById(R.id.input_plot_sr_no);
            input_plot_indent_area = findViewById(R.id.input_plot_indent_area);


            input_seed_source = findViewById(R.id.input_seed_source);
            input_seed_type = findViewById(R.id.input_seed_type);
            /*input_actual_area = findViewById(R.id.input_actual_area);*/
            input_variety = findViewById(R.id.input_variety);
            /*input_transport = findViewById(R.id.input_transport);
            input_pay_mode = findViewById(R.id.input_pay_mode);*/
            input_item = findViewById(R.id.input_item);
            input_seed_set_type = findViewById(R.id.input_seed_set_type);
            input_soil_treatment = findViewById(R.id.input_soil_treatment);
            input_seed_treatment = findViewById(R.id.input_seed_treatment);
            input_type_of_planting = findViewById(R.id.input_type_of_planting);
            input_method_of_planting = findViewById(R.id.input_method_of_planting);
            input_row_to_row_distance = findViewById(R.id.input_row_to_row_distance);
            input_inter_crop = findViewById(R.id.input_inter_crop);
            input_irrigation_manner = findViewById(R.id.input_irrigation_manner);
            input_equipment = findViewById(R.id.input_equipment);
            input_indent_number = findViewById(R.id.input_indent_number);
            input_seed_bag_qty = findViewById(R.id.input_seed_bag_qty);
            landTypeList = new ArrayList<>();

            seedTypeList=dbh.getMasterDropDown("6");
            seedtTypeList=dbh.getMasterDropDown("17");
            inputItemList=dbh.getMasterDropDown("9");
            objectList=new ArrayList<>();
            equipmentObjectList=new ArrayList<>();
            soilTreatmentList=dbh.getMasterDropDown("19");
            varietyList=dbh.getMasterDropDown("12");
            seedTreatmentList=dbh.getMasterDropDown("16");
            seedSetTypeList=dbh.getMasterDropDown("18");
            typeOfPlantingList=dbh.getMasterDropDown("13");
            methodOfPlantingList=dbh.getMasterDropDown("8");
            rowToRowDistanceList=dbh.getMasterDropDown("21");
            interCropList=dbh.getMasterDropDown("15");
            irrigationMannerList=dbh.getMasterDropDown("1");
            equipmentList=dbh.getMasterDropDown("4");
            landTypeList = dbh.getMasterDropDown("5");


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

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            input_date.setText(currentDt);
            input_date.setInputType(InputType.TYPE_NULL);
            input_date.setTextIsSelectable(true);
            input_date.setFocusable(false);
            input_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(FieldStaffPlantingCaneArea.this,new DatePickerDialog.OnDateSetListener() {
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
                            input_date.setText(year+"-"+  temmonth + "-"+temDate );

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });


            init();
            findLocation();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }

    protected void findLocation() {

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
                            new AlertDialogManager().AlertPopUpFinish(context,"Security Error: please enable gps service");
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
                            Location location = locationResult.getLastLocation();
                            if(location.isFromMockProvider())
                            {
                                new AlertDialogManager().showToast((Activity) context,"Security Error : you can not use this application because we detected fake GPS ?");
                            }
                            else
                            {
                                try {
                                    List<Address> addresses=new ArrayList<>();
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    manLat = location.getLatitude();
                                    manLng = location.getLongitude();
                                    Accuracy = location.getAccuracy();
                                    if(msgLayout!=null)
                                        setLatDialogue("" + Lat, "" + Lng, "" + Accuracy);
                                    if(googleMap!=null)
                                    {
                                        LatLng latLng = new LatLng(Lat, Lng);
                                        googleMap.clear();
                                        drawPlot();
                                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Your Location"));
                                        /*googleMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_person_pin_circle_24))
                                                .title(""));*/
                                    }
                                    /*Geocoder geocoder;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                                    address.setText(addresses.get(0).getAddressLine(0));*/
                                }
                                catch(SecurityException se)
                                {
                                    new AlertDialogManager().AlertPopUpFinish(context,"Security Error:"+se.toString());
                                }
                                catch(Exception se)
                                {
                                    new AlertDialogManager().AlertPopUpFinish(context,"Error:"+se.toString());
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Security Error:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Error:"+se.toString());
        }
    }

    public void exit(View v)
    {
        finish();
    }

    private void init()
    {
        try {
            setPermission();
            input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    clearCoordinates();
                    //input_actual_area.setSelection(0);
                    if (input_village_code.getText().toString().length() > 0) {
                        List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                        if (villageModalList.size() > 0) {
                            if(villageModalList.get(0).isTarget()==0)
                            {
                                new AlertDialogManager().RedDialog(context,"Target not set for "+villageModalList.get(0).getName()+" village, So you will not get proper report of planting.\n\n"+villageModalList.get(0).getName()+" गांव के लिए लक्ष्य निर्धारित नहीं है, इसलिए आपको प्लांटिंग की उचित रिपोर्ट नहीं मिलेगी।");
                            }
                            input_village_code.setText(villageModalList.get(0).getCode());
                            input_village_name.setText(villageModalList.get(0).getName());
                        } else {
                            input_village_code.setText("");
                            input_village_name.setText("");
                            input_grower_code.setText("");
                            input_grower_name.setText("");
                            input_grower_father.setText("");
                            new AlertDialogManager().RedDialog(context, "Please enter valid village code");
                        }
                    }
                }
            });
            input_grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        input_grower_name.setFocusable(true);
                        input_grower_name.setTextIsSelectable(true);
                        input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        input_grower_father.setFocusable(true);
                        input_grower_father.setTextIsSelectable(true);
                        input_grower_father.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        clearCoordinates();
                        //input_actual_area.setSelection(0);
                        if (input_village_code.getText().toString().length() > 0) {
                            if (input_grower_code.getText().toString().length() > 0) {
                                if (input_grower_code.getText().toString().equals("0")) {
                                    input_grower_name.setFocusable(true);
                                    input_grower_name.setTextIsSelectable(true);
                                    input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                    input_grower_father.setFocusable(true);
                                    input_grower_father.setTextIsSelectable(true);
                                    input_grower_father.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                } else {
                                    input_grower_name.setInputType(InputType.TYPE_NULL);
                                    input_grower_father.setInputType(InputType.TYPE_NULL);
                                    List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                                    if (growerModelList.size() > 0) {
                                        input_grower_code.setText(growerModelList.get(0).getGrowerCode());
                                        input_grower_name.setText(growerModelList.get(0).getGrowerName());
                                        input_grower_father.setText(growerModelList.get(0).getGrowerFather());
                                    } else {
                                        new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                        input_grower_code.setText("");
                                        input_grower_name.setText("");
                                        input_grower_father.setText("");
                                    }

                                }


                                indentModelList=dbh.getIndentModel("","",input_village_code.getText().toString(),input_grower_code.getText().toString(),"");
                                if(indentModelList.size()==0)
                                {
                                    input_village_code.setText("");
                                    input_village_name.setText("");
                                    input_grower_code.setText("");
                                    input_grower_name.setText("");
                                    input_grower_father.setText("");
                                    ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
                                    indentPlotSrNoArrayList.add("Select");
                                    ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                                            R.layout.list_item, indentPlotSrNoArrayList);
                                    input_indent_number.setAdapter(adapterIndentSrNo);
                                    new AlertDialogManager().RedDialog(context,"Sorry indenting not found for this grower please go for indenting first.");
                                }
                                else
                                {
                                    boolean inside = false;
                                    SetIndentLocation:
                                    {
                                        for (int i = 0; i < indentModelList.size(); i++) {
                                            List<LatLng> latLngList = new ArrayList<>();
                                            latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT1()), Double.parseDouble(indentModelList.get(i).getLON1())));
                                            latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT2()), Double.parseDouble(indentModelList.get(i).getLON2())));
                                            latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT3()), Double.parseDouble(indentModelList.get(i).getLON3())));
                                            latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT4()), Double.parseDouble(indentModelList.get(i).getLON4())));
                                            //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                            double lats = manLat;
                                            double lngs = manLng;
                                            LatLng latlng = new LatLng(lats, lngs);
                                            inside = PolyUtil.containsLocation(latlng, latLngList, true);
                                            if (inside) {
                                                ID = ""+i;
                                                //ID = indentModelList.get(i).getColId();
                                                NoOfPlot.setText(indentModelList.get(i).getNOFPLOTS());
                                                rl_capture_coordinate.setVisibility(View.VISIBLE);
                                                input_grower_plot_sr_no.setVisibility(View.VISIBLE);
                                                input_grower_plot_sr_no.setText(indentModelList.get(i).getPlotSerialNumber());
                                                break SetIndentLocation;
                                            }
                                        }
                                    }

                                    ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();

                                    indentPlotSrNoArrayList.add("Select");
                                    if (inside) {
                                        List<MasterDropDown> varitety=dbh.getMasterDropDowncode("12",indentModelList.get(Integer.parseInt(ID)).getVARIETY());
                                        List<VillageModal> plv=dbh.getVillageModal(indentModelList.get(Integer.parseInt(ID)).getPLOTVillage());
                                        if(plv.size()>0)
                                        {
                                            if(varitety.size()==0){
                                                indentPlotSrNoArrayList.add( " Variety : "+indentModelList.get(Integer.parseInt(ID)).getVARIETY()+" - Serial : "+ indentModelList.get(Integer.parseInt(ID)).getPlotSerialNumber()+ " - Area : "+indentModelList.get(Integer.parseInt(ID)).getINDAREA()+" - Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName());


                                            }else{
                                                indentPlotSrNoArrayList.add( " Variety : "+varitety.get(0).getName()+" - Serial : "+ indentModelList.get(Integer.parseInt(ID)).getPlotSerialNumber()+ " - Area : "+indentModelList.get(Integer.parseInt(ID)).getINDAREA()+" - Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName());
                                            }


                                        }
                                        else
                                        {
                                            new AlertDialogManager().RedDialog(context,"Sorry plot village not found");
                                        }
                                    }
                                    else
                                    {
                                        for (int i = 0; i < indentModelList.size(); i++) {
                                            List<VillageModal> plv=dbh.getVillageModal(indentModelList.get(i).getPLOTVillage());
                                            List<MasterDropDown> varitety=dbh.getMasterDropDowncode("12",indentModelList.get(i).getVARIETY());

                                            if(plv.size()>0)
                                            {
                                                if(varitety.size()==0){
                                                    indentPlotSrNoArrayList.add( " Variety : "+indentModelList.get(Integer.parseInt(ID)).getVARIETY()+" - Serial : "+ indentModelList.get(Integer.parseInt(ID)).getPlotSerialNumber()+ " - Area : "+indentModelList.get(Integer.parseInt(ID)).getINDAREA()+" - Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName());

                                                }else{
                                                    indentPlotSrNoArrayList.add( " Variety : "+varitety.get(0).getName()+" - Serial : "+ indentModelList.get(i).getPlotSerialNumber()+ " - Area : "+indentModelList.get(i).getINDAREA()+" - Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName());


                                                }

                                            }
                                        }
                                    }

                                    ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                                            R.layout.list_item, indentPlotSrNoArrayList);
                                    input_indent_number.setAdapter(adapterIndentSrNo);
                                    input_indent_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            if(i==0)
                                            {
                                                clearCoordinates();
                                                //input_actual_area.setSelection(0);
                                                input_plot_village_code.setText("");
                                                input_plot_village_name.setText("");
                                            }
                                            else
                                            {
                                                clearCoordinates();
                                                //input_actual_area.setSelection(0);
                                                List<VillageModal> villageModalList = dbh.getVillageModal(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getPLOTVillage());
                                                List<MasterDropDown> varitety=dbh.getMasterDropDowncode("12",indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getVARIETY());

                                                if (villageModalList.size() > 0) {
                                                    input_plot_village_code.setText(villageModalList.get(0).getCode());
                                                    input_plot_village_name.setText(villageModalList.get(0).getName());
                                                    int plotSrNo = Integer.parseInt(villageModalList.get(0).getMaxPlant());
                                                    plotSrNo++;
                                                    input_plot_sr_no.setText("" + plotSrNo);
                                                    if (indentModelList.get(input_indent_number.getSelectedItemPosition() - 1).getINDAREA().equalsIgnoreCase("")) {
                                                        input_plot_indent_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition() - 1).getINDAREA());
                                                    }
                                                    if(varitety.size()==0){
                                                        input_layout_plot_varietys.setText(varitety.get(0).getName());
                                                        //input_layout_plot_varietys_name.setText(varitety.get(0).getName());

                                                    }

                                                    else {
                                                        input_layout_plot_varietys.setText(varitety.get(0).getName());
                                                        // input_layout_plot_varietys_name.setText(varitety.get(0).getName());

                                                        if (Double.parseDouble(indentModelList.get(input_indent_number.getSelectedItemPosition() - 1).getINDAREA()) > 0) {
                                                            input_plot_indent_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition() - 1).getINDAREA());

                                                        }


                                                        else
                                                        {
                                                            input_plot_sr_no.setText("" + plotSrNo);
                                                            input_layout_plot_varietys.setText(varitety.get(0).getName());
                                                            //input_layout_plot_varietys_name.setText(varitety.get(0).getName());
                                                            input_plot_indent_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getINDAREA());
                                                        }
                                                    }
                                                } else {
                                                    input_plot_village_code.setText("");
                                                    input_plot_village_name.setText("");
                                                    input_plot_sr_no.setText("");
                                                    input_plot_indent_area.setText("");
                                                    input_layout_plot_varietys.setText("");
                                                    input_layout_plot_varietys_name.setText("");
                                                    new AlertDialogManager().RedDialog(context, "Plot village code not found in our record please contact admin");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    if (inside && indentPlotSrNoArrayList.size()>1)
                                    {
                                        input_indent_number.setSelection(1);
                                    }
                                }
                                //input_indent_number=
                            }
                        } else {
                            //new AlertDialogManager().RedDialog(context, "Please enter village code");
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                    }
                }
            });


            ArrayList<String> seedSource = new ArrayList<String>();
            seedSource.add("Seed Source");
            for (int i = 0; i < seedTypeList.size(); i++) {
                seedSource.add(seedTypeList.get(i).getName());
            }
            ArrayAdapter<String> adapterseedSource = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, seedSource);
            input_seed_source.setAdapter(adapterseedSource);



            ArrayList<String> seedTypeArray = new ArrayList<String>();
            seedTypeArray.add("seed Type ");
            for (int i = 0; i < seedtTypeList.size(); i++) {
                seedTypeArray.add(seedtTypeList.get(i).getName());
            }
            ArrayAdapter<String> seedTypeAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, seedTypeArray);
            input_seed_type.setAdapter(seedTypeAdapter);

            ArrayList<String> inputItemArray = new ArrayList<String>();
            inputItemArray.add("Select");
            for (int i = 0; i < inputItemList.size(); i++) {
                inputItemArray.add(inputItemList.get(i).getName());
            }
            ArrayAdapter<String> adapterInputItem = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, inputItemArray);
            input_item.setAdapter(adapterInputItem);

            ArrayList<String> varietyArray = new ArrayList<String>();
            varietyArray.add("Variety");
            for (int i = 0; i < varietyList.size(); i++) {
                varietyArray.add(varietyList.get(i).getName());
            }
            ArrayAdapter<String> adapterVariety = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, varietyArray);
            input_variety.setAdapter(adapterVariety);

            /*ArrayList<String> transporterArray = new ArrayList<String>();
            transporterArray.add("Select");
            transporterArray.add("Self");
            transporterArray.add("Supplier");
            ArrayAdapter<String> adapterTransporter = new ArrayAdapter<String>(Planting.this,
                    R.layout.list_item, transporterArray);
            input_transport.setAdapter(adapterTransporter);*/


            ArrayList<String> seedSetTypeArray = new ArrayList<String>();
            seedSetTypeArray.add("Select");
            for (int i = 0; i < seedSetTypeList.size(); i++) {
                seedSetTypeArray.add(seedSetTypeList.get(i).getName());
            }
            ArrayAdapter<String> seedSetTypeAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, seedSetTypeArray);
            input_seed_set_type.setAdapter(seedSetTypeAdapter);

            ArrayList<String> soilTreatentArray = new ArrayList<String>();
            soilTreatentArray.add("Select");
            for (int i = 0; i < soilTreatmentList.size(); i++) {
                soilTreatentArray.add(soilTreatmentList.get(i).getName());
            }
            ArrayAdapter<String> soilTreatentAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, soilTreatentArray);
            input_soil_treatment.setAdapter(soilTreatentAdapter);

            ArrayList<String> seedTreatmentArray = new ArrayList<String>();
            seedTreatmentArray.add("Select");
            for (int i = 0; i < seedTreatmentList.size(); i++) {
                seedTreatmentArray.add(seedTreatmentList.get(i).getName());
            }
            ArrayAdapter<String> seedTreatentAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, seedTreatmentArray);
            input_seed_treatment.setAdapter(seedTreatentAdapter);

            ArrayList<String> typeOfPlantingArray = new ArrayList<String>();
            typeOfPlantingArray.add("Type Of Planting");
            for (int i = 0; i < typeOfPlantingList.size(); i++) {
                typeOfPlantingArray.add(typeOfPlantingList.get(i).getName());
            }
            ArrayAdapter<String> typeOfPlantingAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, typeOfPlantingArray);
            input_type_of_planting.setAdapter(typeOfPlantingAdapter);

            ArrayList<String> methodOfPlantingArray = new ArrayList<String>();
            methodOfPlantingArray.add("Method Of Planting");
            for (int i = 0; i < methodOfPlantingList.size(); i++) {
                methodOfPlantingArray.add(methodOfPlantingList.get(i).getName());
            }
            ArrayAdapter<String> methodOfPlantingAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, methodOfPlantingArray);
            input_method_of_planting.setAdapter(methodOfPlantingAdapter);

            ArrayList<String> rowToRowDistanceArray = new ArrayList<String>();
            rowToRowDistanceArray.add("Select");
            for (int i = 0; i < rowToRowDistanceList.size(); i++) {
                rowToRowDistanceArray.add(rowToRowDistanceList.get(i).getName());
            }
            ArrayAdapter<String> rowToRowDistanceAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, rowToRowDistanceArray);
            input_row_to_row_distance.setAdapter(rowToRowDistanceAdapter);

            ArrayList<String> interCropArray = new ArrayList<String>();
            interCropArray.add("Select");
            for (int i = 0; i < interCropList.size(); i++) {
                interCropArray.add(interCropList.get(i).getName());
            }
            ArrayAdapter<String> interCropAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, interCropArray);
            input_inter_crop.setAdapter(interCropAdapter);

            ArrayList<String> irrigationMannerArray = new ArrayList<String>();
            irrigationMannerArray.add("Select");
            for (int i = 0; i < irrigationMannerList.size(); i++) {
                irrigationMannerArray.add(irrigationMannerList.get(i).getName());
            }
            ArrayAdapter<String> irrigationMannerAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, irrigationMannerArray);
            input_irrigation_manner.setAdapter(irrigationMannerAdapter);

            ArrayList<String> dataloadType = new ArrayList<String>();
            dataloadType.add("Land Type");
            for (int i = 0; i < landTypeList.size(); i++) {
                dataloadType.add(landTypeList.get(i).getName());
            }

            ArrayAdapter<String> adapterloadType = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, dataloadType);
            loadType.setAdapter(adapterloadType);

            ArrayList<String> equipmentArray = new ArrayList<String>();
            equipmentArray.add("Equipment");
            for (int i = 0; i < equipmentList.size(); i++) {
                equipmentArray.add(equipmentList.get(i).getName());
            }
            ArrayAdapter<String> equipmentAdapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
                    R.layout.list_item, equipmentArray);
            input_equipment.setAdapter(equipmentAdapter);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    private void setPermission()
    {
        try {
            if (dbh.getControlStatus("SEED SOURCE", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_seed_source.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SEED SET TYPE", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_seed_set_type.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SEED TREATMENT", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_seed_treatment.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("ROW TO ROW", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_row_to_row_distance.setVisibility(View.GONE);
            }
            //
            if (dbh.getControlStatus("IRRIGATION", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_irrigation_manner.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SEED TYPE", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_seed_type.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("SOIL", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_soil_treatment.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("VARIETY", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_variety.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("TYPE OF PLANTING", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_type_of_planting.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("CROP", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_inter_crop.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("PLANTATION", "PLANTING").equalsIgnoreCase("0")) {
                input_layout_method_of_planting.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("LAND TYPE", "PLANTING").equalsIgnoreCase("0")) {
                layoutloadType.setVisibility(View.GONE);
            }
            if (dbh.getControlStatus("MAREA", "PLANTING").equalsIgnoreCase("1")) {

                RelativeLayout layout_coordinate=findViewById(R.id.layout_coordinate);
                layout_coordinate.setVisibility(View.GONE);
               /* Button capture_lc= findViewById(R.id.capture_lc);
                Button view_on_map= findViewById(R.id.view_on_map);
                Button find_plots= findViewById(R.id.find_plots);
                Button reset_lc= findViewById(R.id.reset_lc);
                capture_lc.setOnClickListener(null);
                view_on_map.setOnClickListener(null);
                find_plots.setOnClickListener(null);
                reset_lc.setOnClickListener(null);*/

                //  input_layout_seed_source.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {

        }
    }
    private void setPermission2()
    {
        try {



        }
        catch (Exception e)
        {

        }
    }

    public void addItemData(View v)
    {
        try {
            if (input_item.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select item");
            } else if (basel_item_qty.getText().toString().length() == 0) {
                new AlertDialogManager().RedDialog(context, "Please enter quantity");
            } else {
                if (objectList.size() == 0) {
                    Map<String, String> stringStringMap1 = new HashMap<>();
                    stringStringMap1.put("itemid", "Code");
                    stringStringMap1.put("itemcode", "Name");
                    stringStringMap1.put("itemqty", "Qty");
                    stringStringMap1.put("background", "#000000");
                    stringStringMap1.put("color", "#FFFFFF");
                    objectList.add(stringStringMap1);
                }
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("itemid", inputItemList.get(input_item.getSelectedItemPosition() - 1).getCode());
                stringStringMap.put("itemcode", inputItemList.get(input_item.getSelectedItemPosition() - 1).getName());
                stringStringMap.put("itemqty", basel_item_qty.getText().toString());
                stringStringMap.put("color", "#000000");
                stringStringMap.put("background", "#FFFFFF");
                boolean flag = true;
                if (objectList.size() > 0) {

                    for (int i = 0; i < objectList.size(); i++) {
                        Map<String, String> stringStringMapTemp = objectList.get(i);
                        if (stringStringMapTemp.get("itemid").equalsIgnoreCase(inputItemList.get(input_item.getSelectedItemPosition() - 1).getCode())) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    objectList.add(stringStringMap);
                    new AlertDialogManager().GreenDialog(context, "Item successfully added");
                    input_item.setSelection(0);
                    basel_item_qty.setText("");
                } else {
                    new AlertDialogManager().RedDialog(context, "Item already added");
                }
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                ListPlantingTempItemAdapter listPloughingAdapter = new ListPlantingTempItemAdapter(context, objectList);
                recyclerView.setAdapter(listPloughingAdapter);
            }
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void addItemEquipment(View v)
    {
        try {
            if (input_equipment.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog(context, "Please select item");
            } else {
                if (equipmentObjectList.size() == 0) {
                    Map<String, String> stringStringMap1 = new HashMap<>();
                    stringStringMap1.put("itemid", "Code");
                    stringStringMap1.put("itemcode", "Name");
                    stringStringMap1.put("itemqty", "");
                    stringStringMap1.put("background", "#000000");
                    stringStringMap1.put("color", "#FFFFFF");
                    equipmentObjectList.add(stringStringMap1);
                }
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("itemid", equipmentList.get(input_equipment.getSelectedItemPosition() - 1).getCode());
                stringStringMap.put("itemcode", equipmentList.get(input_equipment.getSelectedItemPosition() - 1).getName());
                stringStringMap.put("itemqty", "");
                stringStringMap.put("color", "#000000");
                stringStringMap.put("background", "#FFFFFF");
                boolean flag = true;
                if (equipmentObjectList.size() > 0) {

                    for (int i = 0; i < equipmentObjectList.size(); i++) {
                        Map<String, String> stringStringMapTemp = equipmentObjectList.get(i);
                        if (stringStringMapTemp.get("itemid").equalsIgnoreCase(equipmentList.get(input_equipment.getSelectedItemPosition() - 1).getCode())) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    equipmentObjectList.add(stringStringMap);
                    new AlertDialogManager().GreenDialog(context, "Item successfully added");
                    input_equipment.setSelection(0);
                } else {
                    new AlertDialogManager().RedDialog(context, "Item already added");
                }
                RecyclerView recyclerView = findViewById(R.id.recycler_view_equipment);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                ListPlantingTempItemAdapter listPloughingAdapter = new ListPlantingTempItemAdapter(context, equipmentObjectList);
                recyclerView.setAdapter(listPloughingAdapter);
            }
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void saveData(View v)
    {
        try {


            checkPlotValidation:{
                if(input_plot_village_code.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter village code");
                    break checkPlotValidation;
                }
                if(input_grower_code.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter grower code");
                    break checkPlotValidation;
                }
                if(input_indent_number.getSelectedItemPosition()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please select indent serial number");
                    break checkPlotValidation;
                }
                if(input_plot_village_code.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter plot village code");
                    break checkPlotValidation;
                }

                if(input_plot_sr_no.getText().toString().length()==0 && input_plot_sr_no.getText().toString().length()>5)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter valid plot serial number");
                    break checkPlotValidation;
                }
                if(input_seed_source.getSelectedItemPosition()==0)//seedSourceList
                {
                    AlertPopUp("Please select seed source");
                    break checkPlotValidation;
                }


                if(input_type_of_planting.getSelectedItemPosition()==0)
                {
                    AlertPopUp("Please select planting type");
                    break checkPlotValidation;
                }

                if(loadType.getSelectedItemPosition()==0)
                {
                    AlertPopUp("Please select land type");
                    break checkPlotValidation;
                }
                if(input_method_of_planting.getSelectedItemPosition()==0)
                {
                    AlertPopUp("Please select planting method");
                    break checkPlotValidation;
                }

                if(input_variety.getSelectedItemPosition()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please select variety");
                    break checkPlotValidation;
                }

                if(input_seed_bag_qty.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter seed bag qty");
                    break checkPlotValidation;
                }

                if (dbh.getControlStatus("SEED SOURCE", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_seed_source.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select seed source");
                        break checkPlotValidation;
                    }
                    else {
                        StrSeedSource=seedTypeList.get(input_seed_source.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("SEED SET TYPE", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_seed_set_type.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select seed type");
                        break checkPlotValidation;
                    }
                    else {
                        StrSeedSetType=seedSetTypeList.get(input_seed_set_type.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("SEED TREATMENT", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_seed_treatment.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select seed treatment");
                        break checkPlotValidation;
                    }
                    else {
                        StrSeedTreatment=seedTreatmentList.get(input_seed_treatment.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("ROW TO ROW", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_row_to_row_distance.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select row to row");
                        break checkPlotValidation;
                    }
                    else {
                        StrRowToRowDistance=rowToRowDistanceList.get(input_row_to_row_distance.getSelectedItemPosition() - 1).getCode();
                    }
                }
                //
                if (dbh.getControlStatus("IRRIGATION", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_irrigation_manner.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select irrigation");
                        break checkPlotValidation;
                    }
                    else {
                        StrIrrigationManner=irrigationMannerList.get(input_irrigation_manner.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("SEED TYPE", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_seed_type.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select seed set type");
                        break checkPlotValidation;
                    }
                    else {
                        StrSeedSetType=seedtTypeList.get(input_seed_type.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("SOIL", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_soil_treatment.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select soil treatment");
                        break checkPlotValidation;
                    }
                    else {
                        StrSoilTreatment=soilTreatmentList.get(input_soil_treatment.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("VARIETY", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_variety.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select variety");
                        break checkPlotValidation;
                    }
                    else {
                        StrVariety=varietyList.get(input_variety.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("TYPE OF PLANTING", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_type_of_planting.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select planting");
                        break checkPlotValidation;
                    }
                    else {
                        StrTypeOfPlanting=typeOfPlantingList.get(input_type_of_planting.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("CROP", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_inter_crop.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select inter crop");
                        break checkPlotValidation;
                    }
                    else {
                        StrInterCrop=interCropList.get(input_inter_crop.getSelectedItemPosition() - 1).getCode();
                    }
                }

                if (dbh.getControlStatus("LAND TYPE", "INDENTING").equalsIgnoreCase("1")) {
                    if(loadType.getSelectedItemPosition()==0)
                    {
                        AlertPopUp("Please select land type");
                        break checkPlotValidation;
                    }
                    else{
                        strloadType=landTypeList.get(loadType.getSelectedItemPosition() - 1).getCode();
                    }

                }

                if (dbh.getControlStatus("PLANTATION", "PLANTING").equalsIgnoreCase("1")) {
                    if(input_method_of_planting.getSelectedItemPosition()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Please select planting method");
                        break checkPlotValidation;
                    }
                    else {
                        StrPlantingMethod=methodOfPlantingList.get(input_method_of_planting.getSelectedItemPosition() - 1).getCode();
                    }
                }
                if (dbh.getControlStatus("PLANTATION", "PLANTING").equalsIgnoreCase("1")) {

                    if(AreaMeter.getText().toString().length()==0)
                    {
                        AreaMeter.setText("0");
                        break checkPlotValidation;
                    }

                }


                if(AreaMeter.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter area meter");
                    break checkPlotValidation;
                }

                if(input_mobile_number.getText().toString().length()!=10)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter 10 digit valid mobile number");
                    break checkPlotValidation;
                }
                if(input_date.getText().toString().length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please enter date");
                    break checkPlotValidation;
                }
                if(pictureImagePath.length()<10)
                {
                    new AlertDialogManager().RedDialog(context,"Please capture image");
                    break checkPlotValidation;
                }
                if(objectList.size()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please add item and quantity");
                    break checkPlotValidation;
                }
                if(equipmentObjectList.size()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Please add equipment");
                    break checkPlotValidation;
                }

                if(input_mobile_number.getText().toString().length()!=10)
                {
                    AlertPopUp("Please enter mobile number");
                    break checkPlotValidation;
                }
                if(input_seed_bag_qty.getText().toString().length()==0)
                {
                    AlertPopUp("Please select seed/Bag Quantity");
                    break checkPlotValidation;
                }
                SaveFinalData();

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

    private void SaveFinalData()
    {
        try {
            PlantingModel plantingModel = new PlantingModel();
            plantingModel.setVillage(input_village_code.getText().toString());
            plantingModel.setGrower(input_grower_code.getText().toString());
            plantingModel.setPLOTVillage(input_plot_village_code.getText().toString());

            plantingModel.setSupplyMode("0");
            plantingModel.setHarvesting("0");
            plantingModel.setEquipment("0");
            plantingModel.setLandType("0");
            plantingModel.setBaselDose("0");

            plantingModel.setSeedSource(StrSeedSource);
            plantingModel.setIrrigationmode(StrIrrigationManner);
            plantingModel.setSeedType(StrSeedSetType);
            plantingModel.setMethod(StrPlantingMethod);
            plantingModel.setVARIETY(StrVariety);
            plantingModel.setCrop(StrInterCrop);
            plantingModel.setPlantingType(StrTypeOfPlanting);
            plantingModel.setSeedTreatment(StrSeedTreatment);
            plantingModel.setSeedSetType(StrSeedSetType);
            plantingModel.setSoilTreatment(StrSoilTreatment);
            plantingModel.setRowToRowDistance(StrRowToRowDistance);
            plantingModel.setLandType(strloadType);


            plantingModel.setSmMethod(StrPlantingMethod);
            plantingModel.setDim1(StrEastNorthDistance);
            plantingModel.setDim2(StrEastSouthDistance);
            plantingModel.setDim3(StrWestSouthDistance);
            plantingModel.setDim4(StrWestNorthDistance);
            plantingModel.setArea(CoordinateArea);
            plantingModel.setLAT1(StrEastNorthLat);
            plantingModel.setLON1(StrEastNorthLng);
            plantingModel.setLAT2(StrEastSouthLat);
            plantingModel.setLON2(StrEastSouthLng);
            plantingModel.setLAT3(StrWestSouthLat);
            plantingModel.setLON3(StrWestSouthLng);
            plantingModel.setLAT4(StrWestNorthLat);
            plantingModel.setLON4(StrWestNorthLng);
            plantingModel.setImage(filename);
            plantingModel.setSuperviserCode(loginUserDetailsList.get(0).getCode());
            plantingModel.setId(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getPlotSerialNumber());
            plantingModel.setArea("" + AreaMeter.getText().toString());
            plantingModel.setInsertLAT(""+manLat);
            plantingModel.setInsertLON(""+manLng);
            plantingModel.setSprayType("0");
            plantingModel.setPloughingType("0");
            plantingModel.setManualArea(AreaMeter.getText().toString());
            plantingModel.setMobileNumber(input_mobile_number.getText().toString());
            plantingModel.setmDate(input_date.getText().toString());



            plantingModel.setPlantation("0");
            plantingModel.setActualAreaType("");


            plantingModel.setSeedVillage("0");
            plantingModel.setSeedGrower("0");
            plantingModel.setSeedTransporter("0");
            plantingModel.setSeedDistance("0");
            plantingModel.setSeedQuantity("0");
            plantingModel.setSeedRate("0");
            plantingModel.setSeedOtherAmount("0");
            plantingModel.setSeedPayAmount("0");
            plantingModel.setMillPurchey("0");
            plantingModel.setSeedPayMode("0");
            plantingModel.setPlotSerialNumber(input_plot_sr_no.getText().toString());
            plantingModel.setArea(input_plot_indent_area.getText().toString());
            plantingModel.setVARIETY(StrVariety);
            plantingModel.setServerStatus("No");
            CheckBox isideal=findViewById(R.id.isideal);
            if(isideal.isChecked())
            {
                plantingModel.setIsIdeal("1");
            }
            else
            {
                plantingModel.setIsIdeal("0");
            }
            CheckBox isnursery=findViewById(R.id.isnursery);
            if(isnursery.isChecked())
            {
                plantingModel.setIsNursery("1");
            }
            else
            {
                plantingModel.setIsNursery("0");
            }
            plantingModel.setSeedBagQty(input_seed_bag_qty.getText().toString());
            dbh.insertPlantingModel(plantingModel, equipmentObjectList, objectList);
            //AlertPopUpFinish("Planting done");
            Intent intent = new Intent(context, StaffPlantingFindPlotMapview.class);
            new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Data successfully saved", intent);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context, "Error:"+e.toString());
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

                        //holder.ll.removeAllViewsInLayout();
                        mMapView = (MapView) mView.findViewById(R.id.mapView);
                        MapsInitializer.initialize(context);

                        mMapView = (MapView) mView.findViewById(R.id.mapView);
                        mMapView.onCreate(Alertdialog.onSaveInstanceState());
                        mMapView.onResume();// needed to get the map to display immediately
                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap gMap) {
                                //
                                googleMap=gMap;
                                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                drawPlot();
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
                        new AlertDialogManager().RedDialog(context,"4th lat lng not find");
                    }
                }
                else {
                    new AlertDialogManager().RedDialog(context,"3rd lat lng not find");
                }
            }
            else {
                new AlertDialogManager().RedDialog(context,"2nd lat lng not find");
            }
        }
        else {
            new AlertDialogManager().RedDialog(context,"1st lat lng not find");
        }

    }









    private void drawPlot()
    {
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midPoint, 19.0f));
    }

    private void getCurrentLocation()
    {
        try {
            final DecimalFormat decimalFormat = new DecimalFormat("##");
            android.app.AlertDialog.Builder dialogbilder = new android.app.AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_get_latlng, null);
            dialogbilder.setView(mView);
            final Spinner location_direction=mView.findViewById(R.id.location_direction);
            btnLayout=mView.findViewById(R.id.btnLayout);
            msgLayout=mView.findViewById(R.id.rl_msg);
            location_lat=mView.findViewById(R.id.location_lat);
            location_lng=mView.findViewById(R.id.location_lng);
            location_accuracy=mView.findViewById(R.id.location_accuracy);
            ArrayList<String> divData=getDirDrop();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FieldStaffPlantingCaneArea.this,
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
                        new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                    }
                    dialogPopup.dismiss();
                }
            });
        }
        catch(SecurityException e)
        {
            new AlertDialogManager().RedDialog(context,"Security Error:"+e.toString());
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }

    private ArrayList<String> getDirDrop()
    {
        ArrayList<String> divData=new ArrayList<String>();
        if(currentDistance==1)
        {
            divData.add("East");
            divData.add("West");
            divData.add("North");
            divData.add("South");
        }
        else if(currentDistance==2)
        {
            if(Corner1.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("North");
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("North");
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("East");
                divData.add("West");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("East");
                divData.add("West");
            }
        }
        else if(currentDistance==3)
        {
            if(Corner1.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("West");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("East");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("North");
            }
        }
        else if(currentDistance==4)
        {
            if(Corner2.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("West");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("East");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("South");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("North");
            }
        }
        return divData;
    }

    public void captureCoordinate(View v)
    {
        try {
            if (StrEastNorthLat.equalsIgnoreCase("0.0") || StrEastSouthLat.equalsIgnoreCase("0.0")||StrWestSouthLat.equalsIgnoreCase("0.0")||StrWestNorthLat.equalsIgnoreCase("0.0")||
                    StrEastNorthLng.equalsIgnoreCase("0.0")||StrEastSouthLng.equalsIgnoreCase("0.0")||StrWestSouthLng.equalsIgnoreCase("0.0")||StrWestNorthLng.equalsIgnoreCase("0.0")){
                if (new GpsCheck(context).isGpsEnable())
                {
                    //findLocation();
                    getCurrentLocation();
                }else {
                    new AlertDialogManager().AlertPopUp(context,"Please Enable Your Gps");
                }
            }else {

                new AlertDialogManager().showToast((Activity) context,"All Coordinate Already Captured First Reset Than Capture");

            }

            /*findLocation();
            getCurrentLocation();*/
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Capture Coordinate Error:"+e.toString());
        }
    }

    /*private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                Lat = location.getLatitude();
                Lng = location.getLongitude();
                Location currentLocation = new Location("");
                currentLocation.setLatitude(Lat);
                currentLocation.setLongitude(Lng);
                Accuracy = location.getAccuracy();
                setLatDialogue("" + Lat, "" + Lng, "" + Accuracy);
                *//*final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                    }
                }, 5000);*//*
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Location Changed Error:"+e.toString());
            }
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            //t_master_latlng.setText("Provider status changed");
        }

        public void onProviderDisabled(String s) {
            //t_master_latlng.setText("Provider disabled by the user. GPS turned off");
        }

        public void onProviderEnabled(String s) {
            *//*Toast.makeText(context,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();*//*
        }
    }*/

    /*private void findLocation()
    {
        //startLocationUpdates();
        try {
            //startLocationUpdates();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());
        }
        catch (SecurityException e)
        {
            new AlertDialogManager().RedDialog(context,"Security Error:"+e.toString());
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }*/


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
            //AreaHec.setText("0");

        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Security Error:"+e.toString());
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
            //AreaMeter.setText("0");
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


    public void clearCoordinate(View v)
    {
        clearCoordinates();
    }


    public void clearCoordinates()
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
        AreaMeter.setText("");
        AreaHec.setText("");
        StrEastNorthLat="0.0";
        StrEastSouthLat="0.0";
        StrWestSouthLat="0.0";
        StrWestNorthLat="0.0";
        StrEastNorthLng="0.0";
        StrEastSouthLng="0.0";
        StrWestSouthLng="0.0";
        StrWestNorthLng="0.0";
    }


    private void setLatDialogue(String lt, String ln, String acr)
    {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void openCam(View v)
    {
        try {
            if (dbh.getControlStatus("MAREA", "PLANTING").equalsIgnoreCase("1")) {
                camera();

            }else {
                boolean inside=false;
                if(Double.parseDouble(StrEastNorthLat)>0)
                {
                    if(Double.parseDouble(StrEastSouthLat)>0)
                    {
                        if(Double.parseDouble(StrWestSouthLat)>0)
                        {
                            if(Double.parseDouble(StrWestNorthLat)>0)
                            {
                                List<LatLng> latLngList = new ArrayList<>();
                                latLngList.add(new LatLng(Double.parseDouble(StrEastNorthLat), Double.parseDouble(StrEastNorthLng)));
                                latLngList.add(new LatLng(Double.parseDouble(StrEastSouthLat), Double.parseDouble(StrEastSouthLng)));
                                latLngList.add(new LatLng(Double.parseDouble(StrWestSouthLat), Double.parseDouble(StrWestSouthLng)));
                                latLngList.add(new LatLng(Double.parseDouble(StrWestNorthLat), Double.parseDouble(StrWestNorthLng)));
                                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    //double lats=Double.parseDouble("28.541606");
                                    //double lngs=Double.parseDouble("78.737747");
                                    double lats = location.getLatitude();
                                    double lngs = location.getLongitude();
                                    LatLng latlng = new LatLng(lats, lngs);
                                    inside = PolyUtil.containsLocation(latlng, latLngList, true);
                                    inside=true;
                                    if(inside)
                                    {
                                        camera();
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Sorry you are not inside captured plot");
                                    }
                                }
                                else
                                {
                                    new AlertDialogManager().RedDialog(context,"Geolocation not found");
                                }
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(context,"4th coordinate not captured");
                            }
                        }
                        else
                        {
                            new AlertDialogManager().RedDialog(context,"3rd coordinate not captured");
                        }
                    }
                    else
                    {
                        new AlertDialogManager().RedDialog(context,"2nd coordinate not captured");
                    }
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,"1st coordinate not captured");
                }


            }


        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    void  camera(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt=dateFormat.format(today);
        filename = "image"+currentDt+".jpg";
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
        startActivityForResult(intent,RC_CAMERA_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if(file.exists())
                {
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
                }
                else
                {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context,"Error:" + e.toString());
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
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
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
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }

    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                FieldStaffPlantingCaneArea.this);
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
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                FieldStaffPlantingCaneArea.this);
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



    public void findPlot(View view){
        try {


            //  ShowMap();


            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                            FieldStaffPlantingCaneArea.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        new DownloadMasterData().DownloadMaster(context);
                                    }
                                    // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, StaffPlantingFindPlotMapview.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);


            /*    /////--------------Survey VeritalCkeck Activity---------------///////////
                Intent intent = new Intent(context, PlantingCaneAreaFindPlot.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);*/

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        listStatus=new SessionConfig(context).getListFound();
        Log.d("listStatus",""+listStatus);
        //  Log.d("chcekPlantingPolygonGrowerModelList",""+chcekPlantingPolygonGrowerModelList.get(0).getGfName());
        if (listStatus.equalsIgnoreCase("1")) {

            if (chcekPlantingPolygonGrowerModelList.size()>0){
                StrEastNorthLat = String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhNeLat());
                StrEastSouthLat =String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhSeLat());
                StrWestSouthLat =String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhSwLat());
                StrWestNorthLat =String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhNwLat());

                StrEastNorthLng= String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhNeLng());
                StrEastSouthLng = String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhSeLng());
                StrWestSouthLng =String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhSwLng());
                StrWestNorthLng =String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGhNwLng());

                Lat1.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhNeLat());
                Lng1.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhNeLng());

                Lat2.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhNwLat());
                Lng2.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhNwLng());

                Lat3.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhSwLat());
                Lng3.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhSwLng());

                Lat4.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhSeLat());
                Lng4.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhSeLng());

                Corner1.setText("East");
                Corner2.setText("North");
                Corner3.setText("West");
                Corner4.setText("South");

                Distance1.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhEast());
                Distance2.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhWest());
                Distance3.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhNorth());
                Distance4.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getGhSouth());

                Accuracy1.setText("01.00 M");
                Accuracy2.setText("01.00 M");
                Accuracy3.setText("01.00 M");
                Accuracy4.setText("01.00 M");

                AreaHec.setText(""+chcekPlantingPolygonGrowerModelList.get(0).getCanearea());

                String v_Code= input_village_code.getText().toString();
                String g_Code=  input_grower_code.getText().toString();
                String p_v_Code=  input_plot_village_code.getText().toString();

                String VCode= String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getVillageId());
                String GCode= String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getGCode());
                String PlotVCode= String.valueOf(chcekPlantingPolygonGrowerModelList.get(0).getPlotvillage());

               /* if (input_village_code.getText().toString().isEmpty() ||  input_grower_code.getText().toString().isEmpty() || input_plot_village_code.getText().toString().isEmpty()){

                }else {
                    if (v_Code.equalsIgnoreCase(VCode)||g_Code.equalsIgnoreCase(GCode) ||p_v_Code.equalsIgnoreCase(PlotVCode)){
                        new AlertDialogManager().AlertPopUp(context,"This Village Is Already assigned");
                    }

                }*/
            }

        }else{
            StrEastNorthLat = "0.0";
            StrEastSouthLat ="0.0";
            StrWestSouthLat ="0.0";
            StrWestNorthLat ="0.0";

            StrEastNorthLng= "0.0";
            StrEastSouthLng ="0.0";
            StrWestSouthLng ="0.0";
            StrWestNorthLng ="0.0";

            Lat1.setText("");
            Lng1.setText("");

            Lat2.setText("");
            Lng2.setText("");

            Lat3.setText("");
            Lng3.setText("");

            Lat4.setText("");
            Lng4.setText("");

            Corner1.setText("");
            Corner2.setText("");
            Corner3.setText("");
            Corner4.setText("");

            Accuracy1.setText("");
            Accuracy2.setText("");
            Accuracy3.setText("");
            Accuracy4.setText("");

            AreaHec.setText("");

            Distance1.setText("");
            Distance2.setText("");
            Distance3.setText("");
            Distance4.setText("");

        }
    }




}
