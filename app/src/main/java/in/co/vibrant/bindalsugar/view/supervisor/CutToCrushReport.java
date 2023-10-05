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

import com.google.android.material.navigation.NavigationView;

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
import in.co.vibrant.bindalsugar.adapter.ListCutToCrushAdapter;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.TotalCTC;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.InternetCheck;

public class CutToCrushReport extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue = true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    List<GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    List<TotalCTC> totalCTCList;
    String TAG = "";
    Toolbar toolbar;
    String Lat, Lng;
    Context context;
    String PLOTVILL,PLOTNO,AREA,GROWFATHER,GROWNAME,GROWCODE,PLOTVILLNAME,GROWER_Village_CODE,GROWER_Village_NAME;
    TextView vill_code,plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_to_crush_report);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = CutToCrushReport.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData = new ArrayList<>();
        totalCTCList = new ArrayList<>();
        userDetailsModels = dbh.getUserDetailsModel();
        setTitle(getString(R.string.MENU_CUT_TO_CRUSH_ON_DATE));
        toolbar.setTitle(getString(R.string.MENU_CUT_TO_CRUSH_ON_DATE));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PLOTNO = getIntent().getExtras().getString("PLOTNO");
        PLOTVILL = getIntent().getExtras().getString("PLOTVILL");
        PLOTVILLNAME = getIntent().getExtras().getString("PLOTVILLNAME");
        GROWCODE = getIntent().getExtras().getString("GROWCODE");
        GROWNAME = getIntent().getExtras().getString("GROWNAME");
        GROWFATHER = getIntent().getExtras().getString("GROWFATHER");
        AREA = getIntent().getExtras().getString("AREA");

        GROWER_Village_CODE=getIntent().getExtras().getString("GROWER_Village_CODE");
        GROWER_Village_NAME=getIntent().getExtras().getString("GROWER_Village_NAME");




        vill_code=findViewById(R.id.vill_code);
        plot=findViewById(R.id.plot);
        vill_code.setText("Village :- "+PLOTVILL+" / "+PLOTVILLNAME);
        plot.setText("Plot Number :- "+PLOTNO);
        if (new InternetCheck(context).isOnline()) {
            new GetDataLists().execute();
        } else {
            //AlertPopUp("No internet found");
        }
    }

    public void openCutToCrush(View v)
    {
        Intent intent = new Intent(context, CutToCrush.class);
        intent.putExtra("V_CODE", PLOTVILL);
        intent.putExtra("V_NAME", PLOTVILLNAME);

        intent.putExtra("G_CODE", GROWCODE);
        intent.putExtra("G_NAME", GROWNAME);

        intent.putExtra("GROWER_Village_CODE", GROWER_Village_CODE);
        intent.putExtra("GROWER_Village_NAME", GROWER_Village_NAME);


        intent.putExtra("PLOT_SR_NO", PLOTNO);

        intent.putExtra("PLOT_VILL", PLOTVILL);
        intent.putExtra("PLOTVILLNAME",PLOTVILLNAME);

        finish();
        startActivity(intent);
    }

    private class GetDataLists extends AsyncTask<String, Integer, Void> {
        String message;
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
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETTOTALCTCONPLOT";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("PLOTVILL", PLOTVILL));
                entity.add(new BasicNameValuePair("PLOTNO", PLOTNO));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + message);

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                dashboardData = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(message);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GrowerFinderModel growerFinderModel = new GrowerFinderModel();
                    growerFinderModel.setPlotVillageCode(jsonObject.getString("PLOTVILL"));
                    growerFinderModel.setPlotNo(jsonObject.getString("PLOTNO"));
                    growerFinderModel.setTOTCTC(jsonObject.getString("CTCDATE"));
                    growerFinderModel.setGrowerCode(GROWCODE);
                    growerFinderModel.setGrowerName(GROWNAME);
                    growerFinderModel.setPlotVillageName(PLOTVILLNAME);
                    growerFinderModel.setCaneArea(AREA);
                    growerFinderModel.setJsonData(jsonObject.getJSONArray("CTCDETAILS"));

                    dashboardData.add(growerFinderModel);
                }
                recyclerView = findViewById(R.id.recycler_view);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                ListCutToCrushAdapter listGrowerFinderAdapter = new ListCutToCrushAdapter(context, dashboardData);
                recyclerView.setAdapter(listGrowerFinderAdapter);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                Intent intent = new Intent(context, CutToCrush.class);
                intent.putExtra("V_CODE", PLOTVILL);
                intent.putExtra("V_NAME", PLOTVILLNAME);
                intent.putExtra("G_CODE", GROWCODE);
                intent.putExtra("PLOT_SR_NO", PLOTNO);
                intent.putExtra("PLOT_VILL", PLOTVILL);
                intent.putExtra("G_NAME", GROWNAME);
                intent.putExtra("PLOTVILLNAME",PLOTVILLNAME);
                intent.putExtra("GROWER_Village_CODE", GROWER_Village_CODE);
                intent.putExtra("GROWER_Village_NAME", GROWER_Village_NAME);

                new AlertDialogManager().AlertPopUpFinishWithIntent(context, message, intent);
            } catch (Exception e) {
                Intent intent = new Intent(context, CutToCrush.class);
                intent.putExtra("V_CODE", PLOTVILL);
                intent.putExtra("V_NAME", PLOTVILLNAME);
                intent.putExtra("G_CODE", GROWCODE);
                intent.putExtra("PLOT_SR_NO", PLOTNO);
                intent.putExtra("PLOT_VILL", PLOTVILL);
                intent.putExtra("G_NAME", GROWNAME);
                intent.putExtra("PLOTVILLNAME",PLOTVILLNAME);
                intent.putExtra("GROWER_Village_CODE", GROWER_Village_CODE);
                intent.putExtra("GROWER_Village_NAME", GROWER_Village_NAME);

                new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Error:" + e.toString(), intent);

            }
        }
    }



}