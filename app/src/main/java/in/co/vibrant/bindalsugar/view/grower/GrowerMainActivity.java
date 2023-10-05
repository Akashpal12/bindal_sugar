package in.co.vibrant.bindalsugar.view.grower;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.PotatoTodayActivityAdapter;
import in.co.vibrant.bindalsugar.model.PotatoTodayActivityModal;
import in.co.vibrant.bindalsugar.services.Config;
import in.co.vibrant.bindalsugar.services.NotificationUtils;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;
import in.co.vibrant.bindalsugar.view.Home;
import in.co.vibrant.bindalsugar.view.LanguageSettings;


public class GrowerMainActivity extends AppCompatActivity  {

    SQLiteDatabase db;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Context context;
    FusedLocationProviderClient fusedLocationClient;
    List<PotatoTodayActivityModal> potatoTodayActivityModalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context=this;
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if(new InternetCheck(context).isOnline())
            {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    new getWeatherUpdate().execute(""+location.getLatitude(),""+location.getLongitude());
                                }
                            }
                        });

            }
        }
        catch (SecurityException e)
        {

        }
        catch (Exception e)
        {

        }

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


        potatoTodayActivityModalList=new ArrayList<>();
        PotatoTodayActivityModal factoryModel=new PotatoTodayActivityModal();
        for(int i=0;i<2;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            potatoTodayActivityModalList.add(factoryModel);
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_list);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        PotatoTodayActivityAdapter stockSummeryAdapter =new PotatoTodayActivityAdapter(context,potatoTodayActivityModalList);
        recyclerView.setAdapter(stockSummeryAdapter);


        RecyclerView recyclerViewCane =findViewById(R.id.recycler_list_cane);
        GridLayoutManager managerCane = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerViewCane.setHasFixedSize(true);
        recyclerViewCane.setLayoutManager(managerCane);
        PotatoTodayActivityAdapter stockSummeryAdapterCane =new PotatoTodayActivityAdapter(context,potatoTodayActivityModalList);
        recyclerViewCane.setAdapter(stockSummeryAdapterCane);

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
            Intent mapIntent=new Intent(GrowerMainActivity.this, LanguageSettings.class);
            startActivity(mapIntent);
            return true;
        }
        else if (id == R.id.action_logout) {
            Intent mapIntent=new Intent(GrowerMainActivity.this, Home.class);
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
                GrowerMainActivity.this);
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
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url="http://api.openweathermap.org/data/2.5/weather";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat",params[0]));
                entity.add(new BasicNameValuePair("lon",params[1]));
                entity.add(new BasicNameValuePair("appid","1211973affa328939680b7a52bd0cbfe"));
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
                JSONObject object=new JSONObject(Content);
                ImageView image = (ImageView) findViewById(R.id.weather_img);
                TextView weather_txt_1=findViewById(R.id.weather_txt_1);
                TextView weather_txt_2=findViewById(R.id.weather_txt_2);
                TextView weather_txt_3=findViewById(R.id.weather_txt_3);
                TextView weather_txt_4=findViewById(R.id.weather_txt_4);
                TextView weather_txt_5=findViewById(R.id.weather_txt_5);
                TextView weather_city=findViewById(R.id.weather_city);
                JSONArray weather=object.getJSONArray("weather");
                JSONObject weatherObj=weather.getJSONObject(0);
                JSONObject mainObj=object.getJSONObject("main");
                JSONObject sysObj=object.getJSONObject("sys");
                JSONObject cloudsObj=object.getJSONObject("clouds");
                double cent=mainObj.getDouble("temp")-273.15;
                long unixSeconds=sysObj.getLong("sunset");
                long unixsunriseSeconds=sysObj.getLong("sunrise");
                Date date = new java.util.Date(unixSeconds*1000L);
                Date datesunrise = new java.util.Date(unixsunriseSeconds*1000L);
                // the format of your date
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
                SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("dd MMMM EEEE");
                //Date date = new Date(sysObj.getString("sunset"));
                weather_txt_1.setText("Today "+sdf1.format(date));
                weather_txt_2.setText(cent+" °C");
                weather_txt_3.setText("Sunrise "+sdf.format(datesunrise)+"    Sunset "+sdf.format(date));
                weather_txt_4.setText(weatherObj.getString("main"));
                weather_txt_5.setText(cloudsObj.getString("all")+"%");
                weather_city.setText(object.getString("name"));
                Glide.with(context).load("http://openweathermap.org/img/w/"+weatherObj.getString("icon")+".png").into(image);
                //
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
        }
    }

    public void openDevelopment(View v)
    {
        Intent intent=new Intent(context,GrowerDevelopmentDashboard.class);
        startActivity(intent);
    }

    public void openGrowerAskExportForm(View v)
    {
        Intent intent=new Intent(context,GrowerAskExportForm.class);
        startActivity(intent);
    }

    public void openGrower(View v)
    {
        Intent intent=new Intent(context,GrowerDetailsDashboard.class);
        startActivity(intent);
    }

    public void openGrowerActivityDetails(View v)
    {
        Intent intent=new Intent(context,GrowerActivityDetails.class);
        startActivity(intent);
    }







}
