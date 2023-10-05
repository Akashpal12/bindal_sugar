package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListPlantingVeritalCheckAdapter;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class StaffDevelopmentVeritalCheck extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DBHelper dbh;
    SQLiteDatabase db;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean IsRatingDialogue=true;
    NavigationView navigationView;
    Boolean doubleBackToExitPressedOnce=false;
    List <GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    String TAG="";
    Toolbar toolbar;
    String Lat,Lng;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_finder);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffDevelopmentVeritalCheck.this;
        //setTitle("HOURLY CRUSHING");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        Lat=getIntent().getExtras().getString("lat");
        Lng=getIntent().getExtras().getString("lng");
        userDetailsModels=dbh.getUserDetailsModel();
        setTitle(getString(R.string.MENU_VERITAL_CHECK));
        toolbar.setTitle(getString(R.string.MENU_VERITAL_CHECK));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(new InternetCheck(context).isOnline())
        {
            new GetDataList().execute(Lat,Lng);
        }
        else
        {
            //AlertPopUp("No internet found");
        }

    }


    private class GetDataList extends AsyncTask<String, Void, Void> {
        String Content;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GrowerFinder";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();

                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                //entity.add(new BasicNameValuePair("LAT","29.023719"));
                //entity.add(new BasicNameValuePair("LON","78.264349"));
                entity.add(new BasicNameValuePair("LAT",params[0]));
                entity.add(new BasicNameValuePair("LON",params[1]));


                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + Content);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            try{
                Log.d(TAG, Content);
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()==0)
                {
                    new AlertDialogManager().RedDialog(context,"Error: No details found");
                }
                else {
                    double totalNumber=0;
                    DecimalFormat decimalFormat = new DecimalFormat("##.00");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GrowerFinderModel growerFinderModel = new GrowerFinderModel();
                        growerFinderModel.setPlotVillageCode(jsonObject.getString("PLVILLCODE"));
                        growerFinderModel.setPlotVillageName(jsonObject.getString("PLVillName"));
                        growerFinderModel.setGrowerName(jsonObject.getString("GName"));
                        growerFinderModel.setGrowerCode(jsonObject.getString("GrowCode"));
                        growerFinderModel.setVillageCode(jsonObject.getString("VillCode"));
                        growerFinderModel.setVillageName(jsonObject.getString("VillName"));
                        growerFinderModel.setFather(jsonObject.getString("FatherName"));
                        growerFinderModel.setId("---");
                        growerFinderModel.setCaneArea(jsonObject.getString("TOTAREA"));
                        growerFinderModel.setSurveyDate(jsonObject.getString("PDate"));
                        growerFinderModel.setDopDate(jsonObject.getString("PDate"));
                        growerFinderModel.setNorthEastLat(jsonObject.getDouble("LAT1"));
                        growerFinderModel.setNorthEastLng(jsonObject.getDouble("LON1"));
                        growerFinderModel.setNorthWestLat(jsonObject.getDouble("LAT2"));
                        growerFinderModel.setNorthWestLng(jsonObject.getDouble("LON2"));
                        growerFinderModel.setSouthEastLat(jsonObject.getDouble("LAT3"));
                        growerFinderModel.setSouthEastLng(jsonObject.getDouble("LON3"));
                        growerFinderModel.setSouthWestLat(jsonObject.getDouble("LAT4"));
                        growerFinderModel.setSouthWestLng(jsonObject.getDouble("LON4"));
                        growerFinderModel.setPlotNo(jsonObject.getString("PLVILLNO"));

                        growerFinderModel.setMobile("---");
                        growerFinderModel.setVarietyGroupCode(jsonObject.getString("VrName"));
                        growerFinderModel.setGroupArea(jsonObject.getString("TOTAREA"));
                        growerFinderModel.setPlotPercentage("---");
                        growerFinderModel.setVariety(jsonObject.getString("VrName"));
                        growerFinderModel.setPrakar(jsonObject.getString("CropName"));
                        growerFinderModel.setDataFrom(jsonObject.getString("PDate"));
                        dashboardData.add(growerFinderModel);
                    }
                    recyclerView =findViewById(R.id.recycler_view);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    ListPlantingVeritalCheckAdapter listGrowerFinderAdapter =new ListPlantingVeritalCheckAdapter(context,dashboardData);
                    recyclerView.setAdapter(listGrowerFinderAdapter);
                }

            }
            catch (JSONException e)
            {
                new AlertDialogManager().RedDialog(context,Content);
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                //AlertPopUpFinish("Error: "+e.toString());
            }
        }
    }


}