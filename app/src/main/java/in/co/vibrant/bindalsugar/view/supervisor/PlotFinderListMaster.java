package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.FindPlotListAdapter;
import in.co.vibrant.bindalsugar.adapter.ListPlotListMasterAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.PlotListDataModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class PlotFinderListMaster extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    List <GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    List<PlotListDataModel> plotListDataModelList;
    String TAG="";
    Toolbar toolbar;
    String Lat,Lng;
    CardView deseas_cardView;
    Context context;
    JSONArray JsonData;
    String diseasData="";
    TextView deaseas_txt;
    ImageView remedial_image;
    String currentPlotColorCode,currentPlotDisease;
    SessionConfig sessionConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verital_check_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= PlotFinderListMaster.this;
        sessionConfig=new SessionConfig(context);
        remedial_image=findViewById(R.id.remedial_image);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        Lat=getIntent().getExtras().getString("lat");
        Lng=getIntent().getExtras().getString("lng");
        currentPlotColorCode=getIntent().getExtras().getString("selectedColorCode");
        currentPlotDisease=getIntent().getExtras().getString("selectedDisease");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);

        TextView latitudeTxt=findViewById(R.id.latitude);
        double number = Double.parseDouble(Lat);
        String formattedNumber = String.format("%.6f", number);  //  To Print Starting 6 Digit Number after the decimal Number
        latitudeTxt.setText("Lat:"+formattedNumber);
        TextView longitudeTxt=findViewById(R.id.longitude);
        longitudeTxt.setText("Lng:"+Lng);
        TextView date=findViewById(R.id.date);
        date.setText("Date:"+dateFormat.format(today));

        TextView android_version=findViewById(R.id.android_version);
        android_version.setText("Android:"+ Build.VERSION.RELEASE);
        TextView app_version=findViewById(R.id.app_version);
        app_version.setText("App Version:"+BuildConfig.VERSION_NAME);
        TextView model=findViewById(R.id.model);
        model.setText("Model:"+ Build.MODEL);
        plotListDataModelList=new ArrayList<>();

        LinearLayout latlng_layout=findViewById(R.id.latlng_layout);
        latlng_layout.setVisibility(View.VISIBLE);
        userDetailsModels=dbh.getUserDetailsModel();
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
        if(new InternetCheck(context).isOnline())
        {
            new GetDataList().execute(Lat,Lng);
        }
        else
        {
            //AlertPopUp("No internet found");
        }

        deseas_cardView=findViewById(R.id.deseas_cardView);
        deaseas_txt=findViewById(R.id.deaseas_txt);
        remedial_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(context, RemadialFinderListMaster.class);
                   intent.putExtra("lat", Lat);
                   intent.putExtra("lng", Lng);
                   context.startActivity(intent);
            }
        });
       


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
                String url = APIUrl.BASE_URL + "/checkPolygonGrower_New1";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lng", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("imeino", getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpGet, responseHandler);

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            catch (Exception e) {
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
            try{
                Log.d(TAG, message);
                JSONObject obj=new JSONObject(message);
                if(obj.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=obj.getJSONArray("DATA");
                    JsonData = jsonArray.getJSONObject(0).getJSONArray("JSONDATA");
                    diseasData=jsonArray.getJSONObject(0).getString("DISEASE");
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Error: No details found");
                    }
                    else {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GrowerFinderModel growerFinderModel = new GrowerFinderModel();
                            growerFinderModel.setPlotVillageCode(jsonObject.getString("PLOTVILLAGE").replace(".0",""));
                            growerFinderModel.setPlotVillageName(jsonObject.getString("G_VILLAGE"));
                            growerFinderModel.setGrowerName(jsonObject.getString("G_NAME"));
                            growerFinderModel.setGrowerCode(jsonObject.getString("G_CODE").replace(".0",""));
                            growerFinderModel.setVillageCode(jsonObject.getString("VILLAGE_ID").replace(".0",""));
                            growerFinderModel.setVillageName(jsonObject.getString("V_NAME"));
                            growerFinderModel.setFather(jsonObject.getString("GF_NAME"));
                            growerFinderModel.setId("---");
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
                            growerFinderModel.setPlotNo(jsonObject.getString("PLOT_SERIAL_NO").replace(".0",""));
                            growerFinderModel.setMobile(jsonObject.getString("G_MOBNO"));
                            growerFinderModel.setVarietyGroupCode(jsonObject.getString("VARIETY_GRP_NAME"));
                            growerFinderModel.setGroupArea(jsonObject.getString("PLOTAREA"));
                            growerFinderModel.setPlotPercentage(jsonObject.getString("SHAREPERCENTGE").replace(".0",""));
                            growerFinderModel.setVariety(jsonObject.getString("VERIETYNAME"));
                            growerFinderModel.setPrakar(jsonObject.getString("CROPTYPENAME"));
                            growerFinderModel.setDataFrom(jsonObject.getString("SERVEYDATE"));
                            growerFinderModel.setGhId(jsonObject.getString("GH_ID").replace(".0",""));
                            growerFinderModel.setSuvType(jsonObject.getString("SUVTYPE"));
                            growerFinderModel.setSeason(jsonObject.getString("SEAS"));
                            growerFinderModel.setDisease(jsonObject.getString("DISEASE"));
                            sessionConfig.setDisease(jsonObject.getString("DISEASE"));
                            growerFinderModel.setJsonArray(jsonObject.getJSONArray("JSONDATA"));
                            dashboardData.add(growerFinderModel);
                        }
                        if (sessionConfig.getDisease().isEmpty() || sessionConfig.getDisease().equalsIgnoreCase("null")){
                            deseas_cardView.setVisibility(View.GONE);
                        }
                        else {
                            deseas_cardView.setVisibility(View.VISIBLE);
                            deaseas_txt.setText(""+sessionConfig.getDisease());
                        }
                        recyclerView =findViewById(R.id.recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        ListPlotListMasterAdapter listGrowerFinderAdapter =new ListPlotListMasterAdapter(context,dashboardData);
                        recyclerView.setAdapter(listGrowerFinderAdapter);
                        PlotListData();
                    }
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,obj.getString("MSG"));
                }
            }
            catch (JSONException e)
            {
                new AlertDialogManager().RedDialog(context,message);
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
        }
    }
    void PlotListData(){
        try {
            if(plotListDataModelList.size()>0)
                plotListDataModelList.clear();
            if (JsonData.length()>0){
                for (int i=0;i<JsonData.length();i++){
                    JSONObject jsonObject = JsonData.getJSONObject(i);
                    PlotListDataModel plotListDataModel=new PlotListDataModel();
                    plotListDataModel.setACT_CODE(jsonObject.getString("ACT_CODE"));
                    plotListDataModel.setACT_NAME(jsonObject.getString("ACT_NAME"));
                    plotListDataModel.setCROPCONDITION(jsonObject.getString("CROPCONDITION"));
                    plotListDataModel.setDISEASE(jsonObject.getString("DISEASE"));
                    plotListDataModel.setIRRIGATION(jsonObject.getString("IRRIGATION"));
                    plotListDataModel.setCOJ_CANEERATHING(jsonObject.getString("COJ_CANEERATHING"));
                    plotListDataModel.setACTIVITYTYPE(jsonObject.getString("COJ_CANEPROPPING"));
                    plotListDataModel.setACTIVITYMATHOD(jsonObject.getString("ACTIVITYMATHOD"));
                    plotListDataModel.setDATE(jsonObject.getString("DATE"));
                    plotListDataModelList.add(plotListDataModel);
                }
            }else {
                new AlertDialogManager().RedDialog(context,"No Data Found");
            }
            recyclerView =findViewById(R.id.plot_list_recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            FindPlotListAdapter listGrowerFinderAdapter =new FindPlotListAdapter(context,plotListDataModelList);
            recyclerView.setAdapter(listGrowerFinderAdapter);
        }catch (Exception e){
            e.getMessage();
        }

    }


}