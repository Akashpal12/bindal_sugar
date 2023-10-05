package in.co.vibrant.bindalsugar.view.supervisor.survey;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.LocationDataAdapter;
import in.co.vibrant.bindalsugar.adapter.MasterModeAutoSearchAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageAutoSearchAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.LocationDataModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GenerateLogFile;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class NewPlotEntryCane extends AppCompatActivity {


    LinearLayout btnLayout;
    RelativeLayout msgLayout;
    TextView app_version,os_version,device_name,brand;
    TextView datetimes,app_versions,os_versions,handsets,device_names,date_times,location_status_txt;
    AlertDialog dialogPopup;
    TextView date_time,datetime;
    boolean gpsStatus=true;
    //TextView Corner1,Lat1,Lng1,Distance1,Accuracy1,Corner2,Lat2,Lng2,Distance2,Accuracy2,Corner3,Lat3,Lng3,Distance3,Accuracy3,Corner4,Lat4,Lng4,Distance4,Accuracy4;
    int currentDistance=1;
    double currentAccuracy=200000;
    EditText AreaMeter,AreaHec,khashraNumber,gataNumber;
    RelativeLayout rl_master_coordinate;
    TextView t_master_latlng,t_master_acc;

    List<VillageSurveyModel> villageSurveyModelList;
    AutoCompleteTextView villageCode;
    VillageAutoSearchAdapter villageAutoSearchAdapter;

    List<MasterCaneSurveyModel> masterVarietyModelList,masterCaneTypeModelList,masterMixCropModelList,masterInsectModelList,masterSeedSourceModelList,masterPlantMethodModelList
            ,masterCropConditionModelList,masterDiseaseModelList,masterIrrigationModelList,masterSoilTypeModelList
            ,masterLandTypeModelList,masterBorderCropModelList,masterPlotTypeModelList,masterInterCropModelList;
    AutoCompleteTextView variety,cane_type,mix_crop,insect,seed_source,plant_method,crop_condition,disease,irrigation,soil_type,land_type
            ,border_crop,plot_type,inter_crop;

    RelativeLayout rl_plot_sr_no,rl_khashra_number,rl_gata_number,rl_village,rl_area,rl_mix_crop,rl_insect,rl_seed_source,
            rl_plant_method,rl_crop_condition,rl_disease,rl_irrigation,rl_soil_type
            ,rl_land_type,rl_border_crop,rl_plot_type,rl_inter_crop,rl_plant_date,rl_ghashti_number;
    MasterModeAutoSearchAdapter masterModeAutoSearchAdapter;

    EditText plot_sr_no,plant_date,ghashti_number;
    TextView location_lat,location_lng,location_accuracy;

    //LinearLayout rl_share;

    String StrPlotSrNo="",StrAreaMeter="0.0",StrAreaHec="0.0",StrKhashraNumber="",StrGataNumber="",StrVillageCode=""
            ,StrVarietyCode="",StrCaneTypeCode="",StrMixCrop="",StrInsect="",
            StrSeedSource="",StrPlantMethod="",StrCropCondition="",StrDisease="",StrIrrigation="",StrSoilType="",StrLandType="",
            StrBorderCrop="",StrPlotType="",StrInterCrop="",StrPlantDate="",StrGhashtiNumber=""
            /*StrEastNorthLat="0.0",StrEastNorthLng="0.0",StrEastNorthDistance="",StrEastNorthAccuracy="",StrWestNorthLat="0.0",
            StrWestNorthLng="0.0",StrWestNorthDistance="",StrWestNorthAccuracy="",StrEastSouthLat="0.0",StrEastSouthLng="0.0",
            StrEastSouthDistance="",StrEastSouthAccuracy="",StrWestSouthLat="0.0",StrWestSouthLng="0.0",StrWestSouthDistance="",StrWestSouthAccuracy=""*/;

    List<Location> locationList;

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> factoryModelList;
    List<ControlSurveyModel> controlSurveyModelList;
    int PERMISSION_ID = 44;
    double Lat,Lng,Accuracy,LastLat=0,LastLng=0,dist=0;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    androidx.appcompat.app.AlertDialog dialog;
    AlertDialog Alertdialog;
    Context context;

    //LocationManager locationManager;
    boolean openDialogue=false;
    boolean timerFlag=true;
    TextView t_msg;
    //MyLocationListener myLocationListener;
    CountDownTimer yourCountDownTimer;
    //EditText areaHec,areaAcre,areaHecCoordinate,areaAcreCoordinate;
    String plotFrom="New Plot";
    String plotSerialNumber="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_plot_entry_cane);
        context=NewPlotEntryCane.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        factoryModelList=new ArrayList<>();
        factoryModelList=dbh.getUserDetailsModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Plot Entry");
        toolbar.setTitle("New Plot Entry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try{
            plotFrom=getIntent().getExtras().getString("plotFrom");
            plotSerialNumber=getIntent().getExtras().getString("plotSerialNumber");
        }
        catch(Exception e)
        {
            plotFrom="New Plot";
            plotSerialNumber="0";
        }
        TextView old_plot_id=findViewById(R.id.old_plot_id);
        old_plot_id.setText("Old Plot Id : "+plotSerialNumber);
        TextView old_plot_from=findViewById(R.id.old_plot_from);
        old_plot_from.setText("Type : "+plotFrom);
        controlSurveyModelList =dbh.getControlSurveyModel("");

        rl_plot_sr_no=findViewById(R.id.rl_plot_sr_no);
        rl_khashra_number=findViewById(R.id.rl_khashra_number);
        rl_gata_number=findViewById(R.id.rl_gata_number);
        rl_village=findViewById(R.id.rl_village);
        rl_area=findViewById(R.id.rl_area);
        rl_mix_crop=findViewById(R.id.rl_mix_crop);
        rl_insect=findViewById(R.id.rl_insect);
        rl_seed_source=findViewById(R.id.rl_seed_source);
        rl_plant_method=findViewById(R.id.rl_plant_method);
        rl_crop_condition=findViewById(R.id.rl_crop_condition);
        rl_disease=findViewById(R.id.rl_disease);
        rl_irrigation=findViewById(R.id.rl_irrigation);
        rl_soil_type=findViewById(R.id.rl_soil_type);
        rl_land_type=findViewById(R.id.rl_land_type);
        rl_border_crop=findViewById(R.id.rl_border_crop);
        rl_plot_type=findViewById(R.id.rl_plot_type);
        rl_inter_crop=findViewById(R.id.rl_inter_crop);
        rl_ghashti_number=findViewById(R.id.rl_ghashti_number);
        rl_plant_date=findViewById(R.id.rl_plant_date);
        //rl_share=findViewById(R.id.rl_share);

        locationList=new ArrayList<>();
        villageCode=findViewById(R.id.village_code);
        variety=findViewById(R.id.variety);
        cane_type=findViewById(R.id.cane_type);
        mix_crop=findViewById(R.id.mix_crop);
        insect=findViewById(R.id.insect);
        seed_source=findViewById(R.id.seed_source);
        plant_method=findViewById(R.id.plant_method);
        crop_condition=findViewById(R.id.crop_condition);
        disease=findViewById(R.id.disease);
        irrigation=findViewById(R.id.irrigation);
        soil_type=findViewById(R.id.soil_type);
        land_type=findViewById(R.id.land_type);
        border_crop=findViewById(R.id.border_crop);
        plot_type=findViewById(R.id.plot_type);
        inter_crop=findViewById(R.id.inter_crop);


        plant_date=findViewById(R.id.plant_date);

        rl_master_coordinate=findViewById(R.id.rl_master_coordinate);
        t_master_latlng=findViewById(R.id.t_master_latlng);
        t_master_acc=findViewById(R.id.t_master_acc);


        plot_sr_no=findViewById(R.id.plot_sr_no);
        ghashti_number=findViewById(R.id.ghashti_number);
        khashraNumber=findViewById(R.id.khashra_number);
        gataNumber=findViewById(R.id.gata_number);
        AreaMeter=findViewById(R.id.area_meter);
        AreaHec=findViewById(R.id.area_hec);


        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initAutoSearchAndParameter();
        initCheckValidation();
        getVIllageCode();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setLatLng();
        startLocationUpdates();
        startClock();
    }

    private void startClock(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH : mm :ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        if(date_time!=null)
            date_time.setText("Current Date / Time :  "+currentDateandTime);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar c = Calendar.getInstance();

                                int hours = c.get(Calendar.HOUR_OF_DAY);
                                int minutes = c.get(Calendar.MINUTE);
                                int seconds = c.get(Calendar.SECOND);

                                String curTime = String.format("%02d : %02d : %02d", hours, minutes, seconds);
                                if(datetime!=null)
                                    datetime.setText("Current Time : " +curTime); //change clock to your textview
                                if(location_status_txt!=null)
                                {
                                    gpsStatus=displayGpsStatus();
                                    location_status_txt.setText("Gps Status  : " +gpsStatus);
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private Boolean displayGpsStatus() {
        if (Settings.Secure.isLocationProviderEnabled(getBaseContext().getContentResolver(), "gps")) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
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
                    try {
                        new AlertDialogManager().AlertPopUpFinish(context,"Security Error: please enable gps service");
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
                            try{
                                Location location = locationResult.getLastLocation();
                                if(location.isFromMockProvider())
                                {
                                    new AlertDialogManager().showToast((Activity) context,"Security Error : you can not use this application because we detected fake GPS ?");
                                }
                                else
                                {
                                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    Location currentLocation = new Location("");
                                    currentLocation.setLatitude(Lat);
                                    currentLocation.setLongitude(Lng);
                                    Accuracy = Double.parseDouble(new DecimalFormat("0.00").format(location.getAccuracy()));
                                    if (openDialogue) {
                                    /*if(location.getSpeed()>0)
                                    {

                                        if(yourCountDownTimer != null)
                                        {
                                            yourCountDownTimer.cancel();
                                        }
                                        openDialogue=false;
                                        dialogPopup.dismiss();
                                        new AlertDialogManager().RedDialog(context,"Warning - "+new DecimalFormat("0.00").format(location.getSpeed())+" : please capture coordinate when you reach on point ");
                                    }
                                    else{
                                        setLatDialogue(""+Lat, ""+Lng, ""+Accuracy,location.getSpeed());
                                    }*/
                                        setLatDialogue("" + Lat, "" + Lng, "" + Accuracy, location.getSpeed());
                                    }
                                }
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
            new AlertDialogManager().AlertPopUpFinish(context,"Security Error:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Error:"+se.toString());
        }

    }

    private void getVIllageCode()
    {
        List<VillageSurveyModel> villageModelList1=dbh.getDefaultSurveyVillage();
        if(villageModelList1.size()>0) {
            villageCode.setText(villageModelList1.get(0).getVillageCode() + " - " + villageModelList1.get(0).getVillageName());
            StrVillageCode = villageModelList1.get(0).getVillageCode();
        }
        List<PlotSurveyModel> tempPlotSurveyModelList=dbh.getPlotSurveyModel("");
        Log.d("getVIllageCode", "getVIllageCode: "+tempPlotSurveyModelList.size());
       // if(tempPlotSurveyModelList.size()>0)
       // {
            List<VillageSurveyModel> villageSurveyModelList1=dbh.getDefaultSurveyVillage();
            //List<VillageSurveyModel> villageSurveyModelList1 =dbh.getSurveyVillageModel(tempPlotSurveyModelList.get(0).getVillageCode());
            if(villageSurveyModelList1.size()>0)
            {
                villageCode.setText(villageSurveyModelList1.get(0).getVillageCode()+" - "+ villageSurveyModelList1.get(0).getVillageName());
                StrVillageCode= villageSurveyModelList1.get(0).getVillageCode();
                List<PlotSurveyModel> plotSurveyModelList=dbh.getPlotSurveyModel(StrVillageCode);
                if(plotSurveyModelList.size()==0)
                {
                    if(villageSurveyModelList1.size()==0)
                    {
                        StrPlotSrNo="1";
                    }
                    else
                    {
                        StrPlotSrNo=""+(Integer.parseInt(villageSurveyModelList1.get(0).getPlotSrNo())+1);
                    }
                    List<VillageSurveyModel> villageSurveyModelListCheckPlotSrNo =dbh.getDefaultSurveyVillage();
                    if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                    {
                        if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
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
                    List<VillageSurveyModel> villageSurveyModelListCheckPlotSrNo =dbh.getDefaultSurveyVillage();
                    if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo())>Integer.parseInt(plotSurveyModelList.get(0).getPlotSrNo()))
                    {
                        StrPlotSrNo=""+(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo())+1);
                    }
                    if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                    {
                        if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
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
      //  }

    }

    private void initCheckValidation()
    {
        ControlSurveyModel controlSurveyModel = controlSurveyModelList.get(0);
        if(controlSurveyModel.getKhashraNo().equalsIgnoreCase("1"))
        {
            rl_khashra_number.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getGataNo().equalsIgnoreCase("1"))
        {
            rl_gata_number.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getHideArea().equalsIgnoreCase("0"))
        {
            rl_area.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getMixCrop().equalsIgnoreCase("1"))
        {
            rl_mix_crop.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getInsect().equalsIgnoreCase("1"))
        {
            rl_insect.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getSeedSource().equalsIgnoreCase("1"))
        {
            rl_seed_source.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getPlantMethod().equalsIgnoreCase("1"))
        {
            rl_plant_method.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getCropCond().equalsIgnoreCase("1"))
        {
            rl_crop_condition.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getDisease().equalsIgnoreCase("1"))
        {
            rl_disease.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getPlantDate().equalsIgnoreCase("1"))
        {
            rl_plant_date.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getIrrigation().equalsIgnoreCase("1"))
        {
            rl_irrigation.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getSoilType().equalsIgnoreCase("1"))
        {
            rl_soil_type.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getLandType().equalsIgnoreCase("1"))
        {
            rl_land_type.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getBorderCrop().equalsIgnoreCase("1"))
        {
            rl_border_crop.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getPlotType().equalsIgnoreCase("1"))
        {
            rl_plot_type.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getGhashtiNo().equalsIgnoreCase("1"))
        {
            rl_ghashti_number.setVisibility(View.VISIBLE);
        }
        if(controlSurveyModel.getInterCrop().equalsIgnoreCase("1"))
        {
            rl_inter_crop.setVisibility(View.VISIBLE);
        }
    }


    public void Exit(View v)
    {
        //generatesharePer();
        finish();
    }

    public void saveData(View v)
    {
        try{
            /*if(myLocationListener!=null)
            locationManager.removeUpdates(myLocationListener);*/

            ControlSurveyModel controlSurveyModel = controlSurveyModelList.get(0);
            labelsavedata :
            {
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
                    StrKhashraNumber=khashraNumber.getText().toString();
                    if (StrKhashraNumber.length() == 0) {
                        AlertPopUp("Enter khashra number");
                        khashraNumber.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getGataNo().equalsIgnoreCase("1")) {
                    StrGataNumber=gataNumber.getText().toString();
                    if (StrGataNumber.length() == 0) {
                        AlertPopUp("Enter gata number");
                        gataNumber.requestFocus();
                        break labelsavedata;
                    }
                }
                if (StrVarietyCode.length() == 0) {
                    AlertPopUp("Enter variety");
                    variety.requestFocus();
                    break labelsavedata;
                }
                if (StrCaneTypeCode.length() == 0) {
                    AlertPopUp("Enter cane type");
                    cane_type.requestFocus();
                    break labelsavedata;
                }
                if (controlSurveyModel.getMixCrop().equalsIgnoreCase("1")) {
                    if (StrMixCrop.length() == 0) {
                        AlertPopUp("Enter mix crop");
                        mix_crop.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getMixCrop().equalsIgnoreCase("1")) {
                    if (StrMixCrop.length() == 0) {
                        AlertPopUp("Enter mix crop");
                        mix_crop.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getInsect().equalsIgnoreCase("1")) {
                    if (StrInsect.length() == 0) {
                        AlertPopUp("Enter insect");
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getSeedSource().equalsIgnoreCase("1")) {
                    if (StrSeedSource.length() == 0) {
                        AlertPopUp("Enter seed source");
                        seed_source.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getPlantMethod().equalsIgnoreCase("1")) {
                    if (StrPlantMethod.length() == 0) {
                        AlertPopUp("Enter plant method");
                        plant_method.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getCropCond().equalsIgnoreCase("1")) {
                    if (StrCropCondition.length() == 0) {
                        AlertPopUp("Enter crop condition");
                        crop_condition.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getDisease().equalsIgnoreCase("1")) {
                    if (StrDisease.length() == 0) {
                        AlertPopUp("Enter disease");
                        disease.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getPlantDate().equalsIgnoreCase("1")) {
                    if (StrPlantDate.length() == 0) {
                        AlertPopUp("select plant date");
                        plant_date.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getIrrigation().equalsIgnoreCase("1")) {
                    if (StrIrrigation.length() == 0) {
                        AlertPopUp("Enter irrigation");
                        irrigation.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getSoilType().equalsIgnoreCase("1")) {
                    if (StrSoilType.length() == 0) {
                        AlertPopUp("Enter soil type");
                        soil_type.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getLandType().equalsIgnoreCase("1")) {
                    if (StrLandType.length() == 0) {
                        AlertPopUp("Enter land type");
                        land_type.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getBorderCrop().equalsIgnoreCase("1")) {
                    if (StrBorderCrop.length() == 0) {
                        AlertPopUp("Enter border crop");
                        border_crop.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getPlotType().equalsIgnoreCase("1")) {
                    if (StrPlotType.length() == 0) {
                        AlertPopUp("Enter plot type");
                        plot_type.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getGhashtiNo().equalsIgnoreCase("1")) {
                    StrGhashtiNumber=ghashti_number.getText().toString();
                    if (StrGhashtiNumber.length() == 0) {
                        AlertPopUp("Enter ghashti number");
                        ghashti_number.requestFocus();
                        break labelsavedata;
                    }
                }
                if (controlSurveyModel.getInterCrop().equalsIgnoreCase("1")) {
                    if (StrInterCrop.length() == 0) {
                        AlertPopUp("Enter inter crop");
                        inter_crop.requestFocus();
                        break labelsavedata;
                    }
                }
                if (dbh.getLocationDataModel("").size()<3) {
                    new AlertDialogManager().showToast(NewPlotEntryCane.this, "Please capture minimum 3 coordinate");
                    break labelsavedata;
                }
                if (AreaHec.getText().toString().length()==0) {
                    new AlertDialogManager().showToast(NewPlotEntryCane.this, "Area should be greater than zero");
                    break labelsavedata;
                }
                if (Double.parseDouble(AreaHec.getText().toString())==0) {
                    new AlertDialogManager().showToast(NewPlotEntryCane.this, "Area should be greater than zero");
                    break labelsavedata;
                }

                int east=0,west=0,north=0,south=0;
                int count=0;
                List<LocationDataModel> locationDataModels=dbh.getLocationDataModel("");
                if(locationDataModels.size()>0)
                {
                    east=Integer.parseInt(new DecimalFormat("0").format(locationDataModels.get(0).getDistance()));
                }
                if(locationDataModels.size()>1)
                {
                    north=Integer.parseInt(new DecimalFormat("0").format(locationDataModels.get(1).getDistance()));
                }
                if(locationDataModels.size()>2)
                {
                    west=Integer.parseInt(new DecimalFormat("0").format(locationDataModels.get(2).getDistance()));
                }
                if(locationDataModels.size()>3)
                {
                    south=Integer.parseInt(new DecimalFormat("0").format(locationDataModels.get(3).getDistance()));
                }
                if(east>0)
                    count++;
                if(west>0)
                    count++;
                if(north>0)
                    count++;
                if(south>0)
                    count++;

                if(count<3)
                {
                    new AlertDialogManager().showToast(NewPlotEntryCane.this, "capture fourth location or use skip button in case of triangle plot");
                    break labelsavedata;
                }

                insertData();
            }
        }
        catch (Exception e0)
        {
            new AlertDialogManager().showToast((Activity) context,"Error : "+e0.getMessage());
        }

    }

    private void insertData()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);
        PlotSurveyModel plotSurveyModel=new PlotSurveyModel();
        plotSurveyModel.setPlotSrNo(StrPlotSrNo);
        plotSurveyModel.setKhashraNo(StrKhashraNumber);
        plotSurveyModel.setGataNo(StrGataNumber);
        plotSurveyModel.setVillageCode(StrVillageCode);
        plotSurveyModel.setMixCrop(StrMixCrop);
        plotSurveyModel.setInsect(StrInsect);
        plotSurveyModel.setSeedSource(StrSeedSource);
        plotSurveyModel.setPlantMethod(StrPlantMethod);
        plotSurveyModel.setCropCondition(StrCropCondition);
        plotSurveyModel.setDisease(StrDisease);
        plotSurveyModel.setIrrigation(StrIrrigation);
        plotSurveyModel.setSoilType(StrSoilType);
        plotSurveyModel.setLandType(StrLandType);
        plotSurveyModel.setBorderCrop(StrBorderCrop);
        plotSurveyModel.setPlotType(StrPlotType);
        plotSurveyModel.setInterCrop(StrInterCrop);
        plotSurveyModel.setAadharNumber("");
        plotSurveyModel.setPlantDate(StrPlantDate);
        plotSurveyModel.setGhashtiNumber(StrGhashtiNumber);

        List<LocationDataModel> locationDataModels=dbh.getLocationDataModel("");

        plotSurveyModel.setEastNorthLat("0.00");
        plotSurveyModel.setEastNorthLng("0.00");
        plotSurveyModel.setEastNorthDistance("0.00");
        plotSurveyModel.setEastNorthAccuracy("0.00");
        plotSurveyModel.setWestNorthLat("0.00");
        plotSurveyModel.setWestNorthLng("0.00");
        plotSurveyModel.setWestNorthDistance("0.00");
        plotSurveyModel.setWestNorthAccuracy("0.00");
        plotSurveyModel.setEastSouthLat("0.00");
        plotSurveyModel.setEastSouthLng("0.00");
        plotSurveyModel.setEastSouthDistance("0.00");
        plotSurveyModel.setEastSouthAccuracy("0.00");
        plotSurveyModel.setWestSouthLat("0.00");
        plotSurveyModel.setWestSouthLng("0.00");
        plotSurveyModel.setWestSouthDistance("0.00");
        plotSurveyModel.setWestSouthAccuracy("0.00");
        if(locationDataModels.size()>=1)
        {
            plotSurveyModel.setEastNorthLat(""+locationDataModels.get(0).getLat());
            plotSurveyModel.setEastNorthLng(""+locationDataModels.get(0).getLng());
            plotSurveyModel.setEastNorthDistance(""+locationDataModels.get(0).getDistance());
            plotSurveyModel.setEastNorthAccuracy(""+locationDataModels.get(0).getAccuracy());
        }
        if(locationDataModels.size()>=2)
        {
            plotSurveyModel.setWestNorthLat(""+locationDataModels.get(1).getLat());
            plotSurveyModel.setWestNorthLng(""+locationDataModels.get(1).getLng());
            plotSurveyModel.setWestNorthDistance(""+locationDataModels.get(1).getDistance());
            plotSurveyModel.setWestNorthAccuracy(""+locationDataModels.get(1).getAccuracy());
        }
        if(locationDataModels.size()>=3)
        {
            plotSurveyModel.setWestSouthLat(""+locationDataModels.get(2).getLat());
            plotSurveyModel.setWestSouthLng(""+locationDataModels.get(2).getLng());
            plotSurveyModel.setWestSouthDistance(""+locationDataModels.get(2).getDistance());
            plotSurveyModel.setWestSouthAccuracy(""+locationDataModels.get(2).getAccuracy());

        }
        if(locationDataModels.size()>=4)
        {
            plotSurveyModel.setEastSouthLat(""+locationDataModels.get(3).getLat());
            plotSurveyModel.setEastSouthLng(""+locationDataModels.get(3).getLng());
            plotSurveyModel.setEastSouthDistance(""+locationDataModels.get(3).getDistance());
            plotSurveyModel.setEastSouthAccuracy(""+locationDataModels.get(3).getAccuracy());
        }
        //plotSurveyModel.setAreaHectare(0.00);
        //plotSurveyModel.setAreaAcre(0.00);


        plotSurveyModel.setVarietyCode(StrVarietyCode);
        plotSurveyModel.setCaneType(StrCaneTypeCode);
        plotSurveyModel.setInsertDate(currDate);
        plotSurveyModel.setAreaMeter(AreaMeter.getText().toString());
        plotSurveyModel.setAreaHectare(AreaHec.getText().toString());
        plotSurveyModel.setOldPlotId(plotSerialNumber);
        plotSurveyModel.setPlotFrom(plotFrom);
        long insertId=dbh.insertPlotSurveyModel(plotSurveyModel);
        dbh.truncateLocationDataModel();
        List<PlotSurveyModel> plotSurveyModelsFinal=dbh.getPlotSurveyModelById("");
        new GenerateLogFile(NewPlotEntryCane.this).exportSingleBackup(plotSurveyModelsFinal.get(plotSurveyModelsFinal.size()-1));
        Intent intent=new Intent(NewPlotEntryCane.this, AddGrowerShareCane.class);
        intent.putExtra("survey_id",""+insertId);
        finish();
        startActivity(intent);

        //AlertPopUpWithFinish("Plot survey done survey number is "+StrPlotSrNo);
    }

    private void initAutoSearchAndParameter()
    {
        villageSurveyModelList =dbh.getDefaultSurveyVillage();
        if(villageSurveyModelList.size()>0) {
            villageAutoSearchAdapter = new VillageAutoSearchAdapter(this, R.layout.all_list_row_item_search, villageSurveyModelList);
            villageCode.setThreshold(1);
            villageCode.setAdapter(villageAutoSearchAdapter);
            villageCode.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            VillageSurveyModel purcheyDataModel = (VillageSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            villageCode.setText(purcheyDataModel.getVillageCode()+" - "+purcheyDataModel.getVillageName());
                            StrVillageCode=purcheyDataModel.getVillageCode();
                            List<PlotSurveyModel> plotSurveyModelList=dbh.getPlotSurveyModel(StrVillageCode);
                            if(plotSurveyModelList.size()==0)
                            {
                                List<VillageSurveyModel> villageSurveyModelList1 =new ArrayList<>();
                                villageSurveyModelList1 =dbh.getVillageModel(StrVillageCode);
                                if(villageSurveyModelList1.size()==0)
                                {
                                    StrPlotSrNo="1";
                                }
                                else
                                {
                                    StrPlotSrNo=""+(Integer.parseInt(villageSurveyModelList1.get(0).getPlotSrNo())+1);
                                }
                                List<VillageSurveyModel> villageSurveyModelListCheckPlotSrNo =dbh.getVillageModel(villageSurveyModelList1.get(0).getVillageCode());
                                if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                                {
                                    if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
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
                                List<VillageSurveyModel> villageSurveyModelListCheckPlotSrNo =dbh.getVillageModel(plotSurveyModelList.get(0).getVillageCode());
                                if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo())>Integer.parseInt(plotSurveyModelList.get(0).getPlotSrNo()))
                                {
                                    StrPlotSrNo=""+(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo())+1);
                                }
                                if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo1())<= Integer.parseInt(StrPlotSrNo))
                                {
                                    if(Integer.parseInt(villageSurveyModelListCheckPlotSrNo.get(0).getPlotSrNo2())>= Integer.parseInt(StrPlotSrNo))
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
                    villageSurveyModelList.clear();
                    villageSurveyModelList =dbh.getVillageModel("");
                    villageAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterVarietyModelList=dbh.getMasterModel("1");
        if(masterVarietyModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterVarietyModelList);
            variety.setThreshold(1);
            variety.setAdapter(masterModeAutoSearchAdapter);
            variety.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrVarietyCode= masterCaneSurveyModel.getCode();
                            variety.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            variety.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterVarietyModelList.clear();
                    masterVarietyModelList=dbh.getMasterModel("1");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterCaneTypeModelList=dbh.getMasterModel("2");
        if(masterCaneTypeModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterCaneTypeModelList);
            cane_type.setThreshold(1);
            cane_type.setAdapter(masterModeAutoSearchAdapter);
            cane_type.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrCaneTypeCode= masterCaneSurveyModel.getCode();
                            cane_type.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            cane_type.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterCaneTypeModelList.clear();
                    masterCaneTypeModelList=dbh.getMasterModel("2");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterMixCropModelList=dbh.getMasterModel("12");
        if(masterMixCropModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterMixCropModelList);
            mix_crop.setThreshold(1);
            mix_crop.setAdapter(masterModeAutoSearchAdapter);
            mix_crop.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrMixCrop= masterCaneSurveyModel.getCode();
                            mix_crop.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            mix_crop.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterMixCropModelList.clear();
                    masterMixCropModelList=dbh.getMasterModel("12");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterInsectModelList=dbh.getMasterModel("13");
        if(masterInsectModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterInsectModelList);
            insect.setThreshold(1);
            insect.setAdapter(masterModeAutoSearchAdapter);
            insect.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrInsect= masterCaneSurveyModel.getCode();
                            insect.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            insect.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterInsectModelList.clear();
                    masterInsectModelList=dbh.getMasterModel("13");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }


        masterSeedSourceModelList=dbh.getMasterModel("14");
        if(masterSeedSourceModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterSeedSourceModelList);
            seed_source.setThreshold(1);
            seed_source.setAdapter(masterModeAutoSearchAdapter);
            seed_source.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrSeedSource= masterCaneSurveyModel.getCode();
                            seed_source.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            seed_source.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterSeedSourceModelList.clear();
                    masterSeedSourceModelList=dbh.getMasterModel("14");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }



        masterPlantMethodModelList=dbh.getMasterModel("3");
        if(masterPlantMethodModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterPlantMethodModelList);
            plant_method.setThreshold(1);
            plant_method.setAdapter(masterModeAutoSearchAdapter);
            plant_method.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrPlantMethod= masterCaneSurveyModel.getCode();
                            plant_method.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            plant_method.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterPlantMethodModelList.clear();
                    masterPlantMethodModelList=dbh.getMasterModel("3");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterCropConditionModelList=dbh.getMasterModel("4");
        if(masterCropConditionModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterCropConditionModelList);
            crop_condition.setThreshold(1);
            crop_condition.setAdapter(masterModeAutoSearchAdapter);
            crop_condition.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrCropCondition= masterCaneSurveyModel.getCode();
                            crop_condition.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            crop_condition.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterCropConditionModelList.clear();
                    masterCropConditionModelList=dbh.getMasterModel("4");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterDiseaseModelList=dbh.getMasterModel("5");
        if(masterDiseaseModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterDiseaseModelList);
            disease.setThreshold(1);
            disease.setAdapter(masterModeAutoSearchAdapter);
            disease.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrDisease= masterCaneSurveyModel.getCode();
                            disease.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            disease.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterDiseaseModelList.clear();
                    masterDiseaseModelList=dbh.getMasterModel("5");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }



        masterIrrigationModelList=dbh.getMasterModel("6");
        if(masterIrrigationModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterIrrigationModelList);
            irrigation.setThreshold(1);
            irrigation.setAdapter(masterModeAutoSearchAdapter);
            irrigation.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrIrrigation= masterCaneSurveyModel.getCode();
                            irrigation.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            irrigation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterIrrigationModelList.clear();
                    masterIrrigationModelList=dbh.getMasterModel("6");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterSoilTypeModelList=dbh.getMasterModel("7");
        if(masterSoilTypeModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterSoilTypeModelList);
            soil_type.setThreshold(1);
            soil_type.setAdapter(masterModeAutoSearchAdapter);
            soil_type.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrSoilType= masterCaneSurveyModel.getCode();
                            soil_type.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            soil_type.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterSoilTypeModelList.clear();
                    masterSoilTypeModelList=dbh.getMasterModel("7");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterLandTypeModelList=dbh.getMasterModel("8");
        if(masterLandTypeModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterLandTypeModelList);
            land_type.setThreshold(1);
            land_type.setAdapter(masterModeAutoSearchAdapter);
            land_type.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrLandType= masterCaneSurveyModel.getCode();
                            land_type.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            land_type.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterLandTypeModelList.clear();
                    masterLandTypeModelList=dbh.getMasterModel("8");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }


        masterBorderCropModelList=dbh.getMasterModel("9");
        if(masterBorderCropModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterBorderCropModelList);
            border_crop.setThreshold(1);
            border_crop.setAdapter(masterModeAutoSearchAdapter);
            border_crop.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrBorderCrop= masterCaneSurveyModel.getCode();
                            border_crop.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            border_crop.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterBorderCropModelList.clear();
                    masterBorderCropModelList=dbh.getMasterModel("9");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterPlotTypeModelList=dbh.getMasterModel("10");
        if(masterPlotTypeModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterPlotTypeModelList);
            plot_type.setThreshold(1);
            plot_type.setAdapter(masterModeAutoSearchAdapter);
            plot_type.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrPlotType= masterCaneSurveyModel.getCode();
                            plot_type.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            plot_type.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterPlotTypeModelList.clear();
                    masterPlotTypeModelList=dbh.getMasterModel("10");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        masterInterCropModelList=dbh.getMasterModel("11");
        if(masterInterCropModelList.size()>0) {
            masterModeAutoSearchAdapter = new MasterModeAutoSearchAdapter(NewPlotEntryCane.this, R.layout.all_list_row_item_search, masterInterCropModelList);
            inter_crop.setThreshold(1);
            inter_crop.setAdapter(masterModeAutoSearchAdapter);
            inter_crop.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MasterCaneSurveyModel masterCaneSurveyModel = (MasterCaneSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            StrInterCrop= masterCaneSurveyModel.getCode();
                            inter_crop.setText(masterCaneSurveyModel.getCode()+" - "+ masterCaneSurveyModel.getName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            inter_crop.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    masterInterCropModelList.clear();
                    masterInterCropModelList=dbh.getMasterModel("11");
                    masterModeAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }

        plant_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(NewPlotEntryCane.this,new DatePickerDialog.OnDateSetListener() {

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
                        plant_date.setText(year+"-"+temmonth + "-"
                                +temDate );
                        StrPlantDate=year+"-"+temmonth + "-"+temDate ;

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

    }

    /*private void findLocation()
    {
        //startLocationUpdates();
        try {
            startLocationUpdates();
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());
            //locationManager.requestLocationUpdates("gps", 500, 111.0f, new MyLocationListener());
            *//*myLocationListener=new MyLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1f,
                    myLocationListener
            );*//*
        }
        catch (SecurityException e)
        {

        }
        catch (Exception e)
        {

        }
    }*/


    public void captureCoordinate(View v)
    {
        //findLocation();
        getCurrentLocation();
    }


    public void mapViewPlot(View v)
    {
        final List<LocationDataModel> locationDataModels=dbh.getLocationDataModel("");
        if(locationDataModels.size()>0)
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
                    latLngs.add(new LatLng(locationDataModels.get(0).getLat(), locationDataModels.get(0).getLng()));
                    latLngs.add(new LatLng(locationDataModels.get(1).getLat(), locationDataModels.get(1).getLng()));
                    latLngs.add(new LatLng(locationDataModels.get(2).getLat(), locationDataModels.get(2).getLng()));
                    latLngs.add(new LatLng(locationDataModels.get(3).getLat(), locationDataModels.get(3).getLng()));

                    LatLng midPoint=new LatLngUtil().getPolygonCenterPoint(latLngs);
                    Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                            .add(
                                    new LatLng(locationDataModels.get(0).getLat(), locationDataModels.get(0).getLng()),
                                    new LatLng(locationDataModels.get(1).getLat(), locationDataModels.get(1).getLng()),
                                    new LatLng(locationDataModels.get(2).getLat(), locationDataModels.get(2).getLng()),
                                    new LatLng(locationDataModels.get(3).getLat(), locationDataModels.get(3).getLng())
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
        }
        else {
            new AlertDialogManager().RedDialog(context,"1st lat lng not find");
        }

    }


    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
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
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NewPlotEntryCane.this);
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
                NewPlotEntryCane.this);
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

    private void AlertPopUpWithFinishSUrvey(String msg, final String tempSurveyId1) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NewPlotEntryCane.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(NewPlotEntryCane.this, AddGrowerShareCane.class);
                        intent.putExtra("survey_id",tempSurveyId1);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void getCurrentLocation()
    {
        try {
            final List<LocationDataModel> locationDataModelListTemp = dbh.getLocationDataModel("");
            if (locationDataModelListTemp.size() == 4) {
                new AlertDialogManager().RedDialog(context, "All coordinate captured successfully...");
                return;
            }
            final List<LocationDataModel> locationDataModelListTemp1 = dbh.getLocationDataModel("");
            if (locationDataModelListTemp1.size() == 4) {
                new AlertDialogManager().RedDialog(context, "All coordinate captured successfully...");
                return;
            }
            timerFlag=true;
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_get_latlng_db, null);
            dialogbilder.setView(mView);

            final Spinner location_direction = mView.findViewById(R.id.location_direction);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH : mm : ss ", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            t_msg = mView.findViewById(R.id.t_msg);
            date_times=mView.findViewById(R.id.date_time);
            app_versions=mView.findViewById(R.id.app_versions);
            os_versions=mView.findViewById(R.id.os_version);
            handsets=mView.findViewById(R.id.handset);
            device_names=mView.findViewById(R.id.device_name);
            location_status_txt=mView.findViewById(R.id.location_status_txt);

            datetimes=mView.findViewById(R.id.datetime);
            date_times.setText("Current Date : "+currentDateandTime);

            app_versions.setText("App Version : "+ BuildConfig.VERSION_NAME);
            String androidOS = Build.VERSION.RELEASE;
            os_versions.setText("OS Version : "+androidOS);
            device_names.setText("Device Name (Model) : " +android.os.Build.MODEL);
            handsets.setText("Handset  : " +android.os.Build.BRAND);
            location_status_txt.setText("Gps Status  : " +gpsStatus);

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Calendar c = Calendar.getInstance();

                                    int hours = c.get(Calendar.HOUR_OF_DAY);
                                    int minutes = c.get(Calendar.MINUTE);
                                    int seconds = c.get(Calendar.SECOND);
                                    String curTime = String.format("%02d : %02d : %02d", hours, minutes, seconds);
                                    datetimes.setText("Current Time : "+curTime); //change clock to your textview


                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();

            msgLayout=mView.findViewById(R.id.rl_msg);
            btnLayout=mView.findViewById(R.id.btnLayout);
            location_lat=mView.findViewById(R.id.location_lat);
            location_lng=mView.findViewById(R.id.location_lng);
            location_accuracy=mView.findViewById(R.id.location_accuracy);
            location_lat.setText("-----");
            location_lng.setText("-----");
            location_accuracy.setText("-----");
            dialogPopup = dialogbilder.create();
            dialogPopup.show();
            dialogPopup.setCancelable(false);
            dialogPopup.setCanceledOnTouchOutside(false);
            openDialogue=true;
            Button btn_ok=mView.findViewById(R.id.btn_ok);
            Button skip_cancel=mView.findViewById(R.id.skip_cancel);
            ImageView close=mView.findViewById(R.id.close);
            if(3==locationDataModelListTemp.size())
            {
                btn_ok.setVisibility(View.GONE);
            }
            else
            {
                btn_ok.setVisibility(View.VISIBLE);
            }
            if(locationDataModelListTemp.size()>2)
            {
                skip_cancel.setVisibility(View.VISIBLE);
            }
            else
            {
                skip_cancel.setVisibility(View.GONE);
            }
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(yourCountDownTimer!=null)
                    {
                        yourCountDownTimer.cancel();
                    }
                    openDialogue=false;
                    dialogPopup.dismiss();
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        openDialogue=false;
                        if(locationDataModelListTemp.size()>0)
                        {
                            double oldLat=locationDataModelListTemp.get(locationDataModelListTemp.size()-1).getLat();
                            double oldLng=locationDataModelListTemp.get(locationDataModelListTemp.size()-1).getLng();
                            double dist= SphericalUtil.computeDistanceBetween(new LatLng(oldLat,oldLng),new LatLng(Lat,Lng));
                            dist=Double.parseDouble(new DecimalFormat("0").format(dist));
                            if(1<=dist)
                            {
                                if(500>=dist)
                                {
                                    dbh.updateLocationDataModelDistance(dist,locationDataModelListTemp.size());
                                    LocationDataModel locationDataModel=new LocationDataModel();
                                    locationDataModel.setLat(Lat);
                                    locationDataModel.setLng(Lng);
                                    locationDataModel.setAccuracy(Accuracy);
                                    locationDataModel.setDistance(0.00);
                                    locationDataModel.setSerialNumber(dbh.getLocationDataModel("").size()+1);
                                    dbh.insertLocationDataModel(locationDataModel);
                                }
                                else
                                {
                                    new AlertDialogManager().RedDialog(context,"Distance should be less than 500");
                                }
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(context,"Distance should be greater than 1");
                            }

                        }
                        else
                        {
                            LocationDataModel locationDataModel=new LocationDataModel();
                            locationDataModel.setLat(Lat);
                            locationDataModel.setLng(Lng);
                            locationDataModel.setAccuracy(Accuracy);
                            locationDataModel.setDistance(0.00);
                            locationDataModel.setSerialNumber(dbh.getLocationDataModel("").size()+1);
                            dbh.insertLocationDataModel(locationDataModel);
                        }
                        setLatLng();
                    }
                    catch(Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                    }
                    openDialogue=false;
                    dialogPopup.dismiss();
                }
            });
            skip_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        openDialogue=false;
                        if(locationDataModelListTemp.size()>0)
                        {
                            double oldLat=locationDataModelListTemp.get(locationDataModelListTemp.size()-1).getLat();
                            double oldLng=locationDataModelListTemp.get(locationDataModelListTemp.size()-1).getLng();
                            double dist=SphericalUtil.computeDistanceBetween(new LatLng(oldLat,oldLng),new LatLng(Lat,Lng));
                            dist=Double.parseDouble(new DecimalFormat("0").format(dist));
                            double firstLat=locationDataModelListTemp.get(0).getLat();
                            double firstLng=locationDataModelListTemp.get(0).getLng();
                            double distFinal = SphericalUtil.computeDistanceBetween(new LatLng(Lat,Lng),new LatLng(firstLat,firstLng));
                            distFinal=Double.parseDouble(new DecimalFormat("0").format(distFinal));
                            if(1<=dist && 1<=distFinal)
                            {
                                if(1000>=dist && 1000>=distFinal)
                                {
                                    dbh.updateLocationDataModelDistance(dist,locationDataModelListTemp.size());
                                    LocationDataModel locationDataModel=new LocationDataModel();
                                    locationDataModel.setLat(Lat);
                                    locationDataModel.setLng(Lng);
                                    locationDataModel.setAccuracy(Accuracy);
                                    locationDataModel.setDistance(distFinal);
                                    locationDataModel.setSerialNumber(dbh.getLocationDataModel("").size()+1);
                                    dbh.insertLocationDataModel(locationDataModel);
                                }
                                else
                                {
                                    new AlertDialogManager().RedDialog(context,"Distance should be less than 1000");
                                }
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(context,"Distance should be greater than 1");
                            }
                        }
                        else
                        {
                            if(1<=dist)
                            {
                                if(1000>=dist)
                                {
                                    LocationDataModel locationDataModel=new LocationDataModel();
                                    locationDataModel.setLat(Lat);
                                    locationDataModel.setLng(Lng);
                                    locationDataModel.setAccuracy(Accuracy);
                                    locationDataModel.setDistance(0.00);
                                    locationDataModel.setSerialNumber(dbh.getLocationDataModel("").size()+1);
                                    dbh.insertLocationDataModel(locationDataModel);
                                }
                                else
                                {
                                    new AlertDialogManager().RedDialog(context,"Distance should be less than 1000");
                                }
                            }
                            else
                            {
                                new AlertDialogManager().RedDialog(context,"Distance should be greater than 1");
                            }
                        }
                        setLatLng();
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

        }

    }

    private void setLatDialogue(String lt, String ln, String acr,double speed)
    {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
                location_accuracy.setText(decimalFormat.format(Accuracy) + " / "+factoryModelList.get(0).getGpsAccuracy()+" M ");
            } else {
                location_lat.setText("---");
                location_lng.setText("---");
                location_accuracy.setText("---");
            }
            if (Accuracy <= factoryModelList.get(0).getGpsAccuracy()) {
                if(timerFlag)
                {
                    timerFlag=false;
                    if(t_msg!=null)
                    {
                        yourCountDownTimer=new CountDownTimer(factoryModelList.get(0).getGpsWaitTime() * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                int count= (int) (millisUntilFinished / 1000);
                                //ResendSmsProgress.setProgress(count);
                                if(t_msg!=null)
                                    t_msg.setText("Please wait while we verify your location");
                            }
                            public void onFinish() {
                                if(t_msg!=null)
                                    t_msg.setText("GPS Initilising");
                                msgLayout.setVisibility(View.GONE);
                                btnLayout.setVisibility(View.VISIBLE);
                            }
                        }.start();
                    }
                }
                else
                {
                    if(t_msg!=null)
                        t_msg.setText("Verifying your location");
                    timerFlag=true;
                }
                //msgLayout.setVisibility(View.GONE);
                //btnLayout.setVisibility(View.VISIBLE);
            } else {
                /*location_lat.setText("---");
                location_lng.setText("---");
                location_accuracy.setText("---");*/
                msgLayout.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.GONE);
            }
            if (currentAccuracy >= Accuracy) {
                currentAccuracy = Accuracy;
            }
        }
        catch (Exception e)
        {
            timerFlag=true;
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void setLatLng()
    {
        try {
            List<LatLng> latLngList=new ArrayList<>();
            List<LocationDataModel> locationDataModelList=dbh.getLocationDataModel("");
            List<LocationDataModel> locationDataModelList1=new ArrayList<>();
            locationDataModelList1.add(new LocationDataModel());
            for(int i=0;i<locationDataModelList.size();i++){
                locationDataModelList1.add(locationDataModelList.get(i));
                latLngList.add(new LatLng(locationDataModelList.get(i).getLat(),locationDataModelList.get(i).getLng()));
            }
            Button capture_location=findViewById(R.id.capture_location);
            if(locationDataModelList.size()>=4)
            {
                capture_location.setVisibility(View.GONE);
            }
            else
            {
                capture_location.setVisibility(View.VISIBLE);
            }
            RecyclerView recyclerView =findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            LocationDataAdapter stockSummeryAdapter =new LocationDataAdapter(context,locationDataModelList1);
            recyclerView.setAdapter(stockSummeryAdapter);
            calArea();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    private void calArea() {
        List<LocationDataModel> dbhLocationDataModel = dbh.getLocationDataModel("");
        double areaM = 0;
        if (dbhLocationDataModel.size() == 4) {
            double ew = dbhLocationDataModel.get(0).getDistance() + dbhLocationDataModel.get(2).getDistance();
            double ns = dbhLocationDataModel.get(1).getDistance() + dbhLocationDataModel.get(3).getDistance();
            ew = ew / 2;
            ns = ns / 2;
            areaM = ew * ns;
        } else if (dbhLocationDataModel.size() == 3) {
            double a = dbhLocationDataModel.get(0).getDistance() ;
            double b = dbhLocationDataModel.get(1).getDistance();
            double c = dbhLocationDataModel.get(2).getDistance();
            double s = (a+b+c)/2;
            areaM= Math.sqrt(s*(s-a)*(s-b)*(s-c));
        } else {
            areaM = 0.00;
        }
        //double areaM= SphericalUtil.computeArea(latLngList);
        double ah = areaM / 10000;
        double aac = areaM / 4047;
        AreaMeter.setText(new DecimalFormat("0.000").format(areaM));
        AreaHec.setText(new DecimalFormat("0.000").format(ah));
        currentAccuracy = 20000;

    }

    public void clearCoordinate(View v)
    {
        dbh.deleteLocationDataModel(dbh.getLocationDataModel("").size());
        AreaMeter.setText("");
        AreaHec.setText("");
        //AreaHec.setText("");
        setLatLng();
    }

}
