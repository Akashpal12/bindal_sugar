package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GetRemedialSummaryReportAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.GetRemedialSummaryReportModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class GetRemedialSummaryReport extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    List<GetRemedialSummaryReportModel> modelList = new ArrayList<>();
    TextView date1, vilageName1, growerName1, bleachingP1, hexa1, TrichoP1, TrichoL1, emida1, corajen1, ferrous1,supervisorCode,super_visorName;
    String date, villageCode,grocode,vilageName, growerName, bleachingP, hexa, TrichoP, TrichoL, emida, corajen, ferrous;
    String currentDt;
    EditText fromdate_select,todate_selects;
    List<UserDetailsModel> userDetailsModels;
    List<MasterDropDown> targetList;
    LinearLayout main_layout;

    double plotsonDate = 0.0,areaonDate=0.0,plotstodate=0.0,areatodate=0.0;
    TextView plots_onDate,area_onDate,plots_todate,area_todate;
    SessionConfig sessionConfig;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_remedial_summary_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Remedial Summary Report");
        toolbar.setTitle("Remedial Summary Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = GetRemedialSummaryReport.this;
        sessionConfig=new SessionConfig(context);
        super_visorName = findViewById(R.id.super_visorName);
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);
        modelList=new ArrayList<>();


        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        supervisorCode=findViewById(R.id.supervisorCode);
        supervisorCode.setText(userDetailsModels.get(0).getCode());
        super_visorName.setText(userDetailsModels.get(0).getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDt = dateFormat.format(today);
        main_layout=findViewById(R.id.main_layout);

        plots_onDate=findViewById(R.id.plots_onDate);
        area_onDate=findViewById(R.id.area_onDate);
        plots_todate=findViewById(R.id.plots_todate);
        area_todate=findViewById(R.id.area_todate);




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
                DatePickerDialog dpd = new DatePickerDialog(GetRemedialSummaryReport.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(GetRemedialSummaryReport.this, new DatePickerDialog.OnDateSetListener() {
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
                    new PlotSummaryReport().execute(fromdate_select.getText().toString(),todate_selects.getText().toString());
                    //  new PlotSummaryReport().execute();
                }
                catch(Exception e)
                {

                }
            }
        });


        try {
            //new PlotSummaryReport().execute(currentDt,currentDt);
            //new PlotSummaryReport().execute();

        }catch (Exception e){
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }



    class PlotSummaryReport extends AsyncTask<String, Void, String> {

        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetRemedialSummary";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("Sup", userDetailsModels.get(0).getCode()));// 22001 // 2
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));// 22001 // 2
                entity.add(new BasicNameValuePair("Seas",""+sessionConfig.getSeason()));// 22001 // 2
                entity.add(new BasicNameValuePair("Fdate",params[0]));//2022-05-06
                entity.add(new BasicNameValuePair("Tdate",params[1]));//2023-05-06

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
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
            try {
                if(modelList.size()>0)
                    modelList.clear();

                if (dialog.isShowing())
                    dialog.dismiss();

                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        GetRemedialSummaryReportModel model=new GetRemedialSummaryReportModel();
                        model.setSNO(object.getString("SNO"));//DATE
                        model.setSUPCODE(object.getString("SUPCODE"));//DATE
                        model.setSUPNAME(object.getString("SUPNAME"));//VILLCODE
                        model.setVILLNAME(object.getString("VILLNAME"));//VILLNAME
                        model.setPLOTVILL(object.getString("PLOTVILL"));//VILLNAME
                        model.setPLOTNO(object.getString("PLOTNO"));//GROCODE
                        model.setDISEASE(object.getString("DISEASE"));//GRONAME
                        model.setF_DATE(fromdate_select.getText().toString());//GRONAME
                        model.setT_DATE(todate_selects.getText().toString());//GRONAME
                        modelList.add(model);

                    }

                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    GetRemedialSummaryReportAdapter adapter = new GetRemedialSummaryReportAdapter(context, modelList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    main_layout.setVisibility(View.VISIBLE);



                }
                else {
                    main_layout.setVisibility(View.GONE);

                    new AlertDialogManager().RedDialog(context, "" +new JSONObject(Content).getString("MSG").toString());


                }

            } catch (JSONException e) {
                System.out.println("Error : " + Content);
                Log.e(e.getClass().getName(), e.getMessage(), e);
                try {
                    new AlertDialogManager().RedDialog(context,new JSONObject(Content).getString("MSG"));
                } catch (Exception e1) {
                    e1.getMessage();
                }
                main_layout.setVisibility(View.GONE);
                RecyclerView recyclerView = findViewById(R.id.recycler_list);
                GetRemedialSummaryReportAdapter adapter = new GetRemedialSummaryReportAdapter(context, modelList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            } catch(Exception e){
                main_layout.setVisibility(View.GONE);
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context, "Error :- " + e.getClass().getName() + " - " + e.getMessage());
            }
        }




    }




}
