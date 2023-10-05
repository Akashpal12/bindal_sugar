package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import in.co.vibrant.bindalsugar.adapter.StaffGrowerActivityAdapter;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.model.PotatoTodayActivityModal;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class RaActivityDetails extends AppCompatActivity  {

    DBHelper dbh;
    Context context;
    RelativeLayout caneLayout,potatoLayout;
    List<PotatoTodayActivityModal> potatoTodayActivityModalList;
    List<GrowerActivityDetailsModel> growerActivityDetailsModelList;
    Spinner village,grower;
    List<UserDetailsModel> userDetailsModels;
    CardView caneActivityCard,potatoActivityCard;
    String villCode="",growerCode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_activities_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaActivityDetails.this;
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
        caneActivityCard=findViewById(R.id.caneActivityCard);
        potatoActivityCard=findViewById(R.id.potatoActivityCard);
        village=findViewById(R.id.village);
        grower=findViewById(R.id.grower);
        dbh=new DBHelper(context);
        userDetailsModels=dbh.getUserDetailsModel();

        new GetVillageData().execute();

        ArrayList<String> growerdata=new ArrayList<String>();
        growerdata.add("All Grower");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);


        caneLayout=findViewById(R.id.line3);
        potatoLayout=findViewById(R.id.line2);
        potatoTodayActivityModalList=new ArrayList<>();
        PotatoTodayActivityModal factoryModel=new PotatoTodayActivityModal();
        for(int i=0;i<15;i++)
        {
            factoryModel.setFactoryCode("");
            factoryModel.setFactoryName("");
            potatoTodayActivityModalList.add(factoryModel);
        }
        growerActivityDetailsModelList=new ArrayList<>();
        /*RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        PotatoActivityTrackerAdapter stockSummeryAdapter =new PotatoActivityTrackerAdapter(context,potatoTodayActivityModalList);
        recyclerView.setAdapter(stockSummeryAdapter);*/
        caneLayout.setVisibility(View.VISIBLE);
        potatoLayout.setVisibility(View.GONE);
    }

    /*public void getVillage()
    {
        List<VillageModal> villageSurveyModelList1=dbh.getVillageModal("");
        VillageModal staffTargetModal1=new VillageModal();
        staffTargetModal1.setCode("");
        staffTargetModal1.setName("Select Village");
        villageSurveyModelList.add(staffTargetModal1);
        ArrayList<String> data=new ArrayList<String>();
        data.add("All Village");
        for(int i=0;i<villageSurveyModelList1.size();i++)
        {
            VillageModal staffTargetModal=new VillageModal();
            staffTargetModal.setCode(villageSurveyModelList1.get(i).getCode());
            staffTargetModal.setName(villageSurveyModelList1.get(i).getName());
            data.add(villageSurveyModelList1.get(i).getCode()+" - "+villageSurveyModelList1.get(i).getName());
            villageSurveyModelList.add(staffTargetModal);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getGrower(villageSurveyModelList.get(village.getSelectedItemPosition()).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/

    /*public void getGrower(String villCode)
    {
        List<GrowerModel> growerModelList1=dbh.getGrowerModel(villCode,"");
        if(growerSurveyModelList.size()>0)
            growerSurveyModelList.clear();
        GrowerModel growerSurveyModel1 =new GrowerModel();
        growerSurveyModel1.setVillageCode("");
        growerSurveyModel1.setGrowerCode("");
        growerSurveyModel1.setGrowerName("All Grower");
        growerSurveyModelList.add(growerSurveyModel1);
        ArrayList<String> data=new ArrayList<String>();
        data.add("All Grower");
        for(int i=0;i<growerModelList1.size();i++)
        {
            GrowerModel growerSurveyModel =new GrowerModel();
            growerSurveyModel.setVillageCode(growerModelList1.get(i).getVillageCode());
            growerSurveyModel.setGrowerCode(growerModelList1.get(i).getGrowerCode());
            growerSurveyModel.setGrowerName(growerModelList1.get(i).getGrowerName());
            data.add(growerModelList1.get(i).getGrowerCode()+" - "+growerModelList1.get(i).getGrowerName());
            growerSurveyModelList.add(growerSurveyModel);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        grower.setAdapter(adaptersupply);
        grower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new getPenddingActivityVillageWise().execute(villageSurveyModelList.get(village.getSelectedItemPosition()).getCode(),
                        growerSurveyModelList.get(grower.getSelectedItemPosition()).getGrowerCode(),"C");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }*/

    public void openCaneActivities(View v)
    {
        caneActivityCard.setCardBackgroundColor(Color.parseColor("#FFC107"));
        potatoActivityCard.setCardBackgroundColor(Color.parseColor("#1D8C07"));
        new getPenddingActivityVillageWise().execute(villCode,growerCode,"C");
    }

    public void openPotatoActivities(View v)
    {
        potatoActivityCard.setCardBackgroundColor(Color.parseColor("#FFC107"));
        caneActivityCard.setCardBackgroundColor(Color.parseColor("#1D8C07"));
        new getPenddingActivityVillageWise().execute(villCode,growerCode,"P");

    }



    private class getPenddingActivityVillageWise extends AsyncTask<String, Integer, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_AllActivityVillageWiseDetails);
                request1.addProperty("IMEINO", imei);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("VILLAGECODE", params[0]);
                request1.addProperty("G_Code", params[1]);
                request1.addProperty("type", params[2]);
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_AllActivityVillageWiseDetails, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("AllActivityVillageWiseDetailsResult").toString();
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
                if(growerActivityDetailsModelList.size()>0)
                    growerActivityDetailsModelList.clear();
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
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
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list_cane);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffGrowerActivityAdapter stockSummeryAdapter =new StaffGrowerActivityAdapter(context,growerActivityDetailsModelList);
                recyclerView.setAdapter(stockSummeryAdapter);
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


    private class GetVillageData extends AsyncTask<String, Void, Void> {
        String message;
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

                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetVillageList);
                request1.addProperty("factory", userDetailsModels.get(0).getDivision());
                request1.addProperty("Supvcode", "");
                request1.addProperty("Season", 2022 - 2023);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetVillageList, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetVillageListResult").toString();
                }

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
                if (dialog.isShowing())
                    dialog.dismiss();

                JSONArray jsonArray = new JSONArray(message);
                ArrayList<String> data1 = new ArrayList<String>();
                data1.add(" - Select Village - ");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    data1.add(jsonObject.getString("VCODE")+" - "+jsonObject.getString("VNAME"));
                }
                ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                        R.layout.list_item, data1);
                village.setAdapter(adaptersupply);
                village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            villCode="";
                            //getGrower("");
                        } else {
                            String[] vill=village.getSelectedItem().toString().split(" - ");
                            villCode=vill[0].trim();
                            new GetGrowerData().execute(villCode);
                            //getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(context, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }


    private class GetGrowerData extends AsyncTask<String, Void, Void> {
        String message;
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


                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetGrowerData);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("V_Code", params[0]);
                request1.addProperty("G_Code", "");
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Season", getString(R.string.season));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetGrowerData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetGrowerDataResult").toString();
                }

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
                if (dialog.isShowing())
                    dialog.dismiss();

                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    ArrayList<String> data1 = new ArrayList<String>();
                    data1.add(" - Select Grower - ");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        data1.add(jsonObject1.getString("G_NO")+" - "+jsonObject1.getString("G_NAME"));
                    }
                    ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                            R.layout.list_item, data1);
                    grower.setAdapter(adaptersupply);
                    grower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                growerCode="";
                                //getGrower("");
                            } else {
                                String[] grow=grower.getSelectedItem().toString().split(" - ");
                                growerCode=grow[0].trim();
                                new getPenddingActivityVillageWise().execute(villCode,growerCode,"C");
                                //getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else{

                }

            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(context, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

}
