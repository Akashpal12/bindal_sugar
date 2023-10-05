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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListAddVehicleAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffVehicleCollection extends AppCompatActivity {

    Context context;
    DBHelper dbh;
    SQLiteDatabase db;
    Spinner Supply;
    Button btn_add;
    EditText input_vehicle_num, input_capacity;
    String strsupply = "";
    List<UserDetailsModel> userDetailsModels;
    List<MasterDropDown> supplyModeList;
    EditText input_village_code, input_village_name, input_grower_code, input_grower_name, input_grower_father, input_village_code1, input_village_name1, input_grower_code1, input_grower_name1, input_grower_father1;
    String multipleGrowCode = "", multipleGrowName = "", multipleVillCode = "", multipleGrowFather = "";
    private List<GrowerModel> growerModelList = new ArrayList<>();
    private List<GrowerModel> growerMasterMultipleModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_vehicle_collection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.MENU_VEHICLE_COLLECTION));
        toolbar.setTitle(getString(R.string.MENU_VEHICLE_COLLECTION));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = StaffVehicleCollection.this;


        init();
    }


    private void init() {
        try {
            dbh = new DBHelper(context);
            userDetailsModels = dbh.getUserDetailsModel();
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();

            userDetailsModels = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();

            supplyModeList = new ArrayList<>();


            input_village_code = findViewById(R.id.input_village_code);
            input_village_name = findViewById(R.id.input_village_name);
            input_grower_code = findViewById(R.id.input_grower_code);
            input_grower_name = findViewById(R.id.input_grower_name);
            input_grower_father = findViewById(R.id.input_grower_father);

            input_village_code1 = findViewById(R.id.input_village_code1);
            input_village_name1 = findViewById(R.id.input_village_name1);
            input_grower_code1 = findViewById(R.id.input_grower_code1);
            input_grower_name1 = findViewById(R.id.input_grower_name1);
            input_grower_father1 = findViewById(R.id.input_grower_father1);
            Supply = findViewById(R.id.vehicle_list);
            input_vehicle_num = findViewById(R.id.input_vehicle_num);
            btn_add = findViewById(R.id.btn_add);
            input_capacity = findViewById(R.id.input_capacity);

            supplyModeList = dbh.getMasterDropDown("2");


            ArrayList<String> datasupply = new ArrayList<String>();
            datasupply.add("Vehicle Mode");
            for (int i = 0; i < supplyModeList.size(); i++) {
                datasupply.add(supplyModeList.get(i).getName());
                //datavarietytype.add(mastervarietyArray.getJSONObject(i).getString("Code") + " - " + mastervarietyArray.getJSONObject(i).getString("Name"));

            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(StaffVehicleCollection.this,
                    R.layout.list_item, datasupply);
            Supply.setAdapter(adaptersupply);


          /*  ArrayList<String> relationArray = new ArrayList<String>();
            relationArray.add("Select Vehicle Type");
            relationArray.add("Truck");
            relationArray.add("Trolly");
            relationArray.add("Kathi");
            ArrayAdapter<String> adapterseedSource = new ArrayAdapter<String>(context,
                    R.layout.list_item, relationArray);
            Supply.setAdapter(adapterseedSource);
*/


            input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        //checkPlot = false;
                        List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                        if (villageModalList.size() > 0) {
                            input_village_code.setText(villageModalList.get(0).getCode());
                            input_village_name.setText(villageModalList.get(0).getName());
                            //multipleVillCode=villageModalList.get(0).getCode();


                        } else {
                            input_village_code.setText("");
                            input_village_name.setText("");
                            new AlertDialogManager().RedDialog(context, "Please enter valid village code First");
                        }

                    }
                }
            });
            input_grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        if (input_grower_code.getText().toString().length() > 0) {
                            //checkPlot = false;
                            if (input_grower_code.getText().toString().equals("0")) {
                                input_grower_name.setFocusable(true);
                                input_grower_name.setTextIsSelectable(true);
                                input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                input_grower_father.setFocusable(true);
                                input_grower_father.setTextIsSelectable(true);
                                input_grower_father.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            } else {
                                input_grower_name.setFocusable(false);
                                input_grower_name.setTextIsSelectable(false);
                                input_grower_name.setInputType(InputType.TYPE_NULL);
                                input_grower_father.setFocusable(false);
                                input_grower_father.setTextIsSelectable(false);
                                input_grower_father.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                                if (growerModelList.size() > 0) {
                                    input_grower_code.setText(growerModelList.get(0).getGrowerCode());
                                    input_grower_name.setText(growerModelList.get(0).getGrowerName());
                                    input_grower_father.setText(growerModelList.get(0).getGrowerFather());
                                    /*multipleGrowCode=growerModelList.get(0).getGrowerCode();
                                    multipleGrowName=growerModelList.get(0).getGrowerName();
                                    multipleGrowFather=growerModelList.get(0).getGrowerFather();*/
                                } else {
                                    new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                    input_grower_code.setText("");
                                    input_grower_name.setText("");
                                    input_grower_father.setText("");

                                }
                            }

                        }
                    } else {
                        new AlertDialogManager().RedDialog(context, "Please enter village code First");
                        input_village_code.requestFocus();
                    }
                }
            });


            input_village_code1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code1.getText().toString().length() > 0) {
                        //checkPlot = false;
                        List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code1.getText().toString());
                        if (villageModalList.size() > 0) {
                            input_village_code1.setText(villageModalList.get(0).getCode());
                            input_village_name1.setText(villageModalList.get(0).getName());
                            multipleVillCode = villageModalList.get(0).getCode();


                        } else {
                            input_village_code1.setText("");
                            input_village_name1.setText("");
                            new AlertDialogManager().RedDialog(context, "Please enter valid village code First");
                        }

                    }
                }
            });
            input_grower_code1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code1.getText().toString().length() > 0) {
                        if (input_grower_code1.getText().toString().length() > 0) {
                            //checkPlot = false;
                            if (input_grower_code1.getText().toString().equals("0")) {
                                input_grower_name1.setFocusable(true);
                                input_grower_name1.setTextIsSelectable(true);
                                input_grower_name1.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                input_grower_father1.setFocusable(true);
                                input_grower_father1.setTextIsSelectable(true);
                                input_grower_father1.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            } else {
                                input_grower_name1.setFocusable(false);
                                input_grower_name1.setTextIsSelectable(false);
                                input_grower_name1.setInputType(InputType.TYPE_NULL);
                                input_grower_father1.setFocusable(false);
                                input_grower_father1.setTextIsSelectable(false);
                                input_grower_father1.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                                if (growerModelList.size() > 0) {
                                    input_grower_code1.setText(growerModelList.get(0).getGrowerCode());
                                    input_grower_name1.setText(growerModelList.get(0).getGrowerName());
                                    input_grower_father1.setText(growerModelList.get(0).getGrowerFather());
                                    multipleGrowCode = growerModelList.get(0).getGrowerCode();
                                    multipleGrowName = growerModelList.get(0).getGrowerName();
                                    multipleGrowFather = growerModelList.get(0).getGrowerFather();

                                } else {
                                    new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                    input_grower_code1.setText("");
                                    input_grower_name1.setText("");
                                    input_grower_father1.setText("");

                                }
                            }

                        }
                    } else {
                        new AlertDialogManager().RedDialog(context, "Please enter village code First");
                        input_village_code1.requestFocus();
                    }
                }
            });

            /////------------------Validation---///////////////////////

            input_village_code1.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {


                    checkValidation:
                    {
                        if (input_village_code.getText().toString().length() == 0) {
                            new AlertDialogManager().RedDialog(context, "please enter number of kohlu !!");
                            break checkValidation;
                        }
                        if (input_village_code1.getText().toString().length() == 0) {
                            //new AlertDialogManager().RedDialog(context,"please enter running kohlu !!");
                            //running_kolhu.setText("");
                            input_village_code1.requestFocus();
                            break checkValidation;
                        } else if (Integer.parseInt(input_village_code.getText().toString()) == Integer.parseInt(input_village_code1.getText().toString())) {
                            new AlertDialogManager().RedDialog(context, "please enter other vill code and grower code!!");
                            // running_kolhu.setText("");
                            input_village_code1.getText().clear();
                            input_village_code1.requestFocus();
                            break checkValidation;
                        }

                    }


                }
            });


            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {//multipleGrowCode="",multipleGrowName="",multipleVillCode="",multipleGrowFather="";
                        if (input_village_code.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select Village");
                        } else if (input_grower_code.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter Grower");
                        } else if (input_capacity.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter capacity");
                        } else if (Supply.getSelectedItemPosition() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select Vehicle Type");
                        } else if (input_vehicle_num.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please Enter Vehicle Number");
                        } else {
                            Gson gson = new Gson();
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setRelation(gson.toJson(growerModelList));
                            growerModel.setGrowerName(multipleGrowName);
                            growerModel.setGrowerCode(multipleGrowCode);
                            growerModel.setGrowerFather(multipleGrowFather);
                            growerModel.setVillageCode(multipleVillCode);
                            growerModel.setRelation(Supply.getSelectedItem().toString());
                            growerModel.setVehicleNo(input_vehicle_num.getText().toString());
                            growerModel.setCapacity(input_capacity.getText().toString());

                            if (growerMasterMultipleModels.contains(growerModel)) {
                                new AlertDialogManager().showToast((Activity) context, "This Vehicle type  already exists..");
                            } else {

                                growerMasterMultipleModels.add(growerModel);
                                multipleGrowName = "";
                                multipleGrowCode = "";
                                multipleGrowFather = "";
                                multipleVillCode = "";
                         /* input_village_code.setText("");
                            input_grower_code.setText("");*/
                                Supply.setSelection(0);
                            }
                            //growerMasterModels=new ArrayList<>();
                            setData();
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
                    }

                }
            });

        } catch (Exception e) {
            AlertPopUp("Error:" + e.toString());
        }

    }

    private void setData() {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            //recyclerView.setLayoutManager(manager);
            //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListAddVehicleAdapter listPloughingAdapter = new ListAddVehicleAdapter(context, growerMasterMultipleModels);
            recyclerView.setAdapter(listPloughingAdapter);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }
    }

    public void saveData(View v) {

        IndentModel indentModel = new IndentModel();
        indentModel.setSupplyMode(strsupply);

        if (input_village_code.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter village code");
        } else if (input_grower_code.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter grower code");
        } else if (input_grower_name.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter Grower Name");
        }

        if (dbh.getControlStatus("SUPPLY MODE", "INDENTING").equalsIgnoreCase("2")) {
            if (Supply.getSelectedItemPosition() == 0) {
                AlertPopUp("Please select Vehicle mode");

            } else {
                strsupply = supplyModeList.get(Supply.getSelectedItemPosition() - 1).getCode();
            }
        } else {
            Gson gson = new Gson();
            String growerJson = gson.toJson(growerModelList);
            new saveData().execute(growerJson);
        }


    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffVehicleCollection.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class saveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                ArrayList<Object> vehicleItemList = new ArrayList<>();
                for (int i = 0; i < growerMasterMultipleModels.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("VECHILETYPE", growerMasterMultipleModels.get(i).getRelation());
                    map.put("VECHILENO", growerMasterMultipleModels.get(i).getVehicleNo());
                    map.put("CAPCITY", growerMasterMultipleModels.get(i).getCapacity());
                    vehicleItemList.add(map);
                }
                String json = new Gson().toJson(vehicleItemList);
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVEGROWERVECHILEDETAILS);
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SUPCODE", userDetailsModels.get(0).getCode());
                request1.addProperty("VILLCODE", input_village_code.getText().toString());
                request1.addProperty("GROWCODE", input_grower_code.getText().toString());
                request1.addProperty("VECHILELIST", json);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVEGROWERVECHILEDETAILS, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVEGROWERVECHILEDETAILSResult").toString();
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    Intent intent = new Intent(context, StaffVehicleCollection.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"), intent);
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }
}
