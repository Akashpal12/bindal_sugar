package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import in.co.vibrant.bindalsugar.adapter.GetRemedialSummaryReportViewAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.GetRemedialViewAllReportModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class GetRemedialSummaryReportViewDetails extends AppCompatActivity {
    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<GetRemedialViewAllReportModel> modelList;
    DBHelper dbh;
    String T_DATE;
    String F_DATE;
    String PLOT_VILL;
    String PLOT_NO;

    String DISEASE;
    String SUP_C_NAME;
    String PLOT_V_N;


    String V_NAME;
    TextView f_date_txt,plot_village_name,supervisor_code_name;


    CardView cross_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_remedial_report_view_report);
        context = GetRemedialSummaryReportViewDetails.this;
        dbh = new DBHelper(context);
        userDetailsModels = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        modelList = new ArrayList<>();
        PLOT_VILL= getIntent().getStringExtra("PLOT_VILL");
        F_DATE= getIntent().getStringExtra("F_DATE");
        T_DATE= getIntent().getStringExtra("T_DATE");
        PLOT_NO= getIntent().getStringExtra("PLOT_NO");
        DISEASE= getIntent().getStringExtra("DISEASE");
        SUP_C_NAME= getIntent().getStringExtra("SUP_C_NAME");
        PLOT_V_N= getIntent().getStringExtra("PLOT_V_N");


     //  V_NAME= getIntent().getStringExtra("V_NAME");*

         cross_card=findViewById(R.id.cross_card);
         plot_village_name=findViewById(R.id.plot_village_name);
         supervisor_code_name=findViewById(R.id.supervisor_code_name);
        f_date_txt=findViewById(R.id.f_date_txt);

        f_date_txt.setText("From Date : "+F_DATE+"  To Date : "+T_DATE);
        plot_village_name.setText(""+PLOT_V_N);
        supervisor_code_name.setText(""+SUP_C_NAME);

      cross_card.setOnClickListener(v -> {
            finish();

        });


       new PlotActivityGrowerWiseDetailService().execute(PLOT_VILL,PLOT_NO,F_DATE, T_DATE);

    }


    private class PlotActivityGrowerWiseDetailService extends AsyncTask<String, Void, Void> {
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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GetRemedialsActivity";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("Sup", userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("PLVILL", params[0]));
                entity.add(new BasicNameValuePair("PlotNo", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("Seas", new SessionConfig(context).getSeason()));
                entity.add(new BasicNameValuePair("fdate", params[2]));
                entity.add(new BasicNameValuePair("tdate", params[3]));

                HttpPost httpPost = new HttpPost(url);
                Log.d("Entity",""+entity);
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
                        GetRemedialViewAllReportModel model = new GetRemedialViewAllReportModel();
                        model.setSRNO(object.getString("SRNO"));
                        model.setREMCODE(object.getString("REMCODE"));
                        model.setREMNAME(object.getString("REMNAME"));
                        model.setDISP(object.getString("DISP"));
                        model.setDate(object.getString("Date"));
                        model.setIMAGEURL(object.getString("IMAGEURL"));
                        model.setF_DATE(F_DATE);
                        model.setT_DATE(T_DATE);
                        modelList.add(model);
                    }

                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    GetRemedialSummaryReportViewAdapter adapter = new GetRemedialSummaryReportViewAdapter(context, modelList);
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
                GetRemedialSummaryReportViewAdapter adapter = new GetRemedialSummaryReportViewAdapter(context, modelList);
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