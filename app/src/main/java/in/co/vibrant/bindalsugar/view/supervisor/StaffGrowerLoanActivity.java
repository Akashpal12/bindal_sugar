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
import in.co.vibrant.bindalsugar.adapter.GrowerLoanActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerLoanActivityModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffGrowerLoanActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Toolbar toolbar;
    //List<GrowerEnquiryDbModel> growerEnquiryDbModelList;
    List<GrowerLoanActivityModel> dashboardData;

    List<UserDetailsModel> userDetailsModels;
    String village,grower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_loan_enquiry);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_LOAN_ACTIVITY));
        toolbar. setTitle(getString(R.string.MENU_LOAN_ACTIVITY));


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
        dashboardData=new ArrayList<>();
        //growerEnquiryDbModelList=dbh.getGrowerEnquiryDbModel();
        village=getIntent().getExtras().getString("village");
        grower=getIntent().getExtras().getString("grower");
        new GetDataList().execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(StaffGrowerLoanActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StaffGrowerLoanActivity.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GE_Grower_Loan_Info);
                request1.addProperty("IMEINO", new GetDeviceImei(StaffGrowerLoanActivity.this).GetDeviceImeiNumber());
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GE_Grower_Loan_Info, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GE_Grower_Loan_InfoResult").toString();
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
                    GrowerLoanActivityModel header = new GrowerLoanActivityModel();
                    header.setRecoveryName("Recovery Name");
                    header.setRecoveryCode("Rec Code");
                    header.setCategCode("Category");
                    header.setLoanAmount("Loan");
                    header.setRecovered("Recovered");
                    header.setBalance("Balance");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    dashboardData.add(header);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GrowerLoanActivityModel model = new GrowerLoanActivityModel();
                        model.setRecoveryName(jsonObject.getString("RECOVERY_NAME"));
                        model.setRecoveryCode(jsonObject.getString("RECV_CODE"));
                        model.setCategCode(jsonObject.getString("CATG_CODE"));
                        model.setLoanAmount(jsonObject.getString("LOAN_AMOUNT"));
                        model.setRecovered(jsonObject.getString("RECOVERED"));
                        model.setBalance(jsonObject.getString("BALANCE"));
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
                    new AlertDialogManager().AlertPopUpFinish(StaffGrowerLoanActivity.this,obj.getString("MSG"));
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_view);
                GridLayoutManager manager = new GridLayoutManager(StaffGrowerLoanActivity.this, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                GrowerLoanActivityAdapter adapter =new GrowerLoanActivityAdapter(StaffGrowerLoanActivity.this,dashboardData);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(StaffGrowerLoanActivity.this,"No data found "+e.toString());
            }
        }
    }


}