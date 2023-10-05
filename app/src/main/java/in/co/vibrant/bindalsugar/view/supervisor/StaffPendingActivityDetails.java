package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import in.co.vibrant.bindalsugar.adapter.StaffGrowerPendingActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.model.GrowerSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffPendingActivityDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    RelativeLayout caneLayout,potatoLayout;
    List<GrowerActivityDetailsModel> growerActivityDetailsModelList;
    List<VillageSurveyModel> villageSurveyModelList;
    List<GrowerSurveyModel> growerSurveyModelList;
    Spinner village,grower;
    List<UserDetailsModel> userDetailsModels;
    CardView caneActivityCard,potatoActivityCard;
    String villCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_pending_activities_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= StaffPendingActivityDetails.this;
        setTitle(getString(R.string.MENU_ACTIVITIES));
        toolbar.setTitle(getString(R.string.MENU_ACTIVITIES));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        villageSurveyModelList =new ArrayList<>();
        growerSurveyModelList =new ArrayList<>();
        caneActivityCard=findViewById(R.id.caneActivityCard);
        potatoActivityCard=findViewById(R.id.potatoActivityCard);
        villCode=getIntent().getExtras().getString("villCode");
        village=findViewById(R.id.village);
        grower=findViewById(R.id.grower);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();
        growerActivityDetailsModelList=new ArrayList<>();
        new getCaneVillageListFromServer().execute();
    }

    private class getCaneVillageListFromServer extends AsyncTask<Void, Integer, Void> {
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
        protected Void doInBackground(Void... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_PenddingActivityVillageWiseDetails);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("VILLAGECODE", villCode);
                request1.addProperty("U_CODE", userDetailsModels.get(0).getCode());
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_PenddingActivityVillageWiseDetails, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("PenddingActivityVillageWiseDetailsResult").toString();
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
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {

                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    if(growerActivityDetailsModelList.size()>0)
                        growerActivityDetailsModelList.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        GrowerActivityDetailsModel growerActivityDetailsModel=new GrowerActivityDetailsModel();
                        growerActivityDetailsModel.setPlotSerialId(object.getString("PLOT_SR_NUMBER_ID"));
                        growerActivityDetailsModel.setGrowerCode(object.getString("GROWERCODE"));
                        growerActivityDetailsModel.setGrowerName(object.getString("G_NAME"));
                        growerActivityDetailsModel.setGrowerFather(object.getString("G_FATHER"));
                        growerActivityDetailsModel.setGrowerMobile(object.getString("CONTACT"));
                        growerActivityDetailsModel.setPlotNumber(object.getString("PLOT_SR_NUMBER"));
                        growerActivityDetailsModel.setArea(object.getString("AREAHECTARE"));
                        growerActivityDetailsModel.setMixCrop(object.getString("MIXCROP"));
                        growerActivityDetailsModel.setPlotVillage(object.getString("VILLAGECODE"));
                        growerActivityDetailsModel.setPlotVillageName(object.getString("V_NAME"));
                        growerActivityDetailsModel.setVarietyName(object.getString("VARIETY_CODE"));
                        growerActivityDetailsModel.setVarietyName(object.getString("VAR_NAME"));
                        growerActivityDetailsModel.setEastLat(object.getDouble("EAST_LAT"));
                        growerActivityDetailsModel.setEastLng(object.getDouble("EAST_LNG"));
                        growerActivityDetailsModel.setEastDistance(object.getDouble("EAST_DISTANCE"));
                        growerActivityDetailsModel.setSouthLat(object.getDouble("SOUTH_LAT"));
                        growerActivityDetailsModel.setSouthLng(object.getDouble("SOUTH_LNG"));
                        growerActivityDetailsModel.setSouthDistance(object.getDouble("SOUTH_DISTANCE"));
                        growerActivityDetailsModel.setWestLat(object.getDouble("WEST_LAT"));
                        growerActivityDetailsModel.setWestLng(object.getDouble("WEST_LNG"));
                        growerActivityDetailsModel.setWestDistance(object.getDouble("WEST_DISTANCE"));
                        growerActivityDetailsModel.setNorthLat(object.getDouble("NORTH_LAT"));
                        growerActivityDetailsModel.setNorthLng(object.getDouble("NORTH_LNG"));
                        growerActivityDetailsModel.setNorthDistance(object.getDouble("NORTH_DISTANCE"));
                        growerActivityDetailsModel.setGetActivityData(object.getJSONArray("ldbActivityClass"));
                        growerActivityDetailsModelList.add(growerActivityDetailsModel);
                    }
                    RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    StaffGrowerPendingActivityAdapter stockSummeryAdapter =new StaffGrowerPendingActivityAdapter(context,growerActivityDetailsModelList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

}
