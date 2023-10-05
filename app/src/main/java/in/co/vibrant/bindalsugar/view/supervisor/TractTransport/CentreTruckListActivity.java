package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.TruckDetails;


public class CentreTruckListActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    //List<TransportDetails> transportDetailsList;
    List<TruckDetails> truckDetailsList;
    String centerCode,centerName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context= CentreTruckListActivity.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
       // transportDetailsList=dbh.getTransportDetails();
        centerCode=getIntent().getExtras().getString("center_code");
        centerName=getIntent().getExtras().getString("center_name");
        setTitle(centerCode+"/"+centerName);
        toolbar.setTitle(centerCode+"/"+centerName);
        truckDetailsList=new ArrayList<>();
        //new GetTruckDetails().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory(),centerCode);
    }

    /*private class GetTruckDetails extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTransporterCentreTrucks);
                request1.addProperty("trans_code", params[0]);
                request1.addProperty("tr_factory", params[1]);
                request1.addProperty("c_code", params[2]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTransporterCentreTrucks, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTransporterCentreTrucksResult").toString();
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
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    if(truckDetailsList.size()>0)
                        truckDetailsList.clear();
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    TruckDetails header=new TruckDetails();
                    header.setTruckCode("Code");
                    header.setTruckName("Number");
                    header.setStatus("Status");
                    header.setDriverName("Driver");
                    header.setDriverMobile("Driver Mobile");
                    header.setBackgroundColor("#FF9800");
                    header.setColor("#FFFFFF");
                    truckDetailsList.add(header);
                    if(jsonArray.length()>0)
                    {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            TruckDetails truckDetails=new TruckDetails();
                            truckDetails.setTruckCode(object.getString("C_CODE"));
                            truckDetails.setTruckName(object.getString("TR_NUMBER"));
                            truckDetails.setStatus(object.getString("TRUCKSTATUS"));
                            truckDetails.setDriverName(object.getString("TR_DRIVER"));
                            truckDetails.setDriverMobile(object.getString("MOBILE"));
                            truckDetails.setFactoryCode(transportDetailsList.get(0).getTransporterFactory());
                            if(i%2==0)
                            {
                                truckDetails.setBackgroundColor("#DFDFDF");
                                truckDetails.setColor("#000000");
                            }
                            else
                            {
                                truckDetails.setBackgroundColor("#F5F4E9");
                                truckDetails.setColor("#000000");
                            }
                            truckDetailsList.add(truckDetails);
                        }
                    }
                    RecyclerView recyclerView =findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    TruckListAdapter stockSummeryAdapter =new TruckListAdapter(context,truckDetailsList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                else if(jsonObject.getString("API_STATUS").equalsIgnoreCase("NOTEXISTS"))
                {
                    truckDetailsList.clear();
                    RecyclerView recyclerView =findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    TruckListAdapter stockSummeryAdapter =new TruckListAdapter(context,truckDetailsList);
                    recyclerView.setAdapter(stockSummeryAdapter);
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
*/
}
