package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.services.MyLocationService;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.ImageUtil;


public class SupervisorAttendance extends AppCompatActivity {

    Button checkin, checkout;
    SQLiteDatabase db;
    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffTargetModel> staffTargetModelList;
    double lat, lng;
    LocationManager locationManager;
    boolean fakeLocation = false;
    List<Address> addressesCheckIn, addressesCheckOut;
    String checkOutId = "";
    ImageView image_my;

    Bitmap bmpCheckIn, bmpCheckOut;
    String pictureImagePath = "";
    String formattedDate, formattedTime;
    TextView curent_time_Date;
    public static final int RC_CAMERA_REQUEST_CHECK_IN = 1000, RC_CAMERA_REQUEST_CHECK_OUT = 1002;
    double distanceCheckIn, checkInLat, checkInLng, distanceCheckOut, checkOutLat, checkOutLng;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervisor_attendance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = SupervisorAttendance.this;
        setTitle(getString(R.string.MENU_ATTENDANCE));
        toolbar.setTitle(getString(R.string.MENU_ATTENDANCE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TextView version=findViewById(R.id.version);
        //version.setText("Version:"+ BuildConfig.VERSION_NAME);

        try {
            Intent intent = new Intent(context, MyLocationService.class);
            intent.setAction(MyLocationService.ACTION_START_FOREGROUND_SERVICE);
            startService(intent);
            dbh = new DBHelper(context);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            startLocationUpdates();
            dbh = new DBHelper(context);
            staffTargetModelList = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();
            /*last_update=findViewById(R.id.last_update);
            device_id=findViewById(R.id.device_id);
            btn=findViewById(R.id.btn);
            btn1=findViewById(R.id.btn1);*/

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }


        new DashboardData().execute();
        checkin = findViewById(R.id.checkin);
        image_my = findViewById(R.id.image_my);
        curent_time_Date = findViewById(R.id.curent_time_Date);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fakeLocation) {
                    new AlertDialogManager().RedDialog(context, "Warning: fake location detected please disable fake location");
                } else {
                    AttendanceCheckIn();
                }
            }
        });


        // Get the current date and time
        Date currentDate = new Date();

        // Define formats for the date and time
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        // SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:a", Locale.US);

        // Format the current date and time as strings
        formattedDate = dateFormat.format(currentDate);
        formattedTime = timeFormat.format(currentDate);
        curent_time_Date.setText("Current Time Is : " + formattedDate + "  " + formattedTime);



        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fakeLocation) {
                    new AlertDialogManager().RedDialog(context, "Warning: fake location detected please disable fake location");
                } else {
                    AttendanceCheckOut();
                }
            }
        });


    }


    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            Location location1 = locationResult.getLastLocation();
                            //t_master_latlng.setText(""+locationResult.getLastLocation().getAccuracy());
                            if (location1 != null) {
                                DecimalFormat decimalFormat = new DecimalFormat("##.00");
                                if (location1.isFromMockProvider()) {
                                    fakeLocation = true;
                                    new AlertDialogManager().RedDialog(context, "Warning: fake location detected please disable fake location");
                                } else {
                                    fakeLocation = false;
                                    lat = location1.getLatitude();
                                    lng = location1.getLongitude();
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException e) {

        }
    }

    private class DashboardData extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/CHECKINOUTLIST";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("IMINO", new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User", userDetailsModels.get(0).getCode()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
                System.out.println("Hi-" + Content);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(Content);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getInt("CHECKIN") == 1 && jsonObject.getInt("CHECKOUT") == 0) {
                        checkin.setVisibility(View.GONE);
                        checkout.setVisibility(View.VISIBLE);
                        TextView checkin_time = findViewById(R.id.checkin_time);
                        checkin_time.setText("Check In Time :" + jsonObject.getString("CHECkINDATE"));
                        checkOutId = jsonObject.getString("ID");
                    } else {
                        checkin.setVisibility(View.GONE);
                        checkout.setVisibility(View.GONE);
                        TextView checkin_time = findViewById(R.id.checkin_time);
                        TextView checkout_time = findViewById(R.id.checkout_time);
                        checkin_time.setText("Check In Time :" + jsonObject.getString("CHECkINDATE"));
                        checkout_time.setText("Check Out Time :" + jsonObject.getString("CHECKOUTDATE"));
                    }
                } catch (JSONException e) {
                    checkin.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.GONE);
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
                }

            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }


    private class CheckIn extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);
        int code;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imgFrmt = "";
                if (bmpCheckIn != null) {
                    //Bitmap bitmap = new ImageUtil().ShrinkBitmapByPath(pictureImagePath, 1000, 1000);//decodeFile(params[0]);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bmpCheckIn.compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] byteFormat = bao.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                }

                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/INATTENDANCE";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("IMINO", getDeviceImei.GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("Address", params[3]));
                entity.add(new BasicNameValuePair("LAT", params[1]));
                entity.add(new BasicNameValuePair("LON", params[2]));
                entity.add(new BasicNameValuePair("Image", imgFrmt));
                //entity.add(new BasicNameValuePair("radius",params[4]));
                //String paramString = URLEncodedUtils.format(entity, "utf-8");
                //HttpGet httpGet = new HttpGet(url+"?"+paramString);
                //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                //Content = httpClient.execute(httpGet, responseHandler);

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost);
                code = response.getStatusLine().getStatusCode();
                Content = EntityUtils.toString(response.getEntity());
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                checkin.setVisibility(View.GONE);
                checkout.setVisibility(View.VISIBLE);
                new AlertDialogManager().GreenDialog(context, Content);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    private class CheckOut extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);
        int code;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imgFrmt = "";
                if (bmpCheckOut != null) {
                    //Bitmap bitmap = new ImageUtil().ShrinkBitmapByPath(pictureImagePath, 1000, 1000);//decodeFile(params[0]);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bmpCheckOut.compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] byteFormat = bao.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                }
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/OUTATTENDANCE";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("IMINO", getDeviceImei.toString()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("Address", params[3]));
                entity.add(new BasicNameValuePair("LAT", params[1]));
                entity.add(new BasicNameValuePair("LON", params[2]));
                entity.add(new BasicNameValuePair("ID", checkOutId));
                entity.add(new BasicNameValuePair("Image", imgFrmt));
                //entity.add(new BasicNameValuePair("radius",params[4]));
                /*String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);*/

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost);
                code = response.getStatusLine().getStatusCode();
                Content = EntityUtils.toString(response.getEntity());

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                checkin.setVisibility(View.GONE);
                checkout.setVisibility(View.GONE);
                new AlertDialogManager().GreenDialog(context, Content);

            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }


    public void AttendanceCheckIn() {
        try {
            final List<Address> addresses;
            Geocoder geocoder;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String address = "";
            addresses = geocoder.getFromLocation(lat, lng, 1);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    context);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Current Lat : " + lat + "\nCurrent Lng : " + lng + "\n\nCurrent Address : " + addresses.get(0).getAddressLine(0) + "\n\n\nAre you sure you want to check in");
            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Location locationCentre = new Location("gps");
                                //locationCentre.setLatitude(Double.parseDouble(appSettingModels.get(0).getLat()));
                                //locationCentre.setLongitude(Double.parseDouble(appSettingModels.get(0).getLng()));
                                Location locationCurrent = new Location("gps");
                                locationCurrent.setLatitude(lat);
                                locationCurrent.setLongitude(lng);
                                distanceCheckIn = locationCentre.distanceTo(locationCurrent);
                                /*if (appSettingModels.get(0).getWorkFrom().equalsIgnoreCase("Office")) {
                                    if (distance <= Double.parseDouble(appSettingModels.get(0).getGeofence())) {
                                        new CheckIn().execute("CheckIn", "" + lat, "" + lng, addresses.get(0).getAddressLine(0)
                                                , "" + distance);
                                    } else {
                                        new AlertDialogManager().RedDialog(context, "You are not in checked in location");
                                    }

                                } else {

                                }*/
                                addressesCheckIn = addresses;
                                checkInLat = lat;
                                checkInLng = lng;
                                captureCheckInImage();
                                //
                            } catch (Exception e) {
                                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                            }
                        }
                    });
            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
            //new AttendanceInOnServer().execute(""+location.getLatitude(),""+location.getLongitude());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Location not found please try again");
        }
    }

    public void AttendanceCheckOut() {
        try {
            Geocoder geocoder;
            final List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String address = "";
            addresses = geocoder.getFromLocation(lat, lng, 1);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    context);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Current Lat : " + lat + "\nCurrent Lng : " + lng + "\nCurrent Address : " + addresses.get(0).getAddressLine(0) + "\n\n\nAre you sure you want to check out");
            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Location locationCentre = new Location("gps");
                                //locationCentre.setLatitude(Double.parseDouble(appSettingModels.get(0).getLat()));
                                // locationCentre.setLongitude(Double.parseDouble(appSettingModels.get(0).getLng()));
                                Location locationCurrent = new Location("gps");
                                locationCurrent.setLatitude(lat);
                                locationCurrent.setLongitude(lng);
                                distanceCheckOut = locationCentre.distanceTo(locationCurrent);
                                /*if (appSettingModels.get(0).getWorkFrom().equalsIgnoreCase("Office")) {
                                    if (distance <= Double.parseDouble(appSettingModels.get(0).getGeofence())) {
                                        new CheckIn().execute("CheckOut", "" + lat, "" + lng, addresses.get(0).getAddressLine(0)
                                                , "" + distance);
                                    } else {
                                        new AlertDialogManager().RedDialog(context, "You are not in checked in location");
                                    }

                                } else {

                                }*/
                                addressesCheckOut = addresses;
                                checkOutLat = lat;
                                checkOutLng = lng;
                                captureCheckOutImage();

                            } catch (Exception e) {
                                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                            }
                        }
                    });
            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
            //new AttendanceInOnServer().execute(""+location.getLatitude(),""+location.getLongitude());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Location not found please try again");
        }
    }


    public void captureCheckInImage() {
        try {
            try {
            /*bmp=null;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RC_CAMERA_REQUEST);*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);


                            Log.d("Photo Ui : ", "Photo Ui Is   : " + photoURI);
                            //Log.d("Photo Ui2 : ","Photo Ui Is2   : "+photoURI);

                        }
                    }
                }
                startActivityForResult(takePictureIntent, RC_CAMERA_REQUEST_CHECK_IN);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void captureCheckOutImage() {
        try {
            try {
            /*bmp=null;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RC_CAMERA_REQUEST);*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        }
                    }
                }
                startActivityForResult(takePictureIntent, RC_CAMERA_REQUEST_CHECK_OUT);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_CHECK_IN && resultCode == RESULT_OK) {
            try {
                File checkImg = new File(pictureImagePath);
                if (checkImg.exists()) {
                    Bitmap imageBitmap = new ImageUtil().ShrinkBitmapByPath(pictureImagePath, 800, 800);
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";
                    //  image_my.setImageBitmap(imageBitmap);

                    bmpCheckIn = new ImageUtil().drawTextToBitmap(context, d, t, imageBitmap);

                    showImageCheckIn(imageBitmap, checkInLat, checkInLng, addressesCheckIn.get(0).getAddressLine(0), distanceCheckIn);

                    // new CheckIn().execute("CheckIn", "" + checkInLat, "" + checkInLng, addressesCheckIn.get(0).getAddressLine(0), "" + distanceCheckIn);
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";
                    bmpCheckIn = imageBitmap;;

                    showImageCheckIn(imageBitmap, checkInLat, checkInLng, addressesCheckIn.get(0).getAddressLine(0), distanceCheckIn);

                    // image_my.setImageBitmap(imageBitmap);
                    //bmp = new ImageUtil().drawTextToBitmap(LoginTeamMember.this, d, t, imageBitmap);
                   // new CheckIn().execute("CheckIn", "" + checkInLat, "" + checkInLng, addressesCheckIn.get(0).getAddressLine(0), "" + distanceCheckIn);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
        if (requestCode == RC_CAMERA_REQUEST_CHECK_OUT && resultCode == RESULT_OK) {
            try {
                File checkImg = new File(pictureImagePath);
                if (checkImg.exists()) {
                    Bitmap imageBitmap = new ImageUtil().ShrinkBitmapByPath(pictureImagePath, 800, 800);
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";
                    bmpCheckOut = new ImageUtil().drawTextToBitmap(context, d, t, imageBitmap);


                   // image_my.setImageBitmap(imageBitmap);

                    showImageCheckOut(imageBitmap, checkOutLat, checkOutLng, addressesCheckOut.get(0).getAddressLine(0), distanceCheckOut);

               // new CheckOut().execute("CheckOut", "" + checkOutLat, "" + checkOutLng, addressesCheckOut.get(0).getAddressLine(0), "" + distanceCheckOut);
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";
                    bmpCheckOut = imageBitmap;
                    showImageCheckOut(imageBitmap, checkOutLat, checkOutLng, addressesCheckOut.get(0).getAddressLine(0), distanceCheckOut);
                    //bmp = new ImageUtil().drawTextToBitmap(LoginTeamMember.this, d, t, imageBitmap);
                  //  new CheckOut().execute("CheckOut", "" + checkOutLat, "" + checkOutLng, addressesCheckOut.get(0).getAddressLine(0), "" + distanceCheckOut);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

/*
    void showImage1(Bitmap imageBitmap, double lat, double lng, String address, double distance){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        ImageView imagePreview = bottomSheetView.findViewById(R.id.image_preview);
        Button submitButton = bottomSheetView.findViewById(R.id.save_button);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SupervisorAttendance.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        imagePreview.setImageBitmap(imageBitmap);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the API for check-out here
                new CheckOut().execute("CheckOut", "" + lat, "" + lng, address, "" + distance);
                bottomSheetDialog.dismiss();
                // Close the bottom sheet
            }
        });

        // Now you can work with imagePreview and submitButton as needed








    }
*/
    void showImageCheckOut(Bitmap imageBitmap, double lat, double lng, String address, double distance) {
        // Inflate custom layout for AlertDialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_sheet_layout, null);

        ImageView imagePreview = dialogView.findViewById(R.id.image_preview);
        Button submitButton = dialogView.findViewById(R.id.save_button);
        TextView currentTime = dialogView.findViewById(R.id.current_time);
        TextView checkOut = dialogView.findViewById(R.id.checkOut);
        TextView checkIn = dialogView.findViewById(R.id.checkIn);
        Button exitButton = dialogView.findViewById(R.id.exit_button);
        checkOut.setVisibility(View.VISIBLE);
        checkIn.setVisibility(View.GONE);


        imagePreview.setImageBitmap(imageBitmap);


        currentTime.setText("Current Time Is : " + formattedDate + "  " + formattedTime);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SupervisorAttendance.this);
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the API for check-out here
                new CheckOut().execute("CheckOut", "" + lat, "" + lng, address, "" + distance);
              alertDialog.dismiss();

            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });



    }
    void showImageCheckIn(Bitmap imageBitmap, double lat, double lng, String address, double distance) {
        // Inflate custom layout for AlertDialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_sheet_layout, null);

        ImageView imagePreview = dialogView.findViewById(R.id.image_preview);
        Button submitButton = dialogView.findViewById(R.id.save_button);
        TextView checkIn = dialogView.findViewById(R.id.checkIn);
        TextView checkOut = dialogView.findViewById(R.id.checkOut);
        TextView currentTime = dialogView.findViewById(R.id.current_time);
        Button exitButton = dialogView.findViewById(R.id.exit_button);
        checkIn.setVisibility(View.VISIBLE);
        checkOut.setVisibility(View.GONE);
        currentTime.setText("Current Time Is : " + formattedDate + "  " + formattedTime);


        imagePreview.setImageBitmap(imageBitmap);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SupervisorAttendance.this);
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the API for check-out here
                new CheckIn().execute("CheckIn", "" + lat, "" + lng, address, "" + distance);
                alertDialog.dismiss();

            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });



    }

}