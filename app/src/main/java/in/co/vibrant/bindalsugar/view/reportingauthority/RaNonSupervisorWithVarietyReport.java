package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.RaNonSupervisorWithVarietyAdapter;
import in.co.vibrant.bindalsugar.adapter.RaSupervisorWithVarietyAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.ReportMultiple;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class RaNonSupervisorWithVarietyReport extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    Spinner target_dd;
    EditText fromdate_select,todate_selects;
    String currentDt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_zone_wise_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaNonSupervisorWithVarietyReport.this;
        setTitle(getString(R.string.MENU_SUPERVISOR_WITH_VARIETY_REPORT));
        toolbar.setTitle(getString(R.string.MENU_SUPERVISOR_WITH_VARIETY_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        targetList=new ArrayList<>();
        target_dd=findViewById(R.id.target_dd);
        dbh=new DBHelper(context);
        staffTargetModelList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();

        getSpinner();
//--------------------------Date Calender-----------------------------------------
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDt= dateFormat.format(today);
        fromdate_select.setText(currentDt);
        fromdate_select.setInputType(InputType.TYPE_NULL);
        fromdate_select.setTextIsSelectable(true);
        fromdate_select.setFocusable(false);
        fromdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaNonSupervisorWithVarietyReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate = "" + dayOfMonth;
                        if (temDate.length() == 1) {
                            temDate = "0" + temDate;
                        }
                        String temmonth = "" + (monthOfYear + 1);
                        if (temmonth.length() == 1) {
                            temmonth = "0" + temmonth;
                        }
                        fromdate_select.setText(year + "-" + temmonth + "-" + temDate);

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //-----------------------------------------Todate----------------------------------
        todate_selects.setText(currentDt);
        todate_selects.setInputType(InputType.TYPE_NULL);
        todate_selects.setTextIsSelectable(true);
        todate_selects.setFocusable(false);
        todate_selects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaNonSupervisorWithVarietyReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate = "" + dayOfMonth;
                        if (temDate.length() == 1) {
                            temDate = "0" + temDate;
                        }
                        String temmonth = "" + (monthOfYear + 1);
                        if (temmonth.length() == 1) {
                            temmonth = "0" + temmonth;
                        }
                        todate_selects.setText(year + "-" + temmonth + "-" + temDate);

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });



        Button btn_ok=findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new TargetList().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());

                }
                catch(Exception e)
                {

                }
            }
        });

        //--------------------------------------------------------------------------------------

        new TargetList().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),currentDt,currentDt);


    }


    public void getSpinner()
    {
        targetList=dbh.getMasterDropDown("22");
        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<targetList.size();i++)
        {
            data.add(targetList.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        target_dd.setAdapter(adaptersupply);
        target_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(new InternetCheck(context).isOnline())
                {
                    //new TargetList().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,"No internet found");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class TargetList extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details",true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SupervoiserVarietyWiseSummary1";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("UserCode",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("TRGTYPE",params[0]));
                entity.add(new BasicNameValuePair("Fdate",params[1]));
                entity.add(new BasicNameValuePair("Tdate",params[2]));
                entity.add(new BasicNameValuePair("UTCODE",""));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + Content);
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            List<ReportMultiple> targetFinal=new ArrayList<>();
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONArray jsonArray = new JSONArray(Content);
                String currentZoneName="";
                Gson gson=new Gson();

                if(jsonArray.length()>0)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    currentZoneName = jsonObject1.getString("Name");
                    List<Map<String,String>> target=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(currentZoneName.equalsIgnoreCase(jsonObject.getString("Name")))
                        {
                            Map<String,String> map =new HashMap<String, String>();
                            map.put("VrCode",jsonObject.getString("VrCode"));
                            map.put("VrName",jsonObject.getString("VrName"));
                            map.put("ONMenualArea",jsonObject.getString("ONMenualArea"));
                            map.put("TOMenualArea",jsonObject.getString("TOMenualArea"));
                            map.put("ONGPSArea",jsonObject.getString("ONGPSArea"));
                            map.put("TOGPSArea",jsonObject.getString("TOGPSArea"));
                            map.put("Name",jsonObject.getString("Name"));
                            map.put("Code",jsonObject.getString("Code"));
                            target.add(map);
                        }
                        else
                        {
                            if(target.size()>0)
                            {
                                List<Map<String,String>> target1=new ArrayList<>();
                                for(int j=0;j<target.size();j++)
                                {
                                    Map<String,String> map1 =new HashMap<String, String>();
                                    map1.put("VrCode",target.get(j).get("VrCode"));
                                    map1.put("VrName",target.get(j).get("VrName"));
                                    map1.put("ONMenualArea",target.get(j).get("ONMenualArea"));
                                    map1.put("TOMenualArea",target.get(j).get("TOMenualArea"));
                                    map1.put("ONGPSArea",target.get(j).get("ONGPSArea"));
                                    map1.put("TOGPSArea",target.get(j).get("TOGPSArea"));
                                    map1.put("Name",target.get(j).get("Name"));
                                    map1.put("Code",target.get(j).get("Code"));
                                    target1.add(map1);
                                }
                                ReportMultiple reportMultiple=new ReportMultiple();
                                reportMultiple.setName(currentZoneName);
                                JSONArray array=new JSONArray(gson.toJson(target1));
                                reportMultiple.setJsonArray(array);
                                targetFinal.add(reportMultiple);
                                currentZoneName = jsonObject.getString("Name");
                                target.clear();
                            }
                            Map<String,String> map =new HashMap<String, String>();
                            map.put("VrCode",jsonObject.getString("VrCode"));
                            map.put("VrName",jsonObject.getString("VrName"));
                            map.put("ONMenualArea",jsonObject.getString("ONMenualArea"));
                            map.put("TOMenualArea",jsonObject.getString("TOMenualArea"));
                            map.put("ONGPSArea",jsonObject.getString("ONGPSArea"));
                            map.put("TOGPSArea",jsonObject.getString("TOGPSArea"));
                            map.put("Name",jsonObject.getString("Name"));
                            map.put("Code",jsonObject.getString("Code"));
                            target.add(map);
                        }
                    }
                    List<Map<String,String>> target1=new ArrayList<>();
                    for(int j=0;j<target.size();j++)
                    {
                        Map<String,String> map1 =new HashMap<String, String>();
                        map1.put("VrCode",target.get(j).get("VrCode"));
                        map1.put("VrName",target.get(j).get("VrName"));
                        map1.put("ONMenualArea",target.get(j).get("ONMenualArea"));
                        map1.put("TOMenualArea",target.get(j).get("TOMenualArea"));
                        map1.put("ONGPSArea",target.get(j).get("ONGPSArea"));
                        map1.put("TOGPSArea",target.get(j).get("TOGPSArea"));
                        map1.put("Name",target.get(j).get("Name"));
                        map1.put("Code",target.get(j).get("Code"));
                        target1.add(map1);
                    }
                    ReportMultiple reportMultiple=new ReportMultiple();
                    reportMultiple.setName(currentZoneName);
                    JSONArray array=new JSONArray(gson.toJson(target1));
                    reportMultiple.setJsonArray(array);
                    targetFinal.add(reportMultiple);
                }
                /*ReportMultiple reportMultiple1=new ReportMultiple();
                reportMultiple1.setName(currentZoneName);
                targetFinal.add(reportMultiple1);*/



                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                RaNonSupervisorWithVarietyAdapter stockSummeryAdapter =new RaNonSupervisorWithVarietyAdapter(context,targetFinal);
                recyclerView.setAdapter(stockSummeryAdapter);

            } catch (JSONException e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,Content);
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                RaSupervisorWithVarietyAdapter stockSummeryAdapter =new RaSupervisorWithVarietyAdapter(context,targetFinal);
                recyclerView.setAdapter(stockSummeryAdapter);
            }catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,"Error :- "+e.getClass().getName()+" - "+ e.getMessage());
            }
        }
    }


}
