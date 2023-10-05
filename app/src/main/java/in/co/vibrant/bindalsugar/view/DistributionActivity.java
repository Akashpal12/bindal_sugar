package in.co.vibrant.bindalsugar.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.BluetoothPrint.BluetoothPrinterConnect;
import in.co.vibrant.bindalsugar.BuildConfig;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GrowerSearchListAdapter;
import in.co.vibrant.bindalsugar.adapter.ItemSearchListAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageSearchListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.ItemModel;
import in.co.vibrant.bindalsugar.model.OfflineControlModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.FileUtility;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.PaddingString;

public class DistributionActivity extends AppCompatActivity {

    Context context;
    Spinner payment_type;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModelList;
    Spinner factoryspinner;
    List<VillageModal> villageModalList;
    List<GrowerModel> growerModelList;
    // List<ItemModel>itemModels;
    AutoCompleteTextView auto_Village_code, auto_Grower_code, auto_item;
    TextView village_name, grower_name, village_code, totalAmount, cgstText, sgstText;
    double cgst = 0.00, sgst = 0.00;
    TextView grower_fn, loan_limit, availed_loan_limit, balance_loan_limit, tv_total_amount;
    LinearLayout G_details;
    EditText cheque, quantity, amount;
    LinearLayout list;
    TableLayout dialogueTable;
    String qty;
    SQLiteDatabase db;
    List<OfflineControlModel> offlineControlModelList;
    double basicAmount = 0.00, grandCgstAmount = 0.00, grandSgstAmount = 0.00, grandTotal = 0.00;
    TextView basic_amount, cgst_amount, sgst_amount, total_Amount;
    String tempHsn = "";
    boolean isPrintSlip = false;
    private List<ItemModel> addMaterialDataModels = new ArrayList<>();
    private List<ItemModel> finalMaterialDataModels = new ArrayList<>();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);
        context = DistributionActivity.this;
        payment_type = findViewById(R.id.payment_spinner);
        dbh = new DBHelper(context);
        try {
            offlineControlModelList = dbh.getOfflineControlModel();
        } catch (Exception e) {
            Log.d("", "");
        }
        villageModalList = new ArrayList<>();
        growerModelList = new ArrayList<>();
        //itemModels=new ArrayList<>();
        userDetailsModelList = dbh.getUserDetailsModel();
        auto_Village_code = findViewById(R.id.auto_Village_code);
        auto_Grower_code = findViewById(R.id.auto_Grower_code);
        village_name = findViewById(R.id.village_name);
        grower_name = findViewById(R.id.grower_name);
        grower_fn = findViewById(R.id.grower_fn);
        loan_limit = findViewById(R.id.loan_limit);
        availed_loan_limit = findViewById(R.id.availed_loan_limit);
        balance_loan_limit = findViewById(R.id.balance_loan_limit);
        cheque = findViewById(R.id.cheque);
        quantity = findViewById(R.id.quantity);
        amount = findViewById(R.id.amount);
        tv_total_amount = findViewById(R.id.total_Amount);
        //total_listLayout=findViewById(R.id.total_listLayout);
        cgstText = findViewById(R.id.cgst);
        sgstText = findViewById(R.id.sgst);
        basic_amount = findViewById(R.id.basic_amount);
        cgst_amount = findViewById(R.id.cgst_amount);
        sgst_amount = findViewById(R.id.sgst_amount);
        total_Amount = findViewById(R.id.total_Amount);

        G_details = findViewById(R.id.g_details);
        auto_item = findViewById(R.id.auto_item);
        list = (LinearLayout) findViewById(R.id.layout_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Agri Distribution");
        toolbar.setTitle("Agri Distribution");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new GetVillageList().execute();

        //-----------Payment/loan Type-------------------------------
        ArrayList<String> paymenttype = new ArrayList<>();
        paymenttype.add(" - Select Payment Type -");
        paymenttype.add("Cash");
        paymenttype.add("Loan");
        ArrayAdapter<String> adapter_payment = new ArrayAdapter<String>(this, R.layout.list_item, paymenttype);
        payment_type.setAdapter(adapter_payment);
        payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    LinearLayout ll_loan_limit = findViewById(R.id.ll_loan_limit);
                    if (payment_type.getSelectedItem().toString().equalsIgnoreCase("Cash")) {
                        cheque.setVisibility(View.GONE);
                        cheque.setText("");
                        tv_total_amount.setText("");
                        //total_listLayout.setVisibility(View.GONE);

                        // resetForm();
                        finalMaterialDataModels.clear();
                        basicAmount = 0.00;
                        grandCgstAmount = 0.00;
                        grandSgstAmount = 0.00;
                        grandTotal = 0.00;
                        basic_amount.setText(new DecimalFormat("0.00").format(basicAmount));
                        cgst_amount.setText(new DecimalFormat("0.00").format(grandCgstAmount));
                        sgst_amount.setText(new DecimalFormat("0.00").format(grandSgstAmount));
                        total_Amount.setText(new DecimalFormat("0.00").format(grandTotal));
                        ShowMaterialList();
                        ll_loan_limit.setVisibility(View.GONE);
                        //loan_type_spinner.setSelection(0);
                    } else {
                        basicAmount = 0.00;
                        grandCgstAmount = 0.00;
                        grandSgstAmount = 0.00;
                        grandTotal = 0.00;
                        basic_amount.setText(new DecimalFormat("0.00").format(basicAmount));
                        cgst_amount.setText(new DecimalFormat("0.00").format(grandCgstAmount));
                        sgst_amount.setText(new DecimalFormat("0.00").format(grandSgstAmount));
                        total_Amount.setText(new DecimalFormat("0.00").format(grandTotal));
                        cheque.setVisibility(View.VISIBLE);
                        //total_listLayout.setVisibility(View.GONE);
                        if (balance_loan_limit.getText().toString().length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please select grower details");
                            payment_type.setSelection(0);
                            return;
                        }
                        if (Double.parseDouble(balance_loan_limit.getText().toString()) == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Sorry you dont have a loan limit");
                            payment_type.setSelection(0);
                            return;
                        }
                        ll_loan_limit.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void printerCheck(View v) {
        try {
            String printData = "Printer testing \n";
            printData += "1,2,3,4,5,6,7,8,9,0\n";
            printData += "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z\n";
            printData += "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z\n";
            printData += "!@#$%^&*()_+=-{}|\\][:\"';<>?/.,\n";
            printData += "Printer testing \n";
            printData += "Thank You \n";
            offlineControlModelList = dbh.getOfflineControlModel();
            if (offlineControlModelList.size() > 0) {
                if (offlineControlModelList.get(0).getPrintAddress().length() > 8) {
                    new FileUtility().PrintAtBTPrinter(context, printData, offlineControlModelList.get(0).getPrintAddress());
                } else {
                    new AlertDialogManager().AlertPopUp(context, "Printer not configure properly please reconnect printer");
                }
            } else {
                Intent intent = new Intent(context, BluetoothPrinterConnect.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("", "Error : " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPrintSlip) {
            offlineControlModelList = dbh.getOfflineControlModel();
            if (offlineControlModelList.size() == 0) {
                new AlertDialogManager().AlertPopUp(context, "Printer not configured, please configure printer by clicking on top right printer button on header");
                //return;
            }
        }

    }

    private void resetForm() {

        G_details.setVisibility(View.GONE);
        auto_Grower_code.setText("");
        village_name.setText("");
        grower_name.setText("");
        grower_fn.setText("");
        loan_limit.setText("");
        // availed_loan_limit.setText("");
        balance_loan_limit.setText("");
        cheque.setText("");
        auto_item.setText("");
        quantity.setText("");
        amount.setText("");
        payment_type.setSelection(0);
        finalMaterialDataModels.clear();
        tv_total_amount.setText("");
        cgstText.setText("");
        sgstText.setText("");
        //total_listLayout.setVisibility(View.GONE);
        basicAmount = 0.00;
        grandCgstAmount = 0.00;
        grandSgstAmount = 0.00;
        grandTotal = 0.00;
        basic_amount.setText(new DecimalFormat("0.00").format(basicAmount));
        cgst_amount.setText(new DecimalFormat("0.00").format(grandCgstAmount));
        sgst_amount.setText(new DecimalFormat("0.00").format(grandSgstAmount));
        total_Amount.setText(new DecimalFormat("0.00").format(grandTotal));
        ShowMaterialList();
    }

    public void addItem(View v) {
        try {

            if (payment_type.getSelectedItemPosition() == 2) {
                if (cheque.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select pronot number");
                    return;
                }
            }
            if (auto_item.getText().toString().length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please select item ");
                return;
            }
            if (quantity.getText().toString().length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please enter quantity ");
                return;
            }
            if (amount.getText().toString().length() == 0) {
                new AlertDialogManager().showToast((Activity) context, "Please enter amount ");
                return;
            }
            if (Double.parseDouble(quantity.getText().toString()) == 0) {
                new AlertDialogManager().showToast((Activity) context, "Quantity should be greater than zero");
                return;
            }
            if (Double.parseDouble(amount.getText().toString()) == 0) {
                new AlertDialogManager().showToast((Activity) context, "Amount should be greater than zero");
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Do you want to add " + auto_item.getText().toString().split(" - ")[1] + " in your cart");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String itemCode = auto_item.getText().toString().split(" - ")[0];
                    new GetStockBalance().execute(itemCode, quantity.getText().toString());
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }

    }

    //--------------------------search list---------------------------------------------------------
    private void initVillageSearch() {
        try {
            VillageSearchListAdapter villageSearchListAdapter = new VillageSearchListAdapter(this, R.layout.all_list_row_item_search, villageModalList);
            auto_Village_code.setThreshold(1);
            auto_Village_code.setAdapter(villageSearchListAdapter);

            auto_Village_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        if (auto_Village_code.getText().toString().length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter village code first");
                            return;
                        }
                        VillageModal mVillgModel = (VillageModal) parent.getItemAtPosition(position);
                        new GetGrowerList().execute(mVillgModel.getCode());
                    } catch (Exception e) {
                        Log.d("", e.getMessage());
                    }


                }
            });
            auto_Village_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    villageSearchListAdapter.getFilter().filter(s);


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //  tv_VillageCode.setText("");
                }
            });
        } catch (Exception e) {

        }
    }

    private void initGrowerSearch() {
        try {
            GrowerSearchListAdapter growerSearchListAdapter = new GrowerSearchListAdapter(this, R.layout.all_list_row_item_search_grower, growerModelList);
            auto_Grower_code.setThreshold(1);
            auto_Grower_code.setAdapter(growerSearchListAdapter);
            auto_Grower_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (auto_Village_code.getText().toString().length() == 0) {
                            new AlertDialogManager().showToast((Activity) context, "Please enter village code first");
                            return;
                        }
                        GrowerModel mgModel = (GrowerModel) parent.getItemAtPosition(position);
                        new GrowerDetails().execute(auto_Village_code.getText().toString(), mgModel.getGrowerCode());
                    } catch (Exception e) {
                        Log.d("", e.getMessage());
                    }


                }

            });
            auto_Grower_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    growerSearchListAdapter.getFilter().filter(s);
                    tv_total_amount.setText("");
                    //total_listLayout.setVisibility(View.GONE);

                    // grower_name.setText("");

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });
        } catch (Exception e) {

        }
    }

    private void itemSearch() {
        ItemSearchListAdapter itemSearchListAdapter = new ItemSearchListAdapter(this, R.layout.all_list_row_item_search, addMaterialDataModels);
        auto_item.setThreshold(1);
        auto_item.setAdapter(itemSearchListAdapter);

        auto_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ItemModel itemModel = (ItemModel) parent.getItemAtPosition(position);
                    auto_item.setText(itemModel.getIt_code() + " - " + itemModel.getIt_name());
                    amount.setText(itemModel.getAmount() + "");
                    tempHsn = itemModel.getHsnno() + "";
                    cgstText.setText("CGST : " + itemModel.getCgst());
                    sgstText.setText("SGST : " + itemModel.getSgst());
                    cgst = itemModel.getCgst();
                    sgst = itemModel.getSgst();
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }
            }
        });
        auto_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemSearchListAdapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {
                //  tv_VillageCode.setText("");
            }
        });
    }

    //------------------------------create the list-------------------------------------------------
    public void ShowMaterialList() {
        try {
            basicAmount = 0.00;
            grandCgstAmount = 0.00;
            grandSgstAmount = 0.00;
            grandTotal = 0.00;
            //Intent intent = new Intent(Login.this, MainActivity.class);
            list.removeAllViewsInLayout();
            dialogueTable = new TableLayout(DistributionActivity.this);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //dialogueTable.setShrinkAllColumns(true);
            //dialogueTable.setStretchAllColumns(true);
            TableRow tableHeading = new TableRow(DistributionActivity.this);
            TextView th1 = new TextView(DistributionActivity.this);
            TextView th2 = new TextView(DistributionActivity.this);
            TextView th3 = new TextView(DistributionActivity.this);
            TextView th4 = new TextView(DistributionActivity.this);
            TextView th5 = new TextView(DistributionActivity.this);
            TextView th6 = new TextView(DistributionActivity.this);
            TextView th7 = new TextView(DistributionActivity.this);
            TextView th8 = new TextView(DistributionActivity.this);
            TextView th9 = new TextView(DistributionActivity.this);
            th1.setText("Code");
            th2.setText("Name");
            th3.setText("Rate");
            th4.setText("Qty");
            th9.setText("Amount");
            th5.setText("CGST");
            th6.setText("SGST");
            th7.setText("Total");
            th8.setText("");
            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            th3.setGravity(Gravity.CENTER);
            th4.setGravity(Gravity.CENTER);
            th5.setGravity(Gravity.CENTER);
            th6.setGravity(Gravity.CENTER);
            th7.setGravity(Gravity.CENTER);
            th8.setGravity(Gravity.CENTER);
            th9.setGravity(Gravity.CENTER);
            th1.setWidth(150);
            th2.setWidth(350);
            th3.setWidth(200);
            th4.setWidth(200);
            th5.setWidth(200);
            th6.setWidth(200);
            th7.setWidth(200);
            th8.setWidth(200);
            th9.setWidth(200);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                tableHeading.setBackgroundResource(R.drawable.table_row_bg);
            } else {
                tableHeading.setBackground(ContextCompat.getDrawable(DistributionActivity.this, R.drawable.table_row_bg));
            }
            tableHeading.addView(th1);
            tableHeading.addView(th2);
            tableHeading.addView(th3);
            tableHeading.addView(th4);
            tableHeading.addView(th9);
            tableHeading.addView(th5);
            tableHeading.addView(th6);
            tableHeading.addView(th7);
            tableHeading.addView(th8);

            dialogueTable.addView(tableHeading);
            for (int j = 0; j < finalMaterialDataModels.size(); j++) {
                /*double amountStr = finalMaterialDataModels.get(j).getAmount();
                double qty = finalMaterialDataModels.get(j).getQty();
                double cgstAmount = finalMaterialDataModels.get(j).getCgstAmount();
                double sgstAmount = finalMaterialDataModels.get(j).getSgstAmount();
                double finalAmontItem = amountStr+cgstAmount+sgstAmount;*/
                //double total = finalAmoyntItem*qty;

                basicAmount += (finalMaterialDataModels.get(j).getAmount());
                grandCgstAmount += (finalMaterialDataModels.get(j).getCgstAmount());
                grandSgstAmount += (finalMaterialDataModels.get(j).getSgstAmount());
                grandTotal += (finalMaterialDataModels.get(j).getTotal());
                TableRow row = new TableRow(DistributionActivity.this);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    row.setBackgroundResource(R.drawable.table_row_bg);
                } else {
                    row.setBackground(ContextCompat.getDrawable(DistributionActivity.this, R.drawable.table_row_bg));
                }
                row.setId(j);
                TextView tv1 = new TextView(DistributionActivity.this);
                tv1.setText(finalMaterialDataModels.get(j).getIt_code());
                tv1.setWidth(150);
                tv1.setGravity(Gravity.CENTER);
                TextView tv2 = new TextView(DistributionActivity.this);
                tv2.setText(finalMaterialDataModels.get(j).getIt_name());
                tv2.setWidth(350);
                tv2.setGravity(Gravity.CENTER);
                TextView tv3 = new TextView(DistributionActivity.this);
                tv3.setText("" + finalMaterialDataModels.get(j).getRate());
                tv3.setWidth(200);
                tv3.setGravity(Gravity.CENTER);

                TextView tv4 = new TextView(DistributionActivity.this);
                tv4.setText("" + finalMaterialDataModels.get(j).getQty());
                tv4.setWidth(200);
                tv4.setGravity(Gravity.CENTER);

                TextView tv9 = new TextView(DistributionActivity.this);
                tv9.setText("" + finalMaterialDataModels.get(j).getAmount());
                tv9.setWidth(200);
                tv9.setGravity(Gravity.CENTER);

                TextView tv5 = new TextView(DistributionActivity.this);
                tv5.setText(finalMaterialDataModels.get(j).getCgstAmount() + "\n(" + finalMaterialDataModels.get(j).getCgst() + "%)");
                tv5.setWidth(200);
                tv5.setGravity(Gravity.CENTER);

                TextView tv6 = new TextView(DistributionActivity.this);
                tv6.setText(finalMaterialDataModels.get(j).getSgstAmount() + "\n(" + finalMaterialDataModels.get(j).getSgst() + "%)");
                tv6.setWidth(200);
                tv6.setGravity(Gravity.CENTER);

                TextView tv7 = new TextView(DistributionActivity.this);
                tv7.setText("" + finalMaterialDataModels.get(j).getTotal());
                tv7.setWidth(200);
                tv7.setGravity(Gravity.CENTER);

                ImageView deleteImg = new ImageView(DistributionActivity.this);
                deleteImg.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                int finalJ = j;
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            finalMaterialDataModels.remove(finalJ);
                            ShowMaterialList();
                            //total_listLayout.setVisibility(View.GONE);
                            Toast.makeText(DistributionActivity.this, finalMaterialDataModels.get(finalJ).getIt_code() + " " + "remove successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d(",", e.toString());
                        }
                    }
                });
                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                row.addView(tv9);
                row.addView(tv5);
                row.addView(tv6);
                row.addView(tv7);
                row.addView(deleteImg);
                dialogueTable.addView(row);
            }
            list.addView(dialogueTable);

            basic_amount.setText(new DecimalFormat("0.00").format(basicAmount));
            cgst_amount.setText(new DecimalFormat("0.00").format(grandCgstAmount));
            sgst_amount.setText(new DecimalFormat("0.00").format(grandSgstAmount));
            total_Amount.setText(new DecimalFormat("0.00").format(grandTotal));
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    //------------------------------------------VillageList----------------------------------------------------

    public void submitItem(View v) {
        //new FileUtility().PrintAtBTPrinter(context,"Test");
        try {
            if (auto_Village_code.getText().toString().length() == 0) {
                new AlertDialogManager().RedDialog((Activity) context, "Please enter village code");
                return;
            }
            if (auto_Grower_code.getText().toString().length() == 0) {
                new AlertDialogManager().RedDialog((Activity) context, "Please enter grower code");
                return;
            }
            if (payment_type.getSelectedItemPosition() == 0) {
                new AlertDialogManager().RedDialog((Activity) context, "Please select payment type");
                return;
            }
            if (payment_type.getSelectedItemPosition() == 2) {
                if (cheque.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog((Activity) context, "Please enter pronot number");
                    return;
                }
            }
            if (finalMaterialDataModels.size() == 0) {
                new AlertDialogManager().RedDialog((Activity) context, "Please add item in cart");
                return;
            }

            List<Object> objectList = new ArrayList<>();
            for (int i = 0; i < finalMaterialDataModels.size(); i++) {
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("ITEMCODE", finalMaterialDataModels.get(i).getIt_code());
                stringObjectMap.put("ITEMDESC", finalMaterialDataModels.get(i).getIt_name());
                stringObjectMap.put("RATE", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getAmount()));
                stringObjectMap.put("QTY", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getQty()));
                stringObjectMap.put("AMOUNT", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getAmount()));
                stringObjectMap.put("TAXAMOUNT", "0.00");
                stringObjectMap.put("CGST", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getCgst()));
                stringObjectMap.put("CGSTAMOUNT", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getCgstAmount()));
                stringObjectMap.put("SGST", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getSgst()));
                stringObjectMap.put("CGSTAMOUNT", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getSgstAmount()));
                stringObjectMap.put("TOTAL", new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getTotal()));
                stringObjectMap.put("HSN", finalMaterialDataModels.get(i).getHsnno());
                objectList.add(stringObjectMap);
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Are you sure you want to submit !! ");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Gson gson = new Gson();
                        String jsonDat = gson.toJson(objectList);
                        //Log.d("","");
                        new saveAgrilinput().execute(jsonDat);
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
                    }
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            Log.d("", "Error : " + e.toString());
        }

    }

    private void printData(String invoiceNumber, String date, String time) {
        try {
            String printData = "";
            int length = 24;
            printData += new PaddingString().padBoth("GOVIND SUGAR MILL LTD", length, " ");
            printData += "\n\n";
            printData += new PaddingString().padBoth("PROVISIONAL RECEIPT", length, " ");
            printData += "\n";
            printData += new PaddingString().padLeft("", length, "-");
            printData += new PaddingString().padBoth("ZONAL OFFICE:" + userDetailsModelList.get(0).getZoneName(), length, " ");
            printData += "\n\n";
            printData += new PaddingString().padBoth("DISTT.MEERUT(U.P.)", length, " ");
            printData += "\n\n";
            printData += "Season :" + new PaddingString().padLeft("2022-23", length - 8, " ");
            printData += new PaddingString().padLeft("", 24, "-");
            if (payment_type.getSelectedItem().toString().equalsIgnoreCase("Loan")) {
                printData += "PRONOTE:" + new PaddingString().padLeft(cheque.getText().toString(), length - 8, " ");
            }
            printData += "Rec. No:" + new PaddingString().padLeft(invoiceNumber, length - 8, " ");
            printData += date + "" + new PaddingString().padLeft(time, length - 10, " ");
            printData += "ZONAL OFFICE:" + new PaddingString().padLeft(userDetailsModelList.get(0).getZoneName(), length - 13, " ");
            printData += "VILL:" + new PaddingString().padLeft(village_name.getText().toString(), length - 5, " ");
            printData += "GROW:" + new PaddingString().padLeft(grower_name.getText().toString(), length - 5, " ");
            printData += "FATHER:" + new PaddingString().padLeft(grower_fn.getText().toString(), length - 7, " ");
            printData += new PaddingString().padLeft("", length, "-");

            for (int i = 0; i < finalMaterialDataModels.size(); i++) {
                printData += "SR NO:" + new PaddingString().padLeft("" + (i + 1), length - 6, " ");
                printData += "HSN:" + new PaddingString().padLeft(finalMaterialDataModels.get(i).getHsnno(), length - 4, " ");
                printData += "" + new PaddingString().padBoth(finalMaterialDataModels.get(i).getIt_name(), length, " ");
                printData += "\n";
                printData += "RATE:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getRate()), length - 5, " ");
                printData += "QTY:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getQty()), length - 4, " ");
                printData += "AMOUNT:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getAmount()), length - 7, " ");
                printData += "CGST: @ " + finalMaterialDataModels.get(i).getCgst() + "%" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getCgstAmount()), length - 13, " ");
                printData += "\n";
                printData += "SGST: @ " + finalMaterialDataModels.get(i).getSgst() + "%" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getSgstAmount()), length - 13, " ");
                printData += "\n";
                printData += "TOTAL:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(finalMaterialDataModels.get(i).getTotal()), length - 6, " ");
                printData += "\n\n";
            }


            printData += new PaddingString().padLeft("", length, "-");
            printData += "TOTAL:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(grandTotal), length - 6, " ");
            printData += new PaddingString().padLeft("", length, "-");
            printData += "BASIC Amt:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(basicAmount), length - 10, " ");
            printData += "CGST Amt:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(grandCgstAmount), length - 9, " ");
            printData += "SGST Amt:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(grandSgstAmount), length - 9, " ");
            printData += "Gr TOTAL:" + new PaddingString().padLeft(new DecimalFormat("0.00").format(grandTotal), length - 9, " ");
            printData += new PaddingString().padLeft("", length, "-");
            printData += "\n\n";
            printData += "To Whom So It may Concern.The use of pesticide/Fertilizer is not in our control, If any mishappening or mischief is occured, That is the sole Responsibility of Purchaser";
            printData += "\n";
            printData += new PaddingString().padLeft("", length, "-");
            printData += "\n\n";
            printData += "CLERK:" + new PaddingString().padLeft(userDetailsModelList.get(0).getUserId(), length - 6, " ");
            printData += "" + new PaddingString().padBoth(userDetailsModelList.get(0).getName(), length, " ");
            printData += "\n";
            printData += new PaddingString().padLeft("", length, "=");
            printData += new PaddingString().padBoth("E. & O.E.", length, " ");
            printData += "\n\n";
            printData += "\n\n";
            new FileUtility().PrintAtBTPrinter(context, printData, offlineControlModelList.get(0).getPrintAddress());
            auto_Village_code.setText("");
            cgst = 0.00;
            sgst = 0.00;
            cgstText.setText("");
            sgstText.setText("");
            resetForm();
        } catch (Exception e) {
            Log.d("", "Error : " + e.toString());
        }
    }

    private class GetVillageList extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            message = getString(R.string.MSG_PLEASE_WAIT);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_VillageMaster);
                request1.addProperty("Userid", userDetailsModelList.get(0).getCode());
                request1.addProperty("ZoneCode", userDetailsModelList.get(0).getZoneCode());
                request1.addProperty("Divn", userDetailsModelList.get(0).getDivision());
                request1.addProperty("IMEI", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Version", BuildConfig.VERSION_CODE);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_VillageMaster, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("VillageMasterResult").toString();
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
            if (dialog.isShowing()) dialog.dismiss();
            try {
                if (villageModalList.size() > 0) villageModalList.clear();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("VILLAGE");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        VillageModal modal = new VillageModal();
                        modal.setCode(jsonObject1.getString("VILLCD"));
                        modal.setName(jsonObject1.getString("VILLNM"));
                        villageModalList.add(modal);
                    }
                    initVillageSearch();
                    resetForm();

                } else {
                    new AlertDialogManager().RedDialog(context, "Error : " + jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            }
        }
    }

    private class GetGrowerList extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            message = getString(R.string.MSG_PLEASE_WAIT);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GrowerMaster);
                request1.addProperty("Userid", userDetailsModelList.get(0).getCode());
                request1.addProperty("ZoneCode", userDetailsModelList.get(0).getZoneCode());
                request1.addProperty("village", params[0]);
                request1.addProperty("Divn", userDetailsModelList.get(0).getDivision());
                request1.addProperty("IMEI", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Version", BuildConfig.VERSION_CODE);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GrowerMaster, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GrowerMasterResult").toString();
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
            if (dialog.isShowing()) dialog.dismiss();
            try {
                if (growerModelList.size() > 0) growerModelList.clear();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("GROWER");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        GrowerModel modal = new GrowerModel();
                        modal.setVillageCode(jsonObject1.getString("VILL"));
                        modal.setVillegename(jsonObject1.getString("VILLAGENAME"));
                        modal.setGrowerCode(jsonObject1.getString("GCODE"));
                        modal.setGrowerName(jsonObject1.getString("GNAME"));
                        modal.setGrowerFather(jsonObject1.getString("GFATHER"));
                        //modal.setG_lock(jsonObject1.getString("G_LOCK"));
                        //modal.setResonname(jsonObject1.getString("REASONNAME"));
                        modal.setDueamount(jsonObject1.getString("DUEAMOUNT"));
                        growerModelList.add(modal);
                    }
                    initGrowerSearch();
                    new GetItemList().execute();
                    resetForm();
                    //payment_types.setText("Balance Loan Limit : "+jsonObject.getString("CREDIT"));
                    //loan_type_spinner.setSelection(0);
                } else {
                    new AlertDialogManager().RedDialog(context, "Error : " + jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.getMessage());
            }
        }
    }

    //----------------------------end---------------------------------------------------------------

    private class GetItemList extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            message = getString(R.string.MSG_PLEASE_WAIT);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_ItemMaster);
                request1.addProperty("Userid", userDetailsModelList.get(0).getCode());
                request1.addProperty("ZoneCode", userDetailsModelList.get(0).getZoneCode());
                request1.addProperty("IMEI", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Version", BuildConfig.VERSION_CODE);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_ItemMaster, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("ItemMasterResult").toString();
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
            if (dialog.isShowing()) dialog.dismiss();
            try {
                if (addMaterialDataModels.size() > 0) addMaterialDataModels.clear();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("ITEM");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ItemModel modal = new ItemModel();
                        modal.setIt_code(jsonObject1.getString("IT_CODE"));
                        modal.setIt_name(jsonObject1.getString("IT_DESC"));
                        modal.setUom(jsonObject1.getString("UOM"));
                        modal.setSubsidy(jsonObject1.getString("SUBSIDY"));
                        modal.setCgst(jsonObject1.getDouble("CGSTRATE"));
                        modal.setSgst(jsonObject1.getDouble("SGSTRATE"));
                        modal.setHsnno(jsonObject1.getString("HSNNO"));
                        modal.setAmount(jsonObject1.getDouble("SALERATE"));
                        addMaterialDataModels.add(modal);

                    }
                    itemSearch();

                } else {
                    new AlertDialogManager().RedDialog(context, "Error : " + jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.getMessage());
            }
        }
    }

    private class GrowerDetails extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            message = getString(R.string.MSG_PLEASE_WAIT);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GrowerDetails);
                request1.addProperty("Userid", userDetailsModelList.get(0).getCode());
                request1.addProperty("ZoneCode", userDetailsModelList.get(0).getZoneCode());
                request1.addProperty("Village", params[0]);
                request1.addProperty("GrowerCode", params[1]);
                request1.addProperty("IMEI", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Version", BuildConfig.VERSION_CODE);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GrowerDetails, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GrowerDetailsResult").toString();
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
            if (dialog.isShowing()) dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    G_details.setVisibility(View.VISIBLE);
                    village_name.setText(jsonObject.getString("VILLAGECODE") + " / " + jsonObject.getString("VILLAGENAME"));
                    grower_name.setText(jsonObject.getString("GROWERCODE") + " / " + jsonObject.getString("GROWERNAME"));
                    grower_fn.setText(jsonObject.getString("GROWERFATHER"));
                    availed_loan_limit.setText(jsonObject.getString("BALANCE"));
                    balance_loan_limit.setText(jsonObject.getString("BALANCE"));
                    cheque.setText("");
                    auto_item.setText("");
                    quantity.setText("");
                    amount.setText("");
                    //loan_type_spinner.setSelection(0);
                    finalMaterialDataModels.clear();
                    ShowMaterialList();
                } else {
                    new AlertDialogManager().RedDialog(context, "Error : " + jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.getMessage());
            }
        }
    }


    //-------------------------------GetStockBalance------------------------------

    //------------------------------save the data---------------------------------------------------
    private class saveAgrilinput extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(DistributionActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SaveAgriDistribution);
                request1.addProperty("Userid", userDetailsModelList.get(0).getCode());
                request1.addProperty("zonecode", userDetailsModelList.get(0).getZoneCode());
                request1.addProperty("Village", auto_Village_code.getText().toString());
                request1.addProperty("GrowerCode", auto_Grower_code.getText().toString());
                request1.addProperty("PaymentType", payment_type.getSelectedItemPosition());
                request1.addProperty("PronoteNo", cheque.getText().toString().length() == 0 ? "0" : cheque.getText().toString());
                request1.addProperty("ItemData", params[0]);
                request1.addProperty("IMEI", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("Version", BuildConfig.VERSION_CODE);
                Log.d("Request Data", request1.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SaveAgriDistribution, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("SaveAgriDistributionResult").toString();
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    if (isPrintSlip) {
                        printData(
                                jsonObject.getString("InvoiceNo"),
                                jsonObject.getString("ReceiptDate"),
                                jsonObject.getString("ReceiptTime"));
                    } else {
                        auto_Village_code.setText("");
                        cgst = 0.00;
                        sgst = 0.00;
                        cgstText.setText("");
                        sgstText.setText("");
                        resetForm();
                    }
                   /* {
                      "API_STATUS": "OK",
                      "InvoiceNo": "11",
                      "ReceiptDate": "2023-04-26",
                      "ReceiptTime": "16:22",
                      "MSG": "Data Saved Successfully "
                    }*/
                    new AlertDialogManager().showToast(DistributionActivity.this, jsonObject.getString("MSG"));
                } else {
                    new AlertDialogManager().RedDialog(DistributionActivity.this, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(DistributionActivity.this, "Error:" + e.toString());
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(DistributionActivity.this, "Error:" + e.toString());
            }
        }


    }

    private class GetStockBalance extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(DistributionActivity.this);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetStockBalance);

                // request1.addProperty("Userid", userDetailsModelList.get(0).getUserId());
                ItemModel itemModel = new ItemModel();

                //request1.addProperty("SocCode", userDetailsModelList.get(0).getSduUnit());
                //request1.addProperty("StoreCode", userDetailsModelList.get(0).getSduStore());
                request1.addProperty("supvcode", userDetailsModelList.get(0).getCode());
                request1.addProperty("ItemCode", params[0]);
                request1.addProperty("Qty", params[1]);

                Log.d("Request Data", request1.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_AGRI_DISTRIBUTION, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetStockBalance, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetStockBalanceResult").toString();
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) dialog.dismiss();
            try {
                grandTotal = 0;
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {

                    for (int k = 0; k < finalMaterialDataModels.size(); k++) {
                        grandTotal += finalMaterialDataModels.get(k).getTotal();
                    }
                    String itemCode = auto_item.getText().toString().split(" - ")[0];
                    String itemName = auto_item.getText().toString().split(" - ")[1];
                    double rateStr = Double.parseDouble(amount.getText().toString());
                    double qtyStr = Double.parseDouble(quantity.getText().toString());
                    double amountStr = rateStr * qtyStr;
                    double cgstAmountStr = (cgst * amountStr) / 100;
                    double sgstAmountStr = (sgst * amountStr) / 100;
                    double totalStr = amountStr + cgstAmountStr + sgstAmountStr;
                    grandTotal += totalStr;
                    if (payment_type.getSelectedItemPosition() == 2) {
                        if (balance_loan_limit.getText().toString().length() == 0) {

                            new AlertDialogManager().showToast((Activity) context, "Please select grower details");
                            return;
                        }
                        if (Double.parseDouble(balance_loan_limit.getText().toString()) < grandTotal) {
                            new AlertDialogManager().RedDialog(context, "Sorry you dont have a sufficient loan limit \nBalance Loan Limit : " + balance_loan_limit.getText().toString() + "\nPurchase Amount : " + new DecimalFormat("0.00").format(grandTotal));
                            return;
                        }
                    }
                    ItemModel newDataMaterial = new ItemModel();
                    newDataMaterial.setHsnno("" + tempHsn);
                    newDataMaterial.setIt_name("" + itemName);
                    newDataMaterial.setIt_code("" + itemCode);
                    newDataMaterial.setRate(rateStr);
                    newDataMaterial.setAmount(amountStr);
                    newDataMaterial.setQty(qtyStr);
                    newDataMaterial.setCgst(cgst);
                    newDataMaterial.setCgstAmount(cgstAmountStr);
                    newDataMaterial.setSgst(sgst);
                    newDataMaterial.setSgstAmount(sgstAmountStr);
                    newDataMaterial.setTotal(totalStr);

                    if (finalMaterialDataModels.contains(newDataMaterial)) {
                        new AlertDialogManager().showToast(DistributionActivity.this, itemName + " (" + itemCode + ") already exists in your cart");

                    } else {
                        if (finalMaterialDataModels.size() > 0) {
                            //int index = finalMaterialDataModels.size() - 1;
                            //finalMaterialDataModels.remove(index);
                        }
                        finalMaterialDataModels.add(newDataMaterial);
                        auto_item.setText("");
                        amount.setText("");
                        quantity.setText("");
                        cgstText.setText("");
                        sgstText.setText("");
                        new AlertDialogManager().showToast((Activity) context, itemName + " (" + itemCode + ") successfully added in cart");
                        //totalList();
                    }
                    ShowMaterialList();
                } else {
                    new AlertDialogManager().RedDialog(DistributionActivity.this, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(DistributionActivity.this, "Error:" + e.toString());
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(DistributionActivity.this, "Error:" + e.toString());
            }
        }


    }


}