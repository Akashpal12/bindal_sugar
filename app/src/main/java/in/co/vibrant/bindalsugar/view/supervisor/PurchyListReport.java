package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import in.co.vibrant.bindalsugar.adapter.PurchyAllDeatilsShowAdapter;
import in.co.vibrant.bindalsugar.model.PurchyReportModal;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class PurchyListReport extends AppCompatActivity {

    Context context;
    SQLiteDatabase db;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModelList;
    List<PurchyReportModal> usersurveyreportlist;
    String villname,villcode,grower_code,PLOTNO,grower_name,grower_fathers;
    TextView Purchyno,Grower,grower_father;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchylistshow);

        context = PurchyListReport.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModelList = dbh.getUserDetailsModel();
        usersurveyreportlist = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.purchy_list_show);
        toolbar.setTitle(R.string.purchy_list_show);
        Purchyno=findViewById(R.id.Purchyno);
        Grower=findViewById(R.id.Grower);
        grower_father=findViewById(R.id.grower_father);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PurchyDetails.class);
                startActivity(intent);
                finish();
            }
        });

        villcode=getIntent().getExtras().getString("vill_code");
        villname=getIntent().getExtras().getString("vill_name");
        grower_code=getIntent().getExtras().getString("grower_code");
        grower_name=getIntent().getExtras().getString("grower_name");
        grower_fathers=getIntent().getExtras().getString("grower_father");
        PLOTNO = getIntent().getExtras().getString("Purchy_no");

        Purchyno.setText("Purchy No. "+PLOTNO);
        grower_father.setText("Father Name : "+grower_fathers);
        Grower.setText("Grower : "+grower_code+" / "+grower_name);
        //grower_father.setText("Father Name : "+);
        new GetSurvetReport().execute();

    }


    private class GetSurvetReport extends AsyncTask<String, Integer, Void> {
        String message;
        String Content;
        private ProgressDialog dialog = new ProgressDialog(context);


        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();
            //textView.setText("Verify details from server");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GETGROWERSURVEYREPORT);
                request1.addProperty("DIVN", userDetailsModelList.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("VILLCODE",villcode);
                request1.addProperty("GROWERCODE", grower_code);
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GETGROWERSURVEYREPORT, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GETGROWERSURVEYREPORTResult").toString();
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
            try {

                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonUserDetailsObject = jsonObject.getJSONArray("DATA");
                    for (int i = 0; i < jsonUserDetailsObject.length(); i++) {
                        JSONObject jsonObject1 = jsonUserDetailsObject.getJSONObject(i);
                        PurchyReportModal data = new PurchyReportModal();
                        data.setPLOTVILLAGECODE(jsonObject1.getString("PLOTVILLAGECODE"));
                        data.setPLOTVILLAGENAME(jsonObject1.getString("PLOTVILLAGENAME"));
                        data.setVARIETYCODE(jsonObject1.getString("VARIETYCODE"));
                        data.setVARIETYNAME(jsonObject1.getString("VARIETYNAME"));
                        data.setCATEGORYNAME(jsonObject1.getString("CATEGORYNAME"));
                        data.setCANETYPE(jsonObject1.getString("CANETYPE"));
                        data.setSHARENO(jsonObject1.getString("SHARENO"));
                        data.setAREAPERCENT(jsonObject1.getString("AREAPERCENT"));
                        data.setAREA(jsonObject1.getString("AREA"));
                        data.setSUPCODE(jsonObject1.getString("SUPCODE"));
                        data.setSUPNAME(jsonObject1.getString("SUPNAME"));

                        data.setGH_NE_LNG(jsonObject1.getString("GH_NE_LNG"));
                        data.setGH_NE_LAT(jsonObject1.getString("GH_NE_LAT"));

                        data.setGH_NW_LAT(jsonObject1.getString("GH_NW_LAT"));
                        data.setGH_NW_LNG(jsonObject1.getString("GH_NW_LNG"));

                        data.setGH_SE_LAT(jsonObject1.getString("GH_SE_LAT"));
                        data.setGH_SE_LNG(jsonObject1.getString("GH_SE_LNG"));


                        data.setGH_SW_LAT(jsonObject1.getString("GH_SW_LAT"));
                        data.setGH_SW_LNG(jsonObject1.getString("GH_SW_LNG"));


                        usersurveyreportlist.add(data);
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                    PurchyAllDeatilsShowAdapter stockSummeryAdapter = new PurchyAllDeatilsShowAdapter(context, usersurveyreportlist);
                    recyclerView.setAdapter(stockSummeryAdapter);
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            } catch (JSONException e) {

                new AlertDialogManager().RedDialog(context, Content);
            } catch (Exception e) {


                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context, "Error :- " + e.getClass().getName() + " - " + e.getMessage());
            }


        }
    }
}

