package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ControlModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.MaterialMasterModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffDownloadMasterData extends AppCompatActivity {

    String TAG="StaffDownloadMasterData";

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    String VillageCode="";

    androidx.appcompat.app.AlertDialog dialog;
    TextView download_control_data,download_village_data,
            download_master_data,download_farmer_data,download_dropdown_data,download_survey_data;
    int tempInc=0;
    Context context; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_master);
        context=StaffDownloadMasterData.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        userDetailsModels=new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_IMPORT_MASTER_DATA));
        toolbar.setTitle(getString(R.string.MENU_IMPORT_MASTER_DATA));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context,StaffMainActivity.class);
               startActivity(intent);
            }
        });
      download_dropdown_data=findViewById(R.id.download_dropdown_data);
        download_survey_data=findViewById(R.id.download_survey_data);

        download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");
        download_survey_data.setText(dbh.getPlotSurveyModel("").size()+" Data");
    }

    public void downloadResetDatabase(View v)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_reset_data_master, null);
        dialogbilder.setView(mView);
        final AlertDialog alertDialog = dialogbilder.create();
        final RelativeLayout reset_all_data=mView.findViewById(R.id.reset_all_data);
        final RelativeLayout reset_survey=mView.findViewById(R.id.reset_survey);
        final RelativeLayout reset_planting=mView.findViewById(R.id.reset_planting);
        final RelativeLayout reset_indenting=mView.findViewById(R.id.reset_indenting);

        reset_all_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
                    dialogbilder.setView(mView);
                    final EditText password=mView.findViewById(R.id.password);

                    final AlertDialog dialogPopup = dialogbilder.create();
                    dialogPopup.show();
                    dialogPopup.setCancelable(true);
                    dialogPopup.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(password.getText().toString().length()==0)
                                {
                                    new AlertDialogManager().RedDialog(context,"Please enter password");
                                }
                                else
                                {
                                    if(password.getText().toString().equalsIgnoreCase("all"))
                                    {
                                        dbh.truncatePlotSurveyModel();
                                        dbh.truncateFarmerShareSurveyModel();
                                        dbh.truncatePlanting();
                                        dbh.truncatePlantingEquipmentModel();
                                        dbh.truncatePlantingItemModel();
                                        dbh.truncatePlantingModel();
                                        dbh.truncateIndentModel();
                                        new AlertDialogManager().GreenDialog(context,"Data successfully reset");
                                        alertDialog.dismiss();
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Please enter valid password");
                                    }

                                }
                            }
                            catch(Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                            dialogPopup.dismiss();
                        }
                    });
                }
                catch(SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });

        reset_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
                    dialogbilder.setView(mView);
                    final EditText password=mView.findViewById(R.id.password);

                    final AlertDialog dialogPopup = dialogbilder.create();
                    dialogPopup.show();
                    dialogPopup.setCancelable(true);
                    dialogPopup.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(password.getText().toString().length()==0)
                                {
                                    new AlertDialogManager().RedDialog(context,"Please enter password");
                                }
                                else
                                {
                                    if(password.getText().toString().equalsIgnoreCase("survey"))
                                    {
                                        dbh.truncatePlotSurveyModel();
                                        dbh.truncateFarmerShareSurveyModel();
                                        new AlertDialogManager().GreenDialog(context,"Data successfully reset");
                                        alertDialog.dismiss();
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Please enter valid password");
                                    }

                                }
                            }
                            catch(Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                            dialogPopup.dismiss();
                        }
                    });
                }
                catch(SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });

        reset_planting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
                    dialogbilder.setView(mView);
                    final EditText password=mView.findViewById(R.id.password);

                    final AlertDialog dialogPopup = dialogbilder.create();
                    dialogPopup.show();
                    dialogPopup.setCancelable(true);
                    dialogPopup.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(password.getText().toString().length()==0)
                                {
                                    new AlertDialogManager().RedDialog(context,"Please enter password");
                                }
                                else
                                {
                                    if(password.getText().toString().equalsIgnoreCase("planting"))
                                    {
                                        dbh.truncatePlanting();
                                        dbh.truncatePlantingEquipmentModel();
                                        dbh.truncatePlantingItemModel();
                                        dbh.truncatePlantingModel();
                                        new AlertDialogManager().GreenDialog(context,"Data successfully reset");
                                        alertDialog.dismiss();
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Please enter valid password");
                                    }

                                }
                            }
                            catch(Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                            dialogPopup.dismiss();
                        }
                    });
                }
                catch(SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });

        reset_indenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
                    dialogbilder.setView(mView);
                    final EditText password=mView.findViewById(R.id.password);

                    final AlertDialog dialogPopup = dialogbilder.create();
                    dialogPopup.show();
                    dialogPopup.setCancelable(true);
                    dialogPopup.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if(password.getText().toString().length()==0)
                                {
                                    new AlertDialogManager().RedDialog(context,"Please enter password");
                                }
                                else
                                {
                                    if(password.getText().toString().equalsIgnoreCase("indenting"))
                                    {
                                        dbh.truncateIndentModel();
                                        new AlertDialogManager().GreenDialog(context,"Data successfully reset");
                                        alertDialog.dismiss();
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Please enter valid password");
                                    }

                                }
                            }
                            catch(Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                            dialogPopup.dismiss();
                        }
                    });
                }
                catch(SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });

        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        ImageView close=mView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void downloadVillageData(View v)
    {
        new GetVillageData().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode());
    }

    public void openDropDownMasterData(View v)
    {
        Intent intent=new Intent(context, CaneDownloadMaster.class);
        startActivity(intent);
    }

    public void downloadMasterData(View v)
    {
        new GetDownloadMaster().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode());
    }

    public void downloadDropDownMasterData(View v)
    {
        Intent intent=new Intent(context, CaneDownloadMaster.class);
        startActivity(intent);


    }

    public void OpendownloadSurveyMaster(View v) {

        Intent intent = new Intent(context, StaffSurveyDownloadMaster.class);
        startActivity(intent);
    }

    public void downloadDropDownMasterDataCd(Context ctx,DBHelper dbhelper,String division,String code)
    {
        context=ctx;
        dbh=dbhelper;
        db = dbh.getWritableDatabase();
        new DownloadMaster().execute(division,code);
    }

    public void downloadCaneSurveyData(View v)
    {

    }

    public void downloadPlantingData(View v)
    {
        new GetDownloadPlanting().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode());
    }

    public void downloadAgrimaster(View V)
    {
        Intent intent = new Intent(context,StaffAgriInputMasterData.class);
        startActivity(intent);
    }

    public void downloadFarmerData(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder dialogbilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_download_farmer, null);
        dialogbilder.setView(mView);
        final Spinner village=mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        List<VillageSurveyModel> villageSurveyModelList =new ArrayList<>();
        villageSurveyModelList =dbh.getVillageModel("");
        ArrayList<String> data=new ArrayList<String>();
        for(int i = 0; i< villageSurveyModelList.size(); i++)
        {
            data.add(villageSurveyModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageSurveyModelList = villageSurveyModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    VillageCode=""+ finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode();
                    new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode());
                }
                catch(Exception e)
                {

                }
            }
        });
    }

    private class GetVillageData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadVillage);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("Factory", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadVillage, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadVillageResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);
                    JSONArray jsonArray=new JSONArray(message);
                    if(jsonArray.length()>0)
                    {
                        dbh.deleteVillageModel();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            VillageSurveyModel villageSurveyModel =new VillageSurveyModel();
                            villageSurveyModel.setVillageCode(jsonObject.getString("VILLCD").replace(".0",""));
                            villageSurveyModel.setVillageName(jsonObject.getString("VILLNM").replace(".0",""));
                            villageSurveyModel.setCenterName(jsonObject.getString("CENTNM").replace(".0",""));
                            villageSurveyModel.setPlotSrNo(jsonObject.getString("PLOTSRNO").replace(".0",""));
                            villageSurveyModel.setPlotSrNo1(jsonObject.getString("V_PLOTSRNO1").replace(".0",""));
                            villageSurveyModel.setPlotSrNo2(jsonObject.getString("V_PLOTSRNO2").replace(".0",""));
                            villageSurveyModel.setStopSurvey(jsonObject.getString("STOPSURVEY").replace(".0",""));
                            villageSurveyModel.setTransSurvey(jsonObject.getString("TRANSNO").replace(".0",""));
                            dbh.insertVillageModel(villageSurveyModel);
                        }

                        download_village_data.setText(dbh.getVillageModel("").size()+" Village");
                        AlertPopUp(dbh.getVillageModel("").size()+" successfully imported");
                    }
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private class GetDownloadMaster extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadMaster);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("Factory", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadMaster, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadMasterResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);
                    JSONArray jsonArray=new JSONArray(message);
                    if(jsonArray.length()>0)
                    {
                        dbh.deleteMasterModel();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            MasterCaneSurveyModel masterCaneSurveyModel =new MasterCaneSurveyModel();
                            masterCaneSurveyModel.setMstCode(jsonObject.getString("MSTCD").replace(".0",""));
                            masterCaneSurveyModel.setCode(jsonObject.getString("CODE").replace(".0",""));
                            masterCaneSurveyModel.setName(jsonObject.getString("NAME"));
                            dbh.insertMasterModel(masterCaneSurveyModel);
                        }

                        download_master_data.setText(dbh.getMasterModel("").size()+" Master");
                        AlertPopUp(dbh.getMasterModel("").size()+" successfully imported");
                    }
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private class GetDownloadFarmer extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadFarmer);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("Factory", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("Village", VillageCode);
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadFarmer, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadFarmerResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);
                    JSONArray jsonArray=new JSONArray(message);
                    if(jsonArray.length()>0)
                    {
                        List<FarmerModel> a=dbh.getFarmerWithVillageModel(VillageCode,"");
                        if(a.size()>0)
                        {
                            dbh.deleteFarmerModel(VillageCode);
                        }
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            FarmerModel farmerModel=new FarmerModel();
                            farmerModel.setFarmerCode(jsonObject.getString("FARMCD").replace(".0",""));
                            farmerModel.setVillageCode(jsonObject.getString("VILLCD").replace(".0",""));
                            farmerModel.setFarmerName(jsonObject.getString("FARMNM").replace(".0",""));
                            farmerModel.setFatherName(jsonObject.getString("FATHNM").replace(".0",""));
                            farmerModel.setUniqueCode(jsonObject.getString("UNIQCD").replace(".0",""));
                            farmerModel.setDes(jsonObject.getString("DES").replace(".0",""));
                            farmerModel.setMobile(jsonObject.getString("MOBILENO").replace(".0",""));
                            farmerModel.setCultArea(jsonObject.getString("CULTAREA").replace(".0",""));
                            dbh.insertFarmerModel(farmerModel);
                        }
                        download_farmer_data.setText(dbh.getFarmerModel("").size()+" Farmer");
                        AlertPopUp(dbh.getFarmerModel("").size()+" successfully imported");
                    }
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    public class DownloadMaster extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*/
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/GETMASTER";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("Factory",params[0]));
                entity.add(new BasicNameValuePair("USERID",params[1]));
                entity.add(new BasicNameValuePair("imei",getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);


                int totalData=0;
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    db.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MaterialMasterModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MasterSubDropDown.TABLE_NAME);
                    db.execSQL(MasterDropDown.CREATE_TABLE);
                    db.execSQL(VillageModal.CREATE_TABLE);
                    db.execSQL(GrowerModel.CREATE_TABLE);
                    db.execSQL(MaterialMasterModel.CREATE_TABLE);
                    db.execSQL(ControlModel.CREATE_TABLE);
                    db.execSQL(MasterSubDropDown.CREATE_TABLE);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    final JSONArray IRRIGATIONLIST=jsonObject.getJSONArray("IRRIGATIONLIST");
                    final JSONArray SUPPLYMODELIST=jsonObject.getJSONArray("SUPPLYMODELIST");
                    final JSONArray HARVESTINGLIST=jsonObject.getJSONArray("HARVESTINGLIST");
                    final JSONArray EQUIMENTLIST=jsonObject.getJSONArray("EQUIMENTLIST");
                    final JSONArray LANDTYPELIST=jsonObject.getJSONArray("LANDTYPELIST");
                    final JSONArray SEEDTYPELIST=jsonObject.getJSONArray("SEEDTYPELIST");
                    final JSONArray BASALDOSELIST=jsonObject.getJSONArray("BASALDOSELIST");
                    final JSONArray SEEDTREATMENTLIST=jsonObject.getJSONArray("SEEDTREATMENTLIST");
                    final JSONArray METHODLIST=jsonObject.getJSONArray("METHODLIST");
                    final JSONArray SPRAYITEMLIST=jsonObject.getJSONArray("SPRAYITEMLIST");
                    final JSONArray SPRAYTYPELIST=jsonObject.getJSONArray("SPRAYTYPELIST");
                    final JSONArray PLOUGHINGTYPELIST=jsonObject.getJSONArray("PLOUGHINGTYPELIST");
                    final JSONArray VARIETYLIST=jsonObject.getJSONArray("VARIETYLIST");
                    final JSONArray PLANTINGTYPELIST=jsonObject.getJSONArray("PLANTINGTYPELIST");
                    final JSONArray PLANTATIONLIST=jsonObject.getJSONArray("PLANTATIONLIST");
                    final JSONArray CROPLIST=jsonObject.getJSONArray("CROPLIST");
                    final JSONArray GROWERDATALIST=jsonObject.getJSONArray("GROWERDATALIST");
                    final JSONArray VILLAGEDATALIST=jsonObject.getJSONArray("VILLAGEDATALIST");
                    final JSONArray FILDTRUEFALSELIST=jsonObject.getJSONArray("FILDTRUEFALSELIST");
                    final JSONArray SEEDTTYPELIST=jsonObject.getJSONArray("SEEDTTYPELIST");
                    final JSONArray SEEDSETTYPELIST=jsonObject.getJSONArray("SEEDSETTYPELIST");
                    final JSONArray SOILTREATMENTLIST=jsonObject.getJSONArray("SOILTREATMENTLIST");
                    final JSONArray FILDPREPRATIONLIST=jsonObject.getJSONArray("FILDPREPRATIONLIST");
                    final JSONArray ROWTOROWLIST=jsonObject.getJSONArray("ROWTOROWLIST");
                    final JSONArray TARGETTYPELIST=jsonObject.getJSONArray("TARGETTYPE");
                    final JSONArray PLOTACTIVITYMST=jsonObject.getJSONArray("PLOTACTIVITYMST");
                    final JSONArray PLOTACTIVITYMETHODMST=jsonObject.getJSONArray("PLOTACTIVITYMETHODMST");
                    final JSONArray SUPERVOISERLIST=jsonObject.getJSONArray("SUPERVOISER");
                    final JSONArray AGROITEMS=jsonObject.getJSONArray("AGROITEMS");
                    final JSONArray MEETINGTYPE = jsonObject.getJSONArray("MEETINGTYPE");
                    final JSONArray CANETYPE = jsonObject.getJSONArray("CANETYPE");
                    final JSONArray CATEGORY = jsonObject.getJSONArray("CATEGORY");
                    final JSONArray CANECATEGORY = jsonObject.getJSONArray("CANECATEGORY");

                    totalData +=IRRIGATIONLIST.length();
                    totalData +=SUPPLYMODELIST.length();
                    totalData +=HARVESTINGLIST.length();
                    totalData +=LANDTYPELIST.length();
                    totalData +=SEEDTYPELIST.length();
                    totalData +=BASALDOSELIST.length();
                    totalData +=SEEDTREATMENTLIST.length();
                    totalData +=METHODLIST.length();
                    totalData +=SPRAYITEMLIST.length();
                    totalData +=SPRAYTYPELIST.length();
                    totalData +=PLOUGHINGTYPELIST.length();
                    totalData +=VARIETYLIST.length();
                    totalData +=PLANTINGTYPELIST.length();
                    totalData +=PLANTATIONLIST.length();
                    totalData +=CROPLIST.length();
                    totalData +=GROWERDATALIST.length();
                    totalData +=VILLAGEDATALIST.length();
                    totalData +=FILDTRUEFALSELIST.length();
                    totalData +=SEEDTTYPELIST.length();
                    totalData +=SEEDSETTYPELIST.length();
                    totalData +=SOILTREATMENTLIST.length();
                    totalData +=FILDPREPRATIONLIST.length();
                    totalData +=ROWTOROWLIST.length();
                    totalData +=TARGETTYPELIST.length();
                    totalData +=PLOTACTIVITYMST.length();
                    totalData +=PLOTACTIVITYMETHODMST.length();
                    totalData +=SUPERVOISERLIST.length();
                    totalData +=AGROITEMS.length();
                    totalData += CANETYPE.length();
                    totalData += CATEGORY.length();
                    totalData += MEETINGTYPE.length();
                    totalData += CANECATEGORY.length();

                    /*
                    1-IRRIGATIONLIST
                    2-SUPPLYMODELIST
                    3-HARVESTINGLIST
                    4-EQUIMENTLIST
                    5-LANDTYPELIST
                    6-SEEDTYPELIST
                    7-BASALDOSELIST
                    8-METHODLIST
                    9-SPRAYITEMLIST
                    10-SPRAYTYPELIST
                    11-PLOUGHINGTYPELIST
                    12-VARIETYLIST
                    13-PLANTINGTYPELIST
                    14-PLANTATIONLIST
                    15-CROPLIST
                    16-SEEDTREATMENTLIST
                    17-SEEDTTYPELIST
                    18-SEEDSETTYPELIST
                    19-SOILTREATMENTLIST
                    20-FILDPREPRATIONLIST
                    21-ROWTOROWLIST
                    22-TARGETTYPELIST
                    23-SUPERVOISERLIST
                    24-PLOTACTIVITYMST
                    */

                    dialog.setMax(totalData);
                    dialog.setProgress(0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    if(IRRIGATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating irrigation data...");
                            }
                        });

                        for (int i=0;i<IRRIGATIONLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = IRRIGATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("1");
                            dbh.insertMasterData(masterDropDown);

                        }
                    }

                    if(SUPPLYMODELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supply mode data...");

                            }
                        });

                        for (int i=0;i<SUPPLYMODELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object=SUPPLYMODELIST.getJSONObject(i);
                            MasterDropDown masterDropDown=new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("2");
                            dbh.insertMasterData(masterDropDown);
                        }


                    }

                    if(HARVESTINGLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating harvesting data...");

                            }
                        });

                        for (int i=0;i<HARVESTINGLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = HARVESTINGLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("3");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(EQUIMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating equipment data...");

                            }
                        });

                        for (int i=0;i<EQUIMENTLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = EQUIMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("4");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(LANDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating land type data...");
                            }
                        });

                        for (int i=0;i<LANDTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = LANDTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("5");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i=0;i<SEEDTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("6");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(BASALDOSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating basaldose data...");
                            }
                        });

                        for (int i=0;i<BASALDOSELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = BASALDOSELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("7");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(METHODLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating method data...");
                            }
                        });

                        for (int i=0;i<METHODLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = METHODLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("8");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SPRAYITEMLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray item data...");
                            }
                        });

                        for (int i=0;i<SPRAYITEMLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SPRAYITEMLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("9");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SPRAYTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray type data...");
                            }
                        });

                        for (int i=0;i<SPRAYTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SPRAYTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("10");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLOUGHINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating ploughing data...");
                            }
                        });

                        for (int i=0;i<PLOUGHINGTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOUGHINGTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("11");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(VARIETYLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating variety list data...");
                            }
                        });

                        for (int i=0;i<VARIETYLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = VARIETYLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("12");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLANTINGTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating planting type data...");
                            }
                        });

                        for (int i=0;i<PLANTINGTYPELIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLANTINGTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("13");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLANTATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plantation data...");
                            }
                        });

                        for (int i=0;i<PLANTATIONLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLANTATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("14");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(CROPLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating crop list data...");
                            }
                        });

                        for (int i=0;i<CROPLIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = CROPLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("15");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(VILLAGEDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating village data...");
                            }
                        });

                        for (int i=0;i<VILLAGEDATALIST.length();i++)
                        {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = VILLAGEDATALIST.getJSONObject(i);
                            VillageModal villageModal = new VillageModal();
                            villageModal.setCode(object.getString("VCODE"));
                            villageModal.setName(object.getString("VNAME"));
                            villageModal.setTarget(object.getInt("TARGET"));//0 not set 1 set
                            villageModal.setMaxIndent(object.getString("VINDSERIALNO"));
                            villageModal.setMaxPlant(object.getString("VPLNSERIALNO"));
                            dbh.insertVillageModal(villageModal);
                        }
                    }

                    if(GROWERDATALIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating grower data...");
                            }
                        });

                        for (int i=0;i<GROWERDATALIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = GROWERDATALIST.getJSONObject(i);
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setVillageCode(object.getString("VCODE"));
                            growerModel.setGrowerCode(object.getString("GCODE"));
                            growerModel.setGrowerName(object.getString("GNAME"));
                            growerModel.setGrowerFather(object.getString("FATHER"));
                            dbh.insertGrowerModel(growerModel);
                        }
                    }

                    if(FILDTRUEFALSELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating control data...");
                            }
                        });

                        for (int i=0;i<FILDTRUEFALSELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = FILDTRUEFALSELIST.getJSONObject(i);
                            ControlModel controlSurveyModel = new ControlModel();
                            controlSurveyModel.setName(object.getString("Fild"));
                            controlSurveyModel.setValue(object.getString("Value"));
                            controlSurveyModel.setFormName(object.getString("FormName"));
                            dbh.insertControlModel(controlSurveyModel);
                        }
                    }

                    if(SEEDTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed treatment data...");
                            }
                        });

                        for (int i=0;i<SEEDTREATMENTLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTREATMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("16");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDTTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i=0;i<SEEDTTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDTTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("17");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SEEDSETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed set type data...");
                            }
                        });

                        for (int i=0;i<SEEDSETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SEEDSETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("18");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(SOILTREATMENTLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating soil treatment data...");
                            }
                        });

                        for (int i=0;i<SOILTREATMENTLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SOILTREATMENTLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("19");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(FILDPREPRATIONLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating field prepration data...");
                            }
                        });

                        for (int i=0;i<FILDPREPRATIONLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = FILDPREPRATIONLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("20");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(ROWTOROWLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating row to row data...");
                            }
                        });

                        for (int i=0;i<ROWTOROWLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = ROWTOROWLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("21");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(TARGETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i=0;i<TARGETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = TARGETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("22");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }


                    if(SUPERVOISERLIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supervisor data...");
                            }
                        });

                        for (int i=0;i<SUPERVOISERLIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SUPERVOISERLIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("23");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(TARGETTYPELIST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i=0;i<TARGETTYPELIST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = TARGETTYPELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("24");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }

                    if(PLOTACTIVITYMST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity data...");
                            }
                        });

                        for (int i=0;i<PLOTACTIVITYMST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOTACTIVITYMST.getJSONObject(i);
                            MasterSubDropDown masterDropDown = new MasterSubDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setMasterCode(object.getString("PRBCODE"));
                            masterDropDown.setType("25");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }
                    }

                    if(AGROITEMS.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating Agro Items data...");
                            }
                        });

                        for (int i=0;i<AGROITEMS.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = AGROITEMS.getJSONObject(i);
                            MasterSubDropDown masterDropDown = new MasterSubDropDown();
                            masterDropDown.setCode(object.getString("ITEMCODE"));
                            masterDropDown.setName(object.getString("ITEMNAME"));
                            masterDropDown.setMasterCode(object.getString("UNIT"));
                            masterDropDown.setExtraField(object.getInt("RATE"));
                            masterDropDown.setType("26");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }
                    }

                    if(PLOTACTIVITYMETHODMST.length()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity method data...");
                            }
                        });

                        for (int i=0;i<PLOTACTIVITYMETHODMST.length();i++) {
                            tempInc++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = PLOTACTIVITYMETHODMST.getJSONObject(i);
                            MasterSubDropDown masterDropDown = new MasterSubDropDown();
                            masterDropDown.setCode(object.getString("ATMCODE"));
                            masterDropDown.setName(object.getString("ATMNAME"));
                            masterDropDown.setMasterCode(object.getString("ATMACTCODE"));
                            masterDropDown.setExtraField(object.getInt("ITEMFLG"));
                            masterDropDown.setType("22");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }

                        if (CANETYPE.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("Updating cane type data...");
                                }
                            });

                            for (int i = 0; i < CANETYPE.length(); i++) {
                                tempInc++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempInc);
                                    }
                                });
                                JSONObject object = CANETYPE.getJSONObject(i);
                                MasterDropDown masterDropDown = new MasterDropDown();
                                masterDropDown.setCode(object.getString("Code"));
                                masterDropDown.setName(object.getString("Name"));
                                masterDropDown.setType("25");
                                dbh.insertMasterData(masterDropDown);
                            }
                        }
                        if (CATEGORY.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("Updating category data...");
                                }
                            });

                            for (int i = 0; i < CATEGORY.length(); i++) {
                                tempInc++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempInc);
                                    }
                                });
                                JSONObject object = CATEGORY.getJSONObject(i);
                                MasterDropDown masterDropDown = new MasterDropDown();
                                masterDropDown.setCode(object.getString("Code"));
                                masterDropDown.setName(object.getString("Name"));
                                masterDropDown.setType("26");
                                dbh.insertMasterData(masterDropDown);
                            }
                        }
                        if (MEETINGTYPE.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("Updating meeting type data...");
                                }
                            });

                            for (int i = 0; i < MEETINGTYPE.length(); i++) {
                                tempInc++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempInc);
                                    }
                                });
                                JSONObject object = MEETINGTYPE.getJSONObject(i);
                                MasterDropDown masterDropDown = new MasterDropDown();
                                masterDropDown.setCode(object.getString("MT_CODE"));
                                masterDropDown.setName(object.getString("MT_NAME"));
                                masterDropDown.setType("27");
                                dbh.insertMasterData(masterDropDown);
                            }
                        }
                        if (CANECATEGORY.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("Updating meeting type data...");
                                }
                            });

                            for (int i = 0; i < CANECATEGORY.length(); i++) {
                                tempInc++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempInc);
                                    }
                                });
                                JSONObject object = CANECATEGORY.getJSONObject(i);
                                MasterDropDown masterDropDown = new MasterDropDown();
                                masterDropDown.setCode(object.getString("CODE"));
                                masterDropDown.setName(object.getString("NAME"));
                                masterDropDown.setType("28");
                                dbh.insertMasterData(masterDropDown);
                            }
                        }

                    }



                }

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                dialog.dismiss();
                //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");

                new AlertDialogManager().GreenDialog(context,"Data successfully downloaded");

            }
            catch (Exception e)

            {
                new UpdateMaster().execute();
                new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
            }
        }

    }

    private class UpdateMaster extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*dialog = ProgressDialog.show(Login.this, getString(R.string.app_name),
                    "Please wait while we verify your details", true);*/
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Updating data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/UPDATEMASTERDATETINE";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("USERCODE",userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("IMINO",getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                AlertPopUp("Error:"+e.toString());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                AlertPopUp("Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }




        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                int update=Integer.parseInt(Content);
                dbh.updateMasterDownload(""+update);
                dialog.dismiss();
            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }


    private class GetDownloadPlanting extends AsyncTask<String , Integer, Void> {
        String message;
        int tempData=0;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading data...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadPlanting);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("DIVN", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadPlanting, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadPlantingResult").toString();

                    try{
                        JSONObject jsonObject1=new JSONObject(message);
                        JSONArray jsonArray=jsonObject1.getJSONArray("PLANTINGDATA");
                        int totalData=jsonArray.length();
                        dialog.setMax(totalData);
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        if(jsonArray.length()>0)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting planting data...");
                                }
                            });
                            dbh.truncatePlanting();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                tempData++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                PlantingModel plantingModel=new PlantingModel();
                                plantingModel.setVillage(jsonObject.getString("PL_VILLAGE").replace(".0",""));
                                plantingModel.setGrower(jsonObject.getString("PL_GROW").replace(".0",""));
                                //plantingModel.set(jsonObject.getString("PL_GPLNO").replace(".0",""));
                                plantingModel.setPLOTVillage(jsonObject.getString("PL_PLVILL").replace(".0",""));
                                plantingModel.setPlotSerialNumber(jsonObject.getString("PL_VPLOTNO").replace(".0",""));
                                plantingModel.setLAT1(jsonObject.getString("PL_PLAT1"));
                                plantingModel.setLON1(jsonObject.getString("PL_PLON1"));
                                plantingModel.setLAT2(jsonObject.getString("PL_PLAT2"));
                                plantingModel.setLON2(jsonObject.getString("PL_PLON2"));
                                plantingModel.setLAT3(jsonObject.getString("PL_PLAT3"));
                                plantingModel.setLON3(jsonObject.getString("PL_PLON3"));
                                plantingModel.setLAT4(jsonObject.getString("PL_PLAT4"));
                                plantingModel.setLON4(jsonObject.getString("PL_PLON4"));
                                plantingModel.setDim1(jsonObject.getString("PL_DIM_1"));
                                plantingModel.setDim2(jsonObject.getString("PL_DIM_2"));
                                plantingModel.setDim3(jsonObject.getString("PL_DIM_3"));
                                plantingModel.setDim4(jsonObject.getString("PL_DIM_4"));
                                plantingModel.setArea(jsonObject.getString("PL_AREA"));
                                plantingModel.setSeedSource(jsonObject.getString("PL_SEEDSOURCE").replace(".0",""));
                                plantingModel.setMethod(jsonObject.getString("PL_SMETHOD").replace(".0",""));
                                plantingModel.setSmMethod(jsonObject.getString("PL_SMETHOD").replace(".0",""));
                                plantingModel.setCurrentDate(jsonObject.getString("PL_PDATE").replace("T"," "));
                                plantingModel.setSuperviserCode(jsonObject.getString("PL_PLSUP").replace(".0",""));
                                plantingModel.setManualArea(jsonObject.getString("PL_PMAREA"));
                                plantingModel.setMobileNumber(jsonObject.getString("PL_MOBNO").replace(".0",""));
                                plantingModel.setmDate(jsonObject.getString("PL_MDATE").replace("T"," "));
                                plantingModel.setVARIETY(jsonObject.getString("PL_VARIETY").replace(".0",""));
                                plantingModel.setPlantingType(jsonObject.getString("PL_PLATYPE").replace(".0",""));
                                plantingModel.setSeedType(jsonObject.getString("PL_SEEDTYPE").replace(".0",""));
                                plantingModel.setSeedSetType(jsonObject.getString("PL_SEEDSET").replace(".0",""));
                                plantingModel.setSoilTreatment(jsonObject.getString("PL_SOILTR").replace(".0",""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDPLVILL").replace(".0",""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDVPLOTNO").replace(".0",""));
                                plantingModel.setIsIdeal(jsonObject.getString("PL_ISIDEAL").replace(".0",""));
                                plantingModel.setIsNursery(jsonObject.getString("PL_ISNARSARI").replace(".0",""));
                                plantingModel.setSeedBagQty(jsonObject.getString("PL_SEEDBAGQTY"));
                                plantingModel.setServerStatus("DONE");
                                plantingModel.setRemark("");

                                dbh.saveDownloadedPlantingModel(plantingModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());

                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);


                    download_survey_data.setText(""+dbh.getPlotFarmerShareModel("","").size());
                    AlertPopUp("Data successfully imported");
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void AlertPopUpWithFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }


}


