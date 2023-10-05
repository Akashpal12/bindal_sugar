package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.juanlabrador.badgecounter.BadgeCounter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.StaffDashboardTargetAdapter;
import in.co.vibrant.bindalsugar.adapter.StaffVillageActivityPendingAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;
import in.co.vibrant.bindalsugar.model.StaffVillageActivityPending;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.services.Config;
import in.co.vibrant.bindalsugar.services.MyLocationService;
import in.co.vibrant.bindalsugar.services.NotificationUtils;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.view.DistributionActivity;
import in.co.vibrant.bindalsugar.view.Home;
import in.co.vibrant.bindalsugar.view.LanguageSettings;
import in.co.vibrant.bindalsugar.view.NotificationActivity;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaGrowerDetailsDashboard;
import in.co.vibrant.bindalsugar.view.supervisor.TractTransport.TransportMainActivity;

public class StaffMainActivity extends AppCompatActivity {
    DBHelper dbh;
    Context context;
    List<StaffVillageActivityPending> staffVillageActivityPendingList;
    List<StaffTargetModal> staffTargetModalList;
    Spinner village;
    double lat, lng;
    CardView Card_Attendence;
    List<UserDetailsModel> userDetailsModels;
    int mNotificationCounter = 10;
    CardView caneActivityCard, potatoActivityCard;
    TextView activity_heading, tvCount;
    SwipeRefreshLayout swipeRefreshLayout;
    MenuItem itemNotification;
    String VillageCode;
    TextView day_1, txt_day_1, day_2, txt_day_2, day_3, txt_day_3, day_4, txt_day_4, day_5, txt_day_5, day_6, txt_day_6,
            day_7, txt_day_7, day_8, txt_day_8, day_9, txt_day_9;
    TextView reschedule;
    TextView attendance, inTime, outTime;
    AlertDialog AlertdialogBox;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));
        toolbar.setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));*/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;

        day_1 = findViewById(R.id.day_1);
        txt_day_1 = findViewById(R.id.txt_day_1);
        day_2 = findViewById(R.id.day_2);
        txt_day_2 = findViewById(R.id.txt_day_2);
        day_3 = findViewById(R.id.day_3);
        txt_day_3 = findViewById(R.id.txt_day_3);
        day_4 = findViewById(R.id.day_4);
        txt_day_4 = findViewById(R.id.txt_day_4);
        day_5 = findViewById(R.id.day_5);
        txt_day_5 = findViewById(R.id.txt_day_5);
        day_6 = findViewById(R.id.day_6);
        txt_day_6 = findViewById(R.id.txt_day_6);
        day_7 = findViewById(R.id.day_7);
        txt_day_7 = findViewById(R.id.txt_day_7);
        day_8 = findViewById(R.id.day_8);
        txt_day_8 = findViewById(R.id.txt_day_8);
        day_9 = findViewById(R.id.day_9);
        txt_day_9 = findViewById(R.id.txt_day_9);


        attendance = findViewById(R.id.attendance);
        inTime = findViewById(R.id.inTime);
        outTime = findViewById(R.id.outTime);
        Card_Attendence = findViewById(R.id.Card_Attendence);

        reschedule = findViewById(R.id.reschedule);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        day_5.setText(currentDt);
        txt_day_1.setText("N/A");
        txt_day_2.setText("N/A");
        txt_day_3.setText("N/A");
        txt_day_4.setText("N/A");
        txt_day_5.setText("N/A");
        txt_day_6.setText("N/A");
        txt_day_7.setText("N/A");
        txt_day_8.setText("N/A");
        txt_day_9.setText("N/A");


        village = findViewById(R.id.village);

        dbh = new DBHelper(context);
        userDetailsModels = dbh.getUserDetailsModel();
        //activity_heading=findViewById(R.id.activity_heading);
        caneActivityCard = findViewById(R.id.caneActivityCard);
        potatoActivityCard = findViewById(R.id.potatoActivityCard);

        Intent intent = new Intent(StaffMainActivity.this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);

        TextView welcome = findViewById(R.id.welcome);
        TextView version = findViewById(R.id.version);
        TextView division = findViewById(R.id.division);
        tvCount = findViewById(R.id.tvCount);
        welcome.setText("Welcome : " + userDetailsModels.get(0).getName() + " - " + userDetailsModels.get(0).getCode());
        tvCount.setText("Division :  " + userDetailsModels.get(0).getDivision());
        version.setText("Version:" + BuildConfig.VERSION_NAME);
        mNotificationCounter = dbh.getUnSeenNotificationModel().size();
        /*ArrayList<String> data=new ArrayList<String>();
        data.add("Select Village");
        data.add("Dhanoura");
        data.add("Chinoura");
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setSelection(1);*/

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscription Failed";
                        }
                        Log.d("", msg);
                    }
                });


        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, RescheduleActivityForm.class);
                intent1.putExtra("VILL_CODE", "" + VillageCode);
                startActivity(intent1);

            }
        });

        new SuperVisorAttendance().execute();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    //txtMessage.setText(message);
                }
            }
        };


        staffTargetModalList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StaffTargetModal staffTargetModal = new StaffTargetModal();
            staffTargetModal.setTextColor("");
            staffTargetModal.setAchievment("");
            staffTargetModalList.add(staffTargetModal);
        }

        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });*/
        //new getTargetListFromServer().execute();
        //new getPenddingActivityVillageWise().execute("C");

        new getTargetWiseGraphChart().execute();
        new getScheduleList().execute();
    }

    public void showNotification(View v) {
        Intent intent = new Intent(context, NotificationActivity.class);
        startActivity(intent);
    }

    public void openCaneActivities(View v) {
        caneActivityCard.setCardBackgroundColor(Color.parseColor("#FFC107"));
        potatoActivityCard.setCardBackgroundColor(Color.parseColor("#1D8C07"));
        activity_heading.setText(getString(R.string.LBL_CANE_ACTIVITIES));
        //new getCaneVillageListFromServer().execute();
        new getPenddingActivityVillageWise().execute("C");

    }

    public void openActivitySchedulePopUp(JSONArray jsonArray) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_plot_master_menu, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            RelativeLayout plot_activity = mView.findViewById(R.id.plot_activity);
            plot_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout verital_check = mView.findViewById(R.id.verital_check);
            verital_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout crop_observation = mView.findViewById(R.id.crop_observation);
            crop_observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout soil_testing = mView.findViewById(R.id.soil_testing);
            soil_testing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout growth_observation = mView.findViewById(R.id.growth_observation);
            growth_observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout brix_percent = mView.findViewById(R.id.brix_percent);
            brix_percent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                    startActivity(intent);
                }
            });
            RelativeLayout grower_meeting = mView.findViewById(R.id.grower_meeting);
            grower_meeting.setVisibility(View.VISIBLE);
            grower_meeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, VillageFarmerMeeting.class);
                    intent.putExtra("villageList", jsonArray.toString());
                    startActivity(intent);
                }
            });

            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void openOldActivity(View v) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        new AlertDialogManager().showToast((Activity) context, "Sorry!! activity can be done only " + currentDt);
    }

    public void openPotatoActivities(View v) {
        potatoActivityCard.setCardBackgroundColor(Color.parseColor("#FFC107"));
        caneActivityCard.setCardBackgroundColor(Color.parseColor("#1D8C07"));
        activity_heading.setText(getString(R.string.LBL_POTATO_ACTIVITIES));
        new getPenddingActivityVillageWise().execute("P");
        /*staffVillageActivityPendingList=new ArrayList<>();
        for(int i=0;i<2;i++)
        {
            StaffVillageActivityPending staffVillageActivityPending=new StaffVillageActivityPending();
            staffVillageActivityPendingList.add(staffVillageActivityPending);
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_list);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        StaffVillageActivityPendingAdapter stockSummeryAdapter =new StaffVillageActivityPendingAdapter(context,staffVillageActivityPendingList);
        recyclerView.setAdapter(stockSummeryAdapter);*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        closeApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (mNotificationCounter > 0) {
            BadgeCounter.update(this,
                    menu.findItem(R.id.notification),
                    R.drawable.ic_baseline_notifications_white_24,
                    BadgeCounter.BadgeColor.RED,
                    mNotificationCounter);
        } else {
            BadgeCounter.update(this,
                    menu.findItem(R.id.notification),
                    R.drawable.ic_baseline_notifications_white_24,
                    BadgeCounter.BadgeColor.RED, mNotificationCounter);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            itemNotification = item;
            Intent mapIntent = new Intent(StaffMainActivity.this, NotificationActivity.class);
            startActivity(mapIntent);
            return true;
        } else if (id == R.id.action_language) {
            Intent mapIntent = new Intent(StaffMainActivity.this, LanguageSettings.class);
            finish();
            startActivity(mapIntent);
            return true;
        } else if (id == R.id.action_logout) {
            Intent mapIntent = new Intent(StaffMainActivity.this, Home.class);
            startActivity(mapIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeApplication() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffMainActivity.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to close app ?");
        alertDialog.setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void openDevelopment(View v) {
        Intent intent = new Intent(context, StaffDevelopmentDashboard.class);
        startActivity(intent);
    }

    public void openGrowerAskExportForm(View v) {
        Intent intent = new Intent(context, StaffCommunityNearByPlotList.class);
        startActivity(intent);
    }

    public void openGrower(View v) {
        Intent intent = new Intent(context, RaGrowerDetailsDashboard.class);
        //Intent intent=new Intent(context,StaffGrowerDetailsDashboard.class);
        startActivity(intent);
    }

    public void openBroadCasting(View v) {
        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
        CheckValidation:
        {

            if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                    farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0 || masterDropDownList.size() == 0) {

                Intent intent = new Intent(context, StaffDownloadMasterData.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                break CheckValidation;
            } else {

                Intent intent = new Intent(context, BroadCastingDemo.class);
                startActivity(intent);
            }
        }
    }

    public void openBroadCastReport(View v) {
        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
        CheckValidation:
        {
            if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                    farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0 || masterDropDownList.size() == 0) {

                Intent intent = new Intent(context, StaffDownloadMasterData.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                break CheckValidation;
            } else {

                Intent intent = new Intent(context, BroadCasteReport.class);
                startActivity(intent);
            }
        }
    }

    public void openDiversion(View v) {
        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");


        CheckValidation:
        {

            if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                    farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0 || masterDropDownList.size() == 0) {

                Intent intent = new Intent(context, StaffDownloadMasterData.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                break CheckValidation;
            } else {
                Intent intent = new Intent(context, StaffCaneDiversion.class);
                startActivity(intent);
            }
        }
    }

    public void openTransport(View v) {
        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
        CheckValidation:
        {

            if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                    farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0 || masterDropDownList.size() == 0) {

                Intent intent = new Intent(context, StaffDownloadMasterData.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                break CheckValidation;
            } else {
                Intent intent = new Intent(context, TransportMainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void openCutcrush(View v) {

        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
        CheckValidation:
        {
            if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                    farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0 || masterDropDownList.size() == 0) {

                Intent intent = new Intent(context, StaffDownloadMasterData.class);
                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                break CheckValidation;
            } else {
                Intent intent = new Intent(context, StaffCutToCrushNearByPlotList.class);
                //Intent intent = new Intent(context, StaffCropObservation.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);
            }
        }
    }

    public void openSoiltesting(View v) {

        final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
        final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
        final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
        final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
        final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

        CheckValidation:
        {
            Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
            startActivity(intent);
        }
    }

    //------------------crate purchy-----------------------
    public void openpurchy(View view) {
        Intent intent = new Intent(context, PurchyDetails.class);
        startActivity(intent);

    }

    public void openAgriInputDetails(View v) {
        Intent intent = new Intent(StaffMainActivity.this, DistributionActivity.class);
        startActivity(intent);
        // new AlertDialogManager().showToast((Activity) context,"Under construction");
    }

    private void setupBarChart(JSONArray jsonArray) {
        try {
            BarChart chart = findViewById(R.id.chart1);
            //chart.setOnChartValueSelectedListener(this);

            chart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart.setMaxVisibleValueCount(40);

            // scaling can now only be done on x- and y-axis separately
            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);
            chart.setDrawBarShadow(false);

            chart.setDrawValueAboveBar(false);
            chart.setHighlightFullBarEnabled(false);

            // change the position of the y-labels
            YAxis leftAxis = chart.getAxisLeft();
            //leftAxis.setValueFormatter(new MyAxisValueFormatter());
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            chart.getAxisRight().setEnabled(false);

            XAxis xLabels = chart.getXAxis();
            xLabels.setPosition(XAxis.XAxisPosition.TOP);

            // chart.setDrawXLabels(false);
            // chart.setDrawYLabels(false);

            // setting data
            //seekBarX.setProgress(12);
            //seekBarY.setProgress(100);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setFormSize(8f);
            l.setFormToTextSpace(4f);
            l.setXEntrySpace(6f);

            //tvX.setText(String.valueOf(seekBarX.getProgress()));
            //tvY.setText(String.valueOf(seekBarY.getProgress()));
            ArrayList<BarEntry> values = new ArrayList<>();
            ArrayList<String> label = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                label.add(jsonObject.getString("label"));
                values.add(new BarEntry(i, new float[]{(float) jsonObject.getDouble("value")}));
            }
            BarDataSet set1;
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values, "Target Wise Report");
                set1.setDrawIcons(false);
                set1.setColors(getColors());
                set1.setStackLabels(new String[]{"Target"});
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                //data.setValueFormatter(new MyValueFormatter());
                data.setValueTextColor(Color.WHITE);
                chart.setData(data);
            }
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
            chart.animateY(1000, Easing.EaseInCirc);
            chart.setFitBars(true);
            chart.invalidate();
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }
    }

    private int[] getColors() {
        // Define your custom color here
        int customColor = Color.parseColor("#B22A2C");

        // Create an array of the same custom color for all stack-values per entry
        int[] colors = new int[1];
        Arrays.fill(colors, customColor);

        return colors;
    }


/*    private int[] getColors() {
        // have as many colors as stack-values per entry
        int[] colors = new int[1];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 1);
        return colors;
    }*/

    private class getScheduleList extends AsyncTask<String, Void, Void> {
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/VisitVillageList";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                //entity.add(new BasicNameValuePair("SUP","5245"));
                entity.add(new BasicNameValuePair("SUP", userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
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
                JSONArray object = new JSONArray(Content);

                day_1.setText(object.getJSONObject(0).getString("DATE"));
                day_2.setText(object.getJSONObject(1).getString("DATE"));
                day_3.setText(object.getJSONObject(2).getString("DATE"));
                day_4.setText(object.getJSONObject(3).getString("DATE"));
                day_5.setText(object.getJSONObject(4).getString("DATE"));
                day_6.setText(object.getJSONObject(5).getString("DATE"));
                day_7.setText(object.getJSONObject(6).getString("DATE"));
                day_8.setText(object.getJSONObject(7).getString("DATE"));
                day_9.setText(object.getJSONObject(8).getString("DATE"));

                JSONArray jsonArray1 = object.getJSONObject(0).getJSONArray("DATA");
                if (jsonArray1.length() == 0) {
                    txt_day_1.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject js = jsonArray1.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_1.setText(msg);
                }

                JSONArray jsonArray2 = object.getJSONObject(1).getJSONArray("DATA");
                if (jsonArray2.length() == 0) {
                    txt_day_2.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject js = jsonArray2.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_2.setText(msg);
                }

                JSONArray jsonArray3 = object.getJSONObject(2).getJSONArray("DATA");
                if (jsonArray3.length() == 0) {
                    txt_day_3.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray3.length(); i++) {
                        JSONObject js = jsonArray3.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_3.setText(msg);
                }

                JSONArray jsonArray4 = object.getJSONObject(3).getJSONArray("DATA");
                if (jsonArray4.length() == 0) {
                    txt_day_4.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray4.length(); i++) {
                        JSONObject js = jsonArray4.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_4.setText(msg);
                }

                JSONArray jsonArray5 = object.getJSONObject(4).getJSONArray("DATA");
                if (jsonArray5.length() == 0) {
                    txt_day_5.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray5.length(); i++) {
                        JSONObject js = jsonArray5.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";

                    }
                    txt_day_5.setText(msg);
                    VillageCode = jsonArray5.getJSONObject(0).getString("VILLCODE");
                }
                RelativeLayout rl_day_5 = findViewById(R.id.rl_day_5);
                rl_day_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (jsonArray5.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Sorry you dont have any village for activity");
                        } else {
                            openActivitySchedulePopUp(jsonArray5);
                        }
                    }
                });
                //
                JSONArray jsonArray6 = object.getJSONObject(5).getJSONArray("DATA");
                if (jsonArray6.length() == 0) {
                    txt_day_6.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray6.length(); i++) {
                        JSONObject js = jsonArray6.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_6.setText(msg);
                }

                JSONArray jsonArray7 = object.getJSONObject(6).getJSONArray("DATA");
                if (jsonArray7.length() == 0) {
                    txt_day_7.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray7.length(); i++) {
                        JSONObject js = jsonArray7.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_7.setText(msg);
                }

                JSONArray jsonArray8 = object.getJSONObject(7).getJSONArray("DATA");
                if (jsonArray8.length() == 0) {
                    txt_day_8.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray8.length(); i++) {
                        JSONObject js = jsonArray8.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_8.setText(msg);
                }

                JSONArray jsonArray9 = object.getJSONObject(8).getJSONArray("DATA");
                if (jsonArray9.length() == 0) {
                    txt_day_9.setText("N/A");
                } else {
                    String msg = "";
                    for (int i = 0; i < jsonArray9.length(); i++) {
                        JSONObject js = jsonArray9.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " , ";
                    }
                    txt_day_9.setText(msg);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            }
        }
    }

    private class getPenddingActivityVillageWise extends AsyncTask<String, Integer, Void> {
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_PenddingActivityVillageWise);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("type", params[0]);
                request1.addProperty("U_CODE", userDetailsModels.get(0).getCode());
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_PenddingActivityVillageWise, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("PenddingActivityVillageWiseResult").toString();
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
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                staffVillageActivityPendingList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    if (staffVillageActivityPendingList.size() > 0)
                        staffVillageActivityPendingList.clear();
                    StaffVillageActivityPending header = new StaffVillageActivityPending();
                    header.setVillCode("Vill CD");
                    header.setVillName("Vill Name");
                    header.setGrowerCount("Grower");
                    header.setPlotCount("Plot");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    staffVillageActivityPendingList.add(header);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        StaffVillageActivityPending staffVillageActivityPending = new StaffVillageActivityPending();
                        staffVillageActivityPending.setVillCode(object.getString("VILLCD"));
                        staffVillageActivityPending.setVillName(object.getString("VILLNAME"));
                        staffVillageActivityPending.setGrowerCount(object.getString("COUNT_GROWER"));
                        staffVillageActivityPending.setPlotCount(object.getString("PLOTCOUNT"));
                        if (i % 2 == 0) {
                            staffVillageActivityPending.setColor("#DFDFDF");
                            staffVillageActivityPending.setTextColor("#000000");
                        } else {
                            staffVillageActivityPending.setColor("#FFFFFF");
                            staffVillageActivityPending.setTextColor("#000000");
                        }
                        staffVillageActivityPendingList.add(staffVillageActivityPending);
                    }

                }
                RecyclerView recyclerView = findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffVillageActivityPendingAdapter stockSummeryAdapter = new StaffVillageActivityPendingAdapter(context, staffVillageActivityPendingList);
                recyclerView.setAdapter(stockSummeryAdapter);
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                //  new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    private class getTargetWiseGraphChart extends AsyncTask<String, Integer, Void> {
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GETCROPOBSERVATIONGRAPHS);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("USER", userDetailsModels.get(0).getCode());
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GETCROPOBSERVATIONGRAPHS, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GETCROPOBSERVATIONGRAPHSResult").toString();
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
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                staffVillageActivityPendingList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    setupBarChart(jsonArray);
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                // new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    private class SuperVisorAttendance extends AsyncTask<Void, Integer, Void> {
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
        protected Void doInBackground(Void... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(StaffMainActivity.this);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETSUPERVOISERATTENDANCE";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User", userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
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
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                attendance.setText("" + jsonObject.getString("ATENDANCE"));
                if (jsonObject.getString("ATENDANCE").equalsIgnoreCase("Absent")) {
                    Card_Attendence.setCardBackgroundColor(Color.RED);
                }
                inTime.setText("In Time : " + jsonObject.getString("INTIME"));
                outTime.setText("Out Time : " + jsonObject.getString("OUTTIME"));


            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
            }
        }
    }

    private class getWeatherUpdate extends AsyncTask<String, Void, Void> {
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
                String url = "http://api.openweathermap.org/data/2.5/weather";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lon", params[1]));
                entity.add(new BasicNameValuePair("appid", "1211973affa328939680b7a52bd0cbfe"));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
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
                JSONObject object = new JSONObject(Content);
                ImageView image = (ImageView) findViewById(R.id.weather_img);
                TextView weather_txt_1 = findViewById(R.id.weather_txt_1);
                TextView weather_txt_2 = findViewById(R.id.weather_txt_2);
                TextView weather_txt_3 = findViewById(R.id.weather_txt_3);
                TextView weather_txt_4 = findViewById(R.id.weather_txt_4);
                TextView weather_txt_5 = findViewById(R.id.weather_txt_5);
                TextView weather_city = findViewById(R.id.weather_city);
                JSONArray weather = object.getJSONArray("weather");
                JSONObject weatherObj = weather.getJSONObject(0);
                JSONObject mainObj = object.getJSONObject("main");
                JSONObject sysObj = object.getJSONObject("sys");
                JSONObject cloudsObj = object.getJSONObject("clouds");
                double cent = mainObj.getDouble("temp") - 273.15;
                long unixSeconds = sysObj.getLong("sunset");
                long unixsunriseSeconds = sysObj.getLong("sunrise");
                Date date = new Date(unixSeconds * 1000L);
                Date datesunrise = new Date(unixsunriseSeconds * 1000L);
                // the format of your date
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM EEEE");
                //Date date = new Date(sysObj.getString("sunset"));
                weather_txt_1.setText("Today " + sdf1.format(date));
                weather_txt_2.setText(cent + " °C");
                weather_txt_3.setText("Sunrise " + sdf.format(datesunrise) + "    Sunset " + sdf.format(date));
                weather_txt_4.setText(weatherObj.getString("main"));
                weather_txt_5.setText(cloudsObj.getString("all") + "%");
                weather_city.setText(object.getString("name"));
                Glide.with(context).load("http://openweathermap.org/img/w/" + weatherObj.getString("icon") + ".png").into(image);
                //
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                //new AlertDialogManager().AlertPopUpFinish();
            }
        }
    }

    private class getTargetListFromServer extends AsyncTask<Void, Integer, Void> {
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
        protected Void doInBackground(Void... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(StaffMainActivity.this);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetDashboardTarget";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("FactId", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("user", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("imei", getDeviceImei.GetDeviceImeiNumber()));
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
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(message);
                //JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                if (staffTargetModalList.size() > 0)
                    staffTargetModalList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    StaffTargetModal staffTargetModal = new StaffTargetModal();
                    staffTargetModal.setTargetCode(object.getString("TARGETCODE").replace(".0", ""));
                    staffTargetModal.setTargetName(object.getString("TARGETNAME"));
                    staffTargetModal.setStartDate(object.getString("STARTDATESTR"));
                    staffTargetModal.setEndDate(object.getString("ENDDATESTR"));
                    staffTargetModal.setVillageCount(object.getString("VILLAGECOUNT"));
                    staffTargetModal.setArea(object.getString("AREA"));
                    staffTargetModal.setGrowerCount(object.getString("GROWERCOUNT"));
                    staffTargetModal.setAreaComplete(object.getString("AREACOMP"));
                    staffTargetModal.setGrowerComplete(object.getString("GROWERCOMP"));
                    staffTargetModal.setAreaBalance(object.getString("AREABAL"));
                    staffTargetModal.setGrowerBalance(object.getString("GROWERBAL"));
                    staffTargetModal.setAreaExp(object.getString("AREAEXP"));
                    staffTargetModal.setGrowerExp(object.getString("GROWEREXP"));
                    staffTargetModal.setAreaDelay(object.getString("AREADF"));
                    staffTargetModal.setGrowerDelay(object.getString("GROWERDF"));
                    staffTargetModalList.add(staffTargetModal);
                }
                RecyclerView recyclerViewTarget = findViewById(R.id.recycler_target);
                GridLayoutManager staffTargetmanager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerViewTarget.setHasFixedSize(true);
                recyclerViewTarget.setLayoutManager(staffTargetmanager);
                StaffDashboardTargetAdapter staffTargetSummeryAdapter = new StaffDashboardTargetAdapter(context, staffTargetModalList);
                recyclerViewTarget.setAdapter(staffTargetSummeryAdapter);
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                //    new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

}
