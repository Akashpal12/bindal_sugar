package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
import in.co.vibrant.bindalsugar.adapter.ListAddFamilyMemAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffAddFamilyMember extends AppCompatActivity {
    Context context;
    DBHelper dbh;
    Spinner relation;
    Button btn_add;
    List<UserDetailsModel> userDetailsModels;
    EditText input_village_code, input_village_name, input_grower_code, input_grower_name, input_grower_father, input_village_code1, input_village_name1, input_grower_code1, input_grower_name1, input_grower_father1;
    String multipleGrowCode = "", multipleGrowName = "", multipleVillCode = "", multipleGrowFather = "";
    private List<GrowerModel> growerModelList = new ArrayList<>();
    private List<VillageModal> villageModalList = new ArrayList<>();
    private List<GrowerModel> growerMasterMultipleModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add_family_member);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_ADD_FAMILY_MEMBER));
        toolbar.setTitle(getString(R.string.MENU_ADD_FAMILY_MEMBER));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,StaffDevelopmentDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        context = StaffAddFamilyMember.this;
        dbh = new DBHelper(context);
        userDetailsModels = dbh.getUserDetailsModel();

        init();
    }


    private void init() {
        try {


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
            relation = findViewById(R.id.relation);
            btn_add = findViewById(R.id.btn_add);
            btn_add = findViewById(R.id.btn_add);
            ArrayList<String> relationArray = new ArrayList<String>();
            relationArray.add("Select Relation");
            relationArray.add("Father");
            relationArray.add("Mother");
            relationArray.add("Sister-In-Law");
            relationArray.add("Brother");
            relationArray.add("Sister");
            relationArray.add("Daughter");
            relationArray.add("Grandfather ");
            relationArray.add("Grandmother  ");
            relationArray.add("Mother In Law  ");
            relationArray.add("Father In Law ");
            ArrayAdapter<String> adapterseedSource = new ArrayAdapter<String>(context,
                    R.layout.list_item, relationArray);
            relation.setAdapter(adapterseedSource);


            input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        //checkPlot = false;
                        List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                        if (villageModalList.size() > 0) {
                            input_village_code.setText(villageModalList.get(0).getCode());
                            input_village_name.setText(villageModalList.get(0).getName());
                            multipleVillCode = villageModalList.get(0).getCode();
                            // multipleVillCode=villageModalList.get(0).getCode();


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
                                   /* multipleGrowCode=growerModelList.get(0).getGrowerCode();
                                    multipleGrowName=growerModelList.get(0).getGrowerName();
                                    multipleGrowFather=growerModelList.get(0).getGrowerFather();
*/
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
                    //setData();
                    //
                }
            });


            input_grower_code1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //  if (input_village_code1.getText().toString().length() > 0) {
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
                            List<GrowerModel> growerMasterMultipleModels = dbh.getGrowerModel(input_village_code1.getText().toString(), input_grower_code1.getText().toString());
                            if (growerMasterMultipleModels.size() > 0) {
                                input_grower_code1.setText(growerMasterMultipleModels.get(0).getGrowerCode());
                                input_grower_name1.setText(growerMasterMultipleModels.get(0).getGrowerName());
                                input_grower_father1.setText(growerMasterMultipleModels.get(0).getGrowerFather());
                                multipleGrowCode = growerMasterMultipleModels.get(0).getGrowerCode();
                                multipleGrowName = growerMasterMultipleModels.get(0).getGrowerName();
                                multipleGrowFather = growerMasterMultipleModels.get(0).getGrowerFather();

                            } else {
                                new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                input_grower_code1.setText("");
                                input_grower_name1.setText("");
                                input_grower_father1.setText("");

                            }
                        }

                    }
                } /*else {
                        new AlertDialogManager().RedDialog(context, "Please enter village code First");
                        //input_village_code1.requestFocus();
                    }*/

            });




            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {//multipleGrowCode="",multipleGrowName="",multipleVillCode="",multipleGrowFather="";
                        if (multipleVillCode.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select Village");
                        } else if (multipleGrowCode.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter Grower");
                        } else if (multipleGrowFather.length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter Grower");
                        } else if (Integer.parseInt(input_grower_code.getText().toString()) == Integer.parseInt(input_grower_code1.getText().toString())) {
                            new AlertDialogManager().RedDialog(context, "This Grower code already used!!");
                        } else if (relation.getSelectedItemPosition() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select relation");
                        } else {
                            Gson gson = new Gson();
                            GrowerModel growerModel = new GrowerModel();
                            //growerModel.setRelation(gson.toJson(growerMasterMultipleModels));
                            growerModel.setGrowerName(multipleGrowName);
                            growerModel.setGrowerCode(multipleGrowCode);
                            growerModel.setGrowerFather(multipleGrowFather);
                            growerModel.setVillageCode(multipleVillCode);
                            growerModel.setRelation(relation.getSelectedItem().toString());
                            if (growerMasterMultipleModels.contains(growerModel)) {
                                new AlertDialogManager().showToast((Activity) context, "This village & grower already exists..");
                            } else {
                                growerMasterMultipleModels.add(growerModel);
                                multipleGrowName = "";
                                multipleGrowCode = "";
                                multipleGrowFather = "";
                                multipleVillCode = "";
                                input_village_code1.setText("");
                                input_grower_code1.setText("");
                                input_grower_father1.setText("");
                                relation.setSelection(0);
                            }

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
            ListAddFamilyMemAdapter listPloughingAdapter = new ListAddFamilyMemAdapter(context, growerMasterMultipleModels);
            recyclerView.setAdapter(listPloughingAdapter);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }
    }

    public void saveData(View v) {
        if (input_village_code.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter village code");
        } else if (input_grower_code.length() == 0) {
            new AlertDialogManager().RedDialog(context, "Please enter grower code");
        } else {
            Gson gson = new Gson();
            String growerJson = gson.toJson(growerModelList);
            new saveData().execute(growerJson);
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffAddFamilyMember.this);
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

                ArrayList<Object> finalItemList = new ArrayList<>();
                for (int i = 0; i < growerMasterMultipleModels.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("villageCode", growerMasterMultipleModels.get(i).getVillageCode());
                    map.put("growerCode", growerMasterMultipleModels.get(i).getGrowerCode());
                    map.put("relation", growerMasterMultipleModels.get(i).getRelation());
                    finalItemList.add(map);
                }
                String json = new Gson().toJson(finalItemList);

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVEGROWERMEMBAR);
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SUPCODE", userDetailsModels.get(0).getCode());
                request1.addProperty("PRVILL", input_village_code.getText().toString());
                request1.addProperty("PRGROW", input_grower_code.getText().toString());
                request1.addProperty("MEMBERLIST", json);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVEGROWERMEMBAR, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVEGROWERMEMBARResult").toString();
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
                    Intent intent = new Intent(context, StaffAddFamilyMember.class);
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




