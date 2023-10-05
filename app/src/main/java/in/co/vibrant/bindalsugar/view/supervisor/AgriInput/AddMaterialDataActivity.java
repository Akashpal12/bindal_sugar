package in.co.vibrant.bindalsugar.view.supervisor.AgriInput;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.MaterialSearchListAdapter;
import in.co.vibrant.bindalsugar.model.AddSearchedMaterialModel;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.model.MaterialMasterModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.UtilFile;


public class AddMaterialDataActivity extends AppCompatActivity {
    private static final String TAG = "AddMaterialDataActivity";
    TextView tv_materialCode, tv_materialName, tv_matUnitCode, tv_materialRate, tv_total_amount, tvNoRecordAdded;
    AutoCompleteTextView autoTV_materialCode;
    EditText edt_quantity;
    Button btn_add_material, btn_submit;
    private DBHelper db;
    private List<MaterialMasterModel> materialMasterList = new ArrayList<>();
    private List<AddSearchedMaterialModel> addSerchMaterialList = new ArrayList<>();
    //MaterialAdapterAutoSearch adapter;
    MaterialSearchListAdapter materialSearchListAdapter;
    private Handler handlerMaterial;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    //RecyclerView recyclerView;
    private List<AgriInputFormModel> inputFormList = new ArrayList<>();
    String agriInputId;

