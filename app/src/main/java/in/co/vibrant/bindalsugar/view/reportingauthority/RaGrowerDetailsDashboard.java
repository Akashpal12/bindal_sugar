package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.view.supervisor.StaffAccountDetails;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerEnquiry;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerLoanActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerPaymentActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerPurchiActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffGrowerWeightmentActivity;


public class RaGrowerDetailsDashboard extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    Spinner village, grower;
    String villCode = "", growerCode = "";
    List<UserDetailsModel> userDetailsModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_details_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = RaGrowerDetailsDashboard.this;
        setTitle(getString(R.string.MENU_GROWER_DETAILS));
        toolbar.setTitle(getString(R.string.MENU_GROWER_DETAILS));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userDetailsModelList = new ArrayList<>();
        village = findViewById(R.id.village);
        grower = findViewById(R.id.grower);
        dbh = new DBHelper(context);
        userDetailsModelList = dbh.getUserDetailsModel();


        /*ArrayList<String> data=new ArrayList<String>();
        data.add("Select Village");
        data.add("Dhanoura");
        data.add("Chinoura");
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setSelection(1);

        ArrayList<String> growerdata=new ArrayList<String>();
        growerdata.add("All Grower");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);*/
        // getVillage();
        new GetVillageData().execute();


    }

    /*public void getVillage() {
        villageModelList1 = dbh.getSurveyVillageModel("");
        ArrayList<String> data = new ArrayList<String>();
        data.add("Select Village");
        for (int i = 0; i < villageModelList1.size(); i++) {
            data.add(villageModelList1.get(i).getVillageCode() + " - " + villageModelList1.get(i).getVillageName());
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        village.setAdapter(adaptersupply);
        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //getGrower("");
                } else {
                    getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/

    /*public void getGrower(String villCode) {
        growerModelList = dbh.getGrowerModel(villCode, "");
        if (growerSurveyModelsList.size() > 0)
            growerSurveyModelsList.clear();
        GrowerModel growerSurveyModel1 = new GrowerModel();
        growerSurveyModel1.setVillageCode("");
        growerSurveyModel1.setGrowerCode("");
        growerSurveyModel1.setGrowerName("All Grower");
        //growerSurveyModelsList.add(growerSurveyModel1);
        ArrayList<String> data = new ArrayList<String>();
        data.add("Select Grower");
        for (int i = 0; i < growerModelList.size(); i++) {
            GrowerModel growerSurveyModel = new GrowerModel();
            growerSurveyModel.setVillageCode(growerModelList.get(i).getVillageCode());
            growerSurveyModel.setGrowerCode(growerModelList.get(i).getGrowerCode());
            growerSurveyModel.setGrowerName(growerModelList.get(i).getGrowerName());
            data.add(growerModelList.get(i).getGrowerCode() + " - " + growerModelList.get(i).getGrowerName());
            growerSurveyModelsList.add(growerSurveyModel);
        }
        ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        grower.setAdapter(adaptersupply);

    }*/

    public void openGrowerDetails(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffGrowerEnquiry.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }

    }

    public void openAccountDetails(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffAccountDetails.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }

    }

    public void openLoanDetail(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffGrowerLoanActivity.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }

    }

    public void openPurcheyDetail(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffGrowerPurchiActivity.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }

    }

    public void openPaymentDetail(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffGrowerPaymentActivity.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }

    }

    public void openWeightmentDetail(View v) {
        if (villCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select village");
        } else if (growerCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select grower");
        } else {
            Intent intent = new Intent(context, StaffGrowerWeightmentActivity.class);
            intent.putExtra("village", villCode);
            intent.putExtra("grower", growerCode);
            startActivity(intent);
        }


    }


    private class GetVillageData extends AsyncTask<String, Void, Void> {
        String message;
        String Content;
        private ProgressDialog dialog = new ProgressDialog(RaGrowerDetailsDashboard.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaGrowerDetailsDashboard.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetVillageList);
                request1.addProperty("factory", userDetailsModelList.get(0).getDivision());
                request1.addProperty("Supvcode", userDetailsModelList.get(0).getCode());
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
                            new GetGrowerData().execute(villCode);
                            //getGrower(villageModelList.get(village.getSelectedItemPosition() - 1).getCode());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(RaGrowerDetailsDashboard.this, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(RaGrowerDetailsDashboard.this, "Error:" + e.toString());
            }
        }
    }


    private class GetGrowerData extends AsyncTask<String, Void, Void> {
        String message;
        String Content;
        private ProgressDialog dialog = new ProgressDialog(RaGrowerDetailsDashboard.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaGrowerDetailsDashboard.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetGrowerData);
                request1.addProperty("DIVN", userDetailsModelList.get(0).getDivision());
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
                } else {

                }

            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(RaGrowerDetailsDashboard.this, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(RaGrowerDetailsDashboard.this, "Error:" + e.toString());
            }
        }
    }
}
