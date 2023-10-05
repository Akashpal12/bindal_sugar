package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListSprayItemAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.SprayItemModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;


public class FieldStaffPlotSpray extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> loginUserDetailsList;
    List<SprayItemModel> sprayItemModelList;

    List<MasterDropDown> sprayItemList;

    EditText villageCode,villageName,growerCode,growerName,growerFatherName,plotVillageCode,plotVillageName,plotVillageNumber,
            growerPlotNumber,area,description,manualArea,mobileNumber,date,pronotNumber,sprayNumber;
    Spinner spray_type,spray,sprayItem,input_planting_number;
    TextInputLayout layoutvillageCode,layoutvillageName,layoutgrowerCode,layoutgrowerName,layoutgrowerFatherName,
            layoutplotVillageCode,layoutplotVillageName,layoutspray,layoutplotVillageNumber,layoutgrowerPlotNumber,layoutarea,
            layoutsprayItem,layoutmanualArea,layoutdescription,layoutmobileNumber,layoutdate,layoutPronotNo,layoutSprayNo;

    LocationManager locationManager;

    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;
    double lat,lng;
    String ID="";

    Context context;
    List<PlantingModel> plantingList;
    List<PlotFarmerShareOldModel> plotFarmerShareModelsList;


    InputFilter filter = new InputFilter() {
        final int maxDigitsBeforeDecimalPoint=1;
        final int maxDigitsAfterDecimalPoint=3;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source
                    .subSequence(start, end).toString());
            if (!builder.toString().matches(
                    "(([0-4]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

            )) {
                if(source.length()==0)
                    return dest.subSequence(dstart, dend);
                return "";
            }

            return null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot_spray);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_PLOT_SPRAY));
        toolbar.setTitle(getString(R.string.MENU_PLOT_SPRAY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        context=FieldStaffPlotSpray.this;

        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        dbh.deleteSprayItem("");
        loginUserDetailsList=new ArrayList<>();
        plotFarmerShareModelsList=new ArrayList<>();
        loginUserDetailsList=dbh.getUserDetailsModel();

        layoutvillageCode=findViewById(R.id.input_layout_village_code);
        layoutvillageName=findViewById(R.id.input_layout_village_name);
        layoutgrowerCode=findViewById(R.id.input_layout_grower_code);
        layoutgrowerName=findViewById(R.id.input_layout_grower_name);
        layoutgrowerFatherName=findViewById(R.id.input_layout_grower_father);
        layoutplotVillageCode=findViewById(R.id.input_layout_plot_village_code);
        layoutplotVillageNumber=findViewById(R.id.input_layout_plot_village_number);
        layoutgrowerPlotNumber=findViewById(R.id.input_layout_grower_plot_number);
        layoutarea=findViewById(R.id.input_layout_area);
        layoutsprayItem=findViewById(R.id.input_layout_spray_item);
        layoutmanualArea=findViewById(R.id.input_layout_manual_area);
        layoutdescription=findViewById(R.id.input_layout_description);
        layoutspray=findViewById(R.id.input_layout_spray);
        layoutmobileNumber=findViewById(R.id.input_layout_mobile_number);
        layoutdate=findViewById(R.id.input_layout_date);
        layoutPronotNo=findViewById(R.id.input_layout_pronotno);
        layoutSprayNo=findViewById(R.id.input_layout_sprayno);
        input_planting_number=findViewById(R.id.input_planting_number);



        villageCode=findViewById(R.id.input_village_code);
        villageName=findViewById(R.id.input_village_name);
        growerCode=findViewById(R.id.input_grower_code);
        growerName=findViewById(R.id.input_grower_name);
        growerFatherName=findViewById(R.id.input_grower_father);
        plotVillageCode=findViewById(R.id.input_plot_village_code);
        plotVillageName=findViewById(R.id.input_plot_village_name);
        plotVillageNumber=findViewById(R.id.input_plot_village_number);
        growerPlotNumber=findViewById(R.id.input_grower_plot_number);
        area=findViewById(R.id.input_area);
        manualArea=findViewById(R.id.input_manual_area);
        description=findViewById(R.id.input_description);
        mobileNumber=findViewById(R.id.input_mobile_number);
        pronotNumber=findViewById(R.id.input_pronotno);
        sprayNumber=findViewById(R.id.input_sprayno);
        date=findViewById(R.id.input_date);
        spray_type=findViewById(R.id.input_spray_type);
        manualArea.setFilters(new InputFilter[] { filter });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt=dateFormat.format(today);
        //paymentDate.setText(currentDt);
        date.setInputType(InputType.TYPE_NULL);
        date.setTextIsSelectable(true);
        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
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
                        date.setText(year+"-"+  temmonth + "-"+temDate );

                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        spray=findViewById(R.id.input_spray);
        sprayItem=findViewById(R.id.input_spray_item);

        image=findViewById(R.id.image);

        sprayItemList=new ArrayList<>();
        //new getMasterData().execute();

        //new getWarehouseList().execute();
        ArrayList arrayListSprayType=new ArrayList();
        arrayListSprayType.add("Spray Type");
        arrayListSprayType.add("Ratoon Management Spray");
        arrayListSprayType.add("Planting Spray");
        ArrayAdapter<String> adapterSprayType = new ArrayAdapter<String>(context,
                R.layout.list_item, arrayListSprayType);
        spray_type.setAdapter(adapterSprayType);
        spray_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetPlotSerial();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sprayItemModelList=new ArrayList();
        ArrayList arrayList=new ArrayList();
        arrayList.add("Select Spray");
        arrayList.add("Self");
        arrayList.add("Commpany");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, arrayList);
        spray.setAdapter(adapter);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        init();
    }
    public void exit(View v)
    {
        finish();
    }

    private void init()
    {
        villageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(villageCode.getText().toString().length()>0)
                {
                    //new getVillageData().execute(villageCode.getText().toString());
                    List<VillageModal> plotVillageModelList=dbh.getVillageModal(villageCode.getText().toString());
                    if(plotVillageModelList.size()>0)
                    {
                        villageCode.setText(plotVillageModelList.get(0).getCode());
                        villageName.setText(plotVillageModelList.get(0).getName());
                    }
                    else
                    {
                        villageCode.setText("");
                        villageName.setText("");
                        new AlertDialogManager().RedDialog(context,"Please enter correct village code");
                    }
                }
            }
        });
        growerCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (villageCode.getText().toString().length() > 0) {
                        if (growerCode.getText().toString().length() > 0) {
                            //new getGrowerData().execute(villageCode.getText().toString(),growerCode.getText().toString());
                            List<GrowerModel> growerModelList = dbh.getGrowerModel(villageCode.getText().toString(), growerCode.getText().toString());
                            if (growerModelList.size() > 0) {
                                growerCode.setText(growerModelList.get(0).getGrowerCode());
                                growerName.setText(growerModelList.get(0).getGrowerName());
                                growerFatherName.setText(growerModelList.get(0).getGrowerFather());

                                if(spray_type.getSelectedItemPosition()==1)
                                {
                                    verifyFromGasti();
                                }
                                else if(spray_type.getSelectedItemPosition()==2)
                                {
                                    verifyFromPlanting();
                                }
                                else
                                {
                                    growerCode.setText("");
                                    growerName.setText("");
                                    growerFatherName.setText("");
                                    villageCode.setText("");
                                    villageName.setText("");
                                }

                            } else {
                                growerCode.setText("");
                                growerName.setText("");
                                growerFatherName.setText("");
                                new AlertDialogManager().RedDialog(context, "Invalid grower code!!!!");
                            }

                        }
                    } else {
                        AlertPopUp("Please enter village code");
                        villageCode.requestFocus();
                    }
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });
        plotVillageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(plotVillageCode.getText().toString().length()>0)
                {
                    //new getPlotVillage().execute(plotVillageCode.getText().toString());
                    List<VillageModal> plotVillageModelList=dbh.getVillageModal(plotVillageCode.getText().toString());
                    if(plotVillageModelList.size()>0)
                    {
                        plotVillageCode.setText(plotVillageModelList.get(0).getCode());
                        plotVillageName.setText(plotVillageModelList.get(0).getName());
                        /*plotVillageNumber.setText(jsonObject.getString("VillPLNO"));
                        growerPlotNumber.setText(jsonObject.getString("GPLNO"));
                        area.setText(jsonObject.getString("Area"));
                        ID=jsonObject.getString("ID");*/
                    }
                    else
                    {
                        plotVillageCode.setText("");
                        plotVillageName.setText("");
                        plotVillageNumber.setText("");
                        growerPlotNumber.setText("");
                        area.setText("");
                        ID="";
                        new AlertDialogManager().RedDialog(context,"Please enter correct plot village code");
                    }

                }
            }
        });

        List<MasterDropDown> sprayItemModels=dbh.getMasterDropDown("9");
        ArrayList<String> data=new ArrayList<String>();
        data.add("Select Spray Item");
        for(int i=0;i<sprayItemModels.size();i++) {
            MasterDropDown masterDropDown = new MasterDropDown();
            masterDropDown.setCode(sprayItemModels.get(i).getCode());
            masterDropDown.setName(sprayItemModels.get(i).getName());
            sprayItemList.add(masterDropDown);
            data.add(sprayItemModels.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.list_item, data);
        sprayItem.setAdapter(adapter);
    }

    private void verifyFromGasti()
    {
        plotFarmerShareModelsList = dbh.getPlotFarmerShareOldModelFilterData(villageCode.getText().toString(), growerCode.getText().toString());
        if (plotFarmerShareModelsList.size() == 0) {
            resetPlotSerial();
            new AlertDialogManager().RedDialog(context, "Sorry survey not done for this grower please go for survey first.");
        } else {
            boolean inside = false;
            SetIndentLocation:
            {
                for (int i = 0; i < plotFarmerShareModelsList.size(); i++) {
                    List<LatLng> latLngList = new ArrayList<>();
                    latLngList.add(new LatLng(Double.parseDouble(plotFarmerShareModelsList.get(i).getEastNorthLat()), Double.parseDouble(plotFarmerShareModelsList.get(i).getEastNorthLng())));
                    latLngList.add(new LatLng(Double.parseDouble(plotFarmerShareModelsList.get(i).getEastSouthLat()), Double.parseDouble(plotFarmerShareModelsList.get(i).getEastSouthLng())));
                    latLngList.add(new LatLng(Double.parseDouble(plotFarmerShareModelsList.get(i).getWestSouthLat()), Double.parseDouble(plotFarmerShareModelsList.get(i).getWestSouthLng())));
                    latLngList.add(new LatLng(Double.parseDouble(plotFarmerShareModelsList.get(i).getWestNorthLat()), Double.parseDouble(plotFarmerShareModelsList.get(i).getWestNorthLng())));
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        //double lats=Double.parseDouble("28.541606");
                        //double lngs=Double.parseDouble("78.737747");
                        double lats = location.getLatitude();
                        double lngs = location.getLongitude();
                        LatLng latlng = new LatLng(lats, lngs);
                        inside = PolyUtil.containsLocation(latlng, latLngList, true);
                        if (inside) {
                            ID = "" + i;
                            //ID = indentModelList.get(i).getColId();
                            //NoOfPlot.setText(plantingList.get(i).getNOFPLOTS());
                            //rl_capture_coordinate.setVisibility(View.VISIBLE);
                            //input_grower_plot_sr_no.setVisibility(View.VISIBLE);
                            //input_grower_plot_sr_no.setText(plantingList.get(i).getPlotSerialNumber());
                            break SetIndentLocation;
                        }
                    } else {
                        //input_actual_area.setSelection(0);
                        new AlertDialogManager().RedDialog(context, "Location not found please enable your gps service");
                        break SetIndentLocation;
                    }
                }
            }

            ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
            indentPlotSrNoArrayList.add("Select");
            if (inside) {
                List<VillageModal> plv = dbh.getVillageModal(plotFarmerShareModelsList.get(Integer.parseInt(ID)).getVillageCode());
                if (plv.size() > 0) {
                    indentPlotSrNoArrayList.add("Village : " + plv.get(0).getCode() + "/" + plv.get(0).getName() + " - Serial : " + plotFarmerShareModelsList.get(Integer.parseInt(ID)).getPlotSrNo());
                } else {
                    new AlertDialogManager().RedDialog(context, "Sorry plot village not found");
                }
            } else {
                for (int i = 0; i < plotFarmerShareModelsList.size(); i++) {
                    List<VillageModal> plv = dbh.getVillageModal(plotFarmerShareModelsList.get(i).getPlotVillageCode());
                    if (plv.size() > 0) {
                        indentPlotSrNoArrayList.add("Village : " + plv.get(0).getCode() + "/" + plv.get(0).getName() + " - Serial : " + plotFarmerShareModelsList.get(i).getPlotSrNo());
                    }
                }
            }

            ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                    R.layout.list_item, indentPlotSrNoArrayList);
            input_planting_number.setAdapter(adapterIndentSrNo);
            input_planting_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        //input_actual_area.setSelection(0);
                        plotVillageCode.setText("");
                        plotVillageName.setText("");
                        plotVillageNumber.setText("");
                        growerPlotNumber.setText("");
                        area.setText("");
                    } else {
                        //input_actual_area.setSelection(0);
                        List<VillageModal> villageModalList = dbh.getVillageModal(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotVillageCode());
                        if (villageModalList.size() > 0) {
                            plotVillageCode.setText(villageModalList.get(0).getCode());
                            plotVillageName.setText(villageModalList.get(0).getName());
                            int plotSrNo = Integer.parseInt(villageModalList.get(0).getMaxPlant());
                            plotSrNo++;
                            plotVillageNumber.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotSrNo());
                            growerPlotNumber.setText("0");
                            area.setText(plotFarmerShareModelsList.get(input_planting_number.getSelectedItemPosition() - 1).getAreaHectare());
                            //input_plot_sr_no.setText(""+plotSrNo);
                        } else {
                            plotVillageCode.setText("");
                            plotVillageName.setText("");
                            growerPlotNumber.setText("");
                            area.setText("");
                            //input_plot_sr_no.setText("");
                            new AlertDialogManager().RedDialog(context, "Plot village code not found in our record please contact admin");
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (inside && indentPlotSrNoArrayList.size() > 1) {
                input_planting_number.setSelection(1);
            }
        }
    }

    private void verifyFromPlanting()
    {
        plantingList = dbh.getPlantingModel("", "", villageCode.getText().toString(), growerCode.getText().toString(), "");
        if (plantingList.size() == 0) {
            resetPlotSerial();
            new AlertDialogManager().RedDialog(context, "Sorry planting not done for this grower please go for planting first.");
        } else {
            boolean inside = false;
            SetIndentLocation:
            {
                for (int i = 0; i < plantingList.size(); i++) {
                    List<LatLng> latLngList = new ArrayList<>();
                    latLngList.add(new LatLng(Double.parseDouble(plantingList.get(i).getLAT1()), Double.parseDouble(plantingList.get(i).getLON1())));
                    latLngList.add(new LatLng(Double.parseDouble(plantingList.get(i).getLAT2()), Double.parseDouble(plantingList.get(i).getLON2())));
                    latLngList.add(new LatLng(Double.parseDouble(plantingList.get(i).getLAT3()), Double.parseDouble(plantingList.get(i).getLON3())));
                    latLngList.add(new LatLng(Double.parseDouble(plantingList.get(i).getLAT4()), Double.parseDouble(plantingList.get(i).getLON4())));
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        //double lats=Double.parseDouble("28.541606");
                        //double lngs=Double.parseDouble("78.737747");
                        double lats = location.getLatitude();
                        double lngs = location.getLongitude();
                        LatLng latlng = new LatLng(lats, lngs);
                        inside = PolyUtil.containsLocation(latlng, latLngList, true);
                        if (inside) {
                            ID = "" + i;
                            //ID = indentModelList.get(i).getColId();
                            //NoOfPlot.setText(plantingList.get(i).getNOFPLOTS());
                            //rl_capture_coordinate.setVisibility(View.VISIBLE);
                            //input_grower_plot_sr_no.setVisibility(View.VISIBLE);
                            //input_grower_plot_sr_no.setText(plantingList.get(i).getPlotSerialNumber());
                            break SetIndentLocation;
                        }
                    } else {
                        //input_actual_area.setSelection(0);
                        new AlertDialogManager().RedDialog(context, "Location not found please enable your gps service");
                        break SetIndentLocation;
                    }
                }
            }

            ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
            indentPlotSrNoArrayList.add("Select");
            if (inside) {
                List<VillageModal> plv = dbh.getVillageModal(plantingList.get(Integer.parseInt(ID)).getPLOTVillage());
                if (plv.size() > 0) {
                    indentPlotSrNoArrayList.add("Village : " + plv.get(0).getCode() + "/" + plv.get(0).getName() + " - Serial : " + plantingList.get(Integer.parseInt(ID)).getPlotSerialNumber());
                } else {
                    new AlertDialogManager().RedDialog(context, "Sorry plot village not found");
                }
            } else {
                for (int i = 0; i < plantingList.size(); i++) {
                    List<VillageModal> plv = dbh.getVillageModal(plantingList.get(i).getPLOTVillage());
                    if (plv.size() > 0) {
                        indentPlotSrNoArrayList.add("Village : " + plv.get(0).getCode() + "/" + plv.get(0).getName() + " - Serial : " + plantingList.get(i).getPlotSerialNumber());
                    }
                }
            }

            ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                    R.layout.list_item, indentPlotSrNoArrayList);
            input_planting_number.setAdapter(adapterIndentSrNo);
            input_planting_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        //input_actual_area.setSelection(0);
                        plotVillageCode.setText("");
                        plotVillageName.setText("");
                        plotVillageNumber.setText("");
                        growerPlotNumber.setText("");
                        area.setText("");
                    } else {
                        //input_actual_area.setSelection(0);
                        List<VillageModal> villageModalList = dbh.getVillageModal(plantingList.get(input_planting_number.getSelectedItemPosition() - 1).getPLOTVillage());
                        if (villageModalList.size() > 0) {
                            plotVillageCode.setText(villageModalList.get(0).getCode());
                            plotVillageName.setText(villageModalList.get(0).getName());
                            int plotSrNo = Integer.parseInt(villageModalList.get(0).getMaxPlant());
                            plotSrNo++;
                            plotVillageNumber.setText(plantingList.get(input_planting_number.getSelectedItemPosition() - 1).getPlotSerialNumber());
                            growerPlotNumber.setText("0");
                            area.setText(plantingList.get(input_planting_number.getSelectedItemPosition() - 1).getArea());
                            //input_plot_sr_no.setText(""+plotSrNo);
                        } else {
                            plotVillageCode.setText("");
                            plotVillageName.setText("");
                            growerPlotNumber.setText("");
                            area.setText("");
                            //input_plot_sr_no.setText("");
                            new AlertDialogManager().RedDialog(context, "Plot village code not found in our record please contact admin");
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (inside && indentPlotSrNoArrayList.size() > 1) {
                input_planting_number.setSelection(1);
            }
        }
    }

    private void resetPlotSerial()
    {
        villageCode.setText("");
        villageName.setText("");
        growerCode.setText("");
        growerName.setText("");
        growerFatherName.setText("");
        plotVillageCode.setText("");
        plotVillageName.setText("");
        plotVillageNumber.setText("");
        growerPlotNumber.setText("");
        area.setText("");
        ID = "";
        plotVillageCode.setText("");
        plotVillageName.setText("");
        growerPlotNumber.setText("");
        area.setText("");
        ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
        indentPlotSrNoArrayList.add("Select");
        ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                R.layout.list_item, indentPlotSrNoArrayList);
        input_planting_number.setAdapter(adapterIndentSrNo);
    }

    public void searchData(View v)
    {
        try {
            if (villageCode.getText().toString().length() == 0) {
                layoutvillageCode.setError("Please enter valid village code");
                layoutvillageCode.setErrorEnabled(true);
            } else if (growerCode.getText().toString().length() == 0) {
                layoutgrowerCode.setError("Please enter valid grower code");
                layoutgrowerCode.setErrorEnabled(true);
                layoutvillageCode.setErrorEnabled(false);
            } else if (plotVillageCode.getText().toString().length() == 0) {
                layoutplotVillageName.setError("Please enter valid plot village code");
                layoutplotVillageName.setErrorEnabled(true);
                layoutgrowerCode.setErrorEnabled(false);
            } else {
                /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat=location.getLatitude();
                lng=location.getLongitude();
                new verifyData().execute(villageCode.getText().toString(), growerCode.getText().toString(),
                        plotVillageCode.getText().toString(),
                        "" +lat , "" +lng );*/
            }
        }
        catch (SecurityException e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
    }

    public void saveData(View v)
    {
        try {
            if(spray_type.getSelectedItemPosition()==0)
            {
                AlertPopUp("Please select spray type first");
            }
            List<SprayItemModel> sprayItemModelList1=new ArrayList<>();
            sprayItemModelList1=dbh.getSprayItem("");
            if(villageCode.getText().toString().length()==0)
            {
                layoutvillageCode.setError("Please enter valid village code");
                layoutvillageCode.setErrorEnabled(true);
            }
            else if(growerCode.getText().toString().length()==0)
            {
                layoutgrowerCode.setError("Please enter valid grower code");
                layoutgrowerCode.setErrorEnabled(true);
                layoutvillageCode.setErrorEnabled(false);
            }
            else if(plotVillageCode.getText().toString().length()==0)
            {
                layoutplotVillageName.setError("Please enter valid plot village code");
                layoutplotVillageName.setErrorEnabled(true);
                layoutgrowerCode.setErrorEnabled(false);
            }
            else if(plotVillageNumber.getText().toString().length()==0)
            {
                layoutplotVillageNumber.setError("Please enter plot village number");
                layoutplotVillageNumber.setErrorEnabled(true);
                layoutplotVillageName.setErrorEnabled(false);
            }
            else if(growerPlotNumber.getText().toString().length()==0)
            {
                layoutgrowerPlotNumber.setError("Please enter grower plot number");
                layoutgrowerPlotNumber.setErrorEnabled(true);
                layoutplotVillageNumber.setErrorEnabled(false);
            }
            else if(area.getText().toString().length()==0)
            {
                layoutarea.setError("Please enter area");
                layoutarea.setErrorEnabled(true);
                layoutgrowerPlotNumber.setErrorEnabled(false);
            }
            else if(area.getText().toString().length()==0)
            {
                layoutarea.setError("Please enter area");
                layoutarea.setErrorEnabled(true);
                layoutgrowerPlotNumber.setErrorEnabled(false);
            }
            else if(spray.getSelectedItemPosition()==0)
            {
                layoutspray.setError("Please select spray");
                layoutspray.setErrorEnabled(true);
                layoutarea.setErrorEnabled(false);
            }
            else if(manualArea.getText().toString().length()==0)
            {
                layoutmanualArea.setError("Please enter manual area");
                layoutmanualArea.setErrorEnabled(true);
                layoutspray.setErrorEnabled(false);
            }
            else if(mobileNumber.getText().toString().length()!=10)
            {
                layoutmobileNumber.setError("Please enter 10 digit valid mobile number");
                layoutmobileNumber.setErrorEnabled(true);
                layoutmanualArea.setErrorEnabled(false);
            }
            else if(pronotNumber.getText().toString().length()==0)
            {
                layoutPronotNo.setError("Please enter Pronot Number");
                layoutPronotNo.setErrorEnabled(true);
                layoutmobileNumber.setErrorEnabled(false);
            }
            else if(sprayNumber.getText().toString().length()==0)
            {
                layoutSprayNo.setError("Please enter spray number");
                layoutSprayNo.setErrorEnabled(true);
                layoutPronotNo.setErrorEnabled(false);
            }
            else if(date.getText().toString().length()==0)
            {
                layoutdate.setError("Please enter date");
                layoutdate.setErrorEnabled(true);
                layoutSprayNo.setErrorEnabled(false);
            }
            else if(description.getText().toString().length()==0)
            {
                layoutdescription.setError("Please enter description");
                layoutdescription.setErrorEnabled(true);
                layoutarea.setErrorEnabled(false);
            }
            else if(pictureImagePath.length()<10)
            {
                AlertPopUp("Please capture spray plot photo");
                layoutdescription.setErrorEnabled(false);
            }
            else if(sprayItemModelList1.size()==0)
            {
                AlertPopUp("Please add item");
            }
            else
            {
                ArrayList<Object> finalItemList=new ArrayList<>();
                for(int i=0;i<sprayItemModelList1.size();i++)
                {
                    Map<String, Object> map=new HashMap<>();
                    map.put("ITEM",sprayItemModelList1.get(i).getItem());
                    map.put("UNIT",sprayItemModelList1.get(i).getUnit());
                    map.put("QTY",sprayItemModelList1.get(i).getQty());
                    map.put("AREA",sprayItemModelList1.get(i).getArea());
                    finalItemList.add(map);
                }
                String json = new Gson().toJson(finalItemList);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location==null)
                {
                    lat=0.00;
                    lng=0.00;
                }
                else
                {
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                }
                new ImageUploadTask().execute(villageCode.getText().toString(), growerCode.getText().toString(),
                        plotVillageCode.getText().toString(),plotVillageNumber.getText().toString(),
                        growerPlotNumber.getText().toString(),area.getText().toString(),ID,spray.getSelectedItem().toString(),
                        description.getText().toString(),""+lat,""+lng,"0",
                        manualArea.getText().toString(),mobileNumber.getText().toString(),date.getText().toString(),json,
                        pronotNumber.getText().toString(),sprayNumber.getText().toString());
            }

        }
        catch (SecurityException e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        catch (Exception e)
        {
            //AlertPopUp("Error:"+e.toString());
        }
    }

    public void addItem(View v)
    {
        if(manualArea.getText().toString().length()==0)
        {
            AlertPopUp("Please enter manual area");
        }
        else
        {
            if(sprayItem.getSelectedItemPosition()==0)
            {
                AlertPopUp("Please select spray item");
            }
            else
            {
                String itemId=sprayItemList.get(sprayItem.getSelectedItemPosition()-1).getCode();
                List<SprayItemModel> sprayItemModelList1=new ArrayList<>();
                sprayItemModelList1=dbh.getSprayItem(itemId);
                if(sprayItemModelList1.size()==0)
                {
                    new getItemQtyByServer().execute(itemId);
                }
                else {
                    AlertPopUp(sprayItemList.get(sprayItem.getSelectedItemPosition()-1).getName()+" already added in list");
                }
                showItemList();
            }
        }

    }

    private void showItemList()
    {
        if(sprayItemModelList.size()>0)
            sprayItemModelList.clear();
        SprayItemModel header = new SprayItemModel();
        header.setItem("Item Code");
        header.setItemName("Item Name");
        header.setQty("Qty");
        header.setArea("Area");
        header.setUnit("Unit");
        header.setColor("#000000");
        header.setTextColor("#FFFFFF");
        sprayItemModelList.add(header);
        List<SprayItemModel> sprayItemModelList1=new ArrayList<>();
        sprayItemModelList1=dbh.getSprayItem("");
        if(sprayItemModelList1.size()>0)
        {
            for(int i=0;i<sprayItemModelList1.size();i++)
            {
                SprayItemModel sprayItemModel = new SprayItemModel();
                sprayItemModel.setId(sprayItemModelList1.get(i).getId());
                sprayItemModel.setItem(sprayItemModelList1.get(i).getItem());
                sprayItemModel.setItemName(sprayItemModelList1.get(i).getItemName());
                sprayItemModel.setQty(sprayItemModelList1.get(i).getQty());
                sprayItemModel.setUnit(sprayItemModelList1.get(i).getUnit());
                sprayItemModel.setArea(sprayItemModelList1.get(i).getArea());
                if(i%2==0)
                {
                    sprayItemModel.setColor("#DFDFDF");
                    sprayItemModel.setTextColor("#000000");
                }
                else
                {
                    sprayItemModel.setColor("#FFFFFF");
                    sprayItemModel.setTextColor("#000000");
                }
                sprayItemModelList.add(sprayItemModel);
            }
        }
        RecyclerView recyclerView =findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        ListSprayItemAdapter listGetCenterRunningStatusDataAdapter =new ListSprayItemAdapter(context,sprayItemModelList);
        recyclerView.setAdapter(listGetCenterRunningStatusDataAdapter);
    }

    public void openCam(View v)
    {
        try {
            filename = "image.jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/CaneManagement");
            dir.mkdirs();
            pictureImagePath = sdCard.getAbsolutePath() + "/CaneManagement" + "/" + filename;
            File file = new File(pictureImagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent,RC_CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat dateFormatter2 = new SimpleDateFormat("HH:mm:ss");
                dateFormatter1.setLenient(false);
                dateFormatter2.setLenient(false);
                Date today = new Date();
                // java.util.Timer;
                String d = dateFormatter1.format(today);
                String t = dateFormatter2.format(today);
                Bitmap bmp = drawTextToBitmap(context, d, t);
                FileOutputStream out = new FileOutputStream(pictureImagePath);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                AlertPopUp("Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
        Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(120);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())-250;
        int y = (bitmap.getHeight() + bounds.height())-250;

        canvas.drawText(gText, x, y, paint);
        paint.getTextBounds(gText1, 0, gText1.length(), bounds);
        int x1 = (bitmap.getWidth() - bounds.width())-250;
        int y1 = (bitmap.getHeight() + bounds.height())-100;
        canvas.drawText(gText1, x1, y1, paint);
        return bitmap;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }


    private class getItemQtyByServer extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/GETITEMQTYUNIT";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("ITEMCODE",params[0]));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url+"?"+paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpGet, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            }catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                if (dialog.isShowing())
                    dialog.dismiss();
                //Content="true";
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    DecimalFormat decimalFormat = new DecimalFormat("##.00");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    double qty=Double.parseDouble(manualArea.getText().toString())/jsonObject.getDouble("PURHARQTY");
                    SprayItemModel sprayItemModel=new SprayItemModel();
                    sprayItemModel.setItem(sprayItemList.get(sprayItem.getSelectedItemPosition()-1).getCode());
                    sprayItemModel.setItemName(sprayItemList.get(sprayItem.getSelectedItemPosition()-1).getName());
                    sprayItemModel.setUnit(jsonObject.getString("UNIT").trim());
                    sprayItemModel.setQty(decimalFormat.format(qty));
                    sprayItemModel.setArea(manualArea.getText().toString());
                    dbh.insertSprayItem(sprayItemModel);
                    sprayItem.setSelection(0);
                    showItemList();
                }
                else
                {
                    AlertPopUp("Invalid item code");
                }
            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }

    class ImageUploadTask extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Uploading",
                    "Please wait while we transfer your file to server", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/SAVESPRAY";
                //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                //int i = Integer.parseInt(params[0]);
                Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currentDt=dateFormat.format(today);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt= Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("VillCode",params[0]));
                entity.add(new BasicNameValuePair("GrowCode",params[1]));
                entity.add(new BasicNameValuePair("PLVillCode",params[2]));
                entity.add(new BasicNameValuePair("PLVillNo",params[3]));
                entity.add(new BasicNameValuePair("GPLNO",params[4]));
                entity.add(new BasicNameValuePair("Area",params[5]));
                //entity.add(new BasicNameValuePair("ID",params[6]));
                entity.add(new BasicNameValuePair("Spary",params[7]));
                entity.add(new BasicNameValuePair("Dis",params[8]));
                entity.add(new BasicNameValuePair("InsertLAT",params[9]));
                entity.add(new BasicNameValuePair("InsertLON",params[10]));
                //entity.add(new BasicNameValuePair("SprayItemCode",params[11]));
                entity.add(new BasicNameValuePair("MenulArea",params[12]));
                entity.add(new BasicNameValuePair("MobilNo",params[13]));
                entity.add(new BasicNameValuePair("SDate",params[14]));
                entity.add(new BasicNameValuePair("JSONDATA",params[15]));
                entity.add(new BasicNameValuePair("PRONOTNO",params[16]));
                entity.add(new BasicNameValuePair("SPRAYNO",params[17]));
                entity.add(new BasicNameValuePair("SupervisorCode",loginUserDetailsList.get(0).getCode()));
                entity.add(new BasicNameValuePair("Image",imgFrmt));
                entity.add(new BasicNameValuePair("spray_type",spray_type.getSelectedItemPosition()+""));

                String debugData=new MiscleniousUtil().ListNameValueToString("SAVESPRAY",entity);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + Content);

                //Content=response.getEntity().toString();
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                //AlertPopUp("Error: "+e.toString());
                //Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                File file=new File(pictureImagePath);
                if(file.exists())
                {
                    file.delete();
                }
                AlertPopUpFinish(Content);
            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                context);
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

    private void AlertPopUpFinish(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                context);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }


}
