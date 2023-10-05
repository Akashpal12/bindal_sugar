package in.co.vibrant.bindalsugar.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.LastGpsDataModel;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.broadcast.Restarter;
import in.co.vibrant.bindalsugar.model.AddMaterialDataModel;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.PlantingEquipmentModel;
import in.co.vibrant.bindalsugar.model.PlantingItemModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.SaveGrowerDocumentModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.GenerateLogFile;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;
import in.co.vibrant.bindalsugar.view.NotificationActivity;


public class MyLocationService extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    DBHelper dbh;
    SQLiteDatabase db;
    List<PlotSurveyModel> uploadPlotSurveyModel;
    List<FarmerShareModel> uploadFarmerShareModel;
    List<UserDetailsModel> userDetailsModelList;
    List<VillageSurveyModel> villageModelList;
    List<FarmerModel> farmerModelList;
    List<AgriInputFormModel> agriInputFormModels;
    List<SaveGrowerDocumentModel> saveGrowerDocumentModels;
    private Handler mHandler15Dec;
    private Handler mHandler30Min;

    boolean fakeLocation = false;
    double lat, lng;
    TextToSpeech t1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    Location location;

    LocationManager locationManager;

    public MyLocationService() {

    }

    private Runnable updateRunnable15Sec = new Runnable() {
        @Override
        public void run() {
            createJob15Sec();
            queueRunnable15Sec();
            checkValidation();
        }
    };


    private void queueRunnable15Sec() {
        mHandler15Dec.postDelayed(updateRunnable15Sec, 15000);
    }

    private void queueRunnable30Min() {
        mHandler30Min.postDelayed(updateRunnable30Min, 30 * 60000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(new Locale("hi", "IN"));
                    }
                }
            });
            fakeLocation = false;

            mHandler15Dec = new Handler();
            mHandler30Min = new Handler();
            dbh = new DBHelper(getApplicationContext());
            createJob15Sec();
            createJob30Min();
            queueRunnable15Sec();
            queueRunnable30Min();

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

            startLocationUpdates();
            checkValidation();

        } catch (SecurityException e) {

        } catch (Exception e) {

        }
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");
    }

    private Runnable updateRunnable30Min = new Runnable() {
        @Override
        public void run() {
            createJob30Min();
            queueRunnable30Min();
        }
    };

    @Override
    public void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private void createJob30Min() {
        try {
            dbh.updateAllServerFarmerShareModel("No", "Retry to send all data on server");
        } catch (SecurityException e) {
            Log.d("", e.toString());
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    private void createJob15Sec() {
        try {
            if (new InternetCheck(getApplicationContext()).isOnline()) {
                List<IndentModel> indentModelList = dbh.getIndentModel("No", "", "", "", "");
                if (indentModelList.size() > 0) {
                    storeData();

                } else {
                    List<PlantingModel> plantingModelList = dbh.getPlantingModel("No", "", "", "", "");
                    if (plantingModelList.size() > 0) {
                        storePlantingData();
                    }
                }

                List<AgriInputFormModel> agriInputFormModels = dbh.getAllAgriInputPendingDataById();
                if (agriInputFormModels.size() > 0) {
                    storeMaterialData(agriInputFormModels);
                }


                DBHelper dbh = new DBHelper(getApplicationContext());
                farmerModelList = new ArrayList<>();
                userDetailsModelList = new ArrayList<>();
                //userDetailsModelList=dbh.getFactoryModel("");
                userDetailsModelList = dbh.getUserDetailsModel();
                villageModelList = dbh.getVillageModel("");
                uploadFarmerShareModel = dbh.getFarmerShareUploadingData();
                if (uploadFarmerShareModel.size() > 0) {
                    uploadPlotSurveyModel = dbh.getPlotSurveyModelById(uploadFarmerShareModel.get(0).getSurveyId());
                    if (uploadPlotSurveyModel.size() > 0) {
                        storeCaneSurveyData();
                    }
                }
                List<PlotActivitySaveModel> plotActivitySaveModels = dbh.getPlotActivitySaveModel("Pending");
                if (plotActivitySaveModels.size() > 0) {
                    savePlotActivity();
                }

                saveGrowerDocumentModels = new ArrayList<>();
                List<SaveGrowerDocumentModel> saveGrowerDocumentModels = dbh.getAllGrowerInputData();
                if (saveGrowerDocumentModels.size() > 0) {
                    saveGowerDocument(saveGrowerDocumentModels);
                }
            }
        } catch (SecurityException e) {
            Log.d("", e.toString());
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent != null) {
            String action = intent.getAction();
            if(action!=null)

                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        startForegroundService();
                        Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopForegroundService();
                        Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PLAY:
                        Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PAUSE:
                        Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show();
                        break;
                }
        }*/
        //return super.onStartCommand(intent, flags, startId);
        startForegroundService();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        // PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent restartServicePendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_IMMUTABLE);

        } else {
            restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

        }

        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {
        try {
            Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", getString(R.string.app_name));
            } else {

                // Create notification default intent.
                Intent intent = new Intent();
                // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                PendingIntent pendingIntent;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                } else {
                    pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

                }

                // Create notification builder.
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                // Make notification show big text.
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.setBigContentTitle(getApplicationContext().getString(R.string.app_name));
                bigTextStyle.bigText("");
                // Set big text style.
                builder.setStyle(bigTextStyle);

                builder.setWhen(System.currentTimeMillis());
                builder.setSmallIcon(R.drawable.logo);
                Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                builder.setLargeIcon(largeIconBitmap);
                // Make the notification max priority.
                builder.setPriority(Notification.PRIORITY_MAX);
                // Make head-up notification.
                builder.setFullScreenIntent(pendingIntent, true);

                // Add Play button intent in notification.
                /*Intent playIntent = new Intent(this, MyLocationService.class);
                playIntent.setAction(ACTION_PLAY);
                PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
                NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
                builder.addAction(playAction);*/

                // Add Pause button intent in notification.
                /*Intent pauseIntent = new Intent(this, MyLocationService.class);
                pauseIntent.setAction(ACTION_PAUSE);
                PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
                NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
                builder.addAction(prevAction);*/

                // Build the notification.
                Notification notification = builder.build();

                // Start foreground service.
                startForeground(1, notification);
            }
        } catch (Exception e) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        try {
            Intent resultIntent = new Intent(this, NotificationActivity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getApplicationContext().getString(R.string.app_name))
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(resultPendingIntent) //intent
                    .build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, notificationBuilder.build());
            startForeground(1, notification);
        } catch (Exception e) {

        }
    }

    private void stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {
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

    private void storeData() {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message;
                    GetDeviceImei getDeviceImei = new GetDeviceImei(getApplicationContext());
                    List<IndentModel> indentModelList = dbh.getIndentModel("No", "", "", "", "");
                    List<UserDetailsModel> loginUserDetailsList = dbh.getUserDetailsModel();
                    IndentModel indentModel = indentModelList.get(0);
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/indent");
                    String imgPath = dir.getAbsolutePath() + "/" + indentModel.getImage();
                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEINDENTINGOFFLINE";
                    //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                    //int i = Integer.parseInt(params[0]);
                    HttpClient httpClient = new DefaultHttpClient();
                    File file = new File(imgPath);
                    String imgFrmt = "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";
                    if (file.exists()) {
                        try {
                            Bitmap bitmap = ShrinkBitmap(imgPath, 1500, 1200);//decodeFile(params[0]);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                            byte[] byteFormat = bao.toByteArray();
                            imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                        } catch (Exception e) {

                        }
                    }

                    //MultipartEntity entity = new MultipartEntity();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);

                    entity.add(new BasicNameValuePair("FactId", loginUserDetailsList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("Village", indentModel.getVillage()));
                    entity.add(new BasicNameValuePair("Grower", indentModel.getGrower()));
                    entity.add(new BasicNameValuePair("PLOTVillage", indentModel.getPLOTVillage()));
                    entity.add(new BasicNameValuePair("Irrigationmode", indentModel.getIrrigationmode()));
                    entity.add(new BasicNameValuePair("SupplyMode", indentModel.getSupplyMode()));
                    entity.add(new BasicNameValuePair("Harvesting", indentModel.getHarvesting()));
                    entity.add(new BasicNameValuePair("Equipment", indentModel.getEquipment()));
                    entity.add(new BasicNameValuePair("LandType", indentModel.getLandType()));
                    entity.add(new BasicNameValuePair("SeedType", indentModel.getSeedType()));
                    entity.add(new BasicNameValuePair("PloughingType", indentModel.getPloughingType()));
                    entity.add(new BasicNameValuePair("NOFPLOTS", indentModel.getNOFPLOTS()));
                    if (indentModel.getINDAREA().length() == 0) {
                        entity.add(new BasicNameValuePair("INDAREA", "0"));
                    } else {
                        entity.add(new BasicNameValuePair("INDAREA", indentModel.getINDAREA()));
                    }
                    entity.add(new BasicNameValuePair("InsertLAT", indentModel.getInsertLAT()));
                    entity.add(new BasicNameValuePair("InsertLON", indentModel.getInsertLON()));
                    entity.add(new BasicNameValuePair("MobilNO", indentModel.getMobilNO()));
                    entity.add(new BasicNameValuePair("MDATE", indentModel.getMDATE()));
                    entity.add(new BasicNameValuePair("VARIETY", indentModel.getVARIETY()));
                    //entity.add(new BasicNameValuePair("Vr_Name", indentModel.getVARIETYNAME()));
                    entity.add(new BasicNameValuePair("PLANTINGTYPE", indentModel.getPLANTINGTYPE()));
                    entity.add(new BasicNameValuePair("SprayType", indentModel.getSprayType()));
                    entity.add(new BasicNameValuePair("Crop", indentModel.getCrop()));
                    entity.add(new BasicNameValuePair("PLANTATION", indentModel.getPLANTATION()));
                    entity.add(new BasicNameValuePair("Dim1", indentModel.getDim1()));
                    entity.add(new BasicNameValuePair("Dim2", indentModel.getDim2()));
                    entity.add(new BasicNameValuePair("Dim3", indentModel.getDim3()));
                    entity.add(new BasicNameValuePair("Dim4", indentModel.getDim4()));
                    entity.add(new BasicNameValuePair("Area", indentModel.getArea()));
                    entity.add(new BasicNameValuePair("LAT1", indentModel.getLAT1()));
                    entity.add(new BasicNameValuePair("LON1", indentModel.getLON1()));
                    entity.add(new BasicNameValuePair("LAT2", indentModel.getLAT2()));
                    entity.add(new BasicNameValuePair("LON2", indentModel.getLON2()));
                    entity.add(new BasicNameValuePair("LAT3", indentModel.getLAT3()));
                    entity.add(new BasicNameValuePair("LON3", indentModel.getLON3()));
                    entity.add(new BasicNameValuePair("LAT4", indentModel.getLAT4()));
                    entity.add(new BasicNameValuePair("LON4", indentModel.getLON4()));
                    entity.add(new BasicNameValuePair("SuperviserCode", indentModel.getSuperviserCode()));
                    entity.add(new BasicNameValuePair("Image", imgFrmt));
                    entity.add(new BasicNameValuePair("OTP", "0"));
                    entity.add(new BasicNameValuePair("SMathod", indentModel.getMethod()));
                    entity.add(new BasicNameValuePair("ACKID", indentModel.getColId()));
                    entity.add(new BasicNameValuePair("GName", indentModel.getGrowerName()));
                    entity.add(new BasicNameValuePair("GFName", indentModel.getGrowerFather()));
                    entity.add(new BasicNameValuePair("VPLNO", indentModel.getPlotSerialNumber()));
                    entity.add(new BasicNameValuePair("SEEDSOURSE", indentModel.getSeedSource()));
                    entity.add(new BasicNameValuePair("ISIDEAL", indentModel.getIsIdeal()));
                    entity.add(new BasicNameValuePair("ISNARSARI", indentModel.getIsNursery()));
                    entity.add(new BasicNameValuePair("INDDATE", indentModel.getIndDate()));
                    entity.add(new BasicNameValuePair("SEEDBAGQTY", indentModel.getNOFPLOTS()));
                    //entity.add(new BasicNameValuePair("AREAACRE",indentModel.getINDAREAACRE()));

                    String debugData = new MiscleniousUtil().ListNameValueToString("SAVEINDENTINGOFFLINENEW", entity);
                    Log.d("", entity.toString());
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    dbh.updateServerStatusIndent(jsonObject.getString("EXCEPTIONMSG").replace("'", ""), jsonObject.getString("ACKID"), jsonObject.getString("SAVEMSG"));
                    System.out.println("" + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println(e.toString());
                    //new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java"+e.toString());
                }
            }
        });

        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void storePlantingData() {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message;
                    GetDeviceImei getDeviceImei = new GetDeviceImei(getApplicationContext());
                    List<PlantingModel> plantingModelList = dbh.getPlantingModel("No", "", "", "", "");
                    List<PlantingEquipmentModel> plantingEquipmentModels = dbh.getPlantingEquipmentModel(plantingModelList.get(0).getColId());
                    List<PlantingItemModel> plantingItemModels = dbh.getPlantingItemModel(plantingModelList.get(0).getColId());
                    Gson gson = new Gson();
                    String PITEMJSON = gson.toJson(plantingItemModels);
                    String PEQUIJSON = gson.toJson(plantingEquipmentModels);
                    List<UserDetailsModel> loginUserDetailsList = dbh.getUserDetailsModel();
                    PlantingModel plantingModel = plantingModelList.get(0);
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/planting");
                    String imgPath = dir.getAbsolutePath() + "/" + plantingModel.getImage();
                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEPLANTINGOFFLINE";
                    //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                    //int i = Integer.parseInt(params[0]);
                    HttpClient httpClient = new DefaultHttpClient();
                    File file = new File(imgPath);
                    String imgFrmt = "/9j/4AAQSkZJRgABAgAAZABkAAD/7AARRHVja3kAAQAEAAAAKAAA/+4ADkFkb2JlAGTAAAAAAf/bAIQADAgICAkIDAkJDBELCgsRFQ8MDA8VGBMTFRMTGBcSFBQUFBIXFxscHhwbFyQkJyckJDUzMzM1Ozs7Ozs7Ozs7OwENCwsNDg0QDg4QFA4PDhQUEBEREBQdFBQVFBQdJRoXFxcXGiUgIx4eHiMgKCglJSgoMjIwMjI7Ozs7Ozs7Ozs7/8AAEQgBkAGQAwEiAAIRAQMRAf/EAGoAAQEAAwEBAAAAAAAAAAAAAAABAgMEBQYBAQAAAAAAAAAAAAAAAAAAAAAQAQACAQEECwACAwEBAAAAAAABAgMRMRJSBCFBUWFxgZEyEzMUsSKhcoLRUxEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8A+zAAAAAAFAAAAABQAAAAAAAAAUBBQEFAQUBAAAAAAEUBBQEAAABBQEAAAAAABQRQAAAFAAAAABQEUAAABQEFAQUBBQEFAQUBAAEUBBUAAAABBQEAARQEAABQAAAAFAAAAFAAAFAAAAUEFAQUAAAAARQEFAQVAAAQUBAAQVAAAEUBAAQABUUAABQAAAUAAUEUABQAFBBQAFBBQEFNAQXQBBQEFQEFAQVAQUBAAQVAEUBAAElUBBUAVFgBQAAAUABQAFABQRQAFABQEUABQEFAQUBBQGIoCCgMRQERQEFQEFQEFARFARFQBFQBQBQAFABQABQAUAFABQAUEUUEFABQEFAQXQ0BBQGIugCIyQEFQEFQEFQEFQEFQBFARFQERUBQAUFABQAUAFABQFABRQRRQTRRQTQUA0FAQ0UBBWVMWS/trr39QMB105LryT5Q235bH8Vq0rpOnRPXqDzjRlogIjJARGSAiMkBEUBiKgIKgIKgIioCAAKigoAKACgoAKAqKAooAKAoAKKCCgA2UwZL7I6O2XRTk6x03nXugHJFZmdIjWW6nKZLe7+sd+111pWsaViIZA005XFXpmN6e9t002K1X5jFTr1nsgG0c1eam2SI0iKy6QefzOPcyz2T0w1O3m6b1It11/hxgiKAiKAxFQERkgIioCCoCIoCSiygIioAqKCqigKigKACigKigKKAoAKaN2Plst+rdjtkGpa0tadKxMuynKY6+7+0/wCG6IiI0iNIBy05O09N507trfTBipsjWe2elsSZiOmegFGm/M467P7T3NN+YyW2f1juB1WyUp7p0aL831Ujzlz7doDK+S9/dPl1MFAR34r7+OLdfW4W/lL6TNJ6+mAdNoi1ZidkvOtWa2ms7Yl6Tk5uml4txfyDmFQERkgIigMUZICIqAiMkBEVASUlZSQRFQBUUFhUUBQBQUBUUFVGVYmZ0iNZ7IBFb8fJZbdNv6R37XTj5TFTbG9PbIOKmLJf21me/qdOPkv/AKW8odWxQYUxY6e2unf1swAYXzY6bZ6eyGN8V77bzEdkQ1/jjiBL81afZGnfLTa1rT/aZl0fkjiPyRxA5ldH5I4j8scQOcdH5Y4j8scQOcdH5Y4j8scQOdaW3bxaOpv/ACxxH5I4gb4nWNWGem/jntjphlSu7WK666dbIHmo655SJmZi2ifjji/wDkHX+KOL/Cfijj/wDkR0Z+WjFTe3tenRoBijJARFQERUBBUBJYyylJBjKLKAKigyABVRQFRQUFB1cpy+LLWbWmZmJ9rtpjpSNKViPBwcnk3M0ROy/R5vRAGrmMl8WPerGvTpOvU4b5smT3WmY7OoHbfmcVOjXenshp/Xe14isbsTMd8uZnj99fGP5B6QNXM2tXHE1nSdQbRw/Pl4pX5svFIO0cXzZeKT5svFIO0cfzZeKT5cvFIOwcfzZeKT5svFIOwcfzZeKT5svFIOwcfy5eKT5svFIOwcXzZeKT5svFIO0cXzZeKU+fLxSDuHDXNl3ojena7gaOc+nzhwu7nPp84cIIioCIqAgqAiKgJKSspIMZRZQBkxZAoAKqKAqKCqigsTMTExtjY9XFeMmOt464eU7eQydFsc9XTAOnJSL0tWeuHmTExMxO2Nr1XBzePdy70bLdPmDSzx++vjDBnj99fGAek0c39ceLe0c39ceIOVUUBW/Fy8TG9fr2Q2/Dj2bsA41bcuDdjers64agAAAAEVAEVAEVAK+6PF6Lzq+6PGHog0c59PnDhd3OfT5w4QQVARFQERUBEVASUlZSQYoqAqsVBkACqigKigqooK2YMnx5a26tdJ8Ja1B67RzdN7FM9deleVyb+GO2OifJtmNY0kHls8fvr4wmSm5e1eyVx++vjH8g9Jo5v648W9o5v648QcrPHETkrE7NWDKtt20W7Ad4lbRasWjZKgk9PQ4pjSZjsdl7xSs2nycYAAALSs3tFYBswY96d6fbDDJTcvMdXV4OutYrERGyGvPTeprG2oOVFSYnTUBFQCvujxh6Lzq+6PF6INHOfT5w4Xdzn0+cOEBABEVARFQERUBJSVlJBiioAqKCqigqoAqooKqAMlYqDq5LJu5JpOy2zxh3PJpaa2i0bYnV6tbRasWjZMag5edp0xeOvolox++vjH8u/NTfx2r19Xi8/H76+MfyD02jm/rjxb2jm/qjxByiKDOmS9J/rPk2fqvpshpAZWva862nVEUFEAV1Yce5Xp907Wrl8es787I2OkAAGuuHHE66a+KcxXXH/r0tqTGsTE9YPPFtG7MxO2GILX3R4vRedX3R4vRBo5z6fOHC7uc+nzhwgiKgIioAgAiKgJKSspIMZRZQBUUFhUhQURQVUAVUUFVAGTu5LJvY5pO2uzwlwN3LZNzLE9U9E+YPScOWm5zMdlpiY9Xc08xTXcvG2to9NQbmjm/qjx/wDW9o5z6o/2/wDQcgigojPFjtktpGzrkGWLFOS3ZEbZZZMFqdMdNXTSsUrFa7IZA89njpN7adXXLfl5et+mv9bf4ZYscY66dc7ZBnEREaRshQAAAABx81Xdya8XS0uvm6644twz/LjBa++PF6Tza++vjD0gaOc+nzhwO7nfp84cICCAIAIioAgAksZZSxkElFlAFRQVUAVUUBUUFEUFVFBVYqD1MGT5MVbdeyfFntcfI5NJnHPX0w7QHPzn1R/tH8S6HPzv1R/tH8SDjVFrG9MRs17QZ46WyW3a+cu7HSuOu7HnLXi+HHXSLRr1y2fJj4o9QZDH5MfFHqfJj4o9QZDH5MfFHqfJj4o9QZDH5MfFHqfJj4o9QZDH5MfFHqfJj4o9QZDH5MfFHqfJj4o9QL13qTXth509HRL0Plx8Ueri5iKxltNZ1ienoBhT318Yem8unvr4w9QHPzv0+cOB3879P/UOABBAEVAQEARUBJSVlJBEVAFRQUAFEUFEUFVAFVAGQigzx3ml637JerExMRMbJeO9Hksm/h3Z206AdDn536o/2j+JdDDLirlru22a69APNHb+LD3+q/jw9/qDiHb+PD3+p+PD3+oOJXZ+PD3+p+TD3+oOMdn5MPf6n5MPf6g4x2fkw9/qfkw9/qDjR2/kw9/qfjw9/qDiHb+PD3+p+PD3+oOEd348Pf6p+LD3+oOOnvr4w9Rojk8MTExr0d7eDn576f8AqHnu/nvo/wCoeeACAIACACAgEoqAiKgAAKqAKqAKqAKqAKqAMhAGTo5LJuZorOy/R5uZYkHsjyPkvxT6m/fin1B648jfvxT6rv34p9QesPJ378U+q79+2QeqPK37dsm/bin1B6o8rfvxSb9+2QeqPK379sm/fin1B6o8rftxT6m/btkHqjyt+/bPqm/fin1B6w8nfvxT6pv34p9QeuPI378U+pv34p9Qd3P/AEf9Q89ZvaeiZmYYgCACAAgAIAIioCAACKCgAKigKgCqgCqgCqgCqgCqxUFVioKIAqsVBRAFEAVBAUQABAVBAVBAVAAQAEABAARUBEVAFhFBRFAABRFAVAFVAFEUFEUFEAVWKgogCiKCiAKIAqCAuogCoICiAAgACAAAIAAIAioAioAqKAACiKAAAqAKqAKIoCoAoigogCiKCiAKIAogCiAAICiAAACAACAqAAgAAAIqSAgAAAoAAAKIoAACoAoigKgCiKAqAKIAogCiAKIAogCiAAAAgCoAAICoAAAAIAACAAAAAAoigAAKgCiKAAAqAKIAoACoAogCiAKIAogCoAAAAgCoAAAAICoAAAAIAAAAAAAACiAKAAACiAKIoAAAAAAAAKIAogAAAAAAAAACAqAAAAAAIAAAAAAAAAAAAAAAogCgAAAAAKgCiAKIAogCiAKIAogCoAAAAAAAAgAAAAAAAAD/2Q==";
                    if (file.exists()) {
                        try {
                            Bitmap bitmap = ShrinkBitmap(imgPath, 1500, 1200);//decodeFile(params[0]);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                            byte[] byteFormat = bao.toByteArray();
                            imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                        } catch (Exception e) {

                        }
                    }

                    //MultipartEntity entity = new MultipartEntity();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);

                    entity.add(new BasicNameValuePair("FactId", loginUserDetailsList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("Village", plantingModel.getVillage()));
                    entity.add(new BasicNameValuePair("Grower", plantingModel.getGrower()));
                    entity.add(new BasicNameValuePair("PLOTVillage", plantingModel.getPLOTVillage()));
                    entity.add(new BasicNameValuePair("Irrigationmode", plantingModel.getIrrigationmode()));
                    entity.add(new BasicNameValuePair("SupplyMode", plantingModel.getSupplyMode()));
                    entity.add(new BasicNameValuePair("Harvesting", plantingModel.getHarvesting()));
                    entity.add(new BasicNameValuePair("Equipment", plantingModel.getEquipment()));
                    entity.add(new BasicNameValuePair("LandType", plantingModel.getLandType()));
                    entity.add(new BasicNameValuePair("SeedSource", plantingModel.getSeedSource()));
                    entity.add(new BasicNameValuePair("BasalDose", plantingModel.getBaselDose()));
                    entity.add(new BasicNameValuePair("SeedTreatment", plantingModel.getSeedTreatment()));
                    entity.add(new BasicNameValuePair("SMethod", plantingModel.getSmMethod()));
                    entity.add(new BasicNameValuePair("Dim1", plantingModel.getDim1()));
                    entity.add(new BasicNameValuePair("Dim2", plantingModel.getDim2()));
                    entity.add(new BasicNameValuePair("Dim3", plantingModel.getDim3()));
                    entity.add(new BasicNameValuePair("Dim4", plantingModel.getDim4()));
                    entity.add(new BasicNameValuePair("Area", plantingModel.getArea()));
                    entity.add(new BasicNameValuePair("LAT1", plantingModel.getLAT1()));
                    entity.add(new BasicNameValuePair("LON1", plantingModel.getLON1()));
                    entity.add(new BasicNameValuePair("LAT2", plantingModel.getLAT2()));
                    entity.add(new BasicNameValuePair("LON2", plantingModel.getLON2()));
                    entity.add(new BasicNameValuePair("LAT3", plantingModel.getLAT3()));
                    entity.add(new BasicNameValuePair("LON3", plantingModel.getLON3()));
                    entity.add(new BasicNameValuePair("LAT4", plantingModel.getLAT4()));
                    entity.add(new BasicNameValuePair("LON4", plantingModel.getLON4()));
                    entity.add(new BasicNameValuePair("SuperviserCode", plantingModel.getSuperviserCode()));
                    entity.add(new BasicNameValuePair("Image", imgFrmt));
                    //entity.add(new BasicNameValuePair("ID",plantingModel.getId()));
                    entity.add(new BasicNameValuePair("InsertLAT", plantingModel.getInsertLAT()));
                    entity.add(new BasicNameValuePair("InsertLON", plantingModel.getInsertLON()));
                    entity.add(new BasicNameValuePair("SprayType", plantingModel.getSprayType()));
                    entity.add(new BasicNameValuePair("PloughinType", plantingModel.getPloughingType()));
                    entity.add(new BasicNameValuePair("MANUALAREA", plantingModel.getManualArea()));
                    entity.add(new BasicNameValuePair("MOBILENO", plantingModel.getMobileNumber()));
                    entity.add(new BasicNameValuePair("MDATE", plantingModel.getmDate()));
                    entity.add(new BasicNameValuePair("VARIETY", plantingModel.getVARIETY()));
                    entity.add(new BasicNameValuePair("Crop", plantingModel.getCrop()));
                    entity.add(new BasicNameValuePair("PLANTINGTYPE", plantingModel.getPlantingType()));
                    entity.add(new BasicNameValuePair("PLANTATION", plantingModel.getPlantation()));
                    //entity.add(new BasicNameValuePair("OTP","0"));
                    entity.add(new BasicNameValuePair("SeedType", plantingModel.getSeedType()));
                    entity.add(new BasicNameValuePair("SEEDSET", plantingModel.getSeedSetType()));
                    entity.add(new BasicNameValuePair("SOILTR", plantingModel.getSoilTreatment()));

                    entity.add(new BasicNameValuePair("ROWTOROW", plantingModel.getRowToRowDistance()));
                    //entity.add(new BasicNameValuePair("ActualAreaType",plantingModel.getActualAreaType()));
                    entity.add(new BasicNameValuePair("ACKID", plantingModel.getColId()));
                    //entity.add(new BasicNameValuePair("IMAREA",plantingModel.getManualArea()));
                    //entity.add(new BasicNameValuePair("SVillage",plantingModel.getSeedVillage()));
                    //entity.add(new BasicNameValuePair("SGrower",plantingModel.getSeedGrower()));
                    //entity.add(new BasicNameValuePair("SVariety",plantingModel.getVARIETY()));
                    //entity.add(new BasicNameValuePair("STransporter",plantingModel.getSeedTransporter()));
                    //entity.add(new BasicNameValuePair("SDistance",plantingModel.getSeedDistance()));
                    //entity.add(new BasicNameValuePair("SSeedQty",plantingModel.getSeedQuantity()));
                    //entity.add(new BasicNameValuePair("Rate",plantingModel.getSeedRate()));
                    //entity.add(new BasicNameValuePair("OtherAmount",plantingModel.getSeedOtherAmount()));
                    //entity.add(new BasicNameValuePair("PayAmount",plantingModel.getSeedPayAmount()));
                    //entity.add(new BasicNameValuePair("PayMod",plantingModel.getSeedPayMode()));
                    //entity.add(new BasicNameValuePair("MillPurchy",plantingModel.getMillPurchey()));
                    entity.add(new BasicNameValuePair("PITEMJSON", PITEMJSON));
                    entity.add(new BasicNameValuePair("PEQUIJSON", PEQUIJSON));
                    entity.add(new BasicNameValuePair("INDVPLOTNO", plantingModel.getId()));
                    entity.add(new BasicNameValuePair("VPLOTNO", plantingModel.getPlotSerialNumber()));
                    entity.add(new BasicNameValuePair("ISIDEAL", plantingModel.getIsIdeal()));
                    entity.add(new BasicNameValuePair("ISNARSARI", plantingModel.getIsNursery()));
                    entity.add(new BasicNameValuePair("SEEDBAGQTY", plantingModel.getSeedBagQty()));
                    //entity.add(new BasicNameValuePair("PAREAACRE",plantingModel.getManualAreaAcre()));

                    String debugStr = new MiscleniousUtil().ListNameValueToString("SAVEPLANTINGOFFLINE", entity);
                    Log.d("", entity.toString());
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    dbh.updateServerStatusPlanting(jsonObject.getString("EXCEPTIONMSG").replace("'", ""), jsonObject.getString("ACKID"), jsonObject.getString("SAVEMSG"));
                    System.out.println("" + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //	e.printStackTrace();
                    //System.out.println(e);
                    //new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java"+e.toString());
                }
            }
        });

        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void storeCaneSurveyData() {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message = "";
                    String imei = new GetDeviceImei((getApplicationContext())).GetDeviceImeiNumber();
                    SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_UpLoadSurveyNew);
                    //request1.addProperty("EMEI", "864547042872734");//29-04-2022 11:48:22
                    //request1.addProperty("imeiNo", "355844090708282");
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    //Date today = Calendar.getInstance().getTime();
                    Date dateObj2 = sdfIn.parse(uploadPlotSurveyModel.get(0).getInsertDate() + " 00:00:00");
                    String currDate = dateFormat.format(dateObj2);
                    request1.addProperty("FactId", userDetailsModelList.get(0).getDivision());
                    request1.addProperty("supvcd", uploadFarmerShareModel.get(0).getSupCode());
                    request1.addProperty("survdttm", currDate);
                    request1.addProperty("survvillcd", uploadPlotSurveyModel.get(0).getVillageCode());
                    request1.addProperty("plotsrno", uploadPlotSurveyModel.get(0).getPlotSrNo());
                    request1.addProperty("grwvillcd", uploadFarmerShareModel.get(0).getVillageCode());
                    request1.addProperty("farmcd", uploadFarmerShareModel.get(0).getGrowerCode());
                    request1.addProperty("isgpspts", "0");
                    request1.addProperty("north", uploadPlotSurveyModel.get(0).getEastNorthDistance().replace(".0", ""));
                    request1.addProperty("east", uploadPlotSurveyModel.get(0).getEastSouthDistance().replace(".0", ""));
                    request1.addProperty("south", uploadPlotSurveyModel.get(0).getWestSouthDistance().replace(".0", ""));
                    request1.addProperty("west", uploadPlotSurveyModel.get(0).getWestNorthDistance().replace(".0", ""));
                    request1.addProperty("plotshareperc", uploadFarmerShareModel.get(0).getShare());
                    request1.addProperty("shareno", uploadFarmerShareModel.get(0).getSrNo());
                    request1.addProperty("vrtcd", uploadPlotSurveyModel.get(0).getVarietyCode());
                    request1.addProperty("canetycd", uploadPlotSurveyModel.get(0).getCaneType());
                    request1.addProperty("mobno", "");
                    request1.addProperty("remark", uploadFarmerShareModel.get(0).getColId());
                    request1.addProperty("gastino", uploadPlotSurveyModel.get(0).getGhashtiNumber());
                    request1.addProperty("farmnm", uploadFarmerShareModel.get(0).getGrowerName());
                    request1.addProperty("fathnm", uploadFarmerShareModel.get(0).getGrowerFatherName());
                    request1.addProperty("aadharno", uploadFarmerShareModel.get(0).getGrowerAadharNumber());
                    request1.addProperty("area", uploadPlotSurveyModel.get(0).getAreaHectare());
                    request1.addProperty("plntmthcd", uploadPlotSurveyModel.get(0).getPlantMethod());
                    request1.addProperty("cropcondcd", uploadPlotSurveyModel.get(0).getCropCondition());
                    request1.addProperty("disecd", uploadPlotSurveyModel.get(0).getDisease());
                    request1.addProperty("socclerkcd", uploadFarmerShareModel.get(0).getSupCode());
                    request1.addProperty("plantationdt", uploadPlotSurveyModel.get(0).getPlantDate());
                    request1.addProperty("irrcd", uploadPlotSurveyModel.get(0).getIrrigation());
                    request1.addProperty("soilcd", uploadPlotSurveyModel.get(0).getSoilType());
                    request1.addProperty("landcd", uploadPlotSurveyModel.get(0).getLandType());
                    request1.addProperty("bordercropcd", uploadPlotSurveyModel.get(0).getBorderCrop());
                    request1.addProperty("plottycd", uploadPlotSurveyModel.get(0).getPlotType());
                    request1.addProperty("intercropcd", uploadPlotSurveyModel.get(0).getInterCrop());
                    request1.addProperty("mixcropcd", uploadPlotSurveyModel.get(0).getMixCrop());
                    request1.addProperty("insectcd", uploadPlotSurveyModel.get(0).getInsect());
                    request1.addProperty("seedsourcecd", uploadPlotSurveyModel.get(0).getSeedSource());
                    request1.addProperty("fort", "");
                    request1.addProperty("isautumn", "0");
                    request1.addProperty("sentsms", "0");
                    request1.addProperty("isupdt", "0");
                    request1.addProperty("nelatdeg", uploadPlotSurveyModel.get(0).getEastNorthLat());
                    request1.addProperty("nelngdeg", uploadPlotSurveyModel.get(0).getEastNorthLng());
                    request1.addProperty("selatdeg", uploadPlotSurveyModel.get(0).getEastSouthLat());
                    request1.addProperty("selngdeg", uploadPlotSurveyModel.get(0).getEastSouthLng());
                    request1.addProperty("swlatdeg", uploadPlotSurveyModel.get(0).getWestSouthLat());
                    request1.addProperty("swlngdeg", uploadPlotSurveyModel.get(0).getWestSouthLng());
                    request1.addProperty("nwlatdeg", uploadPlotSurveyModel.get(0).getWestNorthLat());
                    request1.addProperty("nwlngdeg", uploadPlotSurveyModel.get(0).getWestNorthLng());
                    request1.addProperty("KhasaraNo", uploadPlotSurveyModel.get(0).getKhashraNo());
                    request1.addProperty("GataNo", uploadPlotSurveyModel.get(0).getGataNo());
                    if (uploadPlotSurveyModel.get(0).getIsDelete() != null) {
                        if (uploadPlotSurveyModel.get(0).getIsDelete().equalsIgnoreCase("1")) {
                            request1.addProperty("ISDELETE", uploadPlotSurveyModel.get(0).getIsDelete());
                        } else {
                            request1.addProperty("ISDELETE", "0");
                        }
                    } else {
                        request1.addProperty("ISDELETE", "0");
                    }

                    request1.addProperty("PTYPE", uploadPlotSurveyModel.get(0).getPlotType());
                    request1.addProperty("PNO", uploadPlotSurveyModel.get(0).getOldPlotId());
                    request1.addProperty("MachineID", imei);
                    Log.d("", "doInBackground: " + request1);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request1);
                    envelope.implicitTypes = true;
                    // Web method call
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 20000);
                    androidHttpTransport.debug = true;
                    androidHttpTransport.call(APIUrl.SOAP_ACTION_UpLoadSurveyNew, envelope);
                    if (envelope.bodyIn instanceof SoapFault) {
                        SoapFault sf = (SoapFault) envelope.bodyIn;
                        message = sf.getMessage();
                    } else {
                        SoapObject result = (SoapObject) envelope.bodyIn;
                        message = result.getPropertyAsString("UpLoadSurveyNewResult").toString();
                    }
                    JSONObject jsonObject = new JSONObject(message);
                    System.out.println(message);
                    if (message.equals("")) {

                    } else {
                        try {
                            DBHelper dbh = new DBHelper(getApplicationContext());
                            dbh.updateServerFarmerShareModel(jsonObject.getString("REMARK").replace("'", ""), jsonObject.getString("STATUS"), jsonObject.getString("MESSAGE").replace("'", ""));
                            //dbh.deleteGpsDataModel(response);
                            //checkExec=true;
                        } catch (Exception e) {
                            //new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java"+e.toString());
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.toString());
                }
            }
        });
        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void savePlotActivity() {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message;
                    GetDeviceImei getDeviceImei = new GetDeviceImei(getApplicationContext());
                    List<PlotActivitySaveModel> plotActivitySaveModels = dbh.getPlotActivitySaveModel("Pending");
                    List<UserDetailsModel> loginUserDetailsList = dbh.getUserDetailsModel();
                    PlotActivitySaveModel plotActivitySaveModel = plotActivitySaveModels.get(0);
                    File sdCard = Environment.getExternalStorageDirectory();
                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEPLOTACTIVITY";
                    HttpClient httpClient = new DefaultHttpClient();

                    List<NameValuePair> entity = new LinkedList<NameValuePair>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);

                    entity.add(new BasicNameValuePair("FactId", loginUserDetailsList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("PlotType", plotActivitySaveModel.getPlotType()));
                    entity.add(new BasicNameValuePair("Village", plotActivitySaveModel.getVillageCode()));
                    entity.add(new BasicNameValuePair("Grower", plotActivitySaveModel.getGrwerCode()));
                    entity.add(new BasicNameValuePair("PLOTVillage", plotActivitySaveModel.getPlotVillage()));
                    entity.add(new BasicNameValuePair("PlotSerialNumber", plotActivitySaveModel.getPlotSerialNumber()));
                    entity.add(new BasicNameValuePair("Area", plotActivitySaveModel.getArea()));
                    entity.add(new BasicNameValuePair("data", plotActivitySaveModel.getJsonArrayData()));
                    entity.add(new BasicNameValuePair("SupCode", loginUserDetailsList.get(0).getCode()));
                    entity.add(new BasicNameValuePair("mobileNumber", plotActivitySaveModel.getMobileNumber()));
                    entity.add(new BasicNameValuePair("Season", "2023-2024"));
                    entity.add(new BasicNameValuePair("AckId", plotActivitySaveModel.getColId()));
                    entity.add(new BasicNameValuePair("CurrentDate", plotActivitySaveModel.getCurrentDate()));
                    entity.add(new BasicNameValuePair("IMINO", new GetDeviceImei(getApplicationContext()).GetDeviceImeiNumber()));
                    entity.add(new BasicNameValuePair("SURTYPE", plotActivitySaveModel.getSurveyType()));
                    entity.add(new BasicNameValuePair("OLDSEAS", plotActivitySaveModel.getOldSeason()));
                    entity.add(new BasicNameValuePair("OLDGHID", plotActivitySaveModel.getOldGhid()));
                    entity.add(new BasicNameValuePair("MEETINGTYPE", plotActivitySaveModel.getMeetingType()));
                    entity.add(new BasicNameValuePair("MEETINGNAME", plotActivitySaveModel.getMeetingName()));
                    entity.add(new BasicNameValuePair("MEETINGNUMBER", plotActivitySaveModel.getMeetingNumber()));

                    String debugData = new MiscleniousUtil().ListNameValueToString("SAVEPLOTACTIVITY", entity);
                    Log.d("", entity.toString());
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    dbh.updateServerStatusPlotActivitySaveModel(jsonObject.getString("EXCEPTIONMSG").replace("'", ""), jsonObject.getString("ACKID"), jsonObject.getString("SAVEMSG"));
                    System.out.println("" + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println(e.toString());
                    //new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java"+e.toString());
                }
            }
        });

        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void storeMaterialData(final List<AgriInputFormModel> agriInputFormModels) {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message;
                    GetDeviceImei getDeviceImei = new GetDeviceImei(getApplicationContext());
                    AgriInputFormModel agriInputFormModel = agriInputFormModels.get(0);
                    Gson gson = new Gson();

                    List<UserDetailsModel> loginUserDetailsList = dbh.getUserDetailsModel();

                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEAGRIINPUTDATA";
                    //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                    //int i = Integer.parseInt(params[0]);
                    HttpClient httpClient = new DefaultHttpClient();


                    //MultipartEntity entity = new MultipartEntity();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();

                    Bitmap bitmapPhoto1 = dbh.getRetrivePhoto1(agriInputFormModel.getAgriVillageCode(), agriInputFormModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoPhoto1 = new ByteArrayOutputStream();
                    bitmapPhoto1.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto1);
                    byte[] byteFormatPhoto1 = baoPhoto1.toByteArray();
                    String photo1 = Base64.encodeToString(byteFormatPhoto1, Base64.NO_WRAP);

                    Bitmap bitmapPhoto2 = dbh.getRetrivePhoto2(agriInputFormModel.getAgriVillageCode(), agriInputFormModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoPhoto2 = new ByteArrayOutputStream();
                    bitmapPhoto2.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto2);
                    byte[] byteFormatPhoto2 = baoPhoto2.toByteArray();
                    String photo2 = Base64.encodeToString(byteFormatPhoto2, Base64.NO_WRAP);

                    Bitmap bitmapSignature = dbh.getRetriveSignature(agriInputFormModel.getAgriVillageCode(), agriInputFormModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoSignature = new ByteArrayOutputStream();
                    bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100, baoSignature);
                    byte[] byteFormatSignature = baoSignature.toByteArray();
                    String Signature = Base64.encodeToString(byteFormatSignature, Base64.NO_WRAP);

                    /*Bitmap bitmapThumb = dbh.getRetriveThumb(agriInputFormModel.getAgriVillageCode(), agriInputFormModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoThumb = new ByteArrayOutputStream();
                    bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 100, baoThumb);
                    byte[] byteFormatThumb = baoThumb.toByteArray();
                    String Thumb = Base64.encodeToString(byteFormatThumb, Base64.NO_WRAP);*/


                    List<AddMaterialDataModel> addMaterialDataModels = dbh.getAllAgriInputDataMaterialById(agriInputFormModel.getAgri_input_Id());
                    String MATERIAL = gson.toJson(addMaterialDataModels);

                    entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));

                    //entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getFactoryCode()));
                    entity.add(new BasicNameValuePair("IMEI_NO", new GetDeviceImei(getApplicationContext()).GetDeviceImeiNumber()));
                    entity.add(new BasicNameValuePair("VILLAGE_CODE", agriInputFormModel.getAgriVillageCode()));
                    entity.add(new BasicNameValuePair("GROWER_CODE", agriInputFormModel.getAgriGrowerCode()));
                    entity.add(new BasicNameValuePair("SuperviserCode", userDetailsModelList.get(0).getCode()));
                    entity.add(new BasicNameValuePair("PHOTO", photo1));
                    entity.add(new BasicNameValuePair("DOCUMENT", photo2));
                    entity.add(new BasicNameValuePair("SIGNATURE", Signature));
                    entity.add(new BasicNameValuePair("THUMB", "")); //Thumb
                    entity.add(new BasicNameValuePair("MATERIAL", MATERIAL));
                    entity.add(new BasicNameValuePair("ACKID", "" + agriInputFormModel.getAgri_input_Id()));
                    entity.add(new BasicNameValuePair("APPCRDATE", "" + agriInputFormModel.getCreated_at()));
                    //entity.add(new BasicNameValuePair("PAREAACRE",plantingModel.getManualAreaAcre()));

                    String debugStr = new MiscleniousUtil().ListNameValueToString("SAVEAGRIINPUTDATA", entity);

                    Log.d("", entity.toString());
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    dbh.updateAgriInputFormModel(jsonObject.getString("EXCEPTIONMSG").replace("'", ""), jsonObject.getString("ACKID"), jsonObject.getString("SAVEMSG"));
                    System.out.println("" + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //	e.printStackTrace();
                    //System.out.println(e);
                    new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java" + e.toString());
                }
            }
        });

        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void saveGowerDocument(final List<SaveGrowerDocumentModel> saveGrowerDocumentModels) {
        Thread test = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String message;
                    GetDeviceImei getDeviceImei = new GetDeviceImei(getApplicationContext());
                    SaveGrowerDocumentModel saveGrowerDocumentModel = saveGrowerDocumentModels.get(0);
                    Gson gson = new Gson();
                    List<UserDetailsModel> loginUserDetailsList = dbh.getUserDetailsModel();

                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/SAVEGROWERDOCUMENTDATA";
                    //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                    //int i = Integer.parseInt(params[0]);
                    HttpClient httpClient = new DefaultHttpClient();
                    //MultipartEntity entity = new MultipartEntity();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();

                    Bitmap bitmapPhoto1 = dbh.getRetrivePhoto3(saveGrowerDocumentModel.getAgriVillageCode(), saveGrowerDocumentModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoPhoto1 = new ByteArrayOutputStream();
                    bitmapPhoto1.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto1);
                    byte[] byteFormatPhoto1 = baoPhoto1.toByteArray();
                    String photo1 = Base64.encodeToString(byteFormatPhoto1, Base64.NO_WRAP);

                    Bitmap bitmapPhoto2 = dbh.getRetrivePhoto4(saveGrowerDocumentModel.getAgriVillageCode(), saveGrowerDocumentModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoPhoto2 = new ByteArrayOutputStream();
                    bitmapPhoto2.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto2);
                    byte[] byteFormatPhoto2 = baoPhoto2.toByteArray();
                    String photo2 = Base64.encodeToString(byteFormatPhoto2, Base64.NO_WRAP);

                    Bitmap bitmapSignature = dbh.getRetriveSignature1(saveGrowerDocumentModel.getAgriVillageCode(), saveGrowerDocumentModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoSignature = new ByteArrayOutputStream();
                    bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100, baoSignature);
                    byte[] byteFormatSignature = baoSignature.toByteArray();
                    String Signature = Base64.encodeToString(byteFormatSignature, Base64.NO_WRAP);

                    Bitmap bitmapThumb = dbh.getRetriveThumb1(saveGrowerDocumentModel.getAgriVillageCode(), saveGrowerDocumentModel.getAgriGrowerCode());
                    ByteArrayOutputStream baoThumb = new ByteArrayOutputStream();
                    bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 100, baoThumb);
                    byte[] byteFormatThumb = baoThumb.toByteArray();
                    String Thumb = Base64.encodeToString(byteFormatThumb, Base64.NO_WRAP);
                   /* List<AddMaterialDataModel> addMaterialDataModels = dbh.getAllAgriInputDataMaterialById(saveGrowerDocumentModel.getAgri_input_Id());
                    String MATERIAL = gson.toJson(addMaterialDataModels);*/

                    entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                    //entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getFactoryCode()));
                    entity.add(new BasicNameValuePair("IMEI_NO", new GetDeviceImei(getApplicationContext()).GetDeviceImeiNumber()));
                    entity.add(new BasicNameValuePair("VILLAGE_CODE", saveGrowerDocumentModel.getAgriVillageCode()));
                    entity.add(new BasicNameValuePair("GROWER_CODE", saveGrowerDocumentModel.getAgriGrowerCode()));
                    entity.add(new BasicNameValuePair("SuperviserCode", userDetailsModelList.get(0).getCode()));
                    entity.add(new BasicNameValuePair("PHOTO", photo1));
                    entity.add(new BasicNameValuePair("DOCUMENT", photo2));
                    entity.add(new BasicNameValuePair("SIGNATURE", Signature));
                    entity.add(new BasicNameValuePair("THUMB", Thumb));
                    entity.add(new BasicNameValuePair("ACKID", "" + saveGrowerDocumentModel.getAgri_input_Id()));
                    entity.add(new BasicNameValuePair("APPCRDATE", "" + saveGrowerDocumentModel.getCreated_at()));
                    //entity.add(new BasicNameValuePair("PAREAACRE",plantingModel.getManualAreaAcre()));

                    String debugStr = new MiscleniousUtil().ListNameValueToString("SAVEGROWERDOCUMENTDATA", entity);

                    Log.d("", entity.toString());
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    dbh.updateSaveGrowerDocumentModel(jsonObject.getString("EXCEPTIONMSG").replace("'", ""), jsonObject.getString("ACKID"), jsonObject.getString("SAVEMSG"));
                    System.out.println("" + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //	e.printStackTrace();
                    //System.out.println(e);
                    new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java" + e.toString());
                }
            }
        });

        if (new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    protected void startLocationUpdates() {

        try {
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

            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            try {

                                Location location1 = locationResult.getLastLocation();

                                //t_master_latlng.setText(""+locationResult.getLastLocation().getAccuracy());
                                if (location1 != null) {
                                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                    if (location1 == null) {
                                        fakeLocation = false;
                                        location = location1;
                                    } else {
                                        if (location1.isFromMockProvider()) {
                                            fakeLocation = true;
                                            location = location1;
                                            //t1.speak(getApplicationContext().getString(R.string.MSG_FAKE_LOCATION), TextToSpeech.QUEUE_FLUSH, null);
                                            //new AlertDialogManager().RedDialog(getApplicationContext(),"Warning: fake location detected please disable fake location");
                                        } else {
                                            fakeLocation = false;
                                            location = location1;
                                        }
                                    }
                                }
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    },
                    Looper.myLooper());
        }
        catch (SecurityException e)
        {

        }
    }

    private void createNotificationIcon()
    {
        try {
            DBHelper dbh = new DBHelper(getApplicationContext());
            List<UserDetailsModel> userDetailsModels=dbh.getUserDetailsModel();
            String address = "";
            if(fakeLocation)
            {
                t1.speak("fake location detected please disable fake location", TextToSpeech.QUEUE_FLUSH, null);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currentDt = dateFormat.format(today);
                GpsDataModel gpsDataModel = new GpsDataModel();
                gpsDataModel.setLat("0" );
                gpsDataModel.setLng("0");
                gpsDataModel.setBearing("0");
                gpsDataModel.setAccuracy("0");
                gpsDataModel.setSpeed("0");
                gpsDataModel.setAddress("Fake Location Detected");
                gpsDataModel.setProvider("Fake Location");
                gpsDataModel.setGpsStatus("" + 0);
                gpsDataModel.setCreatedAt("" + currentDt);
                gpsDataModel.setBattery("0" );
                gpsDataModel.setCharging("");
                gpsDataModel.setInternetStatus(""+new GetDeviceImei(getApplicationContext()).isMobileDataEnabled());
                gpsDataModel.setAppVersion("" + BuildConfig.VERSION_NAME);
                gpsDataModel.setDistance("0.00");
                gpsDataModel.setOvertime_status("" + userDetailsModels.get(0).getOvertimeStatus());
                //gpsDataModel.setUserName(userDetailsModels.get(0).getUserId());
                dbh.insertGpsDataModel(gpsDataModel);
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("HHmm");
                String currentTm = dateFormat1.format(today);
                int currentTmInt= Integer.parseInt(currentTm);
            }
            else
            {
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

                    boolean isCharging = level == BatteryManager.BATTERY_STATUS_CHARGING ||
                            level == BatteryManager.BATTERY_STATUS_FULL;
                    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                    String status = usbCharge + "Charging By -- " + acCharge + " -- Adapter" + isCharging;
                    if (acCharge) {
                        status = "A.C. Charger Connected";
                    } else if (usbCharge) {
                        status = "USB Charger Connected";
                    } else {
                        status = "Charger Not Connected";
                    }

                    final boolean gpsenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if(gpsenable)
                    {
                        List<Address> addresses=new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date today = Calendar.getInstance().getTime();
                        String currentDt = dateFormat.format(today);
                        GpsDataModel gpsDataModel = new GpsDataModel();
                        if(location==null)
                        {
                            startLocationUpdates();
                        }
                        else
                        {
                            if(new InternetCheck(getApplicationContext()).isOnline())
                            {
                                Geocoder geocoder;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            }
                            if (addresses != null && addresses.size() > 0) {
                                gpsDataModel.setAddress(addresses.get(0).getAddressLine(0).replace("'"," "));
                            }
                            else {
                                gpsDataModel.setAddress("Internet not working");
                            }
                            double distance=0.00;
                            List<LastGpsDataModel> gpsRecord=dbh.gpsLastRecord();
                            if(gpsRecord.size()>0)
                            {
                                Location oldLoc=new Location(LocationManager.GPS_PROVIDER);
                                oldLoc.setLatitude(Double.parseDouble(gpsRecord.get(0).getLat()));
                                oldLoc.setLongitude(Double.parseDouble(gpsRecord.get(0).getLng()));
                                distance=oldLoc.distanceTo(location);
                            }
                            gpsDataModel.setLat("" + location.getLatitude());
                            gpsDataModel.setLng("" + location.getLongitude());
                            gpsDataModel.setBearing("" + new DecimalFormat("0").format(location.getBearing()));
                            gpsDataModel.setAccuracy("" + new DecimalFormat("0.00").format(location.getAccuracy()));
                            gpsDataModel.setSpeed("" + new DecimalFormat("0.00").format(location.getSpeed()));
                            gpsDataModel.setProvider(location.getProvider());
                            gpsDataModel.setGpsStatus("" + 1);
                            gpsDataModel.setCreatedAt("" + currentDt);
                            gpsDataModel.setBattery("" + level);
                            gpsDataModel.setCharging("" + status);
                            gpsDataModel.setInternetStatus(""+new GetDeviceImei(getApplicationContext()).isMobileDataEnabled());
                            gpsDataModel.setAppVersion("" + BuildConfig.VERSION_NAME);
                            gpsDataModel.setDistance("" + new DecimalFormat("0.00").format(distance));
                            gpsDataModel.setOvertime_status("" + userDetailsModels.get(0).getOvertimeStatus());
                            //gpsDataModel.setUserName(userDetailsModels.get(0).getUserId());
                            dbh.insertGpsDataModel(gpsDataModel);
                        }
                    }
                    else
                    {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date today = Calendar.getInstance().getTime();
                            String currentDt = dateFormat.format(today);
                            GpsDataModel gpsDataModel = new GpsDataModel();
                            gpsDataModel.setLat("0.0" );
                            gpsDataModel.setLng("0.0");
                            gpsDataModel.setBearing("0");
                            gpsDataModel.setAccuracy("0");
                            gpsDataModel.setSpeed("0");
                            gpsDataModel.setAddress("Gps Disabled");
                            gpsDataModel.setProvider("0");
                            gpsDataModel.setGpsStatus("" + 0);
                            gpsDataModel.setCreatedAt("" + currentDt);
                            gpsDataModel.setBattery("" + level);
                            gpsDataModel.setCharging("" + status);
                            gpsDataModel.setInternetStatus(""+new GetDeviceImei(getApplicationContext()).isMobileDataEnabled());
                            gpsDataModel.setAppVersion("" + BuildConfig.VERSION_NAME);
                            gpsDataModel.setDistance("0.00");
                            gpsDataModel.setOvertime_status("" + userDetailsModels.get(0).getOvertimeStatus());
                            //gpsDataModel.setUserName(userDetailsModels.get(0).getUserId());

                            dbh.insertGpsDataModel(gpsDataModel);
                            //t1.speak(getApplicationContext().getString(R.string.MSG_GPS_OFF), TextToSpeech.QUEUE_FLUSH, null);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HHmm");
                            String currentTm = dateFormat1.format(today);
                            int currentTmInt= Integer.parseInt(currentTm);
                        }
                        catch (Exception e)
                        {
                            //.e("","Error:"+e.toString());
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.e("","Error:"+e.toString());
                }
            }
            saveDataOnServer();
            //Toast.makeText(getApplicationContext(),"Console", Toast.LENGTH_SHORT).show();

        }
        catch(SecurityException e)
        {

        }
        catch(Exception e)
        {

        }
    }

    private void saveDataOnServer()
    {
        try{
            DBHelper dbh = new DBHelper(getApplicationContext());
            List<GpsDataModel> gpsDataModelList=dbh.getGpsDataModel("100");
            if(gpsDataModelList.size()>0)
            {
                Map<String,Object> map=new HashMap<>();
                map.put("imei",new GetDeviceImei(getApplicationContext()).GetDeviceImeiNumber());
                map.put("data",gpsDataModelList);
                List<Object> lst=new ArrayList<>();
                lst.add(map);
                Gson gson=new Gson();
                String Data=gson.toJson(lst);
                if(new InternetCheck(getApplicationContext()).isOnline())
                {
                    JSONArray jsa=new JSONArray(Data);
                    if(jsa.length()>0)
                    {
                        dbh.updateServerSentTime();
                        JSONObject jso=jsa.getJSONObject(0);
                        SaveData(jso.toString());
                    }
                    //new CheckAuth().execute(Data);
                }
                else
                {
                    dbh.updateError("Internet not working");
                    /*Intent intent=new Intent(getApplicationContext(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    private void SaveData(String Data)
    {
        Thread test=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    DBHelper dbh = new DBHelper(getApplicationContext());
                    String message;
                    String url= APIUrl.BASE_URL+"/SaveLocation";

                    Log.d("MyLocation Save :","SaveLocation ---> :"+url);

                    HttpClient httpClient = new DefaultHttpClient();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();

                    entity.add(new BasicNameValuePair("LocData",Data));
                    entity.add(new BasicNameValuePair("Divn", ""+userDetailsModelList.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("Seas", ""+new SessionConfig(getApplicationContext()).getSeason()));
                    Log.d("",entity.toString());
                    HttpPost httpPost = new HttpPost(url);

                    // Set authorization header
                    // httpPost.setHeader("Authorization", "Bearer <your_token_here>");

                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");


                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));

                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    JSONObject jsonObject = new JSONObject(message);
                    System.out.println(message);
                    //dbh.updateServerStatusPlotActivitySaveModel(jsonObject.getString("EXCEPTIONMSG").replace("'",""),jsonObject.getString("ACKID"),jsonObject.getString("SAVEMSG"));
                    //{"MSG":"DATA SAVE","APISTATUS":"OK","ACTID1":[{"ACTID":"1"},{"ACTID":"2"},{"ACTID":"3"},{"ACTID":"4"},{"ACTID":"5"},{"ACTID":"6"},{"ACTID":"7"},{"ACTID":"8"},{"ACTID":"9"}]}
                    try{
                        if(jsonObject.getString("APISTATUS").equalsIgnoreCase("OK"))
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("ACTID1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                dbh.deleteGpsDataModel(jsonObject1.getString("ACTID"));
                            }
                            dbh.updateError(""+jsonObject.getString("MSG"));
                        }
                        else
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("ACTID1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                dbh.deleteGpsDataModel(jsonObject1.getString("ACTID"));
                            }
                            dbh.updateError(""+jsonObject.getString("MSG"));
                        }
                    }
                    catch (Exception e)
                    {
                        dbh.updateError(""+e.getMessage());
                    }
                    System.out.println(""+message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    DBHelper dbh = new DBHelper(getApplicationContext());
                    System.out.println(e.toString());
                    dbh.updateError(e.getMessage());
                    //new GenerateLogFile(getApplicationContext()).writeToFile("UploadService.java"+e.toString());
                }
            }
        });

        if(new InternetCheck(getApplicationContext()).isOnline())
            test.start();
    }

    private void checkValidation()
    {
        DBHelper dbh = new DBHelper(getApplicationContext());
        List<UserDetailsModel> userDetailsModels=dbh.getUserDetailsModel();
        List<GpsDataModel> gpsDataModelList=dbh.getGpsDataModel("100");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        Date today = Calendar.getInstance().getTime();
        int currentDt = Integer.parseInt((dateFormat.format(today)));
        if(userDetailsModels.size()>0)
        {
            CheckValidation: {
                if(userDetailsModels.get(0).getLeaveFlag()==1)
                {
                    if(gpsDataModelList.size()>0)
                    {
                        saveDataOnServer();
                    }
                    break CheckValidation;
                }
                else{
                    if(userDetailsModels.get(0).getOvertimeStatus()==0)
                    {
                        if(currentDt<=userDetailsModels.get(0).getTimeFrom())
                        {
                            if(gpsDataModelList.size()>0)
                            {
                                saveDataOnServer();
                            }
                            break CheckValidation;
                        }
                        if(currentDt>=userDetailsModels.get(0).getTimeTo())
                        {
                            if(gpsDataModelList.size()>0)
                            {
                                saveDataOnServer();
                            }
                            break CheckValidation;
                        }
                    }
                }
                createNotificationIcon();
            }
        }
    }

}