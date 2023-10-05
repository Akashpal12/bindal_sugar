package in.co.vibrant.bindalsugar.view.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class HistoryPage extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    private GoogleMap mMap;
    double lat = 0.00, lng = 0.00;
    TextView currentAddress, lastUpdate, speed;
    String vehicle;
    List<LatLng> list;
    List<UserDetailsModel> loginUserDetailsList;
    LatLng latlng;
    Button btn;
    private long timeRemaining = 0;
    //long millisInFuture = 50000000; //30 seconds
    long countDownInterval = 2000; //1 second
    float durationInMs = 2000;
    //Declare a variable to hold count down timer's paused status
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    private boolean isStart = false;
    int i = 0;
    String address;
    JSONArray serverData;
    ArrayList<HashMap> dataList;
    PolylineOptions pline;
    private DBHelper dbh;
    private SQLiteDatabase db;
    AlertDialog dialogPopup;
    Context context;
    String userId = "0";
    String historyDate, startTimeStr, endTimeStr;
    //Spinner speedSpinner;
    CountDownTimer timer;
    EditText fromtime;
    EditText totime;
    EditText date;
    AlertDialog dialog;
    String currentDate = "", fromtimes = "", totimes = "";
    int mHour;
    int mMinute;
    String USERCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = HistoryPage.this;
        setContentView(R.layout.history_tracking);
        try {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setTitle("History Tracking");
            toolbar.setTitle("History Tracking");
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
           // userDetailsModelList = dbh.getUserDetailsModel();
            loginUserDetailsList = dbh.getUserDetailsModel();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
         //   USERCODE = getIntent().getExtras().getString("USERCODE");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            currentAddress = (TextView) findViewById(R.id.address);

            list = new ArrayList<LatLng>();
            btn = (Button) findViewById(R.id.btnRegister1);
            btn.setTag(0);


            //String startDate=getIntent().getExtras().getString("start");//"2017-09-15 00:00:00";
            //String endDate=getIntent().getExtras().getString("end");//"2017-09-15 22:00:00";
            //String imei=getIntent().getExtras().getString("imei");//"355488020146019";

            pline = new PolylineOptions().width(5).color(Color.MAGENTA).geodesic(true);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //new getLatLng().execute(startDate,endDate,imei);
            int count = 100; //Declare as inatance variable
            openDateDialogue();
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void openFilterData(View v) {
        openDateDialogue();
    }

    private void openDateDialogue() {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.history_altbox, null);
        dialogbilder.setView(mView);

        date = mView.findViewById(R.id.date);
        fromtime = mView.findViewById(R.id.fromtime);
        totime = mView.findViewById(R.id.totime);

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
                        currentDate = year + temmonth + temDate;
                        date.setText(year + temmonth + temDate);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        //-----------------time from and  to ---------------------------------
        fromtime.setTextIsSelectable(true);
        fromtime.setFocusable(false);
        fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;

                                String minutes = "" + minute;
                                if (minutes.length() == 1) {
                                    minutes = "0" + minutes;
                                }

                                String hourOfDays = "" + hourOfDay;
                                if (hourOfDays.length() == 1) {
                                    hourOfDays = "0" + hourOfDays;
                                }

                                fromtimes = hourOfDays + "" + minutes;
                                fromtime.setText(hourOfDays + "" + minutes);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        totime.setTextIsSelectable(true);
        totime.setFocusable(false);

        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;

                                String minutes = "" + minute;
                                if (minutes.length() == 1) {
                                    minutes = "0" + minutes;
                                }

                                String hourOfDays = "" + hourOfDay;
                                if (hourOfDays.length() == 1) {
                                    hourOfDays = "0" + hourOfDays;
                                }

                                totimes = hourOfDays + "" + minutes;
                                totime.setText(hourOfDays + "" + minutes);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });

        //-------------------------------------------------------------------
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btn_ok = mView.findViewById(R.id.btn_ok);
        ImageView close = mView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currentDate.length() == 0) {
                        new AlertDialogManager().showToast((Activity) context, "Please select tracking date");
                    } else if (fromtimes.length() == 0) {
                        new AlertDialogManager().showToast((Activity) context, "Please select tracking from time");
                    } else if (totimes.length() == 0) {
                        new AlertDialogManager().showToast((Activity) context, "Please select tracking to time");
                    } else {
                        dialog.dismiss();
                        new GetLiveHistory().execute();
                    }
                    //
                } catch (Exception e) {

                }
            }
        });
    }


    //------------------------click the button change the map speed--------------------------------------------
    public void speed1x(View v) {
        try {
            durationInMs = 2000;
            countDownInterval = 2000;
            Button btnRegister1 = findViewById(R.id.btnRegister1);
           // btnRegister1.performClick();
            new AlertDialogManager().RedDialog(context,"No Data Found");
        } catch (Exception e) {

        }
    }

    public void speed2x(View v) {
        try {

            durationInMs = 1000;
            countDownInterval = 1000;
            Button btnRegister1 = findViewById(R.id.btnRegister1);
            //btnRegister1.performClick();
            new AlertDialogManager().RedDialog(context,"No Data Found");
        } catch (Exception e) {

        }
    }

    public void speed4x(View v) {
        try {

            durationInMs = 700;
            countDownInterval = 700;
            Button btnRegister1 = findViewById(R.id.btnRegister1);
           // btnRegister1.performClick();
            new AlertDialogManager().RedDialog(context,"No Data Found");
        } catch (Exception e) {

        }
    }

    public void speed8x(View v) {
        try {
            durationInMs = 200;
            countDownInterval = 200;
            Button btnRegister1 = findViewById(R.id.btnRegister1);
            //btnRegister1.performClick();
            new AlertDialogManager().RedDialog(context,"No Data Found");
        } catch (Exception e) {

        }
    }
    //-----------------------------------------------------------------------------------------------------------


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location location = null;
        mMap.setBuildingsEnabled(true);
        //LatLng point=new LatLng();
        //point.latitude=lat;
        //point.longitude=lng;


        //mMap.setTrafficEnabled(true);
        //log.add("Google Map Instantiated.");
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.d("Map", "Map clicked");
                //Toast.makeText(LiveTracking.this, "Click On map", Toast.LENGTH_SHORT).show();
                //new getVehicle().execute();
                //checkStatus();
            }
        });
        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HistoryTracking.this, R.raw.style_night_map));

        UiSettings uiSettings = googleMap.getUiSettings();
        //log.add("UI Setting Instantiated.");
        uiSettings.setMyLocationButtonEnabled(true);
        //log.add("my location enabled");
        uiSettings.setAllGesturesEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(26.4499, 80.3319), 12.0f));

    }

    private class GetLiveHistory extends AsyncTask<String, Integer, Void> {
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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/EmpLocationHistoryAnd";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("EMPCODE", ""+loginUserDetailsList.get(0).getCode()));//USERCODE
                entity.add(new BasicNameValuePair("FDATE", currentDate + fromtimes));
                entity.add(new BasicNameValuePair("TDATE", currentDate + totimes));
                entity.add(new BasicNameValuePair("Divn", ""+loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("Seas", ""+new SessionConfig(context).getSeason()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

                // for Header and Authothorization in  Basic Auth With user Name And Password
                httpPost.setHeader("X-ApiKey", "LsTrackingVib@1234");
                httpPost.setHeader("Authorization", "Basic " + Base64.encodeToString("LsDemo@45:LsAdmin@123".getBytes(), Base64.NO_WRAP));

                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                dialog.dismiss();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                dialog.dismiss();
                final Marker m;
                MarkerOptions a;
                JSONObject obj = new JSONObject(message);
                if (obj.getString("APISTATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = obj.getJSONArray("DATA");
                    serverData = jsonArray;
                    //JSONObject jsonObject=jsonArray.getJSONObject(0);
                    //Toast.makeText(HistoryTracking.this,"----"+jsonObject.getString("lat"), Toast.LENGTH_LONG).show();
                    //latlng=new LatLng(Double.parseDouble(jsonObject.getString("lat")),Double.parseDouble(jsonObject.getString("lng")));
                    //latlng.latitude=Double.parseDouble(jsonObject.getString("lat"));
                    //list.add(latlng);
                    dataList = new ArrayList<HashMap>();
                    dataList.clear();
                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    for (int z = 0; z < jsonArray.length(); z++) {
                        //LatLng point = list.get(z);
                        JSONObject jsonObject = jsonArray.getJSONObject(z);
                        LatLng l1 = new LatLng(jsonObject.getDouble("LAT"), jsonObject.getDouble("LNG"));
                        options.add(l1);
                    }
                    isStart = true;
                    mMap.addPolyline(options);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    LatLng l1 = new LatLng(jsonObject.getDouble("LAT"), jsonObject.getDouble("LNG"));
                    pline.add(l1);
                    //JSONObject j1 = jsonObject.getJSONObject("Tstamp");
                    Bitmap icon = getBitmapFromVectorDrawable(context, R.drawable.ic_baseline_person_pin_circle_24);
                    BitmapDescriptor iconImage = BitmapDescriptorFactory.fromBitmap(icon);
                    a = new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .icon(iconImage)
                            .title("Address : " + jsonObject.getString("ADDRESS"))
                            .snippet("Speed : " + jsonObject.getString("SPEED") + " Kms/Hour")
                            .anchor(0.5f, 0.5f)
                            .rotation(0)
                            .flat(true);

                    m = mMap.addMarker(a);
                    mMap.addPolyline(pline);
                    m.setPosition(new LatLng(jsonObject.getDouble("LAT"), jsonObject.getDouble("LNG")));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(jsonObject.getDouble("LAT"), jsonObject.getDouble("LNG")), 16.0f));
                    currentAddress.setText(jsonObject.getString("ADDRESS") + "\nDate Time-" +
                            jsonObject.getString("CORDINATEDATE") + "\nSpeed - " +
                            jsonObject.getString("SPEED") + " Kms/Hour");
                } else {
                    new AlertDialogManager().RedDialog(context, obj.getString("MSG"));
                }

            } catch (Exception e) {
                dialog.dismiss();
                new AlertDialogManager().RedDialog(context, "Data Not Found");
            }
        }
    }


    private void runPerson() {
        int status = (Integer) btn.getTag();
        if (status == 0) {
            runningVehicle();
        } else if (status == 1) {
            pauseVehicle();
        } else if (status == 2) {
            resumeVehicle();
        } else {
            finishTrip();
        }
    }

    public void startVehicle(View v) {
        new AlertDialogManager().RedDialog(context,"No Data Found");
       // runPerson();
    }

    private void runningVehicle() {

        long millisInFuture = 30000000; //30 seconds
        //long countDownInterval = 3000; //1 second
        btn.setTag(1);
        btn.setText("Pause");


        //Initialize a new CountDownTimer instance
        timer = new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                if (isPaused || isCanceled) {
                    cancel();
                } else {
                    mMap.clear();
                    if (i < serverData.length()) {
                        drawMarker(i);
                    } else {
                        mMap.addPolyline(pline);
                        Toast.makeText(context, "Finish....", Toast.LENGTH_SHORT).show();
                        isCanceled = true;
                        btn.setText("Finish ");
                        btn.setTag(10);
                    }

                    i++;
                    timeRemaining = millisUntilFinished;
                }

            }

            public void onFinish() {
                //Do something when count down finished
                Toast.makeText(context, "Finish....", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void pauseVehicle() {
        isPaused = true;
        btn.setText("Resume");
        btn.setTag(2);

        Toast.makeText(context, "Person Paused....", Toast.LENGTH_SHORT).show();
    }

    public void finishTrip() {
        finish();
    }

    public void resumeVehicle() {
        isPaused = false;
        isCanceled = false;
        //countDownInterval = 2000;
        btn.setText("Pause");
        btn.setTag(1);
        //Initialize a new CountDownTimer instance
        long millisInFuture = timeRemaining;
        //long countDownInterval = 1000;
        timer = new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                //Do something in every tick
                if (isPaused || isCanceled) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    //btn.setText(""+i);
                    mMap.clear();
                    if (i < serverData.length()) {
                        drawMarker(i);
                    } else {
                        mMap.addPolyline(pline);
                        Toast.makeText(context, "Finish....", Toast.LENGTH_SHORT).show();
                        isCanceled = true;
                        btn.setText("Finish ");
                        btn.setTag(10);
                    }

                    i++;
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                }
            }

            public void onFinish() {
                //Do something when count down finished
                btn.setText("Task Completed");
                btn.setTag(10);
            }
        }.start();

    }


    private void animatevehicle(Double oldlat, Double oldlng, Double newlat, Double newlng, final Marker m) {

        final LatLng startPosition = new LatLng(oldlat, oldlng);
        final LatLng finalPosition = new LatLng(newlat, newlng);
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        //final float durationInMs = 2000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);


                m.setPosition(currentPosition);


                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 20);
                } else {
                    if (hideMarker) {
                        m.setVisible(false);
                    } else {
                        m.setVisible(true);
                    }
                }
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(newlat, newlng), 16.0f));
    }


    private void drawMarker(int i) {
        try {
            JSONObject json1 = serverData.getJSONObject(i);
            JSONObject json2 = serverData.getJSONObject(i + 1);
            //JSONObject j1=json2.getJSONObject("Tstamp");
            LatLng latLng = new LatLng(json2.getDouble("LAT"), json2.getDouble("LNG"));
            pline.add(latLng);
            //btn.setText(json1.getString("lat")+"----"+i+"----"+json2.getString("lat"));
            MarkerOptions a;

            final Marker m;
            {
                Bitmap icon = getBitmapFromVectorDrawable(context, R.drawable.ic_baseline_person_pin_circle_24);
                BitmapDescriptor iconImage = BitmapDescriptorFactory.fromBitmap(icon);
                a = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .icon(iconImage)
                        .title("Address : " + json2.getString("ADDRESS"))
                        .snippet("Speed : " + json2.getString("SPEED"))
                        .anchor(0.5f, 0.5f)
                        .rotation(0)
                        .flat(true);
            }
            mMap.addPolyline(pline);
            currentAddress.setText(json2.getString("ADDRESS") + "\nDate Time-" +
                    json2.getString("CORDINATEDATE") + "\nSpeed - " +
                    json2.getString("SPEED") + " Kms/Hour");
            m = mMap.addMarker(a);
            animatevehicle(json1.getDouble("LAT"), json1.getDouble("LNG"),
                    json2.getDouble("LAT"), json2.getDouble("LNG"), m);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
