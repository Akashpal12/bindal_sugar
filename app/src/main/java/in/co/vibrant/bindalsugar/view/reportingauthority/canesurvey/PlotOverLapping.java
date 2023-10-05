package in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import in.co.vibrant.bindalsugar.adapter.PlotOverLappingAdapter;
import in.co.vibrant.bindalsugar.model.PlotOverLappingModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaSurveyDashboard;


public class PlotOverLapping extends AppCompatActivity{

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    //List <PlotOverLappingModel> dashboardData;
    String TAG="";
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListId;
    TextView subTotal1,subTotal2,subTotal3,Total,Avg,Projected;
    String defaultFactory;
    Toolbar toolbar;
    String factory,SupvcodeFrom,SupvcodeTo,VillageFrom,VillageTo,percent;
    List<PlotOverLappingModel> supervisorSummaryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cane_type_summery);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        spinner=findViewById(R.id.spinner);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        /*Total=findViewById(R.id.tv_total_crushing);
        Avg=findViewById(R.id.tv_avg_crushing);
        Projected=findViewById(R.id.tv_projected_crushing);*/
        factory=getIntent().getExtras().getString("factory");
        SupvcodeFrom=getIntent().getExtras().getString("SupvcodeFrom");
        SupvcodeTo=getIntent().getExtras().getString("SupvcodeTo");
        VillageFrom=getIntent().getExtras().getString("VillageFrom");
        VillageTo=getIntent().getExtras().getString("VillageTo");
        percent=getIntent().getExtras().getString("report_percent");
        setTitle("Plot Overlapping");
        toolbar.setTitle("Plot Overlapping");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlotOverLapping.this, RaSurveyDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        if(new InternetCheck(PlotOverLapping.this).isOnline())
        {
            new GetDataList().execute(factory,SupvcodeFrom,SupvcodeTo,VillageFrom,VillageTo,percent);
        }
        else
        {
            Toast.makeText(PlotOverLapping.this,getString(R.string.oops_connect_your_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(PlotOverLapping.this,RaSurveyDashboard.class);
        startActivity(intent);
        finish();
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PlotOverLapping.this);
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

    private void AlertPopUpFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PlotOverLapping.this);
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

    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(PlotOverLapping.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(PlotOverLapping.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetPlotOverLap);
                request1.addProperty("factory", params[0]);
                request1.addProperty("SupvcodeFrom", params[1]);
                request1.addProperty("SupvcodeTo", params[2]);
                request1.addProperty("VillageFrom", params[3]);
                request1.addProperty("VillageTo", params[4]);
                request1.addProperty("percent", params[5]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetPlotOverLap, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetPlotOverLapResult").toString();
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

            Log.d(TAG, "onPostExecute: " + result);
            supervisorSummaryList=new ArrayList<>();
            try{
                //dashboardData=new ArrayList <>();
                Log.d(TAG, message);
                JSONArray jsonArray=new JSONArray(message);
                if(jsonArray.length()==0)
                {
                    AlertPopUpFinish("No data found");
                }
                else {
                    DecimalFormat decimalFormat = new DecimalFormat("##.00");

                    PlotOverLappingModel header = new PlotOverLappingModel();
                    header.setFactory("Factory");
                    header.setSrNo("SrNo");
                    header.setPlotType("Plot Type");
                    header.setPlotVillCode("PlotVillCode");
                    header.setPlotVillageName("PlotVillageName");
                    header.setPlotSerial("PlotSerial");
                    header.setVillCode("VillCode");
                    header.setVillageName("VillageName");
                    header.setGrwCode("GrwCode");
                    header.setName("Name");
                    header.setFatherName("FatherName");
                    header.setGrwSrNo("GrwSrNo");
                    header.setShareArea("ShareArea");
                    header.setSharePer("Share%");
                    header.setPlotArea("PlotArea");
                    header.setOverLapPer("OverLap%");
                    header.setSurveyDate("SurveyDate");
                    header.setSurveyorCode("SurveyorCode");
                    header.setSurveyorName("SurveyorName");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(header);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonList=jsonArray.getJSONObject(i);
                        PlotOverLappingModel data = new PlotOverLappingModel();
                        data.setFactory(factory);
                        data.setSrNo(jsonList.getString("SrNo"));
                        data.setPlotType(jsonList.getString("PlotType"));
                        data.setPlotVillCode(jsonList.getString("PlotVillCode"));
                        data.setPlotVillageName(jsonList.getString("PlotVillageName"));
                        data.setPlotSerial(jsonList.getString("PlotSerial"));
                        data.setVillCode(jsonList.getString("VillCode"));
                        data.setVillageName(jsonList.getString("VillageName"));
                        data.setGrwCode(jsonList.getString("GrwCode"));
                        data.setName(jsonList.getString("Name"));
                        data.setFatherName(jsonList.getString("FatherName"));
                        data.setGrwSrNo(jsonList.getString("GrwSrNo"));
                        data.setShareArea(jsonList.getString("ShareArea"));
                        data.setSharePer(jsonList.getString("SharePer"));
                        data.setPlotArea(jsonList.getString("PlotArea"));
                        data.setOverLapPer(jsonList.getString("OverLapPer"));
                        data.setSurveyDate(jsonList.getString("SurveyDate"));
                        data.setSurveyorCode(jsonList.getString("surveyorCode"));
                        data.setSurveyorName(jsonList.getString("SurveyorName"));
                        if(i%2==0)
                        {
                            data.setColor("#DFDFDF");
                            data.setTextColor("#000000");
                        }
                        else
                        {
                            data.setColor("#FFFFFF");
                            data.setTextColor("#000000");
                        }
                        supervisorSummaryList.add(data);
                    }

                    recyclerView =findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(PlotOverLapping.this, supervisorSummaryList.size(), GridLayoutManager.HORIZONTAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    PlotOverLappingAdapter stockSummeryAdapter =new PlotOverLappingAdapter(PlotOverLapping.this,supervisorSummaryList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().AlertPopUpFinish(PlotOverLapping.this,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(PlotOverLapping.this,"Error:"+e.toString());
            }
        }
    }


}