package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GrowerPurchiActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerPurchiActivityModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffGrowerPurchiActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Toolbar toolbar;
    //List<GrowerEnquiryDbModel> growerEnquiryDbModelList;
    List<GrowerPurchiActivityModel> dashboardData;

    List<UserDetailsModel> userDetailsModels;
    String village,grower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_loan_enquiry);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PURCHI ACTIVITY");
        toolbar.setTitle("PURCHI ACTIVITY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=dbh.getUserDetailsModel();
        village=getIntent().getExtras().getString("village");
        grower=getIntent().getExtras().getString("grower");
        dashboardData=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //growerEnquiryDbModelList=dbh.getGrowerEnquiryDbModel();
        new GetDataList().execute();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(StaffGrowerPurchiActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StaffGrowerPurchiActivity.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GE_Grower_Purchy_Info);
                request1.addProperty("IMEINO", new GetDeviceImei(StaffGrowerPurchiActivity.this).GetDeviceImeiNumber());
                request1.addProperty("Divn", userDetailsModels.get(0).getDivision());
                request1.addProperty("V_Code", village);
                request1.addProperty("G_Code", grower);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GE_Grower_Purchy_Info, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GE_Grower_Purchy_InfoResult").toString();
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
            dialog.dismiss();
            dashboardData=new ArrayList<>();

            try{
                JSONObject obj=new JSONObject(message);
                if(obj.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=obj.getJSONArray("DATA");
                    GrowerPurchiActivityModel header = new GrowerPurchiActivityModel();
                    header.setCropType("Crop Type");
                    header.setVarietyCode("Variety");
                    header.setIsFtn("FTN");
                    header.setIsCol("Column");
                    header.setMode("Mode");
                    header.setPurchyNumber("Purchi Number");
                    header.setIssueDate("Issue Date");
                    header.setDispatchedDate("Dispatch Date");
                    header.setStatus("Status");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    dashboardData.add(header);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GrowerPurchiActivityModel model = new GrowerPurchiActivityModel();
                        model.setCropType(jsonObject.getString("CROP_TYPE"));
                        model.setVarietyCode(jsonObject.getString("IS_VAR_CODE"));
                        model.setIsFtn(jsonObject.getString("IS_FTN"));
                        model.setIsCol(jsonObject.getString("IS_COL"));
                        model.setMode(jsonObject.getString("MD_NAME"));
                        model.setPurchyNumber(jsonObject.getString("IS_NO"));
                        model.setIssueDate(jsonObject.getString("IS_IS_DT"));
                        model.setDispatchedDate(jsonObject.getString("IS_DS_DT"));
                        model.setStatus(jsonObject.getString("STATUS"));
                        if(i%2==0)
                        {
                            model.setColor("#DFDFDF");
                            model.setTextColor("#000000");
                        }
                        else
                        {
                            model.setColor("#FFFFFF");
                            model.setTextColor("#000000");
                        }
                        dashboardData.add(model);
                    }
                }
                else
                {
                    new AlertDialogManager().AlertPopUpFinish(StaffGrowerPurchiActivity.this,obj.getString("MSG"));
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_view);
                GridLayoutManager manager = new GridLayoutManager(StaffGrowerPurchiActivity.this, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                GrowerPurchiActivityAdapter adapter =new GrowerPurchiActivityAdapter(StaffGrowerPurchiActivity.this,dashboardData);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(StaffGrowerPurchiActivity.this,"No data found "+e.toString());
            }
        }
    }



}