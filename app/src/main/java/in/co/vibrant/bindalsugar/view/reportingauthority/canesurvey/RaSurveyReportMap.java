package in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;


public class RaSurveyReportMap extends AppCompatActivity implements OnMapReadyCallback {

    //Permissions
    DBHelper dbh;
    SQLiteDatabase db;
    String villageCode,growerCode,landType,borderTree,method;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    double lat,lng;
    GoogleMap mMap;
    List<PlotFarmerShareModel> plotFarmerShareModelList;
    List<UserDetailsModel> userDetailsModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.map_plot);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Map View");
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
            userDetailsModelList=dbh.getUserDetailsModel();
            villageCode = getIntent().getExtras().getString("village_code");
            landType = getIntent().getExtras().getString("land_type");
            growerCode = getIntent().getExtras().getString("tv_grower");
            borderTree = getIntent().getExtras().getString("border_tree");
            method = getIntent().getExtras().getString("method");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(RaSurveyReportMap.this);
            fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location
                                        .getLongitude()), 15.0f));
                    }
                }

                ;
            };
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
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
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location
                                                .getLongitude()), 16.0f));
                                //drawPlot();
                                new GetMapData().execute();
                            }
                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    private class GetMapData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(RaSurveyReportMap.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaSurveyReportMap.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GETMAPDATA_NEW1);
                request1.addProperty("Divn", userDetailsModelList.get(0).getDivision());
                request1.addProperty("User", userDetailsModelList.get(0).getCode());
                request1.addProperty("VillageCode", villageCode);
                request1.addProperty("GrowerCode", growerCode);
                request1.addProperty("LANDTYPE", landType);
                request1.addProperty("BORDERTREE", borderTree);
                request1.addProperty("SHOWINGTYPE", method);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GETMAPDATA_NEW1, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GETMAPDATA_NEW1Result").toString();
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
                JSONArray plotFarmerShareArray =new JSONArray(message);
                if(plotFarmerShareArray.length()>0)
                {
                    for (int i = 0; i < plotFarmerShareArray.length(); i++) {
                        JSONObject jsonObject=plotFarmerShareArray.getJSONObject(i);
                        lat = jsonObject.getDouble("LAT1");
                        lng = jsonObject.getDouble("LNG1");
                        ArrayList<LatLng> latLngs=new ArrayList<>();
                        latLngs.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                        latLngs.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));
                        latLngs.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                        latLngs.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                        LatLng midPoint=getPolygonCenterPoint(latLngs);
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
                        polygonOptions.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                        if(jsonObject.getDouble("LAT3")>0)
                        polygonOptions.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                        if(jsonObject.getDouble("LAT4")>0)
                        polygonOptions.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                        if(jsonObject.getDouble("LAT2")>0)
                        polygonOptions.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));

                        polygonOptions.strokeColor(Color.parseColor("#000000"));
                        polygonOptions.strokeWidth(2);
                        polygonOptions.fillColor(Color.parseColor(color));

                        mMap.addPolygon(polygonOptions);

                        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(RaSurveyReportMap.this);
                        mMap.setInfoWindowAdapter(adapter);
                        MarkerOptions markerOpt = new MarkerOptions();
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
                        mMap.addMarker(markerOpt).showInfoWindow();
                    }
                    LatLng latLng=new LatLng(lat,lng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                }
            }
            catch (JSONException e)
            {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyReportMap.this,message);
                //AlertPopUpFinish(message);
            }catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyReportMap.this,"Error:"+e.toString());
            }
        }
    }

    private void drawPlot()
    {
        try {
            if (plotFarmerShareModelList.size() > 0) {

            } else {
                AlertPopUp("Plot not found");
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
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

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RaSurveyReportMap.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void AlertPopUpWithBack(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RaSurveyReportMap.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }


}
