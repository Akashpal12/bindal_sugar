package in.co.vibrant.bindalsugar.view.supervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;

public class RescheduleActivityForm extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    Spinner target_dd, selectVillage_Spinner;
    EditText fromdate_select, todate_selects;
    Spinner supervisor;
    String currentDate = "";
    EditText desription_editText;
    String selectedCode = "";
    String villageShowCode;
    TextInputLayout textInputSelctVillage;
    String SelectedVillageCode;
    String VillageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = RescheduleActivityForm.this;
        setTitle("Reschedule Request Form");
        toolbar.setTitle("Reschedule Request Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        targetList = new ArrayList<>();
        target_dd = findViewById(R.id.target_dd);
        dbh = new DBHelper(context);
        staffTargetModelList = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        desription_editText = findViewById(R.id.desription_editText);
        selectVillage_Spinner = findViewById(R.id.selectVillage_Spinner);
        textInputSelctVillage = findViewById(R.id.textInputSelctVillage);
        Intent intent = getIntent();
        if (intent != null) {
            VillageCode = intent.getStringExtra("VILL_CODE"); // Replace "key" with your key
            // Use the data as needed
        }


        // getSpinner();
        new ReasonList().execute();


        //--------------------------------calender----------------------------------------------
        fromdate_select = findViewById(R.id.fromdate_select);
        todate_selects = findViewById(R.id.todate_selects);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        currentDate = dateFormat.format(today);
        fromdate_select.setText(currentDate);
        fromdate_select.setInputType(InputType.TYPE_NULL);
        fromdate_select.setTextIsSelectable(true);
        fromdate_select.setFocusable(false);
      /*  fromdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RescheduleActivityForm.this, new DatePickerDialog.OnDateSetListener() {
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
        });*/


        //---------------------TODATE-----------------------------------------
        todate_selects = findViewById(R.id.todate_selects);
        todate_selects.setInputType(InputType.TYPE_NULL);
        todate_selects.setFocusable(false);

        todate_selects = findViewById(R.id.todate_selects);
        todate_selects.setInputType(InputType.TYPE_NULL);
        todate_selects.setFocusable(false);

        todate_selects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Set the minimum date as today, so past dates are disabled
                DatePickerDialog dpd = new DatePickerDialog(RescheduleActivityForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
                        // new GetData().execute(supervisorList.get(supervisor.getSelectedItemPosition()).getCode(),todate_selects.getText().toString());
                    }
                }, mYear, mMonth, mDay);

                // Set the minimum date to today
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }
        });


        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finalValidation();

                } catch (Exception e) {

                }
            }
        });

        //--------------------------------------------------------------------------------------

        //new RaCircleWithZoneReport.TargetList().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),currentDate,currentDate);


    }

    public void getVillage() {
        List<VillageModal> villageModelListTemp = new ArrayList<>();
        villageModelListTemp = dbh.getVillageModal("");

        ArrayList<String> data = new ArrayList<String>();
        data.add(" - Select Village - ");

        for (int i = 0; i < villageModelListTemp.size(); i++) {
            data.add(villageModelListTemp.get(i).getCode() + " - " + villageModelListTemp.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        selectVillage_Spinner.setAdapter(adaptersupply);
        List<VillageModal> finalVillageModelListTemp = villageModelListTemp;
        selectVillage_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    SelectedVillageCode = finalVillageModelListTemp.get(i - 1).getCode();

                    if (SelectedVillageCode.equals(VillageCode)) {
                        new AlertDialogManager().showToast((Activity) context, "This Village is Already Assigned");
                        selectVillage_Spinner.setSelection(0);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void getSpinner() {
        targetList = dbh.getMasterDropDown("22");
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < targetList.size(); i++) {
            data.add(targetList.get(i).getName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        target_dd.setAdapter(adaptersupply);
        target_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (new InternetCheck(context).isOnline()) {
                    // new TargetList().execute(targetList.get(target_dd.getSelectedItemPosition()).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
                } else {
                    new AlertDialogManager().RedDialog(context, "No internet found");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    class ReasonList extends AsyncTask<String, Void, String> {

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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/ReScheduleReason";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
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
                JSONObject jsonObject1 = new JSONObject(Content);
                JSONArray jsonArray = jsonObject1.getJSONArray("DATA");
                villageShowCode = jsonArray.getJSONObject(1).getString("RR_VILLDISPLAY");
                if (dialog.isShowing())
                    dialog.dismiss();
                ArrayList<String> data1 = new ArrayList<String>();
                data1.add(" - Select Reason - ");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    data1.add(jsonObject.getString("CODE") + " - " + jsonObject.getString("NAME"));
                }
                ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                        R.layout.list_item, data1);
                target_dd.setAdapter(adaptersupply);
                target_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (position != 0) {
                                JSONObject selectedObject = jsonArray.getJSONObject(position - 1); // Subtract 1 to account for the "- Select Village -" option
                                selectedCode = selectedObject.getString("CODE");
                            } else {
                                selectedCode = "";
                            }
                            if (position == 2) {
                                if (villageShowCode.equalsIgnoreCase(1 + "")) {
                                    getVillage();
                                    textInputSelctVillage.setVisibility(View.VISIBLE);
                                } else {

                                }

                            } else {
                                textInputSelctVillage.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {

                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            } catch (Exception e) {

                new AlertDialogManager().RedDialog(context, Content);

            }
        }
    }

    class SaveRescheduleRequest extends AsyncTask<String, Void, String> {

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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/saveRescheduleRequest";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("SUP", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("VSD_FROM", currentDate));
                entity.add(new BasicNameValuePair("RSD_FROM", todate_selects.getText().toString()));
                entity.add(new BasicNameValuePair("REASON", selectedCode));
                entity.add(new BasicNameValuePair("DESC", desription_editText.getText().toString()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                if (selectVillage_Spinner.getSelectedItemPosition()==0) {
                    SelectedVillageCode = "0";
                }
                entity.add(new BasicNameValuePair("VillCode", SelectedVillageCode));

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
                JSONObject jsonObject1 = new JSONObject(Content);
                if (dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().GreenDialog(context, jsonObject1.getString("MSG").toString());
                ResfreshData();

            } catch (Exception e) {

                new AlertDialogManager().RedDialog(context, Content);

            }
        }
    }

    public void finalValidation() {

        if (todate_selects.getText().toString().isEmpty()) {
            new AlertDialogManager().showToast((Activity) context, "Please select a Reshedule Date");
        } else if (selectedCode.isEmpty()) {
            new AlertDialogManager().showToast((Activity) context, "Please select a valid reason.");
        } else if (todate_selects.getText().toString() == currentDate) {
            new AlertDialogManager().showToast((Activity) context, "Please select a Diffrent Reschedule Date");
        } else if (target_dd.getSelectedItemPosition() == 2) { // Check if the selected position is 2// || villageShowCode.equalsIgnoreCase(1 + "")
            if (selectVillage_Spinner.getSelectedItemPosition() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please select a Village for the target.");
            } else if (desription_editText.getText().toString().isEmpty()) {
                new AlertDialogManager().showToast((Activity) context, "Please select a Desciption reason.");
            } else {
                // new AlertDialogManager().GreenDialog(context, "Data Save Successfully");
                new SaveRescheduleRequest().execute();
            }
        } else if (desription_editText.getText().toString().isEmpty()) {
            new AlertDialogManager().showToast((Activity) context, "Please select a Desciption reason.");
        } else {
            // new AlertDialogManager().GreenDialog(context, "Data Save Succesfully");
            new SaveRescheduleRequest().execute();
        }

    }

    void ResfreshData(){
        todate_selects.setText("");
        desription_editText.setText("");
        target_dd.setSelection(0);
        selectVillage_Spinner.setSelection(0);


    }


}