package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;

public class TruckInTransitActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce=false;
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
   /* List<TransportDetails> transportDetailsList;
    List<TruckInCentreModal> truckDetailsList;*/
    Button centreButton,gateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_in_transit);
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
        context= TruckInTransitActivity.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        //transportDetailsList=dbh.getTransportDetails();
        centreButton=findViewById(R.id.centreButton);
        gateButton=findViewById(R.id.gateButton);
        setTitle(getString(R.string.MENU_TRUCK_TRANSIT));
        toolbar.setTitle(getString(R.string.MENU_TRUCK_TRANSIT));
        //truckDetailsList=new ArrayList<>();
     //   new GetTruckTowardsGateDetails().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory());
    }

 /*   public void openTowardsCentre(View v)
    {
        centreButton.setBackgroundColor(Color.parseColor("#451F16"));
        gateButton.setBackgroundColor(Color.parseColor("#C17402"));
        new GetTruckDetails().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory());
    }

    public void openTowardsGate(View v)
    {
        gateButton.setBackgroundColor(Color.parseColor("#451F16"));
        centreButton.setBackgroundColor(Color.parseColor("#C17402"));
        new GetTruckTowardsGateDetails().execute(transportDetailsList.get(0).getTransporterCode(),transportDetailsList.get(0).getTransporterFactory());
    }



    private class GetTruckDetails extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTruckInTransitTowardsCentre);
                request1.addProperty("trans_code", params[0]);
                request1.addProperty("f_code", params[1]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTruckInTransitTowardsCentre, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTruckInTransitTowardsCentreResult").toString();
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
                    if(jsonArray.length()>0)
                    {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            TruckInCentreModal truckDetails=new TruckInCentreModal();
                            truckDetails.setCentreCode(object.getString("C_CODE"));
                            truckDetails.setCentreName(object.getString("C_NAME"));
                            truckDetails.setTruckName(object.getString("TR_NUMBER"));
                            truckDetails.setDeparterTime(object.getString("TRANSITTIME"));
                            truckDetails.setDriverName(object.getString("TR_DRIVER"));
                            truckDetails.setDriverNumber(object.getString("MOBILE"));
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
                    TruckInTransitTowardsGateAdapter stockSummeryAdapter =new TruckInTransitTowardsGateAdapter(context,truckDetailsList);
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
                    TruckInTransitAdapter stockSummeryAdapter =new TruckInTransitAdapter(context,truckDetailsList);
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


    private class GetTruckTowardsGateDetails extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTruckInTransit);
                request1.addProperty("trans_code", params[0]);
                request1.addProperty("f_code", params[1]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTruckInTransit, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTruckInTransitResult").toString();
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
                    if(jsonArray.length()>0)
                    {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            TruckInCentreModal truckDetails=new TruckInCentreModal();
                            truckDetails.setCentreCode(object.getString("C_CODE"));
                            truckDetails.setCentreName(object.getString("C_NAME"));
                            truckDetails.setTruckName(object.getString("TR_NUMBER"));
                            truckDetails.setDeparterTime(object.getString("DEPTIME"));
                            truckDetails.setCaneWeight(object.getString("CH_WEIGHT"));
                            truckDetails.setTranTime(object.getString("TRANTIME"));
                            truckDetails.setDriverName(object.getString("TR_DRIVER"));
                            truckDetails.setDriverNumber(object.getString("MOBILE"));
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
                    TruckInTransitAdapter stockSummeryAdapter =new TruckInTransitAdapter(context,truckDetailsList);
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
                    TruckInTransitAdapter stockSummeryAdapter =new TruckInTransitAdapter(context,truckDetailsList);
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
