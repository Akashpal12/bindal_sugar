package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import in.co.vibrant.bindalsugar.adapter.StaffGrowerTargetReportListAdapter;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.StaffGrowerTargetModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffTargetGrowerReport extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<MasterDropDown> targetList;
    List<UserDetailsModel> userDetailsModels;
    List<StaffGrowerTargetModel> staffTargetModelList;
    String VillCode="",target="",VillName="";
    TextView txt_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_report_grower_target_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffTargetGrowerReport.this;
        setTitle(getString(R.string.MENU_TARGET_REPORT));
        toolbar.setTitle(getString(R.string.MENU_TARGET_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        targetList=new ArrayList<>();
        dbh=new DBHelper(context);
        staffTargetModelList=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        VillCode=getIntent().getExtras().getString("VillCode");
        VillName=getIntent().getExtras().getString("VillName");
        target=getIntent().getExtras().getString("target");
        //getSpinner();
        txt_name=findViewById(R.id.txt_name);
        txt_name.setText("Village : "+VillCode+"/"+VillName);
        new getReportDetails().execute();

    }


    private class getReportDetails extends AsyncTask<String, Integer, Void> {
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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/VillageGrowerWiseIndentingArea";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("TargetType",target));
                entity.add(new BasicNameValuePair("VillCode",VillCode));
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);


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
                JSONArray jsonArray = new JSONArray(message);
                staffTargetModelList=new ArrayList<>();
                if(jsonArray.length()==0)
                {
                    new AlertDialogManager().AlertPopUp(context,"No data found");
                }
                else
                {
                    StaffGrowerTargetModel header=new StaffGrowerTargetModel();
                    header.setVillageCode("Vill Code");
                    header.setVillageName("Vill Name");
                    header.setGrowerCode("Grower Code");
                    header.setGrowerName("Grower Name");
                    header.setFatherName("Father Name");
                    header.setVarietyCode("Variety Code");
                    header.setVarietyName("Variety Name");
                    header.setManualArea("Manual Area");
                    header.setArea("Area");
                    header.setIndDate("Indate Date");
                    header.setColor("#000000");
                    header.setTextColor("#FFFFFF");
                    staffTargetModelList.add(header);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        StaffGrowerTargetModel growerModel=new StaffGrowerTargetModel();
                        growerModel.setVillageCode(jsonObject1.getString("VillCode"));
                        growerModel.setVillageName(jsonObject1.getString("VillName"));
                        growerModel.setGrowerCode(jsonObject1.getString("GCode"));
                        growerModel.setGrowerName(jsonObject1.getString("GName"));
                        growerModel.setFatherName(jsonObject1.getString("FNAME"));
                        growerModel.setVarietyCode(jsonObject1.getString("VRCode"));
                        growerModel.setVarietyName(jsonObject1.getString("VRName"));
                        growerModel.setManualArea(jsonObject1.getString("AREAMenual"));
                        growerModel.setArea(jsonObject1.getString("Area"));
                        growerModel.setIndDate(jsonObject1.getString("INDDate"));
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
                        staffTargetModelList.add(growerModel);
                    }
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffGrowerTargetReportListAdapter stockSummeryAdapter =new StaffGrowerTargetReportListAdapter(context,staffTargetModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,message);
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
