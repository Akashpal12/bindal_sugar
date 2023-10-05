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
import in.co.vibrant.bindalsugar.adapter.SupervisorCaneTypeCategorySummeryAdapter;
import in.co.vibrant.bindalsugar.model.CaneTypeCategorySummeryModel;
import in.co.vibrant.bindalsugar.model.VillageWiseCaneTypeSummeryModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaSurveyDashboard;


public class SummaryCaneTypeAndCategory extends AppCompatActivity{

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
    List<CaneTypeCategorySummeryModel> supervisorSummaryList;



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
        setTitle("Cane Type & Category");
        toolbar.setTitle("Cane Type & Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryCaneTypeAndCategory.this, RaSurveyDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        if(new InternetCheck(SummaryCaneTypeAndCategory.this).isOnline())
        {
            new GetDataList().execute(factory,SupvcodeFrom,SupvcodeTo,FromDate,ToDate);
        }
        else
        {
            Toast.makeText(SummaryCaneTypeAndCategory.this,getString(R.string.oops_connect_your_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(SummaryCaneTypeAndCategory.this,RaSurveyDashboard.class);
        startActivity(intent);
        finish();
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SummaryCaneTypeAndCategory.this);
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
                SummaryCaneTypeAndCategory.this);
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
        private ProgressDialog dialog = new ProgressDialog(SummaryCaneTypeAndCategory.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(SummaryCaneTypeAndCategory.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetSummaryCaneTypeAndCategory);
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetSummaryCaneTypeAndCategory, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetSummaryCaneTypeAndCategoryResult").toString();
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
                    DecimalFormat decimalFormat = new DecimalFormat("##.00");

                    CaneTypeCategorySummeryModel header = new CaneTypeCategorySummeryModel();
                    header.setCategory("");
                    header.setTodayRatoon("");
                    header.setTodayPlant("");
                    header.setTodayAutumn("Today");
                    header.setTodayArea("");
                    header.setTodayPer("");
                    header.setToDateRatoon("");
                    header.setToDatePlant("");
                    header.setToDateAutumn("Todate");
                    header.setToDateArea("");
                    header.setToDatePer("");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(header);
                    CaneTypeCategorySummeryModel header1 = new CaneTypeCategorySummeryModel();
                    header1.setCategory("Category");
                    header1.setTodayRatoon("Ratoon");
                    header1.setTodayPlant("Plant");
                    header1.setTodayAutumn("Autumn");
                    header1.setTodayArea("Area");
                    header1.setTodayPer("Per %");
                    header1.setToDateRatoon("Ratoon");
                    header1.setToDatePlant("Plant");
                    header1.setToDateAutumn("Autumn");
                    header1.setToDateArea("Area");
                    header1.setToDatePer("Per %");
                    header1.setColor("#000000");
                    header1.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(header1);
                    double ONDATERATOON=0,ONDATEPLANT=0,ONDATEAUTUMN=0,ONDATETOTAL=0,OnDatePercent=0;
                    double TODATERATOON=0,TODATEPLANT=0,TODATEAUTUMN=0,TODATETOTAL=0,OnDatePercent1=0;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonList=jsonArray.getJSONObject(i);
                        CaneTypeCategorySummeryModel data = new CaneTypeCategorySummeryModel();
                        data.setCategory(jsonList.getString("CATEGORY"));
                        data.setTodayRatoon(jsonList.getString("ONDATERATOON"));
                        data.setTodayPlant(jsonList.getString("ONDATEPLANT"));
                        data.setTodayAutumn(jsonList.getString("ONDATEAUTUMN"));
                        data.setTodayArea(jsonList.getString("ONDATETOTAL"));
                        data.setTodayPer(jsonList.getString("ONDATEPERCENT"));
                        data.setToDateRatoon(jsonList.getString("TODATERATOON"));
                        data.setToDatePlant(jsonList.getString("TODATEPLANT"));
                        data.setToDateAutumn(jsonList.getString("TODATEAUTUMN"));
                        data.setToDateArea(jsonList.getString("TODATETOTAL"));
                        data.setToDatePer(jsonList.getString("TODATEPERCENT"));
                        ONDATERATOON +=jsonList.getDouble("ONDATERATOON");
                        ONDATEPLANT +=jsonList.getDouble("ONDATEPLANT");
                        ONDATEAUTUMN +=jsonList.getDouble("ONDATEAUTUMN");
                        ONDATETOTAL +=jsonList.getDouble("ONDATETOTAL");
                        OnDatePercent +=jsonList.getDouble("ONDATEPERCENT");
                        TODATERATOON +=jsonList.getDouble("TODATERATOON");
                        TODATEPLANT +=jsonList.getDouble("TODATEPLANT");
                        TODATEAUTUMN +=jsonList.getDouble("TODATEAUTUMN");
                        TODATETOTAL +=jsonList.getDouble("TODATETOTAL");
                        OnDatePercent1 +=jsonList.getDouble("TODATEPERCENT");
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
                    CaneTypeCategorySummeryModel footer = new CaneTypeCategorySummeryModel();
                    footer.setCategory("Total");
                    footer.setTodayRatoon(""+decimalFormat.format(ONDATERATOON));
                    footer.setTodayPlant(""+decimalFormat.format(ONDATEPLANT));
                    footer.setTodayAutumn(""+decimalFormat.format(ONDATEAUTUMN));
                    footer.setTodayArea(""+decimalFormat.format(ONDATETOTAL));
                    footer.setTodayPer(""+decimalFormat.format(OnDatePercent));
                    footer.setToDateRatoon(""+decimalFormat.format(TODATERATOON));
                    footer.setToDatePlant(""+decimalFormat.format(TODATEPLANT));
                    footer.setToDateAutumn(""+decimalFormat.format(TODATEAUTUMN));
                    footer.setToDateArea(""+decimalFormat.format(TODATETOTAL));
                    footer.setToDatePer(""+decimalFormat.format(OnDatePercent1));
                    footer.setColor("#000000");
                    footer.setTextColor("#FFFFFF");
                    supervisorSummaryList.add(footer);
                    recyclerView =findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(SummaryCaneTypeAndCategory.this, supervisorSummaryList.size(), GridLayoutManager.HORIZONTAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    SupervisorCaneTypeCategorySummeryAdapter stockSummeryAdapter =new SupervisorCaneTypeCategorySummeryAdapter(SummaryCaneTypeAndCategory.this,supervisorSummaryList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().AlertPopUpFinish(SummaryCaneTypeAndCategory.this,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(SummaryCaneTypeAndCategory.this,"Error:"+e.toString());
            }
        }
    }


}