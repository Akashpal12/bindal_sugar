package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;


public class LiveTrackingActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
   // List<TruckDetails> truckDetailsList;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    LocationRequest locationRequest;
    //List<TransportDetails> transportDetailsList;
    //List<FactoryModel> factoryModelListSpinner;
    Spinner spinnerFactory;
    TextView last_update,speed,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_tracking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context= LiveTrackingActivity.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
       // transportDetailsList=dbh.getTransportDetails();

        setTitle(getString(R.string.MENU_TRUCK_LIVE));
        toolbar.setTitle(getString(R.string.MENU_TRUCK_LIVE));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) context);
        //startLocationUpdates(); last_update,speed,address;

        last_update=findViewById(R.id.last_update);
        speed=findViewById(R.id.speed);
        address=findViewById(R.id.address);
      //  factoryModelListSpinner=new ArrayList<>();
        spinnerFactory=findViewById(R.id.factory_list);

      //  new GetTruckList().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory());
        //new GetLiveLocation().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory());
    }

    /*public void refreshData(View v)
    {
        new GetLiveLocation().execute(transportDetailsList.get(0).getTransporterCode(),factoryModelListSpinner.get(spinnerFactory.getSelectedItemPosition()).getFactoryCode());
    }

    private class GetTruckList extends AsyncTask<String, Void, Void> {
        String message;
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
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTransporterFactoryToView);
                request1.addProperty("trans_code", params[0]);
                request1.addProperty("tr_factory", params[1]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTransporterFactoryToView, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTransporterFactoryToViewResult").toString();
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
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                ArrayList<String> datasupply=new ArrayList<String>();
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"No factory mapping");
                    }
                    else
                    {
                        factoryModelListSpinner.clear();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject obj=jsonArray.getJSONObject(i);
                            if(obj.getString("ISACTIVE").equalsIgnoreCase("ACTIVE"))
                            {
                                FactoryModel factoryModel=new FactoryModel();
                                factoryModel.setFactoryCode(obj.getString("F_CODE"));
                                factoryModel.setFactoryName(obj.getString("F_NAME"));
                                datasupply.add(obj.getString("F_NAME"));
                                factoryModelListSpinner.add(factoryModel);
                            }
                        }
                        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                                R.layout.list_item, datasupply);
                        spinnerFactory.setAdapter(adaptersupply);
                        spinnerFactory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                new GetLiveLocation().execute(transportDetailsList.get(0).getTransporterCode(),factoryModelListSpinner.get(spinnerFactory.getSelectedItemPosition()).getFactoryCode());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }
                else if(jsonObject.getString("API_STATUS").equalsIgnoreCase("NOTEXISTS"))
                {
                    truckDetailsList.clear();
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

    protected void startLocationUpdates() {

        try {
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);

            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            DecimalFormat decimalFormat=new DecimalFormat("##");
                            Location location = locationResult.getLastLocation();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location
                                            .getLongitude()), 16.0f));
                        }
                    },
                    Looper.myLooper());
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
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                mMap.setBuildingsEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.setMyLocationEnabled(true);
                                GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
                                //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.style_night_map));
                                UiSettings uiSettings = mMap.getUiSettings();
                                uiSettings.setMyLocationButtonEnabled(true);
                                uiSettings.setAllGesturesEnabled(true);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location
                                                .getLongitude()), 16.0f));
                            }
                        }
                    });
        }
        catch(SecurityException ex)
        {

        }
    }

    private class GetLiveLocation extends AsyncTask<String, Void, Void> {
        String message;
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
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTruckLocation);
                request1.addProperty("trans_code", params[0]);
                request1.addProperty("f_code", params[1]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTruckLocation, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTruckLocationResult").toString();
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
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    mMap.clear();
                    setMarkerOnmap(jsonArray);
                    *//*for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        LatLng latlng = new LatLng(object.getDouble("LAT"), object.getDouble("LNG"));
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(object.getDouble("LAT"), object.getDouble("LNG")), 16.0f));
                        *//**//*Marker melbourne = mMap.addMarker(
                                new MarkerOptions()
                                        .position(latlng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*//**//*
                        //String title = object.getString("TR_NUMBER");
                        //String subTitle = "SPEED :"+object.getString("TR_NUMBER")+"\nLast Update:"+object.getString("CORDINATEDATE")+"\nAddress:"+object.getString("ADDRESS");
                        *//**//*MarkerOptions markerOpt = new MarkerOptions();
                        markerOpt.position(new LatLng(Double.valueOf(object.getDouble("LAT")), Double.valueOf(object.getDouble("LNG"))))
                                .title(title)
                                .snippet(subTitle)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));*//**//*

                        *//**//*Marker melbourne = mMap.addMarker(
                                new MarkerOptions()
                                        .position(latlng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*//**//*

                        //melbourne.setTag(""+i);

                        //Set Custom InfoWindow Adapter
                        //CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(LiveTrackingActivity.this);
                        //mMap.setInfoWindowAdapter(adapter);

                        //mMap.addMarker(melbourne);
                    }*//*

                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

    public void setMarkerOnmap(final JSONArray jsonArray)
    {
        try {
            mMap.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                //final LatLng melbourneLatLng = new LatLng(model.getLat(), model.getLng());
                Marker melbourne = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(jsonObject1.getDouble("LAT"), jsonObject1.getDouble("LNG")))
                                .title(""+i));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(jsonObject1.getDouble("LAT"), jsonObject1.getDouble("LNG")), 14.0f));
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        LinearLayout info = new LinearLayout(context);
                        try {
                            info.setOrientation(LinearLayout.VERTICAL);
                            //JSONObject jsonObject11=jsonArray.getJSONObject(Integer.parseInt(marker.getTitle()));
                            //LiveTrackingModel liveTrackingModel = liveTrackingModels.get(Integer.parseInt(marker.getTitle()));
                            TextView title = new TextView(context);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.LEFT);
                            //title.setTypeface(null, Typeface.BOLD);
                            title.setTextSize(10);
                            title.setText(
                                    "Truck Number : " + jsonObject1.getString("TR_NUMBER") + "\n" +
                                    "SPEED : " + jsonObject1.getString("SPEED") + " Km/Hr\n" +
                                    "Last Location: " + jsonObject1.getString("CORDINATEDATE")  + "\n" +
                                    "Last Address: " + jsonObject1.getString("ADDRESS").substring(0, 50) + "\n" +
                                    "Battery : " + jsonObject1.getString("BATTERYPERCENT") + "% " + jsonObject1.getString("CHARGERSTATUS")
                            );
                            TextView snippet = new TextView(context);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());
                            info.addView(title);
                            //info.addView(snippet);
                        } catch (Exception e) {

                        }
                        return info;
                    }
                });
            }
        }
        catch (Exception e)
        {

        }
    }


    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private Activity context;

        public CustomInfoWindowAdapter(Activity context){
            this.context = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = context.getLayoutInflater().inflate(R.layout.map_info_window, null);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);

            tvTitle.setText(marker.getTitle());
            tvSubTitle.setText(marker.getSnippet());

            return view;
        }
    }
*/
}
