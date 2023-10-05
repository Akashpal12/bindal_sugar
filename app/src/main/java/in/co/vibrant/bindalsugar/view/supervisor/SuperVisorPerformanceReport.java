package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;

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
import in.co.vibrant.bindalsugar.adapter.SupervisePerformanceReportAdapetr;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.SupervisorPerformanceModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class SuperVisorPerformanceReport extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    EditText edt_SupCode;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    SupervisePerformanceReportAdapetr plotOverLoadingAdapter;
    double total_plots,totalAreaValue,totalPlotsCovered ,totalAreaCoverage;
    List<SupervisorPerformanceModel> supervisorPerformanceModelList;
    List<SupervisorPerformanceModel> filteredArrayList;
    private boolean isFiltering = false;
    EditText seach_editText;
    String currentDt;
    EditText fromdate_select,todate_selects;
    TextView tottal_plots,total_area,plots_covered,area_coverage;
    LinearLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_visor_performance_reports);
        Toolbar toolbar = findViewById(R.id.toolbar);
        edt_SupCode = findViewById(R.id.edt_SupCode);
        setSupportActionBar(toolbar);
        context = SuperVisorPerformanceReport.this;
        setTitle(""+getString(R.string.MENU_SUPERVISER_PERFORMANCE_REPORT));
        toolbar.setTitle(""+getString(R.string.MENU_SUPERVISER_PERFORMANCE_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        targetList = new ArrayList<>();
        dbh = new DBHelper(context);
        staffTargetModelList = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        seach_editText=findViewById(R.id.seach_editText);
        tottal_plots=findViewById(R.id.tottal_plots);
        total_area=findViewById(R.id.total_area);
        plots_covered=findViewById(R.id.plots_covered);
        area_coverage=findViewById(R.id.area_coverage);
        main_layout=findViewById(R.id.main_layout);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDt = dateFormat.format(today);

        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);


        supervisorPerformanceModelList = new ArrayList<>();
        plotOverLoadingAdapter = new SupervisePerformanceReportAdapetr(context, supervisorPerformanceModelList);



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
                DatePickerDialog dpd = new DatePickerDialog(SuperVisorPerformanceReport.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(SuperVisorPerformanceReport.this, new DatePickerDialog.OnDateSetListener() {
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





      // new SupwisePerformanceReport().execute(fromdate_select.getText().toString(),todate_selects.getText().toString());


        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (fromdate_select.getText().toString().length() == 0) {
                        new AlertDialogManager().showToast((Activity) context,"Please Select A Valid Date");

                    } else if (todate_selects.getText().toString().length() == 0) {
                        new AlertDialogManager().showToast((Activity) context,"Please Select A Valid Date");


                    } else {
                        new SupwisePerformanceReport().execute(fromdate_select.getText().toString(),todate_selects.getText().toString());

                    }

                } catch (Exception e) {

                }
            }
        });

        seach_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


       /* seach_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the RecyclerView based on the user's input
                plotOverLoadingAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });*/




    }

    private void filter(String text) {
        ArrayList<SupervisorPerformanceModel> filteredList = new ArrayList<>();

        for (SupervisorPerformanceModel item : supervisorPerformanceModelList) {
            if (item.getVILLCODE().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        plotOverLoadingAdapter.filterList(filteredList);
    }




    private class SupwisePerformanceReport extends AsyncTask<String, Integer, Void> {
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/supwisePerformanceReport_New";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("sup", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("seas", "2023-2024"));
                entity.add(new BasicNameValuePair("Fdate",params[0]));//2022-05-06
                // entity.add(new BasicNameValuePair("Fdate","2023-07-20"));//2022-05-06
                //entity.add(new BasicNameValuePair("Tdate","2023-07-28"));//2023-05-06
                entity.add(new BasicNameValuePair("Tdate",params[1]));//2023-05-06
                HttpPost httpPost = new HttpPost(url);
                Log.d("","Do In BackGround  "+entity);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);


            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (supervisorPerformanceModelList.size() > 0)
                    supervisorPerformanceModelList.clear();

                if (dialog.isShowing())
                    dialog.dismiss();

                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        SupervisorPerformanceModel supervisorPerformanceModel = new SupervisorPerformanceModel();

                        supervisorPerformanceModel.setSUPCODE(jsonObject1.getInt("SUPCODE"));
                        supervisorPerformanceModel.setSUPNAME(jsonObject1.getString("SUPNAME"));
                        supervisorPerformanceModel.setVILLCODE(jsonObject1.getString("VILLCODE"));
                        supervisorPerformanceModel.setVILLNAME(jsonObject1.getString("VILLNAME"));
                        supervisorPerformanceModel.setPLOTS(jsonObject1.getString("PLOTS"));
                        supervisorPerformanceModel.setAREA(jsonObject1.getString("AREA"));
                        supervisorPerformanceModel.setA_PLOTS(jsonObject1.getString("A_PLOTS"));
                        supervisorPerformanceModel.setA_AREA(jsonObject1.getString("A_AREA"));
                        supervisorPerformanceModel.setAP_PRC(jsonObject1.getString("AP_PRC"));
                        supervisorPerformanceModel.setAA_PRC(jsonObject1.getString("AA_PRC"));

                        double total_plots_value = Double.parseDouble(jsonObject1.getString("PLOTS"));
                        total_plots += total_plots_value;

                        double total_area_value = Double.parseDouble(jsonObject1.getString("AREA"));
                        totalAreaValue += total_area_value;

                        double total_plots_Covered= Double.parseDouble(jsonObject1.getString("A_PLOTS"));
                        totalPlotsCovered += total_plots_Covered;
                        double total_area_covered = Double.parseDouble(jsonObject1.getString("A_AREA"));
                        totalAreaCoverage += total_area_covered;



                        supervisorPerformanceModelList.add(supervisorPerformanceModel);



                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(plotOverLoadingAdapter);

                    tottal_plots.setText(new DecimalFormat("#0.00").format(total_plots));
                    total_area.setText(new DecimalFormat("#0.00").format(totalAreaValue));
                    plots_covered.setText(new DecimalFormat("#0.00").format(totalPlotsCovered));
                    area_coverage.setText(new DecimalFormat("#0.00").format(totalAreaCoverage));
                    main_layout.setVisibility(View.VISIBLE);
                    //total_area.setText(totalAreaValue+"");
                   // plots_covered.setText(totalPlotsCovered+"");
                    //area_coverage.setText(totalAreaCoverage+"");

                } else {
                    main_layout.setVisibility(View.GONE);
                    new AlertDialogManager().RedDialog(context, "" + new JSONObject(message).getString("MSG").toString());


                }

            } catch (JSONException e) {

                System.out.println("Error : " + message);
                main_layout.setVisibility(View.GONE);
                Log.e(e.getClass().getName(), e.getMessage(), e);
                try {
                    new AlertDialogManager().RedDialog(context, new JSONObject(message).getString("MSG"));
                } catch (Exception e1) {
                    e1.getMessage();
                }
                RecyclerView recyclerView = findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                SupervisePerformanceReportAdapetr plotOverLoadingAdapter = new SupervisePerformanceReportAdapetr(context, supervisorPerformanceModelList);
                recyclerView.setAdapter(plotOverLoadingAdapter);
                main_layout.setVisibility(View.GONE);
            } catch (Exception e) {
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





