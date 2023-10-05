package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GetRequestDetailAdpter;
import in.co.vibrant.bindalsugar.adapter.RaActivityTargetAdapter;
import in.co.vibrant.bindalsugar.adapter.RaDashboardAttendanceReportListAdapter;
import in.co.vibrant.bindalsugar.adapter.RaSelfTargetAdapter;
import in.co.vibrant.bindalsugar.model.DashboardAttendanceModel;
import in.co.vibrant.bindalsugar.model.GetRequestDetailModel;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;
import in.co.vibrant.bindalsugar.model.StaffVillageActivityPending;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.services.Config;
import in.co.vibrant.bindalsugar.services.MyLocationService;
import in.co.vibrant.bindalsugar.services.NotificationUtils;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.view.Home;
import in.co.vibrant.bindalsugar.view.LanguageSettings;
import in.co.vibrant.bindalsugar.view.NotificationActivity;
import in.co.vibrant.bindalsugar.view.supervisor.BroadCasting;
import in.co.vibrant.bindalsugar.view.supervisor.PlotFinderMapViewMaster;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCaneDiversion;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCommunityNearByPlotList;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCutToCrushNearByPlotList;
import in.co.vibrant.bindalsugar.view.supervisor.StaffSoilSampleNearByPlotList;
import in.co.vibrant.bindalsugar.view.supervisor.VillageFarmerMeeting;

public class RaMainActivity extends AppCompatActivity {
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    FusedLocationProviderClient fusedLocationClient;
    List<UserDetailsModel> userDetailsModels;
    CardView PendingSchedule;
    double lat, lng, accuracy;
    List<StaffVillageActivityPending> staffVillageActivityPendingList;
    List<StaffTargetModal> staffTargetModalList;
    List<GrowerActivityDetailsModel> growerActivityDetailsModelList;
    List<GetRequestDetailModel> getRequestDetailModelList;
    int mNotificationCounter = 10;
    AlertDialog AlertdialogBox;
    LinearLayout pendingStatusList;
    JSONArray jsonArray, jsonVillageArray, jsonVarietyArray;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView day_1, txt_day_1, day_2, txt_day_2, day_3, txt_day_3, day_4, txt_day_4, day_5, txt_day_5, day_6, txt_day_6,
            day_7, txt_day_7, day_8, txt_day_8, day_9, txt_day_9;
    TextView target_name;
    TextView txt_day_4_1;
    Integer tempInc = 0;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;

        getRequestDetailModelList = new ArrayList<>();
        PendingSchedule = findViewById(R.id.PendingSchedule);
        pendingStatusList = findViewById(R.id.pendingStatusList);

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


        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        target_name = findViewById(R.id.target_name);
        dbh = new DBHelper(context);
        userDetailsModels = dbh.getUserDetailsModel();

        Intent intent = new Intent(RaMainActivity.this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);


        TextView welcome = findViewById(R.id.welcome);
        TextView version = findViewById(R.id.version);
        TextView division = findViewById(R.id.division);
        welcome.setText("Welcome : " + userDetailsModels.get(0).getName() + " - " + userDetailsModels.get(0).getCode() + " ( " + userDetailsModels.get(0).getDsName() + " )");
        division.setText("Division:" + userDetailsModels.get(0).getDivision());
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


        PendingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingStatusList.getVisibility() == View.VISIBLE) {
                    getRequestDetailModelList.clear();
                    pendingStatusList.setVisibility(View.GONE);
                    new GetRequestDetail().execute();


                } else {
                    getRequestDetailModelList.clear();
                    pendingStatusList.setVisibility(View.VISIBLE);
                    new GetRequestDetail().execute();


                }
            }
        });


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

        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getTargetListFromServer().execute();
                new getPenddingActivityVillageWise().execute("C");
            }
        });*/

        staffTargetModalList = new ArrayList<>();


        for (int i = 0; i < 3; i++) {
            StaffTargetModal staffTargetModal = new StaffTargetModal();
            staffTargetModal.setTextColor("");
            staffTargetModal.setAchievment("");
            staffTargetModalList.add(staffTargetModal);
        }


        staffTargetModalList = new ArrayList<>();
        growerActivityDetailsModelList = new ArrayList<>();
        new TargetList().execute();
        new AttendanceReport().execute();

        new getTargetWiseGraphChart().execute();
        new getScheduleList().execute();
    }

    public void showNotification(View v) {
        Intent intent = new Intent(context, NotificationActivity.class);
        startActivity(intent);
    }

    public void refresh(View v) {
        new TargetList().execute();
        new AttendanceReport().execute();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_language) {
            Intent mapIntent = new Intent(RaMainActivity.this, LanguageSettings.class);
            finish();
            startActivity(mapIntent);
            return true;
        } else if (id == R.id.action_logout) {
            Intent mapIntent = new Intent(RaMainActivity.this, Home.class);
            startActivity(mapIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    /*@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_about_kpta) {
            // Handle the camera action
        } else if (id == R.id.menu_oil_company) {
            Intent mapIntent=new Intent(MainActivity.this, DealerOnMap.class);
            startActivity(mapIntent);
        } else if (id == R.id.menu_dealer_forum) {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            startActivity(launchIntent);

        } else if (id == R.id.menu_trade_knowledge) {
            Intent mapIntent=new Intent(MainActivity.this, TradeKnowledge.class);
            startActivity(mapIntent);
        } else if (id == R.id.menu_imp_dates_know) {
            Intent mapIntent=new Intent(MainActivity.this, ImportantDateCalender.class);
            startActivity(mapIntent);

        } else if (id == R.id.menu_defaulters) {
            Intent mapIntent=new Intent(MainActivity.this, DefaultersList.class);
            startActivity(mapIntent);
        } else if (id == R.id.menu_vendor) {
            Intent mapIntent=new Intent(MainActivity.this, VendorsCategory.class);
            startActivity(mapIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


    public void closeApplication() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RaMainActivity.this);
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
        Intent intent = new Intent(context, RaDevelopmentDashboard.class);
        startActivity(intent);
    }

    public void openGrowerAskExportForm(View v) {
        Intent intent = new Intent(context, StaffCommunityNearByPlotList.class);
        startActivity(intent);
    }

    public void openGrower(View v) {
        Intent intent = new Intent(context, RaGrowerDetailsDashboard.class);
        startActivity(intent);
    }

    public void openGrowerActivityDetails(View v) {
        Intent intent = new Intent(context, RaActivityDetails.class);
        startActivity(intent);
    }


    public void openBroadCasting(View v) {
        Intent intent = new Intent(context, BroadCasting.class);
        startActivity(intent);
    }

    public void openDiversion(View v) {


        Intent intent = new Intent(context, StaffCaneDiversion.class);
        startActivity(intent);
    }

    public void openCutcrush(View v) {
        Intent intent = new Intent(context, StaffCutToCrushNearByPlotList.class);
        //Intent intent = new Intent(context, StaffCropObservation.class);
        intent.putExtra("lat", "" + lat);
        intent.putExtra("lng", "" + lng);
        startActivity(intent);

    }

    public void openSoiltesting(View v) {
        Intent intent = new Intent(context, StaffSoilSampleNearByPlotList.class);

        startActivity(intent);
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
        // have as many colors as stack-values per entry
        int[] colors = new int[1];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 1);
        return colors;
    }

    public void openOldActivity(View v) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        new AlertDialogManager().showToast((Activity) context, "Sorry!! activity can be done only " + currentDt);
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

    class TargetList extends AsyncTask<String, Void, String> {

        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetDashboardTargetReporting";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("FactId", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("user", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("Level", userDetailsModels.get(0).getUserLavel()));
                entity.add(new BasicNameValuePair("UserName", userDetailsModels.get(0).getName()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + Content);
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("ok")) {
                    target_name.setText("Target " + jsonObject.getString("USERNAME"));
                    staffTargetModalList = new ArrayList<>();
                    if (staffTargetModalList.size() > 0)
                        staffTargetModalList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("ReportingPersonTarget");
                    /*StaffTargetModal header=new StaffTargetModal();
                    header.setTargetCode("Target Code");
                    header.setTargetName("Target Name");
                    header.setVillageCount("Village");
                    header.setArea("Tot Area");
                    header.setGrowerCount("Tot Grower");
                    header.setAreaComplete("Area Comp.");
                    header.setGrowerComplete("Grower Comp.");
                    header.setAreaBalance("Area Bal");
                    header.setGrowerBalance("Grower Bal");
                    header.setAreaExp("Area Exp");
                    header.setGrowerExp("Grower Exp");
                    header.setAreaDelay("Area Delay");
                    header.setGrowerDelay("Grower Delay");
                    header.setStartDate("Start Date");
                    header.setEndDate("End Date");
                    header.setBackgroundColor("#000000");
                    header.setTextColor("#FFFFFF");
                    staffTargetModalList.add(header);*/
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        StaffTargetModal staffTargetModal = new StaffTargetModal();
                        staffTargetModal.setTargetCode(object.getString("TARGETCODE"));
                        staffTargetModal.setTargetName(object.getString("TARGETNAME"));
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
                        staffTargetModal.setStartDate(object.getString("STARTDATESTR"));
                        staffTargetModal.setEndDate(object.getString("ENDDATESTR"));
                        if (i % 2 == 0) {
                            staffTargetModal.setBackgroundColor("#DFDFDF");
                            staffTargetModal.setTextColor("#000000");
                        } else {
                            staffTargetModal.setBackgroundColor("#FFFFFF");
                            staffTargetModal.setTextColor("#000000");
                        }
                        staffTargetModalList.add(staffTargetModal);
                    }

                    if (growerActivityDetailsModelList.size() > 0)
                        growerActivityDetailsModelList.clear();

                    JSONArray jsonArraySub = jsonObject.getJSONArray("SubReportingPerson");
                    for (int i = 0; i < jsonArraySub.length(); i++) {
                        GrowerActivityDetailsModel growerActivityDetailsModel = new GrowerActivityDetailsModel();
                        JSONObject jsonObject1 = jsonArraySub.getJSONObject(i);
                        growerActivityDetailsModel.setEmployeeCode(jsonObject1.getString("EmpCode"));
                        growerActivityDetailsModel.setEmployeeName(jsonObject1.getString("EmpName"));
                        growerActivityDetailsModel.setEmployeePhone(jsonObject1.getString("Phone"));
                        growerActivityDetailsModel.setLavel(jsonObject1.getString("Level"));
                        growerActivityDetailsModel.setGetActivityData(jsonObject1.getJSONArray("SubTarget"));
                        growerActivityDetailsModel.setOpenBy(false);
                        growerActivityDetailsModelList.add(growerActivityDetailsModel);
                    }

                }
                RecyclerView recyclerView = findViewById(R.id.recycler_main_target);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                RaSelfTargetAdapter stockSummeryAdapter = new RaSelfTargetAdapter(context, staffTargetModalList);
                recyclerView.setAdapter(stockSummeryAdapter);


                RecyclerView recyclersubReportView = findViewById(R.id.recycler_sub_target);
                GridLayoutManager subReportManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclersubReportView.setHasFixedSize(true);
                recyclersubReportView.setLayoutManager(subReportManager);
                RaActivityTargetAdapter subReportAdapter = new RaActivityTargetAdapter(context, growerActivityDetailsModelList);
                recyclersubReportView.setAdapter(subReportAdapter);


            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    private class AttendanceReport extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*/
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            //dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/DASBORDATTENDANCE";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("imei", getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);


            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                //AlertPopUp("Error:"+e.toString());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                //AlertPopUp("Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                List<DashboardAttendanceModel> dashboardAttendanceModelList = new ArrayList<>();
                dialog.dismiss();
                JSONArray jsonArray = new JSONArray(Content);
                DashboardAttendanceModel header = new DashboardAttendanceModel();
                header.setUserCode("Code");
                header.setName("User");
                header.setTotalUser("Total");
                header.setAbsent("Absent");
                header.setPresent("Present");
                header.setInRightTime("Total in");
                header.setOutRightTime("Total out");
                header.setColor("#355E37");
                header.setTextColor("#FFFFFF");
                dashboardAttendanceModelList.add(header);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DashboardAttendanceModel dashboardAttendanceModel = new DashboardAttendanceModel();
                    dashboardAttendanceModel.setUserCode(jsonObject.getString("UCODE"));
                    dashboardAttendanceModel.setName(jsonObject.getString("NAME"));
                    dashboardAttendanceModel.setTotalUser(jsonObject.getString("TOTALUSER"));
                    dashboardAttendanceModel.setAbsent(jsonObject.getString("APCENT"));
                    dashboardAttendanceModel.setPresent(jsonObject.getString("PRGENT"));
                    dashboardAttendanceModel.setInRightTime(jsonObject.getString("TOTIN"));
                    dashboardAttendanceModel.setOutRightTime(jsonObject.getString("TOTOUT"));
                    if (i % 2 == 0) {
                        dashboardAttendanceModel.setColor("#DFDFDF");
                        dashboardAttendanceModel.setTextColor("#355E37");
                    } else {
                        dashboardAttendanceModel.setColor("#FFFFFF");
                        dashboardAttendanceModel.setTextColor("#355E37");
                    }
                    dashboardAttendanceModelList.add(dashboardAttendanceModel);
                }
                RecyclerView recyclerView = findViewById(R.id.attendance_recycler);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                RaDashboardAttendanceReportListAdapter stockSummeryAdapter = new RaDashboardAttendanceReportListAdapter(context, dashboardAttendanceModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
            } catch (JSONException e) {
                LinearLayout line1_1 = findViewById(R.id.line1_1);
                line1_1.setVisibility(View.GONE);
                new AlertDialogManager().RedDialog(context, "Error : " + Content);
                //AlertPopUp("Error:"+e.toString());
            } catch (Exception e) {
                LinearLayout line1_1 = findViewById(R.id.line1_1);
                line1_1.setVisibility(View.GONE);
                //AlertPopUp("Error:"+e.toString());
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
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
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
                } else {
                    //  new AlertDialogManager().GreenDialog(context,jsonObject.get("MSG").toString());
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                //new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/CdoVisitList";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("EMP", "" + userDetailsModels.get(0).getCode()));
                //entity.add(new BasicNameValuePair("SUP","5245"));
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
                    }
                    txt_day_3.setText(msg);
                }

                JSONArray jsonArray4 = object.getJSONObject(3).getJSONArray("DATA");
                if (jsonArray4.length() == 0) {
                    txt_day_4.setText("N/A");
                } else {
                    String msg = "";
                    String msg1 = "";
                    for (int i = 0; i < jsonArray4.length(); i++) {
                        JSONObject js = jsonArray4.getJSONObject(i);
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += "Vill : " + js.getString("VILLCODE") + " / " + js.getString("VILLNAME") + " / " + "Sup : " + js.getString("SUPCODE") + " / " + js.getString("SUPNAME") + " , ";
                    }
                    txt_day_5.setText(msg);
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
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
                        msg += js.getString("VILLCODE") + " / " + "\n" + js.getString("VILLNAME") + "/" + js.getString("SUPCODE") + " / " + "\n" + js.getString("SUPNAME");
                    }
                    txt_day_9.setText(msg);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            }
        }
    }

    private class GetRequestDetail extends AsyncTask<String, Void, Void> {
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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetRequestDetail";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
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
                if (getRequestDetailModelList.size() > 0) {
                    getRequestDetailModelList.clear();
                }
                JSONObject jsonObject = new JSONObject(Content);
                JSONArray jsonArray1 = jsonObject.getJSONArray("DATA");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    GetRequestDetailModel getRequestDetailModel = new GetRequestDetailModel();
                    getRequestDetailModel.setId(Integer.valueOf(jsonObject1.getString("ID")));
                    getRequestDetailModel.setSupcode(Integer.valueOf(jsonObject1.getString("SUPCODE")));
                    getRequestDetailModel.setSupname(jsonObject1.getString("SUPNAME"));
                    getRequestDetailModel.setPhone(jsonObject1.getString("PHONE"));
                    getRequestDetailModel.setVsdate(jsonObject1.getString("VSDATE"));
                    getRequestDetailModel.setRsdate(jsonObject1.getString("RSDATE"));
                    getRequestDetailModel.setReason(jsonObject1.getString("REASON"));
                    getRequestDetailModel.setRDesc(jsonObject1.getString("R_DESC"));
                    getRequestDetailModel.setReqdate(jsonObject1.getString("REQDATE"));
                    getRequestDetailModel.setStatus(jsonObject1.getString("STATUS"));
                    getRequestDetailModelList.add(getRequestDetailModel);

                }
                RecyclerView recyclerView = findViewById(R.id.pending_listRecyclerView);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                GetRequestDetailAdpter listGrowerFinderAdapter = new GetRequestDetailAdpter(context, getRequestDetailModelList);
                recyclerView.setAdapter(listGrowerFinderAdapter);


            } catch (Exception e) {

                //new AlertDialogManager().RedDialog(context,new JSONObject(Content).getString("MSG"));

            }
        }
    }


}
