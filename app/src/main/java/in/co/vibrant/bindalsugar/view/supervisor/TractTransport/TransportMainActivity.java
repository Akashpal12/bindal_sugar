package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class TransportMainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce=false;
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    List<UserDetailsModel> userDetailsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_dashboard);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setSupportActionBar(toolbar);

            context = TransportMainActivity.this;
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();

           // new GetData().execute();
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void openTotalFactory(View v)
    {
        Intent intent=new Intent(context, FactoryListActivity.class);
        startActivity(intent);
    }

    public void openTotalTruck(View v)
    {
        Intent intent=new Intent(context, TruckListActivity.class);
        startActivity(intent);
    }

    public void openLiveTracking(View v)
    {
        Intent intent=new Intent(context, LiveTrackingActivity.class);
        startActivity(intent);
    }

    public void openTotalTrip(View v)
    {
        Intent intent=new Intent(context, TotalTripActivity.class);
        startActivity(intent);
    }

    public void openTotalCenter(View v)
    {
        Intent intent=new Intent(context, CenterListActivity.class);
        startActivity(intent);
    }

    public void openTotalTravelKm(View v)
    {
        Intent intent=new Intent(context, TotalKmActivity.class);
        startActivity(intent);
    }

    public void openDelayFast(View v)
    {
      Intent intent=new Intent(context,CenterListActivity.class);
        startActivity(intent);
    }

    public void openSummary(View v)
    {
        Intent intent=new Intent(context,TruckSummary.class);
        startActivity(intent);
    }

    public void openTruckInYard(View v)
    {
        Intent intent=new Intent(context, TruckInYardActivity.class);
        startActivity(intent);
    }

    public void openTruckInTransit(View v)
    {
        Intent intent=new Intent(context, TruckInTransitActivity.class);
        startActivity(intent);
    }

    public void openTruckInCenter(View v)
    {
        Intent intent=new Intent(context, TruckInCentreActivity.class);
        startActivity(intent);
    }

    public void exitApp(View v)
    {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

   /* private class GetData extends AsyncTask<Void, Void, Void> {
        String message;
        //private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetDaskboard);
                request1.addProperty("tr_code", transportDetailsList.get(0).getTransporterCode());
                request1.addProperty("tr_factory", transportDetailsList.get(0).getTransporterFactory());
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetDaskboard, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetDaskboardResult").toString();
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //dialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(message);
                TextView data_1=findViewById(R.id.data_1);
                TextView value_1=findViewById(R.id.value_1);
                TextView data_2=findViewById(R.id.data_2);
                TextView value_2=findViewById(R.id.value_2);
                TextView data_3=findViewById(R.id.data_3);
                TextView value_3=findViewById(R.id.value_3);
                TextView data_4=findViewById(R.id.data_4);
                TextView value_4=findViewById(R.id.value_4);
                TextView data_5=findViewById(R.id.data_5);
                TextView value_5=findViewById(R.id.value_5);
                TextView data_6=findViewById(R.id.data_6);
                TextView value_6=findViewById(R.id.value_6);
                TextView data_7=findViewById(R.id.data_7);
                TextView value_7=findViewById(R.id.value_7);
                TextView data_8=findViewById(R.id.data_8);
                TextView value_8=findViewById(R.id.value_8);
                TextView data_9=findViewById(R.id.data_9);
                TextView value_9=findViewById(R.id.value_9);
                TextView data_10=findViewById(R.id.data_10);
                TextView value_10=findViewById(R.id.value_10);
                TextView data_11=findViewById(R.id.data_11);
                TextView value_11=findViewById(R.id.value_11);
                TextView data_12=findViewById(R.id.data_12);
                TextView value_12=findViewById(R.id.value_12);
                if(jsonArray.length()>0)
                {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(jsonObject.getInt("ID")==1)
                        {
                            data_1.setText(jsonObject.getString("data"));
                            value_1.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==2)
                        {
                            data_2.setText(jsonObject.getString("data"));
                            value_2.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==3)
                        {
                            data_3.setText(jsonObject.getString("data"));
                            value_3.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==4)
                        {
                            data_4.setText(jsonObject.getString("data"));
                            value_4.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==5)
                        {
                            data_5.setText(jsonObject.getString("data"));
                            value_5.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==6)
                        {
                            data_6.setText(jsonObject.getString("data"));
                            value_6.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==7)
                        {
                            data_7.setText(jsonObject.getString("data"));
                            value_7.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==8)
                        {
                            data_8.setText(jsonObject.getString("data"));
                            value_8.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==9)
                        {
                            data_9.setText(jsonObject.getString("data"));
                            value_9.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==10)
                        {
                            data_10.setText(jsonObject.getString("data"));
                            value_10.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==11)
                        {
                            data_11.setText(jsonObject.getString("data"));
                            value_11.setText(jsonObject.getString("heading"));
                        }
                        if(jsonObject.getInt("ID")==12)
                        {
                            data_12.setText(jsonObject.getString("data"));
                            value_12.setText(jsonObject.getString("heading"));
                        }
                    }
                }
            }
            catch(JSONException e)
            {
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }*/

}
