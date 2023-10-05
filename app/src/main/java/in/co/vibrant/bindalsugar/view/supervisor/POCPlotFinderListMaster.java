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
import in.co.vibrant.bindalsugar.adapter.POCPlotListMasterAdapter;
import in.co.vibrant.bindalsugar.adapter.PocSingleFindPlotListAdapter;
import in.co.vibrant.bindalsugar.model.POCGrowerFinderModel;
import in.co.vibrant.bindalsugar.model.POCPlotListDataModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class POCPlotFinderListMaster extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    List <POCGrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    List<POCPlotListDataModel> pocPlotListDataModelList;
    String TAG="";
    Toolbar toolbar;
    String Lat,Lng;
    Context context;
    JSONArray JsonData;
    String currentPlotColorCode,currentPlotDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poc_verital_check_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= POCPlotFinderListMaster.this;
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        //Lat="28.896658";
        //Lng="78.3980435";
        Lat=getIntent().getExtras().getString("lat");
        Lng=getIntent().getExtras().getString("lng");
        currentPlotColorCode=getIntent().getExtras().getString("selectedColorCode");
        currentPlotDisease=getIntent().getExtras().getString("selectedDisease");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);

        TextView latitudeTxt=findViewById(R.id.latitude);
        //latitudeTxt.setText("Lat:"+Lat);
        double number = Double.parseDouble(Lat);
        String formattedNumber = String.format("%.6f", number);  //  To Print Starting 6 Digit Number after the decimal Number
        latitudeTxt.setText("Lat:"+formattedNumber);

        TextView longitudeTxt=findViewById(R.id.longitude);
      //  longitudeTxt.setText("Color : "+currentPlotDisease+"    ||  Desease : "+currentPlotDisease);
       longitudeTxt.setText("Lng:"+Lng);
        TextView date=findViewById(R.id.date);
        date.setText("Date:"+dateFormat.format(today));

        TextView android_version=findViewById(R.id.android_version);
        android_version.setText("Android:"+ Build.VERSION.RELEASE);
        TextView app_version=findViewById(R.id.app_version);
        app_version.setText("App Version:"+BuildConfig.VERSION_NAME);
        TextView model=findViewById(R.id.model);
        model.setText("Model:"+ Build.MODEL);
        pocPlotListDataModelList=new ArrayList<>();

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
                String url = APIUrl.BASE_URL + "/checkPocPolygonGrower_New1";
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
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Error: No details found");
                    }
                    else {
                        double totalNumber=0;
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            POCGrowerFinderModel pocGrowerFinderModel = new POCGrowerFinderModel();
                            pocGrowerFinderModel.setSEAS(jsonObject.getString("SEAS")); //.replace(".0",""));
                            pocGrowerFinderModel.setVILLAGE_ID(jsonObject.getString("VILLAGE_ID"));
                            pocGrowerFinderModel.setV_NAME(jsonObject.getString("V_NAME"));
                            pocGrowerFinderModel.setG_NAME(jsonObject.getString("G_NAME")); //.replace(".0",""));
                            pocGrowerFinderModel.setG_CODE(jsonObject.getString("G_CODE")); //.replace(".0",""));
                            pocGrowerFinderModel.setPLOTVILLAGE(jsonObject.getString("PLOTVILLAGE"));
                            pocGrowerFinderModel.setG_VILLAGE(jsonObject.getString("G_VILLAGE"));
                            pocGrowerFinderModel.setPLOT_SERIAL_NO(jsonObject.getString("PLOT_SERIAL_NO"));
                            pocGrowerFinderModel.setSUB_PLOT_VERIETY(jsonObject.getString("SUB_PLOT_VERIETY"));
                            pocGrowerFinderModel.setVERIETYNAME(jsonObject.getString("VERIETYNAME"));
                            pocGrowerFinderModel.setPLANT_TYPE(jsonObject.getString("PLANT_TYPE"));
                            pocGrowerFinderModel.setCANEAREA(jsonObject.getString("CANEAREA"));
                            pocGrowerFinderModel.setSERVEYDATE(jsonObject.getString("SERVEYDATE"));
                            pocGrowerFinderModel.setDATEOFPLANTING(jsonObject.getString("DATEOFPLANTING"));
                            pocGrowerFinderModel.setSHAREPERCENTGE(jsonObject.getString("SHAREPERCENTGE"));
                            pocGrowerFinderModel.setCROPTYPENAME(jsonObject.getString("CROPTYPENAME"));
                            pocGrowerFinderModel.setDISEASE(jsonObject.getString("DISEASE"));
                            pocGrowerFinderModel.setCOLORCODE(jsonObject.getString("COLORCODE"));
                            pocGrowerFinderModel.setGH_NE_LAT(jsonObject.getDouble(("GH_NE_LAT")));
                            pocGrowerFinderModel.setGH_NE_LNG(jsonObject.getDouble(("GH_NE_LNG")));
                            pocGrowerFinderModel.setGH_NW_LAT(jsonObject.getDouble(("GH_NW_LAT")));
                            pocGrowerFinderModel.setGH_NW_LNG(jsonObject.getDouble(("GH_NW_LNG")));
                            pocGrowerFinderModel.setGH_SW_LAT(jsonObject.getDouble(("GH_SW_LAT")));
                            pocGrowerFinderModel.setGH_SW_LNG(jsonObject.getDouble(("GH_SW_LNG")));
                            pocGrowerFinderModel.setGH_SE_LAT(jsonObject.getDouble(("GH_SE_LAT")));
                            pocGrowerFinderModel.setGH_SE_LNG(jsonObject.getDouble(("GH_SE_LNG")));

                            pocGrowerFinderModel.setGF_NAME(jsonObject.getString("GF_NAME"));
                            pocGrowerFinderModel.setVILLAGE_ID(jsonObject.getString("VILLAGE_ID"));
                            pocGrowerFinderModel.setG_MOBNO(jsonObject.getString("G_MOBNO"));
                            pocGrowerFinderModel.setPLOTAREA(jsonObject.getString("PLOTAREA"));
                            pocGrowerFinderModel.setSUVTYPE(jsonObject.getString("SUVTYPE"));
                            pocGrowerFinderModel.setLat(Lat);
                            pocGrowerFinderModel.setLang(Lng);

                            dashboardData.add(pocGrowerFinderModel);



                        }
                        recyclerView =findViewById(R.id.recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        POCPlotListMasterAdapter listGrowerFinderAdapter =new POCPlotListMasterAdapter(context,dashboardData);
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
                //AlertPopUpFinish("Error: "+e.toString());
            }
        }




    }
   void PlotListData(){
        try {
            if(pocPlotListDataModelList.size()>0)
                pocPlotListDataModelList.clear();

            if (JsonData.length()>0){

                for (int i=0;i<JsonData.length();i++){
                    JSONObject jsonObject = JsonData.getJSONObject(i);
                    POCPlotListDataModel pocPlotListDataModel=new POCPlotListDataModel();
                    pocPlotListDataModel.setPOCCODE(jsonObject.getString("POCCODE"));
                    pocPlotListDataModel.setPOCNAME(jsonObject.getString("POCNAME"));
                    pocPlotListDataModel.setOPCID(jsonObject.getString("OPCID"));
                    pocPlotListDataModelList.add(pocPlotListDataModel);
                }

            }else {
                new AlertDialogManager().RedDialog(context,"No Data Found");
            }

            recyclerView =findViewById(R.id.plot_list_recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            PocSingleFindPlotListAdapter listGrowerFinderAdapter =new PocSingleFindPlotListAdapter(context,pocPlotListDataModelList);
            recyclerView.setAdapter(listGrowerFinderAdapter);


        }catch (Exception e){
            e.getMessage();
        }

    }


}