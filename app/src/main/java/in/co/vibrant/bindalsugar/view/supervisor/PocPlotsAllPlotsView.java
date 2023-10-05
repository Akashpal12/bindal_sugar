package in.co.vibrant.bindalsugar.view.supervisor;

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
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.CustomInfoWindowAdapter;

public class PocPlotsAllPlotsView extends AppCompatActivity implements OnMapReadyCallback {
    DBHelper dbh;
    SQLiteDatabase db;
    LocationRequest locationRequest;
    double lat = 0, lng = 0, accuracy;
    Geocoder geocoder;
    String diseaseInfo;
    TextView txtLat, txtLng, txtAccuracy, txtAddress;
    GoogleMap mMap;
    Context context;
    String V_CODE, PLOT_TYPE_SELECTION;
    List<PlotFarmerShareModel> plotFarmerShareModelList;
    List<UserDetailsModel> userDetailsModels;
    String message;
    JSONArray plotFarmerShareArray;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Near By Plot View");
        context = PocPlotsAllPlotsView.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels = dbh.getUserDetailsModel();
        plotFarmerShareModelList = new ArrayList<>();
        plotFarmerShareArray = new JSONArray();
        V_CODE = getIntent().getStringExtra("V_CODE");
        PLOT_TYPE_SELECTION = getIntent().getStringExtra("PLOT_SELECTION");
        try {
            //lat=29.3934217;
            //lng=78.16364;
            txtLat = findViewById(R.id.txt_lat);
            txtLng = findViewById(R.id.txt_lng);
            txtAccuracy = findViewById(R.id.txt_accuracy);
            txtAddress = findViewById(R.id.txt_address);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(PocPlotsAllPlotsView.this);

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error :" + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_vertical_check_mapview);

        init();

        startLocationUpdates();

    }

    protected void startLocationUpdates() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // locationRequest.setInterval(1000);
            // locationRequest.setFastestInterval(500);

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
                                //lat=28.917547499999998;
                                //lng=78.3702595;
                                //lat=28.9170995;
                                //lng=78.3704799;
                                //lat=28.9462996;
                                //lng=78.2872065;
                                if (APIUrl.isDebug) {
                                    lat = APIUrl.lat;
                                    lng = APIUrl.lng;
                                }
                                accuracy = location.getAccuracy();

                                final List<Address> addresses;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                String address = "";
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                txtLat.setText("" + lat);
                                txtLng.setText("" + lng);

