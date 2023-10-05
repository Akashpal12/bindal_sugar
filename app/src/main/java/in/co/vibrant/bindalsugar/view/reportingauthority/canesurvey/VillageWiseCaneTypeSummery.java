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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListGetVillageWiseCaneTypeSummeryAdapter;
import in.co.vibrant.bindalsugar.model.VillageWiseCaneTypeSummeryModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaSurveyDashboard;


public class VillageWiseCaneTypeSummery extends AppCompatActivity{

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
        FromDate=getIntent().getExtras().getString("DateFrom");
        ToDate=getIntent().getExtras().getString("DateTo");
        setTitle(getString(R.string.MENU_VILLAGE_WISE_CANETYPE_SUMMARY));
        toolbar.setTitle(getString(R.string.MENU_VILLAGE_WISE_CANETYPE_SUMMARY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VillageWiseCaneTypeSummery.this, RaSurveyDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        if(new InternetCheck(VillageWiseCaneTypeSummery.this).isOnline())
        {
            new GetDataList().execute(factory,SupvcodeFrom,SupvcodeTo,VillageFrom,VillageTo,FromDate,ToDate);
        }
        else
        {
            Toast.makeText(VillageWiseCaneTypeSummery.this,getString(R.string.oops_connect_your_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(VillageWiseCaneTypeSummery.this,RaSurveyDashboard.class);
        startActivity(intent);
        finish();
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                VillageWiseCaneTypeSummery.this);
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
                VillageWiseCaneTypeSummery.this);
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
        private ProgressDialog dialog = new ProgressDialog(VillageWiseCaneTypeSummery.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(VillageWiseCaneTypeSummery.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetSupervisorCaneTypeAreaNew);
                request1.addProperty("factory", params[0]);
                request1.addProperty("SupvcodeFrom", params[1]);
                request1.addProperty("SupvcodeTo", params[2]);
                request1.addProperty("VillageFrom", params[3]);
                request1.addProperty("VillageTo", params[4]);
                request1.addProperty("DateFrom", params[5]);
                request1.addProperty("DateTo", params[6]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetSupervisorCaneTypeAreaNew, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetSupervisorCaneTypeAreaNewResult").toString();
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

            try{
                dashboardData=new ArrayList <>();
                Log.d(TAG, message);
                JSONArray jsonArray=new JSONArray(message);
                if(jsonArray.length()==0)
                {
                    AlertPopUpFinish("No data found");
                }
                else {
                    //Map<String,Object> supData=new HashMap<>();

                    //List<Object> supData1=new ArrayList<>();
                    /*List<Object> villData1=new ArrayList<>();
                    List<Object> caneType=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(i>0)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i-1);
                            if(jsonObject1.getInt("SUPVCODE") == jsonObject.getInt("SUPVCODE"))
                            {
                                if(jsonObject1.getInt("VCODE") == jsonObject.getInt("VCODE"))
                                {
                                    Map<String,String> dataMap=new HashMap<>();
                                    dataMap.put("ATNAME",jsonObject.getString("ATNAME"));
                                    dataMap.put("PLOTS",jsonObject.getString("PLOTS"));
                                    dataMap.put("GROWCOUNT",jsonObject.getString("GROWCOUNT"));
                                    dataMap.put("AREA",jsonObject.getString("AREA"));
                                    caneType.add(dataMap);
                                }
                                else
                                {
                                    List<Object> finalCaneType=new ArrayList<>();
                                    for(int caneTypeinc=0;caneTypeinc<caneType.size();caneTypeinc++)
                                    {
                                        finalCaneType.add(caneType.get(caneTypeinc));
                                    }
                                    caneType.clear();
                                    Map<String,Object> villData=new HashMap<>();
                                    villData.put("SUPVCODE",jsonObject1.getString("SUPVCODE"));
                                    villData.put("SUPVNAME",jsonObject1.getString("SUPVNAME"));
                                    villData.put("villcode",jsonObject1.getString("VCODE"));
                                    villData.put("villname",jsonObject1.getString("VNAME"));
                                    villData.put("villData",finalCaneType);
                                    villData1.add(villData);
                                    caneType.clear();
                                    Map<String,String> dataMap=new HashMap<>();
                                    dataMap.put("ATNAME",jsonObject.getString("ATNAME"));
                                    dataMap.put("PLOTS",jsonObject.getString("PLOTS"));
                                    dataMap.put("GROWCOUNT",jsonObject.getString("GROWCOUNT"));
                                    dataMap.put("AREA",jsonObject.getString("AREA"));
                                    caneType.add(dataMap);
                                }
                            }
                            else
                            {
                                if(jsonObject1.getInt("VCODE") == jsonObject.getInt("VCODE"))
                                {
                                    Map<String,String> dataMap=new HashMap<>();
                                    dataMap.put("ATNAME",jsonObject.getString("ATNAME"));
                                    dataMap.put("PLOTS",jsonObject.getString("PLOTS"));
                                    dataMap.put("GROWCOUNT",jsonObject.getString("GROWCOUNT"));
                                    dataMap.put("AREA",jsonObject.getString("AREA"));
                                    caneType.add(dataMap);
                                }
                                else
                                {
                                    List<Object> finalCaneType=new ArrayList<>();
                                    for(int caneTypeinc=0;caneTypeinc<caneType.size();caneTypeinc++)
                                    {
                                        finalCaneType.add(caneType.get(caneTypeinc));
                                    }
                                    caneType.clear();
                                    Map<String,Object> villData=new HashMap<>();
                                    villData.put("SUPVCODE",jsonObject1.getString("SUPVCODE"));
                                    villData.put("SUPVNAME",jsonObject1.getString("SUPVNAME"));
                                    villData.put("villcode",jsonObject1.getString("VCODE"));
                                    villData.put("villname",jsonObject1.getString("VNAME"));
                                    villData.put("villData",finalCaneType);
                                    villData1.add(villData);
                                    caneType.clear();
                                    Map<String,String> dataMap=new HashMap<>();
                                    dataMap.put("ATNAME",jsonObject.getString("ATNAME"));
                                    dataMap.put("PLOTS",jsonObject.getString("PLOTS"));
                                    dataMap.put("GROWCOUNT",jsonObject.getString("GROWCOUNT"));
                                    dataMap.put("AREA",jsonObject.getString("AREA"));
                                    caneType.add(dataMap);
                                }
                            }

                        }
                        else
                        {
                            Map<String,String> dataMap=new HashMap<>();
                            dataMap.put("ATNAME",jsonObject.getString("ATNAME"));
                            dataMap.put("PLOTS",jsonObject.getString("PLOTS"));
                            dataMap.put("GROWCOUNT",jsonObject.getString("GROWCOUNT"));
                            dataMap.put("AREA",jsonObject.getString("AREA"));
                            caneType.add(dataMap);
                        }


                        if(jsonArray.length()-1==i)
                        {
                            List<Object> finalCaneType=new ArrayList<>();
                            for(int caneTypeinc=0;caneTypeinc<caneType.size();caneTypeinc++)
                            {
                                finalCaneType.add(caneType.get(caneTypeinc));
                            }
                            caneType.clear();
                            Map<String,Object> villData=new HashMap<>();
                            villData.put("SUPVCODE",jsonObject.getString("SUPVCODE"));
                            villData.put("SUPVNAME",jsonObject.getString("SUPVNAME"));
                            villData.put("villcode",jsonObject.getString("VCODE"));
                            villData.put("villname",jsonObject.getString("VNAME"));
                            villData.put("villData",finalCaneType);
                            villData1.add(villData);
                            caneType.clear();
                        }
                    }*/
                    Gson gson=new Gson();
                    List<Object> supData1=new ArrayList<>();
                    try{
                        JSONArray villArray=new JSONArray(message);
                        List<Object> villSupData=new ArrayList<>();

                        for(int i=0;i<villArray.length();i++) {
                            JSONObject jsonObject = villArray.getJSONObject(i);
                            if (i > 0) {
                                JSONObject jsonObject1 = villArray.getJSONObject(i - 1);
                                if (jsonObject1.getInt("SUPVCODE") == jsonObject.getInt("SUPVCODE")) {
                                    Map<String,Object> supData=new HashMap<>();
                                    supData.put("VCODE",jsonObject.getString("VCODE"));
                                    supData.put("VNAME",jsonObject.getString("VNAME"));
                                    supData.put("PLANTPLOTS",jsonObject.getString("PLANTPLOTS"));
                                    supData.put("RATOONPLOTS",jsonObject.getString("RATOONPLOTS"));
                                    supData.put("AUTUMNPLOTS",jsonObject.getString("AUTUMNPLOTS"));
                                    supData.put("PLANTGROWER",jsonObject.getString("PLANTGROWER"));
                                    supData.put("RATOONGROWER",jsonObject.getString("RATOONGROWER"));
                                    supData.put("PLANTAREA",jsonObject.getString("PLANTAREA"));
                                    supData.put("RATOONAREA",jsonObject.getString("RATOONAREA"));
                                    supData.put("AUTUMNAREA",jsonObject.getString("AUTUMNAREA"));
                                    supData.put("TotalArea",jsonObject.getString("TOTALAREA"));
                                    supData.put("TotalPLOTS",jsonObject.getString("TOTALPLOTS"));
                                    supData.put("TotalGROWCOUNT",jsonObject.getString("TOTALGROWCOUNT"));
                                    villSupData.add(supData);
                                }
                                else
                                {
                                    List<Object> finalVillSupData=new ArrayList<>();
                                    for(int villSupDatainc=0;villSupDatainc<villSupData.size();villSupDatainc++)
                                    {
                                        finalVillSupData.add(villSupData.get(villSupDatainc));
                                    }
                                    villSupData.clear();
                                    Map<String,Object> villData=new HashMap<>();
                                    villData.put("SUPVCODE",jsonObject1.getString("SUPVCODE"));
                                    villData.put("SUPVNAME",jsonObject1.getString("SUPVNAME"));
                                    villData.put("villData",finalVillSupData);
                                    supData1.add(villData);

                                    villSupData.clear();
                                    Map<String,Object> supData=new HashMap<>();
                                    supData.put("VCODE",jsonObject.getString("VCODE"));
                                    supData.put("VNAME",jsonObject.getString("VNAME"));
                                    supData.put("PLANTPLOTS",jsonObject.getString("PLANTPLOTS"));
                                    supData.put("RATOONPLOTS",jsonObject.getString("RATOONPLOTS"));
                                    supData.put("AUTUMNPLOTS",jsonObject.getString("AUTUMNPLOTS"));
                                    supData.put("PLANTGROWER",jsonObject.getString("PLANTGROWER"));
                                    supData.put("RATOONGROWER",jsonObject.getString("RATOONGROWER"));
                                    supData.put("PLANTAREA",jsonObject.getString("PLANTAREA"));
                                    supData.put("RATOONAREA",jsonObject.getString("RATOONAREA"));
                                    supData.put("AUTUMNAREA",jsonObject.getString("AUTUMNAREA"));
                                    supData.put("TotalArea",jsonObject.getString("TOTALAREA"));
                                    supData.put("TotalPLOTS",jsonObject.getString("TOTALPLOTS"));
                                    supData.put("TotalGROWCOUNT",jsonObject.getString("TOTALGROWCOUNT"));
                                    villSupData.add(supData);
                                }
                            }
                            else
                            {
                                Map<String,Object> supData=new HashMap<>();
                                supData.put("VCODE",jsonObject.getString("VCODE"));
                                supData.put("VNAME",jsonObject.getString("VNAME"));
                                supData.put("PLANTPLOTS",jsonObject.getString("PLANTPLOTS"));
                                supData.put("RATOONPLOTS",jsonObject.getString("RATOONPLOTS"));
                                supData.put("AUTUMNPLOTS",jsonObject.getString("AUTUMNPLOTS"));
                                supData.put("PLANTGROWER",jsonObject.getString("PLANTGROWER"));
                                supData.put("RATOONGROWER",jsonObject.getString("RATOONGROWER"));
                                supData.put("PLANTAREA",jsonObject.getString("PLANTAREA"));
                                supData.put("RATOONAREA",jsonObject.getString("RATOONAREA"));
                                supData.put("AUTUMNAREA",jsonObject.getString("AUTUMNAREA"));
                                supData.put("TotalArea",jsonObject.getString("TOTALAREA"));
                                supData.put("TotalPLOTS",jsonObject.getString("TOTALPLOTS"));
                                supData.put("TotalGROWCOUNT",jsonObject.getString("TOTALGROWCOUNT"));
                                villSupData.add(supData);
                            }
                            if(villArray.length()-1==i)
                            {
                                List<Object> finalVillSupData=new ArrayList<>();
                                for(int villSupDatainc=0;villSupDatainc<villSupData.size();villSupDatainc++)
                                {
                                    finalVillSupData.add(villSupData.get(villSupDatainc));
                                }
                                villSupData.clear();
                                Map<String,Object> villData=new HashMap<>();
                                villData.put("SUPVCODE",jsonObject.getString("SUPVCODE"));
                                villData.put("SUPVNAME",jsonObject.getString("SUPVNAME"));
                                villData.put("villData",finalVillSupData);
                                supData1.add(villData);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        AlertPopUp("Error: "+e.toString());
                    }
                    System.out.println(gson.toJson(supData1));
                    JSONArray jsonArray1=new JSONArray(gson.toJson(supData1));
                    //AlertPopUp(gson.toJson(supData1));
                    /*DecimalFormat decimalFormat = new DecimalFormat("##.000");
                    CaneTypeSummeryModel totalC = new CaneTypeSummeryModel();
                    totalC.setSupCode(""+dashboardData.size());
                    totalC.setSupName("TOTAL");
                    totalC.setvCode("");
                    totalC.setvName("");
                    totalC.setAtCode("");
                    totalC.setAtName("");
                    totalC.setPlot(""+plot);
                    totalC.setGrower(""+grower);
                    totalC.setArea(""+decimalFormat.format(area));
                    totalC.setColor("#000000");
                    totalC.setTextColor("#FFFFFF");
                    totalC.setColor("#000000");
                    totalC.setTextColor("#FFFFFF");
                    dashboardData.add(totalC);*/
                        /*DecimalFormat intFormat = new DecimalFormat("##");
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        CenterRunningStatusModal total = new CenterRunningStatusModal();
                        total.setName("");
                        total.setCode("Total");
                        total.setOnDate(""+decimalFormat.format(TotalOnDate));
                        total.setTodate(""+decimalFormat.format(TotalToDate));
                        total.setStatus("");
                        dashboardData.add(total);*/



                    recyclerView =findViewById(R.id.recycler_view);
                    //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(jsonArray1.length(), StaggeredGridLayoutManager.HORIZONTAL));
                    //recyclerView.setAdapter(adapter);
                    GridLayoutManager manager = new GridLayoutManager(VillageWiseCaneTypeSummery.this, 1, RecyclerView.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    ListGetVillageWiseCaneTypeSummeryAdapter listGetCropTypeSummeryAdapter =new ListGetVillageWiseCaneTypeSummeryAdapter(VillageWiseCaneTypeSummery.this,jsonArray1);
                    recyclerView.setAdapter(listGetCropTypeSummeryAdapter);
                }
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch (JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().AlertPopUpFinish(VillageWiseCaneTypeSummery.this,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(VillageWiseCaneTypeSummery.this,"Error:"+e.toString());
            }
        }
    }


}