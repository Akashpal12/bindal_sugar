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
import in.co.vibrant.bindalsugar.adapter.StaffGrowerPlotListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffGrowerPlotDetails extends AppCompatActivity  {

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
        context= StaffGrowerPlotDetails.this;
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetGrowerByVillCodeData);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("V_Code", params[0]);
                request1.addProperty("G_Code", params[1]);
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetGrowerByVillCodeData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetGrowerByVillCodeDataResult").toString();
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
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    growerModelList=new ArrayList<>();
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
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
                    StaffGrowerPlotListAdapter stockSummeryAdapter =new StaffGrowerPlotListAdapter(context,growerModelList);
                    recyclerView.setAdapter(stockSummeryAdapter);

                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
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
