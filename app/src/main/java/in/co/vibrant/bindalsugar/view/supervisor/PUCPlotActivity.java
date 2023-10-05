package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.textfield.TextInputLayout;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.PUCPlotListSaveAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.POCPlotListDataModel;
import in.co.vibrant.bindalsugar.model.PlotActivityModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;

//,
public class PUCPlotActivity extends AppCompatActivity {
    String G_CODE, G_FATHER;
    String G_NAME, PLOT_FROM, OLDSEAS;
    String PLOT_SR_NO;
    String PLOT_VILL, PLOT_VILL_NAME;
    String MOBILE;
    String GSRNO;
    String AREA;
    String G_MOBILE, LAT, LANG;
    RadioButton rbYes, rbNo;
    List<POCPlotListDataModel> pocPlotListDataModelList;
    String SHARE_AREA, selectedAffectedCode;
    RecyclerView puc_recycler;
    String StrCaneType = "";
    String StrVarietyCode = "";
    String TAG = "AddGrowerShare", GroupArea;
    String V_CODE, V_NAME;
    Context context;
    JSONArray JsonData;

    double lat = 0.00, lng = 0.00;

    /* renamed from: db */
    SQLiteDatabase db;
    DBHelper dbh;
    EditText grower_name, input_meeting_name, input_meeting_number;
    List<MasterDropDown> masterCaneTypeList, masterDisease, masterCropCondition, masterIrrigation, masterCaneEarthing, masterCaneProping, diseffectedtype;
    EditText plot_sr_no;
    TextInputLayout input_layout_date;
    List<UserDetailsModel> userDetailsModels;
    EditText village_code, remark;

    TextInputLayout input_layout_spray_item, input_layout_affected_condition;

    EditText activityInsertDate, description, input_activity_area;
    Spinner input_item, plot_type, activityCode, activityMethod, effect_condition;
    List<MasterSubDropDown> activityCodeList, activityMethodList;
    List<MasterDropDown> itemMasterList;
    List<PlotActivitySaveModel> plotActivitySaveModelList;
    List<PlotActivityModel> plotActivityModelList;

    List<MasterDropDown> meetingWith;

