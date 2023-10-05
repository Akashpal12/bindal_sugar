package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.BluetoothPrint.BluetoothChat;
import in.co.vibrant.bindalsugar.BluetoothPrint.ESC;
import in.co.vibrant.bindalsugar.BluetoothPrint.Variables;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.AddShareListAdapter;
import in.co.vibrant.bindalsugar.adapter.FarmerAutoSearchAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageAutoSearchAdapter;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;


public class StaffAddGrowerShare extends AppCompatActivity {

    String TAG="StaffAddGrowerShare";


    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> userDetailsModels;
    List<ControlSurveyModel> controlSurveyModelList;
    int i=0;
    ScrollView scrl;
    List<EditText> allEds = new ArrayList<EditText>();
    String StrSurveyId="";
    AutoCompleteTextView village_code,grower_code;
    EditText grower_name,grower_father,aadhar_number,share_per;
    List<FarmerShareModel> farmerShareModelList,farmerShareModelList1;
    TextView t_share_per;
    int mimimumShare=100;
    PlotSurveyModel uploadPlotSurveyModel;
    FarmerShareModel uploadFarmerShareModel;
    List<VillageSurveyModel> villageSurveyModelList;
    List<FarmerModel> farmerModelList;

    RelativeLayout rl_aadhar_number;
    VillageAutoSearchAdapter villageAutoSearchAdapter;
    FarmerAutoSearchAdapter farmerAutoSearchAdapter;
    String growerCode="",villageCode="";
    String dbgg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_potato_add_grower_share);

        try {
            dbh = new DBHelper(this);
            db = new DBHelper(this).getWritableDatabase();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getString(R.string.MENU_ADD_GROWER_SHARE));
            toolbar.setTitle(getString(R.string.MENU_ADD_GROWER_SHARE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            farmerModelList = new ArrayList<>();
            userDetailsModels = new ArrayList<>();
            dbgg +="1,";
            userDetailsModels = dbh.getUserDetailsModel();
            controlSurveyModelList = dbh.getControlSurveyModel("");
            villageSurveyModelList = dbh.getVillageModel("");
            StrSurveyId = getIntent().getExtras().getString("survey_id");
            rl_aadhar_number = findViewById(R.id.rl_aadhar_number);
            t_share_per = findViewById(R.id.t_share_per);
            village_code = findViewById(R.id.village_code);
            grower_code = findViewById(R.id.grower_code);
            grower_name = findViewById(R.id.grower_name);
            grower_father = findViewById(R.id.grower_father);
            aadhar_number = findViewById(R.id.aadhar_number);
            share_per = findViewById(R.id.share_per);

            if (controlSurveyModelList.get(0).getAadharNo().equalsIgnoreCase("1")) {
                rl_aadhar_number.setVisibility(View.VISIBLE);
            }
            init();

            grower_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (grower_code.getText().length() == 0) {
                        grower_name.setText("");
                        grower_father.setText("");
                    } else {
                        if(Integer.parseInt(grower_code.getText().toString())>0)
                        {
                            List<FarmerModel> farmerModelList = dbh.getFarmerWithVillageModel(village_code.getText().toString(), grower_code.getText().toString());
                            if (farmerModelList.size() == 0) {
                                grower_name.setText("");
                                grower_father.setText("");
                            } else {
                                grower_name.setText(farmerModelList.get(0).getFarmerName());
                                grower_father.setText(farmerModelList.get(0).getFatherName());
                            }
                        }

                    }

                }
            });
        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }


    private void printData(String insertId)
    {
        try {
            List<VillageSurveyModel> villageSurveyModelListprint = dbh.getVillageModel(uploadPlotSurveyModel.getVillageCode());
            List<MasterCaneSurveyModel> masterVarietyModelPrint = dbh.getMasterModelById("1", uploadPlotSurveyModel.getVarietyCode());
            List<MasterCaneSurveyModel> masterCaneTypeModelPrint = dbh.getMasterModelById("2", uploadPlotSurveyModel.getCaneType());
            List<FarmerModel> farmerDetails = dbh.getFarmerModel(uploadFarmerShareModel.getGrowerCode());
            //PrinterApi printerap = new PrinterApi();
            String printData = "SEASON: "+getString(R.string.season)+"\n";
            printData += "S.No. :" + uploadPlotSurveyModel.getPlotSrNo() + "\n";
            printData += "Survey Village : " + villageSurveyModelListprint.get(0).getVillageName() + "\n";
            printData += "========================\n";
            printData += "Plot Village: " + uploadPlotSurveyModel.getVillageCode() + "\n";
            printData += "Plot Sr No.:  " + uploadPlotSurveyModel.getPlotSrNo() + "\n";
            printData += "UniqueCode:" + farmerDetails.get(0).getUniqueCode() + "\n";
            printData += "Name : " + uploadFarmerShareModel.getGrowerName() + "\n";
            printData += "Fath : " + uploadFarmerShareModel.getGrowerFatherName() + "\n";
            printData += "Vill : " + villageSurveyModelListprint.get(0).getVillageName() + "\n";
            printData += "East : " + uploadPlotSurveyModel.getEastSouthDistance() + "   West : " + uploadPlotSurveyModel.getWestNorthDistance() + "\n";
            printData += "North: " + uploadPlotSurveyModel.getEastNorthDistance() + "   South: " + uploadPlotSurveyModel.getWestSouthDistance() + "\n";
            printData += "Plot Share : " + uploadFarmerShareModel.getShare() + " : 100%\n";
            printData += "Variety: " + masterVarietyModelPrint.get(0).getName() + "\n";
            printData += "Cane Area : "+uploadPlotSurveyModel.getAreaHectare()+"\n";
            printData += "========================\n";
            printData += "\n";
            printData += "\n";
            //Bluetooth_Printer_2inch_Impact printapi = new Bluetooth_Printer_2inch_Impact();
            //	printerap.setPrintCommand(printapi.font_Double_Height_Width_Off());
            int i = 1;
            //while (true) {
            try {
                Intent intent = new Intent(StaffAddGrowerShare.this, BluetoothChat.class);
                intent.putExtra(Variables.PrintString, ESC.RESETPRINTER+ESC.NEWLINE+printData);
                intent.putExtra("printData", printData);
                startActivity(intent);
                //Thread.sleep(1000);
                /*printerap.printData(printData);
                Thread.sleep(15000);
                i++;*/
                AlertPopUp("Data saved successfully");
                init();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }

    private void init()
    {
        villageSurveyModelList =dbh.getVillageModel("");
        if(villageSurveyModelList.size()>0) {
            villageAutoSearchAdapter = new VillageAutoSearchAdapter(this, R.layout.all_list_row_item_search, villageSurveyModelList);
            village_code.setThreshold(1);
            village_code.setAdapter(villageAutoSearchAdapter);
            village_code.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            VillageSurveyModel purcheyDataModel = (VillageSurveyModel) parent.getItemAtPosition(position);
                            //mVillageCodeSalected = mVillgModel.getVillage_code();
                            //mVillageNameSalected = mVillgModel.getVillage_name();
                            village_code.setText(purcheyDataModel.getVillageCode());
                            farmerModelList = dbh.getFarmerWithVillageModel(purcheyDataModel.getVillageCode(),"");
                            farmerAutoSearchAdapter = new FarmerAutoSearchAdapter(StaffAddGrowerShare.this, R.layout.all_list_row_item_search, farmerModelList);
                            grower_code.setThreshold(1);
                            grower_code.setAdapter(farmerAutoSearchAdapter);
                            grower_code.requestFocus();
                            villageCode=purcheyDataModel.getVillageCode();
                            //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                        }
                    });
            village_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    villageSurveyModelList.clear();
                    villageSurveyModelList = dbh.getVillageModel("");
                    villageAutoSearchAdapter.getFilter().filter(s);
                }
            });
        }


        farmerAutoSearchAdapter = new FarmerAutoSearchAdapter(StaffAddGrowerShare.this, R.layout.all_list_row_item_search, farmerModelList);
        grower_code.setThreshold(1);
        grower_code.setAdapter(farmerAutoSearchAdapter);
        grower_code.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FarmerModel purcheyDataModel = (FarmerModel) parent.getItemAtPosition(position);
                        //mVillageCodeSalected = mVillgModel.getVillage_code();
                        //mVillageNameSalected = mVillgModel.getVillage_name();
                        grower_code.setText(purcheyDataModel.getFarmerCode());
                        grower_name.setText(purcheyDataModel.getFarmerName());
                        grower_father.setText(purcheyDataModel.getFatherName());
                        aadhar_number.requestFocus();
                        growerCode=purcheyDataModel.getFarmerCode();
                        //tv_VillageCode.setText(mVillgModel.getVillage_code() + "-" + mVillgModel.getVillage_name());
                    }
                });
        grower_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                farmerModelList.clear();
                farmerModelList = dbh.getFarmerWithVillageModel(village_code.getText().toString(),"");
                farmerAutoSearchAdapter.getFilter().filter(s);
            }
        });
        dbgg +=",8,";
        uploadPlotSurveyModel=dbh.getPlotSurveyModelById(StrSurveyId).get(0);
        dbgg +=",9,";
        village_code.setText("");
        grower_code.setText("");
        grower_name.setText("");
        grower_father.setText("");
        aadhar_number.setText("");
        share_per.setText("");
        village_code.requestFocus();
        farmerShareModelList=dbh.getFarmerShareModel(StrSurveyId);
        dbgg +=",10,";
        farmerShareModelList1=new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        FarmerShareModel header = new FarmerShareModel();
        header.setVillageCode("Village");
        header.setSrNo("Sr No");
        header.setSurveyId("Survey No");
        header.setGrowerCode("Grower");
        header.setShare("Share %");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        farmerShareModelList1.add(header);
        int totalShare=dbh.getTotalShareUsed(StrSurveyId);
        for(int i=0;i<farmerShareModelList.size();i++)
        {
            List<PlotSurveyModel> plotSurveyModelList1=dbh.getPlotSurveyModelById(farmerShareModelList.get(i).getSurveyId());
            dbgg +=",11,";
            FarmerShareModel saleModel=new FarmerShareModel();
            saleModel.setVillageCode(farmerShareModelList.get(i).getVillageCode());
            saleModel.setSrNo(farmerShareModelList.get(i).getSrNo());
            saleModel.setSurveyId(plotSurveyModelList1.get(0).getPlotSrNo());
            saleModel.setGrowerCode(farmerShareModelList.get(i).getGrowerCode());
            saleModel.setShare(farmerShareModelList.get(i).getShare());
            //totalShare += Integer.parseInt(farmerShareModelList.get(i).getShare());
            if(i%2==0)
            {
                saleModel.setColor("#DFDFDF");
                saleModel.setTextColor("#000000");
            }
            else
            {
                saleModel.setColor("#FFFFFF");
                saleModel.setTextColor("#000000");
            }
            farmerShareModelList1.add(saleModel);
        }
        mimimumShare=100-totalShare;
        if(mimimumShare==0)
        {
            Intent intent=new Intent(StaffAddGrowerShare.this,StaffNewPlotEntry.class);
            finish();
            startActivity(intent);
        }
        t_share_per.setText("Share % (Maximum Share "+mimimumShare+" %)");
        /*SaleModel footer = new SaleModel();
        footer.setId("Total");
        footer.setName("");
        footer.setTotalAmount(""+decimalFormat.format(total));
        footer.setPayAmount(""+decimalFormat.format(paid));
        footer.setColor("#000000");
        footer.setTextColor("#FFFFFF");
        saleModelList.add(footer);*/
        RecyclerView recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(StaffAddGrowerShare.this, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        AddShareListAdapter stockSummeryAdapter =new AddShareListAdapter(StaffAddGrowerShare.this,farmerShareModelList1);
        recyclerView.setAdapter(stockSummeryAdapter);
    }

    private void addDynamic(LinearLayout ll)
    {

        TextView tvshare=new TextView(getApplicationContext());
        tvshare.setText("-:-  Share "+i+" %  -:-");
        tvshare.setGravity(Gravity.CENTER);
        tvshare.setTextSize(25);
        ll.addView(tvshare);
        TextView tv=new TextView(getApplicationContext());
        tv.setText("Village Code "+i);
        ll.addView(tv);
        EditText et=new EditText(getApplicationContext());
        et.setId(1000+i);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        ll.addView(et);
        TextView tv1=new TextView(getApplicationContext());
        tv1.setText("Grower Code "+i);
        ll.addView(tv1);
        final EditText et1=new EditText(getApplicationContext());
        et1.setId(2000+i);
        et1.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66)
                {
                    List<FarmerModel> farmerModelList=dbh.getFarmerModel(et1.getText().toString());
                    if(farmerModelList.size()==0)
                    {
                        AlertPopUp("Invalid grower code");
                    }
                    else
                    {
                        AlertPopUp("Grower Name "+farmerModelList.get(0).getFarmerName()+"\nFather Name :"+farmerModelList.get(0).getFatherName());
                    }
                }
                return false;
            }
        });
        //et1.setHint("Enter Grower Code");
        ll.addView(et1);
        TextView tv2=new TextView(getApplicationContext());
        tv2.setText("Grower Name "+i);
        ll.addView(tv2);
        EditText et2=new EditText(getApplicationContext());
        et2.setId(3000+i);
        et2.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        ll.addView(et2);

        TextView tv3=new TextView(getApplicationContext());
        tv2.setText("Share Percentage (%) "+i);
        ll.addView(tv2);
        EditText et3=new EditText(getApplicationContext());
        et2.setId(3000+i);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView tv4=new TextView(getApplicationContext());
        tv2.setText("Share Percentage (%) "+i);
        ll.addView(tv4);
        EditText et5=new EditText(getApplicationContext());
        et2.setId(3000+i);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);
        //et2.setHint("Enter Grower Code");
        ll.addView(et2);
        allEds.add(et);
        allEds.add(et1);
        allEds.add(et2);
        et2.requestFocus();
        et.requestFocus();
    }

    public void ExitBtn(View v)
    {
        Intent intent=new Intent(StaffAddGrowerShare.this,StaffNewPlotEntry.class);
        finish();
        startActivity(intent);
    }

    public void saveData(View v)
    {
        CheckValidation :{
            if(village_code.getText().toString().length()==0)
            {
                AlertPopUp("Please enter village code");
                break CheckValidation;
            }
            if(grower_code.getText().toString().length()==0)
            {
                AlertPopUp("Please enter grower code");
                break CheckValidation;
            }
            if(grower_name.getText().toString().length()==0)
            {
                AlertPopUp("Please enter grower name");
                break CheckValidation;
            }
            if(grower_father.getText().toString().length()==0)
            {
                AlertPopUp("Please enter grower father name");
                break CheckValidation;
            }
            if(share_per.getText().toString().length()==0)
            {
                AlertPopUp("Please enter grower share %");
                break CheckValidation;
            }
            if(Integer.parseInt(share_per.getText().toString())>mimimumShare)
            {
                AlertPopUp("Please enter grower share % minimum "+mimimumShare);
                break CheckValidation;
            }
            if (controlSurveyModelList.get(0).getAadharNo().equalsIgnoreCase("1")) {
                if (aadhar_number.getText().toString().length() != 12) {
                    AlertPopUp("Enter 12 digit aadhar card number");
                    aadhar_number.requestFocus();
                    break CheckValidation;
                }
            }
        }
        savesurveyshare();
    }

    private void savesurveyshare()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currDate=dateFormat.format(today);
        uploadFarmerShareModel = new FarmerShareModel();
        uploadFarmerShareModel.setSurveyId(StrSurveyId);
        uploadFarmerShareModel.setSrNo(""+(farmerShareModelList.size()+1));
        uploadFarmerShareModel.setVillageCode(villageCode);
        uploadFarmerShareModel.setGrowerCode(growerCode);
        uploadFarmerShareModel.setGrowerName(grower_name.getText().toString());
        uploadFarmerShareModel.setGrowerFatherName(grower_father.getText().toString());
        uploadFarmerShareModel.setGrowerAadharNumber(aadhar_number.getText().toString());
        uploadFarmerShareModel.setShare(share_per.getText().toString());
        uploadFarmerShareModel.setSupCode(userDetailsModels.get(0).getCode());
        uploadFarmerShareModel.setCurDate(currDate);
        long insertId=dbh.insertFarmerShareModel(uploadFarmerShareModel);
        printDialogue(""+insertId);
    }


    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffAddGrowerShare.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void printDialogue(final String insertId)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffAddGrowerShare.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Would you like to print slip");
        alertDialog.setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        /*if(new InternetCheck(StaffAddGrowerShare.this).isOnline())
                        {
                            new UploadSurvey().execute(""+insertId);
                        }
                        else
                        {

                        }*/
                        AlertPopUp("Data saved successfully");
                        init();
                    }
                });
        alertDialog.setNegativeButton("Print",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        /*if(new InternetCheck(StaffAddGrowerShare.this).isOnline())
                        {
                            new UploadSurvey().execute(""+insertId);
                        }*/
                        printData(insertId);
                    }
                });
        alertDialog.show();
    }

    private void AlertPopUpWithFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StaffAddGrowerShare.this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }


    /*private class UploadSurvey extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(StaffAddGrowerShare.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StaffAddGrowerShare.this, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei((StaffAddGrowerShare.this)).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_UpLoadSurvey);
                //request1.addProperty("EMEI", "864547042872734");
                //request1.addProperty("imeiNo", "355844090708282");
                request1.addProperty("FactId", factoryModelList.get(0).getFactoryCode());
                request1.addProperty("supvcd", uploadFarmerShareModel.getSupCode());
                request1.addProperty("survdttm", uploadFarmerShareModel.getCurDate());
                request1.addProperty("survvillcd", uploadPlotSurveyModel.getVillageCode());
                request1.addProperty("plotsrno", uploadPlotSurveyModel.getPlotSrNo());
                request1.addProperty("grwvillcd", uploadFarmerShareModel.getVillageCode());
                request1.addProperty("farmcd", uploadFarmerShareModel.getGrowerCode());
                request1.addProperty("isgpspts", "0");
                request1.addProperty("north", uploadPlotSurveyModel.getEastNorthDistance());
                request1.addProperty("east", uploadPlotSurveyModel.getEastSouthDistance());
                request1.addProperty("south", uploadPlotSurveyModel.getWestSouthDistance());
                request1.addProperty("west", uploadPlotSurveyModel.getWestNorthDistance());
                request1.addProperty("plotshareperc", uploadFarmerShareModel.getShare());
                request1.addProperty("shareno", uploadFarmerShareModel.getSrNo());
                request1.addProperty("vrtcd", uploadPlotSurveyModel.getVarietyCode());
                request1.addProperty("canetycd", uploadPlotSurveyModel.getCaneType());
                request1.addProperty("mobno", "");
                request1.addProperty("remark", params[0]);
                request1.addProperty("gastino", uploadPlotSurveyModel.getGhashtiNumber());
                request1.addProperty("farmnm", uploadFarmerShareModel.getGrowerName());
                request1.addProperty("fathnm", uploadFarmerShareModel.getGrowerFatherName());
                request1.addProperty("aadharno", uploadFarmerShareModel.getGrowerAadharNumber());
                request1.addProperty("area", uploadPlotSurveyModel.getAreaHectare());
                request1.addProperty("plntmthcd", uploadPlotSurveyModel.getPlantMethod());
                request1.addProperty("cropcondcd", uploadPlotSurveyModel.getCropCondition());
                request1.addProperty("disecd", uploadPlotSurveyModel.getDisease());
                request1.addProperty("socclerkcd", uploadFarmerShareModel.getSupCode());
                request1.addProperty("plantationdt", uploadPlotSurveyModel.getPlantDate());
                request1.addProperty("irrcd", uploadPlotSurveyModel.getIrrigation());
                request1.addProperty("soilcd", uploadPlotSurveyModel.getSoilType());
                request1.addProperty("landcd", uploadPlotSurveyModel.getLandType());
                request1.addProperty("bordercropcd", uploadPlotSurveyModel.getBorderCrop());
                request1.addProperty("plottycd", uploadPlotSurveyModel.getPlotType());
                request1.addProperty("intercropcd", uploadPlotSurveyModel.getInterCrop());
                request1.addProperty("mixcropcd", uploadPlotSurveyModel.getMixCrop());
                request1.addProperty("insectcd", uploadPlotSurveyModel.getInsect());
                request1.addProperty("seedsourcecd", uploadPlotSurveyModel.getSeedSource());
                request1.addProperty("fort", "");
                request1.addProperty("isautumn", "0");
                request1.addProperty("sentsms", "0");
                request1.addProperty("isupdt", "0");
                request1.addProperty("nelatdeg",uploadPlotSurveyModel.getEastNorthLat());
                request1.addProperty("nelngdeg", uploadPlotSurveyModel.getEastNorthLng());
                request1.addProperty("selatdeg", uploadPlotSurveyModel.getEastSouthLat());
                request1.addProperty("selngdeg", uploadPlotSurveyModel.getEastSouthLng());
                request1.addProperty("swlatdeg", uploadPlotSurveyModel.getWestSouthLat());
                request1.addProperty("swlngdeg", uploadPlotSurveyModel.getWestSouthLng());
                request1.addProperty("nwlatdeg", uploadPlotSurveyModel.getWestNorthLat());
                request1.addProperty("nwlngdeg", uploadPlotSurveyModel.getWestNorthLng());
                request1.addProperty("KhasaraNo", uploadPlotSurveyModel.getKhashraNo());
                request1.addProperty("GataNo", uploadPlotSurveyModel.getGataNo());
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call

                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_UpLoadSurvey, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("UpLoadSurveyNewResult").toString();
                }

            } catch (SecurityException e) {
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d(TAG, message);
                    JSONObject jsonObject=new JSONObject(message);
                    dbh.updateServerFarmerShareModel(jsonObject.getString("REMARK"),jsonObject.getString("STATUS"),jsonObject.getString("MESSAGE"));
                    AlertPopUp(jsonObject.getString("MESSAGE"));
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                if (dialog.isShowing())
                    dialog.dismiss();
                //AlertPopUp(getString(R.string.technical_error));
            }
        }
    }*/







}
