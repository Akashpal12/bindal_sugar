package in.co.vibrant.bindalsugar.view.supervisor.TractTransport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.FactoryListAdapter;
import in.co.vibrant.bindalsugar.model.FactoryModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class FactoryListActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce=false;
    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    //List<TransportDetails> transportDetailsList;
    List<FactoryModel> factoryModelList,factoryModelListSpinner;
    AlertDialog Alertdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_list);
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
        context= FactoryListActivity.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        //transportDetailsList=dbh.getTransportDetails();
        setTitle(getString(R.string.MENU_TRUCK_FACTOEY));
        toolbar.setTitle(getString(R.string.MENU_TRUCK_FACTOEY));
       factoryModelList=new ArrayList<>();
        factoryModelListSpinner=new ArrayList<>();
       // new GetFactoryDetails().execute(transportDetailsList.get(0).getTransporterCode());

        RecyclerView recycler_view = findViewById(R.id.recycler_list);
        GridLayoutManager manager = new GridLayoutManager(context, 1,
                GridLayoutManager.VERTICAL, true);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(manager);
        FactoryListAdapter productAdapter = new FactoryListAdapter(FactoryListActivity.this,
                1);
        recycler_view.setAdapter(productAdapter);

    }
    public void addNewItem(View v){
        new AlertDialogManager().showToast((Activity) context,"Under construction");
       //new GetFactoryList().execute();

    }
}

/* private class GetFactoryDetails extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTransporterFactoryToView);
                request1.addProperty("tr_code", params[0]);
                request1.addProperty("f_code", transportDetailsList.get(0).getTransporterFactory());
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTransporterFactoryToView, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTransporterFactoryToViewResult").toString();
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
                    if(factoryModelList.size()>0)
                        factoryModelList.clear();
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    FactoryModel header=new FactoryModel();
                    header.setFactoryCode("Code");
                    header.setFactoryName("Name");
                    header.setStatus("Status");
                    header.setBackgroundColor("#FF9800");
                    header.setColor("#FFFFFF");
                    factoryModelList.add(header);
                    if(jsonArray.length()>0)
                    {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            FactoryModel truckDetails=new FactoryModel();
                            truckDetails.setFactoryCode(object.getString("F_CODE"));
                            truckDetails.setFactoryName(object.getString("F_NAME"));
                            truckDetails.setStatus(object.getString("ISACTIVE"));
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
                            factoryModelList.add(truckDetails);
                        }
                    }
                    RecyclerView recyclerView =findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    FactoryListAdapter stockSummeryAdapter =new FactoryListAdapter(context,factoryModelList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                else if(jsonObject.getString("API_STATUS").equalsIgnoreCase("NOTEXISTS"))
                {
                    factoryModelList.clear();
                    RecyclerView recyclerView =findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    FactoryListAdapter stockSummeryAdapter =new FactoryListAdapter(context,factoryModelList);
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

    public void addNewItem(View v)
    {
        new GetFactoryList().execute();
    }

    private class GetFactoryList extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTransporterFactoryToAdd);
                request1.addProperty("tr_code", transportDetailsList.get(0).getTransporterCode());
                request1.addProperty("IMEI", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTransporterFactoryToAdd, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTransporterFactoryToAddResult").toString();
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
                ArrayList<String> datasupply=new ArrayList<String>();
                datasupply.add("Select Your Factory");
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().RedDialog(context,"No factory mapping");
                    }
                    else
                    {
                        factoryModelListSpinner.clear();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject obj=jsonArray.getJSONObject(i);
                            FactoryModel factoryModel=new FactoryModel();
                            factoryModel.setFactoryCode(obj.getString("F_CODE"));
                            factoryModel.setFactoryName(obj.getString("F_NAME"));
                            datasupply.add(obj.getString("F_NAME"));
                            factoryModelListSpinner.add(factoryModel);
                        }
                    }
                    ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                            R.layout.list_item, datasupply);


                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_add_factory, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();

                    final Spinner input_factory=mView.findViewById(R.id.input_factory);
                    final EditText transporter_code=mView.findViewById(R.id.transporter_code);
                    final Button btn_ok=mView.findViewById(R.id.btn_ok);
                    input_factory.setAdapter(adaptersupply);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(input_factory.getSelectedItemPosition()==0)
                            {
                                new AlertDialogManager().RedDialog(context,getString(R.string.ERR_FACTORY));
                            }
                            else if(transporter_code.getText().toString().length()==0)
                            {
                                new AlertDialogManager().RedDialog(context,getString(R.string.ERR_TRANS_NUMBER));
                            }
                            else
                            {
                                new SaveFactoryList().execute(transporter_code.getText().toString(),
                                        factoryModelListSpinner.get(input_factory.getSelectedItemPosition()-1).getFactoryCode());
                                Alertdialog.dismiss();
                            }
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(true);
                    Alertdialog.setCanceledOnTouchOutside(true);
                }
                else
                {
                    new AlertDialogManager().AlertPopUpFinish(context,jsonObject.getString("MSG"));
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

    private class SaveFactoryList extends AsyncTask<String, Void, Void> {
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
        protected Void doInBackground(String ... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_AddTransporterFactory);
                request1.addProperty("u_code", params[0]);
                request1.addProperty("f_code", params[1]);
                request1.addProperty("IMEINo", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_AddTransporterFactory, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("AddTransporterFactoryResult").toString();
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
                    new AlertDialogManager().GreenDialog(context,jsonObject.getString("MSG"));
                    new GetFactoryDetails().execute(transportDetailsList.get(0).getTransporterCode());
                }
                else
                {
                    new AlertDialogManager().AlertPopUpFinish(context,jsonObject.getString("MSG"));
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
    }*/

