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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.StaffSeedMappingAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.SeedMappingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ReportSupervisorSeedMappingDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<VillageSurveyModel> villageSurveyModelList;
    List<MasterDropDown> varietyList;
    Spinner village,grower;
    List<UserDetailsModel> userDetailsModels;
    List<SeedMappingModel> seedMappingModels;
    EditText fromdate_select,todate_selects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_seed_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= ReportSupervisorSeedMappingDetails.this;
        setTitle("Seed Mapping");
        toolbar.setTitle("Seed Mapping");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        villageSurveyModelList =new ArrayList<>();
        varietyList =new ArrayList<>();
        village=findViewById(R.id.village);
        grower=findViewById(R.id.grower);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
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
                DatePickerDialog dpd = new DatePickerDialog(ReportSupervisorSeedMappingDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(ReportSupervisorSeedMappingDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                    if(village.getSelectedItemPosition()==0){
                        new AlertDialogManager().showToast(ReportSupervisorSeedMappingDetails.this, "Please select the village" );

                    } else if(grower.getSelectedItemPosition()==0){
                        new AlertDialogManager().showToast(ReportSupervisorSeedMappingDetails.this, "Please select the variety" );
                    }else {
                        new GetData().execute(villageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode(),
                                varietyList.get(grower.getSelectedItemPosition()).getCode());

                    }
                }
                catch(Exception e)
                {

                }
            }
        });

        //--------------------------------------------------------------------------------------
        ArrayList<String> growerdata=new ArrayList<String>();
        growerdata.add("All Variety");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);

        getVillage();
        getVariety();
    }

    public void getVillage()
    {
        List<VillageSurveyModel> villageSurveyModelList1=dbh.getSurveyVillageModel("");
        VillageSurveyModel staffTargetModal1=new VillageSurveyModel();
        staffTargetModal1.setVillageCode("");
        staffTargetModal1.setVillageName("All Village");
        villageSurveyModelList.add(staffTargetModal1);
        ArrayList<String> data=new ArrayList<String>();
        data.add("All Village");
        for(int i=0;i<villageSurveyModelList1.size();i++)
        {
            VillageSurveyModel staffTargetModal=new VillageSurveyModel();
            staffTargetModal.setVillageCode(villageSurveyModelList1.get(i).getVillageCode());
            staffTargetModal.setVillageName(villageSurveyModelList1.get(i).getVillageName());
            data.add(villageSurveyModelList1.get(i).getVillageCode()+" - "+villageSurveyModelList1.get(i).getVillageName());
            villageSurveyModelList.add(staffTargetModal);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              /*  new GetData().execute(villageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode(),
                        varietyList.get(grower.getSelectedItemPosition()).getCode());*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getVariety()
    {
        List<MasterDropDown> varietyList1 = dbh.getMasterDropDown("12");
        MasterDropDown variety =new MasterDropDown();
        variety.setCode("");
        variety.setName("All Variety");
        varietyList.add(variety);
        ArrayList<String> data=new ArrayList<String>();
        data.add("All Variety");
        for(int i=0;i<varietyList1.size();i++)
        {
            MasterDropDown masterDropDown =new MasterDropDown();
            masterDropDown.setCode(varietyList1.get(i).getCode());
            masterDropDown.setName(varietyList1.get(i).getName());
            data.add(varietyList1.get(i).getCode()+" - "+varietyList1.get(i).getName());
            varietyList.add(masterDropDown);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,R.layout.list_item, data);
        grower.setAdapter(adaptersupply);
        grower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               /* new GetData().execute(villageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode(),
                        varietyList.get(grower.getSelectedItemPosition()).getCode());*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GETSUPERVOISERSEDDMAPPDETAILS";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("VILL",params[0]));
                entity.add(new BasicNameValuePair("VRCODE",params[1]));
                entity.add(new BasicNameValuePair("DATE",fromdate_select.getText().toString()));
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
                seedMappingModels=new ArrayList<>();
                JSONArray jsonArray = new JSONArray(Content);

                SeedMappingModel header=new SeedMappingModel();
                header.setVillage("SUP VCODE");
                header.setVillageName("SUP VNAME");
                header.setGrower("SUP GCODE");
                header.setGrowerName("SUP GNAME");
                header.setGrowerFatherName("SUP GFATHER");
                header.setVariety("VRCODE");
                header.setVarietyName("VRNAME");
                header.setMapVillage("PUR VCODE");
                header.setMapVillageName("PUR VNAME");
                header.setMapGrower("PUR GCODE");
                header.setMapGrowerName("PUR GNAME");
                header.setMapGrowerFatherName("PUR GFATHER");
                header.setQuantity("QTY");
                header.setCurrentDate("DATE");
                header.setColor("#000000");
                header.setTextColor("#FFFFFF");
                seedMappingModels.add(header);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    SeedMappingModel seedReservationModel=new SeedMappingModel();
                    seedReservationModel.setVillage(jsonObject.getString("SUPVILL"));
                    seedReservationModel.setVillageName(jsonObject.getString("SUPVILLNAME"));
                    seedReservationModel.setGrower(jsonObject.getString("SUPGCODE"));
                    seedReservationModel.setGrowerName(jsonObject.getString("SUPGNAME"));
                    seedReservationModel.setGrowerFatherName(jsonObject.getString("SUPGFATHER"));
                    seedReservationModel.setVariety(jsonObject.getString("VRCODE"));
                    seedReservationModel.setVarietyName(jsonObject.getString("VRNAME"));
                    seedReservationModel.setMapVillage(jsonObject.getString("PURVILL"));
                    seedReservationModel.setMapVillageName(jsonObject.getString("PURVILLNAME"));
                    seedReservationModel.setMapGrower(jsonObject.getString("PURGCODE"));
                    seedReservationModel.setMapGrowerName(jsonObject.getString("PURGNAME"));
                    seedReservationModel.setMapGrowerFatherName(jsonObject.getString("PURGFATHER"));
                    seedReservationModel.setQuantity(jsonObject.getString("PURQTY"));
                    seedReservationModel.setCurrentDate(jsonObject.getString("DATE"));
                    if(i%2==0)
                    {
                        seedReservationModel.setColor("#DFDFDF");
                        seedReservationModel.setTextColor("#000000");
                    }
                    else
                    {
                        seedReservationModel.setColor("#FFFFFF");
                        seedReservationModel.setTextColor("#000000");
                    }
                    seedMappingModels.add(seedReservationModel);
                }

                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                StaffSeedMappingAdapter stockSummeryAdapter =new StaffSeedMappingAdapter(context,seedMappingModels);
                recyclerView.setAdapter(stockSummeryAdapter);
            } catch (JSONException e) {
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                StaffSeedMappingAdapter stockSummeryAdapter =new StaffSeedMappingAdapter(context,seedMappingModels);
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
