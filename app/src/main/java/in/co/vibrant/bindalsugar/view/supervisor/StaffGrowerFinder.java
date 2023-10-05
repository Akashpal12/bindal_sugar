package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListGrowerFinderAdapter;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class StaffGrowerFinder extends AppCompatActivity {

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
    String TAG="";
    Toolbar toolbar;
    String Lat,Lng;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_finder);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=StaffGrowerFinder.this;
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        Lat=getIntent().getExtras().getString("lat");
        Lng=getIntent().getExtras().getString("lng");
        userDetailsModels=dbh.getUserDetailsModel();
        setTitle(getString(R.string.MENU_GROWER_FINDER));
        toolbar.setTitle(getString(R.string.MENU_GROWER_FINDER));
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
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_PlotDetailsLatLong);
                request1.addProperty("imeino",new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("lat", params[0]);
                request1.addProperty("lng", params[1]);
                //request1.addProperty("lat", "29.006258333333");
                //request1.addProperty("lng", "78.311010");
                request1.addProperty("Divn", userDetailsModels.get(0).getDivision());
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_PlotDetailsLatLong, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("PlotDetailsLatLongResult").toString();
                }
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
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"Error: No details found");
                    }
                    else {
                        double totalNumber=0;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GrowerFinderModel growerFinderModel = new GrowerFinderModel();
                            growerFinderModel.setPlotVillageCode(jsonObject.getString("VILLAGE_ID"));
                            growerFinderModel.setPlotVillageName(jsonObject.getString("V_NAME"));
                            growerFinderModel.setGrowerName(jsonObject.getString("G_NAME"));
                            growerFinderModel.setGrowerCode(jsonObject.getString("G_CODE"));
                            growerFinderModel.setVillageCode(jsonObject.getString("VILLAGE_ID"));
                            growerFinderModel.setVillageName(jsonObject.getString("G_VILLAGE"));
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

                            growerFinderModel.setPlotNo(jsonObject.getString("PLOT_SERIAL_NO"));
                            growerFinderModel.setMobile("---");
                            growerFinderModel.setVarietyGroupCode(jsonObject.getString("VERIETYNAME"));
                            growerFinderModel.setGroupArea(jsonObject.getString("CANEAREA"));
                            growerFinderModel.setPlotPercentage(jsonObject.getString("SHAREPERCENTGE"));
                            growerFinderModel.setVariety(jsonObject.getString("VERIETYNAME"));
                            growerFinderModel.setPrakar(jsonObject.getString("CROPTYPENAME"));
                            growerFinderModel.setDataFrom(jsonObject.getString("SERVEYDATE"));
                            dashboardData.add(growerFinderModel);
                        }
                        recyclerView =findViewById(R.id.recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        ListGrowerFinderAdapter listGrowerFinderAdapter =new ListGrowerFinderAdapter(context,dashboardData);
                        recyclerView.setAdapter(listGrowerFinderAdapter);
                    }
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,obj.getString("MSG"));
                }
            }
            catch (JSONException e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                //AlertPopUpFinish("Error: "+e.toString());
            }
        }
    }


}