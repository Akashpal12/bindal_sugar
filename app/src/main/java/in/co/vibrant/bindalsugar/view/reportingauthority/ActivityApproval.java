package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.StaffActivityApprovalListAdapter;
import in.co.vibrant.bindalsugar.model.PlotActivityApprovalModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class ActivityApproval extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<PlotActivityApprovalModel> plotActivityApprovalModelList;
    EditText remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_activity_approval);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= ActivityApproval.this;
        setTitle("Activity Approval");
        toolbar.setTitle("Activity Approval");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        remark=findViewById(R.id.remark);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        plotActivityApprovalModelList =new ArrayList<>();
        new getReportDetails().execute();
        Button approve=findViewById(R.id.approve);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object>  ids=new ArrayList<>();
                for(int i=1;i<plotActivityApprovalModelList.size();i++)
                {
                    if(plotActivityApprovalModelList.get(i).isChecked())
                    {
                        Map<String,String> hm=new HashMap<>();
                        hm.put("ID",""+plotActivityApprovalModelList.get(i).getActivityId());
                        hm.put("TYPE",""+plotActivityApprovalModelList.get(i).getActivityType());
                        ids.add(hm);
                    }

                }
                Gson gson=new Gson();
                String IdsStr=gson.toJson(ids);
                new SaveApproval().execute(IdsStr,"1");
            }
        });

        Button reject=findViewById(R.id.reject);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object>  ids=new ArrayList<>();
                for(int i=1;i<plotActivityApprovalModelList.size();i++)
                {
                    if(plotActivityApprovalModelList.get(i).isChecked())
                    {
                        Map<String,String> hm=new HashMap<>();
                        hm.put("ID",""+plotActivityApprovalModelList.get(i).getActivityId());
                        hm.put("TYPE",""+plotActivityApprovalModelList.get(i).getActivityType());
                        ids.add(hm);
                    }

                }
                Gson gson=new Gson();
                String IdsStr=gson.toJson(ids);
                new SaveApproval().execute(IdsStr,"2");
            }
        });

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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/Approval_CDO";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                entity.add(new BasicNameValuePair("Divn",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("Seas",getString(R.string.season)));
                entity.add(new BasicNameValuePair("UserCode",userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if(dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject=new JSONObject(message);
                PlotActivityApprovalModel header=new PlotActivityApprovalModel();
                header.setChecked(false);
                header.setBackgroundColor("#000000");
                header.setTextColor("#FFFFFF");
                plotActivityApprovalModelList.add(header);
                if(jsonObject.getString("STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject obj=jsonArray.getJSONObject(i);
                        PlotActivityApprovalModel plotActivityApprovalModel =new PlotActivityApprovalModel();
                        plotActivityApprovalModel.setActivityId(obj.getInt("ID"));
                        plotActivityApprovalModel.setActivityType(obj.getString("ACTIVITYTYPE"));
                        plotActivityApprovalModel.setPlotVillageCode(obj.getInt("PLOTVILLAGECODE"));
                        plotActivityApprovalModel.setPlotVillageName(obj.getString("PLOTVILLAGENAME"));
                        plotActivityApprovalModel.setPlotNumber(obj.getInt("PLOTNO"));
                        plotActivityApprovalModel.setGrowerVillageCode(obj.getInt("VILLAGECODE"));
                        plotActivityApprovalModel.setGrowerVillageName(obj.getString("VILLAGENAME"));
                        plotActivityApprovalModel.setGrowerCode(obj.getInt("GROWERCODE"));
                        plotActivityApprovalModel.setGrowerName(obj.getString("GROWERNAME"));
                        plotActivityApprovalModel.setGrowerFather(obj.getString("FATHER"));
                        plotActivityApprovalModel.setArea(obj.getDouble("AREA"));
                        plotActivityApprovalModel.setActivity(obj.getString("ACTIVITY"));
                        plotActivityApprovalModel.setActivityMethod(obj.getString("ACTIVITYMATHOD"));
                        plotActivityApprovalModel.setMeetingType(obj.getString("MEETINGTYPE"));
                        plotActivityApprovalModel.setMeetingName(obj.getString("MEETINGNAME"));
                        plotActivityApprovalModel.setMeetingNumber(obj.getString("MEETINGNUMBER"));
                        plotActivityApprovalModel.setChecked(false);
                        if(i%2==0)
                        {
                            plotActivityApprovalModel.setBackgroundColor("#DFDFDF");
                            plotActivityApprovalModel.setTextColor("#000000");
                        }
                        else
                        {
                            plotActivityApprovalModel.setBackgroundColor("#FFFFFF");
                            plotActivityApprovalModel.setTextColor("#000000");
                        }
                        plotActivityApprovalModelList.add(plotActivityApprovalModel);
                    }
                }
                else{
                    new AlertDialogManager().showToast((Activity) context,jsonObject.getString("MSG"));
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffActivityApprovalListAdapter stockSummeryAdapter =new StaffActivityApprovalListAdapter(context,plotActivityApprovalModelList);
                recyclerView.setAdapter(stockSummeryAdapter);

                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }



    private class SaveApproval extends AsyncTask<String, Integer, Void> {
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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/UpdateApproval";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //entity.add(new BasicNameValuePair("IMEINO",new GetDeviceImei(context).GetDeviceImeiNumber()));
                //entity.add(new BasicNameValuePair("Divn",userDetailsModels.get(0).getDivision()));
                //entity.add(new BasicNameValuePair("Seas",getString(R.string.season)));
                entity.add(new BasicNameValuePair("UserCode",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("ApproveStatus",params[1]));
                entity.add(new BasicNameValuePair("JSON",params[0]));
                entity.add(new BasicNameValuePair("Remarks",remark.getText().toString()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                Intent intent=new Intent(context,ActivityApproval.class);
                JSONObject obj=new JSONObject(message);
                if(obj.getString("STATUS").equalsIgnoreCase("OK"))
                {
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context,obj.getString("MSG"),intent);
                }
                else{
                    new AlertDialogManager().showToast((Activity) context,obj.getString("MSG"));
                }
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}
