package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.view.admin.LiveTrackingLise;
import in.co.vibrant.bindalsugar.view.supervisor.AdminAgriInputReport;
import in.co.vibrant.bindalsugar.view.supervisor.StaffDownloadMasterData;
import in.co.vibrant.bindalsugar.view.supervisor.SupervisorAttendance;


public class RaDevelopmentDashboard extends AppCompatActivity  {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest request;
    private LocationCallback locationCallback;
    List<UserDetailsModel> userDetailsModels;
    AlertDialog alertDialog;
    int tempInc=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaDevelopmentDashboard.this;

        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=dbh.getUserDetailsModel();
        setTitle(getString(R.string.MENU_DASHBOARD));
        toolbar.setTitle(getString(R.string.MENU_DASHBOARD));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest();
        request.setInterval(10 * 1000);
        request.setFastestInterval(5 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };

    }

    public void openCaneDevelopment(View v)
    {
        Intent intent=new Intent(context, RaCaneDevelopmentDashboard.class);
        startActivity(intent);
    }

    public void openTracking(View v) {
        Intent intent = new Intent(context, LiveTrackingLise.class);
        startActivity(intent);
    }

    public void openCaneMarketing(View v)
    {
        Intent intent=new Intent(context, RaSurveyDashboard.class);
        startActivity(intent);
    }

    public void openReport(View v)
    {
        Intent intent=new Intent(context, RaReportDashboard.class);
        startActivity(intent);
    }

    public void openActivityApproval(View v)
    {
        Intent intent=new Intent(context, ActivityApproval.class);
        startActivity(intent);
    }

    public void openAttendance(View v)
    {
        Intent intent=new Intent(context, SupervisorAttendance.class);
        startActivity(intent);
    }

    public void openDownloadMaster(View v)
    {
        new StaffDownloadMasterData().downloadDropDownMasterDataCd(RaDevelopmentDashboard.this,dbh,userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode());
    }


    /*private class DownloadMaster extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            *//*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*//*
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT+"/GETMASTER";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("Factory",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("USERID",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("imei",getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);



                int totalData=0;
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    db.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
                    db.execSQL(MasterDropDown.CREATE_TABLE);
                    db.execSQL(VillageModal.CREATE_TABLE);
                    db.execSQL(GrowerModel.CREATE_TABLE);
                    db.execSQL(ControlModel.CREATE_TABLE);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    final JSONArray IRRIGATIONLIST=jsonObject.getJSONArray("IRRIGATIONLIST");
                    final JSONArray SUPPLYMODELIST=jsonObject.getJSONArray("SUPPLYMODELIST");
                    final JSONArray HARVESTINGLIST=jsonObject.getJSONArray("HARVESTINGLIST");
                    final JSONArray EQUIMENTLIST=jsonObject.getJSONArray("EQUIMENTLIST");
                    final JSONArray LANDTYPELIST=jsonObject.getJSONArray("LANDTYPELIST");
                    final JSONArray SEEDTYPELIST=jsonObject.getJSONArray("SEEDTYPELIST");
                    final JSONArray BASALDOSELIST=jsonObject.getJSONArray("BASALDOSELIST");
                    final JSONArray SEEDTREATMENTLIST=jsonObject.getJSONArray("SEEDTREATMENTLIST");
                    final JSONArray METHODLIST=jsonObject.getJSONArray("METHODLIST");
                    final JSONArray SPRAYITEMLIST=jsonObject.getJSONArray("SPRAYITEMLIST");
                    final JSONArray SPRAYTYPELIST=jsonObject.getJSONArray("SPRAYTYPELIST");
                    final JSONArray PLOUGHINGTYPELIST=jsonObject.getJSONArray("PLOUGHINGTYPELIST");
                    final JSONArray VARIETYLIST=jsonObject.getJSONArray("VARIETYLIST");
                    final JSONArray PLANTINGTYPELIST=jsonObject.getJSONArray("PLANTINGTYPELIST");
                    final JSONArray PLANTATIONLIST=jsonObject.getJSONArray("PLANTATIONLIST");
                    final JSONArray CROPLIST=jsonObject.getJSONArray("CROPLIST");
                    final JSONArray GROWERDATALIST=jsonObject.getJSONArray("GROWERDATALIST");
                    final JSONArray VILLAGEDATALIST=jsonObject.getJSONArray("VILLAGEDATALIST");
                    final JSONArray FILDTRUEFALSELIST=jsonObject.getJSONArray("FILDTRUEFALSELIST");
                    final JSONArray SEEDTTYPELIST=jsonObject.getJSONArray("SEEDTTYPELIST");
                    final JSONArray SEEDSETTYPELIST=jsonObject.getJSONArray("SEEDSETTYPELIST");
                    final JSONArray SOILTREATMENTLIST=jsonObject.getJSONArray("SOILTREATMENTLIST");
                    final JSONArray FILDPREPRATIONLIST=jsonObject.getJSONArray("FILDPREPRATIONLIST");
                    final JSONArray ROWTOROWLIST=jsonObject.getJSONArray("ROWTOROWLIST");
                    final JSONArray TARGETTYPELIST=jsonObject.getJSONArray("TARGETTYPE");
                    final JSONArray SUPERVOISERLIST=jsonObject.getJSONArray("SUPERVOISER");

                    totalData +=IRRIGATIONLIST.length();
                    totalData +=SUPPLYMODELIST.length();
                    totalData +=HARVESTINGLIST.length();
                    totalData +=LANDTYPELIST.length();
                    totalData +=SEEDTYPELIST.length();
                    totalData +=BASALDOSELIST.length();
                    totalData +=SEEDTREATMENTLIST.length();
                    totalData +=METHODLIST.length();
                    totalData +=SPRAYITEMLIST.length();
                    totalData +=SPRAYTYPELIST.length();
                    totalData +=PLOUGHINGTYPELIST.length();
                    totalData +=VARIETYLIST.length();
                    totalData +=PLANTINGTYPELIST.length();
                    totalData +=PLANTATIONLIST.length();
                    totalData +=CROPLIST.length();
                    totalData +=GROWERDATALIST.length();
                    totalData +=VILLAGEDATALIST.length();
                    totalData +=FILDTRUEFALSELIST.length();
                    totalData +=SEEDTTYPELIST.length();
                    totalData +=SEEDSETTYPELIST.length();
                    totalData +=SOILTREATMENTLIST.length();
                    totalData +=FILDPREPRATIONLIST.length();
                    totalData +=ROWTOROWLIST.length();
                    totalData +=TARGETTYPELIST.length();
                    totalData +=SUPERVOISERLIST.length();

                    *//*
                    1-IRRIGATIONLIST
                    2-SUPPLYMODELIST
                    3-HARVESTINGLIST
                    4-EQUIMENTLIST
                    5-LANDTYPELIST
                    6-SEEDTYPELIST
                    7-BASALDOSELIST
                    8-METHODLIST
                    9-SPRAYITEMLIST
                    10-SPRAYTYPELIST
                    11-PLOUGHINGTYPELIST
                    12-VARIETYLIST
                    13-PLANTINGTYPELIST
                    14-PLANTATIONLIST
                    15-CROPLIST
                    16-SEEDTREATMENTLIST
                    17-SEEDTTYPELIST
                    18-SEEDSETTYPELIST
                    19-SOILTREATMENTLIST
                    20-FILDPREPRATIONLIST
                    21-ROWTOROWLIST
                    22-TARGETTYPELIST
                    23-SUPERVOISERLIST
                    *//*


                    dialog.setMax(totalData);
                    dialog.setProgress(0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    if(IRRIGATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating irrigation data...");
                                for (int i=0;i<IRRIGATIONLIST.length();i++)
                                {
                                    try {

                                        tempInc++;
                                        JSONObject object = IRRIGATIONLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("1");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });


                    }

                    if(SUPPLYMODELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supply mode data...");
                                for (int i=0;i<SUPPLYMODELIST.length();i++)
                                {
                                    try{
                                        tempInc++;
                                        JSONObject object=SUPPLYMODELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown=new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("2");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(HARVESTINGLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating harvesting data...");
                                for (int i=0;i<HARVESTINGLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = HARVESTINGLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("3");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(EQUIMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating equipment data...");
                                for (int i=0;i<EQUIMENTLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = EQUIMENTLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("4");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(LANDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating land type data...");
                                for (int i=0;i<LANDTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = LANDTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("5");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SEEDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                                for (int i=0;i<SEEDTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SEEDTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("6");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(BASALDOSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating basaldose data...");
                                for (int i=0;i<BASALDOSELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = BASALDOSELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("7");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(METHODLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating method data...");
                                for (int i=0;i<METHODLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = METHODLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("8");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SPRAYITEMLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray item data...");
                                for (int i=0;i<SPRAYITEMLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SPRAYITEMLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("9");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SPRAYTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray type data...");
                                for (int i=0;i<SPRAYTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SPRAYTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("10");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(PLOUGHINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating ploughing data...");
                                for (int i=0;i<PLOUGHINGTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = PLOUGHINGTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("11");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(VARIETYLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating variety list data...");
                                for (int i=0;i<VARIETYLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = VARIETYLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("12");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(PLANTINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating planting type data...");
                                for (int i=0;i<PLANTINGTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = PLANTINGTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("13");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(PLANTATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plantation data...");
                                for (int i=0;i<PLANTATIONLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = PLANTATIONLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("14");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(CROPLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating crop list data...");
                                for (int i=0;i<CROPLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = CROPLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("15");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(VILLAGEDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating village data...");
                                for (int i=0;i<VILLAGEDATALIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = VILLAGEDATALIST.getJSONObject(i);
                                        VillageModal villageModal = new VillageModal();
                                        villageModal.setCode(object.getString("VCODE"));
                                        villageModal.setName(object.getString("VNAME"));
                                        villageModal.setMaxIndent(object.getString("VINDSERIALNO"));
                                        villageModal.setMaxPlant(object.getString("VPLNSERIALNO"));
                                        dbh.insertVillageModal(villageModal);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(GROWERDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating grower data...");
                                for (int i=0;i<GROWERDATALIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = GROWERDATALIST.getJSONObject(i);
                                        GrowerModel growerModel = new GrowerModel();
                                        growerModel.setVillageCode(object.getString("VCODE"));
                                        growerModel.setGrowerCode(object.getString("GCODE"));
                                        growerModel.setGrowerName(object.getString("GNAME"));
                                        growerModel.setGrowerFather(object.getString("FATHER"));
                                        dbh.insertGrowerModel(growerModel);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(FILDTRUEFALSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating control data...");
                                for (int i=0;i<FILDTRUEFALSELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = FILDTRUEFALSELIST.getJSONObject(i);
                                        ControlModel controlSurveyModel = new ControlModel();
                                        controlSurveyModel.setName(object.getString("Fild"));
                                        controlSurveyModel.setValue(object.getString("Value"));
                                        controlSurveyModel.setFormName(object.getString("FormName"));
                                        dbh.insertControlModel(controlSurveyModel);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SEEDTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed treatment data...");
                                for (int i=0;i<SEEDTREATMENTLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SEEDTREATMENTLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("16");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SEEDTTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                                for (int i=0;i<SEEDTTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SEEDTTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("17");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }


                    if(SEEDSETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed set type data...");
                                for (int i=0;i<SEEDSETTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SEEDSETTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("18");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SOILTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating soil treatment data...");
                                for (int i=0;i<SOILTREATMENTLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SOILTREATMENTLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("19");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(FILDPREPRATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating field prepration data...");
                                for (int i=0;i<FILDPREPRATIONLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = FILDPREPRATIONLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("20");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(ROWTOROWLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating row to row data...");
                                for (int i=0;i<ROWTOROWLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = ROWTOROWLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("21");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(TARGETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                                for (int i=0;i<TARGETTYPELIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = TARGETTYPELIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("22");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }

                    if(SUPERVOISERLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supervisor data...");
                                for (int i=0;i<SUPERVOISERLIST.length();i++)
                                {
                                    try {
                                        tempInc++;
                                        JSONObject object = SUPERVOISERLIST.getJSONObject(i);
                                        MasterDropDown masterDropDown = new MasterDropDown();
                                        masterDropDown.setCode(object.getString("Code"));
                                        masterDropDown.setName(object.getString("Name"));
                                        masterDropDown.setType("23");
                                        dbh.insertMasterData(masterDropDown);
                                        publishProgress(tempInc);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                        });

                    }


                }

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                //AlertPopUp("Error:"+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                //AlertPopUp("Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                dialog.dismiss();

                //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");
            }
            catch (Exception e)
            {
                //AlertPopUp("Error:"+e.toString());
            }
        }
    }*/
    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
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
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
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

    public void openAgriInputReports(View view) {

        Intent intent=new Intent(context, AdminAgriInputReport.class);
        startActivity(intent);




    }
}
