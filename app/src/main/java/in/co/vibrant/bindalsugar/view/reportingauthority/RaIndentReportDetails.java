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
import in.co.vibrant.bindalsugar.adapter.StaffSeedIndentAdapter;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class RaIndentReportDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    EditText date;
    Spinner supervisor;
    List<UserDetailsModel> userDetailsModels;
    List<MasterDropDown> supervisorList;
    List<IndentModel> indentModelList;
    EditText From_date,to_date;
    String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_seed_indent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaIndentReportDetails.this;
        setTitle("Indent Report");
        toolbar.setTitle("Indent Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        date=findViewById(R.id.date);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        supervisor=findViewById(R.id.supervisor);
        supervisorList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();

        From_date=findViewById(R.id.fromdate_select);
        to_date=findViewById(R.id.todate_selects);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDate = dateFormat.format(today);
        From_date.setText(currentDate);
        From_date.setInputType(InputType.TYPE_NULL);
        From_date.setTextIsSelectable(true);
        From_date.setFocusable(false);
        From_date.setOnClickListener(new View.OnClickListener() {
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
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        String yr=""+year;
                        //yr=yr.substring(2);
                        currentDate=year+"-"+temmonth+"-"+temDate;
                        From_date.setText(year+"-"+temmonth+"-"+temDate );
                        // new ReportVillageWithVarietyReport.TargetList().execute(typeOfPlantingList.get(planting_type.getSelectedItemPosition()).getCode(),currentDate);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        //----------------------todate--------------------------------------------------------------


        to_date.setText(currentDate);
        to_date.setInputType(InputType.TYPE_NULL);
        to_date.setTextIsSelectable(true);
        to_date.setFocusable(false);
        to_date.setOnClickListener(new View.OnClickListener() {
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
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        String yr=""+year;
                        //yr=yr.substring(2);
                        currentDate=year+"-"+temmonth+"-"+temDate;
                        to_date.setText(year+"-"+temmonth+"-"+temDate );
                        // new ReportVillageWithVarietyReport.TargetList().execute(typeOfPlantingList.get(planting_type.getSelectedItemPosition()).getCode(),currentDate);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        getSpinner();

        new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),currentDate,currentDate);

    }




    public void searchData(View v) {
        new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),From_date.getText().toString(),to_date.getText().toString());

    }







    public void getSpinner()
    {
        try{
            List<MasterDropDown> supervisorList1=dbh.getMasterDropDown("23");
            ArrayList<String> data=new ArrayList<String>();
            MasterDropDown all=new MasterDropDown();
            all.setCode("0");
            all.setName("0 / All");
            data.add("0 / All");
          /*  all.setCode("0");
            all.setName("Select Supervisor");
            data.add("Select Supervisor");
            data.add("0-All");*/
            supervisorList.add(all);
            for(int i=0;i<supervisorList1.size();i++)
            {
                data.add(supervisorList1.get(i).getCode()+" / "+supervisorList1.get(i).getName());
                supervisorList.add(supervisorList1.get(i));
            }
            ArrayAdapter<String> adapterSupervisor = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            supervisor.setAdapter(adapterSupervisor);
            supervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i>0)
                    {
                        if(new InternetCheck(context).isOnline())
                        {
                            // new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),From_date.getText().toString(), to_date.getText().toString());
                        }
                        else
                        {
                            new AlertDialogManager().RedDialog(context,"No internet found");
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
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
                entity.add(new BasicNameValuePair("SUPCODE",params[0]));
                entity.add(new BasicNameValuePair("TargetType","0"));
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
            JSONArray jsonArray1=new JSONArray();
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                indentModelList=new ArrayList<>();
                JSONArray jsonArray = new JSONArray(Content);
                if(jsonArray.length()==0)
                {
                    new AlertDialogManager().AlertPopUp(context,"No data found");
                }
                else {
                    IndentModel header = new IndentModel();
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
                    double totaarea = 0.0, totalmanualarea = 0.0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        IndentModel indentModel = new IndentModel();
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

                        totaarea += jsonObject.getDouble("GPSAREA");
                        totalmanualarea += jsonObject.getDouble("MUNALAREA");

                        if (i % 2 == 0) {
                            indentModel.setColor("#DFDFDF");
                            indentModel.setTextColor("#000000");
                        } else {
                            indentModel.setColor("#FFFFFF");
                            indentModel.setTextColor("#000000");
                        }
                        indentModelList.add(indentModel);
                    }

                    IndentModel footer=new IndentModel();
                    footer.setVillage("Total");
                    footer.setVillageName("");
                    footer.setGrower("");
                    footer.setGrowerName("");
                    footer.setVARIETY("");
                    footer.setVARIETYNAME("");
                    footer.setArea(new DecimalFormat("0.000").format(totaarea));
                    footer.setINDAREA(new DecimalFormat("0.000").format(totalmanualarea));
                    footer.setColor("#000000");
                    footer.setTextColor("#FFFFFF");
                    indentModelList.add(footer);
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
