package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;

import in.co.vibrant.bindalsugar.adapter.AgreeInpuAdapter;
import in.co.vibrant.bindalsugar.model.AgriInput;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class AgreeInputReport extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    List<AgriInput> agriInputList = new ArrayList<>();
    TextView date1, vilageName1, growerName1, bleachingP1, hexa1, TrichoP1, TrichoL1, emida1, corajen1, ferrous1,supervisorCode,super_visorName;
    String date, villageCode,grocode,vilageName, growerName, bleachingP, hexa, TrichoP, TrichoL, emida, corajen, ferrous;
    String currentDt;
    EditText fromdate_select,todate_selects;
    List<UserDetailsModel> userDetailsModels;
    List<MasterDropDown> targetList;
    LinearLayout linearReport;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_agree_input_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_AGRI_DISTRIBUTION_REPORT));
        toolbar.setTitle(getString(R.string.MENU_AGRI_DISTRIBUTION_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = AgreeInputReport.this;
        linearReport=findViewById(R.id.linearReport);
        date1 = findViewById(R.id.date1);
        vilageName1 = findViewById(R.id.vilageName1);
        growerName1 = findViewById(R.id.growerName1);
        bleachingP1 = findViewById(R.id.bleachingP1);
        hexa1 = findViewById(R.id.hexa1);
        TrichoP1 = findViewById(R.id.TrichoP1);
        TrichoL1 = findViewById(R.id.TrichoL1);
        emida1 = findViewById(R.id.emida1);
        corajen1 = findViewById(R.id.corajen1);
        ferrous1 = findViewById(R.id.ferrous1);
        super_visorName = findViewById(R.id.super_visorName);
        fromdate_select=findViewById(R.id.fromdate_select);
        todate_selects=findViewById(R.id.todate_selects);
        targetList=new ArrayList<>();


        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        supervisorCode=findViewById(R.id.supervisorCode);
        supervisorCode.setText(userDetailsModels.get(0).getCode());
        super_visorName.setText(userDetailsModels.get(0).getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDt = dateFormat.format(today);




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
                DatePickerDialog dpd = new DatePickerDialog(AgreeInputReport.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(AgreeInputReport.this, new DatePickerDialog.OnDateSetListener() {
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
                    new GETAGREEREPORT().execute(supervisorCode.getText().toString(),fromdate_select.getText().toString(),todate_selects.getText().toString());

                }
                catch(Exception e)
                {

                }
            }
        });


        try {
            new GETAGREEREPORT().execute(supervisorCode.getText().toString(),currentDt,currentDt);

        }catch (Exception e){
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }



    }



    class GETAGREEREPORT extends AsyncTask<String, Void, String> {

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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetEmployeeSalesReport";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("sup", params[0]));// 22001 // 2
                entity.add(new BasicNameValuePair("Fdate",params[1]));//2022-05-06
                entity.add(new BasicNameValuePair("Tdate",params[2]));//2023-05-06

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
                if(agriInputList.size()>0)
                    agriInputList.clear();

                if (dialog.isShowing())
                    dialog.dismiss();

                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    JSONObject object = jsonArray.getJSONObject(0);
                    Iterator<String> keys = object.keys();
                    ArrayList<String> stringsKey = new ArrayList<>();
                    ArrayList<String> stringsvalue = new ArrayList<>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = object.getString(key);
                        stringsKey.add(key);
                    }

                    date = stringsKey.get(0).toString();
                    villageCode = stringsKey.get(1).toString();
                    vilageName = stringsKey.get(2).toString();
                    grocode = stringsKey.get(3).toString();
                    growerName = stringsKey.get(4).toString();
                    bleachingP = stringsKey.get(5).toString();
                    TrichoP = stringsKey.get(6).toString();
                    TrichoL = stringsKey.get(7).toString();
                    emida = stringsKey.get(8).toString();
                    corajen = stringsKey.get(9).toString();
                    hexa = stringsKey.get(10).toString();
                    ferrous = stringsKey.get(11).toString();
                    date1.setText("Date");//date
                    vilageName1.setText("Village Name");//vilageName
                    growerName1.setText("Grower Name");//growerName
                    bleachingP1.setText(bleachingP);
                    hexa1.setText(hexa);
                    TrichoP1.setText(TrichoP);
                    TrichoL1.setText(TrichoL);
                    emida1.setText(emida);
                    corajen1.setText(corajen);
                    ferrous1.setText(ferrous);



                   /* JSONObject jsonObj = new JSONObject(result);
                    Iterator<String> keys = jsonObj.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = jsonObj.getString(key);
                        Log.d("JSON", "Key: " + key + ", Value: " + value);

                    JSONObject jsonObject = new JSONObject(jsonString);

                    StringBuilder stringBuilder = new StringBuilder();

                    Iterator<String> keys = jsonObject.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        String value = jsonObject.getString(key);

                        stringBuilder.append(key).append(": ").append(value).append("\n");
                    }*/

                    for (int i = 0; i < jsonArray.length(); i++) {
                        object = jsonArray.getJSONObject(i);
                        AgriInput model = new AgriInput();
                        model.setDate(object.getString(date));//DATE
                        model.setVillCode(object.getString(villageCode));//VILLCODE
                        model.setVillName(object.getString(vilageName));//VILLNAME
                        model.setGroCode(object.getString(grocode));//GROCODE
                        model.setGroName(object.getString(growerName));//GRONAME
                        model.setBleachingPowder(object.getString(bleachingP));//Bleaching Powder @  384.00
                        model.setTrichormePowder(object.getString(TrichoP));//Trichoderma Power @  76.16
                        model.setTrichormeLiquid(object.getString(TrichoL));//Trichoderma Liquid @  208.00
                        model.setEmida(object.getString(emida));//Emida 100 ml @  145.00
                        model.setCorajen(object.getString(corajen));//Corajen @  1750.00
                        model.setFerrosSulphate(object.getString(ferrous));//Ferrous Sulphate @  150.00
                        model.setHexaStop(object.getString(hexa));//Hexastop @  97.00
                        agriInputList.add(model);

                    }
                    linearReport.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.agriInputRecycler);
                    AgreeInpuAdapter adapter = new AgreeInpuAdapter(context, agriInputList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(adapter);

                    }
                else {
                    linearReport.setVisibility(View.GONE);
                    new AlertDialogManager().RedDialog(context, "" +Content.toString());


                }

            } catch (JSONException e) {
                System.out.println("Error : " + Content);
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,Content);
                linearReport.setVisibility(View.GONE);

                RecyclerView recyclerView = findViewById(R.id.agriInputRecycler);
                AgreeInpuAdapter adapter = new AgreeInpuAdapter(context, agriInputList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
                } catch(Exception e){
                    if (dialog.isShowing())
                        dialog.dismiss();
                linearReport.setVisibility(View.GONE);
                    System.out.println("Error : " + e.toString());
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                    new AlertDialogManager().RedDialog(context, "Error :- " + e.getClass().getName() + " - " + e.getMessage());
                }
            }




        }


    }
