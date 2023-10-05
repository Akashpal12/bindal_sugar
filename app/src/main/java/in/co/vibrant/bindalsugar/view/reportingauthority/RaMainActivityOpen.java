package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.RaActivityTargetAdapter;
import in.co.vibrant.bindalsugar.adapter.RaSelfTargetAdapter;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.services.Config;
import in.co.vibrant.bindalsugar.services.MyLocationService;
import in.co.vibrant.bindalsugar.services.NotificationUtils;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.view.Home;
import in.co.vibrant.bindalsugar.view.LanguageSettings;
import in.co.vibrant.bindalsugar.view.supervisor.StaffActivityDetails;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCommunityForm;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerDetailsDashboard;
import in.co.vibrant.bindalsugar.view.supervisor.survey.StaffSurveyDashboard;


public class RaMainActivityOpen extends AppCompatActivity  {

    DBHelper dbh;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Context context;
    FusedLocationProviderClient fusedLocationClient;
    List<UserDetailsModel> userDetailsModels;

    List<StaffTargetModal> staffTargetModalList;
    List<GrowerActivityDetailsModel> growerActivityDetailsModelList;

    SwipeRefreshLayout swipeRefreshLayout;

    String emp_code,level,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context=this;
        emp_code=getIntent().getExtras().getString("emp_code");
        level=getIntent().getExtras().getString("level");
        name=getIntent().getExtras().getString("name");

        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        Intent intent = new Intent(RaMainActivityOpen.this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);

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
        staffTargetModalList=new ArrayList<>();
        growerActivityDetailsModelList=new ArrayList<>();
        new TargetList().execute();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //closeApplication();
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
            Intent mapIntent=new Intent(RaMainActivityOpen.this, LanguageSettings.class);
            finish();
            startActivity(mapIntent);
            return true;
        }
        else if (id == R.id.action_logout) {
            Intent mapIntent=new Intent(RaMainActivityOpen.this, Home.class);
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
                RaMainActivityOpen.this);
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


    public void openDevelopment(View v)
    {
        Intent intent=new Intent(context, StaffSurveyDashboard.class);
        startActivity(intent);
    }

    public void openGrowerAskExportForm(View v)
    {
        Intent intent=new Intent(context, StaffCommunityForm.class);
        startActivity(intent);
    }

    public void openGrower(View v)
    {
        Intent intent=new Intent(context, StaffGrowerDetailsDashboard.class);
        startActivity(intent);
    }

    public void openGrowerActivityDetails(View v)
    {
        Intent intent=new Intent(context, StaffActivityDetails.class);
        startActivity(intent);
    }




    class TargetList extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details",true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GetDashboardTargetReporting";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("FactId",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("user",emp_code));
                entity.add(new BasicNameValuePair("Level",level));
                entity.add(new BasicNameValuePair("UserName",name));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
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
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("ok"))
                {
                    TextView target_name=findViewById(R.id.target_name);
                    target_name.setText("Target "+jsonObject.getString("USERNAME"));
                    staffTargetModalList=new ArrayList<>();
                    if(staffTargetModalList.size()>0)
                        staffTargetModalList.clear();
                    JSONArray jsonArray=jsonObject.getJSONArray("ReportingPersonTarget");
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
                    TextView welcome=findViewById(R.id.welcome);
                    TextView version=findViewById(R.id.version);
                    TextView division=findViewById(R.id.division);
                    welcome.setText("Welcome : ");
                    division.setText("Division:");
                    version.setText("Version:"+ BuildConfig.VERSION_NAME);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        StaffTargetModal staffTargetModal=new StaffTargetModal();
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
                        if(i%2==0)
                        {
                            staffTargetModal.setBackgroundColor("#DFDFDF");
                            staffTargetModal.setTextColor("#000000");
                        }
                        else
                        {
                            staffTargetModal.setBackgroundColor("#FFFFFF");
                            staffTargetModal.setTextColor("#000000");
                        }
                        staffTargetModalList.add(staffTargetModal);
                    }


                    if(growerActivityDetailsModelList.size()>0)
                        growerActivityDetailsModelList.clear();

                    JSONArray jsonArraySub=jsonObject.getJSONArray("SubReportingPerson");
                    for(int i=0;i<jsonArraySub.length();i++)
                    {
                        GrowerActivityDetailsModel growerActivityDetailsModel=new GrowerActivityDetailsModel();
                        JSONObject jsonObject1=jsonArraySub.getJSONObject(i);
                        growerActivityDetailsModel.setEmployeeCode(jsonObject1.getString("EmpCode"));
                        growerActivityDetailsModel.setEmployeeName(jsonObject1.getString("EmpName"));
                        growerActivityDetailsModel.setEmployeePhone(jsonObject1.getString("Phone"));
                        growerActivityDetailsModel.setLavel(jsonObject1.getString("Level"));
                        growerActivityDetailsModel.setGetActivityData(jsonObject1.getJSONArray("SubTarget"));
                        growerActivityDetailsModel.setOpenBy(true);
                        growerActivityDetailsModelList.add(growerActivityDetailsModel);
                    }

                }
                RecyclerView recyclerView =findViewById(R.id.recycler_main_target);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                recyclerView.setAdapter(stockSummeryAdapter);



                RecyclerView recyclersubReportView =findViewById(R.id.recycler_sub_target);
                GridLayoutManager subReportManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclersubReportView.setHasFixedSize(true);
                recyclersubReportView.setLayoutManager(subReportManager);
                RaActivityTargetAdapter subReportAdapter =new RaActivityTargetAdapter(context,growerActivityDetailsModelList);
                recyclersubReportView.setAdapter(subReportAdapter);


            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

}
