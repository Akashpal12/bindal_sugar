package in.co.vibrant.bindalsugar.view.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

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
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class AllLIiveLocations extends AppCompatActivity {
    private static final int BOUND_PADDING = 2000;
    private GoogleMap googleMap;
    SupportMapFragment mapFragment;
    Marker marker;
    private boolean isMarkerRotating = false;
    ArrayList<LatLng> listOfPoints = new ArrayList<>();
    int currentPt = 0;
    LatLng finalPosition;
    Marker mMarker;
    Marker mMarker2;
    Polyline polyline;

    Context context;
    DBHelper dbh;
    SQLiteDatabase db;
    AlertDialog dialog;
    String currentDate="",fromtimes="",totimes="";
    int mHour;
    int mMinute;
    List<UserDetailsModel> userDetailsModelList;
    List<LivetrackingModel> livetrackingModels;
    EditText fromtime;
    EditText totime;
    EditText date;
    String USERCODE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.alllive_location);
        context=this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModelList = dbh.getUserDetailsModel();
        livetrackingModels = new ArrayList<>();
        setTitle("All Live Locations List");

        //--------------BACK BUTTON----------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, Dashboard.class);
//                startActivity(intent);
                finish();
                onBackPressed();

            }
        });
        //-----------------------------------------------------------

        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
        }





        setUpMapIfNeeded();
        new GetLiveTracking().execute();




    }


    private void setUpMapIfNeeded() {
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        loadMap(googleMap);
                    }
                });
            }
        }
    }

    private void loadMap(GoogleMap map) {
        googleMap = map;

     /*   mMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(26.87438040, 81.02323100)).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon)));
        mMarker2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(26.87438045,81.02323110)).icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));

*/
        final Handler handler = new Handler();
        //Code to move car along static latitude and longitude

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentPt < listOfPoints.size()) {
                    //post again
                    Log.d("tess", "inside run ");
                    Location targetLocation = new Location(LocationManager.GPS_PROVIDER);

                    targetLocation.setLatitude(listOfPoints.get(0).latitude);
                    targetLocation.setLongitude(listOfPoints.get(0).longitude);
                    handler.postDelayed(this, 3000);
                    currentPt++;



                } else {
                    Log.d("tess", "call back removed");
                    //removed callbacks
                    handler.removeCallbacks(this);
                }
            }
        }, 3000);



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
            try {

                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/EmpLiveAnd";
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("EMPCODE", ""+userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("Divn", ""+userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("Seas", ""+new SessionConfig(context).getSeason()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

                // for Header and Authothorizationin  Basic Auth With user Name And Password
                httpPost.setHeader("X-ApiKey", "LsTrackingVib@1234");
                httpPost.setHeader("Authorization", "Basic " + Base64.encodeToString("LsDemo@45:LsAdmin@123".getBytes(), Base64.NO_WRAP));

                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);

//                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl. method_EmpLiveAnd);
//
//
//
//                request1.addProperty("Factory", userDetailsModelList.get(0).getFactoryName());
//                request1.addProperty("EmpID", userDetailsModelList.get(0).getEmpID());
//
//                request1.addProperty("lang", getString(R.string.language));
//                Log.d("", "doInBackground: " + request1);
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//                envelope.dotNet = true;
//                envelope.setOutputSoapObject(request1);
//                envelope.implicitTypes = true;
//                // Web method call
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.WEIGHTMENT_URL, 200000);
//
//                androidHttpTransport.debug = true;
//                androidHttpTransport.call(APIUrl.SOAP_ACTION_EmpLiveAnd, envelope);
//                if (envelope.bodyIn instanceof SoapFault) {
//                    SoapFault sf = (SoapFault) envelope.bodyIn;
//                    message = sf.getMessage();
//                } else {
//                    SoapObject result = (SoapObject) envelope.bodyIn;
//                    message = result.getPropertyAsString("EmpLiveAndResult").toString();
//                }

            } catch (SecurityException e) {


                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
            // Jav
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(message);
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
                        data.setCORDINATEDATE(jsonObject1.getString("CORDINATEDATE"));
                        data.setACCURACY(jsonObject1.getString("ACCURACY"));
                        data.setAPPVERSION(jsonObject1.getString("APPVERSION"));
                        data.setBATTERY(jsonObject1.getInt("BATTERY"));
                        data.setBEARING(jsonObject1.getString("BEARING"));
                        data.setCHARGING(jsonObject1.getString("CHARGING"));
                        data.setCREATEDAT(jsonObject1.getString("CREATEDAT"));
                        data.setGPSSTATUS(jsonObject1.getString("GPSSTATUS"));
                        data.setINTERNETSTATUS(jsonObject1.getString("INTERNETSTATUS"));
                        data.setPROVIDER(jsonObject1.getString("PROVIDER"));

                        listOfPoints.add(new LatLng(jsonObject1.getDouble("LAT"), jsonObject1.getDouble("LNG")));
                        final Double currLat = jsonObject1.getDouble("LAT");
                        final Double currLong = jsonObject1.getDouble("LNG");
                        final LatLng hcmus = new LatLng(currLat, currLong);

                        List<LatLng> latList = new ArrayList<LatLng>();
                        latList.add(hcmus);

                        for (int j = 0; j < latList.size(); j++) {
                            final LatLng position = new LatLng(latList.get(j).latitude, latList.get(j).longitude);

                            marker = googleMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon))
                                    .title("Address : " + jsonObject1.getString("ADDRESS")+"                    ")
                                    .snippet("Name : "+jsonObject1.getString("NAME")+ "    Speed : " + jsonObject1.getString("SPEED")+" Kms/Hour"+"                    ")
                                    .position(position));

                           googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                    .target(position)
                                    .zoom(2.5f)
                                    .build()));

                        }

                        livetrackingModels.add(data);
                    }

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