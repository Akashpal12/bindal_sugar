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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.RaSupervisorAreaReportListAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class RaSupervisorWiseAreaReport extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    Spinner target_dd;
    EditText fromdate_select,todate_selects;
    Spinner supervisor;
    String currentDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_supervisor_wise_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaSupervisorWiseAreaReport.this;
        setTitle(getString(R.string.MENU_SUPERVISOR_SUMMARY));
        toolbar.setTitle(getString(R.string.MENU_SUPERVISOR_SUMMARY));
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


        //--------------------------------calender----------------------------------------------
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDate = dateFormat.format(today);
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);

        fromdate_select.setText(currentDate);
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
                DatePickerDialog dpd = new DatePickerDialog(RaSupervisorWiseAreaReport.this, new DatePickerDialog.OnDateSetListener() {
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
                        // new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString());
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        //---------------------TODATE-----------------------------------------
        todate_selects.setText(currentDate);
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
                DatePickerDialog dpd = new DatePickerDialog(RaSupervisorWiseAreaReport.this, new DatePickerDialog.OnDateSetListener() {
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
                        // new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString());
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
                    new getReportDetails().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
                }
                catch(Exception e)
                {

                }
            }
        });

        //-------------------------------------------------------------------------------------

        getSpinner();
        new getReportDetails().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),currentDate,currentDate);



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
                   // new getReportDetails().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
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

    private class getReportDetails extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SupervoiserWiseSummary";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("UserCode",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("UTCODE",userDetailsModels.get(0).getUserTypeCode()));
                entity.add(new BasicNameValuePair("TRGTYPE",params[0]));
                entity.add(new BasicNameValuePair("Fdate",params[1]));
                entity.add(new BasicNameValuePair("Tdate",params[2]));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);


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
            staffTargetModelList=new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(message);

                if(jsonArray.length()==0)
                {
                    new AlertDialogManager().AlertPopUp(context,"No data found");
                }
                else
                {
                    JSONObject jsonObje=jsonArray.getJSONObject(0);
                    StaffTargetModel header=new StaffTargetModel();
                    header.setVillageCode("Supervisor");
                    header.setTargetArea("Target");
                    header.setOnManualArea("On Date Manual");
                    header.setToManualArea("To Date Manual");
                    header.setOnGpsArea("On Date GPS");
                    header.setToGpsArea("To Date GPS");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    staffTargetModelList.add(header);
                    double target=0.0,onManual=0.0,onGps=0.0,toManual=0.0,toGps=0.0;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        StaffTargetModel growerModel=new StaffTargetModel();
                        growerModel.setVillageCode(jsonObject1.getString("Name"));
                        //growerModel.setVillageName(jsonObject1.getString("ReportName"));
                        growerModel.setTargetArea(new DecimalFormat("0.00").format(jsonObject1.getDouble("TargetArea")));
                        growerModel.setOnManualArea(new DecimalFormat("0.000").format(jsonObject1.getDouble("ONMenualArea")));
                        growerModel.setToManualArea(new DecimalFormat("0.000").format(jsonObject1.getDouble("TOMenualArea")));
                        growerModel.setOnGpsArea(new DecimalFormat("0.000").format(jsonObject1.getDouble("ONGPSArea")));
                        growerModel.setToGpsArea(new DecimalFormat("0.000").format(jsonObject1.getDouble("TOGPSArea")));
                        target +=jsonObject1.getDouble("TargetArea");
                        onManual +=jsonObject1.getDouble("ONMenualArea");
                        toManual +=jsonObject1.getDouble("TOMenualArea");
                        onGps +=jsonObject1.getDouble("ONGPSArea");
                        toGps +=jsonObject1.getDouble("TOGPSArea");
                        if(i%2==0)
                        {
                            growerModel.setColor("#DFDFDF");
                            growerModel.setTextColor("#000000");
                        }
                        else
                        {
                            growerModel.setColor("#FFFFFF");
                            growerModel.setTextColor("#000000");
                        }
                        staffTargetModelList.add(growerModel);
                    }
                    StaffTargetModel footer=new StaffTargetModel();
                    footer.setVillageCode("Total");
                    footer.setTargetArea(new DecimalFormat("0.00").format(target));
                    footer.setOnManualArea(new DecimalFormat("0.000").format(onManual));
                    footer.setToManualArea(new DecimalFormat("0.000").format(toManual));
                    footer.setOnGpsArea(new DecimalFormat("0.000").format(onGps));
                    footer.setToGpsArea(new DecimalFormat("0.000").format(toGps));
                    footer.setColor("#000000");
                    footer.setTextColor("#FFFFFF");
                    staffTargetModelList.add(footer);
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                RaSupervisorAreaReportListAdapter stockSummeryAdapter =new RaSupervisorAreaReportListAdapter(context,staffTargetModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context,message);
                if(dialog.isShowing())
                    dialog.dismiss();
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                RaSupervisorAreaReportListAdapter stockSummeryAdapter =new RaSupervisorAreaReportListAdapter(context,staffTargetModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}
