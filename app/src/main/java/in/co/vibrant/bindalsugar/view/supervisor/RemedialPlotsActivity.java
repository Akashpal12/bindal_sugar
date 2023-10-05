package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import in.co.vibrant.bindalsugar.adapter.RemedialPlotsAdapter;
import in.co.vibrant.bindalsugar.model.GETPOCTYPEModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.RemedialPlotsModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageModelNew;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class RemedialPlotsActivity extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    Spinner spinner_items,village_list_spinner;
    String selectedVCODE;
    List<RemedialPlotsModel> modelList = new ArrayList<>();
    List<VillageModelNew> villageModelNewList = new ArrayList<>();
    List<GETPOCTYPEModel> getpoctypeModelList = new ArrayList<>();
    String currentDt;
    EditText fromdate_select,todate_selects;
    List<UserDetailsModel> userDetailsModels;
    List<MasterDropDown> targetList, plotTypeList;;
    LinearLayout main_layout;
    String plotTypeSelection,VillageTypeSelection;
    double plotsonDate = 0.0,areaonDate=0.0,plotstodate=0.0,areatodate=0.0;
    TextView plots_onDate,area_onDate,plots_todate,area_todate;

    String[] getSpinnerItems={"Village - 1","Village - 2","Village - 3","Village - 4"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_plots_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Remedial Plots Report");
        toolbar.setTitle("Remedial Plots Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = RemedialPlotsActivity.this;
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);
        spinner_items=findViewById(R.id.spinner_items);
        village_list_spinner=findViewById(R.id.village_list_spinner);
        modelList=new ArrayList<>();
        villageModelNewList=new ArrayList<>();


        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDt = dateFormat.format(today);
        main_layout=findViewById(R.id.main_layout);

        plots_onDate=findViewById(R.id.plots_onDate);
        area_onDate=findViewById(R.id.area_onDate);
        plots_todate=findViewById(R.id.plots_todate);
        area_todate=findViewById(R.id.area_todate);
        new GETDISEASE().execute();
        getVillage();




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
                DatePickerDialog dpd = new DatePickerDialog(RemedialPlotsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(RemedialPlotsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    if (village_list_spinner.getSelectedItemPosition()==0) {
                        new AlertDialogManager().showToast((Activity) context,"Select Village First");

                    }else {
                        new getDiseaseVillage().execute(selectedVCODE);

                    }


                }
                catch(Exception e)
                {

                }
            }
        });



        Button btn_map=findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (village_list_spinner.getSelectedItemPosition()==0) {
                        new AlertDialogManager().showToast((Activity) context,"Select Village First");

                    }else {
                        //new POCTYPE().execute(selectedVCODE);
                        Intent a = new Intent(context, RemadialFinderMapViewMaster.class);
                        a.putExtra("V_CODE",""+selectedVCODE);
                        a.putExtra("PLOT_SELECTION",""+plotTypeSelection);
                        context.startActivity(a);
                    }


                }
                catch(Exception e)
                {

                }
            }
        });




      //  new getPUCvillages().execute();






        try {
            //new PlotSummaryReport().execute(currentDt,currentDt);
            //new PlotSummaryReport().execute();

        }catch (Exception e){
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }



    class getDiseaseVillage extends AsyncTask<String, Void, String> {

        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Please wait", "Please wait while we getting details", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/getDiseaseVillage";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DiseasCode", plotTypeSelection));// 22001 // 2
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));// 22001 // 2
                entity.add(new BasicNameValuePair("PLVILL", params[0]));// 22001 // 2

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
                        RemedialPlotsModel model=new RemedialPlotsModel();

                        model.setDISEASECODE(object.getString("DISEASECODE"));
                        model.setDISEASE(object.getString("DISEASE"));
                        model.setPLVLCODE(object.getString("PLVLCODE"));
                        model.setPLVLNAME(object.getString("PLVLNAME"));
                        model.setPLNO(object.getString("PLNO"));
                        model.setG_CODE(object.getString("G_CODE"));
                        model.setG_NAME(object.getString("G_NAME"));
                        model.setG_FATHER(object.getString("G_FATHER"));
                        model.setGH_NE_LAT(object.getDouble("GH_NE_LAT"));
                        model.setGH_NE_LNG(object.getDouble("GH_NE_LNG"));
                        model.setGH_SE_LAT(object.getDouble("GH_SE_LAT"));
                        model.setGH_SE_LNG(object.getDouble("GH_SE_LNG"));
                        model.setGH_SW_LAT(object.getDouble("GH_SW_LAT"));
                        model.setGH_SW_LNG(object.getDouble("GH_SW_LNG"));
                        model.setGH_NW_LAT(object.getDouble("GH_NW_LAT"));
                        model.setGH_NW_LNG(object.getDouble("GH_NW_LNG"));
                        modelList.add(model);

                    }
                    main_layout.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    RemedialPlotsAdapter adapter = new RemedialPlotsAdapter(context, modelList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(adapter);


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
                RemedialPlotsAdapter adapter = new RemedialPlotsAdapter(context, modelList);
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

    class GETDISEASE extends AsyncTask<String, Void, String> {

        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Please wait", "Please wait while we getting details", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETDISEASE";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));// 22001 // 2
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
                if(getpoctypeModelList.size()>0)
                    getpoctypeModelList.clear();

                if (dialog.isShowing())
                    dialog.dismiss();

                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        GETPOCTYPEModel model=new GETPOCTYPEModel();
                        model.setCode(object.getString("Code"));
                        model.setName(object.getString("Name"));
                        getpoctypeModelList.add(model);

                    }

                    ArrayList getPocType = new ArrayList();
                    getPocType.add("All Plots");
                    for (int i = 0; i < getpoctypeModelList.size(); i++) {
                        getPocType.add(getpoctypeModelList.get(i).getCode() + " - " + getpoctypeModelList.get(i).getName());
                    }

                    ArrayAdapter<String> diseffectedTypeList = new ArrayAdapter<String>(context, R.layout.list_item, getPocType);
                    spinner_items.setAdapter(diseffectedTypeList);
                    spinner_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                plotTypeSelection = ""; // Change this to "0" for "All Plots"
                            } else {
                                // Subtract 1 from the position to map it to the correct index in getpoctypeModelList
                                int modelIndex = position - 1;
                                if (modelIndex >= 0 && modelIndex < getpoctypeModelList.size()) {
                                    plotTypeSelection = getpoctypeModelList.get(modelIndex).getCode();

                                }
                            }



                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }


            } catch (JSONException e) {

                try {
                  //  new AlertDialogManager().AlertPopUpFinish(context,new JSONObject(Content).getString("MSG"));
                } catch (Exception e1) {
                    e1.getMessage();
                }

            } catch(Exception e){

            }
        }




    }

    public void getVillage() {
        List<VillageModal> villageModelListTemp=new ArrayList<>();
        villageModelListTemp= dbh.getVillageModal("");

        ArrayList<String> data=new ArrayList<String>();
        data.add(" - Select Village - ");

        for(int i=0;i<villageModelListTemp.size();i++)
        {
            data.add(villageModelListTemp.get(i).getCode()+" - "+villageModelListTemp.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village_list_spinner.setAdapter(adaptersupply);
        village_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    //getGrower("");
                }
                else
                {
                   selectedVCODE=   village_list_spinner.getSelectedItem().toString().split(" - ")[0];

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
