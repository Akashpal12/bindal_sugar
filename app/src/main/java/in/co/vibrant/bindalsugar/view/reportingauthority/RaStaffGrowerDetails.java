package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
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
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class RaStaffGrowerDetails extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    Spinner village, grower;
    List<VillageModal> villageModelList;
    List<GrowerModel> growerModelList;
    List<UserDetailsModel> userDetailsModelList;
    List<GrowerModel> growerSurveyModelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ra_staff_grower_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = RaStaffGrowerDetails.this;
        setTitle(getString(R.string.MENU_GROWER_DETAILS));
        toolbar.setTitle(getString(R.string.MENU_GROWER_DETAILS));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        villageModelList = new ArrayList<>();
        userDetailsModelList = new ArrayList<>();
        growerModelList = new ArrayList<>();
        growerSurveyModelsList = new ArrayList<>();
        village = findViewById(R.id.village);
        grower = findViewById(R.id.grower);
        dbh = new DBHelper(context);
        new GetVillData().execute();

    }
    private class GetVillData extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetVillageList);
                request1.addProperty("factory",params[0]);
                request1.addProperty("Supvcode",params[1]);
                request1.addProperty("Season",2022-2023);
                //request1.addProperty("SEAS", "2022-2023");
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetVillageList, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    Content = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    Content = result.getPropertyAsString("GetVillageListResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    if (jsonArray.length() > 0) {
                        ArrayList<String> data = new ArrayList<String>();
                       // data.add(" - Select  -");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                           data.add(object.getString("VCODE") + " - " + object.getString("VILLAGE NAME"));
                        }
                        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                                R.layout.list_item, data);
                        village.setAdapter(adaptersupply);
                    } else {
                        new AlertDialogManager().showToast(RaStaffGrowerDetails.this, "No Village data found..");
                    }
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

}