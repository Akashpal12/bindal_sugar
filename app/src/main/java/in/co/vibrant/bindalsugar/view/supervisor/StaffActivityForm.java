package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.MixCropDialogueAdapter;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class StaffActivityForm extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    ImageView image1, image2, image3, image4;
    String filename1, pictureImagePath1, filename2, pictureImagePath2, filename3, pictureImagePath3, filename4, pictureImagePath4;
    int RC_CAMERA_REQUEST_IMAGE_1 = 1001, RC_CAMERA_REQUEST_IMAGE_2 = 1002, RC_CAMERA_REQUEST_IMAGE_3 = 1003, RC_CAMERA_REQUEST_IMAGE_4 = 1004;
    FusedLocationProviderClient fusedLocationClient;
    Button saveBtn;
    String staffData;
    JSONObject jsonData;
    String BKUP_SNO, ACT_CD;
    List<UserDetailsModel> userDetailsModels;

    TextView tvCount, tv_name, area, mixcrop, plot_village, grower_father, east, west, north, south;
    TextInputEditText time,remark;
    boolean inside;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_activities_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = StaffActivityForm.this;
        dbh = new DBHelper(context);
        setTitle(getString(R.string.MENU_ACTIVITIES));
        toolbar.setTitle(getString(R.string.MENU_ACTIVITIES));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try {
            userDetailsModels = dbh.getUserDetailsModel();
            staffData = getIntent().getExtras().getString("staffData");
            BKUP_SNO = getIntent().getExtras().getString("BKUP_SNO");
            ACT_CD = getIntent().getExtras().getString("ACT_CD");
            jsonData = new JSONObject(staffData);

            tvCount = findViewById(R.id.tvCount);
            tv_name = findViewById(R.id.tv_name);
            area = findViewById(R.id.area);
            mixcrop = findViewById(R.id.mixcrop);
            plot_village = findViewById(R.id.plot_village);
            grower_father = findViewById(R.id.grower_father);
            east = findViewById(R.id.east);
            west = findViewById(R.id.west);
            north = findViewById(R.id.north);
            south = findViewById(R.id.south);
            time = findViewById(R.id.time);
            remark = findViewById(R.id.remark);

            tvCount.setText("1");
            tv_name.setText("Plot Number : " + jsonData.getString("plotNumber"));
            area.setText("Area : " + jsonData.getString("area"));
            mixcrop.setText("Mix Crop : " + jsonData.getString("mixCrop"));
            plot_village.setText("Village : " + jsonData.getString("plotVillageName"));
            grower_father.setText("Father : " + jsonData.getString("growerFather"));
            east.setText("East : " + jsonData.getString("eastDistance"));
            west.setText("West : " + jsonData.getString("westDistance"));
            north.setText("North : " + jsonData.getString("northDistance"));
            south.setText("South : " + jsonData.getString("southDistance"));

            saveBtn = findViewById(R.id.saveBtn);
            image1 = findViewById(R.id.image1);
            image2 = findViewById(R.id.image2);
            image3 = findViewById(R.id.image3);
            image4 = findViewById(R.id.image4);

            mixcrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_mix_crop, null);
                    dialogbilder.setView(mView);
                    AlertDialog Alertdialog = dialogbilder.create();

                    RecyclerView recycler_list = mView.findViewById(R.id.recycler_list);
                    List<String> data = new ArrayList<>();
                    data.add("Curliflower");
                    data.add("Sugarcane");
                    data.add("Potato");
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recycler_list.setHasFixedSize(true);
                    recycler_list.setLayoutManager(manager);
                    MixCropDialogueAdapter stockSummeryAdapter = new MixCropDialogueAdapter(context, data);
                    recycler_list.setAdapter(stockSummeryAdapter);

                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(true);
                }
            });
            checkGeofence();
        } catch (Exception e) {

        }
    }

    private void checkGeofence() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            try {

                                double lat = location.getLatitude();
                                double lng = location.getLongitude();
                                //26.873539, 81.015918
                                //lat=26.87029123; //lat/lng: (,)
                                //lng=81.01539228;
                                /*location.setLatitude(lat);
                                location.setLongitude(lng);*/
                                LatLng latlng = new LatLng(lat, lng);
                                List<LatLng> latLngList = new ArrayList<>();
                                latLngList.add(new LatLng(jsonData.getDouble("eastLat"), jsonData.getDouble("eastLng")));
                                latLngList.add(new LatLng(jsonData.getDouble("southLat"), jsonData.getDouble("southLng")));
                                latLngList.add(new LatLng(jsonData.getDouble("westLat"), jsonData.getDouble("westLng")));
                                latLngList.add(new LatLng(jsonData.getDouble("northLat"), jsonData.getDouble("northLng")));
                                if (location != null) {
                                    inside = PolyUtil.containsLocation(latlng, latLngList, true);
                                    LatLng nearPlotLatlng = new LatLngUtil().findNearestPoint(latlng, latLngList);
                                    Location nearLocation = new Location(LocationManager.GPS_PROVIDER);
                                    nearLocation.setLatitude(nearPlotLatlng.latitude);
                                    nearLocation.setLongitude(nearPlotLatlng.longitude);
                                    double distance = location.distanceTo(nearLocation);
                                    distance = distance / 1000;
                                    DecimalFormat decimalFormat = new DecimalFormat("##.00");
                                    //saveBtn.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        //return inside;
    }

    public void saveActivity(View v) {
        if (time.getText().toString().length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter total work duration");
        } else if (pictureImagePath1.length() < 10) {
            new AlertDialogManager().RedDialog(context, "Please capture first image");
        } else if (pictureImagePath2.length() < 10) {
            new AlertDialogManager().RedDialog(context, "Please capture second image");
        } else if (pictureImagePath3.length() < 10) {
            new AlertDialogManager().RedDialog(context, "Please capture third image");
        } else if (pictureImagePath4.length() < 10) {
            new AlertDialogManager().RedDialog(context, "Please capture fourth image");
        } else {
            try {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                try {
                                    boolean inside;
                                    double lat = location.getLatitude();
                                    double lng = location.getLongitude();
                                    new saveActivity().execute("" + lat, "" + lng);
                                } catch (Exception e) {

                                }
                            }
                        });
            }
            catch (SecurityException se)
            {

            }
        }
    }

    public void captureImage1(View v)
    {
        openCam(1001);
    }

    public void captureImage2(View v)
    {
        openCam(1002);
    }

    public void captureImage3(View v)
    {
        openCam(1003);
    }

    public void captureImage4(View v)
    {
        openCam(1004);
    }

    public void openCam(int CAM_REQUEST)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt=dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            if(RC_CAMERA_REQUEST_IMAGE_1==CAM_REQUEST)
            {
                filename1 = "image_"+currentDt+".jpg";
                pictureImagePath1 = dir.getAbsolutePath() + "/" + filename1;
                File file = new File(pictureImagePath1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_1);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_2==CAM_REQUEST)
            {
                filename2 = "image_"+currentDt+".jpg";
                pictureImagePath2 = dir.getAbsolutePath() + "/" + filename2;
                File file = new File(pictureImagePath2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_2);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_3==CAM_REQUEST)
            {
                filename3 = "image_"+currentDt+".jpg";
                pictureImagePath3 = dir.getAbsolutePath() + "/" + filename3;
                File file = new File(pictureImagePath3);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_3);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_4==CAM_REQUEST)
            {
                filename4 = "image_"+currentDt+".jpg";
                pictureImagePath4 = dir.getAbsolutePath() + "/" + filename4;
                File file = new File(pictureImagePath4);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_4);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_1) {
            try {
                File file = new File(pictureImagePath1);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath1);
                    image1.setImageBitmap(bmp);
                }
                else
                {
                    image1.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_2) {
            try {
                File file = new File(pictureImagePath2);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath2);
                    image2.setImageBitmap(bmp);
                }
                else
                {
                    image2.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_3) {
            try {
                File file = new File(pictureImagePath3);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath3);
                    image3.setImageBitmap(bmp);
                }
                else
                {
                    image3.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_4) {
            try {
                File file = new File(pictureImagePath4);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath4);
                    image4.setImageBitmap(bmp);
                }
                else
                {
                    image4.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

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
            //new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }

    private class saveActivity extends AsyncTask<String, Integer, Void> {
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
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                Bitmap bitmap1 = ShrinkBitmap(pictureImagePath1, 500, 500);//decodeFile(params[0]);
                ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                byte[] byteFormat1 = bao1.toByteArray();
                String imgFrmt1= Base64.encodeToString(byteFormat1,Base64.NO_WRAP);

                Bitmap bitmap2 = ShrinkBitmap(pictureImagePath2, 500, 500);//decodeFile(params[0]);
                ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bao2);
                byte[] byteFormat2 = bao2.toByteArray();
                String imgFrmt2= Base64.encodeToString(byteFormat2,Base64.NO_WRAP);

                Bitmap bitmap3 = ShrinkBitmap(pictureImagePath3, 500, 500);//decodeFile(params[0]);
                ByteArrayOutputStream bao3 = new ByteArrayOutputStream();
                bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, bao3);
                byte[] byteFormat3 = bao3.toByteArray();
                String imgFrmt3= Base64.encodeToString(byteFormat3,Base64.NO_WRAP);

                Bitmap bitmap4 = ShrinkBitmap(pictureImagePath4, 500, 500);//decodeFile(params[0]);
                ByteArrayOutputStream bao4 = new ByteArrayOutputStream();
                bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, bao4);
                byte[] byteFormat4 = bao4.toByteArray();
                String imgFrmt4= Base64.encodeToString(byteFormat4,Base64.NO_WRAP);


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date today = Calendar.getInstance().getTime();
                String currDate=dateFormat.format(today);

                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_InsertAgroActivityStatus);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());

                request1.addProperty("ACT_CD", ACT_CD);
                request1.addProperty("PLOT_SR_NUMBER", jsonData.getString("plotNumber"));
                request1.addProperty("VILLAGE_CODE", jsonData.getString("plotVillage"));
                request1.addProperty("GROWER_CODE", jsonData.getString("growerCode"));
                request1.addProperty("U_CODE", userDetailsModels.get(0).getCode());
                request1.addProperty("CDATE", currDate);
                request1.addProperty("REMARK", remark.getText().toString());
                request1.addProperty("BKUP_SNO", BKUP_SNO);
                request1.addProperty("PLOT_SR_NUMBER_ID", jsonData.getString("plotSerialId"));
                request1.addProperty("WORK_DURATION", time.getText().toString());
                request1.addProperty("IMAGE1", imgFrmt1);
                request1.addProperty("IMAGE2", imgFrmt2);
                request1.addProperty("IMAGE3", imgFrmt3);
                request1.addProperty("IMAGE4", imgFrmt4);
                request1.addProperty("LANG", getString(R.string.language));
                request1.addProperty("LAT", params[0]);
                request1.addProperty("LNG", params[1]);
                request1.addProperty("PlotGeofence", inside);
                request1.addProperty("IMEINO", imei);

                //request1.addProperty("PLOT_SR_NUMBER_ID", jsonData.getString("plotSerialId"));

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_InsertAgroActivityStatus, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("InsertAgroActivityStatusResult").toString();
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
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
                    Intent intent=new Intent(context,StaffMainActivity.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context,jsonObject.getString("MSG"),intent);
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
}
