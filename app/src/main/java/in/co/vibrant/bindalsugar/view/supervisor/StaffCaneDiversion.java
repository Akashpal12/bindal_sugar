package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

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
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GModal;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffCaneDiversion extends AppCompatActivity {

    EditText txt_Vehicleno,input_village_code,input_village_name,input_grower_code,input_grower_name,description,input_grower_father,txt_quantity;
            Spinner cropcondition,type_of_crop,txt_reason,txt_kolh;
            LinearLayout ll_cane;

    String filename1 = "", pictureImagePath1 = "", filename2 = "", pictureImagePath2 = "";
    int RC_CAMERA_REQUEST_IMAGE_1 = 1001, RC_CAMERA_REQUEST_IMAGE_2 = 1002;
    AlertDialog Alertdialog;
    Spinner variety;


Context context;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModels;
    double Lat, Lng;
    String address = "";
    String G_CODE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String V_CODE;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_cane_diversion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.MENU_CANE_DIVERSION));
        toolbar.setTitle(getString(R.string.MENU_CANE_DIVERSION));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context=StaffCaneDiversion.this;
        dbh = new DBHelper(context);
        userDetailsModels = dbh.getUserDetailsModel();
        ll_cane=findViewById(R.id.ll_cane);
        ll_cane.setVisibility(View.GONE);
        
        
        input_village_code=findViewById(R.id.input_village_code);
        input_village_name=findViewById(R.id.input_village_name);
        input_grower_code=findViewById(R.id.input_grower_code);
        input_grower_name=findViewById(R.id.input_grower_name);
        input_grower_father=findViewById(R.id.input_grower_father);
        txt_quantity=findViewById(R.id.txt_quantity);
        txt_Vehicleno=findViewById(R.id.txt_Vehicleno);

        description=findViewById(R.id.description);
        variety = findViewById(R.id.variety);

        cropcondition=findViewById(R.id.cropcondition);
        type_of_crop=findViewById(R.id.type_of_crop);
        txt_reason=findViewById(R.id.txt_reason);
        txt_kolh=findViewById(R.id.txt_kolh);
        init();
        new GetMaster().execute();
        findLocation();

    }
    private void init()
    {
        try {
            input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        //checkPlot = false;
                        new GetVillage().execute();
                        /*List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                        if (villageModalList.size() > 0) {
                            input_village_code.setText(villageModalList.get(0).getCode());
                            input_village_name.setText(villageModalList.get(0).getName());
                        } else {
                            input_village_code.setText("");
                            input_village_name.setText("");
                            new AlertDialogManager().RedDialog(context,"Please enter valid village code First");
                        }*/

                    }
                }
            });
            input_grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        if (input_grower_code.getText().toString().length() > 0) {
                            //checkPlot = false;
                            new GetGrower().execute();
                           /* if(input_grower_code.getText().toString().equals("0"))
                            {
                                input_grower_name.setFocusable(true);
                                input_grower_name.setTextIsSelectable(true);
                                input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                input_grower_father.setFocusable(true);
                                input_grower_father.setTextIsSelectable(true);
                                input_grower_father.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            }
                            else
                            {
                                input_grower_name.setFocusable(false);
                                input_grower_name.setTextIsSelectable(false);
                                input_grower_name.setInputType(InputType.TYPE_NULL);
                                input_grower_father.setFocusable(false);
                                input_grower_father.setTextIsSelectable(false);
                                input_grower_father.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                                if (growerModelList.size() > 0) {
                                    input_grower_code.setText(growerModelList.get(0).getGrowerCode());
                                    input_grower_name.setText(growerModelList.get(0).getGrowerName());
                                    input_grower_father.setText(growerModelList.get(0).getGrowerFather());
                                } else {
                                    new AlertDialogManager().RedDialog(context,"Please enter valid grower code");
                                    input_grower_code.setText("");
                                    input_grower_name.setText("");
                                    input_grower_father.setText("");
                                }

                            }*/

                        }
                    } else {
                       new AlertDialogManager().RedDialog(context,"Please enter village code First");
                        input_village_code.requestFocus();
                    }
                }
            });
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        txt_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                if (txt_reason.getSelectedItemPosition()==0) {
                    ll_cane.setVisibility(View.GONE);

                }

                else  if (txt_reason.getSelectedItemPosition()==1){
                    ll_cane.setVisibility(View.GONE);


                }else if(txt_reason.getSelectedItemPosition()==2){
                    ll_cane.setVisibility(View.GONE);


                }else if(txt_reason.getSelectedItemPosition()==3){
                    ll_cane.setVisibility(View.GONE);


                }
                else if(txt_reason.getSelectedItemPosition()==4){
                    ll_cane.setVisibility(View.GONE);


                }
                else if(txt_reason.getSelectedItemPosition()==5){
                    ll_cane.setVisibility(View.GONE);


                } else if(txt_reason.getSelectedItemPosition()==6){
                    ll_cane.setVisibility(View.GONE);

                   /* if (description.getText().length()  == 0) {
                        new AlertDialogManager().RedDialog(StaffCaneDiversion.this, "Please enter Your Reason In Description Box");

                    }*/
                }
                else if(txt_reason.getSelectedItemPosition()==7) {
                    ll_cane.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

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
                            new AlertDialogManager().AlertPopUpFinish(context, "Security Error: please enable gps service");
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
                            if (location.isFromMockProvider()) {
                                new AlertDialogManager().showToast((Activity) context, "Security Error : you can not use this application because we detected fake GPS ?");
                            } else {
                                try {
                                    List<Address> addresses = new ArrayList<>();
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    Geocoder geocoder;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                                    address = addresses.get(0).getAddressLine(0);

                                } catch (SecurityException se) {
                                    address = "Error : Security error";
                                    new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
                                } catch (Exception se) {
                                    new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
        }

    }



    public void openCam (View v)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            filename1 = "image_" + currentDt + ".jpg";
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
            startActivityForResult(intent, RC_CAMERA_REQUEST_IMAGE_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRecorder (View v)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            filename2 = "image_" + currentDt + ".mp4";
            pictureImagePath2 = dir.getAbsolutePath() + "/" + filename2;
            File file = new File(pictureImagePath2);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST_IMAGE_2);
        }
        catch (Exception e) {
            e.printStackTrace();
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_1) {
            try {
                File file = new File(pictureImagePath1);
                if (file.exists()) {
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";//location
                    Bitmap bmp = drawTextToBitmap(context, d, t, pictureImagePath1);
                    FileOutputStream out = new FileOutputStream(pictureImagePath1);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath1);
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_image_view, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    ImageView d_image = mView.findViewById(R.id.d_image);
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);
                    d_image.setImageBitmap(bitmap);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                            filename1 = "";
                            pictureImagePath1 = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        } else if (requestCode == RC_CAMERA_REQUEST_IMAGE_2) {
            try {
                File file = new File(pictureImagePath2);
                if (file.exists()) {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_video_view, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    final VideoView d_video = mView.findViewById(R.id.d_image);
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);

                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    getWindow().setFormat(PixelFormat.TRANSLUCENT);

                    MediaController mediaController = new MediaController(context);
                    mediaController.show();
                    Uri video = Uri.parse(pictureImagePath2);
                    d_video.setMediaController(mediaController);
                    d_video.setKeepScreenOn(true);
                    d_video.setVideoPath(pictureImagePath2);
                    d_video.requestFocus();
                    /*videoView.setVideoURI(video);
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            //mp.setLooping(true);
                            videoView.start();
                        }
                    });*/
                    d_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            d_video.start();
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                            filename2 = "";
                            pictureImagePath2 = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap (Context gContext, String gText, String gText1, String path){
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
            Bitmap bitmap = BitmapFactory.decodeFile(path);

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
            int x = (bitmap.getWidth() - bounds.width()) - 200;
            int y = (bitmap.getHeight() + bounds.height()) - 260;

            canvas.drawText(gText, x, y, paint);
            paint.getTextBounds(gText1, 0, gText1.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) - 200;
            int y1 = (bitmap.getHeight() + bounds.height()) - 110;
            canvas.drawText(gText1, x1, y1, paint);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap (String file,int width, int height)
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
        } catch (Exception e) {
            //new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }



    public void saveData(View v) {
        try {

            CheckValidation:
            {

                if (input_village_code.getText().length() == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Village Code");
                    break CheckValidation;
                }

                if (input_grower_code.getText().length() == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Grower Code");
                    break CheckValidation;
                }
                if (cropcondition.getSelectedItemPosition()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter CaneType");
                    break CheckValidation;
                }
                if (type_of_crop.getSelectedItemPosition()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Category");
                    break CheckValidation;
                }
                  if (variety.getSelectedItemPosition()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Variety");
                    break CheckValidation;
                }

                if (txt_quantity.getText().length() == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Cane Quantity");
                    break CheckValidation;
                }


                     if (txt_reason.getSelectedItemPosition()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Reason");
                    break CheckValidation;
                }
                if (txt_kolh.getSelectedItemPosition()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Kolhu");
                    break CheckValidation;
                }



                if (txt_Vehicleno.getText().length()  == 0) {
                    new AlertDialogManager().showToast(StaffCaneDiversion.this, "Please enter Vehicle Number");
                    break CheckValidation;
                }

                if (filename1.length() < 5 && filename2.length() < 5) {
                    new AlertDialogManager().RedDialog(context, "Please capture image or record video");
                    break CheckValidation;
                }

                if(txt_reason.getSelectedItemPosition()==7){
                    ll_cane.setVisibility(View.VISIBLE);

                    if (description.getText().length()  == 0) {
                        new AlertDialogManager().RedDialog(StaffCaneDiversion.this, "Please enter Your Reason In Description Box");
                        break CheckValidation;
                    }
                }

                String[] reasonArray = cropcondition.getSelectedItem().toString().split(" - ");
                String[] soiltypeArray = type_of_crop.getSelectedItem().toString().split(" - ");
                String[] landtypeArray = txt_reason.getSelectedItem().toString().split(" - ");
                String[] previousArray = txt_kolh.getSelectedItem().toString().split(" - ");
                String[] varietyArray = variety.getSelectedItem().toString().split(" - ");


                new saveData().execute(input_village_code.getText().toString(),input_grower_code.getText().toString(),reasonArray[0], soiltypeArray[0], txt_quantity.getText().toString(), landtypeArray[0],
                        previousArray[0] ,description.getText().toString(),txt_Vehicleno.getText().toString(),varietyArray[0]);




                ////----Sending On server----
                //String[] cropConditionStr=cropcondition.getSelectedItem().toString().split((" - "));
                //cropConditionStr[0]; // Jisme Code Hoge

                //Jisme nahi hoga cropcondition.getSelectedItem().toString()

            }
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    private class saveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /* dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imgFrmt ="";
                String videoFrmt ="";
                if (filename1.length() > 5) {
                    Bitmap bitmap1 = ShrinkBitmap(pictureImagePath1, 500, 500);//decodeFile(params[0]);
                    ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                    byte[] byteFormat1 = bao1.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat1, Base64.NO_WRAP);

                }
                if (filename2.length() > 5) {
                    FileInputStream fis = new FileInputStream(pictureImagePath2);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];

                    for (int readNum; (readNum = fis.read(b)) != -1; ) {
                        bos.write(b, 0, readNum);
                    }
                    byte[] byteFormat = bos.toByteArray();
                    videoFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                }

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVECANEDIVERSION);
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("VILLCODE",params[0]);
                request1.addProperty("GROCODE", params[1]);
                request1.addProperty("CROPTYPE",params[2]);
                request1.addProperty("VARIETYGROUP", params[3] );
                request1.addProperty("CANEQUANTITY", params[4]);
                request1.addProperty("DIVERSIONREASON", params[5]);
                request1.addProperty("KOLHUNAME",params[6] );
                request1.addProperty("DIS",params[7]);
                request1.addProperty("SUPCODE", userDetailsModels.get(0).getCode());
                request1.addProperty("LAT", "" +Lat);
                request1.addProperty("LON",  "" +Lng);
                request1.addProperty("ADDRESS", address);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("VECHILENO",params[8] );
                request1.addProperty("IMAGENAME", imgFrmt);
                request1.addProperty("VEDIONAME", videoFrmt);
                request1.addProperty("varietyCD", params[9]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVECANEDIVERSION, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVECANEDIVERSIONResult").toString();
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    Intent intent = new Intent(context, StaffMainActivity.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"), intent);
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }


    private class GetMaster extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETONLINEMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();


                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));

                entity.add(new BasicNameValuePair("lang", getString(R.string.language)));
                //entity.add(new BasicNameValuePair("LAT",params[0]));
                //entity.add(new BasicNameValuePair("LON",params[1]));


                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + message);


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

            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                JSONArray masterCategoryArray = jsonObject.getJSONArray("CANETYPE");
                ArrayList<String> dataCategory = new ArrayList<>();
                dataCategory.add(" - Select - ");
                for (int i = 0; i < masterCategoryArray.length(); i++) {
                    dataCategory.add(masterCategoryArray.getJSONObject(i).getString("Code") + " - " + masterCategoryArray.getJSONObject(i).getString("Name"));
                }
                cropcondition.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCategory));


                JSONArray masterREASONArray = jsonObject.getJSONArray("CATEGORY");
                ArrayList<String> dataREASON = new ArrayList<>();
                dataREASON.add(" - Select - ");
                for (int i = 0; i < masterREASONArray.length(); i++) {
                    dataREASON.add(masterREASONArray.getJSONObject(i).getString("Code") + " - " + masterREASONArray.getJSONObject(i).getString("Name"));
                }
                type_of_crop.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataREASON));


               JSONArray mastercanetypeArray = jsonObject.getJSONArray("DIVERSIONREASON");
                ArrayList<String> datacanetype = new ArrayList<>();
                datacanetype.add(" - Select - ");
                for (int i = 0; i < mastercanetypeArray.length(); i++) {
                    datacanetype.add(mastercanetypeArray.getJSONObject(i).getString("Code") + " - " + mastercanetypeArray.getJSONObject(i).getString("Name"));
                }
                txt_reason.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, datacanetype));


                JSONArray masterKOLHUtypeArray = jsonObject.getJSONArray("KOLHU");
                ArrayList<String> dataKOLHUtype = new ArrayList<>();
                dataKOLHUtype.add(" - Select - ");
                for (int i = 0; i < masterKOLHUtypeArray.length(); i++) {
                    dataKOLHUtype.add(masterKOLHUtypeArray.getJSONObject(i).getString("Code") + " - " + masterKOLHUtypeArray.getJSONObject(i).getString("Name"));
                }
                txt_kolh.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataKOLHUtype));

              JSONArray masterVARIETYypeArray = jsonObject.getJSONArray("VARIETY");
                ArrayList<String> dataVARIETYtype = new ArrayList<>();
                dataVARIETYtype.add(" - Select - ");
                for (int i = 0; i < masterVARIETYypeArray.length(); i++) {
                    dataVARIETYtype.add(masterVARIETYypeArray.getJSONObject(i).getString("Code") + " - " + masterVARIETYypeArray.getJSONObject(i).getString("Name"));
                }
                variety.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataVARIETYtype));



                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }

    private class GetVillage extends AsyncTask<String, Integer, Void> {
        String message;
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETVILLAGE";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("VillCode", input_village_code.getText().toString()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);

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
            try {
                input_village_name.setText("");
               // vModalList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(message);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    VModal vmodel = new VModal();
                    input_village_name.setText(jsonObject.getString("VName"));
                    //vModalList.add(vmodel);
                }


                if (dialog.isShowing()) {
                    if (input_village_name.getText().length() == 0) {
                        new AlertDialogManager().RedDialog(context, "Invalid Village Code");
                        input_village_code.requestFocus();
                    }

                    dialog.dismiss();
                }
            } catch (JSONException e) {

                Intent intent = new Intent(context, CutToCrush.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Error:" + e.toString(), intent);

            } catch (Exception e) {
                Intent intent = new Intent(context, CutToCrush.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Error:" + e.toString(), intent);

            }
        }
    }

    private class GetGrower extends AsyncTask<String, Integer, Void> {
        String message;
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETGROWER";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("VillCode", input_village_code.getText().toString()));
                entity.add(new BasicNameValuePair("GCode", input_grower_code.getText().toString()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);

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
            try {
                GModal gModal = new GModal();
                input_grower_name.setText("");
                JSONArray jsonArray = new JSONArray(message);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    input_grower_name.setText(jsonObject.getString("GName"));
                    input_grower_father.setText(jsonObject.getString("GFatherName"));

                }

                if (dialog.isShowing()) {
                    if (input_grower_name.getText().length() == 0) {
                        new AlertDialogManager().RedDialog(context, "Invalid grower Code");
                        input_grower_code.requestFocus();
                    }

                    dialog.dismiss();
                }
            } catch (JSONException e) {

                Intent intent = new Intent(context, CutToCrush.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Error:" + e.toString(), intent);

            } catch (Exception e) {
                Intent intent = new Intent(context, CutToCrush.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Error:" + e.toString(), intent);

            }
        }
    }


    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffCaneDiversion.this);
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
}



