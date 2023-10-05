package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.SeedReservationModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class FieldStaffPlotSeedReservation extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> loginUserDetailsList;
    List<MasterDropDown> datavaraiety,datacategory;

    TextInputLayout input_layout_village_code,input_layout_village_name,input_layout_grower_code,input_layout_grower_name,
            input_layout_grower_father,input_layout_mobile_number,input_layout_date,input_layout_variety,
            input_layout_category,input_layout_seed_qty,input_layout_rate,input_layout_transportation,input_layout_distance;
    EditText input_village_code,input_village_name,input_grower_code,input_grower_name,input_grower_father,seed_qty,rate,
            mobile_number,distance,date;

    Spinner input_variety,input_category,transportation;
    Context context;
    SeedReservationModel seedReservationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_plot_seed_reservation);
        context= FieldStaffPlotSeedReservation.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_SEED_RESERVATION));
        toolbar.setTitle(getString(R.string.MENU_SEED_RESERVATION));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        datavaraiety=dbh.getMasterDropDown("12");
        datacategory=dbh.getMasterDropDown("13");
        loginUserDetailsList=new ArrayList<>();
        loginUserDetailsList=dbh.getUserDetailsModel();


        input_layout_village_code=findViewById(R.id.input_layout_village_code);
        input_layout_village_name=findViewById(R.id.input_layout_village_name);
        input_layout_grower_code=findViewById(R.id.input_layout_grower_code);
        input_layout_grower_name=findViewById(R.id.input_layout_grower_name);
        input_layout_grower_father=findViewById(R.id.input_layout_grower_father);
        input_layout_variety=findViewById(R.id.input_layout_variety);
        input_layout_category=findViewById(R.id.input_layout_category);
        input_layout_seed_qty=findViewById(R.id.input_layout_seed_qty);
        input_layout_mobile_number=findViewById(R.id.input_layout_mobile_number);
        input_layout_rate=findViewById(R.id.input_layout_rate);
        input_layout_date=findViewById(R.id.input_layout_date);
        input_layout_transportation=findViewById(R.id.input_layout_transportation);
        input_layout_distance=findViewById(R.id.input_layout_distance);



        input_village_code=findViewById(R.id.input_village_code);
        input_village_name=findViewById(R.id.input_village_name);
        input_grower_code=findViewById(R.id.input_grower_code);
        input_grower_name=findViewById(R.id.input_grower_name);
        input_grower_father=findViewById(R.id.input_grower_father);
        seed_qty=findViewById(R.id.seed_qty);
        rate=findViewById(R.id.rate);
        transportation=findViewById(R.id.transportation);
        distance=findViewById(R.id.distance);
        mobile_number=findViewById(R.id.mobile_number);
        date=findViewById(R.id.date);


        input_variety=findViewById(R.id.input_variety);
        input_category=findViewById(R.id.input_category);

        //new getWarehouseList().execute();

        init();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt=dateFormat.format(today);
        //paymentDate.setText(currentDt);
        date.setInputType(InputType.TYPE_NULL);
        date.setTextIsSelectable(true);
        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
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
                        date.setText(year+"-"+  temmonth + "-"+temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

    }

    public void exit(View v)
    {
        finish();
    }

    private void init()
    {
        input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (input_village_code.getText().toString().length() > 0) {
                    List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                    if (villageModalList.size() > 0) {
                        input_village_code.setText(villageModalList.get(0).getCode());
                        input_village_name.setText(villageModalList.get(0).getName());
                    } else {
                        input_village_code.setText("");
                        input_village_name.setText("");
                        new AlertDialogManager().RedDialog(context,"Please enter valid village code");
                    }

                }
            }
        });
        input_grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (input_village_code.getText().toString().length() > 0) {
                    if (input_grower_code.getText().toString().length() > 0) {
                        if(input_grower_code.getText().toString().equals("0"))
                        {
                            input_grower_name.setFocusable(true);
                            input_grower_name.setTextIsSelectable(true);
                            input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            input_grower_father.setFocusable(true);
                            input_grower_father.setTextIsSelectable(true);
                            input_grower_father.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        }
                        else
                        {
                            input_grower_name.setFocusable(false);
                            input_grower_name.setTextIsSelectable(false);
                            input_grower_name.setInputType(InputType.TYPE_NULL);
                            input_grower_father.setFocusable(false);
                            input_grower_father.setTextIsSelectable(false);
                            input_grower_father.setInputType(InputType.TYPE_NULL);
                            List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                            if (growerModelList.size() > 0) {
                                input_grower_code.setText(growerModelList.get(0).getGrowerCode());
                                input_grower_name.setText(growerModelList.get(0).getGrowerName());
                                input_grower_father.setText(growerModelList.get(0).getGrowerFather());
                            } else {
                                new AlertDialogManager().RedDialog(context,"Please enter valid grower code");
                                input_grower_code.setText("");
                                input_grower_name.setText("");
                                input_grower_father.setText("");
                            }

                        }

                    }
                } else {
                    new AlertDialogManager().RedDialog(context,"Please enter village code");
                    input_village_code.requestFocus();
                }
            }
        });


        ArrayList<String> datavaraietyArray = new ArrayList<String>();
        datavaraietyArray.add("Variety");
        for (int i = 0; i < datavaraiety.size(); i++) {
            datavaraietyArray.add(datavaraiety.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, datavaraietyArray);
        input_variety.setAdapter(adaptersupply);

        ArrayList<String> datacategoryArray = new ArrayList<String>();
        datacategoryArray.add("Category");
        for (int i = 0; i < datacategory.size(); i++) {
            datacategoryArray.add(datacategory.get(i).getName());
        }
        ArrayAdapter<String> adapterdatacategory = new ArrayAdapter<String>(context,
                R.layout.list_item, datacategoryArray);
        input_category.setAdapter(adapterdatacategory);

        ArrayList<String> transportationArray = new ArrayList<String>();
        transportationArray.add("Transportation");
        transportationArray.add("Self");
        transportationArray.add("Suppliers");
        ArrayAdapter<String> adaptertransportation = new ArrayAdapter<String>(context,
                R.layout.list_item, transportationArray);
        transportation.setAdapter(adaptertransportation);


    }

    public void saveData(View v)
    {
        try {
            input_layout_village_code.setErrorEnabled(false);
            input_layout_village_name.setErrorEnabled(false);
            input_layout_grower_code.setErrorEnabled(false);
            input_layout_variety.setErrorEnabled(false);
            input_layout_mobile_number.setErrorEnabled(false);
            input_layout_date.setErrorEnabled(false);
            input_layout_category.setErrorEnabled(false);
            input_layout_seed_qty.setErrorEnabled(false);
            input_layout_rate.setErrorEnabled(false);
            input_layout_transportation.setErrorEnabled(false);
            input_layout_distance.setErrorEnabled(false);
            if(input_village_code.getText().toString().length()==0)
            {
                input_layout_village_code.setError("Please enter valid village code");
                input_layout_village_code.setErrorEnabled(true);
            }
            else if(input_grower_code.getText().toString().length()==0)
            {
                input_layout_grower_code.setError("Please enter valid grower code");
                input_layout_grower_code.setErrorEnabled(true);
            }
            else if(input_variety.getSelectedItemPosition()==0)
            {
                input_layout_variety.setError("Please select variety");
                input_layout_variety.setErrorEnabled(true);
            }
            else if(input_category.getSelectedItemPosition()==0)
            {
                input_layout_category.setError("Please enter area");
                input_layout_category.setErrorEnabled(true);
            }
            else if(seed_qty.getText().toString().length()==0)
            {
                input_layout_seed_qty.setError("Please enter seed quantity");
                input_layout_seed_qty.setErrorEnabled(true);
            }
            else if(rate.getText().toString().length()==0)
            {
                input_layout_rate.setError("Please enter rate");
                input_layout_rate.setErrorEnabled(true);
            }
            else if(transportation.getSelectedItemPosition()==0)
            {
                input_layout_transportation.setError("Please enter transportation");
                input_layout_transportation.setErrorEnabled(true);
            }
            else if(distance.getText().toString().length()==0)
            {
                input_layout_distance.setError("Please enter distance");
                input_layout_distance.setErrorEnabled(true);
            }
            else if(mobile_number.getText().toString().length()!=10)
            {
                input_layout_mobile_number.setError("Please enter mobile number");
                input_layout_mobile_number.setErrorEnabled(true);
            }
            else if(date.getText().toString().length()==0)
            {
                input_layout_date.setError("Please select date");
                input_layout_date.setErrorEnabled(true);
            }
            else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date today = Calendar.getInstance().getTime();
                String currentDt=dateFormat.format(today);
                seedReservationModel=new SeedReservationModel();
                seedReservationModel.setVillage(input_village_code.getText().toString());
                seedReservationModel.setGrower(input_grower_code.getText().toString());
                seedReservationModel.setVariety(datavaraiety.get(input_variety.getSelectedItemPosition()-1).getCode());
                seedReservationModel.setCategory(datacategory.get(input_category.getSelectedItemPosition()-1).getCode());
                seedReservationModel.setQuantity(seed_qty.getText().toString());
                seedReservationModel.setRate(rate.getText().toString());
                seedReservationModel.setTransportation(transportation.getSelectedItem().toString());
                seedReservationModel.setDistance(distance.getText().toString());
                seedReservationModel.setMobileNumber(mobile_number.getText().toString());
                seedReservationModel.setCurrentDate(currentDt); ;
                seedReservationModel.setSupervisorCode(loginUserDetailsList.get(0).getCode());
                //dbh.insertSeedReservationModel(seedReservationModel);
                //new AlertDialogManager().AlertPopUpFinish(context,"Data saved successfully ");
                new ImageUploadTask().execute();
            }

        }
        catch (SecurityException e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    class ImageUploadTask extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Uploading",
                    "Please wait while we transfer your file to server", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SAVESEEDRESERVATION";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();

                entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("VillCode",seedReservationModel.getVillage()));
                entity.add(new BasicNameValuePair("GrowCode",seedReservationModel.getGrower()));
                entity.add(new BasicNameValuePair("CATEGORY",seedReservationModel.getCategory()));
                entity.add(new BasicNameValuePair("Variety",seedReservationModel.getVariety()));
                entity.add(new BasicNameValuePair("QTY",seedReservationModel.getQuantity()));
                entity.add(new BasicNameValuePair("RATE",seedReservationModel.getRate()));
                entity.add(new BasicNameValuePair("Transportation",seedReservationModel.getTransportation()));
                entity.add(new BasicNameValuePair("Distance",seedReservationModel.getDistance()));
                entity.add(new BasicNameValuePair("MobilNo",seedReservationModel.getMobileNumber()));
                entity.add(new BasicNameValuePair("SDate",seedReservationModel.getCurrentDate()));
                entity.add(new BasicNameValuePair("SupervisorCode",loginUserDetailsList.get(0).getCode()));
                entity.add(new BasicNameValuePair("ACKID","0"));


                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + Content);

                //Content=response.getEntity().toString();
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error: "+e.toString());
                //Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                new AlertDialogManager().AlertPopUpFinish(context,jsonObject.getString("SAVEMSG")+" "+jsonObject.getString("EXCEPTIONMSG"));
            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }


}
