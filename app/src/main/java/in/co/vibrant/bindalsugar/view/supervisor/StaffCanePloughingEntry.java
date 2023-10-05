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
import android.location.LocationListener;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.PolyUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;


public class StaffCanePloughingEntry extends AppCompatActivity {

    String CoordinateArea="0";
    DBHelper dbh;
    SQLiteDatabase db;
    List<UserDetailsModel> loginUserDetailsList;

    EditText villageCode,villageName,growerCode,growerName,growerFatherName,plotVillageCode,plotVillageName,mobileNumber,date,
            driverName,TractorNumber,input_plot_area;
    TextInputLayout layoutvillageCode,layoutvillageName,layoutgrowerCode,layoutgrowerName,layoutgrowerFatherName,
            layoutplotVillageCode,layoutplotVillageName,layoutAreaMeter,layoutAreaHec,layoutMobileNumber,layoutdate,layoutNoOfPlot,
            layoutdriverName,layoutTractorNumber;

    TextView Corner1,Lat1,Lng1,Distance1,Accuracy1,Corner2,Lat2,Lng2,Distance2,Accuracy2,Corner3,Lat3,Lng3,
            Distance3,Accuracy3,Corner4,Lat4,Lng4,Distance4,Accuracy4;
    int currentDistance=1;
    double currentAccuracy=200000;
    LinearLayout btnLayout;
    RelativeLayout msgLayout;

    android.app.AlertDialog dialogPopup;
    EditText AreaMeter,AreaHec,NoOfPlot;
    RelativeLayout rl_master_coordinate;
    TextView t_master_latlng,t_master_acc;
    TextView location_lat,location_lng,location_accuracy;
    double lat,lng;

    List<IndentModel> indentModelList;

    String StrAreaMeter="0.0",StrAreaHec="0.0",StrEastNorthLat="0.0",StrEastNorthLng="0.0",StrEastNorthDistance="0.0",
            StrEastNorthAccuracy="0.0",StrWestNorthLat="0.0",StrWestNorthLng="0.0",StrWestNorthDistance="0.0",StrWestNorthAccuracy="0.0",
            StrEastSouthLat="0.0",StrEastSouthLng="0.0",StrEastSouthDistance="0.0",StrEastSouthAccuracy="",StrWestSouthLat="0.0",
            StrWestSouthLng="0.0",StrWestSouthDistance="0.0",StrWestSouthAccuracy="0.0";

    double Lat,Lng,Accuracy,LastLat=0,LastLng=0,dist=0;
    LocationManager locationManager;

    String ID="";

    String filename="",pictureImagePath="";
    private int RC_CAMERA_REQUEST=1001;
    ImageView image;
    boolean checkPlot=false;
    Spinner input_indent_number;
    
