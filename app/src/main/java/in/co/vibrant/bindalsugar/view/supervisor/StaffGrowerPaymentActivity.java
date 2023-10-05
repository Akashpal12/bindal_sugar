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
import in.co.vibrant.bindalsugar.adapter.GrowerPaymentActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerPaymentActivityModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffGrowerPaymentActivity extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Toolbar toolbar;
    //List<GrowerEnquiryDbModel> growerEnquiryDbModelList;
    List<GrowerPaymentActivityModel> dashboardData;

    List<UserDetailsModel> userDetailsModels;
    String village,grower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_loan_enquiry);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PAYMENT ACTIVITY");
        toolbar.setTitle("PAYMENT ACTIVITY");
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
        private ProgressDialog dialog = new ProgressDialog(StaffGrowerPaymentActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StaffGrowerPaymentActivity.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GE_Grower_Payment_Info);
                request1.addProperty("IMEINO", new GetDeviceImei(StaffGrowerPaymentActivity.this).GetDeviceImeiNumber());
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GE_Grower_Payment_Info, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GE_Grower_Payment_InfoResult").toString();
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
                    GrowerPaymentActivityModel header = new GrowerPaymentActivityModel();
                    header.setPurchyNumber("Purchi Number");
                    header.setMillPurcheyNumber("Mill Pur Number");
                    header.setSupplyDate("Supply Date");
                    header.setShiftDate("Shift Date");
                    header.setNetWeight("Net Weight");
                    header.setAmount("Amount");
                    header.setDecuction("Deduction");
                    header.setNetPay("Net Pay");
                    header.setBillNumber("Bill Number");
                    header.setBillDate("Bill Date");
                    header.setAccountNumber("Account Number");
                    header.setBranchCode("Branch Code");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    dashboardData.add(header);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GrowerPaymentActivityModel model = new GrowerPaymentActivityModel();
                        model.setPurchyNumber(jsonObject.getString("M_IND_NO"));
                        model.setMillPurcheyNumber(jsonObject.getString("MIL_PUR_NO"));
                        model.setSupplyDate(jsonObject.getString("SUPPLY_DT"));
                        model.setShiftDate(jsonObject.getString("M_SHIFT_DATE"));
                        model.setNetWeight(jsonObject.getString("NET_WT"));
                        model.setAmount(jsonObject.getString("AMT"));
                        model.setDecuction(jsonObject.getString("DEDT"));
                        model.setNetPay(jsonObject.getString("NET_PAY"));
                        model.setBillNumber(jsonObject.getString("BILL_NO"));
                        model.setBillDate(jsonObject.getString("BILL_DATE"));
                        model.setAccountNumber(jsonObject.getString("AC_NO"));
                        model.setBranchCode(jsonObject.getString("BRANCH_CODE"));
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
                    new AlertDialogManager().AlertPopUpFinish(StaffGrowerPaymentActivity.this,obj.getString("MSG"));
                }

                RecyclerView recyclerView =findViewById(R.id.recycler_view);
                GridLayoutManager manager = new GridLayoutManager(StaffGrowerPaymentActivity.this, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                GrowerPaymentActivityAdapter adapter =new GrowerPaymentActivityAdapter(StaffGrowerPaymentActivity.this,dashboardData);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(StaffGrowerPaymentActivity.this,"No data found "+e.toString());
            }
        }
    }



}