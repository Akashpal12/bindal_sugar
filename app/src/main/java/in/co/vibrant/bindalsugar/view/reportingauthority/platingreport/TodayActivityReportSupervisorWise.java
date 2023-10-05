package in.co.vibrant.bindalsugar.view.reportingauthority.platingreport;

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
import android.widget.TextView;

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
import in.co.vibrant.bindalsugar.adapter.RaSupervisorWithVarietyAdapter;
import in.co.vibrant.bindalsugar.adapter.TodayActivityReportSupervisorWiseAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.ReportMultiple;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class TodayActivityReportSupervisorWise extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    Spinner planting_type;
    EditText planting_date;
    List<MasterDropDown> typeOfPlantingList;
    String currentDate;
    EditText fromdate_select,todate_selects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_village_wise_variety);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= TodayActivityReportSupervisorWise.this;
        setTitle("Today Activity Report");
        toolbar.setTitle("Today Activity Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        typeOfPlantingList=new ArrayList<>();
        planting_type=findViewById(R.id.planting_type);


        dbh=new DBHelper(context);

        List<PlantingModel> plantingModelList=dbh.getPlantingModel("No","","","","");
        if(plantingModelList.size()>0)
        {
            TextView heading=findViewById(R.id.heading);
            heading.setText("Warning:- Your "+plantingModelList.size()+" planting data are pending to transfer for server\nचेतावनी:- आपका "+plantingModelList.size ()+" प्लांटिंग डाटा सर्वर पर नहीं गया है इसलिए आपका प्लांटिंग रिपोर्ट इन्कम्प्लीट हो सकता है।");
            new AlertDialogManager().RedDialog(context,"Warning:- Your "+plantingModelList.size()+" planting data are pending to transfer for server\nचेतावनी:- आपका "+plantingModelList.size ()+" प्लांटिंग डाटा सर्वर पर नहीं गया है इसलिए आपका प्लांटिंग रिपोर्ट इन्कम्प्लीट हो सकता है।");
            heading.setVisibility(View.GONE);
        }
        
        staffTargetModelList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        typeOfPlantingList = dbh.getMasterDropDown("13");
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);



        //--------------------------Date Calender-----------------------------------------
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
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
                DatePickerDialog dpd = new DatePickerDialog(TodayActivityReportSupervisorWise.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(TodayActivityReportSupervisorWise.this, new DatePickerDialog.OnDateSetListener() {
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
                    new ONDATEAREAList().execute(typeOfPlantingList.get(planting_type.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
                }
                catch(Exception e)
                {

                }
            }
        });

        //--------------------------------------------------------------------------------------




        getSpinner();


    }


    public void getSpinner()
    {

        ArrayList<String> data=new ArrayList<String>();
        for(int i=0;i<typeOfPlantingList.size();i++)
        {
            data.add(typeOfPlantingList.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        planting_type.setAdapter(adaptersupply);
        planting_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(new InternetCheck(context).isOnline())
                {
                   // new ONDATEAREAList().execute(typeOfPlantingList.get(planting_type.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
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

    class ONDATEAREAList extends AsyncTask<String, Void, String> {

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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GETACTIVITYSUPERVOISERWISEDAYTODAY";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("USERCODE",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("PLANTTYPE",params[0]));
                entity.add(new BasicNameValuePair("Fdate",params[1]));
                entity.add(new BasicNameValuePair("Tdate",params[2]));
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
            List<ReportMultiple> ONDATEAREAFinal=new ArrayList<>();
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONArray jsonArray = new JSONArray(Content);
                String currentVillageCode="",currentVillageName="";
                Gson gson=new Gson();

                if(jsonArray.length()>0)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    currentVillageCode = jsonObject1.getString("ATCODE");
                    currentVillageName = jsonObject1.getString("ATNAME");
                    List<Map<String,String>> ONDATEAREA=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(currentVillageCode.equalsIgnoreCase(jsonObject.getString("ATCODE")))
                        {
                            Map<String,String> map =new HashMap<String, String>();
                            map.put("ATCODE",jsonObject.getString("ATCODE"));
                            map.put("ATNAME",jsonObject.getString("ATNAME"));
                            map.put("UCODE",jsonObject.getString("UCODE"));
                            map.put("UNAME",jsonObject.getString("UNAME"));
                            map.put("ONDATEPLOT",jsonObject.getString("ONDATEPLOT"));
                            map.put("TODATEPLOT",jsonObject.getString("TODATEPLOT"));
                            map.put("ONDATEAREA",jsonObject.getString("ONDATEAREA"));
                            map.put("TODATEAREA",jsonObject.getString("TODATEAREA"));
                            ONDATEAREA.add(map);
                        }
                        else
                        {
                            if(ONDATEAREA.size()>0)
                            {
                                List<Map<String,String>> ONDATEAREA1=new ArrayList<>();
                                for(int j=0;j<ONDATEAREA.size();j++)
                                {
                                    Map<String,String> map1 =new HashMap<String, String>();
                                    map1.put("ATCODE",ONDATEAREA.get(j).get("ATCODE"));
                                    map1.put("ATNAME",ONDATEAREA.get(j).get("ATNAME"));
                                    map1.put("UCODE",ONDATEAREA.get(j).get("UCODE"));
                                    map1.put("UNAME",ONDATEAREA.get(j).get("UNAME"));
                                    map1.put("ONDATEPLOT",ONDATEAREA.get(j).get("ONDATEPLOT"));
                                    map1.put("TODATEPLOT",ONDATEAREA.get(j).get("TODATEPLOT"));
                                    map1.put("ONDATEAREA",ONDATEAREA.get(j).get("ONDATEAREA"));
                                    map1.put("TODATEAREA",ONDATEAREA.get(j).get("TODATEAREA"));
                                    ONDATEAREA1.add(map1);
                                }
                                ReportMultiple reportMultiple=new ReportMultiple();
                                reportMultiple.setName(currentVillageCode+" / "+currentVillageName);
                                JSONArray array=new JSONArray(gson.toJson(ONDATEAREA1));
                                reportMultiple.setJsonArray(array);
                                ONDATEAREAFinal.add(reportMultiple);
                                currentVillageCode = jsonObject.getString("ATCODE");
                                currentVillageName = jsonObject.getString("ATNAME");
                                ONDATEAREA.clear();
                            }
                            Map<String,String> map =new HashMap<String, String>();
                            map.put("ATCODE",jsonObject.getString("ATCODE"));
                            map.put("ATNAME",jsonObject.getString("ATNAME"));
                            map.put("UCODE",jsonObject.getString("UCODE"));
                            map.put("UNAME",jsonObject.getString("UNAME"));
                            map.put("ONDATEPLOT",jsonObject.getString("ONDATEPLOT"));
                            map.put("TODATEPLOT",jsonObject.getString("TODATEPLOT"));
                            map.put("ONDATEAREA",jsonObject.getString("ONDATEAREA"));
                            map.put("TODATEAREA",jsonObject.getString("TODATEAREA"));
                            ONDATEAREA.add(map);
                        }
                    }
                    List<Map<String,String>> ONDATEAREA1=new ArrayList<>();
                    for(int j=0;j<ONDATEAREA.size();j++)
                    {
                        Map<String,String> map1 =new HashMap<String, String>();
                        map1.put("ATCODE",ONDATEAREA.get(j).get("ATCODE"));
                        map1.put("ATNAME",ONDATEAREA.get(j).get("ATNAME"));
                        map1.put("UCODE",ONDATEAREA.get(j).get("UCODE"));
                        map1.put("UNAME",ONDATEAREA.get(j).get("UNAME"));
                        map1.put("ONDATEPLOT",ONDATEAREA.get(j).get("ONDATEPLOT"));
                        map1.put("TODATEPLOT",ONDATEAREA.get(j).get("TODATEPLOT"));
                        map1.put("ONDATEAREA",ONDATEAREA.get(j).get("ONDATEAREA"));
                        map1.put("TODATEAREA",ONDATEAREA.get(j).get("TODATEAREA"));
                        ONDATEAREA1.add(map1);
                    }
                    ReportMultiple reportMultiple=new ReportMultiple();
                    reportMultiple.setName(currentVillageCode+" / "+currentVillageName);
                    JSONArray array=new JSONArray(gson.toJson(ONDATEAREA1));
                    reportMultiple.setJsonArray(array);
                    ONDATEAREAFinal.add(reportMultiple);
                }
                //String ONDATEAREAFinalONDATEAREAFinal=gson.toJson(ONDATEAREAFinal);
                /*ReportMultiple reportMultiple1=new ReportMultiple();
                reportMultiple1.setName(currentZoneName);
                ONDATEAREAFinal.add(reportMultiple1);*/



                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfONDATEAREAAdapter stockSummeryAdapter =new RaSelfONDATEAREAAdapter(context,staffONDATEAREAModalList);
                TodayActivityReportSupervisorWiseAdapter adapter =new TodayActivityReportSupervisorWiseAdapter(context,ONDATEAREAFinal);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                System.out.println("Error : " + Content);
                Log.e(e.getClass().getName(), e.getMessage(), e);
                //new AlertDialogManager().RedDialog(context,"Error :- "+e.getClass().getName()+" - "+ e.getMessage());
                new AlertDialogManager().RedDialog(context,Content);
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfONDATEAREAAdapter stockSummeryAdapter =new RaSelfONDATEAREAAdapter(context,staffONDATEAREAModalList);
                RaSupervisorWithVarietyAdapter adapter =new RaSupervisorWithVarietyAdapter(context,ONDATEAREAFinal);
                recyclerView.setAdapter(adapter);
            }catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,"Error :- "+e.getClass().getName()+" - "+ e.getMessage());
            }
        }
    }


}