    Context context;
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
                    "(([0-5]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

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
        setContentView(R.layout.ploughing_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.MENU_PLOUGHING));
        toolbar.setTitle(getString(R.string.MENU_PLOUGHING));
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

        context=StaffCanePloughingEntry.this;
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();

        loginUserDetailsList=new ArrayList<>();
        loginUserDetailsList=dbh.getUserDetailsModel();

        layoutvillageCode=findViewById(R.id.input_layout_village_code);
        layoutvillageName=findViewById(R.id.input_layout_village_name);
        layoutgrowerCode=findViewById(R.id.input_layout_grower_code);
        layoutgrowerName=findViewById(R.id.input_layout_grower_name);
        layoutgrowerFatherName=findViewById(R.id.input_layout_grower_father);
        layoutplotVillageCode=findViewById(R.id.input_layout_plot_village_code);
        layoutplotVillageName=findViewById(R.id.input_layout_plot_village_name);
        layoutAreaMeter=findViewById(R.id.input_layout_area_meter);
        layoutAreaHec=findViewById(R.id.input_layout_area_hec);
        layoutNoOfPlot=findViewById(R.id.input_layout_no_of_plot);
        layoutMobileNumber=findViewById(R.id.input_layout_mobile_number);
        layoutdate=findViewById(R.id.input_layout_date);
        layoutdriverName=findViewById(R.id.input_layout_driver_name);
        layoutTractorNumber=findViewById(R.id.input_layout_tractor_name);
        input_indent_number=findViewById(R.id.input_indent_number);


        villageCode=findViewById(R.id.input_village_code);
        villageName=findViewById(R.id.input_village_name);
        growerCode=findViewById(R.id.input_grower_code);
        growerName=findViewById(R.id.input_grower_name);
        growerFatherName=findViewById(R.id.input_grower_father);
        plotVillageCode=findViewById(R.id.input_plot_village_code);
        plotVillageName=findViewById(R.id.input_plot_village_name);
        AreaMeter=findViewById(R.id.area_meter);
        AreaHec=findViewById(R.id.area_hec);
        NoOfPlot=findViewById(R.id.no_of_plot);
        mobileNumber=findViewById(R.id.mobile_number);
        date=findViewById(R.id.date);
        driverName=findViewById(R.id.input_plot_driver_name);
        TractorNumber=findViewById(R.id.input_plot_tractor_name);
        input_plot_area=findViewById(R.id.input_plot_area);

        AreaMeter.setFilters(new InputFilter[] { filter });
        Corner1=findViewById(R.id.t_corner_1_name);
        Lat1=findViewById(R.id.t_corner_1_lat);
        Lng1=findViewById(R.id.t_corner_1_lng);
        Distance1=findViewById(R.id.t_corner_1_distance);
        Accuracy1=findViewById(R.id.t_corner_1_accuracy);
        Corner2=findViewById(R.id.t_corner_2_name);
        Lat2=findViewById(R.id.t_corner_2_lat);
        Lng2=findViewById(R.id.t_corner_2_lng);
        Distance2=findViewById(R.id.t_corner_2_distance);
        Accuracy2=findViewById(R.id.t_corner_2_accuracy);
        Corner3=findViewById(R.id.t_corner_3_name);
        Lat3=findViewById(R.id.t_corner_3_lat);
        Lng3=findViewById(R.id.t_corner_3_lng);
        Distance3=findViewById(R.id.t_corner_3_distance);
        Accuracy3=findViewById(R.id.t_corner_3_accuracy);
        Corner4=findViewById(R.id.t_corner_4_name);
        Lat4=findViewById(R.id.t_corner_4_lat);
        Lng4=findViewById(R.id.t_corner_4_lng);
        Distance4=findViewById(R.id.t_corner_4_distance);
        Accuracy4=findViewById(R.id.t_corner_4_accuracy);

        image=findViewById(R.id.image);

        Corner1.setText("");
        Corner2.setText("");
        Corner3.setText("");
        Corner4.setText("");
        //new getWarehouseList().execute();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        init();
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
                    checkPlot=false;
                    //new getVillageData().execute(villageCode.getText().toString());
                    List<VillageModal> villageModals=dbh.getVillageModal(villageCode.getText().toString());
                    if(villageModals.size()>0)
                    {
                        villageCode.setText(villageModals.get(0).getCode());
                        villageName.setText(villageModals.get(0).getName());
                    }
                    else
                    {
                        villageCode.setText("");
                        villageName.setText("");
                    }

                }
            }
        });
        growerCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    growerName.setFocusable(true);
                    growerName.setTextIsSelectable(true);
                    growerName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    growerFatherName.setFocusable(true);
                    growerFatherName.setTextIsSelectable(true);
                    growerFatherName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    //input_actual_area.setSelection(0);
                    if (villageCode.getText().toString().length() > 0) {
                        if (growerCode.getText().toString().length() > 0) {
                            if (growerCode.getText().toString().equals("0")) {
                                growerName.setFocusable(true);
                                growerName.setTextIsSelectable(true);
                                growerName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                growerFatherName.setFocusable(true);
                                growerFatherName.setTextIsSelectable(true);
                                growerFatherName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                            } else {
                                growerName.setInputType(InputType.TYPE_NULL);
                                growerFatherName.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(villageCode.getText().toString(), growerCode.getText().toString());
                                if (growerModelList.size() > 0) {
                                    growerCode.setText(growerModelList.get(0).getGrowerCode());
                                    growerName.setText(growerModelList.get(0).getGrowerName());
                                    growerFatherName.setText(growerModelList.get(0).getGrowerFather());
                                } else {
                                    new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                    growerCode.setText("");
                                    growerName.setText("");
                                    growerFatherName.setText("");
                                }

                            }


                            indentModelList=dbh.getIndentModel("","",villageCode.getText().toString(),growerCode.getText().toString(),"");
                            if(indentModelList.size()==0)
                            {
                                villageCode.setText("");
                                villageCode.setText("");
                                growerCode.setText("");
                                growerName.setText("");
                                growerFatherName.setText("");
                                ArrayList<String> indentPlotSrNoArrayList = new ArrayList<String>();
                                indentPlotSrNoArrayList.add("Select");
                                ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                                        R.layout.list_item, indentPlotSrNoArrayList);
                                input_indent_number.setAdapter(adapterIndentSrNo);
                                new AlertDialogManager().RedDialog(context,"Sorry indenting not found for this grower please go for indenting first.");
                            }
                            else
                            {
                                boolean inside = false;
                                SetIndentLocation:
                                {
                                    for (int i = 0; i < indentModelList.size(); i++) {
                                        List<LatLng> latLngList = new ArrayList<>();
                                        latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT1()), Double.parseDouble(indentModelList.get(i).getLON1())));
                                        latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT2()), Double.parseDouble(indentModelList.get(i).getLON2())));
                                        latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT3()), Double.parseDouble(indentModelList.get(i).getLON3())));
                                        latLngList.add(new LatLng(Double.parseDouble(indentModelList.get(i).getLAT4()), Double.parseDouble(indentModelList.get(i).getLON4())));
                                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        if (location != null) {
                                            //double lats=Double.parseDouble("28.541606");
                                            //double lngs=Double.parseDouble("78.737747");
                                            double lats = location.getLatitude();
                                            double lngs = location.getLongitude();
                                            LatLng latlng = new LatLng(lats, lngs);
                                            inside = PolyUtil.containsLocation(latlng, latLngList, true);
                                            if (inside) {
                                                ID = ""+i;
                                                //ID = indentModelList.get(i).getColId();
                                                NoOfPlot.setText(indentModelList.get(i).getNOFPLOTS());
                                                //rl_capture_coordinate.setVisibility(View.VISIBLE);
                                                //input_grower_plot_sr_no.setVisibility(View.VISIBLE);
                                                //input_grower_plot_sr_no.setText(indentModelList.get(i).getPlotSerialNumber());
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
                                    List<VillageModal> plv=dbh.getVillageModal(indentModelList.get(Integer.parseInt(ID)).getPLOTVillage());
                                    if(plv.size()>0)
                                    {
                                        indentPlotSrNoArrayList.add("Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName()+ " - Serial : "+ indentModelList.get(Integer.parseInt(ID)).getPlotSerialNumber());
                                    }
                                    else
                                    {
                                        new AlertDialogManager().RedDialog(context,"Sorry plot village not found");
                                    }
                                }
                                else
                                {
                                    for (int i = 0; i < indentModelList.size(); i++) {
                                        List<VillageModal> plv=dbh.getVillageModal(indentModelList.get(i).getPLOTVillage());
                                        if(plv.size()>0)
                                        {
                                            indentPlotSrNoArrayList.add("Village : "+plv.get(0).getCode()+"/"+plv.get(0).getName()+ " - Serial : "+ indentModelList.get(i).getPlotSerialNumber());
                                        }
                                    }
                                }

                                ArrayAdapter<String> adapterIndentSrNo = new ArrayAdapter<String>(context,
                                        R.layout.list_item, indentPlotSrNoArrayList);
                                input_indent_number.setAdapter(adapterIndentSrNo);
                                input_indent_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i==0)
                                        {
                                            //clearCoordinates();
                                            //input_actual_area.setSelection(0);
                                            plotVillageCode.setText("");
                                            plotVillageName.setText("");
                                        }
                                        else
                                        {
                                            //clearCoordinates();
                                            //input_actual_area.setSelection(0);
                                            List<VillageModal> villageModalList = dbh.getVillageModal(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getPLOTVillage());
                                            if (villageModalList.size() > 0) {
                                                plotVillageCode.setText(villageModalList.get(0).getCode());
                                                plotVillageName.setText(villageModalList.get(0).getName());
                                                int plotSrNo=Integer.parseInt(villageModalList.get(0).getMaxPlant());
                                                plotSrNo++;
                                                if(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getArea().equalsIgnoreCase(""))
                                                {
                                                    input_plot_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getINDAREA());
                                                }
                                                else
                                                {
                                                    if(Double.parseDouble(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getArea())>0)
                                                    {
                                                        input_plot_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getArea());
                                                    }
                                                    else
                                                    {
                                                        input_plot_area.setText(indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getINDAREA());
                                                    }
                                                }
                                                //input_plot_sr_no.setText(""+plotSrNo);
                                            } else {
                                                plotVillageCode.setText("");
                                                plotVillageName.setText("");
                                                input_plot_area.setText("");
                                                //input_plot_sr_no.setText("");
                                                new AlertDialogManager().RedDialog(context, "Plot village code not found in our record please contact admin");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                if (inside && indentPlotSrNoArrayList.size()>1)
                                {
                                    input_indent_number.setSelection(1);
                                }
                            }
                            //input_indent_number=
                        }
                    } else {
                        //new AlertDialogManager().RedDialog(context, "Please enter village code");
                    }
                } catch (Exception e) {
                    new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                }
            }
        });
        plotVillageCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(plotVillageCode.getText().toString().length()>0)
                {
                    checkPlot=false;
                    //new getPlotVillage().execute(plotVillageCode.getText().toString());
                    List<VillageModal> villageModals=dbh.getVillageModal(villageCode.getText().toString());
                    if(villageModals.size()>0)
                    {
                        villageCode.setText(villageModals.get(0).getCode());
                        villageName.setText(villageModals.get(0).getName());
                    }
                    else
                    {
                        villageCode.setText("");
                        villageName.setText("");
                    }
                }
            }
        });
    }

    public void saveData(View v)
    {
        try {
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
            else if(input_indent_number.getSelectedItemPosition()==0)
            {
                layoutplotVillageName.setError("Please select indent plot serial number");
                layoutplotVillageName.setErrorEnabled(true);
                layoutgrowerCode.setErrorEnabled(false);
            }
            else if(plotVillageCode.getText().toString().length()==0)
            {
                layoutplotVillageName.setError("Please enter valid plot village code");
                layoutplotVillageName.setErrorEnabled(true);
                layoutgrowerCode.setErrorEnabled(false);
            }
          /*  else if(driverName.getText().toString().length()==0)
            {
                layoutdriverName.setError("Please enter driver name");
                layoutdriverName.setErrorEnabled(true);
                layoutgrowerCode.setErrorEnabled(false);
            }*/
           /* else if(TractorNumber.getText().toString().length()==0)
            {
                layoutTractorNumber.setError("Please enter tractor number");
                layoutTractorNumber.setErrorEnabled(true);
                layoutdriverName.setErrorEnabled(false);
            }*/
            else if(Double.parseDouble(AreaMeter.getText().toString())==0 && Double.parseDouble(CoordinateArea)==0)
            {
                layoutAreaMeter.setError("Please enter area");
                layoutAreaMeter.setErrorEnabled(true);
                layoutplotVillageName.setErrorEnabled(false);
            }
            else if(NoOfPlot.getText().toString().length()==0)
            {
                layoutNoOfPlot.setError("Please enter description");
                layoutNoOfPlot.setErrorEnabled(true);
                layoutAreaMeter.setErrorEnabled(false);
            }
            else if(mobileNumber.getText().toString().length()!=10)
            {
                layoutMobileNumber.setError("Please enter mobile number");
                layoutMobileNumber.setErrorEnabled(true);
                layoutNoOfPlot.setErrorEnabled(false);
            }
            else if(date.getText().toString().length()==0)
            {
                layoutdate.setError("Please select date");
                layoutdate.setErrorEnabled(true);
                layoutMobileNumber.setErrorEnabled(false);
            }
            else if(pictureImagePath.length()<10)
            {
                AlertPopUp("Please capture farmer image");
                layoutAreaMeter.setErrorEnabled(false);
            }
            else
            {
                String sampleBefore="";
                String sampleAfter="";
                CheckBox sample_before=findViewById(R.id.sample_before);
                if(sample_before.isChecked())
                {
                    sampleBefore="1";
                }
                else
                {
                    sampleBefore="0";
                }
                CheckBox sample_after=findViewById(R.id.sample_after);
                if(sample_after.isChecked())
                {
                    sampleAfter="1";
                }
                else
                {
                    sampleAfter="0";
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                new ImageUploadTask().execute(villageCode.getText().toString(), growerCode.getText().toString(), plotVillageCode.getText().toString(),
                        driverName.getText().toString(),TractorNumber.getText().toString(),NoOfPlot.getText().toString(), AreaMeter.getText().toString(), ""+location.getLatitude(),
                        ""+location.getLongitude(),mobileNumber.getText().toString(),date.getText().toString(),sampleBefore,sampleAfter);
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

    private void getCurrentLocation()
    {
        try {
            final DecimalFormat decimalFormat = new DecimalFormat("##");
            android.app.AlertDialog.Builder dialogbilder = new android.app.AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_get_latlng, null);
            dialogbilder.setView(mView);
            final Spinner location_direction=mView.findViewById(R.id.location_direction);
            btnLayout=mView.findViewById(R.id.btnLayout);
            msgLayout=mView.findViewById(R.id.rl_msg);
            location_lat=mView.findViewById(R.id.location_lat);
            location_lng=mView.findViewById(R.id.location_lng);
            location_accuracy=mView.findViewById(R.id.location_accuracy);
            ArrayList<String> divData=getDirDrop();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.list_item, divData);
            location_direction.setAdapter(adapter);
            /*Location curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (curLocation != null) {
                location_lat.setText("" + curLocation.getLatitude());
                location_lng.setText("" + curLocation.getLongitude());
                location_accuracy.setText("" + curLocation.getAccuracy());
            }*/
            location_lat.setText("-----");
            location_lng.setText("-----");
            location_accuracy.setText("-----");
            dialogPopup = dialogbilder.create();
            dialogPopup.show();
            dialogPopup.setCancelable(true);
            dialogPopup.setCanceledOnTouchOutside(true);
            Button btn_ok=mView.findViewById(R.id.btn_ok);
            Button skip_cancel=mView.findViewById(R.id.skip_cancel);
            if(currentDistance==4)
            {
                skip_cancel.setVisibility(View.VISIBLE);
            }
            skip_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Corner4.setText(location_direction.getSelectedItem().toString());
                    Lat4.setText("0.0");
                    Lng4.setText("0.0");
                    Accuracy4.setText("0.0");
                    currentDistance++;
                    calAreaTriangle();
                    dialogPopup.dismiss();
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(currentDistance==1)
                        {
                            Corner1.setText(location_direction.getSelectedItem().toString());
                            Lat1.setText(""+location_lat.getText().toString());
                            Lng1.setText(""+location_lng.getText().toString());
                            Accuracy1.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==2)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            Distance1.setText(""+decimalFormat.format(dist));
                            Corner2.setText(location_direction.getSelectedItem().toString());
                            Lat2.setText(""+location_lat.getText().toString());
                            Lng2.setText(""+location_lng.getText().toString());
                            Accuracy2.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==3)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat2.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng2.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            Distance2.setText(""+decimalFormat.format(dist));
                            Corner3.setText(location_direction.getSelectedItem().toString());
                            Lat3.setText(""+location_lat.getText().toString());
                            Lng3.setText(""+location_lng.getText().toString());
                            Accuracy3.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                        }
                        else if(currentDistance==4)
                        {
                            Location sourceL=new Location("");
                            sourceL.setLatitude(Double.parseDouble(Lat3.getText().toString()));
                            sourceL.setLongitude(Double.parseDouble(Lng3.getText().toString()));
                            Location destinationL=new Location("");
                            destinationL.setLatitude(Double.parseDouble(location_lat.getText().toString()));
                            destinationL.setLongitude(Double.parseDouble(location_lng.getText().toString()));
                            Location startL=new Location("");
                            startL.setLatitude(Double.parseDouble(Lat1.getText().toString()));
                            startL.setLongitude(Double.parseDouble(Lng1.getText().toString()));
                            double dist=sourceL.distanceTo(destinationL);
                            double dist1=destinationL.distanceTo(startL);
                            Distance3.setText(""+decimalFormat.format(dist));
                            Distance4.setText(""+decimalFormat.format(dist1));
                            Corner4.setText(location_direction.getSelectedItem().toString());
                            Lat4.setText(""+location_lat.getText().toString());
                            Lng4.setText(""+location_lng.getText().toString());
                            Accuracy4.setText(""+location_accuracy.getText().toString());
                            currentDistance++;
                            calArea();
                        }
                    }
                    catch(Exception e)
                    {

                    }
                    dialogPopup.dismiss();
                }
            });
        }
        catch(SecurityException e)
        {

        }

    }

    private ArrayList<String> getDirDrop()
    {
        ArrayList<String> divData=new ArrayList<String>();
        if(currentDistance==1)
        {
            divData.add("East");
            divData.add("West");
            divData.add("North");
            divData.add("South");
        }
        else if(currentDistance==2)
        {
            if(Corner1.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("North");
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("North");
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("East");
                divData.add("West");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("East");
                divData.add("West");
            }
        }
        else if(currentDistance==3)
        {
            if(Corner1.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("West");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("East");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("South");
            }
            else if(Corner1.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("North");
            }
        }
        else if(currentDistance==4)
        {
            if(Corner2.getText().toString().equalsIgnoreCase("East"))
            {
                divData.add("West");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("West"))
            {
                divData.add("East");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                divData.add("South");
            }
            else if(Corner2.getText().toString().equalsIgnoreCase("South"))
            {
                divData.add("North");
            }
        }
        return divData;
    }

    public void captureCoordinate(View v)
    {
        if(checkPlot)
        {
            findLocation();
            getCurrentLocation();
        }
        else
        {
            //searchData();
        }

    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            Lat=location.getLatitude();
            Lng=location.getLongitude();
            Location currentLocation=new Location("");
            currentLocation.setLatitude(Lat);
            currentLocation.setLongitude(Lng);
            Accuracy=location.getAccuracy();
            setLatDialogue(""+Lat, ""+Lng, ""+Accuracy);
            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                }
            }, 5000);*/

        }

        public void onStatusChanged(String s, int i, Bundle b) {
            t_master_latlng.setText("Provider status changed");
        }

        public void onProviderDisabled(String s) {
            t_master_latlng.setText("Provider disabled by the user. GPS turned off");
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(context,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void findLocation()
    {
        //startLocationUpdates();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, new MyLocationListener());
        }
        catch (SecurityException e)
        {

        }
        catch (Exception e)
        {

        }
    }


    private void calArea()
    {
        DecimalFormat decimalFormat = new DecimalFormat("##");
        Location l1=new Location("");
        l1.setLatitude(Double.parseDouble(Lat1.getText().toString()));
        l1.setLongitude(Double.parseDouble(Lng1.getText().toString()));
        Location l2=new Location("");
        l2.setLatitude(Double.parseDouble(Lat2.getText().toString()));
        l2.setLongitude(Double.parseDouble(Lng2.getText().toString()));
        Location l3=new Location("");
        l3.setLatitude(Double.parseDouble(Lat3.getText().toString()));
        l3.setLongitude(Double.parseDouble(Lng3.getText().toString()));
        Location l4=new Location("");
        l4.setLatitude(Double.parseDouble(Lat4.getText().toString()));
        l4.setLongitude(Double.parseDouble(Lng4.getText().toString()));
        Distance1.setText(decimalFormat.format(l1.distanceTo(l2)));
        Distance2.setText(decimalFormat.format(l2.distanceTo(l3)));
        Distance3.setText(decimalFormat.format(l3.distanceTo(l4)));
        Distance4.setText(decimalFormat.format(l4.distanceTo(l1)));
        if(Corner1.getText().toString().equalsIgnoreCase("East"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                StrEastNorthLat=Lat1.getText().toString();
                StrEastNorthLng=Lng1.getText().toString();
                StrWestNorthLat=Lat2.getText().toString();
                StrWestNorthLng=Lng2.getText().toString();
                StrWestSouthLat=Lat3.getText().toString();
                StrWestSouthLng=Lng3.getText().toString();
                StrEastSouthLat=Lat4.getText().toString();
                StrEastSouthLng=Lng4.getText().toString();
                StrEastNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestSouthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrEastSouthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrEastNorthAccuracy=Accuracy1.getText().toString();
                StrWestNorthAccuracy=Accuracy2.getText().toString();
                StrWestSouthAccuracy=Accuracy3.getText().toString();
                StrEastSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrEastSouthLat=Lat1.getText().toString();
                StrEastSouthLng=Lng1.getText().toString();
                StrWestSouthLat=Lat2.getText().toString();
                StrWestSouthLng=Lng2.getText().toString();
                StrWestNorthLat=Lat3.getText().toString();
                StrWestNorthLng=Lng3.getText().toString();
                StrEastNorthLat=Lat4.getText().toString();
                StrEastNorthLng=Lng4.getText().toString();
                StrEastSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestNorthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrEastNorthDistance=decimalFormat.format(l4.distanceTo(l1));

                StrEastSouthAccuracy=Accuracy1.getText().toString();
                StrWestSouthAccuracy=Accuracy2.getText().toString();
                StrWestNorthAccuracy=Accuracy3.getText().toString();
                StrEastNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("West"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                StrWestNorthLat=Lat1.getText().toString();
                StrWestNorthLng=Lng1.getText().toString();
                StrEastNorthLat=Lat2.getText().toString();
                StrEastNorthLng=Lng2.getText().toString();
                StrEastSouthLat=Lat3.getText().toString();
                StrEastSouthLng=Lng3.getText().toString();
                StrWestSouthLat=Lat4.getText().toString();
                StrWestSouthLng=Lng4.getText().toString();
                StrWestNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastSouthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrWestSouthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrWestNorthAccuracy=Accuracy1.getText().toString();
                StrEastNorthAccuracy=Accuracy2.getText().toString();
                StrEastSouthAccuracy=Accuracy3.getText().toString();
                StrWestSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrWestSouthLat=Lat1.getText().toString();
                StrWestSouthLng=Lng1.getText().toString();
                StrEastSouthLat=Lat2.getText().toString();
                StrEastSouthLng=Lng2.getText().toString();
                StrEastNorthLat=Lat3.getText().toString();
                StrEastNorthLng=Lng3.getText().toString();
                StrWestNorthLat=Lat4.getText().toString();
                StrWestNorthLng=Lng4.getText().toString();
                StrWestSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastNorthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrWestNorthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrWestSouthAccuracy=Accuracy1.getText().toString();
                StrEastSouthAccuracy=Accuracy2.getText().toString();
                StrEastNorthAccuracy=Accuracy3.getText().toString();
                StrWestNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("North"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("East"))
            {
                StrEastNorthLat=Lat1.getText().toString();
                StrEastNorthLng=Lng1.getText().toString();
                StrEastSouthLat=Lat2.getText().toString();
                StrEastSouthLng=Lng2.getText().toString();
                StrWestSouthLat=Lat3.getText().toString();
                StrWestSouthLng=Lng3.getText().toString();
                StrWestNorthLat=Lat4.getText().toString();
                StrWestNorthLng=Lng4.getText().toString();
                StrEastNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestSouthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrWestNorthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrEastNorthAccuracy=Accuracy1.getText().toString();
                StrEastSouthAccuracy=Accuracy2.getText().toString();
                StrWestSouthAccuracy=Accuracy3.getText().toString();
                StrWestNorthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrWestNorthLat=Lat1.getText().toString();
                StrWestNorthLng=Lng1.getText().toString();
                StrWestSouthLat=Lat2.getText().toString();
                StrWestSouthLng=Lng2.getText().toString();
                StrEastSouthLat=Lat3.getText().toString();
                StrEastSouthLng=Lng3.getText().toString();
                StrEastNorthLat=Lat4.getText().toString();
                StrEastNorthLng=Lng4.getText().toString();
                StrWestNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastSouthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrEastNorthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrWestNorthAccuracy=Accuracy1.getText().toString();
                StrWestSouthAccuracy=Accuracy2.getText().toString();
                StrEastSouthAccuracy=Accuracy3.getText().toString();
                StrEastNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("South"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("West"))
            {
                StrWestSouthLat=Lat1.getText().toString();
                StrWestSouthLng=Lng1.getText().toString();
                StrWestNorthLat=Lat2.getText().toString();
                StrWestNorthLng=Lng2.getText().toString();
                StrEastNorthLat=Lat3.getText().toString();
                StrEastNorthLng=Lng3.getText().toString();
                StrEastSouthLat=Lat4.getText().toString();
                StrEastSouthLng=Lng4.getText().toString();
                StrWestSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastNorthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrEastSouthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrWestSouthAccuracy=Accuracy1.getText().toString();
                StrWestNorthAccuracy=Accuracy2.getText().toString();
                StrEastNorthAccuracy=Accuracy3.getText().toString();
                StrEastSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrEastSouthLat=Lat1.getText().toString();
                StrEastSouthLng=Lng1.getText().toString();
                StrEastNorthLat=Lat2.getText().toString();
                StrEastNorthLng=Lng2.getText().toString();
                StrWestNorthLat=Lat3.getText().toString();
                StrWestNorthLng=Lng3.getText().toString();
                StrWestSouthLat=Lat4.getText().toString();
                StrWestSouthLng=Lng4.getText().toString();
                StrEastSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestNorthDistance=decimalFormat.format(l3.distanceTo(l4));
                StrWestSouthDistance=decimalFormat.format(l4.distanceTo(l1));
                StrEastSouthAccuracy=Accuracy1.getText().toString();
                StrEastNorthAccuracy=Accuracy2.getText().toString();
                StrWestNorthAccuracy=Accuracy3.getText().toString();
                StrWestSouthAccuracy=Accuracy4.getText().toString();
            }
        }
        double eastWestLength=(Double.parseDouble(StrEastNorthDistance)+ Double.parseDouble(StrWestSouthDistance))/2;
        double northSouthLength=(Double.parseDouble(StrWestNorthDistance)+ Double.parseDouble(StrEastSouthDistance))/2;

        DecimalFormat decimalHecFormat = new DecimalFormat("##.000");
        double areaM=eastWestLength*northSouthLength;

        AreaMeter.setText(decimalFormat.format(areaM));
        double ah=areaM/10000;
        AreaHec.setText(""+decimalHecFormat.format(ah));
        StrAreaMeter=decimalFormat.format(areaM);
        //StrAreaHec=String.format("%f", ah);
        StrAreaHec=decimalHecFormat.format(ah);
        currentDistance++;
        currentAccuracy=20000;
        CoordinateArea=decimalFormat.format(areaM);
        AreaMeter.setText("0");
        AreaMeter.setInputType(InputType.TYPE_NULL);
        AreaMeter.setTextIsSelectable(false);
        AreaMeter.setFocusable(false);
        AreaHec.setText("0");
        AreaHec.setInputType(InputType.TYPE_NULL);
        AreaHec.setTextIsSelectable(false);
        AreaHec.setFocusable(false);
    }


    private void calAreaTriangle()
    {
        DecimalFormat decimalFormat = new DecimalFormat("##");
        Location l1=new Location("");
        l1.setLatitude(Double.parseDouble(Lat1.getText().toString()));
        l1.setLongitude(Double.parseDouble(Lng1.getText().toString()));
        Location l2=new Location("");
        l2.setLatitude(Double.parseDouble(Lat2.getText().toString()));
        l2.setLongitude(Double.parseDouble(Lng2.getText().toString()));
        Location l3=new Location("");
        l3.setLatitude(Double.parseDouble(Lat3.getText().toString()));
        l3.setLongitude(Double.parseDouble(Lng3.getText().toString()));
        Distance1.setText(decimalFormat.format(l1.distanceTo(l2)));
        Distance2.setText(decimalFormat.format(l2.distanceTo(l3)));
        Distance3.setText(decimalFormat.format(l3.distanceTo(l1)));
        if(Corner1.getText().toString().equalsIgnoreCase("East"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                StrEastNorthLat=Lat1.getText().toString();
                StrEastNorthLng=Lng1.getText().toString();
                StrWestNorthLat=Lat2.getText().toString();
                StrWestNorthLng=Lng2.getText().toString();
                StrWestSouthLat=Lat3.getText().toString();
                StrWestSouthLng=Lng3.getText().toString();
                StrEastSouthLat=Lat4.getText().toString();
                StrEastSouthLng=Lng4.getText().toString();
                StrEastNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestSouthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrEastSouthDistance="0";
                StrEastNorthAccuracy=Accuracy1.getText().toString();
                StrWestNorthAccuracy=Accuracy2.getText().toString();
                StrWestSouthAccuracy=Accuracy3.getText().toString();
                StrEastSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrEastSouthLat=Lat1.getText().toString();
                StrEastSouthLng=Lng1.getText().toString();
                StrWestSouthLat=Lat2.getText().toString();
                StrWestSouthLng=Lng2.getText().toString();
                StrWestNorthLat=Lat3.getText().toString();
                StrWestNorthLng=Lng3.getText().toString();
                StrEastNorthLat=Lat4.getText().toString();
                StrEastNorthLng=Lng4.getText().toString();
                StrEastSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestNorthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrEastNorthDistance="0";
                StrEastSouthAccuracy=Accuracy1.getText().toString();
                StrWestSouthAccuracy=Accuracy2.getText().toString();
                StrWestNorthAccuracy=Accuracy3.getText().toString();
                StrEastNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("West"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("North"))
            {
                StrWestNorthLat=Lat1.getText().toString();
                StrWestNorthLng=Lng1.getText().toString();
                StrEastNorthLat=Lat2.getText().toString();
                StrEastNorthLng=Lng2.getText().toString();
                StrEastSouthLat=Lat3.getText().toString();
                StrEastSouthLng=Lng3.getText().toString();
                StrWestSouthLat=Lat4.getText().toString();
                StrWestSouthLng=Lng4.getText().toString();
                StrWestNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastSouthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrWestSouthDistance="0";
                StrWestNorthAccuracy=Accuracy1.getText().toString();
                StrEastNorthAccuracy=Accuracy2.getText().toString();
                StrEastSouthAccuracy=Accuracy3.getText().toString();
                StrWestSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrWestSouthLat=Lat1.getText().toString();
                StrWestSouthLng=Lng1.getText().toString();
                StrEastSouthLat=Lat2.getText().toString();
                StrEastSouthLng=Lng2.getText().toString();
                StrEastNorthLat=Lat3.getText().toString();
                StrEastNorthLng=Lng3.getText().toString();
                StrWestNorthLat=Lat4.getText().toString();
                StrWestNorthLng=Lng4.getText().toString();
                StrWestSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastNorthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrWestNorthDistance="0";
                StrWestSouthAccuracy=Accuracy1.getText().toString();
                StrEastSouthAccuracy=Accuracy2.getText().toString();
                StrEastNorthAccuracy=Accuracy3.getText().toString();
                StrWestNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("North"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("East"))
            {
                StrEastNorthLat=Lat1.getText().toString();
                StrEastNorthLng=Lng1.getText().toString();
                StrEastSouthLat=Lat2.getText().toString();
                StrEastSouthLng=Lng2.getText().toString();
                StrWestSouthLat=Lat3.getText().toString();
                StrWestSouthLng=Lng3.getText().toString();
                StrWestNorthLat=Lat4.getText().toString();
                StrWestNorthLng=Lng4.getText().toString();
                StrEastNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestSouthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrWestNorthDistance="0";
                StrEastNorthAccuracy=Accuracy1.getText().toString();
                StrEastSouthAccuracy=Accuracy2.getText().toString();
                StrWestSouthAccuracy=Accuracy3.getText().toString();
                StrWestNorthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrWestNorthLat=Lat1.getText().toString();
                StrWestNorthLng=Lng1.getText().toString();
                StrWestSouthLat=Lat2.getText().toString();
                StrWestSouthLng=Lng2.getText().toString();
                StrEastSouthLat=Lat3.getText().toString();
                StrEastSouthLng=Lng3.getText().toString();
                StrEastNorthLat=Lat4.getText().toString();
                StrEastNorthLng=Lng4.getText().toString();
                StrWestNorthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestSouthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastSouthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrEastNorthDistance="0";
                StrWestNorthAccuracy=Accuracy1.getText().toString();
                StrWestSouthAccuracy=Accuracy2.getText().toString();
                StrEastSouthAccuracy=Accuracy3.getText().toString();
                StrEastNorthAccuracy=Accuracy4.getText().toString();
            }
        }
        if(Corner1.getText().toString().equalsIgnoreCase("South"))
        {
            if(Corner2.getText().toString().equalsIgnoreCase("West"))
            {
                StrWestSouthLat=Lat1.getText().toString();
                StrWestSouthLng=Lng1.getText().toString();
                StrWestNorthLat=Lat2.getText().toString();
                StrWestNorthLng=Lng2.getText().toString();
                StrEastNorthLat=Lat3.getText().toString();
                StrEastNorthLng=Lng3.getText().toString();
                StrEastSouthLat=Lat4.getText().toString();
                StrEastSouthLng=Lng4.getText().toString();
                StrWestSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrWestNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrEastNorthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrEastSouthDistance="0";
                StrWestSouthAccuracy=Accuracy1.getText().toString();
                StrWestNorthAccuracy=Accuracy2.getText().toString();
                StrEastNorthAccuracy=Accuracy3.getText().toString();
                StrEastSouthAccuracy=Accuracy4.getText().toString();
            }
            else
            {
                StrEastSouthLat=Lat1.getText().toString();
                StrEastSouthLng=Lng1.getText().toString();
                StrEastNorthLat=Lat2.getText().toString();
                StrEastNorthLng=Lng2.getText().toString();
                StrWestNorthLat=Lat3.getText().toString();
                StrWestNorthLng=Lng3.getText().toString();
                StrWestSouthLat=Lat4.getText().toString();
                StrWestSouthLng=Lng4.getText().toString();
                StrEastSouthDistance=decimalFormat.format(l1.distanceTo(l2));
                StrEastNorthDistance=decimalFormat.format(l2.distanceTo(l3));
                StrWestNorthDistance=decimalFormat.format(l3.distanceTo(l1));
                StrWestSouthDistance="0";
                StrEastSouthAccuracy=Accuracy1.getText().toString();
                StrEastNorthAccuracy=Accuracy2.getText().toString();
                StrWestNorthAccuracy=Accuracy3.getText().toString();
                StrWestSouthAccuracy=Accuracy4.getText().toString();
            }
        }
        double eastWestLength=(Double.parseDouble(StrEastNorthDistance)+ Double.parseDouble(StrWestSouthDistance))/2;
        double northSouthLength=(Double.parseDouble(StrWestNorthDistance)+ Double.parseDouble(StrEastSouthDistance))/2;
        /*Location l1=new Location("");
        l1.setLatitude(Double.parseDouble("26.89729784"));
        l1.setLongitude(Double.parseDouble("80.99182373"));
        locationList.add(l1);
        Location l2=new Location("");
        l2.setLatitude(Double.parseDouble("26.89729434"));
        l2.setLongitude(Double.parseDouble("80.99170835"));
        locationList.add(l2);
        Location l3=new Location("");
        l3.setLatitude(Double.parseDouble("26.89740148"));
        l3.setLongitude(Double.parseDouble("80.99174044"));
        locationList.add(l3);
        Location l4=new Location("");
        l4.setLatitude(Double.parseDouble("26.8973886"));
        l4.setLongitude(Double.parseDouble("80.99176205"));
        locationList.add(l4);*/
        double areaM=eastWestLength*northSouthLength;
        /*Double areaM=Double.parseDouble(Distance1.getText().toString())*Double.parseDouble(Distance2.getText().toString())*
            Double.parseDouble(Distance3.getText().toString())*Double.parseDouble(Distance4.getText().toString());*/
        AreaMeter.setText(decimalFormat.format(areaM));
        double ah= Double.parseDouble(decimalFormat.format(areaM))/10000;
        AreaHec.setText(""+ah);
        StrAreaMeter=decimalFormat.format(areaM);
        StrAreaHec=""+ah;
        currentDistance++;
        currentAccuracy=20000;
        CoordinateArea=decimalFormat.format(areaM);
        AreaMeter.setText("0");
        AreaMeter.setInputType(InputType.TYPE_NULL);
        AreaMeter.setTextIsSelectable(true);
        AreaMeter.setFocusable(false);
    }


    public void clearCoordinate(View v)
    {
        //findLocation();
        LastLat=0;
        LastLng=0;
        currentDistance=1;
        currentAccuracy=20000;
        Corner1.setText("");
        Corner2.setText("");
        Corner3.setText("");
        Corner4.setText("");
        Lat1.setText("");
        Lat2.setText("");
        Lat3.setText("");
        Lat4.setText("");
        Lng1.setText("");
        Lng2.setText("");
        Lng3.setText("");
        Lng4.setText("");
        Distance1.setText("");
        Distance2.setText("");
        Distance3.setText("");
        Distance4.setText("");
        Accuracy1.setText("");
        Accuracy2.setText("");
        Accuracy3.setText("");
        Accuracy4.setText("");
        AreaMeter.setText("");
        AreaHec.setText("");
        CoordinateArea="0";
        AreaMeter.setText("0");
        AreaMeter.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AreaMeter.setTextIsSelectable(true);
        AreaMeter.setFocusable(true);
        AreaHec.setText("");
        AreaHec.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AreaHec.setTextIsSelectable(true);
        AreaHec.setFocusable(true);
    }


    private void setLatDialogue(String lt, String ln, String acr)
    {
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        Lat= Double.parseDouble(lt);
        Lng= Double.parseDouble(ln);
        Location currentLocation=new Location("");
        currentLocation.setLatitude(Lat);
        currentLocation.setLongitude(Lng);
        Accuracy= Double.parseDouble(acr);
        dist=0;
        if(LastLat>0 && LastLng>0 )
        {
            Location l1=new Location("");
            l1.setLatitude(LastLat);
            l1.setLongitude(LastLng);
            dist=l1.distanceTo(currentLocation);
            //dist=getDistance(LastLat,LastLng,Lat,Lng);
        }
        if(location_lat!=null)
        {
            location_lat.setText("" + Lat);
            location_lng.setText("" + Lng);
            location_accuracy.setText(decimalFormat.format(Accuracy)+" M");
        }
        else
        {
            location_lat.setText("---");
            location_lng.setText("---");
            location_accuracy.setText("---");
        }
        msgLayout.setVisibility(View.GONE);
        btnLayout.setVisibility(View.VISIBLE);
    }

    /*private class getVillageData extends AsyncTask<String, Void, Void> {
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
                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/GETVILLAGE";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("VillCode",params[0]));
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
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    villageCode.setText(jsonObject.getString("VCode"));
                    villageName.setText(jsonObject.getString("VName"));
                }
                else
                {
                    AlertPopUp("Invalid village code");
                    villageCode.setText("");
                    villageName.setText("");
                }

            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }*/

    /*private class getPlotVillage extends AsyncTask<String, Void, Void> {
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
                String url=loginUserDetailsList.get(0).getFactoryUrl()+"/GETVILLAGE";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("VillCode",params[0]));
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
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    plotVillageCode.setText(jsonObject.getString("VCode"));
                    plotVillageName.setText(jsonObject.getString("VName"));
                }
                else
                {
                    AlertPopUp("Invalid village code");
                    plotVillageCode.setText("");
                    plotVillageName.setText("");
                }

            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }*/

    /*private class getGrowerData extends AsyncTask<String, Void, Void> {
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
                String url=loginUserDetailsList.get(0).getFactoryUrl()+"/GETGROWER";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("VillCode",params[0]));
                entity.add(new BasicNameValuePair("GCode",params[1]));
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
                JSONArray jsonArray=new JSONArray(Content);
                if(jsonArray.length()>0)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    growerCode.setText(jsonObject.getString("GCode"));
                    growerName.setText(jsonObject.getString("GName"));
                    growerFatherName.setText(jsonObject.getString("GFatherName"));
                }
                else
                {
                    AlertPopUp("Invalid grower code");
                    growerCode.setText("");
                    growerName.setText("");
                    growerFatherName.setText("");
                }

            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }*/

    /*private void searchData()
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
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat=location.getLatitude();
                lng=location.getLongitude();
                new verifyData().execute(villageCode.getText().toString(), growerCode.getText().toString(),
                        plotVillageCode.getText().toString(),
                        "" +lat , "" +lng );
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
    }*/

    /*private class verifyData extends AsyncTask<String, Void, Void> {
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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/CHECKPLOUGHINGPLOT";
                //String url = "http://192.168.225.60:81/android/trip_image_upload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("VillageCode",params[0]));
                entity.add(new BasicNameValuePair("GrowerCode",params[1]));
                entity.add(new BasicNameValuePair("PLOTVillCode",params[2]));
                entity.add(new BasicNameValuePair("LAT",params[3]));
                entity.add(new BasicNameValuePair("LON",params[4]));
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
                if(Content.equalsIgnoreCase("false"))
                {
                    checkPlot=true;
                    findLocation();
                    getCurrentLocation();
                }
                else
                {
                    checkPlot=false;
                    AlertPopUp("This plot already registered");
                }
            }
            catch (Exception e)
            {
                AlertPopUp("Error:"+e.toString());
            }
        }
    }*/


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

                String url=APIUrl.BASE_URL_CANE_DEVELOPMENT+"/SAVEPLOUGHING";
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
                //File sourceFile = new File(params[0]);
                /*entity.addPart("Village", new StringBody(params[0]));
                entity.addPart("Grower", new StringBody(params[1]));
                entity.addPart("PLOTVillage", new StringBody(params[2]));
                entity.addPart("Irrigationmode", new StringBody(params[3]));
                entity.addPart("SupplyMode", new StringBody(params[4]));
                entity.addPart("Harvesting", new StringBody(params[5]));
                entity.addPart("Equipment", new StringBody(params[6]));
                entity.addPart("LandType", new StringBody(params[7]));
                entity.addPart("SeedType", new StringBody(params[8]));
                entity.addPart("NOFPLOTS", new StringBody(params[9]));
                entity.addPart("INDAREA", new StringBody(params[10]));
                entity.addPart("InsertLAT", new StringBody(params[11]));
                entity.addPart("InsertLON", new StringBody(params[12]));
                entity.addPart("Dim1", new StringBody(StrEastNorthDistance));
                entity.addPart("Dim2", new StringBody(StrEastSouthDistance));
                entity.addPart("Dim3", new StringBody(StrWestSouthDistance));
                entity.addPart("Dim4", new StringBody(StrWestNorthDistance));
                entity.addPart("Area", new StringBody(params[10]));
                entity.addPart("LAT1", new StringBody(StrEastNorthLat));
                entity.addPart("LON1", new StringBody(StrEastNorthLng));
                entity.addPart("LAT2", new StringBody(StrEastSouthLat));
                entity.addPart("LON2", new StringBody(StrEastSouthLng));
                entity.addPart("LAT3", new StringBody(StrWestSouthLat));
                entity.addPart("LON3", new StringBody(StrWestSouthLng));
                entity.addPart("LAT4", new StringBody(StrWestNorthLat));
                entity.addPart("LON4", new StringBody(StrWestNorthLng));
                entity.addPart("SuperviserCode", new StringBody(loginUserDetailsList.get(0).getUserId()));
                entity.addPart("Image",new StringBody(imgFrmt));
                */

                entity.add(new BasicNameValuePair("FactId",loginUserDetailsList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("VillCode",params[0]));
                entity.add(new BasicNameValuePair("GrowCode",params[1]));
                entity.add(new BasicNameValuePair("PLVillCode",params[2]));
                entity.add(new BasicNameValuePair("DriverName",params[3]));
                if (TractorNumber.getText().toString().equalsIgnoreCase(""))
                {entity.add(new BasicNameValuePair("VechileNo","0"));}
                else {entity.add(new BasicNameValuePair("VechileNo",params[4]));}
                entity.add(new BasicNameValuePair("Discription",params[5]));
                entity.add(new BasicNameValuePair("MenulArea",params[6]));
                entity.add(new BasicNameValuePair("InsertLAT",params[7]));
                entity.add(new BasicNameValuePair("InsertLON",params[8]));
                entity.add(new BasicNameValuePair("MOBILNO",params[9]));
                entity.add(new BasicNameValuePair("PDATE",params[10]));
                entity.add(new BasicNameValuePair("SampleBefore",params[11]));
                entity.add(new BasicNameValuePair("SampleAfter",params[12]));
                entity.add(new BasicNameValuePair("Dim1",StrEastNorthDistance));
                entity.add(new BasicNameValuePair("Dim2",StrEastSouthDistance));
                entity.add(new BasicNameValuePair("Dim3",StrWestSouthDistance));
                entity.add(new BasicNameValuePair("Dim4",StrWestNorthDistance));
                entity.add(new BasicNameValuePair("Area",CoordinateArea));
                entity.add(new BasicNameValuePair("LAT1",StrEastNorthLat));
                entity.add(new BasicNameValuePair("LON1",StrEastNorthLng));
                entity.add(new BasicNameValuePair("LAT2",StrEastSouthLat));
                entity.add(new BasicNameValuePair("LON2",StrEastSouthLng));
                entity.add(new BasicNameValuePair("LAT3",StrWestSouthLat));
                entity.add(new BasicNameValuePair("LON3",StrWestSouthLng));
                entity.add(new BasicNameValuePair("LAT4",StrWestNorthLat));
                entity.add(new BasicNameValuePair("LON4",StrWestNorthLng));
                entity.add(new BasicNameValuePair("SupervisorCode",loginUserDetailsList.get(0).getCode()));
                entity.add(new BasicNameValuePair("INDSREALNO",indentModelList.get(input_indent_number.getSelectedItemPosition()-1).getPlotSerialNumber()));
                entity.add(new BasicNameValuePair("Image",imgFrmt));

                String debugData=new MiscleniousUtil().ListNameValueToString("SavePoughing",entity);
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
                AlertPopUp("Error: "+e.toString());
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
