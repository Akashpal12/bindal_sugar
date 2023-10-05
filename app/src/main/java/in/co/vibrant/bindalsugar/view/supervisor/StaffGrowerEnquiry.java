package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffGrowerEnquiry extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Toolbar toolbar;

    TextView tv_village_code,tv_village_name,tv_grower_code,tv_grower_name,tv_grower_mobile,tv_centre_name,tv_society_name,tv_total_land,
            tv_basic_quota,tv_mode,tv_avg_upaj;

    List<UserDetailsModel> userDetailsModels;
    String village,grower;

    //List<GrowerEnquiryDbModel> growerEnquiryDbModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_info);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.MENU_GROWER_ENQUIRY));
        toolbar.setTitle(getString(R.string.MENU_GROWER_ENQUIRY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=dbh.getUserDetailsModel();
        tv_village_code=findViewById(R.id.tv_village_code);
        tv_village_name=findViewById(R.id.tv_village_name);
        tv_grower_code=findViewById(R.id.tv_grower_code);
        tv_grower_name=findViewById(R.id.tv_grower_name);
        tv_grower_mobile=findViewById(R.id.tv_grower_mobile);
        tv_centre_name=findViewById(R.id.tv_centre_name);
        tv_society_name=findViewById(R.id.tv_society_name);
        tv_total_land=findViewById(R.id.tv_total_land);
        tv_basic_quota=findViewById(R.id.tv_basic_quota);
        tv_mode=findViewById(R.id.tv_mode);
        tv_avg_upaj=findViewById(R.id.tv_avg_upaj);
        village=getIntent().getExtras().getString("village");
        grower=getIntent().getExtras().getString("grower");
        //growerEnquiryDbModelList=dbh.getGrowerEnquiryDbModel();

        new GetDataList().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backToDashboard(View v) {
        finish();
    }


    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(StaffGrowerEnquiry.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StaffGrowerEnquiry.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GrowerEnquiryData);
                request1.addProperty("IMEINO", new GetDeviceImei(StaffGrowerEnquiry.this).GetDeviceImeiNumber());
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GrowerEnquiryData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GrowerEnquiryDataResult").toString();
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
            try{
                JSONObject obj=new JSONObject(message);
                if(obj.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=obj.getJSONArray("DATA");
                    if(jsonArray.length()==0)
                    {
                        new AlertDialogManager().AlertPopUpFinish(StaffGrowerEnquiry.this,"No data found");
                    }
                    else {
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        tv_village_code.setText(jsonObject.getString("G_VILL"));
                        tv_village_name.setText(jsonObject.getString("V_NAME"));
                        tv_grower_code.setText(jsonObject.getString("G_NO"));
                        tv_grower_name.setText(jsonObject.getString("G_NAME"));
                        tv_grower_mobile.setText(jsonObject.getString("MOBILENO"));
                        tv_centre_name.setText(jsonObject.getString("CN_NAME"));
                        tv_society_name.setText(jsonObject.getString("SO_NAME"));
                        tv_total_land.setText(jsonObject.getString("TOTAL_LAND"));
                        tv_basic_quota.setText(jsonObject.getString("G_BQUOTA"));
                        tv_mode.setText(jsonObject.getString("GMODE"));
                        tv_avg_upaj.setText(jsonObject.getString("AVG_UPAJ"));
                    }
                }
                else
                {
                    new AlertDialogManager().AlertPopUpFinish(StaffGrowerEnquiry.this,obj.getString("MSG"));
                }
            }
            catch (Exception e)
            {
                new AlertDialogManager().AlertPopUpFinish(StaffGrowerEnquiry.this,"Error: "+e.toString());
            }
        }
    }

}