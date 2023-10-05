package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.gson.Gson;

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
import in.co.vibrant.bindalsugar.adapter.ListVillageGrowerAdapter;
import in.co.vibrant.bindalsugar.model.GrowerVillageMeetingModal;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;


public class VillageFarmerMeeting extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper dbh;
    Context context;
    List<UserDetailsModel> userDetailsModels;
    Spinner village;
    EditText village_code,grower_code,remark,contact_person_number,total_person_present,contact_person_name;
    TextView latStr,lngStr,accuracyStr;
    double lat=0.00,lng=0.00,accuracy=0.00;
    String address;
    JSONArray villageList;

    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;

    List<GrowerVillageMeetingModal> gModals;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_farmer_meeting);
        try {
            context = this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Village Grower Meeting");
            toolbar. setTitle("Village Grower Meeting");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });

            image=findViewById(R.id.image);
            userDetailsModels = dbh.getUserDetailsModel();
            village = findViewById(R.id.village);
            village_code =  findViewById(R.id.village_code);
            grower_code = findViewById(R.id.grower_code);
            remark = findViewById(R.id.remark);
            latStr = findViewById(R.id.lat);
            lngStr = findViewById(R.id.lng);
            accuracyStr = findViewById(R.id.accuracy);
            contact_person_number = findViewById(R.id.contact_person_number);
            total_person_present = findViewById(R.id.total_person_present);
            contact_person_name = findViewById(R.id.contact_person_name);

            gModals=new ArrayList<>();
            try{
                villageList=new JSONArray(getIntent().getExtras().getString("villageList"));
                ArrayList arrayListMeeting = new ArrayList();
                arrayListMeeting.add(" - Select -");
                for (int i = 0; i < villageList.length(); i++) {
                    JSONObject js=villageList.getJSONObject(i);
                    arrayListMeeting.add(js.getString("VILLCODE")+" - "+js.getString("VILLNAME"));
                }
                ArrayAdapter<String> adapterMeeting = new ArrayAdapter<String>(context, R.layout.list_item, arrayListMeeting);
                village.setAdapter(adapterMeeting);
            }
            catch (Exception e)
            {

            }
            startLocationUpdates();
            //initActivity();
        } catch (Exception e) {
            AlertDialogManager alertDialogManager = new AlertDialogManager();
            Context context2 = context;
            alertDialogManager.RedDialog(context2, "Error:" + e.toString());
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
                                accuracy = location.getLongitude();
                                latStr.setText("Lattitude : "+lat);
                                lngStr.setText("Longitude : "+lng);
                                accuracyStr.setText("Accuracy : "+new DecimalFormat("0.00").format(accuracy));
                                //lat=28.917547499999998;
                                //lng=78.3702595;
                                //lat=28.9604204;
                                //lng=78.2882313;
                                if(APIUrl.isDebug)
                                {
                                    lat=APIUrl.lat;
                                    lng=APIUrl.lng;
                                }
                                final List<Address> addressesList;
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                addressesList = geocoder.getFromLocation(lat, lng, 1);
                                address=addressesList.get(0).getAddressLine(0);
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
            new AlertDialogManager().RedDialog(this,"Security Error 3:"+se.getMessage());
        }
        catch(Exception se)
        {
            new AlertDialogManager().RedDialog(this,"Error 4:"+se.getMessage());
        }

    }

    public void exit(View v)
    {
        finish();
    }

    public void openCam(View v)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt=dateFormat.format(today);
            filename = "image"+currentDt+".jpg";
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
            startActivityForResult(intent,RC_CAMERA_REQUEST);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if(file.exists())
                {
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
                }
                else
                {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context,"Error:" + e.toString());
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
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
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
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }

    public void addGrower(View v){
        CheckValidation :
        {
            try {
                if (village_code.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please enter village code");
                    break CheckValidation;
                }
                if (grower_code.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please enter grower code");
                    break CheckValidation;
                }
                if (contact_person_name.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter person name");
                    break CheckValidation;
                }
                if (contact_person_number.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter person number");
                    break CheckValidation;
                }
                else {
                    if (!contact_person_number.getText().toString().matches("\\d{10}")) {
                        new AlertDialogManager().RedDialog(context, "Please enter a valid 10-digit person number");
                        break CheckValidation;
                    }
                }

                if (total_person_present.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter total person present");
                    break CheckValidation;
                }


                GrowerVillageMeetingModal gModal=new GrowerVillageMeetingModal();
                gModal.setVillCode(Integer.parseInt(village_code.getText().toString()));
                gModal.setGrowCode(Integer.parseInt(grower_code.getText().toString()));
                if (gModals.contains(gModal)) {
                    new AlertDialogManager().showToast((Activity) context, "This grower already added");
                    village_code.setText("");
                    grower_code.setText("");
                    //activityMethod.setText("");
                } else {
                    new getGrowerDetails().execute(village_code.getText().toString(),grower_code.getText().toString());

                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
            }
        }
    }

    private void setData()
    {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListVillageGrowerAdapter listPloughingAdapter = new ListVillageGrowerAdapter(context, gModals);
            recyclerView.setAdapter(listPloughingAdapter);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
    }

    public void saveData(View v) {
        try{
            CheckValidate : {
                if (village.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please select village");
                    break CheckValidate;
                }
                if (gModals.size() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please add village and grower");
                    break CheckValidate;
                }
                if (remark.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter remark");
                    break CheckValidate;
                }
                if (pictureImagePath.length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please capture image");
                    break CheckValidate;
                }
                if (contact_person_name.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter person name");
                    break CheckValidate;
                }
                if (contact_person_number.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter person number");
                    break CheckValidate;
                }
                else {
                    if (!contact_person_number.getText().toString().matches("\\d{10}")) {
                        new AlertDialogManager().RedDialog(context, "Please enter a valid 10-digit person number");
                        break CheckValidate;
                    }
                }
                if (total_person_present.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter total person present");
                    break CheckValidate;
                }
                String villageCode=village.getSelectedItem().toString().split(" - ")[0];
                new SaveLog().execute(villageCode);
            }
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
        }

    }

    /* renamed from: in.co.vibrant.canepotatodevelopment.view.fieldstaff.AddPlotEntryLog$SaveLog */
    private class SaveLog extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        String message;

        private SaveLog() {
            dialog = new ProgressDialog(context);
        }

        /* access modifiers changed from: protected */
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog show = ProgressDialog.show(context, getString(R.string.app_name), "Please wait", true);
            dialog = show;
            show.show();
        }

        protected Void doInBackground(String... params) {
            try {
                Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt= Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                Gson gson=new Gson();
                String dt=gson.toJson(gModals);
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SAVEGROWERMEETING";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("VILLAGE",params[0]));
                entity.add(new BasicNameValuePair("SEAS",getString(R.string.season)));
                entity.add(new BasicNameValuePair("DATA",dt));
                entity.add(new BasicNameValuePair("remark",remark.getText().toString()));
                entity.add(new BasicNameValuePair("lat",""+lat));
                entity.add(new BasicNameValuePair("lng",""+lng));
                entity.add(new BasicNameValuePair("address",""+address));
                entity.add(new BasicNameValuePair("image",imgFrmt));
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModels.get(0).getCode()));

                entity.add(new BasicNameValuePair("CONTACTPERSONNAME",contact_person_name.getText().toString()));
                entity.add(new BasicNameValuePair("CONTACTPERSONNUMBER",contact_person_number.getText().toString()));
                entity.add(new BasicNameValuePair("TENTIVEPERSON",total_person_present.getText().toString()));

                String debugData = new MiscleniousUtil().ListNameValueToString("SAVEGROWERMEETING", entity);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
            catch (Exception e2) {
                Log.e("Exception", e2.getMessage());
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (message != null) {
                try {
                    JSONObject jsonObject=new JSONObject(message);
                    if(jsonObject.getString("STATUS").equalsIgnoreCase("OK"))
                    {
                        new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                    }
                    else
                    {
                        new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                    }

                } catch (Exception e) {
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } else if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class getGrowerDetails extends AsyncTask<String, Integer, Void> {
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
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GrowerVerification";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("G_CODE",params[1]));
                entity.add(new BasicNameValuePair("VILL",params[0]));
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);


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
            try {
                if(dialog.isShowing())
                    dialog.dismiss();
                village_code.setText("");
                grower_code.setText("");
                JSONObject jsonObject=new JSONObject(message);
                if(jsonObject.getString("STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    if(jsonArray.length()>0)
                    {
                        JSONObject obj=jsonArray.getJSONObject(0);
                        GrowerVillageMeetingModal growerVillageMeetingModal=new GrowerVillageMeetingModal();
                        growerVillageMeetingModal.setVillCode(obj.getInt("VILLCODE"));
                        growerVillageMeetingModal.setVillName(obj.getString("VILNAME"));
                        growerVillageMeetingModal.setGrowCode(obj.getInt("GCODE"));
                        growerVillageMeetingModal.setGrowerName(obj.getString("GNAME"));
                        growerVillageMeetingModal.setFather(obj.getString("GFATHER"));
                        String msg="Do you want to add below grower\n" +
                                "Village : "+growerVillageMeetingModal.getVillCode()+" / "+growerVillageMeetingModal.getVillName()+"\n" +
                                "Grower : "+growerVillageMeetingModal.getGrowCode()+" / "+growerVillageMeetingModal.getGrowerName()+"\n" +
                                "Grower Father : "+growerVillageMeetingModal.getFather();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                context);
                        alertDialog.setTitle(context.getString(R.string.app_name));
                        alertDialog.setMessage(msg);
                        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        gModals.add(growerVillageMeetingModal);
                                        setData();
                                    }
                                });
                        alertDialog.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        setData();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
                else{
                    new AlertDialogManager().showToast((Activity) context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {

            }
            catch(Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}
