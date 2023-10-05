package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffAddCropGrowthObservation extends AppCompatActivity {

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
    String TAG="Cane Growth Obseration Save";
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    List<UserDetailsModel> loginUserDetailsList;
    TextInputLayout input_layout_type_of_planting;
    Spinner input_type_of_planting;
    EditText village_code,grower_name,BrixPurcent,plot_sr_no,space_row_to_row,germination,tillers,millable_cane,cane_height,cane_girth,weight;
    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_growth_observation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffAddCropGrowthObservation.this;
        setTitle(getString(R.string.MENU_CROP_GROWTH));
        toolbar. setTitle(getString(R.string.MENU_CROP_GROWTH));
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
        image=findViewById(R.id.image);
        //Lat=getIntent().getExtras().getString("lat");
        //Lng=getIntent().getExtras().getString("lng");

        input_layout_type_of_planting=findViewById(R.id.input_layout_type_of_planting);
        input_type_of_planting=findViewById(R.id.input_type_of_planting);

        village_code=findViewById(R.id.village_code);
        grower_name=findViewById(R.id.grower_name);
        plot_sr_no=findViewById(R.id.plot_sr_no);
        space_row_to_row=findViewById(R.id.space_row_to_row);
        germination=findViewById(R.id.germination);
        tillers=findViewById(R.id.tillers);
        millable_cane=findViewById(R.id.millable_cane);
        cane_height=findViewById(R.id.cane_height);
        cane_girth=findViewById(R.id.cane_girth);
        weight=findViewById(R.id.weight);
        BrixPurcent=findViewById(R.id.BrixPurcent);

        village_code.setText(V_CODE);
        grower_name.setText(G_CODE+" / "+G_NAME);
        plot_sr_no.setText(PLOT_SR_NO);
        findLocation();
        new GetMaster().execute();
        input_type_of_planting = findViewById(R.id.input_type_of_planting);

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

    public void saveData(View v)
    {
        //,,,,,,,,,
        checkValidation : {
            if(input_type_of_planting.getSelectedItemPosition()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please select plot condition");
                space_row_to_row.requestFocus();
                break checkValidation;
            }

            if(space_row_to_row.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter Row to Row space");
                space_row_to_row.requestFocus();
                break checkValidation;
            }

            if(germination.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter Germination");
                germination.requestFocus();
                break checkValidation;
            }

            if(tillers.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average tillers  in a meter ");
                tillers.requestFocus();
                break checkValidation;
            }

            if(millable_cane.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average  millable cane  in a meter");
                millable_cane.requestFocus();
                break checkValidation;
            }

            if(cane_height.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average height of cane");
                cane_height.requestFocus();
                break checkValidation;
            }

            if(cane_girth.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average girth of cane");
                cane_girth.requestFocus();
                break checkValidation;
            }

            if(weight.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average weight of cane (expected)");
                weight.requestFocus();
                break checkValidation;
            }
            if(BrixPurcent.getText().toString().length()==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Please enter average BrixPurcent of cane (expected)");
                weight.requestFocus();
                break checkValidation;
            }
            if(Lat==0)
            {
                new AlertDialogManager().showToast((Activity) context,"Sorry we are getting your current location please try again..");
                break checkValidation;
            }
            if (pictureImagePath.length() == 0) {
                new AlertDialogManager().showToast((Activity) context,"Please capture cane image");
                break checkValidation;
            }
            String[] cropConditionArray=input_type_of_planting.getSelectedItem().toString().split(" - ");
            new SaveLog().execute(cropConditionArray[0]);
            //finish();
        }
    }

    private class GetMaster extends AsyncTask<String, Void, Void> {
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
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/CropObservation";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                //entity.add(new BasicNameValuePair("appid","1211973affa328939680b7a52bd0cbfe"));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                if (dialog.isShowing())
                    dialog.dismiss();
                //,,,,masterCaneProping;
                //,,,,,;
                JSONArray jsonArray=new JSONArray(Content);
                JSONObject jsonObject=jsonArray.getJSONObject(0);

                JSONArray masterCaneTypeArray=jsonObject.getJSONArray("CANETYPE");
                ArrayList<String> dataCaneTypeList = new ArrayList<>();
                dataCaneTypeList.add(" - Select - ");
                for (int i = 0; i < masterCaneTypeArray.length(); i++) {
                    dataCaneTypeList.add((String) masterCaneTypeArray.get(i));
                }
                //cane_type.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneTypeList));


                JSONArray masterCropConditionArray=jsonObject.getJSONArray("CropCondition");
                ArrayList<String> dataCropCondition = new ArrayList<>();
                dataCropCondition.add(" - Select - ");
                for (int i = 0; i < masterCropConditionArray.length(); i++) {
                    dataCropCondition.add(masterCropConditionArray.getJSONObject(i).getString("Code")+" - "+masterCropConditionArray.getJSONObject(i).getString("Name"));
                }
                input_type_of_planting.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCropCondition));

            }catch (JSONException e)
            {
                new AlertDialogManager().RedDialog(context,Content);
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
        }
    }

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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVEGROWTHOBSERVATION);
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("U_CODE", loginUserDetailsList.get(0).getCode());
                request1.addProperty("DIVN", loginUserDetailsList.get(0).getDivision());
                request1.addProperty("PLOTVILL", PLOT_VILL);
                request1.addProperty("VillCode", V_CODE);
                request1.addProperty("Grower", G_CODE);
                request1.addProperty("PLOTNO", PLOT_SR_NO);
                request1.addProperty("Variety", "");
                request1.addProperty("CaneType", "");
                request1.addProperty("PLOTCONDITION", params[0]);
                request1.addProperty("ROWTOROW", space_row_to_row.getText().toString());
                request1.addProperty("GERMINATION", germination.getText().toString());
                request1.addProperty("AVGTILLERS", tillers.getText().toString());
                request1.addProperty("AVGMILLABLE", millable_cane.getText().toString());
                request1.addProperty("AVGHEIGHT", cane_height.getText().toString());
                request1.addProperty("AVGGIRTH", cane_girth.getText().toString());
                request1.addProperty("AVGWEIGHT", weight.getText().toString());
                request1.addProperty("Remark", "");
                request1.addProperty("Image", imgFrmt);
                request1.addProperty("ADDRESS", address);
                request1.addProperty("LAT", ""+Lat);
                request1.addProperty("LON", ""+Lng);
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("BrixPurcent", BrixPurcent.getText().toString());
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVEGROWTHOBSERVATION, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVEGROWTHOBSERVATIONResult").toString();
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

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            String str = TAG;
            Log.d(str, "onPostExecute: " + result);
            if (message != null) {
                try {
                    Log.d(TAG, message);
                    JSONObject jsonObject=new JSONObject(message);
                    if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                    {
                        new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                    }
                    else
                    {
                        new AlertDialogManager().RedDialog(context, message);
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

}