    String mMatCodeSal = "";
    String mMatNameSal = "";
    String mMatUnitCodeSal = "";
    double mMatRateSal = 0;
    double enteredQty = 0;
    double totalAmount = 0;
    String mTotalAmt = "";
    Toolbar toolbar;
    //ListMaterailAdapter listMaterailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material_data);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_AGRI_INPUT));
        toolbar. setTitle(getString(R.string.MENU_AGRI_INPUT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = new DBHelper(this);
        init();
    }

    private void init() {


        try {
            if (db != null) {
                agriInputId=getIntent().getExtras().getString("agriInputId");
                inputFormList=db.getAllAgriInputDataById(agriInputId);
                materialMasterList.clear();
                materialMasterList.addAll(db.getMaterialMasterAllData());
                Log.d(TAG, "init: " + "Sisss" + materialMasterList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_materialCode = findViewById(R.id.tv_materialCode);
        tv_materialName = findViewById(R.id.tv_materialName);
        tv_materialRate = findViewById(R.id.tv_materialRate);
        tv_matUnitCode = findViewById(R.id.tv_matUnitCode);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        autoTV_materialCode = findViewById(R.id.autoTV_materialCode);
        tvNoRecordAdded = findViewById(R.id.tvNoRecordAdded);

        edt_quantity = findViewById(R.id.edt_quantity);
        btn_add_material = findViewById(R.id.btn_add_material);
        btn_submit = findViewById(R.id.btn_submit);
        materialSearchListAdapter = new MaterialSearchListAdapter(this, R.layout.all_list_row_item_search, materialMasterList);
        autoTV_materialCode.setThreshold(1);
        autoTV_materialCode.setAdapter(materialSearchListAdapter);

        autoTV_materialCode.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MaterialMasterModel mMaterialModel = (MaterialMasterModel) parent.getItemAtPosition(position);
                        if(mMaterialModel.getMaterialRate()>0)
                        {
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            autoTV_materialCode.setText(mMaterialModel.getMatCode() + "-" + mMaterialModel.getMatName());
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                            mMatCodeSal = mMaterialModel.getMatCode();
                            mMatNameSal = mMaterialModel.getMatName();
                            String mMatName = mMatNameSal.substring(0, 1).toUpperCase() + mMatNameSal.substring(1);
                            mMatUnitCodeSal = mMaterialModel.getMaterialUnitCode();
                            mMatRateSal = mMaterialModel.getMaterialRate();
                            tv_materialCode.setText("Material code : " + mMatCodeSal);
                            tv_materialName.setText(mMatName);
                            tv_matUnitCode.setText("Material unit code : " + mMatUnitCodeSal);
                            tv_materialRate.setText("Material rate : " + String.valueOf(mMatRateSal));
                        }
                        else
                        {
                            autoTV_materialCode.setText("");
                            mMatCodeSal = null;
                            mMatNameSal = null;
                            mMatUnitCodeSal = null;
                            tv_materialCode.setText("");
                            tv_materialName.setText("");
                            tv_matUnitCode.setText("");
                            tv_materialRate.setText("");
                            tv_total_amount.setText("");
                            new AlertDialogManager().showToast(AddMaterialDataActivity.this, "Material rate not defind");
                        }
                    }
                });
        autoTV_materialCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                materialSearchListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  tv_VillageCode.setText("");
            }
        });


        //================For Edittext addTextChangedListener================================
        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTotalAmt = getTotalMat();
                Log.d(TAG, "onTextChanged: "+">>>>"+mTotalAmt);
                tv_total_amount.setText(mTotalAmt);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // tv_total_amount.setText("");
            }
        });

        addSerchMaterialList.clear();
        btn_add_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = edt_quantity.getText().toString();
                if(autoTV_materialCode.getText().toString().length()>0)
                {
                    if(qty.length()>0)
                    {
                        boolean checkItemDuplicate=true;
                        AddSearchedMaterialModel model = new AddSearchedMaterialModel(agriInputId,
                                mMatCodeSal, mMatNameSal, mMatUnitCodeSal, String.valueOf(mMatRateSal), qty, mTotalAmt);
                        if(addSerchMaterialList.size()>0)
                        {
                            for(int i=0;i<addSerchMaterialList.size();i++)
                            {
                                if(addSerchMaterialList.get(i).getMatCode().equalsIgnoreCase(mMatCodeSal))
                                {
                                    checkItemDuplicate=false;
                                }
                            }
                        }
                        if(checkItemDuplicate)
                        {
                            addSerchMaterialList.add(model);
                            new AlertDialogManager().showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_added));
                        }
                        else
                        {
                            new AlertDialogManager().showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_duplicate_added));
                        }
                        //adapter.notifyDataSetChanged();
                        Log.d(TAG, "onClick: " + addSerchMaterialList.size());

                        if (addSerchMaterialList.size() > 0) {
                            ShowMaterialList(addSerchMaterialList);
                            autoTV_materialCode.setText("");
                            edt_quantity.setText("");
                            mMatCodeSal = null;
                            mMatNameSal = null;
                            mMatUnitCodeSal = null;
                            tv_materialCode.setText("");
                            tv_materialName.setText("");
                            tv_matUnitCode.setText("");
                            tv_materialRate.setText("");
                        } else {
                            //recyclerView.setVisibility(View.GONE);
                            //tvNoRecordAdded.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        new AlertDialogManager().showToast(AddMaterialDataActivity.this, "Please enter quantity");
                    }
                }
                else
                {
                    new AlertDialogManager().showToast(AddMaterialDataActivity.this, "Please select material");
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addSerchMaterialList.size() > 0) {
                    int mMaterialId = (int) db.insertMaterialSearched(addSerchMaterialList, "0", UtilFile.myTableId);
                    finish();
                    new AlertDialogManager().showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_add));
                    /*if (isInternetConnected(AddMaterialDataActivity.this)) {
                        //new WTaskAddFactory().execute();
                        //showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_add));
                    }
                    else {
                        finish();
                        new AlertDialogManager().showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_add));
                        //showToast(AddMaterialDataActivity.this, getResources().getString(R.string.oops_connect_your_internet));
                    }*/

                    Log.d(TAG, "onClick: "+"mMaterialId----"+mMaterialId);
                    //showToast(AddMaterialDataActivity.this, getResources().getString(R.string.master_add));
                    //addSerchMaterialList.clear();
                    //adapter.notifyDataSetChanged();
                    //recyclerView.setVisibility(View.GONE);
                    //tvNoRecordAdded.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void getMaterialmaster(String mMaterial) {
        if (materialMasterList.size() > 0) {
            //adapter.setData(materialMasterList);
            //adapter.notifyDataSetChanged();
        }
    }

    private String getTotalMat() {
        try {
            totalAmount=0;
            if (edt_quantity.getText().toString() != "" && edt_quantity.getText().length() > 0) {
                enteredQty = Double.parseDouble(edt_quantity.getText().toString());
            }
            if (mMatRateSal != 0) {
                totalAmount = (mMatRateSal * enteredQty);
            }
            return String.valueOf(totalAmount);
        } catch (Exception er) {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void ShowMaterialList(final List<AddSearchedMaterialModel> arrayList)
    {
        try {

            LinearLayout ll = (LinearLayout) findViewById(R.id.layout_booking);//Intent intent = new Intent(Login.this, MainActivity.class);
            ll.removeAllViewsInLayout();
            TableLayout dialogueTable = new TableLayout(AddMaterialDataActivity.this);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialogueTable.setShrinkAllColumns(true);
            dialogueTable.setStretchAllColumns(true);
            TableRow tableHeading = new TableRow(AddMaterialDataActivity.this);
            TextView th1 = new TextView(AddMaterialDataActivity.this);
            TextView th2 = new TextView(AddMaterialDataActivity.this);
            TextView th3 = new TextView(AddMaterialDataActivity.this);
            TextView th4 = new TextView(AddMaterialDataActivity.this);
            TextView th5 = new TextView(AddMaterialDataActivity.this);
            th1.setText("Material");
            th2.setText("Qty");
            th3.setText("Rate");
            th4.setText("Amount");
            th5.setText("");
            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            th3.setGravity(Gravity.CENTER);
            th4.setGravity(Gravity.CENTER);
            th5.setGravity(Gravity.CENTER);
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                tableHeading.setBackgroundResource(R.drawable.table_row_bg);
            } else {
                tableHeading.setBackground(ContextCompat.getDrawable(AddMaterialDataActivity.this, R.drawable.table_row_bg));
            }
            //tableHeading.setBackgroundResource(R.drawable.row_border);
            //tableHeading.addView(th1);
            tableHeading.addView(th1);
            tableHeading.addView(th2);
            tableHeading.addView(th3);
            tableHeading.addView(th4);
            tableHeading.addView(th5);
            dialogueTable.addView(tableHeading);
            for (int j = 0; j < arrayList.size(); j++) {
                TableRow row = new TableRow(AddMaterialDataActivity.this);
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    row.setBackgroundResource(R.drawable.table_row_bg);
                } else {
                    row.setBackground(ContextCompat.getDrawable(AddMaterialDataActivity.this, R.drawable.table_row_bg));
                }
                row.setId(j);
                TextView tv1 = new TextView(AddMaterialDataActivity.this);
                tv1.setText(arrayList.get(j).getMaterialName());
                tv1.setWidth(70);
                tv1.setGravity(Gravity.LEFT);
                TextView tv2 = new TextView(AddMaterialDataActivity.this);
                tv2.setText(arrayList.get(j).getMaterialQuantity());
                //tv2.setWidth(50);
                tv2.setGravity(Gravity.CENTER);
                TextView tv3 = new TextView(AddMaterialDataActivity.this);
                tv3.setText(arrayList.get(j).getMaterialRate());
                //tv3.setWidth(50);
                tv3.setGravity(Gravity.CENTER);
                TextView tv4 = new TextView(AddMaterialDataActivity.this);
                tv4.setText(arrayList.get(j).getMatTotalAmount());
                //tv4.setWidth(50);
                tv4.setGravity(Gravity.CENTER);
                ImageView deleteImg = new ImageView(AddMaterialDataActivity.this);
                deleteImg.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                final int flag = j;
                final String mName = arrayList.get(j).getMaterialName();
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.remove(flag);
                        //notifyItemRemoved(position);
                        //notifyItemRangeChanged(position, arrayList.size());
                        ShowMaterialList(arrayList);
                        Toast.makeText(AddMaterialDataActivity.this, mName + " " + "remove successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                row.addView(deleteImg);
                dialogueTable.addView(row);
            }
            ll.addView(dialogueTable);
        }
        catch(Exception e)
        {
            Log.d("",e.toString());
        }
    }

    /*private class WTaskAddFactory extends AsyncTask<String, Void, Void> {
        String message,server_status_id;
        private ProgressDialog dialog = new ProgressDialog(AddMaterialDataActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(AddMaterialDataActivity.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                //TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei=new GetDeviceImei(AddMaterialDataActivity.this).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SaveAgri);
                //DatabaseHelper db = new DatabaseHelper(AddMaterialDataActivity.this);
                List<AddMaterialDataModel> addMaterialDataModels=db.getAllAgriInputDataMaterialById(inputFormList.get(0).getAgri_input_Id());
                Map<String,Object> ItemList=new HashMap<>();
                String MatList="<Items>";
                for(int i=0;i<addMaterialDataModels.size();i++)
                {
                    MatList+="<Item>"+
                            "<matcode>"+addMaterialDataModels.get(i).getMatCode()+"</matcode>"+
                            "<matqty>"+addMaterialDataModels.get(i).getMaterialQuantity()+"</matqty>"+
                            "<matrate>"+addMaterialDataModels.get(i).getMaterialRate()+"</matrate>"+
                            "<mattotal>"+addMaterialDataModels.get(i).getMatTotalAmount()+"</mattotal>" +
                            "</Item>";

                }
                MatList+="</Items>";
                server_status_id=inputFormList.get(0).getServer_status();
                Bitmap bitmapPhoto1 = db.getRetrivePhoto1(inputFormList.get(0).getAgriVillageCode(), inputFormList.get(0).getAgriGrowerCode());
                ByteArrayOutputStream baoPhoto1 = new ByteArrayOutputStream();
                bitmapPhoto1.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto1);
                byte[] byteFormatPhoto1 = baoPhoto1.toByteArray();
                String photo1= Base64.encodeToString(byteFormatPhoto1,Base64.NO_WRAP);

                Bitmap bitmapPhoto2 = db.getRetrivePhoto2(inputFormList.get(0).getAgriVillageCode(), inputFormList.get(0).getAgriGrowerCode());
                ByteArrayOutputStream baoPhoto2 = new ByteArrayOutputStream();
                bitmapPhoto2.compress(Bitmap.CompressFormat.JPEG, 100, baoPhoto2);
                byte[] byteFormatPhoto2 = baoPhoto2.toByteArray();
                String photo2= Base64.encodeToString(byteFormatPhoto2,Base64.NO_WRAP);

                Bitmap bitmapSignature = db.getRetriveSignature(inputFormList.get(0).getAgriVillageCode(), inputFormList.get(0).getAgriGrowerCode());
                ByteArrayOutputStream baoSignature = new ByteArrayOutputStream();
                bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100, baoSignature);
                byte[] byteFormatSignature = baoSignature.toByteArray();
                String Signature= Base64.encodeToString(byteFormatSignature,Base64.NO_WRAP);

                Bitmap bitmapThumb = db.getRetriveThumb(inputFormList.get(0).getAgriVillageCode(), inputFormList.get(0).getAgriGrowerCode());
                ByteArrayOutputStream baoThumb = new ByteArrayOutputStream();
                bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 100, baoThumb);
                byte[] byteFormatThumb = baoThumb.toByteArray();
                String Thumb= Base64.encodeToString(byteFormatThumb,Base64.NO_WRAP);

                request1.addProperty("UNITCODE", inputFormList.get(0).getAgriGrowerUnitCode());
                request1.addProperty("VILLAGE_CODE", inputFormList.get(0).getAgriVillageCode());
                request1.addProperty("RYOT_CODE", inputFormList.get(0).getAgriGrowerCode());
                request1.addProperty("MOBILE", inputFormList.get(0).getAgriGrowerMobile());
                request1.addProperty("User",  db.getUserId());
                request1.addProperty("IMEI_NO",imei);
                request1.addProperty("Photo1",photo1);
                request1.addProperty("Photo2", photo2);
                request1.addProperty("Signature",Signature );
                request1.addProperty("Thumbimp",Thumb );
                request1.addProperty("Materials", MatList);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVE_AGRI, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("SaveAgriResult").toString();
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
            dialog.dismiss();
            Log.d("", "onPostExecute: " + result);
            if (message != null) {
                Log.d("", message);
                if(message.equalsIgnoreCase("true"))
                {
                    //DatabaseHelper db = new DatabaseHelper(mContext);
                    db.UpdateDataAgriInput(Integer.parseInt(agriInputId));
                    showToast((Activity) AddMaterialDataActivity.this, "Data successfully transfered to the web server");
                    finish();

                }
                else
                {
                    //showToast((Activity) mContext, message);
                }
            } else {
                //showToast((Activity) mContext, message);
            }
        }
    }*/
}