                                txtAccuracy.setText("" + new DecimalFormat("0.00").format(accuracy) + "  (" + plotFarmerShareModelList.size() + " Plot Find)");
                                txtAddress.setText("" + addresses.get(0).getAddressLine(0));
                                LatLng latLng = new LatLng(lat, lng);
                                MarkerOptions markerOpt = new MarkerOptions();
                                markerOpt.title(userDetailsModels.get(0).getName());
                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_man);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                markerOpt.position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                                mMap.clear();
                                if (plotFarmerShareArray.length() > 0)
                                    drawPlot(plotFarmerShareArray);
                                mMap.addMarker(markerOpt);//.showInfoWindow();
                            } catch (Exception e) {

                            }
                        }
                    },
                    Looper.myLooper());

        } catch (SecurityException se) {
            new AlertDialogManager().RedDialog(this, "Security Error 3:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().RedDialog(this, "Error 4:" + se.toString());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //  KMLParser.parseKML(String.valueOf(getResources().openRawResource(R.raw.allkmz)));
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
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
                           // kmlFileMap(googleMap);
                        }
                    });
        } catch (SecurityException ex) {

        }
    }

    public void openRefreshPlot(View v) {
        new GetMapData().execute();
        //  kmlFileMap(mMap);
    }

    public void openVeritalCheckmapview(View v) {
        Intent intent = new Intent(context, POCPlotFinderListMaster.class);
        intent.putExtra("lat", "" + lat);
        intent.putExtra("lng", "" + lng);
        //intent.putExtra("lat", "29.2086575");
        //intent.putExtra("lng", "78.21641149999999");
        startActivity(intent);
    }

    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList) {
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygonPointsList.size(); i++) {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng = bounds.getCenter();

        return centerLatLng;
    }

    public void drawPlot(JSONArray plotFarmerShareArray) throws JSONException {

        if (plotFarmerShareArray.length() > 0) {

            for (int i = 0; i < plotFarmerShareArray.length(); i++) {

                JSONObject jsonObject = plotFarmerShareArray.getJSONObject(i);
                ArrayList<LatLng> latLngs = new ArrayList<>();

                String color = "#6AFBED6A";//Yellow
                if (jsonObject.getString("COLORCODE").equalsIgnoreCase("0")) {
                    color = "#6AFBED6A";//Yellow
                } else {
                    color = jsonObject.getString("COLORCODE").toUpperCase();
                }

                PolygonOptions polygonOptions = new PolygonOptions();
                if (jsonObject.getDouble("LAT1") > 0) {
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT1"), jsonObject.getDouble("LNG1")));
                }
                if (jsonObject.getDouble("LAT3") > 0) {
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT3"), jsonObject.getDouble("LNG3")));
                }
                if (jsonObject.getDouble("LAT4") > 0) {
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT4"), jsonObject.getDouble("LNG4")));
                }
                if (jsonObject.getDouble("LAT2") > 0) {
                    polygonOptions.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));
                    latLngs.add(new LatLng(jsonObject.getDouble("LAT2"), jsonObject.getDouble("LNG2")));
                }

                polygonOptions.strokeColor(Color.parseColor("#000000"));//000000
                polygonOptions.strokeWidth(2);
                polygonOptions.fillColor(Color.parseColor(color));

                mMap.addPolygon(polygonOptions);


                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(PocPlotsAllPlotsView.this);
                mMap.setInfoWindowAdapter(adapter);

                MarkerOptions markerOpt = new MarkerOptions();
                LatLng midPoint = getPolygonCenterPoint(latLngs);
                markerOpt.position(midPoint)
                        .title(jsonObject.getString("GROWERCODE") + " - " + jsonObject.getString("GROWERNAME"))
                        .snippet(
                                "Plot Village :" + jsonObject.getString("PLOTVILLAGE") + " / " + jsonObject.getString("PLOTVILLNAME") + "\n" +
                                        "Plot Serial No : " + jsonObject.getString("PLOTSERIALNO") + "\n" +
                                        "Father  : " + jsonObject.getString("GROWERFATHERNAMEE") + "\n" +
                                        "Area     : " + jsonObject.getString("AREAHECTARE") + "\n" +
                                        "Area %   : " + jsonObject.getString("SHARES") + "\n" +
                                        "Variety  : " + jsonObject.getString("VARIETY") + "\n" +
                                        "Cane Type : " + jsonObject.getString("CANETYPE") + "\n" +
                                        "Land Type : " + jsonObject.getString("LT_NAME") + "\n" +
                                        "Date : " + jsonObject.getString("GH_ENT_DATE") + "\n" +
                                        "Supervisor : " + jsonObject.getString("U_NAME") + "\n" +
                                        (jsonObject.isNull("DISEASE") ? "" : "DISEASE : " + jsonObject.getString("DISEASE") + "\n") +
                                        "E : " + jsonObject.getString("DISTANCE1") + "   S : " + jsonObject.getString("DISTANCE3") +
                                        "   W : " + jsonObject.getString("DISTANCE2") + "   N : " + jsonObject.getString("DISTANCE4")
                        )
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green));
                mMap.addMarker(markerOpt);//.showInfoWindow();
                // Marker marker = mMap.addMarker(markerOpt);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LatLng markerLatLng = marker.getPosition();

                        double latitude = markerLatLng.latitude;
                        double longitude = markerLatLng.longitude;
                        LatLng userLatLng = new LatLng(lat, lng);
                        LatLng plotMarkerLatLng = new LatLng(latitude, longitude);
                        double distance = SphericalUtil.computeDistanceBetween(plotMarkerLatLng, userLatLng);

                        Log.d("Distance :", "" + distance);

                        if (distance < 100) {

                            Intent intent = new Intent(context, POCPlotFinderListMaster.class);
                            intent.putExtra("lat", "" + lat);
                            intent.putExtra("lng", "" + lng);
                            startActivity(intent);

                        } else {

                            Toast.makeText(context, "You are outside to the plot radius range..!", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, "distance is greater than : "+distance, Toast.LENGTH_SHORT).show();

                        }

                        return true;
                    }
                });

            }

        }
    }

    private void kmlFileMap(GoogleMap mMap) {

        mMap.setBuildingsEnabled(true);
       /* mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 5000, null);
       // LatLng def = new LatLng(28.8180, 78.1858);
        LatLng def = new LatLng(lat, lng);
        CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(def, 19);
        mMap.animateCamera(locations);*/

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16.0f));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //  mMap.setMyLocationEnabled(true);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        ZipInputStream zipInputStream = null;

        try {
            // Open the KMZ file as an InputStream (you can load it from assets, storage, or a network source)
            //  InputStream kmzInputStream = getAssets().open("your_file.kmz");

            InputStream kmzInputStream = getResources().openRawResource(R.raw.allkmz);
            // Create a ZipInputStream to extract KML data
            zipInputStream = new ZipInputStream(kmzInputStream);
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".kml")) {

                    // This is the KML file within the KMZ
                    String kmlData = readInputStream(zipInputStream); // Read the KML data
                    Log.e("KML Content", kmlData);

                    byte[] kmlBytes = kmlData.getBytes(StandardCharsets.UTF_8);
                    KmlLayer kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(kmlBytes), getApplicationContext());
                    kmlLayer.addLayerToMap();

                   /* //KML layer click listener
                    kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(com.google.maps.android.data.Feature feature) {
                            Toast.makeText(getApplicationContext(), "nDescription: kmlLayer ", Toast.LENGTH_LONG).show();
                           // Toast.makeText(getApplicationContext(), "nDescription: " + feature.getProperty("description"), Toast.LENGTH_LONG).show();
                        }
                    });*/

                    // Set up a click listener for polygons (KML shapes)
                    mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                        @Override
                        public void onPolygonClick(Polygon polygon) {

                            LatLng clickedLatLng = polygon.getPoints().get(0); // Get the first point of the polygon

                            Log.e("KML LatLng", "" + clickedLatLng);

                            Toast.makeText(PocPlotsAllPlotsView.this, "" + clickedLatLng, Toast.LENGTH_SHORT).show();
                        }
                    });

                    //  KMLParser.parseKML(String.valueOf(getResources().openRawResource(R.raw.allkmz)));
                    Log.d("KML Content builder", "==" + kmlLayer.getPlacemarks());
                    Log.d("KML Content Containers", "==" + kmlLayer.hasContainers());
                    Log.d("KML Content Placemarks", "==" + kmlLayer.hasPlacemarks());

                    for (KmlContainer container : kmlLayer.getContainers()) {
                        if (container.hasProperty("coordinates")) {
                            System.out.println("KML Containers :" + container.getProperty("coordinates"));
                        }
                    }

                    for (KmlPlacemark placemark : kmlLayer.getPlacemarks()) {

                        Log.d("KML Content builder", "" + placemark);
                        builder.include((LatLng) placemark.getGeometry().getGeometryObject());

                    }
                }
            }

            // zipInputStream.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("KML Display Error", "Error displaying KML file: " + e.getMessage());

        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        inputStreamReader.close();

        return stringBuilder.toString();
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GETALLPOCNPLOTSBYVILLAGE1);
                request1.addProperty("Divn", "" + userDetailsModels.get(0).getDivision());
                request1.addProperty("VILLCODE", "" + V_CODE);
                request1.addProperty("POCTYPE", "" + PLOT_TYPE_SELECTION);

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GETALLPOCNPLOTSBYVILLAGE1, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GETALLPOCNPLOTSBYVILLAGE1Result").toString();
                }

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
            //dialog.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            dialog.dismiss();
            try {
                JSONObject jsonObject1 = new JSONObject(message);
                if (jsonObject1.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    plotFarmerShareArray = jsonObject1.getJSONArray("DATA");

                    drawPlot(plotFarmerShareArray);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16.0f));

                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject1.getString("MSG"));
                }

            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(context, message);

            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error 5:" + e.getMessage());
            }
        }
    }
}