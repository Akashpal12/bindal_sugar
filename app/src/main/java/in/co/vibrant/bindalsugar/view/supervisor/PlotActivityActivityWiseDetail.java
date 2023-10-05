package in.co.vibrant.bindalsugar.view.supervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import in.co.vibrant.bindalsugar.adapter.PlotActivityActivityWiseDetailAdapter;
import in.co.vibrant.bindalsugar.model.PlotActivityActivityWiseDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class PlotActivityActivityWiseDetail extends AppCompatActivity {


    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<PlotActivityActivityWiseDetailModel> modelList;
    DBHelper dbh;
    String T_DATE;
    String F_DATE;
    String PLOT_VL_CODE;
    String PLOT_NO;
    String G_VILLCODE;
    CardView cross_card;
    TextView f_date_txt,grower_village_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_activity_wise_detail);


        context = PlotActivityActivityWiseDetail.this;
        dbh = new DBHelper(context);
        userDetailsModels = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        modelList = new ArrayList<>();
        f_date_txt=findViewById(R.id.f_date_txt);
        grower_village_name=findViewById(R.id.grower_village_name);

        PLOT_VL_CODE= getIntent().getStringExtra("PLOT_VL_CODE");
        F_DATE= getIntent().getStringExtra("F_DATE");
        T_DATE= getIntent().getStringExtra("T_DATE");
        PLOT_NO= getIntent().getStringExtra("PLOT_NO");
        G_VILLCODE= getIntent().getStringExtra("G_VILLCODE");
        cross_card=findViewById(R.id.cross_card);

        f_date_txt.setText("From Date : "+F_DATE+"  To Date : "+T_DATE);
        grower_village_name.setText(""+G_VILLCODE);


        cross_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new PlotActivityActivityWiseDetailService().execute(PLOT_VL_CODE,PLOT_NO,F_DATE,T_DATE);
    }

    private class PlotActivityActivityWiseDetailService extends AsyncTask<String, Void, Void> {
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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/PlotActivityActivityWiseDetail";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("plvlc", params[0]));
                entity.add(new BasicNameValuePair("plc", params[1]));
                entity.add(new BasicNameValuePair("Fdate", params[2]));
                entity.add(new BasicNameValuePair("Tdate", params[3]));
                entity.add(new BasicNameValuePair("divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCD", userDetailsModels.get(0).getCode()));
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
                        PlotActivityActivityWiseDetailModel model = new PlotActivityActivityWiseDetailModel();
                        model.setGNAME(object.getString("GNAME"));//DATE
                        model.setGCODE(object.getString("GCODE"));//DATE
                        model.setPLNO(object.getString("PLNO"));//DATE
                        model.setPLOTTYPE(object.getString("PLOTTYPE"));//VILLCODE
                        model.setVARIETY(object.getString("VARIETY"));//VILLNAME
                        model.setACTIVITY(object.getString("ACTIVITY"));//GROCODE
                        model.setMETHOD(object.getString("METHOD"));//GRONAME
                        model.setAREA(object.getString("AREA"));//Bleaching Powder @  384.00
                        model.setDATE(object.getString("DATE"));//Bleaching Powder @  384.00


                        modelList.add(model);





                    }

                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    PlotActivityActivityWiseDetailAdapter adapter = new PlotActivityActivityWiseDetailAdapter(context, modelList);
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
                PlotActivityActivityWiseDetailAdapter adapter = new PlotActivityActivityWiseDetailAdapter(context, modelList);
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