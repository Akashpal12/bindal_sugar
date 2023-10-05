package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.FarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyOldModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.GenerateLogFile;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffSurveyDownloadMaster extends AppCompatActivity {
    DBHelper dbh;
    String TAG = "StaffSurveyDownloadMaster";
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    String VillageCode = "";
    Context context;
    AlertDialog alertDialog;
    AlertDialog dialog;
    TextView download_control, download_control_data, download_farmer_data, download_survey,
            download_village, download_village_data, download_dropdown_data, download_survey_data, download_master, download_master_data;
    RelativeLayout download_control_data_rl, download_farmer_data_rl, download_village_data_rl, download_suevey_data_rl,
            download_master_data_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_survey_download_master);
        try {
            context = StaffSurveyDownloadMaster.this;
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            userDetailsModels = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Import Master Data");
            toolbar.setTitle("Import Master Data");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            download_survey_data = findViewById(R.id.download_survey_data);
            download_dropdown_data = findViewById(R.id.download_dropdown_data);


            download_control = findViewById(R.id.download_control);
            download_master = findViewById(R.id.download_master);
            download_village = findViewById(R.id.download_village);
            download_master_data = findViewById(R.id.download_master_data);


            download_control_data_rl = findViewById(R.id.download_control_data_rl);
            download_village_data_rl = findViewById(R.id.download_village_data_rl);
            download_master_data_rl = findViewById(R.id.download_master_data_rl);
            download_farmer_data_rl = findViewById(R.id.download_farmer_data_rl);
            download_suevey_data_rl = findViewById(R.id.download_suevey_data_rl);


            download_control_data = findViewById(R.id.download_control_data);
            download_village_data = findViewById(R.id.download_village_data);
            download_master_data = findViewById(R.id.download_master_data);
            download_farmer_data = findViewById(R.id.download_farmer_data);


            download_village_data_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        new DownloadMasterData().GetVillageData(context);
                        //new DownloadMasterData().downloadVillageData(context,userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            download_control_data_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        new DownloadMasterData().GetControlData(context);
                        //new DownloadMasterData().downloadControlData(context,userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            download_master_data_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        new DownloadMasterData().GetDownloadMaster(context);
                        //new DownloadMasterData().downloadMasterData(context,userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            download_farmer_data_rl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //alertDialog.dismiss();
                    try {
                        new DownloadMasterData().GetDownloadFarmer(context);
                        //new DownloadMasterData().downloadfarmerData(context,userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            setData();
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            ImageView close = findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setData()
    {
        try{
            TextView download_ly_survey_data=findViewById(R.id.download_ly_survey_data);
            TextView download_cy_survey_data=findViewById(R.id.download_cy_survey_data);
            //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");
            download_ly_survey_data.setText(dbh.getPlotSurveyOldModelId("","").size()+" Data");
            download_cy_survey_data.setText(dbh.getPlotSurveyModel("").size()+" Data");
            download_control_data.setText(dbh.getControlSurveyModel("").size() + " Control");
            download_village_data.setText(dbh.getVillageModel("").size() + " Village");
            download_master_data.setText(dbh.getMasterModel("").size() + " Master");
            download_farmer_data.setText(dbh.getFarmerModel("").size() + " Farmer");
        }
        catch(Exception e)
        {
            //new AlertDialogManager().RedDialog(context,"Error : "+e.to);
        }
    }

    ////////-----------Download Survey-------------////////////////////


    public void downloadCaneSurveyData(View v)
    {
        try {
            //new DownloadMasterData().GetDownloadSurvey(StaffDownloadMasterData.this);
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_download_farmer, null);
            dialogbilder.setView(mView);
            final Spinner village=mView.findViewById(R.id.tv_village);
            //warehouseModalList;
            List<VillageSurveyModel> villageSurveyModelList =new ArrayList<>();
            villageSurveyModelList =dbh.getVillageModel("");
            ArrayList<String> data=new ArrayList<String>();
            for(int i = 0; i< villageSurveyModelList.size(); i++)
            {
                data.add(villageSurveyModelList.get(i).getVillageCode()+" - "+villageSurveyModelList.get(i).getVillageName());
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
                        new GetDownloadSurvey().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(),VillageCode);
                        //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode());
                    }
                    catch(Exception e)
                    {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadCurrentSurveyData(View v)
    {
        try {
            CheckVillage : {
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
                                dialogPopup.dismiss();
                                if(password.getText().toString().equalsIgnoreCase("current"))
                                {
                                    List<FarmerShareModel> uploadFarmerShareModel=dbh.getFarmerShareUploadingData();
                                    if(uploadFarmerShareModel.size()>0)
                                    {
                                        List<PlotSurveyModel> uploadPlotSurveyModel=dbh.getPlotSurveyModelById(uploadFarmerShareModel.get(0).getSurveyId());
                                        if(uploadPlotSurveyModel.size()>0)
                                        {
                                            new AlertDialogManager().RedDialog(context,"Sorry you can't update your application because you have pending data to upload on server");
                                        }
                                        else
                                        {
                                            downloadSurveyCurrent();
                                            //new GetDownloadSurvey().execute(factoryModelList.get(0).getFactoryCode(),factoryModelList.get(0).getSuperWiserCode());
                                        }
                                    }
                                    else
                                    {
                                        downloadSurveyCurrent();
                                    }
                                }
                                else
                                {
                                    new AlertDialogManager().RedDialog(context,"Please enter password valid password");
                                }

                            }
                        }
                        catch(Exception e)
                        {

                        }
                        dialogPopup.dismiss();
                    }
                });
            }

        }
        catch(SecurityException e)
        {

        }
    }

    private void downloadSurveyCurrent()
    {
        try{
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_download_farmer, null);
            dialogbilder.setView(mView);
            final Spinner village=mView.findViewById(R.id.tv_village);
            //warehouseModalList;
            List<VillageSurveyModel> villageModelList=new ArrayList<>();
            villageModelList=dbh.getVillageModelFiltered("","0");
            ArrayList<String> data=new ArrayList<String>();
            data.add(" - Select Village - ");
            for(int i=0;i<villageModelList.size();i++)
            {
                data.add(villageModelList.get(i).getVillageCode()+" - "+villageModelList.get(i).getVillageName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, data);
            village.setAdapter(adapter);
            final AlertDialog dialogPopup1 = dialogbilder.create();
            dialogPopup1.show();
            dialogPopup1.setCancelable(true);
            dialogPopup1.setCanceledOnTouchOutside(true);
            Button btn_ok=mView.findViewById(R.id.btn_ok);
            final List<VillageSurveyModel> finalVillageModelList = villageModelList;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(village.getSelectedItemPosition()>0)
                        {
                            dialogPopup1.dismiss();
                            String villageCode=village.getSelectedItem().toString().split(" - ")[0];
                            new GetDownloadCurrentSurvey().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(),villageCode);
                        }
                    }
                    catch(Exception e)
                    {

                    }
                }
            });
        }
        catch (Exception e)
        {

        }
    }

    public void opendownloadPlantingData(View v) {
        new GetDownloadPlanting().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());
    }

    public void openResendAllDataToServer(View v) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(StaffSurveyDownloadMaster.this);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
            dialogbilder.setView(mView);
            final EditText password = mView.findViewById(R.id.password);

            final AlertDialog dialogPopup = dialogbilder.create();
            dialogPopup.show();
            dialogPopup.setCancelable(true);
            dialogPopup.setCanceledOnTouchOutside(true);
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (password.getText().toString().length() == 0) {
                            new AlertDialogManager().RedDialog(StaffSurveyDownloadMaster.this, "Please enter password");
                        } else {
                            if (password.getText().toString().equalsIgnoreCase("dplot")) {
                                dbh.updateAllServerFarmerShareModel();
                                dialogPopup.dismiss();
                            } else {
                                new AlertDialogManager().RedDialog(StaffSurveyDownloadMaster.this, "Please enter valid password");
                            }

                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(StaffSurveyDownloadMaster.this, "Error:" + e.toString());
                    }
                    dialogPopup.dismiss();
                }
            });
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void openSurveyImportData(View v) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
            dialogbilder.setView(mView);
            final EditText password = mView.findViewById(R.id.password);
            alertDialog = dialogbilder.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (password.getText().toString().length() == 0) {
                            new AlertDialogManager().RedDialog(context, "Please enter password");
                        } else {
                            if (password.getText().toString().equalsIgnoreCase("vispl")) {
                                new GenerateLogFile(context).importSurveyData();
                            } else {
                                new AlertDialogManager().RedDialog(context, "Please enter valid password");
                            }
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                    }
                    alertDialog.dismiss();
                }
            });
        } catch (SecurityException e) {

        }
    }

    public void openExportSurveyData(View v) {
        new GenerateLogFile(context).generateSurveyData();
    }

    private class GetDownloadPlanting extends AsyncTask<String, Integer, Void> {
        String message;
        int tempData = 0;
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
                String imei = new GetDeviceImei((context)).GetDeviceImeiNumber();
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

                    try {
                        JSONObject jsonObject1 = new JSONObject(message);
                        JSONArray jsonArray = jsonObject1.getJSONArray("PLANTINGDATA");
                        int totalData = jsonArray.length();
                        dialog.setMax(totalData);
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        if (jsonArray.length() > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting planting data...");
                                }
                            });
                            dbh.truncatePlanting();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tempData++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PlantingModel plantingModel = new PlantingModel();
                                plantingModel.setVillage(jsonObject.getString("PL_VILLAGE").replace(".0", ""));
                                plantingModel.setGrower(jsonObject.getString("PL_GROW").replace(".0", ""));
                                //plantingModel.set(jsonObject.getString("PL_GPLNO").replace(".0",""));
                                plantingModel.setPLOTVillage(jsonObject.getString("PL_PLVILL").replace(".0", ""));
                                plantingModel.setPlotSerialNumber(jsonObject.getString("PL_VPLOTNO").replace(".0", ""));
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
                                plantingModel.setSeedSource(jsonObject.getString("PL_SEEDSOURCE").replace(".0", ""));
                                plantingModel.setMethod(jsonObject.getString("PL_SMETHOD").replace(".0", ""));
                                plantingModel.setSmMethod(jsonObject.getString("PL_SMETHOD").replace(".0", ""));
                                plantingModel.setCurrentDate(jsonObject.getString("PL_PDATE").replace("T", " "));
                                plantingModel.setSuperviserCode(jsonObject.getString("PL_PLSUP").replace(".0", ""));
                                plantingModel.setManualArea(jsonObject.getString("PL_PMAREA"));
                                plantingModel.setMobileNumber(jsonObject.getString("PL_MOBNO").replace(".0", ""));
                                plantingModel.setmDate(jsonObject.getString("PL_MDATE").replace("T", " "));
                                plantingModel.setVARIETY(jsonObject.getString("PL_VARIETY").replace(".0", ""));
                                plantingModel.setPlantingType(jsonObject.getString("PL_PLATYPE").replace(".0", ""));
                                plantingModel.setSeedType(jsonObject.getString("PL_SEEDTYPE").replace(".0", ""));
                                plantingModel.setSeedSetType(jsonObject.getString("PL_SEEDSET").replace(".0", ""));
                                plantingModel.setSoilTreatment(jsonObject.getString("PL_SOILTR").replace(".0", ""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDPLVILL").replace(".0",""));
                                //plantingModel.setPLOTVillage(jsonObject.getString("PL_INDVPLOTNO").replace(".0",""));
                                plantingModel.setIsIdeal(jsonObject.getString("PL_ISIDEAL").replace(".0", ""));
                                plantingModel.setIsNursery(jsonObject.getString("PL_ISNARSARI").replace(".0", ""));
                                plantingModel.setSeedBagQty(jsonObject.getString("PL_SEEDBAGQTY"));
                                plantingModel.setServerStatus("DONE");
                                plantingModel.setRemark("");

                                dbh.saveDownloadedPlantingModel(plantingModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());

                        }
                    } catch (Exception e) {

                    }
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
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
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.d(TAG, message);
                download_survey_data.setText("" + dbh.getPlotSurveyModel("").size());
                new AlertDialogManager().showToast((Activity) context, "Data successfully imported");
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, e.getMessage());
            }

        }

    }

    private class GetDownloadSurvey extends AsyncTask<String , Integer, Void> {
        String message;
        int tempData=0;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("downloading survey data ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadLYSurvey);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("DIVN", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("VillCode", params[2]);
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadLYSurvey, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadLYSurveyResult").toString();

                    try{
                        JSONObject jsonObject1=new JSONObject(message);
                        JSONArray jsonArray=jsonObject1.getJSONArray("DTSURVEY");
                        JSONArray jsonArrayShare=jsonObject1.getJSONArray("DTSURVEYDETAILS");
                        int totalData=jsonArray.length()+jsonArrayShare.length();
                        dialog.setMax(totalData);
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        if(jsonArray.length()>0)
                        {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting survey data...");
                                }
                            });
                            dbh.deleteOldPlotSurveyModelByVillage(params[2]);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                tempData++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                        //test
                                    }
                                });
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                PlotSurveyOldModel plotSurveyModel=new PlotSurveyOldModel();
                                double distance1=(jsonObject.getDouble("EAST_DISTANCE")+jsonObject.getDouble("WEST_DISTANCE"))/2;
                                double distance2=(jsonObject.getDouble("NORTH_DISTANCE")+jsonObject.getDouble("SOUTH_DISTANCE"))/2;
                                double areaM=distance1*distance2;
                                double areah=areaM/10000;
                                plotSurveyModel.setPlotSrNo(jsonObject.getString("PLOT_SR_NUMBER"));
                                plotSurveyModel.setAreaMeter(new DecimalFormat("0.000").format(areaM));
                                plotSurveyModel.setAreaHectare(new DecimalFormat("0.000").format(areah));
                                plotSurveyModel.setKhashraNo(jsonObject.getString("KHASHRA_NO"));
                                plotSurveyModel.setGataNo(jsonObject.getString("GATANO"));
                                plotSurveyModel.setVillageCode(jsonObject.getString("VILLAGECODE"));
                                plotSurveyModel.setMixCrop(jsonObject.getString("MIXCROP"));
                                plotSurveyModel.setInsect(jsonObject.getString("INSECT"));
                                plotSurveyModel.setSeedSource(jsonObject.getString("SEEDSOURCE"));
                                plotSurveyModel.setPlantMethod(jsonObject.getString("PLANTMETHOD"));
                                plotSurveyModel.setCropCondition(jsonObject.getString("CROPCONDITION"));
                                plotSurveyModel.setDisease(jsonObject.getString("DISEASE"));
                                plotSurveyModel.setIrrigation(jsonObject.getString("IRRIGATION"));
                                plotSurveyModel.setSoilType(jsonObject.getString("SOILTYPE"));
                                plotSurveyModel.setLandType(jsonObject.getString("LANDTYPE"));
                                plotSurveyModel.setBorderCrop(jsonObject.getString("BORDERCROP"));
                                plotSurveyModel.setPlotType(jsonObject.getString("PLOTTYPE"));
                                plotSurveyModel.setInterCrop(jsonObject.getString("INTERCROP"));
                                plotSurveyModel.setAadharNumber(jsonObject.getString("AADHARNUMBER"));
                                plotSurveyModel.setPlantDate(jsonObject.getString("PLANTDATE"));
                                plotSurveyModel.setGhashtiNumber(jsonObject.getString("GHASHTINUMBER"));
                                plotSurveyModel.setEastNorthLat(jsonObject.getString("EAST_LAT"));
                                plotSurveyModel.setEastNorthLng(jsonObject.getString("EAST_LNG"));
                                plotSurveyModel.setEastNorthAccuracy(jsonObject.getString("EAST_ACCURACY"));
                                plotSurveyModel.setEastNorthDistance(jsonObject.getString("EAST_DISTANCE"));
                                plotSurveyModel.setWestNorthLat(jsonObject.getString("NORTH_LAT"));
                                plotSurveyModel.setWestNorthLng(jsonObject.getString("NORTH_LNG"));
                                plotSurveyModel.setWestNorthAccuracy(jsonObject.getString("NORTH_ACCURACY"));
                                plotSurveyModel.setWestNorthDistance(jsonObject.getString("NORTH_DISTANCE"));
                                plotSurveyModel.setEastSouthLat(jsonObject.getString("SOUTH_LAT"));
                                plotSurveyModel.setEastSouthLng(jsonObject.getString("SOUTH_LNG"));
                                plotSurveyModel.setEastSouthAccuracy(jsonObject.getString("SOUTH_ACCURACY"));
                                plotSurveyModel.setEastSouthDistance(jsonObject.getString("SOUTH_DISTANCE"));
                                plotSurveyModel.setWestSouthLat(jsonObject.getString("WEST_LAT"));
                                plotSurveyModel.setWestSouthLng(jsonObject.getString("WEST_LNG"));
                                plotSurveyModel.setWestSouthAccuracy(jsonObject.getString("WEST_ACCURACY"));
                                plotSurveyModel.setWestSouthDistance(jsonObject.getString("WEST_DISTANCE"));
                                plotSurveyModel.setVarietyCode(jsonObject.getString("VARIETY_CODE"));
                                plotSurveyModel.setCaneType(jsonObject.getString("CANE_TYPE"));
                                //plotSurveyModel.setOldPlotId("0");
                                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
                                Date dateObj2 = sdfIn.parse(jsonObject.getString("INSERT_DATE").replace("T"," "));
                                String currentDt = sdfOut.format(dateObj2);
                                plotSurveyModel.setInsertDate(currentDt);
                                dbh.insertPlotSurveyOldModel(plotSurveyModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());
                        }
                        if(jsonArrayShare.length()>0)
                        {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("updating survey share percent data...");
                                }
                            });
                            for(int i=0;i<jsonArrayShare.length();i++)
                            {
                                tempData++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });
                                JSONObject jsonObject=jsonArrayShare.getJSONObject(i);
                                List<PlotSurveyOldModel> checkPlotSurvey=dbh.getPlotSurveyOldModelId(jsonObject.getString("GH_PLVILL"),jsonObject.getString("GH_NO"));
                                if(checkPlotSurvey.size()>0)
                                {
                                    FarmerShareOldModel plotSurveyModel=new FarmerShareOldModel();
                                    plotSurveyModel.setSurveyId(checkPlotSurvey.get(0).getColId());
                                    plotSurveyModel.setSrNo(jsonObject.getString("GH_SHARE_NO"));
                                    plotSurveyModel.setVillageCode(jsonObject.getString("VILLAGE_CODE"));
                                    plotSurveyModel.setGrowerCode(jsonObject.getString("GROWER_CODE"));
                                    plotSurveyModel.setGrowerName(jsonObject.getString("GROWER_NAME"));
                                    plotSurveyModel.setGrowerFatherName(jsonObject.getString("GROWER_FATHER_CODE"));
                                    plotSurveyModel.setGrowerAadharNumber(jsonObject.getString("GROWER_AADHAR_NUMBER"));
                                    plotSurveyModel.setShare(jsonObject.getString("SHARE2").replace(".0",""));
                                    plotSurveyModel.setSupCode(""+userDetailsModels.get(0).getCode());
                                    plotSurveyModel.setCurDate(checkPlotSurvey.get(0).getInsertDate());
                                    plotSurveyModel.setServerStatus("OK");
                                    dbh.importFarmerShareOldModel(plotSurveyModel);
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                    }
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
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
            super.onPreExecute();
            if (dialog.isShowing())
                dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);
                    JSONObject jsonObject1=new JSONObject(message);
                    JSONArray jsonArray=jsonObject1.getJSONArray("DTSURVEY");
                    JSONArray jsonArrayShare=jsonObject1.getJSONArray("DTSURVEYDETAILS");
                    int totalData=jsonArray.length()+jsonArrayShare.length();
                    //download_survey_data.setText(""+dbh.getPlotFarmerShareModel("","").size());
                    new AlertDialogManager().GreenDialog(context,"survey data successfully imported");
                    setData();
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,message);
                }
            } else {
                //AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private class GetDownloadCurrentSurvey extends AsyncTask<String , Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        String villageCode="0";
        int temp=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Download current year survey data ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                villageCode=params[2];
                String imei=new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadCYSurvey);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("DIVN", params[0]);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("MachineID", imei);
                request1.addProperty("VillCode", villageCode);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadCYSurvey, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadCYSurveyResult").toString();

                    try{
                        Log.d(TAG, message);
                        JSONObject jsonObject1=new JSONObject(message);
                        JSONArray jsonArray=jsonObject1.getJSONArray("DTSURVEY");
                        JSONArray jsonArrayShare=jsonObject1.getJSONArray("DTSURVEYDETAILS");
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        dialog.setMax(jsonArray.length()+jsonArrayShare.length());
                        if(jsonArray.length()>0)
                        {
                            dbh.deletePlotSurveyModelByVillage(villageCode);
                            //dbh.truncateFarmerShareSurveyModel();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            publishProgress(temp);
                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }
                                });
                                temp++;
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                PlotSurveyModel plotSurveyModel=new PlotSurveyModel();
                                double distance1=(jsonObject.getDouble("EAST_DISTANCE")+jsonObject.getDouble("WEST_DISTANCE"))/2;
                                double distance2=(jsonObject.getDouble("NORTH_DISTANCE")+jsonObject.getDouble("SOUTH_DISTANCE"))/2;
                                double areaM=distance1*distance2;
                                double areah=areaM/10000;
                                plotSurveyModel.setPlotSrNo(jsonObject.getString("PLOT_SR_NUMBER"));
                                plotSurveyModel.setAreaMeter(new DecimalFormat("0.00").format(areaM));
                                plotSurveyModel.setAreaHectare(new DecimalFormat("0.00").format(areah));
                                plotSurveyModel.setKhashraNo(jsonObject.getString("KHASHRA_NO"));
                                plotSurveyModel.setGataNo(jsonObject.getString("GATANO"));
                                plotSurveyModel.setVillageCode(jsonObject.getString("VILLAGECODE"));
                                plotSurveyModel.setMixCrop(jsonObject.getString("MIXCROP"));
                                plotSurveyModel.setInsect(jsonObject.getString("INSECT"));
                                plotSurveyModel.setSeedSource(jsonObject.getString("SEEDSOURCE"));
                                plotSurveyModel.setPlantMethod(jsonObject.getString("PLANTMETHOD"));
                                plotSurveyModel.setCropCondition(jsonObject.getString("CROPCONDITION"));
                                plotSurveyModel.setDisease(jsonObject.getString("DISEASE"));
                                plotSurveyModel.setIrrigation(jsonObject.getString("IRRIGATION"));
                                plotSurveyModel.setSoilType(jsonObject.getString("SOILTYPE"));
                                plotSurveyModel.setLandType(jsonObject.getString("LANDTYPE"));
                                plotSurveyModel.setBorderCrop(jsonObject.getString("BORDERCROP"));
                                plotSurveyModel.setPlotType(jsonObject.getString("PLOTTYPE"));
                                plotSurveyModel.setInterCrop(jsonObject.getString("INTERCROP"));
                                plotSurveyModel.setAadharNumber(jsonObject.getString("AADHARNUMBER"));
                                plotSurveyModel.setPlantDate(jsonObject.getString("PLANTDATE"));
                                plotSurveyModel.setGhashtiNumber(jsonObject.getString("GHASHTINUMBER"));
                                plotSurveyModel.setEastNorthLat(jsonObject.getString("EAST_LAT"));
                                plotSurveyModel.setEastNorthLng(jsonObject.getString("EAST_LNG"));
                                plotSurveyModel.setEastNorthAccuracy(jsonObject.getString("EAST_ACCURACY"));
                                plotSurveyModel.setEastNorthDistance(jsonObject.getString("EAST_DISTANCE"));
                                plotSurveyModel.setWestNorthLat(jsonObject.getString("NORTH_LAT"));
                                plotSurveyModel.setWestNorthLng(jsonObject.getString("NORTH_LNG"));
                                plotSurveyModel.setWestNorthAccuracy(jsonObject.getString("NORTH_ACCURACY"));
                                plotSurveyModel.setWestNorthDistance(jsonObject.getString("NORTH_DISTANCE"));
                                plotSurveyModel.setEastSouthLat(jsonObject.getString("SOUTH_LAT"));
                                plotSurveyModel.setEastSouthLng(jsonObject.getString("SOUTH_LNG"));
                                plotSurveyModel.setEastSouthAccuracy(jsonObject.getString("SOUTH_ACCURACY"));
                                plotSurveyModel.setEastSouthDistance(jsonObject.getString("SOUTH_DISTANCE"));
                                plotSurveyModel.setWestSouthLat(jsonObject.getString("WEST_LAT"));
                                plotSurveyModel.setWestSouthLng(jsonObject.getString("WEST_LNG"));
                                plotSurveyModel.setWestSouthAccuracy(jsonObject.getString("WEST_ACCURACY"));
                                plotSurveyModel.setWestSouthDistance(jsonObject.getString("WEST_DISTANCE"));
                                plotSurveyModel.setVarietyCode(jsonObject.getString("VARIETY_CODE"));
                                plotSurveyModel.setCaneType(jsonObject.getString("CANE_TYPE"));

                                plotSurveyModel.setOldPlotId("0");

                                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat stfOut = new SimpleDateFormat("HH:mm:ss");
                                Date dateObj2 = sdfIn.parse(jsonObject.getString("INSERT_DATE").replace("T"," "));
                                String currentDt = sdfOut.format(dateObj2);
                                String currentTime = stfOut.format(dateObj2);
                                plotSurveyModel.setInsertDate(currentDt);
                                //plotSurveyModel.setInsertTime(currentTime);
                                dbh.insertPlotSurveyModel(plotSurveyModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());

                        }
                        if(jsonArrayShare.length()>0)
                        {
                            for(int i=0;i<jsonArrayShare.length();i++)
                            {
                                JSONObject jsonObject=jsonArrayShare.getJSONObject(i);
                                List<PlotSurveyModel> checkPlotSurvey=dbh.getPlotSurveyId(jsonObject.getString("GH_PLVILL"),jsonObject.getString("GH_NO"));
                                if(checkPlotSurvey.size()>0)
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                publishProgress(temp);
                                            }
                                            catch (Exception e)
                                            {

                                            }
                                        }
                                    });
                                    temp++;
                                    FarmerShareModel plotSurveyModel=new FarmerShareModel();
                                    plotSurveyModel.setSurveyId(checkPlotSurvey.get(0).getColId());
                                    plotSurveyModel.setSrNo(jsonObject.getString("GH_SHARE_NO"));
                                    plotSurveyModel.setVillageCode(jsonObject.getString("VILLAGE_CODE"));
                                    plotSurveyModel.setGrowerCode(jsonObject.getString("GROWER_CODE"));
                                    plotSurveyModel.setGrowerName(jsonObject.getString("GROWER_NAME"));
                                    plotSurveyModel.setGrowerFatherName(jsonObject.getString("GROWER_FATHER_CODE"));
                                    plotSurveyModel.setGrowerAadharNumber(jsonObject.getString("GROWER_AADHAR_NUMBER"));
                                    plotSurveyModel.setShare(jsonObject.getString("SHARE2").replace(".0",""));
                                    plotSurveyModel.setSupCode(""+userDetailsModels.get(0).getCode());
                                    plotSurveyModel.setCurDate(checkPlotSurvey.get(0).getInsertDate());
                                    //plotSurveyModel.setCurDate(checkPlotSurvey.get(0).getInsertDate()+" "+checkPlotSurvey.get(0).getInsertTime());
                                    plotSurveyModel.setServerStatus("OK");
                                    dbh.importFarmerShareModel(plotSurveyModel);
                                }
                            }
                        }
                        /*if(jsonArrayVillage.length()>0)
                        {
                            List<VillageSurveyModel> def=dbh.getDefaultSurveyVillage();
                            //dbh.deleteVillageModel();
                            for(int i=0;i<jsonArrayVillage.length();i++)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            publishProgress(temp);
                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }
                                });
                                temp++;
                                JSONObject jsonObject=jsonArrayVillage.getJSONObject(i);
                                VillageSurveyModel villageModel=new VillageSurveyModel();
                                villageModel.setVillageCode(jsonObject.getString("VILLCD"));
                                villageModel.setVillageName(jsonObject.getString("VILLNM"));
                                villageModel.setCenterName(jsonObject.getString("CENTNM"));
                                villageModel.setPlotSrNo(jsonObject.getString("PLOTSRNO"));
                                villageModel.setPlotSrNo1(jsonObject.getString("v_PlotSrNo1"));
                                villageModel.setPlotSrNo2(jsonObject.getString("v_PlotSrNo2"));
                                villageModel.setStopSurvey(jsonObject.getString("STOPSURVEY"));
                                villageModel.setTransSurvey(jsonObject.getString("TRANSNO"));
                                if(def.size()>0)
                                {
                                    if(def.get(0).getVillageCode().equalsIgnoreCase(villageModel.getVillageCode()))
                                    {
                                        villageModel.setIsDefault("1");
                                    }
                                    else
                                    {
                                        villageModel.setIsDefault("0");
                                    }
                                }
                                else
                                {
                                    villageModel.setIsDefault("0");
                                }
                                List<VillageSurveyModel> checkVillage=dbh.getVillageModel(jsonObject.getString("VILLCD"));
                                if(checkVillage.size()>0)
                                {
                                    dbh.deleteVillageModelByCode(jsonObject.getString("VILLCD"));
                                }
                                dbh.insertVillageModel(villageModel);
                            }
                        }*/
                        //download_survey_data.setText(""+dbh.getPlotFarmerShareModel().size());
                        new AlertDialogManager().GreenDialog(context," Data successfully imported");


                    }
                    catch (JSONException e)
                    {
                        //new AlertDialogManager().GreenDialog(context,message);
                    }
                    catch (Exception e)
                    {
                        //new AlertDialogManager().GreenDialog(context,e.toString());
                    }
                }
            } catch (SQLException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (SecurityException e) {
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            try{
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.d("TAG", message);
                JSONObject jsonObject1=new JSONObject(message);
                JSONArray jsonArray=jsonObject1.getJSONArray("DTSURVEY");
                JSONArray jsonArrayShare=jsonObject1.getJSONArray("DTSURVEYDETAILS");
                new AlertDialogManager().GreenDialog(context,"Data successfully imported");
                setData();
                //DataSetCount();
                //download_control_data.setText(""+dbh.getControlModel("").size());
                //download_village_data.setText(""+dbh.getVillageModel("").size());
                //download_master_data.setText(""+dbh.getMasterModel("").size());
                //download_farmer_data.setText(""+dbh.getFarmerModel("","").size());
                //download_old_survey_data.setText(""+dbh.getIdentifyPlotSurveyModel("").size());
                //download_survey_data.setText(""+dbh.getPlotFarmerShareModel().size());
            }
            catch (JSONException e)
            {
                new AlertDialogManager().GreenDialog(context,message);
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,e.getMessage());
                //AlertPopUp(e.toString());
            }
        }
    }

}

