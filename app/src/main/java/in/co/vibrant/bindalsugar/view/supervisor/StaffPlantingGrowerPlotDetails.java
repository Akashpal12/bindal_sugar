package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import in.co.vibrant.bindalsugar.adapter.StaffPlantingGrowerListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffPlantingGrowerPlotDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<GrowerModel> growerModelList;
    List<UserDetailsModel> userDetailsModels;
    String vill,grower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_details_cm_plot);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffPlantingGrowerPlotDetails.this;
        setTitle(getString(R.string.MENU_PLOT_FINDER_PATH_FINDER));
        toolbar.setTitle(getString(R.string.MENU_PLOT_FINDER_PATH_FINDER));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vill=getIntent().getExtras().getString("vill");
        grower=getIntent().getExtras().getString("grower");
        growerModelList=new ArrayList<>();
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        new getGrowerPlotListDetails().execute(vill,grower);
    }


    private class getGrowerPlotListDetails extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/PlantingGrowerDetails";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();

                entity.add(new BasicNameValuePair("IMEINO",imei));
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("V_Code",params[0]));
                entity.add(new BasicNameValuePair("G_Code",params[1]));
                entity.add(new BasicNameValuePair("lang",getString(R.string.language)));
                //entity.add(new BasicNameValuePair("LAT",params[0]));
                //entity.add(new BasicNameValuePair("LON",params[1]));


                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + message);


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
                growerModelList=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(message);
                if(jsonArray.length()==0)
                {
                    new AlertDialogManager().AlertPopUp(context,"No data found");
                }
                else
                {
                    GrowerModel header=new GrowerModel();
                    header.setPlotSerial("Plot Sr ");
                    header.setGrowerSerial("Grower Sr");
                    header.setPlotVillageCode("Plot Vill Code");
                    header.setPlotVillageName("Plot Vill Name");
                    header.setArea("Area");
                    header.setShareArea("Share Area");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    growerModelList.add(header);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        GrowerModel growerModel=new GrowerModel();
                        growerModel.setPlotSerial(jsonObject1.getString("PLOT_SR_NO"));
                        growerModel.setGrowerSerial(jsonObject1.getString("G_CODE"));
                        growerModel.setPlotVillageCode(jsonObject1.getString("V_CODE"));
                        growerModel.setPlotVillageName(jsonObject1.getString("V_CODE"));
                        growerModel.setArea(jsonObject1.getString("AREA"));
                        growerModel.setShareArea(jsonObject1.getString("SHARED_AREA"));
                        growerModel.setLat1(jsonObject1.getDouble("LAT_CORNER_1"));
                        growerModel.setLat2(jsonObject1.getDouble("LAT_CORNER_2"));
                        growerModel.setLat3(jsonObject1.getDouble("LAT_CORNER_3"));
                        growerModel.setLat4(jsonObject1.getDouble("LAT_CORNER_4"));
                        growerModel.setLng1(jsonObject1.getDouble("LON_CORNER_1"));
                        growerModel.setLng2(jsonObject1.getDouble("LON_CORNER_2"));
                        growerModel.setLng3(jsonObject1.getDouble("LON_CORNER_3"));
                        growerModel.setLng4(jsonObject1.getDouble("LON_CORNER_4"));
                        growerModel.setDim1(jsonObject1.getDouble("DIM_1"));
                        growerModel.setDim2(jsonObject1.getDouble("DIM_2"));
                        growerModel.setDim3(jsonObject1.getDouble("DIM_3"));
                        growerModel.setDim4(jsonObject1.getDouble("DIM_4"));
                        if(i%2==0)
                        {
                            growerModel.setColor("#DFDFDF");
                            growerModel.setTextColor("#000000");
                        }
                        else
                        {
                            growerModel.setColor("#FFFFFF");
                            growerModel.setTextColor("#000000");
                        }
                        growerModelList.add(growerModel);
                    }

                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffPlantingGrowerListAdapter stockSummeryAdapter =new StaffPlantingGrowerListAdapter(context,growerModelList);
                recyclerView.setAdapter(stockSummeryAdapter);

                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}
