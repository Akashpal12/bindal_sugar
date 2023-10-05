package in.co.vibrant.bindalsugar.view.supervisor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.MasterModeAutoSearchAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageAutoSearchAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class StaffNewPlotEntry extends AppCompatActivity {

    boolean flag;
    public int loopval = 0;
    ListView lv;
    Boolean IsGetCordActive;
    public int GPSCALL = 0;
    public Double Latitude = null;
    public int gpsloopcount = 0;
    public int gpschangecount = 0;
    private LocationListener locationListener = null;
    public Boolean GotCordinates = Boolean.valueOf(false);
    int k = 0;
    /* access modifiers changed from: private */
    public Double Longitude = null;
    private Runnable mMyRunnable = new Runnable() {
        public void run() {
            IsGetCordActive = Boolean.valueOf(true);
        }
    };
    /* access modifiers changed from: private */
    public float naccuracy = 100.0f;
    /* access modifiers changed from: private */
    public ProgressBar pb = null;
    int retval = 0;
    long startTime = System.currentTimeMillis();
    int[] to;

    String TAG = "MasterData";
    private static final double EARTH_RADIUS = 6371000;// meters

    LinearLayout btnLayout;
    RelativeLayout msgLayout;

    AlertDialog dialogPopup;

    TextView Corner1, Lat1, Lng1, Distance1, Accuracy1, Corner2, Lat2, Lng2, Distance2, Accuracy2, Corner3, Lat3, Lng3, Distance3, Accuracy3, Corner4, Lat4, Lng4, Distance4, Accuracy4;
    int currentDistance = 1;
    double currentAccuracy = 200000;

    EditText AreaMeter, AreaHec, khashraNumber, gataNumber;
    RelativeLayout rl_master_coordinate;
    TextView t_master_latlng, t_master_acc;

    List<VillageSurveyModel> villageSurveyModelList;
    AutoCompleteTextView villageCode;
    VillageAutoSearchAdapter villageAutoSearchAdapter;

    List<MasterCaneSurveyModel> masterVarietyModelList, masterCaneTypeModelList, masterMixCropModelList, masterInsectModelList, masterSeedSourceModelList, masterPlantMethodModelList, masterCropConditionModelList, masterDiseaseModelList, masterIrrigationModelList, masterSoilTypeModelList, masterLandTypeModelList, masterBorderCropModelList, masterPlotTypeModelList, masterInterCropModelList;
    Spinner variety, /*cane_type,*/
            mix_crop, insect, seed_source, plant_method, crop_condition, disease, irrigation, soil_type, land_type, border_crop, plot_type, inter_crop;

    RelativeLayout rl_grower, rl_plot_sr_no, rl_khashra_number, rl_gata_number, rl_village, rl_area, rl_mix_crop, rl_insect, rl_seed_source,
            rl_plant_method, rl_crop_condition, rl_disease, rl_irrigation, rl_soil_type, rl_land_type, rl_border_crop, rl_plot_type, rl_inter_crop, rl_plant_date, rl_ghashti_number;
    MasterModeAutoSearchAdapter masterModeAutoSearchAdapter;

    EditText plot_sr_no, plant_date, ghashti_number, growerCode;
    TextView location_lat, location_lng, location_accuracy;

    PlotSurveyModel savePlotSurveyModel = new PlotSurveyModel();

    //LinearLayout rl_share;

    String StrPlotSrNo = "", StrAreaMeter = "0.0", StrAreaHec = "0.0", StrKhashraNumber = "", StrGataNumber = "", StrVillageCode = "", StrVarietyCode = "", StrCaneTypeCode = "", StrMixCrop = "", StrInsect = "",
            StrSeedSource = "", StrPlantMethod = "", StrCropCondition = "", StrDisease = "", StrIrrigation = "", StrSoilType = "", StrLandType = "",
            StrBorderCrop = "", StrPlotType = "", StrInterCrop = "", StrPlantDate = "", StrGhashtiNumber = "",
            StrEastNorthLat = "0.0", StrEastNorthLng = "0.0", StrEastNorthDistance = "", StrEastNorthAccuracy = "", StrWestNorthLat = "0.0",
            StrWestNorthLng = "0.0", StrWestNorthDistance = "", StrWestNorthAccuracy = "", StrEastSouthLat = "0.0", StrEastSouthLng = "0.0",
            StrEastSouthDistance = "", StrEastSouthAccuracy = "", StrWestSouthLat = "0.0", StrWestSouthLng = "0.0", StrWestSouthDistance = "", StrWestSouthAccuracy = "";

    List<Location> locationList;

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> factoryModelList;
    List<ControlSurveyModel> controlSurveyModelList;
    int PERMISSION_ID = 44;
    double Lat, Lng, Accuracy, LastLat = 0, LastLng = 0, dist = 0;
    //private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Handler mHandler;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    MyLocationListener myLocationListener = new MyLocationListener();
    int iLocationn = 0;

    androidx.appcompat.app.AlertDialog dialog;

    Context context;
    String masterVillCode, masterGrowCode;
    double masterArea, masterMinimumArea, masterMaximumArea;
    AlertDialog Alertdialog;
    //GPSTracker gps;

    //private LocationListener locListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_plot_entry);
        context = this;
        dbh = new DBHelper(this);
        factoryModelList = new ArrayList<>();
        factoryModelList = dbh.getUserDetailsModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            setTitle(getString(R.string.MENU_NEW_PLOT_ENTRY));
            toolbar.setTitle(getString(R.string.MENU_NEW_PLOT_ENTRY));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            controlSurveyModelList = dbh.getControlSurveyModel("");
            rl_grower = findViewById(R.id.rl_grower);
            rl_plot_sr_no = findViewById(R.id.rl_plot_sr_no);
            rl_khashra_number = findViewById(R.id.rl_khashra_number);
            rl_gata_number = findViewById(R.id.rl_gata_number);
            rl_village = findViewById(R.id.rl_village);
            rl_area = findViewById(R.id.rl_area);
            rl_mix_crop = findViewById(R.id.rl_mix_crop);
            rl_insect = findViewById(R.id.rl_insect);
            rl_seed_source = findViewById(R.id.rl_seed_source);
            rl_plant_method = findViewById(R.id.rl_plant_method);
            rl_crop_condition = findViewById(R.id.rl_crop_condition);
            rl_disease = findViewById(R.id.rl_disease);
            rl_irrigation = findViewById(R.id.rl_irrigation);
            rl_soil_type = findViewById(R.id.rl_soil_type);
            rl_land_type = findViewById(R.id.rl_land_type);
            rl_border_crop = findViewById(R.id.rl_border_crop);
            rl_plot_type = findViewById(R.id.rl_plot_type);
            rl_inter_crop = findViewById(R.id.rl_inter_crop);
            rl_ghashti_number = findViewById(R.id.rl_ghashti_number);
            rl_plant_date = findViewById(R.id.rl_plant_date);
            //rl_share=findViewById(R.id.rl_share);

            locationList = new ArrayList<>();
            villageCode = findViewById(R.id.village_code);
            growerCode = findViewById(R.id.grower_code);
            variety = findViewById(R.id.variety);
            //cane_type = findViewById(R.id.cane_type);
            mix_crop = findViewById(R.id.mix_crop);
            insect = findViewById(R.id.insect);
            seed_source = findViewById(R.id.seed_source);
            plant_method = findViewById(R.id.plant_method);
            crop_condition = findViewById(R.id.crop_condition);
            disease = findViewById(R.id.disease);
            irrigation = findViewById(R.id.irrigation);
            soil_type = findViewById(R.id.soil_type);
            land_type = findViewById(R.id.land_type);
            border_crop = findViewById(R.id.border_crop);
            plot_type = findViewById(R.id.plot_type);
            inter_crop = findViewById(R.id.inter_crop);


            plant_date = findViewById(R.id.plant_date);

            rl_master_coordinate = findViewById(R.id.rl_master_coordinate);
            t_master_latlng = findViewById(R.id.t_master_latlng);
            t_master_acc = findViewById(R.id.t_master_acc);


            plot_sr_no = findViewById(R.id.plot_sr_no);
            ghashti_number = findViewById(R.id.ghashti_number);
            khashraNumber = findViewById(R.id.khashra_number);
            gataNumber = findViewById(R.id.gata_number);
            AreaMeter = findViewById(R.id.area_meter);
            AreaHec = findViewById(R.id.area_hec);

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

            Corner1.setText("");
            Corner2.setText("");
            Corner3.setText("");
            Corner4.setText("");

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            initAutoSearchAndParameter();
            initCheckValidation();
            //getVIllageCode();
            //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            //fusedLocationClient = LocationServices.getFusedLocationProviderClient(NewPlotEntry.this);

            //gps = new GPSTracker(NewPlotEntry.this);
            //findLocation();


            // check if GPS enabled
            //generatesharePer();
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(StaffNewPlotEntry.this, "Error:" + e.toString());
        }
    }

    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            Location location = locationResult.getLastLocation();
                            //t_master_latlng.setText(""+locationResult.getLastLocation().getAccuracy());
                            DecimalFormat decimalFormat = new DecimalFormat("##.00");
                            Lat = location.getLatitude();
                            Lng = location.getLongitude();
                            Location currentLocation = new Location("");
                            currentLocation.setLatitude(Lat);
                            currentLocation.setLongitude(Lng);
                            Accuracy = location.getAccuracy();
                        /*if(location.getAccuracy()>=8)
                        {
                            rl_master_coordinate.setBackgroundColor(Color.parseColor("#FFF44336"));
                        }
                        else
                        {
                            rl_master_coordinate.setBackgroundColor(Color.parseColor("#FF8BC34A"));
                        }*/
                            dist = 0;
                            if (LastLat > 0 && LastLng > 0) {
                                Location l1 = new Location("");
                                l1.setLatitude(LastLat);
                                l1.setLongitude(LastLng);
                                dist = l1.distanceTo(currentLocation);
                                //dist=getDistance(LastLat,LastLng,Lat,Lng);
                            }
                            if (Accuracy <= factoryModelList.get(0).getGpsAccuracy()) {
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
                            } else {
                                location_lat.setText("---");
                                location_lng.setText("---");
                                location_accuracy.setText("---");
                                msgLayout.setVisibility(View.VISIBLE);
                                btnLayout.setVisibility(View.GONE);
                            }
                            //t_master_latlng.setText(Lat+" | "+Lng +" - ("+decimalFormat.format(dist)+" M)");
                            //t_master_acc.setText(decimalFormat.format(Accuracy)+" M / "+decimalFormat.format(currentAccuracy)+" M");
                            if (currentAccuracy >= Accuracy) {
                                currentAccuracy = Accuracy;
                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException e) {

        }
    }

    /*private void getVIllageCode()
    {
        List<PlotSurveyModel> tempPlotSurveyModelList=dbh.getPlotSurveyModel("");
        if(tempPlotSurveyModelList.size()>0)
        {
            List<VillageModel> villageModelList1=dbh.getSurveyVillageModel(tempPlotSurveyModelList.get(0).getVillageCode());
            if(villageModelList1.size()>0)
            {
                villageCode.setText(villageModelList1.get(0).getVillageCode()+" - "+villageModelList1.get(0).getVillageName());
                StrVillageCode=villageModelList1.get(0).getVillageCode();
                List<PlotSurveyModel> plotSurveyModelList=dbh.getPlotSurveyModel(StrVillageCode);
                if(plotSurveyModelList.size()==0)
                {
                    if(villageModelList1.size()==0)
                    {
                        StrPlotSrNo="1";
                    }
                    else
                    {
                        StrPlotSrNo=""+(Integer.parseInt(villageModelList1.get(0).getPlotSrNo())+1);
                    }
                    List<VillageModel> villageModelListCheckPlotSrNo=dbh.getVillageModel(villageModelList1.get(0).getVillageCode());
                    if(Integer.parseInt(villageModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                    {
                        if(Integer.parseInt(villageModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
                        {

                        }
                        else
                        {
                            AlertPopUp("Plot serial number limit exceed");
                            StrPlotSrNo="";

                        }
                    }
                    else
                    {
                        AlertPopUp("Plot serial number not in range");
                        StrPlotSrNo="";
                    }
                }
                else
                {
                    StrPlotSrNo=""+(Integer.parseInt(plotSurveyModelList.get(0).getPlotSrNo())+1);
                    List<VillageModel> villageModelListCheckPlotSrNo=dbh.getVillageModel(plotSurveyModelList.get(0).getVillageCode());
                    if(Integer.parseInt(villageModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                    {
                        if(Integer.parseInt(villageModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
                        {

                        }
                        else
                        {
                            AlertPopUp("Plot serial number limit exceed");
                            StrPlotSrNo="";
                        }
                    }
                    else
                    {
                        AlertPopUp("Plot serial number not in range");
                        StrPlotSrNo="";
                    }
                }
                plot_sr_no.setText(StrPlotSrNo);
            }
        }

    }*/

    private void initCheckValidation() {
        ControlSurveyModel controlSurveyModel = controlSurveyModelList.get(0);
        if (controlSurveyModel.getKhashraNo().equalsIgnoreCase("1")) {
            rl_khashra_number.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getGataNo().equalsIgnoreCase("1")) {
            rl_gata_number.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getHideArea().equalsIgnoreCase("0")) {
            rl_area.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getMixCrop().equalsIgnoreCase("1")) {
            rl_mix_crop.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getInsect().equalsIgnoreCase("1")) {
            rl_insect.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getSeedSource().equalsIgnoreCase("1")) {
            rl_seed_source.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getPlantMethod().equalsIgnoreCase("1")) {
            rl_plant_method.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getCropCond().equalsIgnoreCase("1")) {
            rl_crop_condition.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getDisease().equalsIgnoreCase("1")) {
            rl_disease.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getPlantDate().equalsIgnoreCase("1")) {
            rl_plant_date.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getIrrigation().equalsIgnoreCase("1")) {
            rl_irrigation.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getSoilType().equalsIgnoreCase("1")) {
            rl_soil_type.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getLandType().equalsIgnoreCase("1")) {
            rl_land_type.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getBorderCrop().equalsIgnoreCase("1")) {
            rl_border_crop.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getPlotType().equalsIgnoreCase("1")) {
            rl_plot_type.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getGhashtiNo().equalsIgnoreCase("1")) {
            rl_ghashti_number.setVisibility(View.VISIBLE);
        }
        if (controlSurveyModel.getInterCrop().equalsIgnoreCase("1")) {
            rl_inter_crop.setVisibility(View.VISIBLE);
        }

    }


    public void Exit(View v) {
        //generatesharePer();
        finish();
    }

    public void saveData(View v) {
        ControlSurveyModel controlSurveyModel = controlSurveyModelList.get(0);
        labelsavedata:
        {
            StrPlotSrNo = plot_sr_no.getText().toString();
            if (StrPlotSrNo.length() == 0) {
                AlertPopUp("Enter plot serial number");
                villageCode.requestFocus();
                break labelsavedata;
            }
            if (StrVillageCode.length() == 0) {
                AlertPopUp("Enter village code");
                villageCode.requestFocus();
                break labelsavedata;
            }
            if (controlSurveyModel.getKhashraNo().equalsIgnoreCase("1")) {
                StrKhashraNumber = khashraNumber.getText().toString();
                if (StrKhashraNumber.length() == 0) {
                    AlertPopUp("Enter khashra number");
                    khashraNumber.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getGataNo().equalsIgnoreCase("1")) {
                StrGataNumber = gataNumber.getText().toString();
                if (StrGataNumber.length() == 0) {
                    AlertPopUp("Enter gata number");
                    gataNumber.requestFocus();
                    break labelsavedata;
                }
            }
            if (StrVarietyCode.length() == 0) {
                StrVarietyCode = masterVarietyModelList.get(variety.getSelectedItemPosition() - 1).getCode();
                AlertPopUp("Enter variety");
                variety.requestFocus();
                break labelsavedata;
            }
            if (controlSurveyModel.getMixCrop().equalsIgnoreCase("1")) {
                if (StrMixCrop.length() == 0) {
                    StrMixCrop = masterMixCropModelList.get(mix_crop.getSelectedItemPosition() - 1).getCode();
                    AlertPopUp("Enter mix crop");
                    mix_crop.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getInsect().equalsIgnoreCase("1")) {
                StrInsect = masterInsectModelList.get(insect.getSelectedItemPosition() - 1).getCode();
                if (StrInsect.length() == 0) {
                    AlertPopUp("Enter insect");
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getSeedSource().equalsIgnoreCase("1")) {
                StrSeedSource = masterSeedSourceModelList.get(seed_source.getSelectedItemPosition() - 1).getCode();
                if (StrSeedSource.length() == 0) {
                    AlertPopUp("Enter seed source");
                    seed_source.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getPlantMethod().equalsIgnoreCase("1")) {
                StrPlantMethod = masterPlantMethodModelList.get(plant_method.getSelectedItemPosition() - 1).getCode();
                if (StrPlantMethod.length() == 0) {
                    AlertPopUp("Enter plant method");
                    plant_method.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getCropCond().equalsIgnoreCase("1")) {
                StrCropCondition = masterCropConditionModelList.get(crop_condition.getSelectedItemPosition() - 1).getCode();
                if (StrCropCondition.length() == 0) {
                    AlertPopUp("Enter crop condition");
                    crop_condition.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getDisease().equalsIgnoreCase("1")) {
                StrDisease = masterDiseaseModelList.get(disease.getSelectedItemPosition() - 1).getCode();
                if (StrDisease.length() == 0) {
                    AlertPopUp("Enter disease");
                    disease.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getPlantDate().equalsIgnoreCase("1")) {
                StrPlantDate = plant_date.getText().toString();
                if (StrPlantDate.length() == 0) {
                    AlertPopUp("select plant date");
                    plant_date.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getIrrigation().equalsIgnoreCase("1")) {
                StrIrrigation = masterIrrigationModelList.get(irrigation.getSelectedItemPosition() - 1).getCode();
                if (StrIrrigation.length() == 0) {
                    AlertPopUp("Enter irrigation");
                    irrigation.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getSoilType().equalsIgnoreCase("1")) {
                StrSoilType = masterSoilTypeModelList.get(soil_type.getSelectedItemPosition() - 1).getCode();
                if (StrSoilType.length() == 0) {
                    AlertPopUp("Enter soil type");
                    soil_type.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getLandType().equalsIgnoreCase("1")) {
                StrLandType = masterLandTypeModelList.get(land_type.getSelectedItemPosition() - 1).getCode();
                if (StrLandType.length() == 0) {
                    AlertPopUp("Enter land type");
                    land_type.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getBorderCrop().equalsIgnoreCase("1")) {
                StrBorderCrop = masterBorderCropModelList.get(border_crop.getSelectedItemPosition() - 1).getCode();
                if (StrBorderCrop.length() == 0) {
                    AlertPopUp("Enter border crop");
                    border_crop.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getPlotType().equalsIgnoreCase("1")) {
                StrPlotType = masterPlotTypeModelList.get(plot_type.getSelectedItemPosition() - 1).getCode();
                if (StrPlotType.length() == 0) {
                    AlertPopUp("Enter plot type");
                    plot_type.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getGhashtiNo().equalsIgnoreCase("1")) {
                StrGhashtiNumber = ghashti_number.getText().toString();
                if (StrGhashtiNumber.length() == 0) {
                    AlertPopUp("Enter ghashti number");
                    ghashti_number.requestFocus();
                    break labelsavedata;
                }
            }
            if (controlSurveyModel.getInterCrop().equalsIgnoreCase("1")) {
                StrInterCrop = masterInterCropModelList.get(inter_crop.getSelectedItemPosition() - 1).getCode();
                if (StrInterCrop.length() == 0) {
                    AlertPopUp("Enter inter crop");
                    inter_crop.requestFocus();
                    break labelsavedata;
                }
            }
            if (Lat1.getText().toString().length() == 0) {
                AlertPopUp("capture first location");
                break labelsavedata;
            }
            if (Lat2.getText().toString().length() == 0) {
                AlertPopUp("capture second location");
                break labelsavedata;
            }
            if (Lat3.getText().toString().length() == 0) {
                AlertPopUp("capture third location");
                break labelsavedata;
            }
            if (Lat4.getText().toString().length() == 0) {
                AlertPopUp("capture fourth location or skip it");
                break labelsavedata;
            }
            insertData();
        }
    }

    private void insertData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date today = Calendar.getInstance().getTime();
        String currDate = dateFormat.format(today);
        savePlotSurveyModel.setPlotSrNo(StrPlotSrNo);
        savePlotSurveyModel.setAreaMeter(StrAreaMeter);
        savePlotSurveyModel.setAreaHectare(StrAreaHec);
        savePlotSurveyModel.setKhashraNo(StrKhashraNumber);
        savePlotSurveyModel.setGataNo(StrGataNumber);
        savePlotSurveyModel.setVillageCode(StrVillageCode);
        savePlotSurveyModel.setMixCrop(StrMixCrop);
        savePlotSurveyModel.setInsect(StrInsect);
        savePlotSurveyModel.setSeedSource(StrSeedSource);
        savePlotSurveyModel.setPlantMethod(StrPlantMethod);
        savePlotSurveyModel.setCropCondition(StrCropCondition);
        savePlotSurveyModel.setDisease(StrDisease);
        savePlotSurveyModel.setIrrigation(StrIrrigation);
        savePlotSurveyModel.setSoilType(StrSoilType);
        savePlotSurveyModel.setLandType(StrLandType);
        savePlotSurveyModel.setBorderCrop(StrBorderCrop);
        savePlotSurveyModel.setPlotType(StrPlotType);
        savePlotSurveyModel.setInterCrop(StrInterCrop);
        savePlotSurveyModel.setAadharNumber("");
        savePlotSurveyModel.setPlantDate(StrPlantDate);
        savePlotSurveyModel.setGhashtiNumber(StrGhashtiNumber);
        savePlotSurveyModel.setEastNorthLat(StrEastNorthLat);
        savePlotSurveyModel.setEastNorthLng(StrEastNorthLng);
        savePlotSurveyModel.setEastNorthDistance(StrEastNorthDistance);
        savePlotSurveyModel.setEastNorthAccuracy(StrEastNorthAccuracy);
        savePlotSurveyModel.setWestNorthLat(StrWestNorthLat);
        savePlotSurveyModel.setWestNorthLng(StrWestNorthLng);
        savePlotSurveyModel.setWestNorthDistance(StrWestNorthDistance);
        savePlotSurveyModel.setWestNorthAccuracy(StrWestNorthAccuracy);
        savePlotSurveyModel.setEastSouthLat(StrEastSouthLat);
        savePlotSurveyModel.setEastSouthLng(StrEastSouthLng);
        savePlotSurveyModel.setEastSouthAccuracy(StrEastSouthAccuracy);
        savePlotSurveyModel.setEastSouthDistance(StrEastSouthDistance);
        savePlotSurveyModel.setWestSouthLat(StrWestSouthLat);
        savePlotSurveyModel.setWestSouthLng(StrWestSouthLng);
        savePlotSurveyModel.setWestSouthDistance(StrWestSouthDistance);
        savePlotSurveyModel.setWestSouthAccuracy(StrWestSouthAccuracy);
        savePlotSurveyModel.setVarietyCode(StrVarietyCode);
        savePlotSurveyModel.setCaneType(StrCaneTypeCode);
        savePlotSurveyModel.setInsertDate(currDate);
        new insertPlot().execute();
        //long insertId=dbh.insertPlotSurveyModel(plotSurveyModel);
        /*Intent intent=new Intent(StaffNewPlotEntry.this,StaffAddGrowerShare.class);
        intent.putExtra("survey_id",""+insertId);
        finish();
        startActivity(intent);*/

        //AlertPopUpWithFinish("Plot survey done survey number is "+StrPlotSrNo);
    }

    private void initAutoSearchAndParameter() {
        plot_sr_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (plot_sr_no.getText().toString().length() > 0) {
                        new checkPlotSerial().execute(plot_sr_no.getText().toString());
                    } else {
                        new AlertDialogManager().RedDialog(context, "Please enter plot serial number");
                    }
                }
            }
        });
        /*villageModelList=dbh.getSurveyVillageModel("");
        if(villageModelList.size()>0) {
            villageAutoSearchAdapter = new VillageAutoSearchAdapter(this, R.layout.all_list_row_item_search, villageModelList);
            villageCode.setThreshold(1);
            villageCode.setAdapter(villageAutoSearchAdapter);
            villageCode.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            VillageModel purcheyDataModel = (VillageModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            villageCode.setText(purcheyDataModel.getVillageCode()+" - "+purcheyDataModel.getVillageName());
                            StrVillageCode=purcheyDataModel.getVillageCode();
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            villageCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    villageModelList.clear();
                    villageModelList=dbh.getVillageModel("");
                    villageAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }*/

        masterVarietyModelList = dbh.getMasterModel("1");
        ArrayList<String> dataVarietyModelList = new ArrayList<String>();
        dataVarietyModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterVarietyModelList.size(); i++) {
            dataVarietyModelList.add(masterVarietyModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterVarietyModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataVarietyModelList);
        variety.setAdapter(adapterVarietyModelList);
        variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrVarietyCode = masterVarietyModelList.get(variety.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrVarietyCode = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        masterMixCropModelList = dbh.getMasterModel("12");
        ArrayList<String> dataMixCropModelList = new ArrayList<String>();
        dataMixCropModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterMixCropModelList.size(); i++) {
            dataMixCropModelList.add(masterMixCropModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterMixCropModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataMixCropModelList);
        mix_crop.setAdapter(adapterMixCropModelList);
        mix_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrMixCrop = masterMixCropModelList.get(mix_crop.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrMixCrop = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        masterInsectModelList = dbh.getMasterModel("13");
        ArrayList<String> dataInsectModelList = new ArrayList<String>();
        dataInsectModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterInsectModelList.size(); i++) {
            dataInsectModelList.add(masterInsectModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterInsectModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataInsectModelList);
        insect.setAdapter(adapterInsectModelList);
        insect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrInsect = masterInsectModelList.get(insect.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrInsect = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterSeedSourceModelList = dbh.getMasterModel("14");
        ArrayList<String> dataSeedSourceModelList = new ArrayList<String>();
        dataSeedSourceModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterSeedSourceModelList.size(); i++) {
            dataSeedSourceModelList.add(masterSeedSourceModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterSeedSourceModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataSeedSourceModelList);
        seed_source.setAdapter(adapterSeedSourceModelList);
        seed_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrSeedSource = masterSeedSourceModelList.get(seed_source.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrSeedSource = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterPlantMethodModelList = dbh.getMasterModel("3");
        ArrayList<String> dataPlantMethodModelList = new ArrayList<String>();
        dataPlantMethodModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterPlantMethodModelList.size(); i++) {
            dataPlantMethodModelList.add(masterPlantMethodModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterPlantMethodModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataPlantMethodModelList);
        plant_method.setAdapter(adapterPlantMethodModelList);
        plant_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrPlantMethod = masterPlantMethodModelList.get(plant_method.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrPlantMethod = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        masterCropConditionModelList = dbh.getMasterModel("4");
        ArrayList<String> dataCropConditionModelList = new ArrayList<String>();
        dataCropConditionModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterCropConditionModelList.size(); i++) {
            dataCropConditionModelList.add(masterCropConditionModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterCropConditionModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataCropConditionModelList);
        crop_condition.setAdapter(adapterCropConditionModelList);
        crop_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrCropCondition = masterCropConditionModelList.get(crop_condition.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrCropCondition = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        masterDiseaseModelList = dbh.getMasterModel("5");
        ArrayList<String> dataDiseaseModelList = new ArrayList<String>();
        dataDiseaseModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterDiseaseModelList.size(); i++) {
            dataDiseaseModelList.add(masterDiseaseModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterDiseaseModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataDiseaseModelList);
        disease.setAdapter(adapterDiseaseModelList);
        disease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrDisease = masterDiseaseModelList.get(disease.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrDisease = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterIrrigationModelList = dbh.getMasterModel("6");
        ArrayList<String> dataIrrigationModelList = new ArrayList<String>();
        dataIrrigationModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterIrrigationModelList.size(); i++) {
            dataIrrigationModelList.add(masterIrrigationModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterIrrigationModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataIrrigationModelList);
        irrigation.setAdapter(adapterIrrigationModelList);
        irrigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrIrrigation = masterIrrigationModelList.get(irrigation.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrIrrigation = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterSoilTypeModelList = dbh.getMasterModel("7");
        ArrayList<String> dataSoilTypeModelList = new ArrayList<String>();
        dataSoilTypeModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterSoilTypeModelList.size(); i++) {
            dataSoilTypeModelList.add(masterSoilTypeModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterSoilTypeModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataSoilTypeModelList);
        soil_type.setAdapter(adapterSoilTypeModelList);
        soil_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrSoilType = masterSoilTypeModelList.get(soil_type.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrSoilType = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterLandTypeModelList = dbh.getMasterModel("8");
        ArrayList<String> dataLandTypeModelList = new ArrayList<String>();
        dataLandTypeModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterLandTypeModelList.size(); i++) {
            dataLandTypeModelList.add(masterLandTypeModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterLandTypeModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataLandTypeModelList);
        land_type.setAdapter(adapterLandTypeModelList);
        land_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrLandType = masterLandTypeModelList.get(land_type.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrLandType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterBorderCropModelList = dbh.getMasterModel("9");
        ArrayList<String> dataBorderCropModelList = new ArrayList<String>();
        dataBorderCropModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterBorderCropModelList.size(); i++) {
            dataBorderCropModelList.add(masterBorderCropModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterBorderCropModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataBorderCropModelList);
        border_crop.setAdapter(adapterBorderCropModelList);
        border_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrBorderCrop = masterBorderCropModelList.get(border_crop.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrBorderCrop = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterPlotTypeModelList = dbh.getMasterModel("10");
        ArrayList<String> dataPlotTypeModelList = new ArrayList<String>();
        dataPlotTypeModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterPlotTypeModelList.size(); i++) {
            dataPlotTypeModelList.add(masterPlotTypeModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterPlotTypeModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataPlotTypeModelList);
        plot_type.setAdapter(adapterPlotTypeModelList);
        plot_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrPlotType = masterPlotTypeModelList.get(plot_type.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrPlotType = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        masterInterCropModelList = dbh.getMasterModel("11");
        ArrayList<String> dataInterCropModelList = new ArrayList<String>();
        dataInterCropModelList.add(getString(R.string.LBL_SELECT));
        for (int i = 0; i < masterInterCropModelList.size(); i++) {
            dataInterCropModelList.add(masterInterCropModelList.get(i).getName());
        }
        ArrayAdapter<String> adapterInterCropModelList = new ArrayAdapter<String>(context,
                R.layout.list_item, dataInterCropModelList);
        inter_crop.setAdapter(adapterInterCropModelList);
        inter_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    StrInterCrop = masterInterCropModelList.get(inter_crop.getSelectedItemPosition() - 1).getCode();
                } else {
                    StrInterCrop = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        plant_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(StaffNewPlotEntry.this, new DatePickerDialog.OnDateSetListener() {

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
                        plant_date.setText(temDate + "/" + temmonth + "/" + year);
                        StrPlantDate = temDate + "/" + temmonth + "/" + year;
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

    }

    private void findLocation() {
        //startLocationUpdates();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());
            //locationManager.requestLocationUpdates("gps", 500, 111.0f, new MyLocationListener());
            /*locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    500,
                    0,
                    new MyLocationListener()
            );*/
        } catch (SecurityException e) {

        } catch (Exception e) {

        }
    }

    public void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable").setCancelable(false).setTitle("** Gps Status **").setPositiveButton("Gps On", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent("android.settings.SECURITY_SETTINGS"));
                dialog.cancel();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private Boolean displayGpsStatus() {
        if (Settings.Secure.isLocationProviderEnabled(getBaseContext().getContentResolver(), "gps")) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }


    private void getCurrentLocation() {
        try {
            final DecimalFormat decimalFormat = new DecimalFormat("##");
            android.app.AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffNewPlotEntry.this);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_get_latlng, null);
            dialogbilder.setView(mView);
            final Spinner location_direction = mView.findViewById(R.id.location_direction);
            btnLayout = mView.findViewById(R.id.btnLayout);
            msgLayout = mView.findViewById(R.id.rl_msg);
            location_lat = mView.findViewById(R.id.location_lat);
            location_lng = mView.findViewById(R.id.location_lng);
            location_accuracy = mView.findViewById(R.id.location_accuracy);
            ArrayList<String> divData = getDirDrop();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaffNewPlotEntry.this,
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
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            Button skip_cancel = mView.findViewById(R.id.skip_cancel);
            if (currentDistance == 4) {
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
                        if (currentDistance == 1) {
                            Corner1.setText(location_direction.getSelectedItem().toString());
                            Lat1.setText("" + location_lat.getText().toString());
                            Lng1.setText("" + location_lng.getText().toString());
                            Accuracy1.setText("" + location_accuracy.getText().toString());
                            currentDistance++;
                        } else if (currentDistance == 2) {
                            Location sourceL = new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            Location destinationL = new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist = sourceL.distanceTo(destinationL);
                            Distance1.setText("" + decimalFormat.format(dist));
                            if (Double.parseDouble(controlSurveyModelList.get(0).getMinLength()) <= dist) {
                                if (Double.parseDouble(controlSurveyModelList.get(0).getMaxLength()) >= dist) {
                                    Corner2.setText(location_direction.getSelectedItem().toString());
                                    Lat2.setText("" + location_lat.getText().toString());
                                    Lng2.setText("" + location_lng.getText().toString());
                                    Accuracy2.setText("" + location_accuracy.getText().toString());
                                    currentDistance++;
                                } else {
                                    AlertPopUp("Distance should be less than " + controlSurveyModelList.get(0).getMaxLength());
                                }
                            } else {
                                AlertPopUp("Distance should be greater than " + controlSurveyModelList.get(0).getMinLength());
                            }
                        } else if (currentDistance == 3) {
                            Location sourceL = new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat2.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng2.getText().toString()));
                            Location destinationL = new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist = sourceL.distanceTo(destinationL);
                            Distance2.setText("" + decimalFormat.format(dist));
                            if (Double.parseDouble(controlSurveyModelList.get(0).getMinLength()) <= dist) {
                                if (Double.parseDouble(controlSurveyModelList.get(0).getMaxLength()) >= dist) {
                                    Corner3.setText(location_direction.getSelectedItem().toString());
                                    Lat3.setText("" + location_lat.getText().toString());
                                    Lng3.setText("" + location_lng.getText().toString());
                                    Accuracy3.setText("" + location_accuracy.getText().toString());
                                    currentDistance++;
                                } else {
                                    AlertPopUp("Distance should be less than " + controlSurveyModelList.get(0).getMaxLength());
                                }
                            } else {
                                AlertPopUp("Distance should be greater than " + controlSurveyModelList.get(0).getMinLength());
                            }
                        } else if (currentDistance == 4) {
                            Location sourceL = new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat3.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng3.getText().toString()));
                            Location destinationL = new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            Location startL = new Location("");
                            startL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            startL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            double dist = sourceL.distanceTo(destinationL);
                            double dist1 = destinationL.distanceTo(startL);
                            Distance3.setText("" + decimalFormat.format(dist));
                            Distance4.setText("" + decimalFormat.format(dist1));
                            if (Double.parseDouble(controlSurveyModelList.get(0).getMinLength()) <= dist && Double.parseDouble(controlSurveyModelList.get(0).getMinLength()) <= dist1) {
                                if (Double.parseDouble(controlSurveyModelList.get(0).getMaxLength()) >= dist && Double.parseDouble(controlSurveyModelList.get(0).getMaxLength()) >= dist1) {
                                    Corner4.setText(location_direction.getSelectedItem().toString());
                                    Lat4.setText("" + location_lat.getText().toString());
                                    Lng4.setText("" + location_lng.getText().toString());
                                    Accuracy4.setText("" + location_accuracy.getText().toString());
                                    currentDistance++;
                                    calArea();
                                } else {
                                    AlertPopUp("Distance should be less than " + controlSurveyModelList.get(0).getMaxLength());
                                }
                            } else {
                                AlertPopUp("Distance should be greater than " + controlSurveyModelList.get(0).getMinLength());
                            }
                        }
                    } catch (Exception e) {

                    }
                    dialogPopup.dismiss();
                }
            });
        } catch (SecurityException e) {

        }

    }

    private ArrayList<String> getDirDrop() {
        ArrayList<String> divData = new ArrayList<String>();
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
        return divData;
    }

    public void captureCoordinate(View v) {
        findLocation();
        getCurrentLocation();
    }


    private void calArea() {
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
        DecimalFormat decimalHecFormat = new DecimalFormat("##.000");
        double areaM = eastWestLength * northSouthLength;
        /*Double areaM=Double.parseDouble(Distance1.getText().toString())*Double.parseDouble(Distance2.getText().toString())*
            Double.parseDouble(Distance3.getText().toString())*Double.parseDouble(Distance4.getText().toString());*/
        AreaMeter.setText(decimalFormat.format(areaM));
        double ah = areaM / 10000;
        AreaHec.setText("" + decimalHecFormat.format(ah));
        mix_crop.requestFocus();
        StrAreaMeter = decimalFormat.format(areaM);
        //StrAreaHec=String.format("%f", ah);
        StrAreaHec = decimalHecFormat.format(ah);
        currentDistance++;
        currentAccuracy = 20000;
        if (ah < masterMinimumArea) {
            AreaHec.setText("");
            AreaMeter.setText("");
            clearAreaCoordinate();
            new AlertDialogManager().RedDialog(context, "Area should be greater than " + decimalHecFormat.format(masterMinimumArea));
        }
        if (ah > masterMaximumArea) {
            AreaHec.setText("");
            AreaMeter.setText("");
            clearAreaCoordinate();
            new AlertDialogManager().RedDialog(context, "Area should be less than " + decimalHecFormat.format(masterMaximumArea));
        }

    }


    private void calAreaTriangle() {
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
        mix_crop.requestFocus();
        StrAreaMeter = decimalFormat.format(areaM);
        StrAreaHec = "" + ah;
        currentDistance++;
        currentAccuracy = 20000;
    }

    public void mapViewPlot(View v) {
        if (Double.parseDouble(StrEastNorthLat) > 0) {
            if (Double.parseDouble(StrEastSouthLat) > 0) {
                if (Double.parseDouble(StrWestSouthLat) > 0) {
                    if (Double.parseDouble(StrWestNorthLat) > 0) {
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

                                ArrayList<LatLng> latLngs = new ArrayList<>();
                                latLngs.add(new LatLng(Double.parseDouble(StrEastNorthLat), Double.parseDouble(StrEastNorthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrEastSouthLat), Double.parseDouble(StrEastSouthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrWestSouthLat), Double.parseDouble(StrWestSouthLng)));
                                latLngs.add(new LatLng(Double.parseDouble(StrWestNorthLat), Double.parseDouble(StrWestNorthLng)));
                                LatLng midPoint = new LatLngUtil().getPolygonCenterPoint(latLngs);
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

                        Button closeMap = mView.findViewById(R.id.closeMap);
                        closeMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alertdialog.dismiss();
                            }
                        });

                        Alertdialog.show();
                        Alertdialog.setCancelable(false);
                        Alertdialog.setCanceledOnTouchOutside(true);


                    } else {
                        new AlertDialogManager().RedDialog(context, "4th lat lng not find");
                    }
                } else {
                    new AlertDialogManager().RedDialog(context, "3rd lat lng not find");
                }
            } else {
                new AlertDialogManager().RedDialog(context, "2nd lat lng not find");
            }
        } else {
            new AlertDialogManager().RedDialog(context, "1st lat lng not find");
        }

    }

    public void clearCoordinate(View v) {
        clearAreaCoordinate();
    }

    public void clearAreaCoordinate() {
        //findLocation();
        LastLat = 0;
        LastLng = 0;
        currentDistance = 1;
        currentAccuracy = 20000;
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
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
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

        public void onStatusChanged(String s, int i, Bundle b) {
            t_master_latlng.setText("Provider status changed");
        }

        public void onProviderDisabled(String s) {
            t_master_latlng.setText("Provider disabled by the user. GPS turned off");
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(StaffNewPlotEntry.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }


    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffNewPlotEntry.this);
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


    private void setLatDialogue(String lt, String ln, String acr) {
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
        if (Accuracy <= factoryModelList.get(0).getGpsAccuracy()) {
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
        } else {
            location_lat.setText("---");
            location_lng.setText("---");
            location_accuracy.setText("---");
            msgLayout.setVisibility(View.VISIBLE);
            btnLayout.setVisibility(View.GONE);
        }
        if (currentAccuracy >= Accuracy) {
            currentAccuracy = Accuracy;
        }
    }


    private class checkPlotSerial extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetPlotArea);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("PL_SRNO", params[0]);
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetPlotArea, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetPlotAreaResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    if (jsonArray.length() > 0) {
                        JSONObject object = jsonArray.getJSONObject(0);
                        masterVillCode = object.getString("VILL");
                        masterGrowCode = object.getString("GNO");
                        masterArea = object.getDouble("AREA");
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        double areaPer = (masterArea * Double.parseDouble(object.getString("AREA_PER"))) / 100;
                        masterMinimumArea = masterArea - Double.parseDouble(decimalFormat.format(areaPer));
                        masterMaximumArea = masterArea + Double.parseDouble(decimalFormat.format(areaPer));
                        villageCode.setText(masterVillCode);
                        growerCode.setText(masterGrowCode);
                        StrVillageCode = masterVillCode;
                        StrPlotSrNo = plot_sr_no.getText().toString();
                    }
                } else {
                    formReset();
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    private class insertPlot extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_InsertPlotSurvey);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("plot_sr_number", savePlotSurveyModel.getPlotSrNo());
                request1.addProperty("khashra_no", savePlotSurveyModel.getKhashraNo());
                request1.addProperty("gataNo", savePlotSurveyModel.getGataNo());
                request1.addProperty("villageCode", savePlotSurveyModel.getVillageCode());
                request1.addProperty("areaMeter", savePlotSurveyModel.getAreaMeter());
                request1.addProperty("areaHectare", savePlotSurveyModel.getAreaHectare());
                request1.addProperty("mixCrop", savePlotSurveyModel.getMixCrop());
                request1.addProperty("insect", savePlotSurveyModel.getInsect());
                request1.addProperty("seedSource", savePlotSurveyModel.getSeedSource());
                request1.addProperty("aadharNumber", savePlotSurveyModel.getAadharNumber());
                request1.addProperty("plantMethod", savePlotSurveyModel.getPlantMethod());
                request1.addProperty("cropCondition", savePlotSurveyModel.getCropCondition());
                request1.addProperty("disease", savePlotSurveyModel.getDisease());
                request1.addProperty("plantDate", savePlotSurveyModel.getPlantDate());
                request1.addProperty("irrigation", savePlotSurveyModel.getIrrigation());
                request1.addProperty("soilType", savePlotSurveyModel.getSoilType());
                request1.addProperty("landType", savePlotSurveyModel.getLandType());
                request1.addProperty("borderCrop", savePlotSurveyModel.getBorderCrop());
                request1.addProperty("plotType", savePlotSurveyModel.getPlotType());
                request1.addProperty("ghashtiNumber", savePlotSurveyModel.getGhashtiNumber());
                request1.addProperty("interCrop", savePlotSurveyModel.getInterCrop());
                request1.addProperty("east_lat", savePlotSurveyModel.getEastSouthLat());
                request1.addProperty("east_lng", savePlotSurveyModel.getEastSouthLng());
                request1.addProperty("east_distance", savePlotSurveyModel.getEastSouthDistance());
                request1.addProperty("east_accuracy", savePlotSurveyModel.getEastSouthAccuracy());
                request1.addProperty("south_lat", savePlotSurveyModel.getWestSouthLat());
                request1.addProperty("south_lng", savePlotSurveyModel.getWestSouthLng());
                request1.addProperty("south_distance", savePlotSurveyModel.getWestSouthDistance());
                request1.addProperty("south_accuracy", savePlotSurveyModel.getWestSouthAccuracy());
                request1.addProperty("west_lat", savePlotSurveyModel.getWestNorthLat());
                request1.addProperty("west_lng", savePlotSurveyModel.getWestNorthLng());
                request1.addProperty("west_distance", savePlotSurveyModel.getWestNorthDistance());
                request1.addProperty("west_accuracy", savePlotSurveyModel.getWestNorthAccuracy());
                request1.addProperty("north_lat", savePlotSurveyModel.getEastNorthLat());
                request1.addProperty("north_lng", savePlotSurveyModel.getEastNorthLng());
                request1.addProperty("north_distance", savePlotSurveyModel.getEastNorthDistance());
                request1.addProperty("north_accuracy", savePlotSurveyModel.getEastNorthAccuracy());
                request1.addProperty("variety_code", savePlotSurveyModel.getVarietyCode());
                request1.addProperty("growerCode", growerCode.getText().toString());
                request1.addProperty("cane_type", "0");
                request1.addProperty("insert_date", savePlotSurveyModel.getInsertDate());
                request1.addProperty("DIVN", factoryModelList.get(0).getDivision());
                request1.addProperty("U_CODE", factoryModelList.get(0).getCode());
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_InsertPlotSurvey, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("InsertPlotSurveyResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    formReset();
                    Intent intent = new Intent(context, StaffNewPlotEntry.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"), intent);
                } else {
                    //formReset();
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    private void formReset() {

        plot_sr_no.setText("");
        khashraNumber.setText("");
        gataNumber.setText("");
        villageCode.setText("");
        growerCode.setText("");
        AreaMeter.setText("");
        AreaHec.setText("");
        variety.setSelection(0);
        mix_crop.setSelection(0);
        insect.setSelection(0);
        seed_source.setSelection(0);
        plant_method.setSelection(0);
        crop_condition.setSelection(0);
        disease.setSelection(0);
        irrigation.setSelection(0);
        soil_type.setSelection(0);
        land_type.setSelection(0);
        border_crop.setSelection(0);
        plot_type.setSelection(0);
        ghashti_number.setText("");
        inter_crop.setSelection(0);
        plant_date.setText("");
        clearAreaCoordinate();
    }

}
