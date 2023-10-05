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
import in.co.vibrant.bindalsugar.adapter.SupervisorSummeryAdapter;
import in.co.vibrant.bindalsugar.model.SupervisorSummary;
import in.co.vibrant.bindalsugar.model.VillageWiseCaneTypeSummeryModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaSurveyDashboard;


public class GetSupervisorSummery extends AppCompatActivity{

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    List <VillageWiseCaneTypeSummeryModel> dashboardData;
    String TAG="";
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListId;
    TextView subTotal1,subTotal2,subTotal3,Total,Avg,Projected;
    String defaultFactory;
    Toolbar toolbar;
    String factory,SupvcodeFrom,SupvcodeTo,VillageFrom,VillageTo,FromDate,ToDate;
    List<SupervisorSummary> supervisorSummaryList;



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
        FromDate=getIntent().getExtras().getString("DateFrom");
        ToDate=getIntent().getExtras().getString("DateTo");
        setTitle("Supervisor Summary");
        toolbar.setTitle("Supervisor Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GetSupervisorSummery.this,RaSurveyDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        if(new InternetCheck(GetSupervisorSummery.this).isOnline())
        {
            new GetDataList().execute(factory,SupvcodeFrom,SupvcodeTo,FromDate,ToDate);
        }
        else
        {
            Toast.makeText(GetSupervisorSummery.this,getString(R.string.oops_connect_your_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(GetSupervisorSummery.this, RaSurveyDashboard.class);
        startActivity(intent);
        finish();
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GetSupervisorSummery.this);
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
                GetSupervisorSummery.this);
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
        private ProgressDialog dialog = new ProgressDialog(GetSupervisorSummery.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GetSupervisorSummery.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetSupervisorSummary);
                request1.addProperty("factory", params[0]);
                request1.addProperty("SupvcodeFrom", params[1]);
                request1.addProperty("SupvcodeTo", params[2]);
                request1.addProperty("DateFrom", params[3]);
                request1.addProperty("DateTo", params[4]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetSupervisorSummary, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetSupervisorSummaryResult").toString();
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
            //dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            supervisorSummaryList=new ArrayList<>();

            try{
                dashboardData=new ArrayList <>();
                Log.d(TAG, message);
                JSONArray jsonArray=new JSONArray(message);
                if(jsonArray.length()==0)
                {
                    AlertPopUpFinish("No data found");
                }
                else {
                    DecimalFormat intFormat = new DecimalFormat("0");
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    SupervisorSummary header = new SupervisorSummary();
                    header.setSupervisorCode("SUP CODE");
                    header.setSupervisorName("SUP NAME");
                    header.setOnDateArea("ON DATE AREA");
                    header.setOnDatePlots("ON DATE PLOT");
                    header.setTotalArea("TO DATE AREA");
                    header.setTotalPlot("TO DATE PLOT");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(header);
                    double OnDateTArea=0,OnDateTPlots=0,TotalArea=0,TotalPLOTS=0;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonList=jsonArray.getJSONObject(i);
                        SupervisorSummary saleModel=new SupervisorSummary();
                        saleModel.setSupervisorCode(jsonList.getString("SUPVCODE"));
                        saleModel.setSupervisorName(jsonList.getString("SUPVNAME"));
                        saleModel.setOnDateArea(jsonList.getString("ONDATETAREA"));
                        saleModel.setOnDatePlots(jsonList.getString("ONDATETPLOTS"));
                        saleModel.setTotalArea(jsonList.getString("TOTALAREA"));
                        saleModel.setTotalPlot(jsonList.getString("TOTALPLOTS"));
                        OnDateTArea +=jsonList.getDouble("ONDATETAREA");
                        OnDateTPlots +=jsonList.getDouble("ONDATETPLOTS");
                        TotalArea +=jsonList.getDouble("TOTALAREA");
                        TotalPLOTS +=jsonList.getDouble("TOTALPLOTS");
                        if(i%2==0)
                        {
                            saleModel.setColor("#DFDFDF");
                            saleModel.setTextColor("#000000");
                        }
                        else
                        {
                            saleModel.setColor("#FFFFFF");
                            saleModel.setTextColor("#000000");
                        }
                        supervisorSummaryList.add(saleModel);
                    }
                    SupervisorSummary footer = new SupervisorSummary();
                    footer.setSupervisorCode("");
                    footer.setSupervisorName("Total");
                    footer.setOnDateArea(""+decimalFormat.format(OnDateTArea));
                    footer.setOnDatePlots(""+intFormat.format(OnDateTPlots));
                    footer.setTotalArea(""+decimalFormat.format(TotalArea));
                    footer.setTotalPlot(""+intFormat.format(TotalPLOTS));
                    footer.setColor("#000000");
                    footer.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(footer);
                    recyclerView =findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(GetSupervisorSummery.this, supervisorSummaryList.size(), GridLayoutManager.HORIZONTAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    SupervisorSummeryAdapter stockSummeryAdapter =new SupervisorSummeryAdapter(GetSupervisorSummery.this,supervisorSummaryList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().AlertPopUpFinish(GetSupervisorSummery.this,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(GetSupervisorSummery.this,"Error:"+e.toString());
            }
        }
    }


}