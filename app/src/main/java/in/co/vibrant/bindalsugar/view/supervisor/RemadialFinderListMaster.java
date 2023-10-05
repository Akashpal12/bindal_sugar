package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListRemadialMasterAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.adapter.TabAdapter;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.RemedialJsonDataModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class RemadialFinderListMaster extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue = true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    List<GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    List<RemedialJsonDataModel> remedialJsonDataModelList;
    String TAG = "", PlotDisease="", PlotColourCode="";
    Toolbar toolbar;
    String Lat, Lng;
    Context context;
    JSONArray JsonData;
    String currentPlotColorCode, currentPlotDisease;
    LinearLayout remedial_ly;
    TextView remedial_txt;
    TabLayout tabLayout;
    ViewPager viewPager;
   SessionConfig sessionConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verital_remedial_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = RemadialFinderListMaster.this;
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData = new ArrayList<>();
        //Lat="28.896658";
        //Lng="78.3980435";
        Lat = getIntent().getExtras().getString("lat");
        Lng = getIntent().getExtras().getString("lng");
        sessionConfig = new SessionConfig(RemadialFinderListMaster.this);


       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        Date today = Calendar.getInstance().getTime();
        String currDate = dateFormat.format(today);
        remedial_txt = findViewById(R.id.remadial_txt);
        remedial_ly = findViewById(R.id.remedial_ly);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);


        TabAdapter tabAdapter=new TabAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        TextView latitudeTxt = findViewById(R.id.latitude);
        //latitudeTxt.setText("Lat:"+Lat);
        double number = Double.parseDouble(Lat);
        sessionConfig.setLat(Lat);
        sessionConfig.setLng(Lng);
        String formattedNumber = String.format("%.6f", number);  //  To Print Starting 6 Digit Number after the decimal Number
        latitudeTxt.setText("Lat:" + formattedNumber);

        TextView longitudeTxt = findViewById(R.id.longitude);
        //  longitudeTxt.setText("Color : "+currentPlotDisease+"    ||  Desease : "+currentPlotDisease);
        longitudeTxt.setText("Lng:" + Lng);
        TextView date = findViewById(R.id.date);
        date.setText("Date:" + dateFormat.format(today));

        TextView android_version = findViewById(R.id.android_version);
        android_version.setText("Android:" + Build.VERSION.RELEASE);
        TextView app_version = findViewById(R.id.app_version);
        app_version.setText("App Version:" + BuildConfig.VERSION_NAME);
        TextView model = findViewById(R.id.model);
        model.setText("Model:" + Build.MODEL);
        remedialJsonDataModelList = new ArrayList<>();

        LinearLayout latlng_layout = findViewById(R.id.latlng_layout);
        latlng_layout.setVisibility(View.VISIBLE);
        userDetailsModels = dbh.getUserDetailsModel();
        setTitle("Find Plot List");
        toolbar.setTitle("Find Plot List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (new InternetCheck(context).isOnline()) {
            new GetDataList().execute(Lat, Lng);

        } else {
            //AlertPopUp("No internet found");
        }



    }


    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL + "/checkRemidialPolygonGrower_New1";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lng", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpGet, responseHandler);

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
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            try {
                Log.d(TAG, message);
                JSONObject obj = new JSONObject(message);
                if (obj.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = obj.getJSONArray("DATA");
                    JsonData = jsonArray.getJSONObject(0).getJSONArray("JSONDATA");
                    if (jsonArray.length() == 0) {
                        new AlertDialogManager().RedDialog(context, "Error: No details found");
                    } else {

                        double totalNumber = 0;
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GrowerFinderModel growerFinderModel = new GrowerFinderModel();
                            growerFinderModel.setPlotVillageCode(jsonObject.getString("PLOTVILLAGE").replace(".0", "")); //--5
                            growerFinderModel.setPlotVillageName(jsonObject.getString("G_VILLAGE"));
                            growerFinderModel.setGrowerName(jsonObject.getString("G_NAME")); //--6
                            growerFinderModel.setGrowerCode(jsonObject.getString("G_CODE").replace(".0", ""));//--3
                            growerFinderModel.setVillageCode(jsonObject.getString("VILLAGE_ID").replace(".0", ""));//--1
                            growerFinderModel.setVillageName(jsonObject.getString("V_NAME"));//--2
                            growerFinderModel.setFather(jsonObject.getString("GF_NAME"));
                            growerFinderModel.setId("---");
                            PlotDisease =jsonObject.getString("DISEASE");
                            PlotColourCode = jsonObject.getString("COLORCODE");
                            growerFinderModel.setCaneArea(jsonObject.getString("CANEAREA"));
                            growerFinderModel.setSurveyDate(jsonObject.getString("SERVEYDATE"));
                            growerFinderModel.setDopDate(jsonObject.getString("DATEOFPLANTING"));
                            growerFinderModel.setNorthEastLat(jsonObject.getDouble("GH_NE_LAT"));
                            growerFinderModel.setNorthEastLng(jsonObject.getDouble("GH_NE_LNG"));
                            growerFinderModel.setNorthWestLat(jsonObject.getDouble("GH_NW_LAT"));
                            growerFinderModel.setNorthWestLng(jsonObject.getDouble("GH_NW_LNG"));
                            growerFinderModel.setSouthEastLat(jsonObject.getDouble("GH_SE_LAT"));
                            growerFinderModel.setSouthEastLng(jsonObject.getDouble("GH_SE_LNG"));
                            growerFinderModel.setSouthWestLat(jsonObject.getDouble("GH_SW_LAT"));
                            growerFinderModel.setSouthWestLng(jsonObject.getDouble("GH_SW_LNG"));
                            growerFinderModel.setPlotNo(jsonObject.getString("PLOT_SERIAL_NO").replace(".0", "")); //--4

                            growerFinderModel.setMobile(jsonObject.getString("G_MOBNO"));
                            growerFinderModel.setVarietyGroupCode(jsonObject.getString("VARIETY_GRP_NAME"));
                            growerFinderModel.setGroupArea(jsonObject.getString("PLOTAREA"));
                            growerFinderModel.setPlotPercentage(jsonObject.getString("SHAREPERCENTGE").replace(".0", ""));
                            growerFinderModel.setVariety(jsonObject.getString("VERIETYNAME"));
                            growerFinderModel.setPrakar(jsonObject.getString("CROPTYPENAME"));
                            growerFinderModel.setDataFrom(jsonObject.getString("SERVEYDATE"));
                            growerFinderModel.setGhId(jsonObject.getString("GH_ID").replace(".0", ""));
                            growerFinderModel.setSuvType(jsonObject.getString("SUVTYPE"));
                            growerFinderModel.setSeason(jsonObject.getString("SEAS"));
                            growerFinderModel.setJsonArray(jsonObject.getJSONArray("JSONDATA"));
                            growerFinderModel.setDeseaseCode(jsonObject.getString("DISEASECODE")); //--7
                            growerFinderModel.setDisease(jsonObject.getString("DISEASE")); //--8
                            remedial_txt.setText(jsonObject.getString("DISEASECODE")+" / "+jsonObject.getString("DISEASE"));
                            growerFinderModel.setLAT(Lat);
                            growerFinderModel.setLONG(Lng);

                            dashboardData.add(growerFinderModel);
                        }
                        recyclerView = findViewById(R.id.recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        ListRemadialMasterAdapter listGrowerFinderAdapter = new ListRemadialMasterAdapter(context, dashboardData);
                        recyclerView.setAdapter(listGrowerFinderAdapter);
                        PlotListData();


                    }
                } else {
                    new AlertDialogManager().RedDialog(context, obj.getString("MSG"));
                }


            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, message);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                //AlertPopUpFinish("Error: "+e.toString());
            }
        }


    }

    void PlotListData() {
        try {
            if (remedialJsonDataModelList.size() > 0)
                remedialJsonDataModelList.clear();

            if (JsonData.length() > 0) {

                for (int i = 0; i < JsonData.length(); i++) {
                    JSONObject jsonObject = JsonData.getJSONObject(i);
                    RemedialJsonDataModel remedialJsonDataModel = new RemedialJsonDataModel();
                    remedialJsonDataModel.setREMICODE(jsonObject.getString("REMICODE"));
                    remedialJsonDataModel.setREMIDIALNAME(jsonObject.getString("REMIDIALNAME"));
                    remedialJsonDataModel.setDAYES(jsonObject.getString("DAYES"));
                    remedialJsonDataModel.setSTATUS(jsonObject.getString("STATUS"));
                    remedialJsonDataModelList.add(remedialJsonDataModel);



                }

            } else {
                new AlertDialogManager().RedDialog(context, "No Data Found");
            }



           /* recyclerView = findViewById(R.id.plot_list_recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            RemedialJsonDataAdapter listGrowerFinderAdapter = new RemedialJsonDataAdapter(context, remedialJsonDataModelList);
            recyclerView.setAdapter(listGrowerFinderAdapter);*/


        } catch (Exception e) {
            e.getMessage();
        }

    }


}