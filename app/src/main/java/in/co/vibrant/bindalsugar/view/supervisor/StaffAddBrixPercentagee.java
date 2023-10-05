package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffAddBrixPercentagee extends AppCompatActivity {


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationManager locationManager;
    double Lat,Lng;
    String address="";

    String G_CODE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String V_CODE;
    String TAG="Brix Percent Save";
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    List<UserDetailsModel> loginUserDetailsList;
    EditText village_code,grower_name,BrixPurcent,plot_sr_no;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add_brix_percentagee);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffAddBrixPercentagee.this;
        setTitle(getString(R.string.MENU_BrixPurcent));
        toolbar. setTitle(getString(R.string.MENU_BrixPurcent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbh=new DBHelper(context);
        loginUserDetailsList=dbh.getUserDetailsModel();
        V_CODE = getIntent().getExtras().getString("V_CODE");
        G_CODE = getIntent().getExtras().getString("G_CODE");
        PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
        PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
        G_NAME = getIntent().getExtras().getString("G_NAME");

        //Lat=getIntent().getExtras().getString("lat");
        //Lng=getIntent().getExtras().getString("lng");


        village_code=findViewById(R.id.village_code);
        grower_name=findViewById(R.id.grower_name);
        plot_sr_no=findViewById(R.id.plot_sr_no);

        BrixPurcent=findViewById(R.id.BrixPurcent);

        village_code.setText(V_CODE);
        grower_name.setText(G_CODE+" / "+G_NAME);
        plot_sr_no.setText(PLOT_SR_NO);
        findLocation();

    }

    protected void findLocation() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //checkUpdate();
                }
            });
            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            new AlertDialogManager().AlertPopUpFinish(context,"Security Error: please enable gps service");
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity) context,
                                    1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location = locationResult.getLastLocation();
                            if(location.isFromMockProvider())
                            {
                                new AlertDialogManager().showToast((Activity) context,"Security Error : you can not use this application because we detected fake GPS ?");
                            }
                            else
                            {
                                try {
                                    List<Address> addresses=new ArrayList<>();
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    Geocoder geocoder;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                                    address=addresses.get(0).getAddressLine(0);
                                }
                                catch(SecurityException se)
                                {
                                    address="Error : Security error";
                                    new AlertDialogManager().AlertPopUpFinish(context,"Security Error:"+se.toString());
                                }
                                catch(Exception se)
                                {
                                    new AlertDialogManager().AlertPopUpFinish(context,"Error:"+se.toString());
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Security Error:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().AlertPopUpFinish(context,"Error:"+se.toString());
        }

    }

    public void ExitBtn(View v)
    {
        finish();
    }



    public void saveData(View v)
    {
        checkValidation : {


            if(BrixPurcent.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average BrixPurcent of cane (expected)");
                break checkValidation;
            }
            new saveData().execute();

       /*  String[] cropConditionArray=input_type_of_planting.getSelectedItem().toString().split(" - ");
            new SaveLog().execute(cropConditionArray[0]);*/
            //finish();
        }
    }

    private class saveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVECANEBRIX);
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("DIVN", loginUserDetailsList.get(0).getDivision());
                request1.addProperty("SUPCODE", loginUserDetailsList.get(0).getCode());
                request1.addProperty("PLOTVILL", PLOT_VILL);
                request1.addProperty("PLOTNO", PLOT_SR_NO);
                request1.addProperty("VILLCODE", V_CODE);
                request1.addProperty("GROWERCODE", G_CODE);
                request1.addProperty("BRIXPURC", BrixPurcent.getText().toString());
                request1.addProperty("ADDRESS", address);
                request1.addProperty("LAT", "" +Lat);
                request1.addProperty("LON", "" +Lng);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVECANEBRIX, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVECANEBRIXResult").toString();
                return null;
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            } catch (Exception e2) {
                Log.e("Exception", e2.getMessage());
                message = e2.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }




}