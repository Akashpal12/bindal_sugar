package in.co.vibrant.bindalsugar.view.supervisor.AgriInput;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GrowerSearchListAdapter;
import in.co.vibrant.bindalsugar.adapter.ListFormDataAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageSearchListAdapter;
import in.co.vibrant.bindalsugar.model.AddMaterialDataModel;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.UtilFile;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    //NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;
    private DBHelper db;
    ListFormDataAdapter listFormDataAdapter;
    AgriInputFormModel agriInputFormModel;
    private List<AgriInputFormModel> agriInputFormModelArray;
    RecyclerView recyclerView;
    ImageView filterImg;
    AlertDialog dialog;
    private List<VillageModal> villageMasterModels = new ArrayList<>();
    private List<GrowerModel> growerMasterModels = new ArrayList<>();
    VillageSearchListAdapter villageSearchListAdapter = null;
    GrowerSearchListAdapter gAdaper=null;
    String FromDateStr="",ToDateStr="",VillageCodeStr="",GrowerCodeStr="",VillageCodeMaster="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        UtilFile.hideKeyboard(ReportActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Agri Input Report");
        toolbar.setTitle("Agri Input Report");
        db = new DBHelper(this);
        try {
            if (db != null) {
                villageMasterModels.clear();
                growerMasterModels.clear();
                villageMasterModels.addAll(db.getVillageModal(""));
                growerMasterModels.addAll(db.getGrowerModel("",""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        agriInputFormModelArray=db.getAllAgriInputData();
        listFormDataAdapter=new ListFormDataAdapter(ReportActivity.this,agriInputFormModelArray);
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listFormDataAdapter);
        filterImg=findViewById(R.id.filterImg);
        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(ReportActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_report_filer, null);
                    dialogbilder.setView(mView);
                    final EditText tv_date=mView.findViewById(R.id.tv_date);
                    final EditText tv_to_date=mView.findViewById(R.id.tv_to_date);
                    final AutoCompleteTextView tv_village_code=mView.findViewById(R.id.tv_village_code);
                    final AutoCompleteTextView tv_grower_name=mView.findViewById(R.id.tv_grower_name);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);
                    if(FromDateStr.length()==0)
                    {
                        tv_date.setText(currentDt);
                    }
                    else
                    {
                        tv_date.setText(FromDateStr);
                    }
                    if(ToDateStr.length()==0)
                    {
                        tv_to_date.setText(currentDt);
                    }
                    else
                    {
                        tv_to_date.setText(ToDateStr);
                    }
                    if(VillageCodeMaster.length()>0)
                    {
                        growerMasterModels.clear();
                        growerMasterModels.addAll(db.getGrowerMasterVillageWiseAllData(VillageCodeMaster));
                        gAdaper = new GrowerSearchListAdapter(ReportActivity.this, R.layout.all_list_row_item_search_grower, growerMasterModels);
                        tv_grower_name.setThreshold(1);
                        tv_grower_name.setAdapter(gAdaper);
                    }
                    tv_village_code.setText(VillageCodeStr);
                    tv_grower_name.setText(GrowerCodeStr);
                    final TextView err_msg=mView.findViewById(R.id.err_msg);
                    tv_date.setInputType(InputType.TYPE_NULL);
                    tv_date.setTextIsSelectable(true);
                    tv_date.setFocusable(false);
                    String mVillageCodeSalected="";
                    tv_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            // Launch Date Picker Dialog
                            DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this,new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    String temDate =""+dayOfMonth;
                                    if(temDate.length()==1){
                                        temDate="0"+temDate;
                                    }
                                    String temmonth =""+(monthOfYear + 1);
                                    if(temmonth.length()==1){
                                        temmonth="0"+temmonth;
                                    }
                                    //tv_date_h.setText(year+"-"+  temmonth + "-"+ temDate);
                                    tv_date.setText(temDate+"-"+  temmonth + "-"+ year);

                                }
                            }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });
                    tv_to_date.setInputType(InputType.TYPE_NULL);
                    tv_to_date.setTextIsSelectable(true);
                    tv_to_date.setFocusable(false);
                    tv_to_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            // Launch Date Picker Dialog
                            DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this,new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    String temDate =""+dayOfMonth;
                                    if(temDate.length()==1){
                                        temDate="0"+temDate;
                                    }
                                    String temmonth =""+(monthOfYear + 1);
                                    if(temmonth.length()==1){
                                        temmonth="0"+temmonth;
                                    }
                                    //tv_to_date_h.setText(year+"-"+  temmonth + "-"+temDate );
                                    tv_to_date.setText(temDate+"-"+  temmonth + "-"+year );

                                }
                            }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });
                    villageMasterModels.addAll(db.getVillageModal(""));
                    villageSearchListAdapter = new VillageSearchListAdapter(ReportActivity.this, R.layout.all_list_row_item_search, villageMasterModels);
                    tv_village_code.setThreshold(1);
                    tv_village_code.setAdapter(villageSearchListAdapter);
                    tv_village_code.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    VillageModal mVillgModel = (VillageModal) parent.getItemAtPosition(position);
                                    tv_village_code.setText(mVillgModel.getName());
                                    VillageCodeMaster=mVillgModel.getCode();
                                    growerMasterModels.clear();
                                    growerMasterModels.addAll(db.getGrowerMasterVillageWiseAllData(mVillgModel.getCode()));
                                    gAdaper = new GrowerSearchListAdapter(ReportActivity.this, R.layout.all_list_row_item_search_grower, growerMasterModels);
                                    tv_grower_name.setThreshold(1);
                                    tv_grower_name.setAdapter(gAdaper);
                                    Log.d(TAG, "onItemClick: " + mVillgModel.getName());
                                }
                            });
                    tv_village_code.addTextChangedListener(new TextWatcher() {
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
                    tv_grower_name.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    GrowerModel mGrowerMaster = (GrowerModel) parent.getItemAtPosition(position);
                                    tv_grower_name.setText(mGrowerMaster.getGrowerCode());
                                }
                            });


                    tv_grower_name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(tv_village_code.getText().toString().length()>0)
                            {
                                gAdaper.getFilter().filter(s);
                            }
                            else
                            {
                                err_msg.setText("Please enter village name");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    dialog = dialogbilder.create();
                    dialog.show();
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    Button btn_ok=mView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                FromDateStr = "" + tv_date.getText().toString();
                                ToDateStr = "" + tv_to_date.getText().toString();
                                VillageCodeStr = "" + tv_village_code.getText().toString();
                                GrowerCodeStr = "" + tv_grower_name.getText().toString();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                if (FromDateStr.length() > 0) {
                                    if (ToDateStr.length() > 0) {
                                        dialog.dismiss();
                                        Date date = formatter.parse(FromDateStr);
                                        Date Todate = formatter.parse(ToDateStr);
                                        ApplyFilter(new SimpleDateFormat("yyyy-MM-dd").format(date), new SimpleDateFormat("yyyy-MM-dd").format(Todate),VillageCodeStr, GrowerCodeStr);
                                    } else {
                                        err_msg.setText("Please enter to date");
                                    }
                                } else {
                                    dialog.dismiss();
                                    ApplyFilter(FromDateStr, ToDateStr,VillageCodeStr, GrowerCodeStr);
                                }
                            }
                            catch(Exception e)
                            {

                            }
                        }
                    });
                    /*Button getting_weight_button_cancel=mView.findViewById(R.id.gross_weight_cancel);
                    getting_weight_button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });*/
                }
                catch (Exception e)
                {
                    Log.d("",e.toString());
                }
            }
        });
    }

    private void ApplyFilter(String date,String toDateSTr,String VillageCode,String GrowerName)
    {
        agriInputFormModelArray.clear();
        agriInputFormModelArray=db.getFilterAgriInputData(date,toDateSTr,VillageCode,GrowerName);
        listFormDataAdapter=new ListFormDataAdapter(ReportActivity.this,agriInputFormModelArray);
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listFormDataAdapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void ShowMaterialList()
    {
        List<AddMaterialDataModel> arrayList=db.getAllAgriInputDataMaterial();
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_booking);//Intent intent = new Intent(Login.this, MainActivity.class);
        ll.removeAllViewsInLayout();
        TableLayout dialogueTable = new TableLayout(ReportActivity.this);
        dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogueTable.setShrinkAllColumns(true);
        dialogueTable.setStretchAllColumns(true);
        TableRow tableHeading=new TableRow(ReportActivity.this);
        TextView th1 = new TextView(ReportActivity.this);
        TextView th2 = new TextView(ReportActivity.this);
        TextView th3 = new TextView(ReportActivity.this);
        TextView th4= new TextView(ReportActivity.this);
        TextView th5= new TextView(ReportActivity.this);
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
        tableHeading.setBackgroundResource(R.drawable.table_row_bg);
        //tableHeading.addView(th1);
        tableHeading.addView(th1);
        tableHeading.addView(th2);
        tableHeading.addView(th3);
        tableHeading.addView(th4);
        tableHeading.addView(th5);
        dialogueTable.addView(tableHeading);
        for(int j=0; j<arrayList.size(); j++) {
            TableRow row = new TableRow(ReportActivity.this);
            row.setId(j);
            TextView tv1 = new TextView(ReportActivity.this);
            tv1.setText(arrayList.get(j).getMaterialName());
            tv1.setWidth(70);
            tv1.setGravity(Gravity.LEFT);
            TextView tv2 = new TextView(ReportActivity.this);
            tv2.setText(arrayList.get(j).getMaterialQuantity());
            //tv2.setWidth(50);
            tv2.setGravity(Gravity.CENTER);
            TextView tv3 = new TextView(ReportActivity.this);
            tv3.setText(arrayList.get(j).getMaterialRate());
            //tv3.setWidth(50);
            tv3.setGravity(Gravity.CENTER);
            TextView tv4 = new TextView(ReportActivity.this);
            tv4.setText(arrayList.get(j).getMatTotalAmount());
            //tv4.setWidth(50);
            tv4.setGravity(Gravity.CENTER);
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            row.addView(tv4);
            dialogueTable.addView(row);
        }
        ll.addView(dialogueTable);
    }




}
