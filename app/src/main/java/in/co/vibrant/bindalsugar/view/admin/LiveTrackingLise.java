package in.co.vibrant.bindalsugar.view.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.LivetrackingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class LiveTrackingLise extends AppCompatActivity {

    Context context;
    SQLiteDatabase db;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModelList;
    List<LivetrackingModel> livetrackingModels;
    ImageView all_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livetrackinglist);

        context = LiveTrackingLise.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModelList = dbh.getUserDetailsModel();
        livetrackingModels = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Live Tracking List");
        all_location = findViewById(R.id.all_location);
        toolbar.setTitle("Live Tracking List");
        //swipeRefreshLayout = findViewById(R.id.refreshLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(context,.class);
                //startActivity(intent);
                finish();
            }
        });


        new GetLiveTracking().execute();
        //swipeRefreshLayout.setRefreshing(false);


        //------------------OPEN ALL LOCATION-----------------------------
        all_location = findViewById(R.id.all_location);
        all_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, AllLIiveLocations.class);
                context.startActivity(intent);
            }
        });
        //-----------------------------------------------------------------------


    }


    private class GetLiveTracking extends AsyncTask<String, Integer, Void> {
        String message;
        String Content;
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
            Log.d("Request Divn :",""+userDetailsModelList.get(0).getDivision());
            Log.d("Request Seas :",""+new SessionConfig(context).getSeason());
            try {
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/EmpLiveAnd";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("EMPCODE", ""+userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("Divn", ""+userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("Seas", ""+new SessionConfig(context).getSeason()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                // for Header and Authothorization in Basic Auth With user Name And Password
                httpPost.setHeader("X-ApiKey", "LsTrackingVib@1234");
                httpPost.setHeader("Authorization", "Basic " + Base64.encodeToString("LsDemo@45:LsAdmin@123".getBytes(), Base64.NO_WRAP));
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


                JSONObject jsonObject = new JSONObject(message);
                Log.d("Message", message.toString());
                if (jsonObject.getString("APISTATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonUserDetailsObject = jsonObject.getJSONArray("DATA");
                    for (int i = 0; i < jsonUserDetailsObject.length(); i++) {
                        JSONObject jsonObject1 = jsonUserDetailsObject.getJSONObject(i);
                        LivetrackingModel data = new LivetrackingModel();
                        data.setIMEI(jsonObject1.getString("IMEI"));
                        data.setLAT(jsonObject1.getString("LAT"));
                        data.setLNG(jsonObject1.getString("LNG"));
                        data.setUSERCODE(jsonObject1.getString("USERCODE"));
                        data.setNAME(jsonObject1.getString("NAME"));
                        data.setSPEED(jsonObject1.getString("SPEED"));
                        data.setADDRESS(jsonObject1.getString("ADDRESS"));
                        data.setLAST_ADDRESS(jsonObject1.getString("LASTADDRESS"));
                        //data.setCORDINATEDATE(jsonObject1.getString("LASTADDRESS"));
                        data.setACCURACY(jsonObject1.getString("ACCURACY"));
                        data.setAPPVERSION(jsonObject1.getString("APPVERSION"));
                        data.setBATTERY(jsonObject1.getInt("BATTERY"));
                        data.setBEARING(jsonObject1.getString("BEARING"));
                        data.setCHARGING(jsonObject1.getString("CHARGING"));
                        data.setCREATEDAT(jsonObject1.getString("CREATEDAT"));
                        data.setGPSSTATUS(jsonObject1.getString("GPSSTATUS"));
                        data.setINTERNETSTATUS(jsonObject1.getString("INTERNETSTATUS"));
                        data.setPROVIDER(jsonObject1.getString("PROVIDER"));
                        livetrackingModels.add(data);
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    LiveTrackingAdapter stockSummeryAdapter = new LiveTrackingAdapter(context, livetrackingModels);
                    recyclerView.setAdapter(stockSummeryAdapter);
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            } catch (JSONException e) {

                new AlertDialogManager().RedDialog(context, Content);
            } catch (Exception e) {


                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context, "Error :- " + e.getClass().getName() + " - " + e.getMessage());
            }


        }
    }
}

