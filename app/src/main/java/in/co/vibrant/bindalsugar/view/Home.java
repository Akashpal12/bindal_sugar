package in.co.vibrant.bindalsugar.view;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.NotificationModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.services.Config;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaMainActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffMainActivity;

public class Home extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static int REQUEST_PERMISSION_FILE_ACCESS = 1002;
    SessionConfig sessionConfig;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA
    };
    boolean allgranted = false;
    String FirbaseRegistrationId;
    SQLiteDatabase db;
    TextView textView;
    SharedPreferences sharedpreferences;
    Context context;
    DBHelper dbh;
    File dir;
    String url, serverAppVersion;
    private ProgressDialog mProgressDialog;

    private void init() {

        context = Home.this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        textView = (TextView) findViewById(R.id.textView);
        sessionConfig = new SessionConfig(context);

        checkPermission();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        FirbaseRegistrationId = task.getResult().getToken();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("regId", FirbaseRegistrationId);
                      //  editor.commit();
                        editor.apply();
                        Log.d("FirbaseRegistrationId :", "" + FirbaseRegistrationId);

                    }
                });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

     //   getJson();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        init();

    }

    private void checkPermission() {
        textView.setText("Check Permission");
        ActivityCompat.requestPermissions(Home.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            //Get Permission
            permission(allgranted);

        } else if (requestCode == REQUEST_PERMISSION_FILE_ACCESS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted for Background Location Permission.
                proceedAfterPermission();
            } else {
                // User declined for Background Location Permission.
            }
        }
    }

    private void permission(boolean allgranted) {

        if (allgranted) {
            proceedAfterPermission();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[1])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[2])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[3])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[4])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[5])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[6])
                || ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissionsRequired[7])

        ) {
            //txtPermissions.setText("Permissions Required");
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(getString(R.string.MSG_NEED_PERMISSION));
            builder.setMessage(getString(R.string.MSG_NEED_PERMISSION_REQUEST));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.BTN_YES), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(Home.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
            });
            builder.setNegativeButton(getString(R.string.BTN_LATER), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.show();
        } else {
            proceedAfterPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Home.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                textView.setText("All permission granted");
                proceedAfterPermission();
            }
        }

    }

    private void proceedAfterPermission() {

        CheckValidation:
        {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    createDB();
                } else {
                    //request for the permission
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_FILE_ACCESS);
                    // finish();
                    break CheckValidation;
                }
            } else {
                createDB();
            }
        }
    }

    private void createDB() {

        try {

            File sdCard = Environment.getExternalStorageDirectory();
            // File sdCard = FileUtils.createDirectory(this, "MyFolder");

            Log.d("ErrorMsg:-->", "" + sdCard);
            dir = new File(sdCard.getAbsolutePath() + "/BindalSugar_Development");
            Log.d("ErrorMsg:-->", "" + dir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File dir1 = new File(sdCard.getAbsolutePath() + "/bindal_dev");
            if (!dir1.exists()) {
                dir1.mkdir();
            }

            dbh = new DBHelper(context);
            db = dbh.getWritableDatabase();
            dbh.onCreate(db);
        } catch (Exception e) {

            Log.d("ErrorMsg:", "" + new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cane_dev"));
            Log.d("ErrorMsg:", "" + e.getMessage());
            new AlertDialogManager().AlertPopUp(context, "" + e.getMessage());
            e.printStackTrace();
        }

        try {
            String username = sharedpreferences.getString("language", null);
            if (username == null) {
                Intent intent = new Intent(Home.this, Language.class);
                startActivity(intent);
                finish();
            } else {

                boolean checkColumn4 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.Gps_wait_time);
                if (checkColumn4) {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.Gps_wait_time);
                }
                boolean checkColumn3 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.ISUPDATEMASTER);
                if (checkColumn3) {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.ISUPDATEMASTER);
                }

                boolean checkColumn31 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.LEAVEFLG);
                if(checkColumn31)
                {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.LEAVEFLG);
                }

                boolean checkColumn32 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.TIMEFROM);
                if(checkColumn32)
                {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.TIMEFROM);
                }

                boolean checkColumn35 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.TIMETO);
                if(checkColumn35)
                {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.TIMETO);
                }

                boolean checkColumn36 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.Col_overtime_status);
                if(checkColumn36)
                {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.Col_overtime_status);
                }

                boolean checkColumn33 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.ZONECODE);
                if (checkColumn33) {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.ZONECODE);
                }
                boolean checkColumn34 = dbh.columnExistsInTable(UserDetailsModel.TABLE_NAME, UserDetailsModel.ZONENAME);
                if (checkColumn34) {
                    dbh.alterTableAddColumn(UserDetailsModel.TABLE_NAME, UserDetailsModel.ZONENAME);
                }

                List<UserDetailsModel> userDetailsModelList = dbh.getUserDetailsModel();
                if (userDetailsModelList.size() > 0) {
                    boolean checkColumn1 = dbh.columnExistsInTable(PlantingModel.TABLE_NAME, PlantingModel.Col_seed_bag_qty);
                    if (checkColumn1) {
                        dbh.alterTableAddColumn(PlantingModel.TABLE_NAME, PlantingModel.Col_seed_bag_qty);
                    }
                    boolean checkColumn2 = dbh.columnExistsInTable(VillageModal.TABLE_NAME, VillageModal.Col_is_target);
                    if (checkColumn2) {
                        dbh.alterTableAddColumn(VillageModal.TABLE_NAME, VillageModal.Col_is_target);
                    }
                    boolean checkColumn5 = dbh.columnExistsInTable(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_is_delete);
                    if (checkColumn5) {
                        dbh.alterTableAddColumn(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_is_delete);
                    }

                    boolean checkColumn7 = dbh.columnExistsInTable(NotificationModel.TABLE_NAME, NotificationModel.Col_senduser);
                    if (checkColumn7) {
                        dbh.alterTableAddColumn(NotificationModel.TABLE_NAME, NotificationModel.Col_senduser);
                    }
                    boolean checkColumn8 = dbh.columnExistsInTable(VillageSurveyModel.TABLE_NAME, VillageSurveyModel.Col_is_default);
                    if (checkColumn8) {
                        dbh.alterTableAddColumn(VillageSurveyModel.TABLE_NAME, VillageSurveyModel.Col_is_default);
                    }

                    boolean checkColumn9 = dbh.columnExistsInTable(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_old_plot_id);
                    if (checkColumn9) {
                        dbh.alterTableAddColumn(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_old_plot_id);
                    }

                    boolean checkColumn10 = dbh.columnExistsInTable(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_old_plot_from);
                    if (checkColumn10) {
                        dbh.alterTableAddColumn(PlotSurveyModel.TABLE_NAME, PlotSurveyModel.Col_old_plot_from);
                    }

                    boolean checkColumn11 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_type);
                    if (checkColumn11) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_type);
                    }
                    boolean checkColumn12 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_name);
                    if (checkColumn12) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_name);
                    }
                    boolean checkColumn13 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_number);
                    if (checkColumn13) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.meeting_number);
                    }
                    boolean checkColumn14 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_SURTYPE);
                    if (checkColumn14) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_SURTYPE);
                    }
                    boolean checkColumn15 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_OLDSEAS);
                    if (checkColumn15) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_OLDSEAS);
                    }
                    boolean checkColumn16 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_OLDGHID);
                    if (checkColumn16) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_OLDGHID);
                    }
                    boolean checkColumn17 = dbh.columnExistsInTable(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_mobile_number);
                    if (checkColumn17) {
                        dbh.alterTableAddColumn(PlotActivitySaveModel.TABLE_NAME, PlotActivitySaveModel.col_mobile_number);
                    }

                    if (new InternetCheck(context).isOnline()) {
                        new checkImei().execute();

                    } else {
                        if (Integer.parseInt(userDetailsModelList.get(0).getUserLavel().replace(".0", "")) == 0) {
                            Intent intent = new Intent(context, StaffMainActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, RaMainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        //new AlertDialogManager().RedDialog(context,"No internet found");
                    }
                } else {
                    if (new InternetCheck(context).isOnline()) {
                        new checkImei().execute();
                    } else {
                        new AlertDialogManager().RedDialog(context, "User not found please go online to validate device");
                    }
                }
            }
        } catch (Exception e) {
            textView.setText("Error:" + e.toString());
        }
    }

    public void updateDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Home.this);
        alertDialog.setTitle("Update");
        alertDialog.setMessage("This app need to update please update immediately.");
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        new DownloadFileAsync().execute(url);
                    }
                });
        alertDialog.setNegativeButton("Later",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    void installAPK() {

        String PATH = dir + "/cane_development.apk";
        File file = new File(PATH);
        if (file.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!getPackageManager().canRequestPackageInstalls()) {
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
                }
            }

            //Storage Permission

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uriFromFile(getApplicationContext(), new File(PATH)), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    new AlertDialogManager().RedDialog(getApplicationContext(), "Error:" + e.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "installing", Toast.LENGTH_LONG).show();
            }
        } else {
            new AlertDialogManager().RedDialog(getApplicationContext(), "File Not Found");
        }
    }

    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            ActivityCompat.requestPermissions(Home.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private class checkImei extends AsyncTask<Void, Integer, Void> {
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
            message = getString(R.string.MSG_PLEASE_WAIT);
            textView.setText("" + message);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_ValidateIMEINO);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("FirbaseRegistrationId", FirbaseRegistrationId);
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_ValidateIMEINO, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("ValidateIMEINOResult").toString();
                }
            } catch (SecurityException e) {
                textView.setText("Error:" + e.toString());
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                textView.setText("Error:" + e.toString());
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
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                Log.d("Hello", "" + message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    url = jsonObject.getString("URL");
                    serverAppVersion = jsonObject.getString("APPVersion");
                    if (jsonObject.getString("APPStatus").equalsIgnoreCase("ACTIVE")) {
                        if (jsonObject.getInt("APPVersion") <= BuildConfig.VERSION_CODE) {
                            JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                            dbh.deleteUserDetailsModel();

                            if (jsonArray.length() > 0) {

                                JSONObject object = jsonArray.getJSONObject(0);
                                UserDetailsModel userDetailsModel = new UserDetailsModel();
                                userDetailsModel.setCode(object.getString("U_CODE"));
                                userDetailsModel.setName(object.getString("U_NAME"));
                                userDetailsModel.setPhone(object.getString("U_PHONE"));
                                userDetailsModel.setUserTypeCode(object.getString("UT_CODE"));
                                userDetailsModel.setUserTypeName(object.getString("UT_NAME"));
                                userDetailsModel.setDsCode(object.getString("DS_CODE"));
                                userDetailsModel.setDsName(object.getString("D_NAME"));
                                userDetailsModel.setDivision(object.getString("DIVN"));
                                userDetailsModel.setGpsAccuracy(jsonObject.getDouble("GPSACCURACY"));
                                userDetailsModel.setGpsWaitTime(jsonObject.getInt("GPSWTTIME"));
                                userDetailsModel.setCompantName(object.getString("NM"));
                                userDetailsModel.setIsActivate(object.getString("ISACTIVATE"));
                                userDetailsModel.setUserLavel(object.getString("U_LEVEL"));
                                userDetailsModel.setZoneCode(object.getString("ZONE_CODE"));
                                userDetailsModel.setZoneName(object.getString("Z_NAME"));
                                sessionConfig.setSeason(object.getString("SEAS"));
                                userDetailsModel.setIsUpdateMaster(object.getInt("U_UPDMAST"));//0 not upfdate master 1 update master
                                dbh.insertUserDetailsModel(userDetailsModel);

                                if (object.getInt("U_LEVEL") == 0) {
                                    Intent intent = new Intent(context, StaffMainActivity.class); //superwisher
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(context, RaMainActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            textView.setText("Please update your app");
                            updateDialogue();
                        }
                    } else {
                        new AlertDialogManager().AlertPopUpFinish(getApplicationContext(), jsonObject.getString("APPStatus"));
                    }
                } else {
                    textView.setText(jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                textView.setText("Error:" + e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                textView.setText("Error:" + e.toString());
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        String VideoPath = dir + "/cane_development.apk";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            textView.setText("Updating Application ...");
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL urls = new URL(aurl[0]);
                URLConnection conexion = urls.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                final String contentLengthStr = conexion.getHeaderField("content-length");
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(urls.openStream());
                OutputStream output = new FileOutputStream(VideoPath);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                    //mProgressDialog.setProgress((int)((total*100)/lenghtOfFile));
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(Home.this, "Error:" + e.toString());
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            //new AlertDialogManager().RedDialog(context,"Download done");
            try {
                installAPK();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(VideoPath)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                /*File toInstall = new File(VideoPath);
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", toInstall);
                    intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    Uri apkUri = Uri.fromFile(toInstall);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);*/
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(Home.this, "Error:" + e.toString());
            }
        }
    }

      /*public void updateDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Home.this);
        alertDialog.setTitle("Update?");
        alertDialog.setMessage(getString(R.string.UPDATE_APP));
        alertDialog.setPositiveButton(getString(R.string.UPDATE_YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("market://details?id=in.co.vibrant.canedevelopment"));
                        startActivity(i);
                    }
                });
        alertDialog.setNegativeButton(getString(R.string.BTN_LATER),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }*/
}
