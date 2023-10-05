package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import in.co.vibrant.bindalsugar.adapter.StaffGrowerListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.GrowerSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class RaSurveyStaffGrowerDetails extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    List<VillageModal> villageModelList;
    List<GrowerModel> growerModelList;
    List<GrowerSurveyModel> growerSurveyModelsList;
    Spinner village, grower;
    String villCode = "", growerCode = "";
    List<UserDetailsModel> userDetailsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_details_cm);
        Toolbar toolbar = findViewById(R.id.toolbar);


        context = RaSurveyStaffGrowerDetails.this;
        userDetailsModels = new ArrayList<>();
        village = findViewById(R.id.village);
        grower = findViewById(R.id.grower);
        dbh = new DBHelper(context);
        setSupportActionBar(toolbar);
        context = RaSurveyStaffGrowerDetails.this;
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
        villageModelList = new ArrayList<>();
        growerModelList = new ArrayList<>();

        growerSurveyModelsList = new ArrayList<>();
        village = findViewById(R.id.village);
        grower = findViewById(R.id.grower);
        dbh = new DBHelper(context);

        userDetailsModels = dbh.getUserDetailsModel();

        new GetVillageData().execute();


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
                    data1.add(jsonObject.getString("VCODE") + " - " + jsonObject.getString("VNAME"));
                }
                ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                        R.layout.list_item, data1);
                village.setAdapter(adaptersupply);
                village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            villCode = "";

                            //getGrower("");
                        } else {
                            String[] vill = village.getSelectedItem().toString().split(" - ");
                            villCode = vill[0].trim();
                            new GetGrowerData().execute(villCode, growerCode);
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
        private ProgressDialog dialog = new ProgressDialog(RaSurveyStaffGrowerDetails.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaSurveyStaffGrowerDetails.this, getString(R.string.app_name),
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
                    data1.add(" - All Grower - ");
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
                                new getGrowerDetails().execute(villCode,growerCode);
                                //getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    {
                        GrowerModel header = new GrowerModel();
                        header.setVillageCode("Vill Code");
                        header.setGrowerCode("Grower Code");
                        header.setGrowerName("Grower Name");
                        header.setGrowerFather("Grower Father");
                        header.setCentre("Centre");
                        header.setSociety("Society");
                        header.setMobile("Mobile");
                        header.setArea("Area");
                        header.setColor("#000000");
                        header.setTextColor("#FFFFFF");
                        growerModelList.add(header);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setVillageCode(jsonObject1.getString("G_VILL"));
                            growerModel.setGrowerCode(jsonObject1.getString("G_NO"));
                            growerModel.setGrowerName(jsonObject1.getString("G_NAME"));
                            growerModel.setGrowerFather(jsonObject1.getString("G_FATHER"));
                            growerModel.setCentre(jsonObject1.getString("CN_NAME"));
                            growerModel.setSociety(jsonObject1.getString("SO_NAME"));
                            growerModel.setMobile(jsonObject1.getString("G_ENTERTAINMENT_TELEPHONE"));
                            growerModel.setArea(jsonObject1.getString("G_T_AREA"));
                            if (i % 2 == 0) {
                                growerModel.setColor("#DFDFDF");
                                growerModel.setTextColor("#000000");
                            } else {
                                growerModel.setColor("#FFFFFF");
                                growerModel.setTextColor("#000000");
                            }
                            growerModelList.add(growerModel);
                        }

                        RecyclerView recyclerView = findViewById(R.id.recycler_list);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        StaffGrowerListAdapter stockSummeryAdapter = new StaffGrowerListAdapter(context, growerModelList);
                        recyclerView.setAdapter(stockSummeryAdapter);
                        if (dialog.isShowing())
                            dialog.dismiss();

                    }

                }




            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyStaffGrowerDetails.this, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyStaffGrowerDetails.this, "Error:" + e.toString());
            }
        }
    }


    private class getGrowerDetails extends AsyncTask<String, Integer, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetGrowerData);
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
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetGrowerData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetGrowerDataResult").toString();
                }
            }
            catch (SecurityException e) {
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
                        header.setVillageCode("Vill Code");
                        header.setGrowerCode("Grower Code");
                        header.setGrowerName("Grower Name");
                        header.setGrowerFather("Grower Father");
                        header.setCentre("Centre");
                        header.setSociety("Society");
                        header.setMobile("Mobile");
                        header.setArea("Area");
                        header.setColor("#000000");
                        header.setTextColor("#FFFFFF");
                        growerModelList.add(header);
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            GrowerModel growerModel=new GrowerModel();
                            growerModel.setVillageCode(jsonObject1.getString("G_VILL"));
                            growerModel.setGrowerCode(jsonObject1.getString("G_NO"));
                            growerModel.setGrowerName(jsonObject1.getString("G_NAME"));
                            growerModel.setGrowerFather(jsonObject1.getString("G_FATHER"));
                            growerModel.setCentre(jsonObject1.getString("CN_NAME"));
                            growerModel.setSociety(jsonObject1.getString("SO_NAME"));
                            growerModel.setMobile(jsonObject1.getString("G_ENTERTAINMENT_TELEPHONE"));
                            growerModel.setArea(jsonObject1.getString("G_T_AREA"));
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
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
                RecyclerView recyclerView =findViewById(R.id.recycler_list);
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                StaffGrowerListAdapter stockSummeryAdapter =new StaffGrowerListAdapter(context,growerModelList);
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



  /*  private class GetGrowerData extends AsyncTask<String, Void, Void> {
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
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("V_Code", params[0]);
                request1.addProperty("G_Code", params[1]);
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
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data1 = new ArrayList<String>();
                    data1.add(" - All Grower - ");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        data1.add(jsonObject1.getString("G_NO") + " - " + jsonObject1.getString("G_NAME"));
                    }
                    ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                            R.layout.list_item, data1);
                    grower.setAdapter(adaptersupply);
                    grower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                growerCode = "";
                                //getGrower("");
                            } else {
                                String[] grow = grower.getSelectedItem().toString().split(" - ");
                                growerCode = grow[0].trim();

                                //getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    {
                        GrowerModel header = new GrowerModel();
                        header.setVillageCode("Vill Code");
                        header.setGrowerCode("Grower Code");
                        header.setGrowerName("Grower Name");
                        header.setGrowerFather("Grower Father");
                        header.setCentre("Centre");
                        header.setSociety("Society");
                        header.setMobile("Mobile");
                        header.setArea("Area");
                        header.setColor("#000000");
                        header.setTextColor("#FFFFFF");
                        growerModelList.add(header);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setVillageCode(jsonObject1.getString("G_VILL"));
                            growerModel.setGrowerCode(jsonObject1.getString("G_NO"));
                            growerModel.setGrowerName(jsonObject1.getString("G_NAME"));
                            growerModel.setGrowerFather(jsonObject1.getString("G_FATHER"));
                            growerModel.setCentre(jsonObject1.getString("CN_NAME"));
                            growerModel.setSociety(jsonObject1.getString("SO_NAME"));
                            growerModel.setMobile(jsonObject1.getString("G_ENTERTAINMENT_TELEPHONE"));
                            growerModel.setArea(jsonObject1.getString("G_T_AREA"));
                            if (i % 2 == 0) {
                                growerModel.setColor("#DFDFDF");
                                growerModel.setTextColor("#000000");
                            } else {
                                growerModel.setColor("#FFFFFF");
                                growerModel.setTextColor("#000000");
                            }
                            growerModelList.add(growerModel);
                        }

                        RecyclerView recyclerView = findViewById(R.id.recycler_list);
                        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                        //recyclerView.setLayoutManager(manager);
                        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        StaffGrowerListAdapter stockSummeryAdapter = new StaffGrowerListAdapter(context, growerModelList);
                        recyclerView.setAdapter(stockSummeryAdapter);
                        if (dialog.isShowing())
                            dialog.dismiss();

                    }

                }

            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(context, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }*/


}
