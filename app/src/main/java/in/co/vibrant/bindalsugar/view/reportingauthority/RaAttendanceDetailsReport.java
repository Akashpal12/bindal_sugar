package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.RaAttendanceDetailsReportListAdapter;
import in.co.vibrant.bindalsugar.model.DashboardAttendanceModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class RaAttendanceDetailsReport extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    EditText attendance_date;

    String user,userCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_report_staff_attendance_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaAttendanceDetailsReport.this;
        userCode=getIntent().getExtras().getString("userCode");
        user=getIntent().getExtras().getString("user");
        setTitle(user+" Attendance Report");
        toolbar.setTitle(user+" Attendance Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        attendance_date=findViewById(R.id.attendance_date);
        dbh=new DBHelper(context);
        staffTargetModelList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);
        attendance_date.setText(currDate);
        attendance_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        try {
                            String temDate = "" + dayOfMonth;
                            if (temDate.length() == 1) {
                                temDate = "0" + temDate;
                            }
                            String temmonth = "" + (monthOfYear + 1);
                            if (temmonth.length() == 1) {
                                temmonth = "0" + temmonth;
                            }
                            SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date today = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(temDate + "-" + temmonth + "-" + year);
                            String currDate = dateFormatOut.format(today);
                            attendance_date.setText(currDate);
                            new AttendanceReport().execute(attendance_date.getText().toString());
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        new AttendanceReport().execute(attendance_date.getText().toString());
    }

    private class AttendanceReport extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*/
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            //dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/ATTENDANCEUSERWISE";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("imei",getDeviceImei.GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("UTCODE",userCode));
                entity.add(new BasicNameValuePair("date",params[0]));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
                Log.e("SecurityException",Content);

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                //AlertPopUp("Error:"+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                //AlertPopUp("Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                dialog.dismiss();//RaAttendanceDetailsReportListAdapter
                List<DashboardAttendanceModel> dashboardAttendanceModelList=new ArrayList<>();
                dialog.dismiss();
                JSONArray jsonArray=new JSONArray(Content);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    DashboardAttendanceModel dashboardAttendanceModel=new DashboardAttendanceModel();
                    dashboardAttendanceModel.setUserCode(jsonObject.getString("CODE"));
                    dashboardAttendanceModel.setName(jsonObject.getString("NAME"));
                    dashboardAttendanceModel.setStatus(jsonObject.getString("APPR"));
                    dashboardAttendanceModel.setInTime(jsonObject.getString("INATTENDANCE"));
                    dashboardAttendanceModel.setOutTime(jsonObject.getString("OUTATTENDANCE"));
                    dashboardAttendanceModel.setInAddress(jsonObject.getString("INADDRESS"));
                    dashboardAttendanceModel.setOutAddress(jsonObject.getString("OUTADDRESS"));
                    dashboardAttendanceModel.setInImage(jsonObject.getString("INIMAGE"));
                    dashboardAttendanceModel.setOutImage(jsonObject.getString("OUTIMAGE"));
                    dashboardAttendanceModelList.add(dashboardAttendanceModel);
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                RaAttendanceDetailsReportListAdapter stockSummeryAdapter =new RaAttendanceDetailsReportListAdapter(context,dashboardAttendanceModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
            }
            catch (Exception e)
            {
                LinearLayout line1_1=findViewById(R.id.line1_1);
                line1_1.setVisibility(View.GONE);
                //AlertPopUp("Error:"+e.toString());
            }
        }
    }

}
