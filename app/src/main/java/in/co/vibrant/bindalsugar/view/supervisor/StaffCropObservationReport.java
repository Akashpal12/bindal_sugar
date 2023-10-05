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
import in.co.vibrant.bindalsugar.adapter.ListCropObservationReportAdapter;
import in.co.vibrant.bindalsugar.model.CaneObservationReportModel;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.InternetCheck;


public class StaffCropObservationReport extends AppCompatActivity {


    DBHelper dbh;
    SQLiteDatabase db;
    boolean IsRatingDialogue=true;
    Boolean doubleBackToExitPressedOnce=false;
    List <GrowerFinderModel> dashboardData;
    List<UserDetailsModel> userDetailsModels;
    List<CaneObservationReportModel> caneObservationReportModelList;
    Toolbar toolbar;
    String factory,Village,Grower,fromDate,toDate,season,Supervisor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_crop_observation_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffCropObservationReport.this;
        //setTitle("Crop Observation Report");
        /*FloatingActionButton fab = findViewById(R.id.fab);*/
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dashboardData=new ArrayList<>();
        //Lat="28.896658";
        //Lng="78.3980435";
        factory=getIntent().getExtras().getString("factory");
        Village=getIntent().getExtras().getString("Village");
        Grower=getIntent().getExtras().getString("Grower");
        fromDate=getIntent().getExtras().getString("DateFrom");
        toDate=getIntent().getExtras().getString("DateTo");
        season=getIntent().getExtras().getString("season");
        Supervisor=getIntent().getExtras().getString("Supervisor");
        userDetailsModels=dbh.getUserDetailsModel();
        setTitle(getString(R.string.MENU_CROP_OBSERVATION_REPORT));
        toolbar.setTitle(getString(R.string.MENU_CROP_OBSERVATION_REPORT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        caneObservationReportModelList=new ArrayList<>();
        if(new InternetCheck(context).isOnline())
        {
            new GetData().execute();
        }
        else
        {
            //AlertPopUp("No internet found");
        }

    }

    class GetData extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details",true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GETCROPOBSERVATIONREPORT";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("User",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("Villcode",Village));
                entity.add(new BasicNameValuePair("Growcode",Grower));
                entity.add(new BasicNameValuePair("SUPCODE",Supervisor));
                entity.add(new BasicNameValuePair("FDate",fromDate));
                entity.add(new BasicNameValuePair("TODate",toDate));
                entity.add(new BasicNameValuePair("Season",season));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + Content);
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            JSONArray jsonArray1=new JSONArray();
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                caneObservationReportModelList=new ArrayList<>();
                JSONArray jsonArray = new JSONArray(Content);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    CaneObservationReportModel indentModel=new CaneObservationReportModel();
                    indentModel.setUserCode(jsonObject.getString("UCODE"));
                    indentModel.setUserName(jsonObject.getString("UNAME"));
                    indentModel.setGrowerVillCode(jsonObject.getString("GVILLCODE"));
                    indentModel.setGrowerVillName(jsonObject.getString("GVILLNAME"));
                    indentModel.setGrowerCode(jsonObject.getString("GCODE"));
                    indentModel.setGrowerName(jsonObject.getString("GNAME"));
                    indentModel.setGrowerFatherName(jsonObject.getString("GFATHER"));
                    indentModel.setPlotVillageCode(jsonObject.getString("PLVILLCODE"));
                    indentModel.setPlotVillageName(jsonObject.getString("PLVILLNAME"));
                    indentModel.setPlotNumber(jsonObject.getString("PLOTNO"));
                    indentModel.setVarietyGroup(jsonObject.getString("CANETYPECATEG"));
                    indentModel.setCaneEarthing(jsonObject.getString("CANEEARTHING"));
                    indentModel.setCanePropping(jsonObject.getString("CANEPROPPING"));
                    indentModel.setActivity(jsonObject.getString("ACTIVITY"));
                    indentModel.setCropCondition(jsonObject.getString("CROPCONDITION"));
                    indentModel.setDisease(jsonObject.getString("DISEASE"));
                    indentModel.setIrregation(jsonObject.getString("IRRIGATION"));
                    indentModel.setRemark(jsonObject.getString("REMARK"));
                    indentModel.setImage(jsonObject.getString("IMAGE"));
                    indentModel.setEntryDate(jsonObject.getString("ENTRYDATE"));
                    indentModel.setVariety(jsonObject.getString("VARIETY"));
                    indentModel.setCaneType(jsonObject.getString("CANETYPE"));
                    caneObservationReportModelList.add(indentModel);
                }

                RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                //RaSelfTargetAdapter stockSummeryAdapter =new RaSelfTargetAdapter(context,staffTargetModalList);
                ListCropObservationReportAdapter stockSummeryAdapter =new ListCropObservationReportAdapter(context,caneObservationReportModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
            } catch (JSONException e) {

                new AlertDialogManager().RedDialog(context,Content);
            }catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
                new AlertDialogManager().RedDialog(context,"Error :- "+e.getClass().getName()+" - "+ e.getMessage());
            }
        }
    }

}