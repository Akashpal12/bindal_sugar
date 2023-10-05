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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.StaffSeedIndentAdapter;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ReportSupervisorIndentDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    EditText fromdate_select,todate_selects;
    List<UserDetailsModel> userDetailsModels;
    List<IndentModel> indentModelList;
    String currentDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_seed_indent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= ReportSupervisorIndentDetails.this;
        setTitle(getString(R.string.MENU_INDENT_REPORT));
        toolbar. setTitle(getString(R.string.MENU_INDENT_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();

        //--------------------------Date Calender-----------------------------------------
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
                DatePickerDialog dpd = new DatePickerDialog(ReportSupervisorIndentDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(ReportSupervisorIndentDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                    new GetData().execute(fromdate_select.getText().toString(),todate_selects.getText().toString());
                }
                catch(Exception e)
                {

                }
            }
        });

        //new GetData().execute(currentDt,currentDt);

        //--------------------------------------------------------------------------------------

    }

    class GetData extends AsyncTask<String, Void, String> {

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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SUPPERVOISERINDENTINGDETAILS";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("TargetType","1"));
                entity.add(new BasicNameValuePair("Fdate",params[0]));
                entity.add(new BasicNameValuePair("Tdate",params[1]));
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
            JSONArray jsonArray1=new JSONArray();
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                indentModelList=new ArrayList<>();
                JSONArray jsonArray = new JSONArray(Content);

                IndentModel header=new IndentModel();
                header.setVillage("VCODE");
                header.setVillageName("VNAME");
                header.setGrower("GCODE");
                header.setGrowerName("GNAME");
                header.setGrowerFather("GFATHER");
                header.setVARIETY("VRCODE");
                header.setVARIETYNAME("VRNAME");
                header.setArea("AREA");
                header.setINDAREA("MANUAL AREA");
                header.setCurrentDate("DATE");
                header.setColor("#000000");
                header.setTextColor("#FFFFFF");
                indentModelList.add(header);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    IndentModel indentModel=new IndentModel();
                    indentModel.setVillage(jsonObject.getString("VILLCODE"));
                    indentModel.setVillageName(jsonObject.getString("VILLNAME"));
                    indentModel.setGrower(jsonObject.getString("GCODE"));
                    indentModel.setGrowerName(jsonObject.getString("GNAME"));
                    indentModel.setGrowerFather(jsonObject.getString("GFATHER"));
                    indentModel.setVARIETY(jsonObject.getString("VRCODE"));
                    indentModel.setVARIETYNAME(jsonObject.getString("VRNAME"));
                    indentModel.setArea(jsonObject.getString("GPSAREA"));
                    indentModel.setINDAREA(jsonObject.getString("MUNALAREA"));
                    indentModel.setCurrentDate(jsonObject.getString("DATE"));
                    if(i%2==0)
                    {
                        indentModel.setColor("#DFDFDF");
                        indentModel.setTextColor("#000000");
                    }
                    else
                    {
                        indentModel.setColor("#FFFFFF");
                        indentModel.setTextColor("#000000");
                    }
                    indentModelList.add(indentModel);
                }

                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                StaffSeedIndentAdapter stockSummeryAdapter =new StaffSeedIndentAdapter(context,indentModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
            } catch (JSONException e) {
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                StaffSeedIndentAdapter stockSummeryAdapter =new StaffSeedIndentAdapter(context,indentModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,Content);
            }catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,"Error :- "+e.getClass().getName()+" - "+ e.getMessage());
            }
        }
    }


}
