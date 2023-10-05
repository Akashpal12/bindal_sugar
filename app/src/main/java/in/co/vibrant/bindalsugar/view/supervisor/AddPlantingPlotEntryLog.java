package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class AddPlantingPlotEntryLog extends AppCompatActivity {
    String AREA;
    String GSRNO;
    String G_CODE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String SHARE_AREA;
    String StrCaneType = "";
    String StrVarietyCode = "";
    String TAG = "AddGrowerShare";
    String V_CODE;
    Spinner category;
    Context context;

    /* renamed from: db */
    SQLiteDatabase db;
    DBHelper dbh;
    EditText grower_name;
    List<MasterCaneSurveyModel> masterCaneTypeList;
    List<MasterCaneSurveyModel> masterVarietyModelList;
    EditText plot_sr_no;
    EditText remark;
    EditText standing_cane;
    List<UserDetailsModel> userDetailsModels;
    Spinner variety;
    EditText village_code;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plot_entry_log);
        try {
            context = this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.LBL_VARIETY));
            toolbar. setTitle(getString(R.string.LBL_VARIETY));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            userDetailsModels = dbh.getUserDetailsModel();
            V_CODE = getIntent().getExtras().getString("V_CODE");
            G_CODE = getIntent().getExtras().getString("G_CODE");
            PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
            GSRNO = getIntent().getExtras().getString("GSRNO");
            PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
            AREA = getIntent().getExtras().getString("AREA");
            SHARE_AREA = getIntent().getExtras().getString("SHARE_AREA");
            G_NAME = getIntent().getExtras().getString("G_NAME");
            village_code = (EditText) findViewById(R.id.village_code);
            grower_name = (EditText) findViewById(R.id.grower_name);
            plot_sr_no = (EditText) findViewById(R.id.plot_sr_no);
            category = (Spinner) findViewById(R.id.category);
            variety = (Spinner) findViewById(R.id.variety);
            standing_cane = (EditText) findViewById(R.id.standing_cane);
            remark = (EditText) findViewById(R.id.remark);
            village_code.setText(V_CODE);
            grower_name.setText(G_NAME);
            plot_sr_no.setText(PLOT_SR_NO);
            masterVarietyModelList = dbh.getMasterModel("1");
            ArrayList<String> dataVarietyModelList = new ArrayList<>();
            dataVarietyModelList.add(getString(R.string.LBL_SELECT));
            for (int i = 0; i < masterVarietyModelList.size(); i++) {
                dataVarietyModelList.add(masterVarietyModelList.get(i).getName());
            }
            variety.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataVarietyModelList));
            variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        AddPlantingPlotEntryLog addPlotEntryLog = AddPlantingPlotEntryLog.this;
                        addPlotEntryLog.StrVarietyCode = addPlotEntryLog.masterVarietyModelList.get(variety.getSelectedItemPosition() - 1).getCode();
                        return;
                    }
                    StrVarietyCode = "";
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            masterCaneTypeList = dbh.getMasterModel("2");
            ArrayList<String> dataCaneTypeList = new ArrayList<>();
            dataCaneTypeList.add(getString(R.string.LBL_SELECT));
            for (int i2 = 0; i2 < masterCaneTypeList.size(); i2++) {
                dataCaneTypeList.add(masterCaneTypeList.get(i2).getName());
            }
            category.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCaneTypeList));
            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        AddPlantingPlotEntryLog addPlotEntryLog = AddPlantingPlotEntryLog.this;
                        addPlotEntryLog.StrCaneType = addPlotEntryLog.masterCaneTypeList.get(category.getSelectedItemPosition() - 1).getCode();
                        return;
                    }
                    StrCaneType = "";
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            AlertDialogManager alertDialogManager = new AlertDialogManager();
            Context context2 = context;
            alertDialogManager.RedDialog(context2, "Error:" + e.toString());
        }
    }

    public void saveData(View v) {
        try {


        if (V_CODE.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter village code");
        } else if (G_CODE.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter grower code");
        } else if (PLOT_SR_NO.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter plot serial number");
        } else if (PLOT_VILL.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter plot village code");
        } else if (StrCaneType.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select cane type");
        } else if (StrVarietyCode.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please select variety");
        } else if (AREA.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter area");
        } else if (SHARE_AREA.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter share area");
        } else if (standing_cane.getText().toString().length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter standing cane");
        } else {
            new SaveLog().execute(new String[]{standing_cane.getText().toString(), remark.getText().toString()});
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: in.co.vibrant.canepotatodevelopment.view.fieldstaff.AddPlotEntryLog$SaveLog */
    private class SaveLog extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        String message;

        private SaveLog() {
            dialog = new ProgressDialog(context);
        }

        /* access modifiers changed from: protected */
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog show = ProgressDialog.show(context, getString(R.string.app_name), "Please wait", true);
            dialog = show;
            show.show();
        }

        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetCaneDevGrowerFinderDataSave);
                request1.addProperty("U_CODE", userDetailsModels.get(0).getCode());
                request1.addProperty("DIVN",  userDetailsModels.get(0).getDivision());
                request1.addProperty("V_CODE", V_CODE);
                request1.addProperty("G_CODE", G_CODE);
                request1.addProperty("VPLNO", PLOT_SR_NO);
                request1.addProperty("PLOTVILLAGE",  PLOT_VILL);
                request1.addProperty("CANETYPE",StrCaneType);
                request1.addProperty("VARIETY", StrVarietyCode);
                request1.addProperty("AREA", AREA);
                //request1.addProperty("SHARE_AREA", SHARE_AREA);
                request1.addProperty("STANDING_CANE", params[0]);
                request1.addProperty("REMARK",  params[1]);
                request1.addProperty("IMEINO", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetCaneDevGrowerFinderDataSave, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("GetCaneDevGrowerFinderDataSaveResult").toString();
                return null;
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            } catch (Exception e2) {
                Log.e("Exception", e2.getMessage());
                message = e2.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            String str = TAG;
            Log.d(str, "onPostExecute: " + result);
            if (message != null) {
                try {
                    Log.d(TAG, message);
                    JSONObject jsonObject=new JSONObject(message);
                    if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                    {
                        new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));
                    }
                    else
                    {
                        new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                    }

                } catch (Exception e) {
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } else if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
