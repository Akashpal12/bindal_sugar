package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.PurchyAdapter;
import in.co.vibrant.bindalsugar.model.PurchyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class PurchyDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    DBHelper dbh;
    SQLiteDatabase db;
    List<PurchyModel> purchyModels;
    List<UserDetailsModel> userDetailsModels;
    Toolbar toolbar;
    Context context;
    AlertDialog dialog;
    String currentDate="";
    List<VillageModal> villageModelList;
    List<VillageModal> villageModelListTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchylist);


        context = PurchyDetails.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        purchyModels = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        villageModelList=new ArrayList<>();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.purchy_list));
        toolbar.setTitle(getString(R.string.purchy_list));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new GetPurchyList().execute();
        }


        //-----------------------Filter the recyclerView list---------------------------------------
        public void openFilter(View view) {

            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.purchyfilter, null);
            dialogbilder.setView(mView);

            final EditText date = mView.findViewById(R.id.date);

            //-------------------------------------get village----------------------------
            final Spinner villege = mView.findViewById(R.id.village);
            villageModelListTemp= dbh.getVillageModal("");

            ArrayList<String> data = new ArrayList<String>();
            data.add(" - All Village - ");

            for (int i = 0; i < villageModelListTemp.size(); i++) {
                data.add(villageModelListTemp.get(i).getCode()+" - "+villageModelListTemp.get(i).getName());
            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            villege.setAdapter(adaptersupply);
            villege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        // getGrower("");
                    } else {
                        //getGrower(villageModelList.get(village.getSelectedItemPosition()-1).getCode());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //------------------------------------end-------------------------------------
            //villege.setText(userDetailsModels.get(0).getCode());

            date.setText(currentDate);
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
                    DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                            String yr = "" + year;
                            //yr=yr.substring(2);
                            currentDate = year + "-" + temmonth + "-" + temDate;
                            date.setText(year + "-" + temmonth + "-" + temDate);
                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });

            dialog = dialogbilder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button search = mView.findViewById(R.id.search);
            ImageView close = mView.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //--------------------------------filter the data------------------------------------
                        try {
                            String villCode="";
                            if(villege.getSelectedItemPosition()==0)
                            {
                                villCode="";
                            }
                            else{
                                villCode=villege.getSelectedItem().toString().split(" - ")[0];
                            }
                            //---------------filter the list date-----------------------------------------------------------
                            if(villCode.length()==0)
                            {
                                recyclerView = findViewById(R.id.recycler_view);
                                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(manager);
                                PurchyAdapter purchyAdapter = new PurchyAdapter(context, purchyModels);
                                recyclerView.setAdapter(purchyAdapter);
                            }
                            else{
                                if (purchyModels.size() > 0) {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        String finalVillCode = villCode;
                                        List<PurchyModel> result = purchyModels.stream()
                                                //.filter(item -> (item.getCode().toLowerCase().contains(villege.getSelectedItem().toString())))
                                                .filter(p -> p.getVillcode().equalsIgnoreCase(finalVillCode)||p.getVillcode().equalsIgnoreCase(currentDate)) //where condition implement
                                                .collect(Collectors.toList());
                                        recyclerView = findViewById(R.id.recycler_view);
                                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(manager);
                                        PurchyAdapter purchyAdapter = new PurchyAdapter(context, result);
                                        recyclerView.setAdapter(purchyAdapter);

                                    }
                                    else{
                                        recyclerView = findViewById(R.id.recycler_view);
                                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(manager);
                                        PurchyAdapter purchyAdapter = new PurchyAdapter(context, purchyModels);
                                        recyclerView.setAdapter(purchyAdapter);
                                    }
                                }
                            }

                            //------------------------------------------------------------------------------------------
                            dialog.dismiss();
                        } catch (Exception e) {

                        }
                        //--------------------------------------Filter the data---------------------
                    }
                });
            }


                    //------------------------------------------------------------------------------



        //--------------------call the service------------------------------------------------------
            private class GetPurchyList extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);


        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();
            //textView.setText("Verify details from server");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETISSUED";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("Supcode", userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);

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
            super.onPostExecute(result);

            try {
                purchyModels = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(message);
                if (jsonArray.isNull("".length())){
                    new AlertDialogManager().GreenDialog(context,"DATA NOT FOUND !!..");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PurchyModel purchyModel = new PurchyModel();
                        purchyModel.setPurchyno(jsonObject.getString("Purchyno"));
                        purchyModel.setMode(jsonObject.getString("mode"));
                        purchyModel.setCategory(jsonObject.getString("Category"));
                        purchyModel.setVillcode(jsonObject.getString("vill"));
                        purchyModel.setVillname(jsonObject.getString("vname"));
                        purchyModel.setGrower_code(jsonObject.getString("gro"));
                        purchyModel.setGrower_name(jsonObject.getString("groname"));
                        purchyModel.setGfather(jsonObject.getString("gfather"));

                        purchyModels.add(purchyModel);
                    }
                    recyclerView = findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    PurchyAdapter purchyAdapter = new PurchyAdapter(context, purchyModels);
                    recyclerView.setAdapter(purchyAdapter);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }




            } catch (JSONException e) {
                new AlertDialogManager().GreenDialog(context,"Error: "+e.toString());

            } catch (Exception e) {

                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());

            }
        }






    }
        //------------------------------------------------------------------------------------------



}
