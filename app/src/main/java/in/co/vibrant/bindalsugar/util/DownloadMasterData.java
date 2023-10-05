package in.co.vibrant.bindalsugar.util;

import static com.github.vipulasri.timelineview.TimelineView.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ControlModel;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.FarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.MaterialMasterModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyOldModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;

public class DownloadMasterData {

    DBHelper dbh;
    Context context;
    String VillageCode;
    DBHelper getDbh;
    int tempInc = 0;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    AlertDialog dialog;
    boolean downloadingFarmerPlantingSurvey = false;

    public void GetDownloadFarmerPlantingSurvey(Context context, String villCode) {
        dbh = new DBHelper(context);
        this.context = context;
        VillageCode = villCode;
        userDetailsModels = dbh.getUserDetailsModel();
        downloadingFarmerPlantingSurvey = true;
        new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode(), villCode);
    }

    public void GetVillageData(Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        new GetVillageData().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());


    }

    public void GetDownloadMaster(Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        new GetDownloadMaster().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());


    }

    public void GetControlData(Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), villageCode);
        new GetControlData().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());


    }

    public void GetDownloadFarmer(final Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_download_farmer, null);
        dialogbilder.setView(mView);
        final Spinner village = mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        List<VillageSurveyModel> villageSurveyModelList = new ArrayList<>();
        villageSurveyModelList = dbh.getVillageModel("");
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < villageSurveyModelList.size(); i++) {
            data.add(villageSurveyModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok = mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageSurveyModelList = villageSurveyModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    VillageCode = "" + finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode();
                    new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode(), VillageCode);
                    //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode());
                } catch (Exception e) {

                }
            }
        });
        //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), villageCode);
    }

    public void GetDownloadPlanting(Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_download_farmer, null);
        dialogbilder.setView(mView);
        final Spinner village = mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        List<VillageSurveyModel> villageSurveyModelList = new ArrayList<>();
        villageSurveyModelList = dbh.getVillageModel("");
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < villageSurveyModelList.size(); i++) {
            data.add(villageSurveyModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok = mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageSurveyModelList = villageSurveyModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    VillageCode = "" + finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode();
                    new GetDownloadPlanting().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode(), VillageCode);
                    //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode());
                } catch (Exception e) {

                }
            }
        });


    }

    public void DownloadMaster(Context context) throws Exception {
        dbh = new DBHelper(context);
        db = new DBHelper(context).getWritableDatabase();
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        new DownloadMaster().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode());

    }

    public void GetDownloadSurvey(Context context) throws Exception {
        dbh = new DBHelper(context);
        this.context = context;
        userDetailsModels = dbh.getUserDetailsModel();
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_download_farmer, null);
        dialogbilder.setView(mView);
        final Spinner village = mView.findViewById(R.id.tv_village);
        //warehouseModalList;
        List<VillageSurveyModel> villageSurveyModelList = new ArrayList<>();
        villageSurveyModelList = dbh.getVillageModel("");
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < villageSurveyModelList.size(); i++) {
            data.add(villageSurveyModelList.get(i).getVillageName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adapter);
        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_ok = mView.findViewById(R.id.btn_ok);
        final List<VillageSurveyModel> finalVillageSurveyModelList = villageSurveyModelList;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    VillageCode = "" + finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode();
                    new GetDownloadSurvey().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode(), VillageCode);
                    //new GetDownloadFarmer().execute(userDetailsModels.get(0).getDivision(),userDetailsModels.get(0).getCode(), finalVillageSurveyModelList.get(village.getSelectedItemPosition()).getVillageCode());
                } catch (Exception e) {

                }
            }
        });
    }

    private class GetDownloadFarmer extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading farmer data ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei((context)).GetDeviceImeiNumber();
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

                    JSONArray jsonArray = new JSONArray(message);
                    if (jsonArray.length() > 0) {
                        dialog.setMax(jsonArray.length());
                        List<FarmerModel> a = dbh.getFarmerWithVillageModel(VillageCode, "");
                        if (a.size() > 0) {
                            dbh.deleteFarmerModel(VillageCode);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            publishProgress((int) i);
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FarmerModel farmerModel = new FarmerModel();
                            farmerModel.setFarmerCode(jsonObject.getString("FARMCD").replace(".0", ""));
                            farmerModel.setVillageCode(jsonObject.getString("VILLCD").replace(".0", ""));
                            farmerModel.setFarmerName(jsonObject.getString("FARMNM").replace(".0", ""));
                            farmerModel.setFatherName(jsonObject.getString("FATHNM").replace(".0", ""));
                            farmerModel.setUniqueCode(jsonObject.getString("UNIQCD").replace(".0", ""));
                            farmerModel.setDes(jsonObject.getString("DES").replace(".0", ""));
                            farmerModel.setMobile(jsonObject.getString("MOBILENO").replace(".0", ""));
                            farmerModel.setCultArea(jsonObject.getString("CULTAREA").replace(".0", ""));
                            dbh.insertFarmerModel(farmerModel);
                        }
                        //download_farmer_data.setText(dbh.getFarmerModel("").size()+" Farmer");
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                Log.d(TAG, message);
                new AlertDialogManager().GreenDialog(context, dbh.getFarmerModel("").size() + " farmer successfully imported");
                if (downloadingFarmerPlantingSurvey) {
                    new GetDownloadSurvey().execute(userDetailsModels.get(0).getDivision(), userDetailsModels.get(0).getCode(), VillageCode);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, message);
                //AlertPopUp(e.toString());
            }
        }
    }

    private class GetVillageData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name),
                    "Please wait while we getting village data from server", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei((context)).GetDeviceImeiNumber();
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
            } catch (Exception e) {
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
            if (dialog.isShowing())
                dialog.dismiss();
            if (message != null) {
                try {
                    Log.d(TAG, message);
                    JSONArray jsonArray = new JSONArray(message);
                    if (jsonArray.length() > 0) {
                        List<VillageSurveyModel> def = dbh.getDefaultSurveyVillage();
                        dbh.deleteVillageModel();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            VillageSurveyModel villageSurveyModel = new VillageSurveyModel();
                            villageSurveyModel.setVillageCode(jsonObject.getString("VILLCD").replace(".0", ""));
                            villageSurveyModel.setVillageName(jsonObject.getString("VILLNM").replace(".0", ""));
                            villageSurveyModel.setCenterName(jsonObject.getString("CENTNM").replace(".0", ""));
                            villageSurveyModel.setPlotSrNo(jsonObject.getString("PLOTSRNO").replace(".0", ""));
                            villageSurveyModel.setPlotSrNo1(jsonObject.getString("V_PLOTSRNO1").replace(".0", ""));
                            villageSurveyModel.setPlotSrNo2(jsonObject.getString("V_PLOTSRNO2").replace(".0", ""));
                            villageSurveyModel.setStopSurvey(jsonObject.getString("STOPSURVEY").replace(".0", ""));
                            villageSurveyModel.setTransSurvey(jsonObject.getString("TRANSNO").replace(".0", ""));
                            //villageSurveyModel.setIsDefault("0");
                            if (def.size() > 0) {
                                if (def.get(0).getVillageCode().equalsIgnoreCase(villageSurveyModel.getVillageCode())) {
                                    villageSurveyModel.setIsDefault("1");
                                } else {
                                    villageSurveyModel.setIsDefault("0");
                                }
                            } else {
                                villageSurveyModel.setIsDefault("0");
                            }
                            dbh.insertVillageModel(villageSurveyModel);
                        }

                        //download_village_data.setText(dbh.getVillageModel("").size()+" Village");
                        new AlertDialogManager().GreenDialog(context, dbh.getVillageModel("").size() + " successfully imported");
                    }
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, message);
                    // AlertPopUp(e.toString());
                }

            } else {
                // AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private class GetDownloadMaster extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Downloading up master data ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei((context)).GetDeviceImeiNumber();
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

                    JSONArray jsonArray = new JSONArray(message);
                    if (jsonArray.length() > 0) {
                        dialog.setMax(jsonArray.length());
                        dbh.deleteMasterModel();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            publishProgress((int) i);
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MasterCaneSurveyModel masterCaneSurveyModel = new MasterCaneSurveyModel();
                            masterCaneSurveyModel.setMstCode(jsonObject.getString("MSTCD").replace(".0", ""));
                            masterCaneSurveyModel.setCode(jsonObject.getString("CODE").replace(".0", ""));
                            masterCaneSurveyModel.setName(jsonObject.getString("NAME"));
                            dbh.insertMasterModel(masterCaneSurveyModel);
                        }

                        //download_master_data.setText(dbh.getMasterModel("").size()+" Master");

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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try {
                    Log.d(TAG, message);
                    new AlertDialogManager().GreenDialog(context, dbh.getMasterModel("").size() + " successfully imported");
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, message);

                }

            } else {

            }
        }
    }

    private class GetControlData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name),
                    "Please wait while we getting control data from server", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String imei = new GetDeviceImei((context)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadControl);
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_DownloadControl, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("DownloadControlResult").toString();
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("TAG", "onPostExecute: " + result);
            if (dialog.isShowing())
                dialog.dismiss();
            if (message != null) {
                try {
                    Log.d("TAG", message);
                    JSONArray jsonArray = new JSONArray(message);
                    if (jsonArray.length() > 0) {
                        dbh.deleteControlSurveyModel();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ControlSurveyModel factoryModel = new ControlSurveyModel();
                            factoryModel.setKhashraNo(jsonObject.getString("GETKHASARANO").replace(".0", ""));
                            factoryModel.setGataNo(jsonObject.getString("GETGATANO").replace(".0", ""));
                            factoryModel.setIs4G(jsonObject.getString("IS4G").replace(".0", ""));
                            factoryModel.setIsARM9(jsonObject.getString("ISARM9").replace(".0", ""));
                            factoryModel.setMixCrop(jsonObject.getString("GETMIXCROP").replace(".0", ""));
                            factoryModel.setInsect(jsonObject.getString("GETINSECT").replace(".0", ""));
                            factoryModel.setSeedSource(jsonObject.getString("GETSEEDSOURCE").replace(".0", ""));
                            factoryModel.setAadharNo(jsonObject.getString("GETAADHARNO").replace(".0", ""));
                            factoryModel.setPlant(jsonObject.getString("PLANT").replace(".0", ""));
                            factoryModel.setRatoon1(jsonObject.getString("RATOON1").replace(".0", ""));
                            factoryModel.setRatoon2(jsonObject.getString("RATOON2").replace(".0", ""));
                            factoryModel.setAutumn(jsonObject.getString("AUTUMN").replace(".0", ""));
                            factoryModel.setIshunPerShare(jsonObject.getString("ISHUNPERSHARE").replace(".0", ""));
                            factoryModel.setFortNo(jsonObject.getString("GETFORTNO").replace(".0", ""));
                            factoryModel.setAadhar01(jsonObject.getString("GETAADHARNO1").replace(".0", ""));
                            factoryModel.setExtra(jsonObject.getString("GETEXTRA").replace(".0", ""));
                            factoryModel.setSiteCode(jsonObject.getString("SITECODE").replace(".0", ""));
                            factoryModel.setStartHr(jsonObject.getString("STARTHR").replace(".0", ""));
                            factoryModel.setStopHr(jsonObject.getString("STOPHR").replace(".0", ""));
                            factoryModel.setIsOnline(jsonObject.getString("ISONLINE").replace(".0", ""));
                            factoryModel.setFtpAddr(jsonObject.getString("FTPADDR").replace(".0", ""));
                            factoryModel.setFtpUser(jsonObject.getString("FTPUSRNM").replace(".0", ""));
                            factoryModel.setFtpPass(jsonObject.getString("FTPPWD").replace(".0", ""));
                            factoryModel.setStaticIp(jsonObject.getString("STATICIP").replace(".0", ""));
                            factoryModel.setStaticPort(jsonObject.getString("STATICPORT").replace(".0", ""));
                            factoryModel.setHideArea(jsonObject.getString("HIDEAREA").replace(".0", ""));
                            factoryModel.setIsGpsPts(jsonObject.getString("ISGPSPTS").replace(".0", ""));
                            factoryModel.setShareInPer(jsonObject.getString("SHAREINPER").replace(".0", ""));
                            factoryModel.setMinLength(jsonObject.getString("MINLEN").replace(".0", ""));
                            factoryModel.setMaxLength(jsonObject.getString("MAXLEN").replace(".0", ""));
                            factoryModel.setSideDiffPer(jsonObject.getString("SIDEDIFFPER").replace(".0", ""));
                            factoryModel.setMobOPrCd(jsonObject.getString("MOBOPRCD").replace(".0", ""));
                            factoryModel.setPlantMethod(jsonObject.getString("GETPLANTMETHOD").replace(".0", ""));
                            factoryModel.setCropCond(jsonObject.getString("GETCROPCOND").replace(".0", ""));
                            factoryModel.setDisease(jsonObject.getString("GETDISEASE").replace(".0", ""));
                            factoryModel.setSocClerk(jsonObject.getString("GETSOCCLRK").replace(".0", ""));
                            factoryModel.setPlantDate(jsonObject.getString("GETPLNTDATE").replace(".0", ""));
                            factoryModel.setIrrigation(jsonObject.getString("GETIRRIGATION").replace(".0", ""));
                            factoryModel.setSoilType(jsonObject.getString("GETSOILTYPE").replace(".0", ""));
                            factoryModel.setLandType(jsonObject.getString("GETLANDTYPE").replace(".0", ""));
                            factoryModel.setBorderCrop(jsonObject.getString("GETBORDERCROP").replace(".0", ""));
                            factoryModel.setPlotType(jsonObject.getString("GETPLOTTYPE").replace(".0", ""));
                            factoryModel.setGhashtiNo(jsonObject.getString("GETGASHTINO").replace(".0", ""));
                            factoryModel.setSmsRecMobNo(jsonObject.getString("SMSRECMOBNO").replace(".0", ""));
                            factoryModel.setFtpHr1(jsonObject.getString("FTPHR1").replace(".0", ""));
                            factoryModel.setFtpHr2(jsonObject.getString("FTPHR2").replace(".0", ""));
                            factoryModel.setFtpHr3(jsonObject.getString("FTPHR3").replace(".0", ""));
                            factoryModel.setFtpHr4(jsonObject.getString("FTPHR4").replace(".0", ""));
                            factoryModel.setFactoryId(jsonObject.getString("FACTORYID").replace(".0", ""));
                            factoryModel.setInterCrop(jsonObject.getString("GETINTERCROP").replace(".0", ""));
                            factoryModel.setMcCode(jsonObject.getString("MCCODE").replace(".0", ""));
                            factoryModel.setIsXml(jsonObject.getString("ISXML").replace(".0", ""));
                            factoryModel.setInsect1(jsonObject.getString("GETINSECT1").replace(".0", ""));
                            factoryModel.setInterCrop1(jsonObject.getString("GETINTERCROP1").replace(".0", ""));
                            factoryModel.setMixCrop1(jsonObject.getString("GETMIXCROP1").replace(".0", ""));
                            factoryModel.setSeedSource1(jsonObject.getString("GETSEEDSOURCE1").replace(".0", ""));
                            dbh.insertControlSurveyModel(factoryModel);
                        }
                        //download_control_data.setText(dbh.getControlSurveyModel("").size()+" Control");
                        new AlertDialogManager().GreenDialog(context, dbh.getControlSurveyModel("").size() + " successfully imported");
                    }

                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, message);

                    //AlertPopUp(e.toString());
                }
            } else {

                //AlertPopUp(getString(R.string.oops_connect_your_internet));
            }

        }
    }

    private class GetDownloadSurvey extends AsyncTask<String, Integer, Void> {
        String message;
        int tempData = 0;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadLYSurvey);
                //SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_DownloadLYSurvey);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("DIVN", params[0]);
                request1.addProperty("MachineID", imei);
                request1.addProperty("OPR", params[1]);
                request1.addProperty("VILLAGECODE", VillageCode);
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

                    try {
                        JSONObject jsonObject1 = new JSONObject(message);
                        JSONArray jsonArray = jsonObject1.getJSONArray("DTSURVEY");
                        JSONArray jsonArrayShare = jsonObject1.getJSONArray("DTSURVEYDETAILS");
                        int totalData = jsonArray.length() + jsonArrayShare.length();
                        dialog.setMax(totalData);
                        dialog.setProgress(0);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        //JSONArray jsonArrayVillage=jsonObject1.getJSONArray("VILLAGE");
                        if (jsonArray.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting survey data...");
                                }
                            });
                            dbh.truncatePlotSurveyOldModel(VillageCode);
                            // dbh.truncateFarmerShareOldModel();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                tempData++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PlotSurveyOldModel plotSurveyModel = new PlotSurveyOldModel();
                                double distance1 = (jsonObject.getDouble("EAST_DISTANCE") + jsonObject.getDouble("WEST_DISTANCE")) / 2;
                                double distance2 = (jsonObject.getDouble("NORTH_DISTANCE") + jsonObject.getDouble("SOUTH_DISTANCE")) / 2;
                                double areaM = distance1 * distance2;
                                double areah = areaM / 10000;
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
                                Date dateObj2 = sdfIn.parse(jsonObject.getString("INSERT_DATE").replace("T", " "));
                                String currentDt = sdfOut.format(dateObj2);
                                plotSurveyModel.setInsertDate(currentDt);
                                dbh.insertPlotSurveyOldModel(plotSurveyModel);
                            }
                            //download_old_survey_data.setText(""+dbh.getFarmerModel("").size());
                        }
                        if (jsonArrayShare.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("updating survey share percent data...");
                                }
                            });


                            for (int i = 0; i < jsonArrayShare.length(); i++) {
                                tempData++;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        publishProgress(tempData);
                                    }
                                });
                                JSONObject jsonObject = jsonArrayShare.getJSONObject(i);
                                List<PlotSurveyOldModel> checkPlotSurvey = dbh.getPlotSurveyOldModelId(jsonObject.getString("GH_PLVILL"), jsonObject.getString("GH_NO"));
                                if (checkPlotSurvey.size() > 0) {
                                    //{"GH_PLVILL":1,"GH_NO":1,"SURVEY_ID":"11","VILLAGE_CODE":1,
                                    // "GROWER_CODE":57,"GROWER_NAME":"JAGMOHAN LAL",
                                    // "GROWER_FATHER_CODE":"PAIKARMA DEEN","GROWER_AADHAR_NUMBER":"0",
                                    // "SHARE2":100,"GH_SHARE_NO":1}
                                    FarmerShareOldModel plotSurveyModel = new FarmerShareOldModel();
                                    plotSurveyModel.setSurveyId(checkPlotSurvey.get(0).getColId());
                                    plotSurveyModel.setSrNo(jsonObject.getString("GH_SHARE_NO"));
                                    plotSurveyModel.setVillageCode(jsonObject.getString("VILLAGE_CODE"));
                                    plotSurveyModel.setGrowerCode(jsonObject.getString("GROWER_CODE"));
                                    plotSurveyModel.setGrowerName(jsonObject.getString("GROWER_NAME"));
                                    plotSurveyModel.setGrowerFatherName(jsonObject.getString("GROWER_FATHER_CODE"));
                                    plotSurveyModel.setGrowerAadharNumber(jsonObject.getString("GROWER_AADHAR_NUMBER"));
                                    plotSurveyModel.setShare(jsonObject.getString("SHARE2").replace(".0", ""));
                                    plotSurveyModel.setSupCode("" + userDetailsModels.get(0).getCode());
                                    plotSurveyModel.setCurDate(checkPlotSurvey.get(0).getInsertDate());
                                    plotSurveyModel.setServerStatus("OK");
                                    dbh.importFarmerShareOldModel(plotSurveyModel);
                                }
                            }
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
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
            if (message != null) {
                try {
                    Log.d(TAG, message);


                    new AlertDialogManager().GreenDialog(context, dbh.getPlotFarmerShareOldModelFilterData("", "").size() + " successfully imported");

                    //AlertPopUp("Data successfully imported");
                } catch (Exception e) {
                    new AlertDialogManager().GreenDialog(context, e.toString());

                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                new AlertDialogManager().GreenDialog(context, context.getString(R.string.oops_connect_your_internet));
            }
        }
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
                request1.addProperty("MachineID", imei);
                request1.addProperty("OPR", params[1]);

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
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setMessage("inserting planting data...");
                                }
                            });
                            dbh.truncatePlanting();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tempData++;
                                ((Activity) context).runOnUiThread(new Runnable() {
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
            if (message != null) {
                try {
                    Log.d(TAG, message);

                    // download_planting_data.setText(dbh.getPlantingModel("","","","","").size() + " Data");
                    //AlertPopUp(dbh.getPlantingModel("","","","","").size() + " successfully imported");
                    new AlertDialogManager().GreenDialog(context, dbh.getPlantingModel("", "", "", "", "").size() + " successfully imported");

                } catch (Exception e) {
                    new AlertDialogManager().GreenDialog(context, e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                new AlertDialogManager().GreenDialog(context, context.getString(R.string.oops_connect_your_internet));
            }
        }
    }

    private class DownloadMaster extends AsyncTask<String, Integer, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);
        String division,userId;

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
                division=params[0];
                userId=params[1];
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETMASTER";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("Factory", params[0]));
                entity.add(new BasicNameValuePair("USERID", params[1]));
                entity.add(new BasicNameValuePair("imei", getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);


                int totalData = 0;
                JSONArray jsonArray = new JSONArray(Content);
                Log.d("StafF Download Data",Content);
                if (jsonArray.length() > 0) {
                    db.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MasterSubDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MaterialMasterModel.TABLE_NAME);
                    db.execSQL(MasterDropDown.CREATE_TABLE);
                    db.execSQL(VillageModal.CREATE_TABLE);
                    db.execSQL(GrowerModel.CREATE_TABLE);
                    db.execSQL(ControlModel.CREATE_TABLE);
                    db.execSQL(MasterSubDropDown.CREATE_TABLE);
                    db.execSQL(MaterialMasterModel.CREATE_TABLE);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    final JSONArray IRRIGATIONLIST = jsonObject.getJSONArray("IRRIGATIONLIST");
                    final JSONArray SUPPLYMODELIST = jsonObject.getJSONArray("SUPPLYMODELIST");
                    final JSONArray HARVESTINGLIST = jsonObject.getJSONArray("HARVESTINGLIST");
                    final JSONArray EQUIMENTLIST = jsonObject.getJSONArray("EQUIMENTLIST");
                    final JSONArray LANDTYPELIST = jsonObject.getJSONArray("LANDTYPELIST");
                    final JSONArray SEEDTYPELIST = jsonObject.getJSONArray("SEEDTYPELIST");
                    final JSONArray BASALDOSELIST = jsonObject.getJSONArray("BASALDOSELIST");
                    final JSONArray SEEDTREATMENTLIST = jsonObject.getJSONArray("SEEDTREATMENTLIST");
                    final JSONArray METHODLIST = jsonObject.getJSONArray("METHODLIST");
                    final JSONArray SPRAYITEMLIST = jsonObject.getJSONArray("SPRAYITEMLIST");
                    final JSONArray SPRAYTYPELIST = jsonObject.getJSONArray("SPRAYTYPELIST");
                    final JSONArray PLOUGHINGTYPELIST = jsonObject.getJSONArray("PLOUGHINGTYPELIST");
                    final JSONArray VARIETYLIST = jsonObject.getJSONArray("VARIETYLIST");
                    final JSONArray PLANTINGTYPELIST = jsonObject.getJSONArray("PLANTINGTYPELIST");
                    final JSONArray PLANTATIONLIST = jsonObject.getJSONArray("PLANTATIONLIST");
                    final JSONArray CROPLIST = jsonObject.getJSONArray("CROPLIST");
                    final JSONArray GROWERDATALIST = jsonObject.getJSONArray("GROWERDATALIST");
                    final JSONArray VILLAGEDATALIST = jsonObject.getJSONArray("VILLAGEDATALIST");
                    final JSONArray FILDTRUEFALSELIST = jsonObject.getJSONArray("FILDTRUEFALSELIST");
                    final JSONArray SEEDTTYPELIST = jsonObject.getJSONArray("SEEDTTYPELIST");
                    final JSONArray SEEDSETTYPELIST = jsonObject.getJSONArray("SEEDSETTYPELIST");
                    final JSONArray SOILTREATMENTLIST = jsonObject.getJSONArray("SOILTREATMENTLIST");
                    final JSONArray FILDPREPRATIONLIST = jsonObject.getJSONArray("FILDPREPRATIONLIST");
                    final JSONArray ROWTOROWLIST = jsonObject.getJSONArray("ROWTOROWLIST");
                    final JSONArray TARGETTYPELIST = jsonObject.getJSONArray("TARGETTYPE");
                    final JSONArray PLOTACTIVITYMST = jsonObject.getJSONArray("PLOTACTIVITYMST");
                    final JSONArray PLOTACTIVITYMETHODMST = jsonObject.getJSONArray("PLOTACTIVITYMETHODMST");
                    final JSONArray SUPERVOISERLIST = jsonObject.getJSONArray("SUPERVOISER");
                    final JSONArray AGROITEMS = jsonObject.getJSONArray("AGROITEMS");
                    final JSONArray CANETYPE = jsonObject.getJSONArray("CANETYPE");
                    final JSONArray CATEGORY = jsonObject.getJSONArray("CATEGORY");
                    final JSONArray MEETINGTYPE = jsonObject.getJSONArray("MEETINGTYPE");
                    final JSONArray DISEFFECTEDTYPE = jsonObject.getJSONArray("DISEFFECTEDTYPE");
                    final JSONArray POCMASTER = jsonObject.getJSONArray("POCMASTER");

                    totalData += IRRIGATIONLIST.length();
                    totalData += SUPPLYMODELIST.length();
                    totalData += HARVESTINGLIST.length();
                    totalData += LANDTYPELIST.length();
                    totalData += SEEDTYPELIST.length();
                    totalData += BASALDOSELIST.length();
                    totalData += SEEDTREATMENTLIST.length();
                    totalData += METHODLIST.length();
                    totalData += SPRAYITEMLIST.length();
                    totalData += SPRAYTYPELIST.length();
                    totalData += PLOUGHINGTYPELIST.length();
                    totalData += VARIETYLIST.length();
                    totalData += PLANTINGTYPELIST.length();
                    totalData += PLANTATIONLIST.length();
                    totalData += CROPLIST.length();
                    totalData += GROWERDATALIST.length();
                    totalData += VILLAGEDATALIST.length();
                    totalData += FILDTRUEFALSELIST.length();
                    totalData += SEEDTTYPELIST.length();
                    totalData += SEEDSETTYPELIST.length();
                    totalData += SOILTREATMENTLIST.length();
                    totalData += FILDPREPRATIONLIST.length();
                    totalData += ROWTOROWLIST.length();
                    totalData += TARGETTYPELIST.length();
                    totalData += PLOTACTIVITYMST.length();
                    totalData += PLOTACTIVITYMETHODMST.length();
                    totalData += SUPERVOISERLIST.length();
                    totalData += AGROITEMS.length();
                    totalData += CANETYPE.length();
                    totalData += CATEGORY.length();
                    totalData += MEETINGTYPE.length();
                    totalData += DISEFFECTEDTYPE.length();
                    totalData += POCMASTER.length();

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
                    21-TARGETTYPELIST
                    24-PLOTACTIVITYMST
                    */

                    dialog.setMax(totalData);
                    dialog.setProgress(0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    if (IRRIGATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating irrigation data...");
                            }
                        });

                        for (int i = 0; i < IRRIGATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SUPPLYMODELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supply mode data...");

                            }
                        });

                        for (int i = 0; i < SUPPLYMODELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SUPPLYMODELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("2");
                            dbh.insertMasterData(masterDropDown);
                        }


                    }

                    if (HARVESTINGLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating harvesting data...");

                            }
                        });

                        for (int i = 0; i < HARVESTINGLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (EQUIMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating equipment data...");

                            }
                        });

                        for (int i = 0; i < EQUIMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (LANDTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating land type data...");
                            }
                        });

                        for (int i = 0; i < LANDTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i = 0; i < SEEDTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (BASALDOSELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating basaldose data...");
                            }
                        });

                        for (int i = 0; i < BASALDOSELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (METHODLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating method data...");
                            }
                        });

                        for (int i = 0; i < METHODLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SPRAYITEMLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray item data...");
                            }
                        });

                        for (int i = 0; i < SPRAYITEMLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SPRAYTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray type data...");
                            }
                        });

                        for (int i = 0; i < SPRAYTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOUGHINGTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating ploughing data...");
                            }
                        });

                        for (int i = 0; i < PLOUGHINGTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (VARIETYLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating variety list data...");
                            }
                        });

                        for (int i = 0; i < VARIETYLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLANTINGTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating planting type data...");
                            }
                        });

                        for (int i = 0; i < PLANTINGTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLANTATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plantation data...");
                            }
                        });

                        for (int i = 0; i < PLANTATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (CROPLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating crop list data...");
                            }
                        });

                        for (int i = 0; i < CROPLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (VILLAGEDATALIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating village data...");
                            }
                        });

                        for (int i = 0; i < VILLAGEDATALIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (GROWERDATALIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating grower data...");
                            }
                        });

                        for (int i = 0; i < GROWERDATALIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (FILDTRUEFALSELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating control data...");
                            }
                        });

                        for (int i = 0; i < FILDTRUEFALSELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTREATMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed treatment data...");
                            }
                        });

                        for (int i = 0; i < SEEDTREATMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i = 0; i < SEEDTTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDSETTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed set type data...");
                            }
                        });

                        for (int i = 0; i < SEEDSETTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SOILTREATMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating soil treatment data...");
                            }
                        });

                        for (int i = 0; i < SOILTREATMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (FILDPREPRATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating field prepration data...");
                            }
                        });

                        for (int i = 0; i < FILDPREPRATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (ROWTOROWLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating row to row data...");
                            }
                        });

                        for (int i = 0; i < ROWTOROWLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (TARGETTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i = 0; i < TARGETTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOTACTIVITYMST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity data...");
                            }
                        });

                        for (int i = 0; i < PLOTACTIVITYMST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOTACTIVITYMETHODMST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity method data...");
                            }
                        });

                        for (int i = 0; i < PLOTACTIVITYMETHODMST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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
                            masterDropDown.setItemFlag(object.getString("ITEMFLG"));
                            masterDropDown.setType("22");
                            dbh.insertMasterSubDropDown(masterDropDown);
                        }
                    }
                    if (SUPERVOISERLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supervisor data...");
                            }
                        });

                        for (int i = 0; i < SUPERVOISERLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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
                    if (AGROITEMS.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating Agro items data...");
                            }
                        });

                        for (int i = 0; i < AGROITEMS.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = AGROITEMS.getJSONObject(i);
                            MaterialMasterModel masterDropDown = new MaterialMasterModel();
                            masterDropDown.setMatCode(object.getString("ITEMCODE"));
                            masterDropDown.setMatName(object.getString("ITEMNAME"));
                            masterDropDown.setMaterialUnitCode(object.getString("UNIT"));
                            masterDropDown.setMaterialRate(object.getDouble("RATE"));
                            masterDropDown.setMaterialStatus("");
                            dbh.insertMaterialMasterModel(masterDropDown);
                        }
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

                    if (DISEFFECTEDTYPE.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating diseffectedtype  data...");
                            }
                        });

                        for (int i = 0; i < DISEFFECTEDTYPE.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = DISEFFECTEDTYPE.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("28");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }
                    if (POCMASTER.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating diseffectedtype  data...");
                            }
                        });

                        for (int i = 0; i < POCMASTER.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = POCMASTER.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("29");
                            dbh.insertMasterData(masterDropDown);
                        }
                    }
                }

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                //AlertPopUp("Error:"+e.toString());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                //AlertPopUp("Error:"+e.toString());
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
            try {
                dialog.dismiss();
                //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");
                new DownloadMasterData.UpdateMaster().execute(division,userId);
            } catch (Exception e) {
                //AlertPopUp("Error:"+e.toString());
            }
        }
    }

    public class PurchiDetails extends AsyncTask<String, Integer, Void> {
        String Content = null;
        int tempInc = 0;
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
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETPURCHYDETAILS";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN", params[0]));
                entity.add(new BasicNameValuePair("USERID", params[1]));
                entity.add(new BasicNameValuePair("imei", getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);


                int totalData = 0;
                JSONArray jsonArray = new JSONArray(Content);
                if (jsonArray.length() > 0) {
                    db.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MasterSubDropDown.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + MaterialMasterModel.TABLE_NAME);
                    db.execSQL(MasterDropDown.CREATE_TABLE);
                    db.execSQL(VillageModal.CREATE_TABLE);
                    db.execSQL(GrowerModel.CREATE_TABLE);
                    db.execSQL(ControlModel.CREATE_TABLE);
                    db.execSQL(MasterSubDropDown.CREATE_TABLE);
                    db.execSQL(MaterialMasterModel.CREATE_TABLE);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    final JSONArray IRRIGATIONLIST = jsonObject.getJSONArray("IRRIGATIONLIST");
                    final JSONArray SUPPLYMODELIST = jsonObject.getJSONArray("SUPPLYMODELIST");
                    final JSONArray HARVESTINGLIST = jsonObject.getJSONArray("HARVESTINGLIST");
                    final JSONArray EQUIMENTLIST = jsonObject.getJSONArray("EQUIMENTLIST");
                    final JSONArray LANDTYPELIST = jsonObject.getJSONArray("LANDTYPELIST");
                    final JSONArray SEEDTYPELIST = jsonObject.getJSONArray("SEEDTYPELIST");
                    final JSONArray BASALDOSELIST = jsonObject.getJSONArray("BASALDOSELIST");
                    final JSONArray SEEDTREATMENTLIST = jsonObject.getJSONArray("SEEDTREATMENTLIST");
                    final JSONArray METHODLIST = jsonObject.getJSONArray("METHODLIST");
                    final JSONArray SPRAYITEMLIST = jsonObject.getJSONArray("SPRAYITEMLIST");
                    final JSONArray SPRAYTYPELIST = jsonObject.getJSONArray("SPRAYTYPELIST");
                    final JSONArray PLOUGHINGTYPELIST = jsonObject.getJSONArray("PLOUGHINGTYPELIST");
                    final JSONArray VARIETYLIST = jsonObject.getJSONArray("VARIETYLIST");
                    final JSONArray PLANTINGTYPELIST = jsonObject.getJSONArray("PLANTINGTYPELIST");
                    final JSONArray PLANTATIONLIST = jsonObject.getJSONArray("PLANTATIONLIST");
                    final JSONArray CROPLIST = jsonObject.getJSONArray("CROPLIST");
                    final JSONArray GROWERDATALIST = jsonObject.getJSONArray("GROWERDATALIST");
                    final JSONArray VILLAGEDATALIST = jsonObject.getJSONArray("VILLAGEDATALIST");
                    final JSONArray FILDTRUEFALSELIST = jsonObject.getJSONArray("FILDTRUEFALSELIST");
                    final JSONArray SEEDTTYPELIST = jsonObject.getJSONArray("SEEDTTYPELIST");
                    final JSONArray SEEDSETTYPELIST = jsonObject.getJSONArray("SEEDSETTYPELIST");
                    final JSONArray SOILTREATMENTLIST = jsonObject.getJSONArray("SOILTREATMENTLIST");
                    final JSONArray FILDPREPRATIONLIST = jsonObject.getJSONArray("FILDPREPRATIONLIST");
                    final JSONArray ROWTOROWLIST = jsonObject.getJSONArray("ROWTOROWLIST");
                    final JSONArray TARGETTYPELIST = jsonObject.getJSONArray("TARGETTYPE");
                    final JSONArray PLOTACTIVITYMST = jsonObject.getJSONArray("PLOTACTIVITYMST");
                    final JSONArray PLOTACTIVITYMETHODMST = jsonObject.getJSONArray("PLOTACTIVITYMETHODMST");
                    final JSONArray SUPERVOISERLIST = jsonObject.getJSONArray("SUPERVOISER");
                    final JSONArray AGROITEMS = jsonObject.getJSONArray("AGROITEMS");

                    totalData += IRRIGATIONLIST.length();
                    totalData += SUPPLYMODELIST.length();
                    totalData += HARVESTINGLIST.length();
                    totalData += LANDTYPELIST.length();
                    totalData += SEEDTYPELIST.length();
                    totalData += BASALDOSELIST.length();
                    totalData += SEEDTREATMENTLIST.length();
                    totalData += METHODLIST.length();
                    totalData += SPRAYITEMLIST.length();
                    totalData += SPRAYTYPELIST.length();
                    totalData += PLOUGHINGTYPELIST.length();
                    totalData += VARIETYLIST.length();
                    totalData += PLANTINGTYPELIST.length();
                    totalData += PLANTATIONLIST.length();
                    totalData += CROPLIST.length();
                    totalData += GROWERDATALIST.length();
                    totalData += VILLAGEDATALIST.length();
                    totalData += FILDTRUEFALSELIST.length();
                    totalData += SEEDTTYPELIST.length();
                    totalData += SEEDSETTYPELIST.length();
                    totalData += SOILTREATMENTLIST.length();
                    totalData += FILDPREPRATIONLIST.length();
                    totalData += ROWTOROWLIST.length();
                    totalData += TARGETTYPELIST.length();
                    totalData += PLOTACTIVITYMST.length();
                    totalData += PLOTACTIVITYMETHODMST.length();
                    totalData += SUPERVOISERLIST.length();
                    totalData += AGROITEMS.length();


                   /* 1-IRRIGATIONLIST
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
                    24-PLOTACTIVITYMST*/


                    dialog.setMax(totalData);
                    dialog.setProgress(0);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    if (IRRIGATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating irrigation data...");
                            }
                        });

                        for (int i = 0; i < IRRIGATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SUPPLYMODELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supply mode data...");

                            }
                        });

                        for (int i = 0; i < SUPPLYMODELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = SUPPLYMODELIST.getJSONObject(i);
                            MasterDropDown masterDropDown = new MasterDropDown();
                            masterDropDown.setCode(object.getString("Code"));
                            masterDropDown.setName(object.getString("Name"));
                            masterDropDown.setType("2");
                            dbh.insertMasterData(masterDropDown);
                        }


                    }

                    if (HARVESTINGLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating harvesting data...");

                            }
                        });

                        for (int i = 0; i < HARVESTINGLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (EQUIMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating equipment data...");

                            }
                        });

                        for (int i = 0; i < EQUIMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (LANDTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating land type data...");
                            }
                        });

                        for (int i = 0; i < LANDTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i = 0; i < SEEDTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (BASALDOSELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating basaldose data...");
                            }
                        });

                        for (int i = 0; i < BASALDOSELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (METHODLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating method data...");
                            }
                        });

                        for (int i = 0; i < METHODLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SPRAYITEMLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray item data...");
                            }
                        });

                        for (int i = 0; i < SPRAYITEMLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SPRAYTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating spray type data...");
                            }
                        });

                        for (int i = 0; i < SPRAYTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOUGHINGTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating ploughing data...");
                            }
                        });

                        for (int i = 0; i < PLOUGHINGTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (VARIETYLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating variety list data...");
                            }
                        });

                        for (int i = 0; i < VARIETYLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLANTINGTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating planting type data...");
                            }
                        });

                        for (int i = 0; i < PLANTINGTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLANTATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plantation data...");
                            }
                        });

                        for (int i = 0; i < PLANTATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (CROPLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating crop list data...");
                            }
                        });

                        for (int i = 0; i < CROPLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (VILLAGEDATALIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating village data...");
                            }
                        });

                        for (int i = 0; i < VILLAGEDATALIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (GROWERDATALIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating grower data...");
                            }
                        });

                        for (int i = 0; i < GROWERDATALIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (FILDTRUEFALSELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating control data...");
                            }
                        });

                        for (int i = 0; i < FILDTRUEFALSELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTREATMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed treatment data...");
                            }
                        });

                        for (int i = 0; i < SEEDTREATMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDTTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed type data...");
                            }
                        });

                        for (int i = 0; i < SEEDTTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SEEDSETTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating seed set type data...");
                            }
                        });

                        for (int i = 0; i < SEEDSETTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (SOILTREATMENTLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating soil treatment data...");
                            }
                        });

                        for (int i = 0; i < SOILTREATMENTLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (FILDPREPRATIONLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating field prepration data...");
                            }
                        });

                        for (int i = 0; i < FILDPREPRATIONLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (ROWTOROWLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating row to row data...");
                            }
                        });

                        for (int i = 0; i < ROWTOROWLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (TARGETTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i = 0; i < TARGETTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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


                    if (SUPERVOISERLIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating supervisor data...");
                            }
                        });

                        for (int i = 0; i < SUPERVOISERLIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (TARGETTYPELIST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating target data...");
                            }
                        });

                        for (int i = 0; i < TARGETTYPELIST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOTACTIVITYMST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity data...");
                            }
                        });

                        for (int i = 0; i < PLOTACTIVITYMST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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

                    if (PLOTACTIVITYMETHODMST.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating plot activity method data...");
                            }
                        });

                        for (int i = 0; i < PLOTACTIVITYMETHODMST.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
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
                    }

                    if (AGROITEMS.length() > 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("Updating Agro items data...");
                            }
                        });

                        for (int i = 0; i < AGROITEMS.length(); i++) {
                            tempInc++;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publishProgress(tempInc);
                                }
                            });
                            JSONObject object = AGROITEMS.getJSONObject(i);
                            MaterialMasterModel masterDropDown = new MaterialMasterModel();
                            masterDropDown.setMatCode(object.getString("ITEMCODE"));
                            masterDropDown.setMatName(object.getString("ITEMNAME"));
                            masterDropDown.setMaterialUnitCode(object.getString("UNIT"));
                            masterDropDown.setMaterialRate(object.getDouble("RATE"));
                            masterDropDown.setMaterialStatus("");
                            dbh.insertMaterialMasterModel(masterDropDown);
                        }
                    }

                }

            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
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
            try {
                dialog.dismiss();
                //download_dropdown_data.setText(dbh.getMasterDropDown("").size()+" Data");
                new AlertDialogManager().GreenDialog(context, dbh.getMasterDropDown("").size() + " successfully imported");

            } catch (Exception e) {

                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
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
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/UPDATEMASTERDATETINE";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("DIVN", params[0]));
                entity.add(new BasicNameValuePair("USERCODE", params[1]));
                entity.add(new BasicNameValuePair("IMINO", getDeviceImei.GetDeviceImeiNumber()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                // new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
                // new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                //dbh.onUpgrade(db,1,1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub


            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (Content != null) {
                try {
                    Log.d(TAG, Content);
                    new AlertDialogManager().GreenDialog(context, dbh.getMasterDropDown("").size() + " successfully imported");
                    int update = Integer.parseInt(Content);
                    dbh.updateMasterDownload("" + update);
                } catch (Exception e) {
                    // new AlertDialogManager().GreenDialog(context, e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                new AlertDialogManager().GreenDialog(context, context.getString(R.string.oops_connect_your_internet));
            }


        }
    }


}
