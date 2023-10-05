package in.co.vibrant.bindalsugar.view.supervisor.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerFinder;

public class StaffSurveyMapView extends AppCompatActivity implements OnMapReadyCallback {

    DBHelper dbh;
    SQLiteDatabase db;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;
    double lat=0,lng=0,accuracy;
    Geocoder geocoder;
    TextView txtLat,txtLng,txtAccuracy,txtAddress;
    GoogleMap mMap;
    Context context;
    List<PlotFarmerShareModel> plotFarmerShareModelList;
    List<UserDetailsModel> userDetailsModels;
    String message;
    JSONArray plotFarmerShareArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_survey_map_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_NEARBY_PLOT));
        toolbar.setTitle(getString(R.string.MENU_NEARBY_PLOT));
        context= StaffSurveyMapView.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button resendData=findViewById(R.id.resendData);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=dbh.getUserDetailsModel();
        plotFarmerShareModelList=new ArrayList<>();
        plotFarmerShareArray=new JSONArray();
        try {
            //lat=29.3934217;
            //lng=78.16364;
            txtLat=findViewById(R.id.txt_lat);
            txtLng=findViewById(R.id.txt_lng);
            txtAccuracy=findViewById(R.id.txt_accuracy);
            txtAddress=findViewById(R.id.txt_address);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(StaffSurveyMapView.this);
            //fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
            startLocationUpdates();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error 2:"+e.toString());
        }

    }

    protected void startLocationUpdates() {

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
                            try {
                                Location location = locationResult.getLastLocation();
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                accuracy = location.getAccuracy();
                                if(APIUrl.isDebug)
                                {
                                    lat=APIUrl.lat;
                                    lng=APIUrl.lng;
                                }
                                final List<Address> addresses;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                String address = "";
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                txtLat.setText(""+lat);
                                txtLng.setText(""+lng);
                                txtAccuracy.setText(""+new DecimalFormat("0.00").format(accuracy) +"  ("+plotFarmerShareModelList.size()+" Plot Find)");
                                txtAddress.setText(""+addresses.get(0).getAddressLine(0));
                                LatLng latLng=new LatLng(lat,lng);
                                MarkerOptions markerOpt = new MarkerOptions();
                                markerOpt.title(userDetailsModels.get(0).getName());
                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_man);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                markerOpt.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                mMap.clear();
                                if(plotFarmerShareArray.length()>0)
                                    drawPlot(plotFarmerShareArray);
                                mMap.addMarker(markerOpt);//.showInfoWindow();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch(SecurityException se)
        {
            new AlertDialogManager().RedDialog(this,"Security Error 3:"+se.toString());
        }
        catch(Exception se)
        {
            new AlertDialogManager().RedDialog(this,"Error 4:"+se.toString());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mMap.setBuildingsEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.setMyLocationEnabled(true);
                                GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ReportMap.this, R.raw.style_night_map));
                                UiSettings uiSettings = mMap.getUiSettings();
                                uiSettings.setMyLocationButtonEnabled(true);
                                uiSettings.setAllGesturesEnabled(true);
                                new GetMapData().execute();
                            }
                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    public void openRefreshPlot(View v)
    {
        new GetMapData().execute();
    }


    public void opensurveymapview(View v)
    {
        Intent intent = new Intent(context, StaffGrowerFinder.class);
        intent.putExtra("lat", "" + lat);
        intent.putExtra("lng", "" + lng);
        startActivity(intent);
    }


    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }

    private class GetMapData extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GETNEARESTPLOTSBYLATLNG);
                request1.addProperty("Divn", ""+userDetailsModels.get(0).getDivision());
                request1.addProperty("User", ""+userDetailsModels.get(0).getCode());
                request1.addProperty("lat", ""+lat);
                request1.addProperty("lng", ""+lng);
                Log.d("", "doInBackground" + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GETNEARESTPLOTSBYLATLNG, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GETNEARESTPLOTSBYLATLNGResult").toString();
                }

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //dialog.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            dialog.dismiss();
            try{
                JSONObject jsonObject1=new JSONObject(message);
                if(jsonObject1.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    plotFarmerShareArray =jsonObject1.getJSONArray("DATA");
                    drawPlot(plotFarmerShareArray);
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject1.getString("MSG"));
                }
            }
            catch (JSONException e)
            {
                new AlertDialogManager().AlertPopUpFinish(context,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(context,"Error 5:"+e.toString());
            }
            //drawPlot(message);
        }
    }

    public void drawPlot(JSONArray plotFarmerShareArray) throws JSONException {
        if(plotFarmerShareArray.length()>0)
        {
            for (int i = 0; i < plotFarmerShareArray.length(); i++) {
                JSONObject jsonObject=plotFarmerShareArray.getJSONObject(i);
                ArrayList<LatLng> latLngs=new ArrayList<>();

                String color="#6AFBED6A";//Yellow
                if(jsonObject.getString("COLORCODE").equalsIgnoreCase("0"))
                {
                    color="#6AFBED6A";//Yellow
                }
                else
                {
                    color=jsonObject.getString("COLORCODE").toUpperCase();
                }
                PolygonOptions polygonOptions=new PolygonOptions();
                if(jsonObject.getDouble("LAT1")>0)
                {
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                }
                if(jsonObject.getDouble("LAT3")>0){
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                }
                if(jsonObject.getDouble("LAT4")>0){
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                }
                if(jsonObject.getDouble("LAT2")>0)
                {
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));
                }


                polygonOptions.strokeColor(Color.parseColor("#000000"));
                polygonOptions.strokeWidth(2);
                polygonOptions.fillColor(Color.parseColor(color));

                mMap.addPolygon(polygonOptions);

                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(StaffSurveyMapView.this);
                mMap.setInfoWindowAdapter(adapter);
                MarkerOptions markerOpt = new MarkerOptions();
                LatLng midPoint=getPolygonCenterPoint(latLngs);
                markerOpt.position(midPoint)
                        .title(jsonObject.getString("GROWERCODE")+" - "+jsonObject.getString("GROWERNAME"))
                        .snippet("Father  :"+jsonObject.getString("GROWERFATHERNAMEE")+"\n"+
                                "Area     :"+jsonObject.getString("AREAHECTARE")+"\n"+
                                "Area %   :"+jsonObject.getString("SHARES")+"\n"+
                                "Variety  :"+jsonObject.getString("VARIETY")+"\n"+
                                "Cane Type:"+jsonObject.getString("CANETYPE")+"\n"+
                                "Land Type:"+jsonObject.getString("LT_NAME")+"\n"+
                                "Date :"+jsonObject.getString("GH_ENT_DATE")+"\n"+
                                "Supervisor :"+jsonObject.getString("U_NAME")+"\n"+
                                "E: "+jsonObject.getString("DISTANCE1")+"   S: "+jsonObject.getString("DISTANCE3")+
                                "   W: "+jsonObject.getString("DISTANCE2")+"   N: "+jsonObject.getString("DISTANCE4")
                        )
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green));
                mMap.addMarker(markerOpt);//.showInfoWindow();
            }
            //LatLng latLng=new LatLng(lat,lng);
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
        }
    }

}