    String filename = "", pictureImagePath = "", CANE_TYPE, ActivityselectedOption;
    ImageView image;
    String address;
    TextView tvResult;
    String PocCode;
    String opcid;
    String opPOCNAME;
    String remarks = "";
    private int RC_CAMERA_REQUEST = 1001;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puc_plot_activity);

        try {
            context = this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU_PUC_PLOT_ACTIVITY));
            toolbar.setTitle(getString(R.string.MENU_PUC_PLOT_ACTIVITY));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            pocPlotListDataModelList = new ArrayList<>();
            image = findViewById(R.id.image);
            village_code = (EditText) findViewById(R.id.village_code);
            grower_name = (EditText) findViewById(R.id.grower_name);
            plot_sr_no = (EditText) findViewById(R.id.plot_sr_no);

            remark = findViewById(R.id.remark);
            input_meeting_name = findViewById(R.id.input_meeting_name);
            input_meeting_number = findViewById(R.id.input_meeting_number);

            input_layout_affected_condition = findViewById(R.id.input_layout_affected_condition);
            input_item = findViewById(R.id.input_item);
            activityInsertDate = findViewById(R.id.input_inset_date);
            plot_type = findViewById(R.id.input_plot_type);
            activityCode = findViewById(R.id.input_activity);
            activityMethod = findViewById(R.id.input_activity_method);
            //activityNumber = findViewById(R.id.input_activity_no);
            description = findViewById(R.id.input_description);
            input_activity_area = findViewById(R.id.input_activity_area);
            effect_condition = findViewById(R.id.effect_condition);
            tvResult = findViewById(R.id.tvResult);
            rbYes = findViewById(R.id.rbYes);
            rbNo = findViewById(R.id.rbNo);
            input_layout_date = findViewById(R.id.input_layout_date);
            plotActivityModelList = new ArrayList<>();
            plotActivitySaveModelList = new ArrayList<>();
            activityCodeList = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();
            V_CODE = getIntent().getExtras().getString("V_CODE");
            V_NAME = getIntent().getExtras().getString("V_NAME");
            G_CODE = getIntent().getExtras().getString("G_CODE");
            G_FATHER = getIntent().getExtras().getString("G_FATHER");
            PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
            GSRNO = getIntent().getExtras().getString("GSRNO");
            CANE_TYPE = getIntent().getExtras().getString("CANE_TYPE");
            PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
            PLOT_VILL_NAME = getIntent().getExtras().getString("PLOT_VILL_NAME");
            SHARE_AREA = getIntent().getExtras().getString("SHARE_AREA");
            G_NAME = getIntent().getExtras().getString("G_NAME");
            PLOT_FROM = getIntent().getExtras().getString("SURTYPE");
            OLDSEAS = getIntent().getExtras().getString("OLDSEAS");
            MOBILE = getIntent().getExtras().getString("MOBILE");
            LAT = getIntent().getExtras().getString("LAT");
            LANG = getIntent().getExtras().getString("LNG");
            village_code.setText(V_CODE);
            grower_name.setText(G_CODE + " / " + G_NAME + " / Area : " + SHARE_AREA);
            plot_sr_no.setText(PLOT_SR_NO);

            Log.d("LatLong -->", LAT + "  " + LANG);

            if (new InternetCheck(context).isOnline()) {
                new GetDataList().execute(LAT, LANG);
            } else {
                Toast.makeText(context, "No Internet Connection..!", Toast.LENGTH_SHORT).show();
            }


            //activityInsertDate =  findViewById(R.id.activityInsertDate);


            rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        rbYes.setChecked(false);
                        remarks = "No";

                    }
                }
            });
            rbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        rbNo.setChecked(false);
                        remarks = "Yes";

                    }
                }
            });


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            //paymentDate.setText(currentDt);
            activityInsertDate.setInputType(InputType.TYPE_NULL);
            activityInsertDate.setTextIsSelectable(false);
            activityInsertDate.setFocusable(false);
            activityInsertDate.setOnClickListener(new View.OnClickListener() {
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
                            activityInsertDate.setText(year + "-" + temmonth + "-" + temDate);

                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });


        } catch (Exception e) {
            Log.d("", "");
        }
    }

    protected void startLocationUpdates() {

        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            LocationRequest locationRequest = new LocationRequest();
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
                                //lat=28.917547499999998;
                                //lng=78.3702595;
                                //lat=28.9604204;
                                //lng=78.2882313;
                                if (APIUrl.isDebug) {
                                    lat = APIUrl.lat;
                                    lng = APIUrl.lng;
                                }
                                final List<Address> addressesList;
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                addressesList = geocoder.getFromLocation(lat, lng, 1);
                                address = addressesList.get(0).getAddressLine(0);
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

    public void saveData(View v) {

        try {
            if (remarks.length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please select Yes/No");

            } else if (description.getText().toString().length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please Enter a description");

            }  else if (pictureImagePath.length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please Capture image");

            }else {
                new SaveData().execute();

            }
        } catch (Exception e) {
            Log.d("", "Error : " + e.getMessage());
        }


    }

    public void ExitBtn(View v) {
        finish();
    }


    public void openCam(View v) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            filename = "image" + currentDt + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/planting");
            dir.mkdirs();
            pictureImagePath = dir.getAbsolutePath() + "/" + filename;
            File file = new File(pictureImagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if (file.exists()) {
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat dateFormatter2 = new SimpleDateFormat("HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    dateFormatter2.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = dateFormatter2.format(today);
                    Bitmap bmp = drawTextToBitmap(context, d, t);
                    FileOutputStream out = new FileOutputStream(pictureImagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
                    image.setImageBitmap(bitmap);
                } else {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1) {
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);

            Bitmap.Config bitmapConfig =
                    bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(120);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(gText, 0, gText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) - 250;
            int y = (bitmap.getHeight() + bounds.height()) - 250;

            canvas.drawText(gText, x, y, paint);
            paint.getTextBounds(gText1, 0, gText1.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) - 250;
            int y1 = (bitmap.getHeight() + bounds.height()) - 100;
            canvas.drawText(gText1, x1, y1, paint);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {
        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

            if (heightRatio > 1 || widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }


    private class SaveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                System.out.println(imgFrmt);
                Log.d("Image : ", imgFrmt);

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SavePOCRESULT";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                //entity.add(new BasicNameValuePair("Divn",userDetailsModels.get(0).getDivision()));
                //entity.add(new BasicNameValuePair("Seas",getString(R.string.season)));


                entity.add(new BasicNameValuePair("PLVILL", "" + PLOT_VILL));
                entity.add(new BasicNameValuePair("PLOTNO", "" + PLOT_SR_NO));
                entity.add(new BasicNameValuePair("RESULT", "" + remarks));
                entity.add(new BasicNameValuePair("REMARK", "" + tvResult.getText().toString()));
                entity.add(new BasicNameValuePair("POCCODE", "" + PocCode));
                entity.add(new BasicNameValuePair("POCID", "" + opcid));
                entity.add(new BasicNameValuePair("SUPCODE", "" + userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIS", "" + description.getText().toString()));
                entity.add(new BasicNameValuePair("IMAGEDATA", "" + imgFrmt));//
                entity.add(new BasicNameValuePair("DIVN", "" + userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("IRRDate", "" + activityInsertDate.getText().toString()));


                String debugData = new MiscleniousUtil().ListNameValueToString("SavePOCRESULT", entity);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                //{"EXCEPTIONMSG":"","SAVEMSG":"Data Save","ACKID":"0"}
                JSONObject obj = new JSONObject(message);
                if (obj.getString("STATUS").equalsIgnoreCase("OK")) {
                    new AlertDialogManager().AlertPopUpFinish(context, "" + obj.getString("MSG"));
                } else {
                    new AlertDialogManager().RedDialog(context, "" + obj.getString("MSG"));
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


    private class GetDataList extends AsyncTask<String, Void, Void> {
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

            Log.d("LatLong :----->>>", "" + params[0]);
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL + "/checkPocPolygonGrower_New1";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lng", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpGet, responseHandler);

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
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            try {
                Log.d(TAG, message);
                JSONObject obj = new JSONObject(message);
                if (obj.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = obj.getJSONArray("DATA");
                    JsonData = jsonArray.getJSONObject(0).getJSONArray("JSONDATA");
                    if (jsonArray.length() == 0) {
                        new AlertDialogManager().RedDialog(context, "Error: No details found");
                    } else {
                        double totalNumber = 0;
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        for (int i = 0; i < JsonData.length(); i++) {

                            JSONObject jsonObject = JsonData.getJSONObject(i);
                            POCPlotListDataModel pocPlotListDataModel = new POCPlotListDataModel();
                            pocPlotListDataModel.setPOCCODE(jsonObject.getString("POCCODE"));
                            pocPlotListDataModel.setPOCNAME(jsonObject.getString("POCNAME"));
                            pocPlotListDataModel.setOPCID(jsonObject.getString("OPCID"));
                            pocPlotListDataModel.setREMARK(jsonObject.getString("REMARK"));
                            pocPlotListDataModelList.add(pocPlotListDataModel);
                            tvResult.setText(jsonObject.getString("REMARK"));

                            PocCode = jsonObject.getString("POCCODE");
                            opcid = jsonObject.getString("OPCID");
                            opPOCNAME = jsonObject.getString("POCNAME");


                        }
                        puc_recycler = findViewById(R.id.puc_recycler);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        puc_recycler.setHasFixedSize(true);
                        puc_recycler.setLayoutManager(manager);
                        PUCPlotListSaveAdapter listGrowerFinderAdapter = new PUCPlotListSaveAdapter(context, pocPlotListDataModelList);
                        puc_recycler.setAdapter(listGrowerFinderAdapter);


                    }
                } else {
                    new AlertDialogManager().RedDialog(context, obj.getString("MSG"));
                }


            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, message);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());

            }
        }


    }
}
