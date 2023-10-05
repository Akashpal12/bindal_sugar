package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.CRMSummaryDetailAdapter;
import in.co.vibrant.bindalsugar.model.CrmSummaryDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class CRMApprovalReport extends AppCompatActivity {


    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<CrmSummaryDetailModel> modelList;
    DBHelper dbh;
    String T_DATE;
    String F_DATE;
    String PLOT_VL_CODE;
    String PLOT_NO;
    String REPORT_TYPE;
    String T_NAME;
    String S_CODE;
    CardView cross_card;
    TextView Sup_name,report_type,f_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_approval_reportl);


        context = CRMApprovalReport.this;
        dbh = new DBHelper(context);
        userDetailsModels = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        modelList = new ArrayList<>();
        F_DATE= getIntent().getStringExtra("F_DATE");
        T_DATE= getIntent().getStringExtra("T_DATE");
        REPORT_TYPE= getIntent().getStringExtra("REPORT_TYPE");
        S_CODE= getIntent().getStringExtra("S_CODE");
        T_NAME= getIntent().getStringExtra("T_NAME");
        cross_card=findViewById(R.id.cross_card);
        Sup_name=findViewById(R.id.Sup_name);
        report_type=findViewById(R.id.report_type);
        f_date=findViewById(R.id.f_date);

        Sup_name.setText(" "+S_CODE);
        report_type.setText(" "+REPORT_TYPE+ ".  "+T_NAME);
        f_date.setText(" From Date : "+F_DATE+"   To Date : "+T_DATE);


        cross_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new crmSummaryDetail().execute(REPORT_TYPE,F_DATE,T_DATE);
    }

    private class crmSummaryDetail extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Bindal Sugar",
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/crmSummaryDetail";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("sup",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("ReportType", params[0]));
                entity.add(new BasicNameValuePair("fDate", params[1]));
                entity.add(new BasicNameValuePair("tDate", params[2]));
                entity.add(new BasicNameValuePair("divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("seas", "2023-2024"));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
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
            try {
                if (modelList.size() > 0)
                    modelList.clear();

                if (dialog.isShowing())
                    dialog.dismiss();




                JSONObject jsonObject = new JSONObject(Content);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CrmSummaryDetailModel model = new CrmSummaryDetailModel();
                        model.setACT_NAME(object.getString("ACT_NAME"));//DATE
                        model.setU_CODE(object.getString("U_CODE"));//DATE
                        model.setU_NAME(object.getString("U_NAME"));//DATE
                        model.setGV_CODE(object.getString("GV_CODE"));//VILLCODE
                        model.setGV_NAME(object.getString("GV_NAME"));//VILLNAME
                        model.setG_CODE(object.getString("G_CODE"));//GROCODE
                        model.setG_NAME(object.getString("G_NAME"));//GRONAME
                        model.setPV_CODE(object.getString("PV_CODE"));//Bleaching Powder @  384.00
                        model.setPV_NAME(object.getString("PV_NAME"));//Bleaching Powder @  384.00
                        model.setPL_NO(object.getString("PL_NO"));//Bleaching Powder @  384.00
                        model.setDATE(object.getString("DATE"));//Bleaching Powder @  384.00
                        model.setCDO_STATUS(object.getString("CDO_STATUS"));//Bleaching Powder @  384.00
                        model.setCRM_STATUS(object.getString("CRM_STATUS"));//Bleaching Powder @  384.00
                        modelList.add(model);
                    }

                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    CRMSummaryDetailAdapter adapter = new CRMSummaryDetailAdapter(context, modelList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    //main_layout.setVisibility(View.VISIBLE);


                } else {
                    //  main_layout.setVisibility(View.GONE);

                    new AlertDialogManager().RedDialog(context, "" + new JSONObject(Content).getString("MSG").toString());


                }

            } catch (JSONException e) {
                System.out.println("Error : " + Content);
                Log.e(e.getClass().getName(), e.getMessage(), e);
                try {
                    new AlertDialogManager().RedDialog(context, new JSONObject(Content).getString("MSG"));
                } catch (Exception e1) {
                    e1.getMessage();
                }
                // main_layout.setVisibility(View.GONE);
                RecyclerView recyclerView = findViewById(R.id.recycler_list);
                CRMSummaryDetailAdapter adapter = new CRMSummaryDetailAdapter(context, modelList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                // main_layout.setVisibility(View.GONE);
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context, "Error :- " + e.getClass().getName() + " - " + e.getMessage());
            }
        }

    }